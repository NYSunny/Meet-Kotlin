package com.johnny.base

import android.os.Handler
import android.os.Looper
import android.os.Message
import java.lang.ref.WeakReference

/**
 * 解决Handler中内存泄漏问题
 *
 * @author Johnny
 */
class WeakRefHandler(callback: Callback, looper: Looper = Looper.getMainLooper()) :
    Handler(looper) {

    private val mWeakRefCallback: WeakReference<Callback> = WeakReference<Callback>(callback)

    override fun handleMessage(msg: Message) {
        this.mWeakRefCallback.get()?.handleMessage(msg)
    }
}