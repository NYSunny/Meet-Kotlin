//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;

public class RemoteHistoryMsgOption implements Parcelable {
    private long dataTime;
    private int count;
    private int pullOrder;
    private boolean includeLocalExistMessage;
    private static final int PULL_MIN_COUNT = 0;
    private static final int PULL_MAX_COUNT = 20;
    private static final int DESCEND = 0;
    private static final int ASCEND = 1;
    public static final Creator<RemoteHistoryMsgOption> CREATOR = new Creator<RemoteHistoryMsgOption>() {
        public RemoteHistoryMsgOption createFromParcel(Parcel in) {
            return new RemoteHistoryMsgOption(in);
        }

        public RemoteHistoryMsgOption[] newArray(int size) {
            return new RemoteHistoryMsgOption[size];
        }
    };

    public RemoteHistoryMsgOption() {
        this(0L, 5, 1, false);
    }

    public RemoteHistoryMsgOption(long dataTime, int count, int pullOrder, boolean includeLocalExistMessage) {
        this.dataTime = dataTime;
        this.count = count;
        this.pullOrder = pullOrder;
        this.includeLocalExistMessage = includeLocalExistMessage;
    }

    public long getDataTime() {
        return this.dataTime;
    }

    public void setDataTime(long dataTime) {
        this.dataTime = dataTime;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        if (count < 0) {
            count = 0;
        }

        if (count > 20) {
            count = 20;
        }

        this.count = count;
    }

    public int getOrder() {
        return this.pullOrder;
    }

    public void setOrder(int pullOrder) {
        this.pullOrder = pullOrder;
    }

    public boolean isIncludeLocalExistMessage() {
        return this.includeLocalExistMessage;
    }

    public void setIncludeLocalExistMessage(boolean includeLocalExistMessage) {
        this.includeLocalExistMessage = includeLocalExistMessage;
    }

    protected RemoteHistoryMsgOption(Parcel in) {
        this.setDataTime(ParcelUtils.readLongFromParcel(in));
        this.setCount(ParcelUtils.readIntFromParcel(in));
        this.setOrder(ParcelUtils.readIntFromParcel(in));
        this.setIncludeLocalExistMessage(ParcelUtils.readIntFromParcel(in) == 1);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getDataTime());
        ParcelUtils.writeToParcel(dest, this.getCount());
        ParcelUtils.writeToParcel(dest, this.getOrder());
        ParcelUtils.writeToParcel(dest, this.isIncludeLocalExistMessage() ? 1 : 0);
    }
}
