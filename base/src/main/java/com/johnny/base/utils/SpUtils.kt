package com.johnny.base.utils

import android.content.Context
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */

object SpUtils {
    private val SP =
        BaseApplication.getApplication().getSharedPreferences("SP", Context.MODE_PRIVATE)
    private val EDITOR = SP.edit()

    fun saveString(key: String, value: String) {
        EDITOR.putString(key, value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        EDITOR.putBoolean(key, value).apply()
    }

    fun saveInt(key: String, value: Int) {
        EDITOR.putInt(key, value).apply()
    }

    fun getString(key: String, defValue: String) = SP.getString(key, defValue)

    fun getBoolean(key: String, defValue: Boolean) = SP.getBoolean(key, defValue)

    fun getInt(key: String, defValue: Int) = SP.getInt(key, defValue)
}