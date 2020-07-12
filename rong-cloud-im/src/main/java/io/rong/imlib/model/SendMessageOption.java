//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SendMessageOption implements Parcelable {
    private boolean isVoIPPush;
    public static final Creator<SendMessageOption> CREATOR = new Creator<SendMessageOption>() {
        public SendMessageOption createFromParcel(Parcel in) {
            return new SendMessageOption(in);
        }

        public SendMessageOption[] newArray(int size) {
            return new SendMessageOption[size];
        }
    };

    public SendMessageOption(Parcel in) {
        this.isVoIPPush = in.readByte() != 0;
    }

    public SendMessageOption() {
    }

    public boolean isVoIPPush() {
        return this.isVoIPPush;
    }

    public void setVoIPPush(boolean voIPPush) {
        this.isVoIPPush = voIPPush;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(this.isVoIPPush ? 1 : 0));
    }
}
