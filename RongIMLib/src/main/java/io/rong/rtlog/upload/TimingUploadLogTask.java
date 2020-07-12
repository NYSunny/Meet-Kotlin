//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.content.Context;
import io.rong.common.rlog.RLog;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.navigation.NavigationCacheHelper;
import io.rong.rtlog.RtCronListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimingUploadLogTask extends UploadLogTask {
    private static final String TAG = TimingUploadLogTask.class.getSimpleName();
    private static final String TIMING_UPLOAD_URL_FORMAT = "%s?version=%s&appkey=%s&userId=%s&deviceId=%s&deviceInfo=%s&platform=Android";
    private Context context;
    private String version;
    private String deviceId;
    private String appKey;
    private String uploadUrl;
    private int uploadLevel;
    private String queryFromTable;
    private String queryToTable;
    private int queryEndRecordId;
    private String uploadResponse;
    private String logCacheDir;

    public TimingUploadLogTask(Context context, String version, String deviceId, String appKey, int logLevel, String uploadUrl, String logCacheDir) {
        this.context = context;
        this.version = version;
        this.deviceId = deviceId;
        this.appKey = appKey;
        this.uploadLevel = logLevel;
        this.uploadUrl = uploadUrl;
        this.logCacheDir = logCacheDir;
    }

    public boolean execute() {
        String outputLogCachePath = this.getLogFile();
        if (outputLogCachePath == null) {
            return false;
        } else {
            File uploadLog = new File(outputLogCachePath);
            boolean upload;
            if (uploadLog.exists() && uploadLog.length() == 0L) {
                upload = uploadLog.delete();
                if (upload) {
                    RLog.d(TAG, "upload file is empty,so do not upload and delete it.");
                }

                return true;
            } else {
                upload = this.upload(outputLogCachePath);
                if (upload) {
                    RtLogNativeProxy.reportTimingUploadFinished(this.queryFromTable, this.queryToTable, this.queryEndRecordId, System.currentTimeMillis());
                }

                return upload;
            }
        }
    }

    protected String getUploadUrl() {
        String userId = this.getLocalUserId();
        return String.format("%s?version=%s&appkey=%s&userId=%s&deviceId=%s&deviceInfo=%s&platform=Android", this.uploadUrl, this.encodeParams(this.version), this.encodeParams(this.appKey), this.encodeParams(userId), this.encodeParams(this.deviceId), this.encodeParams(DeviceUtils.getDeviceBandModelVersion()));
    }

    private String getLocalUserId() {
        return NavigationCacheHelper.getUserId(this.context);
    }

    protected void onUploadResponse(String response) {
        this.uploadResponse = response;
    }

    public String getUploadResponse() {
        return this.uploadResponse;
    }

    private String getLogFile() {
        final CountDownLatch logReadLatch = new CountDownLatch(1);
        if (this.logCacheDir == null) {
            return null;
        } else {
            File logFile = new File(this.logCacheDir, "t_" + System.currentTimeMillis() + "_log_cache");
            if (!logFile.getParentFile().exists()) {
                boolean mkLogDirResult = logFile.getParentFile().mkdirs();
                if (!mkLogDirResult) {
                    RLog.d(TAG, "getLogFile mkdirs return false");
                }
            }

            final AtomicBoolean queryResult = new AtomicBoolean(false);
            FileOutputStream logFileInput = null;
            boolean var18 = false;

            String var6;
            boolean delete;
            label242: {
                label243: {
//                    boolean delete;
                    label244: {
                        try {
                            var18 = true;
                            logFileInput = new FileOutputStream(logFile);
                            FileOutputStream finalLogFileInput = logFileInput;
                            RtLogNativeProxy.setQueryTimingLogListener(new RtCronListener() {
                                public void NotifyCron() {
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
                                        RLog.e(TimingUploadLogTask.TAG, "getLogFile write log error", var6);
                                    }

                                    logReadBuffer.clear();
                                }

                                public void NotifyCronEnd(int result, String first_table_name, String last_table_name, int last_record_id) {
                                    if (result == 0) {
                                        queryResult.set(true);
                                        TimingUploadLogTask.this.queryFromTable = first_table_name;
                                        TimingUploadLogTask.this.queryToTable = last_table_name;
                                        TimingUploadLogTask.this.queryEndRecordId = last_record_id;
                                    } else {
                                        RLog.e(TimingUploadLogTask.TAG, "getLogFile NotifyFullEnd result:" + result);
                                    }

                                    logReadLatch.countDown();
                                }
                            });
                            RtLogNativeProxy.queryTimingLog(this.uploadLevel);
                            logReadLatch.await();
                            logFileInput.flush();
                            if (queryResult.get()) {
                                var6 = logFile.getAbsolutePath();
                                var18 = false;
                                break label242;
                            }

                            var6 = null;
                            var18 = false;
                            break label243;
                        } catch (InterruptedException var24) {
                            RLog.e(TAG, "getLogFile", var24);
                            Thread.currentThread().interrupt();
                            var18 = false;
                        } catch (IOException var25) {
                            RLog.e(TAG, "getLogFile", var25);
                            var18 = false;
                            break label244;
                        } finally {
                            if (var18) {
                                if (logFileInput != null) {
                                    try {
                                        logFileInput.close();
                                    } catch (IOException var19) {
                                    }
                                }

                                if (!queryResult.get() && logFile.exists()) {
                                    boolean d = logFile.delete();
                                    RLog.i(TAG, "delete result is " + d);
                                }

                            }
                        }

                        if (logFileInput != null) {
                            try {
                                logFileInput.close();
                            } catch (IOException var21) {
                            }
                        }

                        if (!queryResult.get() && logFile.exists()) {
                            delete = logFile.delete();
                            RLog.i(TAG, "delete result is " + delete);
                        }

                        return null;
                    }

                    if (logFileInput != null) {
                        try {
                            logFileInput.close();
                        } catch (IOException var20) {
                        }
                    }

                    if (!queryResult.get() && logFile.exists()) {
                        delete = logFile.delete();
                        RLog.i(TAG, "delete result is " + delete);
                    }

                    return null;
                }

                if (logFileInput != null) {
                    try {
                        logFileInput.close();
                    } catch (IOException var23) {
                    }
                }

                if (!queryResult.get() && logFile.exists()) {
                    delete = logFile.delete();
                    RLog.i(TAG, "delete result is " + delete);
                }

                return var6;
            }

            if (logFileInput != null) {
                try {
                    logFileInput.close();
                } catch (IOException var22) {
                }
            }

            if (!queryResult.get() && logFile.exists()) {
                delete = logFile.delete();
                RLog.i(TAG, "delete result is " + delete);
            }

            return var6;
        }
    }
}
