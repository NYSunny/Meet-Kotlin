package com.johnny.meet_kotlin.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.base.utils.startActivity
import com.johnny.customfragmenttabhostlib.FragmentTabHostFragment
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.activities.AddFriendActivity
import com.johnny.meet_kotlin.adpters.StarTagAdapter
import com.johnny.meet_kotlin.model.StarModel
import kotlinx.android.synthetic.main.fragment_star.view.*

/**
 * 星球
 *
 * @author Johnny
 */
class StarFragment : FragmentTabHostFragment(), View.OnClickListener {

    private lateinit var mContentView: View
    private var mStarTagAdapter: StarTagAdapter? = null

    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mContentView = layoutInflater.inflate(R.layout.fragment_star, null)
        setupClickEvent()
        setupView()
        return this.mContentView
    }

    private fun setupView() {
        this.mStarTagAdapter = context?.let { StarTagAdapter(it, fakeDatas()) }
        this.mStarTagAdapter?.let { mContentView.tagCloudView.setAdapter(it) }
    }

    private fun fakeDatas(): MutableList<StarModel> {
        val fakeDatas = mutableListOf<StarModel>()
        for (index in 0 until 100) {
            val fakeData =
                StarModel("Star-$index", index.toString(), R.drawable.img_star_icon_3.toString())
            fakeDatas.add(fakeData)
        }
        return fakeDatas
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
            R.id.ivAdd -> skipAdd()
            R.id.ivCamera -> skipCamera()
            R.id.llLove -> skipLove()
            R.id.llFate -> skipFate()
            R.id.llRandom -> skipRandom()
            R.id.llSoul -> skipSoul()
        }
    }

    private fun skipSoul() {
    }

    private fun skipRandom() {
    }

    private fun skipLove() {
    }

    private fun skipFate() {
    }

    private fun skipCamera() {
    }

    private fun skipAdd() = context?.startActivity<AddFriendActivity>()
}