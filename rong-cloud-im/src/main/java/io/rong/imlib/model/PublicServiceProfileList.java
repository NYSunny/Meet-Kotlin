//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import java.util.ArrayList;

public class PublicServiceProfileList implements Parcelable {
    private ArrayList<PublicServiceProfile> mList;
    public static final Creator<PublicServiceProfileList> CREATOR = new Creator<PublicServiceProfileList>() {
        public PublicServiceProfileList createFromParcel(Parcel source) {
            return new PublicServiceProfileList(source);
        }

        public PublicServiceProfileList[] newArray(int size) {
            return new PublicServiceProfileList[size];
        }
    };

    public PublicServiceProfileList() {
    }

    public PublicServiceProfileList(ArrayList<PublicServiceProfile> list) {
        this.mList = list;
    }

    public PublicServiceProfileList(Parcel in) {
        this.mList = ParcelUtils.readListFromParcel(in, PublicServiceProfile.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeListToParcel(dest, this.mList);
    }

    public ArrayList<PublicServiceProfile> getPublicServiceData() {
        return this.mList;
    }
}
