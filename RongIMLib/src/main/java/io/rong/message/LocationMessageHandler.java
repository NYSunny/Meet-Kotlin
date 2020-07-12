//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.common.NetUtils;
import io.rong.imlib.model.Message;
import io.rong.message.utils.BitmapUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationMessageHandler extends MessageHandler<LocationMessage> {
    private static final String TAG = "LocationMessageHandler";
    private static final int THUMB_WIDTH = 408;
    private static final int THUMB_HEIGHT = 240;
    private static final int THUMB_COMPRESSED_QUALITY = 30;

    public LocationMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, LocationMessage content) {
        String name = message.getMessageId() + "";
        if (message.getMessageId() == 0) {
            name = message.getSentTime() + "";
        }

        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        File file = new File(uri.toString() + name);
        if (file.exists()) {
            content.setImgUri(Uri.fromFile(file));
        } else {
            if (content != null) {
                String base64 = content.getBase64();
                if (!TextUtils.isEmpty(base64)) {
                    if (base64.startsWith("http")) {
                        content.setImgUri(Uri.parse(base64));
                        content.setBase64((String)null);
                    } else {
                        try {
                            byte[] audio = Base64.decode(content.getBase64(), 2);
                            file = FileUtils.byte2File(audio, uri.toString(), name + "");
                            if (content.getImgUri() == null) {
                                if (file != null && file.exists()) {
                                    content.setImgUri(Uri.fromFile(file));
                                } else {
                                    RLog.e("LocationMessageHandler", "getImgUri is null");
                                }
                            }
                        } catch (IllegalArgumentException var8) {
                            RLog.e("LocationMessageHandler", "Not Base64 Content!");
                            RLog.e("LocationMessageHandler", "IllegalArgumentException", var8);
                        }

                        message.setContent(content);
                        content.setBase64((String)null);
                    }
                }
            }

        }
    }

    public void encodeMessage(Message message) {
        LocationMessage content = (LocationMessage)message.getContent();
        if (content.getImgUri() == null) {
            RLog.w("LocationMessageHandler", "No thumbnail uri.");
            if (this.mHandleMessageListener != null) {
                this.mHandleMessageListener.onHandleResult(message, 0);
            }

        } else {
            Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
            String scheme = content.getImgUri().getScheme();
            File file;
            String thumbnailPath;
            if (!TextUtils.isEmpty(scheme) && scheme.toLowerCase().equals("file")) {
                thumbnailPath = content.getImgUri().getPath();
            } else {
                file = this.loadLocationThumbnail(content, message.getSentTime() + "");
                thumbnailPath = file != null ? file.getPath() : null;
            }

            if (thumbnailPath == null) {
                RLog.e("LocationMessageHandler", "load thumbnailPath null!");
                if (this.mHandleMessageListener != null) {
                    this.mHandleMessageListener.onHandleResult(message, -1);
                }

            } else {
                try {
                    Bitmap bitmap = BitmapUtil.interceptBitmap(thumbnailPath, 408, 240);
                    if (bitmap != null) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(CompressFormat.JPEG, 30, outputStream);
                        byte[] data = outputStream.toByteArray();
                        outputStream.close();
                        String base64 = Base64.encodeToString(data, 2);
                        content.setBase64(base64);
                        file = FileUtils.byte2File(data, uri.toString(), message.getMessageId() + "");
                        if (file != null && file.exists()) {
                            content.setImgUri(Uri.fromFile(file));
                        }

                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                        }

                        if (this.mHandleMessageListener != null) {
                            this.mHandleMessageListener.onHandleResult(message, 0);
                        }
                    } else {
                        RLog.e("LocationMessageHandler", "get null bitmap!");
                        if (this.mHandleMessageListener != null) {
                            this.mHandleMessageListener.onHandleResult(message, -1);
                        }
                    }
                } catch (Exception var11) {
                    RLog.e("LocationMessageHandler", "Not Base64 Content!");
                    RLog.e("LocationMessageHandler", "Exception ", var11);
                    if (this.mHandleMessageListener != null) {
                        this.mHandleMessageListener.onHandleResult(message, -1);
                    }
                }

            }
        }
    }

    private File loadLocationThumbnail(LocationMessage content, String name) {
        File file = null;
        HttpURLConnection conn = null;
        int responseCode = 0;

        try {
            Uri uri = content.getImgUri();
            URL url = new URL(uri.toString());
            conn = NetUtils.createURLConnection(url.toString());
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            conn.connect();
            responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                String path = FileUtils.getInternalCachePath(this.getContext(), "location");
                file = new File(path);
                if (!file.exists()) {
                    boolean successMkdir = file.mkdirs();
                    if (!successMkdir) {
                        RLog.e("LocationMessageHandler", "Created folders unSuccessfully");
                    }
                }

                file = new File(path, name);
                InputStream is = conn.getInputStream();
                FileOutputStream os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];

                int len;
                while((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }

                is.close();
                os.close();
            }
        } catch (Exception var16) {
            RLog.e("LocationMessageHandler", "Exception ", var16);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            RLog.d("LocationMessageHandler", "loadLocationThumbnail result : " + responseCode);
        }

        return file;
    }
}
