package com.johnny.meet_kotlin

import com.johnny.base.BaseApplication
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.rongcloud.RongCloudManager
import com.library.networklib.kit.NetKit
import rxhttp.RxHttp

/**
 * @author Johnny
 */

class App : BaseApplication() {

    override fun onMainProcessInit() {
        // init Bmob
        BmobManager.init()

        // init rong-cloud
        RongCloudManager.init(this)

        // init network lib
        NetKit.getInstance().init("https://api-cn.ronghub.com")

        RxHttp.setDebug(true)
    }
}