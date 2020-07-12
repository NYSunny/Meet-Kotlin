//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.fwlog;

import android.content.Context;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import io.rong.common.rlog.RLog;
import io.rong.imlib.statistics.CrashDetails;

public class FwLog {
    private static final String TAG = FwLog.class.getSimpleName();
    public static final int EXCEPTION_THROWN = -1000;
    public static final int F = 0;
    public static final int E = 1;
    public static final int W = 2;
    public static final int I = 3;
    public static final int D = 4;
    public static final int IM = 1;
    public static final int RTC = 2;
    private static SparseArray<String> typeArray = new SparseArray();
    private static final String LOG_PROCESS_THREAD_NAME = "thread_log_process";
    private static Executor logProcessExecutor;
    private static IFwLogWriter directWriter;
    private static IFwLogWriter proxyWriter;
    private static int consolePrintLevel;
    private static ILogListener logListener;
    private static IFwLogConsolePrinter logConsolePrinter;

    public FwLog() {
    }

    public static void write(final int level, final int type, final String tag, final String keys, final Object... values) {
        logProcessExecutor.execute(new Runnable() {
            public void run() {
                String metaJson = FwLog.formatMetaJson(Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), keys, values);
                String typeStr = (String)FwLog.typeArray.get(type);
                if (typeStr == null) {
                    typeStr = (String)FwLog.typeArray.get(1);
                }

                if (FwLog.logConsolePrinter != null && level <= FwLog.consolePrintLevel) {
                    FwLog.logConsolePrinter.printLog(System.currentTimeMillis(), level, typeStr, tag, metaJson);
                }

                FwLog.write(level, typeStr, tag, metaJson, System.currentTimeMillis());
            }
        });
    }

    public static void write(final int level, final String type, final String tag, final String metaJson, final long timestamp) {
        if (!"thread_log_process".equals(Thread.currentThread().getName())) {
            logProcessExecutor.execute(new Runnable() {
                public void run() {
                    FwLog.write(level, type, tag, metaJson, timestamp);
                }
            });
        } else {
            if (proxyWriter != null) {
                proxyWriter.write(level, type, tag, metaJson, timestamp);
            } else {
                if (logListener != null) {
                    logListener.onLogEvent("[RC:" + tag + "]" + metaJson);
                }

                if (directWriter != null) {
                    directWriter.write(level, type, tag, metaJson, timestamp);
                }
            }

        }
    }

    private static String formatMetaJson(int pid, long tid, long mainTid, String keys, Object... values) {
        String jsonStr = "";
        String[] keyArray = keys != null ? keys.split("\\|") : new String[0];
        if (keyArray.length != values.length) {
            StringBuilder valueStr = new StringBuilder();
            Object[] var10 = values;
            int var11 = values.length;

            for(int var12 = 0; var12 < var11; ++var12) {
                Object obj = var10[var12];
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
                jsonStr = "{\"ptid\":\"" + pid + "-" + tid + (tid == mainTid ? "*" : "") + "\"" + (keyArray.length > 0 ? "," : "") + metaStr.substring(1);
            } catch (JSONException var14) {
                RLog.e(TAG, "formatMetaJson", var14);
            }
        }

        return jsonStr;
    }

    public static void onProtocolLog(String log, boolean isHighLevel) {
        String[] values = log.split(";;;");
        if (values.length > 0) {
            String tag = values[0];
            if (tag.equals(LogTag.P_CODE_C.getTag()) && values.length == 3) {
                write(isHighLevel ? 3 : 4, 1, tag, "action|code", values[1], values[2]);
            } else if (tag.equals(LogTag.P_REASON_C.getTag()) && values.length == 3) {
                write(isHighLevel ? 3 : 4, 1, tag, "action|reason", values[1], values[2]);
            } else if (tag.equals(LogTag.P_MORE_C.getTag()) && values.length == 4) {
                write(isHighLevel ? 3 : 4, 1, tag, "action|code|reason", values[1], values[2], values[3]);
            } else {
                RLog.e(TAG, "[RC:" + LogTag.P_PARSE_ERROR_S.getTag() + "] content:" + log);
            }
        } else {
            RLog.e(TAG, "[RC:" + LogTag.P_PARSE_ERROR_S.getTag() + "] content:" + log);
        }

    }

    public static String stackToString(Throwable error) {
        return Log.getStackTraceString(error).replaceAll("\n", "\\\\n");
    }

    public static synchronized void setProxyWriter(IFwLogWriter writer) {
        proxyWriter = writer;
    }

    public static void setDirectWriter(IFwLogWriter writer) {
        directWriter = writer;
    }

    public static void setLogConsolePrinter(IFwLogConsolePrinter printer) {
        logConsolePrinter = printer;
    }

    public static synchronized void setLogListener(ILogListener listener) {
        logListener = listener;
    }

    public static void setConsoleLogLevel(int level) {
        consolePrintLevel = level;
    }

    public static void listenUncaughtException(final Context context) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                final UncaughtExceptionHandler defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        String reason = e.toString();
                        if (!TextUtils.isEmpty(reason) && reason.contains(":")) {
                            reason = reason.substring(0, reason.indexOf(":"));
                        }

                        FwLog.write(0, 1, LogTag.L_CRASH_MAIN_TRB_F.getTag(), "stack|reason|env", FwLog.stackToString(e), reason, CrashDetails.getIMCrashData(context.getApplicationContext(), e.toString()));
                        if (defaultExceptionHandler != null) {
                            defaultExceptionHandler.uncaughtException(t, e);
                        }

                    }
                });
            }
        }, 2000L);
    }

    static {
        typeArray.put(1, "IM");
        typeArray.put(2, "RTC");
        logProcessExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            public Thread newThread(Runnable r) {
                return new Thread(r, "thread_log_process");
            }
        });
        consolePrintLevel = 4;
    }

    public interface ILogListener {
        void onLogEvent(String var1);
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
        A_REJOIN_CHATROOM_S("A-rejoin_chatroom-S"),
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
        L_DOH_T("L-DoH-T"),
        L_DNS_PARSE_F("L-dns_parse-F");

        private String tag;

        private LogTag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return this.tag;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
    }
}
