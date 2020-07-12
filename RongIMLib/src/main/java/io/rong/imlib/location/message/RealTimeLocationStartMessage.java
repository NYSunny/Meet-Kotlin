//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:RLStart",
        flag = 3
)
public class RealTimeLocationStartMessage extends MessageContent {
    private static final String TAG = "RealTimeLocationStartMessage";
    private String content;
    private String extra;
    public static final Creator<RealTimeLocationStartMessage> CREATOR = new Creator<RealTimeLocationStartMessage>() {
        public RealTimeLocationStartMessage createFromParcel(Parcel source) {
            return new RealTimeLocationStartMessage(source);
        }

        public RealTimeLocationStartMessage[] newArray(int size) {
            return new RealTimeLocationStartMessage[size];
        }
    };

    public RealTimeLocationStartMessage(String content) {
        this.content = "";
        this.extra = "";
        this.content = content;
    }

    public RealTimeLocationStartMessage(byte[] data) {
        this.content = "";
        this.extra = "";
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("RealTimeLocationStartMessage", "RealTimeLocationStartMessage", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("content")) {
                this.content = jsonObj.optString("content");
            }

            if (jsonObj.has("extra")) {
                this.extra = jsonObj.getString("extra");
            }
        } catch (JSONException var4) {
            RLog.e("RealTimeLocationStartMessage", "JSONException " + var4.getMessage());
        }

    }

    public static RealTimeLocationStartMessage obtain(String content) {
        return new RealTimeLocationStartMessage(content);
    }

    public String getContent() {
        return this.content;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    private RealTimeLocationStartMessage(Parcel in) {
        this.content = "";
        this.extra = "";
        this.content = in.readString();
        this.extra = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.extra);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", this.content);
            if (this.extra != null) {
                jsonObj.put("extra", this.extra);
            }
        } catch (JSONException var4) {
            RLog.e("RealTimeLocationStartMessage", "encode JSONException", var4);
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("RealTimeLocationStartMessage", "encode JSONException", var3);
            return new byte[0];
        }
    }
}
