//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.rlog;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import io.rong.common.dlog.LogThreadPool;
import io.rong.imlib.common.NetUtils;

public class RLogReporter {
    private static final String BOUNDARY = "03297e90-eed0-4cec-b18b-92d2574b9331";
    LogThreadPool mLogThreadPool = new LogThreadPool(1);
    private UploadCallback mUploadCallback;

    public RLogReporter() {
    }

    public void report(final UploadConfig pConfig, final IUploadListener pListener) {
        this.mLogThreadPool.getExecutorService().submit(new Runnable() {
            public void run() {
                boolean result = RLogReporter.this.uploadFile(pConfig);
                if (pListener != null) {
                    pListener.onUploadFinish(result, pConfig.getFilePath());
                }

            }
        });
    }

    public void report(UploadConfig pConfig) {
        this.report(pConfig, (IUploadListener)null);
    }

    private boolean uploadFile(UploadConfig pConfig) {
        RLog.i("RongLog", "uploadFile begin.");
        HttpURLConnection conn = null;
        File file = new File(pConfig.getFilePath());
        DataOutputStream outStream = null;
        InputStream inStream = null;
        FileInputStream is = null;
        if (!file.exists()) {
            RLog.e("RongLog", "file not found " + pConfig.getFilePath());
            if (this.mUploadCallback != null) {
                this.mUploadCallback.fail(-4, "file not found", (Throwable)null);
            }

            return false;
        } else if (file.length() <= 0L) {
            if (this.mUploadCallback != null) {
                this.mUploadCallback.fail(-3, "file length is 0", (Throwable)null);
            }

            return true;
        } else if (pConfig.getUserId() != null && pConfig.getSdkVer() != null && pConfig.getAppKey() != null) {
            boolean result;
            try {
                conn = NetUtils.createURLConnection(pConfig.getUploadUrl());
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(5000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("RC-Type", "console");
                conn.setRequestProperty("RC-App-Key", pConfig.getAppKey());
                conn.setRequestProperty("RC-User-ID", pConfig.getUserId());
                conn.setRequestProperty("RC-SDK-Version", pConfig.getSdkVer());
                conn.setRequestProperty("RC-Platform", "Android");
                conn.setRequestProperty("RC-Start-Time", pConfig.getStartTime());
                conn.setRequestProperty("RC-End-Time", pConfig.getEndTime());
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=03297e90-eed0-4cec-b18b-92d2574b9331");
                outStream = new DataOutputStream(conn.getOutputStream());
                outStream.writeBytes("--03297e90-eed0-4cec-b18b-92d2574b9331\r\n");
                outStream.writeBytes("Content-Disposition: form-data; name=\"fileLog\"; filename=\"fileLog.gz\"\r\n");
                outStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                is = new FileInputStream(file);
                byte[] buffer = new byte[1024];

                int len;
                while((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                outStream.writeBytes("\r\n--03297e90-eed0-4cec-b18b-92d2574b9331--\r\n");
                outStream.flush();
                inStream = conn.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder responseSb = new StringBuilder();

                String line;
                while((line = buff.readLine()) != null) {
                    responseSb.append(line);
                }

                String responseBody = responseSb.toString();
                if (responseBody.contains("\"code\":0")) {
                    Log.d("RongLog", "response = " + responseBody);
                    result = true;
                    RLog.i("RongLog", "upload success path is" + pConfig.getFilePath());
                    if (this.mUploadCallback != null) {
                        this.mUploadCallback.success();
                    }
                } else {
                    result = false;
                    RLog.e("RongLog", "upload error server invalidate");
                    if (this.mUploadCallback != null) {
                        this.mUploadCallback.fail(-2, responseBody, (Throwable)null);
                    }
                }
            } catch (Exception var30) {
                result = false;
                RLog.e("RongLog", "http error", var30);
                if (this.mUploadCallback != null) {
                    this.mUploadCallback.fail(-5, "http error", var30);
                }
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException var29) {
                    RLog.e("RongLog", "uploadFile", var29);
                }

                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                } catch (IOException var28) {
                    RLog.e("RongLog", "uploadFile", var28);
                }

                try {
                    if (outStream != null) {
                        outStream.close();
                    }
                } catch (IOException var27) {
                    RLog.e("RongLog", "uploadFile", var27);
                }

            }

            RLog.i("RongLog", "uploadFile end.");
            return result;
        } else {
            RLog.e("RongLog", "params is empty ");
            if (this.mUploadCallback != null) {
                this.mUploadCallback.fail(-1, "params error", (Throwable)null);
            }

            return false;
        }
    }

    public void setUploadCallback(UploadCallback pUploadCallback) {
        this.mUploadCallback = pUploadCallback;
    }

    public static class UploadConfig {
        private String uploadUrl;
        private String filePath;
        private String startTime;
        private String endTime;
        private String sdkVer;
        private String appKey;
        private String userId;

        public UploadConfig(String pUploadUrl, String pFilePath, String pStartTime, String pEndTime, String pSdkVer, String pAppKey, String pUserId) {
            this.uploadUrl = pUploadUrl;
            this.filePath = pFilePath;
            this.startTime = pStartTime;
            this.endTime = pEndTime;
            this.sdkVer = pSdkVer;
            this.appKey = pAppKey;
            this.userId = pUserId;
        }

        public String getUploadUrl() {
            return this.uploadUrl;
        }

        public String getFilePath() {
            return this.filePath;
        }

        public String getStartTime() {
            return this.startTime;
        }

        public String getEndTime() {
            return this.endTime;
        }

        public String getSdkVer() {
            return this.sdkVer;
        }

        public String getAppKey() {
            return this.appKey;
        }

        public String getUserId() {
            return this.userId;
        }
    }

    public interface IUploadListener {
        void onUploadFinish(boolean var1, String var2);
    }

    public interface UploadCallback {
        int PARAMS_ERROR = -1;
        int SERVER_ERROR = -2;
        int FILE_EMPTY = -3;
        int FILE_NOT_FOUND = -4;
        int HTTP_ERROR = -5;

        void success();

        void fail(int var1, String var2, Throwable var3);
    }
}
