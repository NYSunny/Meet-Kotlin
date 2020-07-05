package com.johnny.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * @author Johnny
 */
open class BaseApplication : Application() {

    companion object {
        private lateinit var mAppApplication: Application

        fun getApplication(): Application = this.mAppApplication
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mAppApplication = this
    }

}