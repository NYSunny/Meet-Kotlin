package com.johnny.meet_kotlin.model

import com.johnny.base.views.rv.RVData

/**
 * @author Johnny
 */
data class AddFriendModel(
    var title: String = "",
    var nickName: String = "",
    var avatarUrl: String? = null,
    var sex: Boolean = false,
    var age: Int = 0,
    var desc: String? = null,
    var userId: String = "",
    var viewType: Int = 0,
    var isContact: Boolean = false,
    var contactName: String = "",
    var contactPhone: String = ""
) : RVData() {

    companion object {
        const val TITLE = 1
        const val CONTENT = 2
        const val NO_USER = 3
    }

    override fun getItemType(): Int = viewType
}