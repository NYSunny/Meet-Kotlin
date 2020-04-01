package com.johnny.base.manager

import cn.bmob.v3.Bmob
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */
object BmobManager {

    private const val APPLICATION_ID = "f35305bb497207769a0b024e35d903a3"

    fun init() {
        Bmob.initialize(BaseApplication.getApplication(), APPLICATION_ID);
    }
}