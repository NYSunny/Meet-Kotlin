//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.notification;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import io.rong.push.RongPushClient.ConversationType;

public class PushNotificationMessage implements Parcelable {
    private String pushId;
    private ConversationType conversationType;
    private long receivedTime;
    private String objectName;
    private String senderId;
    private String senderName;
    private Uri senderPortrait;
    private String targetId;
    private String targetUserName;
    private String toId;
    private String pushTitle;
    private String pushContent;
    private String pushData;
    private String extra;
    private String isFromPush;
    private PushSourceType sourceType;
    public static final Creator<PushNotificationMessage> CREATOR = new Creator<PushNotificationMessage>() {
        public PushNotificationMessage createFromParcel(Parcel source) {
            return new PushNotificationMessage(source);
        }

        public PushNotificationMessage[] newArray(int size) {
            return new PushNotificationMessage[size];
        }
    };

    public PushNotificationMessage() {
    }

    public void setPushId(String id) {
        this.pushId = id;
    }

    public void setConversationType(ConversationType type) {
        this.conversationType = type;
    }

    public void setReceivedTime(long time) {
        this.receivedTime = time;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public void setSenderId(String id) {
        this.senderId = id;
    }

    public void setSenderName(String name) {
        this.senderName = name;
    }

    public void setSenderPortrait(Uri uri) {
        this.senderPortrait = uri;
    }

    public void setTargetId(String id) {
        this.targetId = id;
    }

    public void setTargetUserName(String name) {
        this.targetUserName = name;
    }

    public void setToId(String id) {
        this.toId = id;
    }

    public void setPushTitle(String title) {
        this.pushTitle = title;
    }

    public void setPushContent(String content) {
        this.pushContent = content;
    }

    public void setPushData(String appDataContent) {
        this.pushData = appDataContent;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void setPushFlag(String value) {
        this.isFromPush = value;
    }

    public void setSourceType(PushSourceType type) {
        this.sourceType = type;
    }

    public String getPushId() {
        return this.pushId;
    }

    public ConversationType getConversationType() {
        return this.conversationType;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public String getTargetUserName() {
        return this.targetUserName;
    }

    public String getToId() {
        return this.toId;
    }

    public long getReceivedTime() {
        return this.receivedTime;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public String getSenderId() {
        return this.senderId;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public Uri getSenderPortrait() {
        return this.senderPortrait;
    }

    public String getPushTitle() {
        return this.pushTitle;
    }

    public String getPushContent() {
        return this.pushContent;
    }

    public String getPushData() {
        return this.pushData;
    }

    public String getExtra() {
        return this.extra;
    }

    public String getPushFlag() {
        return this.isFromPush;
    }

    public PushSourceType getSourceType() {
        return this.sourceType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pushId);
        dest.writeInt(this.conversationType == null ? -1 : this.conversationType.ordinal());
        dest.writeLong(this.receivedTime);
        dest.writeString(this.objectName);
        dest.writeString(this.senderId);
        dest.writeString(this.senderName);
        dest.writeParcelable(this.senderPortrait, flags);
        dest.writeString(this.targetId);
        dest.writeString(this.targetUserName);
        dest.writeString(this.toId);
        dest.writeString(this.pushTitle);
        dest.writeString(this.pushContent);
        dest.writeString(this.pushData);
        dest.writeString(this.extra);
        dest.writeString(this.isFromPush);
        dest.writeInt(this.sourceType == null ? -1 : this.sourceType.ordinal());
    }

    protected PushNotificationMessage(Parcel in) {
        this.pushId = in.readString();
        int tmpConversationType = in.readInt();
        this.conversationType = tmpConversationType == -1 ? null : ConversationType.values()[tmpConversationType];
        this.receivedTime = in.readLong();
        this.objectName = in.readString();
        this.senderId = in.readString();
        this.senderName = in.readString();
        this.senderPortrait = (Uri)in.readParcelable(Uri.class.getClassLoader());
        this.targetId = in.readString();
        this.targetUserName = in.readString();
        this.toId = in.readString();
        this.pushTitle = in.readString();
        this.pushContent = in.readString();
        this.pushData = in.readString();
        this.extra = in.readString();
        this.isFromPush = in.readString();
        int tmpSourceType = in.readInt();
        this.sourceType = tmpSourceType == -1 ? null : PushSourceType.values()[tmpSourceType];
    }

    public static enum PushSourceType {
        FROM_OFFLINE_MESSAGE,
        FROM_ADMIN,
        LOCAL_MESSAGE,
        UNKNOWN;

        private PushSourceType() {
        }
    }
}
