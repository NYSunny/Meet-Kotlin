////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.mi;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.xiaomi.mipush.sdk.MiPushClient;
//
//import io.rong.push.PushErrorCode;
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class MiPush implements IPush {
//    private final String TAG = MiPush.class.getSimpleName();
//
//    public MiPush() {
//    }
//
//    public void register(Context context, PushConfig pushConfig) {
//        if (!TextUtils.isEmpty(pushConfig.getMiAppId()) && !TextUtils.isEmpty(pushConfig.getMiAppKey())) {
//            MiPushClient.registerPush(context, pushConfig.getMiAppId(), pushConfig.getMiAppKey());
//        } else {
//            RLog.e(this.TAG, "appId or appKey can't be empty!");
//            PushManager.getInstance().onErrorResponse(context, PushType.XIAOMI, "request_token", (long)PushErrorCode.PARAMETER_ERROR.getCode());
//        }
//    }
//}
