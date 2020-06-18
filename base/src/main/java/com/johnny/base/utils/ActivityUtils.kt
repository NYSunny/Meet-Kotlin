package com.johnny.base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.johnny.base.BaseApplication
import kotlin.properties.Delegates

/**
 * @author Johnny
 */

inline fun <reified T : Activity> Context.startActivity(noinline extrasCallback: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, T::class.java)
    extrasCallback?.let { intent.it() }
    startActivity(intent)
}

open class TransActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_HOLDER_KEY = "extra_holder_key"
        private val CALLBACK_MAP = hashMapOf<Int, TransActivityHolder>()

        fun start(
            context: Context? = null,
            activityHolder: TransActivityHolder,
            block: (Intent.() -> Unit)? = null
        ) {
            fun initIntent(intent: Intent) {
                val identityHashCode = System.identityHashCode(intent)
                CALLBACK_MAP[identityHashCode] = activityHolder
                intent.putExtra(EXTRA_HOLDER_KEY, identityHashCode)
                block?.let { it(intent) }
            }
            context?.startActivity<TransActivity> {
                initIntent(this)
            } ?: getApp().startActivity<TransActivity> {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                initIntent(this)
            }
        }
    }

    private var activityHolder: TransActivityHolder? = null
    private var holderKey: Int by Delegates.notNull()

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        holderKey = intent.getIntExtra(EXTRA_HOLDER_KEY, 0)
        if (holderKey == 0) {
            super.onCreate(savedInstanceState)
            finish()
            return
        }
        activityHolder = CALLBACK_MAP[holderKey]
        activityHolder?.onCreateBefore(this, savedInstanceState)
        super.onCreate(savedInstanceState)
        activityHolder?.onCreated(this, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        activityHolder?.onStarted(this)
    }

    override fun onResume() {
        super.onResume()
        activityHolder?.onResumed(this)
    }

    override fun onPause() {
        super.onPause()
        activityHolder?.onPaused(this)
    }

    override fun onStop() {
        super.onStop()
        activityHolder?.onStopped(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        CALLBACK_MAP.remove(this.holderKey)
        activityHolder?.onDestroy(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityHolder?.onSaveInstanceState(this, outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        activityHolder?.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityHolder?.onActivityResult(this, requestCode, resultCode, data)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (activityHolder != null && activityHolder!!.dispatchTouchEvent(this, ev)) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }

    open class TransActivityHolder {
        open fun onCreateBefore(activity: TransActivity, savedInstanceState: Bundle?) {}

        open fun onCreated(activity: TransActivity, savedInstanceState: Bundle?) {}

        open fun onStarted(activity: TransActivity) {}

        open fun onResumed(activity: TransActivity) {}

        open fun onPaused(activity: TransActivity) {}

        open fun onStopped(activity: TransActivity) {}

        open fun onDestroy(activity: TransActivity) {}

        open fun onSaveInstanceState(activity: TransActivity, outState: Bundle?) {}

        open fun onRequestPermissionsResult(
            activity: TransActivity,
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
        }

        open fun onActivityResult(
            activity: TransActivity,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
        }

        open fun dispatchTouchEvent(activity: TransActivity, event: MotionEvent?): Boolean = false
    }
}

