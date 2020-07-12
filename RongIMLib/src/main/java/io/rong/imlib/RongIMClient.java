package io.rong.imlib;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.rong.common.FileUtils;
import io.rong.common.SystemUtils;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.IFwLogWriter;
import io.rong.common.fwlog.FwLog.ILogListener;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.common.rlog.RLog;
import io.rong.common.rlog.RLogReporter.UploadCallback;
import io.rong.imlib.CustomServiceConfig.CSEvaSolveStatus;
import io.rong.imlib.IConnectStringCallback.Stub;
import io.rong.imlib.IRongCallback.IChatRoomHistoryMessageCallback;
import io.rong.imlib.IRongCallback.IDownloadMediaFileCallback;
import io.rong.imlib.IRongCallback.IDownloadMediaMessageCallback;
import io.rong.imlib.IRongCallback.ISendMediaMessageCallback;
import io.rong.imlib.IRongCallback.ISendMediaMessageCallbackWithUploader;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.IRongCallback.MediaMessageUploader;
import io.rong.imlib.RongCommonDefine.GetMessageDirection;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.common.SavePathUtils;
import io.rong.imlib.cs.CustomServiceManager;
import io.rong.imlib.cs.CustomServiceManager.OnHumanEvaluateListener;
import io.rong.imlib.destruct.DestructionTaskManager;
import io.rong.imlib.destruct.MessageBufferPool;
import io.rong.imlib.ipc.IpcCallbackProxy;
import io.rong.imlib.ipc.RongService;
import io.rong.imlib.location.RealTimeLocationManager;
import io.rong.imlib.location.RealTimeLocationObserver;
import io.rong.imlib.location.RealTimeLocationType;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationErrorCode;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationStatus;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.ConversationStatus;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.PublicServiceProfileList;
import io.rong.imlib.model.RCEncryptedSession;
import io.rong.imlib.model.ReadReceiptInfo;
import io.rong.imlib.model.RemoteHistoryMsgOption;
import io.rong.imlib.model.RemoteModelWrap;
import io.rong.imlib.model.RongListWrap;
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.SendMessageOption;
import io.rong.imlib.model.ChatRoomInfo.ChatRoomMemberOrder;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Conversation.PublicServiceType;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.rtc.RoomUserStateMessage;
import io.rong.imlib.statistics.Statistics;
import io.rong.imlib.typingmessage.TypingMessageManager;
import io.rong.imlib.typingmessage.TypingStatus;
import io.rong.imlib.typingmessage.TypingStatusMessage;
import io.rong.message.ChatRoomKVNotiMessage;
import io.rong.message.CommandMessage;
import io.rong.message.CommandNotificationMessage;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.DestructionCmdMessage;
import io.rong.message.DiscussionNotificationMessage;
import io.rong.message.FileMessage;
import io.rong.message.GIFMessage;
import io.rong.message.GroupNotificationMessage;
import io.rong.message.HQVoiceMessage;
import io.rong.message.HandshakeMessage;
import io.rong.message.HistoryDividerMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.LocationMessage;
import io.rong.message.LogCmdMessage;
import io.rong.message.MediaMessageContent;
import io.rong.message.ProfileNotificationMessage;
import io.rong.message.PublicServiceCommandMessage;
import io.rong.message.PublicServiceMultiRichContentMessage;
import io.rong.message.PublicServiceRichContentMessage;
import io.rong.message.RCEncryptCancelMessage;
import io.rong.message.RCEncryptConfirmMessage;
import io.rong.message.RCEncryptRequestMessage;
import io.rong.message.RCEncryptResponseMessage;
import io.rong.message.RCEncryptTerminateMessage;
import io.rong.message.RCEncryptedMessage;
import io.rong.message.ReadReceiptMessage;
import io.rong.message.ReadReceiptRequestMessage;
import io.rong.message.ReadReceiptResponseMessage;
import io.rong.message.RecallCommandMessage;
import io.rong.message.RecallNotificationMessage;
import io.rong.message.ReferenceMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.SuspendMessage;
import io.rong.message.SyncReadStatusMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.message.utils.RCDHCodecTool;
import io.rong.message.utils.RCDHCodecTool.RCDHPublicKey;
import io.rong.message.utils.RCDHCodecTool.RCSecretKey;
import io.rong.push.PushManager;
import io.rong.push.RongPushClient;
import io.rong.push.rongpush.PushReceiver;
import io.rong.rtlog.upload.RtFwLogConsolePrinter;
import java.io.File;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.spec.DHParameterSpec;

public class RongIMClient {
    private static final String TAG = "RongIMClient";
    private final int CONVERSATION_NUMBER_OF_ONE_BATCH;
    private IHandler mLibHandler;
    private static Handler mHandler;
    private final List<String> mRegCache;
    private Context mContext;
    private volatile String mToken;
    private String mAppKey;
    private String mCurrentUserId;
    private RongIMClient.StatusListener mStatusListener;
    private ConnectChangeReceiver mConnectChangeReceiver;
    private RongIMClient.ConnectRunnable mConnectRunnable;
    private String mSavePath;
    private RongIMClient.ConnectionStatusListener.ConnectionStatus mConnectionStatus;
    private static RongIMClient.ConnectionStatusListener sConnectionListener;
    private static RongIMClient.OnReceiveMessageListener sReceiveMessageListener;
    private static RongIMClient.ReadReceiptListener sReadReceiptListener;
    private static RongIMClient.OnRecallMessageListener sOnRecallMessageListener;
    private RongIMClient.SyncConversationReadStatusListener mSyncConversationReadStatusListener;
    private RongIMClient.OnReceiveDestructionMessageListener mOnReceiveDestructionMessageListener;
    private Handler mWorkHandler;
    private Handler mUpStreamHandler;
    private RongIMClient.AidlConnection mAidlConnection;
    private static String URL_STATISTIC = "https://stats.cn.ronghub.com/active.json";
    private static String PRIVATE_STATISTIC;
    private static boolean isPushEnabled;
    private Set<String> mCmdObjectNameList;
    private Set<String> mDeleteObjectNameList;
    private RongIMClient.ConnectCallback connectCallback;
    private static boolean userPolicy;
    private IMLibExtensionModuleManager imLibExtensionModuleManager;
    private RongIMClient.EncryptedSessionConnectionListener mEncSessionConListener;
    private Activity topForegroundActivity;
    private boolean kickReconnectDevice;
    private static boolean isInForeground;
    private static volatile boolean needCallBackDBOpen;
    private Timer connectTimeoutTimer;
    private static RongIMClient.ConversationStatusListener sConversationStatusListener;
    private static AtomicReference<RongIMClient.ChatRoomActionListener> chatRoomActionListener = new AtomicReference();
    private HashMap<String, RongIMClient.ChatRoomCacheRunnable> mRetryCRCache;
    private HashMap<String, RongIMClient.ChatRoomCacheRunnable> mChatRoomCache;
    private ConcurrentHashMap<String, Message> signalBuff;
    private static String mNaviServer;
    private static String mFileServer;

    private RongIMClient() {
        this.CONVERSATION_NUMBER_OF_ONE_BATCH = 10;
        this.mSavePath = "";
        this.mConnectionStatus = RongIMClient.ConnectionStatusListener.ConnectionStatus.UNCONNECTED;
        this.kickReconnectDevice = false;
        this.mRetryCRCache = new HashMap();
        this.mChatRoomCache = new HashMap();
        this.signalBuff = new ConcurrentHashMap();
        RLog.i("RongIMClient", "RongIMClient");
        mHandler = new Handler(Looper.getMainLooper());
        this.mRegCache = new ArrayList();
        HandlerThread workThread = new HandlerThread("IPC_WORK");
        workThread.start();
        HandlerThread upThread = new HandlerThread("UP_WORK");
        upThread.start();
        this.mStatusListener = new RongIMClient.StatusListener();
        this.mWorkHandler = new Handler(workThread.getLooper());
        this.mUpStreamHandler = new Handler(upThread.getLooper());
        this.mConnectChangeReceiver = new ConnectChangeReceiver();
        this.mAidlConnection = new RongIMClient.AidlConnection();
        this.mCmdObjectNameList = new CopyOnWriteArraySet();
        this.mDeleteObjectNameList = new HashSet();
        this.imLibExtensionModuleManager = IMLibExtensionModuleManager.getInstance();
        IMLibRTCClient.getInstance().init(this.mWorkHandler);
    }

    public void setEncryptedSessionConnectionListener(RongIMClient.EncryptedSessionConnectionListener encryptedSessionConnectionListener) {
        this.mEncSessionConListener = encryptedSessionConnectionListener;
    }

    public static RongIMClient getInstance() {
        return RongIMClient.SingletonHolder.sInstance;
    }

    private void initBindService() {
        if (this.mLibHandler != null) {
            this.initReceiver();
        } else if (this.mContext != null && this.mAppKey != null) {
            try {
                Intent intent = new Intent(this.mContext, RongService.class);
                intent.putExtra("appKey", this.mAppKey);
                intent.putExtra("deviceId", DeviceUtils.getDeviceId(this.mContext, this.mAppKey));
                this.mContext.bindService(intent, this.mAidlConnection, Context.BIND_AUTO_CREATE);
            } catch (SecurityException var2) {
                RLog.e("RongIMClient", "initBindService SecurityException");
                RLog.e("RongIMClient", "initBindService", var2);
            }

        } else {
            RLog.d("RongIMClient", "initBindService context or appKey is null,cause by bind before init");
        }
    }

    public RongIMClient.ConnectionStatusListener.ConnectionStatus getCurrentConnectionStatus() {
        return this.mConnectionStatus;
    }

    private void registerCmdMsgType() {
        try {
            this.mCmdObjectNameList.add(((MessageTag)ReadReceiptMessage.class.getAnnotation(MessageTag.class)).value());
            this.mCmdObjectNameList.add(((MessageTag)ReadReceiptRequestMessage.class.getAnnotation(MessageTag.class)).value());
            this.mCmdObjectNameList.add(((MessageTag)ReadReceiptResponseMessage.class.getAnnotation(MessageTag.class)).value());
            this.mCmdObjectNameList.add(((MessageTag)TypingStatusMessage.class.getAnnotation(MessageTag.class)).value());
            this.mCmdObjectNameList.add(((MessageTag)RecallCommandMessage.class.getAnnotation(MessageTag.class)).value());
            this.mCmdObjectNameList.add(((MessageTag)SyncReadStatusMessage.class.getAnnotation(MessageTag.class)).value());
            this.mCmdObjectNameList.add(((MessageTag)LogCmdMessage.class.getAnnotation(MessageTag.class)).value());
            List<Class<? extends MessageContent>> messageContents = this.imLibExtensionModuleManager.getCmdMessageContentList();
            if (messageContents != null) {
                Iterator var2 = messageContents.iterator();

                while(var2.hasNext()) {
                    Class<? extends MessageContent> clazz = (Class)var2.next();
                    this.mCmdObjectNameList.add(((MessageTag)clazz.getAnnotation(MessageTag.class)).value());
                }
            }
        } catch (ArrayIndexOutOfBoundsException var4) {
            RLog.e("RongIMClient", "Exception when register command messages. e:" + var4.getMessage());
        } catch (IncompatibleClassChangeError var5) {
            RLog.e("RongIMClient", "error when register command message. error message:" + var5.getMessage());
        }

    }

    private void registerDeleteMessageType() {
        try {
            this.mDeleteObjectNameList.add(((MessageTag)ReadReceiptMessage.class.getAnnotation(MessageTag.class)).value());
            this.mDeleteObjectNameList.add(((MessageTag)SyncReadStatusMessage.class.getAnnotation(MessageTag.class)).value());
        } catch (ArrayIndexOutOfBoundsException var2) {
            RLog.e("RongIMClient", "Exception when register deleted messages. e:" + var2.getMessage());
        }

    }

    private void registerReconnectIntentFilter() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            this.mContext.registerReceiver(this.mConnectChangeReceiver, intentFilter);
        } catch (Exception var2) {
            RLog.e("RongIMClient", "registerReconnectIntentFilter failed: " + var2.getMessage());
        }

    }

    private void initStatistics(Context context, String appKey) {
        if (!Statistics.sharedInstance().isInitialized()) {
            if (!TextUtils.isEmpty(PRIVATE_STATISTIC)) {
                Statistics.sharedInstance().init(context, PRIVATE_STATISTIC, appKey, DeviceUtils.getDeviceId(context, this.mAppKey));
            } else {
                Statistics.sharedInstance().init(context, URL_STATISTIC, appKey, DeviceUtils.getDeviceId(context, this.mAppKey));
            }

            Statistics.sharedInstance().setLoggingEnabled(false);
            Statistics.sharedInstance().onStart();
        }
    }

    public static void init(Application application, String appKey) {
        init(application, appKey, true);
    }

    private void onAppBackgroundChanged(boolean inForeground) {
        if (this.mContext != null) {
            FwLog.write(3, 1, LogTag.L_APP_STATE_S.getTag(), "foreground", new Object[]{inForeground});
            isInForeground = inForeground;
            if (this.mLibHandler != null) {
                try {
                    if (this.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        this.mLibHandler.replenishPing(inForeground);
                    } else if (inForeground) {
                        this.connectServer(this.mToken, true);
                    }

                    this.mLibHandler.notifyAppBackgroundChanged(!isInForeground);
                } catch (RemoteException var3) {
                    RLog.e("RongIMClient", "onAppBackgroundChanged", var3);
                }
            } else if (inForeground) {
                RLog.e("RongIMClient", "rebind RongService: " + this.mConnectionStatus);
                this.initBindService();
            }

        }
    }

    private void initSDK(final Context context, String appKey) {
        String currentProcess = SystemUtils.getCurrentProcessName(context);
        String mainProcess = context.getPackageName();
        RLog.d("RongIMClient", "init : " + currentProcess + ", " + mainProcess + ", " + context);
        if (!TextUtils.isEmpty(currentProcess) && !TextUtils.isEmpty(mainProcess) && mainProcess.equals(currentProcess)) {
            if (context instanceof Application) {
                ((Application)context).registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    }

                    public void onActivityStarted(Activity activity) {
                    }

                    public void onActivityResumed(Activity activity) {
                        if (RongIMClient.this.topForegroundActivity == null) {
                            RLog.i("RongIMClient", "in Foreground");
                            RongIMClient.this.onAppBackgroundChanged(true);
                        }

                        RongIMClient.this.topForegroundActivity = activity;
                    }

                    public void onActivityPaused(Activity activity) {
                    }

                    public void onActivityStopped(Activity activity) {
                        if (RongIMClient.this.topForegroundActivity == activity) {
                            RLog.i("RongIMClient", "in Background");
                            RongIMClient.this.onAppBackgroundChanged(false);
                            RongIMClient.this.topForegroundActivity = null;
                        }

                    }

                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                        RLog.i("RongIMClient", "saveInstance");
                    }

                    public void onActivityDestroyed(Activity activity) {
                    }
                });
            }

            this.mContext = context.getApplicationContext();
            if (TextUtils.isEmpty(appKey)) {
                try {
                    ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                    this.mAppKey = applicationInfo.metaData.getString("RONG_CLOUD_APP_KEY");
                    if (TextUtils.isEmpty(this.mAppKey) || !SystemUtils.isValidAppKey(this.mAppKey)) {
                        throw new IllegalArgumentException("can't find RONG_CLOUD_APP_KEY in AndroidManifest.xml.");
                    }
                } catch (NameNotFoundException var6) {
                    RLog.e("RongIMClient", "*initSDK", var6);
                    throw new ExceptionInInitializerError("can't find packageName!");
                }
            } else {
                this.mAppKey = appKey;
            }

            RLog.init(context, this.mAppKey, "4.0.0.1");
            FwLog.setLogConsolePrinter(new RtFwLogConsolePrinter());
            FwLog.write(3, 1, LogTag.A_INIT_O.getTag(), "appkey|platform|model|sdk", new Object[]{this.mAppKey, "Android-" + VERSION.SDK_INT, Build.MODEL, "4.0.0.1"});
            MessageBufferPool.init(this.mContext);
            this.imLibExtensionModuleManager.loadAllIMLibExtensionModules();
            this.imLibExtensionModuleManager.onCreate(context, appKey);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    SharedPreferences sp = context.getSharedPreferences("Statistics", 0);
                    sp.edit().putString("appKey", RongIMClient.this.mAppKey).commit();

                    try {
                        RongIMClient.registerMessageType(TextMessage.class);
                        RongIMClient.registerMessageType(ReferenceMessage.class);
                        RongIMClient.registerMessageType(VoiceMessage.class);
                        RongIMClient.registerMessageType(HQVoiceMessage.class);
                        RongIMClient.registerMessageType(ImageMessage.class);
                        RongIMClient.registerMessageType(GIFMessage.class);
                        RongIMClient.registerMessageType(LocationMessage.class);
                        RongIMClient.registerMessageType(CommandNotificationMessage.class);
                        RongIMClient.registerMessageType(ContactNotificationMessage.class);
                        RongIMClient.registerMessageType(RichContentMessage.class);
                        RongIMClient.registerMessageType(PublicServiceMultiRichContentMessage.class);
                        RongIMClient.registerMessageType(PublicServiceRichContentMessage.class);
                        RongIMClient.registerMessageType(PublicServiceCommandMessage.class);
                        RongIMClient.registerMessageType(ProfileNotificationMessage.class);
                        RongIMClient.registerMessageType(HandshakeMessage.class);
                        RongIMClient.registerMessageType(InformationNotificationMessage.class);
                        RongIMClient.registerMessageType(DiscussionNotificationMessage.class);
                        RongIMClient.registerMessageType(SuspendMessage.class);
                        RongIMClient.registerMessageType(ReadReceiptMessage.class);
                        RongIMClient.registerMessageType(CommandMessage.class);
                        RongIMClient.registerMessageType(TypingStatusMessage.class);
                        RongIMClient.registerMessageType(RecallCommandMessage.class);
                        RongIMClient.registerMessageType(RecallNotificationMessage.class);
                        RongIMClient.registerMessageType(ReadReceiptRequestMessage.class);
                        RongIMClient.registerMessageType(ReadReceiptResponseMessage.class);
                        RongIMClient.registerMessageType(SyncReadStatusMessage.class);
                        RongIMClient.registerMessageType(GroupNotificationMessage.class);
                        RongIMClient.registerMessageType(FileMessage.class);
                        RongIMClient.registerMessageType(HistoryDividerMessage.class);
                        RongIMClient.registerMessageType(RCEncryptedMessage.class);
                        RongIMClient.registerMessageType(RCEncryptRequestMessage.class);
                        RongIMClient.registerMessageType(RCEncryptResponseMessage.class);
                        RongIMClient.registerMessageType(RCEncryptCancelMessage.class);
                        RongIMClient.registerMessageType(RCEncryptConfirmMessage.class);
                        RongIMClient.registerMessageType(RCEncryptTerminateMessage.class);
                        RongIMClient.registerMessageType(DestructionCmdMessage.class);
                        RongIMClient.registerMessageType(RoomUserStateMessage.class);
                        RongIMClient.registerMessageType(ChatRoomKVNotiMessage.class);
                        RongIMClient.registerMessageType(LogCmdMessage.class);
                    } catch (AnnotationNotFoundException var7) {
                        RLog.e("RongIMClient", "#initSDK", var7);
                    }

                    List<Class<? extends MessageContent>> messageContents = RongIMClient.this.imLibExtensionModuleManager.getMessageContentList();
                    if (messageContents != null) {
                        Iterator var3 = messageContents.iterator();

                        while(var3.hasNext()) {
                            Class clazz = (Class)var3.next();

                            try {
                                RongIMClient.registerMessageType(clazz);
                            } catch (AnnotationNotFoundException var6) {
                                RLog.e("RongIMClient", "%initSDK", var6);
                            }
                        }
                    }

                    RongIMClient.this.registerCmdMsgType();
                    RongIMClient.this.registerDeleteMessageType();
                    RongIMClient.this.registerReconnectIntentFilter();
                    RongIMClient.this.initBindService();
                    RongIMClient.this.initStatistics(context, RongIMClient.this.mAppKey);
                    if (RongIMClient.isPushEnabled) {
                        if (RongIMClient.mNaviServer != null) {
                            RongPushClient.init(context, RongIMClient.this.mAppKey, RongIMClient.mNaviServer);
                        } else {
                            RongPushClient.init(context, RongIMClient.this.mAppKey);
                        }
                    } else {
                        ComponentName componentName = new ComponentName(context, PushReceiver.class);
                        context.getPackageManager().setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    }

                    TypingMessageManager.getInstance().init(context);
                }
            });
        } else {
            RLog.e("RongIMClient", "SDK should init in main process.");
        }
    }

    public static void init(Context context) {
        init(context, (String)null, true);
    }

    public static void init(Context context, String appKey) {
        init(context, appKey, true);
    }

    public static void init(Context context, String appKey, boolean enablePush) {
        RLog.d("RongIMClient", "isPushEnabled:" + enablePush);
        isPushEnabled = enablePush;
        RongIMClient.SingletonHolder.sInstance.initSDK(context, appKey);
    }

    public static RongIMClient connect(String token, RongIMClient.ConnectCallback connectCallback) {
        return connect(token, -1, connectCallback);
    }

    public static RongIMClient connect(String token, int timeLimit, final RongIMClient.ConnectCallback connectCallback) {
        FwLog.write(3, 1, "A-connect-T", "token", new Object[]{token});
        RongIMClient.ConnectionErrorCode errorCode = null;
        if (TextUtils.isEmpty(token)) {
            errorCode = RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT;
        } else if (RongIMClient.SingletonHolder.sInstance.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING) || RongIMClient.SingletonHolder.sInstance.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.SUSPEND) || RongIMClient.SingletonHolder.sInstance.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) || RongIMClient.SingletonHolder.sInstance.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE)) {
            errorCode = RongIMClient.ConnectionErrorCode.RC_CONNECTION_EXIST;
        }

        if (errorCode != null) {
            FwLog.write(3, 1, "A-connect-R", "code", new Object[]{errorCode});
            if (connectCallback != null) {
                connectCallback.onFail(errorCode);
            }

            return RongIMClient.SingletonHolder.sInstance;
        } else {
            needCallBackDBOpen = true;
            RongIMClient.SingletonHolder.sInstance.connectCallback = new RongIMClient.ConnectCallback() {
                public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
                    FwLog.write(3, 1, "A-connect-S", "code", new Object[]{code.getValue()});
                    RLog.i("RongIMClient", "DatabaseOpenStatus = " + code.getValue());
                    if (connectCallback != null) {
                        connectCallback.onDatabaseOpened(code);
                    }

                }

                public void onSuccess(String userId) {
                    FwLog.write(3, 1, "A-connect-R", "code|user_id", new Object[]{0, userId});
                    RongIMClient.SingletonHolder.sInstance.stopConnectTimeoutTimer();
                    if (connectCallback != null) {
                        connectCallback.onSuccess(userId);
                    }

                }

                public void onError(RongIMClient.ConnectionErrorCode code) {
                    FwLog.write(1, 1, "A-connect-R", "code", new Object[]{code.code});
                    if (connectCallback != null) {
                        connectCallback.onError(code);
                    }

                }
            };
            int interval;
            if (timeLimit <= 0) {
                interval = 2147483646;
            } else {
                interval = timeLimit;
            }

            RongIMClient.SingletonHolder.sInstance.startConnectTimeoutTimer(interval);
            RongIMClient.SingletonHolder.sInstance.connectServer(token, false);
            return RongIMClient.SingletonHolder.sInstance;
        }
    }

    public static void setConnectionStatusListener(RongIMClient.ConnectionStatusListener listener) {
        sConnectionListener = listener;
    }

    static void reconnectServer() {
        if (RongIMClient.SingletonHolder.sInstance.mLibHandler != null) {
            RLog.d("RongIMClient", "reconnectServer, t = " + RongIMClient.SingletonHolder.sInstance.mToken);
            RongIMClient.SingletonHolder.sInstance.connectServer(RongIMClient.SingletonHolder.sInstance.mToken, true);
        } else {
            mHandler.post(new Runnable() {
                public void run() {
                    RongIMClient.SingletonHolder.sInstance.connectServer(RongIMClient.SingletonHolder.sInstance.mToken, true);
                }
            });
        }

    }

    private synchronized void connectServer(final String token, boolean isReconnect) {
        if (!TextUtils.isEmpty(token)) {
            this.setToken(token);
            if (this.mLibHandler == null) {
                FwLog.write(3, 1, isReconnect ? "L-reconnect-T" : "L-connect-T", "sequences", new Object[]{0});
                FwLog.write(1, 1, isReconnect ? "L-reconnect-R" : "L-connect-R", "code|network|sequences", new Object[]{RongIMClient.ErrorCode.IPC_DISCONNECT, DeviceUtils.getNetworkType(this.mContext), 0});
                this.mConnectRunnable = new RongIMClient.ConnectRunnable(token);
                this.initBindService();
            } else {
                try {
                    RLog.d("RongIMClient", "[connect] connect");
                    this.mLibHandler.connect(token, isReconnect, isInForeground, new Stub() {
                        public void onComplete(String userId) {
                            RLog.d("RongIMClient", "[connect] callback onComplete");
                            RongIMClient.this.mStatusListener.onConnectionStatusChange(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED);
                            RongIMClient.SingletonHolder.sInstance.imLibExtensionModuleManager.onConnected(userId, token);
                            RongIMClient.this.mCurrentUserId = userId;
                            if (RongIMClient.this.topForegroundActivity != null) {
                                RLog.i("RongIMClient", "connect success and start replenish heartbeat in foreground");
                                RongIMClient.this.onAppBackgroundChanged(true);
                            }

                            SharedPreferences sp = RongIMClient.this.mContext.getSharedPreferences("Statistics", 0);
                            sp.edit().putString("token", token).putString("userId", userId).commit();
                            RongIMClient.this.mConnectRunnable = null;
                            if (RongIMClient.this.connectCallback != null) {
                                RongIMClient.this.connectCallback.onCallback(userId);
                                RongIMClient.this.connectCallback = null;
                            }

                            if (RongIMClient.isPushEnabled) {
                                PushManager.getInstance().updatePushServerInfoFromToken(token);
                            }

                            if (RongIMClient.isPushEnabled) {
                                PushManager.getInstance().updatePushServerInfoFromToken(token);
                            }

                            if (RongIMClient.isPushEnabled) {
                                PushManager.getInstance().updatePushServerInfoFromToken(token);
                            }

                        }

                        public void onFailure(int errorCode) throws RemoteException {
                            RLog.e("RongIMClient", "[connect] callback onFailure, errorCode = " + errorCode);
                            RongIMClient.this.mConnectRunnable = null;
                            if (RongIMClient.this.connectCallback != null) {
                                RongIMClient.this.connectCallback.onFail(RongIMClient.ConnectionErrorCode.valueOf(errorCode));
                                RongIMClient.this.connectCallback = null;
                            }

                            SharedPreferences sp = RongIMClient.this.mContext.getSharedPreferences("Statistics", 0);
                            sp.edit().putString("token", token).commit();
                        }

                        public void OnDatabaseOpened(int code) throws RemoteException {
                            if (RongIMClient.needCallBackDBOpen) {
                                if (RongIMClient.this.connectCallback != null) {
                                    RongIMClient.this.connectCallback.onDatabaseOpened(RongIMClient.DatabaseOpenStatus.valueOf(code));
                                }

                                RongIMClient.needCallBackDBOpen = false;
                            }

                        }
                    });
                } catch (RemoteException var5) {
                    if (this.connectCallback != null) {
                        this.connectCallback.onError(RongIMClient.ConnectionErrorCode.IPC_DISCONNECT);
                    }

                    FwLog.write(1, 1, "L-crash_main_ept-F", "stacks", new Object[]{FwLog.stackToString(var5)});
                    RLog.e("RongIMClient", "connectServer", var5);
                }
            }

        }
    }

    private void startConnectTimeoutTimer(int interval) {
        this.stopConnectTimeoutTimer();
        this.connectTimeoutTimer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (RongIMClient.this.mLibHandler != null) {
                    try {
                        RongIMClient.this.mLibHandler.setIpcConnectTimeOut();
                    } catch (Exception var2) {
                        RongIMClient.this.mStatusListener.onConnectionStatusChange(RongIMClient.ConnectionStatusListener.ConnectionStatus.TIMEOUT);
                    }
                } else {
                    RongIMClient.this.mStatusListener.onConnectionStatusChange(RongIMClient.ConnectionStatusListener.ConnectionStatus.TIMEOUT);
                }

            }
        };
        this.connectTimeoutTimer.schedule(task, (long)interval * 1000L);
    }

    private void stopConnectTimeoutTimer() {
        if (this.connectTimeoutTimer != null) {
            this.connectTimeoutTimer.cancel();
            this.connectTimeoutTimer = null;
        }

    }

    public static void setOnReceiveMessageListener(RongIMClient.OnReceiveMessageListener listener) {
        sReceiveMessageListener = listener;
    }

    /** @deprecated */
    @Deprecated
    public static void registerMessageType(Class<? extends MessageContent> messageContentClass) throws AnnotationNotFoundException {
        if (messageContentClass == null) {
            throw new IllegalArgumentException("MessageContent 为空！");
        } else {
            synchronized(RongIMClient.SingletonHolder.sInstance.mRegCache) {
                if (!RongIMClient.SingletonHolder.sInstance.mRegCache.contains(messageContentClass.getName())) {
                    RongIMClient.SingletonHolder.sInstance.mRegCache.add(messageContentClass.getName());
                }
            }

            if (RongIMClient.SingletonHolder.sInstance.mLibHandler != null) {
                try {
                    RongIMClient.SingletonHolder.sInstance.mLibHandler.registerMessageType(messageContentClass.getName());
                } catch (RemoteException var3) {
                    RLog.e("RongIMClient", "registerMessageType RemoteException", var3);
                }
            }

        }
    }

    public static void registerMessageType(List<Class<? extends MessageContent>> messageContentClassList) throws AnnotationNotFoundException {
        if (messageContentClassList != null && messageContentClassList.size() != 0) {
            List<String> classNameList = new ArrayList();
            synchronized(RongIMClient.SingletonHolder.sInstance.mRegCache) {
                Iterator var3 = messageContentClassList.iterator();

                while(var3.hasNext()) {
                    Class<? extends MessageContent> messageContentClass = (Class)var3.next();
                    String className = messageContentClass.getName();
                    if (!RongIMClient.SingletonHolder.sInstance.mRegCache.contains(className)) {
                        RongIMClient.SingletonHolder.sInstance.mRegCache.add(className);
                        classNameList.add(className);
                    }
                }
            }

            if (RongIMClient.SingletonHolder.sInstance.mLibHandler != null) {
                try {
                    if (classNameList.size() > 0) {
                        RongIMClient.SingletonHolder.sInstance.mLibHandler.registerMessageTypes(classNameList);
                    }
                } catch (RemoteException var7) {
                    RLog.e("RongIMClient", "registerMessageType RemoteException", var7);
                }
            }

        } else {
            throw new IllegalArgumentException("MessageContent 为空！");
        }
    }

    public Activity getTopForegroundActivity() {
        return this.topForegroundActivity;
    }

    public void logout() {
        this.disconnect(false);
        this.imLibExtensionModuleManager.onLogout();
    }

    public void disconnect() {
        this.disconnect(true);
    }

    public void disconnect(boolean isReceivePush) {
        FwLog.write(3, 1, LogTag.A_DISCONNECT_O.getTag(), "push", new Object[]{isReceivePush});
        if (this.mContext != null && !isReceivePush) {
            SharedPreferences sp = this.mContext.getSharedPreferences("Statistics", 0);
            sp.edit().putString("userId", "").apply();
        }

        if (this.mLibHandler == null) {
            RLog.e("RongIMClient", "disconnect IPC service unbind!");
            this.mStatusListener.onConnectionStatusChange(RongIMClient.ConnectionStatusListener.ConnectionStatus.SIGN_OUT);
        } else {
            if (this.mWorkHandler != null) {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        RongIMClient.this.mChatRoomCache.clear();
                        Iterator var1 = RongIMClient.this.mRetryCRCache.values().iterator();

                        while(var1.hasNext()) {
                            RongIMClient.ChatRoomCacheRunnable runnable = (RongIMClient.ChatRoomCacheRunnable)var1.next();
                            RongIMClient.this.mWorkHandler.removeCallbacks(runnable);
                        }

                        RongIMClient.this.mRetryCRCache.clear();
                        IMLibRTCClient.getInstance().clearRTCCache();
                        RongIMClient.this.cancelAllDownloadMediaMessage((RongIMClient.OperationCallback)null);
                    }
                });
            }

            try {
                if (this.mLibHandler != null) {
                    this.mLibHandler.disconnect(isReceivePush);
                }
            } catch (RemoteException var3) {
                RLog.e("RongIMClient", "disconnect", var3);
            }

            this.clearToken();
        }
    }

    public void getConversationList(final RongIMClient.ResultCallback<List<Conversation>> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.getConversationListByBatch(10, RongIMClient.this.new GetConversationListProcessCallBackWrapper(callback));
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "getConversationList", var2);
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                }
            }
        });
    }

    public void getConversationList(final RongIMClient.ResultCallback<List<Conversation>> callback, final ConversationType... conversationTypes) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else if (conversationTypes != null && conversationTypes.length != 0) {
                    try {
                        int[] typeValues = RongIMClient.this.convertTypes(conversationTypes);
                        RongIMClient.this.mLibHandler.getConversationListOfTypesByBatch(typeValues, 10, RongIMClient.this.new GetConversationListProcessCallBackWrapper(callback));
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "getConversationList", var2);
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                } else {
                    callback.onCallback(null);
                }
            }
        });
    }

    public void getConversationListByPage(final RongIMClient.ResultCallback<List<Conversation>> callback, final long timeStamp, final int count, final ConversationType... conversationTypes) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else if (conversationTypes != null && conversationTypes.length != 0) {
                    try {
                        int[] typeValues = RongIMClient.this.convertTypes(conversationTypes);
                        List<Conversation> conversationList = RongIMClient.this.mLibHandler.getConversationListByPage(typeValues, timeStamp, count);
                        if (callback != null) {
                            callback.onCallback(conversationList);
                        }
                    } catch (RemoteException var3) {
                        RLog.e("RongIMClient", "getConversationListByPage", var3);
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                } else {
                    if (callback != null) {
                        callback.onCallback(null);
                    }

                }
            }
        });
    }

    public void getBlockedConversationList(final RongIMClient.ResultCallback<List<Conversation>> callback, final ConversationType... conversationTypes) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else if (conversationTypes != null && conversationTypes.length != 0) {
                    try {
                        int[] typeValues = RongIMClient.this.convertTypes(conversationTypes);
                        List<Conversation> conversationList = RongIMClient.this.mLibHandler.getBlockedConversationList(typeValues);
                        if (callback != null) {
                            callback.onCallback(conversationList);
                        }
                    } catch (RemoteException var3) {
                        RLog.e("RongIMClient", "getBlockedConversationList", var3);
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                } else {
                    callback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }
            }
        });
    }

    public void getConversation(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Conversation> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            Conversation conversation = RongIMClient.this.mLibHandler.getConversation(conversationType.getValue(), targetId);
                            if (callback != null) {
                                callback.onCallback(conversation);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getConversation", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "getConversation. the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void updateConversationInfo(final ConversationType conversationType, final String targetId, final String title, final String portrait, final RongIMClient.ResultCallback callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(portrait) && (!TextUtils.isEmpty(title.trim()) || !TextUtils.isEmpty(portrait.trim()))) {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                        } else {
                            try {
                                boolean result = RongIMClient.this.mLibHandler.updateConversationInfo(conversationType.getValue(), targetId, title, portrait);
                                if (callback != null) {
                                    callback.onCallback(result);
                                }
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "updateConversationInfo", var2);
                                if (callback != null) {
                                    callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            } else {
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            }
        } else {
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void removeConversation(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Boolean> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            if (targetId.length() > 64) {
                RLog.e("RongIMClient", "TargetId length exceed the limit!");
            }

            if (conversationType == ConversationType.ENCRYPTED) {
                this.quitEncryptedSession(targetId);
            }

            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.removeConversation(conversationType.getValue(), targetId);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "removeConversation", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "getConversation. the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void setConversationToTop(ConversationType conversationType, String id, boolean isTop, RongIMClient.ResultCallback<Boolean> callback) {
        this.setConversationToTop(conversationType, id, isTop, true, callback);
    }

    public void setConversationToTop(final ConversationType conversationType, final String id, final boolean isTop, final boolean needCreate, final RongIMClient.ResultCallback<Boolean> callback) {
        if (!TextUtils.isEmpty(id) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean result = RongIMClient.this.mLibHandler.setConversationTopStatus(conversationType.getValue(), id, isTop, needCreate);
                            if (callback != null) {
                                callback.onCallback(result);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setConversationToTop", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "getConversation. the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getTotalUnreadCount(final RongIMClient.ResultCallback<Integer> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else if (RongIMClient.this.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.UNCONNECTED)) {
                    RLog.d("RongIMClient", "getTotalUnreadCount return 0 as disconnected.");
                    if (callback != null) {
                        callback.onCallback(0);
                    }

                } else {
                    try {
                        int count = RongIMClient.this.mLibHandler.getTotalUnreadCount();
                        if (callback != null) {
                            callback.onCallback(count);
                        }
                    } catch (RemoteException var2) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                }
            }
        });
    }

    public void getTotalUnreadCount(final RongIMClient.ResultCallback<Integer> callback, final Conversation... conversations) {
        if (conversations == null) {
            throw new IllegalArgumentException("conversations can't be null !");
        } else {
            Conversation[] var3 = conversations;
            int var4 = conversations.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Conversation conversation = var3[var5];
                if (conversation.getConversationType() == null || TextUtils.isEmpty(conversation.getTargetId())) {
                    throw new IllegalArgumentException("ConversationType or targetId can't be empty !");
                }
            }

            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            int count = RongIMClient.this.mLibHandler.getUnreadCountByConversation(conversations);
                            if (callback != null) {
                                callback.onCallback(count);
                            }
                        } catch (RemoteException var2) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        }
    }

    public void getUnreadCount(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Integer> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            int count = RongIMClient.this.mLibHandler.getUnreadCountById(conversationType.getValue(), targetId);
                            if (callback != null) {
                                callback.onCallback(count);
                            }
                        } catch (RemoteException var2) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "TargetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getUnreadCount(final RongIMClient.ResultCallback<Integer> callback, final ConversationType... conversationTypes) {
        if (conversationTypes != null && conversationTypes.length != 0) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            int[] types = RongIMClient.this.convertTypes(conversationTypes);
                            int count = RongIMClient.this.mLibHandler.getUnreadCount(types);
                            if (callback != null) {
                                callback.onCallback(count);
                            }
                        } catch (RemoteException var3) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "conversationTypes is null. Return directly!!!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getUnreadCount(final ConversationType[] conversationTypes, final boolean containBlocked, final RongIMClient.ResultCallback<Integer> callback) {
        if (conversationTypes != null && conversationTypes.length != 0) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            int[] types = RongIMClient.this.convertTypes(conversationTypes);
                            int count = RongIMClient.this.mLibHandler.getUnreadCountWithDND(types, containBlocked);
                            if (callback != null) {
                                callback.onCallback(count);
                            }
                        } catch (RemoteException var3) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "conversationTypes is null. Return directly!!!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getUnreadCount(ConversationType[] conversationTypes, RongIMClient.ResultCallback<Integer> callback) {
        this.getUnreadCount(callback, conversationTypes);
    }

    public void getLatestMessages(final ConversationType conversationType, final String targetId, final int count, final RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            List<Message> messages = RongIMClient.this.mLibHandler.getNewestMessages(conversation, count);
                            if (callback != null) {
                                callback.onCallback(messages);
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "getLatestMessages", var3);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    @Deprecated
    public List<Message> getHistoryMessages(ConversationType conversationType, String targetId, int oldestMessageId, int count) {
        final CountDownLatch latch = new CountDownLatch(1);
        final RongIMClient.ResultCallback.Result<List<Message>> result = new RongIMClient.ResultCallback.Result();
        this.getHistoryMessages(conversationType, targetId, oldestMessageId, count, new RongIMClient.SyncCallback<List<Message>>() {
            public void onSuccess(List<Message> messages) {
                result.t = messages;
                latch.countDown();
            }

            public void onError(RongIMClient.ErrorCode e) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException var8) {
            RLog.e("RongIMClient", "getHistoryMessages", var8);
            Thread.currentThread().interrupt();
        }

        return (List)result.t;
    }

    /** @deprecated */
    @Deprecated
    public List<Message> getHistoryMessages(ConversationType conversationType, String targetId, String objectName, int oldestMessageId, int count) {
        final CountDownLatch latch = new CountDownLatch(1);
        final RongIMClient.ResultCallback.Result<List<Message>> result = new RongIMClient.ResultCallback.Result();
        this.getHistoryMessages(conversationType, targetId, objectName, oldestMessageId, count, new RongIMClient.SyncCallback<List<Message>>() {
            public void onSuccess(List<Message> messages) {
                result.t = messages;
                latch.countDown();
            }

            public void onError(RongIMClient.ErrorCode e) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException var9) {
            RLog.e("RongIMClient", "getHistoryMessages", var9);
            Thread.currentThread().interrupt();
        }

        return (List)result.t;
    }

    public void getHistoryMessages(final ConversationType conversationType, final String targetId, final String objectName, final int oldestMessageId, final int count, final RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null && !TextUtils.isEmpty(objectName)) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            List<Message> messages = RongIMClient.this.mLibHandler.getOlderMessagesByObjectName(conversation, objectName, (long)oldestMessageId, count, true);
                            if (callback != null) {
                                callback.onCallback(RongIMClient.this.filterDestructionMessage(conversationType, targetId, messages));
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "getHistoryMessages", var3);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType or objectName is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getHistoryMessages(final ConversationType conversationType, final String targetId, final String objectName, final int baseMessageId, final int count, final GetMessageDirection direction, final RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            if (!TextUtils.isEmpty(objectName) && count > 0 && direction != null) {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                        } else {
                            Conversation conversation = new Conversation();
                            conversation.setConversationType(conversationType);
                            conversation.setTargetId(targetId);

                            try {
                                List<Message> messages = RongIMClient.this.mLibHandler.getOlderMessagesByObjectName(conversation, objectName, (long)baseMessageId, count, direction.equals(GetMessageDirection.FRONT));
                                if (callback != null) {
                                    callback.onCallback(RongIMClient.this.filterDestructionMessage(conversationType, targetId, messages));
                                }
                            } catch (RemoteException var3) {
                                RLog.e("RongIMClient", "getHistoryMessages", var3);
                                if (callback != null) {
                                    callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            } else {
                RLog.e("RongIMClient", "the parameter of objectName, count or direction is error!");
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            }
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getHistoryMessages(final ConversationType conversationType, final String targetId, final List<String> objectNames, final long timestamp, final int count, final GetMessageDirection direction, final RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            if (objectNames.size() != 0 && count > 0 && direction != null) {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                        } else {
                            Conversation conversation = new Conversation();
                            conversation.setConversationType(conversationType);
                            conversation.setTargetId(targetId);

                            try {
                                List<Message> messages = RongIMClient.this.mLibHandler.getOlderMessagesByObjectNames(conversation, objectNames, timestamp, count, direction.equals(GetMessageDirection.FRONT));
                                if (callback != null) {
                                    callback.onCallback(RongIMClient.this.filterDestructionMessage(conversationType, targetId, messages));
                                }
                            } catch (RemoteException var3) {
                                RLog.e("RongIMClient", "getHistoryMessages", var3);
                                if (callback != null) {
                                    callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            } else {
                RLog.e("RongIMClient", "the parameter size of objectNames, count or direction is error!");
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            }
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    private List<Message> getHistoryMessagesByObjectNamesSync(ConversationType conversationType, String targetId, List<String> objectNames, long timestamp, int count, GetMessageDirection direction) {
        if (this.mLibHandler == null) {
            return null;
        } else {
            Conversation conversation = new Conversation();
            conversation.setConversationType(conversationType);
            conversation.setTargetId(targetId);
            List messages = null;

            try {
                messages = this.mLibHandler.getOlderMessagesByObjectNames(conversation, objectNames, timestamp, count, direction.equals(GetMessageDirection.FRONT));
            } catch (RemoteException var11) {
                RLog.e("RongIMClient", "getHistoryMessagesByObjectNamesSync", var11);
            }

            return messages;
        }
    }

    public void getRemoteHistoryMessages(final ConversationType conversationType, final String targetId, final long dateTime, final int count, RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            final IpcCallbackProxy<RongIMClient.ResultCallback<List<Message>>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            RongIMClient.this.mLibHandler.getRemoteHistoryMessages(conversation, dateTime, count, new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        if (model != null && model.getContent() != null && model.getContent() instanceof RongListWrap) {
                                            RongListWrap rongListWrap = (RongListWrap)model.getContent();
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(rongListWrap.getList());
                                        } else {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback((Object)null);
                                        }

                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "getRemoteHistoryMessages", var3);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getRemoteHistoryMessages(final ConversationType conversationType, final String targetId, final RemoteHistoryMsgOption remoteHistoryMsgOption, RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            final IpcCallbackProxy<RongIMClient.ResultCallback<List<Message>>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            RongIMClient.this.mLibHandler.getRemoteHistoryMessagesOption(conversation, remoteHistoryMsgOption, new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        if (model != null && model.getContent() != null && model.getContent() instanceof RongListWrap) {
                                            RongListWrap rongListWrap = (RongListWrap)model.getContent();
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(rongListWrap.getList());
                                        } else {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback((Object)null);
                                        }

                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "getRemoteHistoryMessages", var3);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void cleanRemoteHistoryMessages(final ConversationType conversationType, final String targetId, final long recordTime, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null && ipcCallbackProxy.callback != null) {
                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            RongIMClient.this.mLibHandler.cleanRemoteHistoryMessages(conversation, recordTime, new io.rong.imlib.IOperationCallback.Stub() {
                                public void onComplete() throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onCallback();
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "cleanRemoteHistoryMessages", var3);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void cleanHistoryMessages(final ConversationType conversationType, final String targetId, final long recordTime, final boolean cleanRemote, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null && ipcCallbackProxy.callback != null) {
                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    } else {
                        final Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);
                        if (cleanRemote) {
                            try {
                                RongIMClient.this.mLibHandler.cleanRemoteHistoryMessages(conversation, recordTime, new io.rong.imlib.IOperationCallback.Stub() {
                                    public void onComplete() throws RemoteException {
                                        RongIMClient.this.cleanLocalHistoryMessages(conversation, recordTime, ipcCallbackProxy);
                                    }

                                    public void onFailure(int errorCode) throws RemoteException {
                                        RLog.e("RongIMClient", "cleanHistoryMessages errorCode :" + errorCode);
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }
                                });
                            } catch (RemoteException var3) {
                                RLog.e("RongIMClient", "cleanHistoryMessages", var3);
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }
                            }
                        } else {
                            RongIMClient.this.cleanLocalHistoryMessages(conversation, recordTime, ipcCallbackProxy);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    private void cleanLocalHistoryMessages(Conversation conversation, long recordTime, final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy) {
        try {
            this.mLibHandler.cleanHistoryMessages(conversation, recordTime, new io.rong.imlib.IOperationCallback.Stub() {
                public void onComplete() throws RemoteException {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onCallback();
                        ipcCallbackProxy.callback = null;
                    }

                }

                public void onFailure(int errorCode) throws RemoteException {
                    RLog.e("RongIMClient", "cleanLocalHistoryMessages errorCode" + errorCode);
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                        ipcCallbackProxy.callback = null;
                    }

                }
            });
        } catch (RemoteException var6) {
            RLog.e("RongIMClient", "cleanLocalHistoryMessages", var6);
            if (ipcCallbackProxy.callback != null) {
                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                ipcCallbackProxy.callback = null;
            }
        }

    }

    public void getChatroomHistoryMessages(final String targetId, final long recordTime, final int count, final RongIMClient.TimestampOrder order, IChatRoomHistoryMessageCallback callback) {
        if (!TextUtils.isEmpty(targetId) && order != null) {
            final IpcCallbackProxy<IChatRoomHistoryMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            RongIMClient.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    ((IChatRoomHistoryMessageCallback)ipcCallbackProxy.callback).onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }
                            });
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getChatroomHistoryMessages(targetId, recordTime, count, order.ordinal(), new io.rong.imlib.IChatRoomHistoryMessageCallback.Stub() {
                                public void onComplete(final RemoteModelWrap model, final long syncTime) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                if (model != null) {
                                                    RongListWrap rongListWrap = (RongListWrap)model.getContent();
                                                    ((IChatRoomHistoryMessageCallback)ipcCallbackProxy.callback).onSuccess(rongListWrap.getList(), syncTime);
                                                } else {
                                                    ((IChatRoomHistoryMessageCallback)ipcCallbackProxy.callback).onSuccess((List)null, syncTime);
                                                }

                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }

                                public void onFailure(final int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IChatRoomHistoryMessageCallback)ipcCallbackProxy.callback).onError(RongIMClient.ErrorCode.valueOf(errorCode));
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getChatroomHistoryMessages", var2);
                            if (ipcCallbackProxy.callback != null) {
                                RongIMClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((IChatRoomHistoryMessageCallback)ipcCallbackProxy.callback).onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }
                                });
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or order is null !");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getHistoryMessages(final ConversationType conversationType, final String targetId, final int oldestMessageId, final int count, final RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            List<Message> messages = RongIMClient.this.mLibHandler.getOlderMessages(conversation, (long)oldestMessageId, count);
                            if (callback != null) {
                                callback.onCallback(RongIMClient.this.filterDestructionMessage(conversationType, targetId, messages));
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "getHistoryMessages", var3);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    private List<Message> getHistoryMessagesSync(ConversationType conversationType, String targetId, int oldestMessageId, int count) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            if (this.mLibHandler == null) {
                return null;
            } else {
                Conversation conversation = new Conversation();
                conversation.setConversationType(conversationType);
                conversation.setTargetId(targetId);
                List messages = null;

                try {
                    messages = this.mLibHandler.getOlderMessages(conversation, (long)oldestMessageId, count);
                } catch (RemoteException var8) {
                    RLog.e("RongIMClient", "getHistoryMessagesSync ", var8);
                }

                return messages;
            }
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            return null;
        }
    }

    public void deleteMessages(final int[] messageIds, final RongIMClient.ResultCallback<Boolean> callback) {
        if (messageIds != null && messageIds.length != 0) {
            StringBuilder idString = new StringBuilder();
            int[] var4 = messageIds;
            int var5 = messageIds.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                int id = var4[var6];
                if (id <= 0) {
                    RLog.e("RongIMClient", "the messageIds contains 0 value!");
                    if (callback != null) {
                        callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                    }

                    return;
                }

                idString.append(id).append("/");
            }

            FwLog.write(4, 1, LogTag.A_DELETE_MESSAGES_S.getTag(), "messageIds:", new Object[]{idString.toString()});
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.deleteMessage(messageIds);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "deleteMessages", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the messageIds is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void deleteMessages(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Boolean> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.deleteConversationMessage(conversationType.getValue(), targetId);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "deleteMessages", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void deleteRemoteMessages(final ConversationType conversationType, final String targetId, final Message[] messages, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null && !conversationType.equals(ConversationType.CHATROOM)) {
            if (messages != null && messages.length != 0 && messages.length <= 100) {
                final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.deleteMessages(conversationType.getValue(), targetId, messages, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "deleteMessages", var2);
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }
                            }

                        }
                    }
                });
            } else {
                RLog.e("RongIMClient", "the messages size is error!");
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            }
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void clearMessages(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Boolean> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            boolean bool = RongIMClient.this.mLibHandler.clearMessages(conversation);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "clearMessages", var3);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void clearMessagesUnreadStatus(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Boolean> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        Conversation conversation = new Conversation();
                        conversation.setConversationType(conversationType);
                        conversation.setTargetId(targetId);

                        try {
                            boolean bool = RongIMClient.this.mLibHandler.clearMessagesUnreadStatus(conversation);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "clearMessagesUnreadStatus", var3);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void setMessageExtra(final int messageId, final String value, final RongIMClient.ResultCallback<Boolean> callback) {
        if (messageId == 0) {
            RLog.e("RongIMClient", "messageId is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.setMessageExtra(messageId, value);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setMessageExtra", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        }
    }

    public void setMessageReceivedStatus(final int messageId, final ReceivedStatus receivedStatus, final RongIMClient.ResultCallback<Boolean> callback) {
        if (messageId != 0 && receivedStatus != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.setMessageReceivedStatus(messageId, receivedStatus.getFlag());
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setMessageReceivedStatus", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "Error.messageid is 0 or receivedStatus is null !");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void setMessageSentStatus(final Message message, final RongIMClient.ResultCallback<Boolean> callback) {
        if (message != null && message.getMessageId() > 0 && message.getSentStatus() != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.setMessageSentStatus(message.getMessageId(), message.getSentStatus().getValue());
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setMessageSentStatus", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "setMessageSentStatus Error. message or the sentStatus of message cant't be null , messageId can't <= 0!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getTextMessageDraft(ConversationType conversationType, String targetId, final RongIMClient.ResultCallback<String> callback) {
        final Conversation conversation = new Conversation();
        conversation.setConversationType(conversationType);
        conversation.setTargetId(targetId);
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            String content = RongIMClient.this.mLibHandler.getTextMessageDraft(conversation);
                            if (callback != null) {
                                callback.onCallback(content);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getTextMessageDraft", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the value of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void saveTextMessageDraft(ConversationType conversationType, String targetId, final String content, final RongIMClient.ResultCallback<Boolean> callback) {
        final Conversation conversation = new Conversation();
        conversation.setConversationType(conversationType);
        conversation.setTargetId(targetId);
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.saveTextMessageDraft(conversation, content);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "saveTextMessageDraft", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the value of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void clearTextMessageDraft(ConversationType conversationType, String targetId, final RongIMClient.ResultCallback<Boolean> callback) {
        final Conversation conversation = new Conversation();
        conversation.setConversationType(conversationType);
        conversation.setTargetId(targetId);
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.clearTextMessageDraft(conversation);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "clearTextMessageDraft", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the value of targetId or ConversationType is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    public void getDiscussion(final String discussionId, RongIMClient.ResultCallback<Discussion> callback) {
        if (TextUtils.isEmpty(discussionId)) {
            RLog.e("RongIMClient", "the discussionId can't be empty!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.ResultCallback<Discussion>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getDiscussion(discussionId, new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        if (model != null && model.getContent() != null && model.getContent() instanceof Discussion) {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback((Discussion)model.getContent());
                                        } else {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback((Object)null);
                                        }

                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getDiscussion", var2);
                        }

                    }
                }
            });
        }
    }

    public static void setChatRoomActionListener(RongIMClient.ChatRoomActionListener listener) {
        chatRoomActionListener.set(listener);
    }

    /** @deprecated */
    public void setDiscussionName(final String discussionId, final String name, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(discussionId) && !TextUtils.isEmpty(name)) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            String sub = name;
                            if (!TextUtils.isEmpty(name) && name.length() > 40) {
                                sub = name.substring(0, 39);
                            }

                            RongIMClient.this.mLibHandler.setDiscussionName(discussionId, sub, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setDiscussionName", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "discussionId or name is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    public void createDiscussion(final String name, final List<String> userIdList, RongIMClient.CreateDiscussionCallback callback) {
        if (!TextUtils.isEmpty(name) && userIdList != null && userIdList.size() != 0) {
            final IpcCallbackProxy<RongIMClient.CreateDiscussionCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.CreateDiscussionCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            String sub = name;
                            if (!TextUtils.isEmpty(name) && name.length() > 40) {
                                sub = name.substring(0, 39);
                            }

                            RongIMClient.this.mLibHandler.createDiscussion(sub, userIdList, new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        if (model != null && model.getContent() != null && model.getContent() instanceof Discussion) {
                                            ((RongIMClient.CreateDiscussionCallback)ipcCallbackProxy.callback).onCallback(((Discussion)model.getContent()).getId());
                                        } else {
                                            ((RongIMClient.CreateDiscussionCallback)ipcCallbackProxy.callback).onCallback(null);
                                        }

                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.CreateDiscussionCallback)ipcCallbackProxy.callback).onFail(errorCode);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.CreateDiscussionCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "name or userIdList is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    public void addMemberToDiscussion(final String discussionId, final List<String> userIdList, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(discussionId) && userIdList != null && userIdList.size() != 0) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.addMemberToDiscussion(discussionId, userIdList, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "discussionId or userIdList is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    public void removeMemberFromDiscussion(final String discussionId, final String userId, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(discussionId) && !TextUtils.isEmpty(userId)) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.removeDiscussionMember(discussionId, userId, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "removeMemberFromDiscussion", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "discussionId or userId is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    public void quitDiscussion(final String discussionId, RongIMClient.OperationCallback callback) {
        if (TextUtils.isEmpty(discussionId)) {
            RLog.e("RongIMClient", "discussionId is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.quitDiscussion(discussionId, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "quitDiscussion", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void getMessage(final int messageId, final RongIMClient.ResultCallback<Message> callback) {
        if (messageId <= 0) {
            RLog.e("RongIMClient", "Illegal argument of messageId.");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            Message result = RongIMClient.this.mLibHandler.getMessage(messageId);
                            if (callback != null) {
                                callback.onCallback(result);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getMessage", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        }
    }

    public void insertOutgoingMessage(ConversationType type, String targetId, SentStatus sentStatus, MessageContent content, RongIMClient.ResultCallback<Message> resultCallback) {
        this.insertOutgoingMessage(type, targetId, sentStatus, content, System.currentTimeMillis(), resultCallback);
    }

    public void insertOutgoingMessage(final ConversationType type, final String targetId, final SentStatus sentStatus, final MessageContent content, final long sentTime, final RongIMClient.ResultCallback<Message> resultCallback) {
        if (type != null && !TextUtils.isEmpty(targetId) && content != null) {
            MessageTag msgTag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                throw new RuntimeException("自定义消息没有加注解信息。");
            } else if ((msgTag.flag() & 1) != 1) {
                if (resultCallback != null) {
                    resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

                RLog.e("RongIMClient", "insertMessage Message is missing MessageTag.ISPERSISTED");
            } else {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (resultCallback != null) {
                                resultCallback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                        } else {
                            Message message = Message.obtain(targetId, type, content);
                            message.setSentTime(sentTime);
                            message.setSentStatus(sentStatus);
                            message.setSenderUserId(RongIMClient.this.mCurrentUserId);
                            message.setMessageDirection(MessageDirection.SEND);

                            try {
                                Message result = RongIMClient.this.mLibHandler.insertSettingMessage(message);
                                if (resultCallback != null) {
                                    if (result.getMessageId() < 0) {
                                        if (result.getMessageId() == RongIMClient.ErrorCode.PARAMETER_ERROR.getValue()) {
                                            resultCallback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                                        } else {
                                            resultCallback.onFail(RongIMClient.ErrorCode.BIZ_ERROR_DATABASE_ERROR);
                                        }
                                    } else {
                                        resultCallback.onCallback(result);
                                    }
                                }
                            } catch (RemoteException var3) {
                                RLog.e("RongIMClient", "insertOutgoingMessage", var3);
                                if (resultCallback != null) {
                                    resultCallback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            }
        } else {
            RLog.e("RongIMClient", "insertOutgoingMessage::ConversationType or targetId or content is null");
            if (resultCallback != null) {
                resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void insertIncomingMessage(ConversationType type, String targetId, String senderUserId, ReceivedStatus receivedStatus, MessageContent content, RongIMClient.ResultCallback<Message> resultCallback) {
        this.insertIncomingMessage(type, targetId, senderUserId, receivedStatus, content, System.currentTimeMillis(), resultCallback);
    }

    public void insertIncomingMessage(final ConversationType type, final String targetId, final String senderUserId, final ReceivedStatus receivedStatus, final MessageContent content, final long sentTime, final RongIMClient.ResultCallback<Message> resultCallback) {
        if (type != null && !TextUtils.isEmpty(targetId) && content != null) {
            MessageTag msgTag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                throw new RuntimeException("自定义消息没有加注解信息。");
            } else if ((msgTag.flag() & 1) != 1) {
                if (resultCallback != null) {
                    resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

                RLog.e("RongIMClient", "insertMessage Message is missing MessageTag.ISPERSISTED");
            } else {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (resultCallback != null) {
                                resultCallback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                        } else {
                            Message message = Message.obtain(targetId, type, content);
                            message.setSentTime(sentTime);
                            message.setReceivedStatus(receivedStatus);
                            message.setSenderUserId(senderUserId);
                            message.setMessageDirection(MessageDirection.RECEIVE);

                            try {
                                Message result = RongIMClient.this.mLibHandler.insertSettingMessage(message);
                                if (resultCallback != null) {
                                    if (result.getMessageId() < 0) {
                                        if (result.getMessageId() == RongIMClient.ErrorCode.PARAMETER_ERROR.getValue()) {
                                            resultCallback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                                        } else {
                                            resultCallback.onFail(RongIMClient.ErrorCode.BIZ_ERROR_DATABASE_ERROR);
                                        }
                                    } else {
                                        resultCallback.onCallback(result);
                                    }
                                }
                            } catch (RemoteException var3) {
                                RLog.e("RongIMClient", "insertIncomingMessage", var3);
                                if (resultCallback != null) {
                                    resultCallback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            }
        } else {
            RLog.e("RongIMClient", "insertIncomingMessage::ConversationType or targetId or content is null");
            if (resultCallback != null) {
                resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void sendLocationMessage(final Message message, final String pushContent, final String pushData, ISendMessageCallback sendMessageCallback) {
        if (!this.filterSendMessage(message) && message.getContent() instanceof LocationMessage) {
            if (TypingMessageManager.getInstance().isShowMessageTyping()) {
                TypingMessageManager.getInstance().setTypingEnd(message.getConversationType(), message.getTargetId());
            }

            final IpcCallbackProxy<ISendMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(sendMessageCallback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        RongIMClient.this.runOnUiThread(new Runnable() {
                            public void run() {
                                if (ipcCallbackProxy.callback != null) {
                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }

                            }
                        });
                    } else {
                        try {
                            RongIMClient.this.mLibHandler.sendLocationMessage(message, pushContent, pushData, new io.rong.imlib.ISendMessageCallback.Stub() {
                                public void onAttached(final Message msg) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((ISendMessageCallback)ipcCallbackProxy.callback).onAttached(msg);
                                            }
                                        });
                                    }

                                }

                                public void onSuccess(final Message msg) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((ISendMessageCallback)ipcCallbackProxy.callback).onSuccess(msg);
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }

                                public void onError(final Message msg, final int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((ISendMessageCallback)ipcCallbackProxy.callback).onError(msg, RongIMClient.ErrorCode.valueOf(errorCode));
                                                ipcCallbackProxy.callback = null;
                                            }
                                        });
                                    }

                                }
                            });
                        } catch (Exception var2) {
                            RLog.e("RongIMClient", "sendLocationMessage", var2);
                        }

                    }
                }
            });
        } else {
            if (sendMessageCallback != null) {
                sendMessageCallback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void sendMessage(ConversationType type, String targetId, MessageContent content, String pushContent, String pushData, ISendMessageCallback callback) {
        Message message = Message.obtain(targetId, type, content);
        this.sendMessage(message, pushContent, pushData, callback);
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }

    public void sendMessage(Message message, String pushContent, String pushData, ISendMessageCallback callback) {
        this.sendMessage(message, pushContent, pushData, (SendMessageOption)null, callback);
    }

    public void sendMessage(final Message message, final String pushContent, final String pushData, final SendMessageOption option, ISendMessageCallback callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                RLog.e("RongIMClient", "sendMessage 自定义消息没有加注解信息。");
                if (callback != null) {
                    callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            } else {
                if (TypingMessageManager.getInstance().isShowMessageTyping()) {
                    MessageContent content = message.getContent();
                    if (!(content instanceof TypingStatusMessage) && !(content instanceof ReadReceiptMessage)) {
                        TypingMessageManager.getInstance().setTypingEnd(message.getConversationType(), message.getTargetId());
                    }
                }

                final IpcCallbackProxy<ISendMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            RongIMClient.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((ISendMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } else {
                            try {
                                RongIMClient.this.mLibHandler.sendMessageOption(message, pushContent, pushData, option, new io.rong.imlib.ISendMessageCallback.Stub() {
                                    public void onAttached(final Message msg) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onAttached(msg);
                                                }
                                            });
                                        }

                                    }

                                    public void onSuccess(final Message msg) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onSuccess(msg);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onError(final Message msg, final int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onError(msg, RongIMClient.ErrorCode.valueOf(errorCode));
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }
                                });
                            } catch (Exception var2) {
                                RLog.e("RongIMClient", "sendMessage exception : ", var2);
                            }

                        }
                    }
                });
            }
        }
    }

    public void sendDirectionalMessage(ConversationType type, String targetId, MessageContent content, final String[] userIds, final String pushContent, final String pushData, ISendMessageCallback callback) {
        final Message message = Message.obtain(targetId, type, content);
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            MessageTag msgTag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                RLog.e("RongIMClient", "sendDirectionalMessage 自定义消息没有加注解信息。");
                if (callback != null) {
                    callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            } else {
                if (TypingMessageManager.getInstance().isShowMessageTyping() && !(content instanceof TypingStatusMessage) && !(content instanceof ReadReceiptMessage)) {
                    TypingMessageManager.getInstance().setTypingEnd(message.getConversationType(), message.getTargetId());
                }

                final IpcCallbackProxy<ISendMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            RongIMClient.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((ISendMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } else {
                            try {
                                RongIMClient.this.mLibHandler.sendDirectionalMessage(message, pushContent, pushData, userIds, new io.rong.imlib.ISendMessageCallback.Stub() {
                                    public void onAttached(final Message msg) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onAttached(msg);
                                                }
                                            });
                                        }

                                    }

                                    public void onSuccess(final Message msg) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onSuccess(msg);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onError(final Message msg, final int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMessageCallback)ipcCallbackProxy.callback).onError(msg, RongIMClient.ErrorCode.valueOf(errorCode));
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }
                                });
                            } catch (Exception var2) {
                                RLog.e("RongIMClient", "sendDirectionalMessage exception : ", var2);
                            }

                        }
                    }
                });
            }
        }
    }

    public void sendImageMessage(ConversationType type, String targetId, MessageContent content, String pushContent, String pushData, RongIMClient.SendImageMessageCallback callback) {
        Message message = Message.obtain(targetId, type, content);
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            this.sendImageMessage(message, pushContent, pushData, callback);
        }
    }

    public void sendImageMessage(Message message, final String pushContent, final String pushData, final RongIMClient.SendImageMessageCallback callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final RongIMClient.ResultCallback.Result<Message> result = new RongIMClient.ResultCallback.Result();
            result.t = message;
            final RongIMClient.UploadMediaCallback uploadMediaCallback = new RongIMClient.UploadMediaCallback() {
                public void onProgress(Message message, int progress) {
                    if (callback != null) {
                        callback.onProgressCallback(message, progress);
                    }

                }

                public void onSuccess(Message message) {
                    RongIMClient.this.internalSendImageMessage(message, pushContent, pushData, callback);
                }

                public void onError(Message message, RongIMClient.ErrorCode e) {
                    message.setSentStatus(SentStatus.FAILED);
                    RongIMClient.this.setMessageSentStatus(message, new RongIMClient.ResultCallback<Boolean>() {
                        public void onSuccess(Boolean aBoolean) {
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                    if (callback != null) {
                        callback.onFail(message, e);
                    }

                }
            };
            RongIMClient.ResultCallback<Message> insertCallback = new RongIMClient.ResultCallback<Message>() {
                public void onSuccess(Message message) {
                    if (message != null) {
                        result.t = message;
                        message.setSentStatus(SentStatus.SENDING);
                        RongIMClient.this.setMessageSentStatus(message, (RongIMClient.ResultCallback)null);
                        if (callback != null) {
                            callback.onAttachedCallback(message);
                        }

                        RongIMClient.this.uploadMedia(message, uploadMediaCallback);
                    } else {
                        throw new IllegalArgumentException("Message Content 为空！");
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                    if (callback != null) {
                        callback.onFail(e);
                    }

                }
            };
            if (message.getMessageId() <= 0) {
                this.insertOutgoingMessage(message.getConversationType(), message.getTargetId(), (SentStatus)null, message.getContent(), insertCallback);
            } else {
                message.setSentStatus(SentStatus.SENDING);
                this.setMessageSentStatus(message, new RongIMClient.ResultCallback<Boolean>() {
                    public void onSuccess(Boolean aBoolean) {
                    }

                    public void onError(RongIMClient.ErrorCode e) {
                    }
                });
                this.uploadMedia(message, uploadMediaCallback);
            }

        }
    }

    public void sendImageMessage(final Message message, final String pushContent, final String pushData, final RongIMClient.SendImageMessageWithUploadListenerCallback callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError((Message)null, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else if (this.mLibHandler == null) {
            RLog.e("RongIMClient", "sendImageMessage IPC 进程尚未运行！");
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
            }

        } else {
            RongIMClient.ResultCallback<Message> insertCallback = new RongIMClient.ResultCallback<Message>() {
                public void onSuccess(Message messagex) {
                    if (messagex != null) {
                        messagex.setSentStatus(SentStatus.SENDING);
                        RongIMClient.this.setMessageSentStatus(messagex, (RongIMClient.ResultCallback)null);
                        if (callback != null) {
                            RongIMClient.UploadImageStatusListener watcher = RongIMClient.this.new UploadImageStatusListener(messagex, pushContent, pushData, callback);
                            callback.onAttachedCallback(messagex, watcher);
                        }

                    } else {
                        throw new IllegalArgumentException("Message Content 为空！");
                    }
                }

                public void onError(RongIMClient.ErrorCode e) {
                    if (callback != null) {
                        callback.onError(message, e);
                    }

                }
            };
            this.insertOutgoingMessage(message.getConversationType(), message.getTargetId(), (SentStatus)null, message.getContent(), insertCallback);
        }
    }

    private void internalSendImageMessage(final Message message, final String pushContent, final String pushData, RongIMClient.SendImageMessageCallback callback) {
        final IpcCallbackProxy<RongIMClient.SendImageMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        RongIMClient.this.runOnUiThread(new Runnable() {
                            public void run() {
                                ((RongIMClient.SendImageMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        });
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.sendMediaMessage(message, pushContent, pushData, new io.rong.imlib.ISendMediaMessageCallback.Stub() {
                            public void onAttached(Message messagex) throws RemoteException {
                            }

                            public void onProgress(Message messagex, int progress) throws RemoteException {
                            }

                            public void onSuccess(final Message messagex) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    RongIMClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            ((RongIMClient.SendImageMessageCallback)ipcCallbackProxy.callback).onSuccess(messagex);
                                            ipcCallbackProxy.callback = null;
                                        }
                                    });
                                }

                            }

                            public void onError(final Message messagex, final int errorCode) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    RongIMClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            ((RongIMClient.SendImageMessageCallback)ipcCallbackProxy.callback).onError(messagex, RongIMClient.ErrorCode.valueOf(errorCode));
                                            ipcCallbackProxy.callback = null;
                                        }
                                    });
                                }

                            }

                            public void onCanceled(Message messagex) throws RemoteException {
                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "internalSendImageMessage", var2);
                    }

                }
            }
        });
    }

    private void uploadMedia(final Message message, RongIMClient.UploadMediaCallback callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            Uri localPath = null;
            if (message.getContent() instanceof ImageMessage) {
                localPath = ((ImageMessage)message.getContent()).getLocalUri();
            } else if (message.getContent() instanceof GIFMessage) {
                localPath = ((GIFMessage)message.getContent()).getLocalUri();
            }

            if (!FileUtils.isFileExistsWithUri(this.mContext, localPath)) {
                RLog.e("RongIMClient", "uploadMedia Uri :[" + localPath + "文件不存在");
                if (callback != null) {
                    callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            } else {
                final IpcCallbackProxy<RongIMClient.UploadMediaCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.UploadMediaCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.uploadMedia(message, new io.rong.imlib.IUploadCallback.Stub() {
                                    public void onComplete(String url) throws RemoteException {
                                        RLog.i("RongIMClient", "uploadMedia onComplete url = " + url);
                                        MessageContent content = message.getContent();
                                        if (content instanceof ImageMessage) {
                                            ((ImageMessage)content).setRemoteUri(Uri.parse(url));
                                        }

                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.UploadMediaCallback)ipcCallbackProxy.callback).onCallback(message);
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }

                                    public void onFailure(int errorCode) throws RemoteException {
                                        RLog.e("RongIMClient", "uploadMedia onFailure: " + errorCode);
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.UploadMediaCallback)ipcCallbackProxy.callback).onFail(message, RongIMClient.ErrorCode.valueOf(errorCode));
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }

                                    public void onProgress(int progress) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.UploadMediaCallback)ipcCallbackProxy.callback).onProgressCallback(message, progress);
                                        }

                                    }
                                });
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "uploadMedia", var2);
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.UploadMediaCallback)ipcCallbackProxy.callback).onFail(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            }
        }
    }

    public void downloadMedia(ConversationType conversationType, String targetId, final RongIMClient.MediaType mediaType, final String imageUrl, RongIMClient.DownloadMediaCallback callback) {
        if (conversationType != null && !TextUtils.isEmpty(targetId) && mediaType != null && !TextUtils.isEmpty(imageUrl)) {
            final Conversation conversation = new Conversation();
            conversation.setTargetId(targetId);
            conversation.setConversationType(conversationType);
            final IpcCallbackProxy<RongIMClient.DownloadMediaCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.DownloadMediaCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.downloadMedia(conversation, mediaType.getValue(), imageUrl, new io.rong.imlib.IDownloadMediaCallback.Stub() {
                                public void onComplete(String url) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.DownloadMediaCallback)ipcCallbackProxy.callback).onCallback(url);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.DownloadMediaCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onProgress(int progress) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.DownloadMediaCallback)ipcCallbackProxy.callback).onProgressCallback(progress);
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "downloadMedia", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.DownloadMediaCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "downloadMedia 参数异常。");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void downloadMediaMessage(final Message message, final IDownloadMediaMessageCallback callback) {
        if (message != null && message.getConversationType() != null && !TextUtils.isEmpty(message.getTargetId()) && message.getContent() != null && message.getContent() instanceof MediaMessageContent) {
            final IpcCallbackProxy<IDownloadMediaMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((IDownloadMediaMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.downloadMediaMessage(message, new io.rong.imlib.IDownloadMediaMessageCallback.Stub() {
                                public void onComplete(final Message innerMessage) throws RemoteException {
                                    message.setContent(innerMessage.getContent());
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                if (ipcCallbackProxy.callback != null) {
                                                    ((IDownloadMediaMessageCallback)ipcCallbackProxy.callback).onSuccess(innerMessage);
                                                    ipcCallbackProxy.callback = null;
                                                }

                                            }
                                        });
                                    }

                                }

                                public void onFailure(final int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                if (ipcCallbackProxy.callback != null) {
                                                    ((IDownloadMediaMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.valueOf(errorCode));
                                                    ipcCallbackProxy.callback = null;
                                                }

                                            }
                                        });
                                    }

                                }

                                public void onProgress(final int progress) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                ((IDownloadMediaMessageCallback)ipcCallbackProxy.callback).onProgress(message, progress);
                                            }
                                        });
                                    }

                                }

                                public void onCanceled() throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        RongIMClient.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                if (ipcCallbackProxy.callback != null) {
                                                    ((IDownloadMediaMessageCallback)ipcCallbackProxy.callback).onCanceled(message);
                                                    ipcCallbackProxy.callback = null;
                                                }

                                            }
                                        });
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "downloadMediaMessage", var2);
                            if (callback != null) {
                                RongIMClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        callback.onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    }
                                });
                            }
                        }

                    }
                }
            });
        } else {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void downloadMediaFile(final String fileUniqueId, final String fileUrl, final String fileName, final String path, final IDownloadMediaFileCallback callback) {
        final IpcCallbackProxy<IDownloadMediaFileCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((IDownloadMediaFileCallback)ipcCallbackProxy.callback).onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.downloadMediaFile(fileUniqueId, fileUrl, fileName, path, new io.rong.imlib.IDownloadMediaFileCallback.Stub() {
                            public void onFileNameChanged(String newFileName) {
                                callback.onFileNameChanged(newFileName);
                            }

                            public void onComplete() throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    RongIMClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (ipcCallbackProxy.callback != null) {
                                                ((IDownloadMediaFileCallback)ipcCallbackProxy.callback).onSuccess();
                                                ipcCallbackProxy.callback = null;
                                            }

                                        }
                                    });
                                }

                            }

                            public void onFailure(final int errorCode) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    RongIMClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (ipcCallbackProxy.callback != null) {
                                                ((IDownloadMediaFileCallback)ipcCallbackProxy.callback).onError(RongIMClient.ErrorCode.valueOf(errorCode));
                                                ipcCallbackProxy.callback = null;
                                            }

                                        }
                                    });
                                }

                            }

                            public void onProgress(final int progress) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    RongIMClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            ((IDownloadMediaFileCallback)ipcCallbackProxy.callback).onProgress(progress);
                                        }
                                    });
                                }

                            }

                            public void onCanceled() throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    RongIMClient.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            if (ipcCallbackProxy.callback != null) {
                                                ((IDownloadMediaFileCallback)ipcCallbackProxy.callback).onCanceled();
                                                ipcCallbackProxy.callback = null;
                                            }

                                        }
                                    });
                                }

                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "downloadMediaFile", var2);
                        if (callback != null) {
                            RongIMClient.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            });
                        }
                    }

                }
            }
        });
    }

    public void cancelSendMediaMessage(final Message message, RongIMClient.OperationCallback callback) {
        if (message != null && message.getMessageId() > 0 && message.getContent() instanceof MediaMessageContent && ((MediaMessageContent)message.getContent()).getLocalPath() != null) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.cancelTransferMediaMessage(message, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "cancelSendMediaMessage", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "cancelSendMediaMessage 参数异常。");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void cancelDownloadMediaMessage(final Message message, RongIMClient.OperationCallback callback) {
        if (message != null && message.getMessageId() > 0 && message.getContent() instanceof MediaMessageContent && ((MediaMessageContent)message.getContent()).getMediaUrl() != null) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.cancelTransferMediaMessage(message, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "cancelDownloadMediaMessage", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "cancelDownloadMediaMessage 参数异常。");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    private void cancelAllDownloadMediaMessage(RongIMClient.OperationCallback callback) {
        final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.cancelAllTransferMediaMessage(RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "cancelAllDownloadMediaMessage", var2);
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                }
            }
        });
    }

    public void pauseDownloadMediaMessage(final Message message, RongIMClient.OperationCallback callback) {
        if (message != null && message.getMessageId() > 0 && message.getContent() instanceof MediaMessageContent && ((MediaMessageContent)message.getContent()).getMediaUrl() != null) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.pauseTransferMediaMessage(message, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "pauseDownloadMediaMessage", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "cancelDownloadMediaMessage 参数异常。");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void pauseDownloadMediaFile(final String fileUniqueId, RongIMClient.OperationCallback callback) {
        if (TextUtils.isEmpty(fileUniqueId)) {
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.pauseTransferMediaFile(fileUniqueId, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "pauseDownloadMediaFile", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        }
    }

    public void getConversationNotificationStatus(final ConversationType conversationType, final String targetId, RongIMClient.ResultCallback<ConversationNotificationStatus> callback) {
        if (conversationType != null && !TextUtils.isEmpty(targetId)) {
            if (conversationType.equals(ConversationType.CHATROOM)) {
                RLog.e("RongIMClient", "Not support ChatRoom ConversationType!");
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            } else {
                final IpcCallbackProxy<RongIMClient.ResultCallback<ConversationNotificationStatus>> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.getConversationNotificationStatus(conversationType.getValue(), targetId, new io.rong.imlib.ILongCallback.Stub() {
                                    public void onComplete(long result) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(ConversationNotificationStatus.setValue((int)result));
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }

                                    public void onFailure(int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }
                                });
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "getConversationNotificationStatus", var2);
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                }
                            }

                        }
                    }
                });
            }
        } else {
            RLog.e("RongIMClient", "Conversation parameter is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void setConversationNotificationStatus(final ConversationType conversationType, final String targetId, final ConversationNotificationStatus notificationStatus, RongIMClient.ResultCallback<ConversationNotificationStatus> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null && notificationStatus != null) {
            if (conversationType.equals(ConversationType.CHATROOM)) {
                RLog.e("RongIMClient", "Not support ChatRoom ConversationType!");
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            } else {
                final IpcCallbackProxy<RongIMClient.ResultCallback<ConversationNotificationStatus>> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.setConversationNotificationStatus(conversationType.getValue(), targetId, notificationStatus.getValue(), new io.rong.imlib.ILongCallback.Stub() {
                                    public void onComplete(long result) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(ConversationNotificationStatus.setValue((int)result));
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }

                                    public void onFailure(int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                            ipcCallbackProxy.callback = null;
                                        }

                                    }
                                });
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "setConversationNotificationStatus", var2);
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }
                            }

                        }
                    }
                });
            }
        } else {
            RLog.e("RongIMClient", "Conversation parameter is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    /** @deprecated */
    public void setDiscussionInviteStatus(final String discussionId, final RongIMClient.DiscussionInviteStatus status, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(discussionId) && status != null) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.setDiscussionInviteStatus(discussionId, status.getValue(), RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setDiscussionInviteStatus", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "Parameter is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public String getCurrentUserId() {
        try {
            if (TextUtils.isEmpty(RongIMClient.SingletonHolder.sInstance.mCurrentUserId) && this.mLibHandler != null) {
                RongIMClient.SingletonHolder.sInstance.mCurrentUserId = this.mLibHandler.getCurrentUserId();
            }
        } catch (RemoteException var2) {
            RLog.e("RongIMClient", "getCurrentUserId", var2);
        }

        if (RongIMClient.SingletonHolder.sInstance.mCurrentUserId == null) {
            RLog.w("RongIMClient", "ipc process does not created");
            if (this.mContext != null) {
                SharedPreferences sp = this.mContext.getSharedPreferences("Statistics", 0);
                RongIMClient.SingletonHolder.sInstance.mCurrentUserId = sp.getString("userId", "");
            } else {
                RongIMClient.SingletonHolder.sInstance.mCurrentUserId = null;
            }
        }

        return RongIMClient.SingletonHolder.sInstance.mCurrentUserId;
    }

    public long getDeltaTime() {
        try {
            return this.mLibHandler == null ? 0L : this.mLibHandler.getDeltaTime();
        } catch (RemoteException var2) {
            RLog.e("RongIMClient", "getDeltaTime", var2);
            return 0L;
        }
    }

    public void getChatRoomInfo(final String chatRoomId, final int defMemberCount, final ChatRoomMemberOrder order, RongIMClient.ResultCallback<ChatRoomInfo> callback) {
        if (TextUtils.isEmpty(chatRoomId)) {
            RLog.e("RongIMClient", "id is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.ResultCallback<ChatRoomInfo>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getChatRoomInfo(chatRoomId, defMemberCount, order.getValue(), new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    ChatRoomInfo info = null;
                                    if (model != null) {
                                        info = (ChatRoomInfo)model.getContent();
                                        info.setMemberOrder(order);
                                    }

                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(info);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(errorCode);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getChatRoomInfo", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void joinChatRoom(final String chatRoomId, final int defMessageCount, final RongIMClient.OperationCallback callback) {
        FwLog.write(3, 1, LogTag.A_JOIN_CHATROOM_T.getTag(), "room_id|existed|count", new Object[]{chatRoomId, false, defMessageCount});
        if (TextUtils.isEmpty(chatRoomId)) {
            FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.PARAMETER_ERROR.code, chatRoomId});
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        } else if (TextUtils.isEmpty(this.mToken)) {
            RLog.e("RongIMClient", "joinChatRoom without connect!");
            FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.IPC_DISCONNECT.code, chatRoomId});
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.APP_NOT_CONNECT);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    RongIMClient.ChatRoomActionListener listener = (RongIMClient.ChatRoomActionListener)RongIMClient.chatRoomActionListener.get();
                    if (listener != null) {
                        listener.onJoining(chatRoomId);
                    }

                    if (RongIMClient.this.mLibHandler != null) {
                        try {
                            boolean joinMultiCR = RongIMClient.this.mLibHandler.getJoinMultiChatRoomEnable();
                            RongIMClient.JoinChatRoomCallback callbackx = RongIMClient.this.new JoinChatRoomCallback(ipcCallbackProxy, chatRoomId, defMessageCount, joinMultiCR, false, false);
                            RongIMClient.this.mLibHandler.joinChatRoom(chatRoomId, defMessageCount, callbackx);
                        } catch (RemoteException var4) {
                            FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id|stacks", new Object[]{-1, chatRoomId, FwLog.stackToString(var4)});
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            if (listener != null) {
                                listener.onError(chatRoomId, RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }
                    } else {
                        FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.IPC_DISCONNECT.code, chatRoomId});
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        if (listener != null) {
                            listener.onError(chatRoomId, RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                }
            });
        }
    }

    private void reJoinChatRoomWithCache() {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                int size = RongIMClient.this.mRetryCRCache.size();
                Iterator var2;
                RongIMClient.ChatRoomCacheRunnable runnable;
                if (size > 0) {
                    RLog.d("RongIMClient", "clear retry chatroom cache after connectivity available, cached size = " + size);
                    var2 = RongIMClient.this.mRetryCRCache.values().iterator();

                    while(var2.hasNext()) {
                        runnable = (RongIMClient.ChatRoomCacheRunnable)var2.next();
                        RongIMClient.this.mWorkHandler.removeCallbacks(runnable);
                    }

                    RongIMClient.this.mRetryCRCache.clear();
                }

                size = RongIMClient.this.mChatRoomCache.size();
                if (size > 0) {
                    RLog.d("RongIMClient", "re-join chatroom after connectivity available, cached size = " + size);
                    var2 = RongIMClient.this.mChatRoomCache.values().iterator();

                    while(var2.hasNext()) {
                        runnable = (RongIMClient.ChatRoomCacheRunnable)var2.next();
                        RongIMClient.this.mWorkHandler.post(runnable);
                    }
                }

            }
        });
    }

    public void joinExistChatRoom(final String chatRoomId, final int defMessageCount, RongIMClient.OperationCallback callback) {
        FwLog.write(3, 1, LogTag.A_JOIN_CHATROOM_T.getTag(), "room_id|existed|count", new Object[]{chatRoomId, true, defMessageCount});
        if (TextUtils.isEmpty(chatRoomId)) {
            FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.PARAMETER_ERROR.code, chatRoomId});
            RLog.e("RongIMClient", "id is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else if (TextUtils.isEmpty(this.mToken)) {
            RLog.e("RongIMClient", "joinExitChatRoom without connect!");
            FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.IPC_DISCONNECT.code, chatRoomId});
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.APP_NOT_CONNECT);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    RongIMClient.JoinChatRoomCallback callback = RongIMClient.this.new JoinChatRoomCallback(ipcCallbackProxy, chatRoomId, defMessageCount, false, true, false);
                    RongIMClient.ChatRoomActionListener listener = (RongIMClient.ChatRoomActionListener)RongIMClient.chatRoomActionListener.get();
                    if (listener != null) {
                        listener.onJoining(chatRoomId);
                    }

                    if (RongIMClient.this.mLibHandler != null) {
                        try {
                            boolean joinMultiCR = RongIMClient.this.mLibHandler.getJoinMultiChatRoomEnable();
                            RongIMClient.this.mLibHandler.joinExistChatRoom(chatRoomId, defMessageCount, RongIMClient.this.new JoinChatRoomCallback(ipcCallbackProxy, chatRoomId, defMessageCount, joinMultiCR, true, false), false);
                        } catch (RemoteException var4) {
                            FwLog.write(1, 1, LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id|stacks", new Object[]{-1, chatRoomId, FwLog.stackToString(var4)});
                            RLog.e("RongIMClient", "joinExistChatRoom", var4);
                            callback.onFailure(RongIMClient.ErrorCode.IPC_DISCONNECT.getValue());
                        }
                    } else {
                        callback.onFailure(RongIMClient.ErrorCode.IPC_DISCONNECT.getValue());
                    }

                }
            });
        }
    }

    public void quitChatRoom(final String chatRoomId, RongIMClient.OperationCallback callback) {
        FwLog.write(3, 1, LogTag.A_QUIT_CHATROOM_T.getTag(), "room_id", new Object[]{chatRoomId});
        if (TextUtils.isEmpty(chatRoomId)) {
            FwLog.write(1, 1, LogTag.A_QUIT_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.PARAMETER_ERROR.code, chatRoomId});
            RLog.e("RongIMClient", "id is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    RongIMClient.this.mChatRoomCache.remove(chatRoomId);
                    Runnable runnable = (Runnable)RongIMClient.this.mRetryCRCache.remove(chatRoomId);
                    if (runnable != null) {
                        RongIMClient.this.mWorkHandler.removeCallbacks(runnable);
                    }

                    RongIMClient.ChatRoomActionListener listener = (RongIMClient.ChatRoomActionListener)RongIMClient.chatRoomActionListener.get();
                    if (listener != null) {
                        listener.onQuited(chatRoomId);
                    }

                    if (RongIMClient.this.mLibHandler == null) {
                        FwLog.write(1, 1, LogTag.A_QUIT_CHATROOM_R.getTag(), "code|room_id", new Object[]{RongIMClient.ErrorCode.IPC_DISCONNECT, chatRoomId});
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.quitChatRoom(chatRoomId, new RongIMClient.DefaultOperationCallback(ipcCallbackProxy) {
                                public void onComplete() {
                                    super.onComplete();
                                    FwLog.write(3, 1, LogTag.A_QUIT_CHATROOM_R.getTag(), "code|room_id", new Object[]{0, chatRoomId});
                                }

                                public void onFailure(int errorCode) {
                                    super.onFailure(errorCode);
                                    FwLog.write(1, 1, LogTag.A_QUIT_CHATROOM_R.getTag(), "code|room_id", new Object[]{errorCode, chatRoomId});
                                }
                            });
                        } catch (RemoteException var4) {
                            FwLog.write(1, 1, LogTag.A_QUIT_CHATROOM_R.getTag(), "code|room_id|stacks", new Object[]{-1000, chatRoomId, FwLog.stackToString(var4)});
                            RLog.e("RongIMClient", "quitChatRoom", var4);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void clearConversations(final RongIMClient.ResultCallback callback, final ConversationType... conversationTypes) {
        if (conversationTypes != null && conversationTypes.length != 0) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            int[] types = RongIMClient.this.convertTypes(conversationTypes);
                            boolean result = RongIMClient.this.mLibHandler.clearConversations(types);
                            if (callback != null) {
                                callback.onCallback(result);
                            }
                        } catch (RemoteException var3) {
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "conversationTypes is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void addToBlacklist(final String userId, RongIMClient.OperationCallback callback) {
        if (TextUtils.isEmpty(userId)) {
            RLog.e("RongIMClient", "userId  is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.addToBlacklist(userId, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "addToBlacklist", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void removeFromBlacklist(final String userId, RongIMClient.OperationCallback callback) {
        if (TextUtils.isEmpty(userId)) {
            RLog.e("RongIMClient", "userId  is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.removeFromBlacklist(userId, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "removeFromBlacklist", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void getBlacklistStatus(final String userId, RongIMClient.ResultCallback<RongIMClient.BlacklistStatus> callback) {
        if (TextUtils.isEmpty(userId)) {
            RLog.e("RongIMClient", "userId  is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.ResultCallback<RongIMClient.BlacklistStatus>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getBlacklistStatus(userId, new io.rong.imlib.IIntegerCallback.Stub() {
                                public void onComplete(int result) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(RongIMClient.BlacklistStatus.setValue(result));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(errorCode);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getBlacklistStatus", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void getBlacklist(RongIMClient.GetBlacklistCallback callback) {
        final IpcCallbackProxy<RongIMClient.GetBlacklistCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.GetBlacklistCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.getBlacklist(new io.rong.imlib.IStringCallback.Stub() {
                            public void onComplete(String result) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    if (result == null) {
                                        ((RongIMClient.GetBlacklistCallback)ipcCallbackProxy.callback).onCallback(null);
                                    } else {
                                        ((RongIMClient.GetBlacklistCallback)ipcCallbackProxy.callback).onCallback(result.split("\n"));
                                    }

                                    ipcCallbackProxy.callback = null;
                                }

                            }

                            public void onFailure(int errorCode) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.GetBlacklistCallback)ipcCallbackProxy.callback).onFail(errorCode);
                                    ipcCallbackProxy.callback = null;
                                }

                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "getBlacklist", var2);
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.GetBlacklistCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }
                    }

                }
            }
        });
    }

    public void searchPublicService(final RongIMClient.SearchType searchType, final String keywords, RongIMClient.ResultCallback<PublicServiceProfileList> callback) {
        if (searchType == null) {
            RLog.e("RongIMClient", "searchType  is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.ResultCallback<PublicServiceProfileList>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.searchPublicService(keywords, 0, searchType.getValue(), new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null && model != null && model.getContent() != null && model.getContent() instanceof PublicServiceProfileList) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback((PublicServiceProfileList)model.getContent());
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(errorCode);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "searchPublicService", var2);
                        }

                    }
                }
            });
        }
    }

    public void searchPublicServiceByType(PublicServiceType publicServiceType, final RongIMClient.SearchType searchType, final String keywords, RongIMClient.ResultCallback<PublicServiceProfileList> callback) {
        if (publicServiceType != null && searchType != null) {
            final int[] businessType = new int[]{0};
            if (publicServiceType == PublicServiceType.APP_PUBLIC_SERVICE) {
                businessType[0] = 2;
            } else if (publicServiceType == PublicServiceType.PUBLIC_SERVICE) {
                businessType[0] = 1;
            }

            final IpcCallbackProxy<RongIMClient.ResultCallback<PublicServiceProfileList>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.searchPublicService(keywords, businessType[0], searchType.getValue(), new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null && model != null && model.getContent() != null && model.getContent() instanceof PublicServiceProfileList) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback((PublicServiceProfileList)model.getContent());
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(errorCode);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "searchPublicServiceByType", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "searchType  is null!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void subscribePublicService(final PublicServiceType publicServiceType, final String publicServiceId, RongIMClient.OperationCallback callback) {
        if (publicServiceType != null && !TextUtils.isEmpty(publicServiceId)) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.subscribePublicService(publicServiceId, publicServiceType.getValue(), true, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "subscribePublicService", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "Parameter  is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void unsubscribePublicService(final PublicServiceType publicServiceType, final String publicServiceId, RongIMClient.OperationCallback callback) {
        if (publicServiceType != null && !TextUtils.isEmpty(publicServiceId)) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.subscribePublicService(publicServiceId, publicServiceType.getValue(), false, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "unsubscribePublicService", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "Parameter  is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getPublicServiceProfile(final PublicServiceType publicServiceType, final String publicServiceId, RongIMClient.ResultCallback<PublicServiceProfile> callback) {
        if (publicServiceType != null && !TextUtils.isEmpty(publicServiceId)) {
            final IpcCallbackProxy<RongIMClient.ResultCallback<PublicServiceProfile>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getPublicServiceProfile(publicServiceId, publicServiceType.getValue(), new io.rong.imlib.IResultCallback.Stub() {
                                public void onComplete(RemoteModelWrap model) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        PublicServiceProfile publicServiceProfile = null;
                                        if (model != null) {
                                            publicServiceProfile = (PublicServiceProfile)model.getContent();
                                        }

                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(publicServiceProfile);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getPublicServiceProfile", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "Parameter  is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getPublicServiceList(RongIMClient.ResultCallback<PublicServiceProfileList> callback) {
        final IpcCallbackProxy<RongIMClient.ResultCallback<PublicServiceProfileList>> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.getPublicServiceList(new io.rong.imlib.IResultCallback.Stub() {
                            public void onComplete(RemoteModelWrap model) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    PublicServiceProfileList publicServiceInfoList = (PublicServiceProfileList)model.getContent();
                                    ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(publicServiceInfoList);
                                    ipcCallbackProxy.callback = null;
                                }

                            }

                            public void onFailure(int errorCode) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                    ipcCallbackProxy.callback = null;
                                }

                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "getPublicServiceList", var2);
                    }

                }
            }
        });
    }

    public void setNotificationQuietHours(final String startTime, final int spanMinutes, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(startTime) && spanMinutes > 0 && spanMinutes < 1440) {
            Pattern pattern = Pattern.compile("^(([0-1][0-9])|2[0-3]):[0-5][0-9]:([0-5][0-9])$");
            Matcher matcher = pattern.matcher(startTime);
            if (!matcher.find()) {
                RLog.e("RongIMClient", "startTime 参数异常。");
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            } else {
                final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.setNotificationQuietHours(startTime, spanMinutes, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "setNotificationQuietHours", var2);
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    ipcCallbackProxy.callback = null;
                                }
                            }

                        }
                    }
                });
            }
        } else {
            RLog.e("RongIMClient", "startTime, spanMinutes 或 spanMinutes 参数异常。");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void removeNotificationQuietHours(RongIMClient.OperationCallback callback) {
        final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.removeNotificationQuietHours(RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "removeNotificationQuietHours", var2);
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }
                    }

                }
            }
        });
    }

    private void updateMessageReceiptStatus(final ConversationType conversationType, final String targetId, final long timestamp, final RongIMClient.OperationCallback callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else {
                    try {
                        if (RongIMClient.this.mLibHandler.updateMessageReceiptStatus(targetId, conversationType.getValue(), timestamp)) {
                            if (callback != null) {
                                callback.onCallback();
                            }
                        } else if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.UNKNOWN);
                        }
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "updateMessageReceiptStatus", var2);
                    }

                }
            }
        });
    }

    public void clearMessagesUnreadStatus(final ConversationType conversationType, final String targetId, final long timestamp, final RongIMClient.OperationCallback callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else if (conversationType == null) {
                    RLog.e("RongIMClient", "clearMessagesUnreadStatus conversationType is null!");
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                    }

                } else {
                    try {
                        if (RongIMClient.this.mLibHandler.clearUnreadByReceipt(conversationType.getValue(), targetId, timestamp)) {
                            if (callback != null) {
                                callback.onCallback();
                            }
                        } else if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.UNKNOWN);
                        }
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "clearMessagesUnreadStatus", var2);
                    }

                }
            }
        });
    }

    public long getSendTimeByMessageId(int messageId) {
        try {
            if (this.mLibHandler == null) {
                RLog.e("RongIMClient", "getSendTimeByMessageId mLibHandler is null!");
                return 0L;
            } else {
                return this.mLibHandler.getSendTimeByMessageId(messageId);
            }
        } catch (RemoteException var3) {
            RLog.e("RongIMClient", "getSendTimeByMessageId", var3);
            return 0L;
        }
    }

    public void getNotificationQuietHours(RongIMClient.GetNotificationQuietHoursCallback callback) {
        final IpcCallbackProxy<RongIMClient.GetNotificationQuietHoursCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.GetNotificationQuietHoursCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.getNotificationQuietHours(new io.rong.imlib.IGetNotificationQuietHoursCallback.Stub() {
                            public void onSuccess(String startTime, int spanMin) {
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.GetNotificationQuietHoursCallback)ipcCallbackProxy.callback).onCallback(startTime, spanMin);
                                    ipcCallbackProxy.callback = null;
                                }

                            }

                            public void onError(int code) {
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.GetNotificationQuietHoursCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(code));
                                    ipcCallbackProxy.callback = null;
                                }

                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "getNotificationQuietHours", var2);
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.GetNotificationQuietHoursCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }
                    }

                }
            }
        });
    }

    private void clearToken() {
        RLog.d("RongIMClient", "clear token");
        this.mToken = null;
        SharedPreferences sp = this.mContext.getSharedPreferences("Statistics", 0);
        sp.edit().remove("token").commit();
    }

    private void setToken(String token) {
        RLog.d("RongIMClient", "set token");
        this.mToken = token;
    }

    private void handleReadReceiptMessage(final Message message) {
        if (message.getMessageDirection().equals(MessageDirection.SEND)) {
            this.clearMessagesUnreadStatus(message.getConversationType(), message.getTargetId(), ((ReadReceiptMessage)message.getContent()).getLastMessageSendTime(), (RongIMClient.OperationCallback)null);
            if (this.mSyncConversationReadStatusListener != null) {
                this.mSyncConversationReadStatusListener.onSyncConversationReadStatus(message.getConversationType(), message.getTargetId());
            }
        } else {
            this.updateMessageReceiptStatus(message.getConversationType(), message.getTargetId(), ((ReadReceiptMessage)message.getContent()).getLastMessageSendTime(), new RongIMClient.OperationCallback() {
                public void onSuccess() {
                    if (RongIMClient.sReadReceiptListener != null) {
                        RongIMClient.sReadReceiptListener.onReadReceiptReceived(message);
                    }

                }

                public void onError(RongIMClient.ErrorCode errorCode) {
                    RLog.e("RongIMClient", "RongIMClient : updateMessageReceiptStatus fail");
                }
            });
        }

    }

    private boolean handleCmdMessages(final Message message, final int left, final boolean hasPackage, final boolean offline, final int cmdLeft) {
        boolean result = ModuleManager.routeMessage(message, left, offline, cmdLeft);
        boolean isExtensionModuleHandled = false;
        if (!result) {
            isExtensionModuleHandled = this.imLibExtensionModuleManager.onReceiveMessage(message, left, offline, cmdLeft);
        }

        if (!result && !isExtensionModuleHandled) {
            if (TypingMessageManager.getInstance().isShowMessageTyping()) {
                boolean isTypingMessage = TypingMessageManager.getInstance().onReceiveMessage(message);
                if (isTypingMessage) {
                    return true;
                }
            }

            if (message.getContent() instanceof ReadReceiptMessage) {
                this.handleReadReceiptMessage(message);
                return true;
            } else if (message.getContent() instanceof SyncReadStatusMessage) {
                if (message.getMessageDirection() == MessageDirection.SEND) {
                    this.clearMessagesUnreadStatus(message.getConversationType(), message.getTargetId(), ((SyncReadStatusMessage)message.getContent()).getLastMessageSendTime(), (RongIMClient.OperationCallback)null);
                    if (this.mSyncConversationReadStatusListener != null) {
                        this.mSyncConversationReadStatusListener.onSyncConversationReadStatus(message.getConversationType(), message.getTargetId());
                    }
                }

                return true;
            } else if (message.getContent() instanceof ReadReceiptRequestMessage) {
                if (!message.getConversationType().equals(ConversationType.GROUP) && !message.getConversationType().equals(ConversationType.DISCUSSION)) {
                    return true;
                } else {
                    ReadReceiptRequestMessage requestMessage = (ReadReceiptRequestMessage)message.getContent();

                    try {
                        Message msg = this.mLibHandler.getMessageByUid(requestMessage.getMessageUId());
                        if (msg != null) {
                            ReadReceiptInfo readReceiptInfo = msg.getReadReceiptInfo();
                            if (readReceiptInfo == null) {
                                readReceiptInfo = new ReadReceiptInfo();
                                msg.setReadReceiptInfo(readReceiptInfo);
                            }

                            readReceiptInfo.setIsReadReceiptMessage(true);
                            readReceiptInfo.setHasRespond(false);
                            this.mLibHandler.updateReadReceiptRequestInfo(requestMessage.getMessageUId(), readReceiptInfo.toJSON().toString());
                            if (sReadReceiptListener != null) {
                                sReadReceiptListener.onMessageReceiptRequest(message.getConversationType(), message.getTargetId(), requestMessage.getMessageUId());
                            }
                        }
                    } catch (RemoteException var16) {
                        RLog.e("RongIMClient", "*handleCmdMessages", var16);
                    }

                    return true;
                }
            } else if (message.getContent() instanceof ReadReceiptResponseMessage) {
                if (message.getMessageDirection().equals(MessageDirection.SEND)) {
                    return true;
                } else if (!message.getConversationType().equals(ConversationType.GROUP) && !message.getConversationType().equals(ConversationType.DISCUSSION)) {
                    return true;
                } else {
                    ReadReceiptResponseMessage responseMessage = (ReadReceiptResponseMessage)message.getContent();
                    ArrayList<String> messageUIdList = responseMessage.getMessageUIdListBySenderId(this.getCurrentUserId());
                    String senderUserId = message.getSenderUserId();
                    if (messageUIdList != null && this.mLibHandler != null) {
                        Iterator var26 = messageUIdList.iterator();

                        while(var26.hasNext()) {
                            String messageUId = (String)var26.next();

                            try {
                                Message msg = this.mLibHandler.getMessageByUid(messageUId);
                                if (msg != null) {
                                    ReadReceiptInfo readReceiptInfo = msg.getReadReceiptInfo();
                                    if (readReceiptInfo == null) {
                                        readReceiptInfo = new ReadReceiptInfo();
                                        msg.setReadReceiptInfo(readReceiptInfo);
                                    }

                                    readReceiptInfo.setIsReadReceiptMessage(true);
                                    HashMap<String, Long> respondUserIdList = readReceiptInfo.getRespondUserIdList();
                                    if (respondUserIdList == null) {
                                        respondUserIdList = new HashMap();
                                        readReceiptInfo.setRespondUserIdList(respondUserIdList);
                                    }

                                    respondUserIdList.put(senderUserId, message.getSentTime());
                                    this.mLibHandler.updateReadReceiptRequestInfo(messageUId, readReceiptInfo.toJSON().toString());
                                    if (sReadReceiptListener != null) {
                                        sReadReceiptListener.onMessageReceiptResponse(message.getConversationType(), message.getTargetId(), messageUId, respondUserIdList);
                                    }
                                }
                            } catch (RemoteException var17) {
                                RLog.e("RongIMClient", "#handleCmdMessages", var17);
                            }
                        }
                    }

                    return true;
                }
            } else if (message.getContent() instanceof RecallCommandMessage) {
                final RecallCommandMessage recallCommandMessage = (RecallCommandMessage)message.getContent();
                this.getMessageByUid(recallCommandMessage.getMessageUId(), new RongIMClient.ResultCallback<Message>() {
                    public void onSuccess(final Message msg) {
                        RecallNotificationMessage recallNotificationMessage;
                        if (msg == null) {
                            recallNotificationMessage = new RecallNotificationMessage(message.getSenderUserId(), recallCommandMessage.getSentTime(), message.getObjectName(), recallCommandMessage.isAdmin(), recallCommandMessage.isDelete());
                            recallNotificationMessage.setUserInfo(recallCommandMessage.getUserInfo());
                            Message recallNotificationMsg = Message.obtain(message.getTargetId(), message.getConversationType(), recallNotificationMessage);
                            recallNotificationMsg.setSentTime(recallCommandMessage.getSentTime());
                            recallNotificationMsg.setSenderUserId(message.getSenderUserId());
                            ReceivedStatus receivedStatus = new ReceivedStatus(0);
                            RongIMClient.this.insertIncomingMessage(message.getConversationType(), message.getTargetId(), message.getSenderUserId(), receivedStatus, recallNotificationMessage, recallCommandMessage.getSentTime(), (RongIMClient.ResultCallback)null);
                            if (RongIMClient.sReceiveMessageListener != null) {
                                if (RongIMClient.sReceiveMessageListener instanceof RongIMClient.OnReceiveMessageWrapperListener) {
                                    ((RongIMClient.OnReceiveMessageWrapperListener)RongIMClient.sReceiveMessageListener).onReceived(recallNotificationMsg, left - cmdLeft, hasPackage, offline);
                                } else {
                                    RongIMClient.sReceiveMessageListener.onReceived(recallNotificationMsg, left - cmdLeft);
                                }
                            }
                        } else {
                            if (recallCommandMessage.isDelete()) {
                                int[] messageId = new int[]{msg.getMessageId()};
                                FwLog.write(4, 1, LogTag.P_DELETE_MSG_S.getTag(), "isDelete|messageId|messageUId", new Object[]{recallCommandMessage.isDelete(), messageId, recallCommandMessage.getMessageUId()});
                                RongIMClient.this.deleteMessages(messageId, new RongIMClient.ResultCallback<Boolean>() {
                                    public void onSuccess(Boolean aBoolean) {
                                        RLog.i("RongIMClient", "deleteMessage success");
                                        if (RongIMClient.sOnRecallMessageListener != null) {
                                            RongIMClient.sOnRecallMessageListener.onMessageRecalled(msg, (RecallNotificationMessage)null);
                                        }

                                    }

                                    public void onError(RongIMClient.ErrorCode e) {
                                        RLog.e("RongIMClient", "deleteMessage when recall, error " + e.code);
                                    }
                                });
                                return;
                            }

                            recallNotificationMessage = new RecallNotificationMessage(message.getSenderUserId(), recallCommandMessage.getSentTime(), msg.getObjectName(), recallCommandMessage.isAdmin(), recallCommandMessage.isDelete());
                            msg.getReceivedStatus().setRead();
                            RongIMClient.this.setMessageReceivedStatus(msg.getMessageId(), msg.getReceivedStatus(), (RongIMClient.ResultCallback)null);
                            if (msg.getContent().getUserInfo() != null) {
                                recallNotificationMessage.setUserInfo(msg.getContent().getUserInfo());
                            }

                            if (msg.getContent() instanceof RecallNotificationMessage) {
                                RecallNotificationMessage msgContent = (RecallNotificationMessage)msg.getContent();
                                if (msgContent.getRecallActionTime() > 0L) {
                                    recallNotificationMessage.setRecallActionTime(msgContent.getRecallActionTime());
                                }

                                if (!TextUtils.isEmpty(msgContent.getRecallContent())) {
                                    recallNotificationMessage.setRecallContent(msgContent.getRecallContent());
                                }
                            }

                            byte[] data = recallNotificationMessage.encode();

                            try {
                                MessageTag recallNotificationTag = (MessageTag)RecallNotificationMessage.class.getAnnotation(MessageTag.class);
                                RongIMClient.this.mLibHandler.setMessageContent(msg.getMessageId(), data, recallNotificationTag.value());
                            } catch (RemoteException var5) {
                                RLog.e("RongIMClient", "handleCmdMessages", var5);
                                return;
                            }

                            if (RongIMClient.sOnRecallMessageListener != null) {
                                msg.setContent(recallNotificationMessage);
                                RongIMClient.sOnRecallMessageListener.onMessageRecalled(msg, recallNotificationMessage);
                            }
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                        RLog.e("RongIMClient", "recall message received, but getMessageByUid failed");
                    }
                });
                return true;
            } else if (!(message.getContent() instanceof DestructionCmdMessage)) {
                return this.mCmdObjectNameList.contains(message.getObjectName());
            } else {
                DestructionCmdMessage destructionCmdMessage = (DestructionCmdMessage)message.getContent();
                List<String> msgUids = destructionCmdMessage.getBurnMessageUIds();
                Iterator var10 = msgUids.iterator();

                while(var10.hasNext()) {
                    String uid = (String)var10.next();
                    this.getMessageByUid(uid, new RongIMClient.ResultCallback<Message>() {
                        public void onSuccess(Message msg) {
                            if (msg != null) {
                                if (RongIMClient.this.mOnReceiveDestructionMessageListener == null) {
                                    return;
                                }

                                RongIMClient.this.setMessageReadTime((long)msg.getMessageId(), message.getSentTime(), (RongIMClient.OperationCallback)null);
                                msg.setReadTime(message.getSentTime());
                                RongIMClient.getInstance().deleteRemoteMessages(message.getConversationType(), message.getTargetId(), new Message[]{message}, (RongIMClient.OperationCallback)null);
                                RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()}, (RongIMClient.ResultCallback)null);
                                RongIMClient.this.mOnReceiveDestructionMessageListener.onReceive(msg);
                            }

                        }

                        public void onError(RongIMClient.ErrorCode e) {
                        }
                    });
                }

                return true;
            }
        } else {
            return true;
        }
    }

    public void setSavePath(String savePath) {
        SavePathUtils.setSavePath(savePath);
        this.mSavePath = savePath;
    }

    private void initReceiver() {
        RLog.i("RongIMClient", "initMessageReceiver");

        try {
            this.mLibHandler.setSavePath(this.mSavePath);
            if (!TextUtils.isEmpty(mNaviServer)) {
                this.mLibHandler.setServerInfo(mNaviServer, TextUtils.isEmpty(mFileServer) ? "" : mFileServer);
            }

            this.mLibHandler.initHttpDns();
            this.mLibHandler.setUserPolicy(userPolicy);
            if (this.mStatusListener == null) {
                this.mStatusListener = new RongIMClient.StatusListener();
            }

            this.mLibHandler.setConnectionStatusListener(this.mStatusListener);
            this.mLibHandler.setReconnectKickEnable(this.kickReconnectDevice);
            this.mLibHandler.setOnReceiveMessageListener(new io.rong.imlib.OnReceiveMessageListener.Stub() {
                public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
                    try {
                        boolean result = super.onTransact(code, data, reply, flags);
                        return result;
                    } catch (RuntimeException var7) {
                        RLog.e("RongIMClient", "setOnReceiveMessageListener Unexpected remote exception", var7);
                        throw var7;
                    }
                }

                public boolean onReceived(final Message message, final int left, final boolean offline, final boolean hasPackage, final int cmdLeft) throws RemoteException {
                    RongIMClient.this.mUpStreamHandler.post(new Runnable() {
                        public void run() {
                            if (message.getContent() == null) {
                                RLog.e("RongIMClient", "message content is null. Return directly!");
                            } else {
                                MessageTag msgTag = (MessageTag)message.getContent().getClass().getAnnotation(MessageTag.class);

                                try {
                                    if (msgTag != null && msgTag.value().contains("RC:Encrypt") && RongIMClient.this.handleEncMessageContent(message, left, offline, cmdLeft)) {
                                        return;
                                    }
                                } catch (RemoteException var3) {
                                    RLog.e("RongIMClient", "message:" + var3.getMessage());
                                }

                                if (!hasPackage) {
                                    RLog.i("RongIMClient", "onReceived : " + message.getTargetId() + " " + message.getObjectName() + ", sender = " + message.getSenderUserId() + ", uid = " + message.getUId());
                                }

                                if (!RongIMClient.this.handleCmdMessages(message, left, hasPackage, offline, cmdLeft) && RongIMClient.sReceiveMessageListener != null) {
                                    if (RongIMClient.sReceiveMessageListener instanceof RongIMClient.OnReceiveMessageWrapperListener) {
                                        ((RongIMClient.OnReceiveMessageWrapperListener)RongIMClient.sReceiveMessageListener).onReceived(message, left - cmdLeft, hasPackage, offline);
                                    } else {
                                        RongIMClient.sReceiveMessageListener.onReceived(message, left - cmdLeft);
                                    }
                                }

                            }
                        }
                    });
                    return false;
                }
            });
            this.mLibHandler.setUserProfileListener(new io.rong.imlib.UserProfileSettingListener.Stub() {
                public void OnPushNotificationChanged(long version) throws RemoteException {
                    RongIMClient.this.getPushContentShowStatus(new RongIMClient.ResultCallback<Boolean>() {
                        public void onSuccess(Boolean aBoolean) {
                            RLog.i("RongIMClient", "OnPushNotificationChanged onSuccess  " + aBoolean);
                            RongPushClient.updatePushContentShowStatus(RongIMClient.this.mContext, aBoolean);
                        }

                        public void onError(RongIMClient.ErrorCode e) {
                            RLog.i("RongIMClient", "OnPushNotificationChanged onError  " + e);
                        }
                    });
                }
            });
            this.mLibHandler.setConversationStatusListener(new io.rong.imlib.ConversationStatusListener.Stub() {
                public void OnStatusChanged(ConversationStatus[] conversationStatus) {
                    if (RongIMClient.sConversationStatusListener != null) {
                        RongIMClient.sConversationStatusListener.OnStatusChanged(conversationStatus);
                    }

                }
            });
            synchronized(this.mRegCache) {
                List<String> messageTypeList = new ArrayList(this.mRegCache);
                this.mLibHandler.registerMessageTypes(messageTypeList);
            }

            List<String> cmdMsgTypeList = new ArrayList(this.mCmdObjectNameList);
            this.mLibHandler.registerCmdMsgTypes(cmdMsgTypeList);
            this.mLibHandler.registerDeleteMessageType(new ArrayList(this.mDeleteObjectNameList));
        } catch (RemoteException var5) {
            RLog.e("RongIMClient", "initReceiver", var5);
        }

    }

    private void setIPCLogListener() {
        try {
            FwLog.setProxyWriter(new IFwLogWriter() {
                public void write(int level, String type, String tag, String metaJson, long timestamp) {
                    try {
                        IHandler iHandler = RongIMClient.this.mLibHandler;
                        if (iHandler != null) {
                            iHandler.writeFwLog(level, type, tag, metaJson, timestamp);
                        }
                    } catch (Exception var8) {
                    }

                }
            });
            if (this.mLibHandler != null) {
                this.mLibHandler.setRLogOtherProgressCallback(new io.rong.imlib.IRLogOtherProgressCallback.Stub() {
                    public void write(String log, int level) {
                        RLog.callbackWrite(log, level);
                    }

                    public void setLogLevel(int level) {
                        RLog.setLogLevel(level);
                    }

                    public void uploadRLog() {
                        RLog.uploadRLog();
                    }
                });
                this.mLibHandler.setNaviContentUpdateListener(new io.rong.imlib.INaviContentUpdateCallBack.Stub() {
                    public void naviContentUpdate() {
                        RongIMClient.this.mWorkHandler.post(new Runnable() {
                            public void run() {
                                try {
                                    RLog.setUploadUrl(RongIMClient.this.mLibHandler.getOffLineLogServer());
                                } catch (RemoteException var2) {
                                    RLog.e("RongIMClient", "getUploadLogConfigInfo", var2);
                                }

                            }
                        });
                    }
                });
            }
        } catch (RemoteException var2) {
            RLog.e("RongIMClient", "setIPCLogListener", var2);
        }

    }

    private boolean handleEncMessageContent(Message message, int left, boolean offLine, int cmdLeft) throws RemoteException {
        RLog.d("RongIMClient", "handleEncMsg -> " + message.toString());
        MessageContent content = message.getContent();
        String targetId = message.getTargetId();
        if (content == null) {
            RLog.e("RongIMClient", "messageContent from message is null.");
            return false;
        } else {
            MessageTag msgTag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
            if (msgTag == null) {
                RLog.e("RongIMClient", "messageTag is null from -> " + content.getClass());
                return false;
            } else {
                String value = msgTag.value();
                if (value.contains("RC:Encrypt")) {
                    RLog.d("RongIMClient", "cmdLeft -> " + cmdLeft + "left" + left + " value " + value + " isRetriverd = " + message.getReceivedStatus().isRetrieved() + " isMultiRetrived -> " + message.getReceivedStatus().isMultipleReceive());
                    if (message.getSenderUserId() != null && message.getSenderUserId().equals(this.getCurrentUserId())) {
                        RLog.w("RongIMClient", "received signal msg from the same account of different endpoints , do nothing.");
                        return true;
                    }
                }

                byte var10 = -1;
                switch(value.hashCode()) {
                    case -1919927324:
                        if (value.equals("RC:EncryptResponseMsg")) {
                            var10 = 1;
                        }
                        break;
                    case -1747006066:
                        if (value.equals("RC:EncryptRequestMsg")) {
                            var10 = 0;
                        }
                        break;
                    case -1659202549:
                        if (value.equals("RC:EncryptCancelMsg")) {
                            var10 = 3;
                        }
                        break;
                    case -941711427:
                        if (value.equals("RC:EncryptConfirmMsg")) {
                            var10 = 2;
                        }
                        break;
                    case -173850244:
                        if (value.equals("RC:EncryptTerminateMsg")) {
                            var10 = 4;
                        }
                }

                switch(var10) {
                    case 0:
                        RCEncryptRequestMessage requestMessage = (RCEncryptRequestMessage)content;
                        final String reqeustEncId = requestMessage.getRequesterEncId();
                        this.signalBuff.put(reqeustEncId, message);
                        RLog.d("RongIMClient", reqeustEncId + " in buff at " + System.currentTimeMillis());
                        this.mWorkHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (RongIMClient.this.signalBuff.containsKey(reqeustEncId)) {
                                    Message msgBuff = (Message)RongIMClient.this.signalBuff.get(reqeustEncId);
                                    String targetId = msgBuff.getTargetId();
                                    RCEncryptRequestMessage content = (RCEncryptRequestMessage)msgBuff.getContent();
                                    RongIMClient.this.sendResponseMsg(content, targetId);
                                    RLog.d("RongIMClient", "reqEncId ->" + reqeustEncId + " request from buff handled at " + System.currentTimeMillis());
                                }

                            }
                        }, 200L);
                        break;
                    case 1:
                        RCEncryptResponseMessage responseReceiveMsg = (RCEncryptResponseMessage)content;
                        if (responseReceiveMsg.getResponserEncId() == null) {
                            RLog.e("RongIMClient", "error RC:EncryptResponseMsg -> responseEncId is null.");
                            return true;
                        }

                        if (responseReceiveMsg.getRequesterEncId() == null) {
                            RLog.e("RongIMClient", "error RC:EncryptResponseMsg -> requestEncId is null.");
                            return true;
                        }

                        String encTargetIdInResp = responseReceiveMsg.getRequesterEncId() + ";;;" + targetId;
                        RCEncryptedSession session = this.mLibHandler.getEncryptedConversation(encTargetIdInResp);
                        if (session == null) {
                            RLog.e("RongIMClient", "getEncryptedConversation returns null when onResponse with -> " + encTargetIdInResp + " isRetrived -> " + message.getReceivedStatus().isRetrieved());
                            return true;
                        }

                        RLog.d("RongIMClient", "session status -> " + session.getEncStatus());
                        if (session.getEncStatus() == 3) {
                            this.sendCancelMessage(targetId, responseReceiveMsg, session);
                        } else {
                            RCSecretKey secretKey = this.getRcSecretKey(responseReceiveMsg);
                            if (secretKey == null) {
                                RLog.e("RongIMClient", "get secretKey is null, when sending confirmMessage.");
                                return true;
                            }

                            this.sendConfirmMessage(targetId, responseReceiveMsg, secretKey, session);
                            this.updateSessionStateAndCallback(responseReceiveMsg.getRequesterEncId(), targetId, responseReceiveMsg.getResponserEncId(), secretKey, session);
                        }
                        break;
                    case 2:
                        RCEncryptConfirmMessage confirmMsgRec = (RCEncryptConfirmMessage)content;
                        this.handleRetrivedConfirmMessage(message, left, confirmMsgRec);
                        String encTargetIdInConfirm = confirmMsgRec.getResponserEncId() + ";;;" + targetId;
                        RCEncryptedSession encryptedConversation = this.mLibHandler.getEncryptedConversation(encTargetIdInConfirm);
                        if (encryptedConversation == null) {
                            RLog.e("RongIMClient", "getEncryptedConversation return null,there should be error in request codes.");
                            return true;
                        }

                        if (this.updateConfirmStatusAndCallback(encTargetIdInConfirm, encryptedConversation)) {
                            return true;
                        }
                        break;
                    case 3:
                        RCEncryptCancelMessage cancleMsg = (RCEncryptCancelMessage)content;
                        if (this.handleRetrivedCancelMessage(message, left, cancleMsg)) {
                            return true;
                        }

                        String encTargetIdInCancel = cancleMsg.getResponserEncId() + ";;;" + targetId;
                        RCEncryptedSession encSession2Cancel = this.mLibHandler.getEncryptedConversation(encTargetIdInCancel);
                        RLog.d("RongIMClient", "cancleResponserEncId -> " + encTargetIdInCancel);
                        if (encSession2Cancel == null) {
                            RLog.w("RongIMClient", "can not find encrypted conersation while canceling the session with the targetID -> " + encTargetIdInCancel);
                            return true;
                        }

                        this.updateCancelStatusAndCallback(encTargetIdInCancel, encSession2Cancel);
                        break;
                    case 4:
                        RCEncryptTerminateMessage terminateMessage = (RCEncryptTerminateMessage)content;
                        String requesterEncId = terminateMessage.getReceiverEncId();
                        requesterEncId = requesterEncId == null ? "" : requesterEncId;
                        if (this.signalBuff.containsKey(requesterEncId)) {
                            this.signalBuff.remove(requesterEncId);
                            return true;
                        }

                        String terminateTargetId = terminateMessage.getReceiverEncId() + ";;;" + targetId;
                        RCEncryptedSession encSession2Terminate = this.mLibHandler.getEncryptedConversation(terminateTargetId);
                        if (encSession2Terminate == null) {
                            RLog.w("RongIMClient", "can not find encrypted conversation with encTargetId ->" + terminateTargetId);
                            return true;
                        }

                        String senderEncId = terminateMessage.getSenderEncId();
                        if (senderEncId == null || !senderEncId.equals(encSession2Terminate.getRemoteEncId())) {
                            return true;
                        }

                        this.updateTerinateStatusAndCallback(terminateTargetId, encSession2Terminate);
                }

                return false;
            }
        }
    }

    private boolean updateConfirmStatusAndCallback(String encTargetIdInConfirm, RCEncryptedSession encryptedConversation) throws RemoteException {
        encryptedConversation.setEncStatus(3);
        boolean isEstablished = this.mLibHandler.setEncryptedConversation(encTargetIdInConfirm, encryptedConversation);
        if (isEstablished) {
            this.mEncSessionConListener.onEncryptedSessionEstablished(encTargetIdInConfirm);
            return false;
        } else {
            RLog.e("RongIMClient", "setEncryptedConversation failed while updating encStatus.");
            return true;
        }
    }

    private void updateTerinateStatusAndCallback(String terminateTargetId, RCEncryptedSession encSession2Terminate) throws RemoteException {
        encSession2Terminate.setEncStatus(5);
        this.mLibHandler.setEncryptedConversation(terminateTargetId, encSession2Terminate);
        if (this.mEncSessionConListener != null) {
            this.mEncSessionConListener.onEncryptedSessionTerminated(terminateTargetId);
        }

    }

    private void updateCancelStatusAndCallback(String encTargetIdInCancel, RCEncryptedSession encSession2Cancel) throws RemoteException {
        encSession2Cancel.setEncStatus(4);
        if (this.mLibHandler.setEncryptedConversation(encTargetIdInCancel, encSession2Cancel)) {
            if (this.mEncSessionConListener != null) {
                this.mEncSessionConListener.onEncryptedSessionCanceled(encTargetIdInCancel);
            }
        } else {
            RLog.e("RongIMClient", "setEncryptedConversation failed when receive EncryptCancelMsg.");
        }

    }

    private boolean handleRetrivedCancelMessage(Message message, int left, RCEncryptCancelMessage cancleMsg) {
        String requesterEncId = cancleMsg.getRequesterEncId();
        String responserEncId = cancleMsg.getResponserEncId();
        if (this.signalBuff.containsKey(requesterEncId)) {
            this.signalBuff.remove(requesterEncId);
            RLog.d("RongIMClient", "req -> " + requesterEncId + "resp -> " + responserEncId + " cancel from buff at " + System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }

    private boolean handleRetrivedConfirmMessage(Message message, int left, RCEncryptConfirmMessage confirmMsgRec) {
        String requesterEncId = confirmMsgRec.getRequesterEncId();
        if (this.signalBuff.containsKey(requesterEncId)) {
            this.signalBuff.remove(requesterEncId);
            RLog.d("RongIMClient", "confirm from buff at " + System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }

    private RCSecretKey getRcSecretKey(RCEncryptResponseMessage responseReceiveMsg) {
        String yb = responseReceiveMsg.getResponserKey();
        return RCDHCodecTool.obtainWithEncId(responseReceiveMsg.getRequesterEncId()).genSecretKey(RCDHCodecTool.fromString2RCDHPublicKey(yb));
    }

    private void sendConfirmMessage(String targetId, RCEncryptResponseMessage responseReceiveMsg, RCSecretKey secretKey, RCEncryptedSession session) {
        RCEncryptConfirmMessage confirmMessage = new RCEncryptConfirmMessage();
        confirmMessage.setRequesterEncId(responseReceiveMsg.getRequesterEncId());
        confirmMessage.setResponserEncId(responseReceiveMsg.getResponserEncId());
        this.sendMessage(ConversationType.PRIVATE, targetId, confirmMessage, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                RLog.d("RongIMClient", "confirm message send success.");
            }

            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                RLog.w("RongIMClient", "confirm message send failed ,error occurs with error code : " + errorCode);
            }
        });
    }

    private void sendCancelMessage(String targetId, RCEncryptResponseMessage responseReceiveMsg, RCEncryptedSession session) {
        RCEncryptCancelMessage cancelMsg = new RCEncryptCancelMessage();
        cancelMsg.setRequesterEncId(responseReceiveMsg.getRequesterEncId());
        cancelMsg.setResponserEncId(responseReceiveMsg.getResponserEncId());
        RLog.d("RongIMClient", "set respEncIdInResp in cancel msg -> " + responseReceiveMsg.getResponserEncId() + Thread.currentThread().getName());
        this.sendMessage(ConversationType.PRIVATE, targetId, cancelMsg, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                RLog.d("RongIMClient", "send cancelMsg successfully.");
            }

            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                RLog.d("RongIMClient", "send cancelMsg error." + Thread.currentThread().getName());
            }
        });
    }

    private void updateSessionStateAndCallback(String requestEncIdInResp, String targetId, String responserEncIdInResp, RCSecretKey secretKey, RCEncryptedSession session) {
        try {
            String encTargetId = requestEncIdInResp + ";;;" + targetId;
            session.setRemoteEncId(responserEncIdInResp);
            session.setEncKey(secretKey.toString());
            session.setEncStatus(3);
            boolean isStatusSet = this.mLibHandler.setEncryptedConversation(encTargetId, session);
            if (isStatusSet) {
                if (this.mEncSessionConListener != null) {
                    this.mEncSessionConListener.onEncryptedSessionEstablished(encTargetId);
                }
            } else {
                RLog.e("RongIMClient", "setEncryptedConversation failed when send confirm ms");
            }
        } catch (RemoteException var8) {
            RLog.e("RongIMClient", "updateSessionStateAndCallback", var8);
        }

    }

    private boolean sendResponseMsg(RCEncryptRequestMessage content, final String targetId) {
        String ya = content.getRequesterKey();
        RCDHPublicKey YA = RCDHCodecTool.fromString2RCDHPublicKey(ya);
        final String requestEncId = content.getRequesterEncId();
        if (requestEncId == null) {
            RLog.e("RongIMClient", "error in onReceived -> RC:EncryptRequestMsg requestEncId is null!");
            return true;
        } else {
            final String responseEncId = RCDHCodecTool.genEncId();
            final RCDHCodecTool tool = RCDHCodecTool.obtainWithEncId(responseEncId);
            RLog.d("RongIMClient", "responseEncId -> " + responseEncId);
            RCSecretKey rcSecretKey = tool == null ? null : tool.genSecretKey(YA);
            if (rcSecretKey == null) {
                RLog.e("RongIMClient", "genSecretKey is null");
                return true;
            } else {
                RCEncryptResponseMessage responseMessage = new RCEncryptResponseMessage();
                responseMessage.setRequesterEncId(requestEncId);
                responseMessage.setResponserEncId(responseEncId);
                KeyPair localKeypair = tool.getOrCreateLocalKeyPair((DHParameterSpec)null);
                if (localKeypair == null) {
                    RLog.e("RongIMClient", "create localt keyPair failed!");
                    return true;
                } else {
                    if (localKeypair.getPublic() != null && localKeypair.getPublic() instanceof RCDHPublicKey) {
                        responseMessage.setResponserKey(tool.getRCPubKey().toString());
                    }

                    this.sendMessage(ConversationType.PRIVATE, targetId, responseMessage, (String)null, (String)null, new ISendMessageCallback() {
                        public void onAttached(Message message) {
                        }

                        public void onSuccess(Message message) {
                            RCEncryptedSession encryptedSession = new RCEncryptedSession();
                            encryptedSession.setTargetId(responseEncId + ";;;" + targetId);
                            encryptedSession.setRemoteEncId(requestEncId);
                            encryptedSession.setEncXA(tool.getRCPriKey().toString());
                            RCSecretKey secretKeyByencId = RCDHCodecTool.getRCSecretKeyByEncId(responseEncId);

                            assert secretKeyByencId != null;

                            encryptedSession.setEncKey(secretKeyByencId.toString());
                            encryptedSession.setEncStatus(2);

                            try {
                                if (RongIMClient.this.mLibHandler.createEncryptedConversation(responseEncId + ";;;" + targetId, encryptedSession)) {
                                    if (RongIMClient.this.mEncSessionConListener != null) {
                                        RongIMClient.this.mEncSessionConListener.onEncryptedSessionResponse(responseEncId + ";;;" + targetId);
                                    }
                                } else {
                                    RLog.e("RongIMClient", "createEncryptedConversation failed when sending responseMsg!");
                                }
                            } catch (RemoteException var5) {
                                RLog.e("RongIMClient", "sendResponseMsg", var5);
                            }

                        }

                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            RLog.w("RongIMClient", "response message send failed ,error occurs with error code : " + errorCode);
                        }
                    });
                    return false;
                }
            }
        }
    }

    public void getMessageByUid(final String uid, final RongIMClient.ResultCallback<Message> callback) {
        if (TextUtils.isEmpty(uid)) {
            RLog.e("RongIMClient", "getMessageByUid uid is empty!");
        } else {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            if (callback != null) {
                                Message message = RongIMClient.this.mLibHandler.getMessageByUid(uid);
                                callback.onCallback(message);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getMessageByUid", var2);
                        }

                    }
                }
            });
        }
    }

    public void switchAppKey(String appKey) {
        RLog.d("RongIMClient", "switchAppKey.");
        if (this.mLibHandler == null) {
            RLog.e("RongIMClient", "IPC_DISCONNECT");
        } else {
            try {
                this.mLibHandler.switchAppKey(appKey, DeviceUtils.getDeviceId(this.mContext, appKey));
            } catch (RemoteException var3) {
                RLog.e("RongIMClient", "switchAppKey", var3);
            }

            mNaviServer = null;
            mFileServer = null;
            if (isPushEnabled) {
                RongPushClient.stopService(this.mContext);
            }

        }
    }

    public RealTimeLocationErrorCode getRealTimeLocation(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null) {
            int code = RealTimeLocationManager.getInstance().setupRealTimeLocation(conversationType, targetId);
            return RealTimeLocationErrorCode.valueOf(code);
        } else {
            RLog.e("RongIMClient", "getRealTimeLocation Type or id is null!");
            return null;
        }
    }

    public RealTimeLocationErrorCode startRealTimeLocation(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null) {
            int code = RealTimeLocationManager.getInstance().startRealTimeLocation(conversationType, targetId);
            return RealTimeLocationErrorCode.valueOf(code);
        } else {
            RLog.e("RongIMClient", "startRealTimeLocation Type or id is null!");
            return null;
        }
    }

    public RealTimeLocationErrorCode joinRealTimeLocation(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null) {
            int code = RealTimeLocationManager.getInstance().joinRealTimeLocation(conversationType, targetId);
            return RealTimeLocationErrorCode.valueOf(code);
        } else {
            RLog.e("RongIMClient", "joinRealTimeLocation Type or id is null!");
            return null;
        }
    }

    public void quitRealTimeLocation(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null) {
            RealTimeLocationManager.getInstance().quitRealTimeLocation(conversationType, targetId);
        } else {
            RLog.e("RongIMClient", "quitRealTimeLocation Type or id is null!");
        }
    }

    public List<String> getRealTimeLocationParticipants(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null && RealTimeLocationManager.getInstance() != null) {
            return RealTimeLocationManager.getInstance().getRealTimeLocationParticipants(conversationType, targetId);
        } else {
            RLog.e("RongIMClient", "getRealTimeLocationParticipants Type or id is null!");
            return null;
        }
    }

    public RealTimeLocationStatus getRealTimeLocationCurrentState(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null) {
            return RealTimeLocationManager.getInstance().getRealTimeLocationCurrentState(conversationType, targetId);
        } else {
            RLog.e("RongIMClient", "getRealTimeLocationCurrentState Type or id is null!");
            return RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE;
        }
    }

    public void addRealTimeLocationListener(ConversationType conversationType, String targetId, final RongIMClient.RealTimeLocationListener listener) {
        if (conversationType != null && targetId != null && RealTimeLocationManager.getInstance() != null) {
            RealTimeLocationManager.getInstance().addListener(conversationType, targetId, new RealTimeLocationObserver() {
                public void onStatusChange(final RealTimeLocationStatus status) {
                    RongIMClient.mHandler.post(new Runnable() {
                        public void run() {
                            if (listener != null) {
                                listener.onStatusChange(status);
                            }

                        }
                    });
                }

                public void onReceiveLocation(final double latitude, final double longitude, final String userId) {
                    RongIMClient.mHandler.post(new Runnable() {
                        public void run() {
                            if (listener != null) {
                                listener.onReceiveLocation(latitude, longitude, userId);
                            }

                        }
                    });
                }

                public void onReceiveLocationWithType(final double latitude, final double longitude, final RealTimeLocationType type, final String userId) {
                    RongIMClient.mHandler.post(new Runnable() {
                        public void run() {
                            if (listener != null) {
                                listener.onReceiveLocationWithType(latitude, longitude, type, userId);
                            }

                        }
                    });
                }

                public void onParticipantsJoin(final String userId) {
                    RongIMClient.mHandler.post(new Runnable() {
                        public void run() {
                            if (listener != null) {
                                listener.onParticipantsJoin(userId);
                            }

                        }
                    });
                }

                public void onParticipantsQuit(final String userId) {
                    RongIMClient.mHandler.post(new Runnable() {
                        public void run() {
                            if (listener != null) {
                                listener.onParticipantsQuit(userId);
                            }

                        }
                    });
                }

                public void onError(final RealTimeLocationErrorCode errorCode) {
                    RongIMClient.mHandler.post(new Runnable() {
                        public void run() {
                            if (listener != null) {
                                listener.onError(errorCode);
                            }

                        }
                    });
                }
            });
        } else {
            RLog.e("RongIMClient", "addRealTimeLocationListener Type or id is null!");
        }
    }

    public void removeRealTimeLocationObserver(ConversationType conversationType, String targetId) {
        if (conversationType != null && targetId != null) {
            RealTimeLocationManager.getInstance().removeListener(conversationType, targetId);
        } else {
            RLog.e("RongIMClient", "removeRealTimeLocationObserver Type or id is null!");
        }
    }

    public void updateRealTimeLocationStatus(ConversationType conversationType, String targetId, double latitude, double longitude, RealTimeLocationType realTimeLocationType) {
        if (conversationType != null && targetId != null) {
            RealTimeLocationManager.getInstance().updateLocation(conversationType, targetId, latitude, longitude, realTimeLocationType);
        } else {
            RLog.e("RongIMClient", "updateRealTimeLocationStatus Type or id is null!");
        }
    }

    public Collection<TypingStatus> getTypingUserListFromConversation(ConversationType conversationType, String targetId) {
        return TypingMessageManager.getInstance().getTypingUserListFromConversation(conversationType, targetId);
    }

    public void sendTypingStatus(ConversationType conversationType, String targetId, String typingContentType) {
        TypingMessageManager.getInstance().sendTypingMessage(conversationType, targetId, typingContentType);
    }

    public static void setTypingStatusListener(RongIMClient.TypingStatusListener listener) {
        TypingMessageManager.getInstance().setTypingMessageStatusListener(listener);
    }

    public static void setRCLogInfoListener(final RongIMClient.RCLogInfoListener listener) {
        FwLog.setLogListener(new ILogListener() {
            public void onLogEvent(String log) {
                listener.onRCLogInfoOccurred(log);
            }
        });
    }

    public void sendReadReceiptMessage(ConversationType conversationType, String targetId, long timestamp) {
        if (conversationType != ConversationType.PRIVATE && conversationType != ConversationType.ENCRYPTED) {
            RLog.e("RongIMClient", "sendReadReceiptMessage conversationType only support PRIVATE and ENCRYPTED");
        } else if (TextUtils.isEmpty(targetId)) {
            RLog.e("RongIMClient", "sendReadReceiptMessage targetId is empty");
        } else if (timestamp <= 0L) {
            RLog.e("RongIMClient", "sendReadReceiptMessage timestamp is error");
        } else {
            ReadReceiptMessage readRecMsg = new ReadReceiptMessage(timestamp);
            this.sendMessage(conversationType, targetId, readRecMsg, (String)null, (String)null, (ISendMessageCallback)null);
        }
    }

    public void sendReadReceiptMessage(ConversationType conversationType, String targetId, long timestamp, ISendMessageCallback callback) {
        if (conversationType != ConversationType.PRIVATE && conversationType != ConversationType.ENCRYPTED) {
            RLog.e("RongIMClient", "sendReadReceiptMessage conversationType only support PRIVATE and ENCRYPTED");
            if (callback != null) {
                callback.onError((Message)null, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else if (TextUtils.isEmpty(targetId)) {
            RLog.e("RongIMClient", "sendReadReceiptMessage targetId is empty");
            if (callback != null) {
                callback.onError((Message)null, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else if (timestamp <= 0L) {
            RLog.e("RongIMClient", "sendReadReceiptMessage timestamp is error");
            if (callback != null) {
                callback.onError((Message)null, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            ReadReceiptMessage readRecMsg = new ReadReceiptMessage(timestamp);
            this.sendMessage(conversationType, targetId, readRecMsg, (String)null, (String)null, callback);
        }
    }

    public static void setReadReceiptListener(RongIMClient.ReadReceiptListener listener) {
        sReadReceiptListener = listener;
    }

    public void setOnReceiveDestructionMessageListener(RongIMClient.OnReceiveDestructionMessageListener listener) {
        this.mOnReceiveDestructionMessageListener = listener;
    }

    public void startCustomService(String kefuId, ICustomServiceListener listener, CSCustomServiceInfo customServiceInfo) {
        CustomServiceManager.getInstance().startCustomService(kefuId, listener, customServiceInfo);
    }

    public void selectCustomServiceGroup(String kefuId, String groupId) {
        this.sendChangeModelMessage(kefuId, groupId);
    }

    private void sendChangeModelMessage(String kefuId, String groupId) {
        CustomServiceManager.getInstance().sendChangeModelMessage(kefuId, groupId);
    }

    public void switchToHumanMode(String kefuId) {
        CustomServiceManager.getInstance().switchToHumanMode(kefuId);
    }

    public void evaluateCustomService(String kefuId, boolean isRobotResolved, String knowledgeId) {
        CustomServiceManager.getInstance().evaluateCustomService(kefuId, isRobotResolved, knowledgeId);
    }

    public void evaluateCustomService(String kefuId, int source, String suggest, String dialogId) {
        CustomServiceManager.getInstance().evaluateCustomService(kefuId, source, CSEvaSolveStatus.RESOLVED, suggest, dialogId);
    }

    public void evaluateCustomService(String kefuId, int source, CSEvaSolveStatus resolvestatus, String tagText, String suggest, String dialogId, String extra) {
        CustomServiceManager.getInstance().evaluateCustomService(kefuId, source, resolvestatus, tagText, suggest, dialogId, extra);
    }

    public void evaluateCustomService(String kefuId, int source, CSEvaSolveStatus solveStatus, String suggest, String dialogId) {
        CustomServiceManager.getInstance().evaluateCustomService(kefuId, source, solveStatus, suggest, dialogId);
    }

    public void leaveMessageCustomService(String kefuId, Map<String, String> contentMap, RongIMClient.OperationCallback operationCallback) {
        CustomServiceManager.getInstance().leaveMessageToCustomService(kefuId, contentMap, operationCallback);
    }

    public void stopCustomService(String kefuId) {
        CustomServiceManager.getInstance().stopCustomService(kefuId);
    }

    public void setCustomServiceHumanEvaluateListener(OnHumanEvaluateListener listener) {
        CustomServiceManager.getInstance().setHumanEvaluateListener(listener);
    }

    public static void setServerInfo(String naviServer, String fileServer) {
        RLog.d("RongIMClient", "setServerInfo naviServer :" + naviServer + ", fileServer:" + fileServer);
        if (TextUtils.isEmpty(naviServer)) {
            RLog.e("RongIMClient", "setServerInfo naviServer should not be null.");
            throw new IllegalArgumentException("naviServer should not be null.");
        } else if (naviServer.split("\\.").length < 2) {
            RLog.e("RongIMClient", "setServerInfo naviServer 填写异常，请录入格式为 https://xxx.xxx.xxx http://xxx.xxx.xxx 或 xxx.xxx.xxx");
            throw new IllegalArgumentException("naviServer 填写异常，请录入格式为 https://xxx.xxx.xxx http://xxx.xxx.xxx 或 xxx.xxx.xxx");
        } else {
            FwLog.write(3, 1, LogTag.A_SET_SERVER_O.getTag(), "navi|file", new Object[]{naviServer, fileServer});
            mNaviServer = naviServer.trim();
            if (fileServer != null) {
                mFileServer = fileServer.trim();
            }

        }
    }

    public static void setStatisticDomain(String domain) {
        FwLog.write(3, 1, LogTag.A_SET_STATISTIC_SERVER_O.getTag(), "domain", new Object[]{domain});
        String strFormat = "http://%s/active.json";
        if (domain.toLowerCase().startsWith("http")) {
            strFormat = "%s/active.json";
        }

        PRIVATE_STATISTIC = String.format(strFormat, domain);
    }

    public void recallMessage(final Message message, String pushContent, final RongIMClient.ResultCallback<RecallNotificationMessage> callback) {
        if (this.mLibHandler == null) {
            RLog.e("RongIMClient", "recallMessage IPC 进程尚未运行。");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
            }

        } else {
            MessageTag messageTag = (MessageTag)RecallCommandMessage.class.getAnnotation(MessageTag.class);
            String objectName = messageTag.value();
            final RecallCommandMessage recallCommandMessage = new RecallCommandMessage(message.getUId());
            recallCommandMessage.setConversationType(message.getConversationType().getValue());
            recallCommandMessage.setTargetId(message.getTargetId());
            recallCommandMessage.setSentTime(message.getSentTime());
            recallCommandMessage.setUserInfo(message.getContent().getUserInfo());

            try {
                byte[] content = recallCommandMessage.encode();
                io.rong.imlib.IOperationCallback.Stub mRecallMessageCallback = new io.rong.imlib.IOperationCallback.Stub() {
                    public void onComplete() throws RemoteException {
                        RecallNotificationMessage recallNotificationMessage = new RecallNotificationMessage(RongIMClient.this.getCurrentUserId(), message.getSentTime(), message.getObjectName(), recallCommandMessage.isAdmin(), recallCommandMessage.isDelete());
                        MessageContent msgContent = message.getContent();
                        if (msgContent instanceof TextMessage) {
                            TextMessage textMessage = (TextMessage)msgContent;
                            recallNotificationMessage.setRecallContent(textMessage.getContent());
                            recallNotificationMessage.setRecallActionTime(System.currentTimeMillis());
                        }

                        recallNotificationMessage.setUserInfo(recallCommandMessage.getUserInfo());
                        byte[] data = recallNotificationMessage.encode();
                        MessageTag recallNotificationTag = (MessageTag)RecallNotificationMessage.class.getAnnotation(MessageTag.class);

                        try {
                            RongIMClient.this.mLibHandler.setMessageContent(message.getMessageId(), data, recallNotificationTag.value());
                            if (callback != null) {
                                callback.onCallback(recallNotificationMessage);
                            }
                        } catch (RemoteException var6) {
                            RLog.e("RongIMClient", "recallMessage", var6);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }

                    public void onFailure(int errorCode) throws RemoteException {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                        }

                    }
                };
                this.mLibHandler.recallMessage(objectName, content, pushContent, 0, message.getTargetId(), message.getConversationType().getValue(), mRecallMessageCallback);
            } catch (RemoteException var9) {
                RLog.e("RongIMClient", "recallMessage", var9);
                if (callback != null) {
                    callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                }
            }

        }
    }

    public void getUnreadMentionedMessages(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<List<Message>> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            List<Message> messages = RongIMClient.this.mLibHandler.getUnreadMentionedMessages(conversationType.getValue(), targetId);
                            if (callback != null) {
                                callback.onCallback(messages);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getUnreadMentionedMessages", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "the parameter of targetId or ConversationType is error!");
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        }
    }

    public static void setOnRecallMessageListener(RongIMClient.OnRecallMessageListener listener) {
        sOnRecallMessageListener = listener;
    }

    public void sendMediaMessage(final Message message, final String pushContent, final String pushData, ISendMediaMessageCallback callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            MediaMessageContent mediaMessageContent = (MediaMessageContent)message.getContent();
            if (mediaMessageContent.getMediaUrl() != null) {
                this.sendMessage(message, pushContent, pushData, callback);
            } else {
                Uri localPath = mediaMessageContent.getLocalPath();
                if (!FileUtils.isFileExistsWithUri(this.mContext, localPath)) {
                    RLog.e("RongIMClient", "localPath does not exist!");
                    if (callback != null) {
                        callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                    }

                    return;
                }

                final IpcCallbackProxy<ISendMediaMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                RongIMClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }
                                });
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.sendMediaMessage(message, pushContent, pushData, new io.rong.imlib.ISendMediaMessageCallback.Stub() {
                                    public void onAttached(final Message messagex) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onAttached(messagex);
                                                }
                                            });
                                        }

                                    }

                                    public void onProgress(final Message messagex, final int progress) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onProgress(messagex, progress);
                                                }
                                            });
                                        }

                                    }

                                    public void onSuccess(final Message messagex) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onSuccess(messagex);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onError(final Message messagex, final int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onError(messagex, RongIMClient.ErrorCode.valueOf(errorCode));
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onCanceled(final Message messagex) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onCanceled(messagex);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }
                                });
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "sendMediaMessage", var2);
                            }

                        }
                    }
                });
            }

        }
    }

    public void sendDirectionalMediaMessage(final Message message, final String[] userIds, final String pushContent, final String pushData, ISendMediaMessageCallback callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            MediaMessageContent mediaMessageContent = (MediaMessageContent)message.getContent();
            if (mediaMessageContent.getMediaUrl() != null) {
                this.sendMessage(message, pushContent, pushData, callback);
            } else {
                if (mediaMessageContent.getLocalPath() == null) {
                    RLog.e("RongIMClient", "Media file does not exist!");
                    if (callback != null) {
                        callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                    }

                    return;
                }

                String localPath = mediaMessageContent.getLocalPath().toString();
                String abPath = localPath.substring(7);
                if (!localPath.startsWith("file") || !(new File(abPath)).exists()) {
                    RLog.e("RongIMClient", localPath + " does not exist!");
                    if (callback != null) {
                        callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                    }

                    return;
                }

                final IpcCallbackProxy<ISendMediaMessageCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (ipcCallbackProxy.callback != null) {
                                RongIMClient.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onError(message, RongIMClient.ErrorCode.IPC_DISCONNECT);
                                        ipcCallbackProxy.callback = null;
                                    }
                                });
                            }

                        } else {
                            try {
                                RongIMClient.this.mLibHandler.sendDirectionalMediaMessage(message, userIds, pushContent, pushData, new io.rong.imlib.ISendMediaMessageCallback.Stub() {
                                    public void onAttached(final Message messagex) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onAttached(messagex);
                                                }
                                            });
                                        }

                                    }

                                    public void onProgress(final Message messagex, final int progress) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onProgress(messagex, progress);
                                                }
                                            });
                                        }

                                    }

                                    public void onSuccess(final Message messagex) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onSuccess(messagex);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onError(final Message messagex, final int errorCode) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onError(messagex, RongIMClient.ErrorCode.valueOf(errorCode));
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }

                                    public void onCanceled(final Message messagex) throws RemoteException {
                                        if (ipcCallbackProxy.callback != null) {
                                            RongIMClient.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    ((ISendMediaMessageCallback)ipcCallbackProxy.callback).onCanceled(messagex);
                                                    ipcCallbackProxy.callback = null;
                                                }
                                            });
                                        }

                                    }
                                });
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "sendDirectionalMediaMessage", var2);
                            }

                        }
                    }
                });
            }

        }
    }

    public void sendMediaMessage(final Message message, final String pushContent, final String pushData, final ISendMediaMessageCallbackWithUploader callback) {
        if (this.filterSendMessage(message)) {
            if (callback != null) {
                callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            MediaMessageContent mediaMessageContent = (MediaMessageContent)message.getContent();
            if (!FileUtils.isFileExistsWithUri(this.mContext, mediaMessageContent.getLocalPath())) {
                RLog.e("RongIMClient", "Media file does not exist!");
                if (callback != null) {
                    callback.onError(message, RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            } else {
                this.insertIncomingMessage(message.getConversationType(), message.getTargetId(), this.mCurrentUserId, (ReceivedStatus)null, message.getContent(), new RongIMClient.ResultCallback<Message>() {
                    public void onSuccess(Message messagex) {
                        messagex.setSentStatus(SentStatus.SENDING);
                        RongIMClient.this.setMessageSentStatus(messagex, (RongIMClient.ResultCallback)null);
                        if (callback != null) {
                            callback.onAttached(messagex, new MediaMessageUploader(messagex, pushContent, pushData, callback));
                        }

                    }

                    public void onError(RongIMClient.ErrorCode e) {
                        if (callback != null) {
                            callback.onError(message, RongIMClient.ErrorCode.RC_MSG_SEND_FAIL);
                        }

                    }
                });
            }
        }
    }

    public void sendReadReceiptRequest(final Message message, final RongIMClient.OperationCallback callback) {
        if (message == null) {
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else if (!ConversationType.GROUP.equals(message.getConversationType()) && !ConversationType.DISCUSSION.equals(message.getConversationType())) {
            RLog.w("RongIMClient", "only group and discussion could send read receipt request.");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            ReadReceiptRequestMessage requestMessage = new ReadReceiptRequestMessage(message.getUId());
            this.sendMessage(message.getConversationType(), message.getTargetId(), requestMessage, (String)null, (String)null, new ISendMessageCallback() {
                public void onAttached(Message msg) {
                }

                public void onSuccess(Message msg) {
                    if (RongIMClient.this.mLibHandler == null) {
                        RLog.d("RongIMClient", "sendReadReceiptRequest mLibHandler is null");
                        if (callback != null) {
                            callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            ReadReceiptInfo readReceiptInfo = message.getReadReceiptInfo();
                            if (readReceiptInfo == null) {
                                readReceiptInfo = new ReadReceiptInfo();
                                message.setReadReceiptInfo(readReceiptInfo);
                            }

                            readReceiptInfo.setIsReadReceiptMessage(true);
                            RongIMClient.this.mLibHandler.updateReadReceiptRequestInfo(message.getUId(), readReceiptInfo.toJSON().toString());
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "sendReadReceiptRequest", var3);
                            if (callback != null) {
                                callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }

                            return;
                        }

                        if (callback != null) {
                            callback.onSuccess();
                        }

                    }
                }

                public void onError(Message msg, RongIMClient.ErrorCode errorCode) {
                    if (callback != null) {
                        callback.onError(errorCode);
                    }

                }
            });
        }
    }

    public void sendReadReceiptResponse(ConversationType type, String targetId, final List<Message> messageList, final RongIMClient.OperationCallback callback) {
        if ((ConversationType.GROUP.equals(type) || ConversationType.DISCUSSION.equals(type)) && messageList != null && messageList.size() != 0) {
            ReadReceiptResponseMessage responseMessage = new ReadReceiptResponseMessage(messageList);
            int size = responseMessage.getSenderIdSet().size();
            this.sendDirectionalMessage(type, targetId, responseMessage, (String[])responseMessage.getSenderIdSet().toArray(new String[size]), (String)null, (String)null, new ISendMessageCallback() {
                public void onAttached(Message message) {
                }

                public void onSuccess(Message message) {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        RongIMClient.this.mWorkHandler.post(new Runnable() {
                            public void run() {
                                try {
                                    Iterator var1 = messageList.iterator();

                                    while(var1.hasNext()) {
                                        Message msg = (Message)var1.next();
                                        ReadReceiptInfo readReceiptInfo = msg.getReadReceiptInfo();
                                        if (readReceiptInfo == null) {
                                            readReceiptInfo = new ReadReceiptInfo();
                                            msg.setReadReceiptInfo(readReceiptInfo);
                                        }

                                        readReceiptInfo.setHasRespond(true);
                                        RongIMClient.this.mLibHandler.updateReadReceiptRequestInfo(msg.getUId(), readReceiptInfo.toJSON().toString());
                                    }
                                } catch (RemoteException var4) {
                                    RLog.e("RongIMClient", "sendReadReceiptResponse", var4);
                                    if (callback != null) {
                                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                    }

                                    return;
                                }

                                if (callback != null) {
                                    callback.onCallback();
                                }

                            }
                        });
                    }
                }

                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                    if (callback != null) {
                        callback.onError(errorCode);
                    }

                }
            });
        } else {
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        }
    }

    public void syncConversationReadStatus(ConversationType type, String targetId, long timestamp, final RongIMClient.OperationCallback callback) {
        SyncReadStatusMessage syncReadStatusMessage = new SyncReadStatusMessage(timestamp);
        String[] users = new String[]{this.getCurrentUserId()};
        this.sendDirectionalMessage(type, targetId, syncReadStatusMessage, users, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                if (callback != null) {
                    callback.onError(errorCode);
                }

            }
        });
    }

    public void setSyncConversationReadStatusListener(RongIMClient.SyncConversationReadStatusListener listener) {
        this.mSyncConversationReadStatusListener = listener;
    }

    public void searchConversations(final String keyword, final ConversationType[] conversationTypes, final String[] objectNames, final RongIMClient.ResultCallback<List<SearchConversationResult>> resultCallback) {
        if (!TextUtils.isEmpty(keyword) && conversationTypes != null && conversationTypes.length != 0 && objectNames != null && objectNames.length != 0) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (resultCallback != null) {
                            resultCallback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                        }

                    } else {
                        try {
                            int[] types = RongIMClient.this.convertTypes(conversationTypes);
                            List<SearchConversationResult> result = RongIMClient.this.mLibHandler.searchConversations(keyword, types, objectNames);
                            if (resultCallback != null) {
                                resultCallback.onCallback(result);
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "searchConversations", var3);
                        }

                    }
                }
            });
        } else {
            if (resultCallback != null) {
                resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void searchMessages(final ConversationType conversationType, final String targetId, final String keyword, final int count, final long beginTime, final RongIMClient.ResultCallback<List<Message>> resultCallback) {
        if (!TextUtils.isEmpty(keyword) && !TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (resultCallback != null) {
                            resultCallback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                        }

                    } else {
                        try {
                            List<Message> result = RongIMClient.this.mLibHandler.searchMessages(targetId, conversationType.getValue(), keyword, count, beginTime);
                            if (resultCallback != null) {
                                resultCallback.onCallback(result);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "searchMessages", var2);
                        }

                    }
                }
            });
        } else {
            if (resultCallback != null) {
                resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void searchMessagesByUser(final ConversationType conversationType, final String targetId, final String userId, int count, final long beginTime, final RongIMClient.ResultCallback<List<Message>> resultCallback) {
        if (count > 0 && conversationType != null) {
            final int requestCount;
            if (count > 100) {
                RLog.i("RongIMClient", "searchMessagesByUser : count > 100.");
                requestCount = 100;
            } else {
                requestCount = count;
            }

            if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(targetId)) {
                this.mWorkHandler.post(new Runnable() {
                    public void run() {
                        if (RongIMClient.this.mLibHandler == null) {
                            if (resultCallback != null) {
                                resultCallback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                            }

                        } else {
                            try {
                                List<Message> result = RongIMClient.this.mLibHandler.searchMessagesByUser(targetId, conversationType.getValue(), userId, requestCount, beginTime);
                                if (resultCallback != null) {
                                    resultCallback.onCallback(result);
                                }
                            } catch (RemoteException var2) {
                                RLog.e("RongIMClient", "searchMessagesByUser", var2);
                            }

                        }
                    }
                });
            } else {
                if (resultCallback != null) {
                    resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
                }

            }
        } else {
            RLog.e("RongIMClient", "searchMessagesByUser : count count <= 0 or conversationType is null !");
            if (resultCallback != null) {
                resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getHistoryMessages(final ConversationType conversationType, final String targetId, final long sentTime, final int before, final int after, final RongIMClient.ResultCallback<List<Message>> resultCallback) {
        if (sentTime > 0L && before >= 0 && after >= 0 && !TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (resultCallback != null) {
                            resultCallback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                        }

                    } else {
                        try {
                            List<Message> result = RongIMClient.this.mLibHandler.getMatchedMessages(targetId, conversationType.getValue(), sentTime, before, after);
                            if (resultCallback != null) {
                                resultCallback.onCallback(RongIMClient.this.filterDestructionMessage(conversationType, targetId, result));
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getHistoryMessages", var2);
                        }

                    }
                }
            });
        } else {
            if (resultCallback != null) {
                resultCallback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    private List<Message> filterDestructionMessage(ConversationType conversationType, String targetId, List<Message> messages) {
        if (messages == null) {
            return null;
        } else {
            List<Message> messageList = new ArrayList();
            List<Message> destructionMessages = new ArrayList();
            Iterator var6 = messages.iterator();

            while(true) {
                while(var6.hasNext()) {
                    Message message = (Message)var6.next();
                    if (message.getContent().isDestruct() && message.getReadTime() > 0L) {
                        if (!message.getMessageDirection().equals(MessageDirection.RECEIVE)) {
                            destructionMessages.add(message);
                            continue;
                        }

                        long delay = (System.currentTimeMillis() - message.getReadTime()) / 1000L;
                        if (delay >= message.getContent().getDestructTime()) {
                            destructionMessages.add(message);
                            continue;
                        }
                    }

                    messageList.add(message);
                }

                if (destructionMessages.size() > 0) {
                    Message[] deleteMessages = new Message[destructionMessages.size()];
                    destructionMessages.toArray(deleteMessages);
                    this.deleteRemoteMessages(conversationType, targetId, deleteMessages, new RongIMClient.OperationCallback() {
                        public void onSuccess() {
                        }

                        public void onError(RongIMClient.ErrorCode errorCode) {
                        }
                    });
                }

                return messageList;
            }
        }
    }

    public void getVendorToken(RongIMClient.ResultCallback<String> resultCallback) {
        final IpcCallbackProxy<RongIMClient.ResultCallback<String>> ipcCallbackProxy = new IpcCallbackProxy(resultCallback);
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (ipcCallbackProxy.callback != null) {
                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                        ipcCallbackProxy.callback = null;
                    }

                } else {
                    try {
                        RongIMClient.this.mLibHandler.getVendorToken(new io.rong.imlib.IStringCallback.Stub() {
                            public void onComplete(String string) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(string);
                                    ipcCallbackProxy.callback = null;
                                }

                            }

                            public void onFailure(int errorCode) throws RemoteException {
                                if (ipcCallbackProxy.callback != null) {
                                    ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                    ipcCallbackProxy.callback = null;
                                }

                            }
                        });
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "getVendorToken", var2);
                    }

                }
            }
        });
    }

    public void setPushLanguage(RongIMClient.PushLanguage language, RongIMClient.OperationCallback callback) {
        if (language == null) {
            RLog.d("RongIMClient", "[PushSetting] setPushLanguage language is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        } else {
            this.setPushSetting(RongIMClient.PushSettings.PUSH_SETTINGS_LANGUAGE, language.getMsg(), callback);
        }
    }

    public void setPushContentShowStatus(boolean showStatus, RongIMClient.OperationCallback callback) {
        RongIMClient.PushStatus showPushDetail = showStatus ? RongIMClient.PushStatus.STATUS_ON : RongIMClient.PushStatus.STATUS_OFF;
        this.setPushSetting(RongIMClient.PushSettings.PUSH_SETTINGS_SHOW_CONTENT, showPushDetail.getValue(), callback);
    }

    public void setPushReceiveStatus(boolean receiveStatus, RongIMClient.OperationCallback callback) {
        RongIMClient.PushStatus receivePush = receiveStatus ? RongIMClient.PushStatus.STATUS_ON : RongIMClient.PushStatus.STATUS_OFF;
        this.setPushSetting(RongIMClient.PushSettings.PUSH_SETTINGS_RECEIVE, receivePush.getValue(), callback);
    }

    private void setPushSetting(final RongIMClient.PushSettings status, final String value, RongIMClient.OperationCallback callback) {
        if (this.mLibHandler == null) {
            RLog.d("RongIMClient", "[PushSetting] setPushSetting mLibHandler is null");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
            }

        } else {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.setPushSetting(status.getValue(), value, new io.rong.imlib.ISetPushSettingCallback.Stub() {
                                public void onComplete() throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onCallback();
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setPushSetting", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                                ipcCallbackProxy.callback = null;
                            }
                        }

                    }
                }
            });
        }
    }

    public void getPushLanguage(final RongIMClient.ResultCallback<RongIMClient.PushLanguage> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                String language = "";

                try {
                    if (RongIMClient.this.mLibHandler != null) {
                        language = RongIMClient.this.mLibHandler.getPushSetting(RongIMClient.PushSettings.PUSH_SETTINGS_LANGUAGE.getValue());
                    } else {
                        callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        RLog.e("RongIMClient", "getPushLanguage: mLibHandler is null.");
                    }
                } catch (RemoteException var3) {
                    RLog.e("RongIMClient", "getPushLanguage", var3);
                }

                if (language == null) {
                    callback.onFail(RongIMClient.ErrorCode.UNKNOWN);
                } else if (language.equals(RongIMClient.PushLanguage.EN_US.getMsg())) {
                    callback.onCallback(RongIMClient.PushLanguage.EN_US);
                } else {
                    callback.onCallback(RongIMClient.PushLanguage.ZH_CN);
                }

            }
        });
    }

    public void getPushContentShowStatus(final RongIMClient.ResultCallback<Boolean> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                String contentShowStatus = "";

                try {
                    if (RongIMClient.this.mLibHandler != null) {
                        contentShowStatus = RongIMClient.this.mLibHandler.getPushSetting(RongIMClient.PushSettings.PUSH_SETTINGS_SHOW_CONTENT.getValue());
                    } else {
                        callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        RLog.e("RongIMClient", "getPushContentShowStatus: mLibHandler is null.");
                    }
                } catch (RemoteException var3) {
                    RLog.e("RongIMClient", "getPushContentShowStatus", var3);
                }

                if (contentShowStatus == null) {
                    callback.onFail(RongIMClient.ErrorCode.UNKNOWN);
                } else {
                    callback.onCallback(contentShowStatus.equals(RongIMClient.PushStatus.STATUS_ON.getValue()));
                }

            }
        });
    }

    public void getPushReceiveStatus(final RongIMClient.ResultCallback<Boolean> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.PARAMETER_ERROR);
                    }

                } else {
                    String receiveStatus = "";

                    try {
                        receiveStatus = RongIMClient.this.mLibHandler.getPushSetting(RongIMClient.PushSettings.PUSH_SETTINGS_RECEIVE.getValue());
                    } catch (RemoteException var3) {
                        RLog.e("RongIMClient", "getPushReceiveStatus", var3);
                    }

                    if (receiveStatus == null) {
                        callback.onFail(RongIMClient.ErrorCode.UNKNOWN);
                    } else {
                        callback.onCallback(receiveStatus.equals(RongIMClient.PushStatus.STATUS_ON.getValue()));
                    }

                }
            }
        });
    }

    public void getOfflineMessageDuration(final RongIMClient.ResultCallback<String> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                String offLineDuration = "";

                try {
                    offLineDuration = RongIMClient.this.mLibHandler.getOfflineMessageDuration();
                } catch (RemoteException var3) {
                    RLog.e("RongIMClient", "getOfflineMessageDuration", var3);
                }

                if (offLineDuration == null) {
                    callback.onFail(RongIMClient.ErrorCode.UNKNOWN);
                } else {
                    callback.onCallback(offLineDuration);
                }

            }
        });
    }

    public void setOfflineMessageDuration(final int duration, RongIMClient.ResultCallback<Long> callback) {
        if (duration >= 1 && duration <= 7) {
            final IpcCallbackProxy<RongIMClient.ResultCallback<Long>> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.setOfflineMessageDuration(String.valueOf(duration), new io.rong.imlib.ILongCallback.Stub() {
                                public void onComplete(long result) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onCallback(result);
                                        ipcCallbackProxy.callback = null;
                                    }

                                }

                                public void onFailure(int errorCode) throws RemoteException {
                                    if (ipcCallbackProxy.callback != null) {
                                        ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.valueOf(errorCode));
                                        ipcCallbackProxy.callback = null;
                                    }

                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setOfflineMessageDuration", var2);
                            if (ipcCallbackProxy.callback != null) {
                                ((RongIMClient.ResultCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "Parameter is error!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void setAppVer(String appVer) {
        FwLog.write(3, 1, LogTag.A_APP_VER_S.getTag(), "ver", new Object[]{appVer});
    }

    public void supportResumeBrokenTransfer(final String url, final RongIMClient.ResultCallback<Boolean> callback) {
        if (TextUtils.isEmpty(url) && callback != null) {
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        } else {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            boolean bool = RongIMClient.this.mLibHandler.supportResumeBrokenTransfer(url);
                            if (callback != null) {
                                callback.onCallback(bool);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "supportResumeBrokenTransfer", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        }
    }

    private void isFileDownloading(final String uid, final RongIMClient.ResultCallback<Boolean> callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else {
                    try {
                        boolean bool = RongIMClient.this.mLibHandler.getFileDownloadingStatus(uid);
                        if (callback != null) {
                            callback.onCallback(bool);
                        }
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "isFileDownloading", var2);
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                }
            }
        });
    }

    public void getTheFirstUnreadMessage(final ConversationType conversationType, final String targetId, final RongIMClient.ResultCallback<Message> callback) {
        if (!TextUtils.isEmpty(targetId) && conversationType != null) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (callback != null) {
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    } else {
                        try {
                            Message result = RongIMClient.this.mLibHandler.getTheFirstUnreadMessage(conversationType.getValue(), targetId);
                            if (callback != null) {
                                callback.onCallback(result);
                            }
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getTheFirstUnreadMessage", var2);
                            if (callback != null) {
                                callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            }
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "getConversation. the parameter of targetId or ConversationType is error!");
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        }
    }

    public boolean isFileDownloading(int messageId) {
        return this.isFileDownloading(messageId + "");
    }

    public boolean isFileDownloading(String uid) {
        final CountDownLatch latch = new CountDownLatch(1);
        final RongIMClient.ResultCallback.Result<Boolean> result = new RongIMClient.ResultCallback.Result();
        result.t = false;
        this.isFileDownloading(uid, new RongIMClient.SyncCallback<Boolean>() {
            public void onSuccess(Boolean bool) {
                if (bool != null) {
                    result.t = bool;
                } else {
                    RLog.e("RongIMClient", "removeConversation removeConversation is failure!");
                }

                latch.countDown();
            }

            public void onError(RongIMClient.ErrorCode code) {
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException var5) {
            RLog.e("RongIMClient", "isFileDownloading", var5);
            Thread.currentThread().interrupt();
        }

        return (Boolean)result.t;
    }

    public void setMessageReadTime(final long messageId, final long timestamp, final RongIMClient.OperationCallback callback) {
        this.mWorkHandler.post(new Runnable() {
            public void run() {
                if (RongIMClient.this.mLibHandler == null) {
                    if (callback != null) {
                        callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    }

                } else {
                    try {
                        boolean isSuccess = RongIMClient.this.mLibHandler.setMessageReadTime(messageId, timestamp);
                        if (callback != null) {
                            if (isSuccess) {
                                callback.onCallback();
                            } else {
                                callback.onError(RongIMClient.ErrorCode.UNKNOWN);
                            }
                        }
                    } catch (RemoteException var2) {
                        RLog.e("RongIMClient", "setMessageReadTime", var2);
                        if (callback != null) {
                            callback.onError(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }
                    }

                }
            }
        });
    }

    public void startEncryptedSession(String targetId) {
        if (this.mEncSessionConListener == null) {
            RLog.e("RongIMClient", "member of EncSessionConListener in RongIMClient instance is null, can not perform the session creation.");
        } else if (targetId == null) {
            RLog.e("RongIMClient", " startEncryptedSession - targetId is null! ");
        } else {
            String reqeustEncId = RCDHCodecTool.genEncId();
            RCDHCodecTool rcdhCodecTool = RCDHCodecTool.obtainWithEncId(reqeustEncId);
            rcdhCodecTool.getOrCreateLocalKeyPair((DHParameterSpec)null);
            this.sendRequestMsg(targetId, reqeustEncId, rcdhCodecTool);
        }
    }

    private void sendRequestMsg(final String targetId, final String encId, final RCDHCodecTool tool) {
        RCEncryptRequestMessage rcEncryptRequestMessage = new RCEncryptRequestMessage();
        rcEncryptRequestMessage.setRequesterEncId(encId);
        rcEncryptRequestMessage.setRequesterKey(tool.getRCPubKey().toString());
        this.sendMessage(ConversationType.PRIVATE, targetId, rcEncryptRequestMessage, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                RCEncryptedSession encryptedConversation = new RCEncryptedSession();
                encryptedConversation.setTargetId(encId + ";;;" + targetId);
                encryptedConversation.setRemoteEncId((String)null);
                encryptedConversation.setEncKey((String)null);
                encryptedConversation.setEncXA(tool.getRCPriKey().toString());
                encryptedConversation.setEncStatus(1);

                try {
                    if (RongIMClient.this.mLibHandler.createEncryptedConversation(encId + ";;;" + targetId, encryptedConversation) && RongIMClient.this.mEncSessionConListener != null) {
                        RongIMClient.this.mEncSessionConListener.onEncryptedSessionRequest(encId + ";;;" + targetId, true);
                    }
                } catch (RemoteException var4) {
                    RLog.e("RongIMClient", "sendRequestMsg", var4);
                }

            }

            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                RLog.w("RongIMClient", "request message send failed ,error occurs with error code : " + errorCode);
                if (RongIMClient.this.mEncSessionConListener != null) {
                    RongIMClient.this.mEncSessionConListener.onEncryptedSessionRequest(encId + ";;;" + targetId, false);
                }

            }
        });
    }

    public void quitEncryptedSession(final String targetId) {
        if (targetId == null) {
            RLog.e("RongIMClient", "quitEncryptedSession targetId is null! ");
        } else {
            try {
                String[] ids = targetId.split(";;;");
                if (ids.length != 2) {
                    RLog.e("RongIMClient", "quitEncrypteSession error while parsing the targetId -> " + targetId);
                    return;
                }

                if (this.mLibHandler == null) {
                    return;
                }

                final RCEncryptedSession encryptedConversation = this.mLibHandler.getEncryptedConversation(targetId);
                if (encryptedConversation == null) {
                    RLog.w("RongIMClient", "getEncryptedConversation returns null on call of quitEncryptedSession.");
                    return;
                }

                encryptedConversation.setEncStatus(5);
                String remoteEncId = encryptedConversation.getRemoteEncId();
                if (remoteEncId == null) {
                    RLog.e("RongIMClient", "removeEncId from encrypted conversaion is null! ");
                    return;
                }

                RCEncryptTerminateMessage terminateMessag = new RCEncryptTerminateMessage();
                terminateMessag.setSenderEncId(ids[0]);
                terminateMessag.setReceiverEncId(remoteEncId);
                this.sendMessage(ConversationType.PRIVATE, ids[1], terminateMessag, (String)null, (String)null, new ISendMessageCallback() {
                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                        try {
                            if (RongIMClient.this.mLibHandler != null && RongIMClient.this.mLibHandler.setEncryptedConversation(targetId, encryptedConversation) && RongIMClient.this.mLibHandler.removeEncryptedConversation(targetId)) {
                                RLog.d("RongIMClient", "send terminateMsg successfully,remove encConversation of ->" + targetId);
                            }
                        } catch (RemoteException var3) {
                            RLog.e("RongIMClient", "quitEncryptedSession", var3);
                        }

                    }

                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        RLog.w("RongIMClient", "terminateMessage send failed ,error occurs with error code : " + errorCode);
                    }
                });
            } catch (RemoteException var6) {
                RLog.e("RongIMClient", "quitEncryptedSession", var6);
            }

        }
    }

    public boolean clearEncryptedConversations() {
        try {
            if (this.mLibHandler != null) {
                return this.mLibHandler.clearEncryptedConversations();
            } else {
                RLog.e("RongIMClient", "clearEncryptedConversations: mLibHandler is null.");
                return false;
            }
        } catch (RemoteException var2) {
            RLog.e("RongIMClient", "clearEncryptedConversations", var2);
            return false;
        }
    }

    public int getEncryptedSessionStatus(String targetId) {
        try {
            RCEncryptedSession encryptedConversation = this.mLibHandler.getEncryptedConversation(targetId);
            if (encryptedConversation == null) {
                RLog.w("RongIMClient", "can not find encCoversation by targetId of " + targetId + " while calling getEncryptedSessionStatus()");
                return -1;
            } else {
                return encryptedConversation.getEncStatus();
            }
        } catch (RemoteException var3) {
            RLog.e("RongIMClient", "getEncryptedSessionStatus", var3);
            return -2;
        }
    }

    public List<RCEncryptedSession> getAllEncryptedConversations() {
        if (this.mLibHandler != null) {
            try {
                return this.mLibHandler.getAllEncryptedConversations();
            } catch (RemoteException var2) {
                RLog.e("RongIMClient", "getAllEncryptedConversations", var2);
            }
        }

        return null;
    }

    public int getVideoLimitTime() {
        if (this.mLibHandler != null) {
            try {
                return this.mLibHandler.getVideoLimitTime();
            } catch (RemoteException var2) {
                RLog.e("RongIMClient", "getVideoLimitTime", var2);
            }
        }

        return -1;
    }

    public int getGIFLimitSize() {
        if (this.mLibHandler != null) {
            try {
                return this.mLibHandler.getGIFLimitSize();
            } catch (RemoteException var2) {
                RLog.e("RongIMClient", "getVideoLimitTime", var2);
            }
        }

        return -1;
    }

    public void setReconnectKickEnable(boolean enable) {
        this.kickReconnectDevice = enable;
    }

    private void updatePushContentShowStatus() {
        this.getPushContentShowStatus(new RongIMClient.ResultCallback<Boolean>() {
            public void onSuccess(Boolean aBoolean) {
                RongPushClient.updatePushContentShowStatus(RongIMClient.this.mContext, aBoolean);
            }

            public void onError(RongIMClient.ErrorCode e) {
                RLog.e("RongIMClient", "Can't get push content show status!");
            }
        });
    }

    public void appOnStart() {
        if (this.mContext == null) {
            RLog.e("RongIMClient", "Event ignored. Please call this api after init.!");
        } else {
            RLog.d("RongIMClient", "appOnStart()");
            this.onAppBackgroundChanged(true);
        }
    }

    public void setRLogLevel(int pLevel) {
        RLog.setLogLevel(pLevel);
    }

    public void setRLogFileMaxSize(long pSize) {
        RLog.setFileMaxSize(pSize);
    }

    public void setUploadCallback(UploadCallback pCallback) {
        RLog.setUploadCallback(pCallback);
    }

    public void uploadRLog() {
        RLog.uploadRLog();
    }

    public void beginDestructMessage(Message message, RongIMClient.DestructCountDownTimerListener pListener) {
        if (message != null && message.getContent() != null) {
            if (message.getContent().isDestruct() && message.getMessageDirection() == MessageDirection.RECEIVE) {
                DestructionTaskManager.getInstance().BeginDestruct(message, pListener);
            }

        } else {
            RLog.e("RongIMClient", "beginDestructMessage : message or content can't be null!");
        }
    }

    public void stopDestructMessage(Message message) {
        if (message != null && message.getContent() != null) {
            if (message.getContent().isDestruct() && message.getMessageDirection() == MessageDirection.RECEIVE) {
                DestructionTaskManager.getInstance().messageStopDestruct(message);
            }

        } else {
            RLog.e("RongIMClient", "stopDestructMessage : message or content can't be null!");
        }
    }

    private boolean filterSendMessage(Message message) {
        boolean intercept = true;
        if (message == null) {
            RLog.e("RongIMClient", "filterSendMessage : message can't be null!");
        } else if (message.getConversationType() == null) {
            RLog.e("RongIMClient", "filterSendMessage : conversation type can't be null!");
        } else if (message.getConversationType() == ConversationType.SYSTEM) {
            RLog.e("RongIMClient", "filterSendMessage : conversation type can't be system!");
        } else if (TextUtils.isEmpty(message.getTargetId())) {
            RLog.e("RongIMClient", "filterSendMessage : targetId can't be null!");
        } else if (message.getContent() == null) {
            RLog.e("RongIMClient", "filterSendMessage : content can't be null!");
        } else {
            intercept = false;
        }

        return intercept;
    }

    private int[] convertTypes(ConversationType[] conversationTypes) {
        List<Integer> typeList = new ArrayList();
        ConversationType[] var3 = conversationTypes;
        int var4 = conversationTypes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            ConversationType conversationType = var3[var5];
            if (conversationType != null) {
                typeList.add(conversationType.getValue());
            }
        }

        return this.toIntArray(typeList);
    }

    private int[] toIntArray(Collection<Integer> intCollection) {
        if (intCollection != null && intCollection.size() != 0) {
            int index = 0;
            int[] intArray = new int[intCollection.size()];

            Integer integer;
            for(Iterator iterator = intCollection.iterator(); iterator.hasNext(); intArray[index++] = integer) {
                integer = (Integer)iterator.next();
            }

            return intArray;
        } else {
            return new int[0];
        }
    }

    public void setChatRoomEntry(final String chatRoomId, final String key, final String value, final boolean sendNotification, final boolean autoDelete, final String notificationExtra, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && !TextUtils.isEmpty(chatRoomId) && key.matches("^[A-Za-z0-9+_=-]+$")) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.setChatRoomEntry(key, value, chatRoomId, sendNotification, notificationExtra, autoDelete, false, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "setChatRoomEntry", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "setChatRoomEntry chatRoomId or key or value is empty or key contains illegal characters!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void forceSetChatRoomEntry(final String chatRoomId, final String key, final String value, final boolean sendNotification, final boolean autoDelete, final String notificationExtra, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && !TextUtils.isEmpty(chatRoomId) && key.matches("^[A-Za-z0-9+_=-]+$")) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.setChatRoomEntry(key, value, chatRoomId, sendNotification, notificationExtra, autoDelete, true, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "forceSetChatRoomEntry", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "forceSetChatRoomEntry chatRoomId or key or value is empty or key contains illegal characters!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void getChatRoomEntry(final String chatRoomId, final String key, @NonNull final RongIMClient.ResultCallback<Map<String, String>> callback) {
        if (!TextUtils.isEmpty(chatRoomId) && !TextUtils.isEmpty(key)) {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getChatRoomEntry(chatRoomId, key, new io.rong.imlib.IStringCallback.Stub() {
                                public void onComplete(String value) {
                                    Map<String, String> result = new HashMap();
                                    result.put(key, value);
                                    callback.onSuccess(result);
                                }

                                public void onFailure(int errorCode) {
                                    callback.onError(RongIMClient.ErrorCode.valueOf(errorCode));
                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getChatRoomEntry", var2);
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "getChatRoomEntry Parameter  chatRoomId or key is empty!");
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        }
    }

    public void getAllChatRoomEntries(final String chatRoomId, @NonNull final RongIMClient.ResultCallback<Map<String, String>> callback) {
        if (TextUtils.isEmpty(chatRoomId)) {
            RLog.e("RongIMClient", "getAllChatRoomEntries chatRoomId  is empty!");
            callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
        } else {
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                    } else {
                        try {
                            RongIMClient.this.mLibHandler.getAllChatRoomEntries(chatRoomId, new io.rong.imlib.IDataByBatchListener.Stub() {
                                HashMap<String, String> result = new HashMap();

                                public void onProgress(Map data) {
                                    this.result.putAll(data);
                                }

                                public void onComplete() {
                                    RLog.i("RongIMClient", "getAllChatRoomEntries size =" + this.result.size());
                                    callback.onSuccess(this.result);
                                }

                                public void onError(int status) {
                                    callback.onError(RongIMClient.ErrorCode.valueOf(status));
                                }
                            });
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "getAllChatRoomEntries", var2);
                            callback.onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                        }

                    }
                }
            });
        }
    }

    public void removeChatRoomEntry(final String chatRoomId, final String key, final Boolean sendNotification, final String notificationExtra, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(chatRoomId) && !TextUtils.isEmpty(key)) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.deleteChatRoomEntry(key, (String)null, chatRoomId, sendNotification, notificationExtra, false, false, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "removeChatRoomEntry", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "removeChatRoomEntry chatRoomId or key is empty!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void forceRemoveChatRoomEntry(final String chatRoomId, final String key, final Boolean sendNotification, final String notificationExtra, RongIMClient.OperationCallback callback) {
        if (!TextUtils.isEmpty(chatRoomId) && !TextUtils.isEmpty(key)) {
            final IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy(callback);
            this.mWorkHandler.post(new Runnable() {
                public void run() {
                    if (RongIMClient.this.mLibHandler == null) {
                        if (ipcCallbackProxy.callback != null) {
                            ((RongIMClient.OperationCallback)ipcCallbackProxy.callback).onFail(RongIMClient.ErrorCode.IPC_DISCONNECT);
                            ipcCallbackProxy.callback = null;
                        }

                    } else {
                        try {
                            RongIMClient.this.mLibHandler.deleteChatRoomEntry(key, (String)null, chatRoomId, sendNotification, notificationExtra, false, true, RongIMClient.this.new DefaultOperationCallback(ipcCallbackProxy));
                        } catch (RemoteException var2) {
                            RLog.e("RongIMClient", "forceRemoveChatRoomEntry", var2);
                        }

                    }
                }
            });
        } else {
            RLog.e("RongIMClient", "forceRemoveChatRoomEntry chatRoomId or key is empty!");
            if (callback != null) {
                callback.onError(RongIMClient.ErrorCode.PARAMETER_ERROR);
            }

        }
    }

    public void setConversationStatusListener(RongIMClient.ConversationStatusListener listener) {
        sConversationStatusListener = listener;
    }

    public interface ConversationStatusListener {
        void OnStatusChanged(ConversationStatus[] var1);
    }

    public interface DestructCountDownTimerListener {
        void onTick(long var1, String var3);

        void onStop(String var1);
    }

    private static enum PushStatus {
        STATUS_ON("1"),
        STATUS_OFF("0");

        private String value;

        private PushStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private static enum PushSettings {
        PUSH_SETTINGS_LANGUAGE(1),
        PUSH_SETTINGS_SHOW_CONTENT(2),
        PUSH_SETTINGS_RECEIVE(3);

        private int value;

        private PushSettings(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum PushLanguage {
        EN_US(1, "en_us"),
        ZH_CN(2, "zh_cn");

        private int value;
        private String msg;

        private PushLanguage(int value, String msg) {
            this.value = value;
            this.msg = msg;
        }

        public int getValue() {
            return this.value;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public interface SyncConversationReadStatusListener {
        void onSyncConversationReadStatus(ConversationType var1, String var2);
    }

    public interface OnRecallMessageListener {
        boolean onMessageRecalled(Message var1, RecallNotificationMessage var2);
    }

    public interface OnReceiveDestructionMessageListener {
        void onReceive(Message var1);
    }

    public interface ReadReceiptListener {
        void onReadReceiptReceived(Message var1);

        void onMessageReceiptRequest(ConversationType var1, String var2, String var3);

        void onMessageReceiptResponse(ConversationType var1, String var2, String var3, HashMap<String, Long> var4);
    }

    public interface RCLogInfoListener {
        void onRCLogInfoOccurred(String var1);
    }

    public interface TypingStatusListener {
        void onTypingStatusChanged(ConversationType var1, String var2, Collection<TypingStatus> var3);
    }

    public interface RealTimeLocationListener {
        void onStatusChange(RealTimeLocationStatus var1);

        void onReceiveLocation(double var1, double var3, String var5);

        void onReceiveLocationWithType(double var1, double var3, RealTimeLocationType var5, String var6);

        void onParticipantsJoin(String var1);

        void onParticipantsQuit(String var1);

        void onError(RealTimeLocationErrorCode var1);
    }

    public abstract static class GetBlacklistCallback extends RongIMClient.ResultCallback<String[]> {
        public GetBlacklistCallback() {
        }
    }

    public static enum SearchType {
        EXACT(0),
        FUZZY(1);

        private int value;

        private SearchType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public abstract static class GetNotificationQuietHoursCallback extends RongIMClient.ResultCallback<String> {
        public GetNotificationQuietHoursCallback() {
        }

        public abstract void onSuccess(String var1, int var2);

        public final void onSuccess(String s) {
            throw new RuntimeException("not support");
        }

        public abstract void onError(RongIMClient.ErrorCode var1);

        void onCallback(final String startTime, final int spanMinutes) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    GetNotificationQuietHoursCallback.this.onSuccess(startTime, spanMinutes);
                }
            });
        }
    }

    public static enum BlacklistStatus {
        IN_BLACK_LIST(0),
        NOT_IN_BLACK_LIST(1);

        private int value;

        private BlacklistStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static RongIMClient.BlacklistStatus setValue(int code) {
            RongIMClient.BlacklistStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RongIMClient.BlacklistStatus c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return NOT_IN_BLACK_LIST;
        }
    }

    public static enum MediaType {
        IMAGE(1),
        AUDIO(2),
        VIDEO(3),
        FILE(4);

        private int value;

        private MediaType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static RongIMClient.MediaType setValue(int code) {
            RongIMClient.MediaType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RongIMClient.MediaType c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return IMAGE;
        }
    }

    public abstract static class DownloadMediaCallback extends RongIMClient.ResultCallback<String> {
        public DownloadMediaCallback() {
        }

        public abstract void onProgress(int var1);

        void onProgressCallback(final int progress) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    DownloadMediaCallback.this.onProgress(progress);
                }
            });
        }
    }

    public abstract static class UploadMediaCallback extends RongIMClient.ResultCallback<Message> {
        public UploadMediaCallback() {
        }

        public abstract void onProgress(Message var1, int var2);

        public abstract void onError(Message var1, RongIMClient.ErrorCode var2);

        void onProgressCallback(final Message message, final int progress) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    UploadMediaCallback.this.onProgress(message, progress);
                }
            });
        }

        void onFail(final Message message, final RongIMClient.ErrorCode code) {
            RongIMClient.mHandler.postDelayed(new Runnable() {
                public void run() {
                    UploadMediaCallback.this.onError(message, code);
                }
            }, 100L);
        }

        public void onError(RongIMClient.ErrorCode e) {
        }
    }

    public abstract static class SendMediaMessageCallback extends RongIMClient.SendImageMessageCallback {
        public SendMediaMessageCallback() {
        }
    }

    public abstract static class SendImageMessageCallback extends RongIMClient.SendMessageCallback {
        public SendImageMessageCallback() {
        }

        public abstract void onAttached(Message var1);

        public abstract void onError(Message var1, RongIMClient.ErrorCode var2);

        public abstract void onSuccess(Message var1);

        public abstract void onProgress(Message var1, int var2);

        void onProgressCallback(final Message message, final int progress) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    SendImageMessageCallback.this.onProgress(message, progress);
                }
            });
        }

        void onAttachedCallback(final Message message) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    SendImageMessageCallback.this.onAttached(message);
                }
            });
        }

        void onFail(final Message message, final RongIMClient.ErrorCode code) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    SendImageMessageCallback.this.onError(message, code);
                }
            });
        }

        public void onSuccess(Integer integer) {
        }

        public void onError(Integer messageId, RongIMClient.ErrorCode e) {
        }
    }

    public abstract static class SendImageMessageWithUploadListenerCallback {
        public SendImageMessageWithUploadListenerCallback() {
        }

        public abstract void onAttached(Message var1, RongIMClient.UploadImageStatusListener var2);

        public abstract void onError(Message var1, RongIMClient.ErrorCode var2);

        public abstract void onSuccess(Message var1);

        public abstract void onProgress(Message var1, int var2);

        void onAttachedCallback(final Message message, final RongIMClient.UploadImageStatusListener watcher) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    SendImageMessageWithUploadListenerCallback.this.onAttached(message, watcher);
                }
            });
        }

        void onFail(final Message message, final RongIMClient.ErrorCode code) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    SendImageMessageWithUploadListenerCallback.this.onError(message, code);
                }
            });
        }
    }

    public class UploadImageStatusListener {
        private RongIMClient.SendImageMessageWithUploadListenerCallback callback;
        private Message message;
        private String pushContent;
        private String pushData;

        public UploadImageStatusListener(Message message, String pushContent, String pushData, RongIMClient.SendImageMessageWithUploadListenerCallback callback) {
            this.callback = callback;
            this.message = message;
            this.pushContent = pushContent;
            this.pushData = pushData;
        }

        public void update(int progress) {
            if (this.callback != null) {
                this.callback.onProgress(this.message, progress);
            }

        }

        public void error() {
            if (this.callback != null) {
                this.message.setSentStatus(SentStatus.FAILED);
                RongIMClient.this.setMessageSentStatus(this.message, (RongIMClient.ResultCallback)null);
                this.callback.onFail(this.message, RongIMClient.ErrorCode.RC_MSG_SEND_FAIL);
            }

        }

        public void success(Uri uploadedUri) {
            if (uploadedUri == null) {
                RLog.e("RongIMClient", "UploadImageStatusListener uri is null.");
                if (this.callback != null) {
                    this.callback.onFail(this.message, RongIMClient.ErrorCode.RC_MSG_SEND_FAIL);
                }

            } else {
                MessageContent content = this.message.getContent();
                if (content instanceof ImageMessage) {
                    ((ImageMessage)content).setRemoteUri(uploadedUri);
                } else if (content instanceof GIFMessage) {
                    ((GIFMessage)content).setRemoteUri(uploadedUri);
                }

                RongIMClient.this.internalSendImageMessage(this.message, this.pushContent, this.pushData, new RongIMClient.SendImageMessageCallback() {
                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                        if (UploadImageStatusListener.this.callback != null) {
                            UploadImageStatusListener.this.callback.onSuccess(message);
                        }

                    }

                    public void onProgress(Message message, int progress) {
                    }

                    void onFail(Message message, RongIMClient.ErrorCode code) {
                    }

                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        if (UploadImageStatusListener.this.callback != null) {
                            UploadImageStatusListener.this.callback.onError(message, errorCode);
                        }

                    }
                });
            }
        }
    }

    /** @deprecated */
    public abstract static class CreateDiscussionCallback extends RongIMClient.ResultCallback<String> {
        public CreateDiscussionCallback() {
        }
    }

    public abstract static class SendMessageCallback extends RongIMClient.ResultCallback<Integer> {
        public SendMessageCallback() {
        }

        public final void onError(RongIMClient.ErrorCode e) {
        }

        public final void onFail(int errorCode) {
            super.onFail(errorCode);
        }

        public final void onFail(RongIMClient.ErrorCode errorCode) {
            super.onFail(errorCode);
        }

        public abstract void onError(Integer var1, RongIMClient.ErrorCode var2);

        public final void onFail(final Integer messageId, final int errorCode) {
            RongIMClient.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SendMessageCallback.this.onError(messageId, RongIMClient.ErrorCode.valueOf(errorCode));
                }
            }, 100L);
        }

        public final void onFail(final Integer messageId, final RongIMClient.ErrorCode errorCode) {
            RongIMClient.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SendMessageCallback.this.onError(messageId, errorCode);
                }
            }, 100L);
        }
    }

    public abstract static class OnReceiveMessageWrapperListener implements RongIMClient.OnReceiveMessageListener {
        public OnReceiveMessageWrapperListener() {
        }

        public final boolean onReceived(Message message, int left) {
            return this.onReceived(message, left, false, false);
        }

        public abstract boolean onReceived(Message var1, int var2, boolean var3, boolean var4);
    }

    public interface OnReceiveMessageListener {
        boolean onReceived(Message var1, int var2);
    }

    /** @deprecated */
    public static enum DiscussionInviteStatus {
        CLOSED(1),
        OPENED(0);

        private int value;

        private DiscussionInviteStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static RongIMClient.DiscussionInviteStatus setValue(int code) {
            RongIMClient.DiscussionInviteStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RongIMClient.DiscussionInviteStatus c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return OPENED;
        }
    }

    public interface ConnectionStatusListener {
        void onChanged(RongIMClient.ConnectionStatusListener.ConnectionStatus var1);

        public static enum ConnectionStatus {
            NETWORK_UNAVAILABLE(-1, "Network is unavailable."),
            CONNECTED(0, "Connect Success."),
            CONNECTING(1, "Connecting"),
            UNCONNECTED(2, "UNCONNECTED"),
            KICKED_OFFLINE_BY_OTHER_CLIENT(3, "Login on the other device, and be kicked offline."),
            TOKEN_INCORRECT(4, "Token incorrect."),
            CONN_USER_BLOCKED(6, "User blocked by admin"),
            SIGN_OUT(12, "user sign out"),
            SUSPEND(13, "SUSPEND"),
            TIMEOUT(14, "TIMEOUT");

            private int code;
            private String msg;

            private ConnectionStatus(int code, String msg) {
                this.code = code;
                this.msg = msg;
            }

            public static RongIMClient.ConnectionStatusListener.ConnectionStatus valueOf(int code) {
                RongIMClient.ConnectionStatusListener.ConnectionStatus[] values = values();
                RongIMClient.ConnectionStatusListener.ConnectionStatus[] var2 = values;
                int var3 = values.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    RongIMClient.ConnectionStatusListener.ConnectionStatus status = var2[var4];
                    if (status.code == code) {
                        return status;
                    }
                }

                RLog.d("RongIMClient", "valueOf,ErrorCode:" + code);
                return CONNECTING;
            }

            public int getValue() {
                return this.code;
            }

            public String getMessage() {
                return this.msg;
            }
        }
    }

    public interface EncryptedSessionConnectionListener {
        void onEncryptedSessionRequest(String var1, boolean var2);

        void onEncryptedSessionResponse(String var1);

        void onEncryptedSessionEstablished(String var1);

        void onEncryptedSessionCanceled(String var1);

        void onEncryptedSessionTerminated(String var1);
    }

    public abstract static class OperationCallback extends RongIMClient.Callback {
        public OperationCallback() {
        }
    }

    public static enum DatabaseOpenStatus {
        DATABASE_OPEN_SUCCESS(0),
        DATABASE_OPEN_ERROR(33002);

        private int code;

        private DatabaseOpenStatus(int code) {
            this.code = code;
        }

        public int getValue() {
            return this.code;
        }

        public static RongIMClient.DatabaseOpenStatus valueOf(int code) {
            RongIMClient.DatabaseOpenStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RongIMClient.DatabaseOpenStatus status = var1[var3];
                if (code == status.getValue()) {
                    return status;
                }
            }

            return DATABASE_OPEN_ERROR;
        }
    }

    abstract static class SyncCallback<T> extends RongIMClient.ResultCallback<T> {
        SyncCallback() {
        }

        public void onFail(int errorCode) {
            this.onError(RongIMClient.ErrorCode.valueOf(errorCode));
        }

        public void onFail(RongIMClient.ErrorCode errorCode) {
            this.onError(errorCode);
        }

        public void onCallback(T t) {
            this.onSuccess(t);
        }
    }

    public abstract static class ConnectCallback {
        public ConnectCallback() {
        }

        public abstract void onSuccess(String var1);

        public abstract void onError(RongIMClient.ConnectionErrorCode var1);

        void onFail(final int errorCode) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    ConnectCallback.this.onError(RongIMClient.ConnectionErrorCode.valueOf(errorCode));
                }
            });
        }

        void onFail(final RongIMClient.ConnectionErrorCode errorCode) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    ConnectCallback.this.onError(errorCode);
                }
            });
        }

        void onCallback(final String t) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    ConnectCallback.this.onSuccess(t);
                }
            });
        }

        public abstract void onDatabaseOpened(RongIMClient.DatabaseOpenStatus var1);
    }

    public abstract static class ResultCallback<T> {
        public ResultCallback() {
        }

        public abstract void onSuccess(T var1);

        public abstract void onError(RongIMClient.ErrorCode var1);

        void onFail(final int errorCode) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    ResultCallback.this.onError(RongIMClient.ErrorCode.valueOf(errorCode));
                }
            });
        }

        void onFail(final RongIMClient.ErrorCode errorCode) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    ResultCallback.this.onError(errorCode);
                }
            });
        }

        void onCallback(final T t) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    ResultCallback.this.onSuccess(t);
                }
            });
        }

        public static class Result<T> {
            public T t;

            public Result() {
            }
        }
    }

    public abstract static class Callback {
        public Callback() {
        }

        public void onCallback() {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    Callback.this.onSuccess();
                }
            });
        }

        public void onFail(final int errorCode) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    Callback.this.onError(RongIMClient.ErrorCode.valueOf(errorCode));
                }
            });
        }

        public void onFail(final RongIMClient.ErrorCode errorCode) {
            RongIMClient.mHandler.post(new Runnable() {
                public void run() {
                    Callback.this.onError(errorCode);
                }
            });
        }

        public abstract void onSuccess();

        public abstract void onError(RongIMClient.ErrorCode var1);
    }

    public static enum ErrorCode {
        APP_NOT_CONNECT(-4, "APP hasn't call connect function."),
        PARAMETER_ERROR(-3, "the parameter is error."),
        IPC_DISCONNECT(-2, "IPC is not connected"),
        UNKNOWN(-1, "unknown"),
        CONNECTED(0, "connected"),
        MSG_SEND_OVERFREQUENCY(20604, "message send over frequency."),
        RC_OPERATION_BLOCKED(20605, ""),
        RC_OPERATION_NOT_SUPPORT(20606, ""),
        RC_REQUEST_OVERFREQUENCY(20607, ""),
        MSG_ROAMING_SERVICE_UNAVAILABLE(33007, "Message roaming service unavailable"),
        NOT_IN_DISCUSSION(21406, ""),
        RC_MSG_BLOCKED_SENSITIVE_WORD(21501, "word is blocked"),
        RC_MSG_REPLACED_SENSITIVE_WORD(21502, "word is replaced"),
        NOT_IN_GROUP(22406, ""),
        FORBIDDEN_IN_GROUP(22408, ""),
        NOT_IN_CHATROOM(23406, ""),
        FORBIDDEN_IN_CHATROOM(23408, ""),
        KICKED_FROM_CHATROOM(23409, ""),
        RC_CHATROOM_NOT_EXIST(23410, "Chat room does not exist"),
        RC_CHATROOM_IS_FULL(23411, "Chat room is full"),
        RC_CHATROOM_ILLEGAL_ARGUMENT(23412, "illegal argument."),
        INVALID_PUBLIC_NUMBER(29201, ""),
        REJECTED_BY_BLACKLIST(405, "rejected by blacklist"),
        NOT_IN_WHITELIST(407, ""),
        RC_NET_CHANNEL_INVALID(30001, "Socket does not exist"),
        RC_NET_UNAVAILABLE(30002, ""),
        RC_MSG_RESP_TIMEOUT(30003, ""),
        RC_HTTP_SEND_FAIL(30004, ""),
        RC_HTTP_REQ_TIMEOUT(30005, ""),
        RC_HTTP_RECV_FAIL(30006, ""),
        RC_NAVI_RESOURCE_ERROR(30007, ""),
        RC_NODE_NOT_FOUND(30008, ""),
        RC_DOMAIN_NOT_RESOLVE(30009, ""),
        RC_SOCKET_NOT_CREATED(30010, ""),
        RC_SOCKET_DISCONNECTED(30011, ""),
        RC_PING_SEND_FAIL(30012, ""),
        RC_PONG_RECV_FAIL(30013, ""),
        RC_MSG_SEND_FAIL(30014, ""),
        RC_CONN_OVERFREQUENCY(30015, "Connect over frequency."),
        RC_MSG_SIZE_OUT_OF_LIMIT(30016, ""),
        RC_NETWORK_IS_DOWN_OR_UNREACHABLE(30019, ""),
        RC_CONN_ACK_TIMEOUT(31000, ""),
        RC_CONN_PROTO_VERSION_ERROR(31001, ""),
        RC_CONN_ID_REJECT(31002, ""),
        RC_CONN_SERVER_UNAVAILABLE(31003, ""),
        RC_CONN_USER_OR_PASSWD_ERROR(31004, ""),
        RC_CONN_NOT_AUTHRORIZED(31005, ""),
        RC_CONN_REDIRECTED(31006, ""),
        RC_CONN_PACKAGE_NAME_INVALID(31007, ""),
        RC_CONN_APP_BLOCKED_OR_DELETED(31008, ""),
        RC_CONN_USER_BLOCKED(31009, ""),
        RC_DISCONN_KICK(31010, ""),
        RC_DISCONN_USER_BLOCKED(31011, ""),
        RC_CONN_OTHER_DEVICE_LOGIN(31023, ""),
        RC_QUERY_ACK_NO_DATA(32001, ""),
        RC_MSG_DATA_INCOMPLETE(32002, ""),
        RC_CONN_REFUSED(32061, "connection is refused"),
        RC_CONNECTION_RESET_BY_PEER(32054, "connection reset by peer"),
        BIZ_ERROR_CLIENT_NOT_INIT(33001, ""),
        BIZ_ERROR_DATABASE_ERROR(33002, ""),
        BIZ_ERROR_INVALID_PARAMETER(33003, ""),
        BIZ_ERROR_NO_CHANNEL(33004, ""),
        BIZ_ERROR_RECONNECT_SUCCESS(33005, ""),
        BIZ_ERROR_CONNECTING(33006, ""),
        NOT_FOLLOWED(29106, ""),
        PARAMETER_INVALID_CHATROOM(23412, "invalid parameter"),
        ROAMING_SERVICE_UNAVAILABLE_CHATROOM(23414, ""),
        EXCCED_MAX_KV_SIZE(23423, ""),
        TRY_OVERWRITE_INVALID_KEY(23424, ""),
        EXCCED_MAX_CALL_API_SIZE(23425, ""),
        KV_STORE_NOT_AVAILABLE(23426, ""),
        KEY_NOT_EXIST(23427, ""),
        RC_CONNECTION_EXIST(34001, ""),
        KV_STORE_NOT_SYNC(34004, ""),
        RC_RECALL_PARAMETER_INVALID(25101, ""),
        RC_PUSHSETTING_PARAMETER_INVALID(26001, ""),
        RC_PUSHSETTING_CONFIG_NOT_OPEN(26002, ""),
        RC_SIGHT_SERVICE_UNAVAILABLE(26101, ""),
        RC_SIGHT_MSG_DURATION_LIMIT_EXCEED(34002, ""),
        RC_GIF_MSG_SIZE_LIMIT_EXCEED(34003, ""),
        RC_ENVIRONMENT_ERROR(34005, ""),
        RC_CONNECT_TIMEOUT(34006, "Time out."),
        RC_PUBLIC_SERVICE_PROFILE_NOT_EXIST(34007, "");

        private int code;
        private String msg;

        private ErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getValue() {
            return this.code;
        }

        public String getMessage() {
            return this.msg;
        }

        public static RongIMClient.ErrorCode valueOf(int code) {
            RongIMClient.ErrorCode[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RongIMClient.ErrorCode c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            RLog.d("RongIMClient", "valueOf,ErrorCode:" + code);
            RongIMClient.ErrorCode c = UNKNOWN;
            c.code = code;
            c.msg = code + "";
            return c;
        }
    }

    public static enum ConnectionErrorCode {
        IPC_DISCONNECT(RongIMClient.ErrorCode.IPC_DISCONNECT.getValue()),
        RC_CONN_ID_REJECT(RongIMClient.ErrorCode.RC_CONN_ID_REJECT.getValue()),
        RC_CONN_TOKEN_INCORRECT(RongIMClient.ErrorCode.RC_CONN_USER_OR_PASSWD_ERROR.getValue()),
        RC_CONN_NOT_AUTHRORIZED(RongIMClient.ErrorCode.RC_CONN_NOT_AUTHRORIZED.getValue()),
        RC_CONN_PACKAGE_NAME_INVALID(RongIMClient.ErrorCode.RC_CONN_PACKAGE_NAME_INVALID.getValue()),
        RC_CONN_APP_BLOCKED_OR_DELETED(RongIMClient.ErrorCode.RC_CONN_APP_BLOCKED_OR_DELETED.getValue()),
        RC_CONN_USER_BLOCKED(RongIMClient.ErrorCode.RC_CONN_USER_BLOCKED.getValue()),
        RC_DISCONN_KICK(RongIMClient.ErrorCode.RC_DISCONN_KICK.getValue()),
        RC_CONN_OTHER_DEVICE_LOGIN(RongIMClient.ErrorCode.RC_CONN_OTHER_DEVICE_LOGIN.getValue()),
        RC_CLIENT_NOT_INIT(RongIMClient.ErrorCode.BIZ_ERROR_CLIENT_NOT_INIT.getValue()),
        RC_INVALID_PARAMETER(RongIMClient.ErrorCode.BIZ_ERROR_INVALID_PARAMETER.getValue()),
        RC_CONNECTION_EXIST(RongIMClient.ErrorCode.RC_CONNECTION_EXIST.getValue()),
        RC_ENVIRONMENT_ERROR(RongIMClient.ErrorCode.RC_ENVIRONMENT_ERROR.getValue()),
        RC_CONNECT_TIMEOUT(RongIMClient.ErrorCode.RC_CONNECT_TIMEOUT.getValue()),
        UNKNOWN(-1);

        private int code;

        private ConnectionErrorCode(int code) {
            this.code = code;
        }

        public int getValue() {
            return this.code;
        }

        public static RongIMClient.ConnectionErrorCode valueOf(int code) {
            RongIMClient.ConnectionErrorCode[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RongIMClient.ConnectionErrorCode c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            RLog.d("RongIMClient", "valueOf,ConnectionErrorCode:" + code);
            RongIMClient.ConnectionErrorCode c = UNKNOWN;
            c.code = code;
            return c;
        }
    }

    private class StatusListener extends io.rong.imlib.IConnectionStatusListener.Stub {
        private StatusListener() {
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            try {
                boolean result = super.onTransact(code, data, reply, flags);
                return result;
            } catch (RuntimeException var7) {
                RLog.e("RongIMClient", "StatusListener Unexpected remote exception", var7);
                throw var7;
            }
        }

        public void onChanged(int status) throws RemoteException {
            RongIMClient.ConnectionStatusListener.ConnectionStatus connectionStatus = RongIMClient.ConnectionStatusListener.ConnectionStatus.valueOf(status);
            RLog.d("RongIMClient", "[connect] onChanged cur = " + RongIMClient.this.mConnectionStatus + ", to = " + connectionStatus);
            this.onConnectionStatusChange(connectionStatus);
        }

        synchronized void onConnectionStatusChange(final RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
            if (status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                MessageBufferPool.getInstance().retrySendMessages();
                RongIMClient.getInstance().reJoinChatRoomWithCache();
            }

            ModuleManager.connectivityChanged(status);
            RongIMClient.this.imLibExtensionModuleManager.onConnectStatusChanged(status);
            if (!RongIMClient.this.mConnectionStatus.equals(status)) {
                RongIMClient.this.mConnectionStatus = status;
                RongIMClient.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (RongIMClient.sConnectionListener != null) {
                            RongIMClient.sConnectionListener.onChanged(status);
                        }

                    }
                });
            }

        }
    }

    private class JoinChatRoomCallback extends RongIMClient.DefaultOperationCallback {
        private String chatRoomId;
        private int count;
        private boolean chatRoomIdExist;
        private boolean isRejoin;

        JoinChatRoomCallback(IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy, String chatRoomId, int count, boolean joinMultiCR, boolean chatRoomIdExist, boolean isRejoin) {
            super(ipcCallbackProxy);
            this.chatRoomId = chatRoomId;
            this.count = count;
            this.chatRoomIdExist = chatRoomIdExist;
            this.isRejoin = isRejoin;
            if (!joinMultiCR) {
                RongIMClient.this.mChatRoomCache.clear();
            }

            RongIMClient.this.mChatRoomCache.put(chatRoomId, RongIMClient.this.new ChatRoomCacheRunnable(chatRoomId, count, chatRoomIdExist));
            RLog.d("RongIMClient", this + "; joinMultiCR = " + joinMultiCR);
        }

        public void onFailure(int errorCode) {
            super.onFailure(errorCode);
            FwLog.write(1, 1, this.isRejoin ? LogTag.A_REJOIN_CHATROOM_R.getTag() : LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{errorCode, this.chatRoomId});
            RongIMClient.ErrorCode ec = RongIMClient.ErrorCode.valueOf(errorCode);
            if (!ec.equals(RongIMClient.ErrorCode.RC_CHATROOM_NOT_EXIST) && !ec.equals(RongIMClient.ErrorCode.RC_CHATROOM_IS_FULL) && !ec.equals(RongIMClient.ErrorCode.RC_OPERATION_BLOCKED)) {
                RLog.e("RongIMClient", "join chatroom " + this.chatRoomId + " error: " + errorCode + ", re-join after 2s");
                RongIMClient.ChatRoomCacheRunnable runnable = RongIMClient.this.new ChatRoomCacheRunnable(this.chatRoomId, this.count, this.chatRoomIdExist);
                RongIMClient.this.mRetryCRCache.put(this.chatRoomId, runnable);
                long interval = 2000L;
                FwLog.write(4, 1, LogTag.A_REJOIN_CHATROOM_S.getTag(), "retry_after", new Object[]{interval});
                RongIMClient.this.mWorkHandler.postDelayed(runnable, interval);
            } else {
                RLog.e("RongIMClient", "join chatroom " + this.chatRoomId + " error : " + ec);
            }

            RongIMClient.ChatRoomActionListener listener = (RongIMClient.ChatRoomActionListener)RongIMClient.chatRoomActionListener.get();
            if (listener != null) {
                listener.onError(this.chatRoomId, RongIMClient.ErrorCode.valueOf(errorCode));
            }

        }

        public void onComplete() {
            super.onComplete();
            FwLog.write(3, 1, this.isRejoin ? LogTag.A_REJOIN_CHATROOM_R.getTag() : LogTag.A_JOIN_CHATROOM_R.getTag(), "code|room_id", new Object[]{0, this.chatRoomId});
            RLog.d("RongIMClient", "onComplete: " + this);
            RongIMClient.ChatRoomCacheRunnable runnable = (RongIMClient.ChatRoomCacheRunnable)RongIMClient.this.mChatRoomCache.get(this.chatRoomId);
            if (runnable != null) {
                runnable.onceSuccess = true;
            }

            RongIMClient.ChatRoomActionListener listener = (RongIMClient.ChatRoomActionListener)RongIMClient.chatRoomActionListener.get();
            if (listener != null) {
                listener.onJoined(this.chatRoomId);
            }

        }

        public String toString() {
            return "JoinChatRoomCallback{chatRoomId='" + this.chatRoomId + '\'' + ", count=" + this.count + ", chatRoomIdExist=" + this.chatRoomIdExist + '}';
        }
    }

    private class ChatRoomCacheRunnable implements Runnable {
        String chatRoomId;
        int count;
        boolean onceSuccess;
        boolean chatRoomIdExist;

        ChatRoomCacheRunnable(String chatRoomId, int count, boolean chatRoomIdExist) {
            this.count = count;
            this.chatRoomId = chatRoomId;
            this.chatRoomIdExist = chatRoomIdExist;
        }

        public void run() {
            RLog.d("RongIMClient", "re-join chatroom " + this);
            RongIMClient.this.mRetryCRCache.remove(this.chatRoomId);
            if (RongIMClient.this.mLibHandler != null && RongIMClient.this.mConnectionStatus.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                try {
                    IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy = new IpcCallbackProxy((Object)null);
                    RongIMClient.ChatRoomActionListener listener = (RongIMClient.ChatRoomActionListener)RongIMClient.chatRoomActionListener.get();
                    if (listener != null) {
                        listener.onJoining(this.chatRoomId);
                    }

                    FwLog.write(3, 1, LogTag.A_REJOIN_CHATROOM_T.getTag(), "room_id|existed|count", new Object[]{this.chatRoomId, this.chatRoomIdExist, this.onceSuccess ? -1 : this.count});
                    if (this.chatRoomIdExist) {
                        RongIMClient.this.mLibHandler.joinExistChatRoom(this.chatRoomId, this.onceSuccess ? -1 : this.count, RongIMClient.this.new JoinChatRoomCallback(ipcCallbackProxy, this.chatRoomId, this.count, RongIMClient.this.mLibHandler.getJoinMultiChatRoomEnable(), this.chatRoomIdExist, true), true);
                    } else {
                        RongIMClient.this.mLibHandler.reJoinChatRoom(this.chatRoomId, this.onceSuccess ? -1 : this.count, RongIMClient.this.new JoinChatRoomCallback(ipcCallbackProxy, this.chatRoomId, this.count, RongIMClient.this.mLibHandler.getJoinMultiChatRoomEnable(), this.chatRoomIdExist, true));
                    }
                } catch (Exception var3) {
                    FwLog.write(1, 1, LogTag.A_REJOIN_CHATROOM_R.getTag(), "code|room_id|stacks", new Object[]{-1, this.chatRoomId, FwLog.stackToString(var3)});
                    RLog.e("RongIMClient", "ChatRoomCacheRunnable", var3);
                    RLog.e("RongIMClient", "re-join chatroom exception");
                }
            } else {
                RLog.e("RongIMClient", "re-join chatroom error : " + RongIMClient.this.mConnectionStatus + ", mLibHandler = " + RongIMClient.this.mLibHandler);
            }

        }

        public String toString() {
            return "ChatRoomCacheRunnable{chatRoomId='" + this.chatRoomId + '\'' + ", count=" + this.count + ", onceSuccess=" + this.onceSuccess + ", chatRoomIdExist=" + this.chatRoomIdExist + ", state='" + RongIMClient.this.mConnectionStatus + '\'' + '}';
        }
    }

    public interface ChatRoomActionListener {
        void onJoining(String var1);

        void onJoined(String var1);

        void onQuited(String var1);

        void onError(String var1, RongIMClient.ErrorCode var2);
    }

    private class DefaultOperationCallback extends io.rong.imlib.IOperationCallback.Stub {
        private IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy;

        public DefaultOperationCallback(IpcCallbackProxy<RongIMClient.OperationCallback> ipcCallbackProxy) {
            this.ipcCallbackProxy = ipcCallbackProxy;
        }

        public void onComplete() {
            if (this.ipcCallbackProxy.callback != null) {
                ((RongIMClient.OperationCallback)this.ipcCallbackProxy.callback).onCallback();
                this.ipcCallbackProxy.callback = null;
            }

        }

        public void onFailure(int errorCode) {
            if (this.ipcCallbackProxy.callback != null) {
                ((RongIMClient.OperationCallback)this.ipcCallbackProxy.callback).onFail(errorCode);
                this.ipcCallbackProxy.callback = null;
            }

        }
    }

    public static enum TimestampOrder {
        RC_TIMESTAMP_DESC,
        RC_TIMESTAMP_ASC;

        private TimestampOrder() {
        }
    }

    private class GetConversationListProcessCallBackWrapper extends io.rong.imlib.IGetConversationListWithProcessCallback.Stub {
        private RongIMClient.ResultCallback<List<Conversation>> resultCallback;
        private List<Conversation> conversationList = new ArrayList();

        GetConversationListProcessCallBackWrapper(RongIMClient.ResultCallback<List<Conversation>> callback) {
            this.resultCallback = callback;
        }

        public void onProcess(List<Conversation> list) {
            this.conversationList.addAll(list);
        }

        public void onComplete() {
            if (this.resultCallback != null) {
                if (this.conversationList.size() == 0) {
                    this.resultCallback.onCallback(null);
                } else {
                    this.resultCallback.onCallback(this.conversationList);
                }
            }

        }
    }

    private class AidlConnection implements ServiceConnection {
        private AidlConnection() {
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            FwLog.write(4, 1, LogTag.BIND_SERVICE_S.getTag(), "bent", new Object[]{true});
            RongIMClient.this.mLibHandler = io.rong.imlib.IHandler.Stub.asInterface(service);

            try {
                RongIMClient.this.mLibHandler.initAppendixModule();
            } catch (RemoteException var5) {
                RLog.e("RongIMClient", "onServiceConnected initAppendix error", var5);
            }

            try {
                RLog.i("RongIMClient", "initIpcConnectStatus " + RongIMClient.this.mConnectionStatus);
                RongIMClient.this.mLibHandler.initIpcConnectStatus(RongIMClient.this.mConnectionStatus.getValue());
            } catch (RemoteException var4) {
                RLog.e("RongIMClient", "onServiceConnected initIpcConnectStatus error", var4);
            }

            RongIMClient.this.setIPCLogListener();
            RongIMClient.this.initReceiver();
            ModuleManager.init(RongIMClient.this.mContext, RongIMClient.this.mLibHandler, RongIMClient.sReceiveMessageListener);
            IMLibRTCClient.getInstance().OnServiceConnected(RongIMClient.this.mLibHandler);
            RLog.d("RongIMClient", "onServiceConnected mConnectionStatus = " + RongIMClient.this.mConnectionStatus);
            if (RongIMClient.this.mConnectRunnable != null) {
                RongIMClient.mHandler.post(RongIMClient.this.mConnectRunnable);
            } else if (RongIMClient.this.mToken != null) {
                RongIMClient.this.connectServer(RongIMClient.this.mToken, true);
            }

        }

        public void onServiceDisconnected(ComponentName name) {
            FwLog.write(4, 1, LogTag.BIND_SERVICE_S.getTag(), "bent", new Object[]{false});
            ModuleManager.unInit();
            RongIMClient.this.mLibHandler = null;
            IMLibRTCClient.getInstance().OnServiceDisconnected();
            RLog.d("RongIMClient", "onServiceDisconnected " + RongIMClient.this.mConnectionStatus);
            RongIMClient.this.mStatusListener.onConnectionStatusChange(RongIMClient.ConnectionStatusListener.ConnectionStatus.SUSPEND);
            RongIMClient.this.initBindService();
        }
    }

    private class ConnectRunnable implements Runnable {
        String token;

        ConnectRunnable(String token) {
            RLog.d("RongIMClient", "[connect] ConnectRunnable for connect");
            this.token = token;
        }

        public void run() {
            RLog.d("RongIMClient", "[connect] ConnectRunnable do connect!");
            RongIMClient.this.connectServer(this.token, false);
        }
    }

    private static class SingletonHolder {
        static RongIMClient sInstance = new RongIMClient();

        private SingletonHolder() {
        }
    }
}