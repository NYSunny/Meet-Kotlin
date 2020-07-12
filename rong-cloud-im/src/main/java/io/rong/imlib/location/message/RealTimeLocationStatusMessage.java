//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import io.rong.imlib.MessageTag;
import io.rong.imlib.location.RealTimeLocationType;
import io.rong.imlib.model.MessageContent;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:RL",
        flag = 16
)
public class RealTimeLocationStatusMessage extends MessageContent {
    private double latitude;
    private double longitude;
    private int realTimeLocationType;
    public static final Creator<RealTimeLocationStatusMessage> CREATOR = new Creator<RealTimeLocationStatusMessage>() {
        public RealTimeLocationStatusMessage createFromParcel(Parcel source) {
            return new RealTimeLocationStatusMessage(source);
        }

        public RealTimeLocationStatusMessage[] newArray(int size) {
            return new RealTimeLocationStatusMessage[size];
        }
    };

    public RealTimeLocationStatusMessage() {
        this.latitude = 0.0D;
        this.longitude = 0.0D;
        this.realTimeLocationType = 0;
    }

    public RealTimeLocationStatusMessage(byte[] data) {
        this.latitude = 0.0D;
        this.longitude = 0.0D;
        this.realTimeLocationType = 0;
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("latitude")) {
                this.latitude = jsonObj.optDouble("latitude");
            }

            if (jsonObj.has("longitude")) {
                this.longitude = jsonObj.optDouble("longitude");
            }

            if (jsonObj.has("type")) {
                int locationType = jsonObj.optInt("type");
                this.realTimeLocationType = RealTimeLocationType.valueOf(locationType).getValue();
            }
        } catch (JSONException var5) {
            Log.e("JSONException", var5.getMessage());
        }

    }

    public static RealTimeLocationStatusMessage obtain(double latitude, double longitude) {
        RealTimeLocationStatusMessage model = new RealTimeLocationStatusMessage();
        model.latitude = latitude;
        model.longitude = longitude;
        return model;
    }

    public static RealTimeLocationStatusMessage obtain(double latitude, double longitude, RealTimeLocationType type) {
        RealTimeLocationStatusMessage model = new RealTimeLocationStatusMessage();
        model.latitude = latitude;
        model.longitude = longitude;
        if (type == null) {
            model.realTimeLocationType = 0;
        } else {
            model.realTimeLocationType = type.getValue();
        }

        return model;
    }

    private RealTimeLocationStatusMessage(Parcel in) {
        this.latitude = 0.0D;
        this.longitude = 0.0D;
        this.realTimeLocationType = 0;
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.realTimeLocationType = in.readInt();
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public RealTimeLocationType getRealTimeLocationType() {
        return RealTimeLocationType.valueOf(this.realTimeLocationType);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.realTimeLocationType);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("latitude", this.latitude);
            jsonObj.put("longitude", this.longitude);
            jsonObj.put("type", this.realTimeLocationType);
        } catch (JSONException var3) {
            Log.e("JSONException", var3.getMessage());
        }

        return jsonObj.toString().getBytes();
    }
}
