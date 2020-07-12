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
        value = "RC:CsSp",
        flag = 0
)
public class CSSuspendMessage extends MessageContent {
    private static final String TAG = "CSSuspendMessage";
    private String uid;
    private String sid;
    private String pid;
    public static final Creator<CSSuspendMessage> CREATOR = new Creator<CSSuspendMessage>() {
        public CSSuspendMessage createFromParcel(Parcel source) {
            return new CSSuspendMessage(source);
        }

        public CSSuspendMessage[] newArray(int size) {
            return new CSSuspendMessage[size];
        }
    };

    public CSSuspendMessage() {
    }

    public CSSuspendMessage(byte[] data) {
    }

    public static CSSuspendMessage obtain(String sid, String uid, String pid) {
        CSSuspendMessage message = new CSSuspendMessage();
        message.sid = sid;
        message.uid = uid;
        message.pid = pid;
        return message;
    }

    public CSSuspendMessage(Parcel in) {
        this.sid = ParcelUtils.readFromParcel(in);
        this.uid = ParcelUtils.readFromParcel(in);
        this.pid = ParcelUtils.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.sid);
        ParcelUtils.writeToParcel(dest, this.uid);
        ParcelUtils.writeToParcel(dest, this.pid);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("uid", this.uid);
            jsonObj.put("sid", this.sid);
            jsonObj.put("pid", this.pid);
        } catch (JSONException var4) {
            RLog.e("CSSuspendMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CSSuspendMessage", "UnsupportedEncodingException " + var3);
            return null;
        }
    }
}
