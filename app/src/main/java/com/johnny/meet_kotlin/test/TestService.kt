package com.johnny.meet_kotlin.test

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.johnny.base.utils.i

/**
 * @author Johnny
 */
class TestService : Service() {

    override fun onCreate() {
        i(msg = "TestService onCreate start")
        try {
            Thread.sleep(20 * 1000)
        } catch (_: InterruptedException) {
            // ignore
        }
        i(msg = "TestService onCreate End")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        i(msg = "TestService onDestroy")
    }
}