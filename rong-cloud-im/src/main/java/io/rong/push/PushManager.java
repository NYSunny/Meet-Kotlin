//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.rong.imlib.common.DeviceUtils;
import io.rong.push.common.PushCacheHelper;
import io.rong.push.common.RLog;
import io.rong.push.core.PushUtils;
import io.rong.push.notification.PushNotificationMessage;
import io.rong.push.platform.IPush;
import io.rong.push.pushconfig.IPushConfigObserver;
import io.rong.push.pushconfig.PushConfig;
import io.rong.push.pushconfig.PushConfigManager;
import io.rong.push.pushconfig.PushFactory;
import io.rong.push.rongpush.PushReceiver;

public class PushManager {
    private static final String TAG = PushManager.class.getSimpleName();
    private static final String NAVI_IN_TOKEN_SPLIT_SYMBOL = "@";
    private PushConfigManager pushConfigManager;
    private PushConfig pushConfig;
    private ConcurrentMap<String, IPush> registeredPushMap;
    private String token;
    private Context context;
    private PushType serverPushType;

    private PushManager() {
        this.registeredPushMap = new ConcurrentHashMap();
    }

    public static PushManager getInstance() {
        return SingletonHolder.sIns;
    }

    public void init(Context context, PushConfig pushConfig) {
        this.pushConfig = pushConfig;
        this.context = context.getApplicationContext();
        PushType preferPushType = PushUtils.getPreferPushType(context, pushConfig);
        PushType cachedPushType = PushCacheHelper.getInstance().getConfigPushType(context);
        RLog.d(TAG, "preferPushType:" + preferPushType + "; cachedPushType:" + cachedPushType);
        String cachedEnablePush = PushCacheHelper.getInstance().getCachedEnablePushTypes(context);
        String cachedPushNavi = PushCacheHelper.getInstance().getCachedPushDomain(context);
        String cachedDeviceId = PushCacheHelper.getInstance().getCachedDeviceId(context);
        if (!pushConfig.getPushDomain().equals(cachedPushNavi)) {
            PushCacheHelper.getInstance().setPushServerInIMToken(context, (String)null);
        }

        boolean isConfigChanged = !TextUtils.isEmpty(cachedEnablePush) && (!pushConfig.getEncodedEnabledPushTypes().equals(cachedEnablePush) || !pushConfig.getPushDomain().equals(cachedPushNavi) || !cachedDeviceId.equals(DeviceUtils.getDeviceId(context, pushConfig.getAppKey())));
        RLog.d(TAG, "isConfigChanged:" + isConfigChanged + "; cachedEnablePush:" + cachedEnablePush);
        if ((preferPushType.equals(PushType.RONG) || PushCacheHelper.getInstance().isConfigDone(context)) && !isConfigChanged) {
            this.register(context, cachedPushType);
        } else {
            this.initPushConfig(context);
            this.pushConfigManager.startConfig(context);
        }

        PushCacheHelper.getInstance().savePushDomain(context, pushConfig.getPushDomain());
        PushCacheHelper.getInstance().cacheRongDeviceId(context, DeviceUtils.getDeviceId(context, pushConfig.getAppKey()));
        PushCacheHelper.getInstance().saveEnablePushTypes(context, pushConfig.getEncodedEnabledPushTypes());
    }

    private void initPushConfig(final Context context) {
        if (this.pushConfigManager != null) {
            RLog.e(TAG, "pushConfigManager already init. Return directly.");
        } else {
            this.pushConfigManager = new PushConfigManager();
            this.pushConfigManager.init(context, this.pushConfig, new IPushConfigObserver() {
                public void onSuccess(PushType pushType) {
                    if (PushManager.this.pushConfigManager != null) {
                        RLog.d(PushManager.TAG, "Success to config push: " + pushType.getName());
                        PushManager.this.pushConfigManager.finishConfig(context, pushType.getName());
                        PushManager.this.token = null;
                        PushManager.this.pushConfigManager = null;
                        if (pushType.equals(PushType.RONG)) {
                            PushManager.this.register(context, PushType.RONG);
                        }
                    }

                }

                public void onGetPushType(PushType pushType) {
                    RLog.d(PushManager.TAG, "Success to get pushType. Go to register : " + pushType.getName());
                    PushManager.this.serverPushType = pushType;
                    if (!TextUtils.isEmpty(PushManager.this.token) && PushManager.this.pushConfig != null) {
                        String[] tokenInfoArray = PushManager.this.token.split("\\|");
                        String cachedType = tokenInfoArray[0];
                        if (cachedType.equals(pushType.getName())) {
                            PushManager.this.pushConfigManager.setToken(context, PushManager.this.pushConfig, PushManager.this.token);
                        } else {
                            PushManager.this.register(context, pushType);
                            PushManager.this.token = null;
                        }
                    } else {
                        PushManager.this.register(context, pushType);
                    }

                }

                public void onError(PushErrorCode code) {
                    RLog.e(PushManager.TAG, "error when config push. Will reConfig when network changed!");
                }

                public void onFail(PushType pushType, PushErrorCode code) {
                    RLog.e(PushManager.TAG, "Failed to config push. type:" + pushType + "; errorCode:" + code);
                    if (code.equals(PushErrorCode.NOT_REGISTER_IN_ADMIN)) {
                        RLog.e(PushManager.TAG, "Please fill in the parameters of " + pushType.getName() + " in your RongCloud Admin.");
                    }

                    PushManager.this.onErrorResponse(context, pushType, "queryType", (long)code.getCode());
                }
            });
        }
    }

    public void register(Context context, PushType pushType) {
        RLog.d(TAG, "register. type:" + pushType);
        if (!pushType.equals(PushType.RONG)) {
            try {
                RLog.d(TAG, "stop PushReceiver.");
                ComponentName receiver = new ComponentName(context, PushReceiver.class);
                context.getPackageManager().setComponentEnabledSetting(receiver, 2, 1);
            } catch (Exception var4) {
            }
        }

        IPush iPush = (IPush)this.registeredPushMap.get(pushType.getName());
        if (iPush == null) {
            iPush = PushFactory.getPushCenterByType(pushType);
        }

        if (iPush != null) {
            iPush.register(context, this.pushConfig);
            this.registeredPushMap.put(pushType.getName(), iPush);
        }

    }

    public void onNetworkReconfigEvent(Context context) {
        if (PushCacheHelper.getInstance().isConfigDone(context)) {
            RLog.d(TAG, "Config finished. Ignore this event. ");
        } else {
            if (this.pushConfigManager != null) {
                if (!TextUtils.isEmpty(this.token)) {
                    this.pushConfigManager.setToken(context, this.pushConfig, this.token);
                } else {
                    this.pushConfigManager.reConfig(context);
                }
            } else {
                this.initPushConfig(context);
                this.pushConfigManager.startConfig(context);
            }

        }
    }

    public void onReceiveToken(Context context, PushType pushType, String token) {
        RLog.d(TAG, "onReceiveToken. token:" + token);
        String cachedToken = PushCacheHelper.getInstance().getCachedTokenInfo(context);
        String currentToken = pushType.getName() + "|" + token;
        if (!cachedToken.equals(currentToken)) {
            PushCacheHelper.getInstance().clearPushConfigInfo(context);
            if (this.pushConfigManager == null) {
                this.initPushConfig(context);
                this.pushConfigManager.startConfig(context);
            } else {
                this.pushConfigManager.setToken(context, this.pushConfig, currentToken);
            }

            this.token = currentToken;
        } else {
            RLog.d(TAG, "token is same with cached, do nothing!");
        }

    }

    public void onTokenRefresh(Context context, PushType pushType) {
        PushType cachedPushType = PushCacheHelper.getInstance().getConfigPushType(context);
        if (cachedPushType.equals(pushType)) {
            this.register(context, pushType);
        }

    }

    public void onErrorResponse(Context context, PushType pushType, String action, long resultCode) {
        RLog.d(TAG, "onErrorResponse. pushType:" + pushType + "; errorCode:" + resultCode);
        if (resultCode == (long)PushErrorCode.NOT_SUPPORT_BY_OFFICIAL_PUSH.getCode()) {
            this.register(context, PushType.RONG);
            if (this.pushConfigManager != null) {
                this.pushConfigManager.finishConfig(context, PushType.RONG.getName());
            }
        } else {
            Intent intent = new Intent();
            intent.setAction("io.rong.push.intent.THIRD_PARTY_PUSH_STATE");
            intent.putExtra("pushType", pushType.getName());
            intent.putExtra("action", action);
            intent.putExtra("resultCode", resultCode);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        }

    }

    public void onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        RLog.d(TAG, "onNotificationMessageArrived is called. " + pushNotificationMessage.toString());
        Intent intent = new Intent();
        intent.setAction("io.rong.push.intent.MESSAGE_ARRIVED");
        intent.putExtra("pushType", pushType.getName());
        intent.putExtra("message", pushNotificationMessage);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public void onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        RLog.d(TAG, "onNotificationMessageClicked is called. " + pushNotificationMessage.toString());
        Intent intent = new Intent();
        intent.setAction("io.rong.push.intent.MESSAGE_CLICKED");
        intent.putExtra("pushType", pushType.getName());
        intent.putExtra("message", pushNotificationMessage);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    public void onPushRawData(Context context, PushType pushType, String data) {
        PushNotificationMessage notificationMessage = PushUtils.transformToPushMessage(data);
        if (notificationMessage == null) {
            RLog.e(TAG, "notification message is null. Ignore this event.");
        } else {
            Intent intent = new Intent();
            intent.setAction("io.rong.push.intent.MESSAGE_ARRIVED");
            intent.putExtra("pushType", pushType.getName());
            intent.putExtra("message", notificationMessage);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        }
    }

    public void updatePushServerInfoFromToken(String token) {
        if (this.context == null) {
            RLog.e(TAG, "updatePushServerInfoFromToken return:context is null, may not init ");
        } else {
            if (!TextUtils.isEmpty(token) && token.contains("@")) {
                String[] split = token.split("@");
                if (split.length == 2) {
                    String serverInfoInToken = split[1];
                    PushCacheHelper.getInstance().setPushServerInIMToken(this.context, serverInfoInToken);
                }
            }

        }
    }

    public PushConfig getPushConfig() {
        return this.pushConfig;
    }

    public IPush getRegisteredPush(String key) {
        return this.registeredPushMap == null ? null : (IPush)this.registeredPushMap.get(key);
    }

    public PushType getServerPushType() {
        return this.serverPushType;
    }

    private static class SingletonHolder {
        private static PushManager sIns = new PushManager();

        private SingletonHolder() {
        }
    }
}
