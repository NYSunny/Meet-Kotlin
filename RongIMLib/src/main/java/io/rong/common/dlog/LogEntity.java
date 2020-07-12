//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.rong.common.dlog.DLog.ILogListener;
import io.rong.common.dlog.DLog.LogTag;
import io.rong.imlib.common.SavePathUtils;

public class LogEntity {
    public static final String LogFileName = "rong_sdk.log";
    public static final String CrashLogFileName = "rong_sdk_crash.log";
    public static final int MODE_RELEASE = 0;
    public static final int MODE_DEBUG = 1;
    public static final int MODE_TEST = 2;
    private static final String LOG_DIR = "rong_log";
    private static final String SP_NAME = "FwLog";
    private static final String SP_SDK_VER = "SDK_VER";
    private static final String SP_APP_KEY = "APP_KEY";
    private static final String SP_USER_ID = "USER_ID";
    private static final String SP_TOKEN = "TOKEN";
    private static final String SP_OFFLINE_LOG_SERVER = "OFFLINE_LOG_SERVER";
    private static final String SP_ONLINE_LOG_SERVER = "ONLINE_LOG_SERVER";
    private static final String SP_START_LOG_TIME = "START_LOG_TIME";
    private static final String SP_MONITOR_LEVEL = "MONITOR_LEVEL";
    private static final String SP_MONITOR_TYPE = "MONITOR_TYPE";
    private static final String SP_UPLOAD_CACHE_LIST = "UPLOAD_CACHE_LIST";
    private static final String Log_Default_Url = "https://feedback.cn.ronghub.com";
    private SharedPreferences sharedPreferences;
    private static LogEntity instance;
    private String logDir;
    private String sdkVer;
    private String appKey;
    private String userId;
    private String token;
    private String offlineLogServer = null;
    private String onlineLogServer = null;
    private long startLogTime;
    private int monitorLevel;
    private int monitorType;
    private int consoleLogLevel;
    private boolean isDebugMode;
    private ILogListener logListener;

    static void init(Context context) {
        if (instance == null) {
            instance = new LogEntity(context);
        }

    }

    private LogEntity(Context context) {
        ApplicationInfo info = context.getApplicationInfo();
        this.isDebugMode = info != null && (info.flags & 2) != 0;
        this.sharedPreferences = context.getSharedPreferences("FwLog", 0);
        File extLogDir;
        if (!SavePathUtils.isSavePathEmpty()) {
            this.logDir = SavePathUtils.getSavePath() + File.separator + "rong_log";
            extLogDir = new File(this.logDir);
            if (!extLogDir.exists()) {
                extLogDir.mkdirs();
            }
        } else {
            extLogDir = null;

            try {
                extLogDir = context.getExternalFilesDir("rong_log");
            } catch (ArrayIndexOutOfBoundsException var6) {
                var6.printStackTrace();
            }

            if (extLogDir != null) {
                this.logDir = extLogDir.getAbsolutePath();
            } else {
                try {
                    String intLogDir = context.getFilesDir().getAbsoluteFile() + File.separator + "rong_log";
                    this.logDir = intLogDir;
                } catch (Exception var5) {
                    DLog.write(1, 512, LogTag.L_CRASH_MAIN_EPT_E.getTag(), "stacks", new Object[]{DLog.stackToString(var5)});
                }
            }
        }

    }

    public static LogEntity getInstance() {
        if (instance == null) {
            throw new RuntimeException("LogEntity.init() has not been called.");
        } else {
            return instance;
        }
    }

    String getLogDir() {
        return this.logDir;
    }

    String getSdkVer() {
        if (this.sdkVer == null) {
            this.sdkVer = this.sharedPreferences.getString("SDK_VER", (String)null);
        }

        return this.sdkVer;
    }

    void setSdkVer(String sdkVer) {
        this.sharedPreferences.edit().putString("SDK_VER", sdkVer).apply();
        this.sdkVer = sdkVer;
    }

    String getAppKey() {
        if (this.appKey == null) {
            this.appKey = this.sharedPreferences.getString("APP_KEY", (String)null);
        }

        return this.appKey;
    }

    void setAppKey(String appKey) {
        this.sharedPreferences.edit().putString("APP_KEY", appKey).apply();
        this.appKey = appKey;
    }

    public String getUserId() {
        if (this.userId == null) {
            this.userId = this.sharedPreferences.getString("USER_ID", (String)null);
        }

        return this.userId;
    }

    void setUserId(String userId) {
        this.sharedPreferences.edit().putString("USER_ID", userId).apply();
        this.userId = userId;
    }

    String getToken() {
        if (this.token == null) {
            this.token = this.sharedPreferences.getString("TOKEN", (String)null);
        }

        return this.token;
    }

    void setToken(String token) {
        this.sharedPreferences.edit().putString("TOKEN", token).apply();
        this.token = token;
    }

    void setOfflineLogServer(String offlineLogServer) {
        this.sharedPreferences.edit().putString("OFFLINE_LOG_SERVER", offlineLogServer).apply();
        this.offlineLogServer = offlineLogServer;
    }

    String getOfflineLogServer() {
        if (this.offlineLogServer == null) {
            this.offlineLogServer = this.sharedPreferences.getString("OFFLINE_LOG_SERVER", "https://feedback.cn.ronghub.com");
        }

        return this.offlineLogServer;
    }

    void setOnlineLogServer(String onlineLogServer) {
        this.sharedPreferences.edit().putString("ONLINE_LOG_SERVER", onlineLogServer).apply();
        this.onlineLogServer = onlineLogServer;
    }

    String getOnlineLogServer() {
        if (this.onlineLogServer == null) {
            this.onlineLogServer = this.sharedPreferences.getString("ONLINE_LOG_SERVER", "");
        }

        return this.onlineLogServer;
    }

    long getStartLogTime() {
        if (this.startLogTime == 0L) {
            this.startLogTime = this.sharedPreferences.getLong("START_LOG_TIME", 0L);
        }

        return this.startLogTime;
    }

    void setStartLogTime(long startLogTime) {
        this.sharedPreferences.edit().putLong("START_LOG_TIME", startLogTime).apply();
        this.startLogTime = startLogTime;
    }

    int getMonitorLevel() {
        if (this.monitorLevel == 0) {
            this.monitorLevel = this.sharedPreferences.getInt("MONITOR_LEVEL", 0);
        }

        return this.monitorLevel;
    }

    void setMonitorLevel(int monitorLevel) {
        if (this.getLogMode() == 2) {
            monitorLevel = 6;
        }

        this.sharedPreferences.edit().putInt("MONITOR_LEVEL", monitorLevel).apply();
        this.monitorLevel = monitorLevel;
    }

    int getMonitorType() {
        if (this.monitorType == 0) {
            this.monitorType = this.sharedPreferences.getInt("MONITOR_TYPE", 0);
        }

        return this.monitorType;
    }

    void setMonitorType(int monitorType) {
        if (this.getLogMode() == 2) {
            monitorType = 268435455;
        }

        this.sharedPreferences.edit().putInt("MONITOR_TYPE", monitorType).apply();
        this.monitorType = monitorType;
    }

    public JSONArray getUploadCacheList() {
        String uploadCacheList = this.sharedPreferences.getString("UPLOAD_CACHE_LIST", (String)null);

        JSONArray jsonArray;
        try {
            if (uploadCacheList == null) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(uploadCacheList);
            }
        } catch (JSONException var4) {
            DLog.write(1, 512, LogTag.G_GET_UPLOAD_CACHE_E.getTag(), "cache", new Object[]{uploadCacheList});
            var4.printStackTrace();
            jsonArray = new JSONArray();
        }

        return jsonArray;
    }

    void setUploadCacheList(JSONArray jsonArray) {
        synchronized(this.sharedPreferences) {
            if (jsonArray != null) {
                this.sharedPreferences.edit().putString("UPLOAD_CACHE_LIST", jsonArray.toString()).apply();
            }

        }
    }

    public void deleteUploadCacheList(int item) {
        synchronized(this.sharedPreferences) {
            JSONArray jsonArray = this.getUploadCacheList();
            if (jsonArray != null) {
                if (VERSION.SDK_INT >= 19) {
                    jsonArray.remove(item);
                }

                this.sharedPreferences.edit().putString("UPLOAD_CACHE_LIST", jsonArray.toString()).apply();
            }

        }
    }

    void addLogStamp(String newLogFile) {
        JSONArray jsonArray = this.getUploadCacheList();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("filename", newLogFile);
            jsonObject.put("sdkVer", getInstance().getSdkVer());
            jsonObject.put("appKey", getInstance().getAppKey());
            jsonObject.put("userId", getInstance().getUserId());
            jsonObject.put("token", getInstance().getToken());
            jsonArray.put(jsonObject);
        } catch (JSONException var5) {
            DLog.write(1, 512, LogTag.G_CRASH_E.getTag(), "stacks", new Object[]{DLog.stackToString(var5)});
            var5.printStackTrace();
        }

        this.setUploadCacheList(jsonArray);
    }

    int getLogMode() {
        return this.isDebugMode ? 1 : 0;
    }

    void setConsoleLogLevel(int level) {
        this.consoleLogLevel = level;
    }

    int getConsoleLogLevel() {
        return this.consoleLogLevel;
    }

    void setLogListener(ILogListener listener) {
        this.logListener = listener;
    }

    ILogListener getLogListener() {
        return this.logListener;
    }

    public String getUploadUrl() {
        return this.getOfflineLogServer();
    }

    long getUploadTimeInterval() {
        return this.getLogMode() == 2 ? 120000L : 1200000L;
    }

    long getOutDateTime() {
        return this.getLogMode() == 2 ? 300000L : 259200000L;
    }

    long getFileMaxSize() {
        return this.getLogMode() == 2 ? 51200L : 1048576L;
    }
}
