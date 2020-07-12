//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import io.rong.common.FileInfo;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import io.rong.message.utils.BitmapUtil;
import java.io.File;

public class GIFMessageHandler extends MessageHandler<GIFMessage> {
    private static final String TAG = "GIFMessageHandler";
    private static final String IMAGE_LOCAL_PATH = "/image/local/";

    public GIFMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, GIFMessage content) {
    }

    public void encodeMessage(Message message) {
        GIFMessage model = (GIFMessage)message.getContent();
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getMessageId() + ".gif";
        if (model != null && FileUtils.isValidateLocalUri(model.getLocalUri())) {
            String localPath = uri.toString() + "/image/local/" + name;
            File file = new File(localPath);
            if (file.exists()) {
                model.setLocalUri(Uri.parse("file://" + uri.toString() + "/image/local/" + name));
            } else if (FileUtils.copyFileToInternal(this.getContext(), model.getLocalUri(), uri.toString() + "/image/local/", name)) {
                model.setLocalUri(Uri.parse("file://" + uri.toString() + "/image/local/" + name));
            }

            Bitmap bitmap = BitmapUtil.getFactoryBitmap(this.getContext(), model.getLocalUri());
            if (bitmap != null) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                model.setWidth(width);
                model.setHeight(height);
                FileInfo fileInfo = FileUtils.getFileInfoByUri(this.getContext(), model.getLocalUri());
                if (fileInfo != null) {
                    model.setGifDataSize(fileInfo.getSize());
                    model.setName(fileInfo.getName());
                } else {
                    RLog.e("GIFMessageHandler", "Document is null");
                }
            }
        }

    }
}
