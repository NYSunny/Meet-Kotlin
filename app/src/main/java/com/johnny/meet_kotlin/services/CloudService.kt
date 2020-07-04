package com.johnny.meet_kotlin.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author Johnny
 */
class CloudService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}