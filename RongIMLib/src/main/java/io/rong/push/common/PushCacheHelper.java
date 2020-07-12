//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushType;
import io.rong.push.core.PushUtils;

public class PushCacheHelper {
    public final String APP_PUSH_INFORMATION = "RongPushAppConfig";
    private final String TIME = "navigation_time";
    private final String ADDRESS_LIST = "addressList";
    private final String DEVICE_ID = "deviceId";
    private final String IS_CONFIG_DONE = "isConfig";
    private final String PUSH_TYPE_CONFIGED = "pushType";
    private final String ENABLED_PUSH_TYPES = "enabledPushTypes";
    private final String PUSH_DOMAIN = "pushDomain";
    private final String TOKEN = "token";
    private final long EXPIRED_TIME = 7200000L;

    public PushCacheHelper() {
    }

    public static PushCacheHelper getInstance() {
        return Singleton.sInstance;
    }

    public boolean isCacheValid(Context context, String appKey) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        String cachedIP = sp.getString("addressList", "");
        long cachedTime = sp.getLong("navigation_time", -1L);
        long currentTime = System.currentTimeMillis();
        return currentTime - cachedTime < 7200000L && !TextUtils.isEmpty(cachedIP) && this.getCachedDeviceId(context).equals(DeviceUtils.getDeviceId(context, appKey));
    }

    public ArrayList<String> getCachedAddressList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        ArrayList<String> dataList = new ArrayList();
        String strJson = preferences.getString("addressList", "");
        if (TextUtils.isEmpty(strJson)) {
            return dataList;
        } else {
            Gson gson = new Gson();
            dataList = (ArrayList)gson.fromJson(strJson, (new TypeToken<List<String>>() {
            }).getType());
            return dataList;
        }
    }

    public void saveAllAddress(Context context, ArrayList<String> addressList, long time) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(addressList);
        editor.putString("addressList", jsonString);
        editor.putLong("navigation_time", time);
        editor.commit();
    }

    public void savePushConfigInfo(Context context, String type) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        editor.putBoolean("isConfig", true);
        editor.putString("pushType", type);
        editor.commit();
    }

    public boolean isConfigDone(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        return sp.getBoolean("isConfig", false);
    }

    public PushType getConfigPushType(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        String cachedName = sp.getString("pushType", PushType.RONG.getName());
        return PushType.getType(cachedName);
    }

    public void saveEnablePushTypes(Context context, String info) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        editor.putString("enabledPushTypes", info).commit();
    }

    public String getCachedEnablePushTypes(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        return sp.getString("enabledPushTypes", "");
    }

    public void savePushDomain(Context context, String domain) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        editor.putString("pushDomain", domain);
        editor.apply();
    }

    public String getCachedPushDomain(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        return sp.getString("pushDomain", PushUtils.getDefaultNavi());
    }

    public void cacheRongDeviceId(Context context, String deviceId) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        editor.putString("deviceId", deviceId);
        editor.apply();
    }

    public String getCachedDeviceId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        return sp.getString("deviceId", "");
    }

    public void clearPushConfigInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        editor.remove("isConfig");
        editor.remove("pushType");
        editor.commit();
    }

    public void saveTokenInfo(Context context, String tokenInfo) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = preferences.edit();
        editor.putString("token", tokenInfo);
        editor.apply();
    }

    public String getCachedTokenInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        return preferences.getString("token", "");
    }

    public void clearAll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RongPushAppConfig", 0);
        preferences.edit().clear().apply();
    }

    public void setPushContentShowStatus(Context context, boolean isShowDetail) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = sp.edit();
        editor.putBoolean("isShowDetail", isShowDetail);
        editor.apply();
    }

    public boolean getPushContentShowStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        return sp.getBoolean("isShowDetail", true);
    }

    public void setPushServerInIMToken(Context context, String serverInfo) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        Editor editor = sp.edit();
        editor.putString("domainInIMToken", serverInfo);
        editor.apply();
    }

    public String getPushServerInfoInIMToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("RongPushAppConfig", 0);
        return sp.getString("domainInIMToken", "");
    }

    private static class Singleton {
        static PushCacheHelper sInstance = new PushCacheHelper();

        private Singleton() {
        }
    }
}
