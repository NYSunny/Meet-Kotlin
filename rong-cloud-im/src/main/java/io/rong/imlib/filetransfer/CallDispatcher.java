//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import androidx.annotation.NonNull;
import io.rong.imlib.filetransfer.Call.AsyncCall;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CallDispatcher {
    private static final int MAX_RUNNING_TASK = 4;
    private final Deque<AsyncCall> readyCalls = new ArrayDeque();
    private final Deque<AsyncCall> runningCalls = new ArrayDeque();
    private ExecutorService executorService;

    public CallDispatcher() {
    }

    public synchronized ExecutorService getExecutorService() {
        if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(4, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue(), this.threadFactory());
        }

        return this.executorService;
    }

    private ThreadFactory threadFactory() {
        return new ThreadFactory() {
            public Thread newThread(@NonNull Runnable runnable) {
                Thread result = new Thread(runnable, "HttpEngine Dispatcher");
                result.setDaemon(false);
                return result;
            }
        };
    }

    public synchronized void enqueue(AsyncCall asyncCall) {
        if (this.runningCalls.size() < 4) {
            asyncCall.future = this.getExecutorService().submit(asyncCall);
            this.runningCalls.add(asyncCall);
        } else {
            this.readyCalls.add(asyncCall);
        }

    }

    public synchronized void cancel(Object tag, CancelCallback callback) {
        Iterator var3 = this.readyCalls.iterator();

        AsyncCall call;
        do {
            if (!var3.hasNext()) {
                var3 = this.runningCalls.iterator();

                do {
                    if (!var3.hasNext()) {
                        callback.onError(-3);
                        return;
                    }

                    call = (AsyncCall)var3.next();
                } while(!call.tag().equals(tag));

                call.cancel(callback);
                return;
            }

            call = (AsyncCall)var3.next();
        } while(!call.tag().equals(tag));

        call.cancel(callback);
        this.readyCalls.remove(call);
    }

    public synchronized void cancelAll() {
        Iterator var1 = this.readyCalls.iterator();

        AsyncCall call;
        while(var1.hasNext()) {
            call = (AsyncCall)var1.next();
            this.readyCalls.remove(call);
            call.cancel((CancelCallback)null);
        }

        this.readyCalls.clear();
        var1 = this.runningCalls.iterator();

        while(var1.hasNext()) {
            call = (AsyncCall)var1.next();
            call.cancel((CancelCallback)null);
        }

        this.runningCalls.clear();
    }

    public synchronized void pause(Object tag, PauseCallback callback) {
        Iterator var3 = this.runningCalls.iterator();

        AsyncCall call;
        do {
            if (!var3.hasNext()) {
                callback.onError(-3);
                return;
            }

            call = (AsyncCall)var3.next();
        } while(!call.tag().equals(tag));

        call.pause(callback);
    }

    public synchronized void finish(AsyncCall runnable) {
        if (!this.runningCalls.remove(runnable)) {
            throw new RuntimeException("Not in running list.");
        } else {
            this.promoteCalls();
        }
    }

    private void promoteCalls() {
        if (this.runningCalls.size() < 4 && !this.readyCalls.isEmpty()) {
            Iterator i = this.readyCalls.iterator();

            do {
                if (!i.hasNext()) {
                    return;
                }

                AsyncCall call = (AsyncCall)i.next();
                this.runningCalls.add(call);
                call.future = this.getExecutorService().submit(call);
                i.remove();
            } while(this.runningCalls.size() < 4);

        }
    }

    public boolean inReadyCalls(Object tag) {
        Iterator var2 = this.readyCalls.iterator();

        AsyncCall call;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            call = (AsyncCall)var2.next();
        } while(!call.tag().equals(tag));

        return true;
    }
}
