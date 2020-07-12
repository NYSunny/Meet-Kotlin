//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import io.rong.common.utils.SSLUtils;

public class NetUtils {
    private static final String TAG = NetUtils.class.getSimpleName();
    private static Boolean isLatestNetWorkAvailable = null;

    public NetUtils() {
    }

    public static HttpURLConnection createURLConnection(String urlStr) throws IOException {
        Object conn;
        URL url;
        if (urlStr.toLowerCase().startsWith("https")) {
            url = new URL(urlStr);
            HttpsURLConnection c = (HttpsURLConnection)url.openConnection();
            SSLContext sslContext = SSLUtils.getSSLContext();
            if (sslContext != null) {
                c.setSSLSocketFactory(sslContext.getSocketFactory());
            }

            HostnameVerifier hostnameVerifier = SSLUtils.getHostVerifier();
            if (hostnameVerifier != null) {
                c.setHostnameVerifier(hostnameVerifier);
            }

            conn = c;
        } else {
            url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
        }

        return (HttpURLConnection)conn;
    }

    @SuppressLint({"MissingPermission"})
    public static boolean isNetWorkAvailable(Context context) {
        if (context == null) {
            return false;
        } else {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo networkInfo = null;

            try {
                networkInfo = cm.getActiveNetworkInfo();
                Log.d(TAG, "network : " + (networkInfo != null ? networkInfo.isAvailable() + " " + networkInfo.isConnected() : "null"));
            } catch (Exception var4) {
                Log.e(TAG, "getActiveNetworkInfo Exception", var4);
            }

            boolean isNetWorkAvailable = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
            return isNetWorkAvailable;
        }
    }

    public static boolean getCacheNetworkAvailable(Context context) {
        if (isLatestNetWorkAvailable == null) {
            isLatestNetWorkAvailable = isNetWorkAvailable(context);
        }

        return isLatestNetWorkAvailable;
    }

    public static void updateCacheNetworkAvailable(boolean isAvailable) {
        isLatestNetWorkAvailable = isAvailable;
    }
}
