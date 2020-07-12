//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.net.Uri;
import io.rong.common.RLog;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.ResultCallback;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.RTCUser;
import io.rong.imlib.model.UserOnlineStatusInfo;
import io.rong.imlib.model.Message.SentStatus;
import io.rong.message.MediaMessageContent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IRongCallback {
    public interface IRTCConfigCallback {
        void onSuccess(String var1, long var2);

        void onError(ErrorCode var1);
    }

    public interface IRtcIODataCallback {
        void onSuccess(Map<String, String> var1);

        void onError(ErrorCode var1);
    }

    public interface IRTCJoinRoomCallbackEx<T> {
        void onSuccess(List<RTCUser> var1, T var2);

        void onError(ErrorCode var1);
    }

    public interface IRTCJoinRoomCallback extends IRongCallback.IRTCJoinRoomCallbackEx<String> {
    }

    public interface IRTCDataCallback {
        void onSuccess(List<RTCUser> var1);

        void onError(ErrorCode var1);
    }

    public interface ISetSubscribeStatusCallback {
        void onStatusReceived(String var1, ArrayList<UserOnlineStatusInfo> var2);
    }

    public interface ISetUserOnlineStatusCallback {
        void onSuccess();

        void onError(int var1);
    }

    public interface IGetUserOnlineStatusCallback {
        void onSuccess(ArrayList<UserOnlineStatusInfo> var1);

        void onError(int var1);
    }

    public interface IChatRoomHistoryMessageCallback {
        void onSuccess(List<Message> var1, long var2);

        void onError(ErrorCode var1);
    }

    public interface IDownloadMediaFileCallback {
        void onFileNameChanged(String var1);

        void onSuccess();

        void onProgress(int var1);

        void onError(ErrorCode var1);

        void onCanceled();
    }

    public interface IDownloadMediaMessageCallback {
        void onSuccess(Message var1);

        void onProgress(Message var1, int var2);

        void onError(Message var1, ErrorCode var2);

        void onCanceled(Message var1);
    }

    public static class MediaMessageUploader {
        private IRongCallback.ISendMediaMessageCallbackWithUploader callbackWithUploader;
        private Message message;
        private String pushContent;
        private String pushData;

        public MediaMessageUploader(Message message, String pushContent, String pushData, IRongCallback.ISendMediaMessageCallbackWithUploader callbackWithUploader) {
            this.callbackWithUploader = callbackWithUploader;
            this.message = message;
            this.pushContent = pushContent;
            this.pushData = pushData;
        }

        public void update(int progress) {
            if (this.callbackWithUploader != null) {
                this.callbackWithUploader.onProgress(this.message, progress);
            }

        }

        public void error() {
            this.message.setSentStatus(SentStatus.FAILED);
            RongIMClient.getInstance().setMessageSentStatus(this.message, (ResultCallback)null);
            if (this.callbackWithUploader != null) {
                this.callbackWithUploader.onError(this.message, ErrorCode.RC_MSG_SEND_FAIL);
            }

        }

        public void cancel() {
            if (this.callbackWithUploader != null) {
                this.callbackWithUploader.onCanceled(this.message);
            }

        }

        public void success(Uri uploadedUri) {
            if (uploadedUri == null) {
                RLog.e("MediaMessageUploader", "uploadedUri is null.");
                if (this.callbackWithUploader != null) {
                    this.callbackWithUploader.onError(this.message, ErrorCode.RC_MSG_SEND_FAIL);
                }

            } else {
                MediaMessageContent content = (MediaMessageContent)this.message.getContent();
                content.setMediaUrl(uploadedUri);
                RongIMClient.getInstance().sendMediaMessage(this.message, this.pushContent, this.pushData, new IRongCallback.ISendMediaMessageCallback() {
                    public void onProgress(Message message, int progress) {
                    }

                    public void onAttached(Message message) {
                    }

                    public void onSuccess(Message message) {
                        if (MediaMessageUploader.this.callbackWithUploader != null) {
                            MediaMessageUploader.this.callbackWithUploader.onSuccess(message);
                        }

                    }

                    public void onError(Message message, ErrorCode errorCode) {
                        if (MediaMessageUploader.this.callbackWithUploader != null) {
                            MediaMessageUploader.this.callbackWithUploader.onError(message, errorCode);
                        }

                    }

                    public void onCanceled(Message message) {
                    }
                });
            }
        }
    }

    public interface ISendMediaMessageCallbackWithUploader {
        void onAttached(Message var1, IRongCallback.MediaMessageUploader var2);

        void onProgress(Message var1, int var2);

        void onSuccess(Message var1);

        void onError(Message var1, ErrorCode var2);

        void onCanceled(Message var1);
    }

    public interface ISendMediaMessageCallback extends IRongCallback.ISendMessageCallback {
        void onProgress(Message var1, int var2);

        void onCanceled(Message var1);
    }

    public interface ISendMessageCallback {
        void onAttached(Message var1);

        void onSuccess(Message var1);

        void onError(Message var1, ErrorCode var2);
    }
}
