package com.johnny.base.helper

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.johnny.base.R

/**
 * @author Johnny
 */
object GlideHelper {

    fun loadUrl(context: Context, iv: ImageView, url: String?) {
        Glide.with(context).load(url)
            .placeholder(R.drawable.img_glide_load_ing)
            .error(R.drawable.img_glide_load_error)
            .format(DecodeFormat.PREFER_RGB_565)
            .dontAnimate()
            .thumbnail(0.3F)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(iv)
    }
}