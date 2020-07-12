////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.hms;
//
//import android.text.TextUtils;
//
//import com.huawei.hms.push.HmsMessageService;
//import com.huawei.hms.push.RemoteMessage;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//
//public class HMSPushService extends HmsMessageService {
//    private static final String TAG = "HMSPushService";
//
//    public HMSPushService() {
//    }
//
//    public void onMessageReceived(RemoteMessage pRemoteMessage) {
//        super.onMessageReceived(pRemoteMessage);
//        RLog.d("HMSPushService", "onPushMsg");
//        if (pRemoteMessage == null) {
//            RLog.e("HMSPushService", "pRemoteMessage is empty");
//        } else {
//            if (!TextUtils.isEmpty(pRemoteMessage.getData())) {
//                PushManager.getInstance().onPushRawData(this, PushType.HUAWEI, pRemoteMessage.getData());
//            } else {
//                RLog.e("HMSPushService", "pRemoteMessage.getData is empty");
//            }
//
//        }
//    }
//
//    public void onNewToken(String pS) {
//        super.onNewToken(pS);
//        PushManager.getInstance().onReceiveToken(this, PushType.HUAWEI, pS);
//    }
//}
