//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class CSEvaluateItem implements Parcelable {
    private String configId;
    private String companyId;
    private String groupId;
    private String groupName;
    private String labelId;
    private List<String> labelNameList = new ArrayList();
    private boolean isQuestionFlag;
    private int score;
    private String scoreExplain;
    private boolean isTagMust;
    private boolean isInputMust;
    private String inputLanguage;
    private long createTime;
    private int settingMode;
    private long updateTime;
    private int operateType;
    public static final Creator<CSEvaluateItem> CREATOR = new Creator<CSEvaluateItem>() {
        public CSEvaluateItem createFromParcel(Parcel source) {
            return new CSEvaluateItem(source);
        }

        public CSEvaluateItem[] newArray(int size) {
            return new CSEvaluateItem[size];
        }
    };

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigId() {
        return this.configId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyId() {
        return this.companyId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelId() {
        return this.labelId;
    }

    public void setLabelNameList(List<String> labelNameList) {
        this.labelNameList = labelNameList;
    }

    public List<String> getLabelNameList() {
        return this.labelNameList;
    }

    public void setQuestionFlag(boolean questionFlag) {
        this.isQuestionFlag = questionFlag;
    }

    public boolean getQuestionFlag() {
        return this.isQuestionFlag;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setScoreExplain(String scoreExplain) {
        this.scoreExplain = scoreExplain;
    }

    public String getScoreExplain() {
        return this.scoreExplain;
    }

    public void setTagMust(boolean tagMust) {
        this.isTagMust = tagMust;
    }

    public boolean getTagMust() {
        return this.isTagMust;
    }

    public void setInputMust(boolean inputMust) {
        this.isInputMust = inputMust;
    }

    public boolean getInputMust() {
        return this.isInputMust;
    }

    public void setInputLanguage(String inputLanguage) {
        this.inputLanguage = inputLanguage;
    }

    public String getInputLanguage() {
        return this.inputLanguage;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setSettingMode(int settingMode) {
        this.settingMode = settingMode;
    }

    public int getSettingMode() {
        return this.settingMode;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setOperateType(int operateType) {
        this.operateType = operateType;
    }

    public int getOperateType() {
        return this.operateType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.configId);
        dest.writeString(this.companyId);
        dest.writeString(this.groupId);
        dest.writeString(this.groupName);
        dest.writeString(this.labelId);
        dest.writeStringList(this.labelNameList);
        dest.writeByte((byte)(this.isQuestionFlag ? 1 : 0));
        dest.writeInt(this.score);
        dest.writeString(this.scoreExplain);
        dest.writeByte((byte)(this.isTagMust ? 1 : 0));
        dest.writeByte((byte)(this.isInputMust ? 1 : 0));
        dest.writeString(this.inputLanguage);
        dest.writeLong(this.createTime);
        dest.writeInt(this.settingMode);
        dest.writeLong(this.updateTime);
        dest.writeInt(this.operateType);
    }

    public CSEvaluateItem() {
    }

    protected CSEvaluateItem(Parcel in) {
        this.configId = in.readString();
        this.companyId = in.readString();
        this.groupId = in.readString();
        this.groupName = in.readString();
        this.labelId = in.readString();
        this.labelNameList = in.createStringArrayList();
        this.isQuestionFlag = in.readByte() != 0;
        this.score = in.readInt();
        this.scoreExplain = in.readString();
        this.isTagMust = in.readByte() != 0;
        this.isInputMust = in.readByte() != 0;
        this.inputLanguage = in.readString();
        this.createTime = in.readLong();
        this.settingMode = in.readInt();
        this.updateTime = in.readLong();
        this.operateType = in.readInt();
    }
}
