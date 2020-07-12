//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.common.fwlog.FwLog;
import io.rong.imlib.IRongCallback.IRTCConfigCallback;
import io.rong.imlib.IRongCallback.IRTCDataCallback;
import io.rong.imlib.IRongCallback.IRTCJoinRoomCallback;
import io.rong.imlib.IRongCallback.IRTCJoinRoomCallbackEx;
import io.rong.imlib.IRongCallback.IRtcIODataCallback;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.RTCDataListener.Stub;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.ipc.IpcCallbackProxy;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.RTCStatusDate;
import io.rong.imlib.model.RTCUser;
import io.rong.imlib.model.SendMessageOption;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.typingmessage.TypingMessageManager;
import io.rong.imlib.typingmessage.TypingStatusMessage;
import io.rong.message.ReadReceiptMessage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IMLibRTCClient {
    private static final String TAG = "IMLibRTCClient";
    private Handler mWorkHandler;
    private IHandler mLibHandler;
    private static Handler mHandler;
    private static IMLibRTCClient.RTCRoomActionListener rtcRoomActionListener;
    private Map<String, IMLibRTCClient.RTCRoomCacheRunnable> mRetryRTCCache;
    private Map<String, IMLibRTCClient.RTCRoomCacheRunnable> mRTCRoomCache;

    private IMLibRTCClient() {
        this.mRetryRTCCache = new HashMap();
        this.mRTCRoomCache = new HashMap();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static IMLibRTCClient getInstance() {
        return IMLibRTCClient.SingletonHolder.sInstance;
    }

    public void init(Handler mWorkHandler) {
        this.mWorkHandler = mWorkHandler;
    }

    public void OnServiceConnected(IHandler mLibHandler) {
        this.mLibHandler = mLibHandler;
    }

    public void OnServiceDisconnected() {
        this.mLibHandler = null;
    }

    public static void setRtcRoomActionListener(IMLibRTCClient.RTCRoomActionListener roomActionListener) {
        rtcRoomActionListener = roomActionListener;
    }

    public String getVoIPInfo() {
        try {
            if (this.mLibHandler == null) {
                RLog.e("IMLibRTCClient", "IPC disconnected.");
            }

            return this.mLibHandler == null ? "" : this.mLibHandler.getVoIPCallInfo();
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public void reJoinRTCRoomWithCache() {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                int size = IMLibRTCClient.this.mRetryRTCCache.size();
                Iterator var2;
                IMLibRTCClient.RTCRoomCacheRunnable runnable;
                if (size > 0) {
                    RLog.d("IMLibRTCClient", "clear retry rtc-room cache after connectivity available, cached size = " + size);
                    var2 = IMLibRTCClient.this.mRetryRTCCache.values().iterator();

                    while(var2.hasNext()) {
                        runnable = (IMLibRTCClient.RTCRoomCacheRunnable)var2.next();
                        IMLibRTCClient.this.mWorkHandler.removeCallbacks(runnable);
                    }

                    IMLibRTCClient.this.mRetryRTCCache.clear();
                }

                size = IMLibRTCClient.this.mRTCRoomCache.size();
                if (size > 0) {
                    RLog.d("IMLibRTCClient", "rejoin rtc-room after connectivity available, cached size = " + size);
                    var2 = IMLibRTCClient.this.mRTCRoomCache.values().iterator();

                    while(var2.hasNext()) {
                        runnable = (IMLibRTCClient.RTCRoomCacheRunnable)var2.next();
                        IMLibRTCClient.this.mWorkHandler.post(runnable);
                    }
                }

            }
        });
    }

    public void exitRTCRoom(final String roomId, OperationCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RLog.d("IMLibRTCClient", "exitRTCRoom roomId = " + roomId);
                            IMLibRTCClient.this.mLibHandler.exitRTCRoom(roomId, IMLibRTCClient.this.new RTCExitRoomCallback(ipcCallbackProxy));
                            IMLibRTCClient.this.mRTCRoomCache.remove(roomId);
                            Runnable runnable = (Runnable)IMLibRTCClient.this.mRetryRTCCache.remove(roomId);
                            if (runnable != null) {
                                IMLibRTCClient.this.mWorkHandler.removeCallbacks(runnable);
                            }

                            if (IMLibRTCClient.rtcRoomActionListener != null) {
                                IMLibRTCClient.rtcRoomActionListener.onExited(roomId);
                            }
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public void getRTCUsers(final String roomId, final int order, IRTCDataCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<IRTCDataCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.getRTCUsers(roomId, order, new Stub() {
                                public void OnSuccess(final List data) {
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRTCDataCallback)ipcCallbackProxy.callback).onSuccess(data);
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }

                                public void OnError(final int status) {
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.valueOf(status));
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("IMLibRTCClient", var2.toString());
                            if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }
                                });
                            }
                        }

                    }
                }
            });
        }
    }

    public void getRTCUserData(final String roomId, final int order, IRTCDataCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "L-getRTCUserData-E", "code|desc", new Object[]{ErrorCode.PARAMETER_ERROR.getValue(), "RTC_Room_ID is empty."});
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            FwLog.write(3, 2, "L-getRTCUserData-T", "roomId", new Object[]{roomId});
            final IpcCallbackProxy<IRTCDataCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        FwLog.write(1, 2, "L-getRTCUserData-E", "roomId|code|desc", new Object[]{roomId, ErrorCode.IPC_DISCONNECT.getValue(), "LibHandler is Null"});
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.getRTCUserData(roomId, order, new Stub() {
                                public void OnSuccess(final List data) {
                                    FwLog.write(3, 2, "L-getRTCUserData-R", "roomId", new Object[]{roomId});
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRTCDataCallback)ipcCallbackProxy.callback).onSuccess(data);
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }

                                public void OnError(final int status) {
                                    FwLog.write(1, 2, "L-getRTCUserData-E", "roomId|code", new Object[]{roomId, status});
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.valueOf(status));
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            FwLog.write(1, 2, "L-getRTCUserData-E", "roomId|code|desc", new Object[]{roomId, ErrorCode.IPC_DISCONNECT.getValue(), var2.toString()});
                            if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }
                                });
                            }
                        }

                    }
                }
            });
        }
    }

    public void sendRTCPing(final String roomId, OperationCallback callback) {
        final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (IMLibRTCClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                        ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        IMLibRTCClient.this.mLibHandler.sendRTCPing(roomId, IMLibRTCClient.this.new RTCOperationCallback(ipcCallbackProxy));
                    } catch (RemoteException var2) {
                        var2.printStackTrace();
                    }

                }
            }
        });
    }

    public void setUseRTCOnly(final ResultCallback<Boolean> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (IMLibRTCClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onCallback(false);
                    }

                } else {
                    try {
                        boolean isUseRTCOnly = IMLibRTCClient.this.mLibHandler.useRTCOnly();
                        if (callback != null) {
                            callback.onCallback(isUseRTCOnly);
                        }
                    } catch (RemoteException var2) {
                        if (callback != null) {
                            callback.onCallback(false);
                        }

                        var2.printStackTrace();
                    }

                }
            }
        });
    }

    public void rtcPutInnerDatum(final String roomId, final int type, final String key, final String value, final String objectName, final String content, OperationCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcPutInnerData(roomId, type, key, value, objectName, content, IMLibRTCClient.this.new RTCOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public void rtcPutOuterDatum(final String roomId, final int type, final String key, final String value, final String objectName, final String content, OperationCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcPutOuterData(roomId, type, key, value, objectName, content, IMLibRTCClient.this.new RTCOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                            if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.UNKNOWN);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void rtcDeleteInnerData(final String roomId, final int type, final String[] keys, final String objectName, final String content, OperationCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcDeleteInnerData(roomId, type, keys == null ? new String[0] : keys, objectName, content, IMLibRTCClient.this.new RTCOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public void rtcDeleteOuterData(final String roomId, final int type, final String[] keys, final String objectName, final String content, OperationCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcDeleteOuterData(roomId, type, keys == null ? new String[0] : keys, objectName, content, IMLibRTCClient.this.new RTCOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public void rtcGetInnerData(final String roomId, final int type, final String[] keys, IRtcIODataCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<IRtcIODataCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((IRtcIODataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcGetInnerData(roomId, type, keys == null ? new String[0] : keys, new io.rong.imlib.IRtcIODataListener.Stub() {
                                public void OnSuccess(final Map data) throws RemoteException {
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRtcIODataCallback)ipcCallbackProxy.callback).onSuccess(data);
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }

                                public void OnError(final int status) throws RemoteException {
                                    RLog.d("IMLibRTCClient", "rtcGetInnerData errorCode =  " + status);
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRtcIODataCallback)ipcCallbackProxy.callback).onError(ErrorCode.valueOf(status));
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public void rtcGetOuterData(final String roomId, final int type, final String[] keys, IRtcIODataCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<IRtcIODataCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((IRtcIODataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcGetOuterData(roomId, type, keys == null ? new String[0] : keys, new io.rong.imlib.IRtcIODataListener.Stub() {
                                public void OnSuccess(final Map data) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRtcIODataCallback)ipcCallbackProxy.callback).onSuccess(data);
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }

                                public void OnError(final int status) throws RemoteException {
                                    RLog.d("IMLibRTCClient", "rtcGetOuterData errorCode =  " + status);
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IRtcIODataCallback)ipcCallbackProxy.callback).onError(ErrorCode.valueOf(status));
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    public void joinRTCRoomAndGetData(String roomId, final IRTCJoinRoomCallback callback) {
        this.joinRTCRoomAndGetData(roomId, 0, 1, new IRTCJoinRoomCallbackEx<String[]>() {
            public void onSuccess(List<RTCUser> rtcUsers, String[] exParams) {
                if (callback != null) {
                    callback.onSuccess(rtcUsers, exParams != null && exParams.length >= 1 ? exParams[0] : null);
                }

            }

            public void onError(ErrorCode errorCode) {
                if (callback != null) {
                    callback.onError(errorCode);
                }

            }
        });
    }

    public void joinRTCRoomAndGetData(final String roomId, final int roomType, final int broadcastType, IRTCJoinRoomCallbackEx<String[]> callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<IRTCJoinRoomCallbackEx<String[]>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((IRTCJoinRoomCallbackEx)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.joinRTCRoomAndGetData(roomId, roomType, broadcastType, IMLibRTCClient.this.new JoinRTCRoomCallback(ipcCallbackProxy, roomId, roomType, broadcastType));
                        } catch (RemoteException var2) {
                            RLog.e("IMLibRTCClient", var2.toString());
                            if (ipcCallbackProxy.callback != null) {
                                IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((IRTCJoinRoomCallbackEx)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }
                                });
                            }
                        }

                    }
                }
            });
        }
    }

    /** @deprecated */
    @Deprecated
    public void getRTCConfig(String model, String osVersion, long timestamp, IRTCConfigCallback callback) {
        RLog.w("IMLibRTCClient", "getRTCConfig has already Deprecated");
    }

    public void getRTCConfig(final String model, final String osVersion, final long timestamp, final String sdkVersion, final IRTCConfigCallback callback) {
        if (callback == null) {
            RLog.d("IMLibRTCClient", "getRTCConfig error =   callback is null");
        } else if (!TextUtils.isEmpty(model) && !TextUtils.isEmpty(osVersion)) {
            final IpcCallbackProxy<IRTCConfigCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            RLog.d("IMLibRTCClient", "getRTCConfig error =  ipcCallbackProxy or callback is null");
                            ((IRTCConfigCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.getRTCConfig(model, osVersion, timestamp, sdkVersion, new io.rong.imlib.IRTCConfigCallback.Stub() {
                                public void onSuccess(String config, long version) throws RemoteException {
                                    callback.onSuccess(config, version);
                                }

                                public void onError(int status) throws RemoteException {
                                    RLog.d("IMLibRTCClient", "getRTCConfig errorCode =  " + status);
                                    callback.onError(ErrorCode.valueOf(status));
                                }
                            });
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        } else {
            RLog.e("IMLibRTCClient", "getRTCConfig parameter error, roomid=|model=" + model + "|osVersion=" + osVersion);
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void clearRTCCache() {
        this.mRTCRoomCache.clear();
        Iterator var1 = this.mRetryRTCCache.values().iterator();

        while(var1.hasNext()) {
            IMLibRTCClient.RTCRoomCacheRunnable runnable = (IMLibRTCClient.RTCRoomCacheRunnable)var1.next();
            this.mWorkHandler.removeCallbacks(runnable);
        }

        this.mRetryRTCCache.clear();
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void getRTCToken(final String roomId, final int roomType, final int mediaType, ResultCallback<String> resultCallback) {
        FwLog.write(3, 2, "L-getRTCToken-T", "roomId|roomType|mediaType", new Object[]{roomId, roomType, mediaType});
        final IpcCallbackProxy<ResultCallback<String>> ipcCallbackProxy = new IpcCallbackProxy(resultCallback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                try {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        FwLog.write(1, 2, "L-getRTCToken-E", "code|desc", new Object[]{ErrorCode.IPC_DISCONNECT.getValue(), "IM LibHandler is Null"});
                        if (ipcCallbackProxy.callback != null) {
                            ((ResultCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                        return;
                    }

                    IMLibRTCClient.this.mLibHandler.getRTCToken(roomId, roomType, mediaType, new io.rong.imlib.IStringCallback.Stub() {
                        public void onComplete(String string) throws RemoteException {
                            FwLog.write(3, 2, "L-getRTCToken-R", "code|token", new Object[]{0, string});
                            if (ipcCallbackProxy.callback != null) {
                                ((ResultCallback)ipcCallbackProxy.callback).onCallback(string);
                                ipcCallbackProxy.callback = null;
                            }

                        }

                        public void onFailure(int errorCode) throws RemoteException {
                            FwLog.write(1, 2, "L-getRTCToken-E", "code", new Object[]{errorCode});
                            if (ipcCallbackProxy.callback != null) {
                                ((ResultCallback)ipcCallbackProxy.callback).onFail(ErrorCode.valueOf(errorCode));
                                ipcCallbackProxy.callback = null;
                            }

                        }
                    });
                } catch (RemoteException var2) {
                    RLog.e("IMLibRTCClient", "getRTCToken", var2);
                }

            }
        });
    }

    public void setRTCUserState(final String roomId, final String state, OperationCallback callback) {
        final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                try {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                        return;
                    }

                    IMLibRTCClient.this.mLibHandler.setRTCUserData(roomId, state, new io.rong.imlib.IOperationCallback.Stub() {
                        public void onComplete() throws RemoteException {
                            if (ipcCallbackProxy.callback != null) {
                                ((OperationCallback)ipcCallbackProxy.callback).onCallback();
                                ipcCallbackProxy.callback = null;
                            }

                        }

                        public void onFailure(int errorCode) throws RemoteException {
                            if (ipcCallbackProxy.callback != null) {
                                ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.valueOf(errorCode));
                                ipcCallbackProxy.callback = null;
                            }

                        }
                    });
                } catch (RemoteException var2) {
                    RLog.e("IMLibRTCClient", "setRTCUserState", var2);
                }

            }
        });
    }

    public void solveServerHosts(String host, final ResultCallback<List<String>> callback) {
        if (this.mLibHandler == null) {
            if (callback != null) {
                callback.onFail(ErrorCode.IPC_DISCONNECT);
            }

        } else {
            boolean enabled = false;

            try {
                enabled = this.mLibHandler.isDnsEnabled();
            } catch (RemoteException var6) {
                RLog.e("IMLibRTCClient", "isDnsEnabled", var6);
            }

            if (!enabled) {
                if (callback != null) {
                    callback.onFail(ErrorCode.RC_OPERATION_NOT_SUPPORT);
                }

            } else {
                try {
                    this.mLibHandler.solveServerHosts(host, new io.rong.imlib.ISolveServerHostsCallBack.Stub() {
                        public void onSuccess(List<String> hosts) throws RemoteException {
                            callback.onSuccess(hosts);
                        }

                        public void onFailed(int code) throws RemoteException {
                            callback.onFail(code);
                        }
                    });
                } catch (RemoteException var5) {
                    RLog.e("IMLibRTCClient", "sloveServerHosts", var5);
                }

            }
        }
    }

    public void setRTCUserData(final String id, final int type, final HashMap data, final String objectName, final String content, OperationCallback callback) {
        final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                try {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                        return;
                    }

                    IMLibRTCClient.this.mLibHandler.setRTCUserDatas(id, type, data, objectName, content, new io.rong.imlib.IOperationCallback.Stub() {
                        public void onComplete() throws RemoteException {
                            if (ipcCallbackProxy.callback != null) {
                                ((OperationCallback)ipcCallbackProxy.callback).onCallback();
                                ipcCallbackProxy.callback = null;
                            }

                        }

                        public void onFailure(int errorCode) throws RemoteException {
                            if (ipcCallbackProxy.callback != null) {
                                ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.valueOf(errorCode));
                                ipcCallbackProxy.callback = null;
                            }

                        }
                    });
                } catch (RemoteException var2) {
                    RLog.e("IMLibRTCClient", "setRTCUserState", var2);
                }

            }
        });
    }

    public void getRTCUserData(final String roomId, final List<String> userIds, IRTCDataCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<IRTCDataCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    try {
                        if (IMLibRTCClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                            }

                            return;
                        }

                        String[] array = new String[0];
                        if (userIds != null && !userIds.isEmpty()) {
                            array = (String[])userIds.toArray(new String[0]);
                        }

                        IMLibRTCClient.this.mLibHandler.getRTCUserDatas(roomId, array, new Stub() {
                            public void OnSuccess(final List data) {
                                if (ipcCallbackProxy.callback != null) {
                                    IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            ((IRTCDataCallback)ipcCallbackProxy.callback).onSuccess(data);
                                            ipcCallbackProxy.callback = null;
                                        }
                                    });
                                }

                            }

                            public void OnError(final int status) {
                                RLog.d("IMLibRTCClient", "getRTCUserData errorCode =  " + status);
                                if (ipcCallbackProxy.callback != null) {
                                    IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.valueOf(status));
                                            ipcCallbackProxy.callback = null;
                                        }
                                    });
                                }

                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("IMLibRTCClient", var2.toString());
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    ((IRTCDataCallback)ipcCallbackProxy.callback).onError(ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }
                            });
                        }
                    }

                }
            });
        }
    }

    public void sendRTCDirectMessage(String targetId, MessageContent content, final String[] userIds, final String pushContent, final String pushData, final SendMessageOption option, final boolean isFilterBlackList, ISendMessageCallback callback) {
        final Message message = Message.obtain(targetId, ConversationType.RTC_ROOM, content);
        ConversationType type = message.getConversationType();
        if (type != null && !TextUtils.isEmpty(targetId) && content != null) {
            MessageTag msgTag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                RLog.e("IMLibRTCClient", "sendDirectionalMessage 自定义消息没有加注解信息。");
                if (callback != null) {
                    callback.onError(message, ErrorCode.PARAMETER_ERROR);
                }

            } else {
                if (TypingMessageManager.getInstance().isShowMessageTyping() && !(content instanceof TypingStatusMessage) && !(content instanceof ReadReceiptMessage)) {
                    TypingMessageManager.getInstance().setTypingEnd(message.getConversationType(), message.getTargetId());
                }

                final IpcCallbackProxy<ISendMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (IMLibRTCClient.this.mLibHandler == null) {
                            IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                                        ((ISendMessageCallback)ipcCallbackProxy.callback).onError(message, ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } else {
                            try {
                                IMLibRTCClient.this.mLibHandler.sendRTCDirectionalMessage(message, pushContent, pushData, userIds, option, isFilterBlackList, new io.rong.imlib.ISendMessageCallback.Stub() {
                                    public void onAttached(final Message msg) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onAttached(msg);
                                                }
                                            });
                                        }

                                    }

                                    public void onSuccess(final Message msg) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onSuccess(msg);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onError(final Message msg, final int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            IMLibRTCClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onError(msg, ErrorCode.valueOf(errorCode));
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }
                                });
                            } catch (Exception var2) {
                                RLog.e("IMLibRTCClient", "sendDirectionalMessage exception : ", var2);
                            }

                        }
                    }
                });
            }
        } else {
            RLog.e("IMLibRTCClient", "sendDirectionalMessage : conversation type or targetId or content can't be null!");
            if (callback != null) {
                callback.onError(message, ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void rtcSetUserResource(final String roomId, final RTCStatusDate[] kv, final String objectName, final RTCStatusDate[] content, OperationCallback callback) {
        if (TextUtils.isEmpty(roomId)) {
            RLog.e("IMLibRTCClient", "RTC_Room_ID is empty.");
            if (callback != null) {
                callback.onError(ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (IMLibRTCClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy != null && ipcCallbackProxy.callback != null) {
                            ((OperationCallback)ipcCallbackProxy.callback).onFail(ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            IMLibRTCClient.this.mLibHandler.rtcSetUserResource(roomId, kv, objectName, content, IMLibRTCClient.this.new RTCOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            var2.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    private class DefaultOperationCallback extends io.rong.imlib.IOperationCallback.Stub {
        private IpcCallbackProxy<OperationCallback> ipcCallbackProxy;

        public DefaultOperationCallback(IpcCallbackProxy<OperationCallback> ipcCallbackProxy) {
            this.ipcCallbackProxy = ipcCallbackProxy;
        }

        public void onComplete() {
            if (this.ipcCallbackProxy != null && this.ipcCallbackProxy.callback != null) {
                ((OperationCallback)this.ipcCallbackProxy.callback).onCallback();
                this.ipcCallbackProxy.callback = null;
            }

        }

        public void onFailure(int errorCode) {
            if (this.ipcCallbackProxy != null && this.ipcCallbackProxy.callback != null) {
                ((OperationCallback)this.ipcCallbackProxy.callback).onFail(errorCode);
                this.ipcCallbackProxy.callback = null;
            }

        }
    }

    private class RTCExitRoomCallback extends IMLibRTCClient.DefaultOperationCallback {
        public RTCExitRoomCallback(IpcCallbackProxy<OperationCallback> ipcCallbackProxy) {
            super(ipcCallbackProxy);
        }

        public void onComplete() {
            RLog.d("IMLibRTCClient", "RTCExitRoomCallback onComplete ");
            super.onComplete();
        }

        public void onFailure(int errorCode) {
            RLog.d("IMLibRTCClient", "RTCExitRoomCallback errorCode =  " + errorCode);
            super.onFailure(errorCode);
        }
    }

    private class RTCOperationCallback extends IMLibRTCClient.DefaultOperationCallback {
        public RTCOperationCallback(IpcCallbackProxy<OperationCallback> ipcCallbackProxy) {
            super(ipcCallbackProxy);
        }

        public void onFailure(int errorCode) {
            RLog.d("IMLibRTCClient", "RTCOperationCallback errorCode =  " + errorCode);
            super.onFailure(errorCode);
        }
    }

    private class JoinRTCRoomCallback extends io.rong.imlib.IRTCJoinRoomCallback.Stub {
        private IpcCallbackProxy<IRTCJoinRoomCallbackEx<String[]>> ipcCallbackProxy;
        private String rtcRoomId;
        private int roomType;
        private int broadcastType;

        public JoinRTCRoomCallback(IpcCallbackProxy<IRTCJoinRoomCallbackEx<String[]>> ipcCallbackProxy, String rtcRoomId, int roomType, int broadcastType) {
            this.rtcRoomId = rtcRoomId;
            this.broadcastType = broadcastType;
            this.roomType = roomType;
            this.ipcCallbackProxy = ipcCallbackProxy;
            IMLibRTCClient.this.mRTCRoomCache.put(rtcRoomId, IMLibRTCClient.this.new RTCRoomCacheRunnable(rtcRoomId, roomType, broadcastType));
            RLog.d("IMLibRTCClient", this + "");
        }

        public String toString() {
            return "JoinRTCRoomCallback{rtcRoomId='" + this.rtcRoomId + '\'' + '}';
        }

        public void OnSuccess(final List data, final String token, final String sessionId) throws RemoteException {
            if (this.ipcCallbackProxy != null && this.ipcCallbackProxy.callback != null) {
                IMLibRTCClient.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ((IRTCJoinRoomCallbackEx)JoinRTCRoomCallback.this.ipcCallbackProxy.callback).onSuccess(data, new String[]{token, sessionId});
                        JoinRTCRoomCallback.this.ipcCallbackProxy.callback = null;
                    }
                });
            }

            if (IMLibRTCClient.rtcRoomActionListener != null) {
                IMLibRTCClient.rtcRoomActionListener.onJoined(this.rtcRoomId, data);
            }

        }

        public void OnError(final int errorCode) throws RemoteException {
            RLog.e("IMLibRTCClient", "join rtcRroom " + this.rtcRoomId + ", error: " + errorCode + "re-join after 2s");
            if (this.ipcCallbackProxy != null && this.ipcCallbackProxy.callback != null) {
                IMLibRTCClient.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ((IRTCJoinRoomCallbackEx)JoinRTCRoomCallback.this.ipcCallbackProxy.callback).onError(ErrorCode.valueOf(errorCode));
                        JoinRTCRoomCallback.this.ipcCallbackProxy.callback = null;
                    }
                });
            }

            IMLibRTCClient.RTCRoomCacheRunnable runnable = IMLibRTCClient.this.new RTCRoomCacheRunnable(this.rtcRoomId, this.roomType, this.broadcastType);
            IMLibRTCClient.this.mRetryRTCCache.put(this.rtcRoomId, runnable);
            long interval = 2000L;
            if (IMLibRTCClient.this.mWorkHandler != null) {
                IMLibRTCClient.this.mWorkHandler.postDelayed(runnable, interval);
            }

            if (IMLibRTCClient.rtcRoomActionListener != null) {
                IMLibRTCClient.rtcRoomActionListener.onError(this.rtcRoomId, ErrorCode.valueOf(errorCode));
            }

        }
    }

    private class RTCRoomCacheRunnable implements Runnable {
        String rtcRoomID;
        int roomType;
        int broadcastType;

        public RTCRoomCacheRunnable(String rtcRoomID, int roomType, int broadcastType) {
            this.rtcRoomID = rtcRoomID;
            this.roomType = roomType;
            this.broadcastType = broadcastType;
        }

        public void run() {
            ConnectionStatus mConnectionStatus = RongIMClient.getInstance().getCurrentConnectionStatus();
            RLog.d("IMLibRTCClient", "rejoin rtc_room " + this);
            IMLibRTCClient.this.mRetryRTCCache.remove(this.rtcRoomID);
            if (IMLibRTCClient.this.mLibHandler != null && ConnectionStatus.CONNECTED.equals(mConnectionStatus)) {
                IpcCallbackProxy<IRTCJoinRoomCallbackEx<String[]>> ipcCallbackProxy = new IpcCallbackProxy((Object)null);
                if (IMLibRTCClient.rtcRoomActionListener != null) {
                    IMLibRTCClient.rtcRoomActionListener.onJoining(this.rtcRoomID);
                }

                try {
                    IMLibRTCClient.this.mLibHandler.joinRTCRoomAndGetData(this.rtcRoomID, this.roomType, this.broadcastType, IMLibRTCClient.this.new JoinRTCRoomCallback(ipcCallbackProxy, this.rtcRoomID, this.roomType, this.broadcastType));
                } catch (RemoteException var4) {
                    var4.printStackTrace();
                    RLog.e("IMLibRTCClient", "rejoin rtc-room exception.");
                }
            } else {
                RLog.e("IMLibRTCClient", "rejoin rtc-room error : " + mConnectionStatus);
            }

        }

        public String toString() {
            return "RTCRoomCacheRunnable{rtcRoomID='" + this.rtcRoomID + '\'' + "roomType= " + '}';
        }
    }

    public interface RTCRoomActionListener {
        void onJoining(String var1);

        void onJoined(String var1, List<RTCUser> var2);

        void onExited(String var1);

        void onError(String var1, ErrorCode var2);
    }

    private static class SingletonHolder {
        static IMLibRTCClient sInstance = new IMLibRTCClient();

        private SingletonHolder() {
        }
    }
}
