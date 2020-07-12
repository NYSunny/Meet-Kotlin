//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import io.rong.imlib.filetransfer.FtConst.MimeType;
import io.rong.imlib.model.FileInfo;
import java.io.File;

public class QiniuRequest extends Request {
    private static final String Boundary = "--526f6e67436c6f7564";

    public QiniuRequest(Configuration config, RequestCallBack requestCallBack) {
        super(config, requestCallBack);
    }

    public QiniuRequest(FileInfo fileInfo, Configuration config, RequestCallBack requestCallBack, String pausePath) {
        super(fileInfo, config, requestCallBack, pausePath);
    }

    public String getContentType() {
        return "multipart/form-data; boundary=--526f6e67436c6f7564";
    }

    public long getContentLength() {
        File file = new File(this.url);
        String end = "\r\n----526f6e67436c6f7564--";
        return (long)this.getFormData().length() + file.length() + (long)end.length();
    }

    public MimeType getMimeType() {
        return this.mimeType;
    }

    public String getBoundary() {
        return "--526f6e67436c6f7564";
    }

    public String getFormData() {
        String formData = "--";
        formData = formData + "--526f6e67436c6f7564";
        formData = formData + "\r\nContent-Disposition: form-data; name=\"token\"\r\n\r\n";
        formData = formData + this.token;
        formData = formData + "\r\n--";
        formData = formData + "--526f6e67436c6f7564";
        formData = formData + "\r\nContent-Disposition: form-data; name=\"key\"\r\n\r\n";
        formData = formData + this.fileName;
        formData = formData + "\r\n--";
        formData = formData + "--526f6e67436c6f7564";
        formData = formData + "\r\nContent-Disposition: form-data; name=\"file\"; filename=\"";
        formData = formData + this.fileName;
        formData = formData + "\"\r\nContent-Type: ";
        formData = formData + this.mimeType.getName();
        formData = formData + "\r\n\r\n";
        return formData;
    }

    public String getUploadedUrl(String data) {
        return null;
    }
}
