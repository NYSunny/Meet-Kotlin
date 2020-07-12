//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.security.NetworkSecurityPolicy;
import android.text.TextUtils;
import android.util.Base64;
import io.rong.common.fwlog.FwLog;
import io.rong.common.rlog.RLog;
import io.rong.imlib.model.Message;
import io.rong.imlib.navigation.NavigationCacheHelper;
import io.rong.message.LogCmdMessage;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class RtLogUploadManager {
    private static final String TAG = RtLogUploadManager.class.getSimpleName();
    private static final String FULL_UPLOAD_SENDER_USER_ID = "rongcloudsystem";
    private LimitAliveSingleTaskExecutor workExecutor;
    private Context context;
    private TimingUploadTaskScheduleCenter timingUploadCenter;
    private FullUploadTaskScheduleCenter fullUploadCenter;
    private long lastInBackgroundTimeMillis;

    private RtLogUploadManager() {
        this.workExecutor = new LimitAliveSingleTaskExecutor();
        this.lastInBackgroundTimeMillis = -1L;
    }

    public static RtLogUploadManager getInstance() {
        return RtLogUploadManager.SingletonHolder.instance;
    }

    public synchronized void init(final Context context, final String version, final String deviceId, final String appKey) {
        if (context != null) {
            this.workExecutor.execute(new Runnable() {
                public void run() {
                    RtLogUploadManager.this.internalInit(context.getApplicationContext(), version, deviceId, appKey);
                }
            });
        }

    }

    private void internalInit(Context context, String version, String deviceId, String appKey) {
        this.context = context.getApplicationContext();
        FwLog.setLogConsolePrinter(new RtFwLogConsolePrinter());
        FwLog.listenUncaughtException(context);
        FwLog.setConsoleLogLevel(4);
        RtLogCache logCache = new RtLogCache(this.context, appKey);
        if (this.fullUploadCenter != null) {
            this.fullUploadCenter.endSchedule();
        }

        this.fullUploadCenter = new FullUploadTaskScheduleCenter(version, deviceId, appKey, logCache, this.getUploadFileCacheDir());
        this.fullUploadCenter.loadCacheTaskAndStart();
        File dbParentFile = new File(context.getFilesDir() + File.separator + appKey + File.separator + "rclog");
        boolean initialize;
        if (!dbParentFile.exists()) {
            initialize = dbParentFile.mkdirs();
            if (!initialize) {
                RLog.e(TAG, "create log db directory failed. Write log will not available.");
                return;
            }
        }

        initialize = RtLogNativeProxy.initialize(context, dbParentFile.getAbsolutePath(), appKey, this.getSessionId());
        if (!initialize) {
            RLog.e(TAG, "RtLogNative initialize failed. Write log will not available.");
        }

        FwLog.setDirectWriter(new RtFwLogWriter(context));
        if (this.timingUploadCenter != null) {
            this.timingUploadCenter.endSchedule();
        }

        this.timingUploadCenter = new TimingUploadTaskScheduleCenter(context, version, deviceId, appKey, logCache, this.getUploadFileCacheDir());
        this.timingUploadCenter.setToBackgroundTime(this.lastInBackgroundTimeMillis);
    }

    public void startTimingUploadTask() {
        if (this.timingUploadCenter != null) {
            this.workExecutor.execute(new Runnable() {
                public void run() {
                    RtLogUploadManager.this.timingUploadCenter.startTask();
                }
            });
        } else {
            RLog.d(TAG, "startTimingUploadTask - timingUploadCenter is null, may not init.");
        }

    }

    public void setIsBackgroundMode(final boolean isBackground) {
        this.workExecutor.execute(new Runnable() {
            public void run() {
                if (isBackground) {
                    RtLogUploadManager.this.lastInBackgroundTimeMillis = System.currentTimeMillis();
                } else {
                    RtLogUploadManager.this.lastInBackgroundTimeMillis = -1L;
                }

                if (RtLogUploadManager.this.timingUploadCenter != null) {
                    RtLogUploadManager.this.timingUploadCenter.setToBackgroundTime(RtLogUploadManager.this.lastInBackgroundTimeMillis);
                }

            }
        });
    }

    public void updateTimingUploadConfig(final String configJson) {
        if (this.timingUploadCenter != null) {
            this.workExecutor.execute(new Runnable() {
                public void run() {
                    RtLogUploadManager.this.timingUploadCenter.updateTimingUploadConfig(configJson);
                }
            });
        } else {
            RLog.d(TAG, "updateConfig - timingUploadCenter is null, may not init.");
        }

    }

    public void createFullUploadTask(final Message message) {
        this.workExecutor.execute(new Runnable() {
            public void run() {
                if (message.getContent() instanceof LogCmdMessage && "rongcloudsystem".equals(message.getSenderUserId())) {
                    LogCmdMessage logCmdMessage = (LogCmdMessage)message.getContent();
                    String platform = logCmdMessage.getPlatform();
                    String packageName = logCmdMessage.getPackageName();
                    if (platform != null && platform.toLowerCase().contains("android") && (TextUtils.isEmpty(packageName) || RtLogUploadManager.this.context.getPackageName().toLowerCase().equals(packageName.toLowerCase()))) {
                        String uploadUrl = logCmdMessage.getUri();
                        if (!TextUtils.isEmpty(uploadUrl) && !uploadUrl.toLowerCase().startsWith("http")) {
                            String format = VERSION.SDK_INT >= 28 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted() ? "https://%s/" : "http://%s/";
                            uploadUrl = String.format(format, uploadUrl);
                        }

                        RtLogUploadManager.this.addFullUploadTask(uploadUrl, NavigationCacheHelper.getUserId(RtLogUploadManager.this.context), logCmdMessage.getLogId(), logCmdMessage.getStartTime(), logCmdMessage.getEndTime());
                    }
                }

            }
        });
    }

    private String getSessionId() {
        UUID uuid = UUID.randomUUID();
        MessageDigest mdInst = null;
        String result = uuid.toString();

        try {
            mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(uuid.toString().getBytes());
            byte[] mds = mdInst.digest();
            mds = Base64.encode(mds, 0);
            result = new String(mds);
            result = result.replace("=", "").replace("+", "-").replace("/", "_").replace("\n", "");
        } catch (NoSuchAlgorithmException var5) {
        }

        return result;
    }

    private void addFullUploadTask(String uri, String userId, String logId, long startTime, long endTime) {
        if (this.fullUploadCenter != null) {
            this.fullUploadCenter.addTask(uri, userId, logId, startTime, endTime);
        } else {
            RLog.d(TAG, "addFullUploadTask - fullUploadCenter is null, may not init.");
        }

    }

    private String getUploadFileCacheDir() {
        if (this.context != null) {
            File externalCacheDir = this.context.getExternalCacheDir();
            if (externalCacheDir != null) {
                return externalCacheDir.getAbsolutePath();
            } else {
                File cacheDir = this.context.getCacheDir();
                return cacheDir != null ? cacheDir.getAbsolutePath() : null;
            }
        } else {
            RLog.d(TAG, "getUploadFileCacheDir - context is null, may not init.");
            return null;
        }
    }

    private static class SingletonHolder {
        @SuppressLint({"StaticFieldLeak"})
        private static final RtLogUploadManager instance = new RtLogUploadManager();

        private SingletonHolder() {
        }
    }
}
