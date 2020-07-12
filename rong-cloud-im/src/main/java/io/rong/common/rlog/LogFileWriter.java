//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.rlog;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogFileWriter {
    ExecutorService pool;
    private File mLogFile;
    private FileWriter mFileWriter;
    private IWriterListener mListener;
    private String mFilePath;
    private int openRetry;
    private long timestamp;

    public LogFileWriter(String filePath) {
        this(filePath, (IWriterListener)null);
    }

    public LogFileWriter(String filePath, IWriterListener pListener) {
        this.openRetry = 0;
        this.timestamp = 0L;
        this.pool = Executors.newSingleThreadExecutor();
        this.mFilePath = filePath;
        this.mListener = pListener;
    }

    public void write(final String data) {
        this.pool.submit(new Runnable() {
            public void run() {
                if (LogFileWriter.this.mFileWriter != null || LogFileWriter.this.open()) {
                    try {
                        LogFileWriter.this.mFileWriter.write(data);
                        LogFileWriter.this.mFileWriter.flush();
                        if (LogFileWriter.this.mListener != null) {
                            LogFileWriter.this.mListener.onWriteFinish(LogFileWriter.this.mLogFile.length(), LogFileWriter.this.mLogFile.getAbsolutePath());
                        }
                    } catch (Exception var2) {
                        Log.e("RongLog", "write file error " + LogFileWriter.this.mFilePath, var2);
                    }

                }
            }
        });
    }

    public void stopWrite(final IWriterOnStopListener pListener) {
        this.pool.submit(new Runnable() {
            public void run() {
                pListener.onStopWrite(LogFileWriter.this.mFilePath);
            }
        });
    }

    public boolean open() {
        if (!this.validateOpen()) {
            return false;
        } else {
            try {
                this.mLogFile = new File(this.mFilePath);
                if (!this.mLogFile.exists() && this.mListener != null) {
                    this.mListener.onFileCreate(System.currentTimeMillis());
                }

                File parentFile = this.mLogFile.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }

                this.mFileWriter = new FileWriter(this.mLogFile, true);
                this.openRetry = 0;
                this.timestamp = 0L;
                return true;
            } catch (IOException var2) {
                Log.e("RongLog", "open file error " + this.mFilePath, var2);
                ++this.openRetry;
                this.timestamp = System.currentTimeMillis();
                return false;
            }
        }
    }

    public void close() {
        if (this.mFileWriter != null) {
            try {
                this.mFileWriter.close();
                this.mFileWriter = null;
            } catch (IOException var2) {
                Log.e("RongLog", "RLogFileProcessor close error", var2);
            }
        }

    }

    private boolean validateOpen() {
        if (this.openRetry >= 3) {
            return false;
        } else {
            if (this.timestamp != 0L) {
                long curTime = System.currentTimeMillis();
                if (curTime - this.timestamp < 5000L) {
                    return false;
                }
            }

            return true;
        }
    }

    interface IWriterOnStopListener {
        void onStopWrite(String var1);
    }

    interface IWriterListener {
        void onWriteFinish(long var1, String var3);

        void onFileCreate(long var1);
    }
}
