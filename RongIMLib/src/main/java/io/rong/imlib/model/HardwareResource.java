//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

public class HardwareResource {
    private HardwareResource.ResourceType mResourceType;

    public HardwareResource() {
    }

    public HardwareResource.ResourceType getmResourceType() {
        return this.mResourceType;
    }

    public void setmResourceType(HardwareResource.ResourceType mResourceType) {
        this.mResourceType = mResourceType;
    }

    public static enum ResourceType {
        AUDIO(1),
        VIDEO(2);

        private int value;

        private ResourceType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static HardwareResource.ResourceType valueOf(int value) {
            HardwareResource.ResourceType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                HardwareResource.ResourceType v = var1[var3];
                if (v.value == value) {
                    return v;
                }
            }

            return null;
        }
    }
}
