//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import java.util.concurrent.Future;

public class Call {
    private final Request request;
    private final CallDispatcher dispatcher;

    private Call(CallDispatcher dispatcher, Request request) {
        this.request = request;
        this.dispatcher = dispatcher;
    }

    public static Call create(CallDispatcher dispatcher, Request request) {
        return new Call(dispatcher, request);
    }

    public void enqueue() {
        Call.AsyncCall asyncCall = new Call.AsyncCall();
        this.dispatcher.enqueue(asyncCall);
    }

    class AsyncCall implements Runnable {
        protected Future future;

        AsyncCall() {
        }

        public Object tag() {
            return Call.this.request.tag;
        }

        public void cancel(CancelCallback cancelCallback) {
            if (this.future != null && !this.future.isDone()) {
                this.future.cancel(true);
                this.future = null;
            }

            if (cancelCallback != null) {
                cancelCallback.onCanceled(Call.this.request.tag);
            }

            if (Call.this.request.requestCallBack != null && Call.this.dispatcher.inReadyCalls(Call.this.request.tag)) {
                Call.this.request.requestCallBack.onCanceled(Call.this.request.tag);
            }

        }

        public void pause(PauseCallback pauseCallback) {
            Call.this.request.info.setStop(true);
            pauseCallback.onPaused(Call.this.request.tag);
        }

        public void run() {
            if (Call.this.request.isMessage) {
                Call.this.request.sendRequest();
            } else {
                Call.this.request.sendRequestForNoneMessage();
            }

            Call.this.dispatcher.finish(this);
        }
    }
}
