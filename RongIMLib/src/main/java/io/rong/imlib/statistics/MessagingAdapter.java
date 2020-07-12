//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;

import io.rong.imlib.statistics.DeviceId.Type;

public class MessagingAdapter {
    private static final String TAG = "MessagingAdapter";
    private static final String MESSAGING_CLASS_NAME = "ly.count.android.sdk.messaging.CountlyMessaging";

    public MessagingAdapter() {
    }

    public static boolean isMessagingAvailable() {
        boolean messagingAvailable = false;

        try {
            Class.forName("ly.count.android.sdk.messaging.CountlyMessaging");
            messagingAvailable = true;
        } catch (ClassNotFoundException var2) {
        }

        return messagingAvailable;
    }

    public static boolean init(Activity activity, Class<? extends Activity> activityClass, String sender, String[] buttonNames) {
        try {
            Class<?> cls = Class.forName("ly.count.android.sdk.messaging.CountlyMessaging");
            Method method = cls.getMethod("init", Activity.class, Class.class, String.class, String[].class);
            method.invoke((Object)null, activity, activityClass, sender, buttonNames);
            return true;
        } catch (Throwable var6) {
            Log.e("MessagingAdapter", "Couldn't init Statistics Messaging", var6);
            return false;
        }
    }

    public static boolean storeConfiguration(Context context, String serverURL, String appKey, String deviceID, Type idMode) {
        try {
            Class<?> cls = Class.forName("ly.count.android.sdk.messaging.CountlyMessaging");
            Method method = cls.getMethod("storeConfiguration", Context.class, String.class, String.class, String.class, Type.class);
            method.invoke((Object)null, context, serverURL, appKey, deviceID, idMode);
            return true;
        } catch (Throwable var7) {
            Log.e("MessagingAdapter", "Couldn't store configuration in Statistics Messaging", var7);
            return false;
        }
    }
}
