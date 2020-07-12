//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:DizNtf",
        flag = 1
)
public class DiscussionNotificationMessage extends NotificationMessage {
    private static final String TAG = "DiscussionNotificationMessage";
    private int type;
    private String extension;
    private String operator;
    private boolean hasReceived;
    public static final Creator<DiscussionNotificationMessage> CREATOR = new Creator<DiscussionNotificationMessage>() {
        public DiscussionNotificationMessage createFromParcel(Parcel source) {
            return new DiscussionNotificationMessage(source);
        }

        public DiscussionNotificationMessage[] newArray(int size) {
            return new DiscussionNotificationMessage[size];
        }
    };

    public DiscussionNotificationMessage() {
    }

    public DiscussionNotificationMessage(Parcel in) {
        this.type = ParcelUtils.readIntFromParcel(in);
        this.extension = ParcelUtils.readFromParcel(in);
        this.operator = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public DiscussionNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("DiscussionNotificationMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setType(jsonObj.optInt("type"));
            this.setExtension(jsonObj.optString("extension"));
            this.setOperator(jsonObj.optString("operator"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.type);
        ParcelUtils.writeToParcel(dest, this.extension);
        ParcelUtils.writeToParcel(dest, this.operator);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public boolean isHasReceived() {
        return this.hasReceived;
    }

    public void setHasReceived(boolean hasReceived) {
        this.hasReceived = hasReceived;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("type", this.type);
            jsonObj.put("extension", this.extension);
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("DiscussionNotificationMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
