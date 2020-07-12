//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.model.PublicServiceMenu.PublicServiceMenuItemType;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PublicServiceMenuItem implements Parcelable {
    private static final String TAG = PublicServiceMenuItem.class.getSimpleName();
    private String id;
    private String name;
    private String url;
    private PublicServiceMenuItemType type;
    private List<PublicServiceMenuItem> subMenuItems = new ArrayList();
    public static final Creator<PublicServiceMenuItem> CREATOR = new Creator<PublicServiceMenuItem>() {
        public PublicServiceMenuItem createFromParcel(Parcel source) {
            return new PublicServiceMenuItem(source);
        }

        public PublicServiceMenuItem[] newArray(int size) {
            return new PublicServiceMenuItem[size];
        }
    };

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public PublicServiceMenuItemType getType() {
        return this.type;
    }

    public List<PublicServiceMenuItem> getSubMenuItems() {
        return this.subMenuItems;
    }

    public String getId() {
        return this.id;
    }

    public PublicServiceMenuItem(JSONObject jsonItem) throws Exception {
        try {
            if (jsonItem.has("id")) {
                this.id = jsonItem.optString("id");
            }

            if (jsonItem.has("name")) {
                this.name = jsonItem.optString("name");
            }

            if (jsonItem.has("url")) {
                this.url = jsonItem.optString("url");
            }

            if (jsonItem.has("type")) {
                this.type = PublicServiceMenuItemType.setValue(jsonItem.optInt("type"));
                if (this.type != null && this.type == PublicServiceMenuItemType.Group && jsonItem.has("children")) {
                    JSONArray jsonArray = jsonItem.getJSONArray("children");
                    if (jsonArray != null) {
                        for(int i = 0; i < jsonArray.length(); ++i) {
                            JSONObject jsonSubItem = jsonArray.optJSONObject(i);
                            if (jsonSubItem != null) {
                                try {
                                    PublicServiceMenuItem item = new PublicServiceMenuItem(jsonSubItem);
                                    this.subMenuItems.add(item);
                                } catch (Exception var6) {
                                    RLog.e(TAG, "PublicServiceMenuItem ", var6);
                                }
                            }
                        }
                    }
                }
            }

        } catch (JSONException var7) {
            RLog.e(TAG, "PublicServiceMenuItem", var7);
            throw new Exception("PublicServiceMenuItem parse error!");
        }
    }

    private PublicServiceMenuItem() {
    }

    public int describeContents() {
        return 0;
    }

    public PublicServiceMenuItem(Parcel in) {
        this.id = ParcelUtils.readFromParcel(in);
        this.name = ParcelUtils.readFromParcel(in);
        this.url = ParcelUtils.readFromParcel(in);
        this.type = PublicServiceMenuItemType.setValue(ParcelUtils.readIntFromParcel(in));
        this.subMenuItems = ParcelUtils.readListFromParcel(in, PublicServiceMenuItem.class);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.id);
        ParcelUtils.writeToParcel(dest, this.name);
        ParcelUtils.writeToParcel(dest, this.url);
        ParcelUtils.writeToParcel(dest, this.type != null ? this.type.getValue() : null);
        ParcelUtils.writeToParcel(dest, this.subMenuItems);
    }
}
