//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.RCEncryptedSession;
import io.rong.imlib.model.RTCStatusDate;
import io.rong.imlib.model.RemoteHistoryMsgOption;
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.SendMessageOption;
import io.rong.imlib.model.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IHandler extends IInterface {
    void initAppendixModule() throws RemoteException;

    void connect(String var1, boolean var2, boolean var3, IConnectStringCallback var4) throws RemoteException;

    void disconnect(boolean var1) throws RemoteException;

    void registerMessageType(String var1) throws RemoteException;

    void registerMessageTypes(List<String> var1) throws RemoteException;

    int getTotalUnreadCount() throws RemoteException;

    int getUnreadCountByConversation(Conversation[] var1) throws RemoteException;

    int getUnreadCount(int[] var1) throws RemoteException;

    int getUnreadCountWithDND(int[] var1, boolean var2) throws RemoteException;

    int getUnreadCountById(int var1, String var2) throws RemoteException;

    void setOnReceiveMessageListener(OnReceiveMessageListener var1) throws RemoteException;

    void setConnectionStatusListener(IConnectionStatusListener var1) throws RemoteException;

    void setIpcConnectTimeOut() throws RemoteException;

    void initIpcConnectStatus(int var1) throws RemoteException;

    Message getMessage(int var1) throws RemoteException;

    Message insertMessage(Message var1) throws RemoteException;

    Message insertSettingMessage(Message var1) throws RemoteException;

    void sendMessage(Message var1, String var2, String var3, ISendMessageCallback var4) throws RemoteException;

    void sendMessageOption(Message var1, String var2, String var3, SendMessageOption var4, ISendMessageCallback var5) throws RemoteException;

    void sendDirectionalMessage(Message var1, String var2, String var3, String[] var4, ISendMessageCallback var5) throws RemoteException;

    void sendMediaMessage(Message var1, String var2, String var3, ISendMediaMessageCallback var4) throws RemoteException;

    void sendDirectionalMediaMessage(Message var1, String[] var2, String var3, String var4, ISendMediaMessageCallback var5) throws RemoteException;

    void sendLocationMessage(Message var1, String var2, String var3, ISendMessageCallback var4) throws RemoteException;

    List<Message> getNewestMessages(Conversation var1, int var2) throws RemoteException;

    List<Message> getOlderMessages(Conversation var1, long var2, int var4) throws RemoteException;

    void getOlderMessagesOneWay(Conversation var1, long var2, int var4, OnGetHistoryMessagesCallback var5) throws RemoteException;

    void getRemoteHistoryMessages(Conversation var1, long var2, int var4, IResultCallback var5) throws RemoteException;

    void getRemoteHistoryMessagesOption(Conversation var1, RemoteHistoryMsgOption var2, IResultCallback var3) throws RemoteException;

    void cleanRemoteHistoryMessages(Conversation var1, long var2, IOperationCallback var4) throws RemoteException;

    void cleanHistoryMessages(Conversation var1, long var2, IOperationCallback var4) throws RemoteException;

    void getChatroomHistoryMessages(String var1, long var2, int var4, int var5, IChatRoomHistoryMessageCallback var6) throws RemoteException;

    void getUserStatus(String var1, IGetUserStatusCallback var2) throws RemoteException;

    void setUserStatus(int var1, ISetUserStatusCallback var2) throws RemoteException;

    void subscribeStatus(List<String> var1, IIntegerCallback var2) throws RemoteException;

    void setSubscribeStatusListener(ISubscribeUserStatusCallback var1) throws RemoteException;

    void setPushSetting(int var1, String var2, ISetPushSettingCallback var3) throws RemoteException;

    String getPushSetting(int var1) throws RemoteException;

    List<Message> getOlderMessagesByObjectName(Conversation var1, String var2, long var3, int var5, boolean var6) throws RemoteException;

    List<Message> getOlderMessagesByObjectNames(Conversation var1, List<String> var2, long var3, int var5, boolean var6) throws RemoteException;

    boolean deleteMessage(int[] var1) throws RemoteException;

    void deleteMessages(int var1, String var2, Message[] var3, IOperationCallback var4) throws RemoteException;

    boolean deleteConversationMessage(int var1, String var2) throws RemoteException;

    boolean clearMessages(Conversation var1) throws RemoteException;

    boolean clearMessagesUnreadStatus(Conversation var1) throws RemoteException;

    boolean setMessageExtra(int var1, String var2) throws RemoteException;

    boolean setMessageReceivedStatus(int var1, int var2) throws RemoteException;

    boolean setMessageSentStatus(int var1, int var2) throws RemoteException;

    Message getMessageByUid(String var1) throws RemoteException;

    List<Conversation> getConversationList() throws RemoteException;

    void getConversationListByBatch(int var1, IGetConversationListWithProcessCallback var2) throws RemoteException;

    List<Conversation> getConversationListByType(int[] var1) throws RemoteException;

    void getConversationListOfTypesByBatch(int[] var1, int var2, IGetConversationListWithProcessCallback var3) throws RemoteException;

    List<Conversation> getConversationListByPage(int[] var1, long var2, int var4) throws RemoteException;

    List<Conversation> getBlockedConversationList(int[] var1) throws RemoteException;

    Conversation getConversation(int var1, String var2) throws RemoteException;

    boolean removeConversation(int var1, String var2) throws RemoteException;

    boolean saveConversationDraft(Conversation var1, String var2) throws RemoteException;

    String getConversationDraft(Conversation var1) throws RemoteException;

    boolean cleanConversationDraft(Conversation var1) throws RemoteException;

    void getConversationNotificationStatus(int var1, String var2, ILongCallback var3) throws RemoteException;

    void setConversationNotificationStatus(int var1, String var2, int var3, ILongCallback var4) throws RemoteException;

    boolean syncConversationNotificationStatus(int var1, String var2, int var3) throws RemoteException;

    boolean setConversationTopStatus(int var1, String var2, boolean var3, boolean var4) throws RemoteException;

    int getConversationUnreadCount(Conversation var1) throws RemoteException;

    boolean clearConversations(int[] var1) throws RemoteException;

    void setNotificationQuietHours(String var1, int var2, IOperationCallback var3) throws RemoteException;

    void removeNotificationQuietHours(IOperationCallback var1) throws RemoteException;

    void getNotificationQuietHours(IGetNotificationQuietHoursCallback var1) throws RemoteException;

    boolean updateConversationInfo(int var1, String var2, String var3, String var4) throws RemoteException;

    boolean createEncryptedConversation(String var1, RCEncryptedSession var2) throws RemoteException;

    RCEncryptedSession getEncryptedConversation(String var1) throws RemoteException;

    boolean setEncryptedConversation(String var1, RCEncryptedSession var2) throws RemoteException;

    boolean removeEncryptedConversation(String var1) throws RemoteException;

    boolean clearEncryptedConversations() throws RemoteException;

    List<RCEncryptedSession> getAllEncryptedConversations() throws RemoteException;

    void getDiscussion(String var1, IResultCallback var2) throws RemoteException;

    void setDiscussionName(String var1, String var2, IOperationCallback var3) throws RemoteException;

    void createDiscussion(String var1, List<String> var2, IResultCallback var3) throws RemoteException;

    void addMemberToDiscussion(String var1, List<String> var2, IOperationCallback var3) throws RemoteException;

    void removeDiscussionMember(String var1, String var2, IOperationCallback var3) throws RemoteException;

    void quitDiscussion(String var1, IOperationCallback var2) throws RemoteException;

    void syncGroup(List<Group> var1, IOperationCallback var2) throws RemoteException;

    void joinGroup(String var1, String var2, IOperationCallback var3) throws RemoteException;

    void quitGroup(String var1, IOperationCallback var2) throws RemoteException;

    void getChatRoomInfo(String var1, int var2, int var3, IResultCallback var4) throws RemoteException;

    void reJoinChatRoom(String var1, int var2, IOperationCallback var3) throws RemoteException;

    void joinChatRoom(String var1, int var2, IOperationCallback var3) throws RemoteException;

    void joinExistChatRoom(String var1, int var2, IOperationCallback var3, boolean var4) throws RemoteException;

    void quitChatRoom(String var1, IOperationCallback var2) throws RemoteException;

    void searchPublicService(String var1, int var2, int var3, IResultCallback var4) throws RemoteException;

    void subscribePublicService(String var1, int var2, boolean var3, IOperationCallback var4) throws RemoteException;

    void getPublicServiceProfile(String var1, int var2, IResultCallback var3) throws RemoteException;

    void getPublicServiceList(IResultCallback var1) throws RemoteException;

    void uploadMedia(Message var1, IUploadCallback var2) throws RemoteException;

    void downloadMedia(Conversation var1, int var2, String var3, IDownloadMediaCallback var4) throws RemoteException;

    void downloadMediaMessage(Message var1, IDownloadMediaMessageCallback var2) throws RemoteException;

    void downloadMediaFile(String var1, String var2, String var3, String var4, IDownloadMediaFileCallback var5) throws RemoteException;

    void cancelTransferMediaMessage(Message var1, IOperationCallback var2) throws RemoteException;

    void pauseTransferMediaMessage(Message var1, IOperationCallback var2) throws RemoteException;

    void pauseTransferMediaFile(String var1, IOperationCallback var2) throws RemoteException;

    boolean getFileDownloadingStatus(String var1) throws RemoteException;

    boolean supportResumeBrokenTransfer(String var1) throws RemoteException;

    void cancelAllTransferMediaMessage(IOperationCallback var1) throws RemoteException;

    long getDeltaTime() throws RemoteException;

    void setDiscussionInviteStatus(String var1, int var2, IOperationCallback var3) throws RemoteException;

    void recallMessage(String var1, byte[] var2, String var3, int var4, String var5, int var6, IOperationCallback var7) throws RemoteException;

    void addToBlacklist(String var1, IOperationCallback var2) throws RemoteException;

    void removeFromBlacklist(String var1, IOperationCallback var2) throws RemoteException;

    String getTextMessageDraft(Conversation var1) throws RemoteException;

    boolean saveTextMessageDraft(Conversation var1, String var2) throws RemoteException;

    boolean clearTextMessageDraft(Conversation var1) throws RemoteException;

    void getBlacklist(IStringCallback var1) throws RemoteException;

    void getBlacklistStatus(String var1, IIntegerCallback var2) throws RemoteException;

    void setUserData(UserData var1, IOperationCallback var2) throws RemoteException;

    boolean updateMessageReceiptStatus(String var1, int var2, long var3) throws RemoteException;

    boolean clearUnreadByReceipt(int var1, String var2, long var3) throws RemoteException;

    long getSendTimeByMessageId(int var1) throws RemoteException;

    void getVoIPKey(int var1, String var2, String var3, IStringCallback var4) throws RemoteException;

    String getVoIPCallInfo() throws RemoteException;

    String getCurrentUserId() throws RemoteException;

    void setServerInfo(String var1, String var2) throws RemoteException;

    boolean setMessageContent(int var1, byte[] var2, String var3) throws RemoteException;

    List<Message> getUnreadMentionedMessages(int var1, String var2) throws RemoteException;

    boolean updateReadReceiptRequestInfo(String var1, String var2) throws RemoteException;

    void registerCmdMsgType(String var1) throws RemoteException;

    void registerCmdMsgTypes(List<String> var1) throws RemoteException;

    void registerDeleteMessageType(List<String> var1) throws RemoteException;

    List<Message> searchMessages(String var1, int var2, String var3, int var4, long var5) throws RemoteException;

    List<Message> searchMessagesByUser(String var1, int var2, String var3, int var4, long var5) throws RemoteException;

    List<SearchConversationResult> searchConversations(String var1, int[] var2, String[] var3) throws RemoteException;

    List<Message> getMatchedMessages(String var1, int var2, long var3, int var5, int var6) throws RemoteException;

    void getVendorToken(IStringCallback var1) throws RemoteException;

    void writeFwLog(int var1, String var2, String var3, String var4, long var5) throws RemoteException;

    long getNaviCachedTime() throws RemoteException;

    boolean getJoinMultiChatRoomEnable() throws RemoteException;

    String getOfflineMessageDuration() throws RemoteException;

    void setOfflineMessageDuration(String var1, ILongCallback var2) throws RemoteException;

    void switchAppKey(String var1, String var2) throws RemoteException;

    Message getTheFirstUnreadMessage(int var1, String var2) throws RemoteException;

    boolean setMessageReadTime(long var1, long var3) throws RemoteException;

    void replenishPing(boolean var1) throws RemoteException;

    void sendPing() throws RemoteException;

    void setUserPolicy(boolean var1) throws RemoteException;

    void setReconnectKickEnable(boolean var1) throws RemoteException;

    int getVideoLimitTime() throws RemoteException;

    int getGIFLimitSize() throws RemoteException;

    void setUserProfileListener(UserProfileSettingListener var1) throws RemoteException;

    void setConversationStatusListener(ConversationStatusListener var1) throws RemoteException;

    void initHttpDns() throws RemoteException;

    String getRTCProfile() throws RemoteException;

    void updateVoIPCallInfo(String var1) throws RemoteException;

    void exitRTCRoom(String var1, IOperationCallback var2) throws RemoteException;

    void getRTCUsers(String var1, int var2, RTCDataListener var3) throws RemoteException;

    void getRTCUserData(String var1, int var2, RTCDataListener var3) throws RemoteException;

    void sendRTCPing(String var1, IOperationCallback var2) throws RemoteException;

    boolean useRTCOnly() throws RemoteException;

    void rtcPutInnerData(String var1, int var2, String var3, String var4, String var5, String var6, IOperationCallback var7) throws RemoteException;

    void rtcPutOuterData(String var1, int var2, String var3, String var4, String var5, String var6, IOperationCallback var7) throws RemoteException;

    void rtcDeleteInnerData(String var1, int var2, String[] var3, String var4, String var5, IOperationCallback var6) throws RemoteException;

    void rtcDeleteOuterData(String var1, int var2, String[] var3, String var4, String var5, IOperationCallback var6) throws RemoteException;

    void rtcGetInnerData(String var1, int var2, String[] var3, IRtcIODataListener var4) throws RemoteException;

    void rtcGetOuterData(String var1, int var2, String[] var3, IRtcIODataListener var4) throws RemoteException;

    void joinRTCRoomAndGetData(String var1, int var2, int var3, IRTCJoinRoomCallback var4) throws RemoteException;

    void getRTCConfig(String var1, String var2, long var3, String var5, IRTCConfigCallback var6) throws RemoteException;

    void getRTCToken(String var1, int var2, int var3, IStringCallback var4) throws RemoteException;

    void setRLogOtherProgressCallback(IRLogOtherProgressCallback var1) throws RemoteException;

    void setSavePath(String var1) throws RemoteException;

    void setRTCUserData(String var1, String var2, IOperationCallback var3) throws RemoteException;

    void solveServerHosts(String var1, ISolveServerHostsCallBack var2) throws RemoteException;

    void setRTCUserDatas(String var1, int var2, Map var3, String var4, String var5, IOperationCallback var6) throws RemoteException;

    void getRTCUserDatas(String var1, String[] var2, RTCDataListener var3) throws RemoteException;

    boolean isPhrasesEnabled() throws RemoteException;

    boolean isDnsEnabled() throws RemoteException;

    void sendRTCDirectionalMessage(Message var1, String var2, String var3, String[] var4, SendMessageOption var5, boolean var6, ISendMessageCallback var7) throws RemoteException;

    void setChatRoomEntry(String var1, String var2, String var3, boolean var4, String var5, boolean var6, boolean var7, IOperationCallback var8) throws RemoteException;

    void getChatRoomEntry(String var1, String var2, IStringCallback var3) throws RemoteException;

    void getAllChatRoomEntries(String var1, IDataByBatchListener var2) throws RemoteException;

    void deleteChatRoomEntry(String var1, String var2, String var3, boolean var4, String var5, boolean var6, boolean var7, IOperationCallback var8) throws RemoteException;

    void setNaviContentUpdateListener(INaviContentUpdateCallBack var1) throws RemoteException;

    String getUploadLogConfigInfo() throws RemoteException;

    String getOffLineLogServer() throws RemoteException;

    void notifyAppBackgroundChanged(boolean var1) throws RemoteException;

    void rtcSetUserResource(String var1, RTCStatusDate[] var2, String var3, RTCStatusDate[] var4, IOperationCallback var5) throws RemoteException;

    public abstract static class Stub extends Binder implements IHandler {
        private static final String DESCRIPTOR = "io.rong.imlib.IHandler";
        static final int TRANSACTION_initAppendixModule = 1;
        static final int TRANSACTION_connect = 2;
        static final int TRANSACTION_disconnect = 3;
        static final int TRANSACTION_registerMessageType = 4;
        static final int TRANSACTION_registerMessageTypes = 5;
        static final int TRANSACTION_getTotalUnreadCount = 6;
        static final int TRANSACTION_getUnreadCountByConversation = 7;
        static final int TRANSACTION_getUnreadCount = 8;
        static final int TRANSACTION_getUnreadCountWithDND = 9;
        static final int TRANSACTION_getUnreadCountById = 10;
        static final int TRANSACTION_setOnReceiveMessageListener = 11;
        static final int TRANSACTION_setConnectionStatusListener = 12;
        static final int TRANSACTION_setIpcConnectTimeOut = 13;
        static final int TRANSACTION_initIpcConnectStatus = 14;
        static final int TRANSACTION_getMessage = 15;
        static final int TRANSACTION_insertMessage = 16;
        static final int TRANSACTION_insertSettingMessage = 17;
        static final int TRANSACTION_sendMessage = 18;
        static final int TRANSACTION_sendMessageOption = 19;
        static final int TRANSACTION_sendDirectionalMessage = 20;
        static final int TRANSACTION_sendMediaMessage = 21;
        static final int TRANSACTION_sendDirectionalMediaMessage = 22;
        static final int TRANSACTION_sendLocationMessage = 23;
        static final int TRANSACTION_getNewestMessages = 24;
        static final int TRANSACTION_getOlderMessages = 25;
        static final int TRANSACTION_getOlderMessagesOneWay = 26;
        static final int TRANSACTION_getRemoteHistoryMessages = 27;
        static final int TRANSACTION_getRemoteHistoryMessagesOption = 28;
        static final int TRANSACTION_cleanRemoteHistoryMessages = 29;
        static final int TRANSACTION_cleanHistoryMessages = 30;
        static final int TRANSACTION_getChatroomHistoryMessages = 31;
        static final int TRANSACTION_getUserStatus = 32;
        static final int TRANSACTION_setUserStatus = 33;
        static final int TRANSACTION_subscribeStatus = 34;
        static final int TRANSACTION_setSubscribeStatusListener = 35;
        static final int TRANSACTION_setPushSetting = 36;
        static final int TRANSACTION_getPushSetting = 37;
        static final int TRANSACTION_getOlderMessagesByObjectName = 38;
        static final int TRANSACTION_getOlderMessagesByObjectNames = 39;
        static final int TRANSACTION_deleteMessage = 40;
        static final int TRANSACTION_deleteMessages = 41;
        static final int TRANSACTION_deleteConversationMessage = 42;
        static final int TRANSACTION_clearMessages = 43;
        static final int TRANSACTION_clearMessagesUnreadStatus = 44;
        static final int TRANSACTION_setMessageExtra = 45;
        static final int TRANSACTION_setMessageReceivedStatus = 46;
        static final int TRANSACTION_setMessageSentStatus = 47;
        static final int TRANSACTION_getMessageByUid = 48;
        static final int TRANSACTION_getConversationList = 49;
        static final int TRANSACTION_getConversationListByBatch = 50;
        static final int TRANSACTION_getConversationListByType = 51;
        static final int TRANSACTION_getConversationListOfTypesByBatch = 52;
        static final int TRANSACTION_getConversationListByPage = 53;
        static final int TRANSACTION_getBlockedConversationList = 54;
        static final int TRANSACTION_getConversation = 55;
        static final int TRANSACTION_removeConversation = 56;
        static final int TRANSACTION_saveConversationDraft = 57;
        static final int TRANSACTION_getConversationDraft = 58;
        static final int TRANSACTION_cleanConversationDraft = 59;
        static final int TRANSACTION_getConversationNotificationStatus = 60;
        static final int TRANSACTION_setConversationNotificationStatus = 61;
        static final int TRANSACTION_syncConversationNotificationStatus = 62;
        static final int TRANSACTION_setConversationTopStatus = 63;
        static final int TRANSACTION_getConversationUnreadCount = 64;
        static final int TRANSACTION_clearConversations = 65;
        static final int TRANSACTION_setNotificationQuietHours = 66;
        static final int TRANSACTION_removeNotificationQuietHours = 67;
        static final int TRANSACTION_getNotificationQuietHours = 68;
        static final int TRANSACTION_updateConversationInfo = 69;
        static final int TRANSACTION_createEncryptedConversation = 70;
        static final int TRANSACTION_getEncryptedConversation = 71;
        static final int TRANSACTION_setEncryptedConversation = 72;
        static final int TRANSACTION_removeEncryptedConversation = 73;
        static final int TRANSACTION_clearEncryptedConversations = 74;
        static final int TRANSACTION_getAllEncryptedConversations = 75;
        static final int TRANSACTION_getDiscussion = 76;
        static final int TRANSACTION_setDiscussionName = 77;
        static final int TRANSACTION_createDiscussion = 78;
        static final int TRANSACTION_addMemberToDiscussion = 79;
        static final int TRANSACTION_removeDiscussionMember = 80;
        static final int TRANSACTION_quitDiscussion = 81;
        static final int TRANSACTION_syncGroup = 82;
        static final int TRANSACTION_joinGroup = 83;
        static final int TRANSACTION_quitGroup = 84;
        static final int TRANSACTION_getChatRoomInfo = 85;
        static final int TRANSACTION_reJoinChatRoom = 86;
        static final int TRANSACTION_joinChatRoom = 87;
        static final int TRANSACTION_joinExistChatRoom = 88;
        static final int TRANSACTION_quitChatRoom = 89;
        static final int TRANSACTION_searchPublicService = 90;
        static final int TRANSACTION_subscribePublicService = 91;
        static final int TRANSACTION_getPublicServiceProfile = 92;
        static final int TRANSACTION_getPublicServiceList = 93;
        static final int TRANSACTION_uploadMedia = 94;
        static final int TRANSACTION_downloadMedia = 95;
        static final int TRANSACTION_downloadMediaMessage = 96;
        static final int TRANSACTION_downloadMediaFile = 97;
        static final int TRANSACTION_cancelTransferMediaMessage = 98;
        static final int TRANSACTION_pauseTransferMediaMessage = 99;
        static final int TRANSACTION_pauseTransferMediaFile = 100;
        static final int TRANSACTION_getFileDownloadingStatus = 101;
        static final int TRANSACTION_supportResumeBrokenTransfer = 102;
        static final int TRANSACTION_cancelAllTransferMediaMessage = 103;
        static final int TRANSACTION_getDeltaTime = 104;
        static final int TRANSACTION_setDiscussionInviteStatus = 105;
        static final int TRANSACTION_recallMessage = 106;
        static final int TRANSACTION_addToBlacklist = 107;
        static final int TRANSACTION_removeFromBlacklist = 108;
        static final int TRANSACTION_getTextMessageDraft = 109;
        static final int TRANSACTION_saveTextMessageDraft = 110;
        static final int TRANSACTION_clearTextMessageDraft = 111;
        static final int TRANSACTION_getBlacklist = 112;
        static final int TRANSACTION_getBlacklistStatus = 113;
        static final int TRANSACTION_setUserData = 114;
        static final int TRANSACTION_updateMessageReceiptStatus = 115;
        static final int TRANSACTION_clearUnreadByReceipt = 116;
        static final int TRANSACTION_getSendTimeByMessageId = 117;
        static final int TRANSACTION_getVoIPKey = 118;
        static final int TRANSACTION_getVoIPCallInfo = 119;
        static final int TRANSACTION_getCurrentUserId = 120;
        static final int TRANSACTION_setServerInfo = 121;
        static final int TRANSACTION_setMessageContent = 122;
        static final int TRANSACTION_getUnreadMentionedMessages = 123;
        static final int TRANSACTION_updateReadReceiptRequestInfo = 124;
        static final int TRANSACTION_registerCmdMsgType = 125;
        static final int TRANSACTION_registerCmdMsgTypes = 126;
        static final int TRANSACTION_registerDeleteMessageType = 127;
        static final int TRANSACTION_searchMessages = 128;
        static final int TRANSACTION_searchMessagesByUser = 129;
        static final int TRANSACTION_searchConversations = 130;
        static final int TRANSACTION_getMatchedMessages = 131;
        static final int TRANSACTION_getVendorToken = 132;
        static final int TRANSACTION_writeFwLog = 133;
        static final int TRANSACTION_getNaviCachedTime = 134;
        static final int TRANSACTION_getJoinMultiChatRoomEnable = 135;
        static final int TRANSACTION_getOfflineMessageDuration = 136;
        static final int TRANSACTION_setOfflineMessageDuration = 137;
        static final int TRANSACTION_switchAppKey = 138;
        static final int TRANSACTION_getTheFirstUnreadMessage = 139;
        static final int TRANSACTION_setMessageReadTime = 140;
        static final int TRANSACTION_replenishPing = 141;
        static final int TRANSACTION_sendPing = 142;
        static final int TRANSACTION_setUserPolicy = 143;
        static final int TRANSACTION_setReconnectKickEnable = 144;
        static final int TRANSACTION_getVideoLimitTime = 145;
        static final int TRANSACTION_getGIFLimitSize = 146;
        static final int TRANSACTION_setUserProfileListener = 147;
        static final int TRANSACTION_setConversationStatusListener = 148;
        static final int TRANSACTION_initHttpDns = 149;
        static final int TRANSACTION_getRTCProfile = 150;
        static final int TRANSACTION_updateVoIPCallInfo = 151;
        static final int TRANSACTION_exitRTCRoom = 152;
        static final int TRANSACTION_getRTCUsers = 153;
        static final int TRANSACTION_getRTCUserData = 154;
        static final int TRANSACTION_sendRTCPing = 155;
        static final int TRANSACTION_useRTCOnly = 156;
        static final int TRANSACTION_rtcPutInnerData = 157;
        static final int TRANSACTION_rtcPutOuterData = 158;
        static final int TRANSACTION_rtcDeleteInnerData = 159;
        static final int TRANSACTION_rtcDeleteOuterData = 160;
        static final int TRANSACTION_rtcGetInnerData = 161;
        static final int TRANSACTION_rtcGetOuterData = 162;
        static final int TRANSACTION_joinRTCRoomAndGetData = 163;
        static final int TRANSACTION_getRTCConfig = 164;
        static final int TRANSACTION_getRTCToken = 165;
        static final int TRANSACTION_setRLogOtherProgressCallback = 166;
        static final int TRANSACTION_setSavePath = 167;
        static final int TRANSACTION_setRTCUserData = 168;
        static final int TRANSACTION_solveServerHosts = 169;
        static final int TRANSACTION_setRTCUserDatas = 170;
        static final int TRANSACTION_getRTCUserDatas = 171;
        static final int TRANSACTION_isPhrasesEnabled = 172;
        static final int TRANSACTION_isDnsEnabled = 173;
        static final int TRANSACTION_sendRTCDirectionalMessage = 174;
        static final int TRANSACTION_setChatRoomEntry = 175;
        static final int TRANSACTION_getChatRoomEntry = 176;
        static final int TRANSACTION_getAllChatRoomEntries = 177;
        static final int TRANSACTION_deleteChatRoomEntry = 178;
        static final int TRANSACTION_setNaviContentUpdateListener = 179;
        static final int TRANSACTION_getUploadLogConfigInfo = 180;
        static final int TRANSACTION_getOffLineLogServer = 181;
        static final int TRANSACTION_notifyAppBackgroundChanged = 182;
        static final int TRANSACTION_rtcSetUserResource = 183;

        public Stub() {
            this.attachInterface(this, "io.rong.imlib.IHandler");
        }

        public static IHandler asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            } else {
                IInterface iin = obj.queryLocalInterface("io.rong.imlib.IHandler");
                return (IHandler)(iin != null && iin instanceof IHandler ? (IHandler)iin : new IHandler.Stub.Proxy(obj));
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = "io.rong.imlib.IHandler";
//            String _arg0;
//            String _arg2;
//            boolean _arg5;
//            boolean _arg6;
//            IOperationCallback _arg7;
//            boolean _arg0;
//            String _arg1;
//            String[] _arg1;
//            Message _arg0;
//            int _arg1;
//            boolean _arg3;
//            RTCDataListener _arg2;
//            String[] _arg3;
//            IOperationCallback _arg2;
//            String _arg4;
//            int _arg2;
//            IStringCallback _arg3;
//            long _arg2;
//            IRtcIODataListener _arg3;
//            String[] _arg2;
//            String _arg3;
//            String _arg4;
//            IOperationCallback _arg5;
//            IOperationCallback _arg5;
//            IOperationCallback _arg1;
//            int _arg2;
//            int _arg4;
//            List _result;
//            boolean _result;
//            long _arg4;
//            int _arg3;
//            IOperationCallback _arg2;
//            IResultCallback _arg3;
//            boolean _result;
//            List _result;
//            List _result;
//            byte[] _arg1;
//            int _result;
//            long _arg1;
//            IResultCallback _arg2;
//            IIntegerCallback _arg1;
//            long _arg0;
//            ISendMessageCallback _arg4;
//            boolean _result;
//            ISendMessageCallback _arg3;
//            IStringCallback _arg0;
//            ArrayList _arg0;
//            ArrayList _arg1;
//            Conversation _arg0;
//            RCEncryptedSession _arg1;
//            IOperationCallback _arg0;
//            List _result;
//            Message _result;
//            List _result;
//            int[] _arg0;
            switch(code) {
                case 1:
                    data.enforceInterface(descriptor);
                    this.initAppendixModule();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(descriptor);
                    String _2arg0 = data.readString();
                    boolean _2arg1 = 0 != data.readInt();
                    boolean _2arg2 = 0 != data.readInt();
                    IConnectStringCallback _arg3 = io.rong.imlib.IConnectStringCallback.Stub.asInterface(data.readStrongBinder());
                    this.connect(_2arg0, _2arg1, _2arg2, _arg3);
                    return true;
                case 3:
                    data.enforceInterface(descriptor);
                    boolean _3arg0 = 0 != data.readInt();
                    this.disconnect(_3arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(descriptor);
                    String _4arg0 = data.readString();
                    this.registerMessageType(_4arg0);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(descriptor);
                    ArrayList<String> _5arg0 = data.createStringArrayList();
                    this.registerMessageTypes(_5arg0);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(descriptor);
                    int _6arg0 = this.getTotalUnreadCount();
                    reply.writeNoException();
                    reply.writeInt(_6arg0);
                    return true;
                case 7:
                    data.enforceInterface(descriptor);
                    Conversation[] _7arg0 = (Conversation[])data.createTypedArray(Conversation.CREATOR);
                    int _7arg1 = this.getUnreadCountByConversation(_7arg0);
                    reply.writeNoException();
                    reply.writeInt(_7arg1);
                    return true;
                case 8:
                    data.enforceInterface(descriptor);
                    int[] _8arg0 = data.createIntArray();
                    int _8arg1 = this.getUnreadCount(_8arg0);
                    reply.writeNoException();
                    reply.writeInt(_8arg1);
                    return true;
                case 9:
                    data.enforceInterface(descriptor);
                    int[] _9arg0 = data.createIntArray();
                    boolean _9arg1 = 0 != data.readInt();
                    int _9arg2 = this.getUnreadCountWithDND(_9arg0, _9arg1);
                    reply.writeNoException();
                    reply.writeInt(_9arg2);
                    return true;
                case 10:
                    data.enforceInterface(descriptor);
                    int _10arg0 = data.readInt();
                    String _10arg1 = data.readString();
                    int _10arg2 = this.getUnreadCountById(_10arg0, _10arg1);
                    reply.writeNoException();
                    reply.writeInt(_10arg2);
                    return true;
                case 11:
                    data.enforceInterface(descriptor);
                    OnReceiveMessageListener _11arg0 = io.rong.imlib.OnReceiveMessageListener.Stub.asInterface(data.readStrongBinder());
                    this.setOnReceiveMessageListener(_11arg0);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(descriptor);
                    IConnectionStatusListener _12arg0 = io.rong.imlib.IConnectionStatusListener.Stub.asInterface(data.readStrongBinder());
                    this.setConnectionStatusListener(_12arg0);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(descriptor);
                    this.setIpcConnectTimeOut();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(descriptor);
                    int _14arg0 = data.readInt();
                    this.initIpcConnectStatus(_14arg0);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(descriptor);
                    int _15arg0 = data.readInt();
                    Message _15arg1 = this.getMessage(_15arg0);
                    reply.writeNoException();
                    if (_15arg1 != null) {
                        reply.writeInt(1);
                        _15arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 16:
                    data.enforceInterface(descriptor);
                    Message _16arg0;
                    if (0 != data.readInt()) {
                        _16arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _16arg0 = null;
                    }

                    Message _16arg1 = this.insertMessage(_16arg0);
                    reply.writeNoException();
                    if (_16arg1 != null) {
                        reply.writeInt(1);
                        _16arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 17:
                    data.enforceInterface(descriptor);
                    Message _17arg0;
                    if (0 != data.readInt()) {
                        _17arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _17arg0 = null;
                    }

                    Message _17arg1 = this.insertSettingMessage(_17arg0);
                    reply.writeNoException();
                    if (_17arg1 != null) {
                        reply.writeInt(1);
                        _17arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 18:
                    data.enforceInterface(descriptor);
                    Message _18arg0;
                    if (0 != data.readInt()) {
                        _18arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _18arg0 = null;
                    }

                    String _18arg1 = data.readString();
                    String _18arg2 = data.readString();
                    ISendMessageCallback _18arg3 = io.rong.imlib.ISendMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendMessage(_18arg0, _18arg1, _18arg2, _18arg3);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(descriptor);
                    Message _19arg0;
                    if (0 != data.readInt()) {
                        _19arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _19arg0 = null;
                    }

                    String _19arg1 = data.readString();
                    String _19arg2 = data.readString();
                    SendMessageOption _19arg3;
                    if (0 != data.readInt()) {
                        _19arg3 = (SendMessageOption)SendMessageOption.CREATOR.createFromParcel(data);
                    } else {
                        _19arg3 = null;
                    }

                    ISendMessageCallback _19arg4 = io.rong.imlib.ISendMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendMessageOption(_19arg0, _19arg1, _19arg2, _19arg3, _19arg4);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(descriptor);
                    Message _20arg0;
                    if (0 != data.readInt()) {
                        _20arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _20arg0 = null;
                    }

                    String _20arg1 = data.readString();
                    String _20arg2 = data.readString();
                    String[] _20arg3 = data.createStringArray();
                    ISendMessageCallback _20arg4 = io.rong.imlib.ISendMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendDirectionalMessage(_20arg0, _20arg1, _20arg2, _20arg3, _20arg4);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(descriptor);
                    Message _21arg0;
                    if (0 != data.readInt()) {
                        _21arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _21arg0 = null;
                    }

                    String _21arg1 = data.readString();
                    String _21arg2 = data.readString();
                    ISendMediaMessageCallback _21arg3 = io.rong.imlib.ISendMediaMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendMediaMessage(_21arg0, _21arg1, _21arg2, _21arg3);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(descriptor);
                    Message _22arg0;
                    if (0 != data.readInt()) {
                        _22arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _22arg0 = null;
                    }

                    String[] _22arg1 = data.createStringArray();
                    String _22arg2 = data.readString();
                    String _22arg3 = data.readString();
                    ISendMediaMessageCallback _22arg4 = io.rong.imlib.ISendMediaMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendDirectionalMediaMessage(_22arg0, _22arg1, _22arg2, _22arg3, _22arg4);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(descriptor);
                    Message _23arg0;
                    if (0 != data.readInt()) {
                        _23arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _23arg0 = null;
                    }

                    String _23arg1 = data.readString();
                    String _23arg2 = data.readString();
                    ISendMessageCallback _23arg3 = io.rong.imlib.ISendMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendLocationMessage(_23arg0, _23arg1, _23arg2, _23arg3);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(descriptor);
                    Conversation _24arg0;
                    if (0 != data.readInt()) {
                        _24arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _24arg0 = null;
                    }

                    int _24arg1 = data.readInt();
                    List<Message> _24arg2 = this.getNewestMessages(_24arg0, _24arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_24arg2);
                    return true;
                case 25:
                    data.enforceInterface(descriptor);
                    Conversation _25arg0;
                    if (0 != data.readInt()) {
                        _25arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _25arg0 = null;
                    }

                    long _25arg1 = data.readLong();
                    int _25arg2 = data.readInt();
                    List<Message> _25arg3 = this.getOlderMessages(_25arg0, _25arg1, _25arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_25arg3);
                    return true;
                case 26:
                    data.enforceInterface(descriptor);
                    Conversation _26arg0;
                    if (0 != data.readInt()) {
                        _26arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _26arg0 = null;
                    }

                    long _26arg1 = data.readLong();
                    int _26arg2 = data.readInt();
                    OnGetHistoryMessagesCallback _26arg3 = io.rong.imlib.OnGetHistoryMessagesCallback.Stub.asInterface(data.readStrongBinder());
                    this.getOlderMessagesOneWay(_26arg0, _26arg1, _26arg2, _26arg3);
                    return true;
                case 27:
                    data.enforceInterface(descriptor);
                    Conversation _27arg0;
                    if (0 != data.readInt()) {
                        _27arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _27arg0 = null;
                    }

                    long _27arg1 = data.readLong();
                    int _27arg2 = data.readInt();
                    IResultCallback _27arg3 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.getRemoteHistoryMessages(_27arg0, _27arg1, _27arg2, _27arg3);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(descriptor);
                    Conversation _28arg0;
                    if (0 != data.readInt()) {
                        _28arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _28arg0 = null;
                    }

                    RemoteHistoryMsgOption _28arg1;
                    if (0 != data.readInt()) {
                        _28arg1 = (RemoteHistoryMsgOption)RemoteHistoryMsgOption.CREATOR.createFromParcel(data);
                    } else {
                        _28arg1 = null;
                    }

                    IResultCallback _28arg2 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.getRemoteHistoryMessagesOption(_28arg0, _28arg1, _28arg2);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(descriptor);
                    Conversation _29arg0;
                    if (0 != data.readInt()) {
                        _29arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _29arg0 = null;
                    }

                    long _29arg1 = data.readLong();
                    IOperationCallback _29arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.cleanRemoteHistoryMessages(_29arg0, _29arg1, _29arg2);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(descriptor);
                    Conversation _30arg0;
                    if (0 != data.readInt()) {
                        _30arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _30arg0 = null;
                    }

                    long _30arg1 = data.readLong();
                    IOperationCallback _30arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.cleanHistoryMessages(_30arg0, _30arg1, _30arg2);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(descriptor);
                    String _31arg0 = data.readString();
                    long _31arg1 = data.readLong();
                    int _31arg2 = data.readInt();
                    int _31arg3 = data.readInt();
                    IChatRoomHistoryMessageCallback _31arg4 = io.rong.imlib.IChatRoomHistoryMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.getChatroomHistoryMessages(_31arg0, _31arg1, _31arg2, _31arg3, _31arg4);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(descriptor);
                    String _32arg0 = data.readString();
                    IGetUserStatusCallback _32arg1 = io.rong.imlib.IGetUserStatusCallback.Stub.asInterface(data.readStrongBinder());
                    this.getUserStatus(_32arg0, _32arg1);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(descriptor);
                    int _33arg0 = data.readInt();
                    ISetUserStatusCallback _33arg1 = io.rong.imlib.ISetUserStatusCallback.Stub.asInterface(data.readStrongBinder());
                    this.setUserStatus(_33arg0, _33arg1);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(descriptor);
                    ArrayList<String> _34arg0 = data.createStringArrayList();
                    IIntegerCallback _34arg1 = io.rong.imlib.IIntegerCallback.Stub.asInterface(data.readStrongBinder());
                    this.subscribeStatus(_34arg0, _34arg1);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(descriptor);
                    ISubscribeUserStatusCallback _35arg0 = io.rong.imlib.ISubscribeUserStatusCallback.Stub.asInterface(data.readStrongBinder());
                    this.setSubscribeStatusListener(_35arg0);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(descriptor);
                    int _36arg0 = data.readInt();
                    String _36arg1 = data.readString();
                    ISetPushSettingCallback _36arg2 = io.rong.imlib.ISetPushSettingCallback.Stub.asInterface(data.readStrongBinder());
                    this.setPushSetting(_36arg0, _36arg1, _36arg2);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(descriptor);
                    int _37arg0 = data.readInt();
                    String _37arg1 = this.getPushSetting(_37arg0);
                    reply.writeNoException();
                    reply.writeString(_37arg1);
                    return true;
                case 38:
                    data.enforceInterface(descriptor);
                    Conversation _38arg0;
                    if (0 != data.readInt()) {
                        _38arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _38arg0 = null;
                    }

                    String _38arg1 = data.readString();
                    long _38arg2 = data.readLong();
                    int _38arg3 = data.readInt();
                    boolean _38arg5 = 0 != data.readInt();
                    List<Message> _38arg6 = this.getOlderMessagesByObjectName(_38arg0, _38arg1, _38arg2, _38arg3, _38arg5);
                    reply.writeNoException();
                    reply.writeTypedList(_38arg6);
                    return true;
                case 39:
                    data.enforceInterface(descriptor);
                    Conversation _39arg0;
                    if (0 != data.readInt()) {
                        _39arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _39arg0 = null;
                    }

                    ArrayList<String> _39arg1 = data.createStringArrayList();
                    long _39arg2 = data.readLong();
                    int _39arg3 = data.readInt();
                    boolean _39arg5 = 0 != data.readInt();
                    List<Message> _39arg6 = this.getOlderMessagesByObjectNames(_39arg0, _39arg1, _39arg2, _39arg3, _39arg5);
                    reply.writeNoException();
                    reply.writeTypedList(_39arg6);
                    return true;
                case 40:
                    data.enforceInterface(descriptor);
                    int[] _40arg0 = data.createIntArray();
                    boolean _40arg1 = this.deleteMessage(_40arg0);
                    reply.writeNoException();
                    reply.writeInt(_40arg1 ? 1 : 0);
                    return true;
                case 41:
                    data.enforceInterface(descriptor);
                    int _41arg0 = data.readInt();
                    String _41arg1 = data.readString();
                    Message[] _41arg2 = (Message[])data.createTypedArray(Message.CREATOR);
                    IOperationCallback _41arg3 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.deleteMessages(_41arg0, _41arg1, _41arg2, _41arg3);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(descriptor);
                    int _42arg0 = data.readInt();
                    String _42arg1 = data.readString();
                    boolean _42arg2 = this.deleteConversationMessage(_42arg0, _42arg1);
                    reply.writeNoException();
                    reply.writeInt(_42arg2 ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(descriptor);
                    Conversation _43arg0;
                    if (0 != data.readInt()) {
                        _43arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _43arg0 = null;
                    }

                    boolean _43arg1 = this.clearMessages(_43arg0);
                    reply.writeNoException();
                    reply.writeInt(_43arg1 ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(descriptor);
                    Conversation _44arg0;
                    if (0 != data.readInt()) {
                        _44arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _44arg0 = null;
                    }

                    boolean _44arg1 = this.clearMessagesUnreadStatus(_44arg0);
                    reply.writeNoException();
                    reply.writeInt(_44arg1 ? 1 : 0);
                    return true;
                case 45:
                    data.enforceInterface(descriptor);
                    int _45arg0 = data.readInt();
                    String _45arg1 = data.readString();
                    boolean _45arg2 = this.setMessageExtra(_45arg0, _45arg1);
                    reply.writeNoException();
                    reply.writeInt(_45arg2 ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(descriptor);
                    int _46arg0 = data.readInt();
                    int _46arg1 = data.readInt();
                    boolean _46arg2 = this.setMessageReceivedStatus(_46arg0, _46arg1);
                    reply.writeNoException();
                    reply.writeInt(_46arg2 ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(descriptor);
                    int _47arg0 = data.readInt();
                    int _47arg1 = data.readInt();
                    boolean _47arg2 = this.setMessageSentStatus(_47arg0, _47arg1);
                    reply.writeNoException();
                    reply.writeInt(_47arg2 ? 1 : 0);
                    return true;
                case 48:
                    data.enforceInterface(descriptor);
                    String _48arg0 = data.readString();
                    Message _48arg1 = this.getMessageByUid(_48arg0);
                    reply.writeNoException();
                    if (_48arg1 != null) {
                        reply.writeInt(1);
                        _48arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 49:
                    data.enforceInterface(descriptor);
                    List<Conversation> _49arg0 = this.getConversationList();
                    reply.writeNoException();
                    reply.writeTypedList(_49arg0);
                    return true;
                case 50:
                    data.enforceInterface(descriptor);
                    int _50arg0 = data.readInt();
                    IGetConversationListWithProcessCallback _arg1 = io.rong.imlib.IGetConversationListWithProcessCallback.Stub.asInterface(data.readStrongBinder());
                    this.getConversationListByBatch(_50arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(descriptor);
                    int[] _51arg0 = data.createIntArray();
                    List<Conversation> _51arg1 = this.getConversationListByType(_51arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_51arg1);
                    return true;
                case 52:
                    data.enforceInterface(descriptor);
                    int[] _52arg0 = data.createIntArray();
                    int _52arg1 = data.readInt();
                    IGetConversationListWithProcessCallback _52arg2 = io.rong.imlib.IGetConversationListWithProcessCallback.Stub.asInterface(data.readStrongBinder());
                    this.getConversationListOfTypesByBatch(_52arg0, _52arg1, _52arg2);
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(descriptor);
                    int[] _53arg0 = data.createIntArray();
                    long _53arg1 = data.readLong();
                    int _53arg2 = data.readInt();
                    List<Conversation> _53arg3 = this.getConversationListByPage(_53arg0, _53arg1, _53arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_53arg3);
                    return true;
                case 54:
                    data.enforceInterface(descriptor);
                    int[] _54arg0 = data.createIntArray();
                    List<Conversation> _54arg1 = this.getBlockedConversationList(_54arg0);
                    reply.writeNoException();
                    reply.writeTypedList(_54arg1);
                    return true;
                case 55:
                    data.enforceInterface(descriptor);
                    int _55arg0 = data.readInt();
                    String _55arg1 = data.readString();
                    Conversation _result = this.getConversation(_55arg0, _55arg1);
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 56:
                    data.enforceInterface(descriptor);
                    int _56arg0 = data.readInt();
                    String _56arg1 = data.readString();
                    boolean _56arg2 = this.removeConversation(_56arg0, _56arg1);
                    reply.writeNoException();
                    reply.writeInt(_56arg2 ? 1 : 0);
                    return true;
                case 57:
                    data.enforceInterface(descriptor);
                    Conversation _57arg0;
                    if (0 != data.readInt()) {
                        _57arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _57arg0 = null;
                    }

                    String _57arg1 = data.readString();
                    boolean _57arg2 = this.saveConversationDraft(_57arg0, _57arg1);
                    reply.writeNoException();
                    reply.writeInt(_57arg2 ? 1 : 0);
                    return true;
                case 58:
                    data.enforceInterface(descriptor);
                    Conversation _58arg0;
                    if (0 != data.readInt()) {
                        _58arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _58arg0 = null;
                    }

                    String _58arg1 = this.getConversationDraft(_58arg0);
                    reply.writeNoException();
                    reply.writeString(_58arg1);
                    return true;
                case 59:
                    data.enforceInterface(descriptor);
                    Conversation _59arg0;
                    if (0 != data.readInt()) {
                        _59arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _59arg0 = null;
                    }

                    boolean _59arg1 = this.cleanConversationDraft(_59arg0);
                    reply.writeNoException();
                    reply.writeInt(_59arg1 ? 1 : 0);
                    return true;
                case 60:
                    data.enforceInterface(descriptor);
                    int _60arg0 = data.readInt();
                    String _60arg1 = data.readString();
                    ILongCallback _60arg2 = io.rong.imlib.ILongCallback.Stub.asInterface(data.readStrongBinder());
                    this.getConversationNotificationStatus(_60arg0, _60arg1, _60arg2);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(descriptor);
                    int _61arg0 = data.readInt();
                    String _61arg1 = data.readString();
                    int _61arg2 = data.readInt();
                    ILongCallback _61arg3 = io.rong.imlib.ILongCallback.Stub.asInterface(data.readStrongBinder());
                    this.setConversationNotificationStatus(_61arg0, _61arg1, _61arg2, _61arg3);
                    reply.writeNoException();
                    return true;
                case 62:
                    data.enforceInterface(descriptor);
                    int _62arg0 = data.readInt();
                    String _62arg1 = data.readString();
                    int _62arg2 = data.readInt();
                    boolean _62arg3 = this.syncConversationNotificationStatus(_62arg0, _62arg1, _62arg2);
                    reply.writeNoException();
                    reply.writeInt(_62arg3 ? 1 : 0);
                    return true;
                case 63:
                    data.enforceInterface(descriptor);
                    int _63arg0 = data.readInt();
                    String _63arg1 = data.readString();
                    boolean _63arg2 = 0 != data.readInt();
                    boolean _63arg3 = 0 != data.readInt();
                    boolean _63arg4 = this.setConversationTopStatus(_63arg0, _63arg1, _63arg2, _63arg3);
                    reply.writeNoException();
                    reply.writeInt(_63arg4 ? 1 : 0);
                    return true;
                case 64:
                    data.enforceInterface(descriptor);
                    Conversation _64arg0;
                    if (0 != data.readInt()) {
                        _64arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _64arg0 = null;
                    }

                    int _64arg1 = this.getConversationUnreadCount(_64arg0);
                    reply.writeNoException();
                    reply.writeInt(_64arg1);
                    return true;
                case 65:
                    data.enforceInterface(descriptor);
                    int[] _65arg0 = data.createIntArray();
                    boolean _65arg1 = this.clearConversations(_65arg0);
                    reply.writeNoException();
                    reply.writeInt(_65arg1 ? 1 : 0);
                    return true;
                case 66:
                    data.enforceInterface(descriptor);
                    String _66arg0 = data.readString();
                    int _66arg1 = data.readInt();
                    IOperationCallback _66arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setNotificationQuietHours(_66arg0, _66arg1, _66arg2);
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(descriptor);
                    IOperationCallback _67arg0 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.removeNotificationQuietHours(_67arg0);
                    reply.writeNoException();
                    return true;
                case 68:
                    data.enforceInterface(descriptor);
                    IGetNotificationQuietHoursCallback _68arg0 = io.rong.imlib.IGetNotificationQuietHoursCallback.Stub.asInterface(data.readStrongBinder());
                    this.getNotificationQuietHours(_68arg0);
                    reply.writeNoException();
                    return true;
                case 69:
                    data.enforceInterface(descriptor);
                    int _69arg0 = data.readInt();
                    String _69arg1 = data.readString();
                    String _69arg2 = data.readString();
                    String _69arg3 = data.readString();
                    boolean _69arg4 = this.updateConversationInfo(_69arg0, _69arg1, _69arg2, _69arg3);
                    reply.writeNoException();
                    reply.writeInt(_69arg4 ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(descriptor);
                    String _70arg0 = data.readString();
                    RCEncryptedSession _70arg1;
                    if (0 != data.readInt()) {
                        _70arg1 = (RCEncryptedSession)RCEncryptedSession.CREATOR.createFromParcel(data);
                    } else {
                        _70arg1 = null;
                    }

                    boolean _70arg2 = this.createEncryptedConversation(_70arg0, _70arg1);
                    reply.writeNoException();
                    reply.writeInt(_70arg2 ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(descriptor);
                    String _71arg0 = data.readString();
                    RCEncryptedSession _71arg1 = this.getEncryptedConversation(_71arg0);
                    reply.writeNoException();
                    if (_71arg1 != null) {
                        reply.writeInt(1);
                        _71arg1.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 72:
                    data.enforceInterface(descriptor);
                    String _72arg0 = data.readString();
                    RCEncryptedSession _72arg1;
                    if (0 != data.readInt()) {
                        _72arg1 = (RCEncryptedSession)RCEncryptedSession.CREATOR.createFromParcel(data);
                    } else {
                        _72arg1 = null;
                    }

                    boolean _72arg2 = this.setEncryptedConversation(_72arg0, _72arg1);
                    reply.writeNoException();
                    reply.writeInt(_72arg2 ? 1 : 0);
                    return true;
                case 73:
                    data.enforceInterface(descriptor);
                    String _73arg0 = data.readString();
                    boolean _73arg1 = this.removeEncryptedConversation(_73arg0);
                    reply.writeNoException();
                    reply.writeInt(_73arg1 ? 1 : 0);
                    return true;
                case 74:
                    data.enforceInterface(descriptor);
                    boolean _74arg0 = this.clearEncryptedConversations();
                    reply.writeNoException();
                    reply.writeInt(_74arg0 ? 1 : 0);
                    return true;
                case 75:
                    data.enforceInterface(descriptor);
                    List<RCEncryptedSession> _75arg0 = this.getAllEncryptedConversations();
                    reply.writeNoException();
                    reply.writeTypedList(_75arg0);
                    return true;
                case 76:
                    data.enforceInterface(descriptor);
                    String _76arg0 = data.readString();
                    IResultCallback _76arg1 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.getDiscussion(_76arg0, _76arg1);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(descriptor);
                    String _77arg0 = data.readString();
                    String _77arg1 = data.readString();
                    IOperationCallback _77arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setDiscussionName(_77arg0, _77arg1, _77arg2);
                    reply.writeNoException();
                    return true;
                case 78:
                    data.enforceInterface(descriptor);
                    String _78arg0 = data.readString();
                    ArrayList<String> _78arg1 = data.createStringArrayList();
                    IResultCallback _78arg2 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.createDiscussion(_78arg0, _78arg1, _78arg2);
                    reply.writeNoException();
                    return true;
                case 79:
                    data.enforceInterface(descriptor);
                    String _79arg0 = data.readString();
                    ArrayList<String> _79arg1 = data.createStringArrayList();
                    IOperationCallback _79arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.addMemberToDiscussion(_79arg0, _79arg1, _79arg2);
                    reply.writeNoException();
                    return true;
                case 80:
                    data.enforceInterface(descriptor);
                    String _80arg0 = data.readString();
                    String _80arg1 = data.readString();
                    IOperationCallback _80arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.removeDiscussionMember(_80arg0, _80arg1, _80arg2);
                    reply.writeNoException();
                    return true;
                case 81:
                    data.enforceInterface(descriptor);
                    String _81arg0 = data.readString();
                    IOperationCallback _81arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.quitDiscussion(_81arg0, _81arg1);
                    reply.writeNoException();
                    return true;
                case 82:
                    data.enforceInterface(descriptor);
                    ArrayList<Group> _82arg0 = data.createTypedArrayList(Group.CREATOR);
                    IOperationCallback _82arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.syncGroup(_82arg0, _82arg1);
                    reply.writeNoException();
                    return true;
                case 83:
                    data.enforceInterface(descriptor);
                    String _83arg0 = data.readString();
                    String _83arg1 = data.readString();
                    IOperationCallback _83arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.joinGroup(_83arg0, _83arg1, _83arg2);
                    reply.writeNoException();
                    return true;
                case 84:
                    data.enforceInterface(descriptor);
                    String _84arg0 = data.readString();
                    IOperationCallback _84arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.quitGroup(_84arg0, _84arg1);
                    reply.writeNoException();
                    return true;
                case 85:
                    data.enforceInterface(descriptor);
                    String _85arg0 = data.readString();
                    int _85arg1 = data.readInt();
                    int _85arg2 = data.readInt();
                    IResultCallback _85arg3 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.getChatRoomInfo(_85arg0, _85arg1, _85arg2, _85arg3);
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(descriptor);
                    String _86arg0 = data.readString();
                    int _86arg1 = data.readInt();
                    IOperationCallback _86arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.reJoinChatRoom(_86arg0, _86arg1, _86arg2);
                    reply.writeNoException();
                    return true;
                case 87:
                    data.enforceInterface(descriptor);
                    String _87arg0 = data.readString();
                    int _87arg1 = data.readInt();
                    IOperationCallback _87arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.joinChatRoom(_87arg0, _87arg1, _87arg2);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(descriptor);
                    String _88arg0 = data.readString();
                    int _88arg1 = data.readInt();
                    IOperationCallback _88arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    boolean _88arg3 = 0 != data.readInt();
                    this.joinExistChatRoom(_88arg0, _88arg1, _88arg2, _88arg3);
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(descriptor);
                    String _89arg0 = data.readString();
                    IOperationCallback _89arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.quitChatRoom(_89arg0, _89arg1);
                    reply.writeNoException();
                    return true;
                case 90:
                    data.enforceInterface(descriptor);
                    String _90arg0 = data.readString();
                    int _90arg1 = data.readInt();
                    int _90arg2 = data.readInt();
                    IResultCallback _90arg3 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.searchPublicService(_90arg0, _90arg1, _90arg2, _90arg3);
                    reply.writeNoException();
                    return true;
                case 91:
                    data.enforceInterface(descriptor);
                    String _91arg0 = data.readString();
                    int _91arg1 = data.readInt();
                    boolean _91arg2 = 0 != data.readInt();
                    IOperationCallback _91arg3 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.subscribePublicService(_91arg0, _91arg1, _91arg2, _91arg3);
                    reply.writeNoException();
                    return true;
                case 92:
                    data.enforceInterface(descriptor);
                    String _92arg0 = data.readString();
                    int _92arg1 = data.readInt();
                    IResultCallback _92arg2 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.getPublicServiceProfile(_92arg0, _92arg1, _92arg2);
                    reply.writeNoException();
                    return true;
                case 93:
                    data.enforceInterface(descriptor);
                    IResultCallback _93arg0 = io.rong.imlib.IResultCallback.Stub.asInterface(data.readStrongBinder());
                    this.getPublicServiceList(_93arg0);
                    reply.writeNoException();
                    return true;
                case 94:
                    data.enforceInterface(descriptor);
                    Message _94arg0;
                    if (0 != data.readInt()) {
                        _94arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _94arg0 = null;
                    }

                    IUploadCallback _94arg1 = io.rong.imlib.IUploadCallback.Stub.asInterface(data.readStrongBinder());
                    this.uploadMedia(_94arg0, _94arg1);
                    reply.writeNoException();
                    return true;
                case 95:
                    data.enforceInterface(descriptor);
                    Conversation _95arg0;
                    if (0 != data.readInt()) {
                        _95arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _95arg0 = null;
                    }

                    int _95arg1 = data.readInt();
                    String _95arg2 = data.readString();
                    IDownloadMediaCallback _95arg3 = io.rong.imlib.IDownloadMediaCallback.Stub.asInterface(data.readStrongBinder());
                    this.downloadMedia(_95arg0, _95arg1, _95arg2, _95arg3);
                    reply.writeNoException();
                    return true;
                case 96:
                    data.enforceInterface(descriptor);
                    Message _96arg0;
                    if (0 != data.readInt()) {
                        _96arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _96arg0 = null;
                    }

                    IDownloadMediaMessageCallback _96arg1 = io.rong.imlib.IDownloadMediaMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.downloadMediaMessage(_96arg0, _96arg1);
                    reply.writeNoException();
                    return true;
                case 97:
                    data.enforceInterface(descriptor);
                    String _97arg0 = data.readString();
                    String _97arg1 = data.readString();
                    String _97arg2 = data.readString();
                    String _97arg3 = data.readString();
                    IDownloadMediaFileCallback _97arg4 = io.rong.imlib.IDownloadMediaFileCallback.Stub.asInterface(data.readStrongBinder());
                    this.downloadMediaFile(_97arg0, _97arg1, _97arg2, _97arg3, _97arg4);
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(descriptor);
                    Message _98arg0;
                    if (0 != data.readInt()) {
                        _98arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _98arg0 = null;
                    }

                    IOperationCallback _98arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.cancelTransferMediaMessage(_98arg0, _98arg1);
                    reply.writeNoException();
                    return true;
                case 99:
                    data.enforceInterface(descriptor);
                    Message _99arg0;
                    if (0 != data.readInt()) {
                        _99arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _99arg0 = null;
                    }

                    IOperationCallback _99arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.pauseTransferMediaMessage(_99arg0, _99arg1);
                    reply.writeNoException();
                    return true;
                case 100:
                    data.enforceInterface(descriptor);
                    String _100arg0 = data.readString();
                    IOperationCallback _100arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.pauseTransferMediaFile(_100arg0, _100arg1);
                    reply.writeNoException();
                    return true;
                case 101:
                    data.enforceInterface(descriptor);
                    String _101arg0 = data.readString();
                    boolean _101arg1 = this.getFileDownloadingStatus(_101arg0);
                    reply.writeNoException();
                    reply.writeInt(_101arg1 ? 1 : 0);
                    return true;
                case 102:
                    data.enforceInterface(descriptor);
                    String _102arg0 = data.readString();
                    boolean _102arg1 = this.supportResumeBrokenTransfer(_102arg0);
                    reply.writeNoException();
                    reply.writeInt(_102arg1 ? 1 : 0);
                    return true;
                case 103:
                    data.enforceInterface(descriptor);
                    IOperationCallback _103arg0 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.cancelAllTransferMediaMessage(_103arg0);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(descriptor);
                    long _104arg0 = this.getDeltaTime();
                    reply.writeNoException();
                    reply.writeLong(_104arg0);
                    return true;
                case 105:
                    data.enforceInterface(descriptor);
                    String _105arg0 = data.readString();
                    int _105arg1 = data.readInt();
                    IOperationCallback _105arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setDiscussionInviteStatus(_105arg0, _105arg1, _105arg2);
                    reply.writeNoException();
                    return true;
                case 106:
                    data.enforceInterface(descriptor);
                    String _106arg0 = data.readString();
                    byte[] _106arg1 = data.createByteArray();
                    String _106arg2 = data.readString();
                    int _106arg3 = data.readInt();
                    String _106arg4 = data.readString();
                    int _106arg5 = data.readInt();
                    IOperationCallback _106arg6 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.recallMessage(_106arg0, _106arg1, _106arg2, _106arg3, _106arg4, _106arg5, _106arg6);
                    reply.writeNoException();
                    return true;
                case 107:
                    data.enforceInterface(descriptor);
                    String _107arg0 = data.readString();
                    IOperationCallback _107arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.addToBlacklist(_107arg0, _107arg1);
                    reply.writeNoException();
                    return true;
                case 108:
                    data.enforceInterface(descriptor);
                    String _108arg0 = data.readString();
                    IOperationCallback _108arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.removeFromBlacklist(_108arg0, _108arg1);
                    reply.writeNoException();
                    return true;
                case 109:
                    data.enforceInterface(descriptor);
                    Conversation _109arg0;
                    if (0 != data.readInt()) {
                        _109arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _109arg0 = null;
                    }

                    String _109arg1 = this.getTextMessageDraft(_109arg0);
                    reply.writeNoException();
                    reply.writeString(_109arg1);
                    return true;
                case 110:
                    data.enforceInterface(descriptor);
                    Conversation _110arg0;
                    if (0 != data.readInt()) {
                        _110arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _110arg0 = null;
                    }

                    String _110arg1 = data.readString();
                    boolean _110arg2 = this.saveTextMessageDraft(_110arg0, _110arg1);
                    reply.writeNoException();
                    reply.writeInt(_110arg2 ? 1 : 0);
                    return true;
                case 111:
                    data.enforceInterface(descriptor);
                    Conversation _111arg0;
                    if (0 != data.readInt()) {
                        _111arg0 = (Conversation)Conversation.CREATOR.createFromParcel(data);
                    } else {
                        _111arg0 = null;
                    }

                    boolean _111arg1 = this.clearTextMessageDraft(_111arg0);
                    reply.writeNoException();
                    reply.writeInt(_111arg1 ? 1 : 0);
                    return true;
                case 112:
                    data.enforceInterface(descriptor);
                    IStringCallback _112arg0 = io.rong.imlib.IStringCallback.Stub.asInterface(data.readStrongBinder());
                    this.getBlacklist(_112arg0);
                    reply.writeNoException();
                    return true;
                case 113:
                    data.enforceInterface(descriptor);
                    String _113arg0 = data.readString();
                    IIntegerCallback _113arg1 = io.rong.imlib.IIntegerCallback.Stub.asInterface(data.readStrongBinder());
                    this.getBlacklistStatus(_113arg0, _113arg1);
                    reply.writeNoException();
                    return true;
                case 114:
                    data.enforceInterface(descriptor);
                    UserData _114arg0;
                    if (0 != data.readInt()) {
                        _114arg0 = (UserData)UserData.CREATOR.createFromParcel(data);
                    } else {
                        _114arg0 = null;
                    }

                    IOperationCallback _114arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setUserData(_114arg0, _114arg1);
                    reply.writeNoException();
                    return true;
                case 115:
                    data.enforceInterface(descriptor);
                    String _115arg0 = data.readString();
                    int _115arg1 = data.readInt();
                    long _115arg2 = data.readLong();
                    boolean _115arg3 = this.updateMessageReceiptStatus(_115arg0, _115arg1, _115arg2);
                    reply.writeNoException();
                    reply.writeInt(_115arg3 ? 1 : 0);
                    return true;
                case 116:
                    data.enforceInterface(descriptor);
                    int _116arg0 = data.readInt();
                    String _116arg1 = data.readString();
                    long _116arg2 = data.readLong();
                    boolean _116arg3 = this.clearUnreadByReceipt(_116arg0, _116arg1, _116arg2);
                    reply.writeNoException();
                    reply.writeInt(_116arg3 ? 1 : 0);
                    return true;
                case 117:
                    data.enforceInterface(descriptor);
                    int _117arg0 = data.readInt();
                    long _117arg1 = this.getSendTimeByMessageId(_117arg0);
                    reply.writeNoException();
                    reply.writeLong(_117arg1);
                    return true;
                case 118:
                    data.enforceInterface(descriptor);
                    int _118arg0 = data.readInt();
                    String _118arg1 = data.readString();
                    String _118arg2 = data.readString();
                    IStringCallback _118arg3 = io.rong.imlib.IStringCallback.Stub.asInterface(data.readStrongBinder());
                    this.getVoIPKey(_118arg0, _118arg1, _118arg2, _118arg3);
                    reply.writeNoException();
                    return true;
                case 119:
                    data.enforceInterface(descriptor);
                    String _119arg0 = this.getVoIPCallInfo();
                    reply.writeNoException();
                    reply.writeString(_119arg0);
                    return true;
                case 120:
                    data.enforceInterface(descriptor);
                    String _120arg0 = this.getCurrentUserId();
                    reply.writeNoException();
                    reply.writeString(_120arg0);
                    return true;
                case 121:
                    data.enforceInterface(descriptor);
                    String _121arg0 = data.readString();
                    String _121arg1 = data.readString();
                    this.setServerInfo(_121arg0, _121arg1);
                    reply.writeNoException();
                    return true;
                case 122:
                    data.enforceInterface(descriptor);
                    int _122arg0 = data.readInt();
                    byte[] _122arg1 = data.createByteArray();
                    String _122arg2 = data.readString();
                    boolean _122arg3 = this.setMessageContent(_122arg0, _122arg1, _122arg2);
                    reply.writeNoException();
                    reply.writeInt(_122arg3 ? 1 : 0);
                    return true;
                case 123:
                    data.enforceInterface(descriptor);
                    int _123arg0 = data.readInt();
                    String _123arg1 = data.readString();
                    List<Message> _123arg2 = this.getUnreadMentionedMessages(_123arg0, _123arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_123arg2);
                    return true;
                case 124:
                    data.enforceInterface(descriptor);
                    String _124arg0 = data.readString();
                    String _124arg1 = data.readString();
                    boolean _124arg2 = this.updateReadReceiptRequestInfo(_124arg0, _124arg1);
                    reply.writeNoException();
                    reply.writeInt(_124arg2 ? 1 : 0);
                    return true;
                case 125:
                    data.enforceInterface(descriptor);
                    String _125arg0 = data.readString();
                    this.registerCmdMsgType(_125arg0);
                    reply.writeNoException();
                    return true;
                case 126:
                    data.enforceInterface(descriptor);
                    ArrayList<String> _126arg0 = data.createStringArrayList();
                    this.registerCmdMsgTypes(_126arg0);
                    reply.writeNoException();
                    return true;
                case 127:
                    data.enforceInterface(descriptor);
                    ArrayList<String> _127arg0 = data.createStringArrayList();
                    this.registerDeleteMessageType(_127arg0);
                    reply.writeNoException();
                    return true;
                case 128:
                    data.enforceInterface(descriptor);
                    String _128arg0 = data.readString();
                    int _128arg1 = data.readInt();
                    String _128arg2 = data.readString();
                    int _128arg3 = data.readInt();
                    long _128arg4 = data.readLong();
                    List<Message> _128arg5 = this.searchMessages(_128arg0, _128arg1, _128arg2, _128arg3, _128arg4);
                    reply.writeNoException();
                    reply.writeTypedList(_128arg5);
                    return true;
                case 129:
                    data.enforceInterface(descriptor);
                    String _129arg0 = data.readString();
                    int _129arg1 = data.readInt();
                    String _129arg2 = data.readString();
                    int _129arg3 = data.readInt();
                    long _129arg4 = data.readLong();
                    List<Message> _129arg5 = this.searchMessagesByUser(_129arg0, _129arg1, _129arg2, _129arg3, _129arg4);
                    reply.writeNoException();
                    reply.writeTypedList(_129arg5);
                    return true;
                case 130:
                    data.enforceInterface(descriptor);
                    String _130arg0 = data.readString();
                    int[] _130arg1 = data.createIntArray();
                    String[] _130arg2 = data.createStringArray();
                    List<SearchConversationResult> _130arg3 = this.searchConversations(_130arg0, _130arg1, _130arg2);
                    reply.writeNoException();
                    reply.writeTypedList(_130arg3);
                    return true;
                case 131:
                    data.enforceInterface(descriptor);
                    String _131arg0 = data.readString();
                    int _131arg1 = data.readInt();
                    long _131arg2 = data.readLong();
                    int _131arg3 = data.readInt();
                    int _131arg4 = data.readInt();
                    List<Message> _131arg5 = this.getMatchedMessages(_131arg0, _131arg1, _131arg2, _131arg3, _131arg4);
                    reply.writeNoException();
                    reply.writeTypedList(_131arg5);
                    return true;
                case 132:
                    data.enforceInterface(descriptor);
                    IStringCallback _132arg0 = io.rong.imlib.IStringCallback.Stub.asInterface(data.readStrongBinder());
                    this.getVendorToken(_132arg0);
                    reply.writeNoException();
                    return true;
                case 133:
                    data.enforceInterface(descriptor);
                    int _133arg0 = data.readInt();
                    String _133arg1 = data.readString();
                    String _133arg2 = data.readString();
                    String _133arg3 = data.readString();
                    long _133arg4 = data.readLong();
                    this.writeFwLog(_133arg0, _133arg1, _133arg2, _133arg3, _133arg4);
                    reply.writeNoException();
                    return true;
                case 134:
                    data.enforceInterface(descriptor);
                    long _134arg0 = this.getNaviCachedTime();
                    reply.writeNoException();
                    reply.writeLong(_134arg0);
                    return true;
                case 135:
                    data.enforceInterface(descriptor);
                    boolean _135arg0 = this.getJoinMultiChatRoomEnable();
                    reply.writeNoException();
                    reply.writeInt(_135arg0 ? 1 : 0);
                    return true;
                case 136:
                    data.enforceInterface(descriptor);
                    String _136arg0 = this.getOfflineMessageDuration();
                    reply.writeNoException();
                    reply.writeString(_136arg0);
                    return true;
                case 137:
                    data.enforceInterface(descriptor);
                    String _137arg0 = data.readString();
                    ILongCallback _137arg1 = io.rong.imlib.ILongCallback.Stub.asInterface(data.readStrongBinder());
                    this.setOfflineMessageDuration(_137arg0, _137arg1);
                    reply.writeNoException();
                    return true;
                case 138:
                    data.enforceInterface(descriptor);
                    String _138arg0 = data.readString();
                    String _138arg1 = data.readString();
                    this.switchAppKey(_138arg0, _138arg1);
                    reply.writeNoException();
                    return true;
                case 139:
                    data.enforceInterface(descriptor);
                    int _139arg0 = data.readInt();
                    String _139arg1 = data.readString();
                    Message _139arg2 = this.getTheFirstUnreadMessage(_139arg0, _139arg1);
                    reply.writeNoException();
                    if (_139arg2 != null) {
                        reply.writeInt(1);
                        _139arg2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }

                    return true;
                case 140:
                    data.enforceInterface(descriptor);
                    long _140arg0 = data.readLong();
                    long _140arg1 = data.readLong();
                    boolean _140arg2 = this.setMessageReadTime(_140arg0, _140arg1);
                    reply.writeNoException();
                    reply.writeInt(_140arg2 ? 1 : 0);
                    return true;
                case 141:
                    data.enforceInterface(descriptor);
                    boolean _141arg0 = 0 != data.readInt();
                    this.replenishPing(_141arg0);
                    return true;
                case 142:
                    data.enforceInterface(descriptor);
                    this.sendPing();
                    reply.writeNoException();
                    return true;
                case 143:
                    data.enforceInterface(descriptor);
                    boolean _143arg0 = 0 != data.readInt();
                    this.setUserPolicy(_143arg0);
                    return true;
                case 144:
                    data.enforceInterface(descriptor);
                    boolean _144arg0 = 0 != data.readInt();
                    this.setReconnectKickEnable(_144arg0);
                    reply.writeNoException();
                    return true;
                case 145:
                    data.enforceInterface(descriptor);
                    int _145arg0 = this.getVideoLimitTime();
                    reply.writeNoException();
                    reply.writeInt(_145arg0);
                    return true;
                case 146:
                    data.enforceInterface(descriptor);
                    int _146arg0 = this.getGIFLimitSize();
                    reply.writeNoException();
                    reply.writeInt(_146arg0);
                    return true;
                case 147:
                    data.enforceInterface(descriptor);
                    UserProfileSettingListener _147arg0 = io.rong.imlib.UserProfileSettingListener.Stub.asInterface(data.readStrongBinder());
                    this.setUserProfileListener(_147arg0);
                    reply.writeNoException();
                    return true;
                case 148:
                    data.enforceInterface(descriptor);
                    ConversationStatusListener _148arg0 = io.rong.imlib.ConversationStatusListener.Stub.asInterface(data.readStrongBinder());
                    this.setConversationStatusListener(_148arg0);
                    reply.writeNoException();
                    return true;
                case 149:
                    data.enforceInterface(descriptor);
                    this.initHttpDns();
                    reply.writeNoException();
                    return true;
                case 150:
                    data.enforceInterface(descriptor);
                    String _150arg0 = this.getRTCProfile();
                    reply.writeNoException();
                    reply.writeString(_150arg0);
                    return true;
                case 151:
                    data.enforceInterface(descriptor);
                    String _151arg0 = data.readString();
                    this.updateVoIPCallInfo(_151arg0);
                    reply.writeNoException();
                    return true;
                case 152:
                    data.enforceInterface(descriptor);
                    String _152arg0 = data.readString();
                    IOperationCallback _152arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.exitRTCRoom(_152arg0, _152arg1);
                    reply.writeNoException();
                    return true;
                case 153:
                    data.enforceInterface(descriptor);
                    String _153arg0 = data.readString();
                    int _153arg1 = data.readInt();
                    RTCDataListener _153arg2 = io.rong.imlib.RTCDataListener.Stub.asInterface(data.readStrongBinder());
                    this.getRTCUsers(_153arg0, _153arg1, _153arg2);
                    reply.writeNoException();
                    return true;
                case 154:
                    data.enforceInterface(descriptor);
                    String _154arg0 = data.readString();
                    int _154arg1 = data.readInt();
                    RTCDataListener _154arg2 = io.rong.imlib.RTCDataListener.Stub.asInterface(data.readStrongBinder());
                    this.getRTCUserData(_154arg0, _154arg1, _154arg2);
                    reply.writeNoException();
                    return true;
                case 155:
                    data.enforceInterface(descriptor);
                    String _155arg0 = data.readString();
                    IOperationCallback _155arg1 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendRTCPing(_155arg0, _155arg1);
                    reply.writeNoException();
                    return true;
                case 156:
                    data.enforceInterface(descriptor);
                    boolean _156arg0 = this.useRTCOnly();
                    reply.writeNoException();
                    reply.writeInt(_156arg0 ? 1 : 0);
                    return true;
                case 157:
                    data.enforceInterface(descriptor);
                    String _157arg0 = data.readString();
                    int _157arg1 = data.readInt();
                    String _157arg2 = data.readString();
                    String _157arg3 = data.readString();
                    String _157arg4 = data.readString();
                    String _157arg5 = data.readString();
                    IOperationCallback _157arg6 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.rtcPutInnerData(_157arg0, _157arg1, _157arg2, _157arg3, _157arg4, _157arg5, _157arg6);
                    reply.writeNoException();
                    return true;
                case 158:
                    data.enforceInterface(descriptor);
                    String _158arg0 = data.readString();
                    int _158arg1 = data.readInt();
                    String _158arg2 = data.readString();
                    String _158arg3 = data.readString();
                    String _158arg4 = data.readString();
                    String _158arg5 = data.readString();
                    IOperationCallback _158arg6 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.rtcPutOuterData(_158arg0, _158arg1, _158arg2, _158arg3, _158arg4, _158arg5, _158arg6);
                    reply.writeNoException();
                    return true;
                case 159:
                    data.enforceInterface(descriptor);
                    String _159arg0 = data.readString();
                    int _159arg2 = data.readInt();
                    String[] _159arg3 = data.createStringArray();
                    String _159arg4 = data.readString();
                    String _159arg5 = data.readString();
                    IOperationCallback _159arg6 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.rtcDeleteInnerData(_159arg0, _159arg2, _159arg3, _159arg4, _159arg5, _159arg6);
                    reply.writeNoException();
                    return true;
                case 160:
                    data.enforceInterface(descriptor);
                    String _160arg0 = data.readString();
                    int _160arg1 = data.readInt();
                    String[] _160arg2 = data.createStringArray();
                    String _160arg3 = data.readString();
                    String _160arg4 = data.readString();
                    IOperationCallback _160arg5 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.rtcDeleteOuterData(_160arg0, _160arg1, _160arg2, _160arg3, _160arg4, _160arg5);
                    reply.writeNoException();
                    return true;
                case 161:
                    data.enforceInterface(descriptor);
                    String _161arg0 = data.readString();
                    int _161arg1 = data.readInt();
                    String[] _161arg2 = data.createStringArray();
                    IRtcIODataListener _161arg3 = io.rong.imlib.IRtcIODataListener.Stub.asInterface(data.readStrongBinder());
                    this.rtcGetInnerData(_161arg0, _161arg1, _161arg2, _161arg3);
                    reply.writeNoException();
                    return true;
                case 162:
                    data.enforceInterface(descriptor);
                    String _162arg0 = data.readString();
                    int _162arg1 = data.readInt();
                    String[] _162arg2 = data.createStringArray();
                    IRtcIODataListener _162arg3 = io.rong.imlib.IRtcIODataListener.Stub.asInterface(data.readStrongBinder());
                    this.rtcGetOuterData(_162arg0, _162arg1, _162arg2, _162arg3);
                    reply.writeNoException();
                    return true;
                case 163:
                    data.enforceInterface(descriptor);
                    String _163arg0 = data.readString();
                    int _163arg1 = data.readInt();
                    int _163arg2 = data.readInt();
                    IRTCJoinRoomCallback _163arg3 = io.rong.imlib.IRTCJoinRoomCallback.Stub.asInterface(data.readStrongBinder());
                    this.joinRTCRoomAndGetData(_163arg0, _163arg1, _163arg2, _163arg3);
                    reply.writeNoException();
                    return true;
                case 164:
                    data.enforceInterface(descriptor);
                    String _164arg0 = data.readString();
                    String _164arg1 = data.readString();
                    long _164arg2 = data.readLong();
                    String _164arg3 = data.readString();
                    IRTCConfigCallback _164arg4 = io.rong.imlib.IRTCConfigCallback.Stub.asInterface(data.readStrongBinder());
                    this.getRTCConfig(_164arg0, _164arg1, _164arg2, _164arg3, _164arg4);
                    reply.writeNoException();
                    return true;
                case 165:
                    data.enforceInterface(descriptor);
                    String _165arg0 = data.readString();
                    int _165arg1 = data.readInt();
                    int _165arg2 = data.readInt();
                    IStringCallback _165arg3 = io.rong.imlib.IStringCallback.Stub.asInterface(data.readStrongBinder());
                    this.getRTCToken(_165arg0, _165arg1, _165arg2, _165arg3);
                    reply.writeNoException();
                    return true;
                case 166:
                    data.enforceInterface(descriptor);
                    IRLogOtherProgressCallback _166arg0 = io.rong.imlib.IRLogOtherProgressCallback.Stub.asInterface(data.readStrongBinder());
                    this.setRLogOtherProgressCallback(_166arg0);
                    reply.writeNoException();
                    return true;
                case 167:
                    data.enforceInterface(descriptor);
                    String _167arg0 = data.readString();
                    this.setSavePath(_167arg0);
                    reply.writeNoException();
                    return true;
                case 168:
                    data.enforceInterface(descriptor);
                    String _168arg0 = data.readString();
                    String _168arg1 = data.readString();
                    IOperationCallback _168arg2 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setRTCUserData(_168arg0, _168arg1, _168arg2);
                    reply.writeNoException();
                    return true;
                case 169:
                    data.enforceInterface(descriptor);
                    String _169arg0 = data.readString();
                    ISolveServerHostsCallBack _169arg1 = io.rong.imlib.ISolveServerHostsCallBack.Stub.asInterface(data.readStrongBinder());
                    this.solveServerHosts(_169arg0, _169arg1);
                    reply.writeNoException();
                    return true;
                case 170:
                    data.enforceInterface(descriptor);
                    String _170arg0 = data.readString();
                    int _170arg1 = data.readInt();
                    ClassLoader cl = this.getClass().getClassLoader();
                    Map _170arg2 = data.readHashMap(cl);
                    String _170arg3 = data.readString();
                    String _170arg4 = data.readString();
                    IOperationCallback _170arg5 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setRTCUserDatas(_170arg0, _170arg1, _170arg2, _170arg3, _170arg4, _170arg5);
                    reply.writeNoException();
                    return true;
                case 171:
                    data.enforceInterface(descriptor);
                    String _171arg0 = data.readString();
                    String[] _171arg1 = data.createStringArray();
                    RTCDataListener _171arg2 = io.rong.imlib.RTCDataListener.Stub.asInterface(data.readStrongBinder());
                    this.getRTCUserDatas(_171arg0, _171arg1, _171arg2);
                    reply.writeNoException();
                    return true;
                case 172:
                    data.enforceInterface(descriptor);
                    boolean _172arg0 = this.isPhrasesEnabled();
                    reply.writeNoException();
                    reply.writeInt(_172arg0 ? 1 : 0);
                    return true;
                case 173:
                    data.enforceInterface(descriptor);
                    boolean _173arg0 = this.isDnsEnabled();
                    reply.writeNoException();
                    reply.writeInt(_173arg0 ? 1 : 0);
                    return true;
                case 174:
                    data.enforceInterface(descriptor);
                    Message _174arg0;
                    if (0 != data.readInt()) {
                        _174arg0 = (Message)Message.CREATOR.createFromParcel(data);
                    } else {
                        _174arg0 = null;
                    }

                    String _174arg1 = data.readString();
                    String _174arg2 = data.readString();
                    String[] _174arg3 = data.createStringArray();
                    SendMessageOption _174arg4;
                    if (0 != data.readInt()) {
                        _174arg4 = (SendMessageOption)SendMessageOption.CREATOR.createFromParcel(data);
                    } else {
                        _174arg4 = null;
                    }

                    boolean _174arg5 = 0 != data.readInt();
                    ISendMessageCallback _174arg6 = io.rong.imlib.ISendMessageCallback.Stub.asInterface(data.readStrongBinder());
                    this.sendRTCDirectionalMessage(_174arg0, _174arg1, _174arg2, _174arg3, _174arg4, _174arg5, _174arg6);
                    reply.writeNoException();
                    return true;
                case 175:
                    data.enforceInterface(descriptor);
                    String _175arg0 = data.readString();
                    String _175arg1 = data.readString();
                    String _175arg2 = data.readString();
                    boolean _175arg3 = 0 != data.readInt();
                    String _175arg4 = data.readString();
                    boolean _175arg5 = 0 != data.readInt();
                    boolean _175arg6 = 0 != data.readInt();
                    IOperationCallback _175arg7 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.setChatRoomEntry(_175arg0, _175arg1, _175arg2, _175arg3, _175arg4, _175arg5, _175arg6, _175arg7);
                    reply.writeNoException();
                    return true;
                case 176:
                    data.enforceInterface(descriptor);
                    String _176arg0 = data.readString();
                    String _176arg1 = data.readString();
                    IStringCallback _176arg2 = io.rong.imlib.IStringCallback.Stub.asInterface(data.readStrongBinder());
                    this.getChatRoomEntry(_176arg0, _176arg1, _176arg2);
                    reply.writeNoException();
                    return true;
                case 177:
                    data.enforceInterface(descriptor);
                    String _177arg0 = data.readString();
                    IDataByBatchListener _177arg1 = io.rong.imlib.IDataByBatchListener.Stub.asInterface(data.readStrongBinder());
                    this.getAllChatRoomEntries(_177arg0, _177arg1);
                    reply.writeNoException();
                    return true;
                case 178:
                    data.enforceInterface(descriptor);
                    String _178arg0 = data.readString();
                    String _178arg1 = data.readString();
                    String _178arg2 = data.readString();
                    boolean _178arg3 = 0 != data.readInt();
                    String _178arg4 = data.readString();
                    boolean _178arg5 = 0 != data.readInt();
                    boolean _178arg6 = 0 != data.readInt();
                    IOperationCallback _178arg7 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.deleteChatRoomEntry(_178arg0, _178arg1, _178arg2, _178arg3, _178arg4, _178arg5, _178arg6, _178arg7);
                    reply.writeNoException();
                    return true;
                case 179:
                    data.enforceInterface(descriptor);
                    INaviContentUpdateCallBack _179arg0 = io.rong.imlib.INaviContentUpdateCallBack.Stub.asInterface(data.readStrongBinder());
                    this.setNaviContentUpdateListener(_179arg0);
                    reply.writeNoException();
                    return true;
                case 180:
                    data.enforceInterface(descriptor);
                    String _180arg0 = this.getUploadLogConfigInfo();
                    reply.writeNoException();
                    reply.writeString(_180arg0);
                    return true;
                case 181:
                    data.enforceInterface(descriptor);
                    String _181arg0 = this.getOffLineLogServer();
                    reply.writeNoException();
                    reply.writeString(_181arg0);
                    return true;
                case 182:
                    data.enforceInterface(descriptor);
                    boolean _182arg0 = 0 != data.readInt();
                    this.notifyAppBackgroundChanged(_182arg0);
                    reply.writeNoException();
                    return true;
                case 183:
                    data.enforceInterface(descriptor);
                    String _183arg0 = data.readString();
                    RTCStatusDate[] _183arg1 = (RTCStatusDate[])data.createTypedArray(RTCStatusDate.CREATOR);
                    String _183arg2 = data.readString();
                    RTCStatusDate[] _183arg3 = (RTCStatusDate[])data.createTypedArray(RTCStatusDate.CREATOR);
                    IOperationCallback _183arg4 = io.rong.imlib.IOperationCallback.Stub.asInterface(data.readStrongBinder());
                    this.rtcSetUserResource(_183arg0, _183arg1, _183arg2, _183arg3, _183arg4);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(descriptor);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        public static boolean setDefaultImpl(IHandler impl) {
            if (IHandler.Stub.Proxy.sDefaultImpl == null && impl != null) {
                IHandler.Stub.Proxy.sDefaultImpl = impl;
                return true;
            } else {
                return false;
            }
        }

        public static IHandler getDefaultImpl() {
            return IHandler.Stub.Proxy.sDefaultImpl;
        }

        private static class Proxy implements IHandler {
            private IBinder mRemote;
            public static IHandler sDefaultImpl;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return "io.rong.imlib.IHandler";
            }

            public void initAppendixModule() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().initAppendixModule();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void connect(String token, boolean isReconnect, boolean inForeground, IConnectStringCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(token);
                    _data.writeInt(isReconnect ? 1 : 0);
                    _data.writeInt(inForeground ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, (Parcel)null, 1);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().connect(token, isReconnect, inForeground, callback);
                        return;
                    }
                } finally {
                    _data.recycle();
                }

            }

            public void disconnect(boolean isReceivePush) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(isReceivePush ? 1 : 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().disconnect(isReceivePush);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void registerMessageType(String className) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(className);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().registerMessageType(className);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void registerMessageTypes(List<String> classNameList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStringList(classNameList);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().registerMessageTypes(classNameList);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public int getTotalUnreadCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        int var5 = IHandler.Stub.getDefaultImpl().getTotalUnreadCount();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public int getUnreadCountByConversation(Conversation[] conversations) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int var6;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeTypedArray(conversations, 0);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        int _result = _reply.readInt();
                        return _result;
                    }

                    var6 = IHandler.Stub.getDefaultImpl().getUnreadCountByConversation(conversations);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var6;
            }

            public int getUnreadCount(int[] types) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        int var6 = IHandler.Stub.getDefaultImpl().getUnreadCount(types);
                        return var6;
                    }

                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public int getUnreadCountWithDND(int[] types, boolean withDND) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    _data.writeInt(withDND ? 1 : 0);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        int _result = _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().getUnreadCountWithDND(types, withDND);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public int getUnreadCountById(int type, String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        int var7 = IHandler.Stub.getDefaultImpl().getUnreadCountById(type, targetId);
                        return var7;
                    }

                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setOnReceiveMessageListener(OnReceiveMessageListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setOnReceiveMessageListener(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setConnectionStatusListener(IConnectionStatusListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setConnectionStatusListener(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setIpcConnectTimeOut() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setIpcConnectTimeOut();
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void initIpcConnectStatus(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().initIpcConnectStatus(status);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public Message getMessage(int messageId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Message var6;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(messageId);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        Message _result;
                        if (0 != _reply.readInt()) {
                            _result = (Message)Message.CREATOR.createFromParcel(_reply);
                        } else {
                            _result = null;
                        }

                        return _result;
                    }

                    var6 = IHandler.Stub.getDefaultImpl().getMessage(messageId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var6;
            }

            public Message insertMessage(Message message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Message _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        Message var6 = IHandler.Stub.getDefaultImpl().insertMessage(message);
                        return var6;
                    }

                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Message)Message.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Message insertSettingMessage(Message message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Message _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        Message var6 = IHandler.Stub.getDefaultImpl().insertSettingMessage(message);
                        return var6;
                    }

                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Message)Message.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void sendMessage(Message message, String pushContent, String pushData, ISendMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().sendMessage(message, pushContent, pushData, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void sendMessageOption(Message message, String pushContent, String pushData, SendMessageOption option, ISendMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    if (option != null) {
                        _data.writeInt(1);
                        option.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().sendMessageOption(message, pushContent, pushData, option, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void sendDirectionalMessage(Message message, String pushContent, String pushData, String[] userIds, ISendMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    _data.writeStringArray(userIds);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().sendDirectionalMessage(message, pushContent, pushData, userIds, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void sendMediaMessage(Message message, String pushContent, String pushData, ISendMediaMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().sendMediaMessage(message, pushContent, pushData, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void sendDirectionalMediaMessage(Message message, String[] userIds, String pushContent, String pushData, ISendMediaMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStringArray(userIds);
                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().sendDirectionalMediaMessage(message, userIds, pushContent, pushData, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void sendLocationMessage(Message message, String pushContent, String pushData, ISendMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().sendLocationMessage(message, pushContent, pushData, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public List<Message> getNewestMessages(Conversation conversation, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                ArrayList _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeInt(count);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        List var7 = IHandler.Stub.getDefaultImpl().getNewestMessages(conversation, count);
                        return var7;
                    }

                    _reply.readException();
                    _result = _reply.createTypedArrayList(Message.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public List<Message> getOlderMessages(Conversation conversation, long flagId, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var9;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeLong(flagId);
                    _data.writeInt(count);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Message.CREATOR);
                        return _result;
                    }

                    var9 = IHandler.Stub.getDefaultImpl().getOlderMessages(conversation, flagId, count);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var9;
            }

            public void getOlderMessagesOneWay(Conversation conversation, long flagId, int count, OnGetHistoryMessagesCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeLong(flagId);
                    _data.writeInt(count);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(26, _data, (Parcel)null, 1);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getOlderMessagesOneWay(conversation, flagId, count, callback);
                } finally {
                    _data.recycle();
                }

            }

            public void getRemoteHistoryMessages(Conversation conversation, long dataTime, int count, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeLong(dataTime);
                    _data.writeInt(count);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getRemoteHistoryMessages(conversation, dataTime, count, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getRemoteHistoryMessagesOption(Conversation conversation, RemoteHistoryMsgOption remoteHistoryMsgOption, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    if (remoteHistoryMsgOption != null) {
                        _data.writeInt(1);
                        remoteHistoryMsgOption.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getRemoteHistoryMessagesOption(conversation, remoteHistoryMsgOption, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void cleanRemoteHistoryMessages(Conversation conversation, long recordTime, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeLong(recordTime);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().cleanRemoteHistoryMessages(conversation, recordTime, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void cleanHistoryMessages(Conversation conversation, long recordTime, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeLong(recordTime);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().cleanHistoryMessages(conversation, recordTime, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getChatroomHistoryMessages(String targetId, long recordTime, int count, int order, IChatRoomHistoryMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeLong(recordTime);
                    _data.writeInt(count);
                    _data.writeInt(order);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getChatroomHistoryMessages(targetId, recordTime, count, order, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getUserStatus(String userId, IGetUserStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(userId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getUserStatus(userId, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setUserStatus(int status, ISetUserStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(status);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setUserStatus(status, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void subscribeStatus(List<String> users, IIntegerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStringList(users);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().subscribeStatus(users, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setSubscribeStatusListener(ISubscribeUserStatusCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setSubscribeStatusListener(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setPushSetting(int key, String value, ISetPushSettingCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(key);
                    _data.writeString(value);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setPushSetting(key, value, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getPushSetting(int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(key);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        String var6 = IHandler.Stub.getDefaultImpl().getPushSetting(key);
                        return var6;
                    }

                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public List<Message> getOlderMessagesByObjectName(Conversation conversation, String objectName, long flagId, int count, boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var11;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(objectName);
                    _data.writeLong(flagId);
                    _data.writeInt(count);
                    _data.writeInt(flag ? 1 : 0);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Message.CREATOR);
                        return _result;
                    }

                    var11 = IHandler.Stub.getDefaultImpl().getOlderMessagesByObjectName(conversation, objectName, flagId, count, flag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var11;
            }

            public List<Message> getOlderMessagesByObjectNames(Conversation conversation, List<String> objectNames, long timestamp, int count, boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                ArrayList _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStringList(objectNames);
                    _data.writeLong(timestamp);
                    _data.writeInt(count);
                    _data.writeInt(flag ? 1 : 0);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        List var11 = IHandler.Stub.getDefaultImpl().getOlderMessagesByObjectNames(conversation, objectNames, timestamp, count, flag);
                        return var11;
                    }

                    _reply.readException();
                    _result = _reply.createTypedArrayList(Message.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean deleteMessage(int[] ids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(ids);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().deleteMessage(ids);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void deleteMessages(int conversationType, String targetId, Message[] messages, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(conversationType);
                    _data.writeString(targetId);
                    _data.writeTypedArray(messages, 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().deleteMessages(conversationType, targetId, messages, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean deleteConversationMessage(int conversationType, String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(conversationType);
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var7 = IHandler.Stub.getDefaultImpl().deleteConversationMessage(conversationType, targetId);
                        return var7;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean clearMessages(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().clearMessages(conversation);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean clearMessagesUnreadStatus(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().clearMessagesUnreadStatus(conversation);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean setMessageExtra(int messageId, String values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(messageId);
                    _data.writeString(values);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().setMessageExtra(messageId, values);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public boolean setMessageReceivedStatus(int messageId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(messageId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().setMessageReceivedStatus(messageId, status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public boolean setMessageSentStatus(int messageId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(messageId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().setMessageSentStatus(messageId, status);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public Message getMessageByUid(String uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Message _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(uid);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        Message var6 = IHandler.Stub.getDefaultImpl().getMessageByUid(uid);
                        return var6;
                    }

                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Message)Message.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public List<Conversation> getConversationList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var5;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Conversation.CREATOR);
                        return _result;
                    }

                    var5 = IHandler.Stub.getDefaultImpl().getConversationList();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var5;
            }

            public void getConversationListByBatch(int countPerBatch, IGetConversationListWithProcessCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(countPerBatch);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getConversationListByBatch(countPerBatch, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public List<Conversation> getConversationListByType(int[] types) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var6;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Conversation.CREATOR);
                        return _result;
                    }

                    var6 = IHandler.Stub.getDefaultImpl().getConversationListByType(types);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var6;
            }

            public void getConversationListOfTypesByBatch(int[] types, int countPerBatch, IGetConversationListWithProcessCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    _data.writeInt(countPerBatch);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getConversationListOfTypesByBatch(types, countPerBatch, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public List<Conversation> getConversationListByPage(int[] types, long timeStamp, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var9;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    _data.writeLong(timeStamp);
                    _data.writeInt(count);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Conversation.CREATOR);
                        return _result;
                    }

                    var9 = IHandler.Stub.getDefaultImpl().getConversationListByPage(types, timeStamp, count);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var9;
            }

            public List<Conversation> getBlockedConversationList(int[] types) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                ArrayList _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        List var6 = IHandler.Stub.getDefaultImpl().getBlockedConversationList(types);
                        return var6;
                    }

                    _reply.readException();
                    _result = _reply.createTypedArrayList(Conversation.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public Conversation getConversation(int type, String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Conversation _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        Conversation var7 = IHandler.Stub.getDefaultImpl().getConversation(type, targetId);
                        return var7;
                    }

                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Conversation)Conversation.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean removeConversation(int type, String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().removeConversation(type, targetId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public boolean saveConversationDraft(Conversation conversation, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(content);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var7 = IHandler.Stub.getDefaultImpl().saveConversationDraft(conversation, content);
                        return var7;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public String getConversationDraft(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String var6;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        String _result = _reply.readString();
                        return _result;
                    }

                    var6 = IHandler.Stub.getDefaultImpl().getConversationDraft(conversation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var6;
            }

            public boolean cleanConversationDraft(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().cleanConversationDraft(conversation);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void getConversationNotificationStatus(int type, String targetId, ILongCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getConversationNotificationStatus(type, targetId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setConversationNotificationStatus(int type, String targetId, int status, ILongCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    _data.writeInt(status);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setConversationNotificationStatus(type, targetId, status, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean syncConversationNotificationStatus(int type, String targetId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var8 = IHandler.Stub.getDefaultImpl().syncConversationNotificationStatus(type, targetId, status);
                        return var8;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean setConversationTopStatus(int type, String targetId, boolean isTop, boolean needCreate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var9;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    _data.writeInt(isTop ? 1 : 0);
                    _data.writeInt(needCreate ? 1 : 0);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var9 = IHandler.Stub.getDefaultImpl().setConversationTopStatus(type, targetId, isTop, needCreate);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var9;
            }

            public int getConversationUnreadCount(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        int var6 = IHandler.Stub.getDefaultImpl().getConversationUnreadCount(conversation);
                        return var6;
                    }

                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean clearConversations(int[] types) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeIntArray(types);
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().clearConversations(types);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setNotificationQuietHours(String startTime, int spanMinute, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(startTime);
                    _data.writeInt(spanMinute);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setNotificationQuietHours(startTime, spanMinute, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void removeNotificationQuietHours(IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().removeNotificationQuietHours(callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getNotificationQuietHours(IGetNotificationQuietHoursCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getNotificationQuietHours(callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean updateConversationInfo(int type, String targetId, String title, String portait) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(type);
                    _data.writeString(targetId);
                    _data.writeString(title);
                    _data.writeString(portait);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var9 = IHandler.Stub.getDefaultImpl().updateConversationInfo(type, targetId, title, portait);
                        return var9;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean createEncryptedConversation(String targetId, RCEncryptedSession encryptedSession) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    if (encryptedSession != null) {
                        _data.writeInt(1);
                        encryptedSession.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var7 = IHandler.Stub.getDefaultImpl().createEncryptedConversation(targetId, encryptedSession);
                        return var7;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public RCEncryptedSession getEncryptedConversation(String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                RCEncryptedSession _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        RCEncryptedSession var6 = IHandler.Stub.getDefaultImpl().getEncryptedConversation(targetId);
                        return var6;
                    }

                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (RCEncryptedSession)RCEncryptedSession.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean setEncryptedConversation(String targetId, RCEncryptedSession chaInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    if (chaInfo != null) {
                        _data.writeInt(1);
                        chaInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().setEncryptedConversation(targetId, chaInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public boolean removeEncryptedConversation(String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().removeEncryptedConversation(targetId);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean clearEncryptedConversations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var5;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var5 = IHandler.Stub.getDefaultImpl().clearEncryptedConversations();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var5;
            }

            public List<RCEncryptedSession> getAllEncryptedConversations() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                ArrayList _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        List var5 = IHandler.Stub.getDefaultImpl().getAllEncryptedConversations();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.createTypedArrayList(RCEncryptedSession.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void getDiscussion(String id, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getDiscussion(id, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setDiscussionName(String id, String name, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeString(name);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setDiscussionName(id, name, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void createDiscussion(String name, List<String> userIds, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(name);
                    _data.writeStringList(userIds);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().createDiscussion(name, userIds, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void addMemberToDiscussion(String id, List<String> userIds, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeStringList(userIds);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().addMemberToDiscussion(id, userIds, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void removeDiscussionMember(String id, String userId, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeString(userId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().removeDiscussionMember(id, userId, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void quitDiscussion(String id, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().quitDiscussion(id, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void syncGroup(List<Group> groups, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeTypedList(groups);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().syncGroup(groups, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void joinGroup(String id, String name, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeString(name);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().joinGroup(id, name, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void quitGroup(String id, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().quitGroup(id, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getChatRoomInfo(String id, int count, int type, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeInt(count);
                    _data.writeInt(type);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getChatRoomInfo(id, count, type, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void reJoinChatRoom(String id, int defMessageCount, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeInt(defMessageCount);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().reJoinChatRoom(id, defMessageCount, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void joinChatRoom(String id, int defMessageCount, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeInt(defMessageCount);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(87, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().joinChatRoom(id, defMessageCount, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void joinExistChatRoom(String id, int defMessageCount, IOperationCallback callback, boolean keepMsg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeInt(defMessageCount);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(keepMsg ? 1 : 0);
                    boolean _status = this.mRemote.transact(88, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().joinExistChatRoom(id, defMessageCount, callback, keepMsg);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void quitChatRoom(String id, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(id);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(89, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().quitChatRoom(id, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void searchPublicService(String keyWords, int businessType, int searchType, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(keyWords);
                    _data.writeInt(businessType);
                    _data.writeInt(searchType);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(90, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().searchPublicService(keyWords, businessType, searchType, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void subscribePublicService(String targetId, int publicServiceType, boolean subscribe, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(publicServiceType);
                    _data.writeInt(subscribe ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(91, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().subscribePublicService(targetId, publicServiceType, subscribe, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getPublicServiceProfile(String targetId, int publicServiceType, IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(publicServiceType);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(92, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getPublicServiceProfile(targetId, publicServiceType, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getPublicServiceList(IResultCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(93, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getPublicServiceList(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void uploadMedia(Message message, IUploadCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(94, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().uploadMedia(message, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void downloadMedia(Conversation conversation, int mediaType, String imageUrl, IDownloadMediaCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeInt(mediaType);
                    _data.writeString(imageUrl);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(95, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().downloadMedia(conversation, mediaType, imageUrl, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void downloadMediaMessage(Message message, IDownloadMediaMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(96, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().downloadMediaMessage(message, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void downloadMediaFile(String uid, String fileUrl, String fileName, String path, IDownloadMediaFileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(uid);
                    _data.writeString(fileUrl);
                    _data.writeString(fileName);
                    _data.writeString(path);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(97, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().downloadMediaFile(uid, fileUrl, fileName, path, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void cancelTransferMediaMessage(Message message, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(98, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().cancelTransferMediaMessage(message, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void pauseTransferMediaMessage(Message message, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(99, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().pauseTransferMediaMessage(message, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void pauseTransferMediaFile(String tag, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(tag);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(100, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().pauseTransferMediaFile(tag, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean getFileDownloadingStatus(String uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(uid);
                    boolean _status = this.mRemote.transact(101, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().getFileDownloadingStatus(uid);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean supportResumeBrokenTransfer(String url) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(url);
                    boolean _status = this.mRemote.transact(102, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var6 = IHandler.Stub.getDefaultImpl().supportResumeBrokenTransfer(url);
                        return var6;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void cancelAllTransferMediaMessage(IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(103, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().cancelAllTransferMediaMessage(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public long getDeltaTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                long _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(104, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        long var6 = IHandler.Stub.getDefaultImpl().getDeltaTime();
                        return var6;
                    }

                    _reply.readException();
                    _result = _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setDiscussionInviteStatus(String targetId, int status, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(status);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(105, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setDiscussionInviteStatus(targetId, status, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void recallMessage(String objectName, byte[] content, String pushContent, int messageId, String targetId, int conversationType, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(objectName);
                    _data.writeByteArray(content);
                    _data.writeString(pushContent);
                    _data.writeInt(messageId);
                    _data.writeString(targetId);
                    _data.writeInt(conversationType);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(106, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().recallMessage(objectName, content, pushContent, messageId, targetId, conversationType, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void addToBlacklist(String userId, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(userId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(107, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().addToBlacklist(userId, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void removeFromBlacklist(String userId, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(userId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(108, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().removeFromBlacklist(userId, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getTextMessageDraft(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(109, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        String var6 = IHandler.Stub.getDefaultImpl().getTextMessageDraft(conversation);
                        return var6;
                    }

                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean saveTextMessageDraft(Conversation conversation, String content) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(content);
                    boolean _status = this.mRemote.transact(110, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().saveTextMessageDraft(conversation, content);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public boolean clearTextMessageDraft(Conversation conversation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var6;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (conversation != null) {
                        _data.writeInt(1);
                        conversation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    boolean _status = this.mRemote.transact(111, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var6 = IHandler.Stub.getDefaultImpl().clearTextMessageDraft(conversation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var6;
            }

            public void getBlacklist(IStringCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(112, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getBlacklist(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getBlacklistStatus(String userId, IIntegerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(userId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(113, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getBlacklistStatus(userId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setUserData(UserData userData, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (userData != null) {
                        _data.writeInt(1);
                        userData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(114, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setUserData(userData, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean updateMessageReceiptStatus(String targetId, int categoryId, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(categoryId);
                    _data.writeLong(timestamp);
                    boolean _status = this.mRemote.transact(115, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var9 = IHandler.Stub.getDefaultImpl().updateMessageReceiptStatus(targetId, categoryId, timestamp);
                        return var9;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean clearUnreadByReceipt(int conversationType, String targetId, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(conversationType);
                    _data.writeString(targetId);
                    _data.writeLong(timestamp);
                    boolean _status = this.mRemote.transact(116, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var9 = IHandler.Stub.getDefaultImpl().clearUnreadByReceipt(conversationType, targetId, timestamp);
                        return var9;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public long getSendTimeByMessageId(int messageId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                long _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(messageId);
                    boolean _status = this.mRemote.transact(117, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        long var7 = IHandler.Stub.getDefaultImpl().getSendTimeByMessageId(messageId);
                        return var7;
                    }

                    _reply.readException();
                    _result = _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void getVoIPKey(int engineType, String channelName, String extra, IStringCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(engineType);
                    _data.writeString(channelName);
                    _data.writeString(extra);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(118, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getVoIPKey(engineType, channelName, extra, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getVoIPCallInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String var5;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(119, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        String _result = _reply.readString();
                        return _result;
                    }

                    var5 = IHandler.Stub.getDefaultImpl().getVoIPCallInfo();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var5;
            }

            public String getCurrentUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(120, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        String var5 = IHandler.Stub.getDefaultImpl().getCurrentUserId();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setServerInfo(String naviServer, String fileServer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(naviServer);
                    _data.writeString(fileServer);
                    boolean _status = this.mRemote.transact(121, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setServerInfo(naviServer, fileServer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean setMessageContent(int messageId, byte[] messageContent, String objectName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var8;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(messageId);
                    _data.writeByteArray(messageContent);
                    _data.writeString(objectName);
                    boolean _status = this.mRemote.transact(122, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var8 = IHandler.Stub.getDefaultImpl().setMessageContent(messageId, messageContent, objectName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var8;
            }

            public List<Message> getUnreadMentionedMessages(int conversationType, String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                ArrayList _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(conversationType);
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(123, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        List var7 = IHandler.Stub.getDefaultImpl().getUnreadMentionedMessages(conversationType, targetId);
                        return var7;
                    }

                    _reply.readException();
                    _result = _reply.createTypedArrayList(Message.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean updateReadReceiptRequestInfo(String msgUId, String info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var7;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(msgUId);
                    _data.writeString(info);
                    boolean _status = this.mRemote.transact(124, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var7 = IHandler.Stub.getDefaultImpl().updateReadReceiptRequestInfo(msgUId, info);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var7;
            }

            public void registerCmdMsgType(String objName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(objName);
                    boolean _status = this.mRemote.transact(125, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().registerCmdMsgType(objName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void registerCmdMsgTypes(List<String> objNameList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStringList(objNameList);
                    boolean _status = this.mRemote.transact(126, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().registerCmdMsgTypes(objNameList);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void registerDeleteMessageType(List<String> objNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStringList(objNames);
                    boolean _status = this.mRemote.transact(127, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().registerDeleteMessageType(objNames);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public List<Message> searchMessages(String targetId, int conversationType, String keyword, int count, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                ArrayList _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(conversationType);
                    _data.writeString(keyword);
                    _data.writeInt(count);
                    _data.writeLong(timestamp);
                    boolean _status = this.mRemote.transact(128, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        List var11 = IHandler.Stub.getDefaultImpl().searchMessages(targetId, conversationType, keyword, count, timestamp);
                        return var11;
                    }

                    _reply.readException();
                    _result = _reply.createTypedArrayList(Message.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public List<Message> searchMessagesByUser(String targetId, int conversationType, String userId, int count, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var11;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(conversationType);
                    _data.writeString(userId);
                    _data.writeInt(count);
                    _data.writeLong(timestamp);
                    boolean _status = this.mRemote.transact(129, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Message.CREATOR);
                        return _result;
                    }

                    var11 = IHandler.Stub.getDefaultImpl().searchMessagesByUser(targetId, conversationType, userId, count, timestamp);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var11;
            }

            public List<SearchConversationResult> searchConversations(String keyword, int[] conversationTypes, String[] objName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var8;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(keyword);
                    _data.writeIntArray(conversationTypes);
                    _data.writeStringArray(objName);
                    boolean _status = this.mRemote.transact(130, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(SearchConversationResult.CREATOR);
                        return _result;
                    }

                    var8 = IHandler.Stub.getDefaultImpl().searchConversations(keyword, conversationTypes, objName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var8;
            }

            public List<Message> getMatchedMessages(String targetId, int conversationType, long timestamp, int before, int after) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                List var11;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(targetId);
                    _data.writeInt(conversationType);
                    _data.writeLong(timestamp);
                    _data.writeInt(before);
                    _data.writeInt(after);
                    boolean _status = this.mRemote.transact(131, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        ArrayList _result = _reply.createTypedArrayList(Message.CREATOR);
                        return _result;
                    }

                    var11 = IHandler.Stub.getDefaultImpl().getMatchedMessages(targetId, conversationType, timestamp, before, after);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var11;
            }

            public void getVendorToken(IStringCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(132, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getVendorToken(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void writeFwLog(int level, String type, String tag, String metaJson, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(level);
                    _data.writeString(type);
                    _data.writeString(tag);
                    _data.writeString(metaJson);
                    _data.writeLong(timestamp);
                    boolean _status = this.mRemote.transact(133, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().writeFwLog(level, type, tag, metaJson, timestamp);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public long getNaviCachedTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                long var6;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(134, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        long _result = _reply.readLong();
                        return _result;
                    }

                    var6 = IHandler.Stub.getDefaultImpl().getNaviCachedTime();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var6;
            }

            public boolean getJoinMultiChatRoomEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(135, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var5 = IHandler.Stub.getDefaultImpl().getJoinMultiChatRoomEnable();
                        return var5;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public String getOfflineMessageDuration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(136, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        String var5 = IHandler.Stub.getDefaultImpl().getOfflineMessageDuration();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setOfflineMessageDuration(String duration, ILongCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(duration);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(137, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setOfflineMessageDuration(duration, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void switchAppKey(String appKey, String deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(appKey);
                    _data.writeString(deviceId);
                    boolean _status = this.mRemote.transact(138, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().switchAppKey(appKey, deviceId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public Message getTheFirstUnreadMessage(int conversationType, String targetId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                Message _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(conversationType);
                    _data.writeString(targetId);
                    boolean _status = this.mRemote.transact(139, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        Message var7 = IHandler.Stub.getDefaultImpl().getTheFirstUnreadMessage(conversationType, targetId);
                        return var7;
                    }

                    _reply.readException();
                    if (0 != _reply.readInt()) {
                        _result = (Message)Message.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean setMessageReadTime(long messageId, long timestamp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeLong(messageId);
                    _data.writeLong(timestamp);
                    boolean _status = this.mRemote.transact(140, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var9 = IHandler.Stub.getDefaultImpl().setMessageReadTime(messageId, timestamp);
                        return var9;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void replenishPing(boolean background) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(background ? 1 : 0);
                    boolean _status = this.mRemote.transact(141, _data, (Parcel)null, 1);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().replenishPing(background);
                } finally {
                    _data.recycle();
                }

            }

            public void sendPing() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(142, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().sendPing();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setUserPolicy(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(143, _data, (Parcel)null, 1);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setUserPolicy(enable);
                } finally {
                    _data.recycle();
                }

            }

            public void setReconnectKickEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(enable ? 1 : 0);
                    boolean _status = this.mRemote.transact(144, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setReconnectKickEnable(enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public int getVideoLimitTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(145, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        int var5 = IHandler.Stub.getDefaultImpl().getVideoLimitTime();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public int getGIFLimitSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                int _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(146, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        int var5 = IHandler.Stub.getDefaultImpl().getGIFLimitSize();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void setUserProfileListener(UserProfileSettingListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(147, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setUserProfileListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setConversationStatusListener(ConversationStatusListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(148, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setConversationStatusListener(listener);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void initHttpDns() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(149, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().initHttpDns();
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getRTCProfile() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(150, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        String var5 = IHandler.Stub.getDefaultImpl().getRTCProfile();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void updateVoIPCallInfo(String rtcProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(rtcProfile);
                    boolean _status = this.mRemote.transact(151, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().updateVoIPCallInfo(rtcProfile);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void exitRTCRoom(String roomId, IOperationCallback callbackl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeStrongBinder(callbackl != null ? callbackl.asBinder() : null);
                    boolean _status = this.mRemote.transact(152, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().exitRTCRoom(roomId, callbackl);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getRTCUsers(String roomId, int order, RTCDataListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(order);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(153, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().getRTCUsers(roomId, order, listener);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getRTCUserData(String roomId, int order, RTCDataListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(order);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(154, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getRTCUserData(roomId, order, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void sendRTCPing(String roomId, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(155, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().sendRTCPing(roomId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean useRTCOnly() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(156, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var5 = IHandler.Stub.getDefaultImpl().useRTCOnly();
                        return var5;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public void rtcPutInnerData(String roomId, int type, String key, String value, String objectName, String content, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeString(objectName);
                    _data.writeString(content);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(157, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().rtcPutInnerData(roomId, type, key, value, objectName, content, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void rtcPutOuterData(String roomId, int type, String key, String value, String objectName, String content, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeString(objectName);
                    _data.writeString(content);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(158, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().rtcPutOuterData(roomId, type, key, value, objectName, content, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void rtcDeleteInnerData(String roomId, int type, String[] keys, String objectName, String content, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeStringArray(keys);
                    _data.writeString(objectName);
                    _data.writeString(content);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(159, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().rtcDeleteInnerData(roomId, type, keys, objectName, content, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void rtcDeleteOuterData(String roomId, int type, String[] keys, String objectName, String content, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeStringArray(keys);
                    _data.writeString(objectName);
                    _data.writeString(content);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(160, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().rtcDeleteOuterData(roomId, type, keys, objectName, content, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void rtcGetInnerData(String roomId, int type, String[] keys, IRtcIODataListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeStringArray(keys);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(161, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().rtcGetInnerData(roomId, type, keys, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void rtcGetOuterData(String roomId, int type, String[] keys, IRtcIODataListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeStringArray(keys);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(162, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().rtcGetOuterData(roomId, type, keys, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void joinRTCRoomAndGetData(String roomId, int roomType, int broadcastType, IRTCJoinRoomCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(roomType);
                    _data.writeInt(broadcastType);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(163, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().joinRTCRoomAndGetData(roomId, roomType, broadcastType, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getRTCConfig(String model, String osVersion, long timestamp, String sdkVersion, IRTCConfigCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(model);
                    _data.writeString(osVersion);
                    _data.writeLong(timestamp);
                    _data.writeString(sdkVersion);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(164, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getRTCConfig(model, osVersion, timestamp, sdkVersion, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getRTCToken(String roomId, int roomType, int broadcastType, IStringCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(roomType);
                    _data.writeInt(broadcastType);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(165, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getRTCToken(roomId, roomType, broadcastType, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setRLogOtherProgressCallback(IRLogOtherProgressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(166, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setRLogOtherProgressCallback(callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setSavePath(String savePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(savePath);
                    boolean _status = this.mRemote.transact(167, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setSavePath(savePath);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setRTCUserData(String roomId, String state, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeString(state);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(168, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setRTCUserData(roomId, state, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void solveServerHosts(String server, ISolveServerHostsCallBack callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(server);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(169, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().solveServerHosts(server, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setRTCUserDatas(String roomId, int type, Map data, String objectName, String content, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeInt(type);
                    _data.writeMap(data);
                    _data.writeString(objectName);
                    _data.writeString(content);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(170, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setRTCUserDatas(roomId, type, data, objectName, content, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getRTCUserDatas(String roomId, String[] userIds, RTCDataListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeStringArray(userIds);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(171, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getRTCUserDatas(roomId, userIds, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public boolean isPhrasesEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(172, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        boolean var5 = IHandler.Stub.getDefaultImpl().isPhrasesEnabled();
                        return var5;
                    }

                    _reply.readException();
                    _result = 0 != _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public boolean isDnsEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                boolean var5;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(173, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        boolean _result = 0 != _reply.readInt();
                        return _result;
                    }

                    var5 = IHandler.Stub.getDefaultImpl().isDnsEnabled();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var5;
            }

            public void sendRTCDirectionalMessage(Message message, String pushContent, String pushData, String[] userIds, SendMessageOption option, boolean isFilterBlackList, ISendMessageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    if (message != null) {
                        _data.writeInt(1);
                        message.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeString(pushContent);
                    _data.writeString(pushData);
                    _data.writeStringArray(userIds);
                    if (option != null) {
                        _data.writeInt(1);
                        option.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }

                    _data.writeInt(isFilterBlackList ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(174, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().sendRTCDirectionalMessage(message, pushContent, pushData, userIds, option, isFilterBlackList, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setChatRoomEntry(String key, String value, String chatRoomId, boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeString(chatRoomId);
                    _data.writeInt(sendNotification ? 1 : 0);
                    _data.writeString(notificationExtra);
                    _data.writeInt(autoDelete ? 1 : 0);
                    _data.writeInt(isOverWrite ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(175, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().setChatRoomEntry(key, value, chatRoomId, sendNotification, notificationExtra, autoDelete, isOverWrite, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getChatRoomEntry(String chatRoomId, String key, IStringCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(chatRoomId);
                    _data.writeString(key);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(176, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getChatRoomEntry(chatRoomId, key, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void getAllChatRoomEntries(String chatRoomId, IDataByBatchListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(chatRoomId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(177, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().getAllChatRoomEntries(chatRoomId, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void deleteChatRoomEntry(String key, String value, String chatRoomId, boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeString(chatRoomId);
                    _data.writeInt(sendNotification ? 1 : 0);
                    _data.writeString(notificationExtra);
                    _data.writeInt(autoDelete ? 1 : 0);
                    _data.writeInt(isOverWrite ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(178, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        return;
                    }

                    IHandler.Stub.getDefaultImpl().deleteChatRoomEntry(key, value, chatRoomId, sendNotification, notificationExtra, autoDelete, isOverWrite, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void setNaviContentUpdateListener(INaviContentUpdateCallBack callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(179, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().setNaviContentUpdateListener(callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public String getUploadLogConfigInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String _result;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(180, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        String var5 = IHandler.Stub.getDefaultImpl().getUploadLogConfigInfo();
                        return var5;
                    }

                    _reply.readException();
                    _result = _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return _result;
            }

            public String getOffLineLogServer() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                String var5;
                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    boolean _status = this.mRemote.transact(181, _data, _reply, 0);
                    if (_status || IHandler.Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        String _result = _reply.readString();
                        return _result;
                    }

                    var5 = IHandler.Stub.getDefaultImpl().getOffLineLogServer();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

                return var5;
            }

            public void notifyAppBackgroundChanged(boolean isInBackground) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeInt(isInBackground ? 1 : 0);
                    boolean _status = this.mRemote.transact(182, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().notifyAppBackgroundChanged(isInBackground);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }

            public void rtcSetUserResource(String roomId, RTCStatusDate[] kv, String objectName, RTCStatusDate[] content, IOperationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();

                try {
                    _data.writeInterfaceToken("io.rong.imlib.IHandler");
                    _data.writeString(roomId);
                    _data.writeTypedArray(kv, 0);
                    _data.writeString(objectName);
                    _data.writeTypedArray(content, 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(183, _data, _reply, 0);
                    if (!_status && IHandler.Stub.getDefaultImpl() != null) {
                        IHandler.Stub.getDefaultImpl().rtcSetUserResource(roomId, kv, objectName, content, callback);
                        return;
                    }

                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }

            }
        }
    }

    public static class Default implements IHandler {
        public Default() {
        }

        public void initAppendixModule() throws RemoteException {
        }

        public void connect(String token, boolean isReconnect, boolean inForeground, IConnectStringCallback callback) throws RemoteException {
        }

        public void disconnect(boolean isReceivePush) throws RemoteException {
        }

        public void registerMessageType(String className) throws RemoteException {
        }

        public void registerMessageTypes(List<String> classNameList) throws RemoteException {
        }

        public int getTotalUnreadCount() throws RemoteException {
            return 0;
        }

        public int getUnreadCountByConversation(Conversation[] conversations) throws RemoteException {
            return 0;
        }

        public int getUnreadCount(int[] types) throws RemoteException {
            return 0;
        }

        public int getUnreadCountWithDND(int[] types, boolean withDND) throws RemoteException {
            return 0;
        }

        public int getUnreadCountById(int type, String targetId) throws RemoteException {
            return 0;
        }

        public void setOnReceiveMessageListener(OnReceiveMessageListener callback) throws RemoteException {
        }

        public void setConnectionStatusListener(IConnectionStatusListener callback) throws RemoteException {
        }

        public void setIpcConnectTimeOut() throws RemoteException {
        }

        public void initIpcConnectStatus(int status) throws RemoteException {
        }

        public Message getMessage(int messageId) throws RemoteException {
            return null;
        }

        public Message insertMessage(Message message) throws RemoteException {
            return null;
        }

        public Message insertSettingMessage(Message message) throws RemoteException {
            return null;
        }

        public void sendMessage(Message message, String pushContent, String pushData, ISendMessageCallback callback) throws RemoteException {
        }

        public void sendMessageOption(Message message, String pushContent, String pushData, SendMessageOption option, ISendMessageCallback callback) throws RemoteException {
        }

        public void sendDirectionalMessage(Message message, String pushContent, String pushData, String[] userIds, ISendMessageCallback callback) throws RemoteException {
        }

        public void sendMediaMessage(Message message, String pushContent, String pushData, ISendMediaMessageCallback callback) throws RemoteException {
        }

        public void sendDirectionalMediaMessage(Message message, String[] userIds, String pushContent, String pushData, ISendMediaMessageCallback callback) throws RemoteException {
        }

        public void sendLocationMessage(Message message, String pushContent, String pushData, ISendMessageCallback callback) throws RemoteException {
        }

        public List<Message> getNewestMessages(Conversation conversation, int count) throws RemoteException {
            return null;
        }

        public List<Message> getOlderMessages(Conversation conversation, long flagId, int count) throws RemoteException {
            return null;
        }

        public void getOlderMessagesOneWay(Conversation conversation, long flagId, int count, OnGetHistoryMessagesCallback callback) throws RemoteException {
        }

        public void getRemoteHistoryMessages(Conversation conversation, long dataTime, int count, IResultCallback callback) throws RemoteException {
        }

        public void getRemoteHistoryMessagesOption(Conversation conversation, RemoteHistoryMsgOption remoteHistoryMsgOption, IResultCallback callback) throws RemoteException {
        }

        public void cleanRemoteHistoryMessages(Conversation conversation, long recordTime, IOperationCallback callback) throws RemoteException {
        }

        public void cleanHistoryMessages(Conversation conversation, long recordTime, IOperationCallback callback) throws RemoteException {
        }

        public void getChatroomHistoryMessages(String targetId, long recordTime, int count, int order, IChatRoomHistoryMessageCallback callback) throws RemoteException {
        }

        public void getUserStatus(String userId, IGetUserStatusCallback callback) throws RemoteException {
        }

        public void setUserStatus(int status, ISetUserStatusCallback callback) throws RemoteException {
        }

        public void subscribeStatus(List<String> users, IIntegerCallback callback) throws RemoteException {
        }

        public void setSubscribeStatusListener(ISubscribeUserStatusCallback callback) throws RemoteException {
        }

        public void setPushSetting(int key, String value, ISetPushSettingCallback callback) throws RemoteException {
        }

        public String getPushSetting(int key) throws RemoteException {
            return null;
        }

        public List<Message> getOlderMessagesByObjectName(Conversation conversation, String objectName, long flagId, int count, boolean flag) throws RemoteException {
            return null;
        }

        public List<Message> getOlderMessagesByObjectNames(Conversation conversation, List<String> objectNames, long timestamp, int count, boolean flag) throws RemoteException {
            return null;
        }

        public boolean deleteMessage(int[] ids) throws RemoteException {
            return false;
        }

        public void deleteMessages(int conversationType, String targetId, Message[] messages, IOperationCallback callback) throws RemoteException {
        }

        public boolean deleteConversationMessage(int conversationType, String targetId) throws RemoteException {
            return false;
        }

        public boolean clearMessages(Conversation conversation) throws RemoteException {
            return false;
        }

        public boolean clearMessagesUnreadStatus(Conversation conversation) throws RemoteException {
            return false;
        }

        public boolean setMessageExtra(int messageId, String values) throws RemoteException {
            return false;
        }

        public boolean setMessageReceivedStatus(int messageId, int status) throws RemoteException {
            return false;
        }

        public boolean setMessageSentStatus(int messageId, int status) throws RemoteException {
            return false;
        }

        public Message getMessageByUid(String uid) throws RemoteException {
            return null;
        }

        public List<Conversation> getConversationList() throws RemoteException {
            return null;
        }

        public void getConversationListByBatch(int countPerBatch, IGetConversationListWithProcessCallback callback) throws RemoteException {
        }

        public List<Conversation> getConversationListByType(int[] types) throws RemoteException {
            return null;
        }

        public void getConversationListOfTypesByBatch(int[] types, int countPerBatch, IGetConversationListWithProcessCallback callback) throws RemoteException {
        }

        public List<Conversation> getConversationListByPage(int[] types, long timeStamp, int count) throws RemoteException {
            return null;
        }

        public List<Conversation> getBlockedConversationList(int[] types) throws RemoteException {
            return null;
        }

        public Conversation getConversation(int type, String targetId) throws RemoteException {
            return null;
        }

        public boolean removeConversation(int type, String targetId) throws RemoteException {
            return false;
        }

        public boolean saveConversationDraft(Conversation conversation, String content) throws RemoteException {
            return false;
        }

        public String getConversationDraft(Conversation conversation) throws RemoteException {
            return null;
        }

        public boolean cleanConversationDraft(Conversation conversation) throws RemoteException {
            return false;
        }

        public void getConversationNotificationStatus(int type, String targetId, ILongCallback callback) throws RemoteException {
        }

        public void setConversationNotificationStatus(int type, String targetId, int status, ILongCallback callback) throws RemoteException {
        }

        public boolean syncConversationNotificationStatus(int type, String targetId, int status) throws RemoteException {
            return false;
        }

        public boolean setConversationTopStatus(int type, String targetId, boolean isTop, boolean needCreate) throws RemoteException {
            return false;
        }

        public int getConversationUnreadCount(Conversation conversation) throws RemoteException {
            return 0;
        }

        public boolean clearConversations(int[] types) throws RemoteException {
            return false;
        }

        public void setNotificationQuietHours(String startTime, int spanMinute, IOperationCallback callback) throws RemoteException {
        }

        public void removeNotificationQuietHours(IOperationCallback callback) throws RemoteException {
        }

        public void getNotificationQuietHours(IGetNotificationQuietHoursCallback callback) throws RemoteException {
        }

        public boolean updateConversationInfo(int type, String targetId, String title, String portait) throws RemoteException {
            return false;
        }

        public boolean createEncryptedConversation(String targetId, RCEncryptedSession encryptedSession) throws RemoteException {
            return false;
        }

        public RCEncryptedSession getEncryptedConversation(String targetId) throws RemoteException {
            return null;
        }

        public boolean setEncryptedConversation(String targetId, RCEncryptedSession chaInfo) throws RemoteException {
            return false;
        }

        public boolean removeEncryptedConversation(String targetId) throws RemoteException {
            return false;
        }

        public boolean clearEncryptedConversations() throws RemoteException {
            return false;
        }

        public List<RCEncryptedSession> getAllEncryptedConversations() throws RemoteException {
            return null;
        }

        public void getDiscussion(String id, IResultCallback callback) throws RemoteException {
        }

        public void setDiscussionName(String id, String name, IOperationCallback callback) throws RemoteException {
        }

        public void createDiscussion(String name, List<String> userIds, IResultCallback callback) throws RemoteException {
        }

        public void addMemberToDiscussion(String id, List<String> userIds, IOperationCallback callback) throws RemoteException {
        }

        public void removeDiscussionMember(String id, String userId, IOperationCallback callback) throws RemoteException {
        }

        public void quitDiscussion(String id, IOperationCallback callback) throws RemoteException {
        }

        public void syncGroup(List<Group> groups, IOperationCallback callback) throws RemoteException {
        }

        public void joinGroup(String id, String name, IOperationCallback callback) throws RemoteException {
        }

        public void quitGroup(String id, IOperationCallback callback) throws RemoteException {
        }

        public void getChatRoomInfo(String id, int count, int type, IResultCallback callback) throws RemoteException {
        }

        public void reJoinChatRoom(String id, int defMessageCount, IOperationCallback callback) throws RemoteException {
        }

        public void joinChatRoom(String id, int defMessageCount, IOperationCallback callback) throws RemoteException {
        }

        public void joinExistChatRoom(String id, int defMessageCount, IOperationCallback callback, boolean keepMsg) throws RemoteException {
        }

        public void quitChatRoom(String id, IOperationCallback callback) throws RemoteException {
        }

        public void searchPublicService(String keyWords, int businessType, int searchType, IResultCallback callback) throws RemoteException {
        }

        public void subscribePublicService(String targetId, int publicServiceType, boolean subscribe, IOperationCallback callback) throws RemoteException {
        }

        public void getPublicServiceProfile(String targetId, int publicServiceType, IResultCallback callback) throws RemoteException {
        }

        public void getPublicServiceList(IResultCallback callback) throws RemoteException {
        }

        public void uploadMedia(Message message, IUploadCallback callback) throws RemoteException {
        }

        public void downloadMedia(Conversation conversation, int mediaType, String imageUrl, IDownloadMediaCallback callback) throws RemoteException {
        }

        public void downloadMediaMessage(Message message, IDownloadMediaMessageCallback callback) throws RemoteException {
        }

        public void downloadMediaFile(String uid, String fileUrl, String fileName, String path, IDownloadMediaFileCallback callback) throws RemoteException {
        }

        public void cancelTransferMediaMessage(Message message, IOperationCallback callback) throws RemoteException {
        }

        public void pauseTransferMediaMessage(Message message, IOperationCallback callback) throws RemoteException {
        }

        public void pauseTransferMediaFile(String tag, IOperationCallback callback) throws RemoteException {
        }

        public boolean getFileDownloadingStatus(String uid) throws RemoteException {
            return false;
        }

        public boolean supportResumeBrokenTransfer(String url) throws RemoteException {
            return false;
        }

        public void cancelAllTransferMediaMessage(IOperationCallback callback) throws RemoteException {
        }

        public long getDeltaTime() throws RemoteException {
            return 0L;
        }

        public void setDiscussionInviteStatus(String targetId, int status, IOperationCallback callback) throws RemoteException {
        }

        public void recallMessage(String objectName, byte[] content, String pushContent, int messageId, String targetId, int conversationType, IOperationCallback callback) throws RemoteException {
        }

        public void addToBlacklist(String userId, IOperationCallback callback) throws RemoteException {
        }

        public void removeFromBlacklist(String userId, IOperationCallback callback) throws RemoteException {
        }

        public String getTextMessageDraft(Conversation conversation) throws RemoteException {
            return null;
        }

        public boolean saveTextMessageDraft(Conversation conversation, String content) throws RemoteException {
            return false;
        }

        public boolean clearTextMessageDraft(Conversation conversation) throws RemoteException {
            return false;
        }

        public void getBlacklist(IStringCallback callback) throws RemoteException {
        }

        public void getBlacklistStatus(String userId, IIntegerCallback callback) throws RemoteException {
        }

        public void setUserData(UserData userData, IOperationCallback callback) throws RemoteException {
        }

        public boolean updateMessageReceiptStatus(String targetId, int categoryId, long timestamp) throws RemoteException {
            return false;
        }

        public boolean clearUnreadByReceipt(int conversationType, String targetId, long timestamp) throws RemoteException {
            return false;
        }

        public long getSendTimeByMessageId(int messageId) throws RemoteException {
            return 0L;
        }

        public void getVoIPKey(int engineType, String channelName, String extra, IStringCallback callback) throws RemoteException {
        }

        public String getVoIPCallInfo() throws RemoteException {
            return null;
        }

        public String getCurrentUserId() throws RemoteException {
            return null;
        }

        public void setServerInfo(String naviServer, String fileServer) throws RemoteException {
        }

        public boolean setMessageContent(int messageId, byte[] messageContent, String objectName) throws RemoteException {
            return false;
        }

        public List<Message> getUnreadMentionedMessages(int conversationType, String targetId) throws RemoteException {
            return null;
        }

        public boolean updateReadReceiptRequestInfo(String msgUId, String info) throws RemoteException {
            return false;
        }

        public void registerCmdMsgType(String objName) throws RemoteException {
        }

        public void registerCmdMsgTypes(List<String> objNameList) throws RemoteException {
        }

        public void registerDeleteMessageType(List<String> objNames) throws RemoteException {
        }

        public List<Message> searchMessages(String targetId, int conversationType, String keyword, int count, long timestamp) throws RemoteException {
            return null;
        }

        public List<Message> searchMessagesByUser(String targetId, int conversationType, String userId, int count, long timestamp) throws RemoteException {
            return null;
        }

        public List<SearchConversationResult> searchConversations(String keyword, int[] conversationTypes, String[] objName) throws RemoteException {
            return null;
        }

        public List<Message> getMatchedMessages(String targetId, int conversationType, long timestamp, int before, int after) throws RemoteException {
            return null;
        }

        public void getVendorToken(IStringCallback callback) throws RemoteException {
        }

        public void writeFwLog(int level, String type, String tag, String metaJson, long timestamp) throws RemoteException {
        }

        public long getNaviCachedTime() throws RemoteException {
            return 0L;
        }

        public boolean getJoinMultiChatRoomEnable() throws RemoteException {
            return false;
        }

        public String getOfflineMessageDuration() throws RemoteException {
            return null;
        }

        public void setOfflineMessageDuration(String duration, ILongCallback callback) throws RemoteException {
        }

        public void switchAppKey(String appKey, String deviceId) throws RemoteException {
        }

        public Message getTheFirstUnreadMessage(int conversationType, String targetId) throws RemoteException {
            return null;
        }

        public boolean setMessageReadTime(long messageId, long timestamp) throws RemoteException {
            return false;
        }

        public void replenishPing(boolean background) throws RemoteException {
        }

        public void sendPing() throws RemoteException {
        }

        public void setUserPolicy(boolean enable) throws RemoteException {
        }

        public void setReconnectKickEnable(boolean enable) throws RemoteException {
        }

        public int getVideoLimitTime() throws RemoteException {
            return 0;
        }

        public int getGIFLimitSize() throws RemoteException {
            return 0;
        }

        public void setUserProfileListener(UserProfileSettingListener listener) throws RemoteException {
        }

        public void setConversationStatusListener(ConversationStatusListener listener) throws RemoteException {
        }

        public void initHttpDns() throws RemoteException {
        }

        public String getRTCProfile() throws RemoteException {
            return null;
        }

        public void updateVoIPCallInfo(String rtcProfile) throws RemoteException {
        }

        public void exitRTCRoom(String roomId, IOperationCallback callbackl) throws RemoteException {
        }

        public void getRTCUsers(String roomId, int order, RTCDataListener listener) throws RemoteException {
        }

        public void getRTCUserData(String roomId, int order, RTCDataListener listener) throws RemoteException {
        }

        public void sendRTCPing(String roomId, IOperationCallback callback) throws RemoteException {
        }

        public boolean useRTCOnly() throws RemoteException {
            return false;
        }

        public void rtcPutInnerData(String roomId, int type, String key, String value, String objectName, String content, IOperationCallback callback) throws RemoteException {
        }

        public void rtcPutOuterData(String roomId, int type, String key, String value, String objectName, String content, IOperationCallback callback) throws RemoteException {
        }

        public void rtcDeleteInnerData(String roomId, int type, String[] keys, String objectName, String content, IOperationCallback callback) throws RemoteException {
        }

        public void rtcDeleteOuterData(String roomId, int type, String[] keys, String objectName, String content, IOperationCallback callback) throws RemoteException {
        }

        public void rtcGetInnerData(String roomId, int type, String[] keys, IRtcIODataListener callback) throws RemoteException {
        }

        public void rtcGetOuterData(String roomId, int type, String[] keys, IRtcIODataListener callback) throws RemoteException {
        }

        public void joinRTCRoomAndGetData(String roomId, int roomType, int broadcastType, IRTCJoinRoomCallback listener) throws RemoteException {
        }

        public void getRTCConfig(String model, String osVersion, long timestamp, String sdkVersion, IRTCConfigCallback callback) throws RemoteException {
        }

        public void getRTCToken(String roomId, int roomType, int broadcastType, IStringCallback callback) throws RemoteException {
        }

        public void setRLogOtherProgressCallback(IRLogOtherProgressCallback callback) throws RemoteException {
        }

        public void setSavePath(String savePath) throws RemoteException {
        }

        public void setRTCUserData(String roomId, String state, IOperationCallback callback) throws RemoteException {
        }

        public void solveServerHosts(String server, ISolveServerHostsCallBack callback) throws RemoteException {
        }

        public void setRTCUserDatas(String roomId, int type, Map data, String objectName, String content, IOperationCallback callback) throws RemoteException {
        }

        public void getRTCUserDatas(String roomId, String[] userIds, RTCDataListener listener) throws RemoteException {
        }

        public boolean isPhrasesEnabled() throws RemoteException {
            return false;
        }

        public boolean isDnsEnabled() throws RemoteException {
            return false;
        }

        public void sendRTCDirectionalMessage(Message message, String pushContent, String pushData, String[] userIds, SendMessageOption option, boolean isFilterBlackList, ISendMessageCallback callback) throws RemoteException {
        }

        public void setChatRoomEntry(String key, String value, String chatRoomId, boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, IOperationCallback callback) throws RemoteException {
        }

        public void getChatRoomEntry(String chatRoomId, String key, IStringCallback callback) throws RemoteException {
        }

        public void getAllChatRoomEntries(String chatRoomId, IDataByBatchListener callback) throws RemoteException {
        }

        public void deleteChatRoomEntry(String key, String value, String chatRoomId, boolean sendNotification, String notificationExtra, boolean autoDelete, boolean isOverWrite, IOperationCallback callback) throws RemoteException {
        }

        public void setNaviContentUpdateListener(INaviContentUpdateCallBack callback) throws RemoteException {
        }

        public String getUploadLogConfigInfo() throws RemoteException {
            return null;
        }

        public String getOffLineLogServer() throws RemoteException {
            return null;
        }

        public void notifyAppBackgroundChanged(boolean isInBackground) throws RemoteException {
        }

        public void rtcSetUserResource(String roomId, RTCStatusDate[] kv, String objectName, RTCStatusDate[] content, IOperationCallback callback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }
}
