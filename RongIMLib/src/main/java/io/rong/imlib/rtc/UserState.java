//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.rtc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;

public class UserState implements Parcelable {
    private String userId;
    private UserState.State state;
    public static final Creator<UserState> CREATOR = new Creator<UserState>() {
        public UserState createFromParcel(Parcel source) {
            return new UserState(source);
        }

        public UserState[] newArray(int size) {
            return new UserState[size];
        }
    };

    public UserState() {
    }

    UserState(String userId, UserState.State state) {
        this.userId = userId;
        this.state = state;
    }

    private UserState(Parcel in) {
        this.setUserId(ParcelUtils.readFromParcel(in));
        this.setState(UserState.State.valueOf(ParcelUtils.readIntFromParcel(in)));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeInt(this.state.getValue());
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserState.State getState() {
        return this.state;
    }

    public void setState(UserState.State state) {
        this.state = state;
    }

    public static enum State {
        NONE(-1),
        JOIN(0),
        LEFT(1),
        OFFLINE(2);

        int value;

        public static UserState.State valueOf(int value) {
            UserState.State[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                UserState.State type = var1[var3];
                if (value == type.value) {
                    return type;
                }
            }

            return NONE;
        }

        private State(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}
