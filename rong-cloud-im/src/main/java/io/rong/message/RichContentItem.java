//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import io.rong.common.ParcelUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RichContentItem implements Parcelable {
    private String title;
    private String digest;
    private String imageUrl;
    private String url;
    public static final Creator<RichContentItem> CREATOR = new Creator<RichContentItem>() {
        public RichContentItem createFromParcel(Parcel source) {
            return new RichContentItem(source);
        }

        public RichContentItem[] newArray(int size) {
            return new RichContentItem[0];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.title);
        ParcelUtils.writeToParcel(dest, this.digest);
        ParcelUtils.writeToParcel(dest, this.imageUrl);
        ParcelUtils.writeToParcel(dest, this.url);
    }

    public RichContentItem(Parcel in) {
        this.setTitle(ParcelUtils.readFromParcel(in));
        this.setDigest(ParcelUtils.readFromParcel(in));
        this.setImageUrl(ParcelUtils.readFromParcel(in));
        this.setUrl(ParcelUtils.readFromParcel(in));
    }

    public RichContentItem(JSONObject jsonObj) {
        if (jsonObj != null) {
            if (jsonObj.has("title")) {
                this.setTitle(jsonObj.optString("title"));
            }

            if (jsonObj.has("digest")) {
                this.setDigest(jsonObj.optString("digest"));
            }

            if (jsonObj.has("picurl")) {
                this.setImageUrl(jsonObj.optString("picurl"));
            }

            if (jsonObj.has("url")) {
                this.setUrl(jsonObj.optString("url"));
            }
        }

    }

    public RichContentItem(String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("title")) {
                this.setTitle(jsonObj.optString("title"));
            }

            if (jsonObj.has("digest")) {
                this.setDigest(jsonObj.optString("digest"));
            }

            if (jsonObj.has("picurl")) {
                this.setImageUrl(jsonObj.optString("picurl"));
            }

            if (jsonObj.has("url")) {
                this.setUrl(jsonObj.optString("url"));
            }
        } catch (JSONException var3) {
            Log.e("JSONException", var3.getMessage());
        }

    }

    public String getDigest() {
        return this.digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
