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
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:EncryptedMsg",
        flag = 0
)
public class RCEncryptedMessage extends MessageContent {
    private static String TAG = RCEncryptedMessage.class.getName();
    private String encryptedContent;
    private String remoteEncId;
    private String originalObjName;
    public static final Creator<RCEncryptedMessage> CREATOR = new Creator<RCEncryptedMessage>() {
        public RCEncryptedMessage createFromParcel(Parcel source) {
            return new RCEncryptedMessage(source);
        }

        public RCEncryptedMessage[] newArray(int size) {
            return new RCEncryptedMessage[size];
        }
    };

    public RCEncryptedMessage() {
    }

    public RCEncryptedMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "utf-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e(TAG, "RCEncryptedMessage", var5);
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.has("encryptedContent")) {
                this.setEncryptedContent(jsonObject.optString("encryptedContent"));
            }

            if (jsonObject.has("remoteEncId")) {
                this.setRemoteEncId(jsonObject.optString("remoteEncId"));
            }

            if (jsonObject.has("originalObjName")) {
                this.setOriginalObjName(jsonObject.optString("originalObjName"));
            }

            if (jsonObject.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObject.getJSONObject("user")));
            }

            if (jsonObject.has("mentionedInfo")) {
                this.setMentionedInfo(this.parseJsonToMentionInfo(jsonObject.getJSONObject("mentionedInfo")));
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

    }

    public String getEncryptedContent() {
        return this.encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }

    public String getRemoteEncId() {
        return this.remoteEncId;
    }

    public void setRemoteEncId(String remoteEncId) {
        this.remoteEncId = remoteEncId;
    }

    public String getOriginalObjName() {
        return this.originalObjName;
    }

    public void setOriginalObjName(String originalObjName) {
        this.originalObjName = originalObjName;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("remoteEncId", this.getRemoteEncId());
            jsonObj.put("originalObjName", this.getOriginalObjName());
            jsonObj.put("encryptedContent", this.getEncryptedContent());
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if (this.getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", this.getJsonMentionInfo());
            }
        } catch (JSONException var4) {
            RLog.e(TAG, "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private String getEmotion(String content) {
        Pattern pattern = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher matcher = pattern.matcher(content);
        StringBuffer sb = new StringBuffer();

        while(matcher.find()) {
            int inthex = Integer.parseInt(matcher.group(1), 16);
            matcher.appendReplacement(sb, String.valueOf(Character.toChars(inthex)));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.encryptedContent);
        dest.writeString(this.remoteEncId);
        dest.writeString(this.originalObjName);
        dest.writeParcelable(this.getUserInfo(), flags);
        dest.writeParcelable(this.getMentionedInfo(), flags);
    }

    protected RCEncryptedMessage(Parcel in) {
        this.encryptedContent = in.readString();
        this.remoteEncId = in.readString();
        this.originalObjName = in.readString();
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMentionedInfo((MentionedInfo)ParcelUtils.readFromParcel(in, MentionedInfo.class));
    }
}
