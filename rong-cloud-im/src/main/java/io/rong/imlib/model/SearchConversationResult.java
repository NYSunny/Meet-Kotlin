//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;

public class SearchConversationResult implements Parcelable {
    private Conversation mConversation;
    private int mMatchCount;
    public static final Creator<SearchConversationResult> CREATOR = new Creator<SearchConversationResult>() {
        public SearchConversationResult createFromParcel(Parcel source) {
            return new SearchConversationResult(source);
        }

        public SearchConversationResult[] newArray(int size) {
            return new SearchConversationResult[size];
        }
    };

    public SearchConversationResult() {
    }

    public Conversation getConversation() {
        return this.mConversation;
    }

    public void setConversation(Conversation mConversation) {
        this.mConversation = mConversation;
    }

    public int getMatchCount() {
        return this.mMatchCount;
    }

    public void setMatchCount(int mMatchCount) {
        this.mMatchCount = mMatchCount;
    }

    public int describeContents() {
        return 0;
    }

    public SearchConversationResult(Parcel in) {
        this.mConversation = (Conversation)ParcelUtils.readFromParcel(in, Conversation.class);
        this.mMatchCount = ParcelUtils.readIntFromParcel(in);
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.mConversation);
        ParcelUtils.writeToParcel(dest, this.mMatchCount);
    }
}
