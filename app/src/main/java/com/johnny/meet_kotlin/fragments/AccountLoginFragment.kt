package com.johnny.meet_kotlin.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import com.johnny.base.utils.*
import com.johnny.meet_kotlin.MainActivity
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.SP_KEY_ACCOUNT
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.bmob.User
import kotlinx.android.synthetic.main.fragment_account_login.*
import kotlinx.android.synthetic.main.fragment_account_login.view.*

/**
 * @author Johnny
 */
class AccountLoginFragment : Fragment(), View.OnClickListener {

    private lateinit var mView: View

    private lateinit var loginDialog: AppCompatDialog

    companion object {
        const val TAG = "AccountLoginFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mView =
            LayoutInflater.from(context).inflate(R.layout.fragment_account_login, container, false)
        setupView()
        return this.mView
    }

    private fun setupView() {
        this.mView.tvUseSMSLogin.setOnClickListener(this)
        this.mView.btnLogin.setOnClickListener(this)

        val cacheAccount = SpUtils.getString(SP_KEY_ACCOUNT, "")
        if (cacheAccount.isNotBlank()) this.mView.etAccount.setText(cacheAccount)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvUseSMSLogin -> toSMSLoginFragment()
            R.id.btnLogin -> loginByAccount()
        }
    }

    private fun loginByAccount() {
        val account = etAccount.text.toString().trim()
        if (account.isBlank()) shortToast(R.string.text_account_is_not_null)
        val password = etPassword.text.toString().trim()
        if (password.isBlank()) shortToast(R.string.text_password_is_not_null)

        (context as? Activity)?.let {
            hideSoftInput(it)
        }

        if (!this::loginDialog.isInitialized) {
            context?.let {
                this.loginDialog = showLoadingDialog(it, false, getString(R.string.text_logining))
            }
        } else if (!this.loginDialog.isShowing) {
            this.loginDialog.show()
        }

        BmobManager.loginByAccount(account, password, object : LogInListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                loginDialog.dismiss()
                if (e == null) {
                    SpUtils.saveString(SP_KEY_ACCOUNT, account)
                    context?.startActivity<MainActivity>()
                    (context as? Activity)?.finish()
                } else {
                    longToast("ERROR: ${e.message}")
                }
            }
        })
    }

    private fun toSMSLoginFragment() {
        (context as? FragmentActivity)?.let {
            val fm = it.supportFragmentManager
            val ft = fm.beginTransaction()
            var fragment = fm.findFragmentByTag(SMSLoginFragment.TAG)
            ft.hide(this)
            if (fragment == null) {
                fragment = SMSLoginFragment()
                ft.add(R.id.container, fragment, SMSLoginFragment.TAG)
            } else {
                ft.show(fragment)
            }
            ft.commit()
        }
    }
}