////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by Fernflower decompiler)
////
//
//package io.rong.push.platform.meizu;
//
//import android.content.Context;
//
//import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
//import com.meizu.cloud.pushsdk.handler.MzPushMessage;
//import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
//import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
//import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
//import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
//import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
//
//import io.rong.push.PushManager;
//import io.rong.push.PushType;
//import io.rong.push.common.RLog;
//import io.rong.push.core.PushUtils;
//import io.rong.push.notification.PushNotificationMessage;
//
//public class MeiZuReceiver extends MzPushMessageReceiver {
//    private static final String TAG = MeiZuReceiver.class.getSimpleName();
//
//    public MeiZuReceiver() {
//    }
//
//    public void onRegister(Context context, String s) {
//        RLog.d("MeiZuReceiver", "onRegister");
//    }
//
//    public void onUnRegister(Context context, boolean b) {
//    }
//
//    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
//    }
//
//    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
//        PushManager.getInstance().onReceiveToken(context, PushType.MEIZU, registerStatus.getPushId());
//    }
//
//    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
//    }
//
//    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
//    }
//
//    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
//    }
//
//    public void onNotificationArrived(Context context, MzPushMessage mzPushMessage) {
//        RLog.d(TAG, "onNotificationArrived message: " + mzPushMessage.toString());
//        PushNotificationMessage pushNotificationMessage = PushUtils.transformToPushMessage(mzPushMessage.getSelfDefineContentString());
//        if (pushNotificationMessage != null) {
//            PushManager.getInstance().onNotificationMessageArrived(context, PushType.MEIZU, pushNotificationMessage);
//        }
//
//    }
//
//    public void onNotificationClicked(Context context, MzPushMessage mzPushMessage) {
//        RLog.d(TAG, "onNotificationClicked message: " + mzPushMessage.toString());
//        PushNotificationMessage pushNotificationMessage = PushUtils.transformToPushMessage(mzPushMessage.getSelfDefineContentString());
//        if (pushNotificationMessage != null) {
//            PushManager.getInstance().onNotificationMessageClicked(context, PushType.MEIZU, pushNotificationMessage);
//        }
//
//    }
//}
