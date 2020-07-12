//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push;

public enum PushErrorCode {
    UNKNOWN(-1, "unknown code"),
    IO_EXCEPTION(50001, "IOException"),
    PARAMETER_ERROR(50002, "the parameter is error."),
    NOT_REGISTER_IN_ADMIN(50003, "haven't registered the third party push from your admin"),
    SERVER_DISCONNECTED(50004, "the socket is disconnect."),
    NOT_SUPPORT_BY_OFFICIAL_PUSH(50005, "this device is not support by official push.");

    private int code;
    private String msg;

    private PushErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }
}
