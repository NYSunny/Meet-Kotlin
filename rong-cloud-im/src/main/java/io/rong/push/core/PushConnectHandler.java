//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.core;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.util.ArrayList;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushErrorCode;
import io.rong.push.core.PushClient.ConnectStatusCallback;
import io.rong.push.pushconfig.IResultCallback;

public class PushConnectHandler extends Handler {
    private final String TAG = PushConnectHandler.class.getSimpleName();
    private PushClient pushClient;
    private ArrayList<String> ipList;
    private Context context;
    private int index;
    private String appKey;
    private final String IP_SPLIT = ":";
    private IResultCallback<String> callback;

    public PushConnectHandler(Looper looper) {
        super(looper);
    }

    public void connect(Context context, PushClient pushClient, ArrayList<String> ipList, String appKey, IResultCallback<String> callback) {
        this.pushClient = pushClient;
        this.ipList = ipList;
        this.appKey = appKey;
        this.callback = callback;
        this.context = context;
        this.sendEmptyMessage(0);
    }

    public void handleMessage(Message msg) {
        this.index = msg.what;
        if (!TextUtils.isEmpty((CharSequence)this.ipList.get(this.index))) {
            String[] ipInfo = ((String)this.ipList.get(this.index)).split(":");
            String ip = ipInfo[0];
            String port = ipInfo[1];
            this.pushClient.connect(ip, Integer.parseInt(port), DeviceUtils.getDeviceId(this.context, this.appKey), new ConnectStatusCallback() {
                public void onConnected() {
                    PushConnectHandler.this.callback.onSuccess("");
                }

                public void onError() {
                    PushConnectHandler.this.pushClient.reset();
                    if (PushConnectHandler.this.index < PushConnectHandler.this.ipList.size() - 1) {
                        PushConnectHandler.this.sendEmptyMessage(PushConnectHandler.this.index + 1);
                    } else {
                        PushConnectHandler.this.callback.onError(PushErrorCode.IO_EXCEPTION);
                    }

                }
            });
        }

    }
}
