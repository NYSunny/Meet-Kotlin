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
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MentionedInfo;
import io.rong.imlib.model.UserInfo;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:GIFMsg",
        flag = 3,
        messageHandler = GIFMessageHandler.class
)
public class GIFMessage extends MediaMessageContent {
    private static final String TAG = "GIFMessage";
    private boolean mUpLoadExp = false;
    private int width;
    private int height;
    private long gifDataSize;
    public static final Creator<GIFMessage> CREATOR = new Creator<GIFMessage>() {
        public GIFMessage createFromParcel(Parcel source) {
            return new GIFMessage(source);
        }

        public GIFMessage[] newArray(int size) {
            return new GIFMessage[size];
        }
    };

    private GIFMessage(Uri path) {
        this.setLocalUri(path);
    }

    public static GIFMessage obtain(Uri localUri) {
        if (FileUtils.isValidateLocalUri(localUri)) {
            return new GIFMessage(localUri);
        } else {
            RLog.e("GIFMessage", "localUri error uri is" + localUri);
            return null;
        }
    }

    public GIFMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("remoteUrl")) {
                String uri = jsonObj.optString("remoteUrl");
                if (!TextUtils.isEmpty(uri)) {
                    this.setRemoteUri(Uri.parse(uri));
                }
            }

            if (jsonObj.has("localPath")) {
                this.setLocalUri(Uri.parse(jsonObj.optString("localPath")));
            }

            if (jsonObj.has("extra")) {
                this.setExtra(jsonObj.optString("extra"));
            }

            if (jsonObj.has("exp")) {
                this.setUpLoadExp(true);
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

            if (jsonObj.has("width")) {
                this.setWidth(jsonObj.getInt("width"));
            }

            if (jsonObj.has("height")) {
                this.setHeight(jsonObj.getInt("height"));
            }

            if (jsonObj.has("gifDataSize")) {
                this.setGifDataSize((long)jsonObj.getInt("gifDataSize"));
            }

            if (jsonObj.has("name")) {
                this.setName(jsonObj.getString("name"));
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (this.getMediaUrl() != null) {
                jsonObj.put("remoteUrl", this.getMediaUrl().toString());
            }

            if (this.getLocalPath() != null) {
                jsonObj.put("localPath", this.getLocalPath().toString());
            }

            if (this.mUpLoadExp) {
                jsonObj.put("exp", true);
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            if (!TextUtils.isEmpty(this.getName())) {
                jsonObj.put("name", this.getName());
            }

            jsonObj.put("width", this.getWidth());
            jsonObj.put("height", this.getHeight());
            jsonObj.put("gifDataSize", this.getGifDataSize());
            jsonObj.put("isBurnAfterRead", this.isDestruct());
            jsonObj.put("burnDuration", this.getDestructTime());
        } catch (JSONException var3) {
            RLog.e("JSONException", var3.getMessage());
        }

        return jsonObj.toString().getBytes();
    }

    public void setUpLoadExp(boolean upLoadExp) {
        this.mUpLoadExp = upLoadExp;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public long getGifDataSize() {
        return this.gifDataSize;
    }

    void setWidth(int width) {
        this.width = width;
    }

    void setHeight(int height) {
        this.height = height;
    }

    void setGifDataSize(long gifDataSize) {
        this.gifDataSize = gifDataSize;
    }

    public Uri getLocalUri() {
        return this.getLocalPath();
    }

    public void setLocalUri(Uri localUri) {
        this.setLocalPath(localUri);
    }

    public Uri getRemoteUri() {
        return this.getMediaUrl();
    }

    public void setRemoteUri(Uri remoteUri) {
        this.setMediaUrl(remoteUri);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(this.mUpLoadExp ? 1 : 0));
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeLong(this.gifDataSize);
        dest.writeString(this.getName());
        dest.writeParcelable(this.getLocalUri(), flags);
        dest.writeParcelable(this.getRemoteUri(), flags);
        dest.writeString(this.getExtra());
        dest.writeParcelable(this.getUserInfo(), flags);
        dest.writeParcelable(this.getMentionedInfo(), flags);
        dest.writeByte((byte)(this.isDestruct() ? 1 : 0));
        dest.writeLong(this.getDestructTime());
    }

    protected GIFMessage(Parcel in) {
        this.mUpLoadExp = in.readByte() != 0;
        this.width = in.readInt();
        this.height = in.readInt();
        this.gifDataSize = in.readLong();
        this.setName(in.readString());
        Uri localUri = (Uri)in.readParcelable(Uri.class.getClassLoader());
        this.setLocalUri(localUri);
        Uri remoteUri = (Uri)in.readParcelable(Uri.class.getClassLoader());
        this.setRemoteUri(remoteUri);
        this.setExtra(in.readString());
        UserInfo info = (UserInfo)in.readParcelable(UserInfo.class.getClassLoader());
        this.setUserInfo(info);
        MentionedInfo mentionedInfo = (MentionedInfo)in.readParcelable(MentionedInfo.class.getClassLoader());
        this.setMentionedInfo(mentionedInfo);
        this.setDestruct(in.readByte() != 0);
        this.setDestructTime(in.readLong());
    }
}
