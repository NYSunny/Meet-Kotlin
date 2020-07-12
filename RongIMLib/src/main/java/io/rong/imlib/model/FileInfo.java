//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.model;

public class FileInfo {
    private int messageId;
    private String fileName;
    private String url;
    private long length;
    private long finished;
    private boolean isStop = false;
    private boolean isDownLoading = false;

    public FileInfo() {
    }

    public FileInfo(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return this.finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStop() {
        return this.isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public boolean isDownLoading() {
        return this.isDownLoading;
    }

    public void setDownLoading(boolean downLoading) {
        this.isDownLoading = downLoading;
    }

    public void setMessageId(int id) {
        this.messageId = id;
    }

    public int getMessageId() {
        return this.messageId;
    }
}
