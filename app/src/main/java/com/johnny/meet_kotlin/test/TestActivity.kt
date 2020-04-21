package com.johnny.meet_kotlin.test

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.johnny.base.utils.i
import com.johnny.base.utils.shortToast
import com.johnny.meet_kotlin.R
import kotlinx.android.synthetic.main.activity_test.*

/**
 * @author Johnny
 */
class TestActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        btnAdd.setOnClickListener(this)
        btnDelete.setOnClickListener(this)
        btnModify.setOnClickListener(this)
        btnQuery.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> add()
            R.id.btnDelete -> delete()
            R.id.btnModify -> modify()
            R.id.btnQuery -> query()
        }
    }

    private fun query() {
        BmobQuery<TestData>().getObject("aa0b397e31", object : QueryListener<TestData>() {
            override fun done(t: TestData?, e: BmobException?) {
                if (e == null) {
                    shortToast("查询成功")
                    i(t.toString())
                } else {
                    shortToast("查询失败")
                }
            }
        })
    }

    private fun modify() {
        TestData().apply {
            name = "李四"
            update("d1d4db68bd", object : UpdateListener() {
                override fun done(e: BmobException?) {
                    if (e == null) shortToast("修改成功")
                    else shortToast("修改失败")
                }
            })
        }
    }

    private fun delete() {
        TestData().delete("aa0b397e31", object : UpdateListener() {
            override fun done(e: BmobException?) {
                if (e == null) shortToast("删除成功")
                else shortToast("删除失败")
            }
        })
    }

    private fun add() {
        TestData("张三", 0).apply {
            save(object : SaveListener<String>() {
                override fun done(t: String?, e: BmobException?) {
                    if (e == null) {
                        shortToast("增加成功")
                        i("id = $t")
                    } else {
                        shortToast("增加失败")
                    }
                }
            })
        }
    }
}