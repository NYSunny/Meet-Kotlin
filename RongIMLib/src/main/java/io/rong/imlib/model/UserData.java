//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.telephony.TelephonyManager;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;

public final class UserData implements Parcelable {
    private UserData.PersonalInfo personalInfo;
    private UserData.AccountInfo accountInfo;
    private UserData.ContactInfo contactInfo;
    private UserData.ClientInfo clientInfo;
    private String appVersion;
    private String extra;
    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        public UserData createFromParcel(Parcel source) {
            return new UserData(source);
        }

        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public UserData(Context context) {
        this.clientInfo = new UserData.ClientInfo(context);
    }

    public UserData(Parcel in) {
        this.setPersonalInfo((UserData.PersonalInfo)ParcelUtils.readFromParcel(in, UserData.PersonalInfo.class));
        this.setAccountInfo((UserData.AccountInfo)ParcelUtils.readFromParcel(in, UserData.AccountInfo.class));
        this.setContactInfo((UserData.ContactInfo)ParcelUtils.readFromParcel(in, UserData.ContactInfo.class));
        this.clientInfo = (UserData.ClientInfo)ParcelUtils.readFromParcel(in, UserData.ClientInfo.class);
        this.setAppVersion(ParcelUtils.readFromParcel(in));
        this.setExtra(ParcelUtils.readFromParcel(in));
    }

    public UserData.PersonalInfo getPersonalInfo() {
        return this.personalInfo;
    }

    public void setPersonalInfo(UserData.PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public UserData.AccountInfo getAccountInfo() {
        return this.accountInfo;
    }

    public void setAccountInfo(UserData.AccountInfo accountInfo) {
        this.accountInfo = accountInfo;
    }

    public UserData.ContactInfo getContactInfo() {
        return this.contactInfo;
    }

    public void setContactInfo(UserData.ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public UserData.ClientInfo getClientInfo() {
        return this.clientInfo;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.personalInfo);
        ParcelUtils.writeToParcel(dest, this.accountInfo);
        ParcelUtils.writeToParcel(dest, this.contactInfo);
        ParcelUtils.writeToParcel(dest, this.clientInfo);
        ParcelUtils.writeToParcel(dest, this.appVersion);
        ParcelUtils.writeToParcel(dest, this.extra);
    }

    public static class ClientInfo implements Parcelable {
        private static final String TAG = UserData.ClientInfo.class.getSimpleName();
        String network;
        String carrier;
        String systemVersion;
        String os = "Android";
        String device;
        String mobilePhoneManufacturers;
        public static final Creator<UserData.ClientInfo> CREATOR = new Creator<UserData.ClientInfo>() {
            public UserData.ClientInfo createFromParcel(Parcel source) {
                return new UserData.ClientInfo(source);
            }

            public UserData.ClientInfo[] newArray(int size) {
                return new UserData.ClientInfo[size];
            }
        };

        public ClientInfo() {
        }

        public ClientInfo(Context context) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
                ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
                if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
                    this.network = connectivityManager.getActiveNetworkInfo().getTypeName();
                }

                if (telephonyManager != null) {
                    this.carrier = telephonyManager.getNetworkOperator();
                }
            } catch (SecurityException var4) {
                RLog.e(TAG, "ClientInfo", var4);
            }

            this.mobilePhoneManufacturers = Build.MANUFACTURER;
            this.device = Build.MODEL;
            this.systemVersion = String.valueOf(VERSION.SDK_INT);
        }

        public ClientInfo(Parcel in) {
            this.setNetwork(ParcelUtils.readFromParcel(in));
            this.setCarrier(ParcelUtils.readFromParcel(in));
            this.setSystemVersion(ParcelUtils.readFromParcel(in));
            this.setOs(ParcelUtils.readFromParcel(in));
            this.setDevice(ParcelUtils.readFromParcel(in));
            this.setMobilePhoneManufacturers(ParcelUtils.readFromParcel(in));
        }

        public String getNetwork() {
            return this.network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getCarrier() {
            return this.carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getSystemVersion() {
            return this.systemVersion;
        }

        public void setSystemVersion(String systemVersion) {
            this.systemVersion = systemVersion;
        }

        public String getOs() {
            return this.os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getDevice() {
            return this.device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getMobilePhoneManufacturers() {
            return this.mobilePhoneManufacturers;
        }

        public void setMobilePhoneManufacturers(String mobilePhoneManufacturers) {
            this.mobilePhoneManufacturers = mobilePhoneManufacturers;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            ParcelUtils.writeToParcel(dest, this.network);
            ParcelUtils.writeToParcel(dest, this.carrier);
            ParcelUtils.writeToParcel(dest, this.systemVersion);
            ParcelUtils.writeToParcel(dest, this.os);
            ParcelUtils.writeToParcel(dest, this.device);
            ParcelUtils.writeToParcel(dest, this.mobilePhoneManufacturers);
        }
    }

    public static class ContactInfo implements Parcelable {
        String tel;
        String email;
        String address;
        String qq;
        String weibo;
        String weixin;
        public static final Creator<UserData.ContactInfo> CREATOR = new Creator<UserData.ContactInfo>() {
            public UserData.ContactInfo createFromParcel(Parcel source) {
                return new UserData.ContactInfo(source);
            }

            public UserData.ContactInfo[] newArray(int size) {
                return new UserData.ContactInfo[size];
            }
        };

        public ContactInfo() {
        }

        public ContactInfo(Parcel in) {
            this.setTel(ParcelUtils.readFromParcel(in));
            this.setEmail(ParcelUtils.readFromParcel(in));
            this.setAddress(ParcelUtils.readFromParcel(in));
            this.setQQ(ParcelUtils.readFromParcel(in));
            this.setWeibo(ParcelUtils.readFromParcel(in));
            this.setWeixin(ParcelUtils.readFromParcel(in));
        }

        public String getTel() {
            return this.tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return this.address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getQQ() {
            return this.qq;
        }

        public void setQQ(String qq) {
            this.qq = qq;
        }

        public String getWeibo() {
            return this.weibo;
        }

        public void setWeibo(String weibo) {
            this.weibo = weibo;
        }

        public String getWeixin() {
            return this.weixin;
        }

        public void setWeixin(String weixin) {
            this.weixin = weixin;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            ParcelUtils.writeToParcel(dest, this.tel);
            ParcelUtils.writeToParcel(dest, this.email);
            ParcelUtils.writeToParcel(dest, this.address);
            ParcelUtils.writeToParcel(dest, this.qq);
            ParcelUtils.writeToParcel(dest, this.weibo);
            ParcelUtils.writeToParcel(dest, this.weixin);
        }
    }

    public static class AccountInfo implements Parcelable {
        String appUserId;
        String userName;
        String nickName;
        public static final Creator<UserData.AccountInfo> CREATOR = new Creator<UserData.AccountInfo>() {
            public UserData.AccountInfo createFromParcel(Parcel source) {
                return new UserData.AccountInfo(source);
            }

            public UserData.AccountInfo[] newArray(int size) {
                return new UserData.AccountInfo[size];
            }
        };

        public AccountInfo() {
        }

        public AccountInfo(Parcel in) {
            this.setAppUserId(ParcelUtils.readFromParcel(in));
            this.setUserName(ParcelUtils.readFromParcel(in));
            this.setNickName(ParcelUtils.readFromParcel(in));
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickName() {
            return this.nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAppUserId() {
            return this.appUserId;
        }

        public void setAppUserId(String appUserId) {
            this.appUserId = appUserId;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            ParcelUtils.writeToParcel(dest, this.appUserId);
            ParcelUtils.writeToParcel(dest, this.userName);
            ParcelUtils.writeToParcel(dest, this.nickName);
        }
    }

    public static class PersonalInfo implements Parcelable {
        String realName;
        String sex;
        String birthday;
        String age;
        String job;
        String portraitUri;
        String comment;
        public static final Creator<UserData.PersonalInfo> CREATOR = new Creator<UserData.PersonalInfo>() {
            public UserData.PersonalInfo createFromParcel(Parcel source) {
                return new UserData.PersonalInfo(source);
            }

            public UserData.PersonalInfo[] newArray(int size) {
                return new UserData.PersonalInfo[size];
            }
        };

        public PersonalInfo() {
        }

        public PersonalInfo(Parcel in) {
            this.setRealName(ParcelUtils.readFromParcel(in));
            this.setSex(ParcelUtils.readFromParcel(in));
            this.setBirthday(ParcelUtils.readFromParcel(in));
            this.setAge(ParcelUtils.readFromParcel(in));
            this.setJob(ParcelUtils.readFromParcel(in));
            this.setPortraitUri(ParcelUtils.readFromParcel(in));
            this.setComment(ParcelUtils.readFromParcel(in));
        }

        public String getRealName() {
            return this.realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getSex() {
            return this.sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return this.birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getAge() {
            return this.age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getJob() {
            return this.job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getPortraitUri() {
            return this.portraitUri;
        }

        public void setPortraitUri(String portraitUri) {
            this.portraitUri = portraitUri;
        }

        public String getComment() {
            return this.comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            ParcelUtils.writeToParcel(dest, this.realName);
            ParcelUtils.writeToParcel(dest, this.sex);
            ParcelUtils.writeToParcel(dest, this.birthday);
            ParcelUtils.writeToParcel(dest, this.age);
            ParcelUtils.writeToParcel(dest, this.job);
            ParcelUtils.writeToParcel(dest, this.portraitUri);
            ParcelUtils.writeToParcel(dest, this.comment);
        }
    }
}
