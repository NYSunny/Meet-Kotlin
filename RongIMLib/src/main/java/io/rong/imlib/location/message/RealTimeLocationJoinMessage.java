//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

@MessageTag(
        value = "RC:RLJoin",
        flag = 0
)
public class RealTimeLocationJoinMessage extends MessageContent {
    private String content;
    public static final Creator<RealTimeLocationJoinMessage> CREATOR = new Creator<RealTimeLocationJoinMessage>() {
        public RealTimeLocationJoinMessage createFromParcel(Parcel source) {
            return new RealTimeLocationJoinMessage(source);
        }

        public RealTimeLocationJoinMessage[] newArray(int size) {
            return new RealTimeLocationJoinMessage[size];
        }
    };

    public RealTimeLocationJoinMessage(String content) {
        this.content = "";
        this.content = content;
    }

    public RealTimeLocationJoinMessage(byte[] data) {
        this.content = "";
    }

    public static RealTimeLocationJoinMessage obtain(String content) {
        return new RealTimeLocationJoinMessage(content);
    }

    private RealTimeLocationJoinMessage(Parcel in) {
        this.content = "";
        this.content = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    public byte[] encode() {
        return new byte[0];
    }
}
