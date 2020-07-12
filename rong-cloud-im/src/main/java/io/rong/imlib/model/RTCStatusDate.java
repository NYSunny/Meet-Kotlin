//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RTCStatusDate implements Parcelable {
    String key;
    String value;
    boolean autoDelete;
    boolean overwrite;
    public static final Creator<RTCStatusDate> CREATOR = new Creator<RTCStatusDate>() {
        public RTCStatusDate createFromParcel(Parcel in) {
            return new RTCStatusDate(in);
        }

        public RTCStatusDate[] newArray(int size) {
            return new RTCStatusDate[size];
        }
    };

    public RTCStatusDate() {
    }

    protected RTCStatusDate(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
        this.autoDelete = in.readByte() != 0;
        this.overwrite = in.readByte() != 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeByte((byte)(this.autoDelete ? 1 : 0));
        dest.writeByte((byte)(this.overwrite ? 1 : 0));
    }

    public int describeContents() {
        return 0;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isAutoDelete() {
        return this.autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    public boolean isOverwrite() {
        return this.overwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }
}
