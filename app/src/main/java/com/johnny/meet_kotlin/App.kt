package com.johnny.meet_kotlin

import android.content.Context
import androidx.multidex.MultiDex
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */

class App : BaseApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}