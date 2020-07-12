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
        value = "RC:CsEnd",
        flag = 0
)
public class CSTerminateMessage extends MessageContent {
    private static final String TAG = "CSTerminateMessage";
    private int code;
    private String msg;
    private String sid;
    public static final Creator<CSTerminateMessage> CREATOR = new Creator<CSTerminateMessage>() {
        public CSTerminateMessage createFromParcel(Parcel source) {
            return new CSTerminateMessage(source);
        }

        public CSTerminateMessage[] newArray(int size) {
            return new CSTerminateMessage[size];
        }
    };

    public CSTerminateMessage() {
    }

    public CSTerminateMessage(byte[] content) {
        String jsonStr = null;

        try {
            jsonStr = new String(content, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CSTerminateMessage", "UnsupportedEncodingException ", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.code = jsonObj.optInt("code");
            this.msg = jsonObj.optString("msg");
            this.sid = jsonObj.optString("sid");
        } catch (JSONException var4) {
            RLog.e("CSTerminateMessage", "JSONException " + var4.getMessage());
        }

    }

    public static CSTerminateMessage obtain() {
        return new CSTerminateMessage();
    }

    public CSTerminateMessage(Parcel in) {
        this.code = ParcelUtils.readIntFromParcel(in);
        this.msg = ParcelUtils.readFromParcel(in);
        this.sid = ParcelUtils.readFromParcel(in);
    }

    public String getsid() {
        return this.sid;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.code);
        ParcelUtils.writeToParcel(dest, this.msg);
        ParcelUtils.writeToParcel(dest, this.sid);
    }

    public byte[] encode() {
        return null;
    }
}
