//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import io.rong.imlib.filetransfer.FtConst.MimeType;
import io.rong.imlib.model.FileInfo;
import java.io.File;

public class BaiDuRequest extends Request {
    private static final String Boundary = "--526f6e67436c6f7564";

    public BaiDuRequest(Configuration config, RequestCallBack requestCallBack) {
        super(config, requestCallBack);
    }

    public BaiDuRequest(FileInfo fileInfo, Configuration config, RequestCallBack requestCallBack, String pausedPath) {
        super(fileInfo, config, requestCallBack, pausedPath);
    }

    public String getContentType() {
        return this.mimeType.getName();
    }

    public long getContentLength() {
        File file = new File(this.url);
        return (long)this.getFormData().length() + file.length();
    }

    public MimeType getMimeType() {
        return this.mimeType;
    }

    public String getBoundary() {
        return "--526f6e67436c6f7564";
    }

    public String getFormData() {
        return "";
    }

    public String getUploadedUrl(String data) {
        return this.serverIp;
    }
}
