//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import io.rong.common.FileInfo;
import io.rong.common.FileUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.DestructionTag;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.UserInfo;
import java.io.File;
import java.io.UnsupportedEncodingException;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:SightMsg",
        flag = 3,
        messageHandler = SightMessageHandler.class
)
@DestructionTag(
        destructionFlag = 0
)
public class SightMessage extends MediaMessageContent {
    private static final String TAG = "SightMessage";
    private Uri mThumbUri;
    private String mBase64;
    private int mDuration;
    private long mSize;
    public static final Creator<SightMessage> CREATOR = new Creator<SightMessage>() {
        public SightMessage createFromParcel(Parcel source) {
            return new SightMessage(source);
        }

        public SightMessage[] newArray(int size) {
            return new SightMessage[size];
        }
    };

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.mBase64)) {
                jsonObj.put("content", this.mBase64);
            } else {
                Log.d("SightMessage", "base64 is null");
            }

            if (!TextUtils.isEmpty(this.getName())) {
                jsonObj.put("name", this.getName());
            }

            jsonObj.put("size", this.mSize);
            if (this.getLocalPath() != null) {
                jsonObj.put("localPath", this.getLocalPath().toString());
            }

            if (this.getMediaUrl() != null) {
                jsonObj.put("sightUrl", this.getMediaUrl().toString());
            }

            jsonObj.put("duration", this.getDuration());
            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }

            jsonObj.put("isBurnAfterRead", this.isDestruct());
            jsonObj.put("burnDuration", this.getDestructTime());
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("SightMessage", "encode", var3);
            return null;
        }
    }

    public SightMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("name")) {
                this.setName(jsonObj.optString("name"));
            }

            if (jsonObj.has("size")) {
                this.setSize(jsonObj.getLong("size"));
            }

            if (jsonObj.has("content")) {
                this.setBase64(jsonObj.optString("content"));
            }

            if (jsonObj.has("localPath")) {
                this.setLocalPath(Uri.parse(jsonObj.optString("localPath")));
            }

            if (jsonObj.has("sightUrl")) {
                this.setMediaUrl(Uri.parse(jsonObj.optString("sightUrl")));
            }

            if (jsonObj.has("duration")) {
                this.setDuration(jsonObj.optInt("duration"));
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

    public SightMessage() {
    }

    private SightMessage(File localFile, Uri thumbUri, Uri localUri, int duration) {
        this.mThumbUri = thumbUri;
        this.setLocalPath(localUri);
        this.setDuration(duration);
        this.setName(localFile.getName());
        this.setSize(localFile.length());
    }

    private SightMessage(FileInfo file, Uri thumbUri, Uri localUri, int duration) {
        this.mThumbUri = thumbUri;
        this.setLocalPath(localUri);
        this.setDuration(duration);
        this.setName(file.getName());
        this.setSize(file.getSize());
    }

    /** @deprecated */
    @Deprecated
    public static SightMessage obtain(Uri thumbUri, Uri localUri, int duration) {
        if (FileUtils.uriStartWithFile(localUri)) {
            File file = new File(localUri.toString().substring(7));
            if (file.exists() && file.isFile()) {
                return new SightMessage(file, thumbUri, localUri, duration);
            }
        }

        RLog.e("SightMessage", "localUri is not file scheme");
        return null;
    }

    public static SightMessage obtain(Uri localUri, int duration) {
        if (FileUtils.uriStartWithFile(localUri)) {
            File file = new File(localUri.toString().substring(7));
            if (file.exists() && file.isFile()) {
                return new SightMessage(file, (Uri)null, localUri, duration);
            }
        }

        RLog.e("SightMessage", "localUri is not file scheme");
        return null;
    }

    public static SightMessage obtain(Context context, Uri localUri, int duration) {
        if (context != null && localUri != null) {
            FileInfo fileInfo = FileUtils.getFileInfoByUri(context, localUri);
            if (fileInfo != null) {
                return new SightMessage(fileInfo, (Uri)null, localUri, duration);
            } else {
                RLog.e("SightMessage", "localUri is not file or content scheme");
                return null;
            }
        } else {
            RLog.e("SightMessage", "url or context is null");
            return null;
        }
    }

    public static SightMessage obtain(Context context, Uri thumbUri, Uri localUri, int duration) {
        if (context != null && localUri != null) {
            FileInfo fileInfo = FileUtils.getFileInfoByUri(context, localUri);
            if (fileInfo != null) {
                return new SightMessage(fileInfo, thumbUri, localUri, duration);
            } else {
                RLog.e("SightMessage", "localUri is not file or content scheme");
                return null;
            }
        } else {
            RLog.e("SightMessage", "url or context is null");
            return null;
        }
    }

    public Uri getThumbUri() {
        return this.mThumbUri;
    }

    public void setThumbUri(Uri thumbUri) {
        this.mThumbUri = thumbUri;
    }

    public void setBase64(String base64) {
        this.mBase64 = base64;
    }

    public String getBase64() {
        return this.mBase64;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public long getSize() {
        return this.mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }

    public int describeContents() {
        return 0;
    }

    public SightMessage(Parcel in) {
        this.mThumbUri = (Uri)ParcelUtils.readFromParcel(in, Uri.class);
        this.setLocalPath((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setMediaUrl((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setDuration(ParcelUtils.readIntFromParcel(in));
        this.setName(ParcelUtils.readFromParcel(in));
        this.setSize(ParcelUtils.readLongFromParcel(in));
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
        this.setDestruct(ParcelUtils.readIntFromParcel(in) == 1);
        this.setDestructTime(ParcelUtils.readLongFromParcel(in));
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.mThumbUri);
        ParcelUtils.writeToParcel(dest, this.getLocalPath());
        ParcelUtils.writeToParcel(dest, this.getMediaUrl());
        ParcelUtils.writeToParcel(dest, this.getDuration());
        ParcelUtils.writeToParcel(dest, this.getName());
        ParcelUtils.writeToParcel(dest, this.getSize());
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
        ParcelUtils.writeToParcel(dest, this.isDestruct() ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.getDestructTime());
    }
}
