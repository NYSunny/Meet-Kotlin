package com.johnny.meet_kotlin.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.johnny.base.R

/**
 * 登录时图片验证
 *
 * @author Johnny
 */
class PictureCheckView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attr, defStyleAttr) {

    /*
    * 1.绘制整张图片的背景用作View的背景
    * 2.绘制白色方块
    * 3.把白色方块位置原本的View扣出来绘制到View宽度三分之一的位置
    * 4.实现抠出来的图滑动逻辑
    * */

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val blankPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val movePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mWidth = 0
    private var mHeight = 0

    private var mBgBitmap: Bitmap? = null

    private var lineX = 0
    private var lineY = 0
    private var cardSize = 100

    private var isMove = false
    private var moveX = 0
    private val errorValue = 10

    var onCheckListener: OnCheckListener? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        this.mWidth = w
        this.mHeight = h
        this.moveX = this.mWidth / 3
    }

    override fun onDraw(canvas: Canvas?) {
        // 绘制整张图片
        drawBg(canvas)
        // 绘制白色方块
        drawWhiteCard(canvas)
        // 绘制可移动的方块
        drawMoveCard(canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x ?: 0F
        val y = event?.y ?: 0F
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val tX = this.moveX
                val tY = this.mHeight / 2
                val arg = cardSize / 2
                val rect = Rect(tX, tY - arg, tX + cardSize, tY + arg)
                isMove = rect.contains(x.toInt(), y.toInt())
            }
            MotionEvent.ACTION_MOVE -> {
                if (isMove && x > 0 && x < (this.mWidth - cardSize)) {
                    this.moveX = x.toInt()
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                this.isMove = false
                if (this.moveX > this.lineX - this.errorValue && this.moveX < this.lineX + this.errorValue) {
                    // 重置moveX
                    this.moveX = this.mWidth / 3
                    this.onCheckListener?.onCheckSuccess()
                    invalidate()
                } else {
                    this.onCheckListener?.onCheckFail()
                }
            }
        }
        return true
    }

    /**
     * 绘制可移动方块
     */
    private fun drawMoveCard(canvas: Canvas?) {
        // 1.把白色方块占据的那块背景View抠出来
        val moveCardBitmap = Bitmap.createBitmap(
            this.mBgBitmap!!,
            this.lineX,
            this.lineY,
            this.cardSize,
            this.cardSize
        )
        // 2.把抠出来的背景绘制在View宽度的三分之一处
        canvas?.drawBitmap(
            moveCardBitmap,
            this.moveX.toFloat(),
            this.lineY.toFloat(),
            this.movePaint
        )
    }

    /**
     * 绘制白色方块
     */
    private fun drawWhiteCard(canvas: Canvas?) {
        val whiteCardBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_null_card)
        this.cardSize = whiteCardBitmap.width
        // 白色方块在View三分之二的位置
        this.lineX = this.mWidth / 3 * 2
        this.lineY = this.mHeight / 2 - (this.cardSize / 2)
        canvas?.drawBitmap(
            whiteCardBitmap,
            this.lineX.toFloat(),
            this.lineY.toFloat(),
            this.blankPaint
        )
    }

    /**
     * 绘制图片背景
     */
    private fun drawBg(canvas: Canvas?) {
        // 获取到原始图片的Bitmap
        val bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.img_bg)
        // 获取到一个和View相同宽高的空白Bitmap
        this.mBgBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888)
        // 把原始背景图片绘制到刚刚创建的空白Bitmap上
        val tCanvas = Canvas(this.mBgBitmap!!)
        tCanvas.drawBitmap(bgBitmap, null, Rect(0, 0, this.mWidth, this.mHeight), this.bgPaint)
        // 绘制到View上
        canvas?.drawBitmap(
            this.mBgBitmap!!,
            null,
            Rect(0, 0, this.mWidth, this.mHeight),
            this.bgPaint
        )
    }

    interface OnCheckListener {
        fun onCheckSuccess()

        fun onCheckFail()
    }
}