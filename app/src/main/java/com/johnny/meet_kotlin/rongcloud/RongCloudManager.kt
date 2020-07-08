package com.johnny.meet_kotlin.rongcloud

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.johnny.base.utils.i
import com.johnny.meet_kotlin.rongcloud.bean.Token
import com.johnny.meet_kotlin.rongcloud.rxhttp.HttpWrapper
import com.rxjava.rxlife.lifeOnMain
import io.rong.imlib.RongIMClient

/**
 * @author Johnny
 */
object RongCloudManager {

    const val APP_KEY = "x18ywvqfx522c"
    const val APP_SECRET = "eVwX4qYe85IGK"

    /**
     * init rong-cloud sdk
     */
    fun init(context: Context) = RongIMClient.init(context, APP_KEY)

    fun getToken(
        owner: LifecycleOwner,
        formData: Map<String, String>,
        block: (Token?, Throwable?) -> Unit
    ) {
        HttpWrapper.postForm("/user/getToken.json")
            .addAll(formData)
            .asClass(Token::class.java)
            .lifeOnMain(owner)
            .subscribe({
                block(it, null)
            }, {
                block(null, it)
            })
    }
}