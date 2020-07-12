package io.rong.common;

public class FileInfo {
    private String name;
    private long size;
    private String type;

    public FileInfo() {
    }

    public String getName() {
        return this.name == null ? "" : this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getType() {
        return this.type == null ? "" : this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}