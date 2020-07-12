//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.common;

public class RLog {
    private static final boolean DEBUG = true;

    public RLog() {
    }

    public static void i(String tag, String msg) {
        log(tag, msg, 'i');
    }

    public static void v(String tag, String msg) {
        log(tag, msg, 'v');
    }

    public static void d(String tag, String msg) {
        log(tag, msg, 'd');
    }

    public static void e(String tag, String msg) {
        log(tag, msg, 'e');
    }

    private static void log(String tag, String msg, char level) {
        String RongLog = "RongLog-Push";
        tag = RongLog + "[" + tag + "]";
        if ('e' == level) {
            io.rong.common.rlog.RLog.e(tag, msg);
        } else if ('w' == level) {
            io.rong.common.rlog.RLog.w(tag, msg);
        } else if ('d' == level) {
            io.rong.common.rlog.RLog.d(tag, msg);
        } else if ('i' == level) {
            io.rong.common.rlog.RLog.i(tag, msg);
        } else {
            io.rong.common.rlog.RLog.v(tag, msg);
        }

    }
}
