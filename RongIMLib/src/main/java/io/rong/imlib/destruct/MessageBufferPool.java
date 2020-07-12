//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.destruct;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.NonNull;
import io.rong.imlib.MD5;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.message.DestructionCmdMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MessageBufferPool {
    private static final String RETRY_MESSAGES = "retry_messages_%s";
    private static final String SPLIT = "'''";
    private static MessageBufferPool sInstance = new MessageBufferPool();
    private Handler mHandler;
    private final Map<String, Message> mMessageList = new HashMap();
    private ExecutorService mSingleThreadPoolExecutor = Executors.newSingleThreadExecutor();
    private static Context mContext;
    private volatile boolean isActive = false;
    private Runnable mSendMessageTask = new Runnable() {
        public void run() {
            synchronized(MessageBufferPool.this.mMessageList) {
                Iterator var2 = MessageBufferPool.this.mMessageList.entrySet().iterator();

                while(var2.hasNext()) {
                    Entry<String, Message> entry = (Entry)var2.next();
                    Message message = (Message)entry.getValue();
                    MessageBufferPool.this.sendMessage(message);
                }

                MessageBufferPool.this.mMessageList.clear();
                MessageBufferPool.this.isActive = false;
            }
        }
    };

    private MessageBufferPool() {
        HandlerThread handlerThread = new HandlerThread("message-buffer-pool");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper());
    }

    public static MessageBufferPool getInstance() {
        return sInstance;
    }

    public static void init(Context context) {
        mContext = context;
    }

    public void putMessageInBuffer(@NonNull final Message message) {
        boolean isDestructionMessage = message.getContent() instanceof DestructionCmdMessage;
        if (isDestructionMessage) {
            this.mSingleThreadPoolExecutor.execute(new Runnable() {
                public void run() {
                    synchronized(MessageBufferPool.this.mMessageList) {
                        String targetId = message.getTargetId();
                        Message msg = (Message)MessageBufferPool.this.mMessageList.get(targetId);
                        if (msg != null) {
                            DestructionCmdMessage destructionCmdMessage = (DestructionCmdMessage)msg.getContent();
                            DestructionCmdMessage newMsg = (DestructionCmdMessage)message.getContent();
                            destructionCmdMessage.getBurnMessageUIds().addAll(newMsg.getBurnMessageUIds());
                        } else {
                            MessageBufferPool.this.mMessageList.put(message.getTargetId(), message);
                        }
                    }

                    if (!MessageBufferPool.this.isActive) {
                        MessageBufferPool.this.isActive = true;
                        MessageBufferPool.this.mHandler.postDelayed(MessageBufferPool.this.mSendMessageTask, 500L);
                    }

                }
            });
        }
    }

    private void cacheFailedMessage(final Message message) {
        this.mSingleThreadPoolExecutor.execute(new Runnable() {
            public void run() {
                String spName = String.format("retry_messages_%s", MD5.encrypt(RongIMClient.getInstance().getCurrentUserId()));
                SharedPreferences sp = MessageBufferPool.mContext.getSharedPreferences(spName, 0);
                String key = message.getTargetId() + "'''" + message.getConversationType().getValue();
                String value = sp.getString(key, (String)null);
                if (value == null) {
                    value = "";
                }

                StringBuilder sb = new StringBuilder(value);
                DestructionCmdMessage destructionCmdMessage = (DestructionCmdMessage)message.getContent();
                List<String> msgUIds = destructionCmdMessage.getBurnMessageUIds();

                for(int i = 0; i < msgUIds.size(); ++i) {
                    sb.append("'''").append((String)msgUIds.get(i));
                }

                sp.edit().putString(key, sb.toString()).commit();
            }
        });
    }

    private List<Message> getAndClearFailedMessages() {
        String spName = String.format("retry_messages_%s", MD5.encrypt(RongIMClient.getInstance().getCurrentUserId()));
        SharedPreferences sp = mContext.getSharedPreferences(spName, 0);
        Map<String, String> messages = (Map<String, String>) sp.getAll();
        List<Message> messageList = new ArrayList();
        Iterator var5 = messages.entrySet().iterator();

        while(var5.hasNext()) {
            Entry<String, String> entry = (Entry)var5.next();
            String[] key = ((String)entry.getKey()).split("'''");
            String targetId = key[0];
            ConversationType conversationType = ConversationType.setValue(Integer.valueOf(key[1]));
            DestructionCmdMessage message = new DestructionCmdMessage();
            String value = (String)entry.getValue();
            String[] UIds = value.replaceFirst("'''", "").split("'''");
            List<String> msgUIds = new ArrayList(Arrays.asList(UIds));
            message.setBurnMessageUIds(msgUIds);
            messageList.add(Message.obtain(targetId, conversationType, message));
        }

        sp.edit().clear().commit();
        return messageList;
    }

    public void retrySendMessages() {
        List<Message> messages = this.getAndClearFailedMessages();
        Iterator var2 = messages.iterator();

        while(var2.hasNext()) {
            Message message = (Message)var2.next();
            this.putMessageInBuffer(message);
        }

    }

    private void sendMessage(Message message) {
        RongIMClient.getInstance().sendMessage(message, (String)null, (String)null, new ISendMessageCallback() {
            public void onAttached(Message message) {
            }

            public void onSuccess(Message message) {
            }

            public void onError(Message message, ErrorCode errorCode) {
                if (errorCode == ErrorCode.MSG_SEND_OVERFREQUENCY) {
                    MessageBufferPool.this.putMessageInBuffer(message);
                } else {
                    MessageBufferPool.this.cacheFailedMessage(message);
                }

            }
        });
    }
}
