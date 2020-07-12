//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class SightMessageHandler extends MessageHandler<SightMessage> {
    private static final String TAG = "Sight-SightMessageHandler";
    private static final int THUMB_COMPRESSED_QUALITY = 30;
    private static final String VIDEO_THUMBNAIL_PATH = "/video/thumbnail/";

    public SightMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, SightMessage model) {
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getMessageId() + ".jpg";
        if (message.getMessageId() == 0) {
            name = message.getSentTime() + ".jpg";
        }

        String thumb = uri.toString() + "/video/thumbnail/";
        File thumbFile = new File(thumb + name);
        if (!TextUtils.isEmpty(model.getBase64()) && !thumbFile.exists()) {
            byte[] data = null;

            try {
                data = Base64.decode(model.getBase64(), 2);
            } catch (IllegalArgumentException var9) {
                RLog.e("Sight-SightMessageHandler", "decodeMessage afterDecodeMessage Not Base64 Content!", var9);
            }

            if (!isImageFile(data)) {
                RLog.e("Sight-SightMessageHandler", "afterDecodeMessage Not Image File!");
                return;
            }

            FileUtils.byte2File(data, thumb, name);
        }

        model.setThumbUri(Uri.parse("file://" + thumb + name));
        model.setBase64((String)null);
    }

    public void encodeMessage(Message message) {
        SightMessage model = (SightMessage)message.getContent();
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getMessageId() + ".jpg";
        Options options = new Options();
        options.inJustDecodeBounds = true;
        byte[] data;
        String path;
        if (model != null && FileUtils.isValidateLocalUri(model.getThumbUri())) {
            File file = new File(uri.toString() + "/video/thumbnail/" + name);
            if (file.exists()) {
                model.setThumbUri(Uri.parse("file://" + uri.toString() + "/video/thumbnail/" + name));
                data = FileUtils.file2byte(file);
                if (data != null) {
                    model.setBase64(Base64.encodeToString(data, 2));
                }

                return;
            }

            data = FileUtils.file2byte(this.getContext(), model.getThumbUri());
            if (data != null) {
                model.setBase64(Base64.encodeToString(data, 2));
                path = uri.toString() + "/video/thumbnail/";
                if (FileUtils.copyFileToInternal(this.getContext(), model.getThumbUri(), path, name)) {
                    model.setThumbUri(Uri.parse("file://" + path + name));
                    return;
                }
            }
        }

        try {
            if (model == null || model.getLocalPath() == null) {
                RLog.d("Sight-SightMessageHandler", "model or model.getLocalPath() is null ");
                return;
            }

            path = model.getLocalPath().toString().substring(7);
            RLog.d("Sight-SightMessageHandler", "beforeEncodeMessage Thumbnail not save yet! " + path);
            Bitmap bitmap;
            if (FileUtils.uriStartWithFile(model.getLocalPath())) {
                bitmap = ThumbnailUtils.createVideoThumbnail(path, 1);
            } else {
                try {
                    MediaMetadataRetriever media = new MediaMetadataRetriever();
                    media.setDataSource(this.getContext(), model.getLocalPath());
                    bitmap = media.getFrameAtTime();
                } catch (Exception var10) {
                    RLog.e("Sight-SightMessageHandler", "video get thumbnail error", var10);
                    bitmap = null;
                }
            }

            if (bitmap != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(CompressFormat.JPEG, 30, outputStream);
                data = outputStream.toByteArray();
                model.setBase64(Base64.encodeToString(data, 2));
                outputStream.close();
                FileUtils.byte2File(data, uri.toString() + "/video/thumbnail/", name);
                model.setThumbUri(Uri.parse("file://" + uri.toString() + "/video/thumbnail/" + name));
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
        } catch (IllegalArgumentException var11) {
            RLog.e("Sight-SightMessageHandler", "encodeMessage beforeEncodeMessage Not Base64 Content!", var11);
        } catch (IOException var12) {
            RLog.e("Sight-SightMessageHandler", "encodeMessage beforeEncodeMessage IOException", var12);
        } catch (Exception var13) {
            RLog.e("Sight-SightMessageHandler", "encodeMessage exception", var13);
        }

    }

    private static boolean isImageFile(byte[] data) {
        if (data != null && data.length != 0) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            return options.outWidth != -1;
        } else {
            return false;
        }
    }
}
