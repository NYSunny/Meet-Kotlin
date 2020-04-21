package com.johnny.base.utils

import android.widget.Toast
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */

fun longToast(text: String) {
    Toast.makeText(BaseApplication.getApplication(), text, Toast.LENGTH_LONG).show()
}

fun shortToast(text: String) {
    Toast.makeText(BaseApplication.getApplication(), text, Toast.LENGTH_SHORT).show()
}