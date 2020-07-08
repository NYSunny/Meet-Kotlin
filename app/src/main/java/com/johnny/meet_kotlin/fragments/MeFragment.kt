package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.base.helper.GlideHelper
import com.johnny.base.utils.getApp
import com.johnny.base.utils.startActivity
import com.johnny.customfragmenttabhostlib.FragmentTabHostFragment
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.activities.MeInfoActivity
import com.johnny.meet_kotlin.activities.NewFriendActivity
import com.johnny.meet_kotlin.activities.PrivacySettingActivity
import com.johnny.meet_kotlin.activities.SettingActivity
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.bmob.User
import kotlinx.android.synthetic.main.fragment_me.view.*

/**
 * 我的
 *
 * @author Johnny
 */
class MeFragment : FragmentTabHostFragment(), View.OnClickListener {

    private lateinit var mView: View

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mView = layoutInflater.inflate(R.layout.fragment_me, null)
        setupView()
        return this.mView
    }

    private fun setupView() {
        this.mView.llMeInfo.setOnClickListener(this)
        this.mView.llNewFriend.setOnClickListener(this)
        this.mView.llPrivacySetting.setOnClickListener(this)
        this.mView.llShare.setOnClickListener(this)
        this.mView.llSetting.setOnClickListener(this)

        setupMeInfo()
    }

    private fun setupMeInfo() {
        val currentUser: User? = BmobManager.getCurrentUser()
        currentUser?.let {
            GlideHelper.loadUrl(getApp(), this.mView.ivAvatar, it.photo)
            this.mView.tvNickName.text = it.nickName
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llMeInfo -> toMeInfoActivity()
            R.id.llNewFriend -> toNewFriendActivity()
            R.id.llPrivacySetting -> toPrivacySetting()
            R.id.llShare -> toShareActivity()
            R.id.llSetting -> toSettingActivity()
        }
    }

    private fun toSettingActivity() {
        context?.startActivity<SettingActivity>()
    }

    private fun toShareActivity() {
    }

    private fun toPrivacySetting() {
        context?.startActivity<PrivacySettingActivity>()
    }

    private fun toNewFriendActivity() {
        context?.startActivity<NewFriendActivity>()
    }

    private fun toMeInfoActivity() {
        context?.startActivity<MeInfoActivity>()
    }
}