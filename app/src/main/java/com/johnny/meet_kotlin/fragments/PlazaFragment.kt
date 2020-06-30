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
 * 广场
 *
 * @author Johnny
 */
class PlazaFragment : FragmentTabHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i(msg = "PlazaFragment onCreate is Called")
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        i(msg = "PlazaFragment onCreateView is Called")
        val contentView = layoutInflater.inflate(R.layout.fragment_plaza, null)
        return contentView
    }

    override fun onResume() {
        super.onResume()
        i(msg = "PlazaFragment onResume is Called")
    }

    override fun onPause() {
        super.onPause()
        i(msg = "PlazaFragment onPause is Called")
    }

    override fun onStop() {
        super.onStop()
        i(msg = "PlazaFragment onStop is Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        i(msg = "PlazaFragment onDestroyView is Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        i(msg = "PlazaFragment onDestroy is Called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        i(msg = "PlazaFragment onAttach is Called")
    }

    override fun onDetach() {
        super.onDetach()
        i(msg = "PlazaFragment onDetach is Called")
    }

    override fun onHide() {
        i(msg = "PlazaFragment onHide is Called")
    }

    override fun onShow() {
        i(msg = "PlazaFragment onShow is Called")
    }
}
