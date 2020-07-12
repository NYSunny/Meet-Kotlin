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
import io.rong.common.FileInfo;
import io.rong.common.FileUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.UserInfo;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:FileMsg",
        flag = 3
)
public class FileMessage extends MediaMessageContent {
    private static final String TAG = "FileMessage";
    private long mSize;
    private String mType;
    public int progress;
    public static final Creator<FileMessage> CREATOR = new Creator<FileMessage>() {
        public FileMessage createFromParcel(Parcel source) {
            return new FileMessage(source);
        }

        public FileMessage[] newArray(int size) {
            return new FileMessage[size];
        }
    };

    public long getSize() {
        return this.mSize;
    }

    public void setSize(long size) {
        this.mSize = size;
    }

    public String getType() {
        return this.mType;
    }

    public void setType(String type) {
        if (!TextUtils.isEmpty(type)) {
            this.mType = type;
        } else {
            this.mType = "bin";
        }

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

            jsonObj.put("size", this.mSize);
            if (!TextUtils.isEmpty(this.mType)) {
                jsonObj.put("type", this.mType);
            }

            if (this.getLocalPath() != null) {
                jsonObj.put("localPath", this.getLocalPath().toString());
            }

            if (this.getMediaUrl() != null) {
                jsonObj.put("fileUrl", this.getMediaUrl().toString());
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObj.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var4) {
            RLog.e("FileMessage", "JSONException " + var4.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("FileMessage", "UnsupportedEncodingException", var3);
            return null;
        }
    }

    public FileMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("FileMessage", "UnsupportedEncodingException", var5);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("name")) {
                this.setName(jsonObj.optString("name"));
            }

            if (jsonObj.has("size")) {
                this.setSize(jsonObj.getLong("size"));
            }

            if (jsonObj.has("type")) {
                this.setType(jsonObj.optString("type"));
            }

            if (jsonObj.has("localPath")) {
                this.setLocalPath(Uri.parse(jsonObj.optString("localPath")));
            }

            if (jsonObj.has("fileUrl")) {
                this.setFileUrl(Uri.parse(jsonObj.optString("fileUrl")));
            }

            if (jsonObj.has("extra")) {
                this.setExtra(jsonObj.optString("extra"));
            }

            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }
        } catch (JSONException var4) {
            RLog.e("FileMessage", "JSONException " + var4.getMessage());
        }

    }

    private FileMessage() {
    }

    private FileMessage(FileInfo file, Uri uri) {
        this.setLocalPath(uri);
        this.setName(file.getName());
        this.setSize(file.getSize());
        this.setType(file.getType());
    }

    private FileMessage(File file, Uri url) {
        if (url.toString().startsWith("file")) {
            this.setLocalPath(url);
        } else {
            this.setMediaUrl(url);
        }

        this.setName(file.getName());
        this.setSize(file.length());
    }

    /** @deprecated */
    @Deprecated
    public static FileMessage obtain(Uri url) {
        if (url == null) {
            RLog.e("FileMessage", "url is null");
            return null;
        } else {
            File webFile;
            if (FileUtils.uriStartWithFile(url)) {
                webFile = new File(url.toString().substring(7));
                if (webFile.exists() && webFile.isFile()) {
                    return new FileMessage(webFile, url);
                } else {
                    RLog.e("FileMessage", "file is not exists, url is " + url);
                    return null;
                }
            } else if (FileUtils.uriStartWithContent(url)) {
                RLog.e("FileMessage", "url is content scheme");
                return null;
            } else {
                webFile = new File(url.toString());
                return new FileMessage(webFile, url);
            }
        }
    }

    public static FileMessage obtain(Context context, Uri url) {
        if (url != null && context != null) {
            FileInfo fileInfo = FileUtils.getFileInfoByUri(context, url);
            if (fileInfo != null) {
                return new FileMessage(fileInfo, url);
            } else {
                File webFile = new File(url.toString());
                return new FileMessage(webFile, url);
            }
        } else {
            RLog.e("FileMessage", "url or context is null");
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getName());
        ParcelUtils.writeToParcel(dest, this.getSize());
        ParcelUtils.writeToParcel(dest, this.getType());
        ParcelUtils.writeToParcel(dest, this.getLocalPath());
        ParcelUtils.writeToParcel(dest, this.getFileUrl());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public FileMessage(Parcel in) {
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setName(ParcelUtils.readFromParcel(in));
        this.setSize(ParcelUtils.readLongFromParcel(in));
        this.setType(ParcelUtils.readFromParcel(in));
        this.setLocalPath((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setFileUrl((Uri)ParcelUtils.readFromParcel(in, Uri.class));
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public List<String> getSearchableWord() {
        List<String> words = new ArrayList();
        if (this.getName() != null) {
            words.add(this.getName());
        }

        return words;
    }
}
