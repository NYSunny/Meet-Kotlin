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

    private fun <T> setup(block: (timeStamp: String, nonce: String, signature: String) -> T): T {
        val timeStamp = System.currentTimeMillis().toString()
        val nonce = floor(Math.random() * 100000).toInt().toString()
        val signature = sha1(RongCloudManager.APP_SECRET + nonce + timeStamp)
        return block(timeStamp, nonce, signature)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : RxHttp<*, *>> setupCommonHeaders(http: T): T {
        val timeStamp = System.currentTimeMillis().toString()
        val nonce = floor(Math.random() * 100000).toInt().toString()
        val signature = sha1(RongCloudManager.APP_SECRET + nonce + timeStamp)

        return http.addHeader("Timestamp", timeStamp)
            .addHeader("App-Key", RongCloudManager.APP_KEY)
            .addHeader("Nonce", nonce)
            .addHeader("Signature", signature) as T
    }

    fun get(url: String, vararg formatArgs: Any): RxHttpNoBodyParam =
        setupCommonHeaders(RxHttp.get(url, formatArgs))

    fun postForm(url: String, vararg formatArgs: Any): RxHttpFormParam =
        setupCommonHeaders(RxHttp.postForm(url, formatArgs))
}