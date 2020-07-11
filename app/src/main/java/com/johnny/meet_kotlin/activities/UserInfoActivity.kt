package com.johnny.meet_kotlin.activities

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.johnny.base.BaseUIActivity
import com.johnny.base.helper.GlideHelper
import com.johnny.base.utils.showLoadingDialog
import com.johnny.base.utils.startActivity
import com.johnny.base.views.rv.RVAdapter
import com.johnny.base.views.rv.RVViewHolder
import com.johnny.meet_kotlin.INTENT_KEY_USER_ID
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.bmob.User
import com.johnny.meet_kotlin.model.UserInfoModel
import kotlinx.android.synthetic.main.activity_user_info.*

/**
 * @author Johnny
 */
class UserInfoActivity : BaseUIActivity(), View.OnClickListener {

    private var mUserId = ""
    private var mUser: User? = null
    private val mBgColors = intArrayOf(
        0x881E90FF.toInt(),
        0x8800FF7F.toInt(),
        0x88FFD700.toInt(),
        0x88FF6347.toInt(),
        0x88F08080.toInt(),
        0x8840E0D0.toInt()
    )

    companion object {

        fun start(context: Context, userId: String) {
            context.startActivity<UserInfoActivity> {
                putExtra(INTENT_KEY_USER_ID, userId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fixSystemUIEnabled = true
        super.onCreate(savedInstanceState)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.activity_user_info)

        parseIntentExtra()
        setupView()
        queryUserInfo()
    }

    private fun parseIntentExtra() {
        this.mUserId = intent.getStringExtra(INTENT_KEY_USER_ID) ?: ""
    }

    private fun queryUserInfo() {
        if (this.mUserId.isBlank()) return
        val dialog = showLoadingDialog(this, false, getString(R.string.text_loading))
        BmobManager.queryUserByObjectId(this.mUserId, object : FindListener<User>() {
            override fun done(users: MutableList<User>?, e: BmobException?) {
                dialog.dismiss()
                if (e == null) {
                    // 查询成功
                    if (!users.isNullOrEmpty()) {
                        mUser = users[0].apply {
                            notifyDataChanged(this)
                        }
                    }
                }
            }
        })
    }

    private fun notifyDataChanged(user: User) {
        GlideHelper.loadUrl(this, ivAvatar, user.photo)
        tvNickName.text = user.nickName
        tvDesc.text = user.desc

        val userInfoModels = getUserInfoModels(user)

        (rvUserInfo.adapter as RVAdapter<UserInfoModel>).datas = userInfoModels
        rvUserInfo.adapter?.notifyDataSetChanged()
    }

    private fun getUserInfoModels(user: User): MutableList<UserInfoModel> =
        mutableListOf<UserInfoModel>().apply {
            add(
                provideUserInfoModel(
                    mBgColors[0],
                    getString(R.string.text_sex),
                    if (user.sex) getString(R.string.text_man) else getString(
                        R.string.text_woman
                    )
                )
            )
            add(
                provideUserInfoModel(
                    mBgColors[1],
                    getString(R.string.text_age),
                    user.age.toString() + getString(R.string.text_age_suffix)
                )
            )
            add(
                provideUserInfoModel(
                    mBgColors[2],
                    getString(R.string.text_birthday),
                    user.birthday ?: ""
                )
            )
            add(
                provideUserInfoModel(
                    mBgColors[3],
                    getString(R.string.text_constellation),
                    user.constellation ?: ""
                )
            )
            add(
                provideUserInfoModel(
                    mBgColors[4],
                    getString(R.string.text_hobby),
                    user.hobby ?: ""
                )
            )
            add(
                provideUserInfoModel(
                    mBgColors[5],
                    getString(R.string.text_status),
                    user.status ?: ""
                )
            )
        }

    private fun provideUserInfoModel(bgColor: Int, title: String, content: String) =
        UserInfoModel().apply {
            this.bgColor = bgColor
            this.title = title
            this.content = content
        }

    private fun setupView() {
        btnAddFriend.setOnClickListener(this)
        btnChat.setOnClickListener(this)
        btnAudioChat.setOnClickListener(this)
        btnVideoChat.setOnClickListener(this)

        rvUserInfo.layoutManager = GridLayoutManager(this, 3)
        rvUserInfo.adapter = RVAdapter<UserInfoModel>(null).apply {
            setOnProvideLayoutIdListener(object : RVAdapter.OnProvideLayoutIdListener {
                override fun onProvideLayoutId(viewType: Int): Int = R.layout.item_view_user_info
            })
            setOnBindViewListener(object : RVAdapter.OnBindViewListener<UserInfoModel> {
                override fun onBindView(
                    data: UserInfoModel,
                    holder: RVViewHolder,
                    itemType: Int,
                    position: Int
                ) {
                    holder.getViewById<View>(R.id.bg).setBackgroundColor(data.bgColor)
                    holder.getViewById<TextView>(R.id.tvTitle).text = data.title
                    holder.getViewById<TextView>(R.id.tvContent).text = data.content
                }
            })
        }
    }

    override fun actionBarTitle(): String? = ""

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAddFriend -> addFriend()
            R.id.btnChat -> chat()
            R.id.btnAudioChat -> audioChat()
            R.id.btnVideoChat -> videoChat()
        }
    }

    private fun videoChat() {

    }

    private fun audioChat() {

    }

    private fun chat() {

    }

    private fun addFriend() {

    }
}