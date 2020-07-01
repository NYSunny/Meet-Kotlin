package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.customfragmenttabhostlib.FragmentTabHostFragment
import com.johnny.meet_kotlin.R
import kotlinx.android.synthetic.main.fragment_star.view.*

/**
 * 星球
 *
 * @author Johnny
 */
class StarFragment : FragmentTabHostFragment(),View.OnClickListener {

    private lateinit var mContentView: View

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mContentView = layoutInflater.inflate(R.layout.fragment_star, null)
        setupClickEvent()
        return this.mContentView
    }

    private fun setupClickEvent() {
        mContentView.ivCamera.setOnClickListener(this)
        mContentView.ivAdd.setOnClickListener(this)
        mContentView.llFate.setOnClickListener(this)
        mContentView.llLove.setOnClickListener(this)
        mContentView.llRandom.setOnClickListener(this)
        mContentView.llSoul.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }
}