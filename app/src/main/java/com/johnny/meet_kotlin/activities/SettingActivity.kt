package com.johnny.meet_kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.johnny.base.BaseUIActivity
import com.johnny.base.utils.SpUtils
import com.johnny.base.utils.showLoadingDialog
import com.johnny.base.utils.startActivity
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.SP_KEY_TOKEN
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.rongcloud.RongCloudManager
import com.johnny.meet_kotlin.test.TestActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * @author Johnny
 */
class SettingActivity:BaseUIActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setupView()
    }

    private fun setupView() {
        btnLogOut.setOnClickListener(this)
        btnTest.setOnClickListener(this)
    }

    override fun actionBarTitle(): String? = getString(R.string.text_setting)

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogOut -> logOut()
            R.id.btnTest -> toTest()
        }
    }

    private fun toTest() {
        startActivity<TestActivity>()
    }

    private fun logOut() {
        val dialog = showLoadingDialog(this, false, getString(R.string.text_exiting))
        BmobManager.logout()
        SpUtils.saveString(SP_KEY_TOKEN, "")
        RongCloudManager.logout()
        dialog.dismiss()
        startActivity<LoginActivity> {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        finish()
    }
}