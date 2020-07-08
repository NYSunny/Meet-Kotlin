package com.johnny.meet_kotlin

import com.johnny.base.BaseApplication
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.rongcloud.RongCloudManager
import com.library.networklib.kit.NetKit

/**
 * @author Johnny
 */

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        // init Bmob
        BmobManager.init()

        // init rong-cloud
        RongCloudManager.init(this)

        // init network lib
        NetKit.getInstance().init("https://api-cn.ronghub.com")
    }
}