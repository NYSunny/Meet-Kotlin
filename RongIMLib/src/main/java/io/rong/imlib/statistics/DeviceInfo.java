//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.TimeZone;

class DeviceInfo {
    DeviceInfo() {
    }

    static String getOS() {
        return "Android";
    }

    static String getOSVersion() {
        return VERSION.RELEASE;
    }

    static String getDevice() {
        return Build.MODEL;
    }

    static String getResolution(Context context) {
        String resolution = "";

        try {
            WindowManager wm = (WindowManager)context.getSystemService("window");
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            resolution = metrics.widthPixels + "x" + metrics.heightPixels;
        } catch (Throwable var5) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.i("Statistics", "Device resolution cannot be determined");
            }
        }

        return resolution;
    }

    static String getDensity(Context context) {
        String densityStr = "";
        int density = context.getResources().getDisplayMetrics().densityDpi;
        switch(density) {
            case 120:
                densityStr = "LDPI";
                break;
            case 160:
                densityStr = "MDPI";
                break;
            case 213:
                densityStr = "TVDPI";
                break;
            case 240:
                densityStr = "HDPI";
                break;
            case 320:
                densityStr = "XHDPI";
                break;
            case 400:
                densityStr = "XMHDPI";
                break;
            case 480:
                densityStr = "XXHDPI";
                break;
            case 640:
                densityStr = "XXXHDPI";
        }

        return densityStr;
    }

    static String getCarrier(Context context) {
        String carrier = "";

        try {
            TelephonyManager manager = (TelephonyManager)context.getSystemService("phone");
            if (manager != null) {
                carrier = manager.getNetworkOperatorName();
            }
        } catch (SecurityException var3) {
        }

        if (carrier == null || carrier.length() == 0) {
            carrier = "";
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.i("Statistics", "No carrier found");
            }
        }

        return carrier;
    }

    static String getNetworkType(Context context) {
        String type = "UNKNOWN";
        ConnectivityManager connectMgr = (ConnectivityManager)context.getSystemService("connectivity");
        if (connectMgr != null) {
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == 1) {
                    type = "WIFI";
                } else if (info.getType() == 0) {
                    type = "MOBILE";
                }
            }
        }

        return type;
    }

    static String getLocale() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    static String getAppVersion(Context context) {
        String result = "1.0";

        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException var3) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.i("Statistics", "No app version found");
            }
        }

        return result;
    }

    static String getStore(Context context) {
        String result = "";
        if (VERSION.SDK_INT >= 3) {
            try {
                result = context.getPackageManager().getInstallerPackageName(context.getPackageName());
            } catch (Exception var3) {
                if (Statistics.sharedInstance().isLoggingEnabled()) {
                    Log.i("Statistics", "Can't get Installer package");
                }
            }

            if (result == null || result.length() == 0) {
                result = "";
                if (Statistics.sharedInstance().isLoggingEnabled()) {
                    Log.i("Statistics", "No store found");
                }
            }
        }

        return result;
    }

    static String getMetrics(Context context) {
        JSONObject json = new JSONObject();
        String displayName = "";

        try {
            displayName = TimeZone.getDefault().getDisplayName(false, 0);
        } catch (AssertionError var6) {
            displayName = "";
        }

        fillJSONIfValuesNotEmpty(json, "device", getDevice(), "osName", getOS(), "osVersion", getOSVersion(), "carrier", getCarrier(context), "resolution", getResolution(context), "density", getDensity(context), "locale", getLocale(), "appVersion", getAppVersion(context), "channel", getStore(context), "bundleId", context.getPackageName(), "sdkVersion", "4.0.0.1", "network", getNetworkType(context), "timeZone", displayName);
        String result = json.toString();

        try {
            result = URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
        }

        return result;
    }

    static void fillJSONIfValuesNotEmpty(JSONObject json, String... objects) {
        try {
            if (objects.length > 0 && objects.length % 2 == 0) {
                for(int i = 0; i < objects.length; i += 2) {
                    String key = objects[i];
                    String value = objects[i + 1];
                    if (value != null && value.length() > 0) {
                        json.put(key, value);
                    }
                }
            }
        } catch (JSONException var5) {
        }

    }
}
