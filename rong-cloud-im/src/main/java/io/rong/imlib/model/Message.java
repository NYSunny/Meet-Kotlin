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
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.push.common.RLog;

public class Message implements Parcelable {
    private static final String TAG = "Message";
    private ConversationType conversationType;
    private String targetId;
    private int messageId;
    private Message.MessageDirection messageDirection;
    private String senderUserId;
    private Message.ReceivedStatus receivedStatus;
    private Message.SentStatus sentStatus;
    private long receivedTime;
    private long sentTime;
    private long readTime;
    private String objectName;
    private MessageContent content;
    private String extra;
    private ReadReceiptInfo readReceiptInfo;
    private String UId;
    public static final Creator<Message> CREATOR = new Creator<Message>() {
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getUId() {
        return this.UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public Message() {
    }

    public Message(io.rong.imlib.NativeObject.Message msg) {
        this.conversationType = ConversationType.setValue(msg.getConversationType());
        this.targetId = msg.getTargetId();
        this.messageId = (int)msg.getMessageId();
        this.messageDirection = !msg.getMessageDirection() ? Message.MessageDirection.SEND : Message.MessageDirection.RECEIVE;
        this.senderUserId = msg.getSenderUserId();
        this.receivedStatus = new Message.ReceivedStatus(msg.getReadStatus());
        this.sentStatus = Message.SentStatus.setValue(msg.getSentStatus());
        this.receivedTime = msg.getReceivedTime();
        this.sentTime = msg.getSentTime();
        this.readTime = msg.getReadTime();
        this.objectName = msg.getObjectName();
        this.UId = msg.getUId();
        this.extra = msg.getExtra();
        this.readReceiptInfo = new ReadReceiptInfo(msg.getReadReceiptInfo());
    }

    public static Message obtain(String targetId, ConversationType type, MessageContent content) {
        Message obj = new Message();
        obj.setTargetId(targetId);
        obj.setConversationType(type);
        obj.setContent(content);
        return obj;
    }

    public ConversationType getConversationType() {
        return this.conversationType;
    }

    public void setConversationType(ConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getTargetId() {
        return this.targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public long getReadTime() {
        return this.readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public Message.MessageDirection getMessageDirection() {
        return this.messageDirection;
    }

    public void setMessageDirection(Message.MessageDirection messageDirection) {
        this.messageDirection = messageDirection;
    }

    public Message.ReceivedStatus getReceivedStatus() {
        return this.receivedStatus;
    }

    public void setReceivedStatus(Message.ReceivedStatus receivedStatus) {
        this.receivedStatus = receivedStatus;
    }

    public Message.SentStatus getSentStatus() {
        return this.sentStatus;
    }

    public void setSentStatus(Message.SentStatus sentStatus) {
        this.sentStatus = sentStatus;
    }

    public long getReceivedTime() {
        return this.receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public long getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public String getObjectName() {
        return this.objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public MessageContent getContent() {
        return this.content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getSenderUserId() {
        return this.senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public ReadReceiptInfo getReadReceiptInfo() {
        return this.readReceiptInfo;
    }

    public void setReadReceiptInfo(ReadReceiptInfo readReceiptInfo) {
        this.readReceiptInfo = readReceiptInfo;
    }

    public int describeContents() {
        return 0;
    }

    public Message(Parcel in) {
        String className = ParcelUtils.readFromParcel(in);
        Class loader;
        if (className != null) {
            try {
                loader = Class.forName(className);
            } catch (ClassNotFoundException var5) {
                loader = UnknownMessage.class;
                RLog.e("Message", "Message class catch exception :" + var5);
            }
        } else {
            loader = UnknownMessage.class;
            RLog.e("Message", "ClassName is null! Illegal messageContent");
        }

        this.setTargetId(ParcelUtils.readFromParcel(in));
        this.setMessageId(ParcelUtils.readIntFromParcel(in));
        String sendUserId = ParcelUtils.readFromParcel(in);
        this.setSenderUserId(!TextUtils.isEmpty(sendUserId) ? sendUserId : "");
        this.setReceivedTime(ParcelUtils.readLongFromParcel(in));
        this.setSentTime(ParcelUtils.readLongFromParcel(in));
        this.setReadTime(ParcelUtils.readLongFromParcel(in));
        this.setObjectName(ParcelUtils.readFromParcel(in));
        this.setContent((MessageContent)ParcelUtils.readFromParcel(in, loader));
        this.setExtra(ParcelUtils.readFromParcel(in));
        this.setUId(ParcelUtils.readFromParcel(in));
        this.setConversationType(ConversationType.setValue(ParcelUtils.readIntFromParcel(in)));
        this.setMessageDirection(Message.MessageDirection.setValue(ParcelUtils.readIntFromParcel(in)));
        this.setReceivedStatus(new Message.ReceivedStatus(ParcelUtils.readIntFromParcel(in)));
        this.setSentStatus(Message.SentStatus.setValue(ParcelUtils.readIntFromParcel(in)));
        this.setReadReceiptInfo((ReadReceiptInfo)ParcelUtils.readFromParcel(in, ReadReceiptInfo.class));
    }

    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.getContent() != null ? this.getContent().getClass().getName() : null);
        ParcelUtils.writeToParcel(dest, this.getTargetId());
        ParcelUtils.writeToParcel(dest, this.getMessageId());
        ParcelUtils.writeToParcel(dest, this.getSenderUserId());
        ParcelUtils.writeToParcel(dest, this.getReceivedTime());
        ParcelUtils.writeToParcel(dest, this.getSentTime());
        ParcelUtils.writeToParcel(dest, this.getReadTime());
        ParcelUtils.writeToParcel(dest, this.getObjectName());
        ParcelUtils.writeToParcel(dest, this.getContent());
        ParcelUtils.writeToParcel(dest, this.getExtra());
        ParcelUtils.writeToParcel(dest, this.getUId());
        ParcelUtils.writeToParcel(dest, this.getConversationType().getValue());
        ParcelUtils.writeToParcel(dest, this.getMessageDirection() == null ? 0 : this.getMessageDirection().getValue());
        ParcelUtils.writeToParcel(dest, this.getReceivedStatus() == null ? 0 : this.getReceivedStatus().getFlag());
        ParcelUtils.writeToParcel(dest, this.getSentStatus() == null ? 0 : this.getSentStatus().getValue());
        ParcelUtils.writeToParcel(dest, this.getReadReceiptInfo());
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o instanceof Message) {
            return this.messageId == ((Message)o).getMessageId();
        } else {
            return super.equals(o);
        }
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "Message{conversationType=" + this.conversationType + ", targetId='" + this.targetId + '\'' + ", messageId=" + this.messageId + ", messageDirection=" + this.messageDirection + ", senderUserId='" + this.senderUserId + '\'' + ", receivedStatus=" + this.receivedStatus + ", sentStatus=" + this.sentStatus + ", receivedTime=" + this.receivedTime + ", sentTime=" + this.sentTime + ", objectName='" + this.objectName + '\'' + ", content=" + this.content + ", extra='" + this.extra + '\'' + ", readReceiptInfo=" + this.readReceiptInfo + ", UId='" + this.UId + '\'' + '}';
    }

    public static class ReceivedStatus {
        private static final int READ = 1;
        private static final int LISTENED = 2;
        private static final int DOWNLOADED = 4;
        private static final int RETRIEVED = 8;
        private static final int MULTIPLERECEIVE = 16;
        private int flag;
        private boolean isRead;
        private boolean isListened;
        private boolean isDownload;
        private boolean isRetrieved;
        private boolean isMultipleReceive;

        public ReceivedStatus(int flag) {
            this.flag = flag;
            this.isRead = (flag & 1) == 1;
            this.isListened = (flag & 2) == 2;
            this.isDownload = (flag & 4) == 4;
            this.isRetrieved = (flag & 8) == 8;
            this.isMultipleReceive = (flag & 16) == 16;
        }

        public int getFlag() {
            return this.flag;
        }

        public boolean isRead() {
            return this.isRead;
        }

        public void setRead() {
            this.flag |= 1;
            this.isRead = true;
        }

        public boolean isListened() {
            return this.isListened;
        }

        public void setListened() {
            this.flag |= 2;
            this.isListened = true;
        }

        public boolean isDownload() {
            return this.isDownload;
        }

        public void setDownload() {
            this.flag |= 4;
            this.isDownload = true;
        }

        public boolean isRetrieved() {
            return this.isRetrieved;
        }

        public void setRetrieved() {
            this.flag |= 8;
            this.isRetrieved = true;
        }

        public boolean isMultipleReceive() {
            return this.isMultipleReceive;
        }

        public void setMultipleReceive() {
            this.flag |= 16;
            this.isMultipleReceive = true;
        }
    }

    public static enum SentStatus {
        SENDING(10),
        FAILED(20),
        SENT(30),
        RECEIVED(40),
        READ(50),
        DESTROYED(60),
        CANCELED(70);

        private int value;

        private SentStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static Message.SentStatus setValue(int code) {
            Message.SentStatus[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Message.SentStatus c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return SENDING;
        }
    }

    public static enum MessageDirection {
        SEND(1),
        RECEIVE(2);

        private int value;

        private MessageDirection(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static Message.MessageDirection setValue(int code) {
            Message.MessageDirection[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Message.MessageDirection c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return SEND;
        }
    }
}
