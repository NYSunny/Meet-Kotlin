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
        value = "RC:EncryptRequestMsg",
        flag = 0
)
public class RCEncryptRequestMessage extends MessageContent {
    private String TAG = RCEncryptRequestMessage.class.getName();
    private String requesterEncId;
    private String requesterKey;
    public static final Creator<RCEncryptRequestMessage> CREATOR = new Creator<RCEncryptRequestMessage>() {
        public RCEncryptRequestMessage createFromParcel(Parcel source) {
            return new RCEncryptRequestMessage(source);
        }

        public RCEncryptRequestMessage[] newArray(int size) {
            return new RCEncryptRequestMessage[size];
        }
    };

    public String getRequesterEncId() {
        return this.requesterEncId;
    }

    public void setRequesterEncId(String requesterEncId) {
        this.requesterEncId = requesterEncId;
    }

    public String getRequesterKey() {
        return this.requesterKey;
    }

    public void setRequesterKey(String requesterKey) {
        this.requesterKey = requesterKey;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getRequesterEncId())) {
                jsonObj.put("requesterEncId", this.getRequesterEncId());
            }

            if (!TextUtils.isEmpty(this.getRequesterKey())) {
                jsonObj.put("requesterKey", this.getRequesterKey());
            }
        } catch (JSONException var4) {
            RLog.e(this.TAG, "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e(this.TAG, "encode", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requesterEncId);
        dest.writeString(this.requesterKey);
    }

    public RCEncryptRequestMessage() {
    }

    public RCEncryptRequestMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "utf-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.has("requesterEncId")) {
                this.setRequesterEncId(jsonObject.optString("requesterEncId"));
            }

            if (jsonObject.has("requesterKey")) {
                this.setRequesterKey(jsonObject.optString("requesterKey"));
            }
        } catch (JSONException var4) {
            RLog.e(this.TAG, "JSONException " + var4.getMessage());
            var4.printStackTrace();
        }

    }

    protected RCEncryptRequestMessage(Parcel in) {
        this.requesterEncId = in.readString();
        this.requesterKey = in.readString();
    }

    public String toString() {
        return "RCEncryptRequestMessage{requesterEncId='" + this.requesterEncId + '\'' + ", requesterKey='" + this.requesterKey + '\'' + '}';
    }
}
