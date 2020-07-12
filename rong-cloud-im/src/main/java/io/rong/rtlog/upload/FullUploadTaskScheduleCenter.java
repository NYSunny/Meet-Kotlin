//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class FullUploadTaskScheduleCenter {
    private LinkedList<FullUploadLogTask> taskQueue = new LinkedList();
    private int retryTaskTimes = 0;
    private LimitAliveSingleTaskExecutor executor = new LimitAliveSingleTaskExecutor();
    private String version;
    private String deviceId;
    private String appKey;
    private RtLogCache logCache;
    private String logCacheDir;

    FullUploadTaskScheduleCenter(String version, String deviceId, String appKey, RtLogCache logCache, String logCacheDir) {
        this.version = version;
        this.deviceId = deviceId;
        this.appKey = appKey;
        this.logCache = logCache;
        this.logCacheDir = logCacheDir;
    }

    synchronized void loadCacheTaskAndStart() {
        this.taskQueue.clear();
        List<FullUploadLogCache> fullUploadLogCaches = this.logCache.loadFullUploadLogCacheList();
        Iterator var2 = fullUploadLogCaches.iterator();

        while(var2.hasNext()) {
            FullUploadLogCache cache = (FullUploadLogCache)var2.next();
            FullUploadLogTask task = new FullUploadLogTask(cache.getVersion(), this.deviceId, this.appKey, cache.getUri(), cache.getUserId(), cache.getLogId(), cache.getStartTime(), cache.getEndTime(), this.logCacheDir);
            this.taskQueue.add(task);
        }

        if (!this.executor.isExecutingTask()) {
            this.nextTask();
        }

    }

    synchronized void addTask(String uri, String userId, String logId, long startTime, long endTime) {
        this.logCache.addFullUploadTaskCache(this.version, this.deviceId, this.appKey, uri, userId, logId, startTime, endTime);
        this.loadCacheTaskAndStart();
    }

    synchronized void endSchedule() {
        this.taskQueue.clear();
    }

    private synchronized void nextTask() {
        if (!this.taskQueue.isEmpty()) {
            FullUploadLogTask next = (FullUploadLogTask)this.taskQueue.getLast();
            this.executeTask(next, 0L);
        }

    }

    private synchronized void executeTask(final FullUploadLogTask task, long delayTime) {
        this.executor.execute(new Runnable() {
            public void run() {
                boolean result = task.execute();
                FullUploadTaskScheduleCenter.this.onTaskEnd(task, result);
            }
        }, delayTime * 1000L);
    }

    private synchronized long getRetryDelayTime() {
        return 5L * (long)Math.pow(2.0D, (double)(this.retryTaskTimes - 1));
    }

    private synchronized void onTaskEnd(FullUploadLogTask task, boolean result) {
        if (result) {
            this.logCache.removeFullUploadTaskCache(task.getLogId());
            this.taskQueue.remove(task);
            this.retryTaskTimes = 0;
            this.nextTask();
        } else if (this.retryTaskTimes < 2) {
            ++this.retryTaskTimes;
            this.executeTask(task, this.getRetryDelayTime());
        } else {
            this.taskQueue.remove(task);
            this.retryTaskTimes = 0;
            this.nextTask();
        }

    }
}
