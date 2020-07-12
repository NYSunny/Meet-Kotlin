//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.text.TextUtils;

import io.rong.push.PushErrorCode;
import io.rong.push.PushType;
import io.rong.push.RongPushClient;
import io.rong.push.common.RLog;
import io.rong.push.notification.PushNotificationMessage.PushSourceType;

public abstract class PushMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "PushMessageReceiver";

    public PushMessageReceiver() {
    }

    public final void onReceive(Context context, Intent intent) {
        RLog.d("PushMessageReceiver", "onReceive.action:" + intent.getAction());
        if (intent.getAction() == null) {
            RLog.e("PushMessageReceiver", "the intent action is null, return directly. ");
        } else {
            String name = intent.getStringExtra("pushType");
            int left = intent.getIntExtra("left", 0);
            PushNotificationMessage message = (PushNotificationMessage)intent.getParcelableExtra("message");
            if (message == null && !intent.getAction().equals("io.rong.push.intent.THIRD_PARTY_PUSH_STATE")) {
                RLog.e("PushMessageReceiver", "message is null. Return directly!");
            } else {
                PushType pushType = PushType.getType(name);
                if (intent.getAction().equals("io.rong.push.intent.MESSAGE_ARRIVED")) {
                    if (!this.onNotificationMessageArrived(context, pushType, message) && (pushType.equals(PushType.RONG) || pushType.equals(PushType.GOOGLE_FCM) || pushType.equals(PushType.GOOGLE_GCM)) && !this.handleVoIPNotification(context, message)) {
                        RongNotificationInterface.sendNotification(context, message, left);
                    }
                } else if (intent.getAction().equals("io.rong.push.intent.MESSAGE_CLICKED")) {
                    if (!TextUtils.isEmpty(message.getPushId())) {
                        RongPushClient.recordNotificationEvent(message);
                    }

                    if (!this.onNotificationMessageClicked(context, pushType, message) && !this.handleVoIPNotification(context, message)) {
                        boolean isMulti = intent.getBooleanExtra("isMulti", false);
                        this.handleNotificationClickEvent(context, isMulti, message);
                    }
                } else if (intent.getAction().equals("io.rong.push.intent.THIRD_PARTY_PUSH_STATE")) {
                    String action = intent.getStringExtra("action");
                    long resultCode = intent.getLongExtra("resultCode", (long)PushErrorCode.UNKNOWN.getCode());
                    this.onThirdPartyPushState(pushType, action, resultCode);
                } else {
                    RLog.e("PushMessageReceiver", "Unknown action, do nothing!");
                }

            }
        }
    }

    public abstract boolean onNotificationMessageArrived(Context var1, PushType var2, PushNotificationMessage var3);

    public abstract boolean onNotificationMessageClicked(Context var1, PushType var2, PushNotificationMessage var3);

    public void onThirdPartyPushState(PushType pushType, String action, long resultCode) {
        RLog.e("PushMessageReceiver", "onThirdPartyPushState pushType: " + pushType + " action: " + action + " resultCode: " + resultCode);
    }

    private void handleNotificationClickEvent(Context context, boolean isMulti, PushNotificationMessage notificationMessage) {
        String isFromPush = !notificationMessage.getSourceType().equals(PushSourceType.FROM_OFFLINE_MESSAGE) && !notificationMessage.getSourceType().equals(PushSourceType.FROM_ADMIN) ? "false" : "true";
        Intent intent = new Intent();
        intent.setFlags(268435456);
        Builder builder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
        if (notificationMessage.getSourceType().equals(PushSourceType.FROM_ADMIN)) {
            builder.appendPath("push_message").appendQueryParameter("targetId", notificationMessage.getTargetId()).appendQueryParameter("pushContent", notificationMessage.getPushContent()).appendQueryParameter("pushData", notificationMessage.getPushData()).appendQueryParameter("extra", notificationMessage.getExtra()).appendQueryParameter("isFromPush", isFromPush);
        } else if (isMulti) {
            builder.appendPath("conversationlist").appendQueryParameter("isFromPush", isFromPush);
        } else {
            builder.appendPath("conversation").appendPath(notificationMessage.getConversationType().getName()).appendQueryParameter("targetId", notificationMessage.getTargetId()).appendQueryParameter("title", TextUtils.isEmpty(notificationMessage.getPushTitle()) ? notificationMessage.getTargetUserName() : notificationMessage.getPushTitle()).appendQueryParameter("isFromPush", isFromPush);
        }

        intent.setData(builder.build());
        intent.setPackage(context.getPackageName());
        context.startActivity(intent);
    }

    public boolean handleVoIPNotification(Context context, PushNotificationMessage notificationMessage) {
        String objName = notificationMessage.getObjectName();
        if (objName != null && objName.equals("RC:VCInvite")) {
            Intent intent;
            if (VERSION.SDK_INT >= 29) {
                intent = new Intent();
                intent.setPackage(context.getPackageName());
                intent.putExtra("message", notificationMessage);
                intent.setAction("action.push.CallInviteMessage");
                context.sendBroadcast(intent);
            } else {
                RLog.d("PushMessageReceiver", "handle VoIP event.");
                intent = new Intent();
                intent.setFlags(268435456);
                Uri uri = Uri.parse("rong://" + context.getPackageName()).buildUpon().appendPath("conversationlist").appendQueryParameter("isFromPush", "false").build();
                intent.setData(uri);
                intent.setPackage(context.getPackageName());
                context.startActivity(intent);
            }

            return true;
        } else {
            return false;
        }
    }
}
