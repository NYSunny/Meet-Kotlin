//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.rongpush;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.common.DeviceUtils;

public class RongPushCacheHelper {
    private static final String PUSH_SHARE_PREFERENCE = "RongPush";
    public static final String DeviceId = "deviceId";
    private final String TIME = "navigation_time";
    private final String ADDRESS_LIST = "addressList";
    private final long EXPIRED_TIME = 7200000L;

    public RongPushCacheHelper() {
    }

    public static RongPushCacheHelper getInstance() {
        return Singleton.sInstance;
    }

    public boolean isCacheValid(Context context, String appKey) {
        SharedPreferences sp = context.getSharedPreferences("RongPush", 0);
        String cachedIP = sp.getString("addressList", "");
        long cachedTime = sp.getLong("navigation_time", -1L);
        long currentTime = System.currentTimeMillis();
        return currentTime - cachedTime < 7200000L && !TextUtils.isEmpty(cachedIP) && this.getCachedDeviceId(context).equals(DeviceUtils.getDeviceId(context, appKey));
    }

    public void cacheRongPushIPs(Context context, ArrayList<String> addressList, long time) {
        SharedPreferences preferences = context.getSharedPreferences("RongPush", 0);
        Editor editor = preferences.edit();
        Gson gson = new Gson();
        String jsonString = gson.toJson(addressList);
        editor.putString("addressList", jsonString);
        editor.putLong("navigation_time", time);
        editor.commit();
    }

    public ArrayList<String> getCachedAddressList(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RongPush", 0);
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

    private String getCachedDeviceId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("RongPush", 0);
        return preferences.getString("deviceId", "");
    }

    private static class Singleton {
        static RongPushCacheHelper sInstance = new RongPushCacheHelper();

        private Singleton() {
        }
    }
}
