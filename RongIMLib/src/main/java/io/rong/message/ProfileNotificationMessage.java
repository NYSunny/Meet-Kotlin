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
        value = "RC:ProfileNtf",
        flag = 1
)
public class ProfileNotificationMessage extends MessageContent {
    private static final String TAG = "ProfileNotificationMessage";
    private String operation;
    private String data;
    private String extra;
    public static final Creator<ProfileNotificationMessage> CREATOR = new Creator<ProfileNotificationMessage>() {
        public ProfileNotificationMessage createFromParcel(Parcel source) {
            return new ProfileNotificationMessage(source);
        }

        public ProfileNotificationMessage[] newArray(int size) {
            return new ProfileNotificationMessage[size];
        }
    };

    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ProfileNotificationMessage(Parcel in) {
        this.operation = ParcelUtils.readFromParcel(in);
        this.data = ParcelUtils.readFromParcel(in);
        this.extra = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static ProfileNotificationMessage obtain(String operation, String data) {
        ProfileNotificationMessage obj = new ProfileNotificationMessage();
        obj.operation = operation;
        obj.data = data;
        return obj;
    }

    private ProfileNotificationMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("operation", this.operation);
            if (!TextUtils.isEmpty(this.data)) {
                jsonObj.put("data", this.data);
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("ProfileNotificationMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("ProfileNotificationMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public ProfileNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("ProfileNotificationMessage", "UnsupportedEncodingException ", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setOperation(jsonObj.optString("operation"));
            this.setData(jsonObj.optString("data"));
            this.setExtra(jsonObj.optString("extra"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("ProfileNotificationMessage", "JSONException " + var4.getMessage());
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.operation);
        ParcelUtils.writeToParcel(dest, this.data);
        ParcelUtils.writeToParcel(dest, this.extra);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public int describeContents() {
        return 0;
    }
}
