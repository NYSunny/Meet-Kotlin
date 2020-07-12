//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.rong.push.common.RLog;

public class PushProtocalStack {
    public PushProtocalStack() {
    }

    public static class QueryAckMessage extends RetryableMessage {
        private byte[] data;
        private int status;
        private static final int msgLen = 8;
        private int date;

        public QueryAckMessage(int messageId, int status, byte[] data) {
            this(messageId);
            this.data = data;
            this.date = (int)(System.currentTimeMillis() / 1000L);
            this.status = status;
        }

        public QueryAckMessage(int messageId) {
            super(Type.QUERYACK);
            this.setMessageId(messageId);
        }

        public QueryAckMessage(Header header) throws IOException {
            super(header);
        }

        protected int messageLength() {
            int length = 8;
            if (this.data != null && this.data.length > 0) {
                length += this.data.length;
            }

            return length;
        }

        protected void writeMessage(OutputStream out) throws IOException {
            super.writeMessage(out);
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(this.date);
            dos.writeInt(this.status);
            if (this.data != null && this.data.length > 0) {
                dos.write(this.data);
            }

            dos.flush();
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            super.readMessage(in, msgLength);
            DataInputStream dis = new DataInputStream(in);
            this.date = dis.readInt();
            this.status = dis.readInt();
            if (msgLength > 1024) {
                throw new IOException("[PushProtocalStack] Length of message is too large :  " + msgLength);
            } else {
                if (msgLength > 8) {
                    this.data = new byte[msgLength - 8];
                    dis.read(this.data);
                }

            }
        }

        public int getStatus() {
            return this.status;
        }

        public void setDup(boolean dup) {
            throw new UnsupportedOperationException("PubAck messages don't use the DUP flag.");
        }

        public void setQos(QoS qos) {
            throw new UnsupportedOperationException("PubAck messages don't use the QoS flags.");
        }

        public String getDataAsString() {
            return this.data != null ? FormatUtil.toString(this.data) : null;
        }

        public static enum QueryStatus {
            STATUS_ERROR(0),
            STATUS_OK(1),
            STATUS_NODBCONF(2),
            STATUS_PARAMERROR(3);

            private int value;

            private QueryStatus(int val) {
                this.value = val;
            }

            public int get() {
                return this.value;
            }
        }
    }

    public static class QueryMessage extends RetryableMessage {
        private String topic;
        private byte[] data;
        private String targetId;
        private long signature;

        public QueryMessage(String topic, String msg, String targetId) {
            this(topic, FormatUtil.toWMtpString(msg), targetId);
        }

        public QueryMessage(String topic, byte[] data, String targetId) {
            super(Type.QUERY);
            this.topic = topic;
            this.targetId = targetId;
            this.data = data;
            this.signature = 255L;
        }

        public QueryMessage(Header header) throws IOException {
            super(header);
        }

        protected int messageLength() {
            int length = 8;
            length = length + FormatUtil.toWMtpString(this.topic).length;
            length += FormatUtil.toWMtpString(this.targetId).length;
            length += 2;
            length += this.data.length;
            return length;
        }

        protected void writeMessage(OutputStream out) throws IOException {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeLong(this.signature);
            dos.writeUTF(this.topic);
            dos.writeUTF(this.targetId);
            dos.flush();
            super.writeMessage(out);
            dos.write(this.data);
            dos.flush();
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            int pos = 0;
            DataInputStream dis = new DataInputStream(in);
            this.signature = dis.readLong();
            this.topic = dis.readUTF();
            this.targetId = dis.readUTF();
            pos = pos + 8;
            pos += FormatUtil.toWMtpString(this.topic).length;
            pos += FormatUtil.toWMtpString(this.targetId).length;
            super.readMessage(in, msgLength);
            pos += 2;
            this.data = new byte[msgLength - pos];
            dis.read(this.data);
        }

        public String getTopic() {
            return this.topic;
        }

        public byte[] getData() {
            return this.data;
        }

        public String getTargetId() {
            return this.targetId;
        }

        public String getDataAsString() {
            return new String(this.data);
        }
    }

    public static class FormatUtil {
        public FormatUtil() {
        }

        public static String dumpByteArray(byte[] bytes) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < bytes.length; ++i) {
                byte b = bytes[i];
                int iVal = b & 255;
                int byteN = Integer.parseInt(Integer.toBinaryString(iVal));
                sb.append(String.format("%1$02d: %2$08d %3$1c %3$d\n", i, byteN, iVal));
            }

            return sb.toString();
        }

        public static byte[] toWMtpString(String s) {
            if (s == null) {
                return new byte[0];
            } else {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(byteOut);

                try {
                    dos.writeUTF(s);
                    dos.flush();
                } catch (IOException var4) {
                    return new byte[0];
                }

                return byteOut.toByteArray();
            }
        }

        public static String toString(byte[] data) {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            DataInputStream dis = new DataInputStream(bais);

            try {
                return dis.readUTF();
            } catch (IOException var4) {
                return null;
            }
        }
    }

    public abstract static class RetryableMessage extends Message {
        private int messageId;

        public RetryableMessage(Header header) throws IOException {
            super(header);
        }

        public RetryableMessage(Type type) {
            super(type);
        }

        protected int messageLength() {
            return 2;
        }

        protected void writeMessage(OutputStream out) throws IOException {
            int id = this.getMessageId();
            int lsb = id & 255;
            int msb = (id & '\uff00') >> 8;
            out.write(msb);
            out.write(lsb);
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            int msgId = in.read() * 255 + in.read();
            this.setMessageId(msgId);
        }

        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        public int getMessageId() {
            return this.messageId;
        }
    }

    public static enum QoS {
        AT_MOST_ONCE(0),
        AT_LEAST_ONCE(1),
        EXACTLY_ONCE(2),
        DEFAULT(3);

        public final int val;

        private QoS(int val) {
            this.val = val;
        }

        static QoS valueOf(int i) {
            QoS[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                QoS q = var1[var3];
                if (q.val == i) {
                    return q;
                }
            }

            throw new IllegalArgumentException("Not a valid QoS number: " + i);
        }
    }

    public static class PublishMessage extends RetryableMessage {
        private String topic;
        private byte[] data;
        private String targetId;
        private int date;

        public PublishMessage(Header header) throws IOException {
            super(header);
        }

        protected int messageLength() {
            return 0;
        }

        protected void writeMessage(OutputStream out) throws IOException {
            super.writeMessage(out);
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            int pos = 14;
            DataInputStream dis = new DataInputStream(in);
            dis.readLong();
            this.date = dis.readInt();
            this.topic = dis.readUTF();
            this.targetId = dis.readUTF();
            pos = pos + FormatUtil.toWMtpString(this.topic).length;
            pos += FormatUtil.toWMtpString(this.targetId).length;
            super.readMessage(in, msgLength);
            if (msgLength >= pos) {
                this.data = new byte[msgLength - pos];
                dis.read(this.data);
            } else {
                RLog.e("PushProtocal", "error msgLength. msgLength:" + msgLength + "pos:" + pos);
            }

        }

        public String getTopic() {
            return this.topic;
        }

        public byte[] getData() {
            return this.data;
        }

        public int getServerTime() {
            return this.date;
        }

        public String getTargetId() {
            return this.targetId;
        }

        public String getDataAsString() {
            return this.data == null ? null : FormatUtil.toString(this.data);
        }
    }

    public static class PingRespMessage extends Message {
        public PingRespMessage(Header header) throws IOException {
            super(header);
        }

        public PingRespMessage() {
            super(Type.PINGRESP);
        }
    }

    public static class PingReqMessage extends Message {
        public PingReqMessage() {
            super(Type.PINGREQ);
        }

        public PingReqMessage(Header header) throws IOException {
            super(header);
        }

        public void setDup(boolean dup) {
            throw new UnsupportedOperationException("PINGREQ message does not support the DUP flag");
        }

        public void setQos(QoS qos) {
            throw new UnsupportedOperationException("PINGREQ message does not support the QoS flag");
        }

        public void setRetained(boolean retain) {
            throw new UnsupportedOperationException("PINGREQ message does not support the RETAIN flag");
        }
    }

    public static class MessageOutputStream {
        private final OutputStream out;

        public MessageOutputStream(OutputStream out) {
            this.out = out;
        }

        public void writeMessage(Message msg) throws IOException {
            msg.write(this.out);
            this.out.flush();
        }

        public void close() throws IOException {
            this.out.close();
        }
    }

    public static class MessageInputStream implements Closeable {
        private InputStream in;

        public MessageInputStream(InputStream in) {
            this.in = in;
        }

        public Message readMessage() throws IOException {
            byte flags = (byte)this.in.read();
            Message.Header header = new Message.Header(flags);
            Message msg = null;
            if (header.getType() == null) {
                return null;
            } else {
                RLog.i("PushProtocalStack", "receive message type:" + header.getType());
                switch(header.getType()) {
                    case CONNACK:
                        msg = new ConnAckMessage(header);
                        break;
                    case PUBLISH:
                        msg = new PublishMessage(header);
                        break;
                    case PINGRESP:
                        msg = new PingRespMessage(header);
                        break;
                    case CONNECT:
                        msg = new ConnectMessage(header);
                        break;
                    case PINGREQ:
                        msg = new PingReqMessage(header);
                        break;
                    case DISCONNECT:
                        msg = new DisconnectMessage(header);
                        break;
                    case QUERY:
                        msg = new QueryMessage(header);
                        break;
                    case QUERYACK:
                        msg = new QueryAckMessage(header);
                        break;
                    default:
                        RLog.e("PushProtocalStack", "No support for deserializing" + header.getType() + "messages");
                        return null;
                }

                this.in.read();
                ((Message)msg).read(this.in);
                return (Message)msg;
            }
        }

        public void close() throws IOException {
            this.in.close();
        }
    }

    public abstract static class Message {
        private final Header header;
        private byte headerCode;

        public Message(Type type) {
            this.header = new Header(type, false, QoS.AT_MOST_ONCE, false);
        }

        public Message(Header header) throws IOException {
            this.header = header;
        }

        final void read(InputStream in) throws IOException {
            int msgLength = this.readMsgLength(in);
            this.readMessage(in, msgLength);
        }

        public final void write(OutputStream out) throws IOException {
            this.headerCode = this.header.encode();
            out.write(this.headerCode);
            this.writeMsgCode(out);
            this.writeMsgLength(out);
            this.writeMessage(out);
        }

        private int readMsgLength(InputStream in) throws IOException {
            int msgLength = 0;
            int multiplier = 1;

            int digit;
            do {
                digit = in.read();
                msgLength += (digit & 127) * multiplier;
                multiplier *= 128;
            } while((digit & 128) > 0);

            return msgLength;
        }

        private void writeMsgLength(OutputStream out) throws IOException {
            int val = this.messageLength();

            do {
                byte b = (byte)(val & 127);
                val >>= 7;
                if (val > 0) {
                    b = (byte)(b | 128);
                }

                out.write(b);
            } while(val > 0);

        }

        private void writeMsgCode(OutputStream out) throws IOException {
            int val = this.messageLength();
            int code = this.headerCode;

            do {
                byte b = (byte)(val & 127);
                val >>= 7;
                if (val > 0) {
                    b = (byte)(b | 128);
                }

                code ^= b;
            } while(val > 0);

            out.write(code);
        }

        public final byte[] toBytes() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                this.write(baos);
            } catch (IOException var3) {
            }

            return baos.toByteArray();
        }

        protected int messageLength() {
            return 0;
        }

        protected void writeMessage(OutputStream out) throws IOException {
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
        }

        public void setRetained(boolean retain) {
            this.header.retain = retain;
        }

        public boolean isRetained() {
            return this.header.retain;
        }

        public void setQos(QoS qos) {
            this.header.qos = qos;
        }

        public QoS getQos() {
            return this.header.qos;
        }

        public void setDup(boolean dup) {
            this.header.dup = dup;
        }

        public boolean isDup() {
            return this.header.dup;
        }

        public Type getType() {
            return this.header.type;
        }

        public static class Header {
            private Type type;
            private boolean retain;
            private QoS qos;
            private boolean dup;

            private Header(Type type, boolean retain, QoS qos, boolean dup) {
                this.qos = QoS.AT_MOST_ONCE;
                this.type = type;
                this.retain = retain;
                this.qos = qos;
                this.dup = dup;
            }

            public Header(byte flags) {
                this.qos = QoS.AT_MOST_ONCE;
                this.retain = (flags & 1) > 0;
                this.qos = QoS.valueOf((flags & 6) >> 1);
                this.dup = (flags & 8) > 0;
                this.type = Type.valueOf(flags >> 4 & 15);
            }

            public Type getType() {
                return this.type;
            }

            private byte encode() {
                byte b = (byte)(this.type.val << 4);
                b = (byte)(b | (this.retain ? 1 : 0));
                b = (byte)(b | this.qos.val << 1);
                b = (byte)(b | (this.dup ? 8 : 0));
                return b;
            }

            public String toString() {
                return "Header [type=" + this.type + ", retain=" + this.retain + ", qos=" + this.qos + ", dup=" + this.dup + "]";
            }
        }

        public static enum Type {
            CONNECT(1),
            CONNACK(2),
            PUBLISH(3),
            PUBACK(4),
            QUERY(5),
            QUERYACK(6),
            QUERYCON(7),
            SUBSCRIBE(8),
            SUBACK(9),
            UNSUBSCRIBE(10),
            UNSUBACK(11),
            PINGREQ(12),
            PINGRESP(13),
            DISCONNECT(14);

            private final int val;

            private Type(int val) {
                this.val = val;
            }

            static Type valueOf(int i) {
                Type[] var1 = values();
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    Type t = var1[var3];
                    if (t.val == i) {
                        return t;
                    }
                }

                return null;
            }
        }
    }

    public static class DisconnectMessage extends Message {
        public static final int MESSAGE_LENGTH = 2;
        private DisconnectionStatus status;

        public DisconnectMessage(Header header) throws IOException {
            super(header);
        }

        public DisconnectMessage(DisconnectionStatus status) {
            super(Type.DISCONNECT);
            if (status == null) {
                throw new IllegalArgumentException("The status of ConnAskMessage can't be null");
            } else {
                this.status = status;
            }
        }

        public DisconnectMessage() {
            super(Type.DISCONNECT);
        }

        protected int messageLength() {
            return 2;
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            in.read();
            int result = in.read();
            switch(result) {
                case 0:
                    this.status = DisconnectionStatus.RECONNECT;
                    break;
                case 1:
                    this.status = DisconnectionStatus.OTHER_DEVICE_LOGIN;
                    break;
                case 2:
                    this.status = DisconnectionStatus.CLOSURE;
                    break;
                default:
                    RLog.e("PushProtocol", "Unsupported DisconnectMessage status: " + result);
            }

        }

        protected void writeMessage(OutputStream out) throws IOException {
            out.write(0);
            switch(this.status) {
                case RECONNECT:
                    out.write(0);
                    break;
                case OTHER_DEVICE_LOGIN:
                    out.write(1);
                    break;
                case CLOSURE:
                    out.write(2);
                    break;
                default:
                    RLog.e("PushProtocol", "Unsupported DisconnectMessage code.");
            }

        }

        public DisconnectionStatus getStatus() {
            return this.status;
        }

        public void setDup(boolean dup) {
            throw new UnsupportedOperationException("DISCONNECT message does not support the DUP flag");
        }

        public void setQos(QoS qos) {
            throw new UnsupportedOperationException("DISCONNECT message does not support the QoS flag");
        }

        public void setRetained(boolean retain) {
            throw new UnsupportedOperationException("DISCONNECT message does not support the RETAIN flag");
        }

        public static enum DisconnectionStatus {
            RECONNECT,
            OTHER_DEVICE_LOGIN,
            CLOSURE;

            private DisconnectionStatus() {
            }
        }
    }

    public static class ConnectMessage extends Message {
        private static int CONNECT_HEADER_SIZE = 12;
        private String protocolId = "MQIsdp";
        private byte protocolVersion = 3;
        private String clientId;
        private int keepAlive;
        private String username;
        private String password;
        private boolean cleanSession;
        private String willTopic;
        private String will;
        private QoS willQoS;
        private boolean retainWill;
        private boolean hasUsername;
        private boolean hasPassword;
        private boolean hasWill;

        public ConnectMessage() {
            super(Type.CONNECT);
        }

        public ConnectMessage(Header header) throws IOException {
            super(header);
        }

        public ConnectMessage(String clientId, boolean cleanSession, int keepAlive) {
            super(Type.CONNECT);
            if (clientId != null && clientId.length() <= 64) {
                this.clientId = clientId;
                this.cleanSession = cleanSession;
                this.keepAlive = keepAlive;
            } else {
                throw new IllegalArgumentException("Client id cannot be null and must be at most 64 characters long: " + clientId);
            }
        }

        protected int messageLength() {
            int payloadSize = FormatUtil.toWMtpString(this.clientId).length;
            payloadSize += FormatUtil.toWMtpString(this.willTopic).length;
            payloadSize += FormatUtil.toWMtpString(this.will).length;
            payloadSize += FormatUtil.toWMtpString(this.username).length;
            payloadSize += FormatUtil.toWMtpString(this.password).length;
            return payloadSize + CONNECT_HEADER_SIZE;
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            DataInputStream dis = new DataInputStream(in);
            this.protocolId = dis.readUTF();
            this.protocolVersion = dis.readByte();
            byte cFlags = dis.readByte();
            this.hasUsername = (cFlags & 128) > 0;
            this.hasPassword = (cFlags & 64) > 0;
            this.retainWill = (cFlags & 32) > 0;
            this.willQoS = QoS.valueOf(cFlags >> 3 & 3);
            this.hasWill = (cFlags & 4) > 0;
            this.cleanSession = (cFlags & 32) > 0;
            this.keepAlive = dis.read() * 256 + dis.read();
            this.clientId = dis.readUTF();
            if (this.hasWill) {
                this.willTopic = dis.readUTF();
                this.will = dis.readUTF();
            }

            if (this.hasUsername) {
                try {
                    this.username = dis.readUTF();
                } catch (EOFException var7) {
                }
            }

            if (this.hasPassword) {
                try {
                    this.password = dis.readUTF();
                } catch (EOFException var6) {
                }
            }

        }

        protected void writeMessage(OutputStream out) throws IOException {
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF(this.protocolId);
            dos.write(this.protocolVersion);
            int flags = this.cleanSession ? 2 : 0;
            flags |= this.hasWill ? 4 : 0;
            flags |= this.willQoS == null ? 0 : this.willQoS.val << 3;
            flags |= this.retainWill ? 32 : 0;
            flags |= this.hasPassword ? 64 : 0;
            flags |= this.hasUsername ? 128 : 0;
            dos.write((byte)flags);
            dos.writeChar(this.keepAlive);
            dos.writeUTF(this.clientId);
            if (this.hasWill) {
                dos.writeUTF(this.willTopic);
                dos.writeUTF(this.will);
            }

            if (this.hasUsername) {
                dos.writeUTF(this.username);
            }

            if (this.hasPassword) {
                dos.writeUTF(this.password);
            }

            dos.flush();
        }

        public void setCredentials(String username) {
            this.setCredentials(username, (String)null);
        }

        public void setCredentials(String username, String password) {
            if ((username == null || username.isEmpty()) && password != null && !password.isEmpty()) {
                throw new IllegalArgumentException("It is not valid to supply a password without supplying a username.");
            } else {
                this.username = username;
                this.password = password;
                this.hasUsername = this.username != null;
                this.hasPassword = this.password != null;
            }
        }

        public void setWill(String willTopic, String will) {
            this.setWill(willTopic, will, QoS.AT_MOST_ONCE, false);
        }

        public void setWill(String willTopic, String will, QoS willQoS, boolean retainWill) {
            if (!(willTopic == null ^ will == null) && !(will == null ^ willQoS == null)) {
                this.willTopic = willTopic;
                this.will = will;
                this.willQoS = willQoS;
                this.retainWill = retainWill;
                this.hasWill = willTopic != null;
            } else {
                throw new IllegalArgumentException("Can't set willTopic, will or willQoS value independently");
            }
        }

        public void setDup(boolean dup) {
            throw new UnsupportedOperationException("CONNECT messages don't use the DUP flag.");
        }

        public void setRetained(boolean retain) {
            throw new UnsupportedOperationException("CONNECT messages don't use the RETAIN flag.");
        }

        public void setQos(QoS qos) {
            throw new UnsupportedOperationException("CONNECT messages don't use the QoS flags.");
        }

        public String getProtocolId() {
            return this.protocolId;
        }

        public byte getProtocolVersion() {
            return this.protocolVersion;
        }

        public String getClientId() {
            return this.clientId;
        }

        public int getKeepAlive() {
            return this.keepAlive;
        }

        public String getUsername() {
            return this.username;
        }

        public String getPassword() {
            return this.password;
        }

        public boolean isCleanSession() {
            return this.cleanSession;
        }

        public String getWillTopic() {
            return this.willTopic;
        }

        public String getWill() {
            return this.will;
        }

        public QoS getWillQoS() {
            return this.willQoS;
        }

        public boolean isWillRetained() {
            return this.retainWill;
        }

        public boolean hasUsername() {
            return this.hasUsername;
        }

        public boolean hasPassword() {
            return this.hasPassword;
        }

        public boolean hasWill() {
            return this.hasWill;
        }
    }

    public static class ConnAckMessage extends Message {
        public static final int MESSAGE_LENGTH = 2;
        private ConnectionStatus status;
        private String userId;

        public ConnAckMessage() {
            super(Type.CONNACK);
        }

        public ConnAckMessage(Header header) throws IOException {
            super(header);
        }

        public ConnAckMessage(ConnectionStatus status) {
            super(Type.CONNACK);
            if (status == null) {
                throw new IllegalArgumentException("The status of ConnAskMessage can't be null");
            } else {
                this.status = status;
            }
        }

        protected int messageLength() {
            int length = 2;
            if (this.userId != null && !this.userId.isEmpty()) {
                length += FormatUtil.toWMtpString(this.userId).length;
            }

            return length;
        }

        protected void readMessage(InputStream in, int msgLength) throws IOException {
            in.read();
            int result = in.read();
            switch(result) {
                case 0:
                    this.status = ConnectionStatus.ACCEPTED;
                    break;
                case 1:
                    this.status = ConnectionStatus.UNACCEPTABLE_PROTOCOL_VERSION;
                    break;
                case 2:
                    this.status = ConnectionStatus.IDENTIFIER_REJECTED;
                    break;
                case 3:
                    this.status = ConnectionStatus.SERVER_UNAVAILABLE;
                    break;
                case 4:
                    this.status = ConnectionStatus.BAD_USERNAME_OR_PASSWORD;
                    break;
                case 5:
                    this.status = ConnectionStatus.NOT_AUTHORIZED;
                    break;
                case 6:
                    this.status = ConnectionStatus.REDIRECT;
                    break;
                default:
                    RLog.e("PushProtocol", "Unsupported CONNACK code");
                    this.status = ConnectionStatus.REDIRECT;
            }

            if (msgLength > 2) {
                DataInputStream dis = new DataInputStream(in);
                this.userId = dis.readUTF();
            }

        }

        protected void writeMessage(OutputStream out) throws IOException {
            out.write(0);
            switch(this.status) {
                case ACCEPTED:
                    out.write(0);
                    break;
                case UNACCEPTABLE_PROTOCOL_VERSION:
                    out.write(1);
                    break;
                case IDENTIFIER_REJECTED:
                    out.write(2);
                    break;
                case SERVER_UNAVAILABLE:
                    out.write(3);
                    break;
                case BAD_USERNAME_OR_PASSWORD:
                    out.write(4);
                    break;
                case NOT_AUTHORIZED:
                    out.write(5);
                    break;
                case REDIRECT:
                    out.write(6);
                    break;
                default:
                    RLog.e("PushProtocol", "Unsupported CONNACK message status: " + this.status);
            }

            if (this.userId != null && !this.userId.isEmpty()) {
                DataOutputStream dos = new DataOutputStream(out);
                dos.writeUTF(this.userId);
                dos.flush();
            }

        }

        public ConnectionStatus getStatus() {
            return this.status;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setDup(boolean dup) {
            throw new UnsupportedOperationException("CONNACK messages don't use the DUP flag.");
        }

        public void setRetained(boolean retain) {
            throw new UnsupportedOperationException("CONNACK messages don't use the RETAIN flag.");
        }

        public void setQos(QoS qos) {
            throw new UnsupportedOperationException("CONNACK messages don't use the QoS flags.");
        }

        public static enum ConnectionStatus {
            ACCEPTED,
            UNACCEPTABLE_PROTOCOL_VERSION,
            IDENTIFIER_REJECTED,
            SERVER_UNAVAILABLE,
            BAD_USERNAME_OR_PASSWORD,
            NOT_AUTHORIZED,
            REDIRECT;

            private ConnectionStatus() {
            }
        }
    }
}
