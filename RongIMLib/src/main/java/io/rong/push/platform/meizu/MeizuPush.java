////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.meizu;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.meizu.cloud.pushsdk.PushManager;
//
//import io.rong.push.PushErrorCode;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class MeizuPush implements IPush {
//    private final String TAG = MeizuPush.class.getSimpleName();
//
//    public MeizuPush() {
//    }
//
//    public void register(Context context, PushConfig pushConfig) {
//        if (!TextUtils.isEmpty(pushConfig.getMzAppId()) && !TextUtils.isEmpty(pushConfig.getMzAppKey())) {
//            String pushId = PushManager.getPushId(context);
//            if (TextUtils.isEmpty(pushId)) {
//                PushManager.register(context, pushConfig.getMzAppId(), pushConfig.getMzAppKey());
//            } else {
//                io.rong.push.PushManager.getInstance().onReceiveToken(context, PushType.MEIZU, pushId);
//            }
//
//        } else {
//            RLog.e(this.TAG, "appId or appKey can't be empty!");
//            io.rong.push.PushManager.getInstance().onErrorResponse(context, PushType.MEIZU, "request_token", (long)PushErrorCode.PARAMETER_ERROR.getCode());
//        }
//    }
//}
