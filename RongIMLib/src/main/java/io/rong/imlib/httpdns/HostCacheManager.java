//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

class HostCacheManager {
    private final String dnsPrefix;
    private final LruCache<String, HostEntry> hostCache;
    private boolean strictCachePolicy;

    HostCacheManager(String dnsPrefix, boolean strictCachePolicy) {
        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        this.hostCache = new LruCache(maxMemory / 16);
        this.dnsPrefix = dnsPrefix;
        this.strictCachePolicy = strictCachePolicy;
    }

    void clearHostCacheMemory() {
        this.hostCache.evictAll();
        Logger.printLog("Clear %s cache", new Object[]{this.dnsPrefix});
    }

    void setHostCacheEntry(String host, HostEntry hostEntry) {
        ArrayList<String> ipv4List = hostEntry.getIpv4List();
        ArrayList<String> ipv6List = hostEntry.getIpv6List();
        if (ipv4List != null && !ipv4List.isEmpty() || ipv6List != null && !ipv6List.isEmpty()) {
            this.hostCache.put(host, hostEntry);
            Logger.printLog("Set entry to %s cache, host(%s), ipv4List(%s), ipv6List(%s), ttl(%d)", new Object[]{this.dnsPrefix, host, ipv4List != null ? ipv4List.toString() : null, ipv6List != null ? ipv6List.toString() : null, hostEntry.getTtl()});
        }
    }

    HostEntry getHostCacheEntry(String host) {
        HostEntry entry = (HostEntry)this.hostCache.get(host);
        if (entry != null && entry.isExpired() && this.strictCachePolicy) {
            this.hostCache.remove(host);
            Logger.printLog("Remove expired entry from %s cache while reading, host(%s)", new Object[]{this.dnsPrefix, host});
            return null;
        } else {
            return entry;
        }
    }

    void removeExpiredEntry(String host) {
        HostEntry hostEntry = this.getHostCacheEntry(host);
        if (hostEntry != null) {
            if (!hostEntry.isExpired()) {
                return;
            }

            this.hostCache.remove(host);
            Logger.printLog("Remove expired entry from %s cache, host(%s)", new Object[]{this.dnsPrefix, host});
        }

    }

    ArrayList<String> getAllHosts() {
        ArrayList<String> cacheHosts = new ArrayList();
        Map<String, HostEntry> cacheMap = this.hostCache.snapshot();
        Set<String> setKey = cacheMap.keySet();
        cacheHosts.addAll(setKey);
        return cacheHosts;
    }

    void setStrictCachePolicy(boolean strictCachePolicy) {
        this.strictCachePolicy = strictCachePolicy;
    }

    boolean isStrictCachePolicy() {
        return this.strictCachePolicy;
    }

    static class HostEntry {
        private ArrayList<String> ipv4List;
        private ArrayList<String> ipv6List;
        private long ttl;
        private long queryTime;
        private String clientIp;

        HostEntry() {
        }

        public boolean isExpired() {
            return this.getQueryTime() + this.ttl < System.currentTimeMillis() / 1000L;
        }

        ArrayList<String> getIpv4List() {
            return this.ipv4List;
        }

        ArrayList<String> getIpv6List() {
            return this.ipv6List;
        }

        void setIpv4List(ArrayList<String> ipv4List) {
            this.ipv4List = ipv4List;
        }

        void setIpv6List(ArrayList<String> ipv6List) {
            this.ipv6List = ipv6List;
        }

        long getTtl() {
            return this.ttl;
        }

        void setTtl(long ttl) {
            this.ttl = ttl;
        }

        long getQueryTime() {
            return this.queryTime;
        }

        void setQueryTime(long queryTime) {
            this.queryTime = queryTime;
        }

        public String getClientIp() {
            return this.clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }
    }
}
