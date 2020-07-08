package com.johnny.meet_kotlin.rongcloud.rxhttp

import com.johnny.base.utils.sha1
import com.johnny.meet_kotlin.rongcloud.RongCloudManager
import rxhttp.RxHttp
import rxhttp.RxHttpFormParam
import rxhttp.RxHttpNoBodyParam
import rxhttp.wrapper.annotation.DefaultDomain
import kotlin.math.floor

/**
 * @author Johnny
 */
object HttpWrapper {

    @DefaultDomain
    const val baseUrl = "https://api-cn.ronghub.com"

    fun get(url: String, vararg formatArgs: Any): RxHttpNoBodyParam {
        val timeStamp = System.currentTimeMillis().toString()
        val nonce = floor(Math.random() * 100000).toString()
        val signature = sha1(RongCloudManager.APP_SECRET + nonce + timeStamp)
        return RxHttp.get(url, formatArgs)
            .addHeader("Timestamp", timeStamp)
            .addHeader("App-Key", RongCloudManager.APP_KEY)
            .addHeader("Nonce", nonce)
            .addHeader("Signature", signature)
    }

    fun postForm(url: String, vararg formatArgs: Any): RxHttpFormParam {
        val timeStamp = System.currentTimeMillis().toString()
        val nonce = floor(Math.random() * 100000).toInt().toString()
        val signature = sha1(RongCloudManager.APP_SECRET + nonce + timeStamp)
        return RxHttp.postForm(url, formatArgs)
            .addHeader("Timestamp", timeStamp)
            .addHeader("App-Key", RongCloudManager.APP_KEY)
            .addHeader("Nonce", nonce)
            .addHeader("Signature", signature)
    }
}