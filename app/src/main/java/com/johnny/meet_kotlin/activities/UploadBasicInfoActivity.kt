package com.johnny.meet_kotlin.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import com.johnny.base.BaseUIActivity
import com.johnny.base.helper.FileHelper
import com.johnny.base.utils.*
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.bmob.BmobManager
import kotlinx.android.synthetic.main.activity_upload_basic_info.*
import kotlinx.android.synthetic.main.dialog_select_photo_or_take_photo.*
import java.io.File

/**
 * @author Johnny
 */
class UploadBasicInfoActivity : BaseUIActivity(), View.OnClickListener {

    private var uploadPhotoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_basic_info)

        setupView()
        setupClickEvent()
    }

    private fun setupView() {
        etNickName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnDone.isEnabled = s != null && s.isNotBlank() && uploadPhotoFile != null
            }
        })
    }

    private fun setupClickEvent() {
        btnDone.setOnClickListener(this)
        ivLayerMask.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnDone -> done()
            R.id.ivLayerMask -> selectGetPhotoWay()
        }
    }

    private fun selectGetPhotoWay() = showDialog(
        this,
        R.layout.dialog_select_photo_or_take_photo,
        false,
        Gravity.BOTTOM,
        R.style.BottomInBottomOutAnimationDialogTheme
    ) {
        // 取消
        tvCancel.setOnClickListener {
            dismiss()
        }
        // 拍照
        tvTakePhoto.setOnClickListener {
            dismiss()
            // TODO:权限请求流程需要完善，这块还要加上权限引导，统一封装
            PermissionUtils.permissions(CAMERA, STORAGE)
                .resultCallback(object : OnPermissionCallback {
                    override fun onGrantedCallback(grantedPermissions: List<String>) {
                        FileHelper.toCamera(this@UploadBasicInfoActivity)
                    }

                    override fun onDeniedCallback(
                        grantedPermissions: List<String>,
                        deniedPermissions: List<String>,
                        foreverDeniedPermissions: List<String>
                    ) {
                    }
                }).request()
        }
        // 相册
        tvSelectFromAlbum.setOnClickListener {
            dismiss()
            FileHelper.toAlbum(this@UploadBasicInfoActivity)
        }
    }

    private fun done() {
        val nickName = etNickName.text.toString().trim()
        this.uploadPhotoFile?.let {
            val dialog =
                showLoadingDialog(this, false, getString(R.string.text_uploading_personal_info))
            BmobManager.uploadPersonalInfo(nickName, it) { isDone ->
                dialog.dismiss()
                if (isDone) {
                    finish()
                }
            }
        }
    }

    /**
     * 1.跳转相机拍照然后跳转裁剪页面进行裁剪
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                // 从相机返回
                FileHelper.REQUEST_CODE_TO_CAMERA -> FileHelper.takePhotoTemFile?.let {
                    FileHelper.startPhotoCrop(
                        this,
                        it
                    )
                }
                // 从裁剪程序返回
                FileHelper.REQUEST_CODE_PHOTO_CROP -> FileHelper.cropTemPath?.let {
                    this.uploadPhotoFile = File(it)
                }
                // 从相册返回
                FileHelper.REQUEST_CODE_TO_ALBUM -> {
                    val uri: Uri? = data?.data
                    var path: String? = null
                    uri?.let {
                        path = FileHelper.getRealPathFromUri(this, it)
                    }
                    if (!path.isNullOrBlank()) {
                        this.uploadPhotoFile = File(path!!)
                        FileHelper.startPhotoCrop(this, this.uploadPhotoFile!!)
                        super.onActivityResult(requestCode, resultCode, data)
                        return
                    }
                }
            }
            this.uploadPhotoFile?.let {
                val bitmap = BitmapFactory.decodeFile(it.path)
                ivPhoto.setImageBitmap(bitmap)
                btnDone.isEnabled = etNickName.text.trim().isNotBlank()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun actionBarTitle(): String? = resources.getString(R.string.text_write_personal_info)
}