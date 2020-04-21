package com.johnny.base.utils

import android.app.Activity
import android.content.Intent

/**
 * @author Johnny
 */

inline fun <reified T : Activity> Activity.startActivity(noinline extrasCallback: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, T::class.java)
    extrasCallback?.let { intent.it() }
    startActivity(intent)
}