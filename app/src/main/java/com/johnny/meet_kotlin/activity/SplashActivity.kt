package com.johnny.meet_kotlin.activity

import android.os.Bundle
import android.os.Handler
import com.johnny.base.BaseUIActivity
import com.johnny.base.SP_KEY_IS_FIRST_START_APP
import com.johnny.base.SP_KEY_TOKEN
import com.johnny.base.WeakRefHandler
import com.johnny.base.utils.SpUtils
import com.johnny.base.utils.startActivity
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.test.TestActivity

/**
 * 闪屏页
 * 功能点：
 * 1.闪屏页全屏
 * 2.适配刘海屏、水滴屏
 * 3.延时1秒根据逻辑判断跳转引导页、登录页还是主页
 *
 * @author Johnny
 */
class SplashActivity : BaseUIActivity() {

    companion object {
        private const val START = 2000
        private const val DELAY = 1000L
    }

    private val mHandlerCallback = Handler.Callback { msg ->
        when (msg.what) {
            START -> start()
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 延迟1秒
        WeakRefHandler(this.mHandlerCallback).sendEmptyMessageDelayed(
            START,
            DELAY
        )
    }

    private fun start() {
        val isFirstStartApp = SpUtils.getBoolean(SP_KEY_IS_FIRST_START_APP, true)
        when {
            isFirstStartApp -> /*第一次启动App，先进入引导页 */SpUtils.saveBoolean(
                SP_KEY_IS_FIRST_START_APP,
                false
            ).apply {
                // 进入引导页
                startActivity<GuideActivity>()
            }
            SpUtils.getString(SP_KEY_TOKEN, "").isNullOrBlank() -> startActivity<TestActivity>()
            else -> startActivity<MainActivity>()
        }
        finish()
    }
}