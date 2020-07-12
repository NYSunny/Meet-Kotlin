//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common;

public class RLog {
    public RLog() {
    }

    public static int v(String tag, String msg) {
        return io.rong.common.rlog.RLog.v(tag, msg);
    }

    public static int d(String tag, String msg) {
        return io.rong.common.rlog.RLog.d(tag, msg);
    }

    public static int i(String tag, String msg) {
        return io.rong.common.rlog.RLog.i(tag, msg);
    }

    public static int w(String tag, String msg) {
        return io.rong.common.rlog.RLog.w(tag, msg);
    }

    public static int e(String tag, String msg) {
        return io.rong.common.rlog.RLog.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return io.rong.common.rlog.RLog.e(tag, msg, tr);
    }

    public static int f(String tag, String msg) {
        return io.rong.common.rlog.RLog.f(tag, msg);
    }
}
