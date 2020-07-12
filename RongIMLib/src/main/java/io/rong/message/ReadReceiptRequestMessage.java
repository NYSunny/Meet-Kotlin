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
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:RRReqMsg",
        flag = 0
)
public class ReadReceiptRequestMessage extends MessageContent {
    private static final String TAG = "ReadReceiptRequestMessage";
    private String mMessageUId;
    public static final Creator<ReadReceiptRequestMessage> CREATOR = new Creator<ReadReceiptRequestMessage>() {
        public ReadReceiptRequestMessage createFromParcel(Parcel source) {
            return new ReadReceiptRequestMessage(source);
        }

        public ReadReceiptRequestMessage[] newArray(int size) {
            return new ReadReceiptRequestMessage[size];
        }
    };

    public String getMessageUId() {
        return this.mMessageUId;
    }

    public ReadReceiptRequestMessage(String uId) {
        this.mMessageUId = uId;
    }

    public ReadReceiptRequestMessage(Parcel in) {
        this.mMessageUId = ParcelUtils.readFromParcel(in);
    }

    public ReadReceiptRequestMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("ReadReceiptRequestMessage", var5.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("messageUId")) {
                this.mMessageUId = jsonObj.getString("messageUId");
            }
        } catch (JSONException var4) {
            RLog.e("ReadReceiptRequestMessage", var4.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.mMessageUId)) {
                jsonObj.put("messageUId", this.mMessageUId);
            }
        } catch (JSONException var4) {
            RLog.e("ReadReceiptRequestMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("ReadReceiptRequestMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.mMessageUId);
    }
}
