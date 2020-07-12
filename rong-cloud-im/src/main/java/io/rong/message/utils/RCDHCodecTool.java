//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message.utils;

import io.rong.common.RLog;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class RCDHCodecTool {
    private static final String TAG = RCDHCodecTool.class.getName();
    private static final String pStrDefault = "25135566567101483196994790440833279750474660393232382279277736257066266618532493517139001963526957179514521981877335815379755618191324858392834843718048308951653115284529736874534289456833723962912807104017411854314007953484461899139734367756070456068592886771130491355511301923675421649355211882120329692353507392677087555292357140606251171702417804959957862991259464749806480821163999054978911727901705780417863120490095024926067731615229486812312187386108568833026386220686253160504779704721744600638258183939573405528962511242337923530869616215532193967628076922234051908977996352800560160181197923404454023908443";
    private static final String gStrDefault = "3";
    private static final int lSize = 2048;
    private KeyPair mLocalKeyPair;
    private RCDHCodecTool.RCSecretKey mRCSecretKey;
    private static Map<String, RCDHCodecTool> sToolBag = new HashMap();

    public RCDHCodecTool() {
    }

    private static RCDHCodecTool create() {
        return new RCDHCodecTool();
    }

    public static RCDHCodecTool obtainWithEncId(String encId) {
        if (encId != null && encId.length() != 0) {
            if (sToolBag.containsKey(encId)) {
                return (RCDHCodecTool)sToolBag.get(encId);
            } else {
                RCDHCodecTool tool = create();
                sToolBag.put(encId, tool);
                return tool;
            }
        } else {
            RLog.w(TAG, "encId is null or empty while creating RCDHTool,so create a normal one.");
            return create();
        }
    }

    public static RCDHCodecTool.RCSecretKey getRCSecretKeyByEncId(String encId) {
        RCDHCodecTool localTool = obtainWithEncId(encId);
        return localTool == null ? null : localTool.mRCSecretKey;
    }

    private KeyPair genLocalKeyPair(DHParameterSpec parameterSpec) {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException var11) {
            RLog.e(TAG, "genLocalKeyPair could not get the NativePRNG random algorithm , generate local keypair failed!", var11);
            return null;
        }

        if (parameterSpec == null) {
            parameterSpec = new DHParameterSpec(new BigInteger("25135566567101483196994790440833279750474660393232382279277736257066266618532493517139001963526957179514521981877335815379755618191324858392834843718048308951653115284529736874534289456833723962912807104017411854314007953484461899139734367756070456068592886771130491355511301923675421649355211882120329692353507392677087555292357140606251171702417804959957862991259464749806480821163999054978911727901705780417863120490095024926067731615229486812312187386108568833026386220686253160504779704721744600638258183939573405528962511242337923530869616215532193967628076922234051908977996352800560160181197923404454023908443"), new BigInteger("3"), 2048);
        }

        BigInteger p = parameterSpec.getP();
        BigInteger g = parameterSpec.getG();
        int l = parameterSpec.getL();
        BigInteger pMinus2 = p.subtract(BigInteger.valueOf(2L));

        BigInteger x;
        do {
            do {
                x = new BigInteger(l, random);
            } while(x.compareTo(BigInteger.valueOf(1L)) < 0);
        } while(x.compareTo(pMinus2) > 0 || x.bitLength() != l);

        BigInteger y = g.modPow(x, p);
        PublicKey pubKey = new RCDHCodecTool.RCDHPublicKey(y);
        PrivateKey priKey = new RCDHCodecTool.RCDHPrivateKey(x);
        this.mLocalKeyPair = new KeyPair(pubKey, priKey);
        return this.mLocalKeyPair;
    }

    public KeyPair getOrCreateLocalKeyPair(DHParameterSpec parameterSpec) {
        if (this.mLocalKeyPair == null) {
            this.mLocalKeyPair = this.genLocalKeyPair(parameterSpec);
        }

        return this.mLocalKeyPair;
    }

    public RCDHCodecTool.RCSecretKey genSecretKey(RCDHCodecTool.RCDHPublicKey publicKey) {
        if (this.mLocalKeyPair == null || this.mLocalKeyPair.getPrivate() == null) {
            this.genLocalKeyPair((DHParameterSpec)null);
        }

        if (publicKey != null && publicKey.getY() != null) {
            RCDHCodecTool.RCDHPrivateKey priKey2Use = null;
            if (this.mLocalKeyPair.getPrivate() instanceof RCDHCodecTool.RCDHPrivateKey) {
                priKey2Use = (RCDHCodecTool.RCDHPrivateKey)this.mLocalKeyPair.getPrivate();
            }

            if (priKey2Use == null) {
                RLog.e(TAG, "private key is null ,can not generate the SecretKey.");
                return null;
            } else {
                try {
                    BigInteger keyInteger = publicKey.getY().modPow(priKey2Use.getX(), new BigInteger("25135566567101483196994790440833279750474660393232382279277736257066266618532493517139001963526957179514521981877335815379755618191324858392834843718048308951653115284529736874534289456833723962912807104017411854314007953484461899139734367756070456068592886771130491355511301923675421649355211882120329692353507392677087555292357140606251171702417804959957862991259464749806480821163999054978911727901705780417863120490095024926067731615229486812312187386108568833026386220686253160504779704721744600638258183939573405528962511242337923530869616215532193967628076922234051908977996352800560160181197923404454023908443"));
                    this.mRCSecretKey = new RCDHCodecTool.RCSecretKey(keyInteger);
                    return this.mRCSecretKey;
                } catch (Exception var4) {
                    RLog.e(TAG, "exception of " + var4.getLocalizedMessage() + "occurs,return null.");
                    return null;
                }
            }
        } else {
            RLog.e(TAG, "public key is null ,can not generate the SecretKey.");
            return null;
        }
    }

    public static byte[] encrypt(byte[] content, RCDHCodecTool.RCSecretKey rcSecretKey) {
        try {
            byte[] encodedKey = rcSecretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");
            byte[] ivByte = new byte[16];
            System.arraycopy(encodedKey, 0, ivByte, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (Exception var7) {
            RLog.e(TAG, "return byte[0],exception occurs while calling encrypt() " + var7.getLocalizedMessage());
            return new byte[0];
        }
    }

    public static byte[] decrypt(byte[] content, RCDHCodecTool.RCSecretKey rcSecretKey) {
        try {
            byte[] encodedKey = rcSecretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(encodedKey, "AES");
            byte[] ivByte = new byte[16];
            System.arraycopy(encodedKey, 0, ivByte, 0, 16);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(ivByte);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(content);
        } catch (InvalidKeyException | IllegalBlockSizeException | NoSuchPaddingException | BadPaddingException | NoSuchAlgorithmException var7) {
            RLog.e(TAG, "decrypt", var7);
        } catch (InvalidAlgorithmParameterException var8) {
            RLog.e(TAG, "decrypt", var8);
        }

        return null;
    }

    public static String[] parseEncTargetId(String targetId) {
        if (targetId != null && targetId.length() != 0) {
            return targetId.split(";;;");
        } else {
            RLog.e(TAG, "targetId is null or empty!");
            return null;
        }
    }

    public static String genEncId() {
        long timeStamp = System.currentTimeMillis();
        Random random = new Random();

        int randomInt;
        do {
            randomInt = random.nextInt(9999);
        } while(randomInt / 1000 <= 0);

        return String.valueOf(timeStamp) + randomInt;
    }

    public RCDHCodecTool.RCDHPublicKey getRCPubKey() {
        if (this.mLocalKeyPair == null) {
            return null;
        } else {
            return this.mLocalKeyPair.getPublic() instanceof RCDHCodecTool.RCDHPublicKey ? (RCDHCodecTool.RCDHPublicKey)this.mLocalKeyPair.getPublic() : null;
        }
    }

    public RCDHCodecTool.RCDHPrivateKey getRCPriKey() {
        if (this.mLocalKeyPair == null) {
            return null;
        } else {
            return this.mLocalKeyPair.getPrivate() instanceof RCDHCodecTool.RCDHPrivateKey ? (RCDHCodecTool.RCDHPrivateKey)this.mLocalKeyPair.getPrivate() : null;
        }
    }

    public static RCDHCodecTool.RCSecretKey fromString2RCSecretKey(String keyString) {
        BigInteger keyBI;
        try {
            keyBI = new BigInteger(keyString);
        } catch (Exception var3) {
            RLog.e(TAG, "exception occurs when transforming keyString to BigInger.");
            return null;
        }

        return new RCDHCodecTool.RCSecretKey(keyBI);
    }

    public static RCDHCodecTool.RCDHPublicKey fromString2RCDHPublicKey(String keyString) {
        BigInteger keyBI;
        try {
            keyBI = new BigInteger(keyString);
        } catch (Exception var3) {
            RLog.e(TAG, "fromString2RCDHPublicKey exception occurs when transforming keyString to BigInger.", var3);
            return null;
        }

        return new RCDHCodecTool.RCDHPublicKey(keyBI);
    }

    public static RCDHCodecTool.RCDHPrivateKey fromString2RCDHPrivateKey(String keyString) {
        BigInteger keyBI;
        try {
            keyBI = new BigInteger(keyString);
        } catch (Exception var3) {
            RLog.e(TAG, "fromString2RCDHPrivateKey exception occurs when transforming keyString to BigInger.", var3);
            return null;
        }

        return new RCDHCodecTool.RCDHPrivateKey(keyBI);
    }

    private static byte[] sha265Digest(byte[] origin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(origin);
            return digest.digest();
        } catch (NoSuchAlgorithmException var2) {
            RLog.e(TAG, "sha265Digest", var2);
            return null;
        }
    }

    public static class RCSecretKey implements SecretKey {
        public BigInteger key;

        RCSecretKey(BigInteger key) {
            this.key = key;
        }

        public String getAlgorithm() {
            return null;
        }

        public String getFormat() {
            return null;
        }

        public byte[] getEncoded() {
            byte[] encodedKey = null;
            if (this.key != null) {
                try {
                    encodedKey = this.key.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException var3) {
                    RLog.e(RCDHCodecTool.TAG, "RCSecretKey getEncoded", var3);
                    return null;
                }
            }

            return RCDHCodecTool.sha265Digest(encodedKey);
        }

        public String toString() {
            return this.key.toString();
        }
    }

    public static class RCDHPrivateKey implements DHPrivateKey {
        private BigInteger x;

        RCDHPrivateKey(BigInteger x) {
            this.x = x;
        }

        public BigInteger getX() {
            return this.x;
        }

        public String getAlgorithm() {
            return null;
        }

        public String getFormat() {
            return null;
        }

        public byte[] getEncoded() {
            return new byte[0];
        }

        public DHParameterSpec getParams() {
            return null;
        }

        public String toString() {
            return this.x.toString();
        }
    }

    public static class RCDHPublicKey implements DHPublicKey {
        private BigInteger Y;

        RCDHPublicKey(BigInteger y) {
            this.Y = y;
        }

        public BigInteger getY() {
            return this.Y;
        }

        public String getAlgorithm() {
            return null;
        }

        public String getFormat() {
            return null;
        }

        public byte[] getEncoded() {
            return new byte[0];
        }

        public DHParameterSpec getParams() {
            return null;
        }

        public String toString() {
            return this.Y.toString();
        }
    }
}
