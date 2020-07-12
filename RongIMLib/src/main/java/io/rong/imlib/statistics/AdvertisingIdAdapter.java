//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.lang.reflect.Method;

import io.rong.imlib.statistics.DeviceId.Type;

public class AdvertisingIdAdapter {
    private static final String TAG = "AdvertisingIdAdapter";
    private static final String ADVERTISING_ID_CLIENT_CLASS_NAME = "com.google.android.gms.ads.identifier.AdvertisingIdClient";
    private static Handler handler;

    public AdvertisingIdAdapter() {
    }

    public static boolean isAdvertisingIdAvailable() {
        boolean advertisingIdAvailable = false;

        try {
            Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
            advertisingIdAvailable = true;
        } catch (ClassNotFoundException var2) {
        }

        return advertisingIdAvailable;
    }

    public static void setAdvertisingId(final Context context, final StatisticsStore store, final DeviceId deviceId) {
        (new Thread(new Runnable() {
            public void run() {
                try {
                    deviceId.setId(Type.ADVERTISING_ID, AdvertisingIdAdapter.getAdvertisingId(context));
                } catch (Throwable var2) {
                    if (var2.getCause() != null && var2.getCause().getClass().toString().contains("GooglePlayServicesAvailabilityException")) {
                        if (Statistics.sharedInstance().isLoggingEnabled()) {
                            Log.i("AdvertisingIdAdapter", "Advertising ID cannot be determined yet");
                        }
                    } else if (var2.getCause() != null && var2.getCause().getClass().toString().contains("GooglePlayServicesNotAvailableException")) {
                        if (Statistics.sharedInstance().isLoggingEnabled()) {
                            Log.w("AdvertisingIdAdapter", "Advertising ID cannot be determined because Play Services are not available");
                        }

                        deviceId.switchToIdType(Type.OPEN_UDID, context, store);
                    } else {
                        Log.e("AdvertisingIdAdapter", "Couldn't get advertising ID", var2);
                    }
                }

            }
        })).start();
    }

    private static String getAdvertisingId(Context context) throws Throwable {
        Class<?> cls = Class.forName("com.google.android.gms.ads.identifier.AdvertisingIdClient");
        Method getAdvertisingIdInfo = cls.getMethod("getAdvertisingIdInfo", Context.class);
        Object info = getAdvertisingIdInfo.invoke((Object)null, context);
        if (info != null) {
            Method getId = info.getClass().getMethod("getId");
            Object id = getId.invoke(info);
            return (String)id;
        } else {
            return null;
        }
    }
}
