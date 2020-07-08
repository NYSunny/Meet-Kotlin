package com.johnny.meet_kotlin.bmob

import cn.bmob.v3.BmobUser

/**
 * @author Johnny
 */
data class User(
    /* Token属性 */
    var tokenPhoto: String?,
    var tokenNickName: String?,
    /* 基本属性 */
    var nickName: String?,
    var photo: String?,
    var sex: Boolean = true,
    var desc: String?,
    var age: Int = 0,
    var birthday: String?,
    var constellation: String?,
    var hobby: String?,
    var status: String?
) : BmobUser()