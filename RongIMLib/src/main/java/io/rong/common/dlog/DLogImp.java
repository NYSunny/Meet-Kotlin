//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import io.rong.imlib.statistics.CrashDetails;

/** @deprecated */
@Deprecated
class DLogImp extends DLog {
    private static ArrayList<String> levelArray = new ArrayList();
    private static SparseArray<String> typeArray = new SparseArray();
    private LogThreadPool threadPool = new LogThreadPool(3);
    private LogWriter fileLogWriter;
    private LogWriter crashLogWriter;
    private LogWriter realTimeLogWriter;
    private LogReporter logReporter;
    private Context context;

    public DLogImp(final Context context, String appKey, String sdkVer) {
        this.context = context;
        LogEntity.init(context);
        this.fileLogWriter = new SimpleLogWriter(LogEntity.getInstance().getLogDir() + File.separator + "rong_sdk.log", new LogThresholdCallback() {
            public void onSize(long size) {
                if (size > LogEntity.getInstance().getFileMaxSize()) {
                    DLogImp.this.fileLogWriter.flush();
                }

            }
        });
        this.crashLogWriter = new CrashLogWriter(LogEntity.getInstance().getLogDir());
        this.realTimeLogWriter = new RealTimeLogWriter(this.threadPool.getExecutorService(), new LogThresholdCallback() {
            void onTimeout(Vector<String> lruLog, String startTime, String endTime) {
                DLogImp.this.logReporter.reportLruLog(lruLog, startTime, endTime);
            }
        });
        this.logReporter = new LogReporter(this.threadPool);
        Timer timer = new Timer();
        if (LogEntity.getInstance().getLogMode() != 1 && LogEntity.getInstance().getMonitorLevel() > 0 && LogEntity.getInstance().getMonitorType() > 0) {
            timer.schedule(new TimerTask() {
                public void run() {
                    DLogImp.this.fileLogWriter.flushAndReport(true, DLogImp.this.logReporter, (ILogUploadCallback)null);
                }
            }, LogEntity.getInstance().getUploadTimeInterval(), LogEntity.getInstance().getUploadTimeInterval());
        }

        Log.i("FwLog", "init FwLog: LogMode = " + LogEntity.getInstance().getLogMode() + ", monitorLevel = " + LogEntity.getInstance().getMonitorLevel() + ", monitorType = " + LogEntity.getInstance().getMonitorType());
        if (LogEntity.getInstance().getLogMode() != 1 && LogEntity.getInstance().getMonitorLevel() > 0 && LogEntity.getInstance().getMonitorType() > 0) {
            Log.d("FwLog", "upload the last log");
            this.fileLogWriter.flushAndReport(true, this.logReporter, (ILogUploadCallback)null);
        }

        this.writeLogHeader();
        LogEntity.getInstance().setAppKey(appKey);
        LogEntity.getInstance().setSdkVer(sdkVer);
        timer.schedule(new TimerTask() {
            public void run() {
                final UncaughtExceptionHandler defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        String reason = e.toString();
                        if (!TextUtils.isEmpty(reason) && reason.contains(":")) {
                            reason = reason.substring(0, reason.indexOf(":"));
                        }

                        DLog.write(1, 2048, LogTag.L_CRASH_MAIN_TRB_F.getTag(), "stack|reason|env", new Object[]{DLog.stackToString(e), reason, CrashDetails.getIMCrashData(context, e.toString())});
                        if (defaultExceptionHandler != null) {
                            defaultExceptionHandler.uncaughtException(t, e);
                        }

                    }
                });
            }
        }, 2000L);
    }

    private void writeLogHeader() {
        String metaJson = DLog.formatMetaJson(false, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), (String)null, new Object[0]);
        this.writeLog(System.currentTimeMillis(), 4, 512, "Log-Opened", metaJson);
    }

    public String formatLog(long timestamp, int level, int type, String tag, String metaJson) {
        if (LogEntity.getInstance().getLogMode() != 0 || level <= LogEntity.getInstance().getConsoleLogLevel()) {
            showConsoleLog(level, tag, metaJson);
            if (LogEntity.getInstance().getLogListener() != null) {
                LogEntity.getInstance().getLogListener().onLogEvent("[RC:" + tag + "]" + metaJson);
            }
        }

        if (LogEntity.getInstance().getLogMode() == 0 && (level > LogEntity.getInstance().getMonitorLevel() || (type & LogEntity.getInstance().getMonitorType()) == 0)) {
            return null;
        } else {
            String jsonStr;
            if (LogEntity.getInstance().getLogMode() == 1) {
                SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
                String gmtTime = sdf.format(new Date(timestamp));
                jsonStr = "{\"time\":\"" + gmtTime + "\",\"level\":\"" + (String)levelArray.get(level) + "\",\"type\":\"" + (String)typeArray.get(type) + "\",\"tag\":\"" + tag + "\",\"meta\":" + metaJson + "}";
            } else {
                jsonStr = "{\"time\":" + timestamp + ",\"level\":\"" + (String)levelArray.get(level) + "\",\"type\":\"" + (String)typeArray.get(type) + "\",\"tag\":\"" + tag + "\",\"meta\":" + metaJson + "}";
            }

            return jsonStr;
        }
    }

    public void uploadLog(ILogUploadCallback callback) {
        this.fileLogWriter.flushAndReport(true, this.logReporter, callback);
    }

    public void writeLog(long timestamp, int level, int type, String tag, String metaJson) {
        String jsonStr = this.formatLog(timestamp, level, type, tag, metaJson);
        if (jsonStr != null) {
            if (type == 2048) {
                this.crashLogWriter.write(jsonStr);
            } else {
                this.fileLogWriter.write(jsonStr);
            }
        }

    }

    private static void showConsoleLog(int level, String tag, String log) {
        tag = "[RC:" + tag + "]";
        switch(level) {
            case 1:
                Log.wtf(tag, log);
                break;
            case 2:
                Log.e(tag, log);
                break;
            case 3:
                Log.w(tag, log);
                break;
            case 4:
                Log.i(tag, log);
                break;
            case 5:
                Log.d(tag, log);
                break;
            case 6:
                Log.v(tag, log);
        }

    }

    public void writeRtLog(long timestamp, int level, int type, String tag, String metaJson) {
        if (LogEntity.getInstance().getLogMode() == 0) {
            if (this.isNetworkOnline()) {
                String jsonStr = this.formatRtLog(timestamp, level, type, tag, metaJson);
                if (jsonStr != null) {
                    this.realTimeLogWriter.write(jsonStr);
                }

            }
        }
    }

    private boolean isNetworkOnline() {
        if (this.context == null) {
            return false;
        } else {
            ConnectivityManager cm = (ConnectivityManager)this.context.getSystemService("connectivity");
            NetworkInfo networkInfo = cm != null ? cm.getActiveNetworkInfo() : null;
            return networkInfo != null ? networkInfo.isConnected() : false;
        }
    }

    public String formatRtLog(long timestamp, int level, int type, String tag, String metaJson) {
        String jsonStr = "{\"time\":" + timestamp + ",\"level\":\"" + (String)levelArray.get(level) + "\",\"type\":\"" + (String)typeArray.get(type) + "\",\"tag\":\"" + tag + "\",\"meta\":" + metaJson + "}";
        return jsonStr;
    }

    static {
        levelArray.add("N");
        levelArray.add("F");
        levelArray.add("E");
        levelArray.add("W");
        levelArray.add("I");
        levelArray.add("D");
        levelArray.add("V");
        typeArray.put(1, "APP");
        typeArray.put(2, "PTC");
        typeArray.put(4, "ENV");
        typeArray.put(8, "DET");
        typeArray.put(16, "CON");
        typeArray.put(32, "RCO");
        typeArray.put(64, "CRM");
        typeArray.put(128, "MSG");
        typeArray.put(256, "MED");
        typeArray.put(512, "LOG");
        typeArray.put(1024, "DEB");
        typeArray.put(2048, "CRS");
        typeArray.put(4096, "RTC");
        typeArray.put(8192, "EPT");
    }
}
