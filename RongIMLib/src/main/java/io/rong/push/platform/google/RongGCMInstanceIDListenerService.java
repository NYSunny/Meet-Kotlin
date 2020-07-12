////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.google;
//
//import com.google.android.gms.iid.InstanceIDListenerService;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//
//public class RongGCMInstanceIDListenerService extends InstanceIDListenerService {
//    public RongGCMInstanceIDListenerService() {
//    }
//
//    public void onTokenRefresh() {
//        PushManager.getInstance().onTokenRefresh(this, PushType.GOOGLE_GCM);
//    }
//}
