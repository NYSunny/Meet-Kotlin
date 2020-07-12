//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VoiceMessageHandler extends MessageHandler<VoiceMessage> {
    private static final String TAG = "VoiceMessageHandler";
    private static final String VOICE_PATH = "/voice/";

    public VoiceMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, VoiceMessage model) {
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        String name = message.getMessageId() + ".amr";
        if (message.getMessageId() == 0) {
            name = message.getSentTime() + ".amr";
        }

        File file = new File(uri.toString() + "/voice/" + name);
        if (!TextUtils.isEmpty(model.getBase64()) && !file.exists()) {
            try {
                byte[] data = Base64.decode(model.getBase64(), 2);
                file = saveFile(data, uri.toString() + "/voice/", name);
            } catch (IllegalArgumentException var7) {
                RLog.e("VoiceMessageHandler", "afterDecodeMessage Not Base64 Content!");
                RLog.e("VoiceMessageHandler", "IllegalArgumentException ", var7);
            } catch (IOException var8) {
                RLog.e("VoiceMessageHandler", "IOException ", var8);
            }
        }

        model.setUri(Uri.fromFile(file));
        model.setBase64((String)null);
    }

    public void encodeMessage(Message message) {
        VoiceMessage model = (VoiceMessage)message.getContent();
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        byte[] voiceData = FileUtils.getByteFromUri(model.getUri());
        File file = null;

        try {
            String base64 = Base64.encodeToString(voiceData, 2);
            model.setBase64(base64);
            String name = message.getMessageId() + ".amr";
            file = saveFile(voiceData, uri.toString() + "/voice/", name);
        } catch (IllegalArgumentException var8) {
            RLog.e("VoiceMessageHandler", "beforeEncodeMessage Not Base64 Content!");
            RLog.e("VoiceMessageHandler", "IllegalArgumentException ", var8);
        } catch (IOException var9) {
            RLog.e("VoiceMessageHandler", "IOException ", var9);
        }

        if (file != null && file.exists()) {
            model.setUri(Uri.fromFile(file));
        }

    }

    private static File saveFile(byte[] data, String path, String fileName) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            boolean successMkdir = file.mkdirs();
            if (!successMkdir) {
                RLog.e("VoiceMessageHandler", "Created folders unSuccessfully");
            }
        }

        file = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bos.write(data);
        bos.flush();
        bos.close();
        return file;
    }
}
