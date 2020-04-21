package com.johnny.meet_kotlin.activity

import android.os.Bundle
import com.johnny.base.BaseUIActivity
import com.johnny.meet_kotlin.R
import kotlinx.android.synthetic.main.activity_login.*
import com.johnny.base.utils.showDialog
import com.johnny.base.utils.showLoadingDialog

/**
 * 登录页
 *
 * @author Johnny
 */
class LoginActivity : BaseUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        fixSystemUIEnabled = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnTest.setOnClickListener {
//            showDialog(
//                this,
//                R.layout.dialog_login_picture_check
//            )
            showLoadingDialog(this, true, "加载中...")
        }
    }
}