//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.os.Build.VERSION;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.SSLContext;

import io.rong.imlib.common.NetUtils;

public class ConnectionProcessor implements Runnable {
    private static final int CONNECT_TIMEOUT_IN_MILLISECONDS = 30000;
    private static final int READ_TIMEOUT_IN_MILLISECONDS = 30000;
    private final StatisticsStore store_;
    private final DeviceId deviceId_;
    private final String serverURL_;
    private final SSLContext sslContext_;

    ConnectionProcessor(String serverURL, StatisticsStore store, DeviceId deviceId, SSLContext sslContext) {
        this.serverURL_ = serverURL;
        this.store_ = store;
        this.deviceId_ = deviceId;
        this.sslContext_ = sslContext;
        if (VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", "false");
        }

    }

    URLConnection urlConnectionForEventData(String eventData) throws IOException {
        String urlStr = this.serverURL_;
        URL url = new URL(urlStr);
        HttpURLConnection conn = NetUtils.createURLConnection(urlStr);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        String picturePath = UserData.getPicturePathFromQuery(url);
        if (Statistics.sharedInstance().isLoggingEnabled()) {
            Log.d("Statistics", "Got picturePath: " + picturePath);
        }

        if (!picturePath.equals("")) {
            File binaryFile = new File(picturePath);
            conn.setDoOutput(true);
            String boundary = Long.toHexString(System.currentTimeMillis());
            String CRLF = "\r\n";
            String charset = "UTF-8";
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            OutputStream output = conn.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"binaryFile\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            FileInputStream fileInputStream = new FileInputStream(binaryFile);
            byte[] buffer = new byte[1024];

            int len;
            while((len = fileInputStream.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }

            output.flush();
            writer.append(CRLF).flush();
            fileInputStream.close();
            writer.append("--" + boundary + "--").append(CRLF).flush();
        } else {
            OutputStream os;
            BufferedWriter writer;
            if (eventData.contains("&crash=")) {
                if (Statistics.sharedInstance().isLoggingEnabled()) {
                    Log.d("Statistics", "Using post because of crash");
                }

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                os = conn.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(eventData);
                writer.flush();
                writer.close();
                os.close();
            } else {
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "close");
                os = conn.getOutputStream();
                writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(eventData);
                writer.flush();
                writer.close();
                os.close();
            }
        }

        return conn;
    }

    public void run() {
        while(true) {
            String[] storedEvents = this.store_.connections();
            if (storedEvents != null && storedEvents.length != 0) {
                if (this.deviceId_.getId() == null) {
                    if (Statistics.sharedInstance().isLoggingEnabled()) {
                        Log.i("Statistics", "No Device ID available yet, skipping request " + storedEvents[0]);
                    }
                } else {
                    String eventData = storedEvents[0];
                    URLConnection conn = null;
                    BufferedInputStream responseStream = null;

                    try {
                        conn = this.urlConnectionForEventData(eventData);
                        conn.connect();
                        responseStream = new BufferedInputStream(conn.getInputStream());
                        ByteArrayOutputStream responseData = new ByteArrayOutputStream(256);

                        int c;
                        while((c = responseStream.read()) != -1) {
                            responseData.write(c);
                        }

                        boolean success = true;
                        if (conn instanceof HttpURLConnection) {
                            HttpURLConnection httpConn = (HttpURLConnection)conn;
                            int responseCode = httpConn.getResponseCode();
                            success = responseCode >= 200 && responseCode < 300;
                            Log.d("Statistics", "code=" + responseCode);
                            if (!success && Statistics.sharedInstance().isLoggingEnabled()) {
                                Log.w("Statistics", "HTTP error response code was " + responseCode + " from submitting event data: " + eventData);
                            }
                        }

                        if (success) {
                            if (Statistics.sharedInstance().isLoggingEnabled()) {
                                Log.d("Statistics", "ok ->" + eventData);
                            }

                            this.store_.removeConnection(storedEvents[0]);
                            continue;
                        }
                    } catch (Exception var19) {
                        if (Statistics.sharedInstance().isLoggingEnabled()) {
                            Log.w("Statistics", "Got exception while trying to submit event data: " + eventData, var19);
                        }

                        Log.d("Statistics", "exception :" + var19.getMessage());
                    } finally {
                        if (responseStream != null) {
                            try {
                                responseStream.close();
                            } catch (IOException var18) {
                            }
                        }

                        if (conn != null && conn instanceof HttpURLConnection) {
                            ((HttpURLConnection)conn).disconnect();
                        }

                    }
                }
            }

            return;
        }
    }

    String getServerURL() {
        return this.serverURL_;
    }

    StatisticsStore getCountlyStore() {
        return this.store_;
    }

    DeviceId getDeviceId() {
        return this.deviceId_;
    }
}
