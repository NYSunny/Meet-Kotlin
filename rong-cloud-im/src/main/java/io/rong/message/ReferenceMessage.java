//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import io.rong.common.ParcelUtils;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:ReferenceMsg",
        flag = 3,
        messageHandler = ReferenceMessageHandler.class
)
public class ReferenceMessage extends MediaMessageContent {
    private static final String TAG = "ReferenceMessage";
    private String content;
    private String referMsgUserId;
    private String objName;
    private MessageContent referMsg;
    private String extra;
    public static final Creator<ReferenceMessage> CREATOR = new Creator<ReferenceMessage>() {
        public ReferenceMessage createFromParcel(Parcel in) {
            return new ReferenceMessage(in);
        }

        public ReferenceMessage[] newArray(int size) {
            return new ReferenceMessage[size];
        }
    };

    private ReferenceMessage() {
    }

    private ReferenceMessage(String userId, MessageContent referMsg) {
        this.referMsgUserId = userId;
        this.referMsg = referMsg;
        if (referMsg == null) {
            RLog.e("ReferenceMessage", "MessageContent is null");
        } else {
            MessageTag msgTag = null;

            try {
                msgTag = (MessageTag)referMsg.getClass().getAnnotation(MessageTag.class);
            } catch (Throwable var6) {
                RLog.e("ReferenceMessage", "throwable:" + var6.toString());
            }

            if (msgTag == null) {
                RLog.e("ReferenceMessage", "MessageTag is null");
            } else {
                String[] referenceSupportMsgType = new String[]{"RC:TxtMsg", "RC:ImgMsg", "RC:FileMsg", "RC:ImgTextMsg", "RC:ReferenceMsg"};
                boolean isContains = Arrays.asList(referenceSupportMsgType).contains(msgTag.value());
                if (isContains) {
                    this.objName = msgTag.value();
                } else {
                    throw new RuntimeException("引用消息不支持此消息类型");
                }
            }
        }
    }

    public static ReferenceMessage obtainMessage(String userId, MessageContent referMsg) {
        return userId != null && referMsg != null ? new ReferenceMessage(userId, referMsg) : null;
    }

    public byte[] encode() {
        JSONObject jsonObject = new JSONObject();

        try {
            if (!TextUtils.isEmpty(this.getUserId())) {
                jsonObject.put("referMsgUserId", this.getUserId());
            }

            if (!TextUtils.isEmpty(this.getEditSendText())) {
                jsonObject.put("content", this.getEditSendText());
            }

            if (!TextUtils.isEmpty(this.getObjName())) {
                jsonObject.put("objName", this.getObjName());
            }

            if (this.getReferenceContent() != null) {
                byte[] encode = this.getReferenceContent().encode();
                String string = new String(encode, "UTF-8");
                JSONObject jsonObject1 = new JSONObject(string);
                jsonObject.putOpt("referMsg", jsonObject1);
            }

            if (!TextUtils.isEmpty(this.getExtra())) {
                jsonObject.put("extra", this.getExtra());
            }

            if (this.getJSONUserInfo() != null) {
                jsonObject.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException var6) {
            RLog.e("ReferenceMessage", "encode JSONException", var6);
        } catch (UnsupportedEncodingException var7) {
            RLog.e("ReferenceMessage", "encode UnsupportedEncodingException", var7);
        }

        try {
            return jsonObject.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var5) {
            RLog.e("ReferenceMessage", "encode jsonObject UnsupportedEncodingException", var5);
            return null;
        }
    }

    public ReferenceMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var15) {
            RLog.e("ReferenceMessage", "UnsupportedEncodingException", var15);
        }

        if (jsonStr == null) {
            RLog.e("ReferenceMessage", "jsonStr is null");
        } else {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                if (jsonObj.has("referMsgUserId")) {
                    this.setUserId(jsonObj.getString("referMsgUserId"));
                }

                if (jsonObj.has("content")) {
                    this.setEditSendText(jsonObj.getString("content"));
                }

                if (jsonObj.has("objName")) {
                    this.setObjName(jsonObj.getString("objName"));
                }

                if (jsonObj.has("referMsg") && !TextUtils.isEmpty(this.getObjName())) {
                    JSONObject jsonObject = (JSONObject)jsonObj.get("referMsg");
                    byte[] bytes = jsonObject.toString().getBytes("UTF-8");
                    String var6 = this.getObjName();
                    byte var7 = -1;
                    switch(var6.hashCode()) {
                        case -961182724:
                            if (var6.equals("RC:FileMsg")) {
                                var7 = 2;
                            }
                            break;
                        case -911587622:
                            if (var6.equals("RC:ImgTextMsg")) {
                                var7 = 3;
                            }
                            break;
                        case -623230209:
                            if (var6.equals("RC:ReferenceMsg")) {
                                var7 = 4;
                            }
                            break;
                        case 751141447:
                            if (var6.equals("RC:ImgMsg")) {
                                var7 = 1;
                            }
                            break;
                        case 1076608122:
                            if (var6.equals("RC:TxtMsg")) {
                                var7 = 0;
                            }
                    }

                    switch(var7) {
                        case 0:
                            TextMessage textMessage = new TextMessage(bytes);
                            this.setContent(textMessage);
                            break;
                        case 1:
                            ImageMessage imageMessage = new ImageMessage(bytes);
                            this.setContent(imageMessage);
                            break;
                        case 2:
                            FileMessage fileMessage = new FileMessage(bytes);
                            this.setContent(fileMessage);
                            break;
                        case 3:
                            RichContentMessage richContentMessage = new RichContentMessage(bytes);
                            this.setContent(richContentMessage);
                            break;
                        case 4:
                            ReferenceMessage referenceMessage = new ReferenceMessage(bytes);
                            this.setContent(referenceMessage);
                    }
                }

                if (jsonObj.has("extra")) {
                    this.setExtra(jsonObj.getString("extra"));
                }

                if (jsonObj.has("user")) {
                    this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
                }
            } catch (JSONException var13) {
                RLog.e("ReferenceMessage", "JSONException " + var13.getMessage());
            } catch (UnsupportedEncodingException var14) {
                RLog.e("ReferenceMessage", "ReferenceMessage UnsupportedEncodingException", var14);
            }

        }
    }

    public int describeContents() {
        return 0;
    }

    public ReferenceMessage(Parcel in) {
        this.setUserId(in.readString());
        this.setEditSendText(in.readString());
        this.setObjName(in.readString());
        this.setContent((MessageContent)ParcelUtils.readFromParcel(in, MessageContent.class));
        this.setExtra(in.readString());
        this.setUserInfo((UserInfo)ParcelUtils.readFromParcel(in, UserInfo.class));
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getUserId());
        ParcelUtils.writeToParcel(dest, this.getEditSendText());
        ParcelUtils.writeToParcel(dest, this.getObjName());
        ParcelUtils.writeToParcel(dest, this.getReferenceContent());
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }

    public MessageContent getReferenceContent() {
        return this.referMsg;
    }

    public void setContent(MessageContent referMsg) {
        this.referMsg = referMsg;
    }

    public Uri getLocalPath() {
        return this.objName.equals("RC:FileMsg") ? ((FileMessage)this.referMsg).getLocalPath() : null;
    }

    public Uri getMediaUrl() {
        return this.objName.equals("RC:FileMsg") ? ((FileMessage)this.referMsg).getMediaUrl() : null;
    }

    public void setMediaUrl(Uri mMediaUrl) {
        if (this.objName.equals("RC:FileMsg")) {
            ((FileMessage)this.referMsg).setMediaUrl(mMediaUrl);
        }

    }

    public void setLocalPath(Uri mLocalPath) {
        if (this.objName.equals("RC:FileMsg")) {
            ((FileMessage)this.referMsg).setLocalPath(mLocalPath);
        }

    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getName() {
        return this.objName.equals("RC:FileMsg") ? ((FileMessage)this.referMsg).getName() : null;
    }

    public void setName(String name) {
        if (this.objName.equals("RC:FileMsg")) {
            ((FileMessage)this.referMsg).setName(name);
        }

    }

    public String getEditSendText() {
        return this.content;
    }

    public void setEditSendText(String referenceContent) {
        this.content = referenceContent;
    }

    String getObjName() {
        return this.objName;
    }

    void setObjName(String objName) {
        this.objName = objName;
    }

    public ReferenceMessage buildSendText(String content) {
        this.content = content;
        return this;
    }

    public String getUserId() {
        return this.referMsgUserId;
    }

    public void setUserId(String userId) {
        this.referMsgUserId = userId;
    }

    public List<String> getSearchableWord() {
        List<String> words = new ArrayList();
        words.add(this.getEditSendText());
        return words;
    }
}
