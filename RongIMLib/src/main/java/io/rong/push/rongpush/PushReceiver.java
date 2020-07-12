//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.rongpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.rong.push.common.RLog;

public class PushReceiver extends BroadcastReceiver {
    private static final String TAG = "PushReceiver";

    public PushReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        RLog.d("PushReceiver", "onReceive intent = " + intent);
        if (intent != null && intent.getAction() != null) {
            try {
                Intent newIntent = new Intent(context, PushService.class);
                newIntent.setAction(intent.getAction());
                newIntent.putExtra("PING", intent.getStringExtra("PING"));
                PushService.enqueueWork(context, newIntent);
            } catch (SecurityException var4) {
                RLog.e("PushReceiver", "SecurityException. Failed to start PushService.");
            }

        } else {
            RLog.e("PushReceiver", "intent or action is null.");
        }
    }
}
