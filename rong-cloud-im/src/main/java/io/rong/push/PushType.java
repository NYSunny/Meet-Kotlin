//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push;

import android.text.TextUtils;

public enum PushType {
    UNKNOWN("UNKNOWN"),
    RONG("RONG"),
    HUAWEI("HW"),
    XIAOMI("MI"),
    GOOGLE_FCM("FCM"),
    GOOGLE_GCM("GCM"),
    MEIZU("MEIZU"),
    VIVO("VIVO"),
    OPPO("OPPO");

    private String name;

    private PushType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static PushType getType(String name) {
        if (TextUtils.isEmpty(name)) {
            return RONG;
        } else {
            PushType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                PushType type = var1[var3];
                if (type.getName().equals(name)) {
                    return type;
                }
            }

            return RONG;
        }
    }
}
