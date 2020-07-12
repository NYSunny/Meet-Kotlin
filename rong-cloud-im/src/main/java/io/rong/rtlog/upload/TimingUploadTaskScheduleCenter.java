//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.content.Context;
import android.text.TextUtils;
import io.rong.common.rlog.RLog;
import org.json.JSONException;
import org.json.JSONObject;

class TimingUploadTaskScheduleCenter {
    private static final String TAG = TimingUploadTaskScheduleCenter.class.getSimpleName();
    private LimitAliveSingleTaskExecutor executor = new LimitAliveSingleTaskExecutor();
    private RtLogTimingUploadConfig config;
    private RtLogCache cache;
    private String version;
    private String deviceId;
    private String appKey;
    private String logCacheDir;
    private boolean isStartSchedule = false;
    private Context context;
    private long inBackgroundTime = -1L;
    private boolean isStopInBackground = false;

    TimingUploadTaskScheduleCenter(Context context, String version, String deviceId, String appKey, RtLogCache logCache, String logCacheDir) {
        this.context = context;
        this.version = version;
        this.deviceId = deviceId;
        this.appKey = appKey;
        this.cache = logCache;
        this.logCacheDir = logCacheDir;
        this.config = new RtLogTimingUploadConfig();
        this.config.loadLogConfig(this.cache.loadTimingUploadConfigCache());
    }

    private synchronized TimingUploadLogTask obtainTask() {
        return new TimingUploadLogTask(this.context, this.version, this.deviceId, this.appKey, this.config.getLogLevel(), this.config.getUploadUrl(), this.logCacheDir);
    }

    synchronized void startTask() {
        if (!this.isStartSchedule) {
            this.isStartSchedule = true;
            this.nextTask(30L, false);
        }
    }

    synchronized void endSchedule() {
        this.isStartSchedule = false;
    }

    synchronized void updateTimingUploadConfig(String configJson) {
        this.config.loadLogConfig(configJson);
        this.cache.saveTimingUploadConfig(configJson);
    }

    private synchronized void nextTask(long delayTime, boolean isCountDelayTimes) {
        if (this.shouldStopInBackground()) {
            this.isStopInBackground = true;
        } else {
            if (this.isStartSchedule && this.config.isUploadEnabled()) {
                TimingUploadLogTask next = this.obtainTask();
                this.executeTask(next, delayTime, isCountDelayTimes);
            }

        }
    }

    private synchronized void executeTask(final TimingUploadLogTask task, long delayTime, final boolean isCountDelayTimes) {
        this.executor.execute(new Runnable() {
            public void run() {
                boolean result = task.execute();
                TimingUploadTaskScheduleCenter.this.onTaskEnd(task, result, isCountDelayTimes);
            }
        }, delayTime * 1000L);
    }

    private synchronized long getDelayTime() {
        return (long)this.config.getIntervalUploadTime() * (long)Math.pow(2.0D, (double)(this.config.getCurrentDelayTimes() - 1));
    }

    private synchronized void onTaskEnd(TimingUploadLogTask task, boolean result, boolean isCountDelayTimes) {
        if (result && !TextUtils.isEmpty(task.getUploadResponse())) {
            try {
                JSONObject responseJson = new JSONObject(task.getUploadResponse());
                int nextTime = responseJson.optInt("nextTime");
                int level = responseJson.optInt("level");
                int logSwitch = responseJson.optInt("logSwitch");
                if (logSwitch == 1) {
                    this.config.setUploadEnabled(true);
                    this.config.setIntervalUploadTime(nextTime);
                    this.config.resetCurrentDelayTimes();
                    this.config.setLogLevel(level);
                } else {
                    this.config.setUploadEnabled(false);
                }
            } catch (JSONException var8) {
                RLog.e(TAG, "onTaskEnd", var8);
            }
        }

        if (isCountDelayTimes) {
            this.config.increaseDelayTimes();
        }

        this.nextTask(this.getDelayTime(), true);
    }

    private synchronized boolean shouldStopInBackground() {
        return this.inBackgroundTime != -1L && System.currentTimeMillis() - this.inBackgroundTime > 300000L;
    }

    synchronized void setToBackgroundTime(long lastInBackgroundTimeMillis) {
        if (lastInBackgroundTimeMillis > 0L) {
            this.inBackgroundTime = lastInBackgroundTimeMillis;
        } else {
            this.inBackgroundTime = -1L;
            if (this.isStopInBackground) {
                this.isStopInBackground = false;
                this.nextTask(this.getDelayTime(), true);
            }
        }

    }
}
