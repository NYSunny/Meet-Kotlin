package com.johnny.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnny.base.utils.fixNotchScreen
import com.johnny.base.utils.fixSystemUI

/**
 * @author Johnny
 */
open class BaseUIActivity : AppCompatActivity() {

    var fixSystemUIEnabled = false
    var statusBarColor = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixNotchScreen(this)
        if (fixSystemUIEnabled) fixSystemUI(this, statusBarColor)
    }
}