package com.johnny.meet_kotlin.activities

import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.johnny.meet_kotlin.model.AddFriendModel
import kotlinx.android.synthetic.main.activity_contact_friend.*

/**
 * 从通讯录导入联系人
 * 
 * @author Johnny
 */
class ContactFriendActivity : BaseUIActivity() {

    private val models = mutableListOf<AddFriendModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_friend)
        setupView()
        loadUsers()
    }

    private fun setupView() {
        contactRv.layoutManager = LinearLayoutManager(this)
        contactRv.adapter = RVAdapter<AddFriendModel>(this.models).apply {
            setOnProvideLayoutIdListener(object : RVAdapter.OnProvideLayoutIdListener {
                override fun onProvideLayoutId(viewType: Int): Int =
                    R.layout.item_view_add_friend_content
            })
            setOnBindViewListener(object : RVAdapter.OnBindViewListener<AddFriendModel> {
                override fun onBindView(
                    data: AddFriendModel,
                    holder: RVViewHolder,
                    itemType: Int,
                    position: Int
                ) {
                    holder.getViewById<TextView>(R.id.tvNickName).text = data.nickName
                    holder.getViewById<TextView>(R.id.tvAge).text = data.age.toString()
                    holder.getViewById<ViewGroup>(R.id.llContactInfo).visibility = View.VISIBLE
                    holder.getViewById<TextView>(R.id.tvContactName).text = data.contactName
                    holder.getViewById<TextView>(R.id.tvContactPhone).text = data.contactPhone
                    holder.getViewById<TextView>(R.id.tvDesc).text = data.desc
                    GlideHelper.loadUrl(
                        this@ContactFriendActivity,
                        holder.getViewById(R.id.ivAvatar),
                        data.avatarUrl
                    )
                }
            })
            setOnItemClickListener(object : RVAdapter.OnItemClickListener<AddFriendModel> {
                override fun onItemClick(data: AddFriendModel) {
                    UserInfoActivity.start(this@ContactFriendActivity, data.userId)
                }
            })
        }
    }

    private fun loadUsers() {
        val dialog = showLoadingDialog(this, false, getString(R.string.text_importing))
        // 从本地手机通讯录中查询联系人
        val localContacts = queryContactFromLocal()
        if (localContacts.isEmpty()) {
            contactRv.visibility = View.GONE
            emptyErrorView.visibility = View.VISIBLE
            emptyErrorView.showEmptyView()
            dialog.dismiss()
            return
        }
        var N = 0
        for ((name, phone) in localContacts) {
            // 根据通讯录中的手机号去后台查询相关用户显示
            BmobManager.queryUserByPhone(phone, object : FindListener<User>() {
                override fun done(users: MutableList<User>?, e: BmobException?) {
                    // 这里是主线程
                    N++
                    if (N == localContacts.size) {
                        dialog.dismiss()
                    }

                    if (e == null) {
                        if (!users.isNullOrEmpty()) {
                            contactRv.visibility = View.VISIBLE
                            emptyErrorView.visibility = View.GONE
                            notifyDataChange(users[0], name, phone)
                        }
                    } else {
                        // 出错了
                        contactRv.visibility = View.GONE
                        emptyErrorView.visibility = View.VISIBLE
                        emptyErrorView.showErrorView()
                    }
                }
            })
        }
    }

    private fun notifyDataChange(user: User, contactName: String, contactPhone: String) {
        val model = AddFriendModel()
        model.age = user.age
        model.avatarUrl = user.photo
        model.desc = user.desc
        user.nickName?.let { model.nickName = it }
        model.userId = user.objectId
        model.sex = user.sex

        model.isContact = true
        model.contactName = contactName
        model.contactPhone = contactPhone

        this.models.add(model)
        contactRv.adapter?.notifyDataSetChanged()
    }

    /**
     * 从本地手机通讯录中查询联系人
     */
    private fun queryContactFromLocal(): Map<String, String> {
        // 这步需要android.permission.READ_CONTACTS权限
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        val localContacts = mutableMapOf<String, String>()
        cursor?.let {
            var name: String
            var number: String
            while (it.moveToNext()) {
                name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                number =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                number = number.replace(" ", "").replace("-", "")
                localContacts[name] = number
            }
        }
        cursor?.close()
        return localContacts
    }

    override fun actionBarTitle(): String? = getString(R.string.text_to_contact)
}