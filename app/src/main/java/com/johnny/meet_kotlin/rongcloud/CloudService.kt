package com.johnny.meet_kotlin.rongcloud

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.johnny.base.utils.SpUtils
import com.johnny.base.utils.i
import com.johnny.meet_kotlin.SP_KEY_TOKEN
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Message

/**
 * @author Johnny
 */
class CloudService : Service() {

    override fun onCreate() {
        super.onCreate()

        linkToRemoteCloudService()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun linkToRemoteCloudService() {
        val token = SpUtils.getString(SP_KEY_TOKEN, "")
        // 连接融云IM远程服务
        RongCloudManager.connect(token)
        RongCloudManager.setOnReceiveMessageListener(object :
            RongIMClient.OnReceiveMessageWrapperListener() {
            override fun onReceived(
                message: Message?,
                left: Int,
                hasPackage: Boolean,
                offline: Boolean
            ): Boolean {
                parseIMMessage(message)
                return false
            }
        })
    }

    private fun parseIMMessage(message: Message?) {
        i(msg = "message = ${message?.toString() ?: ""}")
    }
}