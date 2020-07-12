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
        value = "RC:RLQuit",
        flag = 0
)
public class RealTimeLocationQuitMessage extends MessageContent {
    private String content;
    public static final Creator<RealTimeLocationQuitMessage> CREATOR = new Creator<RealTimeLocationQuitMessage>() {
        public RealTimeLocationQuitMessage createFromParcel(Parcel source) {
            return new RealTimeLocationQuitMessage(source);
        }

        public RealTimeLocationQuitMessage[] newArray(int size) {
            return new RealTimeLocationQuitMessage[size];
        }
    };

    public RealTimeLocationQuitMessage(String content) {
        this.content = "";
        this.content = content;
    }

    public RealTimeLocationQuitMessage(byte[] data) {
        this.content = "";
    }

    public static RealTimeLocationQuitMessage obtain(String content) {
        return new RealTimeLocationQuitMessage(content);
    }

    private RealTimeLocationQuitMessage(Parcel in) {
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
