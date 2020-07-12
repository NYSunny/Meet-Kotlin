//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import android.net.Uri;
import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HQVoiceMessageHandler extends MessageHandler<HQVoiceMessage> {
    private static final String TAG = "HQVoiceMessageHandler";
    private static final String VOICE_PATH = "/hq_voice/";

    public HQVoiceMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, HQVoiceMessage content) {
    }

    public void encodeMessage(Message message) {
        HQVoiceMessage model = (HQVoiceMessage)message.getContent();
        Uri uri = NativeClient.getInstance().obtainMediaFileSavedUri();
        byte[] voiceData = FileUtils.file2byte(this.getContext(), model.getLocalPath());
        File file = null;

        try {
            String name = message.getMessageId() + ".aac";
            file = saveFile(voiceData, uri.toString() + "/hq_voice/", name);
        } catch (IOException var7) {
            RLog.e("HQVoiceMessageHandler", "IOException ", var7);
        }

        if (file != null && file.exists()) {
            model.setLocalPath(Uri.fromFile(file));
            model.setName(file.getName());
        }

    }

    private static File saveFile(byte[] data, String path, String fileName) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            boolean successMkdir = file.mkdirs();
            if (!successMkdir) {
                RLog.e("HQVoiceMessageHandler", "Created folders unSuccessfully");
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
