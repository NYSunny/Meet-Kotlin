@file:JvmName("ViewHolder")

package com.johnny.base.views.rv

import android.util.SparseArray
import android.view.View

/**
 * @author Johnny
 */

@Suppress("UNCHECKED_CAST")
fun <V : View> View.findViewOften(viewId: Int): V {
    val childViews: SparseArray<View> = tag as? SparseArray<View> ?: SparseArray()
    val childView = childViews.get(viewId)
    childView?.let { return it as V }
    val child = findViewById<V>(viewId)
    childViews.put(viewId, child)
    return child
}