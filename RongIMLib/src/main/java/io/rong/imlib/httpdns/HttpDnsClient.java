//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.common.rlog.RLog;

final class HttpDnsClient {
    private static final String TAG = "HttpDnsClient";
    private static volatile HttpDnsClient httpDnsClient;
    private static final int timeOut = 3000;
    private static final int kDftSignatureTime = 300;
    private static boolean isSecretEncrypted = true;
    private String defaultServerIp = "180.76.76.200";
    private static final String serverDomain = "httpdns.baidubce.com";
    private static HttpDnsHostnameVerifier httpDnsHostnameVerifier;
    private boolean isHttps = true;
    private long repairLocalClock = 0L;
    private final HashSet<String> hostsToResolve = new HashSet();
    private final Object hostToResolveLock = new Object();
    private final Object hostsExpiredLock = new Object();
    private ArrayList<String> hostsExpired = new ArrayList();
    private String accountID = "";
    private String secret = "";
    private static final String sdkVersion = "1.3";
    private static final String osType = "android";
    static final int HTTPDNS_RESOLVE_OK = 0;
    static final int HTTPDNS_RESOLVE_ERR = -1;
    private static final int RSP_DATA_NULL = 101;
    private static final int SIGN_EXPIRED = 102;
    private static final int RESOLVE_DNS_ERR = 103;
    private boolean preResolveFinish = false;
    private int maxHostNum = 10;

    private HttpDnsClient() {
        httpDnsHostnameVerifier = new HttpDnsHostnameVerifier();
    }

    static HttpDnsClient getInstance() {
        if (httpDnsClient == null) {
            Class var0 = HttpDnsClient.class;
            synchronized(HttpDnsClient.class) {
                if (httpDnsClient == null) {
                    httpDnsClient = new HttpDnsClient();
                }
            }
        }

        return httpDnsClient;
    }

    private String calcSign(String host, long t) {
        String secret = this.getSecret();
        String origin = String.format("%s-%s-%d", host, secret, t);
        return RongHttpDnsUtil.md5(origin);
    }

    private String getStringFromInputStream(InputStream in, HttpURLConnection conn) {
        String encoding = conn.getContentEncoding();

        try {
            if (encoding != null && encoding.contains("gzip")) {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                int size = conn.getContentLength();
                byte[] buffer;
                if (size > 0) {
                    buffer = new byte[size];
                    int count;
                    if ((count = in.read(buffer)) != -1) {
                        output.write(buffer, 0, count);
                    }
                } else {
                    size = 1024;
                    buffer = new byte[size];

                    int n;
                    while(-1 != (n = in.read(buffer))) {
                        output.write(buffer, 0, n);
                    }
                }

                output.flush();
                output.close();
                buffer = output.toByteArray();
                return this.getStringFromGzip(buffer, size);
            } else {
                BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder rspData = new StringBuilder();

                String line;
                while((line = streamReader.readLine()) != null) {
                    rspData.append(line);
                }

                streamReader.close();
                return rspData.toString();
            }
        } catch (UnsupportedEncodingException var9) {
            RLog.e("HttpDnsClient", "getStringFromInputStream UnsupportedEncodingException", var9);
            return null;
        } catch (IOException var10) {
            RLog.e("HttpDnsClient", "getStringFromInputStream IOException", var10);
            return null;
        }
    }

    private String getStringFromGzip(byte[] gzipBuffer, int size) {
        byte[] buffer = new byte[size];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(gzipBuffer);

        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream, size);

            int n;
            do {
                n = gzipInputStream.read(buffer, 0, size);
                out.write(buffer, 0, n);
            } while(n != -1 && !isJson(out.toString()));

            gzipInputStream.close();
            return out.toString();
        } catch (IOException var8) {
            RLog.e("HttpDnsClient", "getStringFromGzip IOException", var8);
            return null;
        }
    }

    private static boolean isJson(String value) {
        try {
            new JSONObject(value);
            return true;
        } catch (JSONException var2) {
            return false;
        }
    }

    int getMaxHostNum() {
        return this.maxHostNum;
    }

    private Map checkRspMsg(String rspData, String hostNameOrTag, RequestParamType requestParamType) {
        Map<String, Boolean> rspMsgMap = new HashMap();
        rspMsgMap.put("isMsgOK", false);
        rspMsgMap.put("isSignExpired", false);

        JSONObject json;
        try {
            json = new JSONObject(rspData);
        } catch (JSONException var8) {
            var8.printStackTrace();
            Logger.printLog("Httpdns request failed for %s(%s), response parse json error", new Object[]{requestParamType.toString(), hostNameOrTag});
            return rspMsgMap;
        }

        String msg = json.optString("msg");
        if (msg != null && !msg.isEmpty()) {
            if ("SignatureExpired".equals(msg)) {
                int timeStamp = json.optInt("timestamp");
                if (timeStamp == 0) {
                    Logger.printLog("Httpdns request failed for %s(%s), response get invalid timestamp", new Object[]{requestParamType.toString(), hostNameOrTag});
                } else {
                    this.repairLocalClock = (long)timeStamp - System.currentTimeMillis() / 1000L;
                    rspMsgMap.put("isSignExpired", true);
                }

                return rspMsgMap;
            } else if (!"ok".equals(msg)) {
                Logger.printLog("Httpdns request failed for %s(%s), response msg(%s) is not ok", new Object[]{requestParamType.toString(), hostNameOrTag, msg});
                return rspMsgMap;
            } else {
                rspMsgMap.put("isMsgOK", true);
                return rspMsgMap;
            }
        } else {
            Logger.printLog("Httpdns request failed for %s(%s), response lack of msg", new Object[]{requestParamType.toString(), hostNameOrTag});
            return rspMsgMap;
        }
    }

    private Map parseRspData(String rspData, String hostsOrTag) {
        HashMap resultsMap = new HashMap();

        JSONObject json;
        try {
            json = new JSONObject(rspData);
        } catch (JSONException var19) {
            var19.printStackTrace();
            Logger.printLog("Httpdns request failed, hostsOrTag(%s), response parse data json error", new Object[]{hostsOrTag});
            return null;
        }

        String clientIp = null;
        if (json.has("clientip")) {
            try {
                clientIp = json.getString("clientip");
            } catch (JSONException var18) {
                var18.printStackTrace();
            }
        }

        JSONObject dataObj = json.optJSONObject("data");
        if (dataObj == null) {
            Logger.printLog("Httpdns request failed, hostsOrTag(%s), response has empty data", new Object[]{hostsOrTag});
            return null;
        } else {
            int totalParsed = 0;
            Iterator iterator = dataObj.keys();

            while(true) {
                while(iterator.hasNext()) {
                    String hostKey = (String)iterator.next();
                    JSONObject hostObj = dataObj.optJSONObject(hostKey);
                    JSONObject ipv4Obj = hostObj.optJSONObject("ipv4");
                    JSONArray ips = ipv4Obj.optJSONArray("ip");
                    if (ips != null && ips.length() != 0) {
                        ArrayList<String> ipv4List = new ArrayList();

                        for(int i = 0; i < ips.length(); ++i) {
                            String ip = ips.optString(i);
                            if (ip != null && !ip.isEmpty()) {
                                if (!RongHttpDnsUtil.validateIpv4(ip)) {
                                    Logger.printLog("Httpdns request warning, host(%s), response get invalid ip(%s)", new Object[]{hostKey, ip});
                                } else {
                                    ipv4List.add(ip);
                                }
                            } else {
                                Logger.printLog("Httpdns request warning, host(%s), response get ip error", new Object[]{hostKey});
                            }
                        }

                        if (ipv4List.isEmpty()) {
                            Logger.printLog("Httpdns request failed, host(%s), response has no valid ip", new Object[]{hostKey});
                            resultsMap.put(hostKey, (Object)null);
                        } else {
                            long ttl;
                            try {
                                ttl = ipv4Obj.getLong("ttl");
                            } catch (JSONException var17) {
                                var17.printStackTrace();
                                Logger.printLog("Httpdns request failed, host(%s), response has no ttl", new Object[]{hostKey});
                                return null;
                            }

                            if (ttl < 0L) {
                                Logger.printLog("Httpdns request failed, host(%s), response has invalid ttl(%s)", new Object[]{hostKey, ttl});
                                resultsMap.put(hostKey, (Object)null);
                            } else {
                                Result result = new Result(ipv4List, ttl, clientIp);
                                resultsMap.put(hostKey, result);
                                ++totalParsed;
                            }
                        }
                    } else {
                        Logger.printLog("Httpdns request failed, host(%s), response has no ip field", new Object[]{hostKey});
                        resultsMap.put(hostKey, (Object)null);
                    }
                }

                if (totalParsed == 0) {
                    return null;
                }

                return resultsMap;
            }
        }
    }

    void asyncSendRequest(String hostsOrTag, RequestParamType requestParamType, AsyncHttpDnsCompletion httpDnsCompletion) {
        if (hostsOrTag != null && !hostsOrTag.isEmpty()) {
            synchronized(this.hostToResolveLock) {
                if (requestParamType.equals(RequestParamType.DNLIST_HOSTS)) {
                    List<String> hosts = new ArrayList(Arrays.asList(hostsOrTag.split(",")));
                    Iterator it = hosts.iterator();

                    while(it.hasNext()) {
                        String singleHost = (String)it.next();
                        if (!this.hostsToResolve.contains(singleHost)) {
                            this.hostsToResolve.add(singleHost);
                        } else {
                            Logger.printLog("Httpdns request request for host(%s) is in processing，will exclude it.", new Object[]{singleHost});
                            it.remove();
                        }
                    }

                    StringBuilder sb = new StringBuilder();

                    for(int i = 0; i < hosts.size(); ++i) {
                        sb.append((String)hosts.get(i)).append(",");
                    }

                    String regex = "^,*|,*$";
                    hostsOrTag = sb.toString().replaceAll(regex, "");
                }

                if (!hostsOrTag.isEmpty()) {
                    try {
                        ThreadPool.getInstance().getExecutor().execute(new AsyncHttpTask(hostsOrTag, requestParamType, httpDnsCompletion));
                    } catch (RejectedExecutionException var10) {
                        RLog.e("HttpDnsClient", "asyncSendRequest RejectedExecutionException", var10);
                        Logger.printLog("Httpdns request failed, host(%s), async tasks has exceed the maximum thread limit.", new Object[]{hostsOrTag});
                    }
                }

            }
        }
    }

    void splitHostsAndSendRequest(ArrayList<String> hosts, AsyncHttpDnsCompletion httpDnsCompletion) {
        hosts = new ArrayList(new HashSet(hosts));
        int prefetchNum = 0;
        int currNum = 0;

        while(currNum < hosts.size()) {
            StringBuilder strBuilder = new StringBuilder();

            for(int index = 0; index < this.maxHostNum; ++index) {
                currNum = index + prefetchNum * this.maxHostNum;
                if (currNum >= hosts.size()) {
                    break;
                }

                strBuilder.append((String)hosts.get(currNum)).append(",");
            }

            String str = strBuilder.toString();
            ++prefetchNum;
            if (str.length() > 0) {
                String hostsToSend = str.substring(0, str.length() - 1);
                Logger.printLog("Hosts for httpdns request is (%s) ", new Object[]{hostsToSend});
                this.asyncSendRequest(hostsToSend, RequestParamType.DNLIST_HOSTS, httpDnsCompletion);
            }
        }

    }

    void setHttps(boolean isHttps) {
        this.isHttps = isHttps;
    }

    boolean isHttps() {
        return this.isHttps;
    }

    void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    String getAccountID() {
        return this.accountID;
    }

    void setSecret(String secret) {
        this.secret = RongHttpDnsUtil.encode(secret);
        if (this.secret == null) {
            this.secret = secret;
            isSecretEncrypted = false;
        }

    }

    void setDefaultServerIp(String defaultServerIp) {
        this.defaultServerIp = defaultServerIp;
    }

    private String getSecret() {
        return !isSecretEncrypted ? this.secret : RongHttpDnsUtil.decode(this.secret);
    }

    long getRepairLocalClock() {
        return this.repairLocalClock;
    }

    boolean getPreResolveFinish() {
        return this.preResolveFinish;
    }

    void setPreResolveFinish(boolean isFinish) {
        this.preResolveFinish = isFinish;
    }

    private class AsyncHttpTask implements Runnable {
        private String hostsOrTag;
        private RequestParamType requestParamType;
        private AsyncHttpDnsCompletion httpDnsCompletion;
        private boolean needRetryBySignExpired;

        AsyncHttpTask(String hostsOrTag, RequestParamType requestParamType, AsyncHttpDnsCompletion httpDnsCompletion) {
            this.hostsOrTag = hostsOrTag;
            this.requestParamType = requestParamType;
            this.httpDnsCompletion = httpDnsCompletion;
            this.needRetryBySignExpired = false;
        }

        public void run() {
            this.startResolveRequest();
            if (this.needRetryBySignExpired) {
                Logger.printLog("Retry for %s(%s).", new Object[]{this.requestParamType.toString(), this.hostsOrTag});
                this.startResolveRequest();
            }

            String[] hosts;
            String[] var3;
            int var4;
            int var5;
            String singleHost;
            synchronized(HttpDnsClient.this.hostToResolveLock) {
                if (!this.requestParamType.equals(RequestParamType.TAG_OF_HOSTS)) {
                    hosts = this.hostsOrTag.split(",");
                    var3 = hosts;
                    var4 = hosts.length;

                    for(var5 = 0; var5 < var4; ++var5) {
                        singleHost = var3[var5];
                        HttpDnsClient.this.hostsToResolve.remove(singleHost);
                    }
                }
            }

            synchronized(HttpDnsClient.this.hostsExpiredLock) {
                if (this.requestParamType.equals(RequestParamType.DNLIST_HOSTS)) {
                    hosts = this.hostsOrTag.split(",");
                    var3 = hosts;
                    var4 = hosts.length;

                    for(var5 = 0; var5 < var4; ++var5) {
                        singleHost = var3[var5];
                        HttpDnsClient.this.hostsExpired.remove(singleHost);
                    }
                }

            }
        }

        private void startResolveRequest() {
            String url = this.getHttpDnsUrl(this.hostsOrTag, this.requestParamType);
            if (url == null) {
                this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
                Logger.printLog("Httpdns request failed for  %s(%s), get url error", new Object[]{this.requestParamType.toString(), this.hostsOrTag});
            } else {
                this.httpGet(url);
            }
        }

        private String getHttpDnsUrl(String host, RequestParamType requestParamType) {
            long t = System.currentTimeMillis() / 1000L + 300L + HttpDnsClient.this.repairLocalClock;
            String sign = HttpDnsClient.this.calcSign(host, t);
            if (sign == null) {
                return null;
            } else {
                String serverIP = HttpDnsClient.this.defaultServerIp;
                Logger.printLog("Using BGPServerIp(%s)", new Object[]{HttpDnsClient.this.defaultServerIp});
                String uri;
                if (requestParamType.equals(RequestParamType.TAG_OF_HOSTS)) {
                    uri = String.format("%s/v4/resolve?account_id=%s&tag=%s&sign=%s&t=%d&sdk_ver=%s&os_type=%s&alt_server_ip=true", serverIP, HttpDnsClient.this.accountID, host, sign, t, "1.3", "android");
                } else {
                    uri = String.format("%s/v4/resolve?account_id=%s&dn=%s&sign=%s&t=%d&sdk_ver=%s&os_type=%s&alt_server_ip=true", serverIP, HttpDnsClient.this.accountID, host, sign, t, "1.3", "android");
                }

                String url;
                if (HttpDnsClient.this.isHttps) {
                    url = String.format("https://%s", uri);
                } else {
                    url = String.format("http://%s", uri);
                }

                return url;
            }
        }

        private void httpGet(String url) {
            HttpURLConnection conn = null;
            Map<String, Result> resultsMap = new HashMap();
            long preResolveStartTime = 0L;

            long curTime;
            long timeSpan;
            try {
                URL mURL = new URL(url);
                if (HttpDnsClient.this.isHttps) {
                    HttpsURLConnection httpsConn = (HttpsURLConnection)mURL.openConnection();
                    httpsConn.setRequestProperty("Host", "httpdns.baidubce.com");
                    httpsConn.setHostnameVerifier(HttpDnsClient.httpDnsHostnameVerifier);
                    conn = httpsConn;
                } else {
                    conn = (HttpURLConnection)mURL.openConnection();
                }

                ((HttpURLConnection)conn).setRequestMethod("GET");
                ((HttpURLConnection)conn).setReadTimeout(3000);
                ((HttpURLConnection)conn).setConnectTimeout(3000);
                ((HttpURLConnection)conn).setRequestProperty("connection", "Keep-Alive");
                ((HttpURLConnection)conn).setRequestProperty("Accept-Encoding", "gzip, deflate");
                FwLog.write(3, 1, LogTag.L_DOH_T.getTag(), "nav", new Object[]{this.hostsOrTag});
                preResolveStartTime = System.currentTimeMillis();
                ((HttpURLConnection)conn).connect();
                int responseCode = ((HttpURLConnection)conn).getResponseCode();
                InputStream in;
                if (responseCode >= 400) {
                    in = ((HttpURLConnection)conn).getErrorStream();
                } else {
                    in = ((HttpURLConnection)conn).getInputStream();
                }

                if (in != null) {
                    String rspData = HttpDnsClient.this.getStringFromInputStream(in, (HttpURLConnection)conn);
                    if (rspData == null) {
                        Logger.printLog("HttpDns request failed for %s(%s), get empty response data", new Object[]{this.requestParamType.toString(), this.hostsOrTag});
                        curTime = System.currentTimeMillis();
                        timeSpan = curTime - preResolveStartTime;
                        FwLog.write(1, 1, LogTag.L_DOH_R.getTag(), "error|duration", new Object[]{101, timeSpan});
                        this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
                        return;
                    }

                    Map rspMsgMap = HttpDnsClient.this.checkRspMsg(rspData, this.hostsOrTag, this.requestParamType);
                    if (rspMsgMap.get("isSignExpired").equals(true)) {
                        curTime = System.currentTimeMillis();
                        timeSpan = curTime - preResolveStartTime;
                        FwLog.write(1, 1, LogTag.L_DOH_R.getTag(), "error|duration", new Object[]{102, timeSpan});
                        this.needRetryBySignExpired = true;
                        return;
                    }

                    if (rspMsgMap.get("isMsgOK").equals(true) && responseCode == 200) {
                        resultsMap = HttpDnsClient.this.parseRspData(rspData, this.hostsOrTag);
                    } else {
                        this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
                    }
                } else {
                    Logger.printLog("HttpDns request failed for %s(%s), get null response stream", new Object[]{this.requestParamType.toString(), this.hostsOrTag});
                    this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
                }
            } catch (IOException var20) {
                RLog.e("HttpDnsClient", "httpGet IOException", var20);
                this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
            } catch (ArrayIndexOutOfBoundsException var21) {
                RLog.e("HttpDnsClient", "httpGet ArrayIndexOutOfBoundsException", var21);
                this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
            } catch (IllegalStateException var22) {
                RLog.e("HttpDnsClient", "httpGet IllegalStateException", var22);
                this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
            } finally {
                if (conn != null) {
                    ((HttpURLConnection)conn).disconnect();
                }

            }

            curTime = System.currentTimeMillis();
            timeSpan = curTime - preResolveStartTime;
            if (resultsMap != null && !((Map)resultsMap).isEmpty() && ((Map)resultsMap).get(this.hostsOrTag) != null) {
                ArrayList<String> ipv4List = ((Result)((Map)resultsMap).get(this.hostsOrTag)).getIpv4List();
                if (ipv4List == null || ipv4List.isEmpty()) {
                    FwLog.write(1, 1, LogTag.L_DOH_R.getTag(), "error|duration", new Object[]{103, timeSpan});
                    this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)resultsMap, this.hostsOrTag);
                    return;
                }

                FwLog.write(3, 1, LogTag.L_DOH_R.getTag(), "nav|ip|duration", new Object[]{this.hostsOrTag, ipv4List.get(0), timeSpan});
                this.httpDnsCompletion.callback(0, this.requestParamType, (Map)resultsMap, this.hostsOrTag);
            } else {
                FwLog.write(1, 1, LogTag.L_DOH_R.getTag(), "error|duration", new Object[]{103, timeSpan});
                this.httpDnsCompletion.callback(-1, this.requestParamType, (Map)null, this.hostsOrTag);
            }

        }
    }

    public class Result {
        private final ArrayList<String> ipv4List;
        private final long ttl;
        private final String clientIp;

        Result(ArrayList<String> ipv4List, long ttl, String clientIp) {
            this.ipv4List = ipv4List;
            this.ttl = ttl;
            this.clientIp = clientIp;
        }

        ArrayList<String> getIpv4List() {
            return this.ipv4List;
        }

        long getTtl() {
            return this.ttl;
        }

        String getClientIp() {
            return this.clientIp;
        }
    }

    private class HttpDnsHostnameVerifier implements HostnameVerifier {
        private HttpDnsHostnameVerifier() {
        }

        public boolean verify(String s, SSLSession sslSession) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify("httpdns.baidubce.com", sslSession);
        }
    }

    interface AsyncHttpDnsCompletion {
        void callback(int var1, RequestParamType var2, Map<String, Result> var3, String var4);
    }

    public static enum RequestParamType {
        DNLIST_HOSTS,
        TAG_OF_HOSTS;

        private RequestParamType() {
        }
    }
}
