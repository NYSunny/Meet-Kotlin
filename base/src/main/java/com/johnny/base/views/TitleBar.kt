package com.johnny.base.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.johnny.base.utils.getStatusBarHeight

/**
 * @author Johnny
 */
class TitleBar(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private lateinit var mFakeStatusBar: View
    private var mDefaultTitleBar: View? = null
    private var mDefaultLeftView: View? = null
    private var mDefaultLeft2View: View? = null
    private var mDefaultMiddleView: View? = null
    private var mDefaultRightView: View? = null
    private var mDefaultRight2View: View? = null

    private companion object {
        const val TITLE_BAR_TAG = "TITLE_BAR_TAG"
        const val DEFAULT_TITLE_BAR_HEIGHT = 50
    }

    constructor(context: Context) : this(context, null)

    init {
        setupView()
    }

    private fun setupView() {
        orientation = VERTICAL

        addFakeStatusBar()
//        addDefaultTitleBar()
    }

    private fun addDefaultTitleBar() {
        this.mDefaultTitleBar = LinearLayout(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                resources.getDimensionPixelSize(DEFAULT_TITLE_BAR_HEIGHT)
            )
            tag = TITLE_BAR_TAG
            orientation = HORIZONTAL
        }
        addView(this.mDefaultLeft2View)
        
    }

    private fun addFakeStatusBar() {
        this.mFakeStatusBar = View(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight()
            )
        }
        addView(this.mFakeStatusBar)
    }

    fun setFakeStatusBarColor(colorInt: Int) {
        this.mFakeStatusBar.setBackgroundColor(colorInt)
    }
}