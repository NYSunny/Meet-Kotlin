package com.johnny.meet_kotlin.rongcloud

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.johnny.base.utils.i
import com.johnny.meet_kotlin.rongcloud.bean.Token
import com.johnny.meet_kotlin.rongcloud.rxhttp.HttpWrapper
import com.rxjava.rxlife.lifeOnMain
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.message.TextMessage

/**
 * @author Johnny
 */
object RongCloudManager {

    const val APP_KEY = "x18ywvqfx522c"
    const val APP_SECRET = "eVwX4qYe85IGK"

    private val mServiceStatusObservers: MutableSet<ServiceStatusObserver> = mutableSetOf()
    private var mCurrentServiceStatus: ServiceStatus = ServiceStatus.UNCONNECTED

    enum class ServiceStatus {
        UNCONNECTED,
        CONNECTING,
        CONNECTED,
        PREPARED,
        PREPARE_FAIL
    }

    interface ServiceStatusObserver {
        fun onConnecting()
        fun onConnectSuccess()
        fun onConnectFail()
        fun onPrepared()
        fun onPrepareFail()
    }

    class DefaultServiceStatusObserver : ServiceStatusObserver {
        override fun onConnecting() {}
        override fun onConnectSuccess() {}
        override fun onConnectFail() {}
        override fun onPrepared() {}
        override fun onPrepareFail() {}
    }

    /**
     * init rong-cloud sdk
     */
    fun init(context: Context) = RongIMClient.init(context, APP_KEY)

    /**
     * 从融云服务器获取token
     */
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

    /**
     * 主进程调用
     * 根据token连接融云IM远程服务器
     */
    fun connect(token: String) {
        if (this.mCurrentServiceStatus == ServiceStatus.CONNECTED
            || this.mCurrentServiceStatus == ServiceStatus.CONNECTING
            || this.mCurrentServiceStatus == ServiceStatus.PREPARED
        ) return

        i(msg = "connecting... current thread = ${Thread.currentThread()}")
        // 设置融云SDK连接状态监听器
        RongIMClient.setConnectionStatusListener {
            when (it) {
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED -> {
                    i(msg = "ConnectionStatus = CONNECTED, current thread = ${Thread.currentThread()}")
                    mCurrentServiceStatus = ServiceStatus.CONNECTED
                    notifyObservers()
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING -> {
                    i(msg = "ConnectionStatus = CONNECTING, current thread = ${Thread.currentThread()}")
                    mCurrentServiceStatus = ServiceStatus.CONNECTING
                    notifyObservers()
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT -> {
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.SUSPEND -> {}
                else -> {
                    i(msg = "ConnectionStatus = ${it.name}, current thread = ${Thread.currentThread()}")
                    mCurrentServiceStatus = ServiceStatus.UNCONNECTED
                    notifyObservers()
                }
            }
        }

        // 连接融云服务器
        RongIMClient.connect(token, object : RongIMClient.ConnectCallback() {
            override fun onSuccess(result: String?) {
                i(msg = "connect success. result = ${result}. current thread = ${Thread.currentThread()}")
            }

            override fun onDatabaseOpened(code: RongIMClient.DatabaseOpenStatus?) {
                mCurrentServiceStatus =
                    if (code == RongIMClient.DatabaseOpenStatus.DATABASE_OPEN_SUCCESS) {
                        i(msg = "prepare success. current thread = ${Thread.currentThread()}")
                        ServiceStatus.PREPARED
                    } else {
                        i(msg = "prepare fail. current thread = ${Thread.currentThread()}")
                        ServiceStatus.PREPARE_FAIL
                    }
                notifyObservers()
            }

            override fun onError(e: RongIMClient.ConnectionErrorCode?) {
                i(msg = "connect fail. current thread = ${Thread.currentThread()}")
            }
        })
    }

    /**
     * 断开和融云的连接，有新消息时，仍然有推送通知
     */
    fun disconnect() {
        RongIMClient.getInstance().disconnect()
    }

    /**
     * 不想收到任何推送通知并断开连接
     * 切换账号也需调用该方法
     */
    fun logout() {
        RongIMClient.getInstance().logout()
    }

    fun setOnReceiveMessageListener(listener: RongIMClient.OnReceiveMessageWrapperListener) {
        RongIMClient.setOnReceiveMessageListener(listener)
    }

    @Synchronized
    private fun notifyObservers() {
        when (this.mCurrentServiceStatus) {
            ServiceStatus.UNCONNECTED -> this.mServiceStatusObservers.forEach { it.onConnectFail() }
            ServiceStatus.CONNECTING -> this.mServiceStatusObservers.forEach { it.onConnecting() }
            ServiceStatus.CONNECTED -> this.mServiceStatusObservers.forEach { it.onConnectSuccess() }
            ServiceStatus.PREPARED -> this.mServiceStatusObservers.forEach { it.onPrepared() }
            ServiceStatus.PREPARE_FAIL -> this.mServiceStatusObservers.forEach { it.onPrepareFail() }
        }
    }

    @Synchronized
    fun registerServiceStatusObserver(observer: ServiceStatusObserver) {
        this.mServiceStatusObservers.add(observer)
        when (this.mCurrentServiceStatus) {
            ServiceStatus.UNCONNECTED -> observer.onConnectFail()
            ServiceStatus.CONNECTING -> observer.onConnecting()
            ServiceStatus.CONNECTED -> observer.onConnectSuccess()
            ServiceStatus.PREPARED -> observer.onPrepared()
            ServiceStatus.PREPARE_FAIL -> observer.onPrepareFail()
        }
    }

    @Synchronized
    fun unregisterServiceStatusObserver(observer: ServiceStatusObserver) {
        this.mServiceStatusObservers.remove(observer)
    }

    @Synchronized
    fun releaseServiceStatusObservers() {
        this.mServiceStatusObservers.clear()
    }

    /**
     * 发送文本消息
     */
    fun sendTextMessage(
        text: String,
        targetId: String,
        conversationType: Conversation.ConversationType = Conversation.ConversationType.PRIVATE,
        callback: IRongCallback.ISendMessageCallback
    ) {
        val textMessage = TextMessage.obtain(text)
        RongIMClient.getInstance()
            .sendMessage(conversationType, targetId, textMessage, null, null, callback)
    }
}