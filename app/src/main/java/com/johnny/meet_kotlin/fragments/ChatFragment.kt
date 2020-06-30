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
 * 聊天
 *
 * @author Johnny
 */
class ChatFragment : FragmentTabHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i(msg = "ChatFragment onCreate is Called")
    }

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        i(msg = "ChatFragment onCreateView is Called")
        val contentView = layoutInflater.inflate(R.layout.fragment_chat, null)
        return contentView
    }

    override fun onResume() {
        super.onResume()
        i(msg = "ChatFragment onResume is Called")
    }

    override fun onPause() {
        super.onPause()
        i(msg = "ChatFragment onPause is Called")
    }

    override fun onStop() {
        super.onStop()
        i(msg = "ChatFragment onStop is Called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        i(msg = "ChatFragment onDestroyView is Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        i(msg = "ChatFragment onDestroy is Called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        i(msg = "ChatFragment onAttach is Called")
    }

    override fun onDetach() {
        super.onDetach()
        i(msg = "ChatFragment onDetach is Called")
    }

    override fun onHide() {
        i(msg = "ChatFragment onHide is Called")
    }

    override fun onShow() {
        i(msg = "ChatFragment onShow is Called")
    }
}