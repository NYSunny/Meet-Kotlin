//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.rong.common.rlog.RLog;
import io.rong.imlib.httpdns.HttpDnsCompletion;
import io.rong.imlib.httpdns.RongHttpDns;
import io.rong.imlib.httpdns.RongHttpDnsResult;
import io.rong.imlib.httpdns.RongHttpDns.CachePolicy;
import io.rong.imlib.httpdns.RongHttpDns.CompletionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class HttpDnsManager {
    private static final String TAG = "HttpDnsManager";
    private static final int TIMEOUT = 3000;
    private ArrayList<String> hosts;
    private CountDownLatch countDownLatch;
    private boolean enableHttpDns;
    private static final String NAVIGATION_HOST_SUFFIX = "ronghub.com";

    private HttpDnsManager() {
        this.hosts = new ArrayList();
        this.countDownLatch = new CountDownLatch(1);
        this.enableHttpDns = true;
    }

    public static HttpDnsManager getInstance() {
        return HttpDnsManager.HttpDnsManagerHolder.instance;
    }

    public void initHttpDns(Context context, NativeObject nativeObject) {
        RongHttpDns httpDns = RongHttpDns.getService(context);
        httpDns.setLogEnable(true);

        try {
            httpDns.setAccountID(nativeObject.GetHttpDnsAccountId());
            httpDns.setSecret(nativeObject.GetHttpDnsSecret());
        } catch (IllegalArgumentException var5) {
            RLog.e("HttpDnsManager", "initHttpDns", var5);
        }

        httpDns.setHttpsRequestEnable(true);
        httpDns.setNetworkSwitchPolicy(true, false);
        httpDns.setCachePolicy(CachePolicy.POLICY_TOLERANT);
    }

    public void shouldEnableHttpDns(List<String> serverAddressList) {
        this.enableHttpDns = false;
        if (serverAddressList != null && serverAddressList.size() > 0) {
            Iterator var2 = serverAddressList.iterator();

            while(var2.hasNext()) {
                String address = (String)var2.next();
                if (!TextUtils.isEmpty(address)) {
                    Uri uri = Uri.parse(address);
                    String host = uri.getHost();
                    if (host != null && host.endsWith("ronghub.com")) {
                        this.enableHttpDns = true;
                        break;
                    }
                }
            }
        }

    }

    public HashMap<String, String> getHttpDnsIps(@NonNull Context context, String host) {
        HashMap<String, String> ipMap = new HashMap();
        if (!this.enableHttpDns) {
            ipMap.put("resolveIp", "");
            ipMap.put("clientIp", "");
            return ipMap;
        } else {
            final CountDownLatch countDownLatchRequestDns = new CountDownLatch(1);
            RongHttpDnsResult httpDnsResult = RongHttpDns.getService(context).syncResolve(host);
            ArrayList<String> ipv4List = httpDnsResult.getIpv4List();
            final String[] resolveIp = new String[1];
            String clientIp = httpDnsResult.getClientIp();
            RLog.i("HttpDnsManager", "host = " + host + ", + ipv4List = " + ipv4List + ", clientIp = " + clientIp);
            if (ipv4List != null && !ipv4List.isEmpty()) {
                resolveIp[0] = (String)ipv4List.get(0);
            } else {
                HttpDnsCompletion completion = new HttpDnsCompletion(context) {
                    protected void onSuccess(ArrayList<String> hosts) {
                        resolveIp[0] = (String)hosts.get(0);
                        RLog.i("HttpDnsManager", "resolveIp success = " + resolveIp[0]);
                        countDownLatchRequestDns.countDown();
                    }

                    protected void onFailed(int code) {
                        resolveIp[0] = "";
                        RLog.i("HttpDnsManager", "resolveIp fail = " + resolveIp[0]);
                        countDownLatchRequestDns.countDown();
                    }
                };
                RongHttpDns.getService(context).asyncResolve(host, new CompletionHandler() {
                    public void completionHandler(RongHttpDnsResult result) {
                    }
                }, completion);

                try {
                    if (countDownLatchRequestDns.await(3000L, TimeUnit.MILLISECONDS)) {
                        RLog.i("HttpDnsManager", "request dns ip countDownLatch is success");
                        RongHttpDnsResult requestDnsResult = RongHttpDns.getService(context).syncResolve(host);
                        clientIp = requestDnsResult.getClientIp();
                        RLog.i("HttpDnsManager", "request clientIp = " + clientIp);
                    } else {
                        RLog.i("HttpDnsManager", "request dns ip countDownLatch is timeout");
                    }
                } catch (InterruptedException var11) {
                    RLog.e("HttpDnsManager", "request dns ip", var11);
                    Thread.currentThread().interrupt();
                }
            }

            ipMap.put("resolveIp", resolveIp[0]);
            ipMap.put("clientIp", clientIp);
            return ipMap;
        }
    }

    void asyncSolveDnsIp(Context context, String host, CompletionHandler handler, HttpDnsCompletion completion) {
        RongHttpDns.getService(context).asyncResolve(host, handler, completion);
    }

    private static class HttpDnsManagerHolder {
        private static final HttpDnsManager instance = new HttpDnsManager();

        private HttpDnsManagerHolder() {
        }
    }
}
