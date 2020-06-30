package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.base.utils.i
import com.johnny.customfragmenttabhostlib.FragmentTabHostFragment
import com.johnny.meet_kotlin.R

/**
 * 我的
 *
 * @author Johnny
 */
class MeFragment : FragmentTabHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i(msg = "MeFragment onCreate is Called")
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        i(msg = "MeFragment onCreateView is Called")
        val contentView = layoutInflater.inflate(R.layout.fragment_me, null)
        return contentView
    }

    override fun onResume() {
        super.onResume()
        i(msg = "MeFragment onResume is Called")
    }

    override fun onPause() {
        super.onPause()
        i(msg = "MeFragment onPause is Called")
    }

    override fun onStop() {
        super.onStop()
        i(msg = "MeFragment onStop is Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        i(msg = "MeFragment onDestroyView is Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        i(msg = "MeFragment onDestroy is Called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        i(msg = "MeFragment onAttach is Called")
    }

    override fun onDetach() {
        super.onDetach()
        i(msg = "MeFragment onDetach is Called")
    }

    override fun onHide() {
        i(msg = "MeFragment onHide is Called")
    }

    override fun onShow() {
        i(msg = "MeFragment onShow is Called")
    }
}