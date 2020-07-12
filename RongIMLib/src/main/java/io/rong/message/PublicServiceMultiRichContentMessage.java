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
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:PSMultiImgTxtMsg",
        flag = 3
)
public class PublicServiceMultiRichContentMessage extends MessageContent {
    private ArrayList<RichContentItem> mRichContentItems = new ArrayList();
    private UserInfo mUser;
    public static final Creator<PublicServiceMultiRichContentMessage> CREATOR = new Creator<PublicServiceMultiRichContentMessage>() {
        public PublicServiceMultiRichContentMessage createFromParcel(Parcel source) {
            return new PublicServiceMultiRichContentMessage(source);
        }

        public PublicServiceMultiRichContentMessage[] newArray(int size) {
            return new PublicServiceMultiRichContentMessage[size];
        }
    };

    public PublicServiceMultiRichContentMessage() {
    }

    public PublicServiceMultiRichContentMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray items = (JSONArray)jsonObj.get("articles");

            for(int i = 0; i < items.length(); ++i) {
                JSONObject item = (JSONObject)items.get(i);
                RichContentItem richContentItem = new RichContentItem(item);
                this.mRichContentItems.add(richContentItem);
            }

            JSONObject user = (JSONObject)jsonObj.get("user");
            if (user != null && !TextUtils.isEmpty(user.optString("portrait"))) {
                Uri uri = Uri.parse(user.optString("portrait"));
                this.mUser = new UserInfo(user.optString("id"), user.optString("name"), uri);
            }
        } catch (JSONException var8) {
            RLog.e("PSMultiImgTxtMsg", var8.getMessage());
        }

    }

    public byte[] encode() {
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeListToParcel(dest, this.mRichContentItems);
    }

    public ArrayList<RichContentItem> getMessages() {
        return this.mRichContentItems;
    }

    public UserInfo getPublicServiceUserInfo() {
        return this.mUser;
    }

    public PublicServiceMultiRichContentMessage(Parcel in) {
        this.mRichContentItems = ParcelUtils.readListFromParcel(in, RichContentItem.class);
    }
}
