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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:CsChaR",
        flag = 0
)
public class CSChangeModeResponseMessage extends MessageContent {
    private static final String TAG = "CSChangeModeResponseMessage";
    private int code;
    private int status;
    private String errMsg;
    private String evaluation;
    public static final Creator<CSChangeModeResponseMessage> CREATOR = new Creator<CSChangeModeResponseMessage>() {
        public CSChangeModeResponseMessage createFromParcel(Parcel source) {
            return new CSChangeModeResponseMessage(source);
        }

        public CSChangeModeResponseMessage[] newArray(int size) {
            return new CSChangeModeResponseMessage[size];
        }
    };

    public CSChangeModeResponseMessage() {
    }

    public int getResult() {
        return this.code;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public int getStatus() {
        return this.status;
    }

    public String getEvaluation() {
        return this.evaluation;
    }

    public CSChangeModeResponseMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var8) {
            RLog.e("CSChangeModeResponseMessage", "UnsupportedEncodingException ", var8);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.code = jsonObj.optInt("code");
            this.errMsg = jsonObj.optString("msg");
            if (jsonObj.has("data")) {
                jsonObj = jsonObj.getJSONObject("data");
                this.status = jsonObj.getInt("status");
                if (jsonObj.has("satisfaction")) {
                    JSONArray jsonArray = jsonObj.getJSONArray("satisfaction");
                    JSONObject evaJsonObject = new JSONObject();
                    JSONObject satisJsonObject = new JSONObject();
                    satisJsonObject.put("satisfaction", jsonArray);
                    evaJsonObject.put("evaluation", satisJsonObject);
                    this.evaluation = evaJsonObject.toString();
                }
            }
        } catch (JSONException var7) {
            var7.printStackTrace();
        }

    }

    public static CSChangeModeResponseMessage obtain() {
        return new CSChangeModeResponseMessage();
    }

    public CSChangeModeResponseMessage(Parcel in) {
        this.code = ParcelUtils.readIntFromParcel(in);
        this.status = ParcelUtils.readIntFromParcel(in);
        this.errMsg = ParcelUtils.readFromParcel(in);
        this.evaluation = ParcelUtils.readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.code);
        ParcelUtils.writeToParcel(dest, this.status);
        ParcelUtils.writeToParcel(dest, this.errMsg);
        ParcelUtils.writeToParcel(dest, this.evaluation);
    }

    public byte[] encode() {
        return null;
    }
}
