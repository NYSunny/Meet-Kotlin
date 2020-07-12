//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location;

public enum RealTimeLocationType {
    UNKNOWN(0),
    WGS84(1),
    GCJ02(2),
    BD09(3);

    int value;

    private RealTimeLocationType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static RealTimeLocationType valueOf(int value) {
        RealTimeLocationType[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            RealTimeLocationType type = var1[var3];
            if (value == type.ordinal()) {
                return type;
            }
        }

        return UNKNOWN;
    }
}
