package com.johnny.meet_kotlin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.johnny.base.BaseUIActivity
import com.johnny.meet_kotlin.fragments.ChatFragment
import com.johnny.meet_kotlin.fragments.MeFragment
import com.johnny.meet_kotlin.fragments.PlazaFragment
import com.johnny.meet_kotlin.fragments.StarFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_bottom_tab.view.*

class MainActivity : BaseUIActivity() {

    /* 内容 */
    private val fragments = arrayOf(
        StarFragment::class.java,
        PlazaFragment::class.java,
        ChatFragment::class.java,
        MeFragment::class.java
    )

    /* 底部标签图片 */
    private val tabImages = arrayOf(
        R.drawable.selector_bottom_tab_star_icon,
        R.drawable.selector_bottom_tab_plaza_icon,
        R.drawable.selector_bottom_tab_chat_icon,
        R.drawable.selector_bottom_tab_me_icon
    )

    /* 底部标签文本 */
    private val tabTexts = arrayOf(
        R.string.text_bottom_tab_star,
        R.string.text_bottom_tab_plaza,
        R.string.text_bottom_tab_chat,
        R.string.text_bottom_tab_me
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        fixSystemUIEnabled = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        // 初始化FragmentTabHost
        container.setup(this, supportFragmentManager, android.R.id.tabcontent)
        for (index in fragments.indices) {
            val tabSpec = container.newTabSpec(index.toString())
            // 设置底部标签View
            tabSpec.setIndicator(provideTabView(index))
            // FragmentTabHost添加Context和TabSpec
            container.addTab(tabSpec, fragments[index], null)
        }
    }

    /**
     * 获取底部标签View
     */
    @SuppressLint("InflateParams")
    private fun provideTabView(index: Int): View =
        layoutInflater.inflate(R.layout.view_bottom_tab, null).apply {
            ivTab.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, tabImages[index]))
            tvTab.text = resources.getString(tabTexts[index])
        }
}
