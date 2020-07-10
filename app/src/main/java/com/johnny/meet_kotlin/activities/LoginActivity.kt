package com.johnny.meet_kotlin.activities

import android.os.Bundle
import com.johnny.base.BaseUIActivity
import com.johnny.base.utils.setLightStatusBarIcon
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.fragments.SMSLoginFragment

/**
 *
 *
 *
 *
 *
 * @author Johnny
 */
class LoginActivity : BaseUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        fixSystemUIEnabled = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setLightStatusBarIcon(this, true)
        toSMSLoginFragment()
    }

    private fun toSMSLoginFragment() {
        val ft = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(SMSLoginFragment.TAG)
        if (fragment == null) {
            fragment = SMSLoginFragment()
            ft.add(R.id.container, fragment, SMSLoginFragment.TAG)
        } else {
            ft.show(fragment)
        }
        ft.commit()
    }
}