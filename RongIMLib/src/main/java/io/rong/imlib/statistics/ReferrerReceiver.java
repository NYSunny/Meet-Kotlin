//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.net.URLDecoder;

public class ReferrerReceiver extends BroadcastReceiver {
    private static String key = "referrer";

    public static String getReferrer(Context context) {
        return context.getSharedPreferences(key, 0).getString(key, (String)null);
    }

    public static void deleteReferrer(Context context) {
        context.getSharedPreferences(key, 0).edit().remove(key).commit();
    }

    public ReferrerReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        try {
            if (null != intent && intent.getAction().equals("com.android.vending.INSTALL_REFERRER")) {
                String rawReferrer = intent.getStringExtra(key);
                if (null != rawReferrer) {
                    String referrer = URLDecoder.decode(rawReferrer, "UTF-8");
                    Log.d("Statistics", "Referrer: " + referrer);
                    String[] parts = referrer.split("&");
                    String cid = null;
                    String uid = null;

                    for(int i = 0; i < parts.length; ++i) {
                        if (parts[i].startsWith("countly_cid")) {
                            cid = parts[i].replace("countly_cid=", "").trim();
                        }

                        if (parts[i].startsWith("countly_cuid")) {
                            uid = parts[i].replace("countly_cuid=", "").trim();
                        }
                    }

                    String res = "";
                    if (cid != null) {
                        res = res + "&campaign_id=" + cid;
                    }

                    if (uid != null) {
                        res = res + "&campaign_user=" + uid;
                    }

                    Log.d("Statistics", "Processed: " + res);
                    if (!res.equals("")) {
                        context.getSharedPreferences(key, 0).edit().putString(key, res).commit();
                    }
                }
            }
        } catch (Exception var9) {
            Log.d("Statistics", var9.toString());
        }

    }
}
