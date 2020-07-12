//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RCEncryptedSession extends Conversation implements Parcelable {
    private String targetId;
    private String remoteEncId;
    private String encKey;
    private String encXA;
    private int encStatus;
    public static final Creator<RCEncryptedSession> CREATOR = new Creator<RCEncryptedSession>() {
        public RCEncryptedSession createFromParcel(Parcel source) {
            return new RCEncryptedSession(source);
        }

        public RCEncryptedSession[] newArray(int size) {
            return new RCEncryptedSession[size];
        }
    };

    public int getEncStatus() {
        return this.encStatus;
    }

    public void setEncStatus(int encStatus) {
        this.encStatus = encStatus;
    }

    public String getRemoteEncId() {
        return this.remoteEncId;
    }

    public void setRemoteEncId(String remoteEncId) {
        this.remoteEncId = remoteEncId;
    }

    public String getEncKey() {
        return this.encKey;
    }

    public void setEncKey(String encKey) {
        this.encKey = encKey;
    }

    public String getEncXA() {
        return this.encXA;
    }

    public void setEncXA(String encXA) {
        this.encXA = encXA;
    }

    public static Creator<RCEncryptedSession> getCREATOR() {
        return CREATOR;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.targetId);
        dest.writeString(this.remoteEncId);
        dest.writeString(this.encKey);
        dest.writeString(this.encXA);
        dest.writeInt(this.encStatus);
    }

    public RCEncryptedSession() {
    }

    protected RCEncryptedSession(Parcel in) {
        this.targetId = in.readString();
        this.remoteEncId = in.readString();
        this.encKey = in.readString();
        this.encXA = in.readString();
        this.encStatus = in.readInt();
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String toString() {
        return "RCEncryptedSession{targetId='" + this.targetId + '\'' + ", remoteEncId='" + this.remoteEncId + '\'' + ", encKey='" + this.encKey + '\'' + ", encXA='" + this.encXA + '\'' + ", encStatus=" + this.encStatus + '}';
    }

    public static class RCEncryptedSessionStatus {
        public static final int UNKNOWN = -2;
        public static final int NOT_FOUND = -1;
        public static final int REQUEST = 1;
        public static final int RESPONSE = 2;
        public static final int ENCRYPTED = 3;
        public static final int CANCELED = 4;
        public static final int TERMINATED = 5;

        public RCEncryptedSessionStatus() {
        }
    }
}
