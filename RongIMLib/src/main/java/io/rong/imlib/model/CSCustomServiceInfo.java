//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.RongIMClient;
import java.util.ArrayList;
import java.util.List;

public class CSCustomServiceInfo implements Parcelable {
    private static final String TAG = "CSCustomServiceInfo";
    private String userId = "";
    private String nickName = "";
    private String loginName = "";
    private String name = "";
    private String grade = "";
    private String gender = "";
    private String birthday = "";
    private String age = "";
    private String profession = "";
    private String portraitUrl = "";
    private String province = "";
    private String city = "";
    private String memo = "";
    private String mobileNo = "";
    private String email = "";
    private String address = "";
    private String QQ = "";
    private String weibo = "";
    private String weixin = "";
    private String page = "";
    private String referrer = "";
    private String enterUrl = "";
    private String skillId = "";
    private List<String> listUrl = new ArrayList();
    private String define = "";
    private String productId = "";
    public static final Creator<CSCustomServiceInfo> CREATOR = new Creator<CSCustomServiceInfo>() {
        public CSCustomServiceInfo createFromParcel(Parcel source) {
            return new CSCustomServiceInfo(source);
        }

        public CSCustomServiceInfo[] newArray(int size) {
            return new CSCustomServiceInfo[size];
        }
    };

    CSCustomServiceInfo() {
        if (RongIMClient.getInstance() != null) {
            this.nickName = RongIMClient.getInstance().getCurrentUserId();
        } else {
            RLog.e("CSCustomServiceInfo", "JSONException CSCustomServiceInfo: RongIMClient.getInstance() is null");
        }

    }

    public CSCustomServiceInfo(Parcel in) {
        this.userId = ParcelUtils.readFromParcel(in);
        this.nickName = ParcelUtils.readFromParcel(in);
        this.loginName = ParcelUtils.readFromParcel(in);
        this.name = ParcelUtils.readFromParcel(in);
        this.grade = ParcelUtils.readFromParcel(in);
        this.gender = ParcelUtils.readFromParcel(in);
        this.birthday = ParcelUtils.readFromParcel(in);
        this.age = ParcelUtils.readFromParcel(in);
        this.profession = ParcelUtils.readFromParcel(in);
        this.portraitUrl = ParcelUtils.readFromParcel(in);
        this.province = ParcelUtils.readFromParcel(in);
        this.city = ParcelUtils.readFromParcel(in);
        this.memo = ParcelUtils.readFromParcel(in);
        this.mobileNo = ParcelUtils.readFromParcel(in);
        this.email = ParcelUtils.readFromParcel(in);
        this.address = ParcelUtils.readFromParcel(in);
        this.QQ = ParcelUtils.readFromParcel(in);
        this.weibo = ParcelUtils.readFromParcel(in);
        this.weixin = ParcelUtils.readFromParcel(in);
        this.page = ParcelUtils.readFromParcel(in);
        this.referrer = ParcelUtils.readFromParcel(in);
        this.enterUrl = ParcelUtils.readFromParcel(in);
        this.skillId = ParcelUtils.readFromParcel(in);
        this.listUrl = ParcelUtils.readListFromParcel(in, String.class);
        this.define = ParcelUtils.readFromParcel(in);
        this.productId = ParcelUtils.readFromParcel(in);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getLoginName() {
        return this.loginName;
    }

    public String getName() {
        return this.name;
    }

    public String getGrade() {
        return this.grade;
    }

    public String getGender() {
        return this.gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getAge() {
        return this.age;
    }

    public String getProfession() {
        return this.profession;
    }

    public String getPortraitUrl() {
        return this.portraitUrl;
    }

    public String getProvince() {
        return this.province;
    }

    public String getCity() {
        return this.city;
    }

    public String getMemo() {
        return this.memo;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAddress() {
        return this.address;
    }

    public String getQQ() {
        return this.QQ;
    }

    public String getWeibo() {
        return this.weibo;
    }

    public String getWeixin() {
        return this.weixin;
    }

    public String getPage() {
        return this.page;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public String getEnterUrl() {
        return this.enterUrl;
    }

    public String getSkillId() {
        return this.skillId;
    }

    public String getDefine() {
        return this.define;
    }

    public List<String> getListUrl() {
        return this.listUrl;
    }

    public String getProductId() {
        return this.productId;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.userId);
        ParcelUtils.writeToParcel(dest, this.nickName);
        ParcelUtils.writeToParcel(dest, this.loginName);
        ParcelUtils.writeToParcel(dest, this.name);
        ParcelUtils.writeToParcel(dest, this.grade);
        ParcelUtils.writeToParcel(dest, this.gender);
        ParcelUtils.writeToParcel(dest, this.birthday);
        ParcelUtils.writeToParcel(dest, this.age);
        ParcelUtils.writeToParcel(dest, this.profession);
        ParcelUtils.writeToParcel(dest, this.portraitUrl);
        ParcelUtils.writeToParcel(dest, this.province);
        ParcelUtils.writeToParcel(dest, this.city);
        ParcelUtils.writeToParcel(dest, this.memo);
        ParcelUtils.writeToParcel(dest, this.mobileNo);
        ParcelUtils.writeToParcel(dest, this.email);
        ParcelUtils.writeToParcel(dest, this.address);
        ParcelUtils.writeToParcel(dest, this.QQ);
        ParcelUtils.writeToParcel(dest, this.weibo);
        ParcelUtils.writeToParcel(dest, this.weixin);
        ParcelUtils.writeToParcel(dest, this.page);
        ParcelUtils.writeToParcel(dest, this.referrer);
        ParcelUtils.writeToParcel(dest, this.enterUrl);
        ParcelUtils.writeToParcel(dest, this.skillId);
        ParcelUtils.writeToParcel(dest, this.listUrl);
        ParcelUtils.writeToParcel(dest, this.define);
        ParcelUtils.writeToParcel(dest, this.productId);
    }

    public static class Builder {
        private String userId;
        private String nickName;
        private String loginName;
        private String name;
        private String grade;
        private String gender;
        private String birthday;
        private String age;
        private String profession;
        private String portraitUrl;
        private String province;
        private String city;
        private String memo;
        private String mobileNo;
        private String email;
        private String address;
        private String QQ;
        private String weibo;
        private String weixin;
        private String page = "";
        private String referrer = "";
        private String enterUrl = "";
        private String skillId = "";
        private List<String> listUrl = new ArrayList();
        private String define = "";
        private String productId = "";

        public Builder() {
        }

        public CSCustomServiceInfo build() {
            if (RongIMClient.getInstance() == null) {
                return null;
            } else {
                CSCustomServiceInfo message = new CSCustomServiceInfo();
                message.userId = this.userId != null ? this.userId : "";
                message.nickName = this.nickName != null ? this.nickName : RongIMClient.getInstance().getCurrentUserId();
                message.loginName = this.loginName != null ? this.loginName : "";
                message.name = this.name != null ? this.name : "";
                message.grade = this.grade != null ? this.grade : "";
                message.gender = this.gender != null ? this.gender : "";
                message.birthday = this.birthday != null ? this.birthday : "";
                message.age = this.age != null ? this.age : "";
                message.profession = this.profession != null ? this.profession : "";
                message.portraitUrl = this.portraitUrl != null ? this.portraitUrl : "";
                message.province = this.province != null ? this.province : "";
                message.city = this.city != null ? this.city : "";
                message.memo = this.memo != null ? this.memo : "";
                message.mobileNo = this.mobileNo != null ? this.mobileNo : "";
                message.email = this.email != null ? this.email : "";
                message.address = this.address != null ? this.address : "";
                message.QQ = this.QQ != null ? this.QQ : "";
                message.weibo = this.weibo != null ? this.weibo : "";
                message.weixin = this.weixin != null ? this.weixin : "";
                message.page = this.page != null ? this.page : "";
                message.referrer = this.referrer != null ? this.referrer : "";
                message.enterUrl = this.enterUrl != null ? this.enterUrl : "";
                message.skillId = this.skillId != null ? this.skillId : "";
                message.listUrl = this.listUrl;
                message.define = this.define != null ? this.define : "";
                message.productId = this.productId != null ? this.productId : "";
                return message;
            }
        }

        public CSCustomServiceInfo.Builder page(String page) {
            if (!TextUtils.isEmpty(page)) {
                this.page = page;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder nickName(String nickName) {
            if (!TextUtils.isEmpty(nickName)) {
                this.nickName = nickName;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder gender(String gender) {
            if (!TextUtils.isEmpty(gender)) {
                this.gender = gender;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder mobileNo(String mobileNo) {
            if (!TextUtils.isEmpty(mobileNo)) {
                this.mobileNo = mobileNo;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder memo(String memo) {
            if (!TextUtils.isEmpty(memo)) {
                this.memo = memo;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder name(String name) {
            if (!TextUtils.isEmpty(name)) {
                this.name = name;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder grade(String grade) {
            if (!TextUtils.isEmpty(grade)) {
                this.grade = grade;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder skillId(String skillId) {
            if (!TextUtils.isEmpty(skillId)) {
                this.skillId = skillId;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder userId(String userId) {
            if (!TextUtils.isEmpty(userId)) {
                this.userId = userId;
            }

            return this;
        }

        public CSCustomServiceInfo.Builder city(String city) {
            this.city = city;
            return this;
        }

        public CSCustomServiceInfo.Builder referrer(String referrer) {
            this.referrer = referrer;
            return this;
        }

        public CSCustomServiceInfo.Builder enterUrl(String enterUrl) {
            this.enterUrl = enterUrl;
            return this;
        }

        public CSCustomServiceInfo.Builder province(String province) {
            this.province = province;
            return this;
        }

        public CSCustomServiceInfo.Builder loginName(String loginName) {
            this.loginName = loginName;
            return this;
        }

        public CSCustomServiceInfo.Builder define(String define) {
            this.define = define;
            return this;
        }

        public CSCustomServiceInfo.Builder birthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public CSCustomServiceInfo.Builder age(String age) {
            this.age = age;
            return this;
        }

        public CSCustomServiceInfo.Builder profession(String profession) {
            this.profession = profession;
            return this;
        }

        public CSCustomServiceInfo.Builder portraitUrl(String portraitUrl) {
            this.portraitUrl = portraitUrl;
            return this;
        }

        public CSCustomServiceInfo.Builder email(String email) {
            this.email = email;
            return this;
        }

        public CSCustomServiceInfo.Builder address(String address) {
            this.address = address;
            return this;
        }

        public CSCustomServiceInfo.Builder QQ(String QQ) {
            this.QQ = QQ;
            return this;
        }

        public CSCustomServiceInfo.Builder weibo(String weibo) {
            this.weibo = weibo;
            return this;
        }

        public CSCustomServiceInfo.Builder weixin(String weixin) {
            this.weixin = weixin;
            return this;
        }

        public CSCustomServiceInfo.Builder listUrl(List<String> listUrl) {
            if (listUrl != null && !listUrl.isEmpty()) {
                this.listUrl.addAll(listUrl);
            }

            return this;
        }

        public CSCustomServiceInfo.Builder productId(String productId) {
            this.productId = productId;
            return this;
        }
    }
}
