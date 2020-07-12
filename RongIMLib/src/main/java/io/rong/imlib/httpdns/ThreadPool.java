//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

final class ThreadPool {
    private static volatile ThreadPool threadPool;
    private static final int corePoolSize = 1;
    private static final int maximumPoolSize = 1;
    private static final int keepAliveTime = 20;
    private static final int maximumBlockingQueue = 50;
    private final Executor executor;

    private ThreadPool() {
        this.executor = new ThreadPoolExecutor(1, 1, 20L, TimeUnit.SECONDS, new LinkedBlockingDeque(50));
    }

    static ThreadPool getInstance() {
        if (threadPool == null) {
            Class var0 = ThreadPool.class;
            synchronized(ThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPool();
                }
            }
        }

        return threadPool;
    }

    Executor getExecutor() {
        return this.executor;
    }
}
