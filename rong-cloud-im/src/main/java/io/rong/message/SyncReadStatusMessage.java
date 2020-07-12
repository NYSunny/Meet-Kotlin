//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:SRSMsg",
        flag = 0
)
public class SyncReadStatusMessage extends MessageContent {
    private static final String TAG = "SyncReadStatusMessage";
    private long lastMessageSendTime;
    public static final Creator<SyncReadStatusMessage> CREATOR = new Creator<SyncReadStatusMessage>() {
        public SyncReadStatusMessage createFromParcel(Parcel source) {
            return new SyncReadStatusMessage(source);
        }

        public SyncReadStatusMessage[] newArray(int size) {
            return new SyncReadStatusMessage[size];
        }
    };

    public long getLastMessageSendTime() {
        return this.lastMessageSendTime;
    }

    public SyncReadStatusMessage(long lastMessageSendTime) {
        this.lastMessageSendTime = lastMessageSendTime;
    }

    public SyncReadStatusMessage(Parcel in) {
        this.lastMessageSendTime = ParcelUtils.readLongFromParcel(in);
    }

    public SyncReadStatusMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("SyncReadStatusMessage", var5.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("lastMessageSendTime")) {
                this.lastMessageSendTime = jsonObj.getLong("lastMessageSendTime");
            }
        } catch (JSONException var4) {
            RLog.e("SyncReadStatusMessage", var4.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("lastMessageSendTime", this.lastMessageSendTime);
        } catch (JSONException var4) {
            RLog.e("SyncReadStatusMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("SyncReadStatusMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.lastMessageSendTime);
    }
}
