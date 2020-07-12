//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.AppOpsManagerCompat;
import io.rong.common.RLog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationErrorCode;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationStatus;
import io.rong.imlib.location.message.RealTimeLocationJoinMessage;
import io.rong.imlib.location.message.RealTimeLocationQuitMessage;
import io.rong.imlib.location.message.RealTimeLocationStartMessage;
import io.rong.imlib.location.message.RealTimeLocationStatusMessage;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.ReceivedStatus;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.imlib.navigation.LocationConfig;
import io.rong.imlib.navigation.NavigationCacheHelper;
import io.rong.imlib.stateMachine.State;
import io.rong.imlib.stateMachine.StateMachine;
import io.rong.message.InformationNotificationMessage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RealTimeLocation extends StateMachine {
    private static final String TAG = RealTimeLocation.class.getSimpleName();
    public static final int RC_REAL_TIME_LOCATION_EVENT_START = 0;
    public static final int RC_REAL_TIME_LOCATION_EVENT_JOIN = 1;
    public static final int RC_REAL_TIME_LOCATION_EVENT_QUIT = 2;
    public static final int RC_REAL_TIME_LOCATION_EVENT_PARTICIPANT_START = 3;
    public static final int RC_REAL_TIME_LOCATION_EVENT_PARTICIPANT_JOIN = 4;
    public static final int RC_REAL_TIME_LOCATION_EVENT_PARTICIPANT_QUIT = 5;
    public static final int RC_REAL_TIME_LOCATION_EVENT_RECEIVE_LOCATION_MESSAGE = 6;
    public static final int RC_REAL_TIME_LOCATION_EVENT_SEND_LOCATION_MESSAGE = 7;
    public static final int RC_REAL_TIME_LOCATION_EVENT_START_FAILURE = 8;
    public static final int RC_REAL_TIME_LOCATION_EVENT_JOIN_FAILURE = 9;
    public static final int RC_REAL_TIME_LOCATION_EVENT_REFRESH_TIME_EXPIRE = 10;
    public static final int RC_REAL_TIME_LOCATION_EVENT_TERMINAL = 11;
    public static final int RC_REAL_TIME_LOCATION_EVENT_PARTICIPANT_NO_RESPONSE = 12;
    public static final int RC_REAL_TIME_LOCATION_EVENT_NETWORK_UNAVAILABLE = 13;
    public static final int RC_REAL_TIME_LOCATION_EVENT_DISABLE_GPS = 14;
    private ConversationType mConversationType;
    private String mTargetId;
    private String mSelfId;
    private int mRefreshInterval = 10000;
    private Runnable mRefreshRunnable;
    private int mFilterDistance = 5;
    private double mLatitude = 0.0D;
    private double mLongitude = 0.0D;
    private RealTimeLocationType mRealTimeLocationType;
    private boolean mGpsEnable;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private ArrayList<String> mParticipants;
    private HashMap<String, RealTimeLocation.ParticipantWatcher> mWatcher;
    private RealTimeLocationObserver mObservers;
    private RealTimeLocationStatus mCurrentState;
    private RongIMClient mClient;
    private OnReceiveMessageListener mReceiveMessageListener;
    private RealTimeLocation.OnRealTimeLocationQuitListener mOnRealTimeLocationQuitListener;
    private Context mContext;
    private State mOutgoingState;
    private State mIncomingState;
    private State mConnectedState;
    private State mTerminalState;

    public void addListener(final RealTimeLocationObserver listener) {
        this.getHandler().post(new Runnable() {
            public void run() {
                RealTimeLocation.this.mObservers = listener;
            }
        });
    }

    public void deleteListener() {
        this.getHandler().post(new Runnable() {
            public void run() {
                RealTimeLocation.this.mObservers = null;
            }
        });
    }

    void setOnRealTimeLocationQuitListener(RealTimeLocation.OnRealTimeLocationQuitListener listener) {
        this.mOnRealTimeLocationQuitListener = listener;
    }

    public RealTimeLocation(Context context, ConversationType type, String targetId, OnReceiveMessageListener listener) {
        super(type == null ? "" : type.getName() + targetId);
        this.mRealTimeLocationType = RealTimeLocationType.UNKNOWN;
        this.mOutgoingState = new RealTimeLocation.OutgoingState();
        this.mIncomingState = new RealTimeLocation.IncomingState();
        this.mConnectedState = new RealTimeLocation.ConnectedState();
        this.mTerminalState = new RealTimeLocation.TerminalState();
        RLog.d(TAG, "RealTimeLocation");
        this.mContext = context;
        this.mConversationType = type;
        this.mTargetId = targetId;
        this.mClient = RongIMClient.getInstance();
        this.mCurrentState = RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE;
        this.mParticipants = new ArrayList();
        this.mWatcher = new HashMap();
        this.mReceiveMessageListener = listener;
        LocationConfig config = NavigationCacheHelper.getLocationConfig(context);
        if (config != null) {
            this.mFilterDistance = config.getDistanceFilter();
            this.mRefreshInterval = config.getRefreshInterval() * 1000;
        }

        this.mRefreshRunnable = new Runnable() {
            public void run() {
                RealTimeLocation.this.getHandler().sendEmptyMessage(10);
                RealTimeLocation.this.getHandler().postDelayed(RealTimeLocation.this.mRefreshRunnable, (long)RealTimeLocation.this.mRefreshInterval);
            }
        };
        this.mGpsEnable = this.isGpsEnable(context);
        State mIdleState = new RealTimeLocation.IdleState();
        this.addState(mIdleState);
        this.addState(this.mIncomingState, mIdleState);
        this.addState(this.mOutgoingState, mIdleState);
        this.addState(this.mConnectedState, mIdleState);
        this.addState(this.mTerminalState, mIdleState);
        this.setInitialState(mIdleState);
        this.start();
    }

    private void startTimer() {
        this.getHandler().removeCallbacks(this.mRefreshRunnable);
        this.getHandler().postDelayed(this.mRefreshRunnable, (long)this.mRefreshInterval);
    }

    private void stopTimer() {
        this.getHandler().removeCallbacks(this.mRefreshRunnable);
    }

    public void updateLocation(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public void updateLocationWithType(double latitude, double longitude, RealTimeLocationType realTimeLocationType) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRealTimeLocationType = realTimeLocationType;
    }

    public RealTimeLocationStatus getRealTimeLocationCurrentState() {
        return this.mCurrentState;
    }

    public boolean gpsIsAvailable() {
        return this.mGpsEnable;
    }

    public List<String> getParticipants() {
        return this.mParticipants;
    }

    private void gpsInit(Context context) {
        RLog.d(TAG, "gpsInit");
        this.mLocationManager = (LocationManager)context.getSystemService("location");
        if (this.mLocationManager != null && this.mLocationManager.isProviderEnabled("gps")) {
            this.mGpsEnable = true;
            this.mLocationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    RLog.d(RealTimeLocation.TAG, "onLocationChanged");
                    if (location != null) {
                        RealTimeLocation.this.mLatitude = location.getLatitude();
                        RealTimeLocation.this.mLongitude = location.getLongitude();
                    }

                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                    RLog.d(RealTimeLocation.TAG, "onStatusChanged");
                    switch(status) {
                        case 0:
                            RLog.i(RealTimeLocation.TAG, "当前GPS状态为服务区外状态");
                            break;
                        case 1:
                            RLog.i(RealTimeLocation.TAG, "当前GPS状态为暂停服务状态");
                            break;
                        case 2:
                            RLog.i(RealTimeLocation.TAG, "当前GPS状态为可见状态");
                    }

                }

                public void onProviderEnabled(String provider) {
                    RealTimeLocation.this.mGpsEnable = true;
                    if (ActivityCompat.checkSelfPermission(RealTimeLocation.this.mContext, "android.permission.ACCESS_FINE_LOCATION") != 0 && ActivityCompat.checkSelfPermission(RealTimeLocation.this.mContext, "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                        RLog.e(RealTimeLocation.TAG, "permission is not granted");
                    } else {
                        Location location = RealTimeLocation.this.mLocationManager.getLastKnownLocation(provider);
                        if (location != null) {
                            RealTimeLocation.this.mLatitude = location.getLatitude();
                            RealTimeLocation.this.mLongitude = location.getLongitude();
                        }

                    }
                }

                public void onProviderDisabled(String provider) {
                    RealTimeLocation.this.mGpsEnable = false;
                    RealTimeLocation.this.getHandler().sendEmptyMessage(14);
                }
            };
            String bestProvider = this.mLocationManager.getBestProvider(this.getCriteria(), true);
            Location location = null;
            if (bestProvider != null) {
                location = this.mLocationManager.getLastKnownLocation(bestProvider);
            }

            if (location != null) {
                this.mLatitude = location.getLatitude();
                this.mLongitude = location.getLongitude();
            }

            RLog.e(TAG, "gpsInit: location = " + (location != null ? "[ " + this.mLatitude + " " + this.mLongitude + " ]" : "null"));
        } else {
            RLog.e(TAG, "GSP is disabled");
        }
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(1);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(1);
        return criteria;
    }

    private void updateSelfLocation() {
        this.onReceiveLocation(this.mLatitude, this.mLongitude, this.mRealTimeLocationType, this.mSelfId);
    }

    private void sendStartMessage() {
        RealTimeLocationStartMessage start = RealTimeLocationStartMessage.obtain("start real time location.");
        String content = "收到一条位置共享消息";
        this.mClient.sendMessage(this.mConversationType, this.mTargetId, start, content, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
                if (RealTimeLocation.this.mReceiveMessageListener != null) {
                    message.setSentStatus(SentStatus.SENT);
                    RealTimeLocation.this.mReceiveMessageListener.onReceived(message, 0);
                }

            }

            public void onSuccess(Message message) {
                if (RealTimeLocation.this.getHandler() != null) {
                    RealTimeLocation.this.getHandler().sendEmptyMessage(7);
                }

            }

            public void onError(Message message, ErrorCode errorCode) {
                if (RealTimeLocation.this.getHandler() != null) {
                    RealTimeLocation.this.getHandler().sendEmptyMessage(8);
                }

            }
        });
    }

    private void sendJoinMessage() {
        RealTimeLocationJoinMessage content = RealTimeLocationJoinMessage.obtain("join real time location.");
        this.mClient.sendMessage(this.mConversationType, this.mTargetId, content, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                if (RealTimeLocation.this.getHandler() != null) {
                    RealTimeLocation.this.getHandler().sendEmptyMessage(7);
                }

            }

            public void onError(Message message, ErrorCode errorCode) {
                if (RealTimeLocation.this.getHandler() != null) {
                    RealTimeLocation.this.getHandler().sendEmptyMessage(9);
                }

            }
        });
    }

    private void sendQuitMessage() {
        RealTimeLocationQuitMessage content = RealTimeLocationQuitMessage.obtain("quit real time location.");
        this.mClient.sendMessage(this.mConversationType, this.mTargetId, content, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
            }

            public void onError(Message message, ErrorCode errorCode) {
            }
        });
    }

    private void sendLocationMessage() {
        MessageContent content = RealTimeLocationStatusMessage.obtain(this.mLatitude, this.mLongitude, this.mRealTimeLocationType);
        this.mClient.sendMessage(this.mConversationType, this.mTargetId, content, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
                if (RealTimeLocation.this.getHandler() != null && RealTimeLocation.this.getHandler().hasMessages(13)) {
                    RealTimeLocation.this.getHandler().removeMessages(13);
                }

            }

            public void onError(Message message, ErrorCode errorCode) {
                RLog.e(RealTimeLocation.TAG, "sendLocationMessage error = " + errorCode);
                if (RealTimeLocation.this.getHandler() != null && !RealTimeLocation.this.getHandler().hasMessages(13)) {
                    RealTimeLocation.this.getHandler().sendEmptyMessageDelayed(13, (long)(RealTimeLocation.this.mRefreshInterval * 3));
                }

            }
        });
    }

    private boolean isGpsEnable(Context context) {
        String opStr = AppOpsManagerCompat.permissionToOp("android.permission.ACCESS_FINE_LOCATION");
        if (opStr == null) {
            return true;
        } else {
            return context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0;
        }
    }

    private void onStatusChanged(RealTimeLocationStatus state) {
        if (this.mObservers != null) {
            this.mObservers.onStatusChange(state);
        }

    }

    private void onParticipantQuit(String id) {
        if (this.mObservers != null) {
            this.mObservers.onParticipantsQuit(id);
        }

    }

    private void onParticipantsJoin(String id) {
        if (this.mObservers != null) {
            this.mObservers.onParticipantsJoin(id);
        }

    }

    private void onReceiveLocation(double latitude, double longitude, RealTimeLocationType type, String id) {
        if (this.mObservers != null) {
            this.mObservers.onReceiveLocationWithType(latitude, longitude, type, id);
        }

    }

    private void onError(RealTimeLocationErrorCode errorCode) {
        if (this.mObservers != null) {
            this.mObservers.onError(errorCode);
        }

    }

    void destroy() {
        RLog.d(TAG, "destroy");
        this.quitNow();
        String str = this.mContext.getResources().getString(this.mContext.getResources().getIdentifier("rc_location_sharing_ended", "string", this.mContext.getPackageName()));
        InformationNotificationMessage msg = InformationNotificationMessage.obtain(str);
        RongIMClient.getInstance().insertIncomingMessage(this.mConversationType, this.mTargetId, RongIMClient.getInstance().getCurrentUserId(), (ReceivedStatus)null, msg, System.currentTimeMillis() - RongIMClient.getInstance().getDeltaTime(), new ResultCallback<Message>() {
            public void onSuccess(Message message) {
                if (RealTimeLocation.this.mReceiveMessageListener != null) {
                    RealTimeLocation.this.mReceiveMessageListener.onReceived(message, 0);
                }

            }

            public void onError(ErrorCode e) {
            }
        });
    }

    interface OnRealTimeLocationQuitListener {
        void onRealTimeLocationQuit(ConversationType var1, String var2);
    }

    private class ParticipantWatcher {
        Runnable runnable;
        String id;

        ParticipantWatcher(final String id) {
            this.id = id;
            this.runnable = new Runnable() {
                public void run() {
                    android.os.Message msg = android.os.Message.obtain();
                    msg.what = 12;
                    msg.obj = id;
                    RealTimeLocation.this.getHandler().sendMessage(msg);
                }
            };
        }

        public void start() {
            RealTimeLocation.this.getHandler().postDelayed(this.runnable, (long)(RealTimeLocation.this.mRefreshInterval * 3));
        }

        public void stop() {
            RealTimeLocation.this.getHandler().removeCallbacks(this.runnable);
        }

        public void update() {
            RealTimeLocation.this.getHandler().removeCallbacks(this.runnable);
            RealTimeLocation.this.getHandler().postDelayed(this.runnable, (long)(RealTimeLocation.this.mRefreshInterval * 3));
        }
    }

    private class TerminalState extends State {
        private TerminalState() {
        }

        public void enter() {
            RLog.d(RealTimeLocation.TAG, "terminal enter : current = " + RealTimeLocation.this.mCurrentState);
            RealTimeLocation.this.mParticipants.clear();
            RealTimeLocation.this.stopTimer();
            if (RealTimeLocation.this.mWatcher.size() > 0) {
                Collection<RealTimeLocation.ParticipantWatcher> c = RealTimeLocation.this.mWatcher.values();
                Iterator var2 = c.iterator();

                while(var2.hasNext()) {
                    RealTimeLocation.ParticipantWatcher participantWatcher = (RealTimeLocation.ParticipantWatcher)var2.next();
                    participantWatcher.stop();
                }

                RealTimeLocation.this.mWatcher.clear();
            }

            RealTimeLocation.this.getHandler().sendEmptyMessage(11);
        }

        public boolean processMessage(android.os.Message msg) {
            RLog.i(RealTimeLocation.TAG, this.getName() + ", msg = " + msg.what);
            switch(msg.what) {
                case 11:
                    if (RealTimeLocation.this.mOnRealTimeLocationQuitListener != null) {
                        RealTimeLocation.this.mOnRealTimeLocationQuitListener.onRealTimeLocationQuit(RealTimeLocation.this.mConversationType, RealTimeLocation.this.mTargetId);
                    }
                default:
                    return true;
            }
        }
    }

    private class ConnectedState extends State {
        private ConnectedState() {
        }

        public void enter() {
            RealTimeLocation.this.mCurrentState = RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_CONNECTED;
            RealTimeLocation.this.onStatusChanged(RealTimeLocation.this.mCurrentState);
            RealTimeLocation.this.startTimer();
            RLog.d(RealTimeLocation.TAG, "connected enter : current = " + RealTimeLocation.this.mCurrentState);
        }

        public boolean processMessage(android.os.Message msg) {
            RLog.i(RealTimeLocation.TAG, this.getName() + ", msg = " + msg.what);
            String id;
            RealTimeLocation.ParticipantWatcher watcher;
            switch(msg.what) {
                case 2:
                    RealTimeLocation.this.sendQuitMessage();
                    RealTimeLocation.this.mParticipants.remove(RealTimeLocation.this.mSelfId);
                    if (RealTimeLocation.this.mParticipants.size() == 0) {
                        RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    } else {
                        RealTimeLocation.this.transitionTo(RealTimeLocation.this.mIncomingState);
                    }
                case 3:
                case 11:
                default:
                    break;
                case 4:
                    id = (String)((String)msg.obj);
                    watcher = RealTimeLocation.this.new ParticipantWatcher(id);
                    watcher.start();
                    RealTimeLocation.this.mWatcher.put(id, watcher);
                    if (!RealTimeLocation.this.mParticipants.contains(id)) {
                        RealTimeLocation.this.mParticipants.add(id);
                    }

                    RealTimeLocation.this.onParticipantsJoin(id);
                    RealTimeLocation.this.getHandler().sendEmptyMessage(7);
                    break;
                case 5:
                case 12:
                    id = (String)((String)msg.obj);
                    RealTimeLocation.this.mParticipants.remove(id);
                    RealTimeLocation.ParticipantWatcher pw = (RealTimeLocation.ParticipantWatcher)RealTimeLocation.this.mWatcher.get(id);
                    if (pw != null) {
                        pw.stop();
                    }

                    RealTimeLocation.this.mWatcher.remove(id);
                    RealTimeLocation.this.onParticipantQuit(id);
                    if (RealTimeLocation.this.mParticipants.size() == 1) {
                        RealTimeLocation.this.transitionTo(RealTimeLocation.this.mOutgoingState);
                    }
                    break;
                case 6:
                    Message message = (Message)((Message)msg.obj);
                    id = message.getSenderUserId();
                    RealTimeLocation.ParticipantWatcher pwTemp = (RealTimeLocation.ParticipantWatcher)RealTimeLocation.this.mWatcher.get(id);
                    if (pwTemp == null) {
                        watcher = RealTimeLocation.this.new ParticipantWatcher(id);
                        watcher.start();
                        RealTimeLocation.this.mWatcher.put(id, watcher);
                        if (!RealTimeLocation.this.mParticipants.contains(id)) {
                            RealTimeLocation.this.mParticipants.add(id);
                        }

                        RealTimeLocation.this.onParticipantsJoin(id);
                    } else {
                        pwTemp.update();
                    }

                    MessageContent content = message.getContent();
                    RealTimeLocationStatusMessage coor = (RealTimeLocationStatusMessage)content;
                    RealTimeLocation.this.onReceiveLocation(coor.getLatitude(), coor.getLongitude(), coor.getRealTimeLocationType(), id);
                    break;
                case 7:
                case 10:
                    RealTimeLocation.this.sendLocationMessage();
                    RealTimeLocation.this.updateSelfLocation();
                    break;
                case 8:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_START_FAILURE);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    break;
                case 9:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_JOIN_FAILURE);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    break;
                case 13:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_NETWORK_UNAVAILABLE);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    break;
                case 14:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_GPS_DISABLED);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
            }

            return true;
        }
    }

    private class IncomingState extends State {
        private IncomingState() {
        }

        public void enter() {
            RealTimeLocation.this.stopTimer();
            RealTimeLocation.this.mCurrentState = RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_INCOMING;
            RealTimeLocation.this.onStatusChanged(RealTimeLocation.this.mCurrentState);
            RLog.d(RealTimeLocation.TAG, "incoming enter : current = " + RealTimeLocation.this.mCurrentState);
        }

        public boolean processMessage(android.os.Message msg) {
            RLog.i(RealTimeLocation.TAG, this.getName() + ", msg = " + msg.what);
            RealTimeLocation.ParticipantWatcher watcher;
            String id;
            switch(msg.what) {
                case 1:
                    RealTimeLocation.this.sendJoinMessage();
                    RealTimeLocation.this.mSelfId = RealTimeLocation.this.mClient.getCurrentUserId();
                    RealTimeLocation.this.mParticipants.add(RealTimeLocation.this.mSelfId);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mConnectedState);
                case 2:
                case 3:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    break;
                case 4:
                    String userId = (String)msg.obj;
                    watcher = RealTimeLocation.this.new ParticipantWatcher(userId);
                    watcher.start();
                    RealTimeLocation.this.mWatcher.put(userId, watcher);
                    RealTimeLocation.this.mParticipants.add(userId);
                    RealTimeLocation.this.onParticipantsJoin(userId);
                    break;
                case 5:
                case 12:
                    id = (String)msg.obj;
                    RealTimeLocation.ParticipantWatcher participantWatcher = (RealTimeLocation.ParticipantWatcher)RealTimeLocation.this.mWatcher.get(id);
                    if (participantWatcher != null) {
                        participantWatcher.stop();
                        RealTimeLocation.this.mWatcher.remove(id);
                        RealTimeLocation.this.mParticipants.remove(id);
                        RealTimeLocation.this.onParticipantQuit(id);
                    }

                    if (RealTimeLocation.this.mParticipants.size() == 0) {
                        RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    }
                    break;
                case 6:
                    Message message = (Message)((Message)msg.obj);
                    id = message.getSenderUserId();
                    RealTimeLocation.ParticipantWatcher pw = (RealTimeLocation.ParticipantWatcher)RealTimeLocation.this.mWatcher.get(id);
                    if (pw == null) {
                        watcher = RealTimeLocation.this.new ParticipantWatcher(id);
                        watcher.start();
                        RealTimeLocation.this.mWatcher.put(id, watcher);
                        RealTimeLocation.this.mParticipants.add(id);
                        RealTimeLocation.this.onParticipantsJoin(id);
                    } else {
                        pw.update();
                    }

                    MessageContent content = message.getContent();
                    RealTimeLocationStatusMessage coor = (RealTimeLocationStatusMessage)content;
                    RealTimeLocation.this.onReceiveLocation(coor.getLatitude(), coor.getLongitude(), coor.getRealTimeLocationType(), id);
                    break;
                case 13:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_NETWORK_UNAVAILABLE);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    break;
                case 14:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_GPS_DISABLED);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
            }

            return true;
        }
    }

    private class OutgoingState extends State {
        private OutgoingState() {
        }

        public void enter() {
            RealTimeLocation.this.mCurrentState = RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_OUTGOING;
            RealTimeLocation.this.onStatusChanged(RealTimeLocation.this.mCurrentState);
            RealTimeLocation.this.startTimer();
            RLog.d(RealTimeLocation.TAG, "outgoing enter : current = " + RealTimeLocation.this.mCurrentState);
        }

        public boolean processMessage(android.os.Message msg) {
            RLog.i(RealTimeLocation.TAG, this.getName() + ", msg = " + msg.what);
            String userId;
            RealTimeLocation.ParticipantWatcher watcher;
            switch(msg.what) {
                case 2:
                    RealTimeLocation.this.sendQuitMessage();
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                case 3:
                case 5:
                case 9:
                case 11:
                case 12:
                default:
                    break;
                case 4:
                    userId = (String)((String)msg.obj);
                    watcher = RealTimeLocation.this.new ParticipantWatcher(userId);
                    watcher.start();
                    RealTimeLocation.this.mWatcher.put(userId, watcher);
                    RealTimeLocation.this.mParticipants.add(userId);
                    RealTimeLocation.this.onParticipantsJoin(userId);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mConnectedState);
                    break;
                case 6:
                    Message message = (Message)((Message)msg.obj);
                    userId = message.getSenderUserId();
                    if (!RealTimeLocation.this.mWatcher.containsKey(userId)) {
                        watcher = RealTimeLocation.this.new ParticipantWatcher(userId);
                        watcher.start();
                        RealTimeLocation.this.mWatcher.put(userId, watcher);
                        RealTimeLocation.this.mParticipants.add(userId);
                        RealTimeLocation.this.onParticipantsJoin(userId);
                        RealTimeLocation.this.transitionTo(RealTimeLocation.this.mConnectedState);
                    }
                    break;
                case 7:
                case 10:
                    RealTimeLocation.this.sendLocationMessage();
                    RealTimeLocation.this.updateSelfLocation();
                    break;
                case 8:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_START_FAILURE);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    break;
                case 13:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_NETWORK_UNAVAILABLE);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
                    break;
                case 14:
                    RealTimeLocation.this.onError(RealTimeLocationErrorCode.RC_REAL_TIME_LOCATION_GPS_DISABLED);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mTerminalState);
            }

            return true;
        }
    }

    private class IdleState extends State {
        private IdleState() {
        }

        public void enter() {
            RealTimeLocation.this.mCurrentState = RealTimeLocationStatus.RC_REAL_TIME_LOCATION_STATUS_IDLE;
            if (RealTimeLocation.this.mSelfId != null) {
                RealTimeLocation.this.onStatusChanged(RealTimeLocation.this.mCurrentState);
            }

            RLog.d(RealTimeLocation.TAG, "idle enter : current = " + RealTimeLocation.this.mCurrentState);
        }

        public boolean processMessage(android.os.Message msg) {
            RLog.i(RealTimeLocation.TAG, this.getName() + ", msg = " + msg.what);
            String id;
            RealTimeLocation.ParticipantWatcher watcher;
            switch(msg.what) {
                case 0:
                    RealTimeLocation.this.sendStartMessage();
                    RealTimeLocation.this.mSelfId = RealTimeLocation.this.mClient.getCurrentUserId();
                    RealTimeLocation.this.mParticipants.add(RealTimeLocation.this.mSelfId);
                    RealTimeLocation.this.updateSelfLocation();
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mOutgoingState);
                    break;
                case 3:
                    id = (String)((String)msg.obj);
                    watcher = RealTimeLocation.this.new ParticipantWatcher(id);
                    watcher.start();
                    RealTimeLocation.this.mWatcher.put(id, watcher);
                    RealTimeLocation.this.mParticipants.add(id);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mIncomingState);
                    break;
                case 6:
                    Message message = (Message)((Message)msg.obj);
                    id = message.getSenderUserId();
                    watcher = RealTimeLocation.this.new ParticipantWatcher(id);
                    watcher.start();
                    RealTimeLocation.this.mWatcher.put(id, watcher);
                    RealTimeLocation.this.mParticipants.add(id);
                    RealTimeLocation.this.transitionTo(RealTimeLocation.this.mIncomingState);
            }

            return true;
        }
    }
}
