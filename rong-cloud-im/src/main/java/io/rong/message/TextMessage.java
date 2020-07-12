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
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:TxtMsg",
        flag = 3
)
@DestructionTag(
        destructionFlag = 0
)
public class TextMessage extends MessageContent {
    private static final String TAG = "TextMessage";
    private String content;
    protected String extra;
    public static final Creator<TextMessage> CREATOR = new Creator<TextMessage>() {
        public TextMessage createFromParcel(Parcel source) {
            return new TextMessage(source);
        }

        public TextMessage[] newArray(int size) {
            return new TextMessage[size];
        }
    };

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getContent())) {
                jsonObj.put("content", this.getEmotion(this.getContent()));
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if (this.getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", this.getJsonMentionInfo());
            }

            jsonObj.put("isBurnAfterRead", this.isDestruct());
            jsonObj.put("burnDuration", this.getDestructTime());
        } catch (JSONException var4) {
            RLog.e("TextMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("TextMessage", "UnsupportedEncodingException ", var3);
            return null;
        }
    }

    private String getEmotion(String content) {
        Pattern pattern = Pattern.compile("\\[/u([0-9A-Fa-f]+)\\]");
        Matcher matcher = pattern.matcher(content);

        StringBuffer sb;
        int inthex;
        for(sb = new StringBuffer(); matcher.find(); matcher.appendReplacement(sb, String.valueOf(Character.toChars(inthex)))) {
            String matchStr = matcher.group(1);
            inthex = 0;
            if (matchStr != null) {
                inthex = Integer.parseInt(matchStr, 16);
            }
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    protected TextMessage() {
    }

    public static TextMessage obtain(String text) {
        TextMessage model = new TextMessage();
        model.setContent(text);
        return model;
    }

    public TextMessage(byte[] data) {
        if (data == null) {
            RLog.e("TextMessage", "data is null ");
        } else {
            String jsonStr = null;

            try {
                if (data.length >= 40960) {
                    RLog.e("TextMessage", "TextMessage length is larger than 40KB, length :" + data.length);
                }

                jsonStr = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException var5) {
                RLog.e("TextMessage", "UnsupportedEncodingException ", var5);
            }

            if (jsonStr == null) {
                RLog.e("TextMessage", "jsonStr is null ");
            } else {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    if (jsonObj.has("content")) {
                        this.setContent(jsonObj.optString("content"));
                    }

                    if (jsonObj.has("extra")) {
                        this.setExtra(jsonObj.optString("extra"));
                    }

                    if (jsonObj.has("user")) {
                        this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
                    }

                    if (jsonObj.has("mentionedInfo")) {
                        this.setMentionedInfo(this.parseJsonToMentionInfo(jsonObj.getJSONObject("mentionedInfo")));
                    }

                    if (jsonObj.has("isBurnAfterRead")) {
                        this.setDestruct(jsonObj.getBoolean("isBurnAfterRead"));
                    }

                    if (jsonObj.has("burnDuration")) {
                        this.setDestructTime(jsonObj.getLong("burnDuration"));
                    }
                } catch (JSONException var4) {
                    RLog.e("TextMessage", "JSONException " + var4.getMessage());
                }

            }
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.content);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.getMentionedInfo());
        ParcelUtils.writeToParcel(dest, this.isDestruct() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.getDestructTime());
    }

    public TextMessage(Parcel in) {
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setContent(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setMentionedInfo((MentionedInfo)ParcelUtils.readFromParcel(in, MentionedInfo.class));
        this.setDestruct(ParcelUtils.readIntFromParcel(in) == 1);
        this.setDestructTime(ParcelUtils.readLongFromParcel(in));
    }

    public String toString() {
        return "TextMessage{content='" + this.content + '\'' + ", extra='" + this.extra + '\'' + '}';
    }

    public TextMessage(String content) {
        this.setContent(content);
    }

    public String getContent() {
        return this.content;
    }

    public List<String> getSearchableWord() {
        List<String> words = new ArrayList();
        words.add(this.content);
        return words;
    }
}
