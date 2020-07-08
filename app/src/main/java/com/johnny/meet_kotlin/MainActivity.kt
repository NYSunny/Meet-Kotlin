package com.johnny.meet_kotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.johnny.base.BaseUIActivity
import com.johnny.base.utils.SpUtils
import com.johnny.base.utils.startActivity
import com.johnny.meet_kotlin.activities.UploadBasicInfoActivity
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.bmob.User
import com.johnny.meet_kotlin.fragments.ChatFragment
import com.johnny.meet_kotlin.fragments.MeFragment
import com.johnny.meet_kotlin.fragments.PlazaFragment
import com.johnny.meet_kotlin.fragments.StarFragment
import com.johnny.meet_kotlin.rongcloud.RongCloudManager
import com.johnny.meet_kotlin.services.CloudService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_first_hint.*
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

        checkToken()
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

    private fun checkToken() {
        val token = SpUtils.getString(SP_KEY_TOKEN, "")
        if (!token.isBlank()) {
            startRongCloudService()
        } else {
            val currentUser: User? = BmobManager.getCurrentUser()
            val tokenPhoto = currentUser?.tokenPhoto
            val tokenNickName = currentUser?.tokenNickName
            if (tokenPhoto.isNullOrBlank() || tokenNickName.isNullOrBlank()) {
                // 上传个人信息
                uploadBasicInfo()
            } else {
                // 用tokenPhoto和tokenNickName获取Token
                createToken()
            }
        }
    }

    private fun uploadBasicInfo() {
        com.johnny.base.utils.showDialog(
            this,
            R.layout.dialog_first_hint,
            false,
            style = R.style.EndInEndOutAnimationDialogTheme
        ) {
            ivNext.setOnClickListener {
                this@showDialog.dismiss()
                startActivity<UploadBasicInfoActivity>()
            }
        }
    }

    private fun createToken() {
        val formMap = mutableMapOf<String, String>()
        BmobManager.getCurrentUser()?.let {
            formMap["userId"] = it.objectId
            formMap["name"] = it.tokenNickName ?: ""
            formMap["portraitUri"] = it.tokenPhoto ?: ""

            // 获取融云token
            RongCloudManager.getToken(this, formMap) { token, throwable ->
                if (token != null) {
                    SpUtils.saveString(SP_KEY_TOKEN, token.token)
                    startRongCloudService()
                }
            }
        }
    }

    /**
     * 启动本地融云服务
     */
    private fun startRongCloudService() {
        // 启动云服务去连接融云服务
        startService(Intent(this, CloudService::class.java))
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
