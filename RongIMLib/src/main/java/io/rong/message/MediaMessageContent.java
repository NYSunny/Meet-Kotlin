//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.message;

import android.net.Uri;
import io.rong.imlib.model.MessageContent;

public abstract class MediaMessageContent extends MessageContent {
    private Uri mLocalPath;
    private Uri mMediaUrl;
    private String mName;
    private String mExtra;

    public MediaMessageContent() {
    }

    public Uri getLocalPath() {
        return this.mLocalPath;
    }

    public Uri getMediaUrl() {
        return this.mMediaUrl;
    }

    public void setMediaUrl(Uri mMediaUrl) {
        this.mMediaUrl = mMediaUrl;
    }

    public void setLocalPath(Uri mLocalPath) {
        this.mLocalPath = mLocalPath;
    }

    public String getExtra() {
        return this.mExtra;
    }

    public void setExtra(String mExtra) {
        this.mExtra = mExtra;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
