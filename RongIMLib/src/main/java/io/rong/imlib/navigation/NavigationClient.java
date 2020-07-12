//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.security.NetworkSecurityPolicy;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.rong.common.RLog;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.imlib.CMPStrategy;
import io.rong.imlib.HttpDnsManager;
import io.rong.imlib.common.NetUtils;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class NavigationClient {
    private static final String TAG = "NavigationClient";
    private static final String NAVIGATION_HTTP_IP_URL = "http://%s/navi.json";
    private static final String NAVIGATION_HTTPS_URL = "https://nav.cn.ronghub.com/navi.json";
    private static final String NAVIGATION_HTTP_URL = "http://nav.cn.ronghub.com/navi.json";
    private static final String NAVIGATION_2_HTTPS_URL = "https://nav2-cn.ronghub.com/navi.json";
    private static final String NAVIGATION_2_HTTP_URL = "http://nav2-cn.ronghub.com/navi.json";
    private final String NAVI_SUFFIX;
    private List<String> naviUrlList;
    private String naviString;
    private NavigationClient.NaviUpdateListener naviUpdateListener;
    private ExecutorService executor;
    private Context context;
    private volatile NavigationObserver navigationObserver;
    private static final String SEP1 = ";";
    private static final String NAVI_SPLIT_SYMBOL = ";";
    private static final String NAVI_IN_TOKEN_SPLIT_SYMBOL = "@";
    private String latestTokenNaviEncodeStr;
    private List<String> latestTokenNaviList;

    private NavigationClient() {
        this.NAVI_SUFFIX = "navi.json";
        this.naviUrlList = new ArrayList();
        this.naviUpdateListener = null;
        this.latestTokenNaviList = new ArrayList();
        this.executor = Executors.newSingleThreadExecutor();
        if (VERSION.SDK_INT >= 28 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted()) {
            this.naviUrlList.add("https://nav.cn.ronghub.com/navi.json");
            this.naviUrlList.add("https://nav2-cn.ronghub.com/navi.json");
        } else {
            this.naviUrlList.add("http://nav.cn.ronghub.com/navi.json");
            this.naviUrlList.add("http://nav2-cn.ronghub.com/navi.json");
        }

        this.naviString = this.listToString(this.naviUrlList);
    }

    private String listToString(List<?> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for(int i = 0; i < list.size(); ++i) {
                if (list.get(i) != null && list.get(i) != "") {
                    sb.append(list.get(i));
                    sb.append(";");
                }
            }
        }

        return sb.toString();
    }

    public static NavigationClient getInstance() {
        return NavigationClient.SingletonHolder.sIns;
    }

    public void setNaviDomainList(String navi) {
        this.naviString = navi;
        if (this.naviUrlList != null) {
            this.naviUrlList.clear();
            if (navi != null) {
                this.naviUrlList.addAll(this.naviStrToList(navi, ";"));
                HttpDnsManager.getInstance().shouldEnableHttpDns(this.naviUrlList);
            }
        }
    }

    private List<String> naviStrToList(String naviStr, String split) {
        List<String> naviList = new ArrayList();
        if (TextUtils.isEmpty(naviStr)) {
            return naviList;
        } else {
            String[] naviArray = naviStr.split(split);
            String[] var5 = naviArray;
            int var6 = naviArray.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String navi = var5[var7];
                if (!TextUtils.isEmpty(navi)) {
                    String naviUrl = this.formatServerAddress(navi.trim(), "navi.json");
                    if (!naviList.contains(naviUrl)) {
                        naviList.add(naviUrl);
                    }
                }
            }

            return naviList;
        }
    }

    public String formatServerAddress(@NonNull String domain, String path) {
        String strFormat;
        if (domain.toLowerCase().startsWith("http")) {
            strFormat = "%s/%s";
        } else {
            strFormat = VERSION.SDK_INT >= 28 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted() ? "https://%s/%s" : "http://%s/%s";
        }

        return String.format(strFormat, domain, path);
    }

    public void addObserver(NavigationObserver observer) {
        this.navigationObserver = observer;
    }

    public void getCMPServerString(Context context, String appKey, String token) {
        RLog.d("NavigationClient", "[connect] getCMPServerString.");
        this.context = context.getApplicationContext();
        String userId;
        if (this.isNaviCacheValid(context, appKey, token)) {
            RLog.d("NavigationClient", "[connect] cache is valid.");
            userId = NavigationCacheHelper.getUserId(context);
            this.navigationObserver.onSuccess(userId);
        } else {
            userId = NavigationCacheHelper.getUserId(context);
            if (!token.equals(NavigationCacheHelper.getToken(context))) {
                userId = "";
                NavigationCacheHelper.clearUserId(context);
                RLog.d("NavigationClient", "[connect] clear userId.");
            }

            if (CMPStrategy.getInstance().getCmpList().size() > 0) {
                RLog.d("NavigationClient", "[connect] cache cmp length > 0.");
                this.navigationObserver.onSuccess(userId);
            } else {
                this.requestNavi(appKey, token, true);
            }
        }

    }

    public boolean isNaviCacheValid(Context context, String appKey, String token) {
        return NavigationCacheHelper.isCacheValid(context, appKey, token, this.naviString);
    }

    public void requestNavi(final String appKey, final String token, final boolean shouldNotifyObserver) {
        this.executor.submit(new Runnable() {
            public void run() {
                try {
                    ArrayList<String> combineNaviList = new ArrayList();
                    List<String> naviFromToken = NavigationClient.this.getNaviFromToken(token);
                    if (naviFromToken.size() > 0) {
                        combineNaviList.addAll(naviFromToken);
                    }

                    Iterator var3 = NavigationClient.this.naviUrlList.iterator();

                    while(var3.hasNext()) {
                        String navi = (String)var3.next();
                        if (!combineNaviList.contains(navi)) {
                            combineNaviList.add(navi);
                        }
                    }

                    if (combineNaviList.size() > 0) {
                        Iterator var10 = combineNaviList.iterator();

                        while(var10.hasNext()) {
                            String naviUrl = (String)var10.next();
                            boolean isLastNavi = combineNaviList.indexOf(naviUrl) == combineNaviList.size() - 1;
                            boolean result = NavigationClient.this.request(naviUrl, appKey, token, shouldNotifyObserver, isLastNavi);
                            if (result) {
                                return;
                            }
                        }
                    }
                } catch (Exception var7) {
                    FwLog.write(1, 1, LogTag.L_CRASH_IPC_EPT_F.getTag(), "stacks", new Object[]{FwLog.stackToString(var7)});
                    if (shouldNotifyObserver) {
                        String userId = NavigationCacheHelper.getUserId(NavigationClient.this.context);
                        NavigationClient.this.navigationObserver.onError(userId, 30004);
                    }
                }

            }
        });
    }

    public boolean isMPOpened(Context context) {
        return NavigationCacheHelper.isMPOpened(context);
    }

    public boolean isGROpened(Context context) {
        return NavigationCacheHelper.isGetRemoteEnabled(context);
    }

    public int getGroupMessageLimit(Context context) {
        return NavigationCacheHelper.getGroupMessageLimit(context);
    }

    public boolean isUSOpened(Context context) {
        return NavigationCacheHelper.isUSOpened(context);
    }

    public String getUserId(Context context) {
        return NavigationCacheHelper.getUserId(context);
    }

    public void requestCmpIfNeed(Context context, final String appKey, final String token) {
        if (NavigationCacheHelper.isCacheTimeout(context)) {
            this.executor.submit(new Runnable() {
                public void run() {
                    NavigationClient.this.requestNavi(appKey, token, true);
                }
            });
        }

    }

    public void clearCacheTime(Context context) {
        NavigationCacheHelper.updateTime(context, 0L);
    }

    public String getVoIPCallInfo(Context context) {
        return NavigationCacheHelper.getVoIPCallInfo(context);
    }

    public String getMediaServer(Context context) {
        return NavigationCacheHelper.getMediaServer(context);
    }

    public String getBaiDuMediaServer(Context context) {
        return NavigationCacheHelper.getBaiDuMediaServer(context);
    }

    public boolean isGetRemoteHistoryEnabled(Context context) {
        return NavigationCacheHelper.isGetRemoteEnabled(context);
    }

    public boolean isChatroomHistoryEnabled(Context context) {
        return NavigationCacheHelper.isChatroomHistoryEnabled(context);
    }

    private boolean request(String urlStr, String appKey, String token, boolean shouldNotifyObserver, boolean isLastNavi) {
        RLog.d("NavigationClient", "[connect] request " + urlStr + ", appKey:" + appKey + ", token: " + token + ", forceUpdate:" + shouldNotifyObserver);
        HttpURLConnection connection = null;
        BufferedInputStream responseStream = null;
        int responseCode = 0;
        boolean requestResult = false;
        String host = "";
        URL url = this.getUrlStr(urlStr);
        if (url != null) {
            host = url.getHost();
        }

        boolean var28;
        try {
            String userId;
            try {
                HashMap<String, String> ipMap = HttpDnsManager.getInstance().getHttpDnsIps(this.context, host);
                userId = (String)ipMap.get("resolveIp");
                String clientIp = (String)ipMap.get("clientIp");
                String requestToken = this.getTokenExceptNavi(token);
                long start_time;
                String requestIp;
                if (TextUtils.isEmpty(userId)) {
                    requestIp = NavigationCacheHelper.queryRequestIP(urlStr);
                    start_time = System.currentTimeMillis();
                    FwLog.write(3, 1, LogTag.L_GET_NAVI_T.getTag(), "url|ip", new Object[]{urlStr, requestIp});
                    connection = this.createConnection("", urlStr, appKey, requestToken, "");
                } else {
                    requestIp = userId;
                    start_time = System.currentTimeMillis();
                    FwLog.write(3, 1, LogTag.L_GET_NAVI_T.getTag(), "url|ip", new Object[]{urlStr, userId});
                    RLog.i("NavigationClient", "https connection ip = " + userId);
                    URL dnsUrl = new URL(urlStr.replaceFirst(host, userId));
                    connection = this.createConnection(host, dnsUrl.toString(), appKey, requestToken, clientIp);
                }

                connection.connect();
                responseCode = connection.getResponseCode();
                InputStream inputStream;
                if (responseCode != 200) {
                    inputStream = connection.getErrorStream();
                } else {
                    inputStream = connection.getInputStream();
                }

                responseStream = new BufferedInputStream(inputStream);
                ByteArrayOutputStream responseData = new ByteArrayOutputStream(512);

                int c;
                while((c = responseStream.read()) != -1) {
                    responseData.write(c);
                }

                String data = (new String(responseData.toByteArray(), "utf-8")).trim();
                long delta = System.currentTimeMillis() - start_time;
                FwLog.write(3, 1, LogTag.L_GET_NAVI_R.getTag(), "code|url|duration|ip", new Object[]{responseCode, urlStr, delta, requestIp});
                int result = NavigationCacheHelper.decode2File(this.context, data, responseCode);
                userId = NavigationCacheHelper.getUserId(this.context);
                if (result != 0) {
                    FwLog.write(1, 1, LogTag.L_DECODE_NAVI_S.getTag(), "code|data", new Object[]{result, data.replaceAll("\\n", "")});
                    if (this.navigationObserver != null && isLastNavi && shouldNotifyObserver) {
                        this.navigationObserver.onError(userId, result);
                    }

                    RLog.e("NavigationClient", "request failure : " + result + ", data = " + data);
                    return requestResult;
                }

                FwLog.write(3, 1, LogTag.L_DECODE_NAVI_S.getTag(), "code", new Object[]{result});
                if (!NavigationCacheHelper.getPrivateCloudConfig(this.context)) {
                    NavigationCacheHelper.clearComplexConnectionEntries(this.context);
                    CMPStrategy.getInstance().onGetCmpEntriesFromNavi();
                    if (shouldNotifyObserver) {
                        this.navigationObserver.onSuccess(userId);
                    }

                    NavigationCacheHelper.cacheRequest(this.context, appKey, token);
                    NavigationCacheHelper.cacheLastSuccessNaviDomainList(this.context, this.naviString);
                    this.notifyNaviUpdate();
                    requestResult = true;
                    return requestResult;
                }

                if (shouldNotifyObserver) {
                    this.navigationObserver.onError(userId, 34005);
                }

                var28 = false;
            } catch (Exception var39) {
                FwLog.write(1, 1, LogTag.L_CRASH_IPC_EPT_F.getTag(), "stacks", new Object[]{FwLog.stackToString(var39)});
                FwLog.write(3, 1, LogTag.L_GET_NAVI_R.getTag(), "code|url", new Object[]{responseCode, urlStr});
                if (this.navigationObserver != null && isLastNavi && shouldNotifyObserver) {
                    userId = NavigationCacheHelper.getUserId(this.context);
                    this.navigationObserver.onError(userId, 30004);
                }

                RLog.e("NavigationClient", "request exception.");
                return requestResult;
            }
        } finally {
            RLog.i("NavigationClient", "request end: " + responseCode + ", force = " + shouldNotifyObserver);
            if (responseStream != null) {
                try {
                    responseStream.close();
                } catch (IOException var38) {
                    RLog.e("NavigationClient", "IOException ", var38);
                }
            }

            if (connection != null) {
                connection.disconnect();
            }

        }

        return var28;
    }

    private URL getUrlStr(String urlStr) {
        URL url;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException var4) {
            url = null;
            RLog.e("NavigationClient", "getUrlStr ", var4);
        }

        return url;
    }

    private HttpURLConnection createConnection(String originalHost, final String urlStr, String appKey, String token, String clientIp) throws IOException {
        HttpURLConnection conn = NetUtils.createURLConnection(urlStr);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(10000);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Close");
        conn.setRequestProperty("User-Agent", "RongCloud");
        String host;
        if (TextUtils.isEmpty(originalHost)) {
            host = getNavHost(urlStr);
        } else {
            host = originalHost;
        }

        if (!TextUtils.isEmpty(clientIp)) {
            conn.setRequestProperty("clientIp", clientIp);
            NavigationCacheHelper.updateClientIp(this.context, clientIp);
        }

        if (!TextUtils.isEmpty(host)) {
            conn.setRequestProperty("Host", host);
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection)conn).setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return !TextUtils.isEmpty(urlStr) && urlStr.contains(hostname);
                    }
                });
            }
        }

        String params = "token=";
        params = params + URLEncoder.encode(token, "UTF-8");
        params = params + "&v=4.0.0.1";
        params = params + "&p=Android";
        conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
        conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("appId", appKey);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(params);
        writer.flush();
        writer.close();
        os.close();
        return conn;
    }

    private static String getNavHost(String navi) {
        try {
            URL url = new URL(navi);
            String host = url.getHost();
            int port = url.getPort();
            if (port != -1 && url.getDefaultPort() != url.getPort()) {
                host = host + ":" + port;
            }

            return host;
        } catch (MalformedURLException var4) {
            RLog.e("NavigationClient", "MalformedURLException ", var4);
            return null;
        }
    }

    public String getNaviString() {
        return this.naviString;
    }

    private List<String> getNaviFromToken(String token) {
        List<String> naviList = new ArrayList();
        String[] splitResult = token.split("@");
        if (splitResult.length > 1) {
            String naviStr = splitResult[1];
            if (!TextUtils.isEmpty(naviStr)) {
                if (!naviStr.equals(this.latestTokenNaviEncodeStr)) {
                    naviList.addAll(this.naviStrToList(naviStr, ";"));
                    this.latestTokenNaviList.clear();
                    this.latestTokenNaviList.addAll(naviList);
                    this.latestTokenNaviEncodeStr = naviStr;
                } else {
                    naviList.addAll(this.latestTokenNaviList);
                }
            }
        }

        return naviList;
    }

    public String getTokenExceptNavi(String token) {
        if (!TextUtils.isEmpty(token) && token.contains("@")) {
            token = token.substring(0, token.indexOf("@") + 1);
        }

        return token;
    }

    public void setNaviUpdateListener(NavigationClient.NaviUpdateListener listener) {
        this.naviUpdateListener = listener;
    }

    private void notifyNaviUpdate() {
        if (this.naviUpdateListener != null) {
            this.naviUpdateListener.onNaviUpdate();
        }

    }

    public String getUploadLogConfigInfo(Context context) {
        return NavigationCacheHelper.getRealTimeLogConfig(context);
    }

    public interface NaviUpdateListener {
        void onNaviUpdate();
    }

    private static class SingletonHolder {
        @SuppressLint({"StaticFieldLeak"})
        private static final NavigationClient sIns = new NavigationClient();

        private SingletonHolder() {
        }
    }
}
