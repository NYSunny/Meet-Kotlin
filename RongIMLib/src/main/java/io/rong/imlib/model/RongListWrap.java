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
import java.util.List;

public class RongListWrap implements Parcelable {
    private List mList = new ArrayList();
    public static Class mClass;
    public static final Creator<RongListWrap> CREATOR = new Creator<RongListWrap>() {
        public RongListWrap createFromParcel(Parcel source) {
            return new RongListWrap(source);
        }

        public RongListWrap[] newArray(int size) {
            return new RongListWrap[size];
        }
    };

    public RongListWrap() {
    }

    public static RongListWrap obtain(List list, Class cls) {
        return new RongListWrap(list, cls);
    }

    public RongListWrap(List list, Class cls) {
        this.mList = list;
        mClass = cls;
    }

    public RongListWrap(Parcel in) {
        this.mList = ParcelUtils.readListFromParcel(in, Message.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeListToParcel(dest, this.mList);
    }

    public List getList() {
        return this.mList;
    }

    public void setList(List list) {
        this.mList = list;
    }
}
