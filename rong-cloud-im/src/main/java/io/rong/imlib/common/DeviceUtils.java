//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class DeviceUtils {
    public DeviceUtils() {
    }

    public static synchronized String getDeviceId(Context context, String appKey) {
        SharedPreferences sp = context.getSharedPreferences("Statistics", 0);
        String deviceId = sp.getString("deviceId", "");
        if (TextUtils.isEmpty(deviceId)) {
            String[] params = new String[]{getDeviceId(context), appKey, context.getPackageName()};
            deviceId = ShortMD5(0, params);
            Editor editor = sp.edit();
            editor.putString("deviceId", deviceId);
            editor.commit();
        }

        return deviceId;
    }

    public static String getDeviceId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Statistics", 0);
        String deviceId = sp.getString("ANDROID_ID", "");
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = Secure.getString(context.getApplicationContext().getContentResolver(), "android_id");
            if (TextUtils.isEmpty(deviceId)) {
                SecureRandom random = new SecureRandom();
                deviceId = (new BigInteger(64, random)).toString(16);
            }

            Editor editor = sp.edit();
            editor.putString("ANDROID_ID", deviceId);
            editor.commit();
        }

        return deviceId;
    }

    public static String getDeviceIMEI(Context context) {
        return getDeviceId(context);
    }

    /** @deprecated */
    @Deprecated
    public static String ShortMD5(String... args) {
        return ShortMD5(0, args);
    }

    public static String ShortMD5(int flags, String... args) {
        try {
            StringBuilder builder = new StringBuilder();
            String[] var3 = args;
            int var4 = args.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String arg = var3[var5];
                builder.append(arg);
            }

            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(builder.toString().getBytes());
            byte[] mds = mdInst.digest();
            mds = Base64.encode(mds, flags);
            String result = new String(mds);
            result = result.replace("=", "").replace("+", "-").replace("/", "_").replace("\n", "");
            return result;
        } catch (Exception var7) {
            return "";
        }
    }

    public static String getPhoneInformation(Context context) {
        String MCCMNC = "";

        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
            MCCMNC = telephonyManager.getNetworkOperator();
        } catch (SecurityException var9) {
            Log.e("DeviceUtils", "SecurityException!!!");
        }

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        String network = "";
        NetworkInfo networkInfo = null;

        try {
            networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        } catch (SecurityException var8) {
            Log.e("DeviceUtils", "getPhoneInformation securityException!!!");
        }

        if (networkInfo != null) {
            network = networkInfo.getTypeName();
        }

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (manufacturer == null) {
            manufacturer = "";
        }

        if (model == null) {
            model = "";
        }

        String devInfo = manufacturer + "|";
        devInfo = devInfo + model;
        devInfo = devInfo + "|";
        devInfo = devInfo + String.valueOf(VERSION.SDK_INT);
        devInfo = devInfo + "|";
        devInfo = devInfo + network;
        devInfo = devInfo + "|";
        devInfo = devInfo + MCCMNC;
        devInfo = devInfo + "|";
        devInfo = devInfo + context.getPackageName();
        devInfo = devInfo.replace("-", "_");
        Log.i("DeviceUtils", "getPhoneInformation.the phone information is: " + devInfo);
        return devInfo;
    }

    public static String getDeviceManufacturer() {
        String manufacturer = Build.MANUFACTURER.replace("-", "_");
        long outTime = 3000L;
        if (!TextUtils.isEmpty(manufacturer)) {
            if ("vivo".equals(manufacturer)) {
                manufacturer = manufacturer.toUpperCase();
            }

            return manufacturer;
        } else {
            Runtime runtime = Runtime.getRuntime();
            String line = "";
            Worker worker = new Worker(runtime);
            long startTime = System.currentTimeMillis();
            worker.start();

            try {
                worker.join(outTime);
                line = worker.line;
            } catch (InterruptedException var12) {
                worker.interrupt();
            } finally {
                worker.interrupt();
            }

            long endTime = System.currentTimeMillis();
            if (endTime - startTime >= outTime) {
                Log.e("DeviceUtils", "getDeviceManufacturer====OutTime");
            }

            return !TextUtils.isEmpty(line) ? "Xiaomi" : "";
        }
    }

    public static String getDeviceBandModelVersion() {
        return Build.BRAND + "|" + Build.MODEL + "|" + VERSION.RELEASE;
    }

    public static boolean isEMUI() {
        return Build.MANUFACTURER.equalsIgnoreCase("HUAWEI");
    }

    public static String getNetworkType(Context context) {
        String strNetworkType = "none";
        if (context == null) {
            return "null";
        } else {
            NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                try {
                    if (networkInfo.getType() == 1) {
                        strNetworkType = "WIFI";
                    } else if (networkInfo.getType() == 0) {
                        String _strSubTypeName = networkInfo.getSubtypeName();
                        int networkType = networkInfo.getSubtype();
                        switch(networkType) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                                strNetworkType = "2G";
                                break;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                                strNetworkType = "3G";
                                break;
                            case 13:
                                strNetworkType = "4G";
                                break;
                            default:
                                if (!_strSubTypeName.equalsIgnoreCase("TD-SCDMA") && !_strSubTypeName.equalsIgnoreCase("WCDMA") && !_strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                    strNetworkType = _strSubTypeName;
                                } else {
                                    strNetworkType = "3G";
                                }
                        }
                    }
                } catch (Exception var5) {
                    Log.e("DeviceUtils", var5.getMessage());
                }
            }

            return strNetworkType;
        }
    }

    private static class Worker extends Thread {
        String propName;
        Runtime runtime;
        BufferedReader input;
        String line;
        Process process;

        private Worker(Runtime runtime) {
            this.propName = "ro.miui.ui.version.name";
            this.input = null;
            this.runtime = runtime;
        }

        public void run() {
            try {
                this.process = this.runtime.exec("getprop " + this.propName);
                this.input = new BufferedReader(new InputStreamReader(this.process.getInputStream()), 1024);
                this.line = this.input.readLine();
                this.input.close();
            } catch (IOException var10) {
                Log.e("DeviceUtils", "Unable to read sysprop ");
            } finally {
                if (this.input != null) {
                    try {
                        this.input.close();
                    } catch (IOException var9) {
                        var9.printStackTrace();
                    }
                }

                if (this.process != null) {
                    this.process.destroy();
                }

            }

        }
    }
}
