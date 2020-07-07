package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.base.utils.startActivity
import com.johnny.customfragmenttabhostlib.FragmentTabHostFragment
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.activities.MeInfoActivity
import com.johnny.meet_kotlin.activities.NewFriendActivity
import com.johnny.meet_kotlin.activities.PrivacySettingActivity
import com.johnny.meet_kotlin.activities.SettingActivity
import kotlinx.android.synthetic.main.fragment_me.*

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
        llMeInfo.setOnClickListener(this)
        llNewFriend.setOnClickListener(this)
        llPrivacySetting.setOnClickListener(this)
        llShare.setOnClickListener(this)
        llSetting.setOnClickListener(this)
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