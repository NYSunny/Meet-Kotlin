//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.rongpush;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.ArrayList;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushErrorCode;
import io.rong.push.PushType;
import io.rong.push.common.RLog;
import io.rong.push.common.stateMachine.State;
import io.rong.push.common.stateMachine.StateMachine;
import io.rong.push.core.PushClient;
import io.rong.push.core.PushClient.ClientListener;
import io.rong.push.core.PushConnectHandler;
import io.rong.push.core.PushNaviClient;
import io.rong.push.core.PushProtocalStack.PublishMessage;
import io.rong.push.core.PushUtils;
import io.rong.push.notification.PushNotificationMessage;
import io.rong.push.pushconfig.IResultCallback;
import io.rong.push.pushconfig.PushNaviObserver;

public class PushConnectivityManager extends StateMachine {
    private static final String TAG = "PushConnectivityManager";
    private Context mContext;
    private PushClient pushClient;
    private String appKey;
    private static final int EVENT_CONNECT_TO_NAVI = 1;
    private static final int EVENT_NAVI_CONNECTED = 2;
    private static final int EVENT_CONNECT_TO_SERVER = 2;
    private static final int EVENT_CONNECTED = 3;
    private static final int EVENT_DISCONNECT = 4;
    private static final int EVENT_DISCONNECTED = 5;
    private static final int EVENT_HEART_BEAT = 6;
    private static final int EVENT_PING_FAILURE = 7;
    private static final int EVENT_PING_SUCCESS = 8;
    private static final int EVENT_USER_OPERATION = 9;
    private static final int EVENT_SEND_REGISTRATION_INFO = 10;
    private NetworkType networkType;
    private boolean initialized;
    private int ALARM_REQUEST_CODE;
    private int ALARM_PING_REQUEST_CODE;
    private PushNaviClient pushNaviClient;
    private ArrayList<String> serverIpList;
    private PushConnectHandler connectHandler;
    private DisconnectedState disconnectedState;
    private NaviConnectingState naviConnectingState;
    private ConnectingState connectingState;
    PingState pingState;
    private ConnectedState connectedState;

    public static PushConnectivityManager getInstance() {
        return Singleton.sInstance;
    }

    protected PushConnectivityManager() {
        super("PushConnectivityManager");
        this.networkType = NetworkType.NONE;
        this.initialized = false;
        this.ALARM_REQUEST_CODE = 101;
        this.ALARM_PING_REQUEST_CODE = 102;
        this.disconnectedState = new DisconnectedState();
        this.naviConnectingState = new NaviConnectingState();
        this.connectingState = new ConnectingState();
        this.pingState = new PingState();
        this.connectedState = new ConnectedState();
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public synchronized void init(final Context context, String deviceId, String appKey) {
        RLog.d("PushConnectivityManager", "init, initialized = " + this.initialized + ", deviceId = " + deviceId + ", appKey = " + appKey);
        this.mContext = context;
        this.initialized = true;
        this.appKey = appKey;
        this.pushNaviClient = new PushNaviClient();
        HandlerThread thread = new HandlerThread("connect");
        thread.start();
        this.connectHandler = new PushConnectHandler(thread.getLooper());
        this.pushClient = new PushClient(context, appKey, DeviceUtils.getPhoneInformation(context), new ClientListener() {
            public void onMessageArrived(PublishMessage msg) {
                if (msg != null && !TextUtils.isEmpty(msg.getDataAsString())) {
                    RLog.i("PushConnectivityManager", msg.getDataAsString());
                    PushNotificationMessage pushNotificationMessage = PushUtils.transformToPushMessage(msg.getDataAsString());
                    String packageName = PushUtils.getPackageName(msg.getDataAsString());
                    if (TextUtils.isEmpty(packageName)) {
                        RLog.e("PushConnectivityManager", "package name can't empty!");
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("io.rong.push.intent.MESSAGE_ARRIVED");
                        intent.setPackage(packageName);
                        intent.putExtra("pushType", PushType.RONG.getName());
                        intent.putExtra("message", pushNotificationMessage);
                        if (VERSION.SDK_INT >= 12) {
                            intent.setFlags(32);
                        }

                        context.sendBroadcast(intent);
                    }
                } else {
                    RLog.e("PushConnectivityManager", "sendNotification, msg = null");
                }
            }

            public void onPingSuccess() {
                RLog.d("PushConnectivityManager", "onPingSuccess");
                PushConnectivityManager.this.getHandler().sendEmptyMessage(8);
            }

            public void onDisConnected() {
                RLog.d("PushConnectivityManager", "onDisConnected");
                PushConnectivityManager.this.getHandler().sendEmptyMessage(5);
            }

            public void onPingFailure() {
                RLog.d("PushConnectivityManager", "onPingFailure");
                PushConnectivityManager.this.getHandler().sendEmptyMessage(7);
            }
        });
        this.addState(this.disconnectedState);
        this.addState(this.naviConnectingState, this.disconnectedState);
        this.addState(this.connectingState, this.disconnectedState);
        this.addState(this.connectedState, this.disconnectedState);
        this.addState(this.pingState, this.disconnectedState);
        this.setInitialState(this.disconnectedState);
        this.start();
    }

    public void setServerDomain(String domain) {
        RLog.i("PushConnectivityManager", "setServerDomain " + domain);
        if (TextUtils.isEmpty(domain)) {
            RLog.e("PushConnectivityManager", "server address can't be empty!!");
        } else if (this.pushNaviClient == null) {
            RLog.e("PushConnectivityManager", "should init first!");
        } else {
            this.pushNaviClient.setPushNaviUrl(domain);
        }
    }

    public void connect() {
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "connect does not init.");
        } else {
            this.getHandler().sendEmptyMessage(1);
        }
    }

    public void ping() {
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "ping: does not init.");
        } else {
            this.getHandler().sendEmptyMessage(6);
        }
    }

    public void onPingTimeout() {
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "onPingTimeout: does not init.");
        } else {
            this.getHandler().sendEmptyMessage(7);
        }
    }

    public void sendRegistrationIDToServer(String regInfo) {
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "sendRegistrationIDToServer: does not init.");
        } else {
            Message msg = new Message();
            msg.what = 10;
            msg.obj = regInfo;
            this.getHandler().sendMessage(msg);
        }
    }

    public void disconnect() {
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "disconnect does not init.");
        } else {
            this.cancelHeartbeat();
            this.getHandler().sendEmptyMessage(4);
        }
    }

    public void setNetworkType(NetworkType networkType) {
        this.networkType = networkType;
    }

    public NetworkType getNetworkType() {
        return this.networkType;
    }

    private void connectToNavi() {
        this.pushNaviClient.getPushServerIPs(this.mContext, this.appKey, true, new PushNaviObserver() {
            public void onSuccess(ArrayList<String> addressList) {
                PushConnectivityManager.this.serverIpList = addressList;
                PushConnectivityManager.this.getHandler().sendEmptyMessage(2);
            }

            public void onError(PushErrorCode errorCode) {
                RLog.e("PushConnectivityManager", "get navi onError.");
                PushConnectivityManager.this.getHandler().sendEmptyMessage(5);
            }
        });
    }

    private void connectToServer() {
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "connect does not init.");
        } else if (this.serverIpList != null && this.serverIpList.size() > 0) {
            this.connectHandler.connect(this.mContext, this.pushClient, this.serverIpList, this.appKey, new IResultCallback<String>() {
                public void onSuccess(String s) {
                    PushConnectivityManager.this.getHandler().sendEmptyMessage(3);
                }

                public void onError(PushErrorCode code) {
                    PushConnectivityManager.this.getHandler().sendEmptyMessage(5);
                }
            });
        } else {
            RLog.e("PushConnectivityManager", "server ip can't be null.");
            this.getHandler().sendEmptyMessage(5);
        }
    }

    public void startPingTimer() {
        RLog.i("PushConnectivityManager", "startPingTimer, 10s");
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "startPingTimer. does not init.");
        } else {
            long nextMillis = SystemClock.elapsedRealtime() + 10000L;
            this.startPushTimer("PING", "PING", this.ALARM_PING_REQUEST_CODE, nextMillis);
        }
    }

    public void stopPingTimer() {
        RLog.i("PushConnectivityManager", "stopPingTimer");
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "stopPingTimer. does not init.");
        } else {
            this.stopPushTimer("PING", "PING", this.ALARM_PING_REQUEST_CODE);
        }
    }

    @TargetApi(23)
    public void setNextHeartbeat() {
        RLog.i("PushConnectivityManager", "startHeartbeat");
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "setNextHeartbeat. does not init.");
        } else {
            long nextMillis;
            try {
                String heartbeat_timer = this.mContext.getResources().getString(this.mContext.getResources().getIdentifier("push_heartbeat_timer", "string", this.mContext.getPackageName()));
                nextMillis = SystemClock.elapsedRealtime() + Long.parseLong(heartbeat_timer);
            } catch (Exception var4) {
                RLog.d("PushConnectivityManager", "use default heartbeat timer.");
                nextMillis = SystemClock.elapsedRealtime() + 240000L;
            }

            this.startPushTimer((String)null, (String)null, this.ALARM_REQUEST_CODE, nextMillis);
        }
    }

    private void startPushTimer(String pushExtraName, String pushExtraValue, int requestCode, long nextMillis) {
        Intent intent = new Intent(this.mContext, PushReceiver.class);
        intent.setAction("io.rong.push.intent.action.HEART_BEAT");
        if (!TextUtils.isEmpty(pushExtraName) && !TextUtils.isEmpty(pushExtraValue)) {
            intent.putExtra(pushExtraName, pushExtraValue);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mContext, requestCode, intent, 134217728);
        AlarmManager alarmManager = (AlarmManager)this.mContext.getSystemService("alarm");
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            if (VERSION.SDK_INT >= 23) {
                try {
                    alarmManager.setExactAndAllowWhileIdle(2, nextMillis, pendingIntent);
                } catch (Exception var12) {
                    RLog.e("PushConnectivityManager", var12.getMessage());
                } catch (NoSuchMethodError var13) {
                    RLog.e("PushConnectivityManager", var13.getMessage());
                    alarmManager.setExact(2, nextMillis, pendingIntent);
                }
            } else if (VERSION.SDK_INT >= 19) {
                try {
                    alarmManager.setExact(2, nextMillis, pendingIntent);
                } catch (Exception var10) {
                    RLog.e("PushConnectivityManager", var10.getMessage());
                } catch (NoSuchMethodError var11) {
                    RLog.e("PushConnectivityManager", var11.getMessage());
                    alarmManager.set(2, nextMillis, pendingIntent);
                }
            } else {
                alarmManager.set(2, nextMillis, pendingIntent);
            }

        } else {
            io.rong.common.rlog.RLog.e("PushConnectivityManager", "alarmManager or pendingIntent is null");
        }
    }

    private void stopPushTimer(String pushExtraName, String pushExtraValue, int requestCode) {
        Intent intent = new Intent(this.mContext, PushReceiver.class);
        intent.setAction("io.rong.push.intent.action.HEART_BEAT");
        if (!TextUtils.isEmpty(pushExtraName) && !TextUtils.isEmpty(pushExtraValue)) {
            intent.putExtra(pushExtraName, pushExtraValue);
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.mContext, requestCode, intent, 134217728);
        AlarmManager mAlarmMng = (AlarmManager)this.mContext.getSystemService("alarm");
        if (pendingIntent != null) {
            mAlarmMng.cancel(pendingIntent);
        }

    }

    public void cancelHeartbeat() {
        RLog.i("PushConnectivityManager", "cancelHeartbeat");
        if (!this.isInitialized()) {
            RLog.e("PushConnectivityManager", "cancelHeartbeat. does not init.");
        } else {
            this.stopPushTimer((String)null, (String)null, this.ALARM_REQUEST_CODE);
            this.stopPingTimer();
        }
    }

    public void uninit() {
        if (this.pushClient != null) {
            this.pushClient.uninit();
        }

        this.cancelHeartbeat();
        this.initialized = false;
    }

    private class ConnectedState extends State {
        private ConnectedState() {
        }

        public void enter() {
            RLog.d("PushConnectivityManager", "enter " + this.getClass().getSimpleName());
        }

        public boolean processMessage(Message msg) {
            RLog.d("PushConnectivityManager", this.getClass().getSimpleName() + ": process msg = " + msg.what);
            switch(msg.what) {
                case 4:
                    PushConnectivityManager.this.pushClient.disconnect();
                    break;
                case 5:
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                    break;
                case 6:
                    PushConnectivityManager.this.pushClient.ping();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.pingState);
                    break;
                case 7:
                    PushConnectivityManager.this.stopPingTimer();
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                    PushConnectivityManager.this.getHandler().sendEmptyMessage(2);
            }

            return true;
        }
    }

    private class PingState extends State {
        private PingState() {
        }

        public void enter() {
            RLog.d("PushConnectivityManager", "enter " + this.getClass().getSimpleName());
            PushConnectivityManager.this.startPingTimer();
        }

        public boolean processMessage(Message msg) {
            RLog.d("PushConnectivityManager", this.getClass().getSimpleName() + ": process msg = " + msg.what);
            switch(msg.what) {
                case 2:
                    PushConnectivityManager.this.connectToServer();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.connectingState);
                    break;
                case 3:
                case 8:
                    PushConnectivityManager.this.stopPingTimer();
                    PushConnectivityManager.this.setNextHeartbeat();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.connectedState);
                    break;
                case 4:
                    PushConnectivityManager.this.pushClient.disconnect();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                    break;
                case 5:
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                case 10:
                    PushConnectivityManager.this.deferMessage(msg);
                    break;
                case 6:
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                    PushConnectivityManager.this.getHandler().sendEmptyMessage(2);
                    break;
                case 7:
                    PushConnectivityManager.this.stopPingTimer();
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.getHandler().sendEmptyMessage(2);
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                case 9:
            }

            return true;
        }
    }

    private class ConnectingState extends State {
        private ConnectingState() {
        }

        public void enter() {
            RLog.d("PushConnectivityManager", "enter " + this.getClass().getSimpleName());
        }

        public boolean processMessage(Message msg) {
            RLog.d("PushConnectivityManager", this.getClass().getSimpleName() + ": process msg = " + msg.what);
            switch(msg.what) {
                case 2:
                case 10:
                    PushConnectivityManager.this.deferMessage(msg);
                    break;
                case 3:
                    PushConnectivityManager.this.setNextHeartbeat();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.connectedState);
                    break;
                case 4:
                case 5:
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
                case 6:
                case 7:
                case 9:
                default:
                    break;
                case 8:
                    PushConnectivityManager.this.stopPingTimer();
            }

            return true;
        }
    }

    private class NaviConnectingState extends State {
        private NaviConnectingState() {
        }

        public void enter() {
            RLog.d("PushConnectivityManager", "enter " + this.getClass().getSimpleName());
        }

        public boolean processMessage(Message msg) {
            switch(msg.what) {
                case 2:
                    PushConnectivityManager.this.connectToServer();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.connectingState);
                    break;
                case 5:
                    PushConnectivityManager.this.pushClient.reset();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.disconnectedState);
            }

            return true;
        }
    }

    private class DisconnectedState extends State {
        private DisconnectedState() {
        }

        public void enter() {
            RLog.d("PushConnectivityManager", "enter " + this.getClass().getSimpleName());
        }

        public boolean processMessage(Message msg) {
            RLog.d("PushConnectivityManager", this.getClass().getSimpleName() + ": process msg = " + msg.what);
            switch(msg.what) {
                case 1:
                case 2:
                case 6:
                case 9:
                    PushConnectivityManager.this.connectToNavi();
                    PushConnectivityManager.this.transitionTo(PushConnectivityManager.this.naviConnectingState);
                case 3:
                case 4:
                case 7:
                default:
                    break;
                case 5:
                    PushConnectivityManager.this.pushClient.reset();
                    break;
                case 8:
                    PushConnectivityManager.this.stopPingTimer();
                    break;
                case 10:
                    PushConnectivityManager.this.deferMessage(msg);
            }

            return true;
        }
    }

    private static class Singleton {
        static PushConnectivityManager sInstance = new PushConnectivityManager();

        private Singleton() {
        }
    }

    public static enum NetworkType {
        NONE,
        WIFI,
        MOBILE,
        ERROR;

        private NetworkType() {
        }
    }
}
