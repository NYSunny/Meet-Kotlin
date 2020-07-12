//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.cs;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.imlib.CustomServiceConfig;
import io.rong.imlib.ICustomServiceListener;
import io.rong.imlib.IHandler;
import io.rong.imlib.MessageTag;
import io.rong.imlib.ModuleManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.CustomServiceConfig.CSEvaEntryPoint;
import io.rong.imlib.CustomServiceConfig.CSEvaSolveStatus;
import io.rong.imlib.CustomServiceConfig.CSEvaType;
import io.rong.imlib.CustomServiceConfig.CSLeaveMessageType;
import io.rong.imlib.CustomServiceConfig.CSQuitSuspendType;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.ISendMessageCallback.Stub;
import io.rong.imlib.ModuleManager.MessageRouter;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.imlib.model.CSCustomServiceInfo.Builder;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.message.CSChangeModeMessage;
import io.rong.message.CSChangeModeResponseMessage;
import io.rong.message.CSEvaluateMessage;
import io.rong.message.CSHandShakeMessage;
import io.rong.message.CSHandShakeResponseMessage;
import io.rong.message.CSLeaveMessage;
import io.rong.message.CSPullEvaluateMessage;
import io.rong.message.CSPullLeaveMessage;
import io.rong.message.CSSuspendMessage;
import io.rong.message.CSTerminateMessage;
import io.rong.message.CSUpdateMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomServiceManager implements MessageRouter {
    private static final String TAG = CustomServiceManager.class.getSimpleName();
    private static CustomServiceManager sIns;
    private boolean mInitialized;
    private OnReceiveMessageListener onReceiveMessageListener;
    private HashMap<String, CustomServiceManager.CustomServiceProfile> customServiceCache;
    private List<Class<? extends MessageContent>> csMessages;
    private String failureStr;
    private String quitStr;
    private IHandler libStub;
    private Handler mainHandler;
    private CustomServiceManager.OnHumanEvaluateListener onHumanEvaluateListener;

    private CustomServiceManager() {
        this.failureStr = "";
        this.quitStr = "";
        this.csMessages = new ArrayList();
        this.customServiceCache = new HashMap();
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.mInitialized = false;
    }

    public void init(Context context, OnReceiveMessageListener onReceiveMessageListener, IHandler stub) {
        RLog.i(TAG, "init " + this.mInitialized);
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.csMessages.add(CSHandShakeMessage.class);
            this.csMessages.add(CSHandShakeResponseMessage.class);
            this.csMessages.add(CSChangeModeMessage.class);
            this.csMessages.add(CSChangeModeResponseMessage.class);
            this.csMessages.add(CSSuspendMessage.class);
            this.csMessages.add(CSTerminateMessage.class);
            this.csMessages.add(CSEvaluateMessage.class);
            this.csMessages.add(CSUpdateMessage.class);
            this.csMessages.add(CSPullEvaluateMessage.class);
            this.csMessages.add(CSPullLeaveMessage.class);
            this.csMessages.add(CSLeaveMessage.class);
            this.onReceiveMessageListener = onReceiveMessageListener;

            try {
                this.failureStr = context.getResources().getString(context.getResources().getIdentifier("rc_init_failed", "string", context.getPackageName()));
                this.quitStr = context.getResources().getString(context.getResources().getIdentifier("rc_quit_custom_service", "string", context.getPackageName()));
            } catch (Exception var5) {
                RLog.e(TAG, "init ", var5);
            }

            ModuleManager.addMessageRouter(this);
        }

        this.libStub = stub;
        this.registerMsg(stub);
    }

    private void registerMsg(IHandler stub) {
        try {
            Iterator var2 = this.csMessages.iterator();

            while(var2.hasNext()) {
                Class<? extends MessageContent> msg = (Class)var2.next();
                String v = ((MessageTag)msg.getAnnotation(MessageTag.class)).value();
                stub.registerMessageType(msg.getName());
                stub.registerCmdMsgType(v);
            }
        } catch (Exception var5) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            var5.printStackTrace(printWriter);
            RLog.e(TAG, "Exception : \n" + stringWriter.toString());
        } catch (IncompatibleClassChangeError var6) {
            RLog.e(TAG, "IncompatibleClassChangeError.");
        }

    }

    public boolean onReceived(Message message, int left, boolean offline, int cmdLeft) {
        boolean contains = this.csMessages.contains(message.getContent().getClass());
        if (contains) {
            final String msg;
            if (message.getContent() instanceof CSHandShakeResponseMessage) {
                final CSHandShakeResponseMessage csHandShakeResponseMessage = (CSHandShakeResponseMessage)message.getContent();
                final int code = csHandShakeResponseMessage.getCode();
                msg = csHandShakeResponseMessage.getMsg();
                String kefuId = message.getTargetId();
                final CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
                if (profile != null && profile.time != 0L && message.getSentTime() >= profile.time) {
                    if (code == 0 && profile.customServiceListener.get() != null) {
                        this.mainHandler.post(new Runnable() {
                            public void run() {
                                ICustomServiceListener iCustomServiceListener = (ICustomServiceListener)profile.customServiceListener.get();
                                if (iCustomServiceListener != null) {
                                    iCustomServiceListener.onError(code, TextUtils.isEmpty(msg) ? CustomServiceManager.this.failureStr : msg);
                                }

                            }
                        });
                        return true;
                    }

                    profile.mode = csHandShakeResponseMessage.getMode();
                    profile.sid = csHandShakeResponseMessage.getSid();
                    profile.uid = csHandShakeResponseMessage.getUid();
                    profile.pid = csHandShakeResponseMessage.getPid();
                    profile.groupList = csHandShakeResponseMessage.getGroupList();
                    profile.showResolveStatus = csHandShakeResponseMessage.isReportResolveStatus();
                    this.customServiceCache.put(kefuId, profile);
                    if (profile.customServiceListener.get() != null) {
                        final CustomServiceConfig config = new CustomServiceConfig();
                        config.companyName = csHandShakeResponseMessage.getCompanyName();
                        config.isBlack = csHandShakeResponseMessage.isBlack();
                        config.msg = csHandShakeResponseMessage.getMsg();
                        config.companyIcon = csHandShakeResponseMessage.getCompanyIcon();
                        config.robotSessionNoEva = csHandShakeResponseMessage.getRobotSessionNoEva() != null && csHandShakeResponseMessage.getRobotSessionNoEva().equals("1");
                        config.humanEvaluateList = csHandShakeResponseMessage.getHumanEvaluateList();
                        config.userTipTime = csHandShakeResponseMessage.getUserTipTime();
                        config.userTipWord = csHandShakeResponseMessage.getUserTipWord();
                        config.adminTipTime = csHandShakeResponseMessage.getAdminTipTime();
                        config.adminTipWord = csHandShakeResponseMessage.getAdminTipWord();
                        config.isDisableLocation = csHandShakeResponseMessage.isDisableLocation();
                        config.quitSuspendType = CSQuitSuspendType.valueOf(csHandShakeResponseMessage.isSuspendWhenQuit());
                        config.evaluateType = csHandShakeResponseMessage.getEvaType() == 0 ? CSEvaType.EVA_SEPARATELY : CSEvaType.EVA_UNIFIED;
                        config.evaEntryPoint = CSEvaEntryPoint.valueOf(csHandShakeResponseMessage.getEntryPoint());
                        config.isReportResolveStatus = csHandShakeResponseMessage.isReportResolveStatus();
                        config.leaveMessageConfigType = csHandShakeResponseMessage.getLeaveMessageConfigType() == 0 ? CSLeaveMessageType.NATIVE : CSLeaveMessageType.WEB;
                        config.uri = csHandShakeResponseMessage.getLeaveMessageWebUrl();
                        config.leaveMessageNativeInfo = csHandShakeResponseMessage.getLeaveMessageNativeInfo();
                        boolean announceMsgFlag = csHandShakeResponseMessage.getAnnounceMsgFlag() == 1;
                        if (announceMsgFlag) {
                            config.announceMsg = csHandShakeResponseMessage.getAnnounceMsg();
                        } else {
                            config.announceMsg = "";
                        }

                        boolean announceClickFlag = csHandShakeResponseMessage.getAnnounceClickFlag() == 1;
                        if (announceClickFlag) {
                            config.announceClickUrl = csHandShakeResponseMessage.getAnnounceClickUrl();
                        } else {
                            config.announceClickUrl = "";
                        }

                        this.mainHandler.post(new Runnable() {
                            public void run() {
                                ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                if (listener != null) {
                                    listener.onSuccess(config);
                                }

                            }
                        });
                    }

                    String portrait = csHandShakeResponseMessage.getRobotLogo();
                    String name = csHandShakeResponseMessage.getRobotName();
                    String hello = csHandShakeResponseMessage.getRobotHelloWord();
                    profile.welcome = hello;
                    profile.name = name;
                    profile.portrait = portrait;
                    if (!csHandShakeResponseMessage.getMode().equals(CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT) && !csHandShakeResponseMessage.getMode().equals(CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT_FIRST)) {
                        if (csHandShakeResponseMessage.isRequiredChangMode()) {
                            this.switchToHumanMode(kefuId);
                        } else {
                            this.mainHandler.post(new Runnable() {
                                public void run() {
                                    ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                    if (listener != null) {
                                        listener.onModeChanged(csHandShakeResponseMessage.getMode());
                                    }

                                }
                            });
                        }
                    } else {
                        if (!TextUtils.isEmpty(hello)) {
                            TextMessage textMessage = TextMessage.obtain(hello);
                            if (portrait != null) {
                                textMessage.setUserInfo(new UserInfo(kefuId, name, Uri.parse(portrait)));
                            }

                            RongIMClient.getInstance().insertIncomingMessage(ConversationType.CUSTOMER_SERVICE, kefuId, kefuId, (ReceivedStatus)null, textMessage, new ResultCallback<Message>() {
                                public void onSuccess(Message message) {
                                    if (CustomServiceManager.this.onReceiveMessageListener != null) {
                                        CustomServiceManager.this.onReceiveMessageListener.onReceived(message, 0);
                                    }

                                }

                                public void onError(ErrorCode e) {
                                    RLog.e(CustomServiceManager.TAG, "insertMessage , error code : " + e.getValue());
                                }
                            });
                        }

                        if (profile.customServiceListener.get() != null) {
                            this.mainHandler.post(new Runnable() {
                                public void run() {
                                    ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                    if (listener != null) {
                                        listener.onModeChanged(csHandShakeResponseMessage.getMode());
                                    }

                                }
                            });
                        }
                    }

                    RLog.d(TAG, csHandShakeResponseMessage.toString());
                    return true;
                }

                return true;
            }

            final CustomServiceManager.CustomServiceProfile profile;
            if (message.getContent() instanceof CSChangeModeResponseMessage) {
                final CSChangeModeResponseMessage csChangeModeResponseMessage = (CSChangeModeResponseMessage)message.getContent();
                profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(message.getTargetId());
                if (profile != null && profile.customServiceListener.get() != null && profile.time != 0L && message.getSentTime() > profile.time) {
                    int code = csChangeModeResponseMessage.getResult();
                    if (code == 1) {
                        switch(csChangeModeResponseMessage.getStatus()) {
                            case 1:
                                profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN;
                                this.mainHandler.post(new Runnable() {
                                    public void run() {
                                        ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                        if (listener != null) {
                                            listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN);
                                        }

                                    }
                                });
                                break;
                            case 2:
                                if (profile.mode != null) {
                                    if (profile.mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN)) {
                                        profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_NO_SERVICE;
                                        this.mainHandler.post(new Runnable() {
                                            public void run() {
                                                ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                                if (listener != null) {
                                                    listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_NO_SERVICE);
                                                }

                                            }
                                        });
                                    } else if (profile.mode.equals(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN_FIRST)) {
                                        profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT_FIRST;
                                        this.mainHandler.post(new Runnable() {
                                            public void run() {
                                                ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                                if (listener != null) {
                                                    listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT_FIRST);
                                                }

                                            }
                                        });
                                        if (!TextUtils.isEmpty(profile.welcome)) {
                                            TextMessage textMessage = TextMessage.obtain(profile.welcome);
                                            if (profile.portrait != null) {
                                                textMessage.setUserInfo(new UserInfo(message.getTargetId(), profile.name, Uri.parse(profile.portrait)));
                                            }

                                            RongIMClient.getInstance().insertIncomingMessage(ConversationType.CUSTOMER_SERVICE, message.getTargetId(), message.getTargetId(), (ReceivedStatus)null, textMessage, new ResultCallback<Message>() {
                                                public void onSuccess(Message message) {
                                                    if (CustomServiceManager.this.onReceiveMessageListener != null) {
                                                        CustomServiceManager.this.onReceiveMessageListener.onReceived(message, 0);
                                                    }

                                                }

                                                public void onError(ErrorCode e) {
                                                    RLog.e(CustomServiceManager.TAG, "insertMessage , error code : " + e.getValue());
                                                }
                                            });
                                        }
                                    }
                                }
                                break;
                            case 3:
                                this.mainHandler.post(new Runnable() {
                                    public void run() {
                                        String msg = csChangeModeResponseMessage.getErrMsg();
                                        ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                        if (listener != null) {
                                            listener.onError(3, msg);
                                        }

                                    }
                                });
                        }

                        if (this.onHumanEvaluateListener != null && !TextUtils.isEmpty(csChangeModeResponseMessage.getEvaluation())) {
                            JSONObject jsonObject = null;

                            try {
                                jsonObject = new JSONObject(csChangeModeResponseMessage.getEvaluation());
                                if (jsonObject.has("evaluation")) {
                                    JSONObject jsonObjEva = jsonObject.optJSONObject("evaluation");
                                    JSONArray jsonArray = jsonObjEva.getJSONArray("satisfaction");
                                    if (jsonArray != null && jsonArray.length() > 0) {
                                        JSONObject jsonSaticfaction = jsonArray.optJSONObject(0);
                                        if (jsonSaticfaction.has("isQuestionFlag")) {
                                            profile.showResolveStatus = jsonSaticfaction.optInt("isQuestionFlag", 0) == 1;
                                        }
                                    }
                                }
                            } catch (JSONException var15) {
                                RLog.e(TAG, "onReceive ", var15);
                            }

                            this.onHumanEvaluateListener.onHumanEvaluate(jsonObject);
                        }
                    }
                }

                return true;
            }

            if (message.getContent() instanceof CSTerminateMessage) {
                CSTerminateMessage csTerminateMessage = (CSTerminateMessage)message.getContent();
                profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(message.getTargetId());
                if (profile != null && profile.customServiceListener.get() != null && csTerminateMessage.getsid().equals(profile.sid) && profile.time != 0L && message.getSentTime() > profile.time) {
                    if (csTerminateMessage.getCode() == 0) {
                        msg = csTerminateMessage.getMsg();
                        this.mainHandler.post(new Runnable() {
                            public void run() {
                                ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                if (listener != null) {
                                    listener.onQuit(TextUtils.isEmpty(msg) ? CustomServiceManager.this.quitStr : msg);
                                }

                            }
                        });
                    } else {
                        profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT_FIRST;
                        this.mainHandler.post(new Runnable() {
                            public void run() {
                                ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                if (listener != null) {
                                    listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT_FIRST);
                                }

                            }
                        });
                    }
                }

                return true;
            }

            if (message.getContent() instanceof CSUpdateMessage) {
                CSUpdateMessage csUpdateMessage = (CSUpdateMessage)message.getContent();
                profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(message.getTargetId());
                if (profile != null && profile.time != 0L && message.getSentTime() > profile.time) {
                    profile.sid = csUpdateMessage.getSid();
                    msg = csUpdateMessage.getServiceStatus();
                    byte var9 = -1;
                    switch(msg.hashCode()) {
                        case 49:
                            if (msg.equals("1")) {
                                var9 = 0;
                            }
                            break;
                        case 50:
                            if (msg.equals("2")) {
                                var9 = 1;
                            }
                            break;
                        case 51:
                            if (msg.equals("3")) {
                                var9 = 2;
                            }
                    }

                    switch(var9) {
                        case 0:
                            profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT;
                            this.mainHandler.post(new Runnable() {
                                public void run() {
                                    ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                    if (listener != null) {
                                        listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_ROBOT);
                                    }

                                }
                            });
                            break;
                        case 1:
                            profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN;
                            this.mainHandler.post(new Runnable() {
                                public void run() {
                                    ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                    if (listener != null) {
                                        listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN);
                                    }

                                }
                            });
                            break;
                        case 2:
                            profile.mode = CustomServiceMode.CUSTOM_SERVICE_MODE_NO_SERVICE;
                            this.mainHandler.post(new Runnable() {
                                public void run() {
                                    ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                                    if (listener != null) {
                                        listener.onModeChanged(CustomServiceMode.CUSTOM_SERVICE_MODE_NO_SERVICE);
                                    }

                                }
                            });
                    }
                }

                return true;
            }

            if (message.getContent() instanceof CSPullEvaluateMessage) {
                final CSPullEvaluateMessage csPullEvaluateMessage = (CSPullEvaluateMessage)message.getContent();
                profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(message.getTargetId());
                if (profile != null && profile.time != 0L && message.getSentTime() > profile.time) {
                    profile.sid = csPullEvaluateMessage.getMsgId();
                    this.mainHandler.post(new Runnable() {
                        public void run() {
                            ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                            if (listener != null) {
                                listener.onPullEvaluation(csPullEvaluateMessage.getMsgId());
                            }

                        }
                    });
                }

                return true;
            }

            if (message.getContent() instanceof CSPullLeaveMessage) {
                return false;
            }
        }

        return contains;
    }

    public static CustomServiceManager getInstance() {
        return CustomServiceManager.SingletonHolder.sIns;
    }

    public void startCustomService(final String kefuId, ICustomServiceListener listener, CSCustomServiceInfo customServiceInfo) {
        if (TextUtils.isEmpty(kefuId)) {
            RLog.e(TAG, "startCustomService kefuId should not be null!");
        } else {
            CustomServiceManager.CustomServiceProfile profile;
            if (this.customServiceCache != null) {
                profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
                if (profile != null) {
                    this.customServiceCache.remove(kefuId);
                }
            }

            if (customServiceInfo == null) {
                Builder csBuilder = new Builder();
                customServiceInfo = csBuilder.build();
            }

            CSHandShakeMessage csHandShakeMessage = new CSHandShakeMessage();
            csHandShakeMessage.setCustomInfo(customServiceInfo);
            profile = new CustomServiceManager.CustomServiceProfile(listener);
            if (this.customServiceCache != null) {
                this.customServiceCache.put(kefuId, profile);
            }

            try {
                Message message = Message.obtain(kefuId, ConversationType.CUSTOMER_SERVICE, csHandShakeMessage);
                if (this.libStub != null) {
                    this.libStub.sendMessage(message, "", "", new Stub() {
                        public void onAttached(Message message) throws RemoteException {
                        }

                        public void onSuccess(Message message) throws RemoteException {
                            CustomServiceManager.CustomServiceProfile serviceProfile = (CustomServiceManager.CustomServiceProfile)CustomServiceManager.this.customServiceCache.get(kefuId);
                            if (serviceProfile != null) {
                                serviceProfile.time = message.getSentTime();
                            }

                        }

                        public void onError(Message message, int errorCode) throws RemoteException {
                            CustomServiceManager.CustomServiceProfile serviceProfile = (CustomServiceManager.CustomServiceProfile)CustomServiceManager.this.customServiceCache.get(kefuId);
                            if (serviceProfile != null) {
                                ICustomServiceListener listener = (ICustomServiceListener)serviceProfile.customServiceListener.get();
                                if (listener != null) {
                                    listener.onError(errorCode, CustomServiceManager.this.failureStr);
                                }
                            }

                        }
                    });
                }
            } catch (Exception var7) {
                RLog.e(TAG, "startCustomService ", var7);
            }

        }
    }

    public void sendChangeModelMessage(final String kefuId, String groupId) {
        if (TextUtils.isEmpty(kefuId)) {
            RLog.e(TAG, "sendChangeModelMessage kefuId should not be null!");
        } else if (!this.customServiceCache.containsKey(kefuId)) {
            RLog.e(TAG, "sendChangeModelMessage " + kefuId + " is not started yet!");
        } else {
            CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
            CSChangeModeMessage changeModeMessage = CSChangeModeMessage.obtain(profile.sid, profile.uid, profile.pid, groupId);
            RongIMClient.getInstance().sendMessage(ConversationType.CUSTOMER_SERVICE, kefuId, changeModeMessage, (String)null, (String)null, new ISendMessageCallback() {
                public void onAttached(Message message) {
                }

                public void onSuccess(Message message) {
                }

                public void onError(Message message, ErrorCode errorCode) {
                    InformationNotificationMessage informationNotificationMessage = InformationNotificationMessage.obtain("无人工在线");
                    RongIMClient.getInstance().insertIncomingMessage(ConversationType.CUSTOMER_SERVICE, kefuId, "rong", (ReceivedStatus)null, informationNotificationMessage, (ResultCallback)null);
                }
            });
        }
    }

    public void switchToHumanMode(String kefuId) {
        if (TextUtils.isEmpty(kefuId)) {
            RLog.e(TAG, "switchToHumanMode kefuId should not be null!");
        } else if (!this.customServiceCache.containsKey(kefuId)) {
            RLog.e(TAG, "switchToHumanMode " + kefuId + " is not started yet!");
        } else {
            final CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
            if (profile.groupList != null && profile.groupList.size() > 0) {
                this.mainHandler.post(new Runnable() {
                    public void run() {
                        ICustomServiceListener listener = (ICustomServiceListener)profile.customServiceListener.get();
                        if (listener != null && profile.groupList != null && profile.groupList.size() > 0) {
                            listener.onSelectGroup(profile.groupList);
                        }

                    }
                });
            } else {
                this.sendChangeModelMessage(kefuId, (String)null);
            }

        }
    }

    public void evaluateCustomService(String kefuId, boolean isRobotResolved, String knowledgeId) {
        if (TextUtils.isEmpty(kefuId)) {
            RLog.e(TAG, "evaluateCustomService kefuId should not be null!");
        } else if (!this.customServiceCache.containsKey(kefuId)) {
            RLog.e(TAG, "evaluateCustomService " + kefuId + " is not started yet!");
        } else {
            CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
            io.rong.message.CSEvaluateMessage.Builder csBuilder = new io.rong.message.CSEvaluateMessage.Builder();
            CSEvaluateMessage csEvaluateMessage = csBuilder.sid(TextUtils.isEmpty(knowledgeId) ? profile.sid : knowledgeId).pid(profile.pid).uid(profile.uid).type(CustomServiceManager.EvaluateType.EVALUATE_ROBOT.getValue()).setSolveStatus(isRobotResolved ? CSEvaSolveStatus.RESOLVED.getValue() : CSEvaSolveStatus.UNRESOLVED.getValue()).build();
            RongIMClient.getInstance().sendMessage(ConversationType.CUSTOMER_SERVICE, kefuId, csEvaluateMessage, (String)null, (String)null, new ISendMessageCallback() {
                public void onAttached(Message message) {
                }

                public void onSuccess(Message message) {
                }

                public void onError(Message message, ErrorCode errorCode) {
                    RLog.e(CustomServiceManager.TAG, "evaluateCustomService onError " + errorCode);
                }
            });
        }
    }

    public void evaluateCustomService(String kefuId, int source, CSEvaSolveStatus solveStatus, String suggest, String dialogId) {
        if (!TextUtils.isEmpty(kefuId) && solveStatus != null) {
            if (!this.customServiceCache.containsKey(kefuId)) {
                RLog.e(TAG, "evaluateCustomService " + kefuId + " is not started yet!");
            } else {
                CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
                int resloveStatus;
                if (!profile.showResolveStatus) {
                    resloveStatus = -1;
                } else {
                    resloveStatus = solveStatus.getValue();
                }

                io.rong.message.CSEvaluateMessage.Builder csBuilder = new io.rong.message.CSEvaluateMessage.Builder();
                CSEvaluateMessage csEvaluateMessage = csBuilder.sid(TextUtils.isEmpty(dialogId) ? profile.sid : dialogId).pid(profile.pid).uid(profile.uid).source(source).setSolveStatus(resloveStatus).suggest(suggest).type(CustomServiceManager.EvaluateType.EVALUATE_HUMAN.getValue()).build();
                RongIMClient.getInstance().sendMessage(ConversationType.CUSTOMER_SERVICE, kefuId, csEvaluateMessage, (String)null, (String)null, new ISendMessageCallback() {
                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                        RLog.d(CustomServiceManager.TAG, "onSuccess.");
                    }

                    public void onError(Message message, ErrorCode errorCode) {
                        RLog.e(CustomServiceManager.TAG, "evaluateCustomService onError " + errorCode);
                    }
                });
            }
        } else {
            RLog.e(TAG, "evaluateCustomService kefuId or solveStatus should not be null!");
        }
    }

    public void evaluateCustomService(String kefuId, int source, CSEvaSolveStatus solveStatus, String tagtext, String suggest, String dialogId, String extra) {
        if (!TextUtils.isEmpty(kefuId) && solveStatus != null) {
            if (!this.customServiceCache.containsKey(kefuId)) {
                RLog.e(TAG, "evaluateCustomService " + kefuId + " is not started yet!");
            } else {
                CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
                io.rong.message.CSEvaluateMessage.Builder csBuilder = new io.rong.message.CSEvaluateMessage.Builder();
                CustomServiceManager.EvaluateType type;
                if (profile.mode != CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN && profile.mode != CustomServiceMode.CUSTOM_SERVICE_MODE_HUMAN_FIRST) {
                    type = CustomServiceManager.EvaluateType.EVALUATE_ROBOT;
                } else {
                    type = CustomServiceManager.EvaluateType.EVALUATE_HUMAN;
                }

                int resloveStatus;
                if (!profile.showResolveStatus) {
                    resloveStatus = -1;
                } else {
                    resloveStatus = solveStatus.getValue();
                }

                CSEvaluateMessage csEvaluateMessage = csBuilder.sid(TextUtils.isEmpty(dialogId) ? profile.sid : dialogId).pid(profile.pid).uid(profile.uid).source(source).setSolveStatus(resloveStatus).tablets(tagtext).suggest(suggest).type(type.getValue()).extra(extra).build();
                RongIMClient.getInstance().sendMessage(ConversationType.CUSTOMER_SERVICE, kefuId, csEvaluateMessage, (String)null, (String)null, new ISendMessageCallback() {
                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                        RLog.d(CustomServiceManager.TAG, "onSuccess.");
                    }

                    public void onError(Message message, ErrorCode errorCode) {
                        RLog.e(CustomServiceManager.TAG, "evaluateCustomService onError " + errorCode);
                    }
                });
            }
        } else {
            RLog.e(TAG, "evaluateCustomService kefuId or solveStatus should not be null!");
        }
    }

    public void leaveMessageToCustomService(String kefuId, Map<String, String> contentMap, final OperationCallback operationCallback) {
        CSLeaveMessage csLeaveMessage = new CSLeaveMessage();
        csLeaveMessage.setDataSet(contentMap);
        RongIMClient.getInstance().sendMessage(ConversationType.CUSTOMER_SERVICE, kefuId, csLeaveMessage, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                if (operationCallback != null) {
                    operationCallback.onSuccess();
                }

            }

            public void onError(Message message, ErrorCode errorCode) {
                if (operationCallback != null) {
                    operationCallback.onError(errorCode);
                }

            }
        });
    }

    public void stopCustomService(String kefuId) {
        if (TextUtils.isEmpty(kefuId)) {
            RLog.e(TAG, "stopCustomService kefuId should not be null!");
        } else if (!this.customServiceCache.containsKey(kefuId)) {
            RLog.e(TAG, "stopCustomService " + kefuId + " is not started yet!");
        } else {
            CustomServiceManager.CustomServiceProfile profile = (CustomServiceManager.CustomServiceProfile)this.customServiceCache.get(kefuId);
            CSSuspendMessage csSuspendMessage = CSSuspendMessage.obtain(profile.sid, profile.uid, profile.pid);
            RongIMClient.getInstance().sendMessage(ConversationType.CUSTOMER_SERVICE, kefuId, csSuspendMessage, (String)null, (String)null, new ISendMessageCallback() {
                public void onAttached(Message message) {
                }

                public void onSuccess(Message message) {
                }

                public void onError(Message message, ErrorCode errorCode) {
                    RLog.e(CustomServiceManager.TAG, "stopCustomService onError " + errorCode);
                }
            });
            this.customServiceCache.remove(kefuId);
        }
    }

    public void setHumanEvaluateListener(CustomServiceManager.OnHumanEvaluateListener listener) {
        this.onHumanEvaluateListener = listener;
    }

    public interface OnHumanEvaluateListener {
        void onHumanEvaluate(JSONObject var1);
    }

    private static enum EvaluateType {
        EVALUATE_ROBOT(0),
        EVALUATE_HUMAN(1);

        int value;

        private EvaluateType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    private static class SingletonHolder {
        static CustomServiceManager sIns = new CustomServiceManager();

        private SingletonHolder() {
        }
    }

    private static class CustomServiceProfile {
        CustomServiceMode mode;
        String pid;
        String uid;
        String sid;
        long time;
        String welcome;
        String name;
        String portrait;
        ArrayList<CSGroupItem> groupList;
        WeakReference<ICustomServiceListener> customServiceListener;
        boolean showResolveStatus;

        CustomServiceProfile(ICustomServiceListener listener) {
            this.customServiceListener = new WeakReference(listener);
        }
    }
}
