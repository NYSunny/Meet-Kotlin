//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;

public class CSGroupItem implements Parcelable {
    private String id;
    private String name;
    private boolean online;
    public static final Creator<CSGroupItem> CREATOR = new Creator<CSGroupItem>() {
        public CSGroupItem createFromParcel(Parcel source) {
            return new CSGroupItem(source);
        }

        public CSGroupItem[] newArray(int size) {
            return new CSGroupItem[size];
        }
    };

    public CSGroupItem(String id, String name, boolean online) {
        this.id = id;
        this.name = name;
        this.online = online;
    }

    private CSGroupItem(Parcel in) {
        this.id = ParcelUtils.readFromParcel(in);
        this.name = ParcelUtils.readFromParcel(in);
        this.online = ParcelUtils.readIntFromParcel(in) == 1;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean getOnline() {
        return this.online;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.id);
        ParcelUtils.writeToParcel(dest, this.name);
        ParcelUtils.writeToParcel(dest, this.online ? 1 : 0);
    }
}
