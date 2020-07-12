//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;

public class UserInfo implements Parcelable {
    private String id;
    private String name;
    private Uri portraitUri;
    private String extra;
    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public UserInfo(Parcel in) {
        this.setUserId(ParcelUtils.readFromParcel(in));
        this.setName(ParcelUtils.readFromParcel(in));
        this.setPortraitUri((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setExtra(ParcelUtils.readFromParcel(in));
    }

    public UserInfo(String id, String name, Uri portraitUri) {
        if (TextUtils.isEmpty(id)) {
            throw new NullPointerException("userId is null");
        } else {
            this.id = id;
            this.name = name;
            this.portraitUri = portraitUri;
        }
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getPortraitUri() {
        return this.portraitUri;
    }

    public void setPortraitUri(Uri uri) {
        this.portraitUri = uri;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserId());
        ParcelUtils.writeToParcel(dest, this.getName());
        ParcelUtils.writeToParcel(dest, this.getPortraitUri());
        ParcelUtils.writeToParcel(dest, this.getExtra());
    }
}
