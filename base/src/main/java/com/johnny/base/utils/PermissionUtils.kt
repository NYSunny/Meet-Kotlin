package com.johnny.base.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 功能：
 * 1.支持单个权限申请
 * 2.支持多个权限同时申请
 * 3.支持权限组的申请
 * 4.提供给调用者对被拒绝的权限有解释的机会
 * 5.所有权限都允许的情况下成功回调，至少一个权限不被允许失败回调
 *
 * @author Johnny
 */

const val CALENDAR = "CALENDAR"
const val CAMERA = "CAMERA"
const val CONTACTS = "CONTACTS"
const val LOCATION = "LOCATION"
const val MICROPHONE = "MICROPHONE"
const val PHONE = "PHONE"
const val SENSORS = "SENSORS"
const val SMS = "SMS"
const val STORAGE = "STORAGE"

private val GROUP_CALENDAR = arrayOf(
    Manifest.permission.READ_CALENDAR,
    Manifest.permission.WRITE_CALENDAR
)

private val GROUP_CAMERA = arrayOf(Manifest.permission.CAMERA)

private val GROUP_CONTACTS = arrayOf(
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.WRITE_CONTACTS,
    Manifest.permission.GET_ACCOUNTS
)

private val GROUP_LOCATION = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION
)

private val GROUP_MICROPHONE = arrayOf(Manifest.permission.RECORD_AUDIO)

@RequiresApi(Build.VERSION_CODES.O)
private val GROUP_PHONE = arrayOf(
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.READ_PHONE_NUMBERS,
    Manifest.permission.CALL_PHONE,
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.WRITE_CALL_LOG,
    Manifest.permission.ADD_VOICEMAIL,
    Manifest.permission.USE_SIP,
    Manifest.permission.PROCESS_OUTGOING_CALLS,
    Manifest.permission.ANSWER_PHONE_CALLS
)

private val GROUP_PHONE_BELOW_O = arrayOf(
    Manifest.permission.READ_PHONE_STATE,
    Manifest.permission.CALL_PHONE,
    Manifest.permission.READ_CALL_LOG,
    Manifest.permission.WRITE_CALL_LOG,
    Manifest.permission.ADD_VOICEMAIL,
    Manifest.permission.USE_SIP,
    Manifest.permission.PROCESS_OUTGOING_CALLS
)

@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
private val GROUP_SENSORS = arrayOf(Manifest.permission.BODY_SENSORS)

private val GROUP_SMS = arrayOf(
    Manifest.permission.SEND_SMS,
    Manifest.permission.RECEIVE_SMS,
    Manifest.permission.READ_SMS,
    Manifest.permission.RECEIVE_WAP_PUSH,
    Manifest.permission.RECEIVE_MMS
)

private val GROUP_STORAGE = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

private fun getPermissions(permission: String): Array<String> =
    when (permission) {
        CALENDAR -> GROUP_CALENDAR
        CAMERA -> GROUP_CAMERA
        CONTACTS -> GROUP_CONTACTS
        LOCATION -> GROUP_LOCATION
        MICROPHONE -> GROUP_MICROPHONE
        PHONE -> if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) GROUP_PHONE_BELOW_O else GROUP_PHONE
        SENSORS -> GROUP_SENSORS
        SMS -> GROUP_SMS
        STORAGE -> GROUP_STORAGE
        else -> arrayOf(permission)
    }

fun getManifestPermissions(packageName: String = getApp().packageName): Array<String> {
    try {
        return getApp().packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        ).requestedPermissions
            ?: return emptyArray()
    } catch (_: PackageManager.NameNotFoundException) {
        return emptyArray()
    }
}

class PermissionUtils private constructor(private val permissionParams: MutableList<String> = mutableListOf()) {

    /* 需要请求的权限列表 */
    val permissionsRequest: MutableList<String> = mutableListOf()

    /* 已经被允许的权限列表 */
    private val permissionsGrant: MutableList<String> = mutableListOf()

    /* 已经被拒绝的权限列表（包含被永久拒绝的权限列表） */
    private val permissionsDenied: MutableList<String> = mutableListOf()

    /* 被永久拒绝的权限列表 */
    private val permissionsDeniedForever: MutableList<String> = mutableListOf()

    private var mOnRationaleCallback: OnRationaleCallback? = null
    private var mOnPermissionCallback: OnPermissionCallback? = null

    companion object {

        lateinit var INSTANCE: PermissionUtils

        fun permissions(vararg permissionParams: String): PermissionUtils =
            PermissionUtils(mutableListOf(*permissionParams))
    }

    init {
        INSTANCE = this
    }

    fun rationaleCallback(onRationaleCallback: OnRationaleCallback): PermissionUtils {
        this.mOnRationaleCallback = onRationaleCallback
        return this
    }

    fun resultCallback(onPermissionCallback: OnPermissionCallback): PermissionUtils {
        this.mOnPermissionCallback = onPermissionCallback
        return this
    }

    /**
     * 请求权限
     */
    fun request() {
        clearCache()
        if (this.permissionParams.isEmpty()) return
        // 请求的权限列表，权限状态：第一次请求、被拒绝、永久拒绝、已授权的
        val permissions = mutableListOf<String>()
        val manifestPermissions = getManifestPermissions()
        for (param in this.permissionParams) {
            val permissionsByParam = getPermissions(param)
            for (p in permissionsByParam) {
                if (manifestPermissions.contains(p)) {
                    permissions.add(p)
                } else {
                    this.permissionsDenied.add(p)
                    e("PermissionUtils", "You should add the permission of $p in manifest.")
                }
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 低于android 6.0的版本，在AndroidManifest.xml中配置的权限默认全部授权
            this.permissionsGrant.addAll(permissions)
            resultCallback()
        } else {
            for (permission in permissions) {
                if (isGranted(permission)) /*已授权的权限*/ this.permissionsGrant.add(permission)
                else /*需要请求的权限*/ this.permissionsRequest.add(permission)
            }
            if (this.permissionsRequest.isEmpty()) resultCallback()
            else startPermissionActivity()
        }
    }

    fun shouldRationale(activity: Activity, requestPermission: () -> Unit): Boolean {
        if (this.mOnRationaleCallback == null) return false
        // 对所有请求的权限检查是否被拒绝过
        val deniedPermissions = mutableListOf<String>()
        for (permission in this.permissionsRequest) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                deniedPermissions.add(permission)
            }
        }
        return if (deniedPermissions.isNotEmpty()) {
            this.mOnRationaleCallback?.onRationaleCallback(
                activity,
                deniedPermissions,
                object : OnRationaleCallback.OnShouldRequestCallback {
                    override fun onShouldRequestCallback(shouldRequest: Boolean) {
                        if (shouldRequest) requestPermission()
                        else {
                            activity.finish()
                            this@PermissionUtils.permissionsDenied.addAll(deniedPermissions)
                            resultCallback()
                        }
                    }
                })
            true
        } else false
    }

    fun onRequestPermissionsResult(activity: Activity) {
        getPermissionsStatus(activity)
        resultCallback()
    }

    private fun getPermissionsStatus(activity: Activity) {
        if (this.permissionsRequest.isNotEmpty()) {
            for (permission in this.permissionsRequest) {
                if (isGranted(permission)) {
                    this.permissionsGrant.add(permission)
                } else {
                    this.permissionsDenied.add(permission)
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permission
                        )
                    ) {
                        this.permissionsDeniedForever.add(permission)
                    }
                }
            }
        }
    }

    private fun clearCache() {
        this.permissionsGrant.clear()
        this.permissionsDenied.clear()
        this.permissionsDeniedForever.clear()
        this.permissionsRequest.clear()
    }

    private fun isGranted(permission: String): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                ContextCompat.checkSelfPermission(
                    getApp(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED

    private fun resultCallback() {
        if (this.mOnPermissionCallback == null) return
        // 全部权限允许后回调
        if (this.permissionsDenied.isEmpty()) {
            this.mOnPermissionCallback?.onPermissionGranted(this.permissionsGrant)
        } else {
            // 至少有一个权限被禁止回调
            this.mOnPermissionCallback?.onPermissionDenied(
                this.permissionsGrant,
                this.permissionsDenied,
                this.permissionsDeniedForever
            )
        }
    }

    private fun startPermissionActivity() =
        PermissionActivityHolderImpl.start(PermissionActivityHolderImpl.TYPE_RUNTIME)
}

class PermissionActivityHolderImpl private constructor() : TransActivity.TransActivityHolder() {

    companion object {
        private const val TYPE = "TYPE"
        const val TYPE_RUNTIME = 0x01
        private const val REQUEST_PERMISSION_CODE = 1001
        private val INSTANCE = PermissionActivityHolderImpl()

        fun start(type: Int) {
            TransActivity.start(activityHolder = INSTANCE) {
                putExtra(TYPE, type)
            }
        }
    }

    override fun onCreated(activity: TransActivity, savedInstanceState: Bundle?) {
        when (activity.intent.getIntExtra(TYPE, 0)) {
            TYPE_RUNTIME -> {
                if (PermissionUtils.INSTANCE.shouldRationale(activity) {
                        requestPermissions(activity)
                    }) return
                requestPermissions(activity)
            }
        }
    }

    override fun onRequestPermissionsResult(
        activity: TransActivity,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        activity.finish()
        PermissionUtils.INSTANCE.onRequestPermissionsResult(activity)
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, PermissionUtils.INSTANCE.permissionsRequest.toTypedArray(),
            REQUEST_PERMISSION_CODE
        )
    }
}

interface OnRationaleCallback {
    fun onRationaleCallback(
        context: Context,
        deniedPermissions: List<String>,
        shouldRequest: OnShouldRequestCallback
    )

    interface OnShouldRequestCallback {
        fun onShouldRequestCallback(shouldRequest: Boolean)
    }
}

interface OnPermissionCallback {

    fun onPermissionGranted(grantedPermissions: List<String>)

    fun onPermissionDenied(
        grantedPermissions: List<String>,
        deniedPermissions: List<String>,
        foreverDeniedPermissions: List<String>
    )
}