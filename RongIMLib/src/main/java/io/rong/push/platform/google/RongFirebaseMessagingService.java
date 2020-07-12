////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.google;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//
//public class RongFirebaseMessagingService extends FirebaseMessagingService {
//    private static final String TAG = "RongFirebaseMessagingService";
//
//    public RongFirebaseMessagingService() {
//    }
//
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//        if (null != remoteMessage) {
//            try {
//                JSONObject json = new JSONObject(remoteMessage.getData());
//                String message = json.getString("message");
//                PushManager.getInstance().onPushRawData(this, PushType.GOOGLE_FCM, message);
//            } catch (JSONException var4) {
//                var4.printStackTrace();
//            }
//
//        }
//    }
//
//    public void onNewToken(String s) {
//        super.onNewToken(s);
//        RLog.d("RongFirebaseMessagingService", "on NewToken");
//        PushManager.getInstance().onReceiveToken(this, PushType.GOOGLE_FCM, s);
//    }
//}
