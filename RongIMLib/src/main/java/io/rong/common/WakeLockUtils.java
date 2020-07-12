//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.SystemClock;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.imlib.HeartbeatReceiver;

public class WakeLockUtils {
    private static final String TAG = WakeLockUtils.class.getSimpleName();
    private static final long DEF_HEARTBEAT_TIMER = 150000L;
    private static final int HEARTBEAT_TIMER_MIN = 10000;
    private static long interval;

    public WakeLockUtils() {
    }

    public static void startNextHeartbeat(@NonNull Context context) {
        RLog.d(TAG, "startNextHeartbeat " + context.getPackageName());
        Intent heartbeatIntent = new Intent(context, HeartbeatReceiver.class);
        heartbeatIntent.setPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getBroadcast(context, 0, heartbeatIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        if (alarmManager == null) {
            RLog.e(TAG, "alarmManager is null");
        } else {
            Resources resources = context.getResources();
            if (interval <= 0L) {
                try {
                    String config_time = resources.getString(resources.getIdentifier("rc_heartbeat_timer", "string", context.getPackageName()));
                    interval = Long.parseLong(config_time);
                } catch (Exception var7) {
                    interval = 150000L;
                    RLog.e(TAG, "Read config file exception. Use default heartbeat time value.");
                }

                if (interval < 10000L) {
                    interval = 10000L;
                }
            }

            long triggerTime = SystemClock.elapsedRealtime() + interval;
            alarmManager.cancel(intent);
            FwLog.write(3, 1, LogTag.L_PING_S.getTag(), "interval|enabled", new Object[]{interval, true});
            if (VERSION.SDK_INT < 19) {
                alarmManager.set(2, triggerTime, intent);
            } else {
                alarmManager.setExact(2, triggerTime, intent);
            }

        }
    }

    public static void cancelHeartbeat(@NonNull Context context) {
        FwLog.write(3, 1, LogTag.L_PING_S.getTag(), "interval|enabled", new Object[]{interval, false});
        RLog.d(TAG, "cancelHeartbeat " + context.getPackageName());
        Intent heartbeatIntent = new Intent(context, HeartbeatReceiver.class);
        heartbeatIntent.setPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getBroadcast(context, 0, heartbeatIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        if (alarmManager != null) {
            alarmManager.cancel(intent);
        }

    }
}
