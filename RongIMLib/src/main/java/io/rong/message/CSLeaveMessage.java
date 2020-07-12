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
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:CsLM",
        flag = 0
)
public class CSLeaveMessage extends MessageContent {
    private static final String TAG = "CSLeaveMessage";
    private Map<String, String> dataMap;
    public static final Creator<CSLeaveMessage> CREATOR = new Creator<CSLeaveMessage>() {
        public CSLeaveMessage createFromParcel(Parcel source) {
            return new CSLeaveMessage(source);
        }

        public CSLeaveMessage[] newArray(int size) {
            return new CSLeaveMessage[size];
        }
    };

    public void setDataSet(Map<String, String> sourceData) {
        this.dataMap = sourceData;
    }

    public CSLeaveMessage(byte[] data) {
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        Iterator it = this.dataMap.keySet().iterator();

        try {
            while(it.hasNext()) {
                String key = (String)it.next();
                jsonObj.put(key, this.dataMap.get(key));
            }
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var4) {
            RLog.e("CSLeaveMessage", "UnsupportedEncodingException ", var4);
            return new byte[0];
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.dataMap);
    }

    public CSLeaveMessage() {
    }

    protected CSLeaveMessage(Parcel in) {
        this.setDataSet(ParcelUtils.readMapFromParcel(in));
    }
}
