package com.johnny.meet_kotlin

import android.content.Context
import androidx.multidex.MultiDex
import com.johnny.base.BaseApplication
import com.johnny.meet_kotlin.bmob.BmobManager
import com.library.networklib.kit.NetKit

/**
 * @author Johnny
 */

class App : BaseApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        // init Bmob
        BmobManager.init()

        // init network lib
        NetKit.getInstance().init("http://www.baidu.com")
    }
}