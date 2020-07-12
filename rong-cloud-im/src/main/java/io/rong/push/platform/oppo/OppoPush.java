////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.oppo;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.os.Build.VERSION;
//
//import com.heytap.msp.push.HeytapPushManager;
//import com.heytap.msp.push.callback.ICallBackResultService;
//
//import io.rong.push.PushErrorCode;
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class OppoPush implements IPush {
//    private final String TAG = OppoPush.class.getSimpleName();
//
//    public OppoPush() {
//    }
//
//    public void register(final Context context, PushConfig pushConfig) {
//        HeytapPushManager.init(context, true);
//        if (!HeytapPushManager.isSupportPush()) {
//            RLog.e(this.TAG, "the phone is not support oppo push!");
//            PushManager.getInstance().onErrorResponse(context, PushType.OPPO, "request_token", (long)PushErrorCode.NOT_SUPPORT_BY_OFFICIAL_PUSH.getCode());
//        } else {
//            RLog.d(this.TAG, "Oppo push start to register");
//            HeytapPushManager.register(context.getApplicationContext(), pushConfig.getOppoAppKey(), pushConfig.getOppoAppSecret(), new ICallBackResultService() {
//                public void onRegister(int responseCode, String registerID) {
//                    RLog.d(OppoPush.this.TAG, "Oppo Push onRegister responseCode " + String.valueOf(responseCode) + ",registerID:" + registerID);
//                    if (responseCode == 0) {
//                        PushManager.getInstance().onReceiveToken(context, PushType.OPPO, registerID);
//                    } else {
//                        PushManager.getInstance().onErrorResponse(context, PushType.OPPO, "request_token", (long)responseCode);
//                    }
//
//                }
//
//                public void onUnRegister(int responseCode) {
//                    RLog.d(OppoPush.this.TAG, "OPPO Push onUnRegister - responseCode:" + responseCode);
//                }
//
//                public void onSetPushTime(int responseCode, String pushTime) {
//                    RLog.d(OppoPush.this.TAG, "OPPO Push onSetPushTime - responseCode:" + responseCode + ",pushTime:" + pushTime);
//                }
//
//                public void onGetPushStatus(int responseCode, int status) {
//                    RLog.d(OppoPush.this.TAG, "OPPO Push onGetPushStatus - responseCode:" + responseCode + ",status:" + status);
//                }
//
//                public void onGetNotificationStatus(int responseCode, int status) {
//                    RLog.d(OppoPush.this.TAG, "OPPO Push onGetNotificationStatus - responseCode:" + responseCode + ",status:" + status);
//                }
//            });
//            if (VERSION.SDK_INT >= 26) {
//                NotificationManager nm = (NotificationManager)context.getSystemService("notification");
//                if (nm != null) {
//                    int importance = 3;
//                    String channelName = context.getResources().getString(context.getResources().getIdentifier("rc_notification_channel_name", "string", context.getPackageName()));
//                    NotificationChannel notificationChannel = new NotificationChannel("rc_notification_id", channelName, importance);
//                    notificationChannel.enableLights(true);
//                    notificationChannel.setLightColor(-16711936);
//                    nm.createNotificationChannel(notificationChannel);
//                }
//            }
//
//        }
//    }
//}
