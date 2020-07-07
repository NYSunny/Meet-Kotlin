package com.johnny.meet_kotlin.activities

import android.os.Bundle
import com.johnny.base.BaseUIActivity
import com.johnny.meet_kotlin.R

/**
 * @author Johnny
 */
class UserInfoActivity : BaseUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
    }
}