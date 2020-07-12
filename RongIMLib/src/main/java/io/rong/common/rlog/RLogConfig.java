//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.rlog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.TreeSet;

import io.rong.imlib.common.SavePathUtils;

public class RLogConfig {
    private static final String TAG = "RLogConfig";
    public static final long DEFAULT_MAX_SIZE = 1048576L;
    public static final String ZIP_SUFFIX = ".gz";
    public static final String LOG_SUFFIX = ".log";
    private static final String LOG_DIR = "RLog";
    private static final String SP_NAME = "FwLog";
    private String mLogPath;
    private String mFileName = "r.log";
    private int mLogLevel = 0;
    private long mFileMaxSize = 1048576L;
    private long mZipMaxSize = 1048576L;
    private String mAppKey;
    private String mSdkVersion;
    private static final String R_LOG_LEVEL = "r_log_level";
    private static final String R_FILE_MAX_SIZE = "r_file_max_size";
    private static final String R_ZIP_MAX_SIZE = "r_zip_max_size";
    private static final String R_ZIP_CONFIG = "r_zip_config";
    private static final String R_START_TIME = "r_start_time";
    private static final String R_UPLOAD_URL = "r_upload_url";
    private static SharedPreferences mSp;
    private boolean isFirstGetLevel = true;
    private boolean isFirstFileMaxSize = true;
    private boolean isFirstZipMaxSize = true;
    private boolean isFirstStartTime = true;
    private boolean isDebugMode;
    private long mStartTime;
    private String uploadUrl;
    private String userId;
    private ZipConfig mZipConfig;

    public RLogConfig(Context pContext, String pAppKey, String pSdkVersion) {
        ApplicationInfo info = pContext.getApplicationInfo();
        this.isDebugMode = info != null && (info.flags & 2) != 0;
        File extLogDir = null;
        this.mAppKey = pAppKey;
        this.mSdkVersion = pSdkVersion;
        mSp = pContext.getSharedPreferences("FwLog", 0);
        if (!SavePathUtils.isSavePathEmpty()) {
            RLog.e("RLogConfig", SavePathUtils.getSavePath());
            this.mLogPath = SavePathUtils.getSavePath() + File.separator + "RLog";
            File file = new File(this.mLogPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } else {
            try {
                extLogDir = pContext.getExternalFilesDir("RLog");
            } catch (ArrayIndexOutOfBoundsException var8) {
                RLog.e("RLogConfig", "RLogConfig", var8);
            }

            if (extLogDir != null) {
                this.mLogPath = extLogDir.getAbsolutePath();
            } else {
                try {
                    String intLogDir = pContext.getFilesDir().getAbsoluteFile() + File.separator + "RLog";
                    this.mLogPath = intLogDir;
                } catch (Exception var7) {
                    RLog.e("RLogConfig", "RLogConfig", var7);
                }
            }
        }

        this.initZipConfig();
    }

    public String getLogPath() {
        return this.mLogPath;
    }

    public void setLogPath(String pLogPath) {
        this.mLogPath = pLogPath;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public void setFileName(String pFileName) {
        this.mFileName = pFileName;
    }

    public String getFilePath() {
        return this.mLogPath + File.separator + this.mFileName;
    }

    public void setLogLevel(int pLogLevel) {
        this.mLogLevel = pLogLevel;
        if (mSp != null) {
            mSp.edit().putInt("r_log_level", pLogLevel).apply();
        }

    }

    public void setFileMaxSize(long pFileMaxSize) {
        this.mFileMaxSize = pFileMaxSize;
        if (mSp != null) {
            mSp.edit().putLong("r_file_max_size", pFileMaxSize).apply();
        }

    }

    public void setZipMaxSize(long pZipMaxSize) {
        this.mZipMaxSize = pZipMaxSize;
        if (mSp != null) {
            mSp.edit().putLong("r_zip_max_size", pZipMaxSize).apply();
        }

    }

    public void setStartTime(long pStartTime) {
        this.mStartTime = pStartTime;
        if (mSp != null) {
            mSp.edit().putLong("r_start_time", pStartTime).apply();
        }

    }

    public int getLogLevel() {
        if (this.isFirstGetLevel && mSp != null) {
            this.mLogLevel = mSp.getInt("r_log_level", 0);
            this.isFirstGetLevel = false;
        }

        return this.mLogLevel;
    }

    public long getFileMaxSize() {
        if (this.isFirstFileMaxSize && mSp != null) {
            this.mFileMaxSize = mSp.getLong("r_file_max_size", 1048576L);
            this.isFirstFileMaxSize = false;
        }

        return this.mFileMaxSize;
    }

    public long getZipMaxSize() {
        if (this.isFirstZipMaxSize && mSp != null) {
            this.mZipMaxSize = mSp.getLong("r_zip_max_size", 1048576L);
            this.isFirstZipMaxSize = false;
        }

        return this.mZipMaxSize;
    }

    public long getStartTime() {
        if (this.isFirstStartTime && mSp != null) {
            this.mStartTime = mSp.getLong("r_start_time", System.currentTimeMillis());
            this.isFirstStartTime = false;
        }

        return this.mStartTime;
    }

    public String getUploadUrl() {
        if (this.uploadUrl == null && mSp != null) {
            this.uploadUrl = mSp.getString("r_upload_url", "https://feedback.cn.ronghub.com");
        }

        return this.uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
        if (mSp != null) {
            mSp.edit().putString("r_upload_url", uploadUrl).apply();
        }

    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public String getSdkVersion() {
        return this.mSdkVersion;
    }

    public boolean isDebugMode() {
        return this.isDebugMode;
    }

    public ZipConfig getZipConfig() {
        return this.mZipConfig;
    }

    public void clearZipConfig() {
        if (this.mZipConfig != null) {
            this.mZipConfig.clear();
            this.mZipConfig = null;
        }

    }

    public void initZipConfig() {
        this.mZipConfig = this.refreshZipConfig();
    }

    private ZipConfig refreshZipConfig() {
        File dir = new File(this.mLogPath);
        if (!dir.exists()) {
            return null;
        } else if (!dir.isDirectory()) {
            return null;
        } else {
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return !pathname.isFile() ? false : pathname.getName().endsWith(".gz");
                }
            });
            TreeSet<File> set = new TreeSet(new Comparator<File>() {
                public int compare(File o1, File o2) {
                    return o1.lastModified() - o2.lastModified() < 0L ? -1 : 1;
                }
            });
            long zipSize = 0L;
            if (files != null) {
                File[] var6 = files;
                int var7 = files.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    File zip = var6[var8];
                    zipSize += zip.length();
                    set.add(zip);
                }
            }

            return new ZipConfig(zipSize, set);
        }
    }

    public class ZipConfig {
        private long currentSize;
        private TreeSet<File> zipFiles;

        public ZipConfig(long pCurrentSize, TreeSet<File> pZipFiles) {
            this.currentSize = pCurrentSize;
            this.zipFiles = pZipFiles;
        }

        public void setCurrentSize(long pCurrentSize) {
            this.currentSize = pCurrentSize;
        }

        public long getCurrentSize() {
            return this.currentSize;
        }

        public TreeSet<File> getZipFiles() {
            return this.zipFiles;
        }

        public void addFile(File pFile) {
            this.zipFiles.add(pFile);
        }

        public void clear() {
            if (this.zipFiles != null) {
                this.zipFiles.clear();
                this.zipFiles = null;
            }

        }
    }
}
