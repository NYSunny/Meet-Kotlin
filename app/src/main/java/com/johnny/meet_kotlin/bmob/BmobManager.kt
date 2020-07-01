package com.johnny.meet_kotlin.bmob

import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.QueryListener
import com.johnny.base.utils.getApp

/**
 * @author Johnny
 */
object BmobManager {

    private const val APPLICATION_ID = "f35305bb497207769a0b024e35d903a3"

    fun init() {
        Bmob.initialize(
            getApp(),
            APPLICATION_ID
        )
    }

    /**
     * 请求短信验证码
     */
    fun requestSMSCode(phone: String, listener: QueryListener<Int>) {
        BmobSMS.requestSMSCode(phone, "", listener)
    }

    /**
     * 一键注册或登录
     */
    fun signOrLoginByMobilePhone(phone: String, smsCode: String, listener: LogInListener<IMUser>) {
        BmobUser.signOrLoginByMobilePhone(phone, smsCode, listener)
    }
}