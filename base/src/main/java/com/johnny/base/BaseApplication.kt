package com.johnny.base

import android.app.Application

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
    }

}