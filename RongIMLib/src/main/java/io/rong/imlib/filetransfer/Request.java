//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.common.NetUtils;
import io.rong.imlib.filetransfer.FtConst.MimeType;
import io.rong.imlib.filetransfer.FtConst.ServiceType;
import io.rong.imlib.model.FileInfo;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Request {
    private static final String TAG = Request.class.getSimpleName();
    private static final int TIMEOUT = 10000;
    protected MimeType mimeType;
    protected String url;
    protected String method;
    protected Object tag;
    protected String token;
    protected int connTimeout;
    protected int readTimeout;
    protected String serverIp;
    protected String fileName;
    protected long fileLength;
    protected String date;
    protected RequestCallBack requestCallBack;
    protected boolean terminated;
    protected FileInfo info;
    protected OnProgressListener listener;
    protected int finished = 0;
    protected String pausedPath;
    protected int messageId;
    protected boolean isMessage;
    protected Context mContext;
    private HttpURLConnection conn;

    public void setContext(Context pContext) {
        this.mContext = pContext;
    }

    public Request(Configuration config, RequestCallBack requestCallBack) {
        this.connTimeout = config.getConnectTimeout();
        this.readTimeout = config.getReadTimeout();
        this.requestCallBack = requestCallBack;
    }

    public Request(FileInfo fileInfo, Configuration config, RequestCallBack requestCallBack, String pausedPath) {
        this.pausedPath = pausedPath;
        this.setFileInfo(fileInfo);
        this.connTimeout = config.getConnectTimeout();
        this.readTimeout = config.getReadTimeout();
        this.requestCallBack = requestCallBack;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract String getContentType();

    public abstract long getContentLength();

    public abstract String getFormData();

    public abstract String getBoundary();

    public abstract String getUploadedUrl(String var1);

    public abstract MimeType getMimeType();

    public void sendRequest() {
        BufferedInputStream responseStream = null;
        ByteArrayOutputStream responseData = null;
        DataOutputStream os = null;
        FileInputStream is = null;
        boolean supportResumeTransfer = false;

        try {
            if (this.method.equals("POST")) {
                this.conn = NetUtils.createURLConnection(this.serverIp);
                if (FileUtils.uriStartWithFile(Uri.parse(this.url))) {
                    is = new FileInputStream(new File(this.url.substring(7)));
                } else {
                    ParcelFileDescriptor r = this.mContext.getContentResolver().openFileDescriptor(Uri.parse(this.url), "r");
                    is = new FileInputStream(r.getFileDescriptor());
                }

                this.conn.setUseCaches(false);
                this.conn.setDoOutput(true);
                this.conn.setDoInput(true);
                this.conn.setRequestMethod(this.method);
                this.conn.setConnectTimeout(10000);
                this.conn.setRequestProperty("Connection", "close");
                String endBoundary = "\r\n--" + this.getBoundary() + "--";
                if (FileTransferClient.getInstance().getServiceType() == ServiceType.BAI_DU) {
                    this.conn.setRequestProperty("Authorization", this.token);
                    this.conn.setRequestProperty("x-bce-date", this.date);
                    if (!MimeType.FILE_HTML.getName().equals(this.getContentType())) {
                        this.conn.setRequestProperty("Content-Disposition", "attachment;filename=" + this.fileName);
                    }

                    endBoundary = "";
                }

                this.conn.setRequestProperty("Charset", "UTF-8");
                this.conn.setRequestProperty("Content-Type", this.getContentType());
                String formData = this.getFormData();
                int fileSize = is.available();
                if (fileSize < 0) {
                    this.requestCallBack.onError(31002);
                }

                int total = formData.length() + fileSize + endBoundary.length();
                this.conn.setRequestProperty("Content-Length", total + "");
                this.conn.setFixedLengthStreamingMode(total);
                this.conn.connect();
                os = new DataOutputStream(this.conn.getOutputStream());
                os.writeBytes(formData);
                int current = formData.length();
                int cachedLength = formData.length();
                int progress = 1;
                this.requestCallBack.onProgress(progress);
                byte[] buffer = new byte[1024];

                int read;
                int responseCode;
                while((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                    current += read;
                    cachedLength += read;
                    responseCode = (int)(100L * (long)current / (long)total);
                    if (responseCode > progress) {
                        progress = responseCode;
                        this.requestCallBack.onProgress(responseCode);
                    }

                    if (cachedLength > 2097152) {
                        os.flush();
                        cachedLength = 0;
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        RLog.w(TAG, "sendRequest post terminated.tag:" + this.tag);
                        this.terminated = true;
                        this.requestCallBack.onCanceled(this.tag);
                        throw new InterruptedIOException();
                    }
                }

                os.writeBytes(endBoundary);
                this.requestCallBack.onProgress(100);
                is.close();
                os.flush();
                responseCode = this.conn.getResponseCode();
                responseStream = new BufferedInputStream(this.conn.getInputStream());
                responseData = new ByteArrayOutputStream(1024);

                int c;
                while((c = responseStream.read()) != -1) {
                    responseData.write(c);
                }

                if (responseCode >= 200 && responseCode < 300) {
                    String url = this.getUploadedUrl(responseData.toString());
                    this.requestCallBack.onComplete(url);
                } else {
                    this.requestCallBack.onError(30002);
                }
            } else if (this.method.equals("GET")) {
                supportResumeTransfer = FileTransferClient.checkSupportResumeTransfer(this.url);
                if (!supportResumeTransfer) {
                    this.sendRequestNotSupportResumeTransfer();
                } else {
                    this.sendRequestSupportResumeTransfer();
                }
            }
        } catch (Exception var26) {
            if (!this.terminated) {
                this.requestCallBack.onError(30002);
                if (supportResumeTransfer) {
                    FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), false);
                }
            }

            RLog.e(TAG, "sendRequest", var26);
        } finally {
            try {
                if (responseData != null) {
                    responseData.close();
                }

                if (responseStream != null) {
                    responseStream.close();
                }

                if (os != null) {
                    os.close();
                }

                if (is != null) {
                    is.close();
                }
            } catch (IOException var25) {
                RLog.e(TAG, "sendRequest", var25);
            }

            if (this.conn != null) {
                this.conn.disconnect();
                this.conn = null;
            }

        }

    }

    public void sendRequestForNoneMessage() {
        boolean supportResumeTransfer = false;

        try {
            if (this.method.equals("GET")) {
                supportResumeTransfer = FileTransferClient.checkSupportResumeTransfer(this.url);
                if (!supportResumeTransfer) {
                    this.sendRequestNotSupportResumeTransfer();
                } else {
                    this.sendRequestSupportResumeTransfer();
                }
            }
        } catch (Exception var3) {
            if (!this.terminated) {
                this.requestCallBack.onError(30002);
                if (supportResumeTransfer) {
                    FileTransferClient.getInstance().addToFileDownloadMap(this.url, false);
                }
            }

            RLog.e(TAG, "sendRequestForNoneMessage", var3);
        }

    }

    private void sendRequestSupportResumeTransfer() throws Exception {
        this.getLength();
        int sliceLength = FileTransferClient.getInstance().getDownloadEachSliceLength();
        long start = this.info.getFinished();
        if (start == this.info.getLength()) {
            this.downloadComplete();
        } else if (start + (long)sliceLength - 1L > this.info.getLength()) {
            this.downloadInOnceSlice(start);
        } else {
            this.downloadInMultiSlice(start, sliceLength);
        }

    }

    private void downloadInMultiSlice(long start, int sliceLength) {
        long finished = start;

        while(finished < this.info.getLength()) {
            HttpURLConnection conn = null;
            RandomAccessFile rwd = null;

            long end;
            try {
                conn = NetUtils.createURLConnection(this.url);
                conn.setRequestMethod(this.method);
                conn.setConnectTimeout(3000);
                if (finished + (long)sliceLength >= this.info.getLength()) {
                    end = this.info.getLength() - 1L;
                } else {
                    end = finished + (long)sliceLength - 1L;
                }

                conn.setRequestProperty("Range", "bytes=" + finished + "-" + end);
                File file = new File(this.info.getFileName());
                rwd = new RandomAccessFile(file, "rwd");
                rwd.seek(finished);
                RLog.d(TAG, "downloadInMultiSlice conn code :" + conn.getResponseCode());
                if (conn.getResponseCode() >= 200 && conn.getResponseCode() < 300) {
                    if (conn.getResponseCode() == 206) {
                        InputStream inputStream = conn.getInputStream();
                        byte[] buffer = new byte[4096];
                        long total = this.info.getLength();
                        long current = this.info.getFinished();
                        int progress = 0;

                        int len;
                        while((len = inputStream.read(buffer)) != -1) {
                            rwd.write(buffer, 0, len);
                            finished += (long)len;
                            this.info.setFinished(finished);
                            String savedFileInfoString = FileUtils.getStringFromFile(this.pausedPath);
                            if (!TextUtils.isEmpty(savedFileInfoString)) {
                                FileInfo savedFileInfo = this.getFileInfoFromJsonString(savedFileInfoString);
                                if (savedFileInfo.getFinished() > finished) {
                                    return;
                                }
                            }

                            current += (long)len;
                            int temp = (int)(100L * current / total);
                            if (progress < temp) {
                                progress = temp;
                                this.requestCallBack.onProgress(temp);
                            }

                            if (Thread.currentThread().isInterrupted()) {
                                RLog.w(TAG, "sendRequest post terminated.tag:" + this.tag);
                                this.terminated = true;
                                this.requestCallBack.onCanceled(this.tag);
                                throw new InterruptedIOException();
                            }

                            String saveString;
                            if (this.info.isStop()) {
                                this.info.setDownLoading(false);
                                saveString = this.getSaveJsonString();
                                FileUtils.saveFile(saveString, this.pausedPath);
                                if (this.isMessage) {
                                    FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), false);
                                } else {
                                    FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), false);
                                }

                                return;
                            }

                            this.info.setDownLoading(true);
                            this.info.setStop(false);
                            saveString = this.getSaveJsonString();
                            FileUtils.saveFile(saveString, this.pausedPath);
                            if (this.isMessage) {
                                FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), true);
                            } else {
                                FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), true);
                            }
                        }
                    }
                } else {
                    this.requestCallBack.onError(30002);
                    if (this.isMessage) {
                        FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), false);
                    } else {
                        FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), false);
                    }
                }
            } catch (Exception var34) {
                if (!this.terminated) {
                    this.requestCallBack.onError(30002);
                    if (this.isMessage) {
                        FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), false);
                    } else {
                        FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), false);
                    }
                }

                RLog.e(TAG, "downloadInMultiSlice", var34);
                return;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

                if (rwd != null) {
                    try {
                        rwd.close();
                    } catch (IOException var33) {
                        RLog.e(TAG, "downloadInMultiSlice", var33);
                    }
                }

            }

            if (end == this.info.getLength() - 1L) {
                this.downloadComplete();
            }
        }

    }

    private void downloadInOnceSlice(long start) {
        RandomAccessFile rwd = null;

        try {
            this.conn = NetUtils.createURLConnection(this.url);
            this.conn.setRequestMethod(this.method);
            this.conn.setConnectTimeout(3000);
            this.conn.setRequestProperty("Range", "bytes=" + start + "-" + (this.info.getLength() - 1L));
            File file = new File(this.info.getFileName());
            rwd = new RandomAccessFile(file, "rwd");
            rwd.seek(start);
            this.finished = (int)((long)this.finished + this.info.getFinished());
            RLog.d(TAG, "downloadInOnceSlice conn code :" + this.conn.getResponseCode());
            if (this.conn.getResponseCode() >= 200 && this.conn.getResponseCode() < 300) {
                if (this.conn.getResponseCode() != 206) {
                    return;
                }

                InputStream inputStream = this.conn.getInputStream();
                byte[] buffer = new byte[4096];
                long total = this.info.getLength();
                long current = this.info.getFinished();
                int progress = 0;

                int len;
                while((len = inputStream.read(buffer)) != -1) {
                    rwd.write(buffer, 0, len);
                    this.finished += len;
                    this.info.setFinished((long)this.finished);
                    String savedFileInfoString = FileUtils.getStringFromFile(this.pausedPath);
                    if (!TextUtils.isEmpty(savedFileInfoString)) {
                        FileInfo savedFileInfo = this.getFileInfoFromJsonString(savedFileInfoString);
                        if (savedFileInfo.getFinished() > (long)this.finished) {
                            return;
                        }
                    }

                    current += (long)len;
                    int temp = (int)(100L * current / total);
                    if (progress < temp) {
                        progress = temp;
                        this.requestCallBack.onProgress(temp);
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        RLog.w(TAG, "sendRequest post terminated.tag:" + this.tag);
                        this.terminated = true;
                        this.requestCallBack.onCanceled(this.tag);
                        throw new InterruptedIOException();
                    }

                    String saveString;
                    if (this.info.isStop()) {
                        this.info.setDownLoading(false);
                        saveString = this.getSaveJsonString();
                        FileUtils.saveFile(saveString, this.pausedPath);
                        if (this.isMessage) {
                            FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), false);
                        } else {
                            FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), false);
                        }

                        return;
                    }

                    this.info.setDownLoading(true);
                    this.info.setStop(false);
                    saveString = this.getSaveJsonString();
                    FileUtils.saveFile(saveString, this.pausedPath);
                    if (this.isMessage) {
                        FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), true);
                    } else {
                        FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), true);
                    }
                }

                this.downloadComplete();
                return;
            }

            this.requestCallBack.onError(30002);
            if (this.isMessage) {
                FileTransferClient.getInstance().addToFileDownloadMap(this.info.getMessageId(), false);
            } else {
                FileTransferClient.getInstance().addToFileDownloadMap(this.tag.toString(), false);
            }
        } catch (Exception var28) {
            RLog.e(TAG, "downloadInOnceSlice", var28);
            return;
        } finally {
            if (rwd != null) {
                try {
                    rwd.close();
                } catch (IOException var27) {
                    RLog.e(TAG, "downloadInOnceSlice", var27);
                }
            }

        }

    }

    private void downloadComplete() {
        this.info.setDownLoading(false);
        this.requestCallBack.onComplete(this.info.getFileName());
        FileUtils.removeFile(this.pausedPath);
        if (this.isMessage) {
            FileTransferClient.getInstance().removeFromFileDownloadMap(this.info.getMessageId());
        } else {
            FileTransferClient.getInstance().removeFromFileDownloadMap(this.tag.toString());
        }

    }

    private void sendRequestNotSupportResumeTransfer() throws Exception {
        this.conn = NetUtils.createURLConnection(this.url);
        this.conn.setConnectTimeout(10000);
        this.conn.setUseCaches(false);
        this.conn.setRequestMethod(this.method);
        this.conn.setDoInput(true);
        this.conn.connect();
        int responseCode = this.conn.getResponseCode();
        if (responseCode >= 200 && responseCode < 300) {
            BufferedInputStream responseStream = new BufferedInputStream(this.conn.getInputStream());
            int total = this.conn.getContentLength();
            int current = 0;
            int cachedLength = 0;
            ByteArrayOutputStream responseData = new ByteArrayOutputStream(1024);
            int progress = 0;
            byte[] buffer = new byte[1024];
            File f = new File(this.fileName);
            FileOutputStream fos = new FileOutputStream(f);

            int length;
            while((length = responseStream.read(buffer)) != -1) {
                responseData.write(buffer, 0, length);
                current += length;
                cachedLength += length;
                int temp = (int)(100L * (long)current / (long)total);
                if (progress < temp) {
                    progress = temp;
                    this.requestCallBack.onProgress(temp);
                }

                if (Thread.currentThread().isInterrupted()) {
                    RLog.w(TAG, "sendRequest terminated.");
                    this.terminated = true;
                    this.requestCallBack.onCanceled(this.tag);
                    throw new InterruptedIOException();
                }

                if (cachedLength > 1048576) {
                    fos.write(responseData.toByteArray(), 0, responseData.size());
                    responseData.reset();
                    cachedLength = 0;
                }
            }

            fos.write(responseData.toByteArray(), 0, responseData.size());
            fos.close();
            this.requestCallBack.onComplete(this.fileName);
        } else {
            this.requestCallBack.onError(30002);
            RLog.d("fileTransfer", "download request response code is " + responseCode);
        }

    }

    private String getSaveJsonString() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("filename", this.info.getFileName());
            jsonObject.put("url", this.info.getUrl());
            jsonObject.put("length", this.info.getLength());
            jsonObject.put("finish", this.info.getFinished());
            jsonObject.put("isStop", this.info.isStop());
            jsonObject.put("isDownLoading", this.info.isDownLoading());
        } catch (JSONException var3) {
            RLog.e(TAG, "getSaveJsonString", var3);
        }

        return jsonObject.toString();
    }

    private FileInfo getFileInfoFromJsonString(String jsonString) {
        FileInfo fileInfo = new FileInfo();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            fileInfo.setFileName(jsonObject.optString("filename"));
            fileInfo.setUrl(jsonObject.optString("url"));
            fileInfo.setLength(jsonObject.optLong("length"));
            fileInfo.setFinished(jsonObject.optLong("finish"));
            fileInfo.setStop(jsonObject.optBoolean("isStop", false));
            fileInfo.setDownLoading(jsonObject.optBoolean("isDownLoading", false));
        } catch (JSONException var4) {
            RLog.e(TAG, "getFileInfoFromJsonString", var4);
        }

        return fileInfo;
    }

    private void setFileInfo(FileInfo fileInfo) {
        try {
            this.info = fileInfo;
            FileInfo savedFileInfo = null;
            String savedFileInfoString = FileUtils.getStringFromFile(this.pausedPath);
            if (!TextUtils.isEmpty(savedFileInfoString)) {
                savedFileInfo = this.getFileInfoFromJsonString(savedFileInfoString);
            }

            if (savedFileInfo != null) {
                this.info.setFinished(savedFileInfo.getFinished());
                this.info.setDownLoading(savedFileInfo.isDownLoading());
                this.info.setLength(savedFileInfo.getLength());
                this.info.setFileName(savedFileInfo.getFileName());
                this.info.setUrl(savedFileInfo.getUrl());
            }
        } catch (Exception var4) {
            RLog.e(TAG, "setFileInfo", var4);
        }

    }

    private void getLength() {
        HttpURLConnection connection = null;
        if (this.fileLength > 0L) {
            this.info.setLength(this.fileLength);
        } else {
            try {
                connection = NetUtils.createURLConnection(this.info.getUrl());
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                long length = -1L;
                if (connection.getResponseCode() == 200) {
                    length = Long.parseLong(connection.getHeaderField("Content-Length"));
                }

                if (length <= 0L) {
                    RLog.e(TAG, "file length from server is 0. Return directly!");
                }

                this.info.setLength(length);
            } catch (Exception var12) {
                RLog.e(TAG, "getLength", var12);
            } finally {
                try {
                    if (connection != null) {
                        connection.disconnect();
                    }
                } catch (Exception var11) {
                    RLog.e(TAG, "getLength", var11);
                }

            }
        }

    }
}
