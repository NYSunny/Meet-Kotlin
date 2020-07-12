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
        value = "RC:RcCmd",
        flag = 0
)
public class RecallCommandMessage extends MessageContent {
    private static final String TAG = "RecallCommandMessage";
    private String messageUId;
    private String targetId;
    private long sentTime;
    private int conversationType;
    private boolean isAdmin;
    private boolean isDelete;
    private String extra;
    public static final Creator<RecallCommandMessage> CREATOR = new Creator<RecallCommandMessage>() {
        public RecallCommandMessage createFromParcel(Parcel source) {
            return new RecallCommandMessage(source);
        }

        public RecallCommandMessage[] newArray(int size) {
            return new RecallCommandMessage[size];
        }
    };

    public String getMessageUId() {
        return this.messageUId;
    }

    public void setMessageUId(String messageUId) {
        this.messageUId = messageUId;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public long getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public int getConversationType() {
        return this.conversationType;
    }

    public void setConversationType(int conversationType) {
        this.conversationType = conversationType;
    }

    public boolean isAdmin() {
        return this.isAdmin;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public boolean isDelete() {
        return this.isDelete;
    }

    public void setDelete(boolean delete) {
        this.isDelete = delete;
    }

    public RecallCommandMessage(String UId) {
        this.setMessageUId(UId);
    }

    public RecallCommandMessage(String UId, String ex) {
        this.setMessageUId(UId);
        this.setExtra(ex);
    }

    public RecallCommandMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("RecallCommandMessage", var5.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            this.setMessageUId(jsonObj.optString("messageUId"));
            this.setExtra(jsonObj.optString("extra"));
            this.setTargetId(jsonObj.optString("targetId"));
            this.setConversationType(jsonObj.optInt("conversationType"));
            this.setSentTime(jsonObj.optLong("sentTime"));
            this.setAdmin(jsonObj.optBoolean("isAdmin"));
            this.setDelete(jsonObj.optBoolean("isDelete"));
        } catch (JSONException var4) {
            RLog.e("RecallCommandMessage", var4.getMessage());
        }

    }

    public RecallCommandMessage(Parcel in) {
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMessageUId(ParcelUtils.readFromParcel(in));
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setTargetId(ParcelUtils.readFromParcel(in));
        this.setConversationType(ParcelUtils.readIntFromParcel(in));
        this.setSentTime(ParcelUtils.readLongFromParcel(in));
        this.setAdmin(ParcelUtils.readIntFromParcel(in) == 1);
        this.setDelete(ParcelUtils.readIntFromParcel(in) == 1);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if (!TextUtils.isEmpty(this.getMessageUId())) {
                jsonObj.put("messageUId", this.getMessageUId());
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (!TextUtils.isEmpty(this.getTargetId())) {
                jsonObj.put("targetId", this.getTargetId());
            }

            jsonObj.put("isAdmin", this.isAdmin());
            jsonObj.put("isDelete", this.isDelete());
            jsonObj.put("conversationType", this.getConversationType());
            jsonObj.put("sentTime", this.getSentTime());
        } catch (JSONException var4) {
            RLog.e("RecallCommandMessage", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("RecallCommandMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getMessageUId());
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getTargetId());
        ParcelUtils.writeToParcel(dest, this.getConversationType());
        ParcelUtils.writeToParcel(dest, this.getSentTime());
        ParcelUtils.writeToParcel(dest, this.isAdmin ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.isDelete ? 1 : 0);
    }
}
