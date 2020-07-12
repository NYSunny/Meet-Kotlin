//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import io.rong.common.rlog.RLog;

final class Logger {
    private static final String TAG = "RongLog";
    private static boolean isDebug = false;

    private Logger() {
    }

    static void printLog(String msg, Object... args) {
        if (isDebug) {
            RLog.v("RongLog", String.format(msg, args));
        }

    }

    static void setLogEnable(boolean debug) {
        isDebug = debug;
    }
}
