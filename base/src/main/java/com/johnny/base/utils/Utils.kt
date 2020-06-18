package com.johnny.base.utils

import android.app.Application
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */

/**
 * 暂时用这种方式获得Application，作为依赖库以后可能会采用反射等方式
 */
fun getApp(): Application {
    return BaseApplication.getApplication()
}