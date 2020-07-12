//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.rong.push.PushManager;
import io.rong.push.common.RLog;

public class PushConfigReceiver extends BroadcastReceiver {
    private final String TAG = PushConfigReceiver.class.getSimpleName();

    public PushConfigReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo networkInfo = null;

            try {
                RLog.d(this.TAG, "intent : " + intent.toString());
                networkInfo = cm.getActiveNetworkInfo();
                RLog.d(this.TAG, "network : " + (networkInfo != null ? networkInfo.isAvailable() + " " + networkInfo.isConnected() : "null"));
            } catch (Exception var6) {
                RLog.e(this.TAG, "getActiveNetworkInfo Exception");
                var6.printStackTrace();
            }

            boolean networkAvailable = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            if ((intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") || intent.getAction().equals("android.intent.action.USER_PRESENT")) && networkAvailable) {
                PushManager.getInstance().onNetworkReconfigEvent(context);
            }

        }
    }
}
