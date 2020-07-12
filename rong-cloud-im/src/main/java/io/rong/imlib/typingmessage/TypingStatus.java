//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.typingmessage;

public class TypingStatus {
    private String userId;
    private String typingContentType;
    private long sentTime;

    public TypingStatus(String userId, String typingContentType, long sentTime) {
        this.setUserId(userId);
        this.setTypingContentType(typingContentType);
        this.setSentTime(sentTime);
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypingContentType() {
        return this.typingContentType;
    }

    public void setTypingContentType(String typingContentType) {
        this.typingContentType = typingContentType;
    }

    public long getSentTime() {
        return this.sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }
}
