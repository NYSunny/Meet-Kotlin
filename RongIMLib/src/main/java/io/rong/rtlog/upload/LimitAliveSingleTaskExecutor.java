//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class LimitAliveSingleTaskExecutor {
    private static final long DEFAULT_THREAD_KEEP_ALIVE_TIME_MILLIS = 60000L;
    private ThreadPoolExecutor executor;

    LimitAliveSingleTaskExecutor() {
        this.executor = new ThreadPoolExecutor(1, 1, 60000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this.executor.allowCoreThreadTimeOut(true);
    }

    void execute(final Runnable runnable, final long delay) {
        this.executor.execute(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException var2) {
                    Thread.currentThread().interrupt();
                }

                runnable.run();
            }
        });
    }

    void execute(Runnable runnable) {
        this.executor.execute(runnable);
    }

    boolean isExecutingTask() {
        return this.executor.getActiveCount() > 0;
    }
}
