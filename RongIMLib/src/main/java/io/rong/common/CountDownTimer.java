package io.rong.common;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountDownTimer {
    private boolean isStart;
    private final long mMillisInFuture;
    private final long mCountdownInterval;
    private long mStopTimeInFuture;
    private boolean mCancelled = false;
    private static final int MSG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            synchronized(CountDownTimer.this) {
                if (!CountDownTimer.this.mCancelled) {
                    long millisLeft = CountDownTimer.this.mStopTimeInFuture - SystemClock.elapsedRealtime();
                    if (millisLeft <= 0L) {
                        CountDownTimer.this.onFinish();
                    } else {
                        long lastTickStart = SystemClock.elapsedRealtime();
                        CountDownTimer.this.onTick(millisLeft);
                        long lastTickDuration = SystemClock.elapsedRealtime() - lastTickStart;
                        long delay;
                        if (millisLeft < CountDownTimer.this.mCountdownInterval) {
                            delay = millisLeft - lastTickDuration;
                            if (delay < 0L) {
                                delay = 0L;
                            }
                        } else {
                            for(delay = CountDownTimer.this.mCountdownInterval - lastTickDuration; delay < 0L; delay += CountDownTimer.this.mCountdownInterval) {
                            }
                        }

                        this.sendMessageDelayed(this.obtainMessage(1), delay);
                    }

                }
            }
        }
    };

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
    }

    public final synchronized void cancel() {
        this.mCancelled = true;
        this.isStart = false;
        this.mHandler.removeMessages(1);
    }

    public final synchronized CountDownTimer start() {
        this.mCancelled = false;
        this.isStart = true;
        if (this.mMillisInFuture <= 0L) {
            this.onFinish();
            return this;
        } else {
            this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mMillisInFuture;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
            return this;
        }
    }

    public abstract void onTick(long var1);

    public abstract void onFinish();

    public boolean isStart() {
        return this.isStart;
    }
}