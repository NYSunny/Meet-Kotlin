//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Vector;

import io.rong.common.dlog.DLog.ILogUploadCallback;
import io.rong.common.dlog.DLog.LogTag;
import io.rong.imlib.common.NetUtils;

public class LogReporter {
    private static final String BOUNDARY = "03297e90-eed0-4cec-b18b-92d2574b9331";
    LogThreadPool logThreadPool;

    public LogReporter(LogThreadPool threadPool) {
        this.logThreadPool = threadPool;
        this.checkAndReportCrashLog();
        this.clearExpiredLogFile();
    }

    public void reportFileLog(final ILogUploadCallback callback) {
        final boolean[] hasCalled = new boolean[]{false};

        try {
            JSONArray jsonArray = LogEntity.getInstance().getUploadCacheList();

            for(int i = 0; i < jsonArray.length(); ++i) {
                JSONObject record = jsonArray.getJSONObject(i);
                final String filename = record.getString("filename");
                final String sdkVer = record.getString("sdkVer");
                final String appKey = record.getString("appKey");
                final String userId = record.getString("userId");
                final String token = record.getString("token");
                int finalI = i;
                this.logThreadPool.getExecutorService().submit(new Runnable() {
                    public void run() {
                        File file = new File(LogEntity.getInstance().getLogDir(), filename);
                        if (file.exists() && file.length() != 0L) {
                            String startTimeStr = filename.substring(0, filename.indexOf("_"));
                            String endTimeStr = filename.substring(filename.indexOf("_") + 1, filename.indexOf("."));
                            long endTime = Long.parseLong(endTimeStr);
                            if (System.currentTimeMillis() - endTime <= LogEntity.getInstance().getOutDateTime()) {
                                boolean result = LogReporter.this.uploadFile(file.getAbsolutePath(), startTimeStr, endTimeStr, sdkVer, appKey, userId, token, false);
                                if (result) {
                                    LogEntity.getInstance().deleteUploadCacheList(finalI);
                                } else if (callback != null && !hasCalled[0]) {
                                    callback.onLogUploaded(-1);
                                    hasCalled[0] = true;
                                    Log.i("FwLog", "log upload failed.");
                                }
                            } else {
                                DLog.write(2, 512, LogTag.G_DROP_LOG_E.getTag(), "start|end|size", new Object[]{startTimeStr, endTimeStr, file.length()});
                                file.delete();
                            }
                        } else if (file.exists()) {
                            file.delete();
                        }

                    }
                });
            }
        } catch (JSONException var12) {
            Log.e("FwLog", "ignored = " + var12);
            if (callback != null && !hasCalled[0]) {
                callback.onLogUploaded(-1);
                hasCalled[0] = true;
                Log.i("FwLog", "log upload exception.");
            }
        }

        if (callback != null && !hasCalled[0]) {
            callback.onLogUploaded(0);
            Log.i("FwLog", "log upload success.");
        }

    }

    private void checkAndReportCrashLog() {
        if (LogEntity.getInstance().getLogMode() != 1) {
            this.logThreadPool.getExecutorService().submit(new Runnable() {
                public void run() {
                    File logDir = new File(LogEntity.getInstance().getLogDir());
                    File[] zipFiles = logDir.listFiles(new FilenameFilter() {
                        public boolean accept(File file, String name) {
                            return name.endsWith("rong_sdk_crash.log");
                        }
                    });
                    File[] var3 = zipFiles;
                    int var4 = zipFiles.length;

                    for(int var5 = 0; var5 < var4; ++var5) {
                        File file = var3[var5];
                        if (file.length() != 0L) {
                            long startTime = file.lastModified();
                            long endTime = System.currentTimeMillis();
                            String zipFilename = startTime + "_" + endTime + ".gz";
                            String zipFilePath = LogEntity.getInstance().getLogDir() + File.separator + zipFilename;
                            if (LogZipper.gzipFile(file.getAbsolutePath(), zipFilePath)) {
                                boolean result = LogReporter.this.uploadFile(zipFilePath, startTime + "", endTime + "", LogEntity.getInstance().getSdkVer(), LogEntity.getInstance().getAppKey(), LogEntity.getInstance().getUserId(), LogEntity.getInstance().getToken(), true);
                                if (result) {
                                    LogReporter.this.clearInfoForFile(LogEntity.getInstance().getLogDir() + File.separator + "rong_sdk_crash.log");
                                } else {
                                    LogReporter.this.deleteCrashZipFile(zipFilename);
                                }
                            } else {
                                LogReporter.this.deleteCrashZipFile(zipFilename);
                            }
                        }
                    }

                }
            });
        }
    }

    private void deleteCrashZipFile(String zipFilename) {
        File zipFile = new File(LogEntity.getInstance().getLogDir(), zipFilename);
        if (zipFile.exists()) {
            zipFile.delete();
        }

    }

    private void clearInfoForFile(String fileName) {
        File file = new File(fileName);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }

    }

    public void reportLruLog(Vector<String> lruLog, final String startTime, final String endTime) {
        final Vector<String> tmpLog = new Vector(lruLog);
        this.logThreadPool.getExecutorService().submit(new Runnable() {
            public void run() {
                JSONArray jsonArray = new JSONArray();
                int count = tmpLog.size();

                for(int i = 0; i < count; ++i) {
                    try {
                        JSONObject jsonObject = new JSONObject((String)tmpLog.get(i));
                        jsonArray.put(jsonObject);
                    } catch (JSONException var5) {
                        var5.printStackTrace();
                    }
                }

                if (count > 0) {
                    LogReporter.this.uploadString(jsonArray.toString(), startTime, endTime, LogEntity.getInstance().getSdkVer(), LogEntity.getInstance().getAppKey(), LogEntity.getInstance().getUserId(), LogEntity.getInstance().getToken());
                }

            }
        });
    }

    private boolean uploadFile(String filePath, String startTime, String endTime, String sdkVer, String appKey, String userId, String token, boolean crashLog) {
        Log.i("FwLog", "uploadFile begin.");
        HttpURLConnection conn = null;
        File file = new File(filePath);
        DataOutputStream outStream = null;
        InputStream inStream = null;
        FileInputStream is = null;
        boolean result = false;

        try {
            conn = NetUtils.createURLConnection(LogEntity.getInstance().getUploadUrl());
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            if (crashLog) {
                conn.setRequestProperty("RC-Type", "crash");
            }

            conn.setRequestProperty("RC-App-Key", appKey);
            conn.setRequestProperty("RC-User-ID", userId);
            conn.setRequestProperty("RC-SDK-Version", sdkVer);
            conn.setRequestProperty("RC-Platform", "Android");
            conn.setRequestProperty("RC-Start-Time", startTime);
            conn.setRequestProperty("RC-End-Time", endTime);
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
                Log.d("FwLog", "response = " + responseBody);
                result = true;
                DLog.write(4, 512, LogTag.G_UPLOAD_LOG_S.getTag(), "start|end|size", new Object[]{startTime, endTime, file.length()});
            } else {
                int responseCode = conn.getResponseCode();
                result = false;
                DLog.write(2, 512, LogTag.G_UPLOAD_LOG_E.getTag(), "code|body|start|end|size", new Object[]{responseCode, responseBody, startTime, endTime, file.length()});
            }
        } catch (Exception var30) {
            result = false;
            DLog.write(1, 512, LogTag.G_UPLOAD_LOG_F.getTag(), "start|end|size|stacks", new Object[]{startTime, endTime, file.length(), DLog.stackToString(var30)});
            var30.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            try {
                if (outStream != null) {
                    outStream.close();
                }

                if (inStream != null) {
                    inStream.close();
                }

                if (is != null) {
                    is.close();
                }
            } catch (IOException var29) {
            }

            if (result) {
                file.delete();
            }

        }

        Log.i("FwLog", "uploadFile end.");
        return result;
    }

    private boolean uploadString(String content, String startTime, String endTime, String sdkVer, String appKey, String userId, String token) {
        HttpURLConnection conn = null;

        try {
            conn = NetUtils.createURLConnection(LogEntity.getInstance().getOnlineLogServer());
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("RC-App-Key", appKey);
            conn.setRequestProperty("RC-User-ID", userId);
            conn.setRequestProperty("RC-SDK-Version", sdkVer);
            conn.setRequestProperty("RC-Platform", "Android");
            conn.setRequestProperty("RC-Start-Time", String.valueOf(startTime));
            conn.setRequestProperty("RC-End-Time", String.valueOf(endTime));
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(content);
            int code = conn.getResponseCode();
            os.flush();
            os.close();
            if (code == 200) {
                boolean var11 = true;
                return var11;
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

        }

        return false;
    }

    private void clearExpiredLogFile() {
        try {
            File logDir = new File(LogEntity.getInstance().getLogDir());
            File[] zipFiles = logDir.listFiles(new FilenameFilter() {
                public boolean accept(File file, String name) {
                    return name.endsWith(".gz");
                }
            });
            File[] var3 = zipFiles;
            int var4 = zipFiles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];
                String filename = file.getName();
                if (!filename.contains("_")) {
                    file.delete();
                } else {
                    String startTimeStr = filename.substring(0, filename.indexOf("_"));
                    String endTimeStr = filename.substring(filename.indexOf("_") + 1, filename.indexOf("."));
                    long endTime = Long.parseLong(endTimeStr);
                    if (System.currentTimeMillis() - endTime > LogEntity.getInstance().getOutDateTime()) {
                        file.delete();
                        DLog.write(2, 512, LogTag.G_DROP_LOG_E.getTag(), "start|end|size", new Object[]{startTimeStr, endTimeStr, file.length()});
                    }
                }
            }
        } catch (Exception var12) {
            DLog.write(1, 512, LogTag.G_CRASH_E.getTag(), "stacks", new Object[]{DLog.stackToString(var12)});
            var12.printStackTrace();
        }

    }
}
