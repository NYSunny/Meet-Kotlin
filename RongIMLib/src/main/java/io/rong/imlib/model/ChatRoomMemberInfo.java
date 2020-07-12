//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;

public class ChatRoomMemberInfo implements Parcelable {
    private String id;
    private long joinTime;
    public static final Creator<ChatRoomMemberInfo> CREATOR = new Creator<ChatRoomMemberInfo>() {
        public ChatRoomMemberInfo createFromParcel(Parcel source) {
            return new ChatRoomMemberInfo(source);
        }

        public ChatRoomMemberInfo[] newArray(int size) {
            return new ChatRoomMemberInfo[size];
        }
    };

    public ChatRoomMemberInfo() {
    }

    public long getJoinTime() {
        return this.joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    private ChatRoomMemberInfo(Parcel in) {
        this.setUserId(ParcelUtils.readFromParcel(in));
        this.setJoinTime(ParcelUtils.readLongFromParcel(in));
    }

    public String getUserId() {
        if (TextUtils.isEmpty(this.id)) {
            throw new NullPointerException("userId  is null");
        } else {
            return this.id;
        }
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserId());
        ParcelUtils.writeToParcel(dest, this.joinTime);
    }
}
