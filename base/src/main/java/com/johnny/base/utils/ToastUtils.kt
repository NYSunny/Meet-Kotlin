package com.johnny.base.utils

import android.widget.Toast
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */

fun longToast(text: String) {
    Toast.makeText(getApp(), text, Toast.LENGTH_LONG).show()
}

fun longToast(res: Int) {
    Toast.makeText(getApp(), res, Toast.LENGTH_LONG).show()
}

fun shortToast(text: String) {
    Toast.makeText(getApp(), text, Toast.LENGTH_SHORT).show()
}

fun shortToast(res: Int) {
    Toast.makeText(getApp(), res, Toast.LENGTH_SHORT).show()
}