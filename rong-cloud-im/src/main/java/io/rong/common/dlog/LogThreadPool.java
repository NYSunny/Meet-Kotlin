//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class LogThreadPool {
    private ScheduledThreadPoolExecutor executorService;

    public LogThreadPool(int threadSize) {
        this.executorService = new ScheduledThreadPoolExecutor(threadSize, this.threadFactory("Upload Dispatcher", false));
        this.executorService.setMaximumPoolSize(5);
        this.executorService.setKeepAliveTime(60L, TimeUnit.SECONDS);
        this.executorService.allowCoreThreadTimeOut(true);
    }

    public ScheduledThreadPoolExecutor getExecutorService() {
        return this.executorService;
    }

    private ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }
}
