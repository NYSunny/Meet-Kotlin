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
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@MessageTag(
        value = "RC:RRRspMsg",
        flag = 0
)
public class ReadReceiptResponseMessage extends MessageContent {
    private static final String TAG = "ReadReceiptResponseMessage";
    private static final String RECEIPT_MAP = "receiptMessageDic";
    private HashMap<String, ArrayList<String>> mReceiptMap;
    public static final Creator<ReadReceiptResponseMessage> CREATOR = new Creator<ReadReceiptResponseMessage>() {
        public ReadReceiptResponseMessage createFromParcel(Parcel source) {
            return new ReadReceiptResponseMessage(source);
        }

        public ReadReceiptResponseMessage[] newArray(int size) {
            return new ReadReceiptResponseMessage[size];
        }
    };

    public ReadReceiptResponseMessage(HashMap<String, ArrayList<String>> receiptMap) {
        this.mReceiptMap = receiptMap;
    }

    public ReadReceiptResponseMessage(List<Message> messageList) {
        this.mReceiptMap = new HashMap();
        Iterator var2 = messageList.iterator();

        while(var2.hasNext()) {
            Message message = (Message)var2.next();
            String userId = message.getSenderUserId();
            String messageUId = message.getUId();
            ArrayList<String> messageUIdList = (ArrayList)this.mReceiptMap.get(userId);
            if (messageUIdList == null) {
                messageUIdList = new ArrayList();
            }

            if (!messageUIdList.contains(messageUId)) {
                messageUIdList.add(messageUId);
                this.mReceiptMap.put(userId, messageUIdList);
            }
        }

    }

    public Set<String> getSenderIdSet() {
        return this.mReceiptMap.keySet();
    }

    public ArrayList<String> getMessageUIdListBySenderId(String senderId) {
        return (ArrayList)this.mReceiptMap.get(senderId);
    }

    public ReadReceiptResponseMessage(Parcel in) {
        this.mReceiptMap = (HashMap)ParcelUtils.readMapFromParcel(in);
    }

    public ReadReceiptResponseMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException var10) {
            RLog.e("ReadReceiptResponseMessage", var10.getMessage());
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            if (jsonObj.has("receiptMessageDic")) {
                this.mReceiptMap = new HashMap();
                JSONObject valueObj = jsonObj.getJSONObject("receiptMessageDic");
                Iterator it = valueObj.keys();

                while(it.hasNext()) {
                    String key = it.next().toString();
                    JSONArray jsonArray = valueObj.getJSONArray(key);
                    ArrayList<String> messageUIdList = new ArrayList();

                    for(int i = 0; i < jsonArray.length(); ++i) {
                        messageUIdList.add(jsonArray.getString(i));
                    }

                    this.mReceiptMap.put(key, messageUIdList);
                }
            }
        } catch (JSONException var11) {
            RLog.e("ReadReceiptResponseMessage", var11.getMessage());
        }

    }

    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        JSONObject valueObj = new JSONObject();

        try {
            if (this.mReceiptMap != null) {
                Iterator var3 = this.mReceiptMap.keySet().iterator();

                while(var3.hasNext()) {
                    String key = (String)var3.next();
                    ArrayList<String> messageUIdList = (ArrayList)this.mReceiptMap.get(key);
                    JSONArray jsonArray = new JSONArray();
                    Iterator var7 = messageUIdList.iterator();

                    while(var7.hasNext()) {
                        String uId = (String)var7.next();
                        jsonArray.put(uId);
                    }

                    valueObj.put(key, jsonArray);
                }

                jsonObj.put("receiptMessageDic", valueObj);
            }
        } catch (JSONException var10) {
            RLog.e("ReadReceiptResponseMessage", "JSONException " + var10.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException var9) {
            RLog.e("ReadReceiptResponseMessage", "UnsupportedEncodingException ", var9);
            return null;
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.mReceiptMap);
    }
}
