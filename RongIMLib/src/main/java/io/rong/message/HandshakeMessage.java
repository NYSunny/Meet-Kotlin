//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.imlib.MessageTag;

@MessageTag(
        value = "RC:HsMsg",
        flag = 0
)
public class HandshakeMessage extends TextMessage {
    private int type;
    public static final Creator<HandshakeMessage> CREATOR = new Creator<HandshakeMessage>() {
        public HandshakeMessage createFromParcel(Parcel source) {
            return new HandshakeMessage(source);
        }

        public HandshakeMessage[] newArray(int size) {
            return new HandshakeMessage[size];
        }
    };

    public HandshakeMessage() {
    }

    public HandshakeMessage(byte[] data) {
    }

    public static HandshakeMessage obtain(String text) {
        HandshakeMessage model = new HandshakeMessage();
        model.setContent(text);
        return model;
    }

    public HandshakeMessage(Parcel in) {
        super(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public byte[] encode() {
        return ("{\"type\":" + this.type + "}").getBytes();
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
