package com.library.networklib.auth;

import android.annotation.SuppressLint;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * https认证
 *
 * @author Johnny
 */
public final class SSLHelper {

    private static final String PROTOCOL_TYPE = "TLS";
    private static final String CLIENT_CER_LIB_TYPE = "BKS";
    private static final String CER_STANDARD = "X.509";

    /**
     * 对外提供{@link SSLSocketFactory}和{@link X509TrustManager}两个对象
     * 用于构建OkHttp
     */
    public static class SSLParams {
        public SSLSocketFactory sslSocketFactory;
        public X509TrustManager x509TrustManager;
    }

    private SSLHelper() {
    }

    /**
     * 客户端不验证服务端证书(有很大安全风险)
     *
     * @return {@link SSLParams}
     */
    public static SSLParams getSSLParams() {
        return getSSLParamsRoot(null, null, null);
    }

    /**
     * https单向认证
     * 使用用户自定义的TrustManager来校验服务端证书
     *
     * @param x509TrustManager 用户自定义的TrustManager
     * @return {@link SSLParams}
     */
    public static SSLParams getSSLParams(X509TrustManager x509TrustManager) {
        return getSSLParamsRoot(x509TrustManager, null, null);
    }

    /**
     * https单向认证
     * 根据客户端预埋的服务端证书使用系统定义的TrustManager来校验服务端证书
     *
     * @param certificates 客户端预埋的服务端证书流
     * @return {@link SSLParams}
     */
    public static SSLParams getSSLParams(InputStream... certificates) {
        return getSSLParamsRoot(null, null, null, certificates);
    }

    /**
     * https双向认证
     * 使用用户自定义的TrustManager来校验服务端证书
     *
     * @param bksFile      客户端证书文件流(注：这不是预埋的服务端证书)
     * @param bksPassword  客户端证书密码
     * @param trustManager 自定义TrustManager
     * @return {@link SSLParams}
     */
    public static SSLParams getSSLParams(InputStream bksFile,
                                         String bksPassword,
                                         X509TrustManager trustManager) {
        return getSSLParamsRoot(trustManager, bksFile, bksPassword);
    }

    /**
     * https双向认证
     * 根据客户端预埋的服务端证书使用系统定义的TrustManager来校验服务端证书
     *
     * @param bksFile      客户端证书文件流(注：这不是预埋的服务端证书)
     * @param bksPassword  客户端证书密码
     * @param certificates 预埋的服务端证书流
     * @return {@link SSLParams}
     */
    public static SSLParams getSSLParams(InputStream bksFile,
                                         String bksPassword,
                                         InputStream... certificates) {
        return getSSLParamsRoot(null, bksFile, bksPassword, certificates);
    }

    private static SSLParams getSSLParamsRoot(@Nullable X509TrustManager trustManager,
                                              @Nullable InputStream bksFile,
                                              @Nullable String bksPassword,
                                              @Nullable InputStream... certificates) {
        final SSLParams sslParams = new SSLParams();
        final KeyManager[] keyManagers = prepareKeyManagers(bksFile, bksPassword);
        final TrustManager[] trustManagers = prepareTrustManagers(certificates);
        X509TrustManager x509TrustManager;
        if (trustManager != null) {
            // 优先使用用户自定义TrustManager
            x509TrustManager = trustManager;
        } else if (trustManagers != null) {
            // 没有用户自定义的TrustManager就使用系统默认的TrustManager
            x509TrustManager = chooseX509TrustManager(trustManagers);
        } else {
            // 用户客户端没有配置证书就不对服务端证书做校验(有很大安全风险)
            x509TrustManager = UNSAFE_TRUST_MANAGER;
        }
        try {
            // 创建TLS类型的SSLContext对象
            final SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            // 用上面的KeyManager[]和X509TrustManager初始化SSLContext
            // KeyManager[]简单理解就是给服务器做校验用的，如果这个参数有值，并且第二个参数也有值，当前https是双向验证(客户端校验
            // 服务端证书，服务端也校验客户端证书)
            // 如果没有KeyManager[]，只有TrustManager[]，当前https是单向验证(仅客户端校验服务端证书)
            sslContext.init(keyManagers, new TrustManager[]{x509TrustManager}, null);
            sslParams.sslSocketFactory = sslContext.getSocketFactory();
            sslParams.x509TrustManager = x509TrustManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslParams;
    }

    @Nullable
    private static X509TrustManager chooseX509TrustManager(@NonNull TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    /**
     * 获取证书管理器
     *
     * @param certificates 证书文件输入流(这里的证书一般是服务器上的证书，服务器返回证书需要用它来校验)
     * @return TrustManager数组
     */
    @Nullable
    private static TrustManager[] prepareTrustManagers(@Nullable InputStream... certificates) {
        TrustManager[] trustManagers = null;
        if (certificates == null || certificates.length <= 0) {
            return null;
        }
        try {
            // 获取X.509标准的证书工厂CertificateFactory对象
            final CertificateFactory cf = CertificateFactory.getInstance(CER_STANDARD);
            // 创建一个默认类型的证书库，存储我们信任的证书
            final KeyStore defaultKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            defaultKeyStore.load(null);
            int index = 0;
            for (InputStream stream : certificates) {
                // 生成证书别名
                final String certificateAlias = Integer.toString(index++);
                // 证书工厂根据证书文件的流生成证书cert
                final Certificate certificate = cf.generateCertificate(stream);
                // 将cert作为可信任证书放入到KeyStore中
                defaultKeyStore.setCertificateEntry(certificateAlias, certificate);
                if (stream != null) {
                    stream.close();
                }
            }
            // 创建一个默认类型的TrustManagerFactory
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // 用之前存放可信任证书的KeyStore初始化TrustManagerFactory，这样tmf就会信任KeyStore中的证书，
            // 这样就可以用这些证书来校验服务器证书的合法性
            tmf.init(defaultKeyStore);
            trustManagers = tmf.getTrustManagers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trustManagers;
    }

    /**
     * 根据客户端证书和密码生成KeyManager数组，用于给服务器验证客户端证书(双向验证中的服务器验证)
     *
     * @param bksFile     客户端证书
     * @param bksPassword 证书密码
     * @return KeyManager数组
     */
    @Nullable
    private static KeyManager[] prepareKeyManagers(@Nullable InputStream bksFile, @Nullable String bksPassword) {
        KeyManager[] keyManagers = null;
        if (bksFile == null || bksPassword == null) {
            return null;
        }
        try {
            final char[] passwordCharArray = bksPassword.toCharArray();
            // 获取KeyStore(证书库)，android端仅支持BKS类型的证书库存储客户端证书
            final KeyStore clientKeyStore = KeyStore.getInstance(CLIENT_CER_LIB_TYPE);
            // 把证书加载进KeyStore
            clientKeyStore.load(bksFile, passwordCharArray);
            // 使用已加载了客户端证书的KeyStore初始化KeyManagerFactory
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientKeyStore, passwordCharArray);
            // 利用KeyManagerFactory这个工厂类生成KeyManager[]
            keyManagers = kmf.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyManagers;
    }

    private static final X509TrustManager UNSAFE_TRUST_MANAGER = new X509TrustManager() {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Nothing to do
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Nothing to do
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
}
