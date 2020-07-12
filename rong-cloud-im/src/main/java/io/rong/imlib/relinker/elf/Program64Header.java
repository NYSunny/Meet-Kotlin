//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.rong.imlib.relinker.elf.Elf.Header;
import io.rong.imlib.relinker.elf.Elf.ProgramHeader;

public class Program64Header extends ProgramHeader {
    public Program64Header(ElfParser parser, Header header, long index) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long baseOffset = header.phoff + index * (long)header.phentsize;
        this.type = parser.readWord(buffer, baseOffset);
        this.offset = parser.readLong(buffer, baseOffset + 8L);
        this.vaddr = parser.readLong(buffer, baseOffset + 16L);
        this.memsz = parser.readLong(buffer, baseOffset + 40L);
    }
}
