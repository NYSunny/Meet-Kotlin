//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.imlib.LibHandlerStub;
import io.rong.imlib.statistics.CrashDetails;
import java.lang.Thread.UncaughtExceptionHandler;

public class RongService extends Service implements UncaughtExceptionHandler {
    private final String TAG = RongService.class.getSimpleName();
    private UncaughtExceptionHandler defaultExceptionHandler;

    public RongService() {
    }

    public void onCreate() {
        super.onCreate();
        RLog.d(this.TAG, "onCreate, pid=" + Process.myPid());
        this.defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public IBinder onBind(Intent intent) {
        RLog.d(this.TAG, "onBind, pid=" + Process.myPid());
        String appKey = intent.getStringExtra("appKey");
        String deviceId = intent.getStringExtra("deviceId");
        return new LibHandlerStub(this, appKey, deviceId);
    }

    public boolean onUnbind(Intent intent) {
        RLog.d(this.TAG, "onUnbind, pid=" + Process.myPid());
        return super.onUnbind(intent);
    }

    public void onDestroy() {
        RLog.d(this.TAG, "onDestroy, pid=" + Process.myPid());
        Process.killProcess(Process.myPid());
    }

    public void uncaughtException(Thread t, Throwable e) {
        String reason = e.toString();
        if (!TextUtils.isEmpty(reason) && reason.contains(":")) {
            reason = reason.substring(0, reason.indexOf(":"));
        }

        FwLog.write(0, 1, LogTag.L_CRASH_IPC_TRB_F.getTag(), "stacks|reason|env", new Object[]{FwLog.stackToString(e), reason, CrashDetails.getIMCrashData(this.getApplicationContext(), e.toString())});
        this.defaultExceptionHandler.uncaughtException(t, e);
    }
}
