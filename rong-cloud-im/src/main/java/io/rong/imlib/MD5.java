//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import androidx.annotation.NonNull;
import io.rong.common.RLog;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static final String TAG = "MD5";
    private static final String[] strDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public MD5() {
    }

    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (bByte < 0) {
            iRet = bByte + 256;
        }

        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        byte[] var2 = bByte;
        int var3 = bByte.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte b = var2[var4];
            sBuffer.append(byteToArrayString(b));
        }

        return sBuffer.toString();
    }

    public static String encrypt(@NonNull String str) {
        String result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteToString(md.digest(str.getBytes()));
        } catch (NoSuchAlgorithmException var3) {
            RLog.e("MD5", "encrypt", var3);
        }

        return result;
    }

    public static String encrypt(String str, boolean lowerCase) {
        String result = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            result = byteToString(md.digest(str.getBytes()));
            if (lowerCase) {
                result = result.toLowerCase();
            }
        } catch (NoSuchAlgorithmException var4) {
            RLog.e("MD5", "encrypt", var4);
        }

        return result;
    }
}
