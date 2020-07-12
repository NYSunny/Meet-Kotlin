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

public class Program32Header extends ProgramHeader {
    public Program32Header(ElfParser parser, Header header, long index) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long baseOffset = header.phoff + index * (long)header.phentsize;
        this.type = parser.readWord(buffer, baseOffset);
        this.offset = parser.readWord(buffer, baseOffset + 4L);
        this.vaddr = parser.readWord(buffer, baseOffset + 8L);
        this.memsz = parser.readWord(buffer, baseOffset + 20L);
    }
}
