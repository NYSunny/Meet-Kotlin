//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import io.rong.common.RLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserOnlineStatusInfo {
    private static final String TAG = "UserOnlineStatusInfo";
    private int serviceStatus;
    private int customerStatus;
    private UserOnlineStatusInfo.PlatformInfo platform;

    private UserOnlineStatusInfo.PlatformInfo getPlatFormEnum(String platformStr) {
        if (platformStr == null) {
            return UserOnlineStatusInfo.PlatformInfo.valueOf(0);
        } else {
            platformStr = platformStr.toLowerCase();
            byte var3 = -1;
            switch(platformStr.hashCode()) {
                case -861391249:
                    if (platformStr.equals("android")) {
                        var3 = 1;
                    }
                    break;
                case 3571:
                    if (platformStr.equals("pc")) {
                        var3 = 3;
                    }
                    break;
                case 104461:
                    if (platformStr.equals("ios")) {
                        var3 = 0;
                    }
                    break;
                case 117588:
                    if (platformStr.equals("web")) {
                        var3 = 2;
                    }
            }

            switch(var3) {
                case 0:
                    return UserOnlineStatusInfo.PlatformInfo.valueOf(1);
                case 1:
                    return UserOnlineStatusInfo.PlatformInfo.valueOf(2);
                case 2:
                    return UserOnlineStatusInfo.PlatformInfo.valueOf(3);
                case 3:
                    return UserOnlineStatusInfo.PlatformInfo.valueOf(4);
                default:
                    return UserOnlineStatusInfo.PlatformInfo.valueOf(0);
            }
        }
    }

    public UserOnlineStatusInfo(JSONObject jsonObject, int index) {
        try {
            this.serviceStatus = jsonObject.getInt("o");
            this.customerStatus = jsonObject.getInt("s");
            JSONArray jsonArray = jsonObject.optJSONArray("p");
            if (jsonArray != null && jsonArray.length() > 0) {
                if (index < jsonArray.length()) {
                    String platInfo = jsonArray.optString(index);
                    this.platform = this.getPlatFormEnum(platInfo);
                }
            } else {
                this.platform = this.getPlatFormEnum((String)null);
            }
        } catch (JSONException var5) {
            RLog.e("UserOnlineStatusInfo", "UserOnlineStatusInfo", var5);
        }

    }

    public int getServiceStatus() {
        return this.serviceStatus;
    }

    public int getCustomerStatus() {
        return this.customerStatus;
    }

    public UserOnlineStatusInfo.PlatformInfo getPlatform() {
        return this.platform;
    }

    public static enum PlatformInfo {
        Platform_Other(0),
        Platform_iOS(1),
        Platform_Android(2),
        Platform_Web(3),
        Platform_PC(4);

        private int code;

        private PlatformInfo(int code) {
            this.code = code;
        }

        public int getValue() {
            return this.code;
        }

        public static UserOnlineStatusInfo.PlatformInfo valueOf(int code) {
            UserOnlineStatusInfo.PlatformInfo[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                UserOnlineStatusInfo.PlatformInfo platformCode = var1[var3];
                if (code == platformCode.getValue()) {
                    return platformCode;
                }
            }

            return Platform_Other;
        }
    }
}
