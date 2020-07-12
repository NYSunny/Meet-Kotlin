//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class CertificateTrustManager implements X509TrustManager {
    private final List<byte[]> keys;

    public CertificateTrustManager(List<String> certificates) throws CertificateException {
        if (certificates != null && certificates.size() != 0) {
            this.keys = new ArrayList();
            Iterator var2 = certificates.iterator();

            while(var2.hasNext()) {
                String key = (String)var2.next();
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                Certificate cert = cf.generateCertificate(new ByteArrayInputStream(Base64.decode(key, 0)));
                this.keys.add(cert.getPublicKey().getEncoded());
            }

        } else {
            throw new IllegalArgumentException("You must specify non-empty keys list");
        }
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("PublicKeyManager: X509Certificate array is null");
        } else if (chain.length <= 0) {
            throw new IllegalArgumentException("PublicKeyManager: X509Certificate is empty");
        } else if (null != authType && authType.equalsIgnoreCase("RSA")) {
            try {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                tmf.init((KeyStore)null);
                TrustManager[] var4 = tmf.getTrustManagers();
                int var5 = var4.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    TrustManager trustManager = var4[var6];
                    ((X509TrustManager)trustManager).checkServerTrusted(chain, authType);
                }
            } catch (Exception var8) {
                throw new CertificateException(var8);
            }

            byte[] server = chain[0].getPublicKey().getEncoded();
            Iterator var10 = this.keys.iterator();

            byte[] key;
            do {
                if (!var10.hasNext()) {
                    throw new CertificateException("Public keys didn't pass checks");
                }

                key = (byte[])var10.next();
            } while(!Arrays.equals(key, server));

        } else {
            throw new CertificateException("PublicKeyManager: AuthType is not RSA");
        }
    }

    public void checkClientTrusted(X509Certificate[] xcs, String string) {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
