//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:InfoNtf",
        flag = 1
)
public class InformationNotificationMessage extends MessageContent {
    private static final String TAG = "InformationNotificationMessage";
    private String message;
    protected String extra;
    public static final Creator<InformationNotificationMessage> CREATOR = new Creator<InformationNotificationMessage>() {
        public InformationNotificationMessage createFromParcel(Parcel source) {
            return new InformationNotificationMessage(source);
        }

        public InformationNotificationMessage[] newArray(int size) {
            return new InformationNotificationMessage[size];
        }
    };

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getMessage())) {
                jsonObj.put("message", this.getMessage());
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("InformationNotificationMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("InformationNotificationMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    protected InformationNotificationMessage() {
    }

    public static InformationNotificationMessage obtain(String message) {
        InformationNotificationMessage model = new InformationNotificationMessage();
        model.setMessage(message);
        return model;
    }

    public InformationNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("InformationNotificationMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("message")) {
                this.setMessage(jsonObj.optString("message"));
            }

            if (jsonObj.has("extra")) {
                this.setExtra(jsonObj.optString("extra"));
            }

            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("InformationNotificationMessage", "JSONException " + var4.getMessage());
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getMessage());
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public InformationNotificationMessage(Parcel in) {
        this.setMessage(ParcelUtils.readFromParcel(in));
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public InformationNotificationMessage(String message) {
        this.setMessage(message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
