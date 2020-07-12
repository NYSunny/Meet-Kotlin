//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

public enum CustomServiceMode {
    CUSTOM_SERVICE_MODE_NO_SERVICE(0),
    CUSTOM_SERVICE_MODE_ROBOT(1),
    CUSTOM_SERVICE_MODE_HUMAN(2),
    CUSTOM_SERVICE_MODE_ROBOT_FIRST(3),
    CUSTOM_SERVICE_MODE_HUMAN_FIRST(4);

    private int mode;

    private CustomServiceMode(int mode) {
        this.mode = mode;
    }

    public static CustomServiceMode valueOf(int mode) {
        CustomServiceMode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            CustomServiceMode m = var1[var3];
            if (m.mode == mode) {
                return m;
            }
        }

        return CUSTOM_SERVICE_MODE_ROBOT;
    }

    public int getValue() {
        return this.mode;
    }
}
