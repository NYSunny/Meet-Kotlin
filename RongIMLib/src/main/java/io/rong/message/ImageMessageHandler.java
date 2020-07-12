//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import io.rong.common.FileInfo;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import io.rong.message.utils.BitmapUtil;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageMessageHandler extends MessageHandler<ImageMessage> {
    private static final String TAG = "ImageMessageHandler";
    private static int COMPRESSED_SIZE = 1080;
    private static int COMPRESSED_QUALITY = 70;
    private static final int COMPRESSED_FULL_QUALITY = 100;
    private static int MAX_ORIGINAL_IMAGE_SIZE = 500;
    private static final int THUMB_COMPRESSED_SIZE = 240;
    private static final int THUMB_COMPRESSED_MIN_SIZE = 100;
    private static final int THUMB_COMPRESSED_QUALITY = 30;
    private static final String IMAGE_LOCAL_PATH = "/image/local/";
    private static final String IMAGE_THUMBNAIL_PATH = "/image/thumbnail/";
    private static final int MAX_FILE_LENGTH = 20480;

    public ImageMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, ImageMessage model) {
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getUId() + ".jpg";
        if (TextUtils.isEmpty(message.getUId())) {
            name = message.getMessageId() + ".jpg";
        }

        String thumb = uri.toString() + "/image/thumbnail/";
        String local = uri.toString() + "/image/local/";
        model.setLocalUri((Uri)null);
        File localFile = new File(local + name);
        if (localFile.exists()) {
            model.setLocalUri(Uri.parse("file://" + local + name));
        }

        File thumbFile = new File(thumb + name);
        if (!TextUtils.isEmpty(model.getBase64()) && !thumbFile.exists()) {
            byte[] data = null;

            try {
                data = Base64.decode(model.getBase64(), 2);
            } catch (IllegalArgumentException var11) {
                RLog.e("ImageMessageHandler", "afterDecodeMessage Not Base64 Content!");
                RLog.e("ImageMessageHandler", "IllegalArgumentException ", var11);
            }

            if (!isImageFile(data)) {
                RLog.e("ImageMessageHandler", "afterDecodeMessage Not Image File!");
                return;
            }

            FileUtils.byte2File(data, thumb, name);
        }

        model.setThumUri(Uri.parse("file://" + thumb + name));
        model.setBase64((String)null);
    }

    public void encodeMessage(Message message) {
        ImageMessage model;
        if (message.getContent() instanceof ReferenceMessage) {
            model = (ImageMessage)((ReferenceMessage)message.getContent()).getReferenceContent();
        } else {
            model = (ImageMessage)message.getContent();
        }

        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getMessageId() + ".jpg";
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Resources resources = this.getContext().getResources();

        try {
            COMPRESSED_QUALITY = resources.getInteger(resources.getIdentifier("rc_image_quality", "integer", this.getContext().getPackageName()));
            COMPRESSED_SIZE = resources.getInteger(resources.getIdentifier("rc_image_size", "integer", this.getContext().getPackageName()));
            MAX_ORIGINAL_IMAGE_SIZE = resources.getInteger(resources.getIdentifier("rc_max_original_image_size", "integer", this.getContext().getPackageName()));
        } catch (NotFoundException var21) {
            var21.printStackTrace();
        }

        if (model != null && FileUtils.isValidateLocalUri(model.getThumUri())) {
            File file = new File(uri.toString() + "/image/thumbnail/" + name);
            byte[] data;
            if (file.exists()) {
                model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                data = FileUtils.file2byte(file);
                if (data != null) {
                    model.setBase64(Base64.encodeToString(data, 2));
                }
            } else {
                try {
                    BitmapUtil.getFactoryBitmap(this.getContext(), model.getThumUri(), options);
                    String imageFormat = options.outMimeType != null ? options.outMimeType : "";
                    RLog.d("ImageMessageHandler", "Image format:" + imageFormat);
                    if (options.outWidth <= 240 && options.outHeight <= 240) {
                        byte var26 = -1;
                        switch(imageFormat.hashCode()) {
                            case -1487018032:
                                if (imageFormat.equals("image/webp")) {
                                    var26 = 1;
                                }
                                break;
                            case -879267568:
                                if (imageFormat.equals("image/gif")) {
                                    var26 = 0;
                                }
                        }

                        switch(var26) {
                            case 0:
                            case 1:
                                Options bmOptions = new Options();
                                bmOptions.inJustDecodeBounds = false;
                                RLog.d("ImageMessageHandler", "beforeEncodeMessage Thumbnail not save yet! " + model.getThumUri());
                                Bitmap bitmap = BitmapUtil.getFactoryBitmap(this.getContext(), model.getThumUri(), bmOptions);
                                if (bitmap != null) {
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                    bitmap.compress(CompressFormat.JPEG, 100, outputStream);
                                    data = outputStream.toByteArray();
                                    outputStream.close();
                                    if (data != null) {
                                        model.setBase64(Base64.encodeToString(data, 2));
                                        FileUtils.byte2File(data, uri.toString() + "/image/thumbnail/", name);
                                        model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                                    }

                                    if (!bitmap.isRecycled()) {
                                        bitmap.recycle();
                                    }
                                }
                                break;
                            default:
                                FileInfo fileInfo = FileUtils.getFileInfoByUri(this.getContext(), model.getThumUri());
                                long fileSize;
                                if (fileInfo != null) {
                                    fileSize = fileInfo.getSize();
                                } else {
                                    RLog.e("ImageMessageHandler", "ContentDocument is null");
                                    fileSize = 0L;
                                }

                                if (fileSize > 20480L) {
                                    int sizeLimit = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
                                    Bitmap bitmapLargeFile = BitmapUtil.getThumbBitmap(this.getContext(), model.getThumUri(), sizeLimit, 100);
                                    if (bitmapLargeFile != null) {
                                        ByteArrayOutputStream outputStreamLargeFile = new ByteArrayOutputStream();
                                        bitmapLargeFile.compress(CompressFormat.JPEG, 30, outputStreamLargeFile);
                                        data = outputStreamLargeFile.toByteArray();
                                        model.setBase64(Base64.encodeToString(data, 2));
                                        outputStreamLargeFile.close();
                                        FileUtils.byte2File(data, uri.toString() + "/image/thumbnail/", name);
                                        model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                                        if (!bitmapLargeFile.isRecycled()) {
                                            bitmapLargeFile.recycle();
                                        }
                                    }
                                } else {
                                    data = FileUtils.file2byte(this.getContext(), model.getThumUri());
                                    if (data != null) {
                                        model.setBase64(Base64.encodeToString(data, 2));
                                        String path = uri.toString() + "/image/thumbnail/";
                                        if (FileUtils.copyFileToInternal(this.getContext(), model.getThumUri(), path, name)) {
                                            model.setThumUri(Uri.parse("file://" + path + name));
                                        }
                                    }
                                }
                        }
                    } else {
                        Bitmap bitmap = BitmapUtil.getThumbBitmap(this.getContext(), model.getThumUri(), 240, 100);
                        if (bitmap != null) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            boolean success = bitmap.compress(CompressFormat.JPEG, 30, outputStream);
                            if (!success) {
                                bitmap.compress(CompressFormat.PNG, 30, outputStream);
                            }

                            data = outputStream.toByteArray();
                            model.setBase64(Base64.encodeToString(data, 2));
                            outputStream.close();
                            FileUtils.byte2File(data, uri.toString() + "/image/thumbnail/", name);
                            model.setThumUri(Uri.parse("file://" + uri.toString() + "/image/thumbnail/" + name));
                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }
                    }
                } catch (Exception var22) {
                    RLog.e("ImageMessageHandler", "Exception ", var22);
                }
            }
        }

        if (model != null && FileUtils.isValidateLocalUri(model.getLocalUri())) {
            File file = new File(uri.toString() + "/image/local/" + name);
            if (file.exists()) {
                model.setLocalUri(Uri.parse("file://" + uri.toString() + "/image/local/" + name));
            } else {
                try {
                    BitmapUtil.getFactoryBitmap(this.getContext(), model.getLocalUri(), options);
                    FileInfo fileInfo = FileUtils.getFileInfoByUri(this.getContext(), model.getThumUri());
                    long fileSize;
                    if (fileInfo != null) {
                        fileSize = fileInfo.getSize();
                    } else {
                        RLog.e("ImageMessageHandler", "ContentDocument is null");
                        fileSize = 0L;
                    }

                    fileSize /= 1024L;
                    if (!model.isFull()) {
                        Bitmap bitmap = BitmapUtil.getNewResizedBitmap(this.getContext(), model.getLocalUri(), COMPRESSED_SIZE);
                        if (bitmap != null) {
                            String dir = uri.toString() + "/image/local/";
                            file = new File(dir);
                            if (!file.exists()) {
                                boolean successMkdir = file.mkdirs();
                                if (!successMkdir) {
                                    RLog.e("ImageMessageHandler", "Created folders unSuccessfully");
                                }
                            }

                            file = new File(dir + name);
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            int quality;
                            if (fileSize > (long)MAX_ORIGINAL_IMAGE_SIZE) {
                                quality = COMPRESSED_QUALITY;
                            } else {
                                quality = 100;
                            }

                            boolean success = bitmap.compress(CompressFormat.JPEG, quality, bos);
                            if (!success) {
                                bitmap.compress(CompressFormat.PNG, quality, bos);
                            }

                            bos.close();
                            model.setLocalUri(Uri.parse("file://" + dir + name));
                            if (!bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        }
                    } else if (FileUtils.copyFileToInternal(this.getContext(), model.getLocalUri(), uri.toString() + "/image/local/", name)) {
                        model.setLocalUri(Uri.parse("file://" + uri.toString() + "/image/local/" + name));
                    }
                } catch (IOException var20) {
                    RLog.e("ImageMessageHandler", "IOException  ", var20);
                    RLog.e("ImageMessageHandler", "beforeEncodeMessage IOException");
                }
            }
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
