//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.content.Context;
import io.rong.common.fwlog.IFwLogWriter;
import io.rong.common.rlog.RLog;
import io.rong.imlib.common.NetUtils;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RtFwLogWriter implements IFwLogWriter {
    private final String TAG = RtFwLogWriter.class.getCanonicalName();
    private Executor nativeExecutor = Executors.newSingleThreadExecutor();
    private Context context;
    private final int MAX_CONTENT_SIZE = 1024;

    public RtFwLogWriter(Context context) {
        this.context = context.getApplicationContext();
    }

    public void write(final int level, final String type, final String tag, final String metaJson, final long timestamp) {
        this.nativeExecutor.execute(new Runnable() {
            public void run() {
                if (level >= 0 && level <= 4) {
                    int writeLevel = level;
                    if ((level == 1 || level == 2) && !NetUtils.getCacheNetworkAvailable(RtFwLogWriter.this.context)) {
                        writeLevel = 3;
                    }

                    String content = metaJson;
                    if (metaJson != null) {
                        long contentSize = (long)metaJson.getBytes().length;
                        if (contentSize > 1024L) {
                            content = new String(Arrays.copyOf(metaJson.getBytes(), 1024));
                        }
                    }

                    RtLogNativeProxy.writeLog(writeLevel, type, tag, content, timestamp);
                } else {
                    RLog.e(RtFwLogWriter.this.TAG, "write log with invalid level:" + level + ",tag:" + tag + " ,metaJson:" + metaJson + " ,timestamp:" + timestamp);
                }
            }
        });
    }
}
