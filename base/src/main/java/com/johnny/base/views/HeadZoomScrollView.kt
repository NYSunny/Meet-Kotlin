package com.johnny.base.views

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView

/**
 * 头部可缩放的ScrollView
 *
 * @author Johnny
 */
class HeadZoomScrollView(context: Context, attrs: AttributeSet?) :
    NestedScrollView(context, attrs) {

    private companion object {
        const val ZOOM_RATE = 0.3F
        const val ROLL_BACK_RATE = 0.5F
    }

    private var mZoomView: View? = null
    private var mZoomViewWidth = 0
    private var mZoomViewHeight = 0
    private var mDownPosition = 0F
    private var isFingerToDown = false

    constructor(context: Context) : this(context, null)

    override fun onFinishInflate() {
        super.onFinishInflate()
        getChildAt(0)?.apply {
            mZoomView = (this as ViewGroup).getChildAt(0)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (this.mZoomViewWidth <= 0 || this.mZoomViewHeight <= 0) {
            this.mZoomViewWidth = this.mZoomView?.measuredWidth ?: 0
            this.mZoomViewHeight = this.mZoomView?.measuredHeight ?: 0
        }

        when (ev?.action) {
            MotionEvent.ACTION_MOVE -> {
                // 只处理getScrollY == 0的情况，其他一概不处理
                if (!isFingerToDown) {
                    if (scrollY == 0) {
                        this.mDownPosition = ev.y
                    } else {
                        return super.dispatchTouchEvent(ev)
                    }
                }
                val distance = ev.y - this.mDownPosition
                if (distance < 0) {
                    return super.dispatchTouchEvent(ev)
                }
                // 表示手指正向下滑动
                isFingerToDown = true
                val zoomDistance = distance * ZOOM_RATE
                zoomView(zoomDistance)
            }
            MotionEvent.ACTION_UP -> {
                isFingerToDown = false
                rollbackView()
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun rollbackView() {
        this.mZoomView?.let {
            val diff = it.measuredWidth - this.mZoomViewWidth
            val valueAnimator = ValueAnimator.ofFloat(diff.toFloat(), 0F)
            valueAnimator.duration = (diff * ROLL_BACK_RATE).toLong()
            valueAnimator.addUpdateListener { value ->
                zoomView(value.animatedValue as Float)
            }
            valueAnimator.start()
        }
    }

    private fun zoomView(zoomDistance: Float) {
        if (this.mZoomViewWidth <= 0 || this.mZoomViewHeight <= 0) return
        // 缩放View，实际就是改变View的宽高
        val lp: MarginLayoutParams? = this.mZoomView?.layoutParams as? MarginLayoutParams
        lp?.let {
            it.width = (this.mZoomViewWidth + zoomDistance).toInt()
            it.height =
                (this.mZoomViewHeight * ((this.mZoomViewWidth + zoomDistance) / this.mZoomViewWidth)).toInt()
            it.marginStart = -(it.width - this.mZoomViewWidth) / 2
            this.mZoomView?.layoutParams = it
        }
    }
}