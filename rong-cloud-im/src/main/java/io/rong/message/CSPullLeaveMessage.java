//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:CsPLM",
        flag = 1
)
public class CSPullLeaveMessage extends MessageContent {
    private static final String TAG = "CSRequestLeaveMessage";
    private String content;
    public static final Creator<CSPullLeaveMessage> CREATOR = new Creator<CSPullLeaveMessage>() {
        public CSPullLeaveMessage createFromParcel(Parcel source) {
            return new CSPullLeaveMessage(source);
        }

        public CSPullLeaveMessage[] newArray(int size) {
            return new CSPullLeaveMessage[size];
        }
    };

    public CSPullLeaveMessage() {
    }

    public CSPullLeaveMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CSRequestLeaveMessage", "UnsupportedEncodingException ", var5);
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            this.content = jsonObject.optString("content");
        } catch (JSONException var4) {
            RLog.e("CSRequestLeaveMessage", "JSONException " + var4.getMessage());
        }

    }

    public byte[] encode() {
        return new byte[0];
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    protected CSPullLeaveMessage(Parcel in) {
        this.content = in.readString();
    }
}
