//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import io.rong.common.rlog.RLog;

final class RongHttpDnsUtil {
    private static final String TAG = "RongHttpDnsUtil";
    private static final String IPV4_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
    private static Pattern ipv4Pattern = Pattern.compile("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$");
    private static final String HEX = "0123456789ABCDEF";
    private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";
    private static final String IVPARAMETERSPEC = "01020304";
    private static final String ALGORITHM = "DES";
    private static final String SHA1PRNG = "SHA1PRNG";
    private static String dynamickey = generateKey();

    private RongHttpDnsUtil() {
    }

    static boolean validateIpv4(String ip) {
        Matcher matcher = ipv4Pattern.matcher(ip);
        return matcher.matches();
    }

    static boolean validateIpv6(String ip) {
        ip = ip.replaceAll("[\\[\\]]", "");
        return isIPv6Std(ip) || isIPV6Compress(ip);
    }

    private static boolean isIPv6Std(String ip) {
        return Pattern.matches("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$", ip);
    }

    private static boolean isIPV6Compress(String ip) {
        return Pattern.matches("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$", ip);
    }

    static String md5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException var7) {
            RLog.e("RongHttpDnsUtil", "md5 NoSuchAlgorithmException", var7);
            return null;
        } catch (UnsupportedEncodingException var8) {
            RLog.e("RongHttpDnsUtil", "md5 UnsupportedEncodingException", var8);
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var3 = hash;
        int var4 = hash.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if ((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }

    private static String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            return toHex(bytes_key);
        } catch (Exception var2) {
            RLog.e("RongHttpDnsUtil", "generateKey Exception", var2);
            return null;
        }
    }

    private static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        } else {
            StringBuffer result = new StringBuffer(2 * buf.length);
            byte[] var2 = buf;
            int var3 = buf.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte b = var2[var4];
                appendHex(result, b);
            }

            return result.toString();
        }
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append("0123456789ABCDEF".charAt(b >> 4 & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }

    private static Key getRawKey(String key) throws Exception {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        return keyFactory.generateSecret(dks);
    }

    static String encode(String data) {
        return encode(dynamickey, data.getBytes());
    }

    private static String encode(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("01020304".getBytes());
            cipher.init(1, getRawKey(key), iv);
            byte[] bytes = cipher.doFinal(data);
            return Base64.encodeToString(bytes, 0);
        } catch (Exception var5) {
            return null;
        }
    }

    static String decode(String data) {
        return decode(dynamickey, Base64.decode(data, 0));
    }

    private static String decode(String key, byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec("01020304".getBytes());
            cipher.init(2, getRawKey(key), iv);
            byte[] original = cipher.doFinal(data);
            return new String(original);
        } catch (Exception var5) {
            return null;
        }
    }
}
