package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.QueryListener
import com.johnny.base.WeakRefHandler
import com.johnny.base.utils.*
import com.johnny.meet_kotlin.MainActivity
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.SP_KEY_MOBILE_PHONE
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.bmob.User
import com.johnny.meet_kotlin.view.PictureCheckView
import kotlinx.android.synthetic.main.dialog_login_picture_check.*
import kotlinx.android.synthetic.main.fragment_sms_login.view.*

/**
 * 登录页
 * 输入手机号码，点击【发送】按钮，弹出图片验证码框，用户操作验证正确后，发送手机号请求验证码；
 *【发送】按钮变灰不可点击进入60s倒计时，倒计时完后按钮变蓝可点击，重置为【发送】；
 * 输入验证码，点击【登录】按钮登录，验证码正确即保存状态跳转主页面，否则Toast提示。
 *
 * @author Johnny
 */
class SMSLoginFragment : Fragment(), View.OnClickListener {

    private lateinit var mView: View

    companion object {
        const val TAG = "SMSLoginFragment"
        private var TOTAL_TIME = 60000 / 1000
        private const val COUNT_DOWN = 1001
        private const val INTERVAL = 1000L
    }

    @SuppressLint("SetTextI18n")
    private val handlerCallBack = Handler.Callback {
        when (it.what) {
            COUNT_DOWN -> {
                TOTAL_TIME--
                this.mView.btnSendAuthCode.text = "${TOTAL_TIME}s"
                if (TOTAL_TIME > 0) {
                    it.target.sendEmptyMessageDelayed(COUNT_DOWN, INTERVAL)
                } else {
                    this.mView.btnSendAuthCode.text = getString(R.string.text_send)
                    this.mView.btnSendAuthCode.isEnabled = true
                }
            }
        }
        false
    }

    private val handler = WeakRefHandler(handlerCallBack)

    private lateinit var loginDialog: AppCompatDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mView =
            LayoutInflater.from(context).inflate(R.layout.fragment_sms_login, container, false)
        setupView()
        return this.mView
    }

    private fun setupView() {
        this.mView.btnSendAuthCode.setOnClickListener(this)
        this.mView.btnLogin.setOnClickListener(this)
        this.mView.tvUseAccountPasswordLogin.setOnClickListener(this)

        val lastPhone = SpUtils.getString(SP_KEY_MOBILE_PHONE, "")
        if (lastPhone.isNotBlank()) this.mView.etMobilePhone.setText(lastPhone)
        else this.mView.etMobilePhone.setText("")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSendAuthCode -> picCheckAndSendAuthCode()
            R.id.btnLogin -> login()
            R.id.tvUseAccountPasswordLogin -> toAccountPasswordFragment()
        }
    }

    private fun toAccountPasswordFragment() {
        (context as? FragmentActivity)?.let {
            val ft = it.supportFragmentManager.beginTransaction()
            var fragment = it.supportFragmentManager.findFragmentByTag(AccountLoginFragment.TAG)
            ft.hide(this)
            if (fragment == null) {
                fragment = AccountLoginFragment()
                ft.add(R.id.container, fragment, AccountLoginFragment.TAG)
            } else {
                ft.show(fragment)
            }
            ft.commit()
        }
    }

    /**
     * 登录
     */
    private fun login() {
        val phone = this.mView.etMobilePhone.text.toString()
        if (phone.isBlank()) shortToast(R.string.text_mobile_phone_is_not_null)
        val smsCode = this.mView.etAuthCode.text.toString()
        if (smsCode.isBlank()) shortToast(R.string.text_sms_code_is_not_null)

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
        BmobManager.signOrLoginByMobilePhone(phone, smsCode, object : LogInListener<User>() {
            override fun done(user: User?, e: BmobException?) {
                this@SMSLoginFragment.loginDialog.dismiss()
                if (e == null) {
                    // 保存登录成功的手机号
                    SpUtils.saveString(SP_KEY_MOBILE_PHONE, phone)
                    // 启动主页面
                    context?.startActivity<MainActivity>()
                    // 关闭当前页面
                    (context as? Activity)?.finish()
                } else {
                    longToast("ERROR: ${e.message}")
                }
            }
        })
    }

    /**
     * 发送验证码
     */
    private fun picCheckAndSendAuthCode() {
        // 1.获取输入框中的手机号码
        val phone = this.mView.etMobilePhone.text.toString()
        if (phone.isBlank()) {
            shortToast(R.string.text_mobile_phone_is_not_null)
            return
        }

        // 3.发送验证码
        fun sendAuthCode() {
            BmobManager.requestSMSCode(phone, object : QueryListener<Int>() {
                override fun done(code: Int?, e: BmobException?) {
                    if (e == null) {
                        // 发送按钮不可点击变灰
                        mView.btnSendAuthCode.isEnabled = false
                        // 60s倒计时开始
                        handler.sendEmptyMessage(COUNT_DOWN)
                        shortToast(R.string.text_sms_auth_code_send_success)
                    } else {
                        shortToast(R.string.text_sms_auth_code_send_fail)
                    }
                }
            })
        }

        // 2.显示图片验证弹窗
        context?.let {
            showDialog(it, R.layout.dialog_login_picture_check) {
                pictureCheckView.onCheckListener = object : PictureCheckView.OnCheckListener {
                    override fun onCheckSuccess() {
                        sendAuthCode()
                        this@showDialog.dismiss()
                    }

                    override fun onCheckFail() {
                        shortToast(R.string.text_pic_auth_fail)
                    }
                }
            }
        }
    }
}