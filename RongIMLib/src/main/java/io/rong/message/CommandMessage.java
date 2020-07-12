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
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:CmdMsg",
        flag = 0
)
public class CommandMessage extends NotificationMessage {
    private static final String TAG = "CommandMessage";
    private String name;
    private String data;
    public static final Creator<CommandMessage> CREATOR = new Creator<CommandMessage>() {
        public CommandMessage createFromParcel(Parcel source) {
            return new CommandMessage(source);
        }

        public CommandMessage[] newArray(int size) {
            return new CommandMessage[size];
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

    public CommandMessage(Parcel in) {
        this.name = ParcelUtils.readFromParcel(in);
        this.data = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static CommandMessage obtain(String name, String data) {
        CommandMessage obj = new CommandMessage();
        obj.name = name;
        obj.data = data;
        return obj;
    }

    private CommandMessage() {
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
            RLog.e("CommandMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CommandMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public CommandMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CommandMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setName(jsonObj.optString("name"));
            this.setData(jsonObj.optString("data"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("CommandMessage", "JSONException " + var4.getMessage());
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
