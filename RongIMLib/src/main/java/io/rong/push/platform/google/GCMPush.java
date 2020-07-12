////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.google;
//
//import android.content.Context;
//import android.os.Bundle;
//
//import com.google.android.gms.iid.InstanceID;
//
//import java.io.IOException;
//
//import io.rong.push.PushErrorCode;
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.core.PushUtils;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class GCMPush implements IPush {
//    private final String TAG = GCMPush.class.getSimpleName();
//
//    public GCMPush() {
//    }
//
//    public void register(Context context, PushConfig pushConfig) {
//        long result = PushUtils.checkPlayServices(context);
//        if (result != 0L) {
//            PushManager.getInstance().onErrorResponse(context, PushType.GOOGLE_GCM, "checkPlayServices", result);
//        } else {
//            InstanceID instanceID = InstanceID.getInstance(context);
//
//            try {
//                String token = instanceID.getToken(context.getResources().getString(context.getResources().getIdentifier("gcm_defaultSenderId", "string", context.getPackageName())), "GCM", (Bundle)null);
//                PushManager.getInstance().onReceiveToken(context, PushType.GOOGLE_GCM, token);
//            } catch (IOException var7) {
//                RLog.e(this.TAG, var7.getMessage());
//                PushManager.getInstance().onErrorResponse(context, PushType.GOOGLE_GCM, "checkPlayServices", (long)PushErrorCode.IO_EXCEPTION.getCode());
//            }
//        }
//
//    }
//}
