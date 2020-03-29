package com.johnny.base

import android.app.Application

/**
 * @author Johnny
 */
open class BaseApplication : Application() {

    companion object {
        private var mAppApplication: Application? = null

        fun getApplication(): Application = this.mAppApplication!!
    }

    override fun onCreate() {
        super.onCreate()
        mAppApplication = this
    }

}