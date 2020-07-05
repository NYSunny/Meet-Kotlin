package com.johnny.base.views.rv

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.johnny.base.helper.GlideHelper

/**
 * @author Johnny
 */
class RVViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private var mChildViews: SparseArray<View>? = null


    @Suppress("UNCHECKED_CAST")
    fun <V : View> getViewById(viewId: Int): V {
        if (this.mChildViews == null) this.mChildViews = SparseArray()
        val childView = this.mChildViews!![viewId]
        childView?.let { return it as V }
        val child = view.findViewById<V>(viewId)
        mChildViews?.put(viewId, child)
        return child
    }

    fun <V : View> findViewOften(viewId: Int): V {
        val view: V = view.findViewOften(viewId)
        return view
    }

    fun setImage(viewId: Int, url: String?) {
        val imageView = findViewOften<ImageView>(viewId)
        GlideHelper.loadUrl(view.context, imageView, url)
    }
}