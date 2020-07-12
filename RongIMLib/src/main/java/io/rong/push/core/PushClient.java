//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.core;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.rong.push.PushErrorCode;
import io.rong.push.common.RLog;
import io.rong.push.core.PushProtocalStack.ConnectMessage;
import io.rong.push.core.PushProtocalStack.Message;
import io.rong.push.core.PushProtocalStack.MessageInputStream;
import io.rong.push.core.PushProtocalStack.MessageOutputStream;
import io.rong.push.core.PushProtocalStack.PingReqMessage;
import io.rong.push.core.PushProtocalStack.PublishMessage;
import io.rong.push.core.PushProtocalStack.QueryAckMessage;
import io.rong.push.core.PushProtocalStack.QueryAckMessage.QueryStatus;
import io.rong.push.core.PushProtocalStack.QueryMessage;

public class PushClient {
    private static final String TAG = "PushClient";
    private MessageInputStream in;
    private Socket socket;
    private MessageOutputStream out;
    private PushReader reader;
    private ClientListener listener;
    private ConnectStatusCallback connectCallback;
    private QueryCallback queryCallback;
    private String deviceInfo;
    private boolean running;
    private Context context;
    private String appKey;

    public PushClient(Context context, String appKey, String deviceInfo, ClientListener listener) {
        this.context = context;
        this.appKey = appKey;
        this.listener = listener;
        this.deviceInfo = deviceInfo;
        this.appKey = appKey;
    }

    public void connect(String host, int port, String deviceId, ConnectStatusCallback callback) {
        RLog.d("PushClient", "connect, deviceId = " + deviceId + ", host = " + host + ", port = " + port);
        if (this.reader != null) {
            if (this.socket != null && this.socket.isConnected()) {
                RLog.d("PushClient", "old socket is connected. Ignore this connect event.");
                return;
            }

            RLog.d("PushClient", "reset old socket.");
            this.reset();
        }

        try {
            this.socket = new Socket();
            SocketAddress address = new InetSocketAddress(host, port);
            this.socket.connect(address, 4000);
            InputStream is = this.socket.getInputStream();
            this.in = new MessageInputStream(is);
            this.out = new MessageOutputStream(this.socket.getOutputStream());
            this.connectCallback = callback;
            ConnectMessage connectMessage = new ConnectMessage(deviceId, true, 300);
            connectMessage.setWill(this.context.getPackageName(), String.format("%s-%s-%s", "AndroidPush", this.deviceInfo, "4.0.0.1"));
            connectMessage.setCredentials(this.appKey);
            this.out.writeMessage(connectMessage);
            this.reader = new PushReader();
            this.running = true;
            this.reader.start();
        } catch (Exception var8) {
            RLog.e("PushClient", "connect IOException");
            var8.printStackTrace();
            if (callback != null) {
                callback.onError();
            }
        }

    }

    public void ping() {
        try {
            if (this.socket != null && this.socket.isConnected() && this.out != null) {
                this.out.writeMessage(new PingReqMessage());
            } else if (this.listener != null) {
                this.listener.onPingFailure();
            }
        } catch (IOException var2) {
            RLog.e("PushClient", "ping IOException");
            var2.printStackTrace();
            if (this.listener != null) {
                this.listener.onPingFailure();
            }
        }

    }

    public void query(QueryMethod method, String queryInfo, String deviceId, QueryCallback callback) {
        RLog.d("PushClient", "query. topic:" + method.getMethodName() + ", queryInfo:" + queryInfo);
        this.queryCallback = callback;

        try {
            if (this.socket != null && this.socket.isConnected() && this.out != null && this.running) {
                this.out.writeMessage(new QueryMessage(method.getMethodName(), queryInfo, deviceId));
            } else {
                RLog.e("PushClient", "server has disconnected");
                this.queryCallback.onFailure(PushErrorCode.SERVER_DISCONNECTED);
            }
        } catch (IOException var6) {
            var6.printStackTrace();
            this.queryCallback.onFailure(PushErrorCode.IO_EXCEPTION);
        }

    }

    public void uninit() {
        this.listener = null;
        this.disconnect();
    }

    public void disconnect() {
        RLog.d("PushClient", "disconnect");

        try {
            if (this.reader != null) {
                this.reader.interrupt();
            }

            if (this.in != null) {
                this.in.close();
            }

            if (this.out != null) {
                this.out.close();
            }

            if (this.socket != null) {
                this.socket.close();
            }
        } catch (IOException var5) {
            RLog.e("PushClient", "disconnect IOException");
            var5.printStackTrace();
        } finally {
            this.running = false;
            this.reader = null;
            this.in = null;
            this.out = null;
            this.socket = null;
            if (this.listener != null) {
                this.listener.onDisConnected();
            }

        }

    }

    public void reset() {
        RLog.d("PushClient", "reset");

        try {
            if (this.reader != null) {
                this.reader.interrupt();
            }

            if (this.socket != null) {
                this.socket.close();
            }

            if (this.in != null) {
                this.in.close();
            }

            if (this.out != null) {
                this.out.close();
            }
        } catch (IOException var5) {
            RLog.e("PushClient", "reset IOException");
            var5.printStackTrace();
        } finally {
            this.running = false;
            this.reader = null;
            this.socket = null;
            this.in = null;
            this.out = null;
        }

    }

    private void handleMessage(Message msg) throws IOException {
        if (msg != null) {
            RLog.d("PushClient", "handleMessage, msg type = " + msg.getType());
            switch(msg.getType()) {
                case CONNACK:
                    if (this.connectCallback != null) {
                        this.connectCallback.onConnected();
                    }
                    break;
                case PINGRESP:
                    if (this.listener != null) {
                        this.listener.onPingSuccess();
                    }
                    break;
                case QUERYACK:
                    QueryAckMessage message = (QueryAckMessage)msg;
                    int status = message.getStatus();
                    RLog.d("PushClient", "queryAck status:" + status + "content:" + message.getDataAsString());
                    if (this.queryCallback != null) {
                        if (status == QueryStatus.STATUS_OK.get()) {
                            this.queryCallback.onSuccess(message.getDataAsString());
                        } else {
                            this.queryCallback.onFailure(PushErrorCode.NOT_REGISTER_IN_ADMIN);
                        }
                    }
                    break;
                case PUBLISH:
                    if (this.listener != null) {
                        PublishMessage publishMsg = (PublishMessage)msg;
                        this.listener.onMessageArrived(publishMsg);
                    }
                    break;
                case DISCONNECT:
                    if (this.listener != null) {
                        this.listener.onDisConnected();
                    }
            }

        }
    }

    public static enum QueryMethod {
        GET_PUSH_TYPE("getPushType"),
        SET_TOKEN("setToken");

        private String methodName;

        private QueryMethod(String name) {
            this.methodName = name;
        }

        public String getMethodName() {
            return this.methodName;
        }
    }

    public interface QueryCallback {
        void onSuccess(String var1);

        void onFailure(PushErrorCode var1);
    }

    public interface ConnectStatusCallback {
        void onConnected();

        void onError();
    }

    public interface ClientListener {
        void onMessageArrived(PublishMessage var1);

        void onDisConnected();

        void onPingSuccess();

        void onPingFailure();
    }

    private class PushReader extends Thread {
        private PushReader() {
        }

        public void run() {
            Message msg = null;

            try {
                while(PushClient.this.running) {
                    Thread.sleep(100L);
                    if (PushClient.this.in != null) {
                        msg = PushClient.this.in.readMessage();
                    }

                    if (msg != null) {
                        PushClient.this.handleMessage(msg);
                    }
                }
            } catch (Exception var3) {
                RLog.e("PushClient", "PushReader IOException. " + var3.getMessage());
                var3.printStackTrace();
                if (PushClient.this.listener != null) {
                    PushClient.this.listener.onDisConnected();
                }
            }

        }
    }
}
