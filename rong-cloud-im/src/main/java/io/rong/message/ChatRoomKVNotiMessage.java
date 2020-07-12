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
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:chrmKVNotiMsg",
        flag = 0
)
public class ChatRoomKVNotiMessage extends MessageContent {
    public static final int TYPE_SET_KEY = 1;
    public static final int TYPE_DELETE_KEY = 2;
    private static final String TAG = "ChatRoomKVNotiMessage";
    private String key;
    private String value;
    private String extra;
    private int type;
    public static final Creator<ChatRoomKVNotiMessage> CREATOR = new Creator<ChatRoomKVNotiMessage>() {
        public ChatRoomKVNotiMessage createFromParcel(Parcel source) {
            return new ChatRoomKVNotiMessage(source);
        }

        public ChatRoomKVNotiMessage[] newArray(int size) {
            return new ChatRoomKVNotiMessage[size];
        }
    };

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static String getTAG() {
        return "ChatRoomKVNotiMessage";
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public ChatRoomKVNotiMessage(Parcel in) {
        this.key = ParcelUtils.readFromParcel(in);
        this.value = ParcelUtils.readFromParcel(in);
        this.extra = ParcelUtils.readFromParcel(in);
        this.type = ParcelUtils.readIntFromParcel(in);
    }

    public ChatRoomKVNotiMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("ChatRoomKVNotiMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.setKey(jsonObj.optString("key"));
            this.setValue(jsonObj.optString("value"));
            this.setExtra(jsonObj.optString("extra"));
            this.setType(jsonObj.optInt("type"));
        } catch (JSONException var4) {
            RLog.e("ChatRoomKVNotiMessage", "JSONException " + var4.getMessage());
        }

    }

    public static ChatRoomKVNotiMessage obtain(String key, String value, int type, String extra) {
        ChatRoomKVNotiMessage obj = new ChatRoomKVNotiMessage();
        obj.key = key;
        obj.value = value;
        obj.extra = extra;
        obj.type = type;
        return obj;
    }

    private ChatRoomKVNotiMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("key", this.key);
            jsonObj.put("value", this.value);
            jsonObj.put("extra", this.extra);
            jsonObj.put("type", this.type);
        } catch (JSONException var4) {
            RLog.e("ChatRoomKVNotiMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("ChatRoomKVNotiMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.key);
        ParcelUtils.writeToParcel(dest, this.value);
        ParcelUtils.writeToParcel(dest, this.extra);
        ParcelUtils.writeToParcel(dest, this.type);
    }
}
