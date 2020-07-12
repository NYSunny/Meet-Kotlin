//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;

public class ReadReceiptInfo implements Parcelable {
    private static final String TAG = "ReadReceiptInfo";
    public static final Creator<ReadReceiptInfo> CREATOR = new Creator<ReadReceiptInfo>() {
        public ReadReceiptInfo createFromParcel(Parcel source) {
            return new ReadReceiptInfo(source);
        }

        public ReadReceiptInfo[] newArray(int size) {
            return new ReadReceiptInfo[size];
        }
    };
    private boolean isReadReceiptMessage;
    private boolean hasRespond;
    private HashMap<String, Long> respondUserIdList = new HashMap();

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("isReceiptRequestMessage", this.isReadReceiptMessage);
            jsonObject.put("hasRespond", this.hasRespond);
            if (this.respondUserIdList != null && this.respondUserIdList.size() > 0) {
                JSONObject userObject = new JSONObject();
                Iterator var3 = this.respondUserIdList.entrySet().iterator();

                while(var3.hasNext()) {
                    Entry entry = (Entry)var3.next();
                    userObject.put((String)entry.getKey(), (Long)entry.getValue());
                }

                jsonObject.put("userIdList", userObject);
            }
        } catch (JSONException var5) {
            RLog.e("ReadReceiptInfo", var5.getMessage());
        }

        return jsonObject;
    }

    public ReadReceiptInfo() {
    }

    public ReadReceiptInfo(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (jsonObject.has("isReceiptRequestMessage")) {
                    this.isReadReceiptMessage = jsonObject.optBoolean("isReceiptRequestMessage");
                }

                if (jsonObject.has("hasRespond")) {
                    this.hasRespond = jsonObject.optBoolean("hasRespond");
                }

                if (jsonObject.has("userIdList")) {
                    JSONObject userListObject = jsonObject.getJSONObject("userIdList");
                    HashMap<String, Long> map = new HashMap();
                    Iterator it = userListObject.keys();

                    while(it.hasNext()) {
                        String key = (String)it.next();
                        long value = (Long)userListObject.get(key);
                        map.put(key, value);
                    }

                    this.respondUserIdList = map;
                }
            } catch (JSONException var9) {
                RLog.e("ReadReceiptInfo", var9.getMessage());
            }

        }
    }

    public boolean isReadReceiptMessage() {
        return this.isReadReceiptMessage;
    }

    public void setIsReadReceiptMessage(boolean isReadReceiptMessage) {
        this.isReadReceiptMessage = isReadReceiptMessage;
    }

    public boolean hasRespond() {
        return this.hasRespond;
    }

    public void setHasRespond(boolean hasRespond) {
        this.hasRespond = hasRespond;
    }

    public HashMap<String, Long> getRespondUserIdList() {
        return this.respondUserIdList;
    }

    public void setRespondUserIdList(HashMap<String, Long> respondUserIdList) {
        this.respondUserIdList = respondUserIdList;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.isReadReceiptMessage ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.hasRespond ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.respondUserIdList);
    }

    public ReadReceiptInfo(Parcel in) {
        this.setIsReadReceiptMessage(ParcelUtils.readIntFromParcel(in) == 1);
        this.setHasRespond(ParcelUtils.readIntFromParcel(in) == 1);
        this.setRespondUserIdList((HashMap)ParcelUtils.readMapFromParcel(in));
    }
}
