//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushType;
import io.rong.push.common.RLog;
import io.rong.push.platform.IPush;
//import io.rong.push.platform.google.FCMPush;
//import io.rong.push.platform.google.GCMPush;
//import io.rong.push.platform.hms.HWPush;
//import io.rong.push.platform.meizu.MeizuPush;
//import io.rong.push.platform.mi.MiPush;
//import io.rong.push.platform.oppo.OppoPush;
//import io.rong.push.platform.vivo.VivoPush;
import io.rong.push.rongpush.RongPush;

public class PushFactory {
    private static final String TAG = PushFactory.class.getSimpleName();

    public PushFactory() {
    }

    public static boolean isOnlyDefaultPushOS(PushConfig pushConfig) {
        String os = DeviceUtils.getDeviceManufacturer();
        return (os.contains("Xiaomi") || os.contains("HUAWEI") || os.contains("Meizu")) && !pushConfig.getEnabledPushTypes().contains(PushType.GOOGLE_GCM) && !pushConfig.getEnabledPushTypes().contains(PushType.GOOGLE_FCM);
    }

    public static IPush getPushCenterByType(PushType pushType) {
//        if (pushType.equals(PushType.GOOGLE_GCM)) {
//            return new GCMPush();
//        } else if (pushType.equals(PushType.GOOGLE_FCM)) {
//            return new FCMPush();
//        } else if (pushType.equals(PushType.HUAWEI)) {
//            return new HWPush();
//        } else if (pushType.equals(PushType.XIAOMI)) {
//            return new MiPush();
//        } else if (pushType.equals(PushType.MEIZU)) {
//            return new MeizuPush();
//        } else
            if (pushType.equals(PushType.RONG)) {
            return new RongPush();
        }
//            else if (pushType.equals(PushType.VIVO)) {
//            return new VivoPush();
//        } else if (pushType.equals(PushType.OPPO)) {
//            return new OppoPush();
//        }
            else {
            RLog.e(TAG, "unsupported push type!!");
            return null;
        }
    }
}
