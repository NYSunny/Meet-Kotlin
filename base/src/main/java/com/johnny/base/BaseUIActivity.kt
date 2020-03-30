package com.johnny.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnny.base.utils.fixNotchScreen

/**
 * @author Johnny
 */
open class BaseUIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fixNotchScreen(this)
    }
}