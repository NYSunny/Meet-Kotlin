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

public class RemoteModelWrap implements Parcelable {
    private static final String TAG = RemoteModelWrap.class.getSimpleName();
    Parcelable model;
    public static final Creator<RemoteModelWrap> CREATOR = new Creator<RemoteModelWrap>() {
        public RemoteModelWrap createFromParcel(Parcel source) {
            return new RemoteModelWrap(source);
        }

        public RemoteModelWrap[] newArray(int size) {
            return new RemoteModelWrap[size];
        }
    };

    public RemoteModelWrap() {
    }

    public RemoteModelWrap(Parcelable t) {
        this.model = t;
    }

    public RemoteModelWrap(Parcel in) {
        String className = ParcelUtils.readFromParcel(in);
        Class loader = null;

        try {
            loader = Class.forName(className);
        } catch (ClassNotFoundException var5) {
            RLog.e(TAG, "RemoteModelWrap constructor", var5);
        }

        if (loader != null) {
            this.model = ParcelUtils.readFromParcel(in, loader);
        }

    }

    public <T extends Parcelable> T getContent() {
        return (T) this.model;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.model.getClass().getName());
        ParcelUtils.writeToParcel(dest, this.model);
    }
}
