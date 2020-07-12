//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Vector;

/** @deprecated */
@Deprecated
public class DLog {
    public static final int NONE = 0;
    public static final int F = 1;
    public static final int E = 2;
    public static final int W = 3;
    public static final int I = 4;
    public static final int D = 5;
    public static final int V = 6;
    public static final int APP = 1;
    public static final int PTC = 2;
    public static final int ENV = 4;
    public static final int DET = 8;
    public static final int CON = 16;
    public static final int RCO = 32;
    public static final int CRM = 64;
    public static final int MSG = 128;
    public static final int MED = 256;
    public static final int LOG = 512;
    public static final int DEB = 1024;
    public static final int CRS = 2048;
    public static final int RTC = 4096;
    public static final int EPT = 8192;
    public static final int EXCEPTION_THROWN = -1000;
    private static Vector<LogEntry> lruLogCache = new Vector();
    static final String TAG = "FwLog";
    private static ILogEventCallback logEventCallback;
    private static DLogImp instance;
    private static ILogListener logListener;

    protected DLog() {
    }

    public static void init(Context context, String appKey, String sdkVer) {
        instance = new DLogImp(context, appKey, sdkVer);
        Class var3 = DLog.class;
        synchronized(DLog.class) {
            LogEntity.getInstance().setLogListener(logListener);
        }
    }

    public static void setCallbackInSubProcess(Context context, ILogEventCallback callback) {
        logEventCallback = callback;
    }

    public static void setLogMonitor(int value) {
        if (instance != null) {
            Log.d("FwLog", "setLogMonitor = " + value);
            if (value == 1) {
                value = 1610611711;
            } else if (value == 2) {
                value = 1879048191;
            }

            LogEntity.getInstance().setMonitorLevel(value >> 28);
            LogEntity.getInstance().setMonitorType(value & 268435455);
        } else if (logEventCallback != null) {
            logEventCallback.onSetLogMonitorEventFromSubProcess(value);
        }

    }

    public static void setToken(String token) {
        if (token != null) {
            if (instance != null) {
                LogEntity.getInstance().setToken(token);
            } else if (logEventCallback != null) {
                logEventCallback.onSetTokenEventFromSubProcess(token);
            }

        }
    }

    public static void setUserId(String userId) {
        if (userId != null) {
            if (instance != null) {
                LogEntity.getInstance().setUserId(userId);
            } else if (logEventCallback != null) {
                logEventCallback.onSetUserIdEventFromSubProcess(userId);
            }

        }
    }

    public static void setConsoleLogLevel(int level) {
        if (instance != null) {
            LogEntity.getInstance().setConsoleLogLevel(level);
        } else {
            throw new RuntimeException("Cannot be set in sub process.");
        }
    }

    public static synchronized void setLogListener(ILogListener listener) {
        if (instance != null) {
            LogEntity.getInstance().setLogListener(listener);
        } else {
            logListener = listener;
        }

    }

    public static void onProtocolLog(String log, boolean isHighLevel) {
        String[] values = log.split(";;;");
        if (values.length > 0) {
            String tag = values[0];
            if (tag.equals(LogTag.P_CODE_C.getTag()) && values.length == 3) {
                write(isHighLevel ? 4 : 5, 2, tag, "action|code", values[1], values[2]);
            } else if (tag.equals(LogTag.P_REASON_C.getTag()) && values.length == 3) {
                write(isHighLevel ? 4 : 5, 2, tag, "action|reason", values[1], values[2]);
            } else if (tag.equals(LogTag.P_MORE_C.getTag()) && values.length == 4) {
                write(isHighLevel ? 4 : 5, 2, tag, "action|code|reason", values[1], values[2], values[3]);
            } else {
                write(1, 2, LogTag.P_PARSE_ERROR_S.getTag(), "content", log);
            }
        } else {
            write(1, 2, LogTag.P_PARSE_ERROR_S.getTag(), "content", log);
        }

    }

    public static int getProtocolLogStatus() {
        int logStatus = 0;
        if (instance != null) {
            if (LogEntity.getInstance().getLogMode() == 0 && LogEntity.getInstance().getMonitorLevel() != 6) {
                if (LogEntity.getInstance().getMonitorLevel() >= 1 && LogEntity.getInstance().getMonitorLevel() <= 5) {
                    logStatus = 1;
                }
            } else {
                logStatus = 2;
            }
        } else if (logEventCallback != null) {
            logStatus = logEventCallback.onGetProtocolLogStatus();
        }

        return logStatus;
    }

    public static void upload(ILogUploadCallback callback) {
        if (instance != null) {
            instance.uploadLog(callback);
        } else {
            Log.e("FwLog", "call upload() failed. instance == null");
        }

    }

    public static void write(int level, int type, String tag, String keys, Object... values) {
        String metaJson = formatMetaJson(false, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), keys, values);
        write(System.currentTimeMillis(), level, type, tag, metaJson);
    }

    public static void write(long timestamp, int level, int type, String tag, String metaJson) {
        if (instance == null && logEventCallback == null) {
            cacheLog(timestamp, level, type, tag, metaJson);
        }

        if (instance != null) {
            writeLogFromCache();
            instance.writeLog(timestamp, level, type, tag, metaJson);
        } else if (logEventCallback != null) {
            writeIPCLogFromCache();
            logEventCallback.onLogEventFromSubProcess(timestamp, level, type, tag, metaJson);
        }

    }

    private static void writeIPCLogFromCache() {
        if (lruLogCache.size() > 0) {
            Iterator var0 = lruLogCache.iterator();

            while(var0.hasNext()) {
                LogEntry logEntry = (LogEntry)var0.next();
                logEventCallback.onLogEventFromSubProcess(logEntry.getTimestamp(), logEntry.getLevel(), logEntry.getType(), logEntry.getTag(), logEntry.getMetaJson());
            }

            lruLogCache.clear();
        }

    }

    private static void writeLogFromCache() {
        if (lruLogCache.size() > 0) {
            Iterator var0 = lruLogCache.iterator();

            while(var0.hasNext()) {
                LogEntry logEntry = (LogEntry)var0.next();
                instance.writeLog(logEntry.getTimestamp(), logEntry.getLevel(), logEntry.getType(), logEntry.getTag(), logEntry.getMetaJson());
            }

            lruLogCache.clear();
        }

    }

    private static void cacheLog(long timestamp, int level, int type, String tag, String metaJson) {
        LogEntry logEntry = new LogEntry();
        logEntry.setTimestamp(timestamp);
        logEntry.setLevel(level);
        logEntry.setType(type);
        logEntry.setTag(tag);
        logEntry.setMetaJson(metaJson);
        lruLogCache.add(logEntry);
    }

    public static String stackToString(Throwable error) {
        return Log.getStackTraceString(error).replaceAll("\n", "\\\\n");
    }

    protected static String formatMetaJson(boolean isRtLog, int pid, long tid, long mainTid, String keys, Object... values) {
        String[] keyArray = keys != null ? keys.split("\\|") : new String[0];
        String jsonStr = "";
        if (keyArray.length != values.length) {
            StringBuilder valueStr = new StringBuilder();
            Object[] var11 = values;
            int var12 = values.length;

            for(int var13 = 0; var13 < var12; ++var13) {
                Object obj = var11[var13];
                valueStr.append(obj != null ? obj.toString() : "null");
                valueStr.append("|");
            }

            if (values.length > 0) {
                valueStr.deleteCharAt(valueStr.length() - 1);
                jsonStr = "{\"ptid\":\"" + pid + "-" + tid + (tid == mainTid ? "*" : "") + "\",\"" + keys + "\":\"" + valueStr + "\"}";
            }
        } else {
            try {
                JSONObject json = new JSONObject();

                for(int i = 0; i < keyArray.length; ++i) {
                    json.put(keyArray[i], values[i] != null ? values[i] : "null");
                }

                String metaStr = json.toString().replace("\\/", "/");
                if (isRtLog) {
                    jsonStr = "{" + metaStr.substring(1);
                } else {
                    jsonStr = "{\"ptid\":\"" + pid + "-" + tid + (tid == mainTid ? "*" : "") + "\"" + (keyArray.length > 0 ? "," : "") + metaStr.substring(1);
                }
            } catch (JSONException var15) {
                var15.printStackTrace();
            }
        }

        return jsonStr;
    }

    public static void setLogServer(String onlineLogServer, String offlineLogServer) {
        if (instance != null) {
            if (offlineLogServer != null) {
                LogEntity.getInstance().setOfflineLogServer(offlineLogServer);
            }

            if (onlineLogServer != null) {
                LogEntity.getInstance().setOnlineLogServer(onlineLogServer);
            }
        } else if (logEventCallback != null) {
            logEventCallback.onSetLogServer(onlineLogServer, offlineLogServer);
        }

    }

    public static void rtWrite(int level, int type, String tag, String keys, Object... values) {
        String metaJson = formatMetaJson(true, Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), keys, values);
        rtWrite(System.currentTimeMillis(), level, type, tag, metaJson);
    }

    public static void rtWrite(long timestamp, int level, int type, String tag, String metaJson) {
        if (instance != null) {
            if (TextUtils.isEmpty(LogEntity.getInstance().getOnlineLogServer())) {
                return;
            }

            instance.writeRtLog(timestamp, level, type, tag, metaJson);
        } else if (logEventCallback != null) {
            logEventCallback.onRtLogEventFromSubProcess(timestamp, level, type, tag, metaJson);
        }

    }

    static class LogEntry {
        long timestamp;
        int level;
        int type;
        String tag;
        String metaJson;

        LogEntry() {
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public int getLevel() {
            return this.level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getType() {
            return this.type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTag() {
            return this.tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getMetaJson() {
            return this.metaJson;
        }

        public void setMetaJson(String metaJson) {
            this.metaJson = metaJson;
        }
    }

    public interface ILogUploadCallback {
        void onLogUploaded(int var1);
    }

    public interface ILogListener {
        void onLogEvent(String var1);
    }

    public interface ILogEventCallback {
        void onLogEventFromSubProcess(long var1, int var3, int var4, String var5, String var6);

        void onRtLogEventFromSubProcess(long var1, int var3, int var4, String var5, String var6);

        void onSetLogMonitorEventFromSubProcess(int var1);

        void onSetTokenEventFromSubProcess(String var1);

        void onSetUserIdEventFromSubProcess(String var1);

        void onSetLogServer(String var1, String var2);

        int onGetProtocolLogStatus();
    }

    public static enum LogTag {
        A_INIT_O("A-init-O"),
        L_INIT_O("L-init-O"),
        A_APP_VER_S("A-app_ver-S"),
        A_SET_SERVER_O("A-set_server-O"),
        A_SET_STATISTIC_SERVER_O("A-set_statistic_server-O"),
        BIND_SERVICE_S("2-bind_service-S"),
        A_CONNECT_T("A-connect-T"),
        A_CONNECT_R("A-connect-R"),
        A_CONNECT_S("A-connect-S"),
        A_DISCONNECT_O("A-disconnect-O"),
        L_CONNECT_T("L-connect-T"),
        L_CONNECT_R("L-connect-R"),
        L_CONNECT_S("L-connect-S"),
        L_APP_STATE_S("L-app_state-S"),
        A_DELETE_MESSAGES_S("A-delete_messages-S"),
        L_DELETE_MESSAGES_S("L-delete_messages-S"),
        L_DECODE_MSG_E("L-decode_msg-E"),
        L_REGTYPE_E("L-regtype-E"),
        A_RECONNECT_T("A-reconnect-T"),
        A_RECONNECT_R("A-reconnect-R"),
        A_RECONNECT_S("A-reconnect-S"),
        L_RECONNECT_T("L-reconnect-T"),
        L_RECONNECT_R("L-reconnect-R"),
        L_RECONNECT_S("L-reconnect-S"),
        L_GET_NAVI_T("L-get_navi-T"),
        L_GET_NAVI_R("L-get_navi-R"),
        L_GET_NAVI_S("L-get_navi-S"),
        L_ENV_S("L-Env-S"),
        L_DECODE_NAVI_S("L-decode_navi-S"),
        P_CONNECT_T("P-connect-T"),
        P_CONNECT_R("P-connect-R"),
        P_CONNECT_S("P-connect-S"),
        P_CONNECT_ENTRY_S("P-connect_entry-S"),
        P_SEND_MSG_S("P-send_msg-S"),
        P_DELETE_MSG_S("P-delete_msg-S"),
        P_PARSE_ERROR_S("P-parse_error-S"),
        P_RTCON_E("P-rtcon-E"),
        P_RTMSG_E("P-rtmsg-E"),
        P_RTTCP_E("P-rttcp-E"),
        P_CODE_C("P-code-C"),
        P_REASON_C("P-reason-C"),
        P_MORE_C("P-more-C"),
        L_NETWORK_CHANGED_S("L-network_changed-S"),
        L_PING_S("L-ping-S"),
        A_JOIN_CHATROOM_T("A-join_chatroom-T"),
        A_JOIN_CHATROOM_R("A-join_chatroom-R"),
        L_JOIN_CHATROOM_T("L-join_chatroom-T"),
        L_JOIN_CHATROOM_R("L-join_chatroom-R"),
        A_QUIT_CHATROOM_T("A-quit_chatroom-T"),
        A_QUIT_CHATROOM_R("A-quit_chatroom-R"),
        L_QUIT_CHATROOM_T("L-quit_chatroom-T"),
        L_QUIT_CHATROOM_R("L-quit_chatroom-R"),
        A_REJOIN_CHATROOM_T("A-rejoin_chatroom-T"),
        A_REJOIN_CHATROOM_R("A-rejoin_chatroom-R"),
        L_REJOIN_CHATROOM_T("L-rejoin_chatroom-T"),
        L_REJOIN_CHATROOM_R("L-rejoin_chatroom-R"),
        L_MEDIA_S("L-media-S"),
        L_CRASH_MAIN_TRB_F("L-crash_main_trb-F"),
        L_CRASH_MAIN_EPT_F("L-crash_main_ept-F"),
        L_CRASH_MAIN_EPT_E("L-crash_main_ept-E"),
        L_CRASH_IPC_TRB_F("L-crash_ipc_trb-F"),
        L_CRASH_IPC_EPT_F("L-crash_ipc_ept-F"),
        L_CRASH_IPC_RTM_F("L-crash_ipc_rtm-F"),
        L_CRASH_IPC_RMT_E("L-crash_ipc_rmt-E"),
        G_CRASH_E("G-crash-E"),
        G_GET_UPLOAD_CACHE_E("G-get_upload_cache-E"),
        G_UPLOAD_LOG_S("G-upload_log-S"),
        G_UPLOAD_LOG_E("G-upload_log-E"),
        G_UPLOAD_LOG_F("G-upload_log-F"),
        G_DROP_LOG_E("G-drop_log-E"),
        L_DOH_R("L-DoH-R"),
        L_DOH_T("L-DoH-T");

        private String tag;

        private LogTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }
}
