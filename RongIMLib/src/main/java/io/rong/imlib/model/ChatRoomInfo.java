//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import java.util.List;

public class ChatRoomInfo implements Parcelable {
    private String chatRoomId;
    private ChatRoomInfo.ChatRoomMemberOrder order;
    private List<ChatRoomMemberInfo> memberInfo;
    private int totalMemberCount;
    public static final Creator<ChatRoomInfo> CREATOR = new Creator<ChatRoomInfo>() {
        public ChatRoomInfo createFromParcel(Parcel source) {
            return new ChatRoomInfo(source);
        }

        public ChatRoomInfo[] newArray(int size) {
            return new ChatRoomInfo[size];
        }
    };

    public ChatRoomInfo() {
    }

    public String getChatRoomId() {
        return this.chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public ChatRoomInfo.ChatRoomMemberOrder getMemberOrder() {
        return this.order;
    }

    public void setMemberOrder(ChatRoomInfo.ChatRoomMemberOrder order) {
        this.order = order;
    }

    public void setUsers(List<ChatRoomMemberInfo> users) {
        this.memberInfo = users;
    }

    public int getTotalMemberCount() {
        return this.totalMemberCount;
    }

    public void setTotalMemberCount(int totalMemberCount) {
        this.totalMemberCount = totalMemberCount;
    }

    public List<ChatRoomMemberInfo> getMemberInfo() {
        return this.memberInfo;
    }

    public void setMemberInfo(List<ChatRoomMemberInfo> memberInfo) {
        this.memberInfo = memberInfo;
    }

    public ChatRoomInfo(Parcel in) {
        this.chatRoomId = ParcelUtils.readFromParcel(in);
        this.totalMemberCount = ParcelUtils.readIntFromParcel(in);
        this.memberInfo = ParcelUtils.readListFromParcel(in, ChatRoomMemberInfo.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.chatRoomId);
        ParcelUtils.writeToParcel(dest, this.totalMemberCount);
        ParcelUtils.writeToParcel(dest, this.memberInfo);
    }

    public static enum ChatRoomMemberOrder {
        RC_CHAT_ROOM_MEMBER_ASC(1),
        RC_CHAT_ROOM_MEMBER_DESC(2);

        int value;

        private ChatRoomMemberOrder(int v) {
            this.value = v;
        }

        public int getValue() {
            return this.value;
        }
    }
}
