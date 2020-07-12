//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:LogCmdMsg",
        flag = 0
)
public class LogCmdMessage extends MessageContent {
    private String uri;
    private String logId;
    private String platform;
    private String packageName;
    private long startTime;
    private long endTime;
    public static final Creator<LogCmdMessage> CREATOR = new Creator<LogCmdMessage>() {
        public LogCmdMessage createFromParcel(Parcel source) {
            return new LogCmdMessage(source);
        }

        public LogCmdMessage[] newArray(int size) {
            return new LogCmdMessage[size];
        }
    };

    public LogCmdMessage(byte[] data) {
        String jsonStr = new String(data);

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            this.uri = jsonObj.optString("uri");
            this.logId = jsonObj.optString("logId");
            this.platform = jsonObj.optString("platform");
            this.packageName = jsonObj.optString("packageName");
            this.startTime = jsonObj.optLong("startTime");
            this.endTime = jsonObj.optLong("endTime");
        } catch (JSONException var4) {
            Log.e("JSONException", var4.getMessage());
        }

    }

    public String getUri() {
        return this.uri;
    }

    public String getLogId() {
        return this.logId;
    }

    public String getPlatform() {
        return this.platform;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            String uri = this.getUri();
            if (uri != null) {
                jsonObj.put("uri", uri);
            }

            String logId = this.getLogId();
            if (logId != null) {
                jsonObj.put("logId", logId);
            }

            String platform = this.getPlatform();
            if (platform != null) {
                jsonObj.put("platform", platform);
            }

            String packageName = this.getPackageName();
            if (packageName != null) {
                jsonObj.put("packageName", packageName);
            }

            long startTime = this.getStartTime();
            jsonObj.put("startTime", startTime);
            jsonObj.put("endTime", this.endTime);
        } catch (JSONException var8) {
            RLog.e("JSONException", var8.getMessage());
        }

        return jsonObj.toString().getBytes();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUri());
        ParcelUtils.writeToParcel(dest, this.getLogId());
        ParcelUtils.writeToParcel(dest, this.getPlatform());
        ParcelUtils.writeToParcel(dest, this.getPackageName());
        ParcelUtils.writeToParcel(dest, this.getStartTime());
        ParcelUtils.writeToParcel(dest, this.getEndTime());
    }

    public LogCmdMessage(Parcel in) {
        this.uri = ParcelUtils.readFromParcel(in);
        this.logId = ParcelUtils.readFromParcel(in);
        this.platform = ParcelUtils.readFromParcel(in);
        this.packageName = ParcelUtils.readFromParcel(in);
        this.startTime = ParcelUtils.readLongFromParcel(in);
        this.endTime = ParcelUtils.readLongFromParcel(in);
    }
}
