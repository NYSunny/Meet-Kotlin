//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.notification;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.rong.push.RongPushClient.ConversationType;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.common.RLog;

public class RongNotificationInterface {
    private static final String TAG = "RongNotificationInterface";
    private static HashMap<String, List<PushNotificationMessage>> messageCache = new HashMap();
    private static int NOTIFICATION_ID = 1000;
    private static int PUSH_SERVICE_NOTIFICATION_ID = 2000;
    private static int VOIP_NOTIFICATION_ID = 3000;
    private static final int NEW_NOTIFICATION_LEVEL = 11;
    private static final int PUSH_REQUEST_CODE = 200;
    private static final int NEGLECT_TIME = 3000;
    private static long lastNotificationTimestamp;
    private static Uri mSound;
    private static boolean recallUpdate = false;

    public RongNotificationInterface() {
    }

    public static void sendNotification(Context context, PushNotificationMessage message) {
        sendNotification(context, message, 0);
    }

    public static void sendNotification(Context context, PushNotificationMessage message, int left) {
        if (messageCache == null) {
            messageCache = new HashMap();
        }

        ConversationType conversationType = message.getConversationType();
        String objName = message.getObjectName();
        String content = "";
        boolean isMulti = false;
        int requestCode = 200;
        SoundType soundType = SoundType.DEFAULT;
        RLog.i("RongNotificationInterface", "sendNotification() messageType: " + message.getConversationType() + " messagePushContent: " + message.getPushContent() + " messageObjectName: " + message.getObjectName());
        if (!TextUtils.isEmpty(objName) && conversationType != null) {
            long now = System.currentTimeMillis();
            if (now - lastNotificationTimestamp < 3000L) {
                soundType = SoundType.SILENT;
            } else {
                lastNotificationTimestamp = now;
            }

            String title;
            int notificationId;
            if (conversationType == null || !conversationType.equals(ConversationType.SYSTEM) && !conversationType.equals(ConversationType.PUSH_SERVICE)) {
                if (objName != null && (objName.equals("RC:VCInvite") || objName.equals("RC:VCModifyMem") || objName.equals("RC:VCHangup"))) {
                    if (objName.equals("RC:VCHangup")) {
                        removeNotification(context, VOIP_NOTIFICATION_ID);
                        return;
                    }

                    notificationId = VOIP_NOTIFICATION_ID;
                    soundType = SoundType.VOIP;
                    requestCode = 400;
                    title = (String)context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
                    content = message.getPushContent();
                } else {
                    List<PushNotificationMessage> messages = (List)messageCache.get(message.getTargetId());
                    if (messages == null) {
                        messages = new ArrayList();
                        messages.add(message);
                        messageCache.put(message.getTargetId(), messages);
                    } else if (!objName.equals("RC:RcNtf")) {
                        if (((PushNotificationMessage)messages.get(messages.size() - 1)).getObjectName().equals("RC:RcNtf")) {
                            messages.remove(messages.size() - 1);
                        }

                        messages.add(message);
                    } else {
                        for(int i = messages.size() - 1; i >= 0; --i) {
                            if (messages.get(i) != null && ((PushNotificationMessage)messages.get(i)).getPushId() != null && ((PushNotificationMessage)messages.get(i)).getPushId().equals(message.getPushId())) {
                                messages.remove(messages.get(i));
                                break;
                            }
                        }

                        if (messages.size() == 0) {
                            if (messageCache.size() == 1) {
                                messages.add(message);
                            } else {
                                messageCache.remove(message.getTargetId());
                                if (messageCache.size() == 1) {
                                    recallUpdate = true;
                                }
                            }
                        }
                    }

                    if (messageCache.size() > 1) {
                        isMulti = true;
                    }

                    title = getNotificationTitle(context);
                    notificationId = NOTIFICATION_ID;
                }
            } else {
                title = message.getPushTitle();
                if (TextUtils.isEmpty(title)) {
                    title = (String)context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
                }

                content = message.getPushContent();
                notificationId = PUSH_SERVICE_NOTIFICATION_ID;
                requestCode = 300;
                ++PUSH_SERVICE_NOTIFICATION_ID;
            }

            if (left <= 0) {
                PendingIntent intent;
                if (recallUpdate) {
                    intent = updateRecallPendingIntent(context, requestCode, isMulti);
                } else {
                    intent = createPendingIntent(context, message, requestCode, isMulti);
                }

                Notification notification = createNotification(context, title, intent, content, soundType);
                NotificationManager nm = (NotificationManager)context.getSystemService("notification");
                if (VERSION.SDK_INT >= 26) {
                    int importance = 3;
                    String channelName = context.getResources().getString(context.getResources().getIdentifier("rc_notification_channel_name", "string", context.getPackageName()));
                    NotificationChannel notificationChannel = new NotificationChannel("rc_notification_id", channelName, importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(-16711936);
                    if (notification != null && notification.sound != null) {
                        notificationChannel.setSound(notification.sound, (AudioAttributes)null);
                    }

                    nm.createNotificationChannel(notificationChannel);
                }

                if (notification != null) {
                    RLog.i("RongNotificationInterface", "sendNotification() real notify! notificationId: " + notificationId + " notification: " + notification.toString());
                    nm.notify(notificationId, notification);
                }

            }
        }
    }

    private static PendingIntent updateRecallPendingIntent(Context context, int requestCode, boolean isMulti) {
        Collection<List<PushNotificationMessage>> collection = messageCache.values();
        List<PushNotificationMessage> msg = (List)collection.iterator().next();
        PushNotificationMessage notificationMessage = (PushNotificationMessage)msg.get(0);
        Intent intent = new Intent();
        intent.setAction("io.rong.push.intent.MESSAGE_CLICKED");
        intent.putExtra("message", notificationMessage);
        intent.putExtra("isMulti", isMulti);
        intent.setPackage(context.getPackageName());
        return PendingIntent.getBroadcast(context, requestCode, intent, 134217728);
    }

    public static void removeAllNotification(Context context) {
        messageCache.clear();
        NotificationManager nm = (NotificationManager)context.getSystemService("notification");

        try {
            nm.cancelAll();
        } catch (Exception var3) {
            RLog.e("RongNotificationInterface", "removeAllNotification" + var3.getMessage());
        }

        NOTIFICATION_ID = 1000;
    }

    public static void removeAllPushNotification(Context context) {
        messageCache.clear();
        NotificationManager nm = (NotificationManager)context.getSystemService("notification");
        nm.cancel(NOTIFICATION_ID);
        nm.cancel(VOIP_NOTIFICATION_ID);
    }

    public static void removeAllPushServiceNotification(Context context) {
        NotificationManager nm = (NotificationManager)context.getSystemService("notification");

        for(int i = PUSH_SERVICE_NOTIFICATION_ID; i >= 1000; --i) {
            nm.cancel(i);
        }

        PUSH_SERVICE_NOTIFICATION_ID = 2000;
    }

    public static void removeNotification(Context context, int notificationId) {
        if (notificationId >= 0) {
            if (notificationId >= NOTIFICATION_ID && notificationId < PUSH_SERVICE_NOTIFICATION_ID) {
                messageCache.clear();
            }

            NotificationManager nm = (NotificationManager)context.getSystemService("notification");
            nm.cancel(notificationId);
        }
    }

    private static PendingIntent createPendingIntent(Context context, PushNotificationMessage message, int requestCode, boolean isMulti) {
        Intent intent = new Intent();
        intent.setAction("io.rong.push.intent.MESSAGE_CLICKED");
        intent.putExtra("message", message);
        intent.putExtra("isMulti", isMulti);
        intent.setPackage(context.getPackageName());
        return PendingIntent.getBroadcast(context, requestCode, intent, 134217728);
    }

    private static String getNotificationContent(Context context) {
        String rc_notification_new_msg = context.getResources().getString(context.getResources().getIdentifier("rc_notification_new_msg", "string", context.getPackageName()));
        String rc_notification_new_plural_msg = context.getResources().getString(context.getResources().getIdentifier("rc_notification_new_plural_msg", "string", context.getPackageName()));
        String content;
        if (messageCache.size() == 1) {
            Collection<List<PushNotificationMessage>> collection = messageCache.values();
            List<PushNotificationMessage> msg = (List)collection.iterator().next();
            PushNotificationMessage notificationMessage = (PushNotificationMessage)msg.get(0);
            if (msg.size() == 1) {
                content = notificationMessage.getPushContent();
            } else if (((PushNotificationMessage)msg.get(msg.size() - 1)).getObjectName().equals("RC:RcNtf")) {
                notificationMessage = (PushNotificationMessage)msg.get(msg.size() - 1);
                content = notificationMessage.getPushContent();
            } else {
                content = String.format(rc_notification_new_msg, notificationMessage.getTargetUserName(), msg.size());
            }
        } else {
            int count = 0;
            Collection<List<PushNotificationMessage>> collection = messageCache.values();

            List msg;
            for(Iterator var10 = collection.iterator(); var10.hasNext(); count += msg.size()) {
                msg = (List)var10.next();
            }

            content = String.format(rc_notification_new_plural_msg, messageCache.size(), count);
        }

        return content;
    }

    private static String getNotificationTitle(Context context) {
        String title;
        if (messageCache.size() == 1) {
            Collection<List<PushNotificationMessage>> collection = messageCache.values();
            List<PushNotificationMessage> msg = (List)collection.iterator().next();
            PushNotificationMessage notificationMessage = (PushNotificationMessage)msg.get(0);
            title = notificationMessage.getTargetUserName();
        } else {
            title = (String)context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
        }

        return title;
    }

    public static Notification createNotification(Context context, String title, PendingIntent pendingIntent, String content, SoundType soundType) {
        String tickerText = context.getResources().getString(context.getResources().getIdentifier("rc_notification_ticker_text", "string", context.getPackageName()));
        if (TextUtils.isEmpty(content)) {
            content = getNotificationContent(context);
        }

        Notification notification;
        if (VERSION.SDK_INT < 11) {
            try {
                notification = new Notification(context.getApplicationInfo().icon, tickerText, System.currentTimeMillis());
                Class<?> classType = Notification.class;
                Method method = classType.getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
                method.invoke(notification, context, title, content, pendingIntent);
                notification.flags = 16;
                notification.defaults = -1;
            } catch (Exception var18) {
                var18.printStackTrace();
                return null;
            }
        } else {
            boolean isLollipop = VERSION.SDK_INT >= 21;
            int smallIcon = context.getResources().getIdentifier("notification_small_icon", "drawable", context.getPackageName());
            if (smallIcon <= 0 || !isLollipop) {
                smallIcon = context.getApplicationInfo().icon;
            }

            int defaults = 1;
            Uri sound = null;
            if (soundType.equals(SoundType.SILENT)) {
                defaults = 4;
            } else if (soundType.equals(SoundType.VOIP)) {
                defaults = 6;
                sound = RingtoneManager.getDefaultUri(1);
            } else {
                sound = RingtoneManager.getDefaultUri(2);
            }

            Drawable loadIcon = context.getApplicationInfo().loadIcon(context.getPackageManager());
            Bitmap appIcon = null;

            try {
                if (VERSION.SDK_INT >= 26 && loadIcon instanceof AdaptiveIconDrawable) {
                    appIcon = Bitmap.createBitmap(loadIcon.getIntrinsicWidth(), loadIcon.getIntrinsicHeight(), Config.ARGB_8888);
                    Canvas canvas = new Canvas(appIcon);
                    loadIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    loadIcon.draw(canvas);
                } else {
                    appIcon = ((BitmapDrawable)loadIcon).getBitmap();
                }
            } catch (Exception var19) {
                var19.printStackTrace();
            }

            Builder builder = new Builder(context);
            builder.setLargeIcon(appIcon);
            if (!soundType.equals(SoundType.SILENT)) {
                builder.setVibrate(new long[]{0L, 200L, 250L, 200L});
            }

            builder.setSmallIcon(smallIcon);
            builder.setTicker(tickerText);
            if (PushCacheHelper.getInstance().getPushContentShowStatus(context)) {
                builder.setContentTitle(title);
                builder.setContentText(content);
            } else {
                PackageManager pm = context.getPackageManager();

                String name;
                try {
                    name = pm.getApplicationLabel(pm.getApplicationInfo(context.getPackageName(), 128)).toString();
                } catch (NameNotFoundException var17) {
                    name = "";
                }

                builder.setContentTitle(name);
                builder.setContentText(tickerText);
            }

            builder.setContentIntent(pendingIntent);
            builder.setLights(-16711936, 3000, 3000);
            builder.setAutoCancel(true);
            if (VERSION.SDK_INT >= 26) {
                builder.setChannelId("rc_notification_id");
            }

            if (mSound != null && !TextUtils.isEmpty(mSound.toString())) {
                builder.setSound(mSound);
            } else {
                builder.setSound(sound);
                builder.setDefaults(defaults);
            }

            notification = builder.build();
            notification.flags = 1;
        }

        return notification;
    }

    public static void setNotificationSound(Uri uri) {
        mSound = uri;
    }

    public static enum SoundType {
        DEFAULT(0),
        SILENT(1),
        VOIP(2);

        int value;

        private SoundType(int v) {
            this.value = v;
        }
    }
}
