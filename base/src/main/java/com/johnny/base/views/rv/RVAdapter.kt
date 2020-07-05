package com.johnny.base.views.rv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.johnny.base.utils.getApp

/**
 * @author Johnny
 */
class RVAdapter<D : RVData>(var datas: MutableList<D>?) : RecyclerView.Adapter<RVViewHolder>() {

    private var mOnProvideLayoutIdListener: OnProvideLayoutIdListener? = null

    private var mOnBindViewListener: OnBindViewListener<D>? = null

    private var mOnItemClickListener: OnItemClickListener<D>? = null

    private val mInflater: LayoutInflater = getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    fun setOnProvideLayoutIdListener(listener: OnProvideLayoutIdListener) {
        this.mOnProvideLayoutIdListener = listener
    }

    fun setOnBindViewListener(listener: OnBindViewListener<D>) {
        this.mOnBindViewListener = listener
    }

    fun setOnItemClickListener(listener: OnItemClickListener<D>) {
        this.mOnItemClickListener = listener
    }

    override fun getItemViewType(position: Int): Int =
        datas?.let { it[position].getItemType() } ?: 0

    override fun getItemCount(): Int = datas?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val layoutId: Int? = this.mOnProvideLayoutIdListener?.onProvideLayoutId(viewType)
        return layoutId?.let {
            RVViewHolder(
                this.mInflater.inflate(layoutId, parent, false)
            ) }
            ?: RVViewHolder(View(parent.context))
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        this.mOnBindViewListener?.onBindView(
            datas!![position],
            holder,
            getItemViewType(position),
            position
        )

        this.mOnItemClickListener?.let { onItemClickListener ->
            holder.itemView.setOnClickListener {
                onItemClickListener.onItemClick(datas!![position])
            }
        }
    }

    interface OnProvideLayoutIdListener {
        fun onProvideLayoutId(viewType: Int): Int
    }

    interface OnBindViewListener<DATA : RVData> {
        fun onBindView(data: DATA, holder: RVViewHolder, itemType: Int, position: Int)
    }

    interface OnItemClickListener<DATA : RVData> {
        fun onItemClick(data: DATA)
    }
}