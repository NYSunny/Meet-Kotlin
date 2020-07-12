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
        value = "RC:EncryptConfirmMsg",
        flag = 0
)
public class RCEncryptConfirmMessage extends MessageContent {
    static final String TAG = RCEncryptConfirmMessage.class.getName();
    private String requesterEncId;
    private String responserEncId;
    public static final Creator<RCEncryptConfirmMessage> CREATOR = new Creator<RCEncryptConfirmMessage>() {
        public RCEncryptConfirmMessage createFromParcel(Parcel source) {
            return new RCEncryptConfirmMessage(source);
        }

        public RCEncryptConfirmMessage[] newArray(int size) {
            return new RCEncryptConfirmMessage[size];
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

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getRequesterEncId())) {
                jsonObj.put("requesterEncId", this.getRequesterEncId());
            }

            if (!TextUtils.isEmpty(this.getResponserEncId())) {
                jsonObj.put("responserEncId", this.getResponserEncId());
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requesterEncId);
        dest.writeString(this.responserEncId);
    }

    public RCEncryptConfirmMessage() {
    }

    public RCEncryptConfirmMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("requesterEncId")) {
                this.setRequesterEncId(jsonObj.optString("requesterEncId"));
            }

            if (jsonObj.has("responserEncId")) {
                this.setResponserEncId(jsonObj.optString("responserEncId"));
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

    }

    protected RCEncryptConfirmMessage(Parcel in) {
        this.requesterEncId = in.readString();
        this.responserEncId = in.readString();
    }

    public String toString() {
        return "RCEncryptConfirmMessage{requesterEncId='" + this.requesterEncId + '\'' + ", responserEncId='" + this.responserEncId + '\'' + '}';
    }
}
