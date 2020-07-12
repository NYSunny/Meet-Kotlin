////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.google;
//
//import android.os.Bundle;
//
//import com.google.android.gms.gcm.GcmListenerService;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//
//public class RongGcmListenerService extends GcmListenerService {
//    private static final String TAG = "RongGcmListenerService";
//
//    public RongGcmListenerService() {
//    }
//
//    public void onMessageReceived(String from, Bundle data) {
//        RLog.d("RongGcmListenerService", "onMessageReceived");
//        if (data != null) {
//            String message = data.getString("message");
//            PushManager.getInstance().onPushRawData(this, PushType.GOOGLE_GCM, message);
//        }
//    }
//}
