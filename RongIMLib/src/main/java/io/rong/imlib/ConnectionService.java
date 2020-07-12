//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.rong.common.fwlog.FwLog;
import io.rong.common.rlog.RLog;
import io.rong.imlib.NativeClient.ICodeListener;
import io.rong.imlib.NativeClient.IConnectResultCallback;
import io.rong.imlib.NativeObject.ConnectionEntry;
import io.rong.imlib.NativeObject.ConnectionStatusListener;
import io.rong.imlib.NativeObject.UserProfile;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.filetransfer.FileTransferClient;
import io.rong.imlib.filetransfer.FtConst.ServiceType;
import io.rong.imlib.navigation.NavigationCacheHelper;
import io.rong.imlib.navigation.NavigationClient;
import io.rong.imlib.navigation.NavigationObserver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionService {
    private static final String TAG = "ConnectionService";
    private static final int RECONNECT_INTERVAL = 1000;
    private static final int MAX_RETRY_COUNT = 9;
    private Handler mHandler;
    private Executor mCallBackExecutor;
    private Context mContext;
    private String mAppKey;
    private NativeObject mNativeObj;
    private boolean mEnableReconnectKick;
    private String mToken;
    private ConnectionService.ReconnectRunnable mReconnectRunnable;
    private AtomicInteger mRcRetryCount;
    private boolean mIsForeground;
    private ConnectionState mConnectionState;
    private ConnectionService.ConnectStatusListener mConnectListener;
    private IConnectResultCallback<String> mConnectCallback;

    private ConnectionService() {
        this.mContext = null;
        this.mEnableReconnectKick = false;
        this.mRcRetryCount = new AtomicInteger(0);
        HandlerThread workThread = new HandlerThread("IPC_RECONNECT_WORK");
        workThread.start();
        this.mHandler = new Handler(workThread.getLooper());
        this.mCallBackExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(@NonNull Runnable runnable) {
                Thread result = new Thread(runnable, "IPC_CONNECT_CALLBACK");
                return result;
            }
        });
        this.mConnectionState = new ConnectionState();
    }

    static ConnectionService getInstance() {
        return ConnectionService.SingleHolder.instance;
    }

    void initService(Context context, NativeObject obj, String akey) {
        this.mContext = context;
        this.mNativeObj = obj;
        this.mAppKey = akey;
        this.mConnectListener = new ConnectionService.ConnectStatusListener();
        this.mNativeObj.SetConnectionStatusListener(this.mConnectListener);
        CMPStrategy.getInstance().setEnvironment(this.mContext);
    }

    void setMainProgressConnectionStatusListener(ICodeListener listener) {
        this.mConnectionState.setConnectionStatusListener(listener);
    }

    void connect(final String token, final boolean isReconnect, final boolean inForeground, final IConnectResultCallback<String> callback) {
        this.resetReconnectCount();
        this.mHandler.post(new Runnable() {
            public void run() {
                ConnectionService.this.connectServer(token, isReconnect, inForeground, callback);
            }
        });
    }

    private void connectServer(final String token, final boolean isReconnect, final boolean inForeground, final IConnectResultCallback<String> callback) {
        if (isReconnect && !this.canReconnect()) {
            RLog.i("ConnectionService", "[connect] can't connect status " + this.mConnectionState.getCurrentStatus());
        } else if (TextUtils.isEmpty(token)) {
            RLog.e("ConnectionService", "connectServer token is null");
            if (callback != null) {
                callback.onError(ErrorCode.BIZ_ERROR_INVALID_PARAMETER.getValue());
            }

        } else {
            this.mConnectionState.connecting();
            this.stopRetry();
            NavigationClient.getInstance().addObserver(new NavigationObserver() {
                public void onSuccess(String userId) {
                    RLog.d("ConnectionService", "[connect] get cmp success");
                    boolean isPrivate = NavigationCacheHelper.getPrivateCloudConfig(ConnectionService.this.mContext);
                    FwLog.write(3, 1, "L-Env-S", "private", new Object[]{isPrivate});
                    ConnectionService.this.mNativeObj.SetEnvironment(isPrivate);
                    FileTransferClient.getInstance().setServiceType(isPrivate ? ServiceType.PRIVATE_CLOUD : ServiceType.QI_NIU);
                    ConnectionService.this.internalConnect(token, userId, isReconnect, inForeground, callback);
                }

                public void onError(String userId, int errorCode) {
                    RLog.e("ConnectionService", "[connect] get cmp error, errorCode = " + errorCode);
                    boolean isPrivate = NavigationCacheHelper.getPrivateCloudConfig(ConnectionService.this.mContext);
                    List<String> cmpList = CMPStrategy.getInstance().getCmpList();
                    if (cmpList != null && cmpList.size() != 0) {
                        FwLog.write(3, 1, "L-Env-S", "private:", new Object[]{isPrivate});
                        ConnectionService.this.mNativeObj.SetEnvironment(isPrivate);
                        ConnectionService.this.internalConnect(token, userId, isReconnect, inForeground, callback);
                    } else {
                        ConnectionService.this.disposeReconnectByErrorCode(errorCode);
                        callback.onError(errorCode);
                    }

                    if (errorCode == 30008) {
                        FwLog.write(1, 1, "P-rtcon-E", "code|method|nativeCode|sessionId|seq_id", new Object[]{errorCode, "navi", 0, 0, "0"});
                    }

                    FileTransferClient.getInstance().setServiceType(isPrivate ? ServiceType.PRIVATE_CLOUD : ServiceType.QI_NIU);
                }
            });
            NavigationClient.getInstance().getCMPServerString(this.mContext, this.mAppKey, token);
        }
    }

    private void internalConnect(String token, String userId, final boolean isReconnect, boolean inForeground, final IConnectResultCallback<String> callback) {
        FwLog.write(3, 1, isReconnect ? "L-reconnect-T" : "L-connect-T", "sequences", new Object[]{this.mRcRetryCount.get()});
        this.mToken = token;
        this.tryConnect(token, userId, isReconnect, this.mIsForeground, new IConnectResultCallback<String>() {
            public void onSuccess(String s) {
                FwLog.write(3, 1, isReconnect ? "L-reconnect-R" : "L-connect-R", "code|sequences", new Object[]{0, ConnectionService.this.mRcRetryCount.get()});
                if (callback != null) {
                    callback.onSuccess(s);
                }

            }

            public void onError(int code) {
                FwLog.write(1, 1, isReconnect ? "L-reconnect-R" : "L-connect-R", "code|network|sequences", new Object[]{code, DeviceUtils.getNetworkType(ConnectionService.this.mContext), ConnectionService.this.mRcRetryCount.get()});
                if (callback != null) {
                    callback.onError(code);
                }

            }

            public void OnDatabaseOpened(int code) {
                if (callback != null) {
                    callback.OnDatabaseOpened(code);
                }

            }
        });
    }

    private void tryConnect(String token, String userId, boolean isReconnect, boolean inForeground, IConnectResultCallback<String> callback) {
        this.mIsForeground = inForeground;
        this.mConnectCallback = callback;
        if (userId == null) {
            userId = "";
        }

        List<String> cmpList = CMPStrategy.getInstance().getCmpList();
        cmpList = this.getLegalCmpList(cmpList);
        if (cmpList != null && cmpList.size() > 0) {
            ConnectionEntry[] connectionEntries = this.getConnectionEntries(cmpList);
            NativeClient.getInstance().setCurrentUserId(userId);
            boolean mpOpened = NavigationClient.getInstance().isMPOpened(this.mContext);
            boolean usOpened = NavigationClient.getInstance().isUSOpened(this.mContext);
            boolean grOpened = NavigationClient.getInstance().isGROpened(this.mContext);
            boolean kvStorageOpened = NavigationCacheHelper.isKvStorageEnabled(this.mContext);
            String MCCMNC = "";

            try {
                TelephonyManager telephonyManager = (TelephonyManager)this.mContext.getSystemService("phone");
                if (telephonyManager != null) {
                    MCCMNC = telephonyManager.getNetworkOperator();
                }
            } catch (SecurityException var20) {
                RLog.e("ConnectionService", "tryConnect SecurityException", var20);
            }

            String netType = DeviceUtils.getNetworkType(this.mContext);
            RLog.d("ConnectionService", "[connect] device info: " + Build.MANUFACTURER + ", " + Build.MODEL + ", " + VERSION.SDK_INT + ", " + "4.0.0.1" + ", " + netType + ", " + MCCMNC);
            RLog.d("ConnectionService", "[connect] tryConnect::cmp:" + connectionEntryArrayToString(cmpList) + ", userId : " + userId);
            FwLog.write(3, 1, "P-connect-T", "strategy|cached|use", new Object[]{"parallel", connectionEntryArrayToString(cmpList), connectionEntryArrayToString(cmpList)});
            boolean enable = this.mEnableReconnectKick && isReconnect;
            int groupMessageLimit = 0;
            if (grOpened) {
                groupMessageLimit = NavigationClient.getInstance().getGroupMessageLimit(this.mContext);
            }

            String clientIp = NavigationCacheHelper.getClientIp(this.mContext);
            UserProfile userProfile = new UserProfile();
            userProfile.setIpv6Preferred(false);
            userProfile.setPublicService(mpOpened);
            userProfile.setPushSetting(usOpened);
            userProfile.setSdkReconnect(enable);
            userProfile.setGroupMessageLimit(groupMessageLimit);
            userProfile.setClientIp(clientIp);
            userProfile.setKvStorageOpened(kvStorageOpened);
            String shortToken = NavigationClient.getInstance().getTokenExceptNavi(token);
            int ret = this.mNativeObj.Connect(shortToken, connectionEntries, userId, userProfile);
            if (ret != 0 && this.mConnectListener != null) {
                this.mConnectListener.OnRmtpComplete(ret, "", -1, (short)0, "");
            }

        } else {
            CMPStrategy.getInstance().clearCache(this.mContext);
            this.mConnectListener.OnRmtpComplete(ErrorCode.RC_NODE_NOT_FOUND.getValue(), "", 0, (short)0, "");
        }
    }

    private void OnProtocolRmtpComplete(int status, String userId, int code, short duration, String logInfo) {
        RLog.d("ConnectionService", "[connect] operationComplete status:" + status + ", logInfo:" + logInfo);
        if (status == 0) {
            FwLog.write(3, 1, "P-connect-R", "status_code|user_id|native_code|duration|network", new Object[]{status, userId, code, duration, DeviceUtils.getNetworkType(this.mContext)});
            this.resetReconnectCount();
            this.stopRetry();
            this.mConnectionState.onEvent(status);
            NativeClient.getInstance().setCurrentUserId(userId);
            NavigationCacheHelper.saveUserId(this.mContext, userId);
            this.responseConnectSuccessBlock(userId);
            CMPStrategy.getInstance().onConnected();
        } else {
            FwLog.write(1, 1, "P-connect-R", "status_code|user_id|native_code|duration|network|bg", new Object[]{status, userId, code, duration, DeviceUtils.getNetworkType(this.mContext), this.mIsForeground ? "false" : "true"});
            this.disposeReconnectByErrorCode(status);
            if (!this.mConnectionState.getCurrentStatus().equals(ConnectionStatus.SUSPEND)) {
                this.responseConnectErrorBlock(status);
            }
        }

        if (!NavigationClient.getInstance().isNaviCacheValid(this.mContext, this.mAppKey, this.mToken) && !this.needClearNavi(status)) {
            NavigationClient.getInstance().requestNavi(this.mAppKey, this.mToken, false);
        }

    }

    private void OnProtocolRmtpDisconnected(int status, int errorCode, String logInfo) {
        FwLog.write(4, 1, "P-connect-S", "status_code|native_code", new Object[]{status, errorCode});
        RLog.i("ConnectionService", "ExceptionListener onError. errorCode: " + status);
        this.disposeReconnectByErrorCode(status);
    }

    private void OnProtocolTcpComplete(ConnectionEntry entry) {
        RLog.e("ConnectionService", "connectionCollection onComplete:" + entry.getHost() + " " + entry.getPort() + " " + entry.getError() + " " + entry.getDuration());
        int logLevel = 3;
        if (entry.getError() == 0) {
            String cmp = entry.getHost() + ":" + entry.getPort();
            CMPStrategy.getInstance().setMainCMP(cmp);
        } else if (entry.getError() == -1) {
            logLevel = 3;
        } else {
            logLevel = 1;
        }

        FwLog.write(logLevel, 1, "P-connect_entry-S", "code|cmp|duration", new Object[]{entry.getError(), entry.getHost() + ":" + entry.getPort(), entry.getDuration()});
    }

    private void OnProtocolDBOpened(int code) {
        this.responseDBOpenBlock(code);
    }

    private void OnProtocolPongReceived() {
        RLog.i("ConnectionService", "ConnectionStatusListener OnPongReceived.");
    }

    private void responseConnectSuccessBlock(String userId) {
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onSuccess(userId);
        }

    }

    private void responseConnectErrorBlock(int status) {
        if (this.mConnectCallback != null) {
            this.mConnectCallback.onError(status);
        }

    }

    private void responseDBOpenBlock(int code) {
        RLog.d("ConnectionService", "onDatabaseOpened.");
        if (this.mConnectCallback != null) {
            this.mConnectCallback.OnDatabaseOpened(code);
        }

    }

    private boolean disposeReconnectByErrorCode(int errorCode) {
        this.mConnectionState.onEvent(errorCode);
        boolean willReconnect = false;
        ConnectionStatus status = this.mConnectionState.getCurrentStatus();
        if (status.equals(ConnectionStatus.CONNECTED)) {
            this.stopRetry();
        } else if (status.equals(ConnectionStatus.SUSPEND)) {
            this.handleSuspend(errorCode);
            willReconnect = true;
        } else {
            RLog.d("ConnectionService", "disposeReconnectByErrorCode cannot reconnect : status = " + this.mConnectionState.getCurrentStatus());
            if (status.equals(ConnectionStatus.TIMEOUT)) {
                this.disconnect(false);
            }

            this.stopRetry();
        }

        return willReconnect;
    }

    private void handleSuspend(int errorCode) {
        if (errorCode != ErrorCode.RC_CONNECTION_RESET_BY_PEER.getValue() && errorCode != ErrorCode.RC_CONN_ACK_TIMEOUT.getValue()) {
            if (errorCode == ErrorCode.RC_PONG_RECV_FAIL.getValue() && this.isNetworkAvailable()) {
                CMPStrategy.getInstance().updateCmpList();
            } else if (this.needClearNavi(errorCode)) {
                CMPStrategy.getInstance().clearCache(this.mContext);
            }
        } else {
            CMPStrategy.getInstance().updateCmpList();
        }

        this.retry();
    }

    private boolean needClearNavi(int errorCode) {
        return errorCode == ErrorCode.RC_CONN_REDIRECTED.getValue() || errorCode == ErrorCode.BIZ_ERROR_INVALID_PARAMETER.getValue() || errorCode == ErrorCode.RC_CONN_REFUSED.getValue();
    }

    private void retry() {
        if (this.canReconnect()) {
            int interval;
            if (this.mRcRetryCount.get() == 0) {
                interval = 0;
            } else if (this.mRcRetryCount.get() > 0 && this.mRcRetryCount.get() <= 9) {
                interval = (int)(Math.pow(2.0D, (double)this.mRcRetryCount.get() - 3.0D) * 1000.0D);
            } else {
                interval = (int)(Math.pow(2.0D, 6.0D) * 1000.0D);
            }

            RLog.d("ConnectionService", "onStatusChange, Will reconnect after " + interval);
            FwLog.write(4, 1, "L-reconnect-S", "retry_after", new Object[]{interval});
            this.mReconnectRunnable = new ConnectionService.ReconnectRunnable();
            this.mHandler.postDelayed(this.mReconnectRunnable, (long)interval);
            this.incrementCount();
        }

    }

    private void incrementCount() {
        this.mRcRetryCount.incrementAndGet();
    }

    private void stopRetry() {
        if (this.mReconnectRunnable != null) {
            this.mHandler.removeCallbacks(this.mReconnectRunnable);
            this.mReconnectRunnable = null;
        }

    }

    private boolean canReconnect() {
        if (this.mConnectionState.isTerminate()) {
            RLog.e("ConnectionService", "globalConnectionState can not reconnect");
            this.mToken = null;
            this.stopRetry();
            return false;
        } else if (this.mConnectionState.getCurrentStatus().equals(ConnectionStatus.CONNECTED)) {
            RLog.i("ConnectionService", "already connected. ignore this connect event.");
            return false;
        } else if (this.mToken == null) {
            RLog.e("ConnectionService", "mToken is cleared for terminal reconnect reason");
            return false;
        } else if (!this.isNetworkAvailable()) {
            this.resetReconnectCount();
            this.mConnectionState.networkUnavailable();
            return false;
        } else {
            return true;
        }
    }

    void setReconnectKickEnable(boolean enable) {
        this.mEnableReconnectKick = enable;
    }

    void disconnect(boolean isReceivePush) {
        this.resetReconnectCount();
        this.stopRetry();
        if (this.mNativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            RLog.d("ConnectionService", "[connect] disconnect:" + isReceivePush);
            this.mConnectionState.signUp();
            this.mToken = null;
            this.mNativeObj.Disconnect(isReceivePush ? 2 : 4);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)this.mContext.getSystemService("connectivity");
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    void resetReconnectCount() {
        RLog.i("ConnectionService", "reset reconnectCount");
        this.mRcRetryCount.set(0);
    }

    private ConnectionEntry[] getConnectionEntries(List<String> list) {
        List<ConnectionEntry> result = new ArrayList();
        if (list != null && list.size() > 0) {
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
                String cmp = (String)var5.next();
                String[] elements = cmp.split(":");
                ConnectionEntry entry = new ConnectionEntry();
                entry.setHost(elements[0]);
                entry.setPort(Integer.parseInt(elements[1]));
                result.add(entry);
            }

            return (ConnectionEntry[])result.toArray(new ConnectionEntry[list.size()]);
        } else {
            return (ConnectionEntry[])result.toArray(new ConnectionEntry[0]);
        }
    }

    private List<String> getLegalCmpList(List<String> list) {
        List<String> result = new ArrayList();
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            String cmp = (String)var3.next();
            String str = this.getLegalCmp(cmp);
            if (!TextUtils.isEmpty(str)) {
                result.add(str);
            } else {
                FwLog.write(1, 1, "L-check_cmp-S", "cmp", new Object[]{cmp});
            }
        }

        return result;
    }

    private String getLegalCmp(String cmp) {
        String str = "";
        if (TextUtils.isEmpty(cmp)) {
            str = "";
        } else {
            if (!cmp.startsWith("http")) {
                cmp = "http://" + cmp;
            }

            try {
                URL url = new URL(cmp);
                if (url.getHost() != null && url.getPort() >= 0) {
                    str = url.getHost() + ":" + url.getPort();
                }
            } catch (MalformedURLException var4) {
                var4.printStackTrace();
            }
        }

        return str;
    }

    void setIpcConnectTimeOut() {
        this.mCallBackExecutor.execute(new Runnable() {
            public void run() {
                if (!ConnectionService.this.mConnectionState.getCurrentStatus().equals(ConnectionStatus.CONNECTED)) {
                    RLog.e("ConnectionService", "IM 重连超时，将不会继续连接");
                    ConnectionService.this.disposeReconnectByErrorCode(ErrorCode.RC_CONNECT_TIMEOUT.getValue());
                    ConnectionService.this.responseConnectErrorBlock(ErrorCode.RC_CONNECT_TIMEOUT.getValue());
                }

            }
        });
    }

    void initConnectStatus(int status) {
        this.mConnectionState.initConnectStatus(status);
    }

    static String connectionEntryArrayToString(List<String> cmpList) {
        if (cmpList != null && cmpList.size() != 0) {
            StringBuilder sb = new StringBuilder();
            Iterator var2 = cmpList.iterator();

            while(var2.hasNext()) {
                String cmp = (String)var2.next();
                sb.append(cmp);
                sb.append(",");
            }

            String str = sb.toString();
            return str.substring(0, str.length() - 1);
        } else {
            return null;
        }
    }

    private class ReconnectRunnable implements Runnable {
        ReconnectRunnable() {
            RLog.d("ConnectionService", "ReconnectRunnable");
        }

        public void run() {
            RLog.d("ConnectionService", "ReconnectRunnable, count = " + ConnectionService.this.mRcRetryCount.get());
            String token = ConnectionService.this.mToken;
            if (!TextUtils.isEmpty(token)) {
                ConnectionService.this.connectServer(token, true, true, ConnectionService.this.mConnectCallback);
            }

            ConnectionService.this.mReconnectRunnable = null;
        }
    }

    private class ConnectStatusListener implements ConnectionStatusListener {
        private ConnectStatusListener() {
        }

        public void OnTcpComplete(final ConnectionEntry entry) {
            ConnectionService.this.mCallBackExecutor.execute(new Runnable() {
                public void run() {
                    ConnectionService.this.OnProtocolTcpComplete(entry);
                }
            });
        }

        public void OnRmtpComplete(final int status, final String userId, final int code, final short duration, final String logInfo) {
            ConnectionService.this.mCallBackExecutor.execute(new Runnable() {
                public void run() {
                    ConnectionService.this.OnProtocolRmtpComplete(status, userId, code, duration, logInfo);
                }
            });
        }

        public void OnRmtpDisconnected(final int status, final int errorCode, final String logInfo) {
            ConnectionService.this.mCallBackExecutor.execute(new Runnable() {
                public void run() {
                    ConnectionService.this.OnProtocolRmtpDisconnected(status, errorCode, logInfo);
                }
            });
        }

        public void OnPongReceived() {
            ConnectionService.this.mCallBackExecutor.execute(new Runnable() {
                public void run() {
                    ConnectionService.this.OnProtocolPongReceived();
                }
            });
        }

        public void OnDatabaseOpened(final int code) {
            ConnectionService.this.mCallBackExecutor.execute(new Runnable() {
                public void run() {
                    ConnectionService.this.OnProtocolDBOpened(code);
                }
            });
        }
    }

    private static class SingleHolder {
        static ConnectionService instance = new ConnectionService();

        private SingleHolder() {
        }
    }
}
