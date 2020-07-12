//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.rongpush;

import android.content.Context;
import android.content.Intent;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.common.RLog;
import io.rong.push.platform.IPush;
import io.rong.push.pushconfig.PushConfig;

public class RongPush implements IPush {
    private static final String TAG = RongPush.class.getSimpleName();

    public RongPush() {
    }

    public void register(Context context, PushConfig pushConfig) {
        try {
            Intent intent = new Intent(context, PushService.class);
            intent.setAction("io.rong.push.intent.action.INIT");
            intent.putExtra("deviceId", DeviceUtils.getDeviceId(context, pushConfig.getAppKey()));
            intent.putExtra("appKey", pushConfig.getAppKey());
            intent.putExtra("pushDomain", pushConfig.getPushDomain());
            PushService.enqueueWork(context, intent);
        } catch (SecurityException var4) {
            RLog.e(TAG, "start PushService. " + var4.getMessage());
        }

    }
}
