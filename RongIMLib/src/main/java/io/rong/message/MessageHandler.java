//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.content.Context;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

public abstract class MessageHandler<T extends MessageContent> {
    private Context context;
    protected IHandleMessageListener mHandleMessageListener;

    public MessageHandler(Context context) {
        this.context = context;
    }

    public abstract void decodeMessage(Message var1, T var2);

    public abstract void encodeMessage(Message var1);

    public Context getContext() {
        return this.context;
    }

    public void setHandleMessageListener(IHandleMessageListener mHandleMessageListener) {
        this.mHandleMessageListener = mHandleMessageListener;
    }
}
