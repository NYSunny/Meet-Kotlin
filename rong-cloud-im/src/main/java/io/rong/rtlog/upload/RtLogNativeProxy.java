//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import io.rong.common.rlog.RLog;
import io.rong.rtlog.RtCronListener;
import io.rong.rtlog.RtFullListener;
import io.rong.rtlog.RtLogNative;

class RtLogNativeProxy {
    private static final String TAG = RtLogNativeProxy.class.getSimpleName();
    private RtLogNative nativeObject;

    private RtLogNativeProxy() {
        this.nativeObject = new RtLogNative();
    }

    public static RtLogNativeProxy getInstance() {
        return RtLogNativeProxy.SingletonHolder.instance;
    }

    public static boolean initialize(Context context, String dbParentPath, String appKey, String sessionId) {
        RtLogNative nativeObject = RtLogNativeProxy.SingletonHolder.instance.nativeObject;
        boolean result = false;

        try {
            result = nativeObject.initialize(dbParentPath, appKey, sessionId) == 0;
        } catch (Exception var7) {
            RLog.d(TAG, "initialize - native call exception :" + var7.toString());
        }

        return result;
    }

    public static void writeLog(int level, String type, String tag, String content, long timeStamp) {
        try {
            RtLogNativeProxy.SingletonHolder.instance.nativeObject.writeMessage(level, type, tag, content, timeStamp);
        } catch (Exception var7) {
            RLog.d(TAG, "writeLog - native call exception :" + var7.toString());
        }

    }

    public static boolean queryTimingLog(int level) {
        boolean result = false;

        try {
            result = RtLogNativeProxy.SingletonHolder.instance.nativeObject.queryCronMessage(level, false) == 0;
        } catch (Exception var3) {
            RLog.d(TAG, "queryTimingLog - native call exception :" + var3.toString());
        }

        return result;
    }

    public static boolean queryFullLog(int level, long startTime, long endTime) {
        boolean result = false;

        try {
            result = RtLogNativeProxy.SingletonHolder.instance.nativeObject.queryFullMessage(level, startTime, endTime, true) == 0;
        } catch (Exception var7) {
            RLog.d(TAG, "queryFullLog - native call exception :" + var7.toString());
        }

        return result;
    }

    public static void setQueryTimingLogListener(RtCronListener listener) {
        try {
            RtLogNativeProxy.SingletonHolder.instance.nativeObject.setCronListener(listener);
        } catch (Exception var2) {
            RLog.d(TAG, "setQueryTimingLogListener - native call exception :" + var2.toString());
        }

    }

    public static void setQueryFullLogListener(RtFullListener listener) {
        try {
            RtLogNativeProxy.SingletonHolder.instance.nativeObject.setFullListener(listener);
        } catch (Exception var2) {
            RLog.d(TAG, "setQueryFullLogListener - native call exception :" + var2.toString());
        }

    }

    public static boolean reportTimingUploadFinished(String fromTable, String toTable, int lastRecordId, long timeStamp) {
        boolean result = false;

        try {
            result = RtLogNativeProxy.SingletonHolder.instance.nativeObject.updateCronUploadTime(fromTable, toTable, lastRecordId, timeStamp) == 0;
        } catch (Exception var7) {
            RLog.d(TAG, "reportTimingUploadFinished - native call exception :" + var7.toString());
        }

        return result;
    }

    private static class SingletonHolder {
        @SuppressLint({"StaticFieldLeak"})
        private static final RtLogNativeProxy instance = new RtLogNativeProxy();

        private SingletonHolder() {
        }
    }
}
