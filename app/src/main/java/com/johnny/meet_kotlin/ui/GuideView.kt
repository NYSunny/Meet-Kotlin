package com.johnny.meet_kotlin.ui

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.johnny.meet_kotlin.R
import kotlinx.android.synthetic.main.view_guide.view.*

/**
 * @author Johnny
 */
class GuideView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_guide, this, true)
    }

    fun setImageResource(@DrawableRes imgResource: Int) = iv_guide.setImageResource(imgResource)

    fun setText(text: String) {
        tv_text.text = text
    }

    fun startAnimation() {
        val drawable = iv_guide.drawable
        if (drawable is AnimationDrawable) {
            drawable.start()
        }
    }

    fun stopAnimation() {
        val drawable = iv_guide.drawable
        if (drawable is AnimationDrawable) {
            drawable.stop()
        }
    }
}