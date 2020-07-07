package com.johnny.meet_kotlin.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.johnny.base.BaseUIActivity
import com.johnny.base.utils.*
import com.johnny.base.views.rv.RVAdapter
import com.johnny.base.views.rv.RVViewHolder
import com.johnny.meet_kotlin.R
import com.johnny.meet_kotlin.bmob.BmobManager
import com.johnny.meet_kotlin.bmob.IMUser
import com.johnny.meet_kotlin.model.AddFriendModel
import kotlinx.android.synthetic.main.activity_add_friend.*

/**
 * 添加朋友
 *
 * @author Johnny
 */
class AddFriendActivity : BaseUIActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        setupView()
    }

    private fun setupView() {
        ivSearch.setOnClickListener(this)
        tvToContact.setOnClickListener(this)

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = RVAdapter<AddFriendModel>(null).apply {
            setOnProvideLayoutIdListener(object : RVAdapter.OnProvideLayoutIdListener {
                override fun onProvideLayoutId(viewType: Int): Int =
                    when (viewType) {
                        AddFriendModel.TITLE -> R.layout.item_view_add_friend_title
                        AddFriendModel.CONTENT -> R.layout.item_view_add_friend_content
                        AddFriendModel.NO_USER -> R.layout.item_view_add_friend_no_data
                        else -> 0
                    }
            })
            setOnBindViewListener(object : RVAdapter.OnBindViewListener<AddFriendModel> {
                override fun onBindView(
                    data: AddFriendModel,
                    holder: RVViewHolder,
                    itemType: Int,
                    position: Int
                ) {
                    when (itemType) {
                        AddFriendModel.TITLE -> {
                            holder.findViewOften<TextView>(R.id.tvTitle).text = data.title
                        }
                        AddFriendModel.CONTENT -> {
                            holder.setImage(R.id.ivAvatar, data.avatarUrl)
                            holder.findViewOften<ImageView>(R.id.ivSex)
                                .setImageResource(if (data.sex) R.drawable.img_boy_icon else R.drawable.img_girl_icon)
                            holder.findViewOften<TextView>(R.id.tvNickName).text = data.nickName
                            holder.findViewOften<TextView>(R.id.tvAge).text = data.age.toString()
                            holder.findViewOften<TextView>(R.id.tvDesc).text = data.desc
                        }
                    }
                }
            })
            setOnItemClickListener(object : RVAdapter.OnItemClickListener<AddFriendModel> {
                override fun onItemClick(data: AddFriendModel) {

                }
            })
        }
    }

    override fun actionBarTitle(): String? {
        val currentUer = BmobManager.getCurrentUser()
        currentUer?.let {
            return if (it.sex) getString(R.string.text_gentle_girl)
            else getString(R.string.text_sunshine_boy)
        }
        return null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivSearch -> doSearch()
            R.id.tvToContact -> toContactFriendActivity()
        }
    }

    private fun toContactFriendActivity() {
        PermissionUtils.permissions(android.Manifest.permission.READ_CONTACTS)
            .resultCallback(object : OnPermissionCallback {
                override fun onPermissionGranted(grantedPermissions: List<String>) {
                    startActivity<ContactFriendActivity>()
                }

                override fun onPermissionDenied(
                    grantedPermissions: List<String>,
                    deniedPermissions: List<String>,
                    foreverDeniedPermissions: List<String>
                ) {
                }
            }).request()
    }

    private fun doSearch() {
        // 隐藏软键盘
        hideSoftInput(this)

        // 1.获取电话号码
        val phone = etMobilePhone.text.toString().trim()
        if (phone.isBlank()) shortToast(R.string.text_please_input_mobile_phone)

        // 2.过滤自己
        val currentUserPhone: String? = BmobManager.getCurrentUser()?.mobilePhoneNumber
        if (phone == currentUserPhone) return

        // 3.查询用户
        val dialog = showLoadingDialog(this, false, getString(R.string.text_searching))
        BmobManager.queryUserByPhone(phone, object : FindListener<IMUser>() {
            @Suppress("UNCHECKED_CAST")
            override fun done(datas: MutableList<IMUser>?, e: BmobException?) {

                fun hideRvShowEmptyErrorView() {
                    rv.visibility = View.GONE
                    emptyErrorView.visibility = View.VISIBLE
                }

                if (e != null) {
                    dialog.dismiss()
                    hideRvShowEmptyErrorView()
                    emptyErrorView.showErrorView()
                    return
                }

                // 显示查询结果
                val adapter: RVAdapter<AddFriendModel> = rv.adapter as RVAdapter<AddFriendModel>
                val models = mutableListOf<AddFriendModel>()
                models.add(provideTitleModel(getString(R.string.text_search_result)))
                if (datas.isNullOrEmpty()) {
                    // 显示【未查询到用户】
                    models.add(provideNoDataModel())

                    // 显示推荐列表
                    BmobManager.queryAllUser(object : FindListener<IMUser>() {
                        override fun done(users: MutableList<IMUser>?, e: BmobException?) {
                            dialog.dismiss()
                            if (!users.isNullOrEmpty()) {
                                models.add(provideTitleModel(getString(R.string.text_recommend_friend)))
                                for ((index, user) in users.withIndex()) {
                                    if (user.mobilePhoneNumber == currentUserPhone) continue
                                    if (index >= 100) break
                                    models.add(provideContentModel(user))
                                }
                                adapter.datas = models
                                adapter.notifyDataSetChanged()
                            } else if (e == null) {
                                // 未发生错误显示空View
                                hideRvShowEmptyErrorView()
                                emptyErrorView.showEmptyView()
                            } else {
                                // 发生错误显示错误View
                                hideRvShowEmptyErrorView()
                                emptyErrorView.showErrorView()
                            }
                        }
                    })
                    return
                }

                // 显示查询结果列表
                for (data in datas) {
                    models.add(provideContentModel(data))
                }
                // 显示推荐列表
                BmobManager.queryAllUser(object : FindListener<IMUser>() {
                    override fun done(users: MutableList<IMUser>?, e: BmobException?) {
                        dialog.dismiss()
                        if (!users.isNullOrEmpty()) {
                            models.add(provideTitleModel(getString(R.string.text_recommend_friend)))
                            for ((index, user) in users.withIndex()) {
                                if (user.mobilePhoneNumber == currentUserPhone) continue
                                if (index >= 100) break
                                models.add(provideContentModel(user))
                            }
                        }
                        if (e != null) {
                            hideRvShowEmptyErrorView()
                            emptyErrorView.showErrorView()
                            return
                        }
                        adapter.datas = models
                        adapter.notifyDataSetChanged()
                    }
                })
            }
        })
    }

    private fun provideNoDataModel(): AddFriendModel =
        AddFriendModel(viewType = AddFriendModel.NO_USER)

    private fun provideTitleModel(title: String): AddFriendModel =
        AddFriendModel(title, viewType = AddFriendModel.TITLE)

    private fun provideContentModel(user: IMUser): AddFriendModel = AddFriendModel().apply {
        user.nickName?.let { nickName = it }
        avatarUrl = user.photo
        sex = user.sex
        age = user.age
        desc = user.desc
        userId = user.objectId
        viewType = AddFriendModel.CONTENT
    }
}