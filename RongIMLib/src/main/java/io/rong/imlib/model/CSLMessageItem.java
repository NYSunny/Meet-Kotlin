//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class CSLMessageItem implements Parcelable {
    private String name;
    private String title;
    private String type;
    private String defaultText;
    private boolean required;
    private String verification;
    private int max;
    private Map<String, String> remindMessage;
    public static final Creator<CSLMessageItem> CREATOR = new Creator<CSLMessageItem>() {
        public CSLMessageItem createFromParcel(Parcel source) {
            return new CSLMessageItem(source);
        }

        public CSLMessageItem[] newArray(int size) {
            return new CSLMessageItem[size];
        }
    };

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getVerification() {
        return this.verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Map<String, String> getMessage() {
        return this.remindMessage;
    }

    public void setMessage(Map<String, String> message) {
        this.remindMessage = message;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.defaultText);
        dest.writeByte((byte)(this.required ? 1 : 0));
        dest.writeString(this.verification);
        dest.writeInt(this.max);
        dest.writeInt(this.remindMessage.size());
        Iterator var3 = this.remindMessage.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<String, String> entry = (Entry)var3.next();
            dest.writeString((String)entry.getKey());
            dest.writeString((String)entry.getValue());
        }

    }

    public CSLMessageItem() {
    }

    protected CSLMessageItem(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.defaultText = in.readString();
        this.required = in.readByte() != 0;
        this.verification = in.readString();
        this.max = in.readInt();
        int remindMessageSize = in.readInt();
        this.remindMessage = new HashMap(remindMessageSize);

        for(int i = 0; i < remindMessageSize; ++i) {
            String key = in.readString();
            String value = in.readString();
            this.remindMessage.put(key, value);
        }

    }

    public static enum RemindType {
        EMPTY("empty"),
        WRONG_FORMAT("wrong_format"),
        OVER_LENGTH("over_length");

        private String name;

        private RemindType(String type) {
            this.name = type;
        }

        public String getName() {
            return this.name;
        }
    }
}
