package com.library.networklib.java.sp;

import java.util.Map;

import top.sunny8519.utilslib.hawk.Hawk;

/**
 * @author niyang
 * @date 2019/3/26
 */
public class NetSP {

    private static final String KEY_NET_HEADERS_PARAMS = "KEY_NET_HEADERS_PARAMS";

    private NetSP() {
    }

    static class NetSPHolder {
        static final NetSP NET_SP = new NetSP();
    }

    public static NetSP getInstance() {
        return NetSPHolder.NET_SP;
    }

    public synchronized void saveNetHeaderParams(Map<String, String> params) {
        Hawk.put(KEY_NET_HEADERS_PARAMS, params);
    }

    public synchronized Map<String, String> getNetHeaderParams() {
        return Hawk.get(KEY_NET_HEADERS_PARAMS);
    }
}
