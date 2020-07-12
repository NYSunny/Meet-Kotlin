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
        value = "RC:CmdNtf",
        flag = 1
)
public class CommandNotificationMessage extends MessageContent {
    private static final String TAG = "CommandNotificationMessage";
    private String name;
    private String data;
    public static final Creator<CommandNotificationMessage> CREATOR = new Creator<CommandNotificationMessage>() {
        public CommandNotificationMessage createFromParcel(Parcel source) {
            return new CommandNotificationMessage(source);
        }

        public CommandNotificationMessage[] newArray(int size) {
            return new CommandNotificationMessage[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public CommandNotificationMessage(Parcel in) {
        this.name = ParcelUtils.readFromParcel(in);
        this.data = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static CommandNotificationMessage obtain(String name, String data) {
        CommandNotificationMessage obj = new CommandNotificationMessage();
        obj.name = name;
        obj.data = data;
        return obj;
    }

    private CommandNotificationMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("name", this.name);
            if (!TextUtils.isEmpty(this.data)) {
                jsonObj.put("data", this.data);
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("CommandNotificationMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CommandNotificationMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public CommandNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CommandNotificationMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setName(jsonObj.optString("name"));
            this.setData(jsonObj.optString("data"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("CommandNotificationMessage", "JSONException " + var4.getMessage());
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.name);
        ParcelUtils.writeToParcel(dest, this.data);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public int describeContents() {
        return 0;
    }
}
