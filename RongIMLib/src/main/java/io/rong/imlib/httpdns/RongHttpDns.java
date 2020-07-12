//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import android.content.Context;
import android.content.IntentFilter;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import io.rong.imlib.httpdns.HostCacheManager.HostEntry;
import io.rong.imlib.httpdns.HttpDnsClient.RequestParamType;
import io.rong.imlib.httpdns.RongHttpDnsResult.ResolveStatus;
import io.rong.imlib.httpdns.RongHttpDnsResult.ResolveType;

public final class RongHttpDns {
    private static volatile RongHttpDns httpDns;
    private final HttpDnsClient httpDnsClient = HttpDnsClient.getInstance();
    private final HostCacheManager httpDnsCache = new HostCacheManager("HTTPDNS", false);
    private static final int ACCOUNT_ID_MAX_LEN = 64;
    private static final int SECRET_MAX_LEN = 64;
    private static final int SECRET_MIN_LEN = 8;
    private RongNetworkStateChangeReceiver networkStateChangeReceiver;
    private final Context context;
    private CachePolicy cachePolicy;
    private long preResolveStartTime;
    private long lastReqTimeForExpiredHosts;
    private int preResolveNum;
    private String[] hostWhiteList = new String[]{"nav.cn.ronghub.com", "rtc-info.ronghub.com"};

    private RongHttpDns(Context context) {
        this.cachePolicy = CachePolicy.POLICY_TOLERANT;
        this.context = context;
        this.registerNetworkChangeReceiver();
        this.networkStateChangeReceiver.refreshIpReachable();
        this.lastReqTimeForExpiredHosts = System.currentTimeMillis();
    }

    private void registerNetworkChangeReceiver() {
        this.networkStateChangeReceiver = new RongNetworkStateChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.context.registerReceiver(this.networkStateChangeReceiver, filter);
    }

    public static RongHttpDns getService(Context context) {
        if (httpDns == null) {
            Class var1 = RongHttpDns.class;
            synchronized(RongHttpDns.class) {
                if (httpDns == null) {
                    httpDns = new RongHttpDns(context);
                }
            }
        }

        return httpDns;
    }

    public void setCachePolicy(CachePolicy policy) {
        this.cachePolicy = policy;
        if (policy == CachePolicy.POLICY_STRICT) {
            this.httpDnsCache.setStrictCachePolicy(true);
        } else {
            this.httpDnsCache.setStrictCachePolicy(false);
        }

        Logger.printLog("Set cache policy to %s", new Object[]{policy.name()});
    }

    public void setPreResolveTag(String tag) {
        if (tag != null && !tag.isEmpty()) {
            ++this.preResolveNum;
            if (this.preResolveNum > 1) {
                Logger.printLog("You have already set PreResolveHosts, it is best to set it only once.", new Object[0]);
            }

            Logger.printLog(" Set preResolve tag : %s", new Object[]{tag});
            HttpDnsCompletion httpDnsCompletion = new HttpDnsCompletion(this.context);
            this.preResolveStartTime = System.currentTimeMillis();
            this.httpDnsClient.asyncSendRequest(tag, RequestParamType.TAG_OF_HOSTS, httpDnsCompletion);
        } else {
            Logger.printLog("Set pre resolve hosts error, get empty tag", new Object[0]);
        }
    }

    public void setPreResolveHosts(ArrayList<String> hosts, HttpDnsCompletion completion) {
        Iterator iterator = hosts.iterator();

        while(iterator.hasNext()) {
            String host = (String)iterator.next();
            if (this.filterHostRequest(host)) {
                iterator.remove();
            }
        }

        if (hosts.size() <= 0) {
            Logger.printLog("Set pre resolve hosts error, get empty hosts", new Object[0]);
        } else {
            hosts = new ArrayList(new HashSet(hosts));
            int maxHostNum = this.httpDnsClient.getMaxHostNum();
            if (hosts.size() > maxHostNum) {
                Logger.printLog("The current number of hosts is %d, and the max supported size is %s.Please reduce it to %s or less.", new Object[]{hosts.size(), maxHostNum, maxHostNum});
            } else {
                ++this.preResolveNum;
                if (this.preResolveNum > 1) {
                    Logger.printLog("You have already set PreResolveHosts, it is best to set it only once.", new Object[0]);
                }

                this.preResolveStartTime = System.currentTimeMillis();
                StringBuilder str = new StringBuilder();

                for(int index = 0; index < hosts.size(); ++index) {
                    str.append((String)hosts.get(index)).append(",");
                }

                if (str.length() > 0) {
                    String hostsToSend = str.substring(0, str.length() - 1);
                    Logger.printLog("Set pre resolve hosts: %s", new Object[]{hostsToSend});
                    HttpDnsCompletion httpDnsCompletion;
                    if (completion == null) {
                        httpDnsCompletion = new HttpDnsCompletion(this.context);
                    } else {
                        httpDnsCompletion = completion;
                    }

                    this.httpDnsClient.asyncSendRequest(hostsToSend, RequestParamType.DNLIST_HOSTS, httpDnsCompletion);
                }
            }

        }
    }

    public void setNetworkSwitchPolicy(boolean clearCache, boolean httpDnsPrefetch) {
        this.networkStateChangeReceiver.setClearCache(clearCache);
        this.networkStateChangeReceiver.setHttpDnsPrefetch(httpDnsPrefetch);
        Logger.printLog("Set network change policy, clearCache(%b), httpDnsPrefetch(%b)", new Object[]{clearCache, httpDnsPrefetch});
    }

    public void setAccountID(String accountID) {
        if (accountID.length() > 64) {
            throw new IllegalArgumentException("accountID length(" + accountID.length() + ") is bigger than 64");
        } else {
            this.httpDnsClient.setAccountID(accountID);
        }
    }

    public void setSecret(String secret) {
        int len = secret.length();
        if (len <= 64 && len >= 8) {
            this.httpDnsClient.setSecret(secret);
        } else {
            throw new IllegalArgumentException("secret length(" + secret.length() + ") check failed");
        }
    }

    public RongHttpDnsResult syncResolve(String host) {
        ArrayList ipv6List;
        if (RongHttpDnsUtil.validateIpv4(host)) {
            ipv6List = new ArrayList();
            ipv6List.add(host);
            return new RongHttpDnsResult(ResolveType.RESOLVE_NONEED, ResolveStatus.BDHttpDnsResolveOK, ipv6List, (ArrayList)null);
        } else if (RongHttpDnsUtil.validateIpv6(host)) {
            ipv6List = new ArrayList();
            host = host.replaceAll("[\\[\\]]", "");
            ipv6List.add(host);
            return new RongHttpDnsResult(ResolveType.RESOLVE_NONEED, ResolveStatus.BDHttpDnsResolveOK, (ArrayList)null, ipv6List);
        } else {
            ResolveType resolveType = ResolveType.RESOLVE_NONE;
            HostEntry httpDnsEntry = this.httpDnsCache.getHostCacheEntry(host);
            if (httpDnsEntry != null) {
                if (httpDnsEntry.isExpired()) {
                    resolveType = ResolveType.RESOLVE_FROM_HTTPDNS_EXPIRED_CACHE;
                } else {
                    resolveType = ResolveType.RESOLVE_FROM_HTTPDNS_CACHE;
                }

                Logger.printLog("Sync resolve successful, host(%s) ipv4List(%s) ipv6List(null) resolveType(%s)", new Object[]{host, httpDnsEntry.getIpv4List().toString(), resolveType.toString()});
                return new RongHttpDnsResult(resolveType, ResolveStatus.BDHttpDnsResolveOK, httpDnsEntry.getIpv4List(), httpDnsEntry.getIpv6List(), httpDnsEntry.getClientIp());
            } else {
                Logger.printLog("Sync resolve failed, host(%s), find no httpdns cache entry", new Object[]{host});
                return new RongHttpDnsResult(resolveType, ResolveStatus.BDHttpDnsResolveErrorCacheMiss, (ArrayList)null, (ArrayList)null);
            }
        }
    }

    public void asyncResolve(String host, final CompletionHandler callback, HttpDnsCompletion completion) {
        final ArrayList ipv6List;
        if (RongHttpDnsUtil.validateIpv4(host)) {
            ipv6List = new ArrayList();
            ipv6List.add(host);
            ThreadPool.getInstance().getExecutor().execute(new Runnable() {
                public void run() {
                    callback.completionHandler(new RongHttpDnsResult(ResolveType.RESOLVE_NONEED, ResolveStatus.BDHttpDnsResolveOK, ipv6List, (ArrayList)null));
                }
            });
        } else if (RongHttpDnsUtil.validateIpv6(host)) {
            ipv6List = new ArrayList();
            host = host.replaceAll("[\\[\\]]", "");
            ipv6List.add(host);
            ThreadPool.getInstance().getExecutor().execute(new Runnable() {
                public void run() {
                    callback.completionHandler(new RongHttpDnsResult(ResolveType.RESOLVE_NONEED, ResolveStatus.BDHttpDnsResolveOK, (ArrayList)null, ipv6List));
                }
            });
        } else {
            final HostEntry httpDnsEntry = this.httpDnsCache.getHostCacheEntry(host);
            long curTime = System.currentTimeMillis();
            ArrayList<String> hostsToSend = new ArrayList();
            if (this.allowSendRequest(curTime)) {
                if (httpDnsEntry == null || httpDnsEntry.isExpired()) {
                    hostsToSend.add(host);
                }

                HttpDnsCompletion httpDnsCompletion;
                if (completion == null) {
                    httpDnsCompletion = new HttpDnsCompletion(this.context);
                } else {
                    httpDnsCompletion = completion;
                }

                this.httpDnsClient.splitHostsAndSendRequest(hostsToSend, httpDnsCompletion);
            } else {
                Logger.printLog("please wait a moment to send request for %s, until preResolve finished or has passed 1000ms ", new Object[]{host});
            }

            if (httpDnsEntry != null) {
                final ResolveType resolveType;
                if (httpDnsEntry.isExpired()) {
                    resolveType = ResolveType.RESOLVE_FROM_HTTPDNS_EXPIRED_CACHE;
                } else {
                    resolveType = ResolveType.RESOLVE_FROM_HTTPDNS_CACHE;
                }

                Logger.printLog("Async resolve successful, host(%s) ipv4List(%s) resolveType(%s)", new Object[]{host, httpDnsEntry.getIpv4List().toString(), resolveType.toString()});
                ThreadPool.getInstance().getExecutor().execute(new Runnable() {
                    public void run() {
                        callback.completionHandler(new RongHttpDnsResult(resolveType, ResolveStatus.BDHttpDnsResolveOK, httpDnsEntry.getIpv4List(), (ArrayList)null));
                    }
                });
            } else {
                Logger.printLog("Async resolve failed, host(%s), find no httpdns cache entry ", new Object[]{host});
                ThreadPool.getInstance().getExecutor().execute(new Runnable() {
                    public void run() {
                        callback.completionHandler(new RongHttpDnsResult(ResolveType.RESOLVE_NONE, ResolveStatus.BDHttpDnsResolveErrorCacheMiss, (ArrayList)null, (ArrayList)null));
                    }
                });
            }

        }
    }

    private boolean filterHostRequest(String host) {
        return !TextUtils.isEmpty(host) && !Arrays.asList(this.hostWhiteList).contains(host);
    }

    private boolean allowSendRequest(long curTime) {
        return this.httpDnsClient.getPreResolveFinish() || curTime - this.preResolveStartTime > 1000L && !this.networkStateChangeReceiver.isIPv6Only();
    }

    public void setHttpsRequestEnable(boolean httpsEnabled) {
        this.httpDnsClient.setHttps(httpsEnabled);
        Logger.printLog("Set https enabled to %b", new Object[]{httpsEnabled});
    }

    public void setLogEnable(boolean debugEnabled) {
        Logger.setLogEnable(debugEnabled);
        Logger.printLog("Set debug log enabled to %b", new Object[]{debugEnabled});
    }

    public void setServerIp(String defaultServerIp) {
        this.httpDnsClient.setDefaultServerIp(defaultServerIp);
    }

    RongNetworkStateChangeReceiver getNetworkStateChangeReceiver() {
        return this.networkStateChangeReceiver;
    }

    HostCacheManager getHttpDnsCache() {
        return this.httpDnsCache;
    }

    CachePolicy getCachePolicy() {
        return this.cachePolicy;
    }

    HttpDnsClient getHttpDnsClient() {
        return this.httpDnsClient;
    }

    int getPreResolveNum() {
        return this.preResolveNum;
    }

    public interface CompletionHandler {
        void completionHandler(RongHttpDnsResult var1);
    }

    public static enum CachePolicy {
        POLICY_AGGRESSIVE,
        POLICY_TOLERANT,
        POLICY_STRICT;

        private CachePolicy() {
        }
    }
}
