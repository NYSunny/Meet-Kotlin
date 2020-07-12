//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.rongpush;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.rong.imlib.RongJobIntentService;
import io.rong.push.common.RLog;
import io.rong.push.rongpush.PushConnectivityManager.NetworkType;

public class PushService extends RongJobIntentService {
    private static final String TAG = PushService.class.getSimpleName();
    private static final int UNIQUE_JOB_ID = 2017113004;
    private PushReceiver pushReceiver;

    public PushService() {
    }

    public void onCreate() {
        super.onCreate();

        try {
            this.pushReceiver = new PushReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.registerReceiver(this.pushReceiver, filter);
        } catch (Exception var2) {
            RLog.d(TAG, "Failed to register push receiver.");
        }

        RLog.d(TAG, "OnCreate");
    }

    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        RLog.d(TAG, "onStartCommand, intent " + intent);
        return this.handleWork(intent);
    }

    protected void onHandleWork(@NonNull Intent intent) {
        this.handleWork(intent);
    }

    public void onDestroy() {
        RLog.d(TAG, "onDestroy");
        super.onDestroy();

        try {
            this.unregisterReceiver(this.pushReceiver);
        } catch (Exception var2) {
            RLog.d(TAG, "Failed to unregister push receiver.");
        }

    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, PushService.class, 2017113004, intent);
    }

    private void initWithCachedParams() {
        SharedPreferences sp = this.getSharedPreferences("RongPush", 0);
        String appKey = sp.getString("appKey", "");
        String deviceId = sp.getString("deviceId", "");
        String pushDomain = sp.getString("pushDomain", "");
        if (!TextUtils.isEmpty(appKey) && !TextUtils.isEmpty(deviceId) && !PushConnectivityManager.getInstance().isInitialized()) {
            PushConnectivityManager.getInstance().init(this, deviceId, appKey);
            if (!TextUtils.isEmpty(pushDomain)) {
                PushConnectivityManager.getInstance().setServerDomain(pushDomain);
            }
        }

    }

    private void initAndUpdateWithIntentParams(Intent intent) {
        String appKey = intent.getStringExtra("appKey");
        String deviceId = intent.getStringExtra("deviceId");
        String pushDomain = intent.getStringExtra("pushDomain");
        SharedPreferences sp = this.getSharedPreferences("RongPush", 0);
        String cachedAppKey = sp.getString("appKey", "");
        String cachedDeviceId = sp.getString("deviceId", "");
        String cachedPushDomain = sp.getString("pushDomain", "");
        PushConnectivityManager.getInstance().init(this, deviceId, appKey);
        PushConnectivityManager.getInstance().setServerDomain(pushDomain);
        Editor editor = sp.edit();
        if (!cachedPushDomain.equals(pushDomain)) {
            editor.putString("pushDomain", pushDomain);
        }

        if (!cachedAppKey.equals(appKey) || !cachedDeviceId.equals(deviceId)) {
            editor.putString("appKey", appKey);
            editor.putString("deviceId", deviceId);
            RLog.d(TAG, "update cached values.");
        }

        editor.commit();
    }

    private int handleWork(Intent intent) {
        SharedPreferences sp = this.getSharedPreferences("RongPush", 0);
        Editor editor = sp.edit();
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("io.rong.push.intent.action.INIT")) {
                if (!PushConnectivityManager.getInstance().isInitialized()) {
                    this.initAndUpdateWithIntentParams(intent);
                }

                PushConnectivityManager.getInstance().connect();
            } else {
                if (!PushConnectivityManager.getInstance().isInitialized()) {
                    this.initWithCachedParams();
                }

                String extra;
                if (intent.getAction().equals("io.rong.push.intent.action.REGISTRATION_INFO")) {
                    extra = sp.getString("pushTypeUsed", "");
                    String info = intent.getStringExtra("regInfo");
                    if (!TextUtils.isEmpty(info)) {
                        String[] infos = info.split("\\|");
                        RLog.i(TAG, "received info:" + info + ",pushType cached:" + extra);
                        if (infos[0].equals(extra)) {
                            PushConnectivityManager.getInstance().sendRegistrationIDToServer(info);
                        } else {
                            RLog.e(TAG, "Push type received is different from the one cached. So ignore this event.");
                        }
                    } else {
                        RLog.e(TAG, "regInfo value error. So ignore this event.");
                    }
                } else if (intent.getAction().equals("io.rong.push.intent.action.REDIRECT")) {
                    editor.remove("navigation_ip_value");
                    editor.remove("navigation_time");
                    editor.commit();
                    PushConnectivityManager.getInstance().disconnect();
                    PushConnectivityManager.getInstance().connect();
                } else if (intent.getAction().equals("io.rong.push.intent.action.HEART_BEAT")) {
                    extra = intent.getStringExtra("PING");
                    if (extra == null) {
                        PushConnectivityManager.getInstance().ping();
                    } else if (extra.equals("PING")) {
                        PushConnectivityManager.getInstance().onPingTimeout();
                    }
                } else if (intent.getAction().equals("io.rong.push.intent.action.STOP_PUSH")) {
                    PushConnectivityManager.getInstance().disconnect();
                    this.stopSelf();
                } else if (intent.getAction().equals("io.rong.push.intent.action.UNINIT")) {
                    editor.clear().commit();
                    PushConnectivityManager.getInstance().uninit();
                    this.stopSelf();
                } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                    NetworkType last = PushConnectivityManager.getInstance().getNetworkType();
                    ConnectivityManager ConnManager = (ConnectivityManager)this.getSystemService("connectivity");
                    State wifi_state;
                    if (ConnManager.getNetworkInfo(1) == null) {
                        wifi_state = null;
                    } else {
                        wifi_state = ConnManager.getNetworkInfo(1).getState();
                    }

                    State mobile_state;
                    if (ConnManager.getNetworkInfo(0) == null) {
                        mobile_state = null;
                    } else {
                        mobile_state = ConnManager.getNetworkInfo(0).getState();
                    }

                    if (wifi_state != null && wifi_state == State.CONNECTED) {
                        PushConnectivityManager.getInstance().setNetworkType(NetworkType.WIFI);
                    } else if (mobile_state != null && mobile_state == State.CONNECTED) {
                        PushConnectivityManager.getInstance().setNetworkType(NetworkType.MOBILE);
                    } else {
                        PushConnectivityManager.getInstance().setNetworkType(NetworkType.ERROR);
                    }

                    NetworkType current = PushConnectivityManager.getInstance().getNetworkType();
                    RLog.d(TAG, "wifi = " + wifi_state + ", mobile = " + mobile_state + ", last = " + last + ", current = " + current);
                    if (current.equals(NetworkType.ERROR)) {
                        PushConnectivityManager.getInstance().disconnect();
                    } else if (!last.equals(NetworkType.ERROR) && !last.equals(NetworkType.NONE)) {
                        PushConnectivityManager.getInstance().disconnect();
                        PushConnectivityManager.getInstance().connect();
                    } else {
                        PushConnectivityManager.getInstance().connect();
                    }
                } else if ("android.intent.action.USER_PRESENT".equals(intent.getAction()) || "android.intent.action.ACTION_POWER_CONNECTED".equals(intent.getAction()) || "android.intent.action.ACTION_POWER_DISCONNECTED".equals(intent.getAction()) || "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                    PushConnectivityManager.getInstance().connect();
                }
            }

            return 1;
        } else {
            RLog.d(TAG, "intent is null.");
            if (!PushConnectivityManager.getInstance().isInitialized()) {
                this.initWithCachedParams();
            }

            PushConnectivityManager.getInstance().connect();
            return 1;
        }
    }
}
