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
        value = "RC:CsEva",
        flag = 0
)
public class CSEvaluateMessage extends MessageContent {
    private static final String TAG = "CSEvaluateMessage";
    private String uid;
    private String sid;
    private String pid;
    private int source;
    private String suggest;
    private int status;
    private int type;
    private String tablets;
    private String extra;
    public static final Creator<CSEvaluateMessage> CREATOR = new Creator<CSEvaluateMessage>() {
        public CSEvaluateMessage createFromParcel(Parcel source) {
            return new CSEvaluateMessage(source);
        }

        public CSEvaluateMessage[] newArray(int size) {
            return new CSEvaluateMessage[size];
        }
    };

    private CSEvaluateMessage() {
        this.status = -1;
    }

    public CSEvaluateMessage(byte[] data) {
        this.status = -1;
    }

    public CSEvaluateMessage(Parcel in) {
        this.status = -1;
        this.sid = ParcelUtils.readFromParcel(in);
        this.uid = ParcelUtils.readFromParcel(in);
        this.pid = ParcelUtils.readFromParcel(in);
        this.source = ParcelUtils.readIntFromParcel(in);
        this.suggest = ParcelUtils.readFromParcel(in);
        this.status = ParcelUtils.readIntFromParcel(in);
        this.type = ParcelUtils.readIntFromParcel(in);
        this.tablets = ParcelUtils.readFromParcel(in);
        this.extra = ParcelUtils.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.sid);
        ParcelUtils.writeToParcel(dest, this.uid);
        ParcelUtils.writeToParcel(dest, this.pid);
        ParcelUtils.writeToParcel(dest, this.source);
        ParcelUtils.writeToParcel(dest, this.suggest);
        ParcelUtils.writeToParcel(dest, this.status);
        ParcelUtils.writeToParcel(dest, this.type);
        ParcelUtils.writeToParcel(dest, this.tablets);
        ParcelUtils.writeToParcel(dest, this.extra);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("uid", this.uid);
            jsonObj.put("sid", this.sid);
            jsonObj.put("pid", this.pid);
            jsonObj.put("source", this.source);
            jsonObj.put("suggest", this.suggest);
            jsonObj.put("isresolve", this.status);
            jsonObj.put("type", this.type);
            jsonObj.put("tag", this.tablets);
        } catch (JSONException var4) {
            RLog.e("CSEvaluateMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("CSEvaluateMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public static class Builder {
        private String uid;
        private String sid;
        private String pid;
        private int source;
        private String suggest;
        private int solveStatus = -1;
        private int type;
        private String tablets;
        private String extra;

        public Builder() {
        }

        public CSEvaluateMessage build() {
            CSEvaluateMessage message = new CSEvaluateMessage();
            message.sid = this.sid;
            message.pid = this.pid;
            message.uid = this.uid;
            message.source = this.source;
            message.suggest = this.suggest;
            message.status = this.solveStatus;
            message.type = this.type;
            message.tablets = this.tablets;
            message.extra = this.extra;
            return message;
        }

        public CSEvaluateMessage.Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public CSEvaluateMessage.Builder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public CSEvaluateMessage.Builder pid(String pid) {
            this.pid = pid;
            return this;
        }

        public CSEvaluateMessage.Builder source(int source) {
            this.source = source;
            return this;
        }

        public CSEvaluateMessage.Builder suggest(String suggest) {
            this.suggest = suggest;
            return this;
        }

        public CSEvaluateMessage.Builder setSolveStatus(int status) {
            this.solveStatus = status;
            return this;
        }

        public CSEvaluateMessage.Builder type(int type) {
            this.type = type;
            return this;
        }

        public CSEvaluateMessage.Builder tablets(String tablets) {
            this.tablets = tablets;
            return this;
        }

        public CSEvaluateMessage.Builder extra(String extra) {
            this.extra = extra;
            return this;
        }
    }
}
