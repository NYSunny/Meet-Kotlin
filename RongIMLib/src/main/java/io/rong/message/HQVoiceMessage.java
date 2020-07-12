//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:HQVCMsg",
        flag = 3,
        messageHandler = HQVoiceMessageHandler.class
)
@DestructionTag(
        destructionFlag = 0
)
public class HQVoiceMessage extends MediaMessageContent {
    private static final String TAG = "HQVoiceMessage";
    private int mDuration;
    public static final Creator<HQVoiceMessage> CREATOR = new Creator<HQVoiceMessage>() {
        public HQVoiceMessage createFromParcel(Parcel source) {
            return new HQVoiceMessage(source);
        }

        public HQVoiceMessage[] newArray(int size) {
            return new HQVoiceMessage[size];
        }
    };

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public Uri getFileUrl() {
        return this.getMediaUrl();
    }

    public void setFileUrl(Uri uri) {
        this.setMediaUrl(uri);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getName())) {
                jsonObj.put("name", this.getName());
            }

            if (this.getLocalPath() != null) {
                jsonObj.put("localPath", this.getLocalPath().toString());
            }

            if (this.getMediaUrl() != null) {
                jsonObj.put("remoteUrl", this.getMediaUrl().toString());
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            jsonObj.put("duration", this.mDuration);
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            jsonObj.put("isBurnAfterRead", this.isDestruct());
            jsonObj.put("burnDuration", this.getDestructTime());
        } catch (JSONException var4) {
            RLog.e("HQVoiceMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("HQVoiceMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public HQVoiceMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("HQVoiceMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("name")) {
                this.setName(jsonObj.optString("name"));
            }

            if (jsonObj.has("localPath")) {
                this.setLocalPath(Uri.parse(jsonObj.optString("localPath")));
            }

            if (jsonObj.has("remoteUrl")) {
                this.setFileUrl(Uri.parse(jsonObj.optString("remoteUrl")));
            }

            if (jsonObj.has("extra")) {
                this.setExtra(jsonObj.optString("extra"));
            }

            if (jsonObj.has("duration")) {
                this.setDuration(jsonObj.optInt("duration"));
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
            RLog.e("HQVoiceMessage", "JSONException " + var4.getMessage());
        }

    }

    private HQVoiceMessage(Uri uri, int duration) {
        this.setLocalPath(uri);
        this.mDuration = duration;
    }

    public static HQVoiceMessage obtain(Uri uri, int duration) {
        return new HQVoiceMessage(uri, duration);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getName());
        ParcelUtils.writeToParcel(dest, this.getLocalPath());
        ParcelUtils.writeToParcel(dest, this.getFileUrl());
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getDuration());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.isDestruct() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.getDestructTime());
    }

    public HQVoiceMessage(Parcel in) {
        this.setName(ParcelUtils.readFromParcel(in));
        this.setLocalPath((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setFileUrl((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setDuration(ParcelUtils.readIntFromParcel(in));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setDestruct(ParcelUtils.readIntFromParcel(in) == 1);
        this.setDestructTime(ParcelUtils.readLongFromParcel(in));
    }
}
