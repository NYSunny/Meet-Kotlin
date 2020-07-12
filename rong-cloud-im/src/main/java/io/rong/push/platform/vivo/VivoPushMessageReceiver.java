////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.vivo;
//
//import android.content.Context;
//
//import com.vivo.push.model.UPSNotificationMessage;
//import com.vivo.push.sdk.OpenClientPushMessageReceiver;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.core.PushUtils;
//import io.rong.push.notification.PushNotificationMessage;
//
//public class VivoPushMessageReceiver extends OpenClientPushMessageReceiver {
//    private static final String TAG = VivoPushMessageReceiver.class.getSimpleName();
//
//    public VivoPushMessageReceiver() {
//    }
//
//    public void onNotificationMessageClicked(Context context, UPSNotificationMessage message) {
//        RLog.v(TAG, "onNotificationMessageClicked is called. " + message.getContent());
//        PushNotificationMessage pushNotificationMessage = PushUtils.transformToPushMessage(message.getContent());
//        if (pushNotificationMessage != null) {
//            PushManager.getInstance().onNotificationMessageClicked(context, PushType.VIVO, pushNotificationMessage);
//        }
//
//    }
//
//    public void onReceiveRegId(Context context, String token) {
//        RLog.d(TAG, "Vivo onReceiveRegId:" + token);
//        PushManager.getInstance().onReceiveToken(context, PushType.VIVO, token);
//    }
//}
