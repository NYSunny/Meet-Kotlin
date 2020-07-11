package com.johnny.meet_kotlin.model

import androidx.annotation.DrawableRes
import com.johnny.base.views.rv.RVData

/**
 * @author Johnny
 */
data class UserInfoModel(
    var title: String = "",
    var content: String = "",
    var bgColor: Int = 0
) : RVData()