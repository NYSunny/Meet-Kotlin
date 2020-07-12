//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import io.rong.imlib.httpdns.HostCacheManager.HostEntry;
import io.rong.imlib.httpdns.HttpDnsClient.AsyncHttpDnsCompletion;
import io.rong.imlib.httpdns.HttpDnsClient.RequestParamType;
import io.rong.imlib.httpdns.HttpDnsClient.Result;
import io.rong.imlib.httpdns.RongHttpDns.CachePolicy;

public class HttpDnsCompletion implements AsyncHttpDnsCompletion {
    private static final String TAG = "HttpDnsCompletion";
    private final HostCacheManager httpDnsCache;
    private final RongHttpDns httpDns;
    private final CachePolicy cachePolicy;
    private final HttpDnsClient httpDnsClient;

    public HttpDnsCompletion(Context context) {
        this.httpDns = RongHttpDns.getService(context);
        this.httpDnsCache = this.httpDns.getHttpDnsCache();
        this.cachePolicy = this.httpDns.getCachePolicy();
        this.httpDnsClient = this.httpDns.getHttpDnsClient();
    }

    public void callback(int ret, RequestParamType requestParamType, Map<String, Result> resultsMap, String hostsOrTag) {
        label46:
        switch(ret) {
            case -1:
                if (requestParamType.equals(RequestParamType.DNLIST_HOSTS) && this.cachePolicy == CachePolicy.POLICY_TOLERANT) {
                    String[] hosts = hostsOrTag.split(",");
                    String[] var11 = hosts;
                    int var12 = hosts.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        String singleHost = var11[var13];
                        this.httpDnsCache.removeExpiredEntry(singleHost);
                    }
                }

                this.onFailed(-1);
                break;
            case 0:
                Iterator var5 = resultsMap.entrySet().iterator();

                while(true) {
                    if (!var5.hasNext()) {
                        break label46;
                    }

                    Entry<String, Result> resultMap = (Entry)var5.next();
                    String singleHost = (String)resultMap.getKey();
                    Result result = (Result)resultMap.getValue();
                    if (result != null) {
                        HostEntry hostEntry = new HostEntry();
                        hostEntry.setTtl(result.getTtl());
                        hostEntry.setQueryTime(System.currentTimeMillis() / 1000L);
                        hostEntry.setIpv4List(result.getIpv4List());
                        hostEntry.setClientIp(result.getClientIp());
                        this.httpDnsCache.setHostCacheEntry(singleHost, hostEntry);
                        this.onSuccess(result.getIpv4List());
                    } else {
                        if (this.cachePolicy == CachePolicy.POLICY_TOLERANT) {
                            this.httpDnsCache.removeExpiredEntry(singleHost);
                        }

                        this.onFailed(-1);
                    }
                }
            default:
                this.onFailed(-1);
                Logger.printLog("Internal error: async httpdns resolve completion get error ret(%d)", new Object[]{ret});
        }

        if (this.httpDns.getPreResolveNum() > 0 && !this.httpDnsClient.getPreResolveFinish()) {
            this.httpDnsClient.setPreResolveFinish(true);
            Logger.printLog("preResolve has finished", new Object[0]);
        }

    }

    protected void onSuccess(ArrayList<String> hosts) {
        Log.i("HttpDnsCompletion", "onSuccess()");
    }

    protected void onFailed(int code) {
        Log.i("HttpDnsCompletion", "onFailed()");
    }
}
