//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.ipc;

import android.content.Context;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RongExceptionHandler implements UncaughtExceptionHandler {
    private static final String TAG = "RongExceptionHandler";
    private Context mContext;

    public RongExceptionHandler(Context context) {
        this.mContext = context;
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        long timeMillis = System.currentTimeMillis();
        Date date = new Date(timeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String formatTime = sdf.format(date);
        String path = FileUtils.getCachePath(this.mContext, "crashlog") + "/crash_" + formatTime + ".log";
        File crashFile = new File(path);
        FileOutputStream stream = null;

        try {
            boolean isCreated = crashFile.createNewFile();
            if (!isCreated) {
                RLog.e("RongExceptionHandler", "crashFile.createNewFile() failed");
            }

            stream = new FileOutputStream(crashFile);
            PrintStream printStream = new PrintStream(stream);
            ex.printStackTrace(printStream);
            printStream.close();
        } catch (Exception var21) {
            RLog.e("RongExceptionHandler", "uncaughtException", var21);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException var20) {
                    RLog.e("RongExceptionHandler", "uncaughtException", var20);
                }
            }

        }

        RLog.e("RongExceptionHandler", "uncaughtException", ex);
        System.exit(2);
    }
}
