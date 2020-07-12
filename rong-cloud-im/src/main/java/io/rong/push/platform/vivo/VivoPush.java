////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.vivo;
//
//import android.content.Context;
//
//import com.vivo.push.IPushActionListener;
//import com.vivo.push.PushClient;
//
//import io.rong.push.PushErrorCode;
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class VivoPush implements IPush {
//    private final String TAG = VivoPush.class.getSimpleName();
//
//    public VivoPush() {
//    }
//
//    public void register(final Context context, PushConfig pushConfig) {
//        PushClient.getInstance(context.getApplicationContext()).initialize();
//        PushClient.getInstance(context.getApplicationContext()).turnOnPush(new IPushActionListener() {
//            public void onStateChanged(int i) {
//                RLog.d(VivoPush.this.TAG, "Vivo push onStateChanged:" + i);
//                if (i == 0) {
//                    PushManager.getInstance().onReceiveToken(context, PushType.VIVO, PushClient.getInstance(context.getApplicationContext()).getRegId());
//                } else if (i == 101) {
//                    PushManager.getInstance().onErrorResponse(context, PushType.VIVO, "request_token", (long)PushErrorCode.NOT_SUPPORT_BY_OFFICIAL_PUSH.getCode());
//                } else {
//                    PushManager.getInstance().onErrorResponse(context, PushType.VIVO, "request_token", (long)i);
//                }
//
//            }
//        });
//    }
//}
