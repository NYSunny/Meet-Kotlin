package com.johnny.base.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.johnny.base.BuildConfig
import com.johnny.base.utils.shortToast
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Johnny
 */
object FileHelper {

    const val REQUEST_CODE_TO_CAMERA = 1000
    const val REQUEST_CODE_PHOTO_CROP = 1001
    private const val FILE_PROVIDER_AUTH = "${BuildConfig.APPLICATION_ID}.provider"
    private const val JPGDir = "JPG/"

    @SuppressLint("SimpleDateFormat")
    private val dataFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss")
    private val externalStoreRootDir: String = Environment.getExternalStorageDirectory().path

    var takePhotoTemFile: File? = null
    var cropTemPath: String? = null

    /**
     * 跳转相机拍照
     * 1.兼容android 7.0及以上
     */
    fun toCamera(activity: Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileName = "${this.dataFormat.format(Date())}.jpg"
        takePhotoTemFile = File(externalStoreRootDir, fileName)
        val imageUri: Uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(takePhotoTemFile)
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION.or(Intent.FLAG_GRANT_WRITE_URI_PERMISSION))
            FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTH, takePhotoTemFile!!)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_TO_CAMERA)
        } else {
            shortToast("未打开相机，请检查相机功能")
        }
    }

    fun startPhotoCrop(activity: Activity, file: File) {
        val intent = Intent("com.android.camera.action.CROP")
        val uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(activity, FILE_PROVIDER_AUTH, file)
        }
        intent.setDataAndType(uri, "image/*")
        //设置裁剪
        intent.putExtra("crop", "true")
        //裁剪宽高比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        //裁剪图片的质量
        intent.putExtra("outputX", 320)
        intent.putExtra("outputY", 320)
        //发送数据
        //intent.putExtra("return-data", true);
        this.cropTemPath = "${Environment.getExternalStorageDirectory().path}/meet.jpg"
        val cropUri = Uri.parse("file:///$cropTemPath")
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        if (intent.resolveActivity(activity.packageManager) != null) {
            activity.startActivityForResult(intent, REQUEST_CODE_PHOTO_CROP)
        } else {
            shortToast("当前系统中没有裁剪程序")
        }
    }
}