//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.rong.common.dlog.DLog.ILogUploadCallback;

public class SimpleLogWriter implements LogWriter {
    protected String logPath;
    private File logFile;
    private FileWriter fileWriter;
    private LogThresholdCallback sizeCallback;
    private Handler writeHandler;

    public SimpleLogWriter(String logPath) {
        this.logPath = logPath;
        this.open();
    }

    protected SimpleLogWriter(String logPath, LogThresholdCallback callback) {
        HandlerThread writeThread = new HandlerThread("cn.rongcloud.fwLogWriter");
        writeThread.start();
        this.writeHandler = new Handler(writeThread.getLooper());
        this.sizeCallback = callback;
        this.logPath = logPath;
        this.open();
    }

    public void write(final String log) {
        if (Thread.currentThread().getName().equals("cn.rongcloud.fwLogWriter")) {
            this.internalWrite(log);
        } else {
            this.writeHandler.post(new Runnable() {
                public void run() {
                    SimpleLogWriter.this.internalWrite(log);
                }
            });
        }

    }

    protected final void internalWrite(String log) {
        try {
            if (this.fileWriter != null) {
                this.fileWriter.write(log + "\n");
                this.fileWriter.flush();
            }
        } catch (IOException var3) {
            Log.e("FwLog", "write file error " + this.logPath);
            var3.printStackTrace();
        }

        if (this.sizeCallback != null) {
            this.sizeCallback.onSize(this.logFile.length());
        }

    }

    private void flushAndStamp() {
        this.logFile = new File(this.logPath);
        if (this.logFile.exists() && this.logFile.length() != 0L) {
            this.close();
            long startLogTime = LogEntity.getInstance().getStartLogTime();
            long endLogTime = this.logFile.lastModified();
            if (endLogTime == 0L) {
                endLogTime = System.currentTimeMillis();
            }

            String zipFilename = startLogTime + "_" + endLogTime + this.getZipFileSuffix();
            String zipFilePath = LogEntity.getInstance().getLogDir() + File.separator + zipFilename;
            if (LogZipper.gzipFile(this.logFile.getAbsolutePath(), zipFilePath)) {
                this.logFile.delete();
                this.open();
                LogEntity.getInstance().addLogStamp(zipFilename);
            }

            LogEntity.getInstance().setStartLogTime(System.currentTimeMillis());
            Log.e("FwLog", "zip file logPath = " + this.logPath);
        } else {
            Log.e("FwLog", "file not exist " + this.logPath);
        }
    }

    public void flush() {
        this.writeHandler.post(new Runnable() {
            public void run() {
                SimpleLogWriter.this.flushAndStamp();
            }
        });
    }

    public void flushAndReport(final boolean isNeedFlush, final LogReporter logReporter, final ILogUploadCallback callback) {
        if (LogEntity.getInstance().getUserId() == null) {
            if (callback != null) {
                callback.onLogUploaded(-1);
            }

        } else {
            this.writeHandler.post(new Runnable() {
                public void run() {
                    if (isNeedFlush) {
                        SimpleLogWriter.this.flushAndStamp();
                    }

                    logReporter.reportFileLog(callback);
                }
            });
        }
    }

    public void open() {
        this.logFile = new File(this.logPath);

        try {
            this.fileWriter = new FileWriter(this.logFile, true);
        } catch (IOException var2) {
            Log.e("FwLog", "open file error " + this.logPath);
            var2.printStackTrace();
        }

    }

    public void close() {
        try {
            if (this.fileWriter != null) {
                this.fileWriter.close();
            }
        } catch (IOException var2) {
            Log.e("FwLog", "close file error " + this.logPath);
            var2.printStackTrace();
        }

    }

    protected String getZipFileSuffix() {
        return ".gz";
    }
}
