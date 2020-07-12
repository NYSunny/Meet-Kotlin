//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.net.Uri;
import io.rong.imlib.model.CSLMessageItem;
import io.rong.message.CSHumanEvaluateItem;
import java.util.ArrayList;

public class CustomServiceConfig {
    public boolean isBlack;
    public String msg;
    public String companyName;
    public String companyIcon;
    public boolean robotSessionNoEva;
    public ArrayList<CSHumanEvaluateItem> humanEvaluateList;
    public int userTipTime;
    public String userTipWord;
    public int adminTipTime;
    public String adminTipWord;
    public CustomServiceConfig.CSQuitSuspendType quitSuspendType;
    public CustomServiceConfig.CSEvaType evaluateType;
    public CustomServiceConfig.CSEvaEntryPoint evaEntryPoint;
    public boolean isReportResolveStatus;
    public boolean isDisableLocation;
    public CustomServiceConfig.CSLeaveMessageType leaveMessageConfigType;
    public Uri uri;
    public ArrayList<CSLMessageItem> leaveMessageNativeInfo;
    public String announceMsg;
    public String announceClickUrl;

    public CustomServiceConfig() {
    }

    public static enum CSQuitSuspendType {
        NONE(-1),
        NO_SUSPEND(0),
        SUSPEND(1);

        private int value;

        private CSQuitSuspendType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static CustomServiceConfig.CSQuitSuspendType valueOf(int value) {
            CustomServiceConfig.CSQuitSuspendType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CustomServiceConfig.CSQuitSuspendType status = var1[var3];
                if (value == status.getValue()) {
                    return status;
                }
            }

            return NONE;
        }
    }

    public static enum CSEvaType {
        EVA_SEPARATELY(0),
        EVA_UNIFIED(1);

        private int value;

        private CSEvaType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum CSEvaEntryPoint {
        EVA_LEAVE(0),
        EVA_EXTENSION(1),
        EVA_NONE(2),
        EVA_END(3);

        private int value;

        private CSEvaEntryPoint(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static CustomServiceConfig.CSEvaEntryPoint valueOf(int value) {
            CustomServiceConfig.CSEvaEntryPoint[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CustomServiceConfig.CSEvaEntryPoint status = var1[var3];
                if (value == status.getValue()) {
                    return status;
                }
            }

            return EVA_LEAVE;
        }
    }

    public static enum CSLeaveMessageType {
        NATIVE(0),
        WEB(1);

        private int value;

        private CSLeaveMessageType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum CSEvaSolveStatus {
        UNRESOLVED(0),
        RESOLVED(1),
        RESOLVING(2);

        private int value;

        private CSEvaSolveStatus(int status) {
            this.value = status;
        }

        public int getValue() {
            return this.value;
        }

        public static CustomServiceConfig.CSEvaSolveStatus valueOf(int value) {
            CustomServiceConfig.CSEvaSolveStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CustomServiceConfig.CSEvaSolveStatus status = var1[var3];
                if (value == status.getValue()) {
                    return status;
                }
            }

            return UNRESOLVED;
        }
    }
}
