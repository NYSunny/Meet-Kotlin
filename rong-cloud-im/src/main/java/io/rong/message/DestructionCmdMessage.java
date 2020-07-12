//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:BurnNoticeMsg",
        flag = 0
)
public class DestructionCmdMessage extends MessageContent {
    private static final String TAG = DestructionCmdMessage.class.getSimpleName();
    private List<String> burnMessageUIds;
    public static final Creator<DestructionCmdMessage> CREATOR = new Creator<DestructionCmdMessage>() {
        public DestructionCmdMessage createFromParcel(Parcel source) {
            return new DestructionCmdMessage(source);
        }

        public DestructionCmdMessage[] newArray(int size) {
            return new DestructionCmdMessage[size];
        }
    };

    public DestructionCmdMessage() {
        this.burnMessageUIds = new ArrayList();
    }

    private DestructionCmdMessage(Parcel parcel) {
        this.burnMessageUIds = new ArrayList();
        this.burnMessageUIds = parcel.readArrayList(this.getClass().getClassLoader());
    }

    public DestructionCmdMessage(byte[] data) {
        this.burnMessageUIds = new ArrayList();

        try {
            String json = new String(data, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("burnMessageUIds");

            for(int i = 0; i < array.length(); ++i) {
                this.burnMessageUIds.add(array.getString(i));
            }
        } catch (UnsupportedEncodingException var6) {
            RLog.e(TAG, "DestructionCmdMessage", var6);
        } catch (JSONException var7) {
            RLog.e(TAG, "DestructionCmdMessage", var7);
        }

    }

    public List<String> getBurnMessageUIds() {
        return this.burnMessageUIds;
    }

    public void setBurnMessageUIds(List<String> burnMessageUIds) {
        if (burnMessageUIds != null) {
            this.burnMessageUIds = burnMessageUIds;
        }
    }

    public void addBurnMessageUId(String uid) {
        this.burnMessageUIds.add(uid);
    }

    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray(this.burnMessageUIds);

        try {
            jsonObject.put("burnMessageUIds", array);
        } catch (JSONException var4) {
            return null;
        }

        return jsonObject.toString().getBytes();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.burnMessageUIds);
    }
}
