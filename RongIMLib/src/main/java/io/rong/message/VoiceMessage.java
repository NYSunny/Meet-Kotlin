//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import io.rong.common.ParcelUtils;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:VcMsg",
        flag = 3,
        messageHandler = VoiceMessageHandler.class
)
@DestructionTag(
        destructionFlag = 0
)
public class VoiceMessage extends MessageContent {
    private Uri mUri;
    private int mDuration;
    private String mBase64;
    protected String extra;
    public static final Creator<VoiceMessage> CREATOR = new Creator<VoiceMessage>() {
        public VoiceMessage createFromParcel(Parcel source) {
            return new VoiceMessage(source);
        }

        public VoiceMessage[] newArray(int size) {
            return new VoiceMessage[size];
        }
    };

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public VoiceMessage(Parcel in) {
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.mUri = (Uri)ParcelUtils.readFromParcel(in, Uri.class);
        this.mDuration = ParcelUtils.readIntFromParcel(in);
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setDestruct(ParcelUtils.readIntFromParcel(in) == 1);
        this.setDestructTime(ParcelUtils.readLongFromParcel(in));
    }

    public VoiceMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("duration")) {
                this.setDuration(jsonObj.optInt("duration"));
            }

            if (jsonObj.has("content")) {
                this.setBase64(jsonObj.optString("content"));
            }

            if (jsonObj.has("extra")) {
                this.setExtra(jsonObj.optString("extra"));
            }

            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            if (jsonObj.has("isBurnAfterRead")) {
                this.setDestruct(jsonObj.getBoolean("isBurnAfterRead"));
            }

            if (jsonObj.has("burnDuration")) {
                this.setDestructTime(jsonObj.getLong("burnDuration"));
            }
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

    }

    private VoiceMessage(Uri uri, int duration) {
        this.mUri = uri;
        this.mDuration = duration;
    }

    public static VoiceMessage obtain(Uri uri, int duration) {
        return new VoiceMessage(uri, duration);
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void setUri(Uri uri) {
        this.mUri = uri;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public String getBase64() {
        return this.mBase64;
    }

    public void setBase64(String base64) {
        this.mBase64 = base64;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("content", this.mBase64);
            jsonObj.put("duration", this.mDuration);
            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.extra);
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            jsonObj.put("isBurnAfterRead", this.isDestruct());
            jsonObj.put("burnDuration", this.getDestructTime());
        } catch (JSONException var3) {
            Log.e("JSONException", var3.getMessage());
        }

        this.mBase64 = null;
        return jsonObj.toString().getBytes();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.extra);
        ParcelUtils.writeToParcel(dest, this.mUri);
        ParcelUtils.writeToParcel(dest, this.mDuration);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.isDestruct() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.getDestructTime());
    }
}
