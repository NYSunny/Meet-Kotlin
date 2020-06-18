package com.johnny.meet_kotlin.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.bmob.v3.BmobObject
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.SaveListener
import cn.bmob.v3.listener.UpdateListener
import com.johnny.base.utils.PermissionUtils
import com.johnny.base.utils.i
import com.johnny.base.utils.shortToast
import com.johnny.meet_kotlin.R
import kotlinx.android.synthetic.main.activity_test.*
import java.lang.Exception

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
        btnStartService.setOnClickListener(this)
        btnStopService.setOnClickListener(this)
        btnANR.setOnClickListener(this)
        btnNextClick.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> add()
            R.id.btnDelete -> delete()
            R.id.btnModify -> modify()
            R.id.btnQuery -> query()
            R.id.btnStartService -> startSer()
            R.id.btnStopService -> stopSer()
            R.id.btnANR -> doANR()
            R.id.btnNextClick -> doClick()
        }
    }

    private fun stopSer() {
        stopService(Intent(this, TestService::class.java))
    }

    private fun startSer() {
        startService(Intent(this, TestService::class.java))
    }

    private fun doClick() {
        Toast.makeText(this, "doClick", Toast.LENGTH_SHORT).show()
    }

    private fun doANR() {
        // 主线程休眠5s，触发anr
        try {
            Thread.sleep(10000)
        } catch (_: Exception) {
            // ignore
        }
    }

    private fun query() {
        BmobQuery<TestData>().getObject("aa0b397e31", object : QueryListener<TestData>() {
            override fun done(t: TestData?, e: BmobException?) {
                if (e == null) {
                    shortToast("查询成功")
                    i(msg = t.toString())
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
                        i(msg = "id = $t")
                    } else {
                        shortToast("增加失败")
                    }
                }
            })
        }
    }
}