package com.johnny.meet_kotlin.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.johnny.base.BaseUIActivity
import com.johnny.base.manager.MediaPlayerManager
import com.johnny.base.utils.setLightStatusBarIcon
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.view.GuideView
import kotlinx.android.synthetic.main.activity_guide.*

/**
 * 引导页
 * 1.适配全面屏和刘海屏
 * 2.播放音乐，暂停播放音乐
 * 3.点击跳过跳转登录页
 * 4.页面左右滑动
 * 5.指示器随页面左右滑动而变化
 * 6.音乐按钮旋转动画(用属性动画实现)
 *
 * @author Johnny
 */
class GuideActivity : BaseUIActivity() {

    private val mediaPlayerManager: MediaPlayerManager = MediaPlayerManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        fixSystemUIEnabled = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        setLightStatusBarIcon(this, true)

        init()
    }

    override fun onDestroy() {
        mediaPlayerManager.stopPlay()
        super.onDestroy()
    }

    private fun init() {
        val guideTexts = arrayOf(
            getString(R.string.text_guide_pager_1),
            getString(R.string.text_guide_pager_2),
            getString(R.string.text_guide_pager_3)
        )
        val guideImgRes = arrayOf(
            R.drawable.anim_guide_star,
            R.drawable.anim_guide_night,
            R.drawable.anim_guide_smile
        )

        // 初始化ViewPager中的三个View
        val views = arrayListOf<GuideView>()
        for ((index, text) in guideTexts.withIndex()) {
            val view = GuideView(this)
            val lp = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            view.layoutParams = lp
            view.setImageResource(guideImgRes[index])
            view.setText(text)
            views.add(index, view)
        }

        views[0].startAnimation()

        val rotateAnimator = ObjectAnimator.ofFloat(ivMusic, "rotation", 0F, 360F)
        rotateAnimator.duration = 5000
        rotateAnimator.repeatCount = ValueAnimator.INFINITE
        rotateAnimator.repeatMode = ValueAnimator.RESTART
        rotateAnimator.interpolator = null
        rotateAnimator.start()

        val openRawResourceFd = resources.openRawResourceFd(R.raw.music)
        mediaPlayerManager.startPlay(openRawResourceFd)
        mediaPlayerManager.setLooping(true)

        ivMusic.setOnClickListener {
            val mediaStatus = mediaPlayerManager.mCurrentMediaStatus
            if (mediaStatus == MediaPlayerManager.MEDIA_STATUS_PLAYING) {
                // 暂停播放
                mediaPlayerManager.pausePlay()
                // 动画暂停
                rotateAnimator.pause()
                // 更换按钮背景图
                ivMusic.setImageResource(R.drawable.img_guide_music_off)
            } else if (mediaStatus == MediaPlayerManager.MEDIA_STATUS_PAUSED) {
                // 继续播放
                mediaPlayerManager.continuePlay()
                // 动画继续
                rotateAnimator.resume()
                // 更换按钮背景图
                ivMusic.setImageResource(R.drawable.img_guide_music)
            }
        }

        // 初始化ViewPager
        val adapter =
            Adapter(views)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                selectPoint(position)

                for (view in views) {
                    view.stopAnimation()
                }
                views[position].startAnimation()
            }
        })

        tvSkip.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * 指示器
     */
    private fun selectPoint(position: Int) {
        when (position) {
            0 -> {
                ivPoint1.setImageResource(R.drawable.img_guide_point_p)
                ivPoint2.setImageResource(R.drawable.img_guide_point)
                ivPoint3.setImageResource(R.drawable.img_guide_point)
            }
            1 -> {
                ivPoint1.setImageResource(R.drawable.img_guide_point)
                ivPoint2.setImageResource(R.drawable.img_guide_point_p)
                ivPoint3.setImageResource(R.drawable.img_guide_point)
            }
            2 -> {
                ivPoint1.setImageResource(R.drawable.img_guide_point)
                ivPoint2.setImageResource(R.drawable.img_guide_point)
                ivPoint3.setImageResource(R.drawable.img_guide_point_p)
            }
        }
    }

    class Adapter(private val mViews: ArrayList<GuideView>) : PagerAdapter() {
        override fun isViewFromObject(view: View, obj: Any): Boolean = view === obj

        override fun getCount(): Int = this.mViews.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(this.mViews[position])
            return this.mViews[position]
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(this.mViews[position])
        }
    }
}