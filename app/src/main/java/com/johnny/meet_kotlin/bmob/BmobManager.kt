package com.johnny.meet_kotlin.bmob

import cn.bmob.v3.Bmob
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.BmobUser
import cn.bmob.v3.datatype.BmobFile
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.*
import com.johnny.base.utils.getApp
import com.johnny.base.utils.shortToast
import java.io.File

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

    fun getCurrentUser(): IMUser? = BmobUser.getCurrentUser(IMUser::class.java)

    fun isLogin(): Boolean = BmobUser.isLogin()

    /**
     * 根据电话号查询用户
     */
    fun queryUserByPhone(phone: String, listener: FindListener<IMUser>) {
        queryWhereEqualTo("mobilePhoneNumber", phone, listener)
    }

    /**
     * 查询所有用户
     */
    fun queryAllUser(listener: FindListener<IMUser>) {
        val query = BmobQuery<IMUser>()
        query.findObjects(listener)
    }

    /**
     * 根据objectid查询用户
     */
    fun queryUserByObjectId(objectId: String, listener: FindListener<IMUser>) {
        queryWhereEqualTo("objectId", objectId, listener)
    }

    private fun <T> queryWhereEqualTo(key: String, value: Any, listener: FindListener<T>) {
        val query = BmobQuery<T>()
        query.addWhereEqualTo(key, value)
        query.findObjects(listener)
    }

    /**
     * 上传个人头像和昵称
     */
    fun uploadPersonalInfo(nickName: String, file: File, callback: (Boolean) -> Unit) {
        val bmobFile = BmobFile(file)
        bmobFile.uploadblock(object : UploadFileListener() {
            override fun done(e: BmobException?) {
                e?.let {
                    callback(false)
                    shortToast(it.message ?: "Unknown")
                } ?: Unit.apply {
                    val currentUser = getCurrentUser()
                    currentUser?.let {
                        currentUser.nickName = nickName
                        currentUser.photo = bmobFile.fileUrl

                        currentUser.tokenNickName = nickName
                        currentUser.tokenPhoto = bmobFile.fileUrl

                        it.update(object : UpdateListener() {
                            override fun done(exception: BmobException?) {
                                exception?.let { innerException ->
                                    callback(false)
                                    shortToast(innerException.message ?: "Unknown")
                                } ?: callback(true)
                            }
                        })
                    }
                }
            }
        })
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