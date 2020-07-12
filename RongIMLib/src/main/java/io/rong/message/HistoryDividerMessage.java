//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:HDiv",
        flag = 0
)
public class HistoryDividerMessage extends MessageContent {
    private static final String TAG = "NewMessageDivider";
    private String content;
    public static final Creator<HistoryDividerMessage> CREATOR = new Creator<HistoryDividerMessage>() {
        public HistoryDividerMessage createFromParcel(Parcel source) {
            return new HistoryDividerMessage(source);
        }

        public HistoryDividerMessage[] newArray(int size) {
            return new HistoryDividerMessage[size];
        }
    };

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HistoryDividerMessage(Parcel in) {
        this.content = ParcelUtils.readFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public static HistoryDividerMessage obtain(String content) {
        HistoryDividerMessage obj = new HistoryDividerMessage();
        obj.content = content;
        return obj;
    }

    private HistoryDividerMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", this.content);
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("NewMessageDivider", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("NewMessageDivider", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public HistoryDividerMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("NewMessageDivider", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setContent(jsonObj.optString("content"));
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("NewMessageDivider", "JSONException " + var4.getMessage());
        }

    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.content);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public int describeContents() {
        return 0;
    }
}
