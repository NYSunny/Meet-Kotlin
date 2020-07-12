//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.text.TextUtils;
import io.rong.common.rlog.RLog;
import io.rong.imlib.common.NetUtils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.zip.GZIPOutputStream;

public abstract class UploadLogTask {
    private static final String TAG = UploadLogTask.class.getSimpleName();
    private static final String BOUNDARY = "03297e90-eed0-4cec-b18b-92d2574b9331";
    protected static final String URL_ENCODE_CHARSET = "utf-8";

    public UploadLogTask() {
    }

    protected abstract String getUploadUrl();

    protected boolean upload(String uploadFilePath) {
        HttpURLConnection conn = null;
        DataOutputStream outStream = null;
        InputStream inStream = null;
        boolean result = false;
        FileInputStream fileInputStream = null;
        File uploadFile = null;
        boolean var29 = false;

        boolean delete;
        label307: {
            try {
                var29 = true;
                uploadFile = new File(uploadFilePath);
                conn = NetUtils.createURLConnection(this.getUploadUrl());
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(5000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=03297e90-eed0-4cec-b18b-92d2574b9331");
                outStream = new DataOutputStream(conn.getOutputStream());
                outStream.writeBytes("--03297e90-eed0-4cec-b18b-92d2574b9331\r\n");
                outStream.writeBytes("Content-Disposition: form-data; name=\"fileLog\"; filename=\"fileLog.gz\"\r\n");
                outStream.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                byte[] buffer = new byte[1024];
                fileInputStream = new FileInputStream(uploadFile);
                GZIPOutputStream gzStream = new GZIPOutputStream(outStream);

                int len;
                while((len = fileInputStream.read(buffer)) > 0) {
                    gzStream.write(buffer, 0, len);
                }

                gzStream.finish();
                outStream.writeBytes("\r\n--03297e90-eed0-4cec-b18b-92d2574b9331--\r\n");
                outStream.flush();
                inStream = conn.getInputStream();
                BufferedReader buff = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder responseSb = new StringBuilder();

                String line;
                while((line = buff.readLine()) != null) {
                    responseSb.append(line);
                }

                String response = responseSb.toString();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    if (!TextUtils.isEmpty(response)) {
                        this.onUploadResponse(response);
                    }

                    result = true;
                    var29 = false;
                } else {
                    var29 = false;
                }
                break label307;
            } catch (Exception var39) {
                RLog.e(TAG, "upload", var39);
                var29 = false;
            } finally {
                if (var29) {
                    if (conn != null) {
                        conn.disconnect();
                    }

                    try {
                        if (outStream != null) {
                            outStream.close();
                        }
                    } catch (IOException var32) {
                        RLog.e(TAG, "outStream", var32);
                    }

                    try {
                        if (inStream != null) {
                            inStream.close();
                        }
                    } catch (IOException var31) {
                        RLog.e(TAG, "inStream", var31);
                    }

                    try {
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                    } catch (IOException var30) {
                        RLog.e(TAG, "fileInputStream", var30);
                    }

                    if (uploadFile != null && uploadFile.exists()) {
                        boolean d = uploadFile.delete();
                        if (!d) {
                            RLog.d(TAG, "upload cache file delete return false.file name:" + uploadFile.getName());
                        }
                    }

                }
            }

            if (conn != null) {
                conn.disconnect();
            }

            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException var35) {
                RLog.e(TAG, "outStream", var35);
            }

            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException var34) {
                RLog.e(TAG, "inStream", var34);
            }

            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException var33) {
                RLog.e(TAG, "fileInputStream", var33);
            }

            if (uploadFile != null && uploadFile.exists()) {
                delete = uploadFile.delete();
                if (!delete) {
                    RLog.d(TAG, "upload cache file delete return false.file name:" + uploadFile.getName());
                    return result;
                }
            }

            return result;
        }

        if (conn != null) {
            conn.disconnect();
        }

        try {
            if (outStream != null) {
                outStream.close();
            }
        } catch (IOException var38) {
            RLog.e(TAG, "outStream", var38);
        }

        try {
            if (inStream != null) {
                inStream.close();
            }
        } catch (IOException var37) {
            RLog.e(TAG, "inStream", var37);
        }

        try {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (IOException var36) {
            RLog.e(TAG, "fileInputStream", var36);
        }

        if (uploadFile != null && uploadFile.exists()) {
            delete = uploadFile.delete();
            if (!delete) {
                RLog.d(TAG, "upload cache file delete return false.file name:" + uploadFile.getName());
            }
        }

        return result;
    }

    protected String encodeParams(String params) {
        if (TextUtils.isEmpty(params)) {
            return params;
        } else {
            String encodeParams;
            try {
                encodeParams = URLEncoder.encode(params, "utf-8");
            } catch (UnsupportedEncodingException var4) {
                encodeParams = params;
            }

            return encodeParams;
        }
    }

    protected abstract void onUploadResponse(String var1);
}
