//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.rong.push.PushType;
import io.rong.push.common.RLog;
import io.rong.push.core.PushUtils;

public class PushConfig {
    private static final String TAG = PushConfig.class.getSimpleName();
    private String miAppId;
    private String miAppKey;
    private String mzAppId;
    private String mzAppKey;
    private String oppoAppKey;
    private String oppoAppSecret;
    private Set<PushType> enabledPushTypes;
    private String appKey;
    private String pushNaviAddress;

    public PushConfig() {
    }

    public String getMiAppId() {
        return this.miAppId;
    }

    public String getMiAppKey() {
        return this.miAppKey;
    }

    public String getMzAppId() {
        return this.mzAppId;
    }

    public String getMzAppKey() {
        return this.mzAppKey;
    }

    public String getOppoAppKey() {
        return this.oppoAppKey;
    }

    public String getOppoAppSecret() {
        return this.oppoAppSecret;
    }

    public void setPushNaviAddress(String naviAddress) {
        this.pushNaviAddress = naviAddress;
    }

    public String getPushDomain() {
        if (TextUtils.isEmpty(this.pushNaviAddress)) {
            this.pushNaviAddress = PushUtils.getDefaultNavi();
        }

        return this.pushNaviAddress;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getEncodedEnabledPushTypes() {
        StringBuilder builder = new StringBuilder();
        Iterator var2 = this.enabledPushTypes.iterator();

        while(var2.hasNext()) {
            PushType type = (PushType)var2.next();
            builder.append(type.getName()).append("|");
        }

        RLog.d(TAG, "enabledPushTypes:" + builder.toString());
        return builder.toString();
    }

    public Set<PushType> getEnabledPushTypes() {
        return this.enabledPushTypes;
    }

    public static class Builder {
        private String miAppId;
        private String miAppKey;
        private String mzAppId;
        private String mzAppKey;
        private String oppoAppKey;
        private String oppoAppSecret;
        private Set<PushType> enabledPushTypes = new HashSet();
        private String appKey;
        private String pushNaviAddress;

        public Builder() {
        }

        public Builder enableMiPush(String miAppId, String miAppkey) {
            if (!TextUtils.isEmpty(miAppId) && !TextUtils.isEmpty(miAppkey)) {
                this.miAppId = miAppId;
                this.miAppKey = miAppkey;
                this.enabledPushTypes.add(PushType.XIAOMI);
                return this;
            } else {
                RLog.d(PushConfig.TAG, "appid or appkey can't be empty when enable MI push !");
                return this;
            }
        }

        public Builder enableHWPush(boolean isEnable) {
            if (isEnable) {
                this.enabledPushTypes.add(PushType.HUAWEI);
            }

            return this;
        }

        public Builder enableGCM(boolean isEnable) {
            if (isEnable) {
                if (!this.enabledPushTypes.contains(PushType.GOOGLE_FCM)) {
                    this.enabledPushTypes.add(PushType.GOOGLE_GCM);
                } else {
                    RLog.e(PushConfig.TAG, "the push types of GOOGLE_GCM and GOOGLE_FCM can only enable one.");
                }
            }

            return this;
        }

        public Builder enableFCM(boolean isEnable) {
            if (isEnable) {
                if (!this.enabledPushTypes.contains(PushType.GOOGLE_GCM)) {
                    this.enabledPushTypes.add(PushType.GOOGLE_FCM);
                } else {
                    RLog.e(PushConfig.TAG, "the push types of GOOGLE_FCM and GOOGLE_GCM can only enable one.");
                }
            }

            return this;
        }

        public Builder enableMeiZuPush(String mzAppId, String mzAppKey) {
            if (!TextUtils.isEmpty(mzAppId) && !TextUtils.isEmpty(mzAppKey)) {
                this.mzAppId = mzAppId;
                this.mzAppKey = mzAppKey;
                this.enabledPushTypes.add(PushType.MEIZU);
                return this;
            } else {
                RLog.e(PushConfig.TAG, "appid or appkey can't be empty when enable MEIZU push !");
                return this;
            }
        }

        public Builder enableOppoPush(String oppoAppKey, String oppoAppSecret) {
            if (!TextUtils.isEmpty(oppoAppKey) && !TextUtils.isEmpty(oppoAppSecret)) {
                this.oppoAppKey = oppoAppKey;
                this.oppoAppSecret = oppoAppSecret;
                this.enabledPushTypes.add(PushType.OPPO);
                return this;
            } else {
                RLog.e(PushConfig.TAG, "appid or appkey can't be empty when enable OPPO push !");
                return this;
            }
        }

        public Builder enableVivoPush(boolean isEnable) {
            if (isEnable) {
                this.enabledPushTypes.add(PushType.VIVO);
            }

            return this;
        }

        public Builder setAppKey(String appKey) {
            this.appKey = appKey;
            return this;
        }

        public Builder setPushNaviAddress(String naviAddress) {
            this.pushNaviAddress = naviAddress;
            return this;
        }

        public PushConfig build() {
            PushConfig pushConfig = new PushConfig();
            pushConfig.miAppId = this.miAppId;
            pushConfig.miAppKey = this.miAppKey;
            pushConfig.mzAppId = this.mzAppId;
            pushConfig.mzAppKey = this.mzAppKey;
            pushConfig.oppoAppKey = this.oppoAppKey;
            pushConfig.oppoAppSecret = this.oppoAppSecret;
            pushConfig.enabledPushTypes = this.enabledPushTypes;
            this.enabledPushTypes.add(PushType.RONG);
            pushConfig.appKey = this.appKey;
            pushConfig.pushNaviAddress = this.pushNaviAddress;
            return pushConfig;
        }
    }
}
