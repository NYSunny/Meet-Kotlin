//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtils {
    private static final String TAG = "SystemUtils";
    private static final int CMDLINE_BUFFER_SIZE = 64;

    public SystemUtils() {
    }

    public static boolean isAppRunning(@NonNull Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        } else {
            ActivityManager am = (ActivityManager)context.getSystemService("activity");
            if (am == null) {
                RLog.e("SystemUtils", "activityManager is null");
                return false;
            } else {
                List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
                if (infos == null) {
                    return false;
                } else {
                    Iterator var4 = infos.iterator();

                    RunningAppProcessInfo info;
                    do {
                        if (!var4.hasNext()) {
                            return false;
                        }

                        info = (RunningAppProcessInfo)var4.next();
                    } while(!info.processName.equals(name));

                    return true;
                }
            }
        }
    }

    public static String getCurrentProcessName(Context context) {
        String process = "";
        if (context != null) {
            int pid = Process.myPid();
            ActivityManager am = (ActivityManager)context.getSystemService("activity");
            if (am != null) {
                List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
                if (infos != null) {
                    Iterator var5 = infos.iterator();

                    while(var5.hasNext()) {
                        RunningAppProcessInfo info = (RunningAppProcessInfo)var5.next();
                        if (info.pid == pid) {
                            process = info.processName;
                            break;
                        }
                    }
                }
            }
        }

        if (TextUtils.isEmpty(process)) {
            try {
                process = readProcessName();
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }

        return process;
    }

    private static String readProcessName() throws IOException {
        byte[] cmdlineBuffer = new byte[64];
        FileInputStream stream = new FileInputStream("/proc/self/cmdline");
        boolean success = false;

        String var5;
        try {
            int n = stream.read(cmdlineBuffer);
            success = true;
            int endIndex = indexOf(cmdlineBuffer, 0, n, (byte)0);
            var5 = new String(cmdlineBuffer, 0, endIndex > 0 ? endIndex : n);
        } finally {
            close(stream, !success);
        }

        return var5;
    }

    private static int indexOf(byte[] haystack, int offset, int length, byte needle) {
        for(int i = 0; i < haystack.length; ++i) {
            if (haystack[i] == needle) {
                return i;
            }
        }

        return -1;
    }

    public static void close(Closeable closeable, boolean hideException) throws IOException {
        if (closeable != null) {
            if (hideException) {
                try {
                    closeable.close();
                } catch (IOException var3) {
                    RLog.e("SystemUtils", "Hiding IOException because another is pending", var3);
                }
            } else {
                closeable.close();
            }
        }

    }

    public static boolean isValidAppKey(String appKey) {
        Pattern p = Pattern.compile("^[a-z0-9A-Z]+$");
        Matcher m = p.matcher(appKey);
        return m.find();
    }
}
