////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.google;
//
//import android.content.Context;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.core.PushUtils;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class FCMPush implements IPush {
//    public FCMPush() {
//    }
//
//    public void register(Context context, PushConfig pushConfig) {
//        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
//        long result = PushUtils.checkPlayServices(context);
//        if (result != 0L) {
//            PushManager.getInstance().onErrorResponse(context, PushType.GOOGLE_FCM, "checkPlayServices", result);
//        }
//
//    }
//}
