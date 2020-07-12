//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import io.rong.common.RLog;
import io.rong.common.rlog.IRealTimeLogListener;
import io.rong.imlib.NativeClient.PushNotificationListener;
import io.rong.imlib.model.ConversationStatus;
import io.rong.imlib.model.RCEncryptedSession;
import io.rong.imlib.model.RTCUser;
import io.rong.imlib.relinker.ReLinker;
import io.rong.imlib.relinker.ReLinker.Logger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class NativeObject {
    private static final String TAG = NativeObject.class.getSimpleName();

    NativeObject(Context context) {
        ReLinker.log(new Logger() {
            public void log(String message) {
                RLog.d(NativeObject.TAG, "load RongIMLib:" + message);
            }
        }).recursively().loadLibrary(context, "RongIMLib");
    }

    protected native int InitClient(String var1, String var2, String var3, String var4, String var5);

    protected native int RegisterMessageType(String var1, int var2);

    protected native int RegisterMessage(NativeObject.Conversation[] var1);

    protected native int Connect(String var1, NativeObject.ConnectionEntry[] var2, String var3, NativeObject.UserProfile var4);

    protected native void SetConnectionStatusListener(NativeObject.ConnectionStatusListener var1);

    protected native void Disconnect(int var1);

    protected native boolean DeleteMessages(long[] var1);

    protected native void DeleteRemoteMessages(int var1, String var2, NativeObject.Message[] var3, boolean var4, NativeObject.PublishAckListener var5);

    protected native boolean ClearMessages(int var1, String var2, boolean var3);

    protected native void CleanRemoteHistoryMessage(int var1, String var2, long var3, NativeObject.PublishAckListener var5);

    protected native boolean CleanHistoryMessages(int var1, String var2, long var3);

    protected native boolean ClearUnread(int var1, String var2);

    protected native boolean SetMessageExtra(long var1, String var3);

    protected native boolean RemoveConversation(int var1, String var2);

    protected native boolean SetTextMessageDraft(int var1, String var2, String var3);

    protected native boolean SetMessageContent(long var1, byte[] var3, String var4);

    protected native String GetTextMessageDraft(int var1, String var2);

    protected native boolean SetIsTop(int var1, String var2, boolean var3, boolean var4);

    protected native int GetTotalUnreadCount();

    protected native long GetDeltaTime();

    protected native void CreateInviteDiscussion(String var1, String[] var2, NativeObject.CreateDiscussionCallback var3);

    protected native void InviteMemberToDiscussion(String var1, String[] var2, NativeObject.PublishAckListener var3);

    protected native void RemoveMemberFromDiscussion(String var1, String var2, NativeObject.PublishAckListener var3);

    protected native boolean RemoveMemberFromDiscussionSync(String var1, String var2);

    protected native void QuitDiscussion(String var1, NativeObject.PublishAckListener var2);

    protected native long SaveMessage(String var1, int var2, String var3, String var4, byte[] var5, boolean var6, int var7, int var8, long var9, String var11, int var12, String var13);

    protected native void SendMessage(String var1, int var2, int var3, String var4, byte[] var5, byte[] var6, byte[] var7, long var8, String[] var10, NativeObject.PublishAckListener var11, boolean var12);

    protected native void sendMessageWithOption(String var1, int var2, int var3, String var4, byte[] var5, byte[] var6, byte[] var7, long var8, String[] var10, NativeObject.PublishAckListener var11, boolean var12, boolean var13, boolean var14);

    protected native NativeObject.UserInfo GetUserInfoExSync(String var1, int var2);

    protected native void SetMessageListener(NativeObject.ReceiveMessageListener var1);

    protected native boolean SetReadStatus(long var1, int var3);

    protected native boolean SetSendStatus(long var1, int var3);

    protected native int EnvironmentChangeNotify(int var1);

    protected native void GetDiscussionInfo(String var1, NativeObject.DiscussionInfoListener var2);

    protected native NativeObject.DiscussionInfo GetDiscussionInfoSync(String var1);

    protected native void RenameDiscussion(String var1, String var2, NativeObject.PublishAckListener var3);

    protected native NativeObject.Conversation GetConversationEx(String var1, int var2);

    protected native void SetBlockPush(String var1, int var2, boolean var3, NativeObject.PublishAckListener var4);

    protected native int GetBlockPush(String var1, int var2);

    protected native void SetInviteStatus(String var1, int var2, NativeObject.PublishAckListener var3);

    protected native int GetDNDUnreadCount(NativeObject.Conversation[] var1);

    protected native int GetUnreadCount(String var1, int var2);

    protected native int GetMessageCount(String var1, int var2);

    protected native void RecallMessage(String var1, byte[] var2, String var3, long var4, String var6, int var7, NativeObject.PublishAckListener var8);

    protected native NativeObject.Conversation[] GetConversationListEx(int[] var1);

    protected native NativeObject.Conversation[] GetConversationList(int[] var1, long var2, int var4);

    protected native void SyncGroups(String[] var1, String[] var2, NativeObject.PublishAckListener var3);

    protected native void JoinGroup(String var1, String var2, NativeObject.PublishAckListener var3);

    protected native void QuitGroup(String var1, NativeObject.PublishAckListener var2);

    protected native int GetCateUnreadCount(int[] var1, boolean var2);

    protected native void JoinChatRoom(String var1, int var2, int var3, boolean var4, NativeObject.PublishAckListener var5);

    protected native void JoinExistingChatroom(String var1, int var2, int var3, NativeObject.PublishAckListener var4, boolean var5);

    protected native void QuitChatRoom(String var1, int var2, NativeObject.PublishAckListener var3);

    protected native boolean ClearConversations(int[] var1);

    protected native void AddToBlacklist(String var1, NativeObject.PublishAckListener var2);

    protected native void RemoveFromBlacklist(String var1, NativeObject.PublishAckListener var2);

    protected native void GetBlacklistStatus(String var1, NativeObject.BizAckListener var2);

    protected native void GetBlacklist(NativeObject.SetBlacklistListener var1);

    protected native void GetUploadToken(int var1, String var2, NativeObject.TokenListener var3);

    protected native void GetDownloadUrl(int var1, String var2, String var3, NativeObject.TokenListener var4);

    protected native void SubscribeAccount(String var1, int var2, boolean var3, NativeObject.PublishAckListener var4);

    protected native int SetDeviceInfo(String var1, String var2, String var3, String var4, String var5);

    protected native void SearchAccount(String var1, int var2, int var3, NativeObject.AccountInfoListener var4);

    protected native void LoadHistoryMessage(String var1, int var2, long var3, int var5, NativeObject.HistoryMessageListener var6);

    protected native void LoadHistoryMessageOption(String var1, int var2, long var3, int var5, int var6, boolean var7, NativeObject.HistoryMessageListener var8);

    protected native void GetChatroomHistoryMessage(String var1, long var2, int var4, int var5, NativeObject.HistoryMessageListener var6);

    protected void ping() {
        this.EnvironmentChangeNotify(105);
    }

    protected void networkUnavailable() {
        this.EnvironmentChangeNotify(101);
    }

    protected native NativeObject.AccountInfo[] LoadAccountInfo();

    protected native void AddPushSetting(String var1, int var2, NativeObject.PublishAckListener var3);

    protected native void RemovePushSetting(NativeObject.PublishAckListener var1);

    protected native void QueryPushSetting(NativeObject.PushSettingListener var1);

    protected native void SetUserData(String var1, NativeObject.PublishAckListener var2);

    protected native NativeObject.Message[] GetHistoryMessagesEx(String var1, int var2, String var3, int var4, int var5, boolean var6);

    protected native NativeObject.Message[] GetHistoryMessagesByObjectNames(String var1, int var2, String[] var3, long var4, int var6, boolean var7);

    protected native long GetSendTimeByMessageId(long var1);

    protected native NativeObject.Message GetMessageById(long var1);

    protected native NativeObject.Message GetMessageByUId(String var1);

    protected native boolean UpdateMessageReceiptStatus(String var1, int var2, long var3);

    protected native boolean ClearUnreadByReceipt(String var1, int var2, long var3);

    protected native boolean UpdateConversationInfo(String var1, int var2, String var3, String var4);

    protected native boolean QueryChatroomInfo(String var1, int var2, int var3, NativeObject.ChatroomInfoListener var4);

    protected native void GetVoIPKey(int var1, String var2, String var3, NativeObject.TokenListener var4);

    protected native void SetGetSearchableWordListener(NativeObject.GetSearchableWordListener var1);

    protected native void SetUserStatus(int var1, NativeObject.PublishAckListener var2);

    protected native void GetUserStatus(String var1, NativeObject.CreateDiscussionCallback var2);

    protected native void SetPushSetting(int var1, String var2, NativeObject.SetPushSettingListener var3);

    protected native void SubscribeStatus(String[] var1, NativeObject.PublishAckListener var2);

    protected native void SetSubscribeStatusListener(NativeObject.UserStatusListener var1);

    protected native String GetPushSetting(int var1);

    protected native NativeObject.Message[] SearchMessages(String var1, int var2, String var3, int var4, long var5);

    protected native NativeObject.Message[] SearchMessagesByUser(String var1, int var2, String var3, int var4, long var5);

    protected native NativeObject.Conversation[] SearchConversations(String var1, int[] var2, String[] var3);

    protected native NativeObject.Message[] GetMatchedMessages(String var1, int var2, long var3, int var5, int var6);

    protected native void GetVendorToken(String var1, NativeObject.TokenListener var2);

    protected native NativeObject.Message[] GetMentionMessages(String var1, int var2);

    protected native void SetLogStatus(int var1, NativeObject.NativeLogInfoListener var2);

    protected native boolean UpdateReadReceiptRequestInfo(String var1, String var2);

    protected native int RegisterCmdMsgType(String[] var1);

    protected native int RegisterDeleteMessageType(String[] var1);

    protected native int SetEnvironment(boolean var1);

    protected native void SetOfflineMessageDuration(String var1, NativeObject.SetOfflineMessageDurationListener var2);

    protected native String GetOfflineMessageDuration();

    protected native NativeObject.Conversation[] GetBlockedConversations(int[] var1);

    protected native NativeObject.Message GetTheFirstUnreadMessage(int var1, String var2);

    protected native boolean SetReadTime(long var1, long var3);

    protected native boolean CreateEncryptedConversation(String var1, String var2, String var3, String var4, String var5, int var6);

    protected native RCEncryptedSession GetEncryptedConversationInfo(String var1);

    protected native boolean SetEncryptedConversationInfo(String var1, String var2, String var3, String var4, String var5, int var6);

    protected native boolean RemoveEncryptedConversation(String var1);

    protected native boolean ClearEncryptedConversations();

    protected native List<RCEncryptedSession> GetEncryptedConversations();

    protected native void SetPushNotificationListener(PushNotificationListener var1);

    protected native void SetRealTimeLogListener(IRealTimeLogListener var1);

    protected native void ExitRTCRoom(String var1, NativeObject.PublishAckListener var2);

    protected native void GetRTCUsers(String var1, int var2, NativeObject.RTCUserInfoListener var3);

    protected native void GetRTCUserData(String var1, int var2, NativeObject.RTCUserInfoListener var3);

    protected native void SendRTCPing(String var1, NativeObject.PublishAckListener var2);

    protected native boolean UseRTCOnly();

    protected native void RTCPutInnerDatum(String var1, int var2, String var3, String var4, String var5, String var6, NativeObject.PublishAckListener var7);

    protected native void RTCPutOuterDatum(String var1, int var2, String var3, String var4, String var5, String var6, NativeObject.PublishAckListener var7);

    protected native void RTCDeleteInnerData(String var1, int var2, String[] var3, String var4, String var5, NativeObject.PublishAckListener var6);

    protected native void RTCDeleteOuterData(String var1, int var2, String[] var3, String var4, String var5, NativeObject.PublishAckListener var6);

    protected native void RTCGetInnerData(String var1, int var2, String[] var3, NativeObject.RTCDataListener var4);

    protected native void RTCGetOuterData(String var1, int var2, String[] var3, NativeObject.RTCDataListener var4);

    protected native void JoinRTCRoomAndGetData(String var1, int var2, int var3, NativeObject.RTCUserInfoListener var4);

    protected native void GetRTCConfig(String var1, String var2, long var3, String var5, NativeObject.RTCConfigListener var6);

    protected native void RTCGetToken(String var1, int var2, int var3, NativeObject.TokenListener var4);

    protected native void RTCSetUserState(String var1, String var2, NativeObject.PublishAckListener var3);

    protected native String GetHttpDnsSecret();

    protected native String GetHttpDnsAccountId();

    protected native void RTCSetUserData(String var1, int var2, Map<String, String> var3, String var4, String var5, NativeObject.PublishAckListener var6);

    protected native void RTCGetUserData(String var1, String[] var2, NativeObject.RTCUserInfoListener var3);

    protected native void SetChatRoomStatus(String var1, NativeObject.StatusData var2, NativeObject.StatusNotification var3, NativeObject.PublishAckListener var4);

    protected native void DeleteChatRoomStatus(String var1, NativeObject.StatusData var2, NativeObject.StatusNotification var3, NativeObject.PublishAckListener var4);

    protected native String GetChatRoomStatusByKey(String var1, String var2);

    protected native HashMap<String, String> GetChatRoomStatus(String var1);

    protected native void SetChatRoomStatusNotificationListener(NativeObject.StatusNotificationListener var1);

    protected native void RTCSetUserResource(String var1, NativeObject.StatusData[] var2, String var3, NativeObject.StatusData[] var4, NativeObject.PublishAckListener var5);

    protected native void SetConversationStatusListener(NativeObject.ConversationStatusListener var1);

    protected native String GetRTCProfile();

    interface SetOfflineMessageDurationListener {
        void onSuccess(long var1);

        void onError(int var1);
    }

    public static class StatusNotification {
        boolean notifyAll;
        int conversationType;
        int attributeFlag;
        String objectName;
        String messageContent;

        public StatusNotification() {
        }

        public boolean isNotifyAll() {
            return this.notifyAll;
        }

        public void setNotifyAll(boolean notifyAll) {
            this.notifyAll = notifyAll;
        }

        public int getConversationType() {
            return this.conversationType;
        }

        public void setConversationType(int conversationType) {
            this.conversationType = conversationType;
        }

        public int getAttributeFlag() {
            return this.attributeFlag;
        }

        public void setAttributeFlag(int attributeFlag) {
            this.attributeFlag = attributeFlag;
        }

        public String getObjectName() {
            return this.objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getMessageContent() {
            return this.messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }
    }

    public static class StatusData {
        String key;
        String value;
        boolean autoDelete;
        boolean overwrite;

        public StatusData() {
        }

        public String getKey() {
            return this.key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isAutoDelete() {
            return this.autoDelete;
        }

        public void setAutoDelete(boolean autoDelete) {
            this.autoDelete = autoDelete;
        }

        public boolean isOverwrite() {
            return this.overwrite;
        }

        public void setOverwrite(boolean overwrite) {
            this.overwrite = overwrite;
        }
    }

    public static class UserProfile {
        boolean ipv6Preferred;
        boolean publicService;
        boolean pushSetting;
        boolean sdkReconnect;
        boolean kvStorageOpened;
        int groupMessageLimit;
        String clientIp;

        public UserProfile() {
        }

        public boolean isKvStorageOpened() {
            return this.kvStorageOpened;
        }

        public void setKvStorageOpened(boolean kvStorageOpened) {
            this.kvStorageOpened = kvStorageOpened;
        }

        public boolean isIpv6Preferred() {
            return this.ipv6Preferred;
        }

        public void setIpv6Preferred(boolean ipv6Preferred) {
            this.ipv6Preferred = ipv6Preferred;
        }

        public boolean isPublicService() {
            return this.publicService;
        }

        public void setPublicService(boolean publicService) {
            this.publicService = publicService;
        }

        public boolean isPushSetting() {
            return this.pushSetting;
        }

        public void setPushSetting(boolean pushSetting) {
            this.pushSetting = pushSetting;
        }

        public boolean isSdkReconnect() {
            return this.sdkReconnect;
        }

        public void setSdkReconnect(boolean sdkReconnect) {
            this.sdkReconnect = sdkReconnect;
        }

        public int getGroupMessageLimit() {
            return this.groupMessageLimit;
        }

        public void setGroupMessageLimit(int groupMessageLimit) {
            this.groupMessageLimit = groupMessageLimit;
        }

        public String getClientIp() {
            return this.clientIp;
        }

        public void setClientIp(String clientIp) {
            this.clientIp = clientIp;
        }
    }

    public static class ConnectionEntry {
        private String host;
        private int port;
        private int netType;
        private int duration;
        private int error = -1;

        public ConnectionEntry() {
        }

        public String getHost() {
            return this.host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return this.port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getNetType() {
            return this.netType;
        }

        public void setNetType(int netType) {
            this.netType = netType;
        }

        public int getDuration() {
            return this.duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getError() {
            return this.error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                NativeObject.ConnectionEntry that = (NativeObject.ConnectionEntry)o;
                if (this.port != that.port) {
                    return false;
                } else {
                    return this.host != null ? this.host.equals(that.host) : that.host == null;
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.host != null ? this.host.hashCode() : 0;
            result = 31 * result + this.port;
            return result;
        }
    }

    public static class ReceiptInfo {
        private byte[] targetId;
        private long timestamp;

        public ReceiptInfo() {
        }

        public byte[] getTargetId() {
            return this.targetId;
        }

        public void setTargetId(byte[] targetId) {
            this.targetId = targetId;
        }

        public long getTimestamp() {
            return this.timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class AccountInfo {
        private byte[] accountId;
        private byte[] accountName;
        private byte[] accountUri;
        private byte[] extra;
        private int accountType;

        public AccountInfo() {
        }

        public byte[] getAccountId() {
            return this.accountId;
        }

        public void setAccountId(byte[] accountId) {
            this.accountId = accountId;
        }

        public byte[] getAccountName() {
            return this.accountName;
        }

        public void setAccountName(byte[] accountName) {
            this.accountName = accountName;
        }

        public byte[] getAccountUri() {
            return this.accountUri;
        }

        public void setAccountUri(byte[] accountUri) {
            this.accountUri = accountUri;
        }

        public byte[] getExtra() {
            return this.extra;
        }

        public void setExtra(byte[] extra) {
            this.extra = extra;
        }

        public int getAccountType() {
            return this.accountType;
        }

        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }
    }

    public static class DiscussionInfo {
        private String discussionId;
        private String discussionName;
        private String adminId;
        private String userIds;
        private int inviteStatus;

        public DiscussionInfo() {
        }

        public String getDiscussionId() {
            return this.discussionId;
        }

        public void setDiscussionId(String discussionId) {
            this.discussionId = discussionId;
        }

        public String getDiscussionName() {
            return this.discussionName;
        }

        public void setDiscussionName(byte[] data) {
            this.discussionName = new String(data);
        }

        public String getAdminId() {
            return this.adminId;
        }

        public void setAdminId(String adminId) {
            this.adminId = adminId;
        }

        public String getUserIds() {
            return this.userIds;
        }

        public void setUserIds(String userIds) {
            this.userIds = userIds;
        }

        public int getInviteStatus() {
            return this.inviteStatus;
        }

        public void setInviteStatus(int inviteStatus) {
            this.inviteStatus = inviteStatus;
        }
    }

    public static class Conversation {
        private int conversationType;
        private String targetId;
        private String conversationTitle;
        private boolean isTop;
        private String draft;
        private int unreadMessageCount;
        private String objectName;
        private long messageId;
        private int readStatus;
        private int receiveStatus;
        private int sentStatus;
        private long ReceivedTime;
        private long sentTime;
        private String senderUserId;
        private String senderName;
        private boolean messageDirection;
        private String messageContent;
        private boolean blockPush;
        private long lastTime;
        private String userId;
        private String userName;
        private String userPortrait;
        private byte[] content;
        private String extra;
        private String portraitUrl;
        private String UId;
        private int mentionCount;
        private int matchCount;

        public Conversation(String jsonObj) {
        }

        public Conversation() {
        }

        public int getMatchCount() {
            return this.matchCount;
        }

        public void setMatchCount(int matchCount) {
            this.matchCount = matchCount;
        }

        public String getUId() {
            return this.UId;
        }

        public void setUId(String UId) {
            this.UId = UId;
        }

        public long getSentTime() {
            return this.sentTime;
        }

        public void setSentTime(long sentTime) {
            this.sentTime = sentTime;
        }

        public String getSenderUserId() {
            return this.senderUserId;
        }

        public void setSenderUserId(String senderUserId) {
            this.senderUserId = senderUserId;
        }

        public boolean isMessageDirection() {
            return this.messageDirection;
        }

        public void setMessageDirection(boolean messageDirection) {
            this.messageDirection = messageDirection;
        }

        public void setIsTop(boolean isTop) {
            this.isTop = isTop;
        }

        public int getConversationType() {
            return this.conversationType;
        }

        public void setConversationType(int conversationType) {
            this.conversationType = conversationType;
        }

        public String getTargetId() {
            return this.targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getConversationTitle() {
            return this.conversationTitle;
        }

        public void setConversationTitle(byte[] conversationTitle) {
            this.conversationTitle = new String(conversationTitle);
        }

        public boolean isTop() {
            return this.isTop;
        }

        public void setTop(boolean isTop) {
            this.isTop = isTop;
        }

        public String getDraft() {
            return this.draft;
        }

        public void setDraft(String draft) {
            this.draft = draft;
        }

        public int getUnreadMessageCount() {
            return this.unreadMessageCount;
        }

        public void setUnreadMessageCount(int unreadMessageCount) {
            this.unreadMessageCount = unreadMessageCount;
        }

        public String getObjectName() {
            return this.objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public long getMessageId() {
            return this.messageId;
        }

        public void setMessageId(long messageId) {
            this.messageId = messageId;
        }

        public int getReceiveStatus() {
            return this.receiveStatus;
        }

        public void setReceiveStatus(int receiveStatus) {
            this.receiveStatus = receiveStatus;
        }

        public int getSentStatus() {
            return this.sentStatus;
        }

        public void setSentStatus(int sentStatus) {
            this.sentStatus = sentStatus;
        }

        public long getReceivedTime() {
            return this.ReceivedTime;
        }

        public void setReceivedTime(long receivedTime) {
            this.ReceivedTime = receivedTime;
        }

        public String getSenderName() {
            return this.senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getMessageContent() {
            return this.messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }

        public boolean isBlockPush() {
            return this.blockPush;
        }

        public void setBlockPush(boolean blockPush) {
            this.blockPush = blockPush;
        }

        public long getLastTime() {
            return this.lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPortrait() {
            return this.userPortrait;
        }

        public void setUserPortrait(String userPortrait) {
            this.userPortrait = userPortrait;
        }

        public int getReadStatus() {
            return this.readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public byte[] getContent() {
            return this.content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public String getExtra() {
            return this.extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getPortraitUrl() {
            return this.portraitUrl;
        }

        public void setPortraitUrl(String portraitUrl) {
            this.portraitUrl = portraitUrl;
        }

        public int getMentionCount() {
            return this.mentionCount;
        }

        public void setMentionCount(int mentionCount) {
            this.mentionCount = mentionCount;
        }
    }

    public static class UserInfo {
        private String userId;
        private int categoryId;
        private String userName;
        private String url;
        private String accountExtra;
        private long joinTime;

        public UserInfo() {
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getCategoryId() {
            return this.categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAccountExtra() {
            return this.accountExtra;
        }

        public void setAccountExtra(String accountExtra) {
            this.accountExtra = accountExtra;
        }

        public long getJoinTime() {
            return this.joinTime;
        }

        public void setJoinTime(long joinTime) {
            this.joinTime = joinTime;
        }
    }

    public static class Message {
        private int conversationType;
        private String targetId;
        private long messageId;
        private boolean messageDirection;
        private String senderUserId;
        private int readStatus;
        private int sentStatus;
        private long receivedTime;
        private long sentTime;
        private long readTime;
        private String objectName;
        private byte[] content;
        private String extra;
        private String pushContent;
        private String UId;
        private String readReceiptInfo;
        private boolean isOffLine;

        public Message(JSONObject jsonObj) {
            this.conversationType = jsonObj.optInt("conversation_category");
            this.targetId = jsonObj.optString("target_id");
            this.messageId = jsonObj.optLong("id");
            this.messageDirection = jsonObj.optBoolean("message_direction");
            this.senderUserId = jsonObj.optString("sender_user_id");
            this.readStatus = jsonObj.optInt("read_status");
            this.sentStatus = jsonObj.optInt("send_status");
            this.receivedTime = jsonObj.optLong("receive_time");
            this.sentTime = jsonObj.optLong("send_time");
            this.objectName = jsonObj.optString("object_name");
            this.content = jsonObj.optString("content").getBytes();
            this.extra = jsonObj.optString("extra");
            this.pushContent = jsonObj.optString("push");
        }

        public Message() {
        }

        public String getUId() {
            return this.UId;
        }

        public void setUId(String UId) {
            this.UId = UId;
        }

        public String getPushContent() {
            return this.pushContent;
        }

        public void setPushContent(String pushContent) {
            this.pushContent = pushContent;
        }

        public int getConversationType() {
            return this.conversationType;
        }

        public void setConversationType(int conversationType) {
            this.conversationType = conversationType;
        }

        public String getTargetId() {
            return this.targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public long getMessageId() {
            return this.messageId;
        }

        public void setMessageId(long messageId) {
            this.messageId = messageId;
        }

        public boolean getMessageDirection() {
            return this.messageDirection;
        }

        public void setMessageDirection(boolean messageDirection) {
            this.messageDirection = messageDirection;
        }

        public int getReadStatus() {
            return this.readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public int getSentStatus() {
            return this.sentStatus;
        }

        public void setSentStatus(int sentStatus) {
            this.sentStatus = sentStatus;
        }

        public long getReceivedTime() {
            return this.receivedTime;
        }

        public void setReceivedTime(long receivedTime) {
            this.receivedTime = receivedTime;
        }

        public long getSentTime() {
            return this.sentTime;
        }

        public void setSentTime(long sentTime) {
            this.sentTime = sentTime;
        }

        public long getReadTime() {
            return this.readTime;
        }

        public void setReadTime(long readTime) {
            this.readTime = readTime;
        }

        public String getObjectName() {
            return this.objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public byte[] getContent() {
            return this.content;
        }

        public void setContent(byte[] content) {
            this.content = content;
        }

        public String getExtra() {
            return this.extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public String getSenderUserId() {
            return this.senderUserId;
        }

        public void setSenderUserId(String senderUserId) {
            this.senderUserId = senderUserId;
        }

        public String getReadReceiptInfo() {
            return this.readReceiptInfo;
        }

        public void setReadReceiptInfo(String readReceiptInfo) {
            this.readReceiptInfo = readReceiptInfo;
        }

        public boolean isOffLine() {
            return this.isOffLine;
        }

        public void setOffLine(boolean offLine) {
            this.isOffLine = offLine;
        }
    }

    public interface ConversationStatusListener {
        void OnStatusChanged(ConversationStatus[] var1);
    }

    public interface StatusNotificationListener {
        void OnStatusChanged(String var1);
    }

    public interface UserStatusListener {
        void onStatusReceived(String var1, String var2);
    }

    public interface ChatroomInfoListener {
        void OnSuccess(int var1, NativeObject.UserInfo[] var2);

        void OnError(int var1);
    }

    public interface HistoryMessageListener {
        void onReceived(NativeObject.Message[] var1, long var2);

        void onError(int var1);
    }

    public interface GetUserDataListener {
        void OnSuccess(String var1);

        void OnError(int var1);
    }

    public interface AccountInfoListener {
        void onReceived(NativeObject.AccountInfo[] var1);

        void OnError(int var1);
    }

    public interface TokenListener {
        void OnError(int var1, String var2, String var3, String var4, String var5);
    }

    public interface SetBlacklistListener {
        void OnSuccess(String var1);

        void OnError(int var1);
    }

    public interface BizAckListener {
        void operationComplete(int var1, int var2);
    }

    public interface DiscussionInfoListener {
        void onReceived(NativeObject.DiscussionInfo var1);

        void OnError(int var1);
    }

    public interface SetPushSettingListener {
        void onSuccess(long var1);

        void onError(int var1);
    }

    public interface PushSettingListener {
        void OnSuccess(String var1, int var2);

        void OnError(int var1);
    }

    public abstract static class ReceiveMessageListener {
        public ReceiveMessageListener() {
        }

        public abstract void onReceived(NativeObject.Message var1, int var2, boolean var3, boolean var4, int var5);

        public abstract void onReceived(NativeObject.Message[] var1, boolean var2, int var3);
    }

    public interface RTCConfigListener {
        void onSuccess(String var1, long var2);

        void onError(int var1);
    }

    public interface RTCDataListener {
        void OnSuccess(Map<String, String> var1);

        void OnError(int var1);
    }

    public interface RTCUserInfoListener {
        void OnSuccess(RTCUser[] var1, String var2, String var3);

        void OnError(int var1);
    }

    public interface NativeLogInfoListener {
        void OnLogInfo(String var1, boolean var2);
    }

    public interface GetSearchableWordListener {
        byte[] getSearchableWord(String var1, byte[] var2);
    }

    public interface CreateDiscussionCallback {
        void OnSuccess(String var1);

        void OnError(int var1);
    }

    public interface PublishAckListener {
        void operationComplete(int var1, String var2, long var3);
    }

    public interface ConnectionStatusListener {
        void OnTcpComplete(NativeObject.ConnectionEntry var1);

        void OnRmtpComplete(int var1, String var2, int var3, short var4, String var5);

        void OnRmtpDisconnected(int var1, int var2, String var3);

        void OnPongReceived();

        void OnDatabaseOpened(int var1);
    }
}
