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
        value = "RC:ReadNtf",
        flag = 0
)
public class ReadReceiptMessage extends MessageContent {
    private static final String TAG = "ReadReceiptMessage";
    private long lastMessageSendTime;
    private String messageUId;
    private ReadReceiptMessage.ReadReceiptType type;
    public static final Creator<ReadReceiptMessage> CREATOR = new Creator<ReadReceiptMessage>() {
        public ReadReceiptMessage createFromParcel(Parcel source) {
            return new ReadReceiptMessage(source);
        }

        public ReadReceiptMessage[] newArray(int size) {
            return new ReadReceiptMessage[size];
        }
    };

    public long getLastMessageSendTime() {
        return this.lastMessageSendTime;
    }

    public void setLastMessageSendTime(long lastMessageSendTime) {
        this.lastMessageSendTime = lastMessageSendTime;
    }

    public String getMessageUId() {
        return this.messageUId;
    }

    public void setMessageUId(String messageUId) {
        this.messageUId = messageUId;
    }

    public ReadReceiptMessage.ReadReceiptType getType() {
        return this.type;
    }

    public void setType(ReadReceiptMessage.ReadReceiptType type) {
        this.type = type;
    }

    public ReadReceiptMessage(long sendTime) {
        this.setLastMessageSendTime(sendTime);
        this.setType(ReadReceiptMessage.ReadReceiptType.SEND_TIME);
    }

    public ReadReceiptMessage(String uId) {
        this.setMessageUId(uId);
        this.setType(ReadReceiptMessage.ReadReceiptType.UID);
    }

    public ReadReceiptMessage(long sendTime, String uId, ReadReceiptMessage.ReadReceiptType type) {
        this.setLastMessageSendTime(sendTime);
        this.setMessageUId(uId);
        this.setType(type);
    }

    public ReadReceiptMessage(Parcel in) {
        this.setLastMessageSendTime(ParcelUtils.readLongFromParcel(in));
        this.setMessageUId(ParcelUtils.readFromParcel(in));
        this.setType(ReadReceiptMessage.ReadReceiptType.setValue(ParcelUtils.readIntFromParcel(in)));
    }

    public ReadReceiptMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("ReadReceiptMessage", var5.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("lastMessageSendTime")) {
                this.setLastMessageSendTime(jsonObj.getLong("lastMessageSendTime"));
            }

            if (jsonObj.has("messageUId")) {
                this.setMessageUId(jsonObj.getString("messageUId"));
            }

            if (jsonObj.has("type")) {
                this.setType(ReadReceiptMessage.ReadReceiptType.setValue(jsonObj.getInt("type")));
            }
        } catch (JSONException var4) {
            RLog.e("ReadReceiptMessage", var4.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("lastMessageSendTime", this.getLastMessageSendTime());
            if (!TextUtils.isEmpty(this.getMessageUId())) {
                jsonObj.put("messageUId", this.getMessageUId());
            }

            if (this.getType() != null) {
                jsonObj.put("type", this.getType().getValue());
            }
        } catch (JSONException var4) {
            RLog.e("ReadReceiptMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("ReadReceiptMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    private ReadReceiptMessage() {
    }

    public static ReadReceiptMessage obtain(long sendTime) {
        ReadReceiptMessage obj = new ReadReceiptMessage();
        obj.setLastMessageSendTime(sendTime);
        obj.setType(ReadReceiptMessage.ReadReceiptType.SEND_TIME);
        return obj;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getLastMessageSendTime());
        ParcelUtils.writeToParcel(dest, this.getMessageUId());
        ParcelUtils.writeToParcel(dest, this.getType() != null ? this.getType().getValue() : 0);
    }

    public static enum ReadReceiptType {
        SEND_TIME(1),
        UID(2);

        private int value;

        private ReadReceiptType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static ReadReceiptMessage.ReadReceiptType setValue(int code) {
            ReadReceiptMessage.ReadReceiptType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ReadReceiptMessage.ReadReceiptType c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return SEND_TIME;
        }
    }
}
