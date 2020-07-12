//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.util.Log;
import io.rong.common.fwlog.IFwLogConsolePrinter;

public class RtFwLogConsolePrinter implements IFwLogConsolePrinter {
    private static final String LOG_TAG_FORMAT = "[RC:%s][%s]";

    public RtFwLogConsolePrinter() {
    }

    public void printLog(long timestamp, int level, String type, String tag, String metaJson) {
        String logTag = String.format("[RC:%s][%s]", tag, type);
        switch(level) {
            case 0:
            case 1:
                Log.e(logTag, metaJson);
                break;
            case 2:
                Log.w(logTag, metaJson);
                break;
            case 3:
                Log.i(logTag, metaJson);
                break;
            case 4:
                Log.d(logTag, metaJson);
        }

    }
}
