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
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class PublicServiceMenu implements Parcelable {
    private static final String TAG = PublicServiceMenu.class.getSimpleName();
    private List<PublicServiceMenuItem> menuItems;
    public static final Creator<PublicServiceMenu> CREATOR = new Creator<PublicServiceMenu>() {
        public PublicServiceMenu createFromParcel(Parcel source) {
            return new PublicServiceMenu(source);
        }

        public PublicServiceMenu[] newArray(int size) {
            return new PublicServiceMenu[size];
        }
    };

    public List<PublicServiceMenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(List<PublicServiceMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public PublicServiceMenu(JSONArray jsonArray) {
        this.menuItems = new ArrayList();

        for(int i = 0; i < jsonArray.length(); ++i) {
            try {
                JSONObject jsonItem = jsonArray.optJSONObject(i);
                PublicServiceMenuItem item = new PublicServiceMenuItem(jsonItem);
                this.menuItems.add(item);
            } catch (Exception var5) {
                RLog.e(TAG, "PublicServiceMenu ", var5);
            }
        }

    }

    private PublicServiceMenu() {
    }

    public int describeContents() {
        return 0;
    }

    private PublicServiceMenu(Parcel in) {
        this.menuItems = ParcelUtils.readListFromParcel(in, PublicServiceMenuItem.class);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeListToParcel(dest, this.menuItems);
    }

    public static enum PublicServiceMenuItemType {
        Group(0, "GROUP"),
        View(1, "VIEW"),
        Click(2, "CLICK"),
        Entry(3, "ENTRY");

        private int value;
        private String command;

        private PublicServiceMenuItemType(int value, String command) {
            this.value = value;
            this.command = command;
        }

        public int getValue() {
            return this.value;
        }

        public String getMessage() {
            return this.command;
        }

        public static PublicServiceMenu.PublicServiceMenuItemType setValue(int code) {
            PublicServiceMenu.PublicServiceMenuItemType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                PublicServiceMenu.PublicServiceMenuItemType c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return null;
        }
    }
}
