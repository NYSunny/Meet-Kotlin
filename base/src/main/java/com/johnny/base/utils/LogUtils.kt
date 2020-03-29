package com.johnny.base.utils

import android.annotation.SuppressLint
import android.os.Environment
import android.util.Log
import com.johnny.base.BaseApplication
import com.johnny.base.BuildConfig
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.lang.Exception
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Johnny
 */

@SuppressLint("SimpleDateFormat")
private val TIME_FORMAT = SimpleDateFormat("yyyy:MM:dd HH:mm:ss")

fun i(msg: String, isWrite: Boolean = false) {
    when {
        BuildConfig.LOG_DEBUG && msg.isNotBlank() -> {
            Log.i(BuildConfig.LOG_TAG, msg)
            if (isWrite) {
                write2File(msg)
            }
        }
    }
}

fun e(msg: String, isWrite: Boolean = false) {
    when {
        BuildConfig.LOG_DEBUG && msg.isNotBlank() -> {
            Log.e(BuildConfig.LOG_TAG, msg)
            if (isWrite) {
                write2File(msg)
            }
        }
    }
}

/**
 * 记录日志到文件
 */
private fun write2File(msg: String) {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        // 获取文件保存路径/storage/emulated/0/Android/data/com.johnny.meet_kotlin/files/log
        var absolutePath = BaseApplication.getApplication().getExternalFilesDir("log")?.absolutePath
        absolutePath = absolutePath ?: "/sdcard/Meet"
        val fileGroup = File(absolutePath)
        if (!fileGroup.exists()) {
            fileGroup.mkdirs()
        }
        val content = TIME_FORMAT.format(Date()).plus(" ").plus(msg).plus("\n")
        val fileOutputStream: FileOutputStream
        var bufferedWriter: BufferedWriter? = null
        i(absolutePath)
        try {
            fileOutputStream = FileOutputStream(absolutePath.plus("/meet.log"), true)
            bufferedWriter =
                BufferedWriter(OutputStreamWriter(fileOutputStream, Charset.forName("GBK")))
            bufferedWriter.write(content)
            bufferedWriter.close()
        } catch (e: Exception) {
            // ignore
        } finally {
            bufferedWriter?.close()
        }
    }
}




