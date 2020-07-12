//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import java.util.List;

public class MentionedInfo implements Parcelable {
    private static final String TAG = "MentionedInfo";
    private MentionedInfo.MentionedType type;
    private List<String> userIdList;
    private String mentionedContent;
    public static final Creator<MentionedInfo> CREATOR = new Creator<MentionedInfo>() {
        public MentionedInfo createFromParcel(Parcel source) {
            return new MentionedInfo(source);
        }

        public MentionedInfo[] newArray(int size) {
            return new MentionedInfo[size];
        }
    };

    public MentionedInfo() {
    }

    public MentionedInfo(Parcel in) {
        this.setType(MentionedInfo.MentionedType.valueOf(ParcelUtils.readIntFromParcel(in)));
        this.setMentionedUserIdList(ParcelUtils.readListFromParcel(in, String.class));
        this.setMentionedContent(ParcelUtils.readFromParcel(in));
    }

    public MentionedInfo(MentionedInfo.MentionedType type, List<String> userIdList, String mentionedContent) {
        if (type != null && type.equals(MentionedInfo.MentionedType.ALL)) {
            this.userIdList = null;
        } else if (type != null && type.equals(MentionedInfo.MentionedType.PART)) {
            if (userIdList == null || userIdList.size() == 0) {
                RLog.e("MentionedInfo", "When mentioned parts of the group members, userIdList can't be null!");
            }

            this.userIdList = userIdList;
        }

        this.type = type;
        this.mentionedContent = mentionedContent;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getType().getValue());
        ParcelUtils.writeToParcel(dest, this.getMentionedUserIdList());
        ParcelUtils.writeToParcel(dest, this.getMentionedContent());
    }

    public MentionedInfo.MentionedType getType() {
        return this.type;
    }

    public List<String> getMentionedUserIdList() {
        return this.userIdList;
    }

    public String getMentionedContent() {
        return this.mentionedContent;
    }

    public void setType(MentionedInfo.MentionedType type) {
        this.type = type;
    }

    public void setMentionedUserIdList(List<String> userList) {
        this.userIdList = userList;
    }

    public void setMentionedContent(String content) {
        this.mentionedContent = content;
    }

    public static enum MentionedType {
        NONE(0),
        ALL(1),
        PART(2);

        private int value;

        private MentionedType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static MentionedInfo.MentionedType valueOf(int value) {
            MentionedInfo.MentionedType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                MentionedInfo.MentionedType type = var1[var3];
                if (type.getValue() == value) {
                    return type;
                }
            }

            return NONE;
        }
    }
}
