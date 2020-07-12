//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.destruct;

import io.rong.common.CountDownTimer;
import io.rong.imlib.RongIMClient.DestructCountDownTimerListener;

public class DestructCountDownTimer {
    private static final int COUNTDOWN_INTERVAL = 1000;
    private DestructCountDownTimerListener mListener;
    private CountDownTimer mCountDownTimer;
    private String mMessageId;

    DestructCountDownTimer(String pMessageId, DestructCountDownTimerListener pListener, long millisInFuture) {
        this.mMessageId = pMessageId;
        this.mListener = pListener;
        this.mCountDownTimer = new CountDownTimer(millisInFuture + millisInFuture / 100L, 1000L) {
            public void onTick(long millisUntilFinished) {
                if (DestructCountDownTimer.this.mListener != null) {
                    DestructCountDownTimer.this.mListener.onTick((long)Math.round((float)millisUntilFinished / 1000.0F), DestructCountDownTimer.this.mMessageId);
                }

            }

            public void onFinish() {
                if (DestructCountDownTimer.this.mListener != null) {
                    DestructCountDownTimer.this.mListener.onTick(0L, DestructCountDownTimer.this.mMessageId);
                }

            }
        };
    }

    public void start() {
        if (this.mCountDownTimer != null && !this.mCountDownTimer.isStart()) {
            this.mCountDownTimer.start();
        }

    }

    public void cancel() {
        if (this.mCountDownTimer != null) {
            if (this.mListener != null) {
                this.mListener.onStop(this.mMessageId);
            }

            this.mCountDownTimer.cancel();
        }

    }
}
