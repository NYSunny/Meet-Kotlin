package com.johnny.meet_kotlin.activities

import android.os.Bundle
import com.johnny.base.BaseUIActivity
import com.johnny.meet_kotlin.R

/**
 * @author Johnny
 */
class NewFriendActivity : BaseUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_friend)
    }

    override fun actionBarTitle(): String? = getString(R.string.text_new_friend)
}