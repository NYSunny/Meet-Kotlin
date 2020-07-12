//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog;

import java.nio.ByteBuffer;

public abstract class RtCronListener {
    protected ByteBuffer byteBuffer = ByteBuffer.allocateDirect(65536);

    public RtCronListener() {
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public abstract void NotifyCron();

    public abstract void NotifyCronEnd(int var1, String var2, String var3, int var4);
}
