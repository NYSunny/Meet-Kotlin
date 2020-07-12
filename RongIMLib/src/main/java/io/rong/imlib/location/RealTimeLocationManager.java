//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import io.rong.common.RLog;
import io.rong.imlib.IHandler;
import io.rong.imlib.MessageTag;
import io.rong.imlib.ModuleManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.ModuleManager.ConnectivityStateChangedListener;
import io.rong.imlib.ModuleManager.MessageRouter;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.location.RealTimeLocation.OnRealTimeLocationQuitListener;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationErrorCode;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationStatus;
import io.rong.imlib.location.message.RealTimeLocationJoinMessage;
import io.rong.imlib.location.message.RealTimeLocationQuitMessage;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.location.message.RealTimeLocationStatusMessage;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.Conversation.ConversationType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RealTimeLocationManager implements OnRealTimeLocationQuitListener {
    private static final String TAG = RealTimeLocationManager.class.getSimpleName();
    private static final int TIMEOUT_INTERVAL = 30000;
    private static final int DEFALUT_MAX_PERTICIPANT = 5;
    private Context mContext;
    private HashMap<String, RealTimeLocationManager.RealTimeInstance> mInsMap;
    private List<Class<? extends MessageContent>> mRtMessages;
    private boolean mInitialized;
    private int maxParticipants;
    private String[] supportConversationTypes;
    private OnReceiveMessageListener mReceiveMessageListener;

    private RealTimeLocationManager() {
        this.maxParticipants = 5;
        this.supportConversationTypes = null;
        this.mInsMap = new HashMap();
        this.mRtMessages = new ArrayList();
        this.mInitialized = false;
    }

    public void init(Context context, OnReceiveMessageListener listener, IHandler stub) {
        RLog.i(TAG, "init " + this.mInitialized);
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.mContext = context;
            this.mRtMessages.add(RealTimeLocationStartMessage.class);
            this.mRtMessages.add(RealTimeLocationJoinMessage.class);
            this.mRtMessages.add(RealTimeLocationQuitMessage.class);
            this.mRtMessages.add(RealTimeLocationStatusMessage.class);
            this.mReceiveMessageListener = listener;
            ModuleManager.addMessageRouter(new MessageRouter() {
                public boolean onReceived(Message message, int left, boolean offline, int cmdLeft) {
                    boolean contains = RealTimeLocationManager.this.mRtMessages.contains(message.getContent().getClass());
                    long sentTime = RealTimeLocationManager.this.getDeltaTime(message.getSentTime());
                    if (contains && sentTime < 30000L && left == 0) {
                        MessageContent content = message.getContent();
                        RealTimeLocation rt;
                        android.os.Message msgx;
                        if (content instanceof RealTimeLocationStartMessage) {
                            rt = RealTimeLocationManager.this.getRealTimeLocation(message.getConversationType(), message.getTargetId());
                            msgx = android.os.Message.obtain();
                            msgx.what = 3;
                            msgx.obj = message.getSenderUserId();
                            rt.sendMessage(msgx);
                            return false;
                        }

                        if (content instanceof RealTimeLocationJoinMessage) {
                            rt = RealTimeLocationManager.this.getRealTimeLocation(message.getConversationType(), message.getTargetId());
                            msgx = android.os.Message.obtain();
                            msgx.what = 4;
                            msgx.obj = message.getSenderUserId();
                            rt.sendMessage(msgx);
                        } else if (content instanceof RealTimeLocationQuitMessage) {
                            RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)RealTimeLocationManager.this.mInsMap.get(message.getConversationType().getName() + message.getTargetId());
                            if (rtIns != null && rtIns.realTimeLocation != null) {
                                android.os.Message msg = android.os.Message.obtain();
                                msg.what = 5;
                                msg.obj = message.getSenderUserId();
                                rtIns.realTimeLocation.sendMessage(msg);
                            }
                        } else if (content instanceof RealTimeLocationStatusMessage) {
                            rt = RealTimeLocationManager.this.getRealTimeLocation(message.getConversationType(), message.getTargetId());
                            msgx = android.os.Message.obtain();
                            msgx.what = 6;
                            msgx.obj = message;
                            rt.sendMessage(msgx);
                        }
                    }

                    return contains;
                }
            });
            ModuleManager.addConnectivityStateChangedListener(new ConnectivityStateChangedListener() {
                public void onChanged(ConnectionStatus state) {
                    if (state != null && state.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                        Collection<RealTimeLocationManager.RealTimeInstance> collections = RealTimeLocationManager.this.mInsMap.values();
                        Iterator var3 = collections.iterator();

                        while(var3.hasNext()) {
                            RealTimeLocationManager.RealTimeInstance rt = (RealTimeLocationManager.RealTimeInstance)var3.next();
                            if (rt.realTimeLocation != null && rt.realTimeLocation.getHandler() != null) {
                                rt.realTimeLocation.getHandler().sendEmptyMessage(13);
                            }
                        }
                    }

                }
            });
            Resources resources = this.mContext.getResources();

            try {
                this.maxParticipants = resources.getInteger(resources.getIdentifier("rc_max_realtime_location_participants", "integer", this.mContext.getPackageName()));
            } catch (NotFoundException var7) {
                this.maxParticipants = 5;
            }

            try {
                this.supportConversationTypes = resources.getStringArray(resources.getIdentifier("rc_realtime_support_conversation_types", "array", context.getPackageName()));
            } catch (Exception var6) {
                RLog.w(TAG, "No conversation type is setup for realtime location.");
            }
        }

        this.registerMessage(stub);
    }

    private long getDeltaTime(long sentTime) {
        long deltaTime = RongIMClient.getInstance().getDeltaTime();
        long normalTime = System.currentTimeMillis() - deltaTime;
        return normalTime - sentTime;
    }

    private void registerMessage(IHandler stub) {
        try {
            stub.registerMessageType(RealTimeLocationStartMessage.class.getName());
            Iterator var2 = this.mRtMessages.iterator();

            while(var2.hasNext()) {
                Class<? extends MessageContent> msg = (Class)var2.next();
                String obj = ((MessageTag)msg.getAnnotation(MessageTag.class)).value();
                stub.registerMessageType(msg.getName());
                stub.registerCmdMsgType(obj);
            }
        } catch (Exception var5) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            var5.printStackTrace(printWriter);
            RLog.e(TAG, "Exception : \n" + stringWriter.toString());
        }

    }

    public static RealTimeLocationManager getInstance() {
        return RealTimeLocationManager.SingletonHolder.sIns;
    }

    public void onRealTimeLocationQuit(ConversationType type, String targetId) {
        RealTimeLocationManager.RealTimeInstance realTimeInstance = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        if (realTimeInstance != null) {
            realTimeInstance.realTimeLocation.destroy();
            realTimeInstance.realTimeLocation = null;
        }

    }

    private RealTimeLocation getRealTimeLocation(ConversationType type, String targetId) {
        String key = type.getName() + targetId;
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(key);
        if (rtIns == null) {
            rtIns = new RealTimeLocationManager.RealTimeInstance();
            this.mInsMap.put(key, rtIns);
        }

        if (rtIns.realTimeLocation == null) {
            rtIns.realTimeLocation = new RealTimeLocation(this.mContext, type, targetId, this.mReceiveMessageListener);
            rtIns.realTimeLocation.addListener(rtIns.observer);
            rtIns.realTimeLocation.setOnRealTimeLocationQuitListener(this);
        }

        return rtIns.realTimeLocation;
    }

    public int setupRealTimeLocation(ConversationType conversationType, String targetId) {
        int errorCode = 0;
        String key = conversationType.getName() + targetId;
        if (this.supportConversationTypes != null) {
            String[] var5 = this.supportConversationTypes;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String type = var5[var7];
                if (conversationType.getName().equals(type)) {
                    RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(key);
                    if (rtIns != null && rtIns.realTimeLocation != null && !rtIns.realTimeLocation.getRealTimeLocationCurrentState().equals(RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE)) {
                        errorCode = RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_IS_ON_GOING.getValue();
                    }

                    return errorCode;
                }
            }
        } else if (conversationType.equals(ConversationType.PRIVATE)) {
            RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(key);
            if (rtIns != null && rtIns.realTimeLocation != null && !rtIns.realTimeLocation.getRealTimeLocationCurrentState().equals(RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE)) {
                errorCode = RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_IS_ON_GOING.getValue();
            }

            return errorCode;
        }

        return RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_CONVERSATION_NOT_SUPPORT.getValue();
    }

    public int startRealTimeLocation(ConversationType type, String targetId) {
        boolean typeIsDefined = false;
        String key = type.getName() + targetId;
        RLog.i(TAG, "startRealTimeLocation " + key);
        if (this.supportConversationTypes != null || !type.equals(ConversationType.PRIVATE)) {
            if (this.supportConversationTypes != null) {
                String[] var5 = this.supportConversationTypes;
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    String defType = var5[var7];
                    if (type.getName().equals(defType)) {
                        typeIsDefined = true;
                    }
                }
            }

            if (!typeIsDefined) {
                return RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_CONVERSATION_NOT_SUPPORT.getValue();
            }
        }

        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(key);
        if (rtIns == null) {
            rtIns = new RealTimeLocationManager.RealTimeInstance();
            this.mInsMap.put(key, rtIns);
        }

        if (rtIns.realTimeLocation == null) {
            rtIns.realTimeLocation = new RealTimeLocation(this.mContext, type, targetId, this.mReceiveMessageListener);
            rtIns.realTimeLocation.addListener(rtIns.observer);
            rtIns.realTimeLocation.setOnRealTimeLocationQuitListener(this);
        }

        rtIns.realTimeLocation.sendMessage(0);
        return !rtIns.realTimeLocation.gpsIsAvailable() ? RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_GPS_DISABLED.getValue() : RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_SUCCESS.getValue();
    }

    public int joinRealTimeLocation(ConversationType type, String targetId) {
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        RLog.i(TAG, "joinRealTimeLocation " + type.getName() + targetId);
        if (rtIns != null && rtIns.realTimeLocation != null) {
            List<String> participants = this.getRealTimeLocationParticipants(type, targetId);
            if (participants != null && this.maxParticipants <= participants.size()) {
                return RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_EXCEED_MAX_PARTICIPANT.getValue();
            } else {
                rtIns.realTimeLocation.sendMessage(1);
                return !rtIns.realTimeLocation.gpsIsAvailable() ? RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_GPS_DISABLED.getValue() : RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_SUCCESS.getValue();
            }
        } else {
            RLog.e(TAG, "joinRealTimeLocation No instance!");
            return RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_NOT_INIT.getValue();
        }
    }

    public void quitRealTimeLocation(ConversationType type, String targetId) {
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        RLog.i(TAG, "quitRealTimeLocation " + type.getName() + targetId);
        if (rtIns != null && rtIns.realTimeLocation != null) {
            rtIns.realTimeLocation.sendMessage(2);
            rtIns.observer.onParticipantsQuit(targetId);
            rtIns.observer.onStatusChange(RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING);
        }
    }

    public List<String> getRealTimeLocationParticipants(ConversationType type, String targetId) {
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        return rtIns != null && rtIns.realTimeLocation != null ? rtIns.realTimeLocation.getParticipants() : null;
    }

    public RealTimeLocationStatus getRealTimeLocationCurrentState(ConversationType type, String targetId) {
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        return rtIns != null && rtIns.realTimeLocation != null ? rtIns.realTimeLocation.getRealTimeLocationCurrentState() : RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE;
    }

    public void addListener(ConversationType type, String targetId, RealTimeLocationObserver observer) {
        RLog.i(TAG, "addListener");
        String key = type.getName() + targetId;
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(key);
        if (rtIns != null) {
            rtIns.observer = observer;
        } else {
            rtIns = new RealTimeLocationManager.RealTimeInstance();
            rtIns.observer = observer;
            this.mInsMap.put(key, rtIns);
        }

        if (rtIns.realTimeLocation != null) {
            rtIns.realTimeLocation.addListener(observer);
        }

    }

    public void removeListener(ConversationType type, String targetId) {
        String key = type.getName() + targetId;
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(key);
        if (rtIns != null) {
            rtIns.observer = null;
            if (rtIns.realTimeLocation != null) {
                rtIns.realTimeLocation.deleteListener();
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public void updateLocation(ConversationType type, String targetId, double latitude, double longitude) {
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        if (rtIns != null && rtIns.realTimeLocation != null) {
            rtIns.realTimeLocation.updateLocation(latitude, longitude);
        } else {
            RLog.e(TAG, "getRealTimeLocationCurrentState No instance!");
        }
    }

    public void updateLocation(ConversationType type, String targetId, double latitude, double longitude, RealTimeLocationType realTimeLocationType) {
        RealTimeLocationManager.RealTimeInstance rtIns = (RealTimeLocationManager.RealTimeInstance)this.mInsMap.get(type.getName() + targetId);
        if (rtIns != null && rtIns.realTimeLocation != null) {
            rtIns.realTimeLocation.updateLocationWithType(latitude, longitude, realTimeLocationType);
        } else {
            RLog.e(TAG, "getRealTimeLocationCurrentState No instance!");
        }
    }

    private static class RealTimeInstance {
        RealTimeLocation realTimeLocation;
        RealTimeLocationObserver observer;

        private RealTimeInstance() {
        }
    }

    private static class SingletonHolder {
        static RealTimeLocationManager sIns = new RealTimeLocationManager();

        private SingletonHolder() {
        }
    }
}
