package com.johnny.meet_kotlin.ui

import android.os.Bundle
import com.johnny.base.BaseUIActivity
import com.johnny.meet_kotlin.R

/**
 * 登录页
 *
 * @author Johnny
 */
class LoginActivity : BaseUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}