@file:JvmName("DialogUtils")

package com.johnny.base.utils

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.johnny.base.R

/**
 * @author Johnny
 */

@JvmOverloads
fun createDialog(
    context: Context,
    layoutResId: Int,
    isCanceled: Boolean,
    gravity: Int = Gravity.CENTER,
    style: Int? = null,
    block: (AppCompatDialog.() -> Unit)? = null
): AppCompatDialog = AppCompatDialog(context, R.style.BaseDialogTheme).also { dialog ->
    dialog.setContentView(layoutResId)
    dialog.setCanceledOnTouchOutside(isCanceled)
    val window = dialog.window
    val layoutParams = window?.attributes
    layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    layoutParams?.gravity = gravity
    window?.attributes = layoutParams
    style?.let { window?.setWindowAnimations(it) }
    block?.let { it(dialog) }
}

fun showDialog(dialog: AppCompatDialog) {
    if (!dialog.isShowing) dialog.show()
}

@JvmOverloads
fun showDialog(
    context: Context,
    layoutResId: Int,
    isCanceled: Boolean = true,
    gravity: Int = Gravity.CENTER,
    style: Int? = null,
    block: (AppCompatDialog.() -> Unit)? = null
): AppCompatDialog = createDialog(context, layoutResId, isCanceled, gravity, style, block).apply {
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

fun dismissDialog(dialog: AppCompatDialog) {
    if (dialog.isShowing) dialog.dismiss()
}