//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

public interface RongCommonDefine {
    public static enum GetMessageDirection {
        BEHIND(0),
        FRONT(1);

        int value;

        private GetMessageDirection(int v) {
            this.value = v;
        }

        int getValue() {
            return this.value;
        }
    }
}
