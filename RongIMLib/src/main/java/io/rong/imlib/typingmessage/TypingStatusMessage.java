//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.typingmessage;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.message.StatusMessage;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:TypSts",
        flag = 16
)
public class TypingStatusMessage extends StatusMessage {
    private static final String TAG = "TypingStatusMessage";
    private String typingContentType;
    private String data;
    public static final Creator<TypingStatusMessage> CREATOR = new Creator<TypingStatusMessage>() {
        public TypingStatusMessage createFromParcel(Parcel source) {
            return new TypingStatusMessage(source);
        }

        public TypingStatusMessage[] newArray(int size) {
            return new TypingStatusMessage[size];
        }
    };

    public String getTypingContentType() {
        return this.typingContentType;
    }

    public void setTypingContentType(String typingContentType) {
        this.typingContentType = typingContentType;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public TypingStatusMessage(String type, String data) {
        this.setTypingContentType(type);
        this.setData(data);
    }

    public TypingStatusMessage(byte[] data) {
        String jsonStr = null;
        if (data != null && data.length != 0) {
            try {
                jsonStr = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException var5) {
                RLog.e("TypingStatusMessage", "TypingStatusMessage " + var5.getMessage());
            }

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj.has("typingContentType")) {
                    this.setTypingContentType(jsonObj.getString("typingContentType"));
                }

                if (jsonObj.has("data")) {
                    this.setData(jsonObj.getString("data"));
                }
            } catch (JSONException var4) {
                RLog.e("TypingStatusMessage", "TypingStatusMessage " + var4.getMessage());
            }

        }
    }

    public TypingStatusMessage(Parcel in) {
        this.setTypingContentType(ParcelUtils.readFromParcel(in));
        this.setData(ParcelUtils.readFromParcel(in));
    }

    public TypingStatusMessage() {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("typingContentType", this.getTypingContentType());
            if (!TextUtils.isEmpty(this.getData())) {
                jsonObj.put("data", this.getData());
            }
        } catch (JSONException var4) {
            RLog.e("TypingStatusMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("TypingStatusMessage", "encode", var3);
            return new byte[0];
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getTypingContentType());
        ParcelUtils.writeToParcel(dest, this.getData());
    }
}
