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
        value = "RC:CsCha",
        flag = 0
)
public class CSChangeModeMessage extends MessageContent {
    private static final String TAG = "CSChangeModeMessage";
    private String uid;
    private String sid;
    private String pid;
    private String groupid;
    public static final Creator<CSChangeModeMessage> CREATOR = new Creator<CSChangeModeMessage>() {
        public CSChangeModeMessage createFromParcel(Parcel source) {
            return new CSChangeModeMessage(source);
        }

        public CSChangeModeMessage[] newArray(int size) {
            return new CSChangeModeMessage[size];
        }
    };

    public CSChangeModeMessage() {
    }

    public CSChangeModeMessage(byte[] data) {
    }

    public static CSChangeModeMessage obtain(String sid, String uid, String pid, String groupid) {
        CSChangeModeMessage message = new CSChangeModeMessage();
        message.sid = sid;
        message.uid = uid;
        message.pid = pid;
        message.groupid = groupid;
        return message;
    }

    public CSChangeModeMessage(Parcel in) {
        this.sid = ParcelUtils.readFromParcel(in);
        this.uid = ParcelUtils.readFromParcel(in);
        this.pid = ParcelUtils.readFromParcel(in);
        this.groupid = ParcelUtils.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.sid);
        ParcelUtils.writeToParcel(dest, this.uid);
        ParcelUtils.writeToParcel(dest, this.pid);
        ParcelUtils.writeToParcel(dest, this.groupid);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("uid", this.uid);
            jsonObj.put("sid", this.sid);
            jsonObj.put("pid", this.pid);
            if (!TextUtils.isEmpty(this.groupid)) {
                jsonObj.put("groupid", this.groupid);
            }
        } catch (JSONException var4) {
            RLog.e("CSChangeModeMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CSChangeModeMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }
}
