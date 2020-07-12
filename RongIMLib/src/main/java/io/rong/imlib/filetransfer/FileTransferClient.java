//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.filetransfer.FtConst.ServiceType;
import io.rong.imlib.model.FileInfo;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FileTransferClient {
    private static final String TAG = FileTransferClient.class.getSimpleName();
    private Configuration configuration;
    private CallDispatcher dispatcher;
    private ServiceType serviceType;
    private static FileTransferClient sInstance;
    private String mediaPath;
    private Context mContext;
    private Map<String, Boolean> downloadMap;

    private FileTransferClient(Configuration config) {
        this.serviceType = ServiceType.QI_NIU;
        this.downloadMap = new HashMap();
        this.configuration = config;
        this.dispatcher = new CallDispatcher();
    }

    public static void init(Configuration config) {
        if (sInstance == null) {
            sInstance = new FileTransferClient(config);
        }

    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceType getServiceType() {
        return this.serviceType;
    }

    public static FileTransferClient getInstance() {
        return sInstance;
    }

    public void upload(int id, String url, String token, String date, @NonNull RequestOption option) {
        Object request;
        if (this.serviceType == ServiceType.QI_NIU) {
            request = new QiniuRequest(this.configuration, option.getRequestCallBack());
        } else if (this.serviceType == ServiceType.BAI_DU) {
            request = new BaiDuRequest(this.configuration, option.getRequestCallBack());
        } else {
            request = new PrivateRequest(this.configuration, option.getRequestCallBack());
        }

        ((Request)request).setContext(this.mContext);
        ((Request)request).token = token;
        ((Request)request).date = date;
        ((Request)request).mimeType = option.getMimeType();
        ((Request)request).method = "POST";
        ((Request)request).serverIp = option.getServerIp();
        ((Request)request).url = url;
        ((Request)request).tag = id;
        ((Request)request).fileName = option.getFileName();
        ((Request)request).isMessage = true;
        ((Request)request).requestCallBack = option.getRequestCallBack();
        Call call = Call.create(this.dispatcher, (Request)request);
        call.enqueue();
    }

    public void download(int id, String url, long length, @NonNull RequestOption option) {
        FileInfo info = new FileInfo("", url);
        info.setFileName(option.getFileName());
        info.setStop(false);
        info.setMessageId(option.getMessageId());
        String pausePath = FileUtils.getTempFilePath(this.mContext, id);
        Object request;
        if (this.serviceType == ServiceType.QI_NIU) {
            request = new QiniuRequest(info, this.configuration, option.getRequestCallBack(), pausePath);
        } else {
            request = new PrivateRequest(info, this.configuration, option.getRequestCallBack(), pausePath);
        }

        ((Request)request).mimeType = option.getMimeType();
        ((Request)request).method = "GET";
        ((Request)request).url = url;
        ((Request)request).tag = id;
        ((Request)request).fileName = option.getFileName();
        ((Request)request).fileLength = length;
        ((Request)request).isMessage = true;
        ((Request)request).requestCallBack = option.getRequestCallBack();
        Call call = Call.create(this.dispatcher, (Request)request);
        call.enqueue();
    }

    public void download(String uid, String url, @NonNull RequestOption option) {
        FileInfo info = new FileInfo("", url);
        info.setFileName(option.getFileName());
        info.setStop(false);
        info.setMessageId(option.getMessageId());
        String pausePath = FileUtils.getTempFilePath(this.mContext, uid);
        Object request;
        if (this.serviceType == ServiceType.QI_NIU) {
            request = new QiniuRequest(info, this.configuration, option.getRequestCallBack(), pausePath);
        } else {
            request = new PrivateRequest(info, this.configuration, option.getRequestCallBack(), pausePath);
        }

        ((Request)request).mimeType = option.getMimeType();
        ((Request)request).method = "GET";
        ((Request)request).url = url;
        ((Request)request).tag = uid;
        ((Request)request).fileName = option.getFileName();
        ((Request)request).requestCallBack = option.getRequestCallBack();
        ((Request)request).isMessage = false;
        Call call = Call.create(this.dispatcher, (Request)request);
        call.enqueue();
    }

    public void cancel(int id, CancelCallback callback) {
        if (id > 0) {
            this.dispatcher.cancel(id, callback);
        }

    }

    public void pause(int id, PauseCallback callback) {
        if (id > 0) {
            this.dispatcher.pause(id, callback);
        }

    }

    public void pause(String tag, PauseCallback callback) {
        if (!TextUtils.isEmpty(tag)) {
            this.dispatcher.pause(tag, callback);
        }

    }

    public void cancelAll() {
        this.dispatcher.cancelAll();
        if (this.downloadMap != null) {
            this.downloadMap.clear();
        }

    }

    public String getMediaPath() {
        return this.mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public static boolean checkSupportResumeTransfer(String url) {
        HttpURLConnection conn = null;
        boolean support = true;

        try {
            URL u = new URL(url);
            conn = (HttpURLConnection)u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range", "bytes=0-1");
            if (conn.getResponseCode() != 206) {
                support = false;
            }
        } catch (Exception var7) {
            RLog.e(TAG, "checkSupportResumeTransfer", var7);
            support = false;
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }

        }

        return support;
    }

    public int getDownloadEachSliceLength() {
        int length = 0;
        if (this.mContext != null) {
            Resources resources = this.mContext.getResources();
            length = resources.getInteger(resources.getIdentifier("rc_resume_file_transfer_size_each_slice", "integer", this.mContext.getPackageName()));
        }

        return length;
    }

    public void addToFileDownloadMap(int messageId, boolean isDownloading) {
        String md5 = FileUtils.getTempFileMD5(this.mContext, messageId);
        this.downloadMap.put(md5, isDownloading);
    }

    public void addToFileDownloadMap(String tag, boolean isDownloading) {
        String md5 = FileUtils.getTempFileMD5(this.mContext, tag);
        this.downloadMap.put(md5, isDownloading);
    }

    public void removeFromFileDownloadMap(int messageId) {
        String md5 = FileUtils.getTempFileMD5(this.mContext, messageId);
        if (this.downloadMap != null) {
            this.downloadMap.remove(md5);
        }

    }

    public void removeFromFileDownloadMap(String tag) {
        String md5 = FileUtils.getTempFileMD5(this.mContext, tag);
        if (this.downloadMap != null) {
            this.downloadMap.remove(md5);
        }

    }

    public boolean getDownloadingFromMap(Context context, int messageId) {
        String md5 = FileUtils.getTempFileMD5(context, messageId);
        return this.downloadMap != null && this.downloadMap.size() > 0 ? (Boolean)this.downloadMap.get(md5) : false;
    }

    public boolean getDownloadingFromMap(Context context, String tag) {
        String md5 = FileUtils.getTempFileMD5(context, tag);
        return this.downloadMap != null && this.downloadMap.size() > 0 ? (Boolean)this.downloadMap.get(md5) : false;
    }
}
