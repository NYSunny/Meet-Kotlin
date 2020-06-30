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
 * 星球
 *
 * @author Johnny
 */
class StarFragment : FragmentTabHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i(msg = "StarFragment onCreate is Called")
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        i(msg = "StarFragment onCreateView is Called")
        val contentView = layoutInflater.inflate(R.layout.fragment_star, null)
        return contentView
    }

    override fun onResume() {
        super.onResume()
        i(msg = "StarFragment onResume is Called")
    }

    override fun onPause() {
        super.onPause()
        i(msg = "StarFragment onPause is Called")
    }

    override fun onStop() {
        super.onStop()
        i(msg = "StarFragment onStop is Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        i(msg = "StarFragment onDestroyView is Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        i(msg = "StarFragment onDestroy is Called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        i(msg = "StarFragment onAttach is Called")
    }

    override fun onDetach() {
        super.onDetach()
        i(msg = "StarFragment onDetach is Called")
    }

    override fun onHide() {
        i(msg = "StarFragment onHide is Called")
    }

    override fun onShow() {
        i(msg = "StarFragment onShow is Called")
    }
}