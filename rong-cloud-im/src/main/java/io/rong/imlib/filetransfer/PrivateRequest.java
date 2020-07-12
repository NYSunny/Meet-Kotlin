//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.imlib.filetransfer.FtConst.MimeType;
import io.rong.imlib.model.FileInfo;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONObject;

public class PrivateRequest extends Request {
    private static final String Boundary = "--526f6e67436c6f7564";
    private static final String TAG = "rc_url";
    private static final String TYPE = "type";
    private static final String PATH = "path";

    public PrivateRequest(Configuration config, RequestCallBack requestCallBack) {
        super(config, requestCallBack);
    }

    public PrivateRequest(FileInfo fileInfo, Configuration config, RequestCallBack requestCallBack, String pausePath) {
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
        formData = formData + this.fileNameEncoding(this.url.substring(this.url.lastIndexOf("/") + 1));
        formData = formData + "\"\r\nContent-Type: ";
        formData = formData + this.mimeType.getName();
        formData = formData + "\r\n\r\n";
        return formData;
    }

    public String getUploadedUrl(String data) {
        String url = null;
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject body = jsonObject.getJSONObject("rc_url");
                int type = body.getInt("type");
                String path = body.getString("path");
                if (type == 1) {
                    url = path;
                } else {
                    url = super.serverIp + path;
                }
            } catch (Exception var7) {
                RLog.e("rc_url", "getUploadedUrl error:" + data);
                RLog.e("rc_url", "getUploadedUrl ", var7);
            }
        }

        return url;
    }

    private String fileNameEncoding(String fileName) {
        try {
            return URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            RLog.e("rc_url", "fileNameEncoding ", var3);
            return fileName;
        }
    }
}
