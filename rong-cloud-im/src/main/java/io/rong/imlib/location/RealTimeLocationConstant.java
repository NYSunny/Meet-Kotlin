//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location;

public class RealTimeLocationConstant {
    public RealTimeLocationConstant() {
    }

    public static enum RealTimeLocationErrorCode {
        RC_REAL_TIME_LOCATION_NOT_INIT(-1, "Not init"),
        RC_REAL_TIME_LOCATION_SUCCESS(0, "Success"),
        RC_REAL_TIME_LOCATION_GPS_DISABLED(1, "GPS disabled"),
        RC_REAL_TIME_LOCATION_CONVERSATION_NOT_SUPPORT(2, "Conversation not support"),
        RC_REAL_TIME_LOCATION_IS_ON_GOING(3, "Real-Time location is on going"),
        RC_REAL_TIME_LOCATION_EXCEED_MAX_PARTICIPANT(4, "Exceed max participants"),
        RC_REAL_TIME_LOCATION_JOIN_FAILURE(5, "Join fail"),
        RC_REAL_TIME_LOCATION_START_FAILURE(6, "Start fail"),
        RC_REAL_TIME_LOCATION_NETWORK_UNAVAILABLE(7, "Network unavailable.");

        int code;
        String msg;

        private RealTimeLocationErrorCode(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMessage() {
            return this.msg;
        }

        public int getValue() {
            return this.code;
        }

        public static RealTimeLocationConstant.RealTimeLocationErrorCode valueOf(int value) {
            RealTimeLocationConstant.RealTimeLocationErrorCode[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RealTimeLocationConstant.RealTimeLocationErrorCode code = var1[var3];
                if (code.getValue() == value) {
                    return code;
                }
            }

            return RC_REAL_TIME_LOCATION_CONVERSATION_NOT_SUPPORT;
        }
    }

    public static enum RealTimeLocationStatus {
        RC_REAL_TIME_LOCATION_STATUS_IDLE(0, "Idle state"),
        RC_REAL_TIME_LOCATION_STATUS_INCOMING(1, "Incoming state"),
        RC_REAL_TIME_LOCATION_STATUS_OUTGOING(2, "Outgoing state"),
        RC_REAL_TIME_LOCATION_STATUS_CONNECTED(3, "Connected state");

        int code;
        String msg;

        private RealTimeLocationStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMessage() {
            return this.msg;
        }

        public int getValue() {
            return this.code;
        }

        public static RealTimeLocationConstant.RealTimeLocationStatus valueOf(int value) {
            RealTimeLocationConstant.RealTimeLocationStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                RealTimeLocationConstant.RealTimeLocationStatus code = var1[var3];
                if (code.getValue() == value) {
                    return code;
                }
            }

            return RC_REAL_TIME_LOCATION_STATUS_IDLE;
        }
    }
}
