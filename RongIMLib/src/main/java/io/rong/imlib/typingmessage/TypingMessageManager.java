//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.typingmessage;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import io.rong.common.RLog;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.IRongCallback.ISendMessageCallback;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.TypingStatusListener;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.Conversation.ConversationType;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TypingMessageManager {
    private static final String TAG = "TypingMessageManager";
    private static final String SEPARATOR = ";;;";
    private static int DISAPPEAR_INTERVAL = 6000;
    private HashMap<String, LinkedHashMap<String, TypingStatus>> mTypingMap;
    private HashMap<String, Long> mSendingConversation;
    private Handler mHandler;
    private TypingStatusListener sTypingStatusListener;
    private boolean isShowMessageTyping;

    private TypingMessageManager() {
        this.isShowMessageTyping = false;
        this.mTypingMap = new HashMap();
        this.mSendingConversation = new HashMap();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static TypingMessageManager getInstance() {
        return TypingMessageManager.SingletonHolder.sInstance;
    }

    public void init(Context context) {
        try {
            Resources resources = context.getResources();
            this.isShowMessageTyping = resources.getBoolean(resources.getIdentifier("rc_typing_status", "bool", context.getPackageName()));
        } catch (NotFoundException var3) {
            RLog.e("TypingMessageManager", "getTypingStatus rc_typing_status not configure in rc_configuration.xml");
            var3.printStackTrace();
        }

    }

    public boolean isShowMessageTyping() {
        return this.isShowMessageTyping;
    }

    public Collection<TypingStatus> getTypingUserListFromConversation(ConversationType conversationType, String targetId) {
        String key = conversationType.getName() + ";;;" + targetId;
        return this.mTypingMap.get(key) == null ? null : ((LinkedHashMap)this.mTypingMap.get(key)).values();
    }

    public void sendTypingMessage(ConversationType conversationType, String targetId, String typingContentType) {
        if (conversationType == null) {
            RLog.e("TypingMessageManager", "sendTypingMessage conversationType should not be null!");
        } else {
            final String key = conversationType.getName() + ";;;" + targetId;
            if (conversationType.equals(ConversationType.PRIVATE)) {
                if (!this.mSendingConversation.containsKey(key)) {
                    TypingStatusMessage typingStatusMessage = new TypingStatusMessage(typingContentType, (String)null);
                    this.mSendingConversation.put(key, 0L);
                    RongIMClient.getInstance().sendMessage(conversationType, targetId, typingStatusMessage, (String)null, (String)null, new ISendMessageCallback() {
                        public void onAttached(Message message) {
                        }

                        public void onSuccess(Message message) {
                            TypingMessageManager.this.mHandler.postDelayed(new Runnable() {
                                public void run() {
                                    TypingMessageManager.this.mSendingConversation.remove(key);
                                }
                            }, (long)TypingMessageManager.DISAPPEAR_INTERVAL);
                        }

                        public void onError(Message message, ErrorCode errorCode) {
                        }
                    });
                } else {
                    RLog.d("TypingMessageManager", "sendTypingStatus typing message in this conversation is sending");
                }

            }
        }
    }

    public void setTypingEnd(ConversationType conversationType, String targetId) {
        String key = conversationType.getName() + ";;;" + targetId;
        if (conversationType.equals(ConversationType.PRIVATE)) {
            this.mSendingConversation.remove(key);
        }
    }

    public void setTypingMessageStatusListener(TypingStatusListener listener) {
        this.sTypingStatusListener = listener;
    }

    public boolean onReceiveMessage(@NonNull Message message) {
        if (message.getContent() instanceof TypingStatusMessage && this.isShowMessageTyping) {
            getInstance().onReceiveTypingMessage(message);
            return true;
        } else {
            getInstance().onReceiveOtherMessage(message);
            return false;
        }
    }

    private void onReceiveTypingMessage(Message message) {
        if (!message.getSenderUserId().equals(RongIMClient.getInstance().getCurrentUserId())) {
            final ConversationType conversationType = message.getConversationType();
            final String targetId = message.getTargetId();
            TypingStatusMessage typingStatusMessage = (TypingStatusMessage)message.getContent();
            String typingContentType = typingStatusMessage.getTypingContentType();
            if (typingContentType != null) {
                final String userId = message.getSenderUserId();
                final String key = conversationType.getName() + ";;;" + targetId;
                LinkedHashMap map;
                TypingStatus typingStatus;
                if (this.mTypingMap.containsKey(key)) {
                    map = (LinkedHashMap)this.mTypingMap.get(key);
                    if (map.get(userId) == null) {
                        typingStatus = new TypingStatus(userId, typingContentType, message.getSentTime());
                        map.put(userId, typingStatus);
                        if (this.sTypingStatusListener != null) {
                            this.sTypingStatusListener.onTypingStatusChanged(conversationType, targetId, map.values());
                        }
                    }
                } else {
                    map = new LinkedHashMap();
                    typingStatus = new TypingStatus(userId, typingContentType, message.getSentTime());
                    map.put(userId, typingStatus);
                    if (this.sTypingStatusListener != null) {
                        this.sTypingStatusListener.onTypingStatusChanged(conversationType, targetId, map.values());
                    }

                    this.mTypingMap.put(key, map);
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            if (TypingMessageManager.this.mTypingMap.containsKey(key)) {
                                LinkedHashMap<String, TypingStatus> map = (LinkedHashMap)TypingMessageManager.this.mTypingMap.get(key);
                                if (map.get(userId) != null) {
                                    map.remove(userId);
                                    if (TypingMessageManager.this.sTypingStatusListener != null) {
                                        TypingMessageManager.this.sTypingStatusListener.onTypingStatusChanged(conversationType, targetId, map.values());
                                    }

                                    if (map.isEmpty()) {
                                        TypingMessageManager.this.mTypingMap.remove(key);
                                    }
                                }
                            }

                        }
                    }, (long)DISAPPEAR_INTERVAL);
                }

            }
        }
    }

    private void onReceiveOtherMessage(Message message) {
        MessageContent content = message.getContent();
        MessageTag tag = (MessageTag)content.getClass().getAnnotation(MessageTag.class);
        if (tag != null && (tag.flag() & 1) == 1) {
            ConversationType conversationType = message.getConversationType();
            String targetId = message.getTargetId();
            String userId = message.getSenderUserId();
            String key = conversationType.getName() + ";;;" + targetId;
            if (this.mTypingMap.containsKey(key)) {
                LinkedHashMap<String, TypingStatus> map = (LinkedHashMap)this.mTypingMap.get(key);
                if (map.get(userId) != null) {
                    map.remove(userId);
                    if (this.sTypingStatusListener != null) {
                        this.sTypingStatusListener.onTypingStatusChanged(conversationType, targetId, map.values());
                    }

                    if (map.isEmpty()) {
                        this.mTypingMap.remove(key);
                    }
                }
            }
        }

    }

    private static class SingletonHolder {
        static TypingMessageManager sInstance = new TypingMessageManager();

        private SingletonHolder() {
        }
    }
}
