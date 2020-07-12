//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.utils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

public class SSLUtils {
    private static HostnameVerifier sHostnameVerifier;
    private static SSLContext sSSLContext;

    public SSLUtils() {
    }

    public static SSLContext getSSLContext() {
        return sSSLContext;
    }

    public static void setSSLContext(SSLContext sslContext) {
        sSSLContext = sslContext;
    }

    public static void setHostnameVerifier(HostnameVerifier verifier) {
        sHostnameVerifier = verifier;
    }

    public static HostnameVerifier getHostVerifier() {
        return sHostnameVerifier;
    }
}
