//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.HandlerThread;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.PushErrorCode;
import io.rong.push.PushType;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.common.RLog;
import io.rong.push.core.PushClient;
import io.rong.push.core.PushClient.ClientListener;
import io.rong.push.core.PushClient.QueryCallback;
import io.rong.push.core.PushClient.QueryMethod;
import io.rong.push.core.PushConnectHandler;
import io.rong.push.core.PushNaviClient;
import io.rong.push.core.PushProtocalStack.PublishMessage;
import io.rong.push.core.PushUtils;
import io.rong.push.rongpush.PushReceiver;

public class PushConfigManager {
    private final String TAG = PushConfigManager.class.getSimpleName();
    private static PushClient pushClient;
    private boolean isInitialized;
    private PushConfigReceiver pushConfigReceiver;
    private PushConfig pushConfig;
    boolean isDisconnected;
    private PushNaviClient pushNaviClient;
    private PushConnectHandler connectHandler;
    private String token;
    private IPushConfigObserver configResultCallback;

    public PushConfigManager() {
    }

    public void init(Context context, PushConfig pushConfig, IPushConfigObserver resultCallback) {
        if (this.isInitialized) {
            RLog.d(this.TAG, "already initialized. Return directly!");
        } else {
            this.configResultCallback = resultCallback;
            this.pushConfig = pushConfig;
            HandlerThread workThread = new HandlerThread("PushConfig");
            workThread.start();
            this.connectHandler = new PushConnectHandler(workThread.getLooper());
            this.registerConfigReceiver(context);
            if (this.pushNaviClient == null) {
                this.pushNaviClient = new PushNaviClient();
                this.pushNaviClient.setPushNaviUrl(pushConfig.getPushDomain());
            }

            if (pushClient == null) {
                pushClient = new PushClient(context, pushConfig.getAppKey(), DeviceUtils.getPhoneInformation(context), new ClientListener() {
                    public void onMessageArrived(PublishMessage msg) {
                    }

                    public void onDisConnected() {
                        PushConfigManager.this.isDisconnected = true;
                    }

                    public void onPingSuccess() {
                    }

                    public void onPingFailure() {
                    }
                });
            }

            this.isInitialized = true;
        }
    }

    public void startConfig(final Context context) {
        RLog.d(this.TAG, "startConfig");
        if (!this.isInitialized) {
            RLog.e(this.TAG, "startConfig. Doesn't init, ignore this event.");
        } else {
            this.connectHandler.post(new Runnable() {
                public void run() {
                    PushConfigManager.this.pushNaviClient.getPushServerIPs(context, PushConfigManager.this.pushConfig.getAppKey(), false, new PushNaviObserver() {
                        public void onSuccess(ArrayList<String> addressList) {
                            PushConfigManager.this.connectHandler.connect(context, PushConfigManager.pushClient, addressList, PushConfigManager.this.pushConfig.getAppKey(), new IResultCallback<String>() {
                                public void onSuccess(String s) {
                                    StringBuilder builder = new StringBuilder();
                                    Iterator<PushType> it = PushConfigManager.this.pushConfig.getEnabledPushTypes().iterator();
                                    boolean first = true;

                                    while(it.hasNext()) {
                                        PushType current = (PushType)it.next();
                                        if (!first) {
                                            builder.append("|");
                                        }

                                        first = false;
                                        builder.append(current.getName());
                                    }

                                    String enabledPushTypes = builder.toString();
                                    PushType preferPushType = PushUtils.getPreferPushType(context, PushConfigManager.this.pushConfig);
                                    PushConfigManager.this.queryPushType(context, PushConfigManager.this.pushConfig.getAppKey(), enabledPushTypes, preferPushType);
                                }

                                public void onError(PushErrorCode code) {
                                    RLog.e(PushConfigManager.this.TAG, "error when connect to server.");
                                    PushConfigManager.pushClient.disconnect();
                                    PushConfigManager.this.configResultCallback.onError(code);
                                }
                            });
                        }

                        public void onError(PushErrorCode errorCode) {
                            RLog.e(PushConfigManager.this.TAG, "error when connect to navi.");
                            PushConfigManager.pushClient.disconnect();
                            PushConfigManager.this.configResultCallback.onError(errorCode);
                        }
                    });
                }
            });
        }
    }

    public void reConfig(Context context) {
        if (!this.isInitialized) {
            RLog.e(this.TAG, "reConfig. Doesn't init, ignore this event.");
        } else {
            this.startConfig(context);
        }
    }

    private void queryPushType(final Context context, final String appKey, final String enabledPushTypes, final PushType preferPushType) {
        this.connectHandler.post(new Runnable() {
            public void run() {
                String packageName = context.getPackageName().replace("-", "_");
                String deviceId = DeviceUtils.getDeviceId(context, appKey);
                String manufacturer = DeviceUtils.getDeviceManufacturer();
                String information = String.format("%s-%s-%s-%s", enabledPushTypes, appKey, packageName, manufacturer);
                PushConfigManager.pushClient.query(QueryMethod.GET_PUSH_TYPE, information, deviceId, new QueryCallback() {
                    public void onSuccess(String pushType) {
                        RLog.d(PushConfigManager.this.TAG, "query result:" + pushType + ";prefer type:" + preferPushType.getName());
                        if (pushType.equals(preferPushType.getName()) || pushType.equals(PushType.GOOGLE_GCM.getName()) || pushType.equals(PushType.GOOGLE_FCM.getName()) || preferPushType.equals(PushType.UNKNOWN) && pushType.equals(PushType.RONG.getName())) {
                            if (pushType.equals(PushType.RONG.getName())) {
                                PushConfigManager.this.configResultCallback.onSuccess(PushType.RONG);
                            } else {
                                PushConfigManager.this.configResultCallback.onGetPushType(PushType.getType(pushType));
                            }
                        } else {
                            PushConfigManager.this.configResultCallback.onFail(preferPushType, PushErrorCode.NOT_REGISTER_IN_ADMIN);
                        }

                    }

                    public void onFailure(PushErrorCode code) {
                        PushConfigManager.pushClient.disconnect();
                        PushConfigManager.this.configResultCallback.onError(code);
                        RLog.e(PushConfigManager.this.TAG, "Failure when query!");
                    }
                });
            }
        });
    }

    public void setToken(final Context context, final PushConfig pushConfig, final String token) {
        if (!this.isInitialized) {
            RLog.e(this.TAG, "setToken. Doesn't init, ignore this event.");
        } else {
            this.token = token;
            this.connectHandler.post(new Runnable() {
                public void run() {
                    String manufacturer = Build.MANUFACTURER;
                    String model = Build.MODEL;
                    if (manufacturer == null) {
                        manufacturer = "";
                    }

                    if (model == null) {
                        model = "";
                    }

                    String information = String.format("%s|%s|%s|%s|%s", token, pushConfig.getAppKey(), manufacturer, model, "4.0.0.1");
                    RLog.d(PushConfigManager.this.TAG, "setToken. information:" + information);
                    String deviceId = DeviceUtils.getDeviceId(context, pushConfig.getAppKey());
                    String[] tokenInfoArray = token.split("\\|");
                    final String finalPushType = tokenInfoArray[0];
                    PushConfigManager.pushClient.query(QueryMethod.SET_TOKEN, information, deviceId, new QueryCallback() {
                        public void onSuccess(String pushType) {
                            RLog.d(PushConfigManager.this.TAG, "setToken.onSuccess.");
                            PushConfigManager.this.configResultCallback.onSuccess(PushType.getType(finalPushType));
                        }

                        public void onFailure(PushErrorCode code) {
                            PushConfigManager.pushClient.disconnect();
                            PushConfigManager.this.configResultCallback.onError(code);
                            RLog.e(PushConfigManager.this.TAG, "setToken.onFailure.");
                        }
                    });
                }
            });
        }
    }

    private void registerConfigReceiver(Context context) {
        RLog.d(this.TAG, "registerConfigReceiver");
        this.pushConfigReceiver = new PushConfigReceiver();

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            context.registerReceiver(this.pushConfigReceiver, intentFilter);
        } catch (Exception var3) {
            RLog.i(this.TAG, "registerConfigReceiver failed: " + var3.getMessage());
        }

    }

    public void finishConfig(Context context, String pushType) {
        PushCacheHelper.getInstance().savePushConfigInfo(context, pushType);
        PushCacheHelper.getInstance().saveTokenInfo(context, this.token);
        PushCacheHelper.getInstance().savePushDomain(context, this.pushConfig.getPushDomain());

        try {
            if (this.pushConfigReceiver != null) {
                context.unregisterReceiver(this.pushConfigReceiver);
            }
        } catch (Exception var5) {
            RLog.i(this.TAG, var5.getMessage());
        }

        pushClient.disconnect();
        if (!TextUtils.isEmpty(pushType) && !pushType.equals(PushType.RONG.getName())) {
            try {
                ComponentName receiver = new ComponentName(context, PushReceiver.class);
                context.getPackageManager().setComponentEnabledSetting(receiver, 2, 1);
            } catch (Exception var4) {
            }
        }

    }
}
