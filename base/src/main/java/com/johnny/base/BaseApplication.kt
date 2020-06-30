package com.johnny.base

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.johnny.base.bmob.BmobManager
import com.library.networklib.kit.NetKit

/**
 * @author Johnny
 */
open class BaseApplication : Application() {

    companion object {
        private lateinit var mAppApplication: Application

        fun getApplication(): Application = this.mAppApplication
    }

    override fun onCreate() {
        super.onCreate()
        mAppApplication = this

        // init Bmob
        BmobManager.init()

        // init network lib
        NetKit.getInstance().init("http://www.baidu.com")
    }

}