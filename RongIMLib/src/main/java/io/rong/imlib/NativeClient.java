//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Build.VERSION;
import android.security.NetworkSecurityPolicy;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.rong.common.FileInfo;
import io.rong.common.FileUtils;
import io.rong.common.WakeLockUtils;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.LogSplitUtil;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.common.rlog.IRealTimeLogListener;
import io.rong.common.rlog.RLog;
import io.rong.imlib.NativeObject.AccountInfo;
import io.rong.imlib.NativeObject.AccountInfoListener;
import io.rong.imlib.NativeObject.BizAckListener;
import io.rong.imlib.NativeObject.ChatroomInfoListener;
import io.rong.imlib.NativeObject.ConversationStatusListener;
import io.rong.imlib.NativeObject.CreateDiscussionCallback;
import io.rong.imlib.NativeObject.DiscussionInfo;
import io.rong.imlib.NativeObject.DiscussionInfoListener;
import io.rong.imlib.NativeObject.GetSearchableWordListener;
import io.rong.imlib.NativeObject.HistoryMessageListener;
import io.rong.imlib.NativeObject.NativeLogInfoListener;
import io.rong.imlib.NativeObject.PublishAckListener;
import io.rong.imlib.NativeObject.PushSettingListener;
import io.rong.imlib.NativeObject.RTCConfigListener;
import io.rong.imlib.NativeObject.RTCDataListener;
import io.rong.imlib.NativeObject.RTCUserInfoListener;
import io.rong.imlib.NativeObject.ReceiveMessageListener;
import io.rong.imlib.NativeObject.SetBlacklistListener;
import io.rong.imlib.NativeObject.SetOfflineMessageDurationListener;
import io.rong.imlib.NativeObject.SetPushSettingListener;
import io.rong.imlib.NativeObject.StatusData;
import io.rong.imlib.NativeObject.StatusNotification;
import io.rong.imlib.NativeObject.StatusNotificationListener;
import io.rong.imlib.NativeObject.TokenListener;
import io.rong.imlib.NativeObject.UserInfo;
import io.rong.imlib.NativeObject.UserStatusListener;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.common.NetUtils;
import io.rong.imlib.common.SavePathUtils;
import io.rong.imlib.filetransfer.Configuration;
import io.rong.imlib.filetransfer.FileTransferClient;
import io.rong.imlib.filetransfer.FtUtilities;
import io.rong.imlib.filetransfer.RequestCallBack;
import io.rong.imlib.filetransfer.RequestOption;
import io.rong.imlib.filetransfer.Configuration.Builder;
import io.rong.imlib.filetransfer.FtConst.MimeType;
import io.rong.imlib.filetransfer.FtConst.ServiceType;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;
import io.rong.imlib.model.Conversation;
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
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.SendMessageOption;
import io.rong.imlib.model.UnknownMessage;
import io.rong.imlib.model.UserData;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.navigation.NavigationCacheHelper;
import io.rong.imlib.navigation.NavigationClient;
import io.rong.imlib.navigation.NavigationClient.NaviUpdateListener;
import io.rong.message.ChatRoomKVNotiMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.FileMessage;
import io.rong.message.GIFMessage;
import io.rong.message.HQVoiceMessage;
import io.rong.message.IHandleMessageListener;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.LogCmdMessage;
import io.rong.message.MediaMessageContent;
import io.rong.message.MessageHandler;
import io.rong.message.RCEncryptedMessage;
import io.rong.message.SightMessage;
import io.rong.message.TextMessage;
import io.rong.message.utils.RCDHCodecTool;
import io.rong.message.utils.RCDHCodecTool.RCSecretKey;
import io.rong.rtlog.upload.RtLogUploadManager;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class NativeClient {
    private static final String TAG = "NativeClient";
    private Context mContext;
    private NativeObject nativeObj;
    private Handler mWorkHandler;
    private String mFileServer;
    private String mNaviServer;
    private String appKey;
    private String deviceId;
    private String dbPath;
    private String curUserId;
    private HashMap<String, Constructor<? extends MessageContent>> messageContentConstructorMap;
    private HashMap<String, MessageHandler> messageHandlerMap;
    private Set<String> mCmdObjectNameSet;
    private ConcurrentHashMap<String, Boolean> chatRoomStatusMap;
    private ScheduledThreadPoolExecutor executorService;
    private NaviUpdateListener naviUpdateListener;
    private Executor receiveMessageExecutor;
    private Timer timer;

    private MessageContent renderMessageContent(String objectName, byte[] content, Message message) {
        MessageContent result = this.newMessageContent(objectName, content);
        if (result instanceof UnknownMessage) {
            return result;
        } else {
            MessageHandler messageHandler = this.getMessageHandler(objectName);
            if (messageHandler != null) {
                messageHandler.decodeMessage(message, result);
            } else {
                RLog.e("NativeClient", "renderMessageContent 该消息未注册，请调用registerMessageType方法注册。");
            }

            return result;
        }
    }

    private NativeClient() {
        this.mContext = null;
        ConnectionService.getInstance();
    }

    public static NativeClient getInstance() {
        return NativeClient.NativeClientHolder.client;
    }

    public void init(Context context, String appKey, String deviceId) {
        this.mContext = context.getApplicationContext();
        this.appKey = appKey;
        this.deviceId = deviceId;
        this.nativeObj = new NativeObject(context);
        this.messageHandlerMap = new HashMap();
        this.messageContentConstructorMap = new HashMap();
        this.mCmdObjectNameSet = new TreeSet();
        this.chatRoomStatusMap = new ConcurrentHashMap();
        HandlerThread workThread = new HandlerThread("PING_WORK");
        workThread.start();
        this.mWorkHandler = new Handler(workThread.getLooper());
        this.initThreadPool();
        if (!SavePathUtils.isSavePathEmpty()) {
            this.dbPath = SavePathUtils.getSavePath();
        } else {
            File dbFile = this.mContext.getFilesDir();
            if (dbFile != null && dbFile.exists()) {
                this.dbPath = dbFile.getPath();
            } else {
                if (dbFile == null) {
                    dbFile = this.mContext.getDir("rcdb", 0);
                } else {
                    boolean successMkdir = dbFile.mkdirs();
                    if (!successMkdir) {
                        io.rong.common.RLog.e("NativeClient", "Created folders UnSuccessfully");
                    }
                }

                this.dbPath = dbFile.getPath();
            }
        }

        int result = this.nativeObj.InitClient(appKey, context.getPackageName(), deviceId, this.dbPath, "4.0.0.1");
        ConnectionService.getInstance().initService(this.mContext, this.nativeObj, appKey);
        this.nativeObj.SetRealTimeLogListener(new IRealTimeLogListener() {
            public void OnLogUpload(String data) {
                try {
                    JsonElement jsonElement = (new JsonParser()).parse(data);
                    JsonObject JsonObject = jsonElement.getAsJsonObject();
                    Set<Entry<String, JsonElement>> entries = JsonObject.entrySet();
                    Iterator var5 = entries.iterator();

                    while(var5.hasNext()) {
                        Entry<String, JsonElement> entry = (Entry)var5.next();
                        String key = (String)entry.getKey();
                        if (key.equals("level")) {
                            int level = ((JsonElement)entry.getValue()).getAsInt();
                            if (level >= 0 && level <= 6) {
                                RLog.setLogLevel(level, false);
                            }
                        } else if (key.equals("upload")) {
                            RLog.uploadRLog(false);
                        }
                    }
                } catch (Exception var9) {
                    RLog.e("OnLogUpload", "json error:" + data, var9);
                }

            }
        });
        ApplicationInfo info = context.getApplicationInfo();
        boolean isDebugMode = info != null && (info.flags & 2) != 0;
        this.setLogStatus(isDebugMode ? 2 : 3, new NativeLogInfoListener() {
            public void OnLogInfo(String log, boolean upload) {
                FwLog.onProtocolLog(log, upload);
            }
        });
        NavigationClient.getInstance().setNaviUpdateListener(new NaviUpdateListener() {
            public void onNaviUpdate() {
                RtLogUploadManager.getInstance().updateTimingUploadConfig(NavigationClient.getInstance().getUploadLogConfigInfo(NativeClient.this.mContext));
                if (NativeClient.this.naviUpdateListener != null) {
                    NativeClient.this.naviUpdateListener.onNaviUpdate();
                }

            }
        });
        FwLog.write(3, 1, LogTag.L_INIT_O.getTag(), "appkey|deviceId|dbPath|initResult", new Object[]{appKey, deviceId, this.dbPath, result});
    }

    public void initAppendixModule() {
        RtLogUploadManager.getInstance().init(this.mContext, "4.0.0.1", this.deviceId, this.appKey);
        this.initFileTransferClient();
        this.setChatRoomStatusNotificationListener();
    }

    public void initThreadPool() {
        this.executorService = new ScheduledThreadPoolExecutor(1, this.threadFactory("Get ChatRoomEntry", false));
        this.executorService.setKeepAliveTime(60L, TimeUnit.SECONDS);
        this.executorService.allowCoreThreadTimeOut(true);
        this.receiveMessageExecutor = Executors.newSingleThreadExecutor(this.threadFactory("IPC_RECEIVEMSG_WORK", false));
    }

    private ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                Thread result = new Thread(runnable, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }

    public void registerMessageType(Class<? extends MessageContent> msgType) {
        try {
            MessageTag tag = (MessageTag)msgType.getAnnotation(MessageTag.class);
            if (tag != null) {
                String objName = tag.value();
                Constructor<? extends MessageContent> constructor = msgType.getDeclaredConstructor(byte[].class);
                Constructor<? extends MessageHandler> handlerConstructor = tag.messageHandler().getConstructor(Context.class);
                MessageHandler messageHandler = (MessageHandler)handlerConstructor.newInstance(this.mContext);
                this.messageHandlerMap.put(objName, messageHandler);
                this.messageContentConstructorMap.put(objName, constructor);
                this.nativeObj.RegisterMessageType(objName, tag.flag());
            }
        } catch (Throwable var7) {
            FwLog.write(1, 1, LogTag.L_REGTYPE_E.getTag(), "msg", new Object[]{var7.getMessage()});
        }

    }

    public void registerMessageType(List<Class<? extends MessageContent>> msgTypeList) {
        try {
            if (msgTypeList == null || msgTypeList.size() == 0) {
                return;
            }

            Iterator var2 = msgTypeList.iterator();

            while(var2.hasNext()) {
                Class<? extends MessageContent> msgType = (Class)var2.next();
                MessageTag tag = (MessageTag)msgType.getAnnotation(MessageTag.class);
                if (tag != null) {
                    String objName = tag.value();
                    Constructor<? extends MessageContent> constructor = msgType.getDeclaredConstructor(byte[].class);
                    Constructor<? extends MessageHandler> handlerConstructor = tag.messageHandler().getConstructor(Context.class);
                    MessageHandler messageHandler = (MessageHandler)handlerConstructor.newInstance(this.mContext);
                    this.messageHandlerMap.put(objName, messageHandler);
                    this.messageContentConstructorMap.put(objName, constructor);
                    this.nativeObj.RegisterMessageType(objName, tag.flag());
                }
            }
        } catch (Throwable var9) {
            FwLog.write(1, 1, LogTag.L_REGTYPE_E.getTag(), "msg", new Object[]{var9.getMessage()});
        }

    }

    void setIpcConnectTimeOut() {
        ConnectionService.getInstance().setIpcConnectTimeOut();
    }

    void initIpcConnectStatus(int status) {
        ConnectionService.getInstance().initConnectStatus(status);
    }

    void connect(String token, boolean isReconnect, boolean inForeground, NativeClient.IConnectResultCallback<String> callback) {
        this.setEnvInfo(this.mContext);
        RtLogUploadManager.getInstance().startTimingUploadTask();
        ConnectionService.getInstance().connect(token, isReconnect, inForeground, callback);
    }

    void ping(final Context context) {
        if (this.mWorkHandler != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (NativeClient.this.nativeObj != null) {
                        WakeLockUtils.startNextHeartbeat(context);
                        NativeClient.this.nativeObj.ping();
                    }

                }
            });
        }

    }

    public String getCurrentUserId() {
        return TextUtils.isEmpty(this.curUserId) ? (this.curUserId = NavigationClient.getInstance().getUserId(this.mContext)) : this.curUserId;
    }

    void setCurrentUserId(String userId) {
        this.curUserId = userId;
    }

    public void disconnect() {
        this.disconnect(true);
    }

    public void disconnect(boolean isReceivePush) {
        ConnectionService.getInstance().disconnect(isReceivePush);
    }

    public List<Conversation> getConversationList() {
        int[] conversationTypes = new int[]{ConversationType.PRIVATE.getValue(), ConversationType.DISCUSSION.getValue(), ConversationType.GROUP.getValue(), ConversationType.SYSTEM.getValue()};
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            return this.getConversationList(conversationTypes);
        }
    }

    public List<Conversation> getConversationList(int[] conversationTypeValues) {
        io.rong.imlib.NativeObject.Conversation[] conversations = null;

        try {
            conversations = this.nativeObj.GetConversationListEx(conversationTypeValues);
        } catch (Exception var8) {
            RLog.e("NativeClient", "getConversationList Exception", var8);
        }

        if (conversations == null) {
            return null;
        } else {
            List<Conversation> result = new ArrayList();
            io.rong.imlib.NativeObject.Conversation[] var4 = conversations;
            int var5 = conversations.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                io.rong.imlib.NativeObject.Conversation item = var4[var6];
                result.add(this.renderConversationFromNative(item));
            }

            return result;
        }
    }

    public List<Conversation> getBlockedConversationList(int[] conversationTypes) {
        io.rong.imlib.NativeObject.Conversation[] conversations = this.nativeObj.GetBlockedConversations(conversationTypes);
        if (conversations == null) {
            return null;
        } else {
            List<Conversation> result = new ArrayList();
            io.rong.imlib.NativeObject.Conversation[] var4 = conversations;
            int var5 = conversations.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                io.rong.imlib.NativeObject.Conversation item = var4[var6];
                result.add(this.renderConversationFromNative(item));
            }

            return result;
        }
    }

    public List<Conversation> getConversationListByPage(int[] conversationTypeValues, long timeStamp, int count) {
        io.rong.imlib.NativeObject.Conversation[] conversations = null;

        try {
            conversations = this.nativeObj.GetConversationList(conversationTypeValues, timeStamp, count);
        } catch (Exception var11) {
            RLog.e("NativeClient", "getConversationListByPage Exception", var11);
        }

        if (conversations == null) {
            return null;
        } else {
            List<Conversation> result = new ArrayList();
            io.rong.imlib.NativeObject.Conversation[] var7 = conversations;
            int var8 = conversations.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                io.rong.imlib.NativeObject.Conversation item = var7[var9];
                result.add(this.renderConversationFromNative(item));
            }

            return result;
        }
    }

    private Conversation renderConversationFromNative(io.rong.imlib.NativeObject.Conversation conversation) {
        Conversation result = new Conversation();
        result.setTargetId(conversation.getTargetId());
        result.setLatestMessageId((int)conversation.getMessageId());
        result.setConversationTitle(conversation.getConversationTitle());
        result.setUnreadMessageCount(conversation.getUnreadMessageCount());
        result.setConversationType(ConversationType.setValue(conversation.getConversationType()));
        result.setTop(conversation.isTop());
        result.setObjectName(conversation.getObjectName());
        if (conversation.getMessageId() > 0L) {
            Message message = new Message();
            message.setMessageId((int)conversation.getMessageId());
            message.setSenderUserId(conversation.getSenderUserId());
            result.setLatestMessage(this.renderMessageContent(conversation.getObjectName(), conversation.getContent(), message));
        }

        result.setReceivedStatus(new ReceivedStatus(conversation.getReadStatus()));
        result.setReceivedTime(conversation.getReceivedTime());
        result.setSentTime(conversation.getSentTime());
        result.setSenderUserId(conversation.getSenderUserId());
        result.setSentStatus(SentStatus.setValue(conversation.getSentStatus()));
        result.setSenderUserName(conversation.getSenderName());
        result.setDraft(conversation.getDraft());
        result.setPortraitUrl(conversation.getPortraitUrl());
        result.setNotificationStatus(conversation.isBlockPush() ? ConversationNotificationStatus.DO_NOT_DISTURB : ConversationNotificationStatus.NOTIFY);
        result.setMentionedCount(conversation.getMentionCount());
        return result;
    }

    public Conversation getConversation(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            io.rong.imlib.NativeObject.Conversation conversation = this.nativeObj.GetConversationEx(targetId, conversationType.getValue());
            if (conversation == null) {
                return null;
            } else {
                Conversation c = this.renderConversationFromNative(conversation);
                c.setConversationType(conversationType);
                return c;
            }
        } else {
            throw new IllegalArgumentException("ConversationType 和 TargetId 参数异常");
        }
    }

    public boolean removeConversation(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && !TextUtils.isEmpty(targetId.trim())) {
            targetId = targetId.trim();
            return this.nativeObj.RemoveConversation(conversationType.getValue(), targetId);
        } else {
            throw new IllegalArgumentException("ConversationType 和 TargetId 参数异常");
        }
    }

    public boolean setConversationToTop(ConversationType conversationType, String targetId, boolean isTop, boolean needCreate) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            return this.nativeObj.SetIsTop(conversationType.getValue(), targetId, isTop, needCreate);
        } else {
            throw new IllegalArgumentException("ConversationType 或 TargetId 参数异常");
        }
    }

    private void preCheck(Object... params) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (params != null) {
            Object[] var2 = params;
            int var3 = params.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object param = var2[var4];
                if (param == null || param instanceof String && TextUtils.isEmpty((String)param)) {
                    throw new IllegalArgumentException("参数异常");
                }
            }

        }
    }

    private String replaceNullStr(String string) {
        return string == null ? "" : string;
    }

    public boolean createEncryptedConversation(String targetId, RCEncryptedSession chatInfo) {
        this.preCheck(targetId, chatInfo);
        return this.nativeObj.CreateEncryptedConversation(this.replaceNullStr(targetId), this.replaceNullStr(chatInfo.getTargetId()), this.replaceNullStr(chatInfo.getRemoteEncId()), this.replaceNullStr(chatInfo.getEncKey()), this.replaceNullStr(chatInfo.getEncXA()), chatInfo.getEncStatus());
    }

    public RCEncryptedSession getEncryptedConversation(String targetId) {
        this.preCheck(targetId);
        RCEncryptedSession chaInfo = this.nativeObj.GetEncryptedConversationInfo(targetId);
        RLog.d("NativeClient", "getEncryptedConversation --> " + (chaInfo == null ? "null" : chaInfo.toString()));
        return chaInfo;
    }

    public List<RCEncryptedSession> getAllEncryptedConversations() {
        this.preCheck();
        return this.nativeObj.GetEncryptedConversations();
    }

    public boolean setEncryptedConversation(String targetId, RCEncryptedSession chatInfo) {
        this.preCheck(targetId, chatInfo);
        return this.nativeObj.SetEncryptedConversationInfo(this.replaceNullStr(targetId), this.replaceNullStr(chatInfo.getTargetId()), this.replaceNullStr(chatInfo.getRemoteEncId()), this.replaceNullStr(chatInfo.getEncKey()), this.replaceNullStr(chatInfo.getEncXA()), chatInfo.getEncStatus());
    }

    public boolean removeEncryptedConversation(String targetId) {
        this.preCheck(targetId);
        return this.nativeObj.RemoveEncryptedConversation(this.replaceNullStr(targetId));
    }

    public boolean clearEncryptedConversations() {
        this.preCheck();
        return this.nativeObj.ClearEncryptedConversations();
    }

    public int getTotalUnreadCount() {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            return this.nativeObj.GetTotalUnreadCount();
        }
    }

    public int getUnreadCount(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            return this.nativeObj.GetUnreadCount(targetId, conversationType.getValue());
        } else {
            throw new IllegalArgumentException("ConversationType 或 TargetId 参数异常");
        }
    }

    public int getTotalUnreadCount(Conversation... conversations) {
        io.rong.imlib.NativeObject.Conversation[] nativeConversations = new io.rong.imlib.NativeObject.Conversation[conversations.length];

        for(int i = 0; i < conversations.length; ++i) {
            io.rong.imlib.NativeObject.Conversation conversation = new io.rong.imlib.NativeObject.Conversation();
            conversation.setConversationType(conversations[i].getConversationType().getValue());
            conversation.setTargetId(conversations[i].getTargetId());
            nativeConversations[i] = conversation;
        }

        return this.nativeObj.GetDNDUnreadCount(nativeConversations);
    }

    public int getUnreadCount(ConversationType... conversationTypes) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationTypes != null && conversationTypes.length != 0) {
            int[] conversationTypeValues = new int[conversationTypes.length];
            int i = 0;
            ConversationType[] var4 = conversationTypes;
            int var5 = conversationTypes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ConversationType conversationType = var4[var6];
                conversationTypeValues[i] = conversationType.getValue();
                ++i;
            }

            return this.nativeObj.GetCateUnreadCount(conversationTypeValues, true);
        } else {
            throw new IllegalArgumentException("ConversationTypes 参数异常。");
        }
    }

    public int getUnreadCount(boolean withDND, ConversationType... conversationTypes) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationTypes != null && conversationTypes.length != 0) {
            int[] conversationTypeValues = new int[conversationTypes.length];
            int i = 0;
            ConversationType[] var5 = conversationTypes;
            int var6 = conversationTypes.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                ConversationType conversationType = var5[var7];
                conversationTypeValues[i] = conversationType.getValue();
                ++i;
            }

            return this.nativeObj.GetCateUnreadCount(conversationTypeValues, withDND);
        } else {
            throw new IllegalArgumentException("ConversationTypes 参数异常。");
        }
    }

    public List<Message> getLatestMessages(ConversationType conversationType, String targetId, int count) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            targetId = targetId.trim();
            return this.getHistoryMessages(conversationType, targetId, -1, count);
        } else {
            throw new IllegalArgumentException("ConversationTypes 或 targetId 参数异常。");
        }
    }

    public List<Message> getHistoryMessages(ConversationType conversationType, String targetId, int oldestMessageId, int count) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            targetId = targetId.trim();
            io.rong.imlib.NativeObject.Message[] array = this.nativeObj.GetHistoryMessagesEx(targetId, conversationType.getValue(), "", oldestMessageId, count, true);
            List<Message> list = new ArrayList();
            if (array == null) {
                return list;
            } else {
                io.rong.imlib.NativeObject.Message[] var7 = array;
                int var8 = array.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    io.rong.imlib.NativeObject.Message item = var7[var9];
                    Message msg = new Message(item);
                    msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                    list.add(msg);
                }

                return list;
            }
        } else {
            throw new IllegalArgumentException("ConversationTypes 或 targetId 参数异常。");
        }
    }

    public List<Message> getHistoryMessages(ConversationType conversationType, String targetId, String objectName, int oldestMessageId, int count) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && !TextUtils.isEmpty(objectName)) {
            targetId = targetId.trim();
            io.rong.imlib.NativeObject.Message[] messages = this.nativeObj.GetHistoryMessagesEx(targetId, conversationType.getValue(), objectName, oldestMessageId, count, true);
            List<Message> list = new ArrayList();
            if (messages == null) {
                return null;
            } else {
                io.rong.imlib.NativeObject.Message[] var8 = messages;
                int var9 = messages.length;

                for(int var10 = 0; var10 < var9; ++var10) {
                    io.rong.imlib.NativeObject.Message item = var8[var10];
                    Message msg = new Message(item);
                    msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                    list.add(msg);
                }

                return list;
            }
        } else {
            throw new IllegalArgumentException("ConversationTypes, objectName 或 targetId 参数异常。");
        }
    }

    public List<Message> getHistoryMessages(ConversationType conversationType, String targetId, String objectName, int oldestMessageId, int count, boolean direction) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && !TextUtils.isEmpty(objectName)) {
            targetId = targetId.trim();
            io.rong.imlib.NativeObject.Message[] messages = this.nativeObj.GetHistoryMessagesEx(targetId, conversationType.getValue(), objectName, oldestMessageId, count, direction);
            List<Message> list = new ArrayList();
            if (messages == null) {
                return null;
            } else {
                io.rong.imlib.NativeObject.Message[] var9 = messages;
                int var10 = messages.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    io.rong.imlib.NativeObject.Message item = var9[var11];
                    Message msg = new Message(item);
                    msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                    list.add(msg);
                }

                return list;
            }
        } else {
            throw new IllegalArgumentException("ConversationTypes, objectName 或 targetId 参数异常。");
        }
    }

    public List<Message> getHistoryMessages(ConversationType conversationType, String targetId, List<String> objectNames, long timestamp, int count, boolean direction) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && objectNames != null && objectNames.size() != 0) {
            targetId = targetId.trim();
            int size = objectNames.size();
            io.rong.imlib.NativeObject.Message[] messages = this.nativeObj.GetHistoryMessagesByObjectNames(targetId, conversationType.getValue(), (String[])objectNames.toArray(new String[size]), timestamp, count, direction);
            List<Message> list = new ArrayList();
            if (messages == null) {
                return null;
            } else {
                io.rong.imlib.NativeObject.Message[] var11 = messages;
                int var12 = messages.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    io.rong.imlib.NativeObject.Message item = var11[var13];
                    Message msg = new Message(item);
                    msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                    list.add(msg);
                }

                return list;
            }
        } else {
            throw new IllegalArgumentException("ConversationTypes, objectName 或 targetId 参数异常。");
        }
    }

    public void getRemoteHistoryMessages(ConversationType conversationType, String targetId, long dataTime, int count, final NativeClient.IResultCallback<List<Message>> callback) {
        RLog.i("NativeClient", "getRemoteHistoryMessages call");
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && callback != null) {
            targetId = targetId.trim();
            if (NavigationClient.getInstance().isGetRemoteHistoryEnabled(this.mContext)) {
                this.nativeObj.LoadHistoryMessage(targetId, conversationType.getValue(), dataTime, count, new HistoryMessageListener() {
                    public void onReceived(io.rong.imlib.NativeObject.Message[] messages, long timestamp) {
                        List<Message> list = new ArrayList();
                        if (messages != null && messages.length > 0) {
                            io.rong.imlib.NativeObject.Message[] var5 = messages;
                            int var6 = messages.length;

                            for(int var7 = 0; var7 < var6; ++var7) {
                                io.rong.imlib.NativeObject.Message item = var5[var7];
                                Message msg = new Message(item);
                                msg.setContent(NativeClient.this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                                list.add(msg);
                            }
                        }

                        callback.onSuccess(list);
                    }

                    public void onError(int status) {
                        callback.onError(status);
                    }
                });
            } else {
                callback.onError(33007);
            }

        } else {
            throw new IllegalArgumentException("ConversationTypes，callback 或 targetId 参数异常。");
        }
    }

    public void getRemoteHistoryMessages(ConversationType conversationType, String targetId, RemoteHistoryMsgOption remoteHistoryMsgOption, final NativeClient.IResultCallback<List<Message>> callback) {
        RLog.i("NativeClient", "getRemoteHistoryMessages call");
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && callback != null) {
            targetId = targetId.trim();
            if (NavigationClient.getInstance().isGetRemoteHistoryEnabled(this.mContext)) {
                this.nativeObj.LoadHistoryMessageOption(targetId, conversationType.getValue(), remoteHistoryMsgOption.getDataTime(), remoteHistoryMsgOption.getCount(), remoteHistoryMsgOption.getOrder(), remoteHistoryMsgOption.isIncludeLocalExistMessage(), new HistoryMessageListener() {
                    public void onReceived(io.rong.imlib.NativeObject.Message[] messages, long timestamp) {
                        List<Message> list = new ArrayList();
                        if (messages != null && messages.length > 0) {
                            io.rong.imlib.NativeObject.Message[] var5 = messages;
                            int var6 = messages.length;

                            for(int var7 = 0; var7 < var6; ++var7) {
                                io.rong.imlib.NativeObject.Message item = var5[var7];
                                Message msg = new Message(item);
                                msg.setContent(NativeClient.this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                                list.add(msg);
                            }
                        }

                        callback.onSuccess(list);
                    }

                    public void onError(int status) {
                        callback.onError(status);
                    }
                });
            } else {
                callback.onError(33007);
            }

        } else {
            throw new IllegalArgumentException("ConversationTypes，callback 或 targetId 参数异常。");
        }
    }

    public void getChatroomHistoryMessages(String targetId, long recordTime, int count, int order, final NativeClient.IResultCallbackEx<List<Message>, Long> callback) {
        RLog.i("NativeClient", "getChatroomHistoryMessages");
        if (NavigationClient.getInstance().isChatroomHistoryEnabled(this.mContext)) {
            this.nativeObj.GetChatroomHistoryMessage(targetId, recordTime, count, order, new HistoryMessageListener() {
                public void onReceived(io.rong.imlib.NativeObject.Message[] messages, long timestamp) {
                    List<Message> list = new ArrayList();
                    if (messages != null && messages.length > 0) {
                        io.rong.imlib.NativeObject.Message[] var5 = messages;
                        int var6 = messages.length;

                        for(int var7 = 0; var7 < var6; ++var7) {
                            io.rong.imlib.NativeObject.Message item = var5[var7];
                            Message msg = new Message(item);
                            msg.setContent(NativeClient.this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                            list.add(msg);
                        }
                    }

                    callback.onSuccess(list, timestamp);
                }

                public void onError(int status) {
                    callback.onError(status);
                }
            });
        } else {
            callback.onError(23414);
        }

    }

    public void getUserStatus(String userId, final NativeClient.IResultCallbackEx<String, Integer> resultCallbackEx) {
        this.nativeObj.GetUserStatus(userId, new CreateDiscussionCallback() {
            public void OnSuccess(String platformInfo) {
                resultCallbackEx.onSuccess(platformInfo, 0);
            }

            public void OnError(int errorCode) {
                resultCallbackEx.onError(errorCode);
            }
        });
    }

    public void subscribeStatus(List<String> users, final NativeClient.IResultCallbackEx<Integer, Integer> resultCallbackEx) {
        int userSize = users.size();
        this.nativeObj.SubscribeStatus((String[])users.toArray(new String[userSize]), new PublishAckListener() {
            public void operationComplete(int opStatus, String msg_uid, long timestamp) {
                if (opStatus == 0) {
                    resultCallbackEx.onSuccess(opStatus, 0);
                } else {
                    resultCallbackEx.onError(opStatus);
                }

            }
        });
    }

    public void setSubscribeStatusListener(final NativeClient.IResultCallbackEx<String, String> resultCallback) {
        this.nativeObj.SetSubscribeStatusListener(new UserStatusListener() {
            public void onStatusReceived(String objName, String content) {
                resultCallback.onSuccess(objName, content);
            }
        });
    }

    public void setPushSetting(int key, String value, final NativeClient.IResultCallback<Long> resultCallback) {
        this.nativeObj.SetPushSetting(key, value, new SetPushSettingListener() {
            public void onSuccess(long version) {
                resultCallback.onSuccess(version);
            }

            public void onError(int code) {
                resultCallback.onError(code);
            }
        });
    }

    public String getPushSetting(int key) {
        return this.nativeObj.GetPushSetting(key);
    }

    public void setUserStatus(int status, final NativeClient.IResultCallback<Integer> resultCallback) {
        this.nativeObj.SetUserStatus(status, new PublishAckListener() {
            public void operationComplete(int errorCode, String token, long timestamp) {
                if (errorCode == 0) {
                    resultCallback.onSuccess(0);
                } else {
                    resultCallback.onError(errorCode);
                }

            }
        });
    }

    public boolean deleteMessages(int[] messageIds) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (messageIds != null && messageIds.length != 0) {
            long[] lIds = new long[]{(long)messageIds.length};

            for(int i = 0; i < messageIds.length; ++i) {
                lIds[i] = (long)messageIds[i];
            }

            return this.nativeObj.DeleteMessages(lIds);
        } else {
            throw new IllegalArgumentException("MessageIds 参数异常。");
        }
    }

    public boolean deleteMessage(ConversationType conversationType, String targetId) {
        return this.nativeObj.ClearMessages(conversationType.getValue(), targetId, true);
    }

    public void deleteMessages(ConversationType conversationType, String targetId, Message[] messages, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (messages != null && messages.length != 0) {
            io.rong.imlib.NativeObject.Message[] nativeMessages = new io.rong.imlib.NativeObject.Message[messages.length];
            StringBuilder uIds = new StringBuilder();

            for(int i = 0; i < messages.length; ++i) {
                nativeMessages[i] = new io.rong.imlib.NativeObject.Message();
                nativeMessages[i].setUId(messages[i].getUId());
                nativeMessages[i].setSentTime(messages[i].getSentTime());
                nativeMessages[i].setMessageDirection(messages[i].getMessageDirection().equals(MessageDirection.RECEIVE));
                uIds.append(messages[i].getUId()).append("/");
            }

            FwLog.write(4, 1, LogTag.L_DELETE_MESSAGES_S.getTag(), "messageUIds:", new Object[]{uIds.toString()});
            this.nativeObj.DeleteRemoteMessages(conversationType.getValue(), targetId, nativeMessages, true, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException("messages 参数异常。");
        }
    }

    public boolean clearMessages(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            return this.nativeObj.ClearMessages(conversationType.getValue(), targetId, false);
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public void cleanRemoteHistoryMessages(ConversationType conversationType, String targetId, long recordTime, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            this.nativeObj.CleanRemoteHistoryMessage(conversationType.getValue(), targetId, recordTime, new PublishAckListener() {
                public void operationComplete(int opStatus, String msg_uid, long timestamp) {
                    if (callback != null) {
                        if (opStatus == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(opStatus);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public void cleanHistoryMessages(ConversationType conversationType, String targetId, long recordTime, NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            boolean result = this.nativeObj.CleanHistoryMessages(conversationType.getValue(), targetId, recordTime);
            if (callback != null) {
                if (result) {
                    callback.onSuccess();
                } else {
                    callback.onError(-1);
                }
            }

        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public boolean clearMessagesUnreadStatus(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            return this.nativeObj.ClearUnread(conversationType.getValue(), targetId);
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public boolean setMessageExtra(int messageId, String value) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (messageId == 0) {
            throw new IllegalArgumentException("messageId 参数异常。");
        } else {
            return this.nativeObj.SetMessageExtra((long)messageId, value);
        }
    }

    public boolean setMessageReceivedStatus(int messageId, ReceivedStatus receivedStatus) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (receivedStatus != null && messageId != 0) {
            return this.nativeObj.SetReadStatus((long)messageId, receivedStatus.getFlag());
        } else {
            throw new IllegalArgumentException("receivedStatus 或 messageId 参数异常。");
        }
    }

    public boolean setMessageSentStatus(int messageId, SentStatus sentStatus) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (sentStatus != null && messageId != 0) {
            return this.nativeObj.SetSendStatus((long)messageId, sentStatus.getValue());
        } else {
            throw new IllegalArgumentException("sentStatus 或 messageId 参数异常。");
        }
    }

    public String getTextMessageDraft(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            return this.nativeObj.GetTextMessageDraft(conversationType.getValue(), targetId);
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public boolean saveTextMessageDraft(ConversationType conversationType, String targetId, String content) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            return this.nativeObj.SetTextMessageDraft(conversationType.getValue(), targetId, content);
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public boolean clearTextMessageDraft(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            String draft = this.getTextMessageDraft(conversationType, targetId);
            return !TextUtils.isEmpty(draft) ? this.saveTextMessageDraft(conversationType, targetId, "") : true;
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public void getDiscussion(String discussionId, final NativeClient.IResultCallback<Discussion> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(discussionId)) {
            throw new IllegalArgumentException(" discussionId 参数异常。");
        } else {
            DiscussionInfo discussionInfo = this.nativeObj.GetDiscussionInfoSync(discussionId);
            if (discussionInfo != null) {
                Discussion discussion = new Discussion(discussionInfo);
                if (discussion.getMemberIdList() != null && discussion.getMemberIdList().size() != 0) {
                    if (callback != null) {
                        callback.onSuccess(discussion);
                    }
                } else {
                    this.nativeObj.GetDiscussionInfo(discussionId, new DiscussionInfoListener() {
                        public void onReceived(DiscussionInfo info) {
                            if (callback != null) {
                                callback.onSuccess(new Discussion(info));
                            }

                        }

                        public void OnError(int status) {
                            if (callback != null) {
                                callback.onError(status);
                            }

                        }
                    });
                }
            } else {
                this.nativeObj.GetDiscussionInfo(discussionId, new DiscussionInfoListener() {
                    public void onReceived(DiscussionInfo info) {
                        if (callback != null) {
                            callback.onSuccess(new Discussion(info));
                        }

                    }

                    public void OnError(int status) {
                        if (callback != null) {
                            callback.onError(status);
                        }

                    }
                });
            }

        }
    }

    public void setDiscussionName(String discussionId, String name, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(discussionId) && !TextUtils.isEmpty(discussionId.trim()) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(name.trim())) {
            this.nativeObj.RenameDiscussion(discussionId, name, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException(" discussionId 或 name 参数异常。");
        }
    }

    public void createDiscussion(String name, List<String> userIdList, final NativeClient.IResultCallback<String> callback) {
        if (!TextUtils.isEmpty(this.curUserId)) {
            userIdList.remove(this.curUserId);
        }

        String[] ids = new String[userIdList.size()];
        userIdList.toArray(ids);
        this.nativeObj.CreateInviteDiscussion(name, ids, new CreateDiscussionCallback() {
            public void OnError(int errorCode) {
                if (callback != null) {
                    callback.onError(errorCode);
                }

            }

            public void OnSuccess(String discussionId) {
                if (callback != null) {
                    callback.onSuccess(discussionId);
                }

            }
        });
    }

    public void searchPublicService(String keyWords, int businessType, int searchType, final NativeClient.IResultCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (keyWords == null) {
            throw new IllegalArgumentException("keyWords 参数异常。");
        } else {
            this.nativeObj.SearchAccount(keyWords, businessType, searchType, new AccountInfoListener() {
                public void onReceived(AccountInfo[] info) {
                    ArrayList<PublicServiceProfile> list = new ArrayList();
                    AccountInfo[] var3 = info;
                    int var4 = info.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        AccountInfo accountInfo = var3[var5];
                        PublicServiceProfile item = new PublicServiceProfile();
                        item.setTargetId(new String(accountInfo.getAccountId()));
                        item.setName(new String(accountInfo.getAccountName()));
                        item.setPublicServiceType(ConversationType.setValue(accountInfo.getAccountType()));
                        item.setPortraitUri(Uri.parse(new String(accountInfo.getAccountUri())));
                        String ss = new String(accountInfo.getExtra());
                        RLog.i("NativeClient", "getPublicAccountInfoList extra:" + ss);
                        item.setExtra(ss);
                        list.add(item);
                    }

                    PublicServiceProfileList infoList = new PublicServiceProfileList(list);
                    callback.onSuccess(infoList);
                }

                public void OnError(int status) {
                    callback.onError(status);
                }
            });
        }
    }

    public void subscribePublicService(String targetId, int categoryId, boolean subscribe, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.SubscribeAccount(targetId, categoryId, subscribe, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        }
    }

    public void getPublicServiceProfile(String targetId, int categoryId, NativeClient.IResultCallback<PublicServiceProfile> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("RongIMClient 尚未初始化!");
        } else if (targetId == null) {
            throw new IllegalArgumentException("targetId 参数异常。");
        } else {
            if (callback != null) {
                PublicServiceProfile serviceInfo = new PublicServiceProfile();
                UserInfo info = this.nativeObj.GetUserInfoExSync(targetId, categoryId);
                if (info != null) {
                    serviceInfo.setTargetId(targetId);
                    serviceInfo.setName(info.getUserName());
                    if (info.getUrl() != null) {
                        serviceInfo.setPortraitUri(Uri.parse(info.getUrl()));
                    }

                    serviceInfo.setPublicServiceType(ConversationType.setValue(info.getCategoryId()));
                    serviceInfo.setExtra(info.getAccountExtra());
                    callback.onSuccess(serviceInfo);
                } else {
                    RLog.e("NativeClient", "Public service info is null");
                    callback.onError(ErrorCode.RC_PUBLIC_SERVICE_PROFILE_NOT_EXIST.getValue());
                }
            }

        }
    }

    public void getPublicServiceList(NativeClient.IResultCallback<PublicServiceProfileList> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("RongIMClient 尚未初始化!");
        } else {
            if (callback != null) {
                AccountInfo[] info = this.nativeObj.LoadAccountInfo();
                ArrayList list;
                PublicServiceProfileList infoList;
                if (info != null && info.length > 0) {
                    list = new ArrayList();
                    AccountInfo[] var10 = info;
                    int var5 = info.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        AccountInfo accountInfo = var10[var6];
                        PublicServiceProfile item = new PublicServiceProfile();
                        item.setTargetId(new String(accountInfo.getAccountId()));
                        item.setName(new String(accountInfo.getAccountName()));
                        item.setPublicServiceType(ConversationType.setValue(accountInfo.getAccountType()));
                        item.setPortraitUri(Uri.parse(new String(accountInfo.getAccountUri())));
                        String ss = new String(accountInfo.getExtra());
                        item.setExtra(ss);
                        list.add(item);
                    }

                    infoList = new PublicServiceProfileList(list);
                    callback.onSuccess(infoList);
                } else {
                    RLog.e("NativeClient", "Public service list is empty");
                    list = new ArrayList();
                    infoList = new PublicServiceProfileList(list);
                    callback.onSuccess(infoList);
                }
            }

        }
    }

    public void addMemberToDiscussion(String discussionId, List<String> userIdList, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(discussionId) && userIdList != null && userIdList.size() != 0) {
            String[] ids = new String[userIdList.size()];
            userIdList.toArray(ids);
            this.nativeObj.InviteMemberToDiscussion(discussionId, ids, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException("discussionId 或 userIdList 参数异常。");
        }
    }

    public void removeMemberFromDiscussion(String discussionId, String userId, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(discussionId) && !TextUtils.isEmpty(userId)) {
            this.nativeObj.RemoveMemberFromDiscussion(discussionId, userId, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException("discussionId 或 userId 参数异常。");
        }
    }

    public void quitDiscussion(String discussionId, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(discussionId)) {
            throw new IllegalArgumentException("discussionId 参数异常。");
        } else {
            this.nativeObj.QuitDiscussion(discussionId, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        }
    }

    public Message getMessage(int messageId) {
        io.rong.imlib.NativeObject.Message nativeMsg = this.nativeObj.GetMessageById((long)messageId);
        if (nativeMsg == null) {
            return null;
        } else {
            Message message = new Message(nativeMsg);
            MessageContent content = this.renderMessageContent(nativeMsg.getObjectName(), nativeMsg.getContent(), message);
            message.setContent(content);
            return message;
        }
    }

    private String getSearchableWord(MessageContent content) {
        String filteredStr = "";
        if (content != null) {
            List<String> list = content.getSearchableWord();
            if (list != null && list.size() > 0) {
                StringBuilder builder = new StringBuilder();
                Iterator var5 = list.iterator();

                while(var5.hasNext()) {
                    String str = (String)var5.next();
                    builder.append(str).append("\n");
                }

                filteredStr = builder.toString();
            }
        }

        return filteredStr;
    }

    public void sendMessage(ConversationType conversationType, String targetId, MessageContent content, String pushContent, String pushData, NativeClient.ISendMessageCallback<Message> callback) {
        Message message = Message.obtain(targetId, conversationType, content);
        this.sendMessage(message, pushContent, pushData, (String[])null, callback);
    }

    public void sendMessage(Message message, String pushContent, String pushData, String[] userIds, NativeClient.ISendMessageCallback<Message> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (message.getConversationType() != null && !TextUtils.isEmpty(message.getTargetId()) && message.getContent() != null) {
            MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
            if (TextUtils.isEmpty(message.getSenderUserId())) {
                message.setSenderUserId(this.curUserId);
            }

            message.setMessageDirection(MessageDirection.SEND);
            message.setSentStatus(SentStatus.SENDING);
            message.setSentTime(System.currentTimeMillis());
            message.setObjectName(msgTag.value());
            byte[] data = new byte[1];
            int type;
            if ((msgTag.flag() & 1) == 1 && message.getMessageId() <= 0) {
                type = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), msgTag.value(), message.getSenderUserId(), data, false, 0, SentStatus.SENDING.getValue(), System.currentTimeMillis(), this.getSearchableWord(message.getContent()), 1, "");
                if (type < 0) {
                    message.setSentStatus(SentStatus.FAILED);
                    if (type == ErrorCode.PARAMETER_ERROR.getValue()) {
                        callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                    } else {
                        callback.onError(message, ErrorCode.BIZ_ERROR_DATABASE_ERROR.getValue());
                    }

                    return;
                }

                message.setMessageId(type);
            }

            type = msgTag.flag() == 16 ? 1 : 3;
            MessageHandler handler = this.getMessageHandler(msgTag.value());
            if (handler == null) {
                RLog.e("NativeClient", "sendMessage MessageHandler is null");
                if (callback != null) {
                    callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                }

            } else {
                handler.encodeMessage(message);
                data = message.getContent().encode();
                if (callback != null) {
                    callback.onAttached(message);
                }

                boolean isMentioned = message.getContent().getMentionedInfo() != null;
                if (message.getMessageId() > 0) {
                    this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                }

                this.internalSendMessage(message, pushContent, pushData, userIds, callback, msgTag, data, type, isMentioned);
            }
        } else {
            throw new IllegalArgumentException("message, ConversationType 或 TargetId 参数异常。");
        }
    }

    public void sendMessageOption(Message message, String pushContent, String pushData, SendMessageOption option, String[] userIds, NativeClient.ISendMessageCallback<Message> callback) {
        this.sendMessageOption(message, pushContent, pushData, option, userIds, false, callback);
    }

    public void sendMessageOption(Message message, String pushContent, String pushData, SendMessageOption option, String[] userIds, boolean isFilterBlackList, NativeClient.ISendMessageCallback<Message> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (message.getConversationType() != null && !TextUtils.isEmpty(message.getTargetId()) && message.getContent() != null) {
            MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
            if (TextUtils.isEmpty(message.getSenderUserId())) {
                message.setSenderUserId(this.curUserId);
            }

            message.setMessageDirection(MessageDirection.SEND);
            message.setSentStatus(SentStatus.SENDING);
            message.setSentTime(System.currentTimeMillis());
            message.setObjectName(msgTag.value());
            byte[] data = new byte[1];
            int type;
            if ((msgTag.flag() & 1) == 1 && message.getMessageId() <= 0) {
                type = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), msgTag.value(), message.getSenderUserId(), data, false, 0, SentStatus.SENDING.getValue(), System.currentTimeMillis(), this.getSearchableWord(message.getContent()), 1, "");
                if (type < 0) {
                    message.setSentStatus(SentStatus.FAILED);
                    if (type == ErrorCode.PARAMETER_ERROR.getValue()) {
                        callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                    } else {
                        callback.onError(message, ErrorCode.BIZ_ERROR_DATABASE_ERROR.getValue());
                    }

                    return;
                }

                message.setMessageId(type);
            }

            type = msgTag.flag() == 16 ? 1 : 3;
            MessageHandler handler = this.getMessageHandler(msgTag.value());
            if (handler == null) {
                RLog.e("NativeClient", "sendMessage MessageHandler is null");
                if (callback != null) {
                    callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                }

            } else {
                handler.encodeMessage(message);
                data = message.getContent().encode();
                if (callback != null) {
                    callback.onAttached(message);
                }

                boolean isMentioned = message.getContent().getMentionedInfo() != null;
                boolean isVoIPPush = false;
                if (option != null) {
                    isVoIPPush = option.isVoIPPush();
                }

                if (message.getMessageId() > 0) {
                    this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                }

                this.internalSendMessage(message, pushContent, pushData, userIds, callback, msgTag, data, type, isMentioned, isVoIPPush, isFilterBlackList);
            }
        } else {
            throw new IllegalArgumentException("message, ConversationType 或 TargetId 参数异常。");
        }
    }

    private void internalSendMessage(Message message, String pushContent, String pushData, String[] userIds, NativeClient.ISendMessageCallback<Message> callback, MessageTag msgTag, byte[] data, int type, boolean isMentioned) {
        this.internalSendMessage(message, pushContent, pushData, userIds, callback, msgTag, data, type, isMentioned, false, false);
    }

    private void internalSendMessage(final Message message, String pushContent, String pushData, String[] userIds, final NativeClient.ISendMessageCallback<Message> callback, final MessageTag msgTag, byte[] data, int type, boolean isMentioned, boolean isVoIPPush, boolean isFilterBlackList) {
        Message waitingToSendMessage;
        byte[] encodeData;
        if (message.getConversationType() == ConversationType.ENCRYPTED) {
            try {
                waitingToSendMessage = this.encryptMessageContentIfNeeded(data, message);
                encodeData = waitingToSendMessage.getContent().encode();
            } catch (RuntimeException var16) {
                RLog.e("NativeClient", "stop sending message cause exception occurs while calling encrypteMessageContent() -> " + var16.getLocalizedMessage());
                return;
            }
        } else {
            waitingToSendMessage = message;
            encodeData = data;
        }

        if (waitingToSendMessage.getContent() instanceof MediaMessageContent) {
            Uri localPath = ((MediaMessageContent)waitingToSendMessage.getContent()).getLocalPath();
            MessageHandler handler = this.getMessageHandler(msgTag.value());
            handler.encodeMessage(message);
            ((MediaMessageContent)waitingToSendMessage.getContent()).setLocalPath((Uri)null);
            encodeData = waitingToSendMessage.getContent().encode();
            ((MediaMessageContent)waitingToSendMessage.getContent()).setLocalPath(localPath);
        }

        this.nativeObj.sendMessageWithOption(waitingToSendMessage.getTargetId(), waitingToSendMessage.getConversationType().getValue(), type, msgTag.value(), encodeData, TextUtils.isEmpty(pushContent) ? null : pushContent.getBytes(), TextUtils.isEmpty(pushData) ? null : pushData.getBytes(), (long)waitingToSendMessage.getMessageId(), userIds, new PublishAckListener() {
            public void operationComplete(int code, String msgUId, long sendTime) {
                RLog.d("NativeClient", "sendMessage code = " + code + ", id = " + message.getMessageId() + ", uid = " + msgUId + " " + message.getObjectName());
                if (code == 0) {
                    message.setSentStatus(SentStatus.SENT);
                    message.setSentTime(sendTime);
                    message.setUId(msgUId);
                    if (callback != null) {
                        callback.onSuccess(message);
                    }
                } else {
                    FwLog.write(1, 1, LogTag.P_SEND_MSG_S.getTag(), "code|type|id|tag", new Object[]{code, message.getConversationType().getValue(), message.getTargetId(), msgTag.value()});
                    if (code == ErrorCode.RC_MSG_BLOCKED_SENSITIVE_WORD.getValue()) {
                        NativeClient.this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.FAILED.getValue());
                        message.setSentStatus(SentStatus.FAILED);
                    } else if (code == ErrorCode.RC_MSG_REPLACED_SENSITIVE_WORD.getValue()) {
                        message.setSentStatus(SentStatus.SENT);
                        message.setSentTime(sendTime);
                        message.setUId(msgUId);
                    } else {
                        message.setSentStatus(SentStatus.FAILED);
                    }

                    if (callback != null) {
                        callback.onError(message, code);
                    }
                }

            }
        }, isMentioned, isVoIPPush, isFilterBlackList);
    }

    private Message encryptMessageContentIfNeeded(byte[] content, Message message) throws RuntimeException {
        String targetId = message.getTargetId();
        if (TextUtils.isEmpty(targetId)) {
            throw new RuntimeException("error! message TargetId is empty. message -> " + message.toString());
        } else {
            String[] ids = targetId.split(";;;");
            if (ids.length != 2) {
                throw new RuntimeException("error occurs while spliting targetId -> " + targetId);
            } else {
                RCEncryptedSession encryptedSession = this.nativeObj.GetEncryptedConversationInfo(targetId);
                if (encryptedSession == null) {
                    throw new RuntimeException("error ! getEncryptedConversationInfo is null.");
                } else {
                    String encKeyStr = encryptedSession.getEncKey();
                    RCSecretKey encKey = RCDHCodecTool.fromString2RCSecretKey(encKeyStr);
                    if (encKey == null) {
                        throw new RuntimeException("error occurs when transfering encKey.");
                    } else {
                        byte[] encryptedData = RCDHCodecTool.encrypt(content, encKey);
                        String encryptedContent = Base64.encodeToString(encryptedData, 2);
                        RCEncryptedMessage encContent = new RCEncryptedMessage();
                        encContent.setEncryptedContent(encryptedContent);
                        encContent.setOriginalObjName(message.getObjectName());
                        encContent.setRemoteEncId(encryptedSession.getRemoteEncId());
                        Message encMsg = Message.obtain(ids[1], message.getConversationType(), encContent);
                        encMsg.setObjectName(((MessageTag)encContent.getClass().getAnnotation(MessageTag.class)).value());
                        encMsg.setExtra(message.getExtra());
                        encMsg.setMessageDirection(message.getMessageDirection());
                        encMsg.setMessageId(message.getMessageId());
                        encMsg.setUId(message.getUId());
                        encMsg.setReadReceiptInfo(message.getReadReceiptInfo());
                        encMsg.setReadTime(message.getReadTime());
                        encMsg.setSenderUserId(message.getSenderUserId());
                        encMsg.setSentStatus(message.getSentStatus());
                        encMsg.setReceivedStatus(message.getReceivedStatus());
                        encMsg.setSentTime(message.getSentTime());
                        encMsg.setReceivedTime(message.getReceivedTime());
                        return encMsg;
                    }
                }
            }
        }
    }

    public void sendLocationMessage(Message message, final String pushContent, final String pushData, final NativeClient.ISendMessageCallback<Message> callback) {
        final MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
        if (TextUtils.isEmpty(message.getSenderUserId())) {
            message.setSenderUserId(this.curUserId);
        }

        message.setMessageDirection(MessageDirection.SEND);
        message.setSentTime(System.currentTimeMillis());
        message.setObjectName(msgTag.value());
        byte[] data = message.getContent().encode();
        int id = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), msgTag.value(), message.getSenderUserId(), data, false, 0, SentStatus.SENDING.getValue(), System.currentTimeMillis(), this.getSearchableWord(message.getContent()), 1, "");
        if (id < 0 && callback != null) {
            message.setSentStatus(SentStatus.FAILED);
            if (id == ErrorCode.PARAMETER_ERROR.getValue()) {
                callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
            } else {
                callback.onError(message, ErrorCode.BIZ_ERROR_DATABASE_ERROR.getValue());
            }

        } else {
            message.setMessageId(id);
            if (callback != null) {
                message.setSentStatus(SentStatus.SENDING);
                callback.onAttached(message);
            }

            if (message.getMessageId() == 0) {
                RLog.e("NativeClient", "Location Message saved error");
                if (callback != null) {
                    message.setSentStatus(SentStatus.FAILED);
                    this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.FAILED.getValue());
                    callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                }

            } else {
                MessageHandler handler = this.getMessageHandler(msgTag.value());
                if (handler == null) {
                    RLog.e("NativeClient", "MessageHandler is null");
                    if (callback != null) {
                        message.setSentStatus(SentStatus.FAILED);
                        this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.FAILED.getValue());
                        callback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                    }

                } else {
                    handler.setHandleMessageListener(new IHandleMessageListener() {
                        public void onHandleResult(Message message, int resultCode) {
                            RLog.d("NativeClient", "onHandleResult " + ((LocationMessage)message.getContent()).getImgUri());
                            if (resultCode == 0) {
                                boolean isMentioned = NativeClient.this.isMentionedMessage(message);
                                byte[] data = message.getContent().encode();
                                NativeClient.this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                                NativeClient.this.internalSendMessage(message, pushContent, pushData, (String[])null, callback, msgTag, data, 3, isMentioned);
                            } else {
                                message.setSentStatus(SentStatus.FAILED);
                                NativeClient.this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.FAILED.getValue());
                                if (callback != null) {
                                    callback.onError(message, 30014);
                                }
                            }

                        }
                    });
                    handler.encodeMessage(message);
                }
            }
        }
    }

    public void sendMediaMessage(final Message message, final String[] userIds, final String pushContent, final String pushData, final NativeClient.ISendMediaMessageCallback<Message> mediaMessageCallback) {
        final MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
        if (TextUtils.isEmpty(message.getSenderUserId())) {
            message.setSenderUserId(this.curUserId);
        }

        message.setSentTime(System.currentTimeMillis());
        message.setObjectName(msgTag.value());
        byte[] data = new byte[1];
        final boolean isMentioned = message.getContent().getMentionedInfo() != null;
        final MediaMessageContent mediaMessageContent = (MediaMessageContent)message.getContent();
        final MessageHandler handler = this.getMessageHandler(msgTag.value());
        if (handler == null) {
            RLog.e("NativeClient", "sendMediaMessage MessageHandler is null");
            if (mediaMessageCallback != null) {
                mediaMessageCallback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
            }

        } else {
            if (message.getMessageId() <= 0) {
                int id = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), msgTag.value(), message.getSenderUserId(), data, false, 0, SentStatus.SENDING.getValue(), System.currentTimeMillis(), this.getSearchableWord(message.getContent()), 1, "");
                if (id < 0 && mediaMessageCallback != null) {
                    message.setSentStatus(SentStatus.FAILED);
                    if (id == ErrorCode.PARAMETER_ERROR.getValue()) {
                        mediaMessageCallback.onError(message, ErrorCode.PARAMETER_ERROR.getValue());
                    } else {
                        mediaMessageCallback.onError(message, ErrorCode.BIZ_ERROR_DATABASE_ERROR.getValue());
                    }

                    return;
                }

                message.setMessageId(id);
            }

            message.setSentStatus(SentStatus.SENDING);
            message.setMessageDirection(MessageDirection.SEND);
            handler.encodeMessage(message);
            data = message.getContent().encode();
            this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
            if (mediaMessageCallback != null) {
                mediaMessageCallback.onAttached(message);
            }

            if (mediaMessageContent.getMediaUrl() != null && (mediaMessageContent.getMediaUrl() == null || !TextUtils.isEmpty(mediaMessageContent.getMediaUrl().toString()))) {
                this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                this.internalSendMessage(message, pushContent, pushData, userIds, new NativeClient.ISendMessageCallback<Message>() {
                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                        if (mediaMessageCallback != null) {
                            mediaMessageCallback.onSuccess(message);
                        }

                    }

                    public void onError(Message message, int code) {
                        if (mediaMessageCallback != null) {
                            mediaMessageCallback.onError(message, code);
                        }

                    }
                }, msgTag, data, 3, isMentioned);
            } else {
                NativeClient.IResultProgressCallback progressCallback = new NativeClient.IResultProgressCallback<String>() {
                    public void onSuccess(String s) {
                        mediaMessageContent.setMediaUrl(Uri.parse(s));
                        handler.encodeMessage(message);
                        message.setContent(mediaMessageContent);
                        byte[] finalData = mediaMessageContent.encode();
                        NativeClient.this.nativeObj.SetMessageContent((long)message.getMessageId(), finalData, "");
                        NativeClient.this.internalSendMessage(message, pushContent, pushData, userIds, new NativeClient.ISendMessageCallback<Message>() {
                            public void onAttached(Message messagex) {
                            }

                            public void onSuccess(Message messagex) {
                                if (mediaMessageCallback != null) {
                                    mediaMessageCallback.onSuccess(messagex);
                                }

                            }

                            public void onError(Message messagex, int code) {
                                if (mediaMessageCallback != null) {
                                    mediaMessageCallback.onError(messagex, code);
                                }

                            }
                        }, msgTag, finalData, 3, isMentioned);
                    }

                    public void onProgress(int progress) {
                        RLog.i("NativeClient", "upload onProgress " + progress);
                        if (mediaMessageCallback != null) {
                            mediaMessageCallback.onProgress(message, progress);
                        }

                    }

                    public void onError(int code) {
                        message.setSentStatus(SentStatus.FAILED);
                        if (mediaMessageCallback != null) {
                            mediaMessageCallback.onError(message, code);
                        }

                        NativeClient.this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.FAILED.getValue());
                    }

                    public void onCanceled(int messageId) {
                        message.setSentStatus(SentStatus.CANCELED);
                        if (mediaMessageCallback != null) {
                            mediaMessageCallback.onCanceled(message);
                        }

                        NativeClient.this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.CANCELED.getValue());
                    }
                };
                this.uploadMedia(message, progressCallback);
            }

        }
    }

    public void sendMediaMessage(Message message, String pushContent, String pushData, NativeClient.ISendMediaMessageCallback<Message> mediaMessageCallback) {
        this.sendMediaMessage(message, (String[])null, pushContent, pushData, mediaMessageCallback);
    }

    public Message insertMessage(ConversationType conversationType, String targetId, String senderUserId, MessageContent content, long sentTime) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && content != null) {
            MessageTag msgTag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                throw new RuntimeException("自定义消息没有加注解信息。");
            } else if (msgTag.flag() == 16) {
                RLog.e("NativeClient", "insertMessage MessageTag can not be STATUS.");
                return null;
            } else {
                String sender = senderUserId;
                Message message = new Message();
                message.setConversationType(conversationType);
                message.setTargetId(targetId);
                if (TextUtils.isEmpty(senderUserId)) {
                    sender = this.curUserId;
                }

                if (TextUtils.isEmpty(this.curUserId)) {
                    message.setMessageDirection(MessageDirection.SEND);
                    message.setSentStatus(SentStatus.SENT);
                } else {
                    message.setMessageDirection(this.curUserId.equals(sender) ? MessageDirection.SEND : MessageDirection.RECEIVE);
                    message.setSentStatus(this.curUserId.equals(sender) ? SentStatus.SENT : SentStatus.RECEIVED);
                }

                message.setSenderUserId(sender);
                message.setReceivedTime(sentTime);
                message.setSentTime(sentTime);
                message.setObjectName(msgTag.value());
                message.setContent(content);
                byte[] data = new byte[1];
                if (message.getMessageId() <= 0) {
                    boolean direction = message.getMessageDirection() != null && message.getMessageDirection().equals(MessageDirection.RECEIVE);
                    int id = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), msgTag.value(), sender, data, direction, 0, SentStatus.SENDING.getValue(), message.getSentTime(), this.getSearchableWord(message.getContent()), 1, "");
                    message.setMessageId(id);
                }

                MessageHandler handler = this.getMessageHandler(msgTag.value());
                if (handler != null) {
                    handler.encodeMessage(message);
                    data = message.getContent().encode();
                    this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                    this.nativeObj.SetSendStatus((long)message.getMessageId(), SentStatus.SENT.getValue());
                    RLog.d("NativeClient", "insertMessage Inserted, id = " + message.getMessageId());
                } else {
                    RLog.e("NativeClient", "insertMessage 该消息未注册，请调用registerMessageType方法注册。");
                }

                return message;
            }
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public Message insertSettingMessage(Message message) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (message.getConversationType() != null && !TextUtils.isEmpty(message.getTargetId()) && message.getContent() != null) {
            MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                throw new RuntimeException("自定义消息没有加注解信息。");
            } else if (msgTag.flag() == 16) {
                RLog.e("NativeClient", "insertMessage MessageTag can not be STATUS.");
                return null;
            } else {
                String sender = message.getSenderUserId();
                if (TextUtils.isEmpty(sender)) {
                    sender = this.curUserId;
                }

                if (TextUtils.isEmpty(this.curUserId)) {
                    message.setMessageDirection(MessageDirection.SEND);
                    message.setSentStatus(SentStatus.SENT);
                } else if (message.getMessageDirection().equals(MessageDirection.RECEIVE)) {
                    message.setSentStatus(SentStatus.RECEIVED);
                }

                message.setReceivedTime(System.currentTimeMillis());
                message.setObjectName(msgTag.value());
                byte[] data = new byte[1];
                if (message.getMessageId() <= 0) {
                    boolean direction = message.getMessageDirection() != null && message.getMessageDirection().equals(MessageDirection.RECEIVE);
                    byte markUnread;
                    if (message.getMessageDirection() == MessageDirection.RECEIVE && message.getReceivedStatus().getFlag() == 0 && msgTag.flag() == 3) {
                        markUnread = 0;
                    } else {
                        markUnread = 1;
                    }

                    int id = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), msgTag.value(), sender, data, direction, message.getReceivedStatus().getFlag(), message.getSentStatus().getValue(), message.getSentTime(), this.getSearchableWord(message.getContent()), markUnread, "");
                    message.setMessageId(id);
                }

                MessageHandler handler = this.getMessageHandler(msgTag.value());
                if (handler != null) {
                    handler.encodeMessage(message);
                    data = message.getContent().encode();
                    this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                    this.nativeObj.SetSendStatus((long)message.getMessageId(), message.getSentStatus().getValue());
                    RLog.d("NativeClient", "insertMessage Inserted, id = " + message.getMessageId());
                } else {
                    RLog.e("NativeClient", "insertMessage 该消息未注册，请调用registerMessageType方法注册。");
                }

                return message;
            }
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public void uploadMedia(Message message, final NativeClient.IResultProgressCallback<String> callback) {
        if (message != null && message.getConversationType() != null && !TextUtils.isEmpty(message.getTargetId()) && message.getContent() != null) {
            Uri localPath = null;
            int type = MimeType.FILE_IMAGE.getValue();
            if (message.getContent() instanceof ImageMessage) {
                localPath = ((ImageMessage)message.getContent()).getLocalUri();
            } else if (message.getContent() instanceof GIFMessage) {
                long length = ((GIFMessage)message.getContent()).getGifDataSize();
                if (length > (long)(this.getGIFLimitSize() * 1024)) {
                    RLog.e("NativeClient", "Gif size is too long! ");
                    callback.onError(ErrorCode.RC_GIF_MSG_SIZE_LIMIT_EXCEED.getValue());
                    return;
                }

                localPath = ((GIFMessage)message.getContent()).getLocalPath();
            } else if (message.getObjectName() != null && message.getObjectName().equals("RC:SightMsg")) {
                int duration = ((SightMessage)message.getContent()).getDuration();
                if (duration > this.getVideoLimitTime()) {
                    RLog.e("NativeClient", "sight duration is too long! ");
                    callback.onError(ErrorCode.RC_SIGHT_MSG_DURATION_LIMIT_EXCEED.getValue());
                    return;
                }

                type = MimeType.FILE_SIGHT.getValue();
                localPath = ((MediaMessageContent)message.getContent()).getLocalPath();
            } else if (message.getContent() instanceof HQVoiceMessage) {
                type = MimeType.FILE_AUDIO.getValue();
                localPath = ((MediaMessageContent)message.getContent()).getLocalPath();
            } else if (message.getObjectName() != null && message.getObjectName().equals("RC:CombineMsg")) {
                type = MimeType.FILE_HTML.getValue();
                localPath = ((MediaMessageContent)message.getContent()).getLocalPath();
            } else if (message.getContent() instanceof MediaMessageContent) {
                type = MimeType.FILE_TEXT_PLAIN.getValue();
                localPath = ((MediaMessageContent)message.getContent()).getLocalPath();
            }

            if (!FileUtils.isFileExistsWithUri(this.mContext, localPath)) {
                RLog.e("NativeClient", "local path of the media file can't be empty!");
                callback.onError(ErrorCode.PARAMETER_ERROR.getValue());
            } else {
                final String fileUri = localPath.toString();
                FileInfo fileInfo = FileUtils.getFileInfoByUri(this.mContext, localPath);
                if (fileInfo != null) {
                    final long fileSize = fileInfo.getSize();
                    final String fileName = fileInfo.getName();
                    final MimeType mimeType = FtUtilities.getMimeType(fileName);
                    final int id = message.getMessageId();
                    final String mimeKey = FtUtilities.generateKey(mimeType.getName()) + this.getSuffixName(fileName);
                    int finalType = type;
                    this.nativeObj.GetUploadToken(type, mimeKey, new TokenListener() {
                        public void OnError(int errorCode, String token, String alternative, String date, String path) {
                            if (errorCode == 0) {
                                String mediaServer = NavigationClient.getInstance().getMediaServer(NativeClient.this.mContext);
                                if (!TextUtils.isEmpty(NativeClient.this.mFileServer)) {
                                    mediaServer = NativeClient.this.mFileServer;
                                }

                                if (!mediaServer.toLowerCase().startsWith("http")) {
                                    mediaServer = (VERSION.SDK_INT >= 28 ? "https://" : "http://") + mediaServer;
                                }

                                Date start = new Date(System.currentTimeMillis());
                                boolean isPrivate = NavigationCacheHelper.getPrivateCloudConfig(NativeClient.this.mContext);
                                FileTransferClient.getInstance().setServiceType(isPrivate ? ServiceType.PRIVATE_CLOUD : ServiceType.QI_NIU);
                                this.uploadFile(token, mimeKey, mediaServer, start, alternative, mediaServer, date, path);
                            } else {
                                callback.onError(errorCode);
                            }

                        }

                        private void uploadFile(String token, final String mimeKeyx, String mediaServer, final Date start, final String alternative, final String logServer, final String date, final String path) {
                            FileTransferClient.getInstance().upload(id, fileUri, token, date, new RequestOption(mimeKeyx, mimeType, mediaServer, new RequestCallBack() {
                                public void onError(int code) {
                                    RLog.i("NativeClient", "mFileServer = " + NativeClient.this.mFileServer);
                                    RLog.i("NativeClient", "getServiceType = " + FileTransferClient.getInstance().getServiceType());
                                    if (TextUtils.isEmpty(NativeClient.this.mFileServer) && FileTransferClient.getInstance().getServiceType() == ServiceType.QI_NIU && NetUtils.isNetWorkAvailable(NativeClient.this.mContext)) {
                                        FileTransferClient.getInstance().setServiceType(ServiceType.BAI_DU);
                                        String bdMediaServer = NavigationClient.getInstance().getBaiDuMediaServer(NativeClient.this.mContext);
                                        if (TextUtils.isEmpty(bdMediaServer)) {
                                            bdMediaServer = "gz.bcebos.com";
                                        }

                                        if (!bdMediaServer.toLowerCase().startsWith("http")) {
                                            bdMediaServer = (VERSION.SDK_INT >= 28 && !NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted() ? "https://" : "http://") + bdMediaServer;
                                        }

                                        bdMediaServer = bdMediaServer + path;
                                        uploadFile(alternative, fileName, bdMediaServer, start, logServer, alternative, date, path);
                                    } else {
                                        Date end = new Date(System.currentTimeMillis());
                                        long diff = end.getTime() - start.getTime();
                                        FwLog.write(1, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"upload", mimeType.getName(), false, fileSize / 1024L, logServer, diff, code});
                                        RLog.d("NativeClient", "uploadMedia onError code =" + code);
                                        callback.onError(code);
                                    }
                                }

                                public void onComplete(String url) {
                                    Date end = new Date(System.currentTimeMillis());
                                    long diff = end.getTime() - start.getTime();
                                    FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"upload", mimeType.getName(), false, fileSize / 1024L, logServer, diff, 0});
                                    if (url == null) {
                                        NativeClient.this.nativeObj.GetDownloadUrl(finalType, mimeKeyx, fileName, new TokenListener() {
                                            public void OnError(int errorCode, String token, String alternativex, String datex, String pathx) {
                                                if (errorCode == 0) {
                                                    callback.onSuccess(token);
                                                } else {
                                                    RLog.d("NativeClient", "GetDownloadUrl onError code =" + errorCode);
                                                    callback.onError(errorCode);
                                                }

                                            }
                                        });
                                    } else {
                                        callback.onSuccess(url);
                                    }

                                }

                                public void onProgress(int progress) {
                                    callback.onProgress(progress);
                                }

                                public void onCanceled(Object tag) {
                                    Date end = new Date(System.currentTimeMillis());
                                    long diff = end.getTime() - start.getTime();
                                    FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"upload", mimeType.getName(), true, fileSize / 1024L, logServer, diff, 0});
                                    callback.onCanceled((Integer)tag);
                                }
                            }));
                        }
                    });
                } else {
                    RLog.e("NativeClient", "Uri error path is " + localPath);
                    callback.onError(ErrorCode.PARAMETER_ERROR.getValue());
                }
            }
        } else {
            RLog.e("NativeClient", "conversation type or targetId or message content can't be null!");
            callback.onError(ErrorCode.PARAMETER_ERROR.getValue());
        }
    }

    private String getSuffixName(String fileName) {
        String suffix = "";
        if (!TextUtils.isEmpty(fileName)) {
            String[] splitArray = fileName.split("\\.");
            if (splitArray.length > 1) {
                suffix = "." + splitArray[splitArray.length - 1];
            }
        }

        return suffix;
    }

    public void downloadMedia(ConversationType conversationType, String targetId, int type, final String imageUrl, final NativeClient.IResultProgressCallback<String> callback) {
        if (conversationType != null && !TextUtils.isEmpty(targetId) && !TextUtils.isEmpty(imageUrl)) {
            String path = FileUtils.getCachePath(this.mContext, "download");
            String fileName = FtUtilities.getFileName(path, DeviceUtils.ShortMD5(2, new String[]{imageUrl}));
            long fileLength = 0L;
            final MimeType mimeType = FtUtilities.getMimeType(fileName);
            final Date start = new Date(System.currentTimeMillis());
            FileTransferClient.getInstance().setMediaPath(path);
            FileTransferClient.getInstance().download(-1, imageUrl, fileLength, new RequestOption(fileName, MimeType.setValue(type), new RequestCallBack() {
                public void onError(int code) {
                    Date end = new Date(System.currentTimeMillis());
                    long diff = end.getTime() - start.getTime();
                    FwLog.write(1, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), false, 0, imageUrl, diff, code});
                    RLog.d("NativeClient", "downloadMedia onError code =" + code);
                    callback.onError(code);
                }

                public void onComplete(String url) {
                    Date end = new Date(System.currentTimeMillis());
                    long diff = end.getTime() - start.getTime();
                    File file = new File(url);
                    long fileSize = file.length();
                    FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), false, fileSize / 1024L, imageUrl, diff, 0});
                    RLog.d("NativeClient", "downloadMedia onComplete url =" + url);
                    callback.onSuccess(url);
                }

                public void onProgress(int progress) {
                    callback.onProgress(progress);
                }

                public void onCanceled(Object tag) {
                    Date end = new Date(System.currentTimeMillis());
                    long diff = end.getTime() - start.getTime();
                    FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), true, 0, imageUrl, diff, 0});
                }
            }));
        } else {
            throw new IllegalArgumentException("conversationType，imageUrl 或 targetId 参数异常。");
        }
    }

    public void downloadMediaMessage(final Message message, final NativeClient.IDownloadMediaMessageCallback<Message> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            String remoteUrl = null;
            String name = "";
            long length = 0L;
            String dir = "media";
            MimeType type = MimeType.NONE;
            if (message.getContent() instanceof ImageMessage && ((ImageMessage)message.getContent()).getRemoteUri() != null) {
                remoteUrl = ((ImageMessage)message.getContent()).getRemoteUri().toString();
                name = DeviceUtils.ShortMD5(2, new String[]{remoteUrl});
                type = MimeType.FILE_IMAGE;
                dir = "image";
            } else if (message.getContent() instanceof GIFMessage && ((GIFMessage)message.getContent()).getRemoteUri() != null) {
                GIFMessage gifMessage = (GIFMessage)message.getContent();
                remoteUrl = gifMessage.getRemoteUri().toString();
                name = DeviceUtils.ShortMD5(2, new String[]{remoteUrl}) + ".gif";
                length = gifMessage.getGifDataSize();
                type = MimeType.FILE_IMAGE;
                dir = "image";
            } else if (message.getContent() instanceof FileMessage && ((FileMessage)message.getContent()).getFileUrl() != null) {
                remoteUrl = ((FileMessage)message.getContent()).getFileUrl().toString();
                FileMessage fileMessage = (FileMessage)message.getContent();
                name = fileMessage.getName();
                if (TextUtils.isEmpty(name)) {
                    name = DeviceUtils.ShortMD5(2, new String[]{remoteUrl});
                }

                length = fileMessage.getSize();
                type = FtUtilities.getMimeType(fileMessage.getType());
                dir = "file";
                MediaMessageContent mediaMessageContent = (MediaMessageContent)message.getContent();
                mediaMessageContent.setLocalPath((Uri)null);
                byte[] data = mediaMessageContent.encode();
                this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
            } else if (message.getContent() instanceof HQVoiceMessage && ((HQVoiceMessage)message.getContent()).getFileUrl() != null) {
                HQVoiceMessage voiceMessage = (HQVoiceMessage)message.getContent();
                remoteUrl = voiceMessage.getFileUrl().toString();
                name = voiceMessage.getName();
                if (TextUtils.isEmpty(name)) {
                    name = DeviceUtils.ShortMD5(2, new String[]{remoteUrl});
                }

                type = MimeType.FILE_AUDIO;
                dir = "audio";
            } else if (message.getContent() instanceof MediaMessageContent && ((MediaMessageContent)message.getContent()).getMediaUrl() != null) {
                remoteUrl = ((MediaMessageContent)message.getContent()).getMediaUrl().toString();
                name = ((MediaMessageContent)message.getContent()).getName();
                if (TextUtils.isEmpty(name)) {
                    name = DeviceUtils.ShortMD5(2, new String[]{remoteUrl});
                }

                type = MimeType.FILE_TEXT_PLAIN;
                dir = "file";
                if (message.getContent() instanceof SightMessage) {
                    length = ((SightMessage)message.getContent()).getSize();
                    dir = "video";
                }
            }

            if (TextUtils.isEmpty(remoteUrl)) {
                RLog.e("NativeClient", "remoteUrl of the media file can't be empty!");
                if (callback != null) {
                    callback.onError(ErrorCode.PARAMETER_ERROR.getValue());
                }

            } else {
                String path = FileUtils.getMediaDownloadDir(this.mContext, dir);
                String fileName = FtUtilities.getFileName(path, name);
                final MimeType mimeType = FtUtilities.getMimeType(fileName);
                final Date start = new Date(System.currentTimeMillis());
                FileTransferClient.getInstance().setMediaPath(path);
                String finalRemoteUrl = remoteUrl;
                FileTransferClient.getInstance().download(message.getMessageId(), remoteUrl, length, new RequestOption(fileName, type, message.getMessageId(), new RequestCallBack() {
                    public void onError(int code) {
                        Date end = new Date(System.currentTimeMillis());
                        long diff = end.getTime() - start.getTime();
                        FwLog.write(1, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), false, 0, finalRemoteUrl, diff, code});
                        RLog.d("NativeClient", "downloadMediaMessage onError code =" + code);
                        callback.onError(code);
                    }

                    public void onComplete(String localUrl) {
                        Date end = new Date(System.currentTimeMillis());
                        long diff = end.getTime() - start.getTime();
                        File file = new File(localUrl);
                        long fileSize = file.length();
                        FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), false, fileSize / 1024L, finalRemoteUrl, diff, 0});
                        RLog.d("NativeClient", "downloadMediaMessage onComplete url =" + localUrl);
                        MediaMessageContent mediaMessageContent = (MediaMessageContent)message.getContent();
                        if (!FileUtils.isValidateLocalUri(Uri.parse(localUrl))) {
                            localUrl = "file://" + localUrl;
                        }

                        mediaMessageContent.setLocalPath(Uri.parse(localUrl));
                        callback.onSuccess(message);
                        byte[] data = mediaMessageContent.encode();
                        NativeClient.this.nativeObj.SetMessageContent((long)message.getMessageId(), data, "");
                    }

                    public void onProgress(int progress) {
                        RLog.i("NativeClient", "download onProgress " + progress);
                        callback.onProgress(progress);
                    }

                    public void onCanceled(Object tag) {
                        Date end = new Date(System.currentTimeMillis());
                        long diff = end.getTime() - start.getTime();
                        FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), true, 0, finalRemoteUrl, diff, 0});
                        callback.onCanceled();
                    }
                }));
            }
        }
    }

    public void downloadMediaFile(String uid, final String fileUrl, String fileName, String path, final NativeClient.IDownloadMediaFileCallback<Boolean> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            final MimeType mimeType = FtUtilities.getMimeType(fileName);
            final Date start = new Date(System.currentTimeMillis());
            String savedPath = FtUtilities.getFileName(path, fileName);
            String pausePath = FileUtils.getTempFilePath(this.mContext, uid);
            if (!(new File(pausePath)).exists()) {
                try {
                    String newFileName = savedPath.substring(path.length() + 1);
                    if (!newFileName.equals(fileName)) {
                        callback.onFileNameChanged(newFileName);
                    }
                } catch (Exception var12) {
                    RLog.e("NativeClient", "downloadMediaFile", var12);
                }
            }

            FileTransferClient.getInstance().setMediaPath(savedPath);
            FileTransferClient.getInstance().download(uid, fileUrl, new RequestOption(savedPath, mimeType, new RequestCallBack() {
                public void onError(int code) {
                    Date end = new Date(System.currentTimeMillis());
                    long diff = end.getTime() - start.getTime();
                    FwLog.write(1, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), false, 0, fileUrl, diff, code});
                    RLog.d("NativeClient", "downloadMediaMessage onError code =" + code);
                    callback.onError(code);
                }

                public void onComplete(String localUrl) {
                    Date end = new Date(System.currentTimeMillis());
                    long diff = end.getTime() - start.getTime();
                    File file = new File(localUrl);
                    long fileSize = file.length();
                    FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), false, fileSize / 1024L, fileUrl, diff, 0});
                    RLog.d("NativeClient", "downloadMediaMessage onComplete url =" + localUrl);
                    callback.onSuccess(true);
                }

                public void onProgress(int progress) {
                    RLog.i("NativeClient", "download onProgress " + progress);
                    callback.onProgress(progress);
                }

                public void onCanceled(Object tag) {
                    Date end = new Date(System.currentTimeMillis());
                    long diff = end.getTime() - start.getTime();
                    FwLog.write(4, 1, LogTag.L_MEDIA_S.getTag(), "type|media_type|user_stop|size|url|duration|code", new Object[]{"download", mimeType.getName(), true, 0, fileUrl, diff, 0});
                    callback.onCanceled();
                }
            }));
        }
    }

    public void getConversationNotificationStatus(ConversationType conversationType, String targetId, NativeClient.IResultCallback<Integer> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId) && callback != null) {
            int status = this.nativeObj.GetBlockPush(targetId, conversationType.getValue());
            callback.onSuccess(status == 100 ? ConversationNotificationStatus.DO_NOT_DISTURB.getValue() : ConversationNotificationStatus.NOTIFY.getValue());
        } else {
            throw new IllegalArgumentException("conversationType 或 targetId 参数异常。");
        }
    }

    public void setConversationNotificationStatus(ConversationType conversationType, String targetId, final ConversationNotificationStatus notificationStatus, final NativeClient.IResultCallback<Integer> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && notificationStatus != null && !TextUtils.isEmpty(targetId) && callback != null) {
            this.nativeObj.SetBlockPush(targetId, conversationType.getValue(), notificationStatus == ConversationNotificationStatus.DO_NOT_DISTURB, new PublishAckListener() {
                public void operationComplete(int status, String uid, long time) {
                    if (status == 0) {
                        callback.onSuccess(notificationStatus == ConversationNotificationStatus.DO_NOT_DISTURB ? ConversationNotificationStatus.DO_NOT_DISTURB.getValue() : ConversationNotificationStatus.NOTIFY.getValue());
                    } else {
                        RLog.d("NativeClient", "setConversationNotificationStatus operationComplete: status = " + status);
                        callback.onError(status);
                    }

                }
            });
        } else {
            throw new IllegalArgumentException("conversationType, notificationStatus 或 targetId 参数异常。");
        }
    }

    public boolean syncConversationNotificationStatus(ConversationType conversationType, String targetId, ConversationNotificationStatus notificationStatus) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && notificationStatus != null && !TextUtils.isEmpty(targetId)) {
            return false;
        } else {
            throw new IllegalArgumentException("conversationType, notificationStatus 或 targetId 参数异常。");
        }
    }

    public void setDiscussionInviteStatus(String targetId, int status, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(targetId)) {
            throw new IllegalArgumentException("targetId 参数异常。");
        } else {
            this.nativeObj.SetInviteStatus(targetId, status, new PublishAckListener() {
                public void operationComplete(int statusCode, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (statusCode == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(statusCode);
                        }

                    }
                }
            });
        }
    }

    public void syncGroup(List<Group> groups, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (groups != null && groups.size() != 0) {
            String[] ids = new String[groups.size()];
            String[] names = new String[groups.size()];
            int i = 0;

            Group item;
            for(Iterator var6 = groups.iterator(); var6.hasNext(); names[i++] = item.getName()) {
                item = (Group)var6.next();
                ids[i] = item.getId();
            }

            this.nativeObj.SyncGroups(ids, names, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException(" groups 参数异常。");
        }
    }

    public void joinGroup(String groupId, String groupName, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(groupName)) {
            this.nativeObj.JoinGroup(groupId, groupName, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                }
            });
        } else {
            throw new IllegalArgumentException("groupId 或 groupName不能为空。");
        }
    }

    public void quitGroup(String groupId, final NativeClient.OperationCallback callback) {
        this.nativeObj.QuitGroup(groupId, new PublishAckListener() {
            public void operationComplete(int code, String msgUId, long timestamp) {
                if (callback != null) {
                    if (code == 0) {
                        callback.onSuccess();
                    } else {
                        callback.onError(code);
                    }

                }
            }
        });
    }

    private void handleReceivedMessage(io.rong.imlib.NativeObject.Message nativeMessage, int left, boolean offline, boolean hasMsg, int cmdLeft, NativeClient.OnReceiveMessageListener listener) {
        Message message = new Message(nativeMessage);
        String objectName = message.getObjectName();
        MessageContent content = this.renderMessageContent(nativeMessage.getObjectName(), nativeMessage.getContent(), message);
        if (objectName.equals("RC:EncryptedMsg")) {
            this.handleEncryptedMessage(message, content);
        } else {
            message.setContent(content);
        }

        if (message.getContent() instanceof DiscussionNotificationMessage) {
            DiscussionNotificationMessage discussionNotificationMessage = (DiscussionNotificationMessage)message.getContent();
            if (!this.curUserId.equals(discussionNotificationMessage.getOperator()) && discussionNotificationMessage.getType() == 4) {
                String extension = discussionNotificationMessage.getExtension();
                if (!TextUtils.isEmpty(extension)) {
                    String[] targetIds = extension.split(",");
                    String[] var13 = targetIds;
                    int var14 = targetIds.length;

                    for(int var15 = 0; var15 < var14; ++var15) {
                        String targetId = var13[var15];
                        this.nativeObj.RemoveMemberFromDiscussionSync(message.getTargetId(), targetId);
                    }
                }
            } else {
                this.nativeObj.GetDiscussionInfo(message.getTargetId(), new DiscussionInfoListener() {
                    public void onReceived(DiscussionInfo info) {
                    }

                    public void OnError(int status) {
                    }
                });
            }
        }

        if (message.getContent() instanceof LogCmdMessage) {
            RtLogUploadManager.getInstance().createFullUploadTask(message);
        }

        if (listener != null) {
            listener.onReceived(message, left, offline, hasMsg, cmdLeft);
        }

    }

    public void setOnReceiveMessageListener(final NativeClient.OnReceiveMessageListener listener) {
        this.nativeObj.SetMessageListener(new ReceiveMessageListener() {
            public void onReceived(final io.rong.imlib.NativeObject.Message nativeMessage, final int left, final boolean offline, final boolean hasMsg, final int cmdLeft) {
                RLog.d("NativeClient", "onReceived onLine message:");
                NativeClient.this.receiveMessageExecutor.execute(new Runnable() {
                    public void run() {
                        NativeClient.this.handleReceivedMessage(nativeMessage, left, offline, hasMsg, cmdLeft, listener);
                    }
                });
            }

            public void onReceived(final io.rong.imlib.NativeObject.Message[] nativeMessages, final boolean hasMsg, final int totalCmd) {
                RLog.d("NativeClient", "onReceived batch of messages.");
                NativeClient.this.receiveMessageExecutor.execute(new Runnable() {
                    public void run() {
                        int total = nativeMessages.length;
                        int cmdLeft = totalCmd;

                        for(int i = 0; i < total; ++i) {
                            int left = total - i - 1;
                            if (NativeClient.this.mCmdObjectNameSet.contains(nativeMessages[i].getObjectName())) {
                                --cmdLeft;
                            }

                            NativeClient.this.handleReceivedMessage(nativeMessages[i], left, nativeMessages[i].isOffLine(), hasMsg, cmdLeft, listener);
                        }

                    }
                });
            }
        });
        this.nativeObj.SetGetSearchableWordListener(new GetSearchableWordListener() {
            public byte[] getSearchableWord(String objectName, byte[] content) {
                String filteredStr = "";
                MessageContent result = NativeClient.this.newMessageContent(objectName, content);
                if (result instanceof UnknownMessage) {
                    RLog.e("NativeClient", "result is UnknownMessage");
                } else {
                    filteredStr = NativeClient.getInstance().getSearchableWord(result);
                }

                return filteredStr.getBytes();
            }
        });
    }

    private void handleEncryptedMessage(Message message, MessageContent messageContent) {
        if (message.getSenderUserId() != null && message.getSenderUserId().equals(this.getCurrentUserId())) {
            RLog.d("NativeClient", "encryptedMsg from the same endpoint , do nothing.");
        } else if (messageContent == null) {
            RLog.e("NativeClient", "render EncryptedMsgContent returns null.");
        } else {
            RCEncryptedMessage encMsgContent = (RCEncryptedMessage)messageContent;
            String encId = encMsgContent.getRemoteEncId();
            String targetId = message.getTargetId();
            RCEncryptedSession encryptedSession = this.nativeObj.GetEncryptedConversationInfo(encId + ";;;" + targetId);
            if (encryptedSession == null) {
                RLog.w("NativeClient", "GetEncryptedConversationInfo of " + encId + ";;;" + targetId + " is null ");
            } else {
                String encKey = encryptedSession.getEncKey();
                if (TextUtils.isEmpty(encKey)) {
                    RLog.w("NativeClient", "result is null when getEncKey from encryptedSesseion of ->" + encId + ";;;" + targetId);
                } else {
                    RCSecretKey secretKey = RCDHCodecTool.fromString2RCSecretKey(encKey);
                    if (encMsgContent.getEncryptedContent() == null) {
                        RLog.e("NativeClient", "instance of RCEncryptedMessage getEncryptedContent() returns null.");
                    } else if (secretKey == null) {
                        RLog.e("NativeClient", "secretKey is null.");
                    } else {
                        byte[] data2Dec = Base64.decode(encMsgContent.getEncryptedContent(), 2);
                        byte[] decryptContent = RCDHCodecTool.decrypt(data2Dec, secretKey);
                        MessageContent originalMsgContent = this.renderMessageContent(encMsgContent.getOriginalObjName(), decryptContent, message);
                        message.setContent(originalMsgContent);
                        message.setObjectName(encMsgContent.getOriginalObjName());
                        message.setTargetId(encId + ";;;" + targetId);
                        message.setConversationType(ConversationType.ENCRYPTED);
                        message.setMessageId(message.getMessageId());
                        MessageTag originMsgTag = (MessageTag)originalMsgContent.getClass().getAnnotation(MessageTag.class);
                        if ((originMsgTag.flag() & 1) == 1) {
                            boolean isCount = (originMsgTag.flag() & 3) == 3;
                            int markUnread = isCount ? 0 : 1;
                            int messageId = (int)this.nativeObj.SaveMessage(message.getTargetId(), message.getConversationType().getValue(), message.getObjectName(), message.getSenderUserId(), decryptContent, true, 0, SentStatus.RECEIVED.getValue(), message.getSentTime(), this.getSearchableWord(originalMsgContent), markUnread, message.getUId());
                            message.setMessageId(messageId);
                        }

                    }
                }
            }
        }
    }

    public void setConnectionStatusListener(NativeClient.ICodeListener listener) {
        ConnectionService.getInstance().setMainProgressConnectionStatusListener(listener);
    }

    public long getDeltaTime() {
        return this.nativeObj.GetDeltaTime();
    }

    void queryChatRoomInfo(final String id, int count, int order, final NativeClient.IResultCallback<ChatRoomInfo> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException("聊天室 Id 参数异常。");
        } else {
            this.nativeObj.QueryChatroomInfo(id, count, order, new ChatroomInfoListener() {
                public void OnSuccess(int members, UserInfo[] users) {
                    List<ChatRoomMemberInfo> list = new ArrayList();
                    if (users != null) {
                        UserInfo[] var4 = users;
                        int var5 = users.length;

                        for(int var6 = 0; var6 < var5; ++var6) {
                            UserInfo user = var4[var6];
                            ChatRoomMemberInfo info = new ChatRoomMemberInfo();
                            info.setUserId(user.getUserId());
                            info.setJoinTime(user.getJoinTime());
                            list.add(info);
                        }
                    }

                    ChatRoomInfo infox = new ChatRoomInfo();
                    infox.setChatRoomId(id);
                    infox.setTotalMemberCount(members);
                    infox.setMemberInfo(list);
                    callback.onSuccess(infox);
                }

                public void OnError(int status) {
                    callback.onError(status);
                }
            });
        }
    }

    public void joinChatRoom(String id, int defMessageCount, final NativeClient.OperationCallback callback) {
        RLog.d("NativeClient", "joinChatRoom id: " + id + ", msgCount : " + defMessageCount);
        this.chatRoomStatusMap.put(id, false);
        this.nativeObj.JoinChatRoom(id, ConversationType.CHATROOM.getValue(), defMessageCount, false, new PublishAckListener() {
            public void operationComplete(int code, String msgUId, long timestamp) {
                if (code == 0) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else if (callback != null) {
                    callback.onError(code);
                }

            }
        });
    }

    public void reJoinChatRoom(String id, int defMessageCount, final NativeClient.OperationCallback callback) {
        RLog.d("NativeClient", "reJoinChatRoom id: " + id + ", msgCount : " + defMessageCount);
        if (this.chatRoomStatusMap.get(id) == null) {
            this.chatRoomStatusMap.put(id, false);
        }

        this.nativeObj.JoinChatRoom(id, ConversationType.CHATROOM.getValue(), defMessageCount, true, new PublishAckListener() {
            public void operationComplete(int code, String msgUId, long timestamp) {
                if (code == 0) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else if (callback != null) {
                    callback.onError(code);
                }

            }
        });
    }

    synchronized void startReplenishHeartbeat() {
        if (this.timer == null) {
            RLog.d("NativeClient", "start replenish heartbeat");
            this.timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    FwLog.write(3, 1, LogTag.L_PING_S.getTag(), "interval|enabled", new Object[]{15000, "polling"});
                    NativeClient.this.nativeObj.ping();
                }
            };
            FwLog.write(3, 1, LogTag.L_PING_S.getTag(), "interval|enabled", new Object[]{2000, true});
            this.timer.schedule(task, 2000L, 15000L);
        }

    }

    synchronized void stopReplenishHeartbeat() {
        if (this.timer != null) {
            FwLog.write(3, 1, LogTag.L_PING_S.getTag(), "interval|enabled", new Object[]{15000, false});
            this.timer.cancel();
            this.timer = null;
            RLog.d("NativeClient", "stop replenish heartbeat");
        }

    }

    public void joinExistChatRoom(String id, int defMessageCount, final NativeClient.OperationCallback callback, boolean keepMsg) {
        RLog.d("NativeClient", "joinExistChatRoom id: " + id + ", msgCount : " + defMessageCount);
        if (this.chatRoomStatusMap.get(id) == null) {
            this.chatRoomStatusMap.put(id, false);
        }

        this.nativeObj.JoinExistingChatroom(id, ConversationType.CHATROOM.getValue(), defMessageCount, new PublishAckListener() {
            public void operationComplete(int code, String msgUId, long timestamp) {
                if (code == 0) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else if (callback != null) {
                    callback.onError(code);
                }

            }
        }, keepMsg);
    }

    public void quitChatRoom(String id, final NativeClient.OperationCallback callback) {
        RLog.d("NativeClient", "quitChatRoom id: " + id);
        this.chatRoomStatusMap.remove(id);
        this.nativeObj.QuitChatRoom(id, ConversationType.CHATROOM.getValue(), new PublishAckListener() {
            public void operationComplete(int code, String msgUId, long timestamp) {
                if (code == 0) {
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else if (callback != null) {
                    callback.onError(code);
                }

            }
        });
        this.clearMessages(ConversationType.CHATROOM, id);
    }

    public boolean clearConversations(ConversationType... conversationTypes) throws IllegalAccessException {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationTypes != null && conversationTypes.length != 0) {
            int[] conversationTypeValues = new int[conversationTypes.length];
            int i = 0;
            ConversationType[] var4 = conversationTypes;
            int var5 = conversationTypes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ConversationType conversationType = var4[var6];
                conversationTypeValues[i] = conversationType.getValue();
                ++i;
            }

            return this.nativeObj.ClearConversations(conversationTypeValues);
        } else {
            throw new IllegalAccessException("ConversationTypes 参数异常。");
        }
    }

    public void addToBlacklist(String userId, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(userId) && callback != null) {
            this.nativeObj.AddToBlacklist(userId, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (code == 0) {
                        callback.onSuccess();
                    } else {
                        callback.onError(code);
                    }

                }
            });
        } else {
            throw new IllegalArgumentException("参数异常。");
        }
    }

    public void recallMessage(String objectName, byte[] content, String pushContent, int messageId, String targetId, int conversationType, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化");
        } else {
            this.nativeObj.RecallMessage(objectName, content, pushContent, (long)messageId, targetId, conversationType, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void removeFromBlacklist(String userId, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(userId) && callback != null) {
            this.nativeObj.RemoveFromBlacklist(userId, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (code == 0) {
                        callback.onSuccess();
                    } else {
                        callback.onError(code);
                    }

                }
            });
        } else {
            throw new IllegalArgumentException("用户 Id 参数异常。");
        }
    }

    public void getBlacklistStatus(String userId, final NativeClient.IResultCallback<NativeClient.BlacklistStatus> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(userId) && callback != null) {
            this.nativeObj.GetBlacklistStatus(userId, new BizAckListener() {
                public void operationComplete(int opStatus, int status) {
                    if (opStatus == 0) {
                        if (status == 0) {
                            callback.onSuccess(NativeClient.BlacklistStatus.EXIT_BLACK_LIST);
                        } else if (status == 101) {
                            callback.onSuccess(NativeClient.BlacklistStatus.NOT_EXIT_BLACK_LIST);
                        }
                    } else {
                        callback.onError(opStatus);
                    }

                }
            });
        } else {
            throw new IllegalArgumentException("用户 Id 参数异常。");
        }
    }

    public void getBlacklist(final NativeClient.IResultCallback<String> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (callback == null) {
            throw new IllegalArgumentException("参数异常。");
        } else {
            this.nativeObj.GetBlacklist(new SetBlacklistListener() {
                public void OnSuccess(String userIds) {
                    if (!TextUtils.isEmpty(userIds)) {
                        callback.onSuccess(userIds);
                    } else {
                        callback.onSuccess(null);
                    }

                }

                public void OnError(int errorCode) {
                    callback.onError(errorCode);
                }
            });
        }
    }

    public void setNotificationQuietHours(String startTime, int spanMinutes, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!TextUtils.isEmpty(startTime) && spanMinutes > 0 && spanMinutes < 1440 && callback != null) {
            Pattern pattern = Pattern.compile("^(([0-1][0-9])|2[0-3]):[0-5][0-9]:([0-5][0-9])$");
            Matcher matcher = pattern.matcher(startTime);
            if (!matcher.find()) {
                throw new IllegalArgumentException("startTime 参数异常。");
            } else {
                this.nativeObj.AddPushSetting(startTime, spanMinutes, new PublishAckListener() {
                    public void operationComplete(int code, String msgUId, long timestamp) {
                        if (0 == code) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }

                    }
                });
            }
        } else {
            throw new IllegalArgumentException("startTime, spanMinutes 或 spanMinutes 参数异常。");
        }
    }

    public void removeNotificationQuietHours(final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (callback == null) {
            throw new IllegalArgumentException("参数异常。");
        } else {
            this.nativeObj.RemovePushSetting(new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long timestamp) {
                    if (0 == code) {
                        callback.onSuccess();
                    } else {
                        callback.onError(code);
                    }

                }
            });
        }
    }

    public void getNotificationQuietHours(final NativeClient.GetNotificationQuietHoursCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (callback == null) {
            throw new IllegalArgumentException("参数异常。");
        } else {
            this.nativeObj.QueryPushSetting(new PushSettingListener() {
                public void OnError(int status) {
                    callback.onError(status);
                }

                public void OnSuccess(String startTime, int spanMins) {
                    callback.onSuccess(startTime, spanMins);
                }
            });
        }
    }

    public void setUserData(UserData userData, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (userData == null) {
            throw new IllegalArgumentException("userData 参数异常。");
        } else {
            JSONObject jsonObj = new JSONObject();

            try {
                JSONObject clientInfo;
                if (userData.getPersonalInfo() != null) {
                    clientInfo = new JSONObject();
                    clientInfo.putOpt("realName", userData.getPersonalInfo().getRealName());
                    clientInfo.putOpt("sex", userData.getPersonalInfo().getSex());
                    clientInfo.putOpt("age", userData.getPersonalInfo().getAge());
                    clientInfo.putOpt("birthday", userData.getPersonalInfo().getBirthday());
                    clientInfo.putOpt("job", userData.getPersonalInfo().getJob());
                    clientInfo.putOpt("portraitUri", userData.getPersonalInfo().getPortraitUri());
                    clientInfo.putOpt("comment", userData.getPersonalInfo().getComment());
                    jsonObj.put("personalInfo", clientInfo);
                }

                if (userData.getAccountInfo() != null) {
                    clientInfo = new JSONObject();
                    clientInfo.putOpt("appUserId", userData.getAccountInfo().getAppUserId());
                    clientInfo.putOpt("userName", userData.getAccountInfo().getUserName());
                    clientInfo.putOpt("nickName", userData.getAccountInfo().getNickName());
                    jsonObj.putOpt("accountInfo", clientInfo);
                }

                if (userData.getContactInfo() != null) {
                    clientInfo = new JSONObject();
                    clientInfo.putOpt("tel", userData.getContactInfo().getTel());
                    clientInfo.putOpt("email", userData.getContactInfo().getEmail());
                    clientInfo.putOpt("address", userData.getContactInfo().getAddress());
                    clientInfo.putOpt("qq", userData.getContactInfo().getQQ());
                    clientInfo.putOpt("weibo", userData.getContactInfo().getWeibo());
                    clientInfo.putOpt("weixin", userData.getContactInfo().getWeixin());
                    jsonObj.putOpt("contactInfo", clientInfo);
                }

                if (userData.getClientInfo() != null) {
                    clientInfo = new JSONObject();
                    clientInfo.putOpt("network", userData.getClientInfo().getNetwork());
                    clientInfo.putOpt("carrier", userData.getClientInfo().getCarrier());
                    clientInfo.putOpt("systemVersion", userData.getClientInfo().getSystemVersion());
                    clientInfo.putOpt("os", userData.getClientInfo().getOs());
                    clientInfo.putOpt("device", userData.getClientInfo().getDevice());
                    clientInfo.putOpt("mobilePhoneManufacturers", userData.getClientInfo().getMobilePhoneManufacturers());
                    jsonObj.putOpt("clientInfo", clientInfo);
                }

                jsonObj.putOpt("appVersion", userData.getAppVersion());
                jsonObj.putOpt("extra", userData.getExtra());
                String result = jsonObj.toString();
                RLog.d("NativeClient", "UserData " + result);
                this.nativeObj.SetUserData(result, new PublishAckListener() {
                    public void operationComplete(int code, String msgUId, long timestamp) {
                        if (callback != null) {
                            if (code == 0) {
                                callback.onSuccess();
                            } else {
                                callback.onError(code);
                            }
                        }

                    }
                });
            } catch (JSONException var5) {
                var5.printStackTrace();
            }

        }
    }

    private void setEnvInfo(Context context) {
        String network = "";
        String MCCMNC = "";

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager == null) {
                RLog.e("NativeClient", "connectivityManager is null");
            } else {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    network = networkInfo.getTypeName();
                }
            }

            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                MCCMNC = telephonyManager.getNetworkOperator();
            }
        } catch (SecurityException var6) {
            RLog.e("NativeClient", "setEnvInfo SecurityException", var6);
        }

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (manufacturer == null) {
            manufacturer = "";
        }

        if (model == null) {
            model = "";
        }

        this.nativeObj.SetDeviceInfo(manufacturer, model, String.valueOf(VERSION.SDK_INT), network, MCCMNC);
    }

    public Message getMessageByUid(String uid) {
        io.rong.imlib.NativeObject.Message message = this.nativeObj.GetMessageByUId(uid);
        if (message == null) {
            return null;
        } else {
            Message msg = new Message(message);
            MessageContent content = this.renderMessageContent(message.getObjectName(), message.getContent(), msg);
            msg.setContent(content);
            return msg;
        }
    }

    public boolean updateMessageReceiptStatus(String targetId, int categoryId, long timestamp) {
        return this.nativeObj.UpdateMessageReceiptStatus(targetId, categoryId, timestamp);
    }

    public boolean clearUnreadByReceipt(int conversationType, String targetId, long timestamp) {
        return this.nativeObj.ClearUnreadByReceipt(targetId, conversationType, timestamp);
    }

    public long getSendTimeByMessageId(int messageId) {
        return this.nativeObj.GetSendTimeByMessageId((long)messageId);
    }

    public boolean updateConversationInfo(ConversationType conversationType, String targetId, String title, String portrait) {
        return this.nativeObj.UpdateConversationInfo(targetId, conversationType.getValue(), title, portrait);
    }

    public void getVoIPKey(int engineType, String channelName, String extra, final NativeClient.IResultCallback<String> callback) {
        this.nativeObj.GetVoIPKey(engineType, channelName, extra, new TokenListener() {
            public void OnError(int errorCode, String token, String alternative, String date, String path) {
                if (errorCode == 0) {
                    callback.onSuccess(token);
                } else {
                    callback.onError(errorCode);
                }

            }
        });
    }

    public String getVoIPCallInfo() {
        return NavigationClient.getInstance().getVoIPCallInfo(this.mContext);
    }

    public Uri obtainMediaFileSavedUri() {
        String key = DeviceUtils.ShortMD5(2, new String[]{this.appKey, this.curUserId});
        String path = SavePathUtils.getSavePath(this.mContext.getFilesDir().getAbsolutePath());
        return Uri.parse(path + File.separator + key);
    }

    public void setServerInfo(String naviServer, String fileServer) {
        if (naviServer != null) {
            this.mNaviServer = naviServer;
            NavigationClient.getInstance().setNaviDomainList(naviServer);
        }

        if (!TextUtils.isEmpty(fileServer)) {
            this.mFileServer = NavigationClient.getInstance().formatServerAddress(fileServer, "");
            FileTransferClient.getInstance().setServiceType(ServiceType.PRIVATE_CLOUD);
        }

    }

    public void initHttpDns() {
        String naviStr = NavigationClient.getInstance().getNaviString();
        HttpDnsManager.getInstance().initHttpDns(this.mContext, this.nativeObj);
    }

    public boolean setMessageContent(int messageId, byte[] messageContent, String objectName) {
        return this.nativeObj.SetMessageContent((long)messageId, messageContent, objectName);
    }

    public String getToken() {
        return NavigationCacheHelper.getToken(this.mContext);
    }

    public int getVideoLimitTime() {
        return NavigationCacheHelper.getVideoLimitTime(this.mContext);
    }

    public boolean isPhrasesEnabled() {
        return NavigationCacheHelper.isPhraseEnabled(this.mContext);
    }

    public boolean isKvStorageEnabled() {
        return NavigationCacheHelper.isKvStorageEnabled(this.mContext);
    }

    public int getGIFLimitSize() {
        return NavigationCacheHelper.getGifSizeLimit(this.mContext);
    }

    public boolean isDnsEnabled() {
        return NavigationCacheHelper.isDnsEnabled(this.mContext);
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    private boolean isMentionedMessage(Message message) {
        if (message.getContent() instanceof TextMessage) {
            TextMessage textMessage = (TextMessage)message.getContent();
            return textMessage.getMentionedInfo() != null;
        } else {
            return false;
        }
    }

    public List<Message> getUnreadMentionedMessages(ConversationType conversationType, String targetId) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            targetId = targetId.trim();
            io.rong.imlib.NativeObject.Message[] array = this.nativeObj.GetMentionMessages(targetId, conversationType.getValue());
            List<Message> list = new ArrayList();
            if (array == null) {
                return list;
            } else {
                io.rong.imlib.NativeObject.Message[] var5 = array;
                int var6 = array.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    io.rong.imlib.NativeObject.Message item = var5[var7];
                    Message msg = new Message(item);
                    msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                    list.add(msg);
                }

                return list;
            }
        } else {
            throw new IllegalArgumentException("ConversationTypes 或 targetId 参数异常。");
        }
    }

    void setLogStatus(int level, NativeLogInfoListener listener) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.SetLogStatus(level, listener);
        }
    }

    boolean updateReadReceiptRequestInfo(String msgUId, String info) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            return this.nativeObj.UpdateReadReceiptRequestInfo(msgUId, info);
        }
    }

    void registerCmdMsgType(String objName) {
        if (TextUtils.isEmpty(objName)) {
            RLog.d("NativeClient", "registerCmdMsgType parameter error");
        } else {
            this.mCmdObjectNameSet.add(objName);
            this.nativeObj.RegisterCmdMsgType(new String[]{objName});
        }
    }

    void registerCmdMsgType(List<String> objNameList) {
        if (objNameList != null && objNameList.size() != 0) {
            this.mCmdObjectNameSet.addAll(objNameList);
            this.nativeObj.RegisterCmdMsgType((String[])objNameList.toArray(new String[0]));
        } else {
            RLog.d("NativeClient", "registerCmdMsgType list is empty");
        }
    }

    void registerDeleteMessageType(List<String> objNames) {
        if (objNames != null && objNames.size() > 0) {
            RLog.e("NativeClient", "registerDeleteMessageType size: " + objNames.size());
            this.nativeObj.RegisterDeleteMessageType((String[])objNames.toArray(new String[0]));
        }

    }

    List<Message> searchMessages(String targetId, ConversationType conversationType, String keyword, int count, long timestamp) {
        io.rong.imlib.NativeObject.Message[] messages = this.nativeObj.SearchMessages(targetId, conversationType.getValue(), keyword, count, timestamp);
        List<Message> list = new ArrayList();
        if (messages != null) {
            io.rong.imlib.NativeObject.Message[] var9 = messages;
            int var10 = messages.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                io.rong.imlib.NativeObject.Message item = var9[var11];
                Message msg = new Message(item);
                msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                list.add(msg);
            }
        }

        return list;
    }

    List<Message> searchMessagesByUser(String targetId, ConversationType conversationType, String userId, int count, long timestamp) {
        io.rong.imlib.NativeObject.Message[] messages = this.nativeObj.SearchMessagesByUser(targetId, conversationType.getValue(), userId, count, timestamp);
        List<Message> list = new ArrayList();
        if (messages != null) {
            io.rong.imlib.NativeObject.Message[] var9 = messages;
            int var10 = messages.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                io.rong.imlib.NativeObject.Message item = var9[var11];
                Message msg = new Message(item);
                msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                list.add(msg);
            }
        }

        return list;
    }

    List<SearchConversationResult> searchConversations(String keyword, int[] conversationTypes, String[] objName) {
        io.rong.imlib.NativeObject.Conversation[] conversations = this.nativeObj.SearchConversations(keyword, conversationTypes, objName);
        List<SearchConversationResult> result = new ArrayList();
        if (conversations != null) {
            io.rong.imlib.NativeObject.Conversation[] var6 = conversations;
            int var7 = conversations.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                io.rong.imlib.NativeObject.Conversation value = var6[var8];
                Conversation conversation = this.renderConversationFromNative(value);
                SearchConversationResult searchConversationResult = new SearchConversationResult();
                searchConversationResult.setConversation(conversation);
                searchConversationResult.setMatchCount(value.getMatchCount());
                result.add(searchConversationResult);
            }
        }

        return result;
    }

    List<Message> getMatchedMessages(String targetId, ConversationType conversationType, long timestamp, int before, int after) {
        io.rong.imlib.NativeObject.Message[] messages = this.nativeObj.GetMatchedMessages(targetId, conversationType.getValue(), timestamp, before, after);
        List<Message> list = new ArrayList();
        if (messages != null) {
            io.rong.imlib.NativeObject.Message[] var9 = messages;
            int var10 = messages.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                io.rong.imlib.NativeObject.Message item = var9[var11];
                Message msg = new Message(item);
                msg.setContent(this.renderMessageContent(item.getObjectName(), item.getContent(), msg));
                list.add(msg);
            }
        }

        return list;
    }

    void getVendorToken(final NativeClient.IResultCallback<String> resultCallback) {
        this.nativeObj.GetVendorToken("", new TokenListener() {
            public void OnError(int errorCode, String token, String alternative, String date, String path) {
                if (resultCallback != null) {
                    if (errorCode == 0) {
                        resultCallback.onSuccess(token);
                    } else {
                        resultCallback.onError(errorCode);
                    }
                }

            }
        });
    }

    private MessageHandler getMessageHandler(String objectName) {
        return (MessageHandler)this.messageHandlerMap.get(objectName);
    }

    private MessageContent newMessageContent(String objectName, byte[] content) {
        Constructor<? extends MessageContent> constructor = (Constructor)this.messageContentConstructorMap.get(objectName);
        if (constructor != null && content != null) {
            Object result;
            try {
                result = (MessageContent)constructor.newInstance(content);
            } catch (Exception var6) {
                result = new UnknownMessage(content);
                FwLog.write(1, 1, LogTag.L_DECODE_MSG_E.getTag(), "msg_type|stacks", new Object[]{objectName, FwLog.stackToString(var6)});
            }

            return (MessageContent)result;
        } else {
            FwLog.write(1, 1, LogTag.L_DECODE_MSG_E.getTag(), "msg_type", new Object[]{objectName});
            return new UnknownMessage(content);
        }
    }

    private void initFileTransferClient() {
        Builder builder = new Builder();
        Configuration configuration = builder.connectTimeout(30).readTimeout(60).build();
        FileTransferClient.init(configuration);
        FileTransferClient.getInstance().setContext(this.mContext);
    }

    public String getOfflineMessageDuration() {
        return this.nativeObj.GetOfflineMessageDuration();
    }

    public void setOfflineMessageDuration(String duration, final NativeClient.IResultCallback<Long> callback) {
        this.nativeObj.SetOfflineMessageDuration(duration, new SetOfflineMessageDurationListener() {
            public void onSuccess(long version) {
                if (callback != null) {
                    callback.onSuccess(version);
                }

            }

            public void onError(int code) {
                if (callback != null) {
                    callback.onError(code);
                }

            }
        });
    }

    public void switchAppKey(String appKey, String deviceId) {
        this.disconnect(false);
        this.appKey = appKey;
        this.deviceId = deviceId;
        NavigationCacheHelper.clearCache(this.mContext);
        NavigationCacheHelper.clearNaviCache(this.mContext);
        this.nativeObj.InitClient(appKey, this.mContext.getPackageName(), deviceId, this.dbPath, FileUtils.getCachePath(this.mContext, "ronglog"));
        RtLogUploadManager.getInstance().init(this.mContext, "4.0.0.1", deviceId, appKey);
        this.mFileServer = null;
    }

    public Message getTheFirstUnreadMessage(int conversationType, String targetId) {
        io.rong.imlib.NativeObject.Message nativeMsg = this.nativeObj.GetTheFirstUnreadMessage(conversationType, targetId);
        if (nativeMsg == null) {
            return null;
        } else {
            Message message = new Message(nativeMsg);
            MessageContent content = this.renderMessageContent(nativeMsg.getObjectName(), nativeMsg.getContent(), message);
            message.setContent(content);
            return message;
        }
    }

    boolean setMessageReadTime(long messageId, long timestamp) {
        return this.nativeObj.SetReadTime(messageId, timestamp);
    }

    public void setReconnectKickEnable(boolean enable) {
        ConnectionService.getInstance().setReconnectKickEnable(enable);
    }

    public void SetPushNotificationListener(NativeClient.PushNotificationListener listener) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.SetPushNotificationListener(listener);
        }
    }

    public void setConversationStatusListener(ConversationStatusListener listener) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.SetConversationStatusListener(listener);
        }
    }

    public void exitRTCRoom(final String roomId, final NativeClient.OperationCallback callback) {
        FwLog.write(3, 2, "P-leaveRoom-T", "roomId", new Object[]{roomId});
        if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-leaveRoom-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("messages 参数异常。");
        } else {
            this.nativeObj.ExitRTCRoom(roomId, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code == 0) {
                        FwLog.write(3, 2, "P-leaveRoom-R", "roomId|code", new Object[]{roomId, code});
                    } else {
                        FwLog.write(1, 2, "P-leaveRoom-E", "roomId|code", new Object[]{roomId, code});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void getRTCUsers(String roomId, int order, final NativeClient.IResultCallback<List<RTCUser>> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            throw new IllegalArgumentException("messages 参数异常。");
        } else {
            this.nativeObj.GetRTCUsers(roomId, order, new RTCUserInfoListener() {
                public void OnSuccess(RTCUser[] data, String token, String sessionId) {
                    callback.onSuccess(new ArrayList(Arrays.asList(data)));
                }

                public void OnError(int status) {
                    callback.onError(status);
                }
            });
        }
    }

    public void getRTCUserData(String roomId, int order, final NativeClient.IResultCallback<List<RTCUser>> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            throw new IllegalArgumentException("messages 参数异常。");
        } else {
            this.nativeObj.GetRTCUserData(roomId, order, new RTCUserInfoListener() {
                public void OnSuccess(RTCUser[] data, String token, String sessionId) {
                    callback.onSuccess(new ArrayList(Arrays.asList(data)));
                }

                public void OnError(int status) {
                    callback.onError(status);
                }
            });
        }
    }

    public void sendRTCPing(String roomId, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            throw new IllegalArgumentException("messages 参数异常。");
        } else {
            this.nativeObj.SendRTCPing(roomId, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code != 0) {
                        FwLog.write(1, 2, "P-RTCPing-E", "code", new Object[]{code});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public boolean useRTCOnly() {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            return this.nativeObj.UseRTCOnly();
        }
    }

    public void rtcPutInnerData(final String roomId, int type, String key, String value, String objectName, String content, final NativeClient.OperationCallback callback) {
        LogSplitUtil.write(3, "P-RTCPutInnerData-T", "roomId|type|fullValues|increValues", new Object[]{roomId, type, value, content});
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCPutInnerData-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCPutInnerData-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            this.nativeObj.RTCPutInnerDatum(roomId, type, key, value, objectName, content, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code == 0) {
                        FwLog.write(3, 2, "P-RTCPutInnerData-R", "roomId|code", new Object[]{roomId, code});
                    } else {
                        FwLog.write(1, 2, "P-RTCPutInnerData-E", "roomId|code", new Object[]{roomId, code});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void rtcPutOuterData(final String roomId, int type, String key, String value, String objectName, String content, final NativeClient.OperationCallback callback) {
        LogSplitUtil.write(3, "P-RTCPutOuterData-T", "roomId|type|fullValues|increValues", new Object[]{roomId, type, value, content});
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCPutOuterData-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCPutOuterData-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            this.nativeObj.RTCPutOuterDatum(roomId, type, key, value, objectName, content, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code == 0) {
                        FwLog.write(3, 2, "P-RTCPutOuterData-R", "roomId|code", new Object[]{roomId, 0});
                    } else {
                        FwLog.write(1, 2, "P-RTCPutOuterData-E", "roomId|code", new Object[]{roomId, code});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void rtcDeleteInnerData(final String roomId, int type, String[] keys, String objectName, String content, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCDeleteInnerData-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCDeleteInnerData-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            FwLog.write(3, 2, "P-RTCDeleteInnerData-T", "roomId|type", new Object[]{roomId, type});
            this.nativeObj.RTCDeleteInnerData(roomId, type, keys, objectName, content, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code == 0) {
                        FwLog.write(3, 2, "P-RTCDeleteInnerData-R", "roomId|code", new Object[]{roomId, 0});
                    } else {
                        FwLog.write(1, 2, "P-RTCDeleteInnerData-E", "roomId|code|desc", new Object[]{roomId, code, "Protocol Error"});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void rtcDeleteOuterData(final String roomId, int type, String[] keys, String objectName, String content, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCDeleteOuterData-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCDeleteOuterData-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            FwLog.write(3, 2, "P-RTCDeleteOuterData-T", "roomId|type", new Object[]{roomId, type});
            this.nativeObj.RTCDeleteOuterData(roomId, type, keys, objectName, content, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code == 0) {
                        FwLog.write(3, 2, "P-RTCDeleteOuterData-R", "roomId|code", new Object[]{roomId, 0});
                    } else {
                        FwLog.write(1, 2, "P-RTCDeleteOuterData-E", "roomId|code|desc", new Object[]{roomId, code, "Protocol Error"});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void rtcGetInnerData(final String roomId, int type, String[] keys, final NativeClient.IResultCallback<Map<String, String>> callback) {
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCGetInnerData-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCGetInnerData-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            FwLog.write(3, 2, "P-RTCGetInnerData-T", "roomId|type", new Object[]{roomId, type});
            this.nativeObj.RTCGetInnerData(roomId, type, keys, new RTCDataListener() {
                public void OnSuccess(Map<String, String> data) {
                    FwLog.write(3, 2, "P-RTCGetInnerData-R", "roomId|code", new Object[]{roomId, 0});
                    callback.onSuccess(data);
                }

                public void OnError(int status) {
                    FwLog.write(1, 2, "P-RTCGetInnerData-E", "roomId|code|desc", new Object[]{roomId, status, "Protocol Error"});
                    callback.onError(status);
                }
            });
        }
    }

    public void rtcGetOuterData(final String roomId, int type, String[] keys, final NativeClient.IResultCallback<Map<String, String>> callback) {
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCGetOuterData-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCGetOuterData-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            FwLog.write(3, 2, "P-RTCGetOuterData-T", "roomId|type", new Object[]{roomId, type});
            this.nativeObj.RTCGetOuterData(roomId, type, keys, new RTCDataListener() {
                public void OnSuccess(Map<String, String> data) {
                    FwLog.write(3, 2, "P-RTCGetOuterData-R", "roomId|code", new Object[]{roomId, 0});
                    callback.onSuccess(data);
                }

                public void OnError(int status) {
                    FwLog.write(1, 2, "P-RTCGetOuterData-E", "roomId|code|desc", new Object[]{roomId, status, "Protocol Error"});
                    callback.onError(status);
                }
            });
        }
    }

    public void joinRTCRoomAndGetData(final String roomId, int roomType, int broadcastType, final NativeClient.IResultCallbackEx<List<RTCUser>, String[]> callback) {
        FwLog.write(3, 2, "P-joinRoom-T", "roomId|uid|roomType|liveType", new Object[]{roomId, this.getCurrentUserId(), roomType, broadcastType});
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-joinRoom-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-joinRoom-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("messages 参数异常。");
        } else {
            this.nativeObj.JoinRTCRoomAndGetData(roomId, roomType, broadcastType, new RTCUserInfoListener() {
                public void OnSuccess(RTCUser[] data, String token, String sessionId) {
                    String remoteUserInfo = "[]";
                    if (data != null && data.length > 0) {
                        JSONArray jsonArray = new JSONArray();
                        JSONObject obj = null;
                        JSONObject dataObj = null;
                        RTCUser[] var8 = data;
                        int var9 = data.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            RTCUser user = var8[var10];
                            obj = new JSONObject();

                            try {
                                obj.put("userId", user.getUid());
                                dataObj = new JSONObject();
                                if (user.getData() != null) {
                                    Set<Entry<String, String>> entrys = user.getData().entrySet();
                                    Iterator var13 = entrys.iterator();

                                    while(var13.hasNext()) {
                                        Entry<String, String> entry = (Entry)var13.next();
                                        dataObj.put((String)entry.getKey(), entry.getValue());
                                    }
                                }

                                obj.put("streamData", dataObj);
                                jsonArray.put(obj);
                            } catch (JSONException var15) {
                                var15.printStackTrace();
                            }
                        }

                        remoteUserInfo = jsonArray.toString();
                    }

                    LogSplitUtil.write(3, "P-joinRoom-R", "roomId|code|remoteUserInfo", new Object[]{roomId, 0, remoteUserInfo});
                    callback.onSuccess(new ArrayList(Arrays.asList(data)), new String[]{token, sessionId});
                }

                public void OnError(int status) {
                    FwLog.write(1, 2, "P-joinRoom-E", "roomId|code|desc", new Object[]{roomId, status, "Protocol Error"});
                    callback.onError(status);
                }
            });
        }
    }

    public void getRTCConfig(String model, String osVersion, long timestamp, String sdkVersion, final RTCConfigListener callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.GetRTCConfig(model, osVersion, timestamp, sdkVersion, new RTCConfigListener() {
                public void onSuccess(String config, long version) {
                    callback.onSuccess(config, version);
                }

                public void onError(int status) {
                    callback.onError(status);
                }
            });
        }
    }

    void getRTCToken(String roomId, int roomType, int broadcastType, final NativeClient.IResultCallback<String> resultCallback) {
        FwLog.write(3, 2, "P-getRTCToken-T", "roomId|roomType|mediaType", new Object[]{roomId, roomType, broadcastType});
        this.nativeObj.RTCGetToken(roomId, roomType, broadcastType, new TokenListener() {
            public void OnError(int errorCode, String token, String alternative, String date, String path) {
                if (errorCode == 0) {
                    FwLog.write(3, 2, "P-getRTCToken-R", "code|token", new Object[]{0, token});
                } else {
                    FwLog.write(1, 2, "P-getRTCToken-E", "code", new Object[]{errorCode});
                }

                if (resultCallback != null) {
                    if (errorCode == 0) {
                        resultCallback.onSuccess(token);
                    } else {
                        resultCallback.onError(errorCode);
                    }
                }

            }
        });
    }

    void setRTCUserState(String roomId, String state, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.RTCSetUserState(roomId, state, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void setRTCUserData(String roomId, int type, Map<String, String> data, String objectName, String content, final NativeClient.OperationCallback callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (!(data instanceof HashMap)) {
            throw new RuntimeException("setRTCUserData data must be HashMap!");
        } else {
            this.nativeObj.RTCSetUserData(roomId, type, data, objectName, content, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void getRTCUserDatas(String roomId, String[] userIds, final NativeClient.IResultCallback<List<RTCUser>> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            throw new IllegalArgumentException("messages 参数异常。");
        } else {
            this.nativeObj.RTCGetUserData(roomId, userIds, new RTCUserInfoListener() {
                public void OnSuccess(RTCUser[] data, String token, String sessionId) {
                    callback.onSuccess(new ArrayList(Arrays.asList(data)));
                }

                public void OnError(int status) {
                    callback.onError(status);
                }
            });
        }
    }

    public void setChatRoomEntry(String key, String value, String chatroomId, Boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, final NativeClient.OperationCallback callback) {
        if (!NavigationCacheHelper.isKvStorageEnabled(this.mContext) && callback != null) {
            callback.onError(ErrorCode.KV_STORE_NOT_AVAILABLE.getValue());
        } else {
            StatusData statusData = new StatusData();
            statusData.setKey(key);
            statusData.setValue(value);
            statusData.setAutoDelete(autoDelete);
            statusData.setOverwrite(isOverWrite);
            StatusNotification statusNotification = new StatusNotification();
            statusNotification.setAttributeFlag(0);
            statusNotification.setConversationType(ConversationType.CHATROOM.getValue());
            statusNotification.setMessageContent(new String(ChatRoomKVNotiMessage.obtain(key, value, 1, notificationExtra).encode()));
            statusNotification.setNotifyAll(sendNotification);
            statusNotification.setObjectName(((MessageTag)ChatRoomKVNotiMessage.class.getAnnotation(MessageTag.class)).value());
            this.nativeObj.SetChatRoomStatus(chatroomId, statusData, statusNotification, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public void deleteChatRoomEntry(String key, String value, String chatroomId, Boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, final NativeClient.OperationCallback callback) {
        if (TextUtils.isEmpty(chatroomId)) {
            throw new IllegalArgumentException("roomId 参数异常。");
        } else if (!NavigationCacheHelper.isKvStorageEnabled(this.mContext) && callback != null) {
            callback.onError(ErrorCode.KV_STORE_NOT_AVAILABLE.getValue());
        } else {
            StatusData statusData = new StatusData();
            statusData.setKey(key);
            statusData.setValue(value);
            statusData.setAutoDelete(autoDelete);
            statusData.setOverwrite(isOverWrite);
            StatusNotification statusNotification = new StatusNotification();
            statusNotification.setAttributeFlag(0);
            statusNotification.setConversationType(ConversationType.CHATROOM.getValue());
            statusNotification.setMessageContent(new String(ChatRoomKVNotiMessage.obtain(key, value, 2, notificationExtra).encode()));
            statusNotification.setNotifyAll(sendNotification);
            statusNotification.setObjectName(((MessageTag)ChatRoomKVNotiMessage.class.getAnnotation(MessageTag.class)).value());
            this.nativeObj.DeleteChatRoomStatus(chatroomId, statusData, statusNotification, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    void getChatRoomStatusByKey(final String roomId, final String key, final NativeClient.IResultCallback<String> callback) {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (callback != null) {
            this.executorService.submit(new Runnable() {
                public void run() {
                    if (!NavigationCacheHelper.isKvStorageEnabled(NativeClient.this.mContext)) {
                        callback.onError(ErrorCode.KV_STORE_NOT_AVAILABLE.getValue());
                    } else if (NativeClient.this.chatRoomStatusMap.get(roomId) == null) {
                        callback.onError(ErrorCode.NOT_IN_CHATROOM.getValue());
                    } else if (!(Boolean)NativeClient.this.chatRoomStatusMap.get(roomId)) {
                        callback.onError(ErrorCode.KV_STORE_NOT_SYNC.getValue());
                    } else {
                        String value = NativeClient.this.nativeObj.GetChatRoomStatusByKey(roomId, key);
                        if (TextUtils.isEmpty(value)) {
                            callback.onError(ErrorCode.KEY_NOT_EXIST.getValue());
                        } else {
                            callback.onSuccess(value);
                        }

                    }
                }
            });
        }
    }

    void getAllChatRoomStatus(final String roomId, final NativeClient.IResultCallback<HashMap<String, String>> callback) {
        this.executorService.submit(new Runnable() {
            public void run() {
                if (!NavigationCacheHelper.isKvStorageEnabled(NativeClient.this.mContext)) {
                    callback.onError(ErrorCode.KV_STORE_NOT_AVAILABLE.getValue());
                } else if (NativeClient.this.chatRoomStatusMap.get(roomId) == null) {
                    callback.onError(ErrorCode.NOT_IN_CHATROOM.getValue());
                } else if (!(Boolean)NativeClient.this.chatRoomStatusMap.get(roomId)) {
                    callback.onError(ErrorCode.KV_STORE_NOT_SYNC.getValue());
                } else {
                    HashMap<String, String> result = NativeClient.this.nativeObj.GetChatRoomStatus(roomId);
                    callback.onSuccess(result);
                }
            }
        });
    }

    private void setChatRoomStatusNotificationListener() {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            this.nativeObj.SetChatRoomStatusNotificationListener(new StatusNotificationListener() {
                public void OnStatusChanged(String roomId) {
                    NativeClient.this.chatRoomStatusMap.put(roomId, true);
                }
            });
        }
    }

    public String getRTCProfile() {
        if (this.nativeObj == null) {
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else {
            return this.nativeObj.GetRTCProfile();
        }
    }

    void setNaviContentUpdateListener(NaviUpdateListener listener) {
        this.naviUpdateListener = listener;
    }

    String getUploadConfigInfo() {
        return NavigationClient.getInstance().getUploadLogConfigInfo(this.mContext);
    }

    String getOffLineLogServer() {
        return NavigationCacheHelper.getOfflineLogServer(this.mContext);
    }

    public void updateVoIPCallInfo(String rtcProfile) {
        NavigationCacheHelper.updateVoIPCallInfo(this.mContext, rtcProfile);
    }

    void rtcSetUserResource(final String roomId, RTCStatusDate[] kv, String objectName, RTCStatusDate[] content, final NativeClient.OperationCallback callback) {
        LogSplitUtil.write(3, "P-RTCSetUserResource-T", "roomId", new Object[]{roomId});
        if (this.nativeObj == null) {
            FwLog.write(1, 2, "P-RTCSetUserResource-E", "roomId|code|desc", new Object[]{roomId, -1, "NativeClient is Null"});
            throw new RuntimeException("NativeClient 尚未初始化!");
        } else if (TextUtils.isEmpty(roomId)) {
            FwLog.write(1, 2, "P-RTCSetUserResource-E", "roomId|code|desc", new Object[]{roomId, -1, "RoomId is Null"});
            throw new IllegalArgumentException("roomId 参数异常。");
        } else {
            int kvSize = kv.length;
            int contentSize = content.length;
            StatusData[] statusData_kv = new StatusData[kvSize];

            StatusData statusData;
            for(int i = 0; i < kvSize; ++i) {
                statusData = new StatusData();
                statusData.setKey(kv[i].getKey());
                statusData.setValue(kv[i].getValue());
                statusData.setAutoDelete(kv[i].isAutoDelete());
                statusData.setOverwrite(kv[i].isOverwrite());
                statusData_kv[i] = statusData;
            }

            StatusData[] statusData_content = new StatusData[contentSize];

            for(int i = 0; i < contentSize; ++i) {
                statusData = new StatusData();
                statusData.setKey(content[i].getKey());
                statusData.setValue(content[i].getValue());
                statusData.setAutoDelete(content[i].isAutoDelete());
                statusData.setOverwrite(content[i].isOverwrite());
                statusData_content[i] = statusData;
            }

            this.nativeObj.RTCSetUserResource(roomId, statusData_kv, objectName, statusData_content, new PublishAckListener() {
                public void operationComplete(int code, String msgUId, long sendTime) {
                    if (code == 0) {
                        FwLog.write(3, 2, "P-RTCSetUserResource-R", "roomId|code", new Object[]{roomId, code});
                    } else {
                        FwLog.write(1, 2, "P-RTCSetUserResource-E", "roomId|code", new Object[]{roomId, code});
                    }

                    if (callback != null) {
                        if (code == 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError(code);
                        }
                    }

                }
            });
        }
    }

    public interface PushNotificationListener {
        void OnPushNotificationChanged(long var1);
    }

    public interface OnReceiveMessageListener {
        void onReceived(Message var1, int var2, boolean var3, boolean var4, int var5);
    }

    public interface GetNotificationQuietHoursCallback {
        void onSuccess(String var1, int var2);

        void onError(int var1);
    }

    public interface IDownloadMediaFileCallback<T> extends NativeClient.IDownloadMediaMessageCallback<T> {
        void onFileNameChanged(String var1);
    }

    public interface IDownloadMediaMessageCallback<T> {
        void onSuccess(T var1);

        void onProgress(int var1);

        void onCanceled();

        void onError(int var1);
    }

    public interface ISendMediaMessageCallback<T> {
        void onAttached(T var1);

        void onSuccess(T var1);

        void onProgress(T var1, int var2);

        void onError(T var1, int var2);

        void onCanceled(T var1);
    }

    public interface IResultProgressCallback<T> {
        void onSuccess(T var1);

        void onProgress(int var1);

        void onError(int var1);

        void onCanceled(int var1);
    }

    public interface ISendMessageCallback<T> {
        void onAttached(T var1);

        void onSuccess(T var1);

        void onError(T var1, int var2);
    }

    public interface IResultCallbackEx<T, K> {
        void onSuccess(T var1, K var2);

        void onError(int var1);
    }

    public interface IConnectResultCallback<T> extends NativeClient.IResultCallback<T> {
        void OnDatabaseOpened(int var1);
    }

    public interface IResultCallback<T> {
        void onSuccess(T var1);

        void onError(int var1);
    }

    public interface OperationCallback {
        void onSuccess();

        void onError(int var1);
    }

    public interface ICodeListener {
        void onChanged(int var1);
    }

    public static enum BlacklistStatus {
        EXIT_BLACK_LIST(0),
        NOT_EXIT_BLACK_LIST(1);

        private int value;

        private BlacklistStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static NativeClient.BlacklistStatus setValue(int code) {
            NativeClient.BlacklistStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                NativeClient.BlacklistStatus c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return NOT_EXIT_BLACK_LIST;
        }
    }

    private static class NativeClientHolder {
        @SuppressLint({"StaticFieldLeak"})
        private static final NativeClient client = new NativeClient();

        private NativeClientHolder() {
        }
    }
}
