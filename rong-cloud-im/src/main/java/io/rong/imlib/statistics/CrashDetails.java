//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.rlog.RLog;

public class CrashDetails {
    private static final String TAG = "CrashDetails";
    private static ArrayList<String> logs = new ArrayList();
    private static int startTime = Statistics.currentTimestamp();
    private static Map<String, String> customSegments = null;
    private static boolean inBackground = true;
    private static long totalMemory = 0L;

    public CrashDetails() {
    }

    private static long getTotalRAM() {
        if (totalMemory == 0L) {
            RandomAccessFile reader = null;

            try {
                reader = new RandomAccessFile("/proc/meminfo", "r");
                String load = reader.readLine();
                Pattern p = Pattern.compile("(\\d+)");
                Matcher m = p.matcher(load);

                String value;
                for(value = ""; m.find(); value = m.group(1)) {
                }

                reader.close();
                totalMemory = Long.parseLong(value) / 1024L;
            } catch (IOException var13) {
                RLog.e("CrashDetails", "getTotalRAM", var13);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var12) {
                        RLog.e("CrashDetails", "getTotalRAM", var12);
                    }
                }

            }
        }

        return totalMemory;
    }

    static void inForeground() {
        inBackground = false;
    }

    static void inBackground() {
        inBackground = true;
    }

    static String isInBackground() {
        return Boolean.toString(inBackground);
    }

    static void addLog(String record) {
        logs.add(record);
    }

    static String getLogs() {
        String allLogs = "";

        String s;
        for(Iterator var1 = logs.iterator(); var1.hasNext(); allLogs = allLogs + s + "\n") {
            s = (String)var1.next();
        }

        logs.clear();
        return allLogs;
    }

    static void setCustomSegments(Map<String, String> segments) {
        customSegments = new HashMap();
        customSegments.putAll(segments);
    }

    static JSONObject getCustomSegments() {
        return customSegments != null && !customSegments.isEmpty() ? new JSONObject(customSegments) : null;
    }

    static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    static String getCpu() {
        return VERSION.SDK_INT < 21 ? Build.CPU_ABI : Build.SUPPORTED_ABIS[0];
    }

    static String getOpenGL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            FeatureInfo[] var3 = featureInfos;
            int var4 = featureInfos.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                FeatureInfo featureInfo = var3[var5];
                if (featureInfo.name == null) {
                    if (featureInfo.reqGlEsVersion != 0) {
                        return Integer.toString((featureInfo.reqGlEsVersion & -65536) >> 16);
                    }

                    return "1";
                }
            }
        }

        return "1";
    }

    static String getRamCurrent(Context context) {
        MemoryInfo mi = new MemoryInfo();
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        activityManager.getMemoryInfo(mi);
        return Long.toString(getTotalRAM() - mi.availMem / 1048576L);
    }

    static String getRamTotal(Context context) {
        return Long.toString(getTotalRAM());
    }

    static String getDiskCurrent() {
        StatFs statFs;
        long total;
        long free;
        if (VERSION.SDK_INT < 18) {
            statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            total = (long)(statFs.getBlockCount() * statFs.getBlockSize());
            free = (long)(statFs.getAvailableBlocks() * statFs.getBlockSize());
            return Long.toString((total - free) / 1048576L);
        } else {
            statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            total = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
            free = statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
            return Long.toString((total - free) / 1048576L);
        }
    }

    static String getDiskTotal() {
        StatFs statFs;
        long total;
        if (VERSION.SDK_INT < 18) {
            statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            total = (long)(statFs.getBlockCount() * statFs.getBlockSize());
            return Long.toString(total / 1048576L);
        } else {
            statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
            total = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
            return Long.toString(total / 1048576L);
        }
    }

    static String getBatteryLevel(Context context) {
        try {
            Intent batteryIntent = context.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            int level = batteryIntent.getIntExtra("level", -1);
            int scale = batteryIntent.getIntExtra("scale", -1);
            if (level > -1 && scale > 0) {
                return Float.toString((float)level / (float)scale * 100.0F);
            }
        } catch (Exception var4) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.i("Statistics", "Can't get batter level");
            }
        }

        return null;
    }

    static String getRunningTime() {
        return Integer.toString(Statistics.currentTimestamp() - startTime);
    }

    static String getOrientation(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        switch(orientation) {
            case 0:
                return "Unknown";
            case 1:
                return "Portrait";
            case 2:
                return "Landscape";
            case 3:
                return "Square";
            default:
                return null;
        }
    }

    static String isRooted() {
        String[] paths = new String[]{"/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"};
        String[] var1 = paths;
        int var2 = paths.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String path = var1[var3];
            if ((new File(path)).exists()) {
                return "true";
            }
        }

        return "false";
    }

    static String isOnline(Context context) {
        try {
            ConnectivityManager conMgr = (ConnectivityManager)context.getSystemService("connectivity");
            return conMgr != null && conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected() ? "true" : "false";
        } catch (Exception var2) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.w("Statistics", "Got exception determining connectivity", var2);
            }

            return null;
        }
    }

    static String isMuted(Context context) {
        AudioManager audio = (AudioManager)context.getSystemService("audio");
        switch(audio.getRingerMode()) {
            case 0:
                return "true";
            case 1:
                return "true";
            default:
                return "false";
        }
    }

    static String getCrashData(Context context, String error, Boolean nonfatal) {
        JSONObject json = new JSONObject();
        fillJSONIfValuesNotEmpty(json, "_error", error, "_nonfatal", Boolean.toString(nonfatal), "_logs", getLogs(), "_device", DeviceInfo.getDevice(), "_os", DeviceInfo.getOS(), "_os_version", DeviceInfo.getOSVersion(), "_resolution", DeviceInfo.getResolution(context), "_app_version", DeviceInfo.getAppVersion(context), "_manufacture", getManufacturer(), "_cpu", getCpu(), "_opengl", getOpenGL(context), "_ram_current", getRamCurrent(context), "_ram_total", getRamTotal(context), "_disk_current", getDiskCurrent(), "_disk_total", getDiskTotal(), "_bat", getBatteryLevel(context), "_run", getRunningTime(), "_orientation", getOrientation(context), "_root", isRooted(), "_online", isOnline(context), "_muted", isMuted(context), "_background", isInBackground());

        try {
            json.put("_custom", getCustomSegments());
        } catch (JSONException var5) {
        }

        String result = json.toString();
        return result;
    }

    public static String getIMCrashData(Context context, String error) {
        String result = getCrashData(context, "", true);
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
