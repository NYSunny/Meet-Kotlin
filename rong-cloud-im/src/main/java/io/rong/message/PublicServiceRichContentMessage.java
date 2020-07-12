//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:PSImgTxtMsg",
        flag = 3
)
public class PublicServiceRichContentMessage extends MessageContent implements Parcelable {
    private RichContentItem mRichContentItem;
    private UserInfo mUser;
    public static final Creator<PublicServiceRichContentMessage> CREATOR = new Creator<PublicServiceRichContentMessage>() {
        public PublicServiceRichContentMessage createFromParcel(Parcel source) {
            return new PublicServiceRichContentMessage(source);
        }

        public PublicServiceRichContentMessage[] newArray(int size) {
            return new PublicServiceRichContentMessage[size];
        }
    };

    public PublicServiceRichContentMessage() {
    }

    public PublicServiceRichContentMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray items = (JSONArray)jsonObj.get("articles");
            JSONObject item = (JSONObject)items.get(0);
            this.mRichContentItem = new RichContentItem(item);
            JSONObject user = (JSONObject)jsonObj.get("user");
            if (user != null && !TextUtils.isEmpty(user.optString("portrait"))) {
                Uri uri = Uri.parse(user.optString("portrait"));
                this.mUser = new UserInfo(user.optString("id"), user.optString("name"), uri);
            }
        } catch (JSONException var8) {
            RLog.e("PSImgTxtMsg", var8.getMessage());
        }

    }

    public RichContentItem getMessage() {
        return this.mRichContentItem;
    }

    public UserInfo getPublicServiceUserInfo() {
        return this.mUser;
    }

    public byte[] encode() {
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.mRichContentItem);
    }

    public PublicServiceRichContentMessage(Parcel in) {
        this.mRichContentItem = (RichContentItem)ParcelUtils.readFromParcel(in, RichContentItem.class);
    }
}
