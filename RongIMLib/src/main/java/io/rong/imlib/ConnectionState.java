//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import io.rong.common.rlog.RLog;
import io.rong.imlib.NativeClient.ICodeListener;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;

public class ConnectionState {
    private static final String TAG = "ConnectionState";
    private final int UNCONNECT_CODE = 34999;
    private final int SIGNUP_CODE = 34998;
    private final int NETWORK_UNAVAILABLE_CODE = 34997;
    private final int CONNECTING_CODE = 34996;
    private final int SUSPEND_CODE = 34995;
    private ICodeListener mConnectionStatusUpdateListener;
    private ConnectionStatus currentStatus;

    public ConnectionState() {
        this.currentStatus = ConnectionStatus.UNCONNECTED;
    }

    protected ConnectionStatus getCurrentStatus() {
        return this.currentStatus;
    }

    protected void setConnectionStatusListener(ICodeListener listener) {
        this.mConnectionStatusUpdateListener = listener;
    }

    protected void initConnectStatus(int status) {
        this.currentStatus = ConnectionStatus.valueOf(status);
        RLog.i("ConnectionState", "initIpcConnectStatus " + this.currentStatus);
    }

    public void onEvent(int errorCode) {
        switch(this.currentStatus) {
            case KICKED_OFFLINE_BY_OTHER_CLIENT:
            case UNCONNECTED:
            case SIGN_OUT:
            case TIMEOUT:
            case TOKEN_INCORRECT:
            case CONN_USER_BLOCKED:
                this.unconnectedOnEvent(errorCode);
                break;
            default:
                this.defaultOnEvent(errorCode);
        }

    }

    public void unconnect() {
        this.onEvent(34999);
    }

    public void signUp() {
        this.onEvent(34998);
    }

    public void networkUnavailable() {
        this.onEvent(34997);
    }

    public void connecting() {
        this.onEvent(34996);
    }

    public void suspend() {
        this.onEvent(34995);
    }

    boolean isTerminate() {
        return this.currentStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) || this.currentStatus.equals(ConnectionStatus.UNCONNECTED) || this.currentStatus.equals(ConnectionStatus.SIGN_OUT) || this.currentStatus.equals(ConnectionStatus.TIMEOUT) || this.currentStatus.equals(ConnectionStatus.TOKEN_INCORRECT) || this.currentStatus.equals(ConnectionStatus.CONN_USER_BLOCKED);
    }

    private void unconnectedOnEvent(int code) {
        switch(code) {
            case 34996:
                this.updateCurrentStatus(ConnectionStatus.CONNECTING);
            default:
        }
    }

    private void defaultOnEvent(int code) {
        if (ErrorCode.CONNECTED.getValue() != code && ErrorCode.BIZ_ERROR_RECONNECT_SUCCESS.getValue() != code) {
            if (ErrorCode.RC_DISCONN_KICK.getValue() != code && ErrorCode.RC_CONN_OTHER_DEVICE_LOGIN.getValue() != code) {
                if (ErrorCode.RC_CONN_USER_OR_PASSWD_ERROR.getValue() == code) {
                    this.updateCurrentStatus(ConnectionStatus.TOKEN_INCORRECT);
                } else if (ErrorCode.RC_CONN_USER_BLOCKED.getValue() != code && ErrorCode.RC_DISCONN_USER_BLOCKED.getValue() != code) {
                    if (ErrorCode.RC_CONN_PROTO_VERSION_ERROR.getValue() != code && ErrorCode.RC_CONN_ID_REJECT.getValue() != code && ErrorCode.RC_CONN_NOT_AUTHRORIZED.getValue() != code && ErrorCode.RC_CONN_PACKAGE_NAME_INVALID.getValue() != code && ErrorCode.RC_CONN_APP_BLOCKED_OR_DELETED.getValue() != code && ErrorCode.RC_QUERY_ACK_NO_DATA.getValue() != code && ErrorCode.RC_MSG_DATA_INCOMPLETE.getValue() != code && ErrorCode.BIZ_ERROR_CLIENT_NOT_INIT.getValue() != code && ErrorCode.BIZ_ERROR_DATABASE_ERROR.getValue() != code && ErrorCode.BIZ_ERROR_INVALID_PARAMETER.getValue() != code && ErrorCode.BIZ_ERROR_NO_CHANNEL.getValue() != code && ErrorCode.BIZ_ERROR_CONNECTING.getValue() != code) {
                        if (ErrorCode.RC_MSG_SEND_FAIL.getValue() != code && ErrorCode.RC_NET_CHANNEL_INVALID.getValue() != code && ErrorCode.RC_NET_UNAVAILABLE.getValue() != code && ErrorCode.RC_MSG_RESP_TIMEOUT.getValue() != code && ErrorCode.RC_HTTP_SEND_FAIL.getValue() != code && ErrorCode.RC_HTTP_REQ_TIMEOUT.getValue() != code && ErrorCode.RC_HTTP_RECV_FAIL.getValue() != code && ErrorCode.RC_NAVI_RESOURCE_ERROR.getValue() != code && ErrorCode.RC_NODE_NOT_FOUND.getValue() != code && ErrorCode.RC_DOMAIN_NOT_RESOLVE.getValue() != code && ErrorCode.RC_SOCKET_NOT_CREATED.getValue() != code && ErrorCode.RC_SOCKET_DISCONNECTED.getValue() != code && ErrorCode.RC_PONG_RECV_FAIL.getValue() != code && ErrorCode.RC_CONN_ACK_TIMEOUT.getValue() != code && ErrorCode.RC_CONN_OVERFREQUENCY.getValue() != code && ErrorCode.RC_CONN_REFUSED.getValue() != code && ErrorCode.RC_CONN_REDIRECTED.getValue() != code && ErrorCode.RC_NETWORK_IS_DOWN_OR_UNREACHABLE.getValue() != code && ErrorCode.RC_CONN_SERVER_UNAVAILABLE.getValue() != code) {
                            if (34999 == code) {
                                this.updateCurrentStatus(ConnectionStatus.UNCONNECTED);
                            } else if (34998 == code) {
                                this.updateCurrentStatus(ConnectionStatus.SIGN_OUT);
                            } else if (34997 == code) {
                                this.updateCurrentStatus(ConnectionStatus.NETWORK_UNAVAILABLE);
                            } else if (34996 == code) {
                                this.updateCurrentStatus(ConnectionStatus.CONNECTING);
                            } else if (34995 == code) {
                                this.updateCurrentStatus(ConnectionStatus.SUSPEND);
                            } else {
                                RLog.e("ConnectionState", "errorCodeToConnectionStatus unknown code : " + code);
                                this.updateCurrentStatus(ConnectionStatus.SUSPEND);
                            }
                        } else {
                            this.updateCurrentStatus(ConnectionStatus.SUSPEND);
                        }
                    } else {
                        this.updateCurrentStatus(ConnectionStatus.UNCONNECTED);
                    }
                } else {
                    this.updateCurrentStatus(ConnectionStatus.CONN_USER_BLOCKED);
                }
            } else {
                this.updateCurrentStatus(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT);
            }
        } else {
            this.updateCurrentStatus(ConnectionStatus.CONNECTED);
        }

    }

    private void updateCurrentStatus(ConnectionStatus status) {
        if (this.currentStatus != status) {
            FwLog.write(4, 1, LogTag.L_CONNECT_S.getTag(), "last_status|status", new Object[]{this.currentStatus, status});
            this.currentStatus = status;
            if (this.mConnectionStatusUpdateListener != null) {
                this.mConnectionStatusUpdateListener.onChanged(status.getValue());
            }
        }

    }
}
