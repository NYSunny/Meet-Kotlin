//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.rong.common.RLog;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.imlib.CMPStrategy;
import io.rong.imlib.RongIMClient.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigationCacheHelper {
    private static final String TAG = "NavigationCacheHelper";
    private static final String NAVIGATION_PREFERENCE = "RongNavigation";
    private static final String NAVIGATION_IP_PREFERENCE = "RongNavigationIp";
    private static final long TIME_OUT = 7200000L;
    private static final String DEFAULT_VIDEO_TIME = "300";
    private static final int DEFAULT_GIF_SIZE = 2048;
    private static final int USERID_MAX_LENGTH = 64;
    private static final String CODE = "code";
    private static final String CMP_SERVER = "server";
    private static final String BACKUP_SERVER = "bs";
    private static final String MEDIA_SERVER = "uploadServer";
    private static final String LOCATION_CONFIG = "location";
    private static final String CACHED_TIME = "cached_time";
    private static final String APP_KEY = "appKey";
    private static final String TOKEN = "token";
    private static final String VOIP_CALL_INFO = "voipCallInfo";
    private static final String LOG_MONITOR = "monitor";
    private static final String OPEN_MP = "openMp";
    private static final String OPEN_US = "openUS";
    private static final String GET_REMOTE_SERVICE = "historyMsg";
    private static final String GET_GROUP_MESSAGE_LIMIT = "grpMsgLimit";
    private static final String GET_CHATROOM_HISTORY_SERVICE = "chatroomMsg";
    private static final String JOIN_MULTI_CHATROOM = "joinMChrm";
    private static final String USER_ID = "userId";
    private static final String TYPE = "type";
    private static final String LAST_SUCCESS_NAVI = "lastSuccessNavi";
    private static final String COMPLEX_CONNECTION = "complexConnection";
    private static final String CONN_POLICY = "connPolicy";
    private static final String VIDEO_TIMES = "videoTimes";
    private static final String GIF_SIZE = "gifSize";
    private static long sCacheTime = 0L;
    private static boolean userPolicy = false;
    private static final String OFFLINE_LOG_SERVER = "offlinelogserver";
    private static final String ONLINE_LOG_SERVER = "onlinelogserver";
    private static final String EXT_KIT_SWITCH = "extkitSwitch";
    private static final String OPEN_DNS = "openHttpDNS";
    private static final String CLIENT_IP = "clientIp";
    private static final String KV_STORAGE = "kvStorage";
    private static final String TIMING_UPLOAD_LOG_CONFIG = "logPolicy";
    private static final String TIMING_UPLOAD_LOG_SWITCH = "logSwitch";
    private static final String BAI_DU_MEDIA_SERVER = "bosAddr";
    private static Object lock = new Object();

    public NavigationCacheHelper() {
    }

    public static boolean isCacheValid(@NonNull Context context, String appKey, String token, String naviUrls) {
        if (getConnectionCmpList(context).size() == 0) {
            return false;
        } else {
            SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
            String cachedKey = sharedPreferences.getString("appKey", (String)null);
            String cachedToken = sharedPreferences.getString("token", (String)null);
            String userId = sharedPreferences.getString("userId", (String)null);
            String cacheNaviDomainList = getLastSuccessNaviDomainList(context);
            sCacheTime = sharedPreferences.getLong("cached_time", 0L);
            long currentTime = System.currentTimeMillis() - (long)TimeZone.getDefault().getRawOffset();
            boolean isValid = cachedKey != null && cachedKey.equals(appKey) && cachedToken != null && cachedToken.equals(token) && currentTime - sCacheTime <= 7200000L && !TextUtils.isEmpty(userId) && isNaviUrlsValid(cacheNaviDomainList, naviUrls);
            FwLog.write(3, 1, LogTag.L_GET_NAVI_S.getTag(), "cache_valid|delta_time", new Object[]{isValid, currentTime - sCacheTime});
            return isValid;
        }
    }

    private static boolean isNaviUrlsValid(String cachedNaviDomain, String naviUrls) {
        return !TextUtils.isEmpty(naviUrls) && !TextUtils.isEmpty(cachedNaviDomain) ? naviUrls.equals(cachedNaviDomain) : false;
    }

    static boolean isUSOpened(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("openUS", false);
    }

    static boolean isMPOpened(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("openMp", true);
    }

    static boolean isCacheTimeout(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        long cachedTime = sharedPreferences.getLong("cached_time", 0L);
        long currentTime = System.currentTimeMillis() - (long)TimeZone.getDefault().getRawOffset();
        return cachedTime != 0L && currentTime - cachedTime > 7200000L;
    }

    public static long getCachedTime() {
        return sCacheTime;
    }

    public static void clearCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        sharedPreferences.edit().clear().commit();
    }

    public static void clearNaviCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigationIp", 0);
        sharedPreferences.edit().clear().commit();
    }

    public static List<String> getConnectionCmpList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        String cmp = sharedPreferences.getString("server", (String)null);
        String backupStr = sharedPreferences.getString("bs", (String)null);
        List<String> cmpList = new ArrayList();
        if (!TextUtils.isEmpty(cmp)) {
            cmpList.add(cmp);
        }

        if (!TextUtils.isEmpty(backupStr)) {
            String[] backups = backupStr.split(",");
            String[] var6 = backups;
            int var7 = backups.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String backup = var6[var8];
                if (!TextUtils.isEmpty(backup)) {
                    cmpList.add(backup);
                }
            }
        }

        return cmpList;
    }

    static void clearComplexConnectionEntries(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        sharedPreferences.edit().remove("complexConnection").commit();
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("token", "");
    }

    public static void updateTime(Context context, long time) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        Editor editor = sharedPreferences.edit();
        editor.putLong("cached_time", time);
        editor.commit();
    }

    static void cacheLastSuccessNaviDomainList(Context context, String naviList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigationIp", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("lastSuccessNavi", naviList);
        editor.commit();
    }

    private static String getLastSuccessNaviDomainList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigationIp", 0);
        return sharedPreferences.getString("lastSuccessNavi", (String)null);
    }

    static String getVoIPCallInfo(Context context) {
        synchronized(lock) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
            return sharedPreferences.getString("voipCallInfo", (String)null);
        }
    }

    public static void updateVoIPCallInfo(Context context, String rtcProfile) {
        synchronized(lock) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
            Editor editor = sharedPreferences.edit();
            editor.putString("voipCallInfo", rtcProfile);
            editor.commit();
        }
    }

    static String getMediaServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("uploadServer", (String)null);
    }

    static String getBaiDuMediaServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("bosAddr", "gz.bcebos.com");
    }

    static boolean isGetRemoteEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("historyMsg", false);
    }

    public static int getGroupMessageLimit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getInt("grpMsgLimit", 1000);
    }

    /** @deprecated */
    @Deprecated
    public static String getOfflineLogServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("offlinelogserver", "https://feedback.cn.ronghub.com");
    }

    /** @deprecated */
    @Deprecated
    public static String getOnlineLogServer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("onlinelogserver", (String)null);
    }

    static boolean isChatroomHistoryEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("chatroomMsg", false);
    }

    public static boolean isJoinMChatroomEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("joinMChrm", false);
    }

    public static boolean isPhraseEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("extkitSwitch", false);
    }

    public static boolean isDnsEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("openHttpDNS", false);
    }

    public static LocationConfig getLocationConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        String value = sharedPreferences.getString("location", (String)null);
        if (!TextUtils.isEmpty(value)) {
            try {
                LocationConfig config = new LocationConfig();
                JSONObject jsonObj = new JSONObject(value);
                config.setConfigure(jsonObj.optBoolean("configure"));
                if (jsonObj.has("conversationTypes")) {
                    JSONArray array = jsonObj.optJSONArray("conversationTypes");
                    int[] types = new int[array.length()];

                    for(int j = 0; j < array.length(); ++j) {
                        types[j] = array.optInt(j);
                    }

                    config.setConversationTypes(types);
                }

                config.setMaxParticipant(jsonObj.optInt("maxParticipant"));
                config.setDistanceFilter(jsonObj.optInt("distanceFilter"));
                config.setRefreshInterval(jsonObj.optInt("refreshInterval"));
                return config;
            } catch (JSONException var8) {
                RLog.e("NavigationCacheHelper", "getLocationConfig ", var8);
            }
        }

        return null;
    }

    public static int getLogMonitor(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getInt("monitor", 0);
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("userId", (String)null);
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("userId", userId);
        editor.apply();
    }

    public static void clearUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        sharedPreferences.edit().remove("userId").commit();
    }

    public static boolean isKvStorageEnabled(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("kvStorage", false);
    }

    public static String getRealTimeLogConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("logPolicy", (String)null);
    }

    static void cacheRequest(Context context, String appKey, String token) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        Editor editor = sharedPreferences.edit();
        long gmtTimestamp = System.currentTimeMillis() - (long)TimeZone.getDefault().getRawOffset();
        editor.putLong("cached_time", gmtTimestamp);
        editor.putString("appKey", appKey);
        editor.putString("token", token);
        editor.commit();
    }

    static int decode2File(Context context, String data, int httpCode) {
        if (TextUtils.isEmpty(data)) {
            RLog.e("NavigationCacheHelper", "[connect] decode2File: navi data is empty.");
            return ErrorCode.RC_NODE_NOT_FOUND.getValue();
        } else {
            try {
                JSONObject jsonObj = new JSONObject(data);
                if (!jsonObj.has("code")) {
                    return ErrorCode.RC_NODE_NOT_FOUND.getValue();
                } else {
                    int naviCode = jsonObj.optInt("code");
                    if (naviCode == 200) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
                        Editor editor = sharedPreferences.edit();
                        String userId = jsonObj.optString("userId");
                        if (!TextUtils.isEmpty(userId) && userId.getBytes().length <= 64) {
                            editor.putString("userId", userId);
                            String server = jsonObj.optString("server");
                            String bsServer = jsonObj.optString("bs");
                            if (TextUtils.isEmpty(server) && TextUtils.isEmpty(bsServer)) {
                                RLog.e("NavigationCacheHelper", "[connect] decode2File: cmp is invalid, " + data);
                                return ErrorCode.RC_NODE_NOT_FOUND.getValue();
                            } else {
                                editor.putString("server", server);
                                editor.putString("bs", bsServer);
                                editor.putString("uploadServer", jsonObj.optString("uploadServer"));
                                editor.putString("location", jsonObj.optString("location"));
                                editor.putString("voipCallInfo", jsonObj.optString("voipCallInfo"));
                                editor.putBoolean("historyMsg", jsonObj.optBoolean("historyMsg"));
                                editor.putBoolean("chatroomMsg", jsonObj.optBoolean("chatroomMsg"));
                                if (jsonObj.has("grpMsgLimit")) {
                                    editor.putInt("grpMsgLimit", jsonObj.optInt("grpMsgLimit"));
                                }

                                editor.putBoolean("joinMChrm", jsonObj.optBoolean("joinMChrm"));
                                editor.putBoolean("openMp", jsonObj.optInt("openMp") == 1);
                                editor.putBoolean("openUS", jsonObj.optInt("openUS") == 1);
                                if (jsonObj.has("monitor")) {
                                    editor.putInt("monitor", jsonObj.optInt("monitor"));
                                }

                                editor.putBoolean("type", jsonObj.optInt("type") == 1);
                                if (jsonObj.has("connPolicy")) {
                                    editor.putInt("connPolicy", jsonObj.optInt("connPolicy"));
                                }

                                if (jsonObj.has("videoTimes")) {
                                    editor.putInt("videoTimes", jsonObj.optInt("videoTimes"));
                                }

                                editor.putString("offlinelogserver", jsonObj.optString("offlinelogserver"));
                                editor.putString("onlinelogserver", jsonObj.optString("onlinelogserver"));
                                editor.putBoolean("extkitSwitch", jsonObj.optInt("extkitSwitch") == 1);
                                editor.putBoolean("kvStorage", jsonObj.optInt("kvStorage") == 1);
                                if (jsonObj.has("gifSize")) {
                                    editor.putInt("gifSize", jsonObj.optInt("gifSize"));
                                }

                                editor.putBoolean("openHttpDNS", jsonObj.optInt("openHttpDNS") == 1);
                                if (jsonObj.has("bosAddr")) {
                                    editor.putString("bosAddr", jsonObj.optString("bosAddr"));
                                }

                                int logSwitch = jsonObj.optInt("logSwitch");
                                if (logSwitch == 1) {
                                    String config = jsonObj.optString("logPolicy");
                                    if (!TextUtils.isEmpty(config)) {
                                        editor.putString("logPolicy", config.replaceAll("&quot;", "\""));
                                    } else {
                                        editor.putString("logPolicy", "");
                                    }
                                } else {
                                    editor.putString("logPolicy", "");
                                }

                                editor.commit();
                                return 0;
                            }
                        } else {
                            RLog.e("NavigationCacheHelper", "[connect] decode2File: no user_id." + data);
                            return ErrorCode.RC_NODE_NOT_FOUND.getValue();
                        }
                    } else {
                        RLog.e("NavigationCacheHelper", "[connect] decode2File: code & httpCode " + naviCode + "-" + httpCode);
                        return (naviCode != 401 || httpCode != 403) && (naviCode != 403 || httpCode != 401) ? ErrorCode.RC_NAVI_RESOURCE_ERROR.getValue() : ErrorCode.RC_CONN_USER_OR_PASSWD_ERROR.getValue();
                    }
                }
            } catch (JSONException var12) {
                return ErrorCode.RC_NODE_NOT_FOUND.getValue();
            }
        }
    }

    public static int getVideoLimitTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getInt("videoTimes", Integer.valueOf("300"));
    }

    public static boolean isCMPValid(String cmp) {
        return CMPStrategy.getInstance().isCMPValid(cmp);
    }

    public static boolean getPrivateCloudConfig(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getBoolean("type", false);
    }

    public static boolean isConnPolicyEnable(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        boolean naviEnabled = sharedPreferences.getInt("connPolicy", 0) == 1;
        RLog.i("NavigationCacheHelper", "isConnPolicyEnable, userPolicy = " + userPolicy + ", naviPolicy = " + naviEnabled);
        return naviEnabled ? true : userPolicy;
    }

    public static void setUserPolicy(boolean enable) {
        RLog.i("NavigationCacheHelper", "setUserPolicy, userPolicy = " + enable);
        userPolicy = enable;
    }

    private static String decode(String data, String key1, String key2) {
        if (data == null) {
            return null;
        } else {
            int start = data.indexOf(key1) + key1.length();
            int end = data.indexOf(key2);
            return start < end && end != 0 ? data.substring(start, end) : null;
        }
    }

    static String queryRequestIP(String urlStr) {
        DNSResolve dnsTh = new DNSResolve(urlStr);
        dnsTh.resolveHost();
        return dnsTh.getIP();
    }

    public static int getGifSizeLimit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getInt("gifSize", 2048);
    }

    static void updateClientIp(Context context, String ip) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        Editor editor = sharedPreferences.edit();
        editor.putString("clientIp", ip);
        editor.commit();
    }

    public static String getClientIp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RongNavigation", 0);
        return sharedPreferences.getString("clientIp", "");
    }
}
