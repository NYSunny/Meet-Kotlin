//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.rong.imlib.relinker.elf.Elf.DynamicStructure;
import io.rong.imlib.relinker.elf.Elf.Header;

public class Dynamic64Structure extends DynamicStructure {
    public Dynamic64Structure(ElfParser parser, Header header, long baseOffset, int index) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        baseOffset += (long)(index * 16);
        this.tag = parser.readLong(buffer, baseOffset);
        this.val = parser.readLong(buffer, baseOffset + 8L);
    }
}
