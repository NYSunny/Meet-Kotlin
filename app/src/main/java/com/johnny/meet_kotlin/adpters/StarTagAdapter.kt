package com.johnny.meet_kotlin.adpters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.model.StarModel
import com.moxun.tagcloudlib.view.TagsAdapter
import kotlinx.android.synthetic.main.item_view_star.view.*

/**
 * @author Johnny
 */
class StarTagAdapter(private val context: Context, var datas: MutableList<StarModel>) : TagsAdapter() {

    private var mInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getPopularity(position: Int): Int = position % 7

    override fun onThemeColorChanged(view: View?, themeColor: Int) {
    }

    override fun getView(context: Context?, position: Int, parent: ViewGroup?): View {
        return this.mInflater.inflate(R.layout.item_view_star, parent, false).apply {
            val starModel = this@StarTagAdapter.datas[position]
            tvStarName.text = starModel.nickName
            ivStarIcon.setImageResource(starModel.photoUrl.toInt())
        }
    }

    override fun getItem(position: Int): Any = datas[position]

    override fun getCount(): Int = datas.size
}