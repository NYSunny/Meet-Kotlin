//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;

public class CSHumanEvaluateItem implements Parcelable {
    private int value;
    private String description;
    public static final Creator<CSHumanEvaluateItem> CREATOR = new Creator<CSHumanEvaluateItem>() {
        public CSHumanEvaluateItem createFromParcel(Parcel source) {
            return new CSHumanEvaluateItem(source);
        }

        public CSHumanEvaluateItem[] newArray(int size) {
            return new CSHumanEvaluateItem[size];
        }
    };

    public CSHumanEvaluateItem(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public CSHumanEvaluateItem(Parcel in) {
        this.value = ParcelUtils.readIntFromParcel(in);
        this.description = ParcelUtils.readFromParcel(in);
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.value);
        ParcelUtils.writeToParcel(dest, this.description);
    }
}
