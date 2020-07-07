package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.customfragmenttabhostlib.FragmentTabHostFragment
import com.johnny.meet_kotlin.R

/**
 * 我的
 *
 * @author Johnny
 */
class MeFragment : FragmentTabHostFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = layoutInflater.inflate(R.layout.fragment_me, null)
        return contentView
    }
}