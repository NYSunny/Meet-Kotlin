//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.os.Build.VERSION;
import android.security.NetworkSecurityPolicy;
import android.text.TextUtils;
import io.rong.common.rlog.RLog;
import org.json.JSONException;
import org.json.JSONObject;

class RtLogTimingUploadConfig {
    private static final String TAG = RtLogTimingUploadConfig.class.getSimpleName();
    private String uploadUrl = this.checkUploadHttpProtocol("logcollection.ronghub.com");
    private int logLevel = 1;
    private int intervalUploadTime = 30;
    private int maxDelayTimes = 5;
    private boolean isUploadEnabled;
    private int currentDelayTimes = 1;

    RtLogTimingUploadConfig() {
    }

    void loadLogConfig(String configJson) {
        this.resetCurrentDelayTimes();
        if ("default_config".equals(configJson)) {
            this.isUploadEnabled = true;
        } else if (TextUtils.isEmpty(configJson)) {
            this.isUploadEnabled = false;
        } else {
            try {
                JSONObject json = new JSONObject(configJson);
                this.uploadUrl = this.checkUploadHttpProtocol(json.optString("url"));
                this.logLevel = json.optInt("level");
                this.intervalUploadTime = json.optInt("itv");
                this.maxDelayTimes = json.optInt("times");
                if (this.maxDelayTimes < 1) {
                    this.maxDelayTimes = 1;
                }

                this.isUploadEnabled = true;
            } catch (JSONException var3) {
                RLog.e(TAG, "parseLogConfig error", var3);
            }

        }
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void setIntervalUploadTime(int intervalUploadTime) {
        this.intervalUploadTime = intervalUploadTime;
    }

    public void setMaxDelayTimes(int maxDelayTimes) {
        this.maxDelayTimes = maxDelayTimes;
    }

    public void setUploadEnabled(boolean uploadEnabled) {
        this.isUploadEnabled = uploadEnabled;
    }

    public String getUploadUrl() {
        return this.uploadUrl;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    public int getIntervalUploadTime() {
        return this.intervalUploadTime;
    }

    public boolean isUploadEnabled() {
        return this.isUploadEnabled;
    }

    public int getCurrentDelayTimes() {
        return this.currentDelayTimes;
    }

    public void increaseDelayTimes() {
        if (this.currentDelayTimes < this.maxDelayTimes) {
            ++this.currentDelayTimes;
        }

    }

    public void resetCurrentDelayTimes() {
        this.currentDelayTimes = 1;
    }

    private String checkUploadHttpProtocol(String oriUrl) {
        if (!TextUtils.isEmpty(oriUrl) && !oriUrl.toLowerCase().startsWith("http")) {
            String format = VERSION.SDK_INT >= 28 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted() ? "https://%s/" : "http://%s/";
            return String.format(format, oriUrl);
        } else {
            return oriUrl;
        }
    }
}
