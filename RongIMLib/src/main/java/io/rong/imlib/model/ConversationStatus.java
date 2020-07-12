//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.imlib.model.Conversation.ConversationNotificationStatus;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

public class ConversationStatus implements Parcelable {
    private String targetId;
    private int conversationType;
    private HashMap<String, String> status;
    public static final Creator<ConversationStatus> CREATOR = new Creator<ConversationStatus>() {
        public ConversationStatus createFromParcel(Parcel source) {
            return new ConversationStatus(source);
        }

        public ConversationStatus[] newArray(int size) {
            return new ConversationStatus[size];
        }
    };

    public String getTargetId() {
        return this.targetId;
    }

    public int getConversationType() {
        return this.conversationType;
    }

    public HashMap<String, String> getStatus() {
        return this.status;
    }

    public void setTargetId(String sid) {
        this.targetId = sid;
    }

    public void setConversationType(int stype) {
        this.conversationType = stype;
    }

    public void setStatus(HashMap<String, String> status) {
        this.status = status;
    }

    public boolean isTop(HashMap<String, String> status) {
        return "1".equals(status.get("2"));
    }

    public ConversationNotificationStatus getNotifyStatus(HashMap<String, String> status) {
        return String.valueOf(ConversationNotificationStatus.NOTIFY.getValue()).equals(status.get("1")) ? ConversationNotificationStatus.DO_NOT_DISTURB : ConversationNotificationStatus.NOTIFY;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.targetId);
        dest.writeInt(this.conversationType);
        dest.writeSerializable(this.status);
    }

    public ConversationStatus() {
    }

    protected ConversationStatus(Parcel in) {
        this.targetId = in.readString();
        this.conversationType = in.readInt();
        this.status = (HashMap)in.readSerializable();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface IsTop {
        String unTop = "0";
        String top = "1";
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StatusMode {
        String NOTIFICATION_STATUS = "1";
        String TOP_STATUS = "2";
    }
}
