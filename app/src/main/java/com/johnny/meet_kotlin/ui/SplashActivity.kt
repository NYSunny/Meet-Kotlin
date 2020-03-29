package com.johnny.meet_kotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.johnny.meet_kotlin.R

/**
 * 闪屏页
 * 功能点：
 * 1.闪屏页全屏
 * 2.适配刘海屏、水滴屏
 * 3.延时1秒根据逻辑判断跳转引导页、登录页还是主页
 *
 * @author Johnny
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }
}