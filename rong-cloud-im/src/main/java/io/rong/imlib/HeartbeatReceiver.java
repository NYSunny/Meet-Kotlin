//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import io.rong.common.RLog;

public class HeartbeatReceiver extends BroadcastReceiver {
    private static final String TAG = "HeartbeatReceiver";

    public HeartbeatReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        RLog.d("HeartbeatReceiver", "onReceive: " + (intent != null ? intent.toString() : "null"));
        NativeClient.getInstance().ping(context);
    }
}
