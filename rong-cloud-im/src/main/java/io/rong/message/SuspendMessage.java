//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(
        value = "RC:SpMsg",
        flag = 0
)
public class SuspendMessage extends MessageContent {
    public static final Creator<SuspendMessage> CREATOR = new Creator<SuspendMessage>() {
        public SuspendMessage createFromParcel(Parcel source) {
            return new SuspendMessage(source);
        }

        public SuspendMessage[] newArray(int size) {
            return new SuspendMessage[size];
        }
    };

    public SuspendMessage() {
    }

    public SuspendMessage(byte[] data) {
    }

    public SuspendMessage(Parcel in) {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }

    public byte[] encode() {
        return "{\"type\":1}".getBytes();
    }
}
