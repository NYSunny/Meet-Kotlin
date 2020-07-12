//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.rtc;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.rtc.UserState.State;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RCRTC:state",
        flag = 16
)
public class RoomUserStateMessage extends MessageContent {
    private static final String TAG = "RoomUserStateMessage";
    private List<UserState> userStates;
    public static final Creator<RoomUserStateMessage> CREATOR = new Creator<RoomUserStateMessage>() {
        public RoomUserStateMessage createFromParcel(Parcel source) {
            return new RoomUserStateMessage(source);
        }

        public RoomUserStateMessage[] newArray(int size) {
            return new RoomUserStateMessage[size];
        }
    };

    private RoomUserStateMessage(Parcel source) {
        this.userStates = ParcelUtils.readListFromParcel(source, UserState.class);
    }

    public byte[] encode() {
        return new byte[0];
    }

    public RoomUserStateMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var9) {
            RLog.e("RoomUserStateMessage", "UnsupportedEncodingException ", var9);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("users")) {
                JSONArray jsonArray = jsonObj.optJSONArray("users");
                this.userStates = new ArrayList();

                for(int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject jsonUser = (JSONObject)jsonArray.get(i);
                    if (jsonUser != null) {
                        String userId = jsonUser.getString("userId");
                        int state = jsonUser.getInt("state");
                        this.userStates.add(new UserState(userId, State.valueOf(state)));
                    }
                }
            }
        } catch (JSONException var10) {
            RLog.e("RoomUserStateMessage", "JSONException " + var10.getMessage());
        }

    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.userStates);
    }

    public List<UserState> getUserStates() {
        return this.userStates;
    }
}
