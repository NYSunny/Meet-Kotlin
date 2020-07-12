//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import io.rong.common.WakeLockUtils;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.common.rlog.RLog;
import io.rong.common.rlog.RLog.IRlogOtherProgressCallback;
import io.rong.imlib.IHandler.Stub;
import io.rong.imlib.NativeClient.BlacklistStatus;
import io.rong.imlib.NativeClient.GetNotificationQuietHoursCallback;
import io.rong.imlib.NativeClient.ICodeListener;
import io.rong.imlib.NativeClient.IConnectResultCallback;
import io.rong.imlib.NativeClient.IResultCallback;
import io.rong.imlib.NativeClient.IResultCallbackEx;
import io.rong.imlib.NativeClient.IResultProgressCallback;
import io.rong.imlib.NativeClient.PushNotificationListener;
import io.rong.imlib.NativeObject.RTCConfigListener;
import io.rong.imlib.common.SavePathUtils;
import io.rong.imlib.filetransfer.CancelCallback;
import io.rong.imlib.filetransfer.FileTransferClient;
import io.rong.imlib.filetransfer.PauseCallback;
import io.rong.imlib.httpdns.HttpDnsCompletion;
import io.rong.imlib.httpdns.RongHttpDnsResult;
import io.rong.imlib.httpdns.RongHttpDns.CompletionHandler;
import io.rong.imlib.httpdns.RongHttpDnsResult.ResolveStatus;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.ConversationStatus;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.PublicServiceProfileList;
import io.rong.imlib.model.RCEncryptedSession;
import io.rong.imlib.model.RTCStatusDate;
import io.rong.imlib.model.RTCUser;
import io.rong.imlib.model.RemoteHistoryMsgOption;
import io.rong.imlib.model.RemoteModelWrap;
import io.rong.imlib.model.RongListWrap;
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.SendMessageOption;
import io.rong.imlib.model.UserData;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.navigation.NavigationCacheHelper;
import io.rong.imlib.navigation.NavigationClient.NaviUpdateListener;
import io.rong.imlib.statistics.CrashDetails;
import io.rong.rtlog.upload.RtLogUploadManager;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class LibHandlerStub extends Stub {
    private static final String TAG = "LibHandlerStub";
    private Context mContext;
    private String mCurrentUserId;
    private NativeClient mClient;

    public LibHandlerStub(Context context, String appKey, String deviceId) {
        this.mContext = context;
        this.mClient = NativeClient.getInstance();
        this.mClient.init(this.mContext, appKey, deviceId);
    }

    public String getCurrentUserId() {
        try {
            return this.mClient.getCurrentUserId();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return null;
        }
    }

    public void replenishPing(boolean inForeground) {
        try {
            if (inForeground) {
                this.mClient.startReplenishHeartbeat();
            } else {
                this.mClient.stopReplenishHeartbeat();
            }
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void sendPing() {
        try {
            this.mClient.ping(this.mContext);
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
        }

    }

    public void setUserPolicy(boolean enable) {
        try {
            NavigationCacheHelper.setUserPolicy(enable);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void initAppendixModule() {
        this.mClient.initAppendixModule();
    }

    public void connect(String token, boolean isReconnect, boolean inForeground, final IConnectStringCallback callback) {
        try {
            this.mClient.connect(token, isReconnect, inForeground, new IConnectResultCallback<String>() {
                public void OnDatabaseOpened(int code) {
                    if (callback != null) {
                        try {
                            callback.OnDatabaseOpened(code);
                        } catch (RemoteException var3) {
                            var3.printStackTrace();
                        }
                    }

                }

                public void onSuccess(String userId) {
                    if (callback != null) {
                        LibHandlerStub.this.mCurrentUserId = userId;
                        WakeLockUtils.startNextHeartbeat(LibHandlerStub.this.mContext);

                        try {
                            callback.onComplete(userId);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    WakeLockUtils.cancelHeartbeat(LibHandlerStub.this.mContext);
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var8) {
            this.handleRuntimeException(var8);
            NavigationCacheHelper.clearCache(this.mContext);

            try {
                callback.onFailure(-1);
            } catch (RemoteException var7) {
                this.handleRemoteException(var7);
            }
        }

    }

    public void setIpcConnectTimeOut() {
        this.mClient.setIpcConnectTimeOut();
    }

    public void initIpcConnectStatus(int status) {
        this.mClient.initIpcConnectStatus(status);
    }

    public void disconnect(boolean isReceivePush) {
        try {
            WakeLockUtils.cancelHeartbeat(this.mContext);
            this.mClient.disconnect(isReceivePush);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void registerMessageType(String className) {
        try {
            Class<? extends MessageContent> loader = (Class<? extends MessageContent>) Class.forName(className);
            this.mClient.registerMessageType(loader);
        } catch (Exception var5) {
            FwLog.write(1, 1, LogTag.L_REGTYPE_E.getTag(), "class_name", new Object[]{className});
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            var5.printStackTrace(printWriter);
            RLog.e("LibHandlerStub", "registerMessageType Exception :\n" + stringWriter.toString());
        }

    }

    public void registerMessageTypes(List<String> classNameList) throws RemoteException {
        try {
            if (classNameList == null || classNameList.size() == 0) {
                return;
            }

            List<Class<? extends MessageContent>> classList = new ArrayList();
            Iterator var7 = classNameList.iterator();

            while(var7.hasNext()) {
                String className = (String)var7.next();
                Class<? extends MessageContent> loader = (Class<? extends MessageContent>) Class.forName(className);
                classList.add(loader);
            }

            this.mClient.registerMessageType(classList);
        } catch (Exception var6) {
            StringBuilder classNamesBuilder = new StringBuilder();
            Iterator var4 = classNameList.iterator();

            while(var4.hasNext()) {
                String className = (String)var4.next();
                classNamesBuilder.append(className).append(",");
            }

            FwLog.write(1, 1, LogTag.L_REGTYPE_E.getTag(), "class_names", new Object[]{classNamesBuilder.toString()});
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            var6.printStackTrace(printWriter);
            RLog.e("LibHandlerStub", "registerMessageTypeList Exception :\n" + stringWriter.toString());
        }

    }

    public void setConnectionStatusListener(final IConnectionStatusListener callback) {
        try {
            this.mClient.setConnectionStatusListener(new ICodeListener() {
                public void onChanged(int status) {
                    RLog.d("LibHandlerStub", "[connect] onChanged status:" + status);
                    if (status != 33005 && status != 0) {
                        WakeLockUtils.cancelHeartbeat(LibHandlerStub.this.mContext);
                        LibHandlerStub.this.mClient.stopReplenishHeartbeat();
                    }

                    if (callback != null) {
                        try {
                            callback.onChanged(status);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public int getTotalUnreadCount() {
        try {
            return this.mClient.getTotalUnreadCount();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return -1;
        }
    }

    public int getUnreadCount(int[] types) {
        try {
            if (types != null && types.length != 0) {
                ConversationType[] conversationTypes = new ConversationType[types.length];

                for(int i = 0; i < types.length; ++i) {
                    conversationTypes[i] = ConversationType.setValue(types[i]);
                }

                return this.mClient.getUnreadCount(conversationTypes);
            } else {
                return 0;
            }
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return -1;
        }
    }

    public int getUnreadCountWithDND(int[] types, boolean withDND) {
        try {
            if (types != null && types.length != 0) {
                ConversationType[] conversationTypes = new ConversationType[types.length];

                for(int i = 0; i < types.length; ++i) {
                    conversationTypes[i] = ConversationType.setValue(types[i]);
                }

                return this.mClient.getUnreadCount(withDND, conversationTypes);
            } else {
                return 0;
            }
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
            return -1;
        }
    }

    public int getUnreadCountById(int type, String id) {
        try {
            ConversationType conversationType = ConversationType.setValue(type);
            return conversationType != null && id != null ? this.mClient.getUnreadCount(conversationType, id) : 0;
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return -1;
        }
    }

    public int getUnreadCountByConversation(Conversation[] conversations) {
        try {
            return this.mClient.getTotalUnreadCount(conversations);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return -1;
        }
    }

    public void setOnReceiveMessageListener(final OnReceiveMessageListener listener) {
        try {
            if (listener != null) {
                io.rong.imlib.NativeClient.OnReceiveMessageListener receiveMessageListener = new io.rong.imlib.NativeClient.OnReceiveMessageListener() {
                    public void onReceived(Message message, int left, boolean offline, boolean hasMsg, int cmdLeft) {
                        try {
                            listener.onReceived(message, left, offline, hasMsg, cmdLeft);
                        } catch (RemoteException var7) {
                            var7.printStackTrace();
                        }

                    }
                };
                this.mClient.setOnReceiveMessageListener(receiveMessageListener);
            }
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public Message insertMessage(Message message) {
        try {
            return this.mClient.insertMessage(message.getConversationType(), message.getTargetId(), message.getSenderUserId(), message.getContent(), message.getSentTime());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public Message insertSettingMessage(Message message) {
        try {
            return this.mClient.insertSettingMessage(message);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public Message getMessage(int messageId) {
        try {
            return this.mClient.getMessage(messageId);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public Message getMessageByUid(String uid) {
        try {
            return this.mClient.getMessageByUid(uid);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public void sendMessage(Message message, String pushContent, String pushData, final ISendMessageCallback callback) {
        try {
            this.mClient.sendMessage(message, pushContent, pushData, (String[])null, new io.rong.imlib.NativeClient.ISendMessageCallback<Message>() {
                public void onAttached(Message message) {
                    if (callback != null) {
                        try {
                            callback.onAttached(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(Message message) {
                    if (callback != null) {
                        try {
                            callback.onSuccess(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(Message message, int code) {
                    if (callback != null) {
                        try {
                            callback.onError(message, code);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void sendMessageOption(Message message, String pushContent, String pushData, SendMessageOption option, final ISendMessageCallback callback) throws RemoteException {
        try {
            this.mClient.sendMessageOption(message, pushContent, pushData, option, (String[])null, new io.rong.imlib.NativeClient.ISendMessageCallback<Message>() {
                public void onAttached(Message message) {
                    if (callback != null) {
                        try {
                            callback.onAttached(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(Message message) {
                    if (callback != null) {
                        try {
                            callback.onSuccess(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(Message message, int code) {
                    if (callback != null) {
                        try {
                            callback.onError(message, code);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var7) {
            this.handleRuntimeException(var7);
        }

    }

    public void sendDirectionalMessage(Message message, String pushContent, String pushData, String[] userIds, final ISendMessageCallback callback) {
        try {
            this.mClient.sendMessage(message, pushContent, pushData, userIds, new io.rong.imlib.NativeClient.ISendMessageCallback<Message>() {
                public void onAttached(Message message) {
                    if (callback != null) {
                        try {
                            callback.onAttached(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(Message message) {
                    if (callback != null) {
                        try {
                            callback.onSuccess(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(Message message, int code) {
                    if (callback != null) {
                        try {
                            callback.onError(message, code);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var7) {
            this.handleRuntimeException(var7);
        }

    }

    public void sendLocationMessage(Message message, String pushContent, String pushData, final ISendMessageCallback callback) {
        try {
            this.mClient.sendLocationMessage(message, pushContent, pushData, new io.rong.imlib.NativeClient.ISendMessageCallback<Message>() {
                public void onAttached(Message message) {
                    if (callback != null) {
                        try {
                            callback.onAttached(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(Message message) {
                    if (callback != null) {
                        try {
                            callback.onSuccess(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(Message message, int code) {
                    if (callback != null) {
                        try {
                            callback.onError(message, code);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public List<Message> getNewestMessages(Conversation conversation, int count) {
        try {
            return this.mClient.getLatestMessages(conversation.getConversationType(), conversation.getTargetId(), count);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return null;
        }
    }

    public List<Message> getOlderMessages(Conversation conversation, long flagId, int count) {
        try {
            return this.mClient.getHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), (int)flagId, count);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return null;
        }
    }

    public void getOlderMessagesOneWay(Conversation conversation, long flagId, int count, OnGetHistoryMessagesCallback callback) {
        try {
            List messages = this.mClient.getHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), (int)flagId, count);

            try {
                callback.onComplete(new RemoteModelWrap(RongListWrap.obtain(messages, Message.class)));
            } catch (RemoteException var8) {
                RLog.e("LibHandlerStub", "getOlderMessages", var8);
            }
        } catch (RuntimeException var9) {
            this.handleRuntimeException(var9);
        }

    }

    public void getChatroomHistoryMessages(String targetId, long recordTime, int count, int order, final IChatRoomHistoryMessageCallback callback) {
        try {
            this.mClient.getChatroomHistoryMessages(targetId, recordTime, count, order, new IResultCallbackEx<List<Message>, Long>() {
                public void onSuccess(List<Message> messages, Long aLong) {
                    if (callback != null) {
                        try {
                            if (messages != null && messages.size() != 0) {
                                callback.onComplete(new RemoteModelWrap(RongListWrap.obtain(messages, Message.class)), aLong);
                            } else {
                                callback.onComplete((RemoteModelWrap)null, aLong);
                            }
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var8) {
            this.handleRuntimeException(var8);
        }

    }

    public void getUserStatus(String userId, final IGetUserStatusCallback callback) {
        try {
            this.mClient.getUserStatus(userId, new IResultCallbackEx<String, Integer>() {
                public void onSuccess(String platformInfo, Integer status) {
                    if (callback != null) {
                        try {
                            callback.onComplete(platformInfo, status);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void subscribeStatus(List<String> users, final IIntegerCallback callback) {
        try {
            this.mClient.subscribeStatus(users, new IResultCallbackEx<Integer, Integer>() {
                public void onSuccess(Integer opStatus, Integer status) {
                    if (callback != null) {
                        try {
                            callback.onComplete(status);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void setSubscribeStatusListener(final ISubscribeUserStatusCallback callback) {
        try {
            this.mClient.setSubscribeStatusListener(new IResultCallbackEx<String, String>() {
                public void onSuccess(String objName, String content) {
                    if (callback != null) {
                        try {
                            callback.onStatusReceived(objName, content);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int code) {
                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void setPushSetting(int key, String value, final ISetPushSettingCallback callback) {
        try {
            this.mClient.setPushSetting(key, value, new IResultCallback<Long>() {
                public void onSuccess(Long aLong) {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    try {
                        callback.onFailure(code);
                    } catch (RemoteException var3) {
                        LibHandlerStub.this.handleRemoteException(var3);
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public String getPushSetting(int key) {
        try {
            return this.mClient.getPushSetting(key);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public void setUserStatus(int status, final ISetUserStatusCallback callback) {
        try {
            this.mClient.setUserStatus(status, new IResultCallback<Integer>() {
                public void onSuccess(Integer code) {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void getRemoteHistoryMessages(Conversation conversation, long dataTime, int count, final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.getRemoteHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), dataTime, count, new IResultCallback<List<Message>>() {
                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(List<Message> messages) {
                    if (callback != null) {
                        RemoteModelWrap result = null;

                        try {
                            if (messages != null && messages.size() != 0) {
                                result = new RemoteModelWrap(RongListWrap.obtain(messages, Message.class));
                                callback.onComplete(result);
                            } else {
                                callback.onComplete(result);
                            }
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var7) {
            this.handleRuntimeException(var7);
        }

    }

    public void getRemoteHistoryMessagesOption(Conversation conversation, RemoteHistoryMsgOption remoteHistoryMsgOption, final io.rong.imlib.IResultCallback callback) throws RemoteException {
        try {
            this.mClient.getRemoteHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), remoteHistoryMsgOption, new IResultCallback<List<Message>>() {
                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(List<Message> messages) {
                    if (callback != null) {
                        RemoteModelWrap result = null;

                        try {
                            if (messages != null && messages.size() != 0) {
                                result = new RemoteModelWrap(RongListWrap.obtain(messages, Message.class));
                                callback.onComplete(result);
                            } else {
                                callback.onComplete(result);
                            }
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public List<Message> getOlderMessagesByObjectName(Conversation conversation, String objectName, long flagId, int count, boolean flag) {
        List list = null;

        try {
            list = this.mClient.getHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), objectName, (int)flagId, count, flag);
        } catch (RuntimeException var9) {
            this.handleRuntimeException(var9);
        }

        return list != null && list.size() != 0 ? list : null;
    }

    public List<Message> getOlderMessagesByObjectNames(Conversation conversation, List<String> objectNames, long flagId, int count, boolean flag) {
        List list = null;

        try {
            list = this.mClient.getHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), objectNames, flagId, count, flag);
        } catch (RuntimeException var9) {
            this.handleRuntimeException(var9);
        }

        return list != null && list.size() != 0 ? list : null;
    }

    public boolean deleteMessage(int[] ids) {
        if (ids != null && ids.length != 0) {
            try {
                return this.mClient.deleteMessages(ids);
            } catch (RuntimeException var3) {
                this.handleRuntimeException(var3);
                return false;
            }
        } else {
            return false;
        }
    }

    public void deleteMessages(int conversationType, String targetId, Message[] messages, IOperationCallback callback) {
        try {
            ConversationType type = ConversationType.setValue(conversationType);
            if (type.equals(ConversationType.CHATROOM)) {
                RLog.e("LibHandlerStub", "this conversationType isn't supported!");
                return;
            }

            if (messages == null || messages.length == 0) {
                throw new IllegalArgumentException("messages 参数异常。");
            }

            this.mClient.deleteMessages(type, targetId, messages, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public boolean deleteConversationMessage(int conversationType, String targetId) {
        try {
            return this.mClient.deleteMessage(ConversationType.setValue(conversationType), targetId);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public boolean clearMessages(Conversation conversation) {
        try {
            return this.mClient.clearMessages(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return false;
        }
    }

    public void cleanRemoteHistoryMessages(Conversation conversation, long recordTime, IOperationCallback callback) {
        try {
            this.mClient.cleanRemoteHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), recordTime, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void cleanHistoryMessages(Conversation conversation, long recordTime, IOperationCallback callback) {
        try {
            this.mClient.cleanHistoryMessages(conversation.getConversationType(), conversation.getTargetId(), recordTime, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public boolean clearMessagesUnreadStatus(Conversation conversation) {
        try {
            return this.mClient.clearMessagesUnreadStatus(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return false;
        }
    }

    public boolean setMessageExtra(int messageId, String values) {
        try {
            return this.mClient.setMessageExtra(messageId, values);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public boolean setMessageReceivedStatus(int messageId, int status) {
        try {
            return this.mClient.setMessageReceivedStatus(messageId, new ReceivedStatus(status));
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public boolean setMessageSentStatus(int messageId, int status) {
        try {
            return this.mClient.setMessageSentStatus(messageId, SentStatus.setValue(status));
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public List<Conversation> getConversationList() {
        List list = null;

        try {
            list = this.mClient.getConversationList();
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

        return list != null && list.size() != 0 ? list : null;
    }

    public void getConversationListByBatch(int countPerBatch, IGetConversationListWithProcessCallback callback) throws RemoteException {
        if (callback != null) {
            try {
                List<Conversation> list = this.mClient.getConversationList();
                this.processConversationList(list, countPerBatch, callback);
            } catch (RuntimeException var4) {
                this.handleRuntimeException(var4);
                callback.onComplete();
            }

        }
    }

    public boolean updateConversationInfo(int type, String targetId, String title, String portrait) {
        try {
            return this.mClient.updateConversationInfo(ConversationType.setValue(type), targetId, title, portrait);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return false;
        }
    }

    public List<Conversation> getConversationListByType(int[] types) {
        List list = null;

        try {
            list = this.mClient.getConversationList(types);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

        return list != null && list.size() != 0 ? list : null;
    }

    public void getConversationListOfTypesByBatch(int[] types, int countPerBatch, IGetConversationListWithProcessCallback callback) throws RemoteException {
        if (callback != null) {
            try {
                List<Conversation> list = this.mClient.getConversationList(types);
                this.processConversationList(list, countPerBatch, callback);
            } catch (RuntimeException var5) {
                this.handleRuntimeException(var5);
                callback.onComplete();
            }

        }
    }

    public List<Conversation> getConversationListByPage(int[] types, long timeStamp, int count) {
        try {
            return this.mClient.getConversationListByPage(types, timeStamp, count);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return null;
        }
    }

    private void processConversationList(List<Conversation> conversationList, int processLimit, IGetConversationListWithProcessCallback callback) throws RemoteException {
        if (conversationList != null) {
            int processSize = 0;
            ArrayList<Conversation> processList = new ArrayList();
            Iterator var6 = conversationList.iterator();

            while(var6.hasNext()) {
                Conversation conversation = (Conversation)var6.next();
                processList.add(conversation);
                ++processSize;
                if (processSize >= processLimit) {
                    callback.onProcess(processList);
                    processSize = 0;
                    processList.clear();
                }
            }

            if (processSize > 0) {
                callback.onProcess(processList);
                processList.clear();
            }
        }

        callback.onComplete();
    }

    public List<Conversation> getBlockedConversationList(int[] types) {
        try {
            return this.mClient.getBlockedConversationList(types);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public Conversation getConversation(int type, String targetId) {
        try {
            return this.mClient.getConversation(ConversationType.setValue(type), targetId);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return null;
        }
    }

    public boolean removeConversation(int typeValue, String targetId) {
        try {
            ConversationType conversationType = ConversationType.setValue(typeValue);
            if (conversationType == null) {
                RLog.i("LibHandlerStub", "removeConversation the conversation type is null");
                return false;
            } else {
                return this.mClient.removeConversation(conversationType, targetId);
            }
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public boolean clearConversations(int[] types) {
        if (types != null && types.length != 0) {
            try {
                ConversationType[] conversationTypes = new ConversationType[types.length];

                for(int i = 0; i < types.length; ++i) {
                    conversationTypes[i] = ConversationType.setValue(types[i]);
                }

                return this.mClient.clearConversations(conversationTypes);
            } catch (RuntimeException var4) {
                this.handleRuntimeException(var4);
            } catch (IllegalAccessException var5) {
                RLog.e("LibHandlerStub", "clearConversations", var5);
            }

            return false;
        } else {
            return false;
        }
    }

    public boolean saveConversationDraft(Conversation conversation, String content) {
        try {
            RLog.i("LibHandlerStub", "saveConversationDraft " + content);
            return this.mClient.saveTextMessageDraft(conversation.getConversationType(), conversation.getTargetId(), content);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public String getConversationDraft(Conversation conversation) {
        try {
            return this.mClient.getTextMessageDraft(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public boolean cleanConversationDraft(Conversation conversation) {
        try {
            return this.mClient.clearTextMessageDraft(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return false;
        }
    }

    public void getConversationNotificationStatus(int type, String targetId, final ILongCallback callback) {
        try {
            this.mClient.getConversationNotificationStatus(ConversationType.setValue(type), targetId, new IResultCallback<Integer>() {
                public void onSuccess(Integer status) {
                    if (callback != null) {
                        try {
                            callback.onComplete((long)status);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void setConversationNotificationStatus(int type, String targetId, int status, final ILongCallback callback) {
        try {
            this.mClient.setConversationNotificationStatus(ConversationType.setValue(type), targetId, ConversationNotificationStatus.setValue(status), new IResultCallback<Integer>() {
                public void onSuccess(Integer status) {
                    if (callback != null) {
                        try {
                            callback.onComplete((long)status);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public boolean syncConversationNotificationStatus(int type, String targetId, int status) {
        try {
            return this.mClient.syncConversationNotificationStatus(ConversationType.setValue(type), targetId, ConversationNotificationStatus.setValue(status));
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
            return false;
        }
    }

    public boolean setConversationTopStatus(int typeValue, String targetId, boolean isTop, boolean needCreate) {
        try {
            ConversationType conversationType = ConversationType.setValue(typeValue);
            if (conversationType == null) {
                RLog.e("LibHandlerStub", "setConversationTopStatus ConversationType is null");
                return false;
            } else {
                return this.mClient.setConversationToTop(conversationType, targetId, isTop, needCreate);
            }
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return false;
        }
    }

    public int getConversationUnreadCount(Conversation conversation) {
        try {
            return this.mClient.getUnreadCount(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return -1;
        }
    }

    public void getDiscussion(String id, final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.getDiscussion(id, new IResultCallback<Discussion>() {
                public void onSuccess(Discussion discussion) {
                    if (callback != null) {
                        RemoteModelWrap result = new RemoteModelWrap(discussion);

                        try {
                            callback.onComplete(result);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void setDiscussionName(String id, String name, IOperationCallback callback) {
        try {
            this.mClient.setDiscussionName(id, name, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void createDiscussion(final String name, final List<String> userIds, final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.createDiscussion(name, userIds, new IResultCallback<String>() {
                public void onSuccess(String discussionId) {
                    if (callback != null) {
                        Discussion model = new Discussion(discussionId, name, LibHandlerStub.this.mCurrentUserId, true, userIds);
                        RemoteModelWrap result = new RemoteModelWrap(model);

                        try {
                            callback.onComplete(result);
                        } catch (RemoteException var5) {
                            LibHandlerStub.this.handleRemoteException(var5);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void searchPublicService(String keyWords, int businessType, int searchType, final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.searchPublicService(keyWords, businessType, searchType, new IResultCallback<PublicServiceProfileList>() {
                public void onSuccess(PublicServiceProfileList publicServiceInfoList) {
                    if (callback != null) {
                        RemoteModelWrap result = new RemoteModelWrap(publicServiceInfoList);

                        try {
                            callback.onComplete(result);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void subscribePublicService(String targetId, int categoryId, boolean subscribe, final IOperationCallback callback) {
        try {
            this.mClient.subscribePublicService(targetId, categoryId, subscribe, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void getPublicServiceProfile(String targetId, int conversationType, final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.getPublicServiceProfile(targetId, conversationType, new IResultCallback<PublicServiceProfile>() {
                public void onSuccess(PublicServiceProfile info) {
                    if (callback != null) {
                        RemoteModelWrap mModelWrap = null;
                        if (info != null) {
                            mModelWrap = new RemoteModelWrap(info);
                        }

                        try {
                            callback.onComplete(mModelWrap);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void getPublicServiceList(final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.getPublicServiceList(new IResultCallback<PublicServiceProfileList>() {
                public void onSuccess(PublicServiceProfileList list) {
                    if (callback != null) {
                        RemoteModelWrap mModelWrap = new RemoteModelWrap(list);

                        try {
                            callback.onComplete(mModelWrap);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void addMemberToDiscussion(String id, List<String> userIds, IOperationCallback callback) {
        try {
            this.mClient.addMemberToDiscussion(id, userIds, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void removeDiscussionMember(String id, String userId, IOperationCallback callback) {
        try {
            this.mClient.removeMemberFromDiscussion(id, userId, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void quitDiscussion(String id, IOperationCallback callback) {
        try {
            this.mClient.quitDiscussion(id, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void syncGroup(List<Group> groups, IOperationCallback callback) {
        try {
            this.mClient.syncGroup(groups, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void joinGroup(String id, String name, IOperationCallback callback) {
        try {
            this.mClient.joinGroup(id, name, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void quitGroup(String id, IOperationCallback callback) {
        try {
            this.mClient.quitGroup(id, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void getChatRoomInfo(String id, int count, int order, final io.rong.imlib.IResultCallback callback) {
        try {
            this.mClient.queryChatRoomInfo(id, count, order, new IResultCallback<ChatRoomInfo>() {
                public void onSuccess(ChatRoomInfo chatRoomInfo) {
                    if (callback != null) {
                        try {
                            RemoteModelWrap result = new RemoteModelWrap(chatRoomInfo);
                            callback.onComplete(result);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void joinChatRoom(final String id, int defMessageCount, final IOperationCallback callback) {
        try {
            FwLog.write(3, 1, LogTag.L_JOIN_CHATROOM_T.getTag(), "room_id|existed", new Object[]{id, false});
            this.mClient.joinChatRoom(id, defMessageCount, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    FwLog.write(3, 1, LogTag.L_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{0, id});
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int errorCode) {
                    FwLog.write(1, 1, LogTag.L_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{errorCode, id});
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void reJoinChatRoom(final String id, int defMessageCount, final IOperationCallback callback) {
        FwLog.write(3, 1, LogTag.L_REJOIN_CHATROOM_T.getTag(), "room_id", new Object[]{id});

        try {
            this.mClient.reJoinChatRoom(id, defMessageCount, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    FwLog.write(3, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{0, id});
                    RLog.d("LibHandlerStub", "reJoinChatRoom " + id);
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int errorCode) {
                    FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{errorCode, id});
                    RLog.e("LibHandlerStub", "reJoinChatRoom " + errorCode);
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void joinExistChatRoom(final String id, int defMessageCount, final IOperationCallback callback, boolean keepMsg) {
        FwLog.write(3, 1, LogTag.L_JOIN_CHATROOM_T.getTag(), "room_id|existed", new Object[]{id, true});

        try {
            this.mClient.joinExistChatRoom(id, defMessageCount, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    FwLog.write(3, 1, LogTag.L_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{0, id});
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int errorCode) {
                    FwLog.write(1, 1, LogTag.L_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{errorCode, id});
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            }, keepMsg);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void quitChatRoom(final String id, final IOperationCallback callback) {
        FwLog.write(3, 1, LogTag.L_QUIT_CHATROOM_T.getTag(), "room_id", new Object[]{id});

        try {
            this.mClient.quitChatRoom(id, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    FwLog.write(3, 1, LogTag.L_QUIT_CHATROOM_R.getTag(), "code|room_id", new Object[]{0, id});
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int errorCode) {
                    FwLog.write(1, 1, LogTag.L_QUIT_CHATROOM_R.getTag(), "code|room_id", new Object[]{errorCode, id});
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void setNotificationQuietHours(String startTime, int spanMinutes, final IOperationCallback callback) {
        try {
            this.mClient.setNotificationQuietHours(startTime, spanMinutes, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void removeNotificationQuietHours(final IOperationCallback callback) {
        try {
            this.mClient.removeNotificationQuietHours(new io.rong.imlib.NativeClient.OperationCallback() {
                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void getNotificationQuietHours(final IGetNotificationQuietHoursCallback callback) {
        try {
            this.mClient.getNotificationQuietHours(new GetNotificationQuietHoursCallback() {
                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onError(code);
                        } catch (RemoteException var3) {
                            var3.printStackTrace();
                        }
                    }

                }

                public void onSuccess(String start, int min) {
                    if (callback != null) {
                        try {
                            callback.onSuccess(start, min);
                        } catch (RemoteException var4) {
                            var4.printStackTrace();
                        }
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void uploadMedia(Message message, final IUploadCallback callback) {
        try {
            this.mClient.uploadMedia(message, new IResultProgressCallback<String>() {
                public void onProgress(int progress) {
                    if (callback != null) {
                        try {
                            callback.onProgress(progress);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(String uri) {
                    if (callback != null) {
                        try {
                            callback.onComplete(uri);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onCanceled(int messageId) {
                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void downloadMedia(Conversation conversation, int type, String imageUrl, final IDownloadMediaCallback callback) {
        try {
            this.mClient.downloadMedia(conversation.getConversationType(), conversation.getTargetId(), type, imageUrl, new IResultProgressCallback<String>() {
                public void onProgress(int progress) {
                    if (callback != null) {
                        try {
                            callback.onProgress(progress);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(String localMediaPath) {
                    if (callback != null) {
                        try {
                            callback.onComplete(localMediaPath);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onCanceled(int messageId) {
                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void downloadMediaMessage(Message message, final IDownloadMediaMessageCallback callback) {
        try {
            this.mClient.downloadMediaMessage(message, new io.rong.imlib.NativeClient.IDownloadMediaMessageCallback<Message>() {
                public void onSuccess(Message message) {
                    if (callback != null) {
                        try {
                            callback.onComplete(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onProgress(int progress) {
                    if (callback != null) {
                        try {
                            callback.onProgress(progress);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onCanceled() {
                    if (callback != null) {
                        try {
                            callback.onCanceled();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void downloadMediaFile(String uid, String fileUrl, String fileName, String path, final IDownloadMediaFileCallback callback) {
        try {
            this.mClient.downloadMediaFile(uid, fileUrl, fileName, path, new io.rong.imlib.NativeClient.IDownloadMediaFileCallback<Boolean>() {
                public void onFileNameChanged(String newFileName) {
                    if (callback != null) {
                        try {
                            callback.onFileNameChanged(newFileName);
                        } catch (RemoteException var3) {
                            var3.printStackTrace();
                        }

                    }
                }

                public void onSuccess(Boolean result) {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onProgress(int progress) {
                    if (callback != null) {
                        try {
                            callback.onProgress(progress);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onCanceled() {
                    if (callback != null) {
                        try {
                            callback.onCanceled();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var7) {
            this.handleRuntimeException(var7);
        }

    }

    public void cancelTransferMediaMessage(Message message, final IOperationCallback callback) {
        try {
            FileTransferClient.getInstance().cancel(message.getMessageId(), new CancelCallback() {
                public void onCanceled(Object tag) {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void cancelAllTransferMediaMessage(IOperationCallback callback) {
        try {
            FileTransferClient.getInstance().cancelAll();
            if (callback != null) {
                callback.onComplete();
            }
        } catch (Exception var5) {
            RLog.e("LibHandlerStub", "cancelAllTransferMediaMessage", var5);
            if (callback != null) {
                try {
                    callback.onFailure(-1);
                } catch (RemoteException var4) {
                    this.handleRemoteException(var4);
                }
            }
        }

    }

    public void pauseTransferMediaMessage(Message message, final IOperationCallback callback) throws RemoteException {
        FileTransferClient.getInstance().pause(message.getMessageId(), new PauseCallback() {
            public void onPaused(Object tag) {
                try {
                    callback.onComplete();
                } catch (RemoteException var3) {
                    var3.printStackTrace();
                }

            }

            public void onError(int code) {
                try {
                    callback.onFailure(code);
                } catch (RemoteException var3) {
                    var3.printStackTrace();
                }

            }
        });
    }

    public void pauseTransferMediaFile(String tag, final IOperationCallback callback) throws RemoteException {
        FileTransferClient.getInstance().pause(tag, new PauseCallback() {
            public void onPaused(Object tag) {
                try {
                    callback.onComplete();
                } catch (RemoteException var3) {
                    var3.printStackTrace();
                }

            }

            public void onError(int code) {
                try {
                    callback.onFailure(code);
                } catch (RemoteException var3) {
                    var3.printStackTrace();
                }

            }
        });
    }

    public boolean getFileDownloadingStatus(String uid) {
        return FileTransferClient.getInstance().getDownloadingFromMap(this.mContext, uid);
    }

    public boolean supportResumeBrokenTransfer(String url) {
        return FileTransferClient.checkSupportResumeTransfer(url);
    }

    public long getDeltaTime() {
        try {
            return this.mClient.getDeltaTime();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return -1L;
        }
    }

    public void setDiscussionInviteStatus(String targetId, int status, final IOperationCallback callback) {
        try {
            this.mClient.setDiscussionInviteStatus(targetId, status, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

    }

    public void recallMessage(String objectName, byte[] content, String pushContent, int messageId, String targetId, int conversationType, final IOperationCallback callback) {
        try {
            this.mClient.recallMessage(objectName, content, pushContent, messageId, targetId, conversationType, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var9) {
            this.handleRuntimeException(var9);
        }

    }

    public void addToBlacklist(String userId, final IOperationCallback callback) {
        try {
            this.mClient.addToBlacklist(userId, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void removeFromBlacklist(String userId, final IOperationCallback callback) {
        try {
            this.mClient.removeFromBlacklist(userId, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void getBlacklistStatus(String userId, final IIntegerCallback callback) {
        try {
            this.mClient.getBlacklistStatus(userId, new IResultCallback<BlacklistStatus>() {
                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(BlacklistStatus blacklistStatus) {
                    if (callback != null) {
                        try {
                            callback.onComplete(blacklistStatus.getValue());
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void getBlacklist(final IStringCallback callback) {
        try {
            this.mClient.getBlacklist(new IResultCallback<String>() {
                public void onSuccess(String userIds) {
                    if (callback != null) {
                        try {
                            callback.onComplete(userIds);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public String getTextMessageDraft(Conversation conversation) {
        try {
            return this.mClient.getTextMessageDraft(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return null;
        }
    }

    public boolean saveTextMessageDraft(Conversation conversation, String content) {
        try {
            return this.mClient.saveTextMessageDraft(conversation.getConversationType(), conversation.getTargetId(), content);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
            return false;
        }
    }

    public boolean clearTextMessageDraft(Conversation conversation) {
        try {
            return this.mClient.clearTextMessageDraft(conversation.getConversationType(), conversation.getTargetId());
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return false;
        }
    }

    public void setUserData(UserData userData, final IOperationCallback callback) {
        try {
            this.mClient.setUserData(userData, new io.rong.imlib.NativeClient.OperationCallback() {
                public void onSuccess() {
                    if (callback != null) {
                        try {
                            callback.onComplete();
                        } catch (RemoteException var2) {
                            LibHandlerStub.this.handleRemoteException(var2);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public boolean updateMessageReceiptStatus(String targetId, int categoryId, long timestamp) {
        try {
            return this.mClient.updateMessageReceiptStatus(targetId, categoryId, timestamp);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return false;
        }
    }

    public boolean clearUnreadByReceipt(int conversationType, String targetId, long timestamp) {
        try {
            return this.mClient.clearUnreadByReceipt(conversationType, targetId, timestamp);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return false;
        }
    }

    public long getSendTimeByMessageId(int messageId) {
        try {
            return this.mClient.getSendTimeByMessageId(messageId);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
            return -1L;
        }
    }

    public void getVoIPKey(int engineType, String channelName, String extra, final IStringCallback callback) {
        try {
            this.mClient.getVoIPKey(engineType, channelName, extra, new IResultCallback<String>() {
                public void onSuccess(String s) {
                    if (callback != null) {
                        try {
                            callback.onComplete(s);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public String getVoIPCallInfo() {
        try {
            return this.mClient.getVoIPCallInfo();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return null;
        }
    }

    public void setServerInfo(String naviServer, String fileServer) {
        try {
            this.mClient.setServerInfo(naviServer, fileServer);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void initHttpDns() {
        try {
            this.mClient.initHttpDns();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
        }

    }

    public long getNaviCachedTime() {
        try {
            return NavigationCacheHelper.getCachedTime();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return -1L;
        }
    }

    public boolean setMessageContent(int messageId, byte[] messageContent, String objectName) {
        try {
            return this.mClient.setMessageContent(messageId, messageContent, objectName);
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
            return false;
        }
    }

    public List<Message> getUnreadMentionedMessages(int conversationType, String targetId) {
        List list = null;

        try {
            list = this.mClient.getUnreadMentionedMessages(ConversationType.setValue(conversationType), targetId);
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
        }

        return list != null && list.size() != 0 ? list : null;
    }

    public void sendMediaMessage(Message message, String pushContent, String pushData, final ISendMediaMessageCallback sendMediaMessageCallback) {
        try {
            this.mClient.sendMediaMessage(message, pushContent, pushData, new io.rong.imlib.NativeClient.ISendMediaMessageCallback<Message>() {
                public void onAttached(Message message) {
                    if (sendMediaMessageCallback != null) {
                        try {
                            sendMediaMessageCallback.onAttached(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(Message message) {
                    if (sendMediaMessageCallback != null) {
                        try {
                            sendMediaMessageCallback.onSuccess(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onProgress(Message message, int progress) {
                    if (sendMediaMessageCallback != null) {
                        try {
                            sendMediaMessageCallback.onProgress(message, progress);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onError(Message message, int code) {
                    if (sendMediaMessageCallback != null) {
                        try {
                            sendMediaMessageCallback.onError(message, code);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }

                public void onCanceled(Message message) {
                    if (sendMediaMessageCallback != null) {
                        try {
                            sendMediaMessageCallback.onCanceled(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void sendDirectionalMediaMessage(Message message, String[] userIds, String pushContent, String pushData, final ISendMediaMessageCallback sendMediaMessageCallback) {
        try {
            this.mClient.sendMediaMessage(message, userIds, pushContent, pushData, new io.rong.imlib.NativeClient.ISendMediaMessageCallback<Message>() {
                public void onAttached(Message message) {
                    try {
                        if (sendMediaMessageCallback != null) {
                            sendMediaMessageCallback.onAttached(message);
                        }
                    } catch (RemoteException var3) {
                        LibHandlerStub.this.handleRemoteException(var3);
                    }

                }

                public void onSuccess(Message message) {
                    try {
                        if (sendMediaMessageCallback != null) {
                            sendMediaMessageCallback.onSuccess(message);
                        }
                    } catch (RemoteException var3) {
                        LibHandlerStub.this.handleRemoteException(var3);
                    }

                }

                public void onProgress(Message message, int progress) {
                    try {
                        if (sendMediaMessageCallback != null) {
                            sendMediaMessageCallback.onProgress(message, progress);
                        }
                    } catch (RemoteException var4) {
                        LibHandlerStub.this.handleRemoteException(var4);
                    }

                }

                public void onError(Message message, int code) {
                    try {
                        if (sendMediaMessageCallback != null) {
                            sendMediaMessageCallback.onError(message, code);
                        }
                    } catch (RemoteException var4) {
                        LibHandlerStub.this.handleRemoteException(var4);
                    }

                }

                public void onCanceled(Message message) {
                    try {
                        if (sendMediaMessageCallback != null) {
                            sendMediaMessageCallback.onCanceled(message);
                        }
                    } catch (RemoteException var3) {
                        LibHandlerStub.this.handleRemoteException(var3);
                    }

                }
            });
        } catch (RuntimeException var7) {
            this.handleRuntimeException(var7);
        }

    }

    public boolean updateReadReceiptRequestInfo(String msgUId, String info) {
        if (msgUId != null && info != null) {
            try {
                return this.mClient.updateReadReceiptRequestInfo(msgUId, info);
            } catch (RuntimeException var4) {
                this.handleRuntimeException(var4);
                return false;
            }
        } else {
            RLog.d("LibHandlerStub", "updateReadReceiptRequestInfo parameter error");
            return false;
        }
    }

    public void registerCmdMsgType(String objName) {
        try {
            this.mClient.registerCmdMsgType(objName);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void registerCmdMsgTypes(List<String> objNameList) throws RemoteException {
        try {
            this.mClient.registerCmdMsgType(objNameList);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void registerDeleteMessageType(List<String> objNames) {
        try {
            this.mClient.registerDeleteMessageType(objNames);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public List<Message> searchMessages(String targetId, int conversationType, String keyword, int count, long timestamp) {
        try {
            return this.mClient.searchMessages(targetId, ConversationType.setValue(conversationType), keyword, count, timestamp);
        } catch (RuntimeException var8) {
            this.handleRuntimeException(var8);
            return null;
        }
    }

    public List<Message> searchMessagesByUser(String targetId, int conversationType, String userId, int count, long timestamp) {
        try {
            return this.mClient.searchMessagesByUser(targetId, ConversationType.setValue(conversationType), userId, count, timestamp);
        } catch (RuntimeException var8) {
            this.handleRuntimeException(var8);
            return null;
        }
    }

    public List<SearchConversationResult> searchConversations(String keyword, int[] conversationTypes, String[] objName) {
        try {
            return this.mClient.searchConversations(keyword, conversationTypes, objName);
        } catch (RuntimeException var5) {
            this.handleRuntimeException(var5);
            return null;
        }
    }

    public List<Message> getMatchedMessages(String targetId, int conversationType, long timestamp, int before, int after) {
        try {
            return this.mClient.getMatchedMessages(targetId, ConversationType.setValue(conversationType), timestamp, before, after);
        } catch (RuntimeException var8) {
            this.handleRuntimeException(var8);
            return null;
        }
    }

    public void getVendorToken(final IStringCallback callback) {
        try {
            this.mClient.getVendorToken(new IResultCallback<String>() {
                public void onSuccess(String s) {
                    if (callback != null) {
                        try {
                            callback.onComplete(s);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void writeFwLog(int level, String type, String tag, String metaJson, long timestamp) {
        FwLog.write(level, type, tag, metaJson, timestamp);
    }

    public boolean getJoinMultiChatRoomEnable() {
        try {
            return NavigationCacheHelper.isJoinMChatroomEnabled(this.mContext);
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return false;
        }
    }

    public boolean isPhrasesEnabled() {
        return this.mClient.isPhrasesEnabled();
    }

    public boolean isDnsEnabled() throws RemoteException {
        return this.mClient.isDnsEnabled();
    }

    public String getOfflineMessageDuration() {
        try {
            return this.mClient.getOfflineMessageDuration();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return null;
        }
    }

    public void setOfflineMessageDuration(String duration, final ILongCallback callback) {
        try {
            this.mClient.setOfflineMessageDuration(duration, new IResultCallback<Long>() {
                public void onSuccess(Long version) {
                    if (callback != null) {
                        try {
                            callback.onComplete(version);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int errorCode) {
                    if (callback != null) {
                        try {
                            callback.onFailure(errorCode);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    public void switchAppKey(String appKey, String deviceId) {
        try {
            this.mClient.switchAppKey(appKey, deviceId);
        } catch (RuntimeException var4) {
            this.handleRuntimeException(var4);
        }

    }

    private void handleRuntimeException(RuntimeException e) {
        FwLog.write(1, 1, LogTag.L_CRASH_IPC_RTM_F.getTag(), "stacks|env", new Object[]{FwLog.stackToString(e), CrashDetails.getIMCrashData(this.mContext, e.toString())});
        throw e;
    }

    private void handleRemoteException(RemoteException e) {
        FwLog.write(1, 1, LogTag.L_CRASH_IPC_RMT_E.getTag(), "stacks|env", new Object[]{FwLog.stackToString(e), CrashDetails.getIMCrashData(this.mContext, e.toString())});
        e.printStackTrace();
    }

    public Message getTheFirstUnreadMessage(int conversationType, String targetId) {
        return this.mClient.getTheFirstUnreadMessage(conversationType, targetId);
    }

    public boolean setMessageReadTime(long messageId, long timestamp) {
        try {
            return this.mClient.setMessageReadTime(messageId, timestamp);
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
            return false;
        }
    }

    public boolean createEncryptedConversation(String targetId, RCEncryptedSession encryptedSession) {
        return this.mClient.createEncryptedConversation(targetId, encryptedSession);
    }

    public RCEncryptedSession getEncryptedConversation(String targetId) {
        return this.mClient.getEncryptedConversation(targetId);
    }

    public boolean setEncryptedConversation(String targetId, RCEncryptedSession chaInfo) {
        return this.mClient.setEncryptedConversation(targetId, chaInfo);
    }

    public boolean removeEncryptedConversation(String targetId) {
        return this.mClient.removeEncryptedConversation(targetId);
    }

    public boolean clearEncryptedConversations() {
        return this.mClient.clearEncryptedConversations();
    }

    public List<RCEncryptedSession> getAllEncryptedConversations() {
        return this.mClient.getAllEncryptedConversations();
    }

    public void setReconnectKickEnable(boolean enable) {
        this.mClient.setReconnectKickEnable(enable);
    }

    public int getVideoLimitTime() {
        return this.mClient.getVideoLimitTime();
    }

    public int getGIFLimitSize() {
        return this.mClient.getGIFLimitSize();
    }

    public void setUserProfileListener(final UserProfileSettingListener listener) {
        try {
            if (listener != null) {
                PushNotificationListener pushNotificationListener = new PushNotificationListener() {
                    public void OnPushNotificationChanged(long version) {
                        try {
                            listener.OnPushNotificationChanged(version);
                            LibHandlerStub.this.updateRTCProfile();
                        } catch (RemoteException var4) {
                            var4.printStackTrace();
                        }

                    }
                };
                this.mClient.SetPushNotificationListener(pushNotificationListener);
            }
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    private void updateRTCProfile() {
        String rtcProfileJson = this.getRTCProfile();
        if (TextUtils.isEmpty(rtcProfileJson)) {
            RLog.e("LibHandlerStub", "updateRTCProfile,rtcProfile is empty");
        } else {
            try {
                this.updateVoIPCallInfo(rtcProfileJson);
            } catch (RemoteException var3) {
                RLog.e("LibHandlerStub", var3.toString());
            }

        }
    }

    public void setConversationStatusListener(final ConversationStatusListener listener) {
        try {
            if (listener != null) {
                io.rong.imlib.NativeObject.ConversationStatusListener conversationStatusListener = new io.rong.imlib.NativeObject.ConversationStatusListener() {
                    public void OnStatusChanged(ConversationStatus[] conversationStatus) {
                        try {
                            listener.OnStatusChanged(conversationStatus);
                        } catch (RemoteException var3) {
                            RLog.e("LibHandlerStub", var3.toString());
                        }

                    }
                };
                this.mClient.setConversationStatusListener(conversationStatusListener);
            }
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void exitRTCRoom(String roomId, IOperationCallback callback) {
        try {
            this.mClient.exitRTCRoom(roomId, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var4) {
            RLog.e("LibHandlerStub", var4.toString());
        }

    }

    public void getRTCUsers(String roomId, int order, final RTCDataListener listener) {
        try {
            this.mClient.getRTCUsers(roomId, order, new IResultCallback<List<RTCUser>>() {
                public void onSuccess(List<RTCUser> rtcUsers) {
                    try {
                        listener.OnSuccess(rtcUsers);
                    } catch (Exception var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }

                public void onError(int code) {
                    try {
                        listener.OnError(code);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }
            });
        } catch (Exception var5) {
            var5.printStackTrace();
            RLog.e("LibHandlerStub", var5.toString());
        }

    }

    public void getRTCUserData(String roomId, int order, final RTCDataListener listener) {
        try {
            this.mClient.getRTCUserData(roomId, order, new IResultCallback<List<RTCUser>>() {
                public void onSuccess(List<RTCUser> rtcUsers) {
                    try {
                        listener.OnSuccess(rtcUsers);
                    } catch (Exception var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }

                public void onError(int code) {
                    try {
                        listener.OnError(code);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }
            });
        } catch (Exception var5) {
            var5.printStackTrace();
            RLog.e("LibHandlerStub", var5.toString());
        }

    }

    public void sendRTCPing(String roomId, IOperationCallback callback) {
        try {
            this.mClient.sendRTCPing(roomId, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var4) {
            RLog.e("LibHandlerStub", "SendRTCPing - " + var4.toString());
        }

    }

    public boolean useRTCOnly() {
        try {
            return this.mClient.useRTCOnly();
        } catch (Exception var2) {
            RLog.e("LibHandlerStub", "UseRTCOnly - " + var2.toString());
            return false;
        }
    }

    public void rtcPutInnerData(String roomId, int type, String key, String value, String objectName, String content, IOperationCallback callback) {
        try {
            this.mClient.rtcPutInnerData(roomId, type, key, value, objectName, content, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var9) {
            RLog.e("LibHandlerStub", "rtcPutInnerDatum - " + var9.toString());
        }

    }

    public void rtcPutOuterData(String roomId, int type, String key, String value, String objectName, String content, IOperationCallback callback) {
        try {
            this.mClient.rtcPutOuterData(roomId, type, key, value, objectName, content, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var9) {
            RLog.e("LibHandlerStub", "rtcPutOuterDatum - " + var9.toString());
        }

    }

    public void rtcDeleteInnerData(String roomId, int type, String[] keys, String objectName, String content, IOperationCallback callback) {
        try {
            this.mClient.rtcDeleteInnerData(roomId, type, keys, objectName, content, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var8) {
            RLog.e("LibHandlerStub", "rtcDeleteInnerData - " + var8.toString());
        }

    }

    public void rtcDeleteOuterData(String roomId, int type, String[] keys, String objectName, String content, IOperationCallback callback) {
        try {
            this.mClient.rtcDeleteOuterData(roomId, type, keys, objectName, content, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var8) {
            RLog.e("LibHandlerStub", "rtcDeleteOuterData - " + var8.toString());
        }

    }

    public void rtcGetInnerData(String roomId, int type, String[] keys, final IRtcIODataListener callback) {
        try {
            this.mClient.rtcGetInnerData(roomId, type, keys, new IResultCallback<Map<String, String>>() {
                public void onSuccess(Map<String, String> data) {
                    try {
                        callback.OnSuccess(data);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }

                public void onError(int code) {
                    try {
                        callback.OnError(code);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }
            });
        } catch (Exception var6) {
            RLog.e("LibHandlerStub", "rtcGetInnerData - " + var6.toString());
        }

    }

    public void rtcGetOuterData(String roomId, int type, String[] keys, final IRtcIODataListener callback) {
        try {
            this.mClient.rtcGetOuterData(roomId, type, keys, new IResultCallback<Map<String, String>>() {
                public void onSuccess(Map<String, String> data) {
                    try {
                        callback.OnSuccess(data);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }

                public void onError(int code) {
                    try {
                        callback.OnError(code);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }
            });
        } catch (Exception var6) {
            RLog.e("LibHandlerStub", "rtcGetOuterData - " + var6.toString());
        }

    }

    public void joinRTCRoomAndGetData(String roomId, int roomType, int broadcastType, final IRTCJoinRoomCallback listener) {
        try {
            this.mClient.joinRTCRoomAndGetData(roomId, roomType, broadcastType, new IResultCallbackEx<List<RTCUser>, String[]>() {
                public void onSuccess(List<RTCUser> rtcUsers, String[] tokenAndSessionId) {
                    try {
                        listener.OnSuccess(rtcUsers, tokenAndSessionId[0], tokenAndSessionId[1]);
                    } catch (Exception var4) {
                        var4.printStackTrace();
                        RLog.e("LibHandlerStub", var4.toString());
                    }

                }

                public void onError(int code) {
                    try {
                        listener.OnError(code);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }
            });
        } catch (Exception var6) {
            var6.printStackTrace();
            RLog.e("LibHandlerStub", var6.toString());
        }

    }

    public void getRTCConfig(String model, String osVersion, long timestamp, String sdkVersion, final IRTCConfigCallback callback) throws RemoteException {
        this.mClient.getRTCConfig(model, osVersion, timestamp, sdkVersion, new RTCConfigListener() {
            public void onSuccess(String config, long version) {
                try {
                    callback.onSuccess(config, version);
                } catch (RemoteException var5) {
                    var5.printStackTrace();
                    RLog.e("LibHandlerStub", var5.toString());
                }

            }

            public void onError(int status) {
                try {
                    callback.onError(status);
                } catch (RemoteException var3) {
                    var3.printStackTrace();
                    RLog.e("LibHandlerStub", var3.toString());
                }

            }
        });
    }

    public void getRTCToken(String roomId, int roomType, int mediaType, final IStringCallback callback) {
        try {
            this.mClient.getRTCToken(roomId, roomType, mediaType, new IResultCallback<String>() {
                public void onSuccess(String s) {
                    if (callback != null) {
                        try {
                            callback.onComplete(s);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(int code) {
                    if (callback != null) {
                        try {
                            callback.onFailure(code);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }
            });
        } catch (RuntimeException var6) {
            this.handleRuntimeException(var6);
        }

    }

    public void setRLogOtherProgressCallback(final IRLogOtherProgressCallback callback) throws RemoteException {
        try {
            RLog.setRlogOtherProgressCallBack(new IRlogOtherProgressCallback() {
                public void write(String log, int level) {
                    try {
                        callback.write(log, level);
                    } catch (RemoteException var4) {
                        var4.printStackTrace();
                    }

                }

                public void setLogLevel(int level) {
                    try {
                        callback.setLogLevel(level);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                    }

                }

                public void uploadRLog() {
                    try {
                        callback.uploadRLog();
                    } catch (RemoteException var2) {
                        var2.printStackTrace();
                    }

                }
            });
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void setSavePath(String savePath) {
        SavePathUtils.setSavePath(savePath);
    }

    public void setRTCUserData(String roomId, String state, IOperationCallback callback) throws RemoteException {
        try {
            this.mClient.setRTCUserState(roomId, state, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var5) {
            RLog.e("LibHandlerStub", "setRTCUserData - " + var5.toString());
        }

    }

    public void solveServerHosts(String server, final ISolveServerHostsCallBack callback) throws RemoteException {
        HttpDnsManager.getInstance().asyncSolveDnsIp(this.mContext, server, new CompletionHandler() {
            public void completionHandler(RongHttpDnsResult result) {
                if (result.getResolveStatus() == ResolveStatus.BDHttpDnsResolveOK) {
                    try {
                        if (callback != null) {
                            callback.onSuccess(result.getIpv4List());
                        }
                    } catch (RemoteException var3) {
                        RLog.e("LibHandlerStub", "solveServerHosts:" + var3);
                    }
                }

            }
        }, new HttpDnsCompletion(this.mContext) {
            protected void onSuccess(ArrayList<String> hosts) {
                try {
                    if (callback != null) {
                        callback.onSuccess(hosts);
                    }
                } catch (RemoteException var3) {
                    RLog.e("LibHandlerStub", "solveServerHosts:" + var3);
                }

            }

            protected void onFailed(int code) {
                try {
                    if (callback != null) {
                        callback.onFailed(code);
                    }
                } catch (RemoteException var3) {
                    RLog.e("LibHandlerStub", "solveServerHosts:" + var3);
                }

            }
        });
    }

    public void setRTCUserDatas(String roomId, int type, Map data, String objectName, String content, IOperationCallback callback) throws RemoteException {
        try {
            this.mClient.setRTCUserData(roomId, type, data, objectName, content, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var8) {
            RLog.e("LibHandlerStub", "setRTCUserDatas - " + var8.toString());
        }

    }

    public void getRTCUserDatas(String roomId, String[] userIds, final RTCDataListener listener) throws RemoteException {
        try {
            this.mClient.getRTCUserDatas(roomId, userIds, new IResultCallback<List<RTCUser>>() {
                public void onSuccess(List<RTCUser> rtcUsers) {
                    try {
                        listener.OnSuccess(rtcUsers);
                    } catch (Exception var3) {
                        io.rong.common.RLog.e("LibHandlerStub", "getRTCUserDatas onSuccess", var3);
                    }

                }

                public void onError(int code) {
                    try {
                        listener.OnError(code);
                    } catch (RemoteException var3) {
                        var3.printStackTrace();
                        RLog.e("LibHandlerStub", var3.toString());
                    }

                }
            });
        } catch (Exception var5) {
            RLog.e("LibHandlerStub", "getRTCUserDatas - " + var5.toString());
        }

    }

    public void sendRTCDirectionalMessage(Message message, String pushContent, String pushData, String[] userIds, SendMessageOption option, boolean isFilterBlackList, final ISendMessageCallback callback) throws RemoteException {
        try {
            this.mClient.sendMessageOption(message, pushContent, pushData, option, userIds, isFilterBlackList, new io.rong.imlib.NativeClient.ISendMessageCallback<Message>() {
                public void onAttached(Message message) {
                    if (callback != null) {
                        try {
                            callback.onAttached(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onSuccess(Message message) {
                    if (callback != null) {
                        try {
                            callback.onSuccess(message);
                        } catch (RemoteException var3) {
                            LibHandlerStub.this.handleRemoteException(var3);
                        }
                    }

                }

                public void onError(Message message, int code) {
                    if (callback != null) {
                        try {
                            callback.onError(message, code);
                        } catch (RemoteException var4) {
                            LibHandlerStub.this.handleRemoteException(var4);
                        }
                    }

                }
            });
        } catch (RuntimeException var9) {
            this.handleRuntimeException(var9);
        }

    }

    public void setChatRoomEntry(String key, String value, String chatRoomId, boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, IOperationCallback callback) throws RemoteException {
        try {
            this.mClient.setChatRoomEntry(key, value, chatRoomId, sendNotification, notificationExtra, autoDelete, isOverWrite, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var10) {
            RLog.e("LibHandlerStub", "setChatRoomEntry - " + var10.toString());
            this.handleRuntimeException(var10);
        }

    }

    public void getChatRoomEntry(String chatRoomId, String key, final IStringCallback callback) throws RemoteException {
        this.mClient.getChatRoomStatusByKey(chatRoomId, key, new IResultCallback<String>() {
            public void onSuccess(String value) {
                if (callback != null) {
                    try {
                        callback.onComplete(value);
                    } catch (RemoteException var3) {
                        RLog.e("LibHandlerStub", "getChatRoomEntry - " + var3.toString());
                        LibHandlerStub.this.handleRemoteException(var3);
                    }
                }

            }

            public void onError(int code) {
                if (callback != null) {
                    try {
                        callback.onFailure(code);
                    } catch (RemoteException var3) {
                        RLog.e("LibHandlerStub", "getChatRoomEntry - " + var3.toString());
                        LibHandlerStub.this.handleRemoteException(var3);
                    }
                }

            }
        });
    }

    public void getAllChatRoomEntries(String chatroomId, final IDataByBatchListener callback) throws RemoteException {
        this.mClient.getAllChatRoomStatus(chatroomId, new IResultCallback<HashMap<String, String>>() {
            public void onSuccess(HashMap<String, String> map) {
                if (callback != null) {
                    try {
                        HashMap<String, String> transferMap = new HashMap();
                        if (map != null && map.size() > 0) {
                            Iterator var3 = map.entrySet().iterator();

                            while(var3.hasNext()) {
                                Entry<String, String> entry = (Entry)var3.next();
                                transferMap.put(entry.getKey(), entry.getValue());
                                if (transferMap.size() % 50 == 0) {
                                    callback.onProgress(transferMap);
                                    transferMap.clear();
                                }
                            }

                            if (transferMap.size() > 0) {
                                callback.onProgress(transferMap);
                                transferMap.clear();
                            }
                        }

                        callback.onComplete();
                    } catch (RemoteException var5) {
                        RLog.e("LibHandlerStub", "getAllChatRoomEntries success ", var5);
                        LibHandlerStub.this.handleRemoteException(var5);
                    }
                }

            }

            public void onError(int code) {
                if (callback != null) {
                    try {
                        callback.onError(code);
                    } catch (RemoteException var3) {
                        RLog.e("LibHandlerStub", "getAllChatRoomEntries error ", var3);
                        LibHandlerStub.this.handleRemoteException(var3);
                    }
                }

            }
        });
    }

    public void deleteChatRoomEntry(String key, String value, String chatroomId, boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, IOperationCallback callback) throws RemoteException {
        try {
            this.mClient.deleteChatRoomEntry(key, value, chatroomId, sendNotification, notificationExtra, autoDelete, isOverWrite, new LibHandlerStub.OperationCallback(callback));
        } catch (RuntimeException var10) {
            RLog.e("LibHandlerStub", "deleteChatRoomEntry ", var10);
            this.handleRuntimeException(var10);
        }

    }

    public void setNaviContentUpdateListener(final INaviContentUpdateCallBack callback) throws RemoteException {
        this.mClient.setNaviContentUpdateListener(new NaviUpdateListener() {
            public void onNaviUpdate() {
                try {
                    callback.naviContentUpdate();
                } catch (RemoteException var2) {
                    LibHandlerStub.this.handleRemoteException(var2);
                }

            }
        });
    }

    public String getUploadLogConfigInfo() throws RemoteException {
        try {
            return this.mClient.getUploadConfigInfo();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return null;
        }
    }

    public String getOffLineLogServer() throws RemoteException {
        try {
            return this.mClient.getOffLineLogServer();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return null;
        }
    }

    public void notifyAppBackgroundChanged(boolean isInBackground) throws RemoteException {
        RtLogUploadManager.getInstance().setIsBackgroundMode(isInBackground);
    }

    public String getRTCProfile() {
        try {
            return this.mClient.getRTCProfile();
        } catch (RuntimeException var2) {
            this.handleRuntimeException(var2);
            return "";
        }
    }

    public void updateVoIPCallInfo(String rtcProfile) throws RemoteException {
        try {
            this.mClient.updateVoIPCallInfo(rtcProfile);
        } catch (RuntimeException var3) {
            this.handleRuntimeException(var3);
        }

    }

    public void rtcSetUserResource(String roomId, RTCStatusDate[] kv, String objectName, RTCStatusDate[] content, IOperationCallback callback) throws RemoteException {
        try {
            this.mClient.rtcSetUserResource(roomId, kv, objectName, content, new LibHandlerStub.OperationCallback(callback));
        } catch (Exception var7) {
            var7.printStackTrace();
            RLog.e("LibHandlerStub", "RTCSetUserResource - " + var7.toString());
        }

    }

    private class OperationCallback implements io.rong.imlib.NativeClient.OperationCallback {
        IOperationCallback callback;

        public OperationCallback(IOperationCallback callback) {
            this.callback = callback;
        }

        public void onSuccess() {
            if (this.callback != null) {
                try {
                    this.callback.onComplete();
                } catch (RemoteException var2) {
                    LibHandlerStub.this.handleRemoteException(var2);
                }
            }

        }

        public void onError(int code) {
            if (this.callback != null) {
                try {
                    this.callback.onFailure(code);
                } catch (RemoteException var3) {
                    LibHandlerStub.this.handleRemoteException(var3);
                }
            }

        }
    }
}
