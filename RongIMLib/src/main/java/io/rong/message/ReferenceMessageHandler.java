//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

public class ReferenceMessageHandler extends MessageHandler<ReferenceMessage> {
    public ReferenceMessageHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, ReferenceMessage model) {
        if (message != null && model != null && model.getReferenceContent() != null) {
            MessageContent content = model.getReferenceContent();
            if (content instanceof ImageMessage) {
                ImageMessage imageMessage = (ImageMessage)content;
                ImageMessageHandler handler = new ImageMessageHandler(this.getContext());
                handler.decodeMessage(message, imageMessage);
            }
        }
    }

    public void encodeMessage(Message message) {
        if (message.getContent() instanceof ReferenceMessage) {
            ReferenceMessage model = (ReferenceMessage)message.getContent();
            if (model.getReferenceContent() != null && model.getReferenceContent() instanceof ImageMessage) {
                ImageMessageHandler handler = new ImageMessageHandler(this.getContext());
                handler.encodeMessage(message);
            }
        }
    }
}
