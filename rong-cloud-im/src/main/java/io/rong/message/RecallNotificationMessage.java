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
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:RcNtf",
        flag = 1
)
public class RecallNotificationMessage extends MessageContent {
    private static final String TAG = "RecallNotificationMessage";
    private String mOperatorId;
    private long mRecallTime;
    private String mOriginalObjectName;
    private boolean mAdmin;
    private boolean mDelete;
    private String recallContent;
    private long recallActionTime;
    public static final Creator<RecallNotificationMessage> CREATOR = new Creator<RecallNotificationMessage>() {
        public RecallNotificationMessage createFromParcel(Parcel source) {
            return new RecallNotificationMessage(source);
        }

        public RecallNotificationMessage[] newArray(int size) {
            return new RecallNotificationMessage[size];
        }
    };

    public String getRecallContent() {
        return this.recallContent;
    }

    public void setRecallContent(String recallContent) {
        this.recallContent = recallContent;
    }

    public long getRecallActionTime() {
        return this.recallActionTime;
    }

    public void setRecallActionTime(long recallActionTime) {
        this.recallActionTime = recallActionTime;
    }

    public String getOperatorId() {
        return this.mOperatorId;
    }

    public long getRecallTime() {
        return this.mRecallTime;
    }

    public String getOriginalObjectName() {
        return this.mOriginalObjectName;
    }

    public boolean isAdmin() {
        return this.mAdmin;
    }

    public boolean isDelete() {
        return this.mDelete;
    }

    public RecallNotificationMessage(String operatorId, long recallTime, String originalObjectName, boolean isAdmin, boolean isDelete) {
        this.mOperatorId = operatorId;
        this.mRecallTime = recallTime;
        this.mOriginalObjectName = originalObjectName;
        this.mDelete = isDelete;
        this.mAdmin = isAdmin;
    }

    public RecallNotificationMessage(String operatorId, long recallTime, String originalObjectName, boolean isAdmin, boolean isDelete, String recallContent, long recallActionTime) {
        this.mOperatorId = operatorId;
        this.mRecallTime = recallTime;
        this.mOriginalObjectName = originalObjectName;
        this.mDelete = isDelete;
        this.mAdmin = isAdmin;
        this.recallContent = recallContent;
        this.recallActionTime = recallActionTime;
    }

    public RecallNotificationMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("RecallNotificationMessage", var5.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            if (jsonObj.has("operatorId")) {
                this.mOperatorId = jsonObj.getString("operatorId");
            }

            if (jsonObj.has("recallTime")) {
                this.mRecallTime = jsonObj.getLong("recallTime");
            }

            if (jsonObj.has("originalObjectName")) {
                this.mOriginalObjectName = jsonObj.getString("originalObjectName");
            }

            if (jsonObj.has("recallContent")) {
                this.recallContent = jsonObj.getString("recallContent");
            }

            if (jsonObj.has("recallActionTime")) {
                this.recallActionTime = jsonObj.getLong("recallActionTime");
            }

            this.mAdmin = jsonObj.getBoolean("admin");
            this.mDelete = jsonObj.getBoolean("delete");
        } catch (JSONException var4) {
            RLog.e("RecallNotificationMessage", var4.getMessage());
        }

    }

    public RecallNotificationMessage(Parcel in) {
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.mOperatorId = ParcelUtils.readFromParcel(in);
        this.mRecallTime = ParcelUtils.readLongFromParcel(in);
        this.mOriginalObjectName = ParcelUtils.readFromParcel(in);
        this.mAdmin = ParcelUtils.readIntFromParcel(in) == 1;
        this.mDelete = ParcelUtils.readIntFromParcel(in) == 1;
        this.recallContent = ParcelUtils.readFromParcel(in);
        this.recallActionTime = ParcelUtils.readLongFromParcel(in);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if (!TextUtils.isEmpty(this.getOperatorId())) {
                jsonObj.put("operatorId", this.getOperatorId());
            }

            jsonObj.put("recallTime", this.getRecallTime());
            if (!TextUtils.isEmpty(this.getOriginalObjectName())) {
                jsonObj.put("originalObjectName", this.getOriginalObjectName());
            }

            if (!TextUtils.isEmpty(this.getRecallContent())) {
                jsonObj.put("recallContent", this.getRecallContent());
            }

            jsonObj.put("recallActionTime", this.getRecallActionTime());
            jsonObj.put("admin", this.isAdmin());
            jsonObj.put("delete", this.isDelete());
        } catch (JSONException var4) {
            RLog.e("RecallNotificationMessage", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("RecallNotificationMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getOperatorId());
        ParcelUtils.writeToParcel(dest, this.getRecallTime());
        ParcelUtils.writeToParcel(dest, this.getOriginalObjectName());
        ParcelUtils.writeToParcel(dest, this.isAdmin() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.isDelete() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.getRecallContent());
        ParcelUtils.writeToParcel(dest, this.getRecallActionTime());
    }
}
