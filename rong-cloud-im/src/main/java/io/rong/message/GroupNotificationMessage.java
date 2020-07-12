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
        value = "RC:GrpNtf",
        flag = 1
)
public class GroupNotificationMessage extends MessageContent {
    private static final String TAG = "GroupNotificationMessage";
    public static final String GROUP_OPERATION_CREATE = "Create";
    public static final String GROUP_OPERATION_ADD = "Add";
    public static final String GROUP_OPERATION_DISMISS = "Dismiss";
    public static final String GROUP_OPERATION_QUIT = "Quit";
    public static final String GROUP_OPERATION_KICKED = "Kicked";
    public static final String GROUP_OPERATION_RENAME = "Rename";
    public static final String GROUP_OPERATION_BULLETIN = "Bulletin";
    private String operatorUserId;
    private String operation;
    private String data;
    private String message;
    private String extra;
    public static final Creator<GroupNotificationMessage> CREATOR = new Creator<GroupNotificationMessage>() {
        public GroupNotificationMessage createFromParcel(Parcel source) {
            return new GroupNotificationMessage(source);
        }

        public GroupNotificationMessage[] newArray(int size) {
            return new GroupNotificationMessage[size];
        }
    };

    public String getOperatorUserId() {
        return this.operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
    }

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

    public GroupNotificationMessage(Parcel in) {
        this.operatorUserId = ParcelUtils.readFromParcel(in);
        this.operation = ParcelUtils.readFromParcel(in);
        this.data = ParcelUtils.readFromParcel(in);
        this.message = ParcelUtils.readFromParcel(in);
        this.extra = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static GroupNotificationMessage obtain(String operatorUserId, String operation, String data, String message) {
        GroupNotificationMessage obj = new GroupNotificationMessage();
        obj.operatorUserId = operatorUserId;
        obj.operation = operation;
        obj.data = data;
        obj.message = message;
        return obj;
    }

    private GroupNotificationMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("operatorUserId", this.operatorUserId);
            jsonObj.put("operation", this.operation);
            if (!TextUtils.isEmpty(this.data)) {
                jsonObj.put("data", this.data);
            }

            if (!TextUtils.isEmpty(this.message)) {
                jsonObj.put("message", this.message);
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("GroupNotificationMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("GroupNotificationMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public GroupNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("GroupNotificationMessage", "UnsupportedEncodingException ", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setOperatorUserId(jsonObj.optString("operatorUserId"));
            this.setOperation(jsonObj.optString("operation"));
            this.setData(jsonObj.optString("data"));
            this.setMessage(jsonObj.optString("message"));
            this.setExtra(jsonObj.optString("extra"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("GroupNotificationMessage", "JSONException " + var4.getMessage());
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.operatorUserId);
        ParcelUtils.writeToParcel(dest, this.operation);
        ParcelUtils.writeToParcel(dest, this.data);
        ParcelUtils.writeToParcel(dest, this.message);
        ParcelUtils.writeToParcel(dest, this.extra);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public int describeContents() {
        return 0;
    }
}
