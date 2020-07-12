////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.hms;
//
//import android.content.Context;
//import android.os.Looper;
//import android.text.TextUtils;
//
//import com.huawei.agconnect.config.AGConnectServicesConfig;
//import com.huawei.hms.aaid.HmsInstanceId;
//import com.huawei.hms.common.ApiException;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.platform.IPush;
//import io.rong.push.pushconfig.PushConfig;
//
//public class HWPush implements IPush {
//    private static final String TAG = HWPush.class.getSimpleName();
//
//    public HWPush() {
//    }
//
//    public void register(final Context context, PushConfig pushConfig) {
//        if (Looper.getMainLooper() != Looper.myLooper()) {
//            this.action(context);
//        } else {
//            (new Thread(new Runnable() {
//                public void run() {
//                    HWPush.this.action(context);
//                }
//            })).start();
//        }
//
//    }
//
//    public void action(Context context) {
//        RLog.d(TAG, "HMS start get token");
//
//        try {
//            String appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id");
//            String pushtoken = HmsInstanceId.getInstance(context).getToken(appId, "HCM");
//            if (!TextUtils.isEmpty(pushtoken)) {
//                PushManager.getInstance().onReceiveToken(context, PushType.HUAWEI, pushtoken);
//            }
//        } catch (ApiException var4) {
//            PushManager.getInstance().onErrorResponse(context, PushType.HUAWEI, "request_token", (long)var4.getStatusCode());
//            RLog.i(TAG, "getToken failed, " + var4);
//        }
//
//    }
//}
