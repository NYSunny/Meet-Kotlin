//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.imlib.MessageTag;

@MessageTag(
        value = "RC:RecNtf",
        flag = 0
)
public class HasReceivedNotificationMessage extends NotificationMessage {
    private boolean hasReceived;
    public static final Creator<HasReceivedNotificationMessage> CREATOR = new Creator<HasReceivedNotificationMessage>() {
        public HasReceivedNotificationMessage createFromParcel(Parcel source) {
            return new HasReceivedNotificationMessage(source);
        }

        public HasReceivedNotificationMessage[] newArray(int size) {
            return new HasReceivedNotificationMessage[size];
        }
    };

    public HasReceivedNotificationMessage(Parcel in) {
    }

    public boolean isHasReceived() {
        return this.hasReceived;
    }

    public void setHasReceived(boolean hasReceived) {
        this.hasReceived = hasReceived;
    }

    public byte[] encode() {
        return new byte[0];
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
