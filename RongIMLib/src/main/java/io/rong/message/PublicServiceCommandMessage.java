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
import io.rong.imlib.model.PublicServiceMenuItem;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:PSCmd",
        flag = 0
)
public class PublicServiceCommandMessage extends MessageContent {
    private static final String TAG = "PublicServiceCommandMessage";
    private String command;
    private String data;
    protected String extra;
    public static final Creator<PublicServiceCommandMessage> CREATOR = new Creator<PublicServiceCommandMessage>() {
        public PublicServiceCommandMessage createFromParcel(Parcel source) {
            return new PublicServiceCommandMessage(source);
        }

        public PublicServiceCommandMessage[] newArray(int size) {
            return new PublicServiceCommandMessage[size];
        }
    };

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("cmd", this.command);
            jsonObj.put("data", this.data);
            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("PublicServiceCommandMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("PublicServiceCommandMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public PublicServiceCommandMessage() {
    }

    public PublicServiceCommandMessage(byte[] data) {
    }

    public static PublicServiceCommandMessage obtain(PublicServiceMenuItem item) {
        PublicServiceCommandMessage model = new PublicServiceCommandMessage();
        if (item.getType() != null) {
            model.command = item.getType().getMessage();
            model.data = item.getId();
        }

        return model;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.command);
        ParcelUtils.writeToParcel(dest, this.data);
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public PublicServiceCommandMessage(Parcel in) {
        this.command = ParcelUtils.readFromParcel(in);
        this.data = ParcelUtils.readFromParcel(in);
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }
}
