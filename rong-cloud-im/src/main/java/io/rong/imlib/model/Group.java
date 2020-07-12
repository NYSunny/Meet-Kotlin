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

public class Group implements Parcelable {
    private String id;
    private String name;
    private Uri portraitUri;
    public static final Creator<Group> CREATOR = new Creator<Group>() {
        public Group createFromParcel(Parcel source) {
            return new Group(source);
        }

        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public Group(Parcel in) {
        this(ParcelUtils.readFromParcel(in), ParcelUtils.readFromParcel(in), (Uri)ParcelUtils.readFromParcel(in, Uri.class));
    }

    public Group(String id, String name, Uri portraitUri) {
        if (TextUtils.isEmpty(id)) {
            throw new RuntimeException("groupId is null");
        } else {
            this.id = id;
            this.name = name;
            this.portraitUri = portraitUri;
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getId());
        ParcelUtils.writeToParcel(dest, this.getName());
        ParcelUtils.writeToParcel(dest, this.getPortraitUri());
    }
}
