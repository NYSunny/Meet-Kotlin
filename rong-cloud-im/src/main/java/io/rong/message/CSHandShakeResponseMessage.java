//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.CSLMessageItem;
import io.rong.imlib.model.CustomServiceMode;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:CsHsR",
        flag = 0
)
public class CSHandShakeResponseMessage extends MessageContent {
    private static final String TAG = "CSHandShakeResponseMessage";
    private int status;
    private String msg;
    private String uid;
    private String sid;
    private String pid;
    private String companyName;
    private String companyIcon;
    private boolean isBlack;
    private boolean requiredChangMode;
    private int mode;
    private String robotName;
    private String robotLogo;
    private String robotHelloWord;
    private String robotSessionNoEva;
    private ArrayList<CSHumanEvaluateItem> humanEvaluateList = new ArrayList();
    private ArrayList<CSGroupItem> groupList = new ArrayList();
    private int userTipTime;
    private String userTipWord;
    private int adminTipTime;
    private String adminTipWord;
    private int announceMsgFlag;
    private String announceMsg;
    private int announceClickFlag;
    private String announceClickUrl;
    private boolean disableLocation;
    private int evaEntryPoint;
    private int evaType;
    private boolean reportResolveStatus;
    private int isSuspendWhenQuit;
    private int leaveMessageConfigType;
    private Uri leaveMessageWebUrl;
    private ArrayList<CSLMessageItem> leaveMessageNativeInfo = new ArrayList();
    public static final Creator<CSHandShakeResponseMessage> CREATOR = new Creator<CSHandShakeResponseMessage>() {
        public CSHandShakeResponseMessage createFromParcel(Parcel source) {
            return new CSHandShakeResponseMessage(source);
        }

        public CSHandShakeResponseMessage[] newArray(int size) {
            return new CSHandShakeResponseMessage[size];
        }
    };

    public CSHandShakeResponseMessage() {
    }

    public CSHandShakeResponseMessage(byte[] content) {
        String jsonStr = null;

        try {
            jsonStr = new String(content, "UTF-8");
        } catch (UnsupportedEncodingException var19) {
            RLog.e("CSHandShakeResponseMessage", "UnsupportedEncodingException ", var19);
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONObject jsonData = jsonObj.getJSONObject("data");
            this.status = jsonObj.optInt("status");
            this.msg = jsonObj.optString("msg");
            if (this.status != 0) {
                this.uid = jsonData.optString("uid");
                this.sid = jsonData.optString("sid");
                this.pid = jsonData.optString("pid");
                String serviceType = (String)jsonData.opt("serviceType");
                if (!TextUtils.isEmpty(serviceType)) {
                    this.mode = Integer.parseInt(serviceType);
                }

                String isblack = jsonData.optString("isblack");
                if (!TextUtils.isEmpty(isblack)) {
                    this.isBlack = Integer.parseInt(isblack) == 1;
                }

                String changeMode = (String)jsonData.opt("notAutoCha");
                if (!TextUtils.isEmpty(changeMode)) {
                    this.requiredChangMode = Integer.parseInt(changeMode) == 1;
                }

                if (TextUtils.isEmpty(jsonData.optString("userTipTime", ""))) {
                    this.userTipTime = 0;
                } else {
                    this.userTipTime = Integer.parseInt(jsonData.optString("userTipTime", ""));
                }

                this.userTipWord = jsonData.optString("userTipWord");
                if (TextUtils.isEmpty(jsonData.optString("adminTipTime", ""))) {
                    this.adminTipTime = 0;
                } else {
                    this.adminTipTime = Integer.parseInt(jsonData.optString("adminTipTime", ""));
                }

                this.adminTipWord = jsonData.optString("adminTipWord");
                this.announceMsgFlag = jsonData.optInt("announceMsgFlag", 0);
                this.announceMsg = jsonData.optString("announceMsg", "");
                this.announceClickFlag = jsonData.optInt("announceClickFlag", 0);
                this.announceClickUrl = jsonData.optString("announceClickUrl", "");
                this.robotName = jsonData.optString("robotName");
                this.robotLogo = jsonData.optString("robotIcon");
                this.robotHelloWord = jsonData.optString("robotWelcome");
                this.companyIcon = jsonData.optString("companyIcon");
                this.companyName = jsonData.optString("companyName");
                this.robotSessionNoEva = jsonData.optString("robotSessionNoEva");
                JSONArray jsonArray = jsonData.optJSONArray("evaluateList");
                this.humanEvaluateList.clear();
                String param2;
                if (jsonArray != null && jsonArray.length() > 0) {
                    for(int i = 0; i < jsonArray.length(); ++i) {
                        String param1 = jsonArray.optJSONObject(i).optString("value");
                        param2 = jsonArray.optJSONObject(i).optString("description");
                        int value;
                        if (TextUtils.isEmpty(param1)) {
                            value = 0;
                        } else {
                            value = Integer.parseInt(param1);
                        }

                        this.humanEvaluateList.add(new CSHumanEvaluateItem(value, param2));
                    }
                }

                JSONArray jsonArrayGroup = jsonData.optJSONArray("groups");
                this.groupList.clear();
                if (jsonArrayGroup != null && jsonArrayGroup.length() > 0) {
                    for(int i = 0; i < jsonArrayGroup.length(); ++i) {
                        param2 = jsonArrayGroup.optJSONObject(i).optString("id");
                        String paramName = jsonArrayGroup.optJSONObject(i).optString("name");
                        boolean param3 = jsonArrayGroup.optJSONObject(i).optBoolean("online");
                        this.groupList.add(new CSGroupItem(param2, paramName, param3));
                    }
                }

                if (jsonData.isNull("suspendWhenQuit")) {
                    this.isSuspendWhenQuit = -1;
                } else {
                    this.isSuspendWhenQuit = jsonData.optInt("suspendWhenQuit");
                }

                this.disableLocation = jsonData.optInt("disableLocation") == 1;
                JSONObject evaConfig = jsonData.optJSONObject("evaConf");
                if (evaConfig != null) {
                    this.evaEntryPoint = evaConfig.optInt("evaEntryPoint");
                    this.evaType = evaConfig.optInt("evaType");
                    this.reportResolveStatus = evaConfig.optInt("reportResolveStatus") == 1;
                }

                JSONObject leaveMsgConf = jsonData.optJSONObject("leaveMsgConf");
                if (leaveMsgConf != null) {
                    this.leaveMessageConfigType = leaveMsgConf.optInt("type");
                    JSONObject customConfig = leaveMsgConf.getJSONObject("customConf");
                    if (customConfig != null) {
                        String urlString = customConfig.optString("url");
                        if (!TextUtils.isEmpty(urlString)) {
                            this.leaveMessageWebUrl = Uri.parse(urlString);
                        }
                    }

                    JSONArray defaultConf = leaveMsgConf.optJSONArray("defaultConf");
                    this.leaveMessageNativeInfo.clear();
                    if (defaultConf != null && defaultConf.length() > 0) {
                        for(int i = 0; i < defaultConf.length(); ++i) {
                            JSONObject item = defaultConf.optJSONObject(i);
                            CSLMessageItem cslMessageItem = new CSLMessageItem();
                            cslMessageItem.setName(item.optString("name"));
                            cslMessageItem.setTitle(item.optString("title"));
                            cslMessageItem.setType(item.optString("type"));
                            cslMessageItem.setDefaultText(item.optString("defaultText"));
                            cslMessageItem.setRequired(item.optBoolean("required"));
                            cslMessageItem.setMax(item.optInt("max"));
                            cslMessageItem.setVerification(item.optString("verification"));
                            JSONArray messages = item.optJSONArray("message");
                            if (messages != null) {
                                Map<String, String> map = new HashMap();
                                map.put("empty", messages.optString(0));
                                map.put("wrong_format", messages.optString(1));
                                map.put("over_length", messages.optString(2));
                                cslMessageItem.setMessage(map);
                            }

                            this.leaveMessageNativeInfo.add(cslMessageItem);
                        }
                    }
                }
            }
        } catch (JSONException var20) {
            RLog.e("CSHandShakeResponseMessage", "JSONException " + var20.getMessage());
        }

    }

    public static CSHandShakeResponseMessage obtain() {
        return new CSHandShakeResponseMessage();
    }

    public boolean isRequiredChangMode() {
        return this.requiredChangMode;
    }

    public String getUid() {
        return this.uid;
    }

    public String getSid() {
        return this.sid;
    }

    public String getPid() {
        return this.pid;
    }

    public int getCode() {
        return this.status;
    }

    public String getMsg() {
        return this.msg;
    }

    public boolean isBlack() {
        return this.isBlack;
    }

    public CustomServiceMode getMode() {
        return CustomServiceMode.valueOf(this.mode);
    }

    public String getRobotName() {
        return this.robotName;
    }

    public String getRobotLogo() {
        return this.robotLogo;
    }

    public String getRobotHelloWord() {
        return this.robotHelloWord;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public String getCompanyIcon() {
        return this.companyIcon;
    }

    public String getRobotSessionNoEva() {
        return this.robotSessionNoEva;
    }

    public ArrayList<CSHumanEvaluateItem> getHumanEvaluateList() {
        return this.humanEvaluateList;
    }

    public ArrayList<CSGroupItem> getGroupList() {
        return this.groupList;
    }

    public int getUserTipTime() {
        return this.userTipTime;
    }

    public String getUserTipWord() {
        return this.userTipWord;
    }

    public int getAdminTipTime() {
        return this.adminTipTime;
    }

    public String getAdminTipWord() {
        return this.adminTipWord;
    }

    public int isSuspendWhenQuit() {
        return this.isSuspendWhenQuit;
    }

    public int getEntryPoint() {
        return this.evaEntryPoint;
    }

    public int getEvaType() {
        return this.evaType;
    }

    public boolean isReportResolveStatus() {
        return this.reportResolveStatus;
    }

    public int getLeaveMessageConfigType() {
        return this.leaveMessageConfigType;
    }

    public Uri getLeaveMessageWebUrl() {
        return this.leaveMessageWebUrl;
    }

    public ArrayList<CSLMessageItem> getLeaveMessageNativeInfo() {
        return this.leaveMessageNativeInfo;
    }

    public int getAnnounceMsgFlag() {
        return this.announceMsgFlag;
    }

    public String getAnnounceMsg() {
        return this.announceMsg;
    }

    public int getAnnounceClickFlag() {
        return this.announceClickFlag;
    }

    public String getAnnounceClickUrl() {
        return this.announceClickUrl;
    }

    public boolean isDisableLocation() {
        return this.disableLocation;
    }

    public byte[] encode() {
        return null;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.msg);
        dest.writeString(this.uid);
        dest.writeString(this.sid);
        dest.writeString(this.pid);
        dest.writeString(this.companyName);
        dest.writeString(this.companyIcon);
        dest.writeByte((byte)(this.isBlack ? 1 : 0));
        dest.writeByte((byte)(this.requiredChangMode ? 1 : 0));
        dest.writeInt(this.mode);
        dest.writeString(this.robotName);
        dest.writeString(this.robotLogo);
        dest.writeString(this.robotHelloWord);
        dest.writeString(this.robotSessionNoEva);
        dest.writeTypedList(this.humanEvaluateList);
        dest.writeTypedList(this.groupList);
        dest.writeInt(this.userTipTime);
        dest.writeString(this.userTipWord);
        dest.writeInt(this.adminTipTime);
        dest.writeString(this.adminTipWord);
        dest.writeInt(this.evaEntryPoint);
        dest.writeInt(this.evaType);
        dest.writeByte((byte)(this.disableLocation ? 1 : 0));
        dest.writeByte((byte)(this.reportResolveStatus ? 1 : 0));
        dest.writeInt(this.leaveMessageConfigType);
        dest.writeParcelable(this.leaveMessageWebUrl, flags);
        dest.writeTypedList(this.leaveMessageNativeInfo);
        dest.writeInt(this.isSuspendWhenQuit);
        dest.writeInt(this.announceMsgFlag);
        dest.writeString(this.announceMsg);
        dest.writeInt(this.announceClickFlag);
        dest.writeString(this.announceClickUrl);
    }

    protected CSHandShakeResponseMessage(Parcel in) {
        this.status = in.readInt();
        this.msg = in.readString();
        this.uid = in.readString();
        this.sid = in.readString();
        this.pid = in.readString();
        this.companyName = in.readString();
        this.companyIcon = in.readString();
        this.isBlack = in.readByte() != 0;
        this.requiredChangMode = in.readByte() != 0;
        this.mode = in.readInt();
        this.robotName = in.readString();
        this.robotLogo = in.readString();
        this.robotHelloWord = in.readString();
        this.robotSessionNoEva = in.readString();
        this.humanEvaluateList = in.createTypedArrayList(CSHumanEvaluateItem.CREATOR);
        this.groupList = in.createTypedArrayList(CSGroupItem.CREATOR);
        this.userTipTime = in.readInt();
        this.userTipWord = in.readString();
        this.adminTipTime = in.readInt();
        this.adminTipWord = in.readString();
        this.evaEntryPoint = in.readInt();
        this.evaType = in.readInt();
        this.disableLocation = in.readByte() != 0;
        this.reportResolveStatus = in.readByte() != 0;
        this.leaveMessageConfigType = in.readInt();
        this.leaveMessageWebUrl = (Uri)in.readParcelable(Uri.class.getClassLoader());
        this.leaveMessageNativeInfo = in.createTypedArrayList(CSLMessageItem.CREATOR);
        this.isSuspendWhenQuit = in.readInt();
        this.announceMsgFlag = in.readInt();
        this.announceMsg = in.readString();
        this.announceClickFlag = in.readInt();
        this.announceClickUrl = in.readString();
    }

    public String toString() {
        return "CSHandShakeResponseMessage{status=" + this.status + ", msg='" + this.msg + '\'' + ", uid='" + this.uid + '\'' + ", sid='" + this.sid + '\'' + ", pid='" + this.pid + '\'' + ", requiredChangMode=" + this.requiredChangMode + ", mode=" + this.mode + '}';
    }
}
