//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;

public class RTCUser implements Parcelable {
    private String uid;
    private HashMap<String, String> data;
    public static final Creator<RTCUser> CREATOR = new Creator<RTCUser>() {
        public RTCUser createFromParcel(Parcel source) {
            return new RTCUser(source);
        }

        public RTCUser[] newArray(int size) {
            return new RTCUser[size];
        }
    };

    public String getUid() {
        return this.uid;
    }

    public HashMap<String, String> getData() {
        return this.data;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeSerializable(this.data);
    }

    public RTCUser() {
    }

    protected RTCUser(Parcel in) {
        this.uid = in.readString();
        this.data = (HashMap)in.readSerializable();
    }
}
