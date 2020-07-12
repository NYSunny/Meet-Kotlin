//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import io.rong.common.ParcelUtils;
import io.rong.imlib.model.Conversation.ConversationType;
import org.json.JSONException;
import org.json.JSONObject;

public class PublicServiceProfile implements Parcelable {
    private String name;
    private Uri portraitUri;
    private String publicServiceId;
    private ConversationType publicServiceType;
    private boolean isFollowed;
    private String introduction;
    private boolean isGlobal;
    private PublicServiceMenu menu;
    public static final Creator<PublicServiceProfile> CREATOR = new Creator<PublicServiceProfile>() {
        public PublicServiceProfile createFromParcel(Parcel source) {
            return new PublicServiceProfile(source);
        }

        public PublicServiceProfile[] newArray(int size) {
            return new PublicServiceProfile[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.name);
        ParcelUtils.writeToParcel(dest, this.portraitUri);
        ParcelUtils.writeToParcel(dest, this.publicServiceId);
        if (this.publicServiceType != null) {
            ParcelUtils.writeToParcel(dest, this.publicServiceType.getValue());
        } else {
            ParcelUtils.writeToParcel(dest, 0);
        }

        ParcelUtils.writeToParcel(dest, this.introduction);
        ParcelUtils.writeToParcel(dest, this.isFollowed ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.isGlobal ? 1 : 0);
        ParcelUtils.writeToParcel(dest, this.menu);
    }

    public PublicServiceProfile() {
    }

    public void setExtra(String extra) {
        try {
            if (!TextUtils.isEmpty(extra)) {
                JSONObject jsonObj = new JSONObject(extra);
                if (jsonObj.has("introduction")) {
                    this.setIntroduction(jsonObj.optString("introduction"));
                }

                if (jsonObj.has("follow")) {
                    this.setIsFollow(jsonObj.optBoolean("follow"));
                }

                if (jsonObj.has("isGlobal")) {
                    this.setIsGlobal(jsonObj.optBoolean("isGlobal"));
                }

                if (jsonObj.has("menu") && jsonObj.getJSONArray("menu") != null) {
                    try {
                        this.menu = new PublicServiceMenu(jsonObj.getJSONArray("menu"));
                    } catch (Exception var4) {
                        Log.e("DecodePSMenu", var4.getMessage());
                    }
                }
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }

    }

    public PublicServiceProfile(Parcel source) {
        this.name = ParcelUtils.readFromParcel(source);
        this.portraitUri = (Uri)ParcelUtils.readFromParcel(source, Uri.class);
        this.publicServiceId = ParcelUtils.readFromParcel(source);
        this.publicServiceType = ConversationType.setValue(ParcelUtils.readIntFromParcel(source));
        this.introduction = ParcelUtils.readFromParcel(source);
        this.isFollowed = ParcelUtils.readIntFromParcel(source) == 1;
        this.isGlobal = ParcelUtils.readIntFromParcel(source) == 1;
        this.menu = (PublicServiceMenu)ParcelUtils.readFromParcel(source, PublicServiceMenu.class);
    }

    public void setIsGlobal(boolean global) {
        this.isGlobal = global;
    }

    public Uri getPortraitUri() {
        return this.portraitUri;
    }

    public void setPortraitUri(Uri portraitUri) {
        this.portraitUri = portraitUri;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetId() {
        return this.publicServiceId;
    }

    public void setTargetId(String targetId) {
        this.publicServiceId = targetId;
    }

    public void setIntroduction(String intro) {
        this.introduction = intro;
    }

    public boolean isFollow() {
        return this.isFollowed;
    }

    public void setIsFollow(boolean isFollow) {
        this.isFollowed = isFollow;
    }

    public ConversationType getConversationType() {
        return this.publicServiceType;
    }

    public void setPublicServiceType(ConversationType publicServiceType) {
        this.publicServiceType = publicServiceType;
    }

    public boolean isGlobal() {
        return this.isGlobal;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public PublicServiceMenu getMenu() {
        return this.menu;
    }

    public void setMenu(PublicServiceMenu menu) {
        this.menu = menu;
    }
}
