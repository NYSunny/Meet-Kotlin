//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.destruct;

import io.rong.common.RLog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.DestructCountDownTimerListener;
import io.rong.imlib.RongIMClient.OperationCallback;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message.MessageDirection;
import io.rong.message.DestructionCmdMessage;
import java.util.HashMap;
import java.util.Map;

public class DestructionTaskManager {
    private static final String TAG = "DestructionTaskManager";
    private Map<String, DestructCountDownTimer> mCountDownTimers;

    private DestructionTaskManager() {
        this.mCountDownTimers = new HashMap();
    }

    public void BeginDestruct(Message pMessage, DestructCountDownTimerListener pListener) {
        if (pMessage != null && pMessage.getContent() != null && pListener != null) {
            if (pMessage.getContent().isDestruct() && pMessage.getMessageDirection() == MessageDirection.RECEIVE) {
                this.startCountDown(pMessage, pListener);
                this.sendDestructingMsg(pMessage);
            }

        } else {
            RLog.e("DestructionTaskManager", "BeginDestruct pMessage or content or pListener should not be null!");
        }
    }

    public void messageStopDestruct(Message pMessage) {
        if (pMessage != null && pMessage.getContent() != null) {
            if (pMessage.getContent().isDestruct() && pMessage.getMessageDirection() == MessageDirection.RECEIVE) {
                this.cancelCountDown(pMessage.getUId());
                this.resetReadTime(pMessage);
            }

        } else {
            RLog.e("DestructionTaskManager", "messageStopDestruct pMessage or content should not be null!");
        }
    }

    public static DestructionTaskManager getInstance() {
        return DestructionTaskManager.DestructionTaskManagerHolder.instance;
    }

    private void startCountDown(final Message pMessage, final DestructCountDownTimerListener pListener) {
        DestructCountDownTimer destructCountDownTimer;
        if (this.mCountDownTimers.containsKey(pMessage.getUId())) {
            destructCountDownTimer = (DestructCountDownTimer)this.mCountDownTimers.get(pMessage.getUId());
        } else {
            long countDownTime;
            if (pMessage.getReadTime() <= 0L) {
                countDownTime = pMessage.getContent().getDestructTime() * 1000L;
            } else {
                long destructTime = pMessage.getContent().getDestructTime() * 1000L;
                long delay = destructTime - (System.currentTimeMillis() - pMessage.getReadTime());
                countDownTime = delay <= 0L ? 0L : delay;
            }

            destructCountDownTimer = new DestructCountDownTimer(pMessage.getUId(), new DestructCountDownTimerListener() {
                public void onTick(long untilFinished, String messageId) {
                    pListener.onTick(untilFinished, messageId);
                    if (untilFinished <= 0L) {
                        DestructionTaskManager.this.deleteMessage(pMessage);
                        DestructionTaskManager.this.mCountDownTimers.remove(messageId);
                    }

                }

                public void onStop(String messageId) {
                    pListener.onStop(messageId);
                    DestructionTaskManager.this.mCountDownTimers.remove(messageId);
                }
            }, countDownTime);
            this.mCountDownTimers.put(pMessage.getUId(), destructCountDownTimer);
        }

        destructCountDownTimer.start();
    }

    private void cancelCountDown(String pMessageId) {
        if (this.mCountDownTimers.containsKey(pMessageId)) {
            DestructCountDownTimer destructCountDownTimer = (DestructCountDownTimer)this.mCountDownTimers.get(pMessageId);
            if (destructCountDownTimer != null) {
                destructCountDownTimer.cancel();
            }

            this.mCountDownTimers.remove(pMessageId);
        }

    }

    public void deleteMessage(Message pMessage) {
        if (pMessage != null) {
            RongIMClient.getInstance().deleteRemoteMessages(pMessage.getConversationType(), pMessage.getTargetId(), new Message[]{pMessage}, (OperationCallback)null);
            RongIMClient.getInstance().deleteMessages(new int[]{pMessage.getMessageId()}, (ResultCallback)null);
        }

    }

    public void deleteMessages(ConversationType pConversationType, String pTargetId, Message[] pMessages) {
        if (pMessages != null && pMessages.length > 0 && pConversationType != null && pTargetId != null) {
            int[] messageIds = new int[pMessages.length];

            for(int i = 0; i < pMessages.length; ++i) {
                if (pMessages[i] != null) {
                    messageIds[i] = pMessages[i].getMessageId();
                }
            }

            RongIMClient.getInstance().deleteMessages(messageIds, (ResultCallback)null);
            RongIMClient.getInstance().deleteRemoteMessages(pConversationType, pTargetId, pMessages, (OperationCallback)null);
        }

    }

    private void sendDestructingMsg(Message message) {
        if (message.getReadTime() <= 0L) {
            long serviceTime = System.currentTimeMillis();
            RongIMClient.getInstance().setMessageReadTime((long)message.getMessageId(), serviceTime, (OperationCallback)null);
            message.setReadTime(serviceTime);
            DestructionCmdMessage destructionCmdMessage = new DestructionCmdMessage();
            destructionCmdMessage.addBurnMessageUId(message.getUId());
            MessageBufferPool.getInstance().putMessageInBuffer(Message.obtain(message.getTargetId(), message.getConversationType(), destructionCmdMessage));
        }

    }

    private void resetReadTime(Message pMessage) {
        RongIMClient.getInstance().setMessageReadTime((long)pMessage.getMessageId(), 0L, (OperationCallback)null);
        pMessage.setReadTime(0L);
    }

    private static class DestructionTaskManagerHolder {
        private static DestructionTaskManager instance = new DestructionTaskManager();

        private DestructionTaskManagerHolder() {
        }
    }
}
