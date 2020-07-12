//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;

@MessageTag(
        value = "RC:UnknownMsg",
        flag = 0
)
public class UnknownMessage extends MessageContent {
    public static final Creator<UnknownMessage> CREATOR = new Creator<UnknownMessage>() {
        public UnknownMessage createFromParcel(Parcel source) {
            return new UnknownMessage(source);
        }

        public UnknownMessage[] newArray(int size) {
            return new UnknownMessage[size];
        }
    };

    public UnknownMessage(byte[] bytes) {
    }

    public UnknownMessage() {
    }

    public byte[] encode() {
        return new byte[0];
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, 0);
    }

    private UnknownMessage(Parcel in) {
        ParcelUtils.readIntFromParcel(in);
    }
}
