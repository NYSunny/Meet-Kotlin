package com.johnny.meet_kotlin.activities

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import com.johnny.base.BaseUIActivity
import com.johnny.base.helper.FileHelper
import com.johnny.base.utils.CAMERA
import com.johnny.base.utils.OnPermissionCallback
import com.johnny.base.utils.PermissionUtils
import com.johnny.base.utils.STORAGE
import com.johnny.meet_kotlin.R
import kotlinx.android.synthetic.main.activity_upload_basic_info.*
import kotlinx.android.synthetic.main.dialog_select_photo_or_take_photo.*
import java.io.File
import java.util.jar.Manifest

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
            R.id.ivLayerMask -> selectWay()
        }
    }

    private fun selectWay() {
        com.johnny.base.utils.showDialog(
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
                            TODO("Not yet implemented")
                        }
                    }).request()
            }
            // 相册
            tvSelectFromAlbum.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun done() {

    }

    /**
     * 1.跳转相机拍照然后跳转裁剪页面进行裁剪
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                FileHelper.REQUEST_CODE_TO_CAMERA -> FileHelper.takePhotoTemFile?.let {
                    FileHelper.startPhotoCrop(
                        this,
                        it
                    )
                }
                FileHelper.REQUEST_CODE_PHOTO_CROP -> {
                    FileHelper.cropTemPath?.let {
                        this.uploadPhotoFile = File(it)
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