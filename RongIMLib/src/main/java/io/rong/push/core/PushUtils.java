//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.core;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

//import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import io.rong.common.rlog.RLog;
import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushType;
import io.rong.push.RongPushClient.ConversationType;
import io.rong.push.notification.PushNotificationMessage;
import io.rong.push.notification.PushNotificationMessage.PushSourceType;
import io.rong.push.pushconfig.PushConfig;

public class PushUtils {
    private static final String PUSH_NAVI = "nav.cn.ronghub.com";
    private static final String PUSH_NAVI_BACKUP = "nav2-cn.ronghub.com";

    public PushUtils() {
    }

    public static String getDefaultNavi() {
        return "nav.cn.ronghub.com;nav2-cn.ronghub.com";
    }

    public static PushType getPreferPushType(Context context, PushConfig pushConfig) {
        String os = DeviceUtils.getDeviceManufacturer().toLowerCase();
        Set<PushType> pushTypes = pushConfig.getEnabledPushTypes();
        if (os.contains("Xiaomi".toLowerCase()) && pushTypes.contains(PushType.XIAOMI)) {
            return PushType.XIAOMI;
        } else if (os.contains("HUAWEI".toLowerCase()) && pushTypes.contains(PushType.HUAWEI)) {
            return PushType.HUAWEI;
        } else if (os.contains("Meizu".toLowerCase()) && pushTypes.contains(PushType.MEIZU)) {
            return PushType.MEIZU;
        } else if (os.contains("oppo".toLowerCase()) && pushTypes.contains(PushType.OPPO)) {
            return PushType.OPPO;
        } else if (os.contains("VIVO".toLowerCase()) && pushTypes.contains(PushType.VIVO)) {
            return PushType.VIVO;
        } else {
            return !pushTypes.contains(PushType.GOOGLE_GCM) && !pushTypes.contains(PushType.GOOGLE_FCM) ? PushType.RONG : PushType.UNKNOWN;
        }
    }

    public static long checkPlayServices(Context context) {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        return (long)apiAvailability.isGooglePlayServicesAvailable(context);
        return 0L;
    }

    public static PushNotificationMessage transformToPushMessage(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            PushNotificationMessage pushNotificationMessage = new PushNotificationMessage();
            String channelType = jsonObject.optString("channelType");
            int typeValue = 0;
            if (!TextUtils.isEmpty(channelType)) {
                try {
                    typeValue = Integer.parseInt(channelType);
                } catch (NumberFormatException var12) {
                    var12.printStackTrace();
                }
            }

            ConversationType conversationType = ConversationType.setValue(typeValue);
            pushNotificationMessage.setConversationType(conversationType);
            if (!conversationType.equals(ConversationType.DISCUSSION) && !conversationType.equals(ConversationType.GROUP) && !conversationType.equals(ConversationType.CHATROOM)) {
                pushNotificationMessage.setTargetId(jsonObject.optString("fromUserId"));
                pushNotificationMessage.setTargetUserName(jsonObject.optString("fromUserName"));
            } else {
                pushNotificationMessage.setTargetId(jsonObject.optString("channelId"));
                pushNotificationMessage.setTargetUserName(jsonObject.optString("channelName"));
            }

            pushNotificationMessage.setReceivedTime(jsonObject.optLong("timeStamp"));
            pushNotificationMessage.setObjectName(jsonObject.optString("objectName"));
            pushNotificationMessage.setSenderId(jsonObject.optString("fromUserId"));
            pushNotificationMessage.setSenderName(jsonObject.optString("fromUserName"));
            pushNotificationMessage.setSenderPortrait(TextUtils.isEmpty(jsonObject.optString("fromUserPo")) ? null : Uri.parse(jsonObject.optString("fromUserPo")));
            pushNotificationMessage.setPushTitle(jsonObject.optString("title"));
            pushNotificationMessage.setPushContent(jsonObject.optString("content"));
            pushNotificationMessage.setPushData(jsonObject.optString("appData"));
            pushNotificationMessage.setPushFlag("true");
            String toId = "";
            String type = "";
            String pushId = "";
            PushSourceType sourceType = PushSourceType.FROM_ADMIN;
            JSONObject temp = jsonObject.optJSONObject("rc");
            if (temp == null) {
                String rcjson = jsonObject.optString("rc");
                if (rcjson != null) {
                    temp = new JSONObject(rcjson);
                }
            }

            if (temp != null) {
                toId = temp.optString("tId");
                type = temp.optString("sourceType");
                pushId = temp.optString("id");
            }

            if (!TextUtils.isEmpty(type)) {
                sourceType = PushSourceType.values()[Integer.parseInt(type)];
            }

            pushNotificationMessage.setToId(toId);
            pushNotificationMessage.setSourceType(sourceType);
            pushNotificationMessage.setPushId(pushId);
            if (temp != null && temp.has("ext") && temp.getJSONObject("ext") != null) {
                pushNotificationMessage.setExtra(temp.getJSONObject("ext").toString());
            }

            return pushNotificationMessage;
        } catch (JSONException var13) {
            RLog.e("PushUtils", var13.getMessage());
            return null;
        }
    }

    public static String getPackageName(String str) {
        String packageName = "";

        try {
            JSONObject json = new JSONObject(str);
            packageName = json.optString("packageName");
        } catch (JSONException var4) {
            RLog.e("PushUtils", "getPackageName", var4);
        }

        return packageName;
    }
}
