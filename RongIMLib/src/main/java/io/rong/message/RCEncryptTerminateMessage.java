//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:EncryptTerminateMsg",
        flag = 0
)
public class RCEncryptTerminateMessage extends MessageContent {
    private static final String TAG = RCEncryptTerminateMessage.class.getName();
    private String senderEncId;
    private String receiverEncId;
    public static final Creator<RCEncryptTerminateMessage> CREATOR = new Creator<RCEncryptTerminateMessage>() {
        public RCEncryptTerminateMessage createFromParcel(Parcel source) {
            return new RCEncryptTerminateMessage(source);
        }

        public RCEncryptTerminateMessage[] newArray(int size) {
            return new RCEncryptTerminateMessage[size];
        }
    };

    public String getSenderEncId() {
        return this.senderEncId;
    }

    public void setSenderEncId(String senderEncId) {
        this.senderEncId = senderEncId;
    }

    public String getReceiverEncId() {
        return this.receiverEncId;
    }

    public void setReceiverEncId(String receiverEncId) {
        this.receiverEncId = receiverEncId;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getSenderEncId())) {
                jsonObj.put("senderEncId", this.getSenderEncId());
            }

            if (!TextUtils.isEmpty(this.getReceiverEncId())) {
                jsonObj.put("receiverEncId", this.getReceiverEncId());
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e(TAG, "encode", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.senderEncId);
        dest.writeString(this.receiverEncId);
    }

    public RCEncryptTerminateMessage() {
    }

    public RCEncryptTerminateMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e(TAG, "RCEncryptTerminateMessage", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("senderEncId")) {
                this.setSenderEncId(jsonObj.optString("senderEncId"));
            }

            if (jsonObj.has("receiverEncId")) {
                this.setReceiverEncId(jsonObj.optString("receiverEncId"));
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

    }

    protected RCEncryptTerminateMessage(Parcel in) {
        this.senderEncId = in.readString();
        this.receiverEncId = in.readString();
    }

    public String toString() {
        return "RCEncryptTerminateMessage{senderEncId='" + this.senderEncId + '\'' + ", receiverEncId='" + this.receiverEncId + '\'' + '}';
    }
}
