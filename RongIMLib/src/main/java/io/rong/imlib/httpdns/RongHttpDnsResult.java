//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import java.util.ArrayList;

public class RongHttpDnsResult {
    private ResolveType resolveType;
    private final ResolveStatus resolveStatus;
    private ArrayList<String> ipv4List;
    private ArrayList<String> ipv6List;
    private String clientIp;

    public RongHttpDnsResult(ResolveStatus resolveStatus) {
        this.resolveType = ResolveType.RESOLVE_NONE;
        this.resolveStatus = resolveStatus;
    }

    RongHttpDnsResult(ResolveType resolveType, ResolveStatus resolveStatus, ArrayList<String> ipv4List, ArrayList<String> ipv6List) {
        this(resolveType, resolveStatus, ipv4List, ipv6List, "");
    }

    RongHttpDnsResult(ResolveType resolveType, ResolveStatus resolveStatus, ArrayList<String> ipv4List, ArrayList<String> ipv6List, String clientIp) {
        this.resolveType = ResolveType.RESOLVE_NONE;
        this.resolveType = resolveType;
        this.resolveStatus = resolveStatus;
        this.ipv4List = ipv4List;
        this.ipv6List = ipv6List;
        this.clientIp = clientIp;
    }

    public ResolveType getResolveType() {
        return this.resolveType;
    }

    public ResolveStatus getResolveStatus() {
        return this.resolveStatus;
    }

    public ArrayList<String> getIpv4List() {
        return this.ipv4List;
    }

    public ArrayList<String> getIpv6List() {
        return this.ipv6List;
    }

    public String getClientIp() {
        return this.clientIp;
    }

    public static enum ResolveStatus {
        BDHttpDnsResolveOK,
        BDHttpDnsInputError,
        BDHttpDnsResolveErrorCacheMiss;

        private ResolveStatus() {
        }
    }

    public static enum ResolveType {
        RESOLVE_NONE,
        RESOLVE_NONEED,
        RESOLVE_FROM_HTTPDNS_CACHE,
        RESOLVE_FROM_HTTPDNS_EXPIRED_CACHE;

        private ResolveType() {
        }
    }
}
