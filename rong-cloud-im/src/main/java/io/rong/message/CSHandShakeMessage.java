//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:CsHs",
        flag = 0
)
public class CSHandShakeMessage extends MessageContent {
    private static final String TAG = "CSHandShakeMessage";
    private CSCustomServiceInfo customServiceInfo;
    public static final Creator<CSHandShakeMessage> CREATOR = new Creator<CSHandShakeMessage>() {
        public CSHandShakeMessage createFromParcel(Parcel source) {
            return new CSHandShakeMessage(source);
        }

        public CSHandShakeMessage[] newArray(int size) {
            return new CSHandShakeMessage[size];
        }
    };

    public CSHandShakeMessage() {
    }

    public void setCustomInfo(CSCustomServiceInfo customInfo) {
        this.customServiceInfo = customInfo;
    }

    public CSHandShakeMessage(byte[] data) {
    }

    public static CSHandShakeMessage obtain() {
        return new CSHandShakeMessage();
    }

    public CSHandShakeMessage(Parcel in) {
        this.customServiceInfo = (CSCustomServiceInfo)ParcelUtils.readFromParcel(in, CSCustomServiceInfo.class);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.customServiceInfo);
    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        JSONObject jsonObj_UserInfo = new JSONObject();
        JSONObject jsonObj_ContactInfo = new JSONObject();
        JSONObject jsonObj_requestInfo = new JSONObject();

        try {
            jsonObj_UserInfo.put("userId", this.customServiceInfo.getUserId());
            jsonObj_UserInfo.put("nickName", this.customServiceInfo.getNickName());
            jsonObj_UserInfo.put("loginName", this.customServiceInfo.getLoginName());
            jsonObj_UserInfo.put("name", this.customServiceInfo.getName());
            jsonObj_UserInfo.put("grade", this.customServiceInfo.getGrade());
            jsonObj_UserInfo.put("gender", this.customServiceInfo.getGender());
            jsonObj_UserInfo.put("birthday", this.customServiceInfo.getBirthday());
            jsonObj_UserInfo.put("age", this.customServiceInfo.getAge());
            jsonObj_UserInfo.put("profession", this.customServiceInfo.getProfession());
            jsonObj_UserInfo.put("portraitUrl", this.customServiceInfo.getPortraitUrl());
            jsonObj_UserInfo.put("province", this.customServiceInfo.getProvince());
            jsonObj_UserInfo.put("city", this.customServiceInfo.getCity());
            jsonObj_UserInfo.put("memo", this.customServiceInfo.getMemo());
            jsonObj.putOpt("userInfo", jsonObj_UserInfo);
            jsonObj_ContactInfo.put("mobileNo", this.customServiceInfo.getMobileNo());
            jsonObj_ContactInfo.put("email", this.customServiceInfo.getEmail());
            jsonObj_ContactInfo.put("address", this.customServiceInfo.getAddress());
            jsonObj_ContactInfo.put("QQ", this.customServiceInfo.getQQ());
            jsonObj_ContactInfo.put("weibo", this.customServiceInfo.getWeibo());
            jsonObj_ContactInfo.put("weixin", this.customServiceInfo.getWeixin());
            jsonObj.putOpt("contactInfo", jsonObj_ContactInfo);
            jsonObj_requestInfo.put("page", this.customServiceInfo.getPage());
            jsonObj_requestInfo.put("referrer", this.customServiceInfo.getReferrer());
            jsonObj_requestInfo.put("enterUrl", this.customServiceInfo.getEnterUrl());
            jsonObj_requestInfo.put("skillId", this.customServiceInfo.getSkillId());
            JSONArray jsonListUrl = new JSONArray();
            List<String> list = this.customServiceInfo.getListUrl();
            if (list != null && list.size() > 0) {
                int i = 0;

                for(Iterator var8 = list.iterator(); var8.hasNext(); ++i) {
                    String u = (String)var8.next();
                    jsonListUrl.put(i, u);
                }
            }

            jsonObj_requestInfo.put("listUrl", jsonListUrl);
            jsonObj_requestInfo.put("define", this.customServiceInfo.getDefine());
            jsonObj_requestInfo.put("productId", this.customServiceInfo.getProductId());
            jsonObj.put("requestInfo", jsonObj_requestInfo);
        } catch (JSONException var11) {
            var11.printStackTrace();
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var10) {
            RLog.e("CSHandShakeMessage", "UnsupportedEncodingException ", var10);
            return new byte[0];
        }
    }
}
