package com.johnny.base.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.johnny.base.R
import kotlinx.android.synthetic.main.view_empty_error.view.*

/**
 * @author Johnny
 */
class EmptyErrorView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_empty_error, this, true)
    }

    fun showEmptyView() {
        ivEmpty.visibility = View.VISIBLE
        tvError.visibility = View.GONE
    }

    fun showErrorView() {
        tvError.visibility = View.VISIBLE
        ivEmpty.visibility = View.GONE
    }
}