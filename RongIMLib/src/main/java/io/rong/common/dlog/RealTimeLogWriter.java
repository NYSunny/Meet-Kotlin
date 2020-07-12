//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import io.rong.common.dlog.DLog.ILogUploadCallback;

public class RealTimeLogWriter implements LogWriter {
    private static final int Interval = 5;
    private static final int MAX_SIZE = 100;
    private Vector<String> lruLog = new Vector();
    private ScheduledExecutorService executorService;
    private LogThresholdCallback thresholdCallback;
    private ScheduledFuture future;
    private long startTime;

    public RealTimeLogWriter(ScheduledExecutorService executorService, LogThresholdCallback callback) {
        this.executorService = executorService;
        this.thresholdCallback = callback;
    }

    public void write(String log) {
        if (this.startTime == 0L) {
            this.startTime = System.currentTimeMillis();
        }

        this.lruLog.add(log);
        if (this.lruLog.size() > 100) {
            this.lruLog.remove(0);
        }

        if (this.future == null || this.future.isDone()) {
            this.future = this.executorService.schedule(new Runnable() {
                public void run() {
                    long endTime = System.currentTimeMillis();
                    RealTimeLogWriter.this.thresholdCallback.onTimeout(RealTimeLogWriter.this.lruLog, RealTimeLogWriter.this.startTime + "", endTime + "");
                    RealTimeLogWriter.this.lruLog.clear();
                    RealTimeLogWriter.this.startTime = 0L;
                }
            }, 5L, TimeUnit.SECONDS);
        }

    }

    public void flush() {
    }

    public void flushAndReport(boolean isNeedFlush, LogReporter logReporter, ILogUploadCallback callback) {
    }

    public void open() {
    }

    public void close() {
    }
}
