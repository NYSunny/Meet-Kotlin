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
        value = "RC:CsUpdate",
        flag = 0
)
public class CSUpdateMessage extends MessageContent {
    private static final String TAG = "CSUpdateMessage";
    private String sid;
    private String serviceStatus;
    public static final Creator<CSUpdateMessage> CREATOR = new Creator<CSUpdateMessage>() {
        public CSUpdateMessage createFromParcel(Parcel source) {
            return new CSUpdateMessage(source);
        }

        public CSUpdateMessage[] newArray(int size) {
            return new CSUpdateMessage[size];
        }
    };

    public CSUpdateMessage() {
    }

    public String getSid() {
        return this.sid;
    }

    public String getServiceStatus() {
        return this.serviceStatus;
    }

    public CSUpdateMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CSUpdateMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.sid = jsonObj.optString("sid");
            this.serviceStatus = jsonObj.optString("serviceStatus");
        } catch (JSONException var4) {
            RLog.e("CSUpdateMessage", "JSONException " + var4.getMessage());
        }

    }

    public static CSUpdateMessage obtain(String sid, String serviceStatus) {
        CSUpdateMessage message = new CSUpdateMessage();
        message.sid = sid;
        message.serviceStatus = serviceStatus;
        return message;
    }

    public CSUpdateMessage(Parcel in) {
        this.sid = ParcelUtils.readFromParcel(in);
        this.serviceStatus = ParcelUtils.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.sid);
        ParcelUtils.writeToParcel(dest, this.serviceStatus);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("sid", this.sid);
            jsonObj.put("serviceStatus", this.serviceStatus);
        } catch (JSONException var4) {
            RLog.e("CSUpdateMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CSUpdateMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }
}
