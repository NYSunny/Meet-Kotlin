package com.johnny.base.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author Johnny
 */

fun hideSoftInput(activity: Activity) {
    hideSoftInput(activity.window)
}

fun hideSoftInput(window: Window) {
    var view:View? = window.currentFocus
    if (view == null) {
        val decorView = window.decorView
        val focusView = decorView.findViewWithTag<View>("keyboardTagView")
        if (focusView == null) {
            view = EditText(window.context)
            view.tag = "keyboardTagView"
            (decorView as ViewGroup).addView(view, 0, 0)
        } else {
            view = focusView
        }
        view.requestFocus()
    }
    hideSoftInput(view)
}

fun hideSoftInput(view: View) {
    val imm: InputMethodManager? =
        getApp().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}