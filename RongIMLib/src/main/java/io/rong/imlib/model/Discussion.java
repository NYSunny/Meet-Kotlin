//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import io.rong.common.ParcelUtils;
import io.rong.imlib.NativeObject.DiscussionInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Discussion implements Parcelable {
    private String id;
    private String name;
    private String creatorId;
    private boolean isOpen;
    private List<String> memberIdList;
    public static final Creator<Discussion> CREATOR = new Creator<Discussion>() {
        public Discussion createFromParcel(Parcel source) {
            return new Discussion(source);
        }

        public Discussion[] newArray(int size) {
            return new Discussion[size];
        }
    };

    public Discussion(DiscussionInfo info) {
        this.isOpen = true;
        this.id = info.getDiscussionId();
        this.name = info.getDiscussionName();
        this.creatorId = info.getAdminId();
        if (!TextUtils.isEmpty(info.getUserIds())) {
            this.memberIdList = new ArrayList(Arrays.asList(info.getUserIds().split("\n")));
        }

        Log.d("Discussion", "info.getInviteStatus():" + info.getInviteStatus());
        this.isOpen = info.getInviteStatus() != 1;
    }

    public Discussion(Parcel in) {
        this(ParcelUtils.readFromParcel(in), ParcelUtils.readFromParcel(in), ParcelUtils.readFromParcel(in), ParcelUtils.readIntFromParcel(in) == 1, ParcelUtils.readListFromParcel(in, String.class));
    }

    public Discussion(String id, String name) {
        this.isOpen = true;
        this.id = id;
        this.name = name;
    }

    public Discussion(String id, String name, String creatorId, boolean isOpen, List<String> memberIdList) {
        this.isOpen = true;
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.isOpen = isOpen;
        this.memberIdList = memberIdList;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<String> getMemberIdList() {
        return this.memberIdList;
    }

    public void setMemberIdList(List<String> memberIdList) {
        this.memberIdList = memberIdList;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getId());
        ParcelUtils.writeToParcel(dest, this.getName());
        ParcelUtils.writeToParcel(dest, this.getCreatorId());
        ParcelUtils.writeToParcel(dest, this.isOpen() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.getMemberIdList());
    }
}
