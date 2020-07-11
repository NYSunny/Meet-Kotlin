package com.johnny.meet_kotlin.test

import android.os.Bundle
import android.view.View
import com.johnny.base.BaseUIActivity
import com.johnny.base.utils.shortToast
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.rongcloud.RongCloudManager
import io.rong.imlib.IRongCallback
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Message
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @author Johnny
 */
class TestActivity : BaseUIActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setupView()
    }

    private fun setupView() {
        this.btnSend.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSend -> sendMessage()
        }
    }

    private fun sendMessage() {
        val messageContent = etInput.text.toString().trim()
        if (messageContent.isBlank()) {
            shortToast("输入内容不能为空")
            return
        }
        val targetId = etTargetId.text.toString().trim()
        if (targetId.isBlank()) {
            shortToast("targetId不能为空")
            return
        }

        RongCloudManager.sendTextMessage(messageContent, targetId, callback = object :
            IRongCallback.ISendMessageCallback {
            override fun onAttached(p0: Message?) {

            }

            override fun onSuccess(p0: Message?) {
                tvResult.text = "发送成功"
            }

            override fun onError(p0: Message?, e: RongIMClient.ErrorCode?) {
                tvResult.text = "发送失败。ERROR = ${e?.message}"
            }
        })
    }
}