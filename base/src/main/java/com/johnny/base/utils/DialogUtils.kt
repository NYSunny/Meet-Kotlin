package com.johnny.base.utils

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.johnny.base.R

/**
 * @author Johnny
 */

fun showDialog(
    context: Context,
    layoutResId: Int,
    isCanceled: Boolean = true,
    block: (AppCompatDialog.() -> Unit)? = null
): AppCompatDialog =
    AppCompatDialog(context, R.style.BaseDialogTheme).apply {
        setContentView(layoutResId)
        setCanceledOnTouchOutside(isCanceled)
        block?.let { it() }
        show()
    }

/**
 * 显示加载弹窗
 */
fun showLoadingDialog(
    context: Context,
    isCanceled: Boolean,
    text: String
): AppCompatDialog = showDialog(context, R.layout.dialog_loading, isCanceled) {
    findViewById<TextView>(R.id.tvContent)?.text = text
}
