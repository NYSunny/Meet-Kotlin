//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.core;

import android.content.Context;
import android.os.Build.VERSION;
import android.security.NetworkSecurityPolicy;
import android.text.TextUtils;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import io.rong.common.utils.SSLUtils;
import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushErrorCode;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.common.RLog;
import io.rong.push.pushconfig.PushNaviObserver;
import io.rong.push.rongpush.RongPushCacheHelper;

public class PushNaviClient {
    private static final String TAG = PushNaviClient.class.getSimpleName();
    private final String NAVI_SPLIT = ";";
    private final String IP_SPLIT = ",";
    private final String NAVI_PATH = "navipush.json";
    private Set<String> naviList = new LinkedHashSet();
    private PushNaviObserver pushNaviObserver;
    private boolean isPushProcess;

    public PushNaviClient() {
    }

    public void setPushNaviUrl(String naviAddress) {
        RLog.i(TAG, "setPushNaviUrl " + naviAddress);
        if (TextUtils.isEmpty(naviAddress)) {
            RLog.e(TAG, "navi address is empty! Use default navi address!");
            naviAddress = PushUtils.getDefaultNavi();
        }

        this.naviList.addAll(this.naviStrToList(naviAddress));
    }

    private List<String> naviStrToList(String naviStr) {
        if (TextUtils.isEmpty(naviStr)) {
            return new ArrayList();
        } else {
            String[] naviArray = naviStr.split(";");
            List<String> list = new ArrayList();
            if (naviArray.length > 0) {
                String[] var4 = naviArray;
                int var5 = naviArray.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String navi = var4[var6];
                    if (!TextUtils.isEmpty(navi)) {
                        String current = this.formatServerAddress(navi, "navipush.json");
                        if (!list.contains(current)) {
                            list.add(current);
                        }
                    }
                }
            }

            return list;
        }
    }

    private String formatServerAddress(String domain, String path) {
        String strFormat;
        if (domain.toLowerCase().startsWith("http")) {
            strFormat = "%s/%s";
        } else {
            strFormat = VERSION.SDK_INT >= 28 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted() ? "https://%s/%s" : "http://%s/%s";
        }

        return String.format(strFormat, domain, path);
    }

    public void getPushServerIPs(Context context, String appKey, boolean isPushProcess, PushNaviObserver observer) {
        this.pushNaviObserver = observer;
        this.isPushProcess = isPushProcess;
        if (isPushProcess && RongPushCacheHelper.getInstance().isCacheValid(context, appKey)) {
            observer.onSuccess(RongPushCacheHelper.getInstance().getCachedAddressList(context));
        } else if (!isPushProcess && PushCacheHelper.getInstance().isCacheValid(context, appKey)) {
            observer.onSuccess(PushCacheHelper.getInstance().getCachedAddressList(context));
        } else {
            this.connectToNavi(context, appKey);
        }

    }

    private void connectToNavi(Context context, String appKey) {
        String tokenInNaviToken = PushCacheHelper.getInstance().getPushServerInfoInIMToken(context);
        List<String> combineList = new ArrayList(this.naviStrToList(tokenInNaviToken));
        Iterator iterator = this.naviList.iterator();

        String navi;
        while(iterator.hasNext()) {
            navi = (String)iterator.next();
            if (!combineList.contains(navi)) {
                combineList.add(navi);
            }
        }

        iterator = combineList.iterator();

        while(iterator.hasNext()) {
            navi = (String)iterator.next();
            if (this.connect(context, navi, appKey, !iterator.hasNext())) {
                break;
            }
        }

    }

    private boolean connect(Context context, String naviUrl, String appKey, boolean isLastNavi) {
        HttpURLConnection conn = null;
        BufferedInputStream responseStream = null;
        boolean result = false;
        String address = "";

        try {
            URL url = new URL(naviUrl);
            RLog.i(TAG, "navigation url : " + url);
            conn = this.createConnection(naviUrl);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("appId", appKey);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            String param = "deviceId=" + DeviceUtils.getDeviceId(context, appKey);
            writer.write(param);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode >= 100 && responseCode <= 300) {
                responseStream = new BufferedInputStream(conn.getInputStream());
                ByteArrayOutputStream responseData = new ByteArrayOutputStream(256);

                int c;
                while((c = responseStream.read()) != -1) {
                    responseData.write(c);
                }

                JSONObject responseDict = new JSONObject(responseData.toString("UTF-8"));
                boolean success = responseDict.optString("code").equalsIgnoreCase("200");
                if (success) {
                    result = true;
                    ArrayList<String> addressList = new ArrayList();
                    address = responseDict.optString("server");
                    addressList.add(address);
                    String bs = responseDict.optString("bs");
                    RLog.d(TAG, "server:" + address + ";bs server:" + bs);
                    if (!TextUtils.isEmpty(bs)) {
                        String[] bsArray = bs.split(",");
                        if (bsArray.length > 0) {
                            String[] var21 = bsArray;
                            int var22 = bsArray.length;

                            for(int var23 = 0; var23 < var22; ++var23) {
                                String str = var21[var23];
                                addressList.add(str);
                            }
                        }
                    }

                    if (this.isPushProcess) {
                        RongPushCacheHelper.getInstance().cacheRongPushIPs(context, addressList, System.currentTimeMillis());
                    } else {
                        PushCacheHelper.getInstance().saveAllAddress(context, addressList, System.currentTimeMillis());
                    }

                    this.pushNaviObserver.onSuccess(addressList);
                } else if (this.pushNaviObserver != null && isLastNavi) {
                    RLog.e(TAG, "Fail to get navi. errorcode:" + responseCode);
                    this.pushNaviObserver.onError(PushErrorCode.IO_EXCEPTION);
                }
            }
        } catch (Exception var33) {
            RLog.e(TAG, "Exception when get navigation address.Retry again.");
            if (this.pushNaviObserver != null && isLastNavi) {
                this.pushNaviObserver.onError(PushErrorCode.IO_EXCEPTION);
            }

            var33.printStackTrace();
        } finally {
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException var32) {
                }
            }

            if (conn != null) {
                conn.disconnect();
            }

        }

        return result;
    }

    private HttpURLConnection createConnection(String urlStr) throws Exception {
        Object connection;
        URL url;
        if (urlStr.toLowerCase().startsWith("https")) {
            url = new URL(urlStr);
            HttpsURLConnection c = (HttpsURLConnection)url.openConnection();
            SSLContext sslContext = SSLUtils.getSSLContext();
            if (sslContext != null) {
                c.setSSLSocketFactory(sslContext.getSocketFactory());
            }

            HostnameVerifier hostVerifier = SSLUtils.getHostVerifier();
            if (hostVerifier != null) {
                c.setHostnameVerifier(hostVerifier);
            }

            connection = c;
        } else {
            url = new URL(urlStr);
            connection = (HttpURLConnection)url.openConnection();
        }

        return (HttpURLConnection)connection;
    }
}
