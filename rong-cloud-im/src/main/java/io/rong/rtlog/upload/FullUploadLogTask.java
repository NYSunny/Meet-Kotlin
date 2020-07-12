//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import io.rong.common.rlog.RLog;
import io.rong.imlib.common.DeviceUtils;
import io.rong.rtlog.RtFullListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

class FullUploadLogTask extends UploadLogTask {
    private static final String TAG = FullUploadLogTask.class.getSimpleName();
    private static final String FULL_UPLOAD_URL_FORMAT = "%s?version=%s&appkey=%s&userId=%s&logId=%s&deviceId=%s&deviceInfo=%s&platform=Android";
    private static final String NO_DATA_LOG_CONTENT = "no data";
    private static final String MODULE_NOT_INIT_CONTENT = "no log module ";
    private String version;
    private String deviceId;
    private String appKey;
    private String uploadUrl;
    private String userId;
    private String logId;
    private long startTime;
    private long endTime;
    private String logCacheDir;

    public FullUploadLogTask(String version, String deviceId, String appKey, String url, String userId, String logId, long startTime, long endTime, String logCacheDir) {
        this.version = version;
        this.deviceId = deviceId;
        this.appKey = appKey;
        this.uploadUrl = url;
        this.userId = userId;
        this.logId = logId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.logCacheDir = logCacheDir;
    }

    public boolean execute() {
        String outputLogCachePath = this.getLogFile();
        return outputLogCachePath == null ? false : this.upload(outputLogCachePath);
    }

    protected String getUploadUrl() {
        return String.format("%s?version=%s&appkey=%s&userId=%s&logId=%s&deviceId=%s&deviceInfo=%s&platform=Android", this.uploadUrl, this.encodeParams(this.version), this.encodeParams(this.appKey), this.encodeParams(this.userId), this.encodeParams(this.logId), this.encodeParams(this.deviceId), this.encodeParams(DeviceUtils.getDeviceBandModelVersion()));
    }

    protected void onUploadResponse(String response) {
    }

    private String getLogFile() {
        final CountDownLatch logReadLatch = new CountDownLatch(1);
        if (this.logCacheDir == null) {
            return null;
        } else {
            File logFile = new File(this.logCacheDir, "f_" + System.currentTimeMillis() + "_log_cache");
            if (!logFile.getParentFile().exists()) {
                boolean mkLogDirResult = logFile.getParentFile().mkdirs();
                if (!mkLogDirResult) {
                    RLog.d(TAG, "getLogFile mkdirs return false");
                }
            }

            final AtomicLong fileWriteLength = new AtomicLong(0L);
            FileOutputStream logFileInput = null;

            try {
                logFileInput = new FileOutputStream(logFile);
                FileOutputStream finalLogFileInput = logFileInput;
                RtLogNativeProxy.setQueryFullLogListener(new RtFullListener() {
                    public void NotifyFull() {
                        ByteBuffer logReadBuffer = this.getByteBuffer();
                        logReadBuffer.flip();
                        int remaining = logReadBuffer.remaining();
                        byte[] buffer = new byte[remaining];
                        logReadBuffer.get(buffer);
                        logReadBuffer.clear();
                        String log = (new String(buffer)).trim() + "\r\n";

                        try {
                            finalLogFileInput.write(log.getBytes());
                        } catch (IOException var6) {
                            RLog.e(FullUploadLogTask.TAG, "getLogFile write log error", var6);
                        }

                        logReadBuffer.clear();
                        fileWriteLength.getAndAdd((long)log.length());
                    }

                    public void NotifyFullEnd(int result) {
                        RLog.d(FullUploadLogTask.TAG, "getLogFile NotifyFullEnd result:" + result);
                        if (fileWriteLength.get() == 0L) {
                            try {
                                finalLogFileInput.write("no data".getBytes());
                            } catch (IOException var3) {
                                RLog.e(FullUploadLogTask.TAG, "getLogFile write no data error", var3);
                            }
                        }

                        logReadLatch.countDown();
                    }
                });
                RtLogNativeProxy.queryFullLog(4, this.startTime, this.endTime);
                logReadLatch.await();
                logFileInput.flush();
                String var6 = logFile.getAbsolutePath();
                return var6;
            } catch (InterruptedException var18) {
                RLog.e(TAG, "getLogFile", var18);
                Thread.currentThread().interrupt();
            } catch (IOException var19) {
                RLog.e(TAG, "getLogFile", var19);
            } finally {
                if (logFileInput != null) {
                    try {
                        logFileInput.close();
                    } catch (IOException var17) {
                    }
                }

            }

            return null;
        }
    }

    public String getLogId() {
        return this.logId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof FullUploadLogTask) {
            FullUploadLogTask targetTask = (FullUploadLogTask)obj;
            if (targetTask.getLogId() != null && targetTask.getLogId().equals(this.getLogId())) {
                return true;
            }
        }

        return super.equals(obj);
    }

    public int hashCode() {
        return super.hashCode();
    }
}
