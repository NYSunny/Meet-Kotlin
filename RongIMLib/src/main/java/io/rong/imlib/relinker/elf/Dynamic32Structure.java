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

public class Dynamic32Structure extends DynamicStructure {
    public Dynamic32Structure(ElfParser parser, Header header, long baseOffset, int index) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        baseOffset += (long)(index * 8);
        this.tag = parser.readWord(buffer, baseOffset);
        this.val = parser.readWord(buffer, baseOffset + 4L);
    }
}
