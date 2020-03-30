package com.johnny.base.utils

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import com.johnny.base.BaseApplication

/**
 * @author Johnny
 */

private const val DEFAULT_STATUS_BAR_COLOR = Color.TRANSPARENT

/**
 * 沉浸式状态栏适配(API 19以上)
 */
fun fixSystemUI(activity: Activity, @ColorInt statusBarColor: Int = DEFAULT_STATUS_BAR_COLOR) {
    // 适配android 5.0
    val window = activity.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        var systemUIFlags = activity.window.decorView.systemUiVisibility
        systemUIFlags =
            systemUIFlags or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity.window.decorView.systemUiVisibility = systemUIFlags
        window.statusBarColor = statusBarColor
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addTranslucentView(window.decorView as ViewGroup, statusBarColor)
    }
}

/**
 * Android6.0及以上系统使用Android提供的标准api来修改状态栏黑白字符
 * Android6.0以下各家厂商需要单独适配
 *
 * @param lightMode true表示黑色字符，false表示白色字符
 */
fun setLightStatusBarIcon(activity: Activity, lightMode: Boolean) {
    val window = activity.window
    // Android 6.0以上
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = if (lightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

/**
 * 设置状态栏颜色(非沉浸式状态栏)
 */
fun setStatusBarColor(activity: Activity, @ColorInt bg: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = bg
    }
}

/**
 * 添加虚拟的状态栏
 */
private fun addTranslucentView(container: ViewGroup, @ColorInt statusBarColor: Int) {
    var translucentView = container.findViewById<View>(android.R.id.custom)
    if (translucentView === null) {
        translucentView = View(container.context)
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight())
        container.addView(translucentView, lp)
    }
    translucentView.setBackgroundColor(statusBarColor)
}

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(): Int {
    val statusBarHeight = 24f
    val resId = BaseApplication.getApplication().resources.getIdentifier(
        "status_bar_height",
        "dimen",
        "android"
    )
    return if (resId > 0) {
        BaseApplication.getApplication().resources.getDimensionPixelSize(resId)
    } else {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            statusBarHeight,
            Resources.getSystem().displayMetrics
        ).toInt()
    }
}

fun fixNotchScreen(activity: Activity) {
    val window = activity.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val lp = window.attributes
        lp.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        window.attributes = lp
    }

    window.decorView.post {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val displayCutout = window.decorView.rootWindowInsets?.displayCutout
            i("安全区域距离屏幕左边的距离：${displayCutout?.safeInsetLeft}")
            i("安全区域距离屏幕右边的距离：${displayCutout?.safeInsetRight}")
            i("安全区域距离屏幕顶部的距离：${displayCutout?.safeInsetTop}")
            i("安全区域距离屏幕底部的距离：${displayCutout?.safeInsetBottom}")

            val rects = displayCutout?.boundingRects
            if (rects.isNullOrEmpty()) {
                i("不是刘海屏")
            } else {
                i("刘海屏数量：${rects.size}")
                for (rect in rects) {
                    i("刘海屏区域：$rect")
                }
            }
        }
    }

}