//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import io.rong.common.RLog;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.common.NetUtils;

public class ConnectChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "ConnectChangeReceiver";
    public static final String RECONNECT_ACTION = "action_reconnect";

    public ConnectChangeReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null) {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            if (cm == null) {
                RLog.e("ConnectChangeReceiver", "ConnectivityManager is null");
            } else {
                NetworkInfo networkInfo = null;

                try {
                    RLog.d("ConnectChangeReceiver", "intent : " + intent.toString());
                    networkInfo = cm.getActiveNetworkInfo();
                    RLog.d("ConnectChangeReceiver", "network : " + (networkInfo != null ? networkInfo.isAvailable() + " " + networkInfo.isConnected() : "null"));
                } catch (Exception var7) {
                    RLog.e("ConnectChangeReceiver", "getActiveNetworkInfo Exception", var7);
                }

                boolean networkAvailable = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
                NetUtils.updateCacheNetworkAvailable(networkAvailable);
                FwLog.write(3, 1, LogTag.L_NETWORK_CHANGED_S.getTag(), "network|available", new Object[]{DeviceUtils.getNetworkType(context), networkAvailable});
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE") && networkAvailable) {
                    RongIMClient.reconnectServer();
                } else if (intent.getAction().equals("action_reconnect") && networkAvailable) {
                    RongIMClient.reconnectServer();
                } else {
                    ConnectionStatus state;
                    if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
                        state = RongIMClient.getInstance().getCurrentConnectionStatus();
                        if (state.equals(ConnectionStatus.NETWORK_UNAVAILABLE) && networkAvailable) {
                            RongIMClient.reconnectServer();
                        }
                    } else if (intent.getAction().equals("action_reconnect") && networkInfo != null && networkInfo.isAvailable() && !networkInfo.isConnected()) {
                        state = RongIMClient.getInstance().getCurrentConnectionStatus();
                        if (state.equals(ConnectionStatus.NETWORK_UNAVAILABLE) && networkAvailable) {
                            RongIMClient.reconnectServer();
                        }
                    } else {
                        RLog.e("ConnectChangeReceiver", "Network exception, NetworkInfo = " + (networkInfo != null ? networkInfo.isAvailable() + ", " + networkInfo.isConnected() : "null"));
                    }
                }

            }
        }
    }
}
