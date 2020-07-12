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
        value = "RC:EncryptResponseMsg",
        flag = 0
)
public class RCEncryptResponseMessage extends MessageContent {
    private static final String TAG = RCEncryptResponseMessage.class.getName();
    private String requesterEncId;
    private String responserEncId;
    private String responserKey;
    public static final Creator<RCEncryptResponseMessage> CREATOR = new Creator<RCEncryptResponseMessage>() {
        public RCEncryptResponseMessage createFromParcel(Parcel source) {
            return new RCEncryptResponseMessage(source);
        }

        public RCEncryptResponseMessage[] newArray(int size) {
            return new RCEncryptResponseMessage[size];
        }
    };

    public String getRequesterEncId() {
        return this.requesterEncId;
    }

    public void setRequesterEncId(String requesterEncId) {
        this.requesterEncId = requesterEncId;
    }

    public String getResponserEncId() {
        return this.responserEncId;
    }

    public void setResponserEncId(String responserEncId) {
        this.responserEncId = responserEncId;
    }

    public String getResponserKey() {
        return this.responserKey;
    }

    public void setResponserKey(String responserKey) {
        this.responserKey = responserKey;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getRequesterEncId())) {
                jsonObj.put("requesterEncId", this.getRequesterEncId());
            }

            if (!TextUtils.isEmpty(this.getResponserEncId())) {
                jsonObj.putOpt("responserEncId", this.getResponserEncId());
            }

            if (!TextUtils.isEmpty(this.getResponserKey())) {
                jsonObj.putOpt("responserKey", this.getResponserKey());
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
        dest.writeString(this.requesterEncId);
        dest.writeString(this.responserEncId);
        dest.writeString(this.responserKey);
    }

    public RCEncryptResponseMessage() {
    }

    public RCEncryptResponseMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e(TAG, "RCEncryptResponseMessage", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("requesterEncId")) {
                this.setRequesterEncId(jsonObj.optString("requesterEncId"));
            }

            if (jsonObj.has("responserEncId")) {
                this.setResponserEncId(jsonObj.optString("responserEncId"));
            }

            if (jsonObj.has("responserKey")) {
                this.setResponserKey(jsonObj.optString("responserKey"));
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

    }

    protected RCEncryptResponseMessage(Parcel in) {
        this.requesterEncId = in.readString();
        this.responserEncId = in.readString();
        this.responserKey = in.readString();
    }

    public String toString() {
        return "RCEncryptResponseMessage{requesterEncId='" + this.requesterEncId + '\'' + ", responserEncId='" + this.responserEncId + '\'' + ", responserKey='" + this.responserKey + '\'' + '}';
    }
}
