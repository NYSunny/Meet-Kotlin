//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import io.rong.imlib.filetransfer.FtConst.MimeType;

public class RequestOption {
    private int start;
    private int end;
    private MimeType mimeType;
    private String serverIp;
    private RequestCallBack requestCallBack;
    private String fileName;
    private int messageId;

    public RequestOption(String fileName, MimeType mimeType, RequestCallBack requestCallBack) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.requestCallBack = requestCallBack;
    }

    public RequestOption(String fileName, MimeType mimeType, int messageId, RequestCallBack requestCallBack) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.requestCallBack = requestCallBack;
        this.messageId = messageId;
    }

    public RequestOption(String fileName, MimeType mimeType, String serverIp, RequestCallBack requestCallBack) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.serverIp = serverIp;
        this.requestCallBack = requestCallBack;
    }

    public RequestOption(int start, int end, MimeType mimeType, String serverIp, RequestCallBack requestCallBack) {
        this.start = start;
        this.end = end;
        this.mimeType = mimeType;
        this.serverIp = serverIp;
        this.requestCallBack = requestCallBack;
    }

    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public MimeType getMimeType() {
        return this.mimeType;
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public RequestCallBack getRequestCallBack() {
        return this.requestCallBack;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getMessageId() {
        return this.messageId;
    }
}
