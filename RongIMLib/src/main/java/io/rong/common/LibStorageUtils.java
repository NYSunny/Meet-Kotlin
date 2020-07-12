package io.rong.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.os.Environment;
import android.os.Build.VERSION;
import android.text.TextUtils;
import io.rong.imlib.R.bool;
import io.rong.imlib.R.string;
import io.rong.imlib.common.SavePathUtils;
import java.io.File;

public class LibStorageUtils {
    private static final String TAG = "LibStorageUtils";
    public static final String DIR = "RongCloud";
    public static final String VIDEO = "video";
    public static final String IMAGE = "image";
    public static final String FILE = "file";
    public static final String AUDIO = "audio";
    public static final String MEDIA = "media";
    private static Boolean isQMode = null;
    private static Boolean isBuildAndTargetForQ = null;

    public LibStorageUtils() {
    }

    public static boolean isQMode(Context context) {
        if (isQMode == null) {
            try {
                isQMode = context.getResources().getBoolean(bool.rc_q_storage_mode_enable);
            } catch (NotFoundException var2) {
                RLog.e("LibStorageUtils", "isQMode rc_q_storage_mode_enable not found");
                isQMode = false;
            }
        }

        if (isBuildAndTargetForQ(context) && !isQMode) {
            RLog.e("LibStorageUtils", "请 rc_configuration 设置 rc_q_storage_mode_enable 为 true ");
        }

        return isQMode;
    }

    public static boolean isBuildAndTargetForQ(Context context) {
        if (isBuildAndTargetForQ == null) {
            isBuildAndTargetForQ = VERSION.SDK_INT >= 29 && context.getApplicationInfo().targetSdkVersion >= 29;
        }

        return isBuildAndTargetForQ;
    }

    public static String getMediaDownloadDir(Context context) {
        return getMediaDownloadDir(context, "media");
    }

    public static String getMediaDownloadDir(Context context, String dir) {
        if (!SavePathUtils.isSavePathEmpty()) {
            String savePath = SavePathUtils.getSavePath();
            File imageDir = new File(savePath, "media");
            if (!imageDir.exists() && !imageDir.mkdirs()) {
                RLog.e("LibStorageUtils", "getSavePath mkdirs error path is  " + imageDir.getAbsolutePath());
            }

            return imageDir.getAbsolutePath();
        } else {
            boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
            String result = context.getCacheDir().getPath();
            if (!sdCardExist) {
                RLog.d("LibStorageUtils", "getSavePath error, sdcard does not exist.");
                return result;
            } else {
                if (isQMode(context)) {
                    File externalFilesDir = context.getExternalFilesDir("RongCloud");
                    File file = new File(externalFilesDir, dir);
                    if (!file.exists() && !file.mkdirs()) {
                        result = externalFilesDir.getPath();
                    } else {
                        result = file.getPath();
                    }
                } else {
                    String path = Environment.getExternalStorageDirectory().getPath();
                    String filePath = context.getString(string.rc_media_message_default_save_path);
                    path = path + filePath;
                    File file = new File(path);
                    if (!file.exists() && !file.mkdirs()) {
                        RLog.e("LibStorageUtils", "mkdirs error path is  " + path);
                        result = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    } else {
                        result = path;
                    }
                }

                return result;
            }
        }
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            if (labelRes > 0) {
                return context.getResources().getString(labelRes);
            }

            CharSequence nonLocalizedLabel = packageInfo.applicationInfo.nonLocalizedLabel;
            if (TextUtils.isEmpty(nonLocalizedLabel)) {
                return null;
            }

            return nonLocalizedLabel.toString();
        } catch (NameNotFoundException var5) {
            RLog.e("LibStorageUtils", "getAppName", var5);
        } catch (Exception var6) {
            RLog.e("LibStorageUtils", "getAppName", var6);
        }

        return null;
    }
}
