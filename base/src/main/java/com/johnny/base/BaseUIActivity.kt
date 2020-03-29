package com.johnny.base

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnny.base.utils.fixSystemUI
import com.johnny.base.utils.setTranslucentStatusBar

/**
 * @author Johnny
 */
open class BaseUIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixSystemUI(this, Color.BLUE)
//        setTranslucentStatusBar(this)
    }
}