//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.text.TextUtils;
import io.rong.common.rlog.RLog;

class FullUploadLogCache {
    private static final String TAG = FullUploadLogCache.class.getSimpleName();
    private static final String COMMA = ",";
    private String version;
    private String deviceId;
    private String appKey;
    private String uri;
    private String userId;
    private String logId;
    private long startTime;
    private long endTime;

    FullUploadLogCache(String version, String deviceId, String appKey, String uri, String userId, String logId, long startTime, long endTime) {
        this.version = version;
        this.deviceId = deviceId;
        this.appKey = appKey;
        this.uri = uri;
        this.userId = userId;
        this.logId = logId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    String getVersion() {
        return this.version;
    }

    String getDeviceId() {
        return this.deviceId;
    }

    String getAppKey() {
        return this.appKey;
    }

    String getUserId() {
        return this.userId;
    }

    String getUri() {
        return this.uri;
    }

    String getLogId() {
        return this.logId;
    }

    long getStartTime() {
        return this.startTime;
    }

    long getEndTime() {
        return this.endTime;
    }

    String toCSV() {
        return this.version + "," + this.deviceId + "," + this.appKey + "," + this.uri + "," + this.userId + "," + this.logId + "," + this.startTime + "," + this.endTime;
    }

    static FullUploadLogCache parseFromCSV(String csv) {
        if (TextUtils.isEmpty(csv)) {
            return null;
        } else {
            FullUploadLogCache cache = null;

            try {
                String[] columns = csv.split(",");
                String version = columns[0];
                String deviceId = columns[1];
                String appKey = columns[2];
                String uri = columns[3];
                String userId = columns[4];
                String logId = columns[5];
                long startTime = Long.parseLong(columns[6]);
                long endTime = Long.parseLong(columns[7]);
                cache = new FullUploadLogCache(version, deviceId, appKey, uri, userId, logId, startTime, endTime);
            } catch (Exception var13) {
                RLog.e(TAG, "parseFromCSV", var13);
            }

            return cache;
        }
    }
}
