//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;

//import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rong.imlib.statistics.Statistics;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.common.RLog;
import io.rong.push.notification.PushNotificationMessage;
import io.rong.push.notification.PushNotificationMessage.PushSourceType;
import io.rong.push.notification.RongNotificationInterface;
import io.rong.push.platform.IPush;
import io.rong.push.pushconfig.PushConfig;
import io.rong.push.pushconfig.PushConfig.Builder;
import io.rong.push.rongpush.PushReceiver;
import io.rong.push.rongpush.PushService;

public class RongPushClient {
    private static final String TAG = RongPushClient.class.getSimpleName();
    private static final ArrayList<PushType> registeredType = new ArrayList();
    private static String miAppId;
    private static String miAppKey;
    private static String mzAppId;
    private static String mzAppKey;
    private static PushConfig pushConfig;

    public RongPushClient() {
    }

    /** @deprecated */
    @Deprecated
    public static void registerGCM(Context context) {
        registeredType.add(PushType.GOOGLE_GCM);
    }

    /** @deprecated */
    @Deprecated
    public static void registerFCM(Context context) {
        registeredType.add(PushType.GOOGLE_FCM);
    }

    /** @deprecated */
    @Deprecated
    public static void registerMiPush(Context context, String miAppId, String miAppKey) {
        if (!TextUtils.isEmpty(miAppId) && !TextUtils.isEmpty(miAppKey)) {
            RongPushClient.miAppId = miAppId;
            RongPushClient.miAppKey = miAppKey;
            registeredType.add(PushType.XIAOMI);
        } else {
            throw new IllegalArgumentException("Failed registerMiPush. miAppId or miAppKey can't be empty.");
        }
    }

    /** @deprecated */
    @Deprecated
    public static void registerHWPush(Context context) {
        registeredType.add(PushType.HUAWEI);
    }

    /** @deprecated */
    @Deprecated
    public static void registerMZPush(Context context, String appId, String appKey) {
        if (!TextUtils.isEmpty(appId) && !TextUtils.isEmpty(appKey)) {
            registeredType.add(PushType.MEIZU);
            mzAppId = appId;
            mzAppKey = appKey;
        } else {
            throw new IllegalArgumentException("Failed registerMZPush. appKey or appId can't be empty.");
        }
    }

    /** @deprecated */
    @Deprecated
    public static void resolveHWPushError(Activity activity, long errorCode) throws IllegalStateException {
        if (activity == null) {
            throw new IllegalStateException("resolve HWPush Error activity is null !");
        }
    }

    public static void resolveHMSCoreUpdate(Activity activity) throws IllegalStateException {
        if (activity == null) {
            throw new IllegalStateException("resolve HWPush Error activity is null !");
        } else {
            PushType currentPushType = PushManager.getInstance().getServerPushType();
            if (!PushCacheHelper.getInstance().isConfigDone(activity) && currentPushType != null && currentPushType.getName().equals(PushType.HUAWEI.getName())) {
                IPush hwPush = PushManager.getInstance().getRegisteredPush(PushType.HUAWEI.getName());
                if (hwPush != null) {
                    hwPush.register(activity, PushManager.getInstance().getPushConfig());
                } else {
                    RLog.e(TAG, "no register HWPush");
                }
            } else {
                RLog.i(TAG, "current pushType is " + currentPushType);
            }

        }
    }

    public static void setPushConfig(PushConfig config) {
        pushConfig = config;
    }

    public static void init(Context context, String appKey) {
        init(context, appKey, "");
    }

    public static void init(Context context, String appKey, String pushDomain) {
        if (TextUtils.isEmpty(appKey)) {
            throw new ExceptionInInitializerError("appKey can't be empty!");
        } else {
            if (pushConfig == null) {
                boolean isHWEnable = registeredType.contains(PushType.HUAWEI);
                boolean isGCMEnable = registeredType.contains(PushType.GOOGLE_GCM);
                boolean isFCMEnable = registeredType.contains(PushType.GOOGLE_FCM);
                pushConfig = (new Builder()).enableHWPush(isHWEnable).enableMiPush(miAppId, miAppKey).enableMeiZuPush(mzAppId, mzAppKey).enableFCM(isFCMEnable).enableGCM(isGCMEnable).setAppKey(appKey).setPushNaviAddress(pushDomain).build();
            } else {
                pushConfig.setPushNaviAddress(pushDomain);
                pushConfig.setAppKey(appKey);
            }

            PushManager.getInstance().init(context, pushConfig);
        }
    }

    public static void stopService(Context context) {
        try {
            if (!PushCacheHelper.getInstance().getConfigPushType(context).equals(PushType.HUAWEI) && PushCacheHelper.getInstance().getConfigPushType(context).equals(PushType.XIAOMI)) {
//                MiPushClient.unregisterPush(context);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            RLog.e(TAG, "stopService throw exception: " + var3.getMessage());
        }

        ComponentName componentName = new ComponentName(context, PushReceiver.class);
        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        Intent intent = new Intent(context, PushService.class);
        intent.setAction("io.rong.push.intent.action.UNINIT");
        PushService.enqueueWork(context, intent);
    }

    public static void redirected(Context context) {
        String pushTypeUsing = PushCacheHelper.getInstance().getConfigPushType(context).getName();
        if (TextUtils.isEmpty(pushTypeUsing)) {
            Intent intent = new Intent(context, PushService.class);
            intent.setAction("io.rong.push.intent.action.REDIRECT");
            PushService.enqueueWork(context, intent);
        }

    }

    public static void clearAllNotifications(Context context) {
        RLog.i(TAG, "clearAllNotifications");
        if (PushCacheHelper.getInstance().getConfigPushType(context).equals(PushType.XIAOMI)) {
//            MiPushClient.clearNotification(context);
        }

        RongNotificationInterface.removeAllNotification(context);
    }

    public static PushType getCurrentPushType(Context context) {
        return PushCacheHelper.getInstance().isConfigDone(context) ? PushCacheHelper.getInstance().getConfigPushType(context) : PushType.UNKNOWN;
    }

    public static void clearAllPushNotifications(Context context) {
        RLog.i(TAG, "clearAllPushNotifications");
        if (PushCacheHelper.getInstance().getConfigPushType(context).equals(PushType.XIAOMI)) {
//            MiPushClient.clearNotification(context);
        }

        RongNotificationInterface.removeAllPushNotification(context);
    }

    public static void clearAllPushServiceNotifications(Context context) {
        if (PushCacheHelper.getInstance().getConfigPushType(context).equals(PushType.XIAOMI)) {
//            MiPushClient.clearNotification(context);
        }

        RongNotificationInterface.removeAllPushServiceNotification(context);
    }

    public static void clearNotificationById(Context context, int notificationId) {
        if (PushCacheHelper.getInstance().getConfigPushType(context).equals(PushType.XIAOMI)) {
//            MiPushClient.clearNotification(context, notificationId);
        }

        RongNotificationInterface.removeNotification(context, notificationId);
    }

    /** @deprecated */
    @Deprecated
    public static void recordNotificationEvent(String pushId) {
        Map<String, String> map = new HashMap();
        if (pushId != null && !pushId.equals("")) {
            if (!Statistics.sharedInstance().isInitialized()) {
                RLog.e(TAG, "Statistics should be initialized firstly!");
            } else {
                RLog.i(TAG, "recordNotificationEvent");
                map.put("id", pushId);
                Statistics.sharedInstance().recordEvent("pushEvent", map);
            }
        } else {
            RLog.e(TAG, "pushId can't be null!");
        }
    }

    public static void recordNotificationEvent(PushNotificationMessage pushNotificationMessage) {
        recordNotificationEvent(pushNotificationMessage.getPushId(), pushNotificationMessage.getToId(), pushNotificationMessage.getObjectName(), pushNotificationMessage.getSourceType());
    }

    private static void recordNotificationEvent(String pushId, String userId, String objName, PushSourceType type) {
        Map<String, String> map = new HashMap();
        if (!Statistics.sharedInstance().isInitialized()) {
            RLog.e(TAG, "Statistics should be initialized firstly!");
        } else {
            RLog.i(TAG, "recordNotificationEvent");
            map.put("id", pushId);
            map.put("osName", "Android");
            String os = String.format("%s|%s", Build.MANUFACTURER, Build.MODEL);
            map.put("osVersion", os);
            map.put("sdkVersion", "4.0.0.1");
            if (TextUtils.isEmpty(objName)) {
                objName = "unKnow";
            }

            map.put("objectName", objName);
            if (TextUtils.isEmpty(userId)) {
                userId = "push_targetId";
            }

            map.put("userId", userId);
            map.put("sourceType", Integer.toString(type.ordinal()));
            Statistics.sharedInstance().recordEvent("pushEvent", map);
        }
    }

    public static void recordHWNotificationEvent(Intent intent) {
        if (intent != null) {
            String options = intent.getStringExtra("options");
            if (!TextUtils.isEmpty(options)) {
                try {
                    JSONObject jsonObject = new JSONObject(options);
                    if (jsonObject.has("rc")) {
                        JSONObject rc = jsonObject.getJSONObject("rc");
                        String pushId = rc.optString("id");
                        if (TextUtils.isEmpty(pushId)) {
                            RLog.d(TAG, "pushId is empty,recordNotificationEvent is failure");
                            return;
                        }

                        String objectName = rc.optString("objectName");
                        String userId = rc.optString("tId");
                        String type = rc.optString("sourceType");
                        PushSourceType sourceType = PushSourceType.FROM_ADMIN;
                        if (!TextUtils.isEmpty(type)) {
                            sourceType = PushSourceType.values()[Integer.parseInt(type)];
                        }

                        recordNotificationEvent(pushId, userId, objectName, sourceType);
                    }
                } catch (JSONException var9) {
                    var9.printStackTrace();
                }
            }

        }
    }

    /** @deprecated */
    @Deprecated
    public static void stopRongPush(Context context) {
        Intent intent = new Intent(context, PushService.class);
        intent.setAction("io.rong.push.intent.action.STOP_PUSH");
        PushService.enqueueWork(context, intent);
    }

    public static void sendNotification(Context context, PushNotificationMessage notificationMessage) {
        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            RLog.e(TAG, "package name can't empty!");
        } else if (null == notificationMessage) {
            RLog.e(TAG, "notificationMessage  can't be  null!");
        } else {
            Intent intent = new Intent();
            intent.setAction("io.rong.push.intent.MESSAGE_ARRIVED");
            intent.setPackage(packageName);
            intent.putExtra("pushType", PushType.RONG.getName());
            intent.putExtra("message", notificationMessage);
            if (VERSION.SDK_INT >= 12) {
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            }

            context.sendBroadcast(intent);
        }
    }

    public static void sendNotification(Context context, PushNotificationMessage notificationMessage, int left) {
        String packageName = context.getPackageName();
        if (TextUtils.isEmpty(packageName)) {
            RLog.e(TAG, "package name can't empty!");
        } else if (null == notificationMessage) {
            RLog.e(TAG, "notificationMessage  can't be  null!");
        } else {
            Intent intent = new Intent();
            intent.setAction("io.rong.push.intent.MESSAGE_ARRIVED");
            intent.setPackage(packageName);
            intent.putExtra("pushType", PushType.RONG.getName());
            intent.putExtra("message", notificationMessage);
            intent.putExtra("left", left);
            if (VERSION.SDK_INT >= 12) {
                intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            }

            context.sendBroadcast(intent);
        }
    }

    public static void setNotifiationSound(Uri uri) {
        RongNotificationInterface.setNotificationSound(uri);
    }

    /** @deprecated */
    @Deprecated
    public static void checkManifest(Context context) {
    }

    public static void updatePushContentShowStatus(Context context, boolean isShow) {
        if (PushCacheHelper.getInstance().getPushContentShowStatus(context) ^ isShow) {
            PushCacheHelper.getInstance().setPushContentShowStatus(context, isShow);
        }

    }

    public static enum ConversationType {
        NONE(0, "none"),
        PRIVATE(1, "private"),
        DISCUSSION(2, "discussion"),
        GROUP(3, "group"),
        CHATROOM(4, "chatroom"),
        CUSTOMER_SERVICE(5, "customer_service"),
        SYSTEM(6, "system"),
        APP_PUBLIC_SERVICE(7, "app_public_service"),
        PUBLIC_SERVICE(8, "public_service"),
        PUSH_SERVICE(9, "push_service"),
        ENCRYPTED(11, "encrypted");

        private int value = 1;
        private String name = "";

        private ConversationType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

        public static ConversationType setValue(int code) {
            ConversationType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ConversationType c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return PRIVATE;
        }
    }
}
