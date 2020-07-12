//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.rlog;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.rong.common.rlog.RLogReporter.UploadCallback;

public class RLog {
    static final String TAG = "RongLog";
    private static ArrayList<String> levelArray = new ArrayList();
    public static final int NONE = 0;
    public static final int F = 1;
    public static final int E = 2;
    public static final int W = 3;
    public static final int I = 4;
    public static final int D = 5;
    public static final int V = 6;
    private static int mLogLevel = 0;
    private static long mFileMaxSize = 1048576L;
    private static long mZipMaxSize = 1048576L;
    private static boolean isSetLevel = false;
    private static boolean isSetFileMaxSize = false;
    private static boolean isSetZipMaxSize = false;
    private static RLogFileProcessor mWriter;
    private static RLogConfig mConfig;
    private static RLogReporter mReporter;
    private static IRlogOtherProgressCallback mCallback;
    private static UploadCallback mUploadCallback;

    public RLog() {
    }

    public static void init(Context pContext, String pAppKey, String pSdkVersion) {
        mConfig = new RLogConfig(pContext, pAppKey, pSdkVersion);
        if (isSetLevel) {
            mConfig.setLogLevel(mLogLevel);
            isSetLevel = false;
        }

        if (isSetFileMaxSize) {
            mConfig.setFileMaxSize(mFileMaxSize);
            isSetFileMaxSize = false;
        }

        if (isSetZipMaxSize) {
            mConfig.setZipMaxSize(mZipMaxSize);
        }

        mWriter = RLogFileProcessor.init(mConfig);
        mReporter = new RLogReporter();
        if (mUploadCallback != null) {
            mReporter.setUploadCallback(mUploadCallback);
            mUploadCallback = null;
        }

    }

    public static void setRlogOtherProgressCallBack(IRlogOtherProgressCallback pCallBack) {
        mCallback = pCallBack;
    }

    public static void uploadRLog() {
        uploadRLog(true);
    }

    public static void uploadRLog(boolean isInitProgress) {
        if (isInitProgress) {
            if (mReporter != null && mWriter != null) {
                mWriter.upload();
            } else {
                e("RongLog", "No initialization");
            }
        } else if (mCallback != null) {
            mCallback.uploadRLog();
        }

    }

    public static void setUploadCallback(UploadCallback pCallback) {
        mUploadCallback = pCallback;
    }

    public static void setUserId(String userId) {
        if (mConfig != null) {
            mConfig.setUserId(userId);
        }

    }

    public static void setUploadUrl(String uploadUrl) {
        if (mConfig != null) {
            mConfig.setUploadUrl(uploadUrl);
        }

    }

    public static void setLogLevel(int pLevel) {
        setLogLevel(pLevel, true);
    }

    public static void setLogLevel(int pLevel, boolean isInitProgress) {
        if (isInitProgress) {
            if (mConfig != null) {
                mLogLevel = pLevel;
                mConfig.setLogLevel(pLevel);
            } else {
                isSetLevel = true;
                mLogLevel = pLevel;
            }
        } else if (mCallback != null) {
            mCallback.setLogLevel(pLevel);
        }

    }

    public static void setFileMaxSize(long pMaxSize) {
        if (mConfig != null) {
            mFileMaxSize = pMaxSize;
            mConfig.setFileMaxSize(pMaxSize);
        } else {
            isSetFileMaxSize = true;
            mFileMaxSize = pMaxSize;
        }

    }

    public static void setZipMaxSize(long pMaxSize) {
        if (mConfig != null) {
            mZipMaxSize = pMaxSize;
            mConfig.setFileMaxSize(pMaxSize);
        } else {
            isSetZipMaxSize = true;
            mZipMaxSize = pMaxSize;
        }

    }

    public static int v(String tag, String msg) {
        return write(6, "RongLog", "[ " + tag + " ] " + msg);
    }

    public static int d(String tag, String msg) {
        return write(5, "RongLog", "[ " + tag + " ] " + msg);
    }

    public static int i(String tag, String msg) {
        return write(4, "RongLog", "[ " + tag + " ] " + msg);
    }

    public static int w(String tag, String msg) {
        return write(3, "RongLog", "[ " + tag + " ] " + msg);
    }

    public static int e(String tag, String msg) {
        return write(2, "RongLog", "[ " + tag + " ] " + msg);
    }

    public static int e(String tag, String msg, Throwable tr) {
        return write(2, "RongLog", "[ " + tag + " ] " + msg, tr);
    }

    public static int f(String tag, String msg) {
        return write(1, "RongLog", "[ " + tag + " ] " + msg);
    }

    public static void callbackWrite(String log, int level) {
        if (mWriter != null && mConfig != null) {
            int logLevel = mConfig.getLogLevel();
            if (logLevel >= level) {
                mWriter.write(log);
            }
        }

    }

    private static int write(int level, String tag, String msg) {
        return write(level, tag, msg, (Throwable)null);
    }

    private static int write(int level, String tag, String msg, Throwable tr) {
        int result = -1;
        switch(level) {
            case 1:
                result = Log.e("RongLog", "[" + tag + "]" + msg);
                break;
            case 2:
                if (tr == null) {
                    result = Log.e("RongLog", "[" + tag + "]" + msg);
                } else {
                    result = Log.e("RongLog", "[" + tag + "]" + msg, tr);
                }
                break;
            case 3:
                result = Log.w("RongLog", "[" + tag + "]" + msg);
                break;
            case 4:
                result = Log.i("RongLog", "[" + tag + "]" + msg);
                break;
            case 5:
                result = Log.d("RongLog", "[" + tag + "]" + msg);
                break;
            case 6:
                result = Log.v("RongLog", "[" + tag + "]" + msg);
        }

        writeFile(level, tag, msg, tr);
        return result;
    }

    private static void writeFile(int level, String tag, String message, Throwable tr) {
        String metaJson = formatJson(Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), System.currentTimeMillis(), level, tag, message);
        metaJson = metaJson + "\n";
        if (tr != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(tr.toString() + "\n");
            StackTraceElement[] trace = tr.getStackTrace();
            int len = Math.min(9, trace.length);

            for(int i = 0; i < len; ++i) {
                builder.append(trace[i].toString() + "\n");
            }

            metaJson = metaJson + builder.toString();
        }

        if (mCallback != null) {
            mCallback.write(metaJson, level);
        } else {
            callbackWrite(metaJson, level);
        }

    }

    protected static String formatJson(int pid, long tid, long mainTid, long timestamp, int level, String tag, String msg) {
        String jsonStr = "";

        try {
            JSONObject json = new JSONObject();
            json.put("ptid", pid + "-" + tid + (tid == mainTid ? "*" : ""));
            String gmtTime;
            if (mConfig != null && mConfig.isDebugMode()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
                gmtTime = sdf.format(new Date(timestamp));
            } else {
                gmtTime = String.valueOf(timestamp);
            }

            json.put("time", gmtTime);
            json.put("level", levelArray.get(level));
            json.put("tag", tag);
            json.put("msg", msg);
            jsonStr = json.toString();
        } catch (JSONException var14) {
            var14.printStackTrace();
        }

        return jsonStr;
    }

    static {
        levelArray.add("None");
        levelArray.add("F");
        levelArray.add("E");
        levelArray.add("W");
        levelArray.add("I");
        levelArray.add("D");
        levelArray.add("V");
    }

    public interface IRlogOtherProgressCallback {
        void write(String var1, int var2);

        void setLogLevel(int var1);

        void uploadRLog();
    }
}
