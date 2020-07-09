package com.johnny.base

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.multidex.MultiDex

/**
 * @author Johnny
 */
abstract class BaseApplication : Application() {

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

        initInMainProcess()
    }

    abstract fun onMainProcessInit()

    private fun initInMainProcess() {
        val pid = Process.myPid()
        val am: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var currentProcessName: String? = null
        for (runningAppProcessInfo in am.runningAppProcesses) {
            if (pid == runningAppProcessInfo.pid) currentProcessName =
                runningAppProcessInfo.processName
        }

        if (applicationInfo.packageName == currentProcessName) onMainProcessInit()
    }
}