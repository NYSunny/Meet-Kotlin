package com.johnny.meet_kotlin.activities

import android.os.Bundle
import com.johnny.base.BaseUIActivity
import com.johnny.meet_kotlin.R

/**
 * @author Johnny
 */
class SettingActivity:BaseUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
    }

    override fun actionBarTitle(): String? = getString(R.string.text_setting)
}