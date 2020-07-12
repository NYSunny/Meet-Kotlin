//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog;

import java.nio.ByteBuffer;

public abstract class RtFullListener {
    protected ByteBuffer byteBuffer = ByteBuffer.allocateDirect(65536);

    public RtFullListener() {
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    public abstract void NotifyFull();

    public abstract void NotifyFullEnd(int var1);
}
