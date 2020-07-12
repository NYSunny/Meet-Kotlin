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
        value = "RC:CsPullEva",
        flag = 0
)
public class CSPullEvaluateMessage extends MessageContent {
    private static final String TAG = "CSPullEvaluateMessage";
    private String msgId;
    public static final Creator<CSPullEvaluateMessage> CREATOR = new Creator<CSPullEvaluateMessage>() {
        public CSPullEvaluateMessage createFromParcel(Parcel source) {
            return new CSPullEvaluateMessage(source);
        }

        public CSPullEvaluateMessage[] newArray(int size) {
            return new CSPullEvaluateMessage[size];
        }
    };

    public CSPullEvaluateMessage() {
    }

    public CSPullEvaluateMessage(byte[] content) {
        String jsonStr = null;

        try {
            jsonStr = new String(content, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("CSPullEvaluateMessage", "UnsupportedEncodingException ", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.msgId = jsonObj.getString("msgId");
        } catch (JSONException var4) {
            RLog.e("CSPullEvaluateMessage", "JSONException" + var4.getMessage());
        }

    }

    public static CSPullEvaluateMessage obtain() {
        return new CSPullEvaluateMessage();
    }

    public CSPullEvaluateMessage(Parcel in) {
        this.msgId = ParcelUtils.readFromParcel(in);
    }

    public String getMsgId() {
        return this.msgId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.msgId);
    }

    public byte[] encode() {
        return null;
    }
}
