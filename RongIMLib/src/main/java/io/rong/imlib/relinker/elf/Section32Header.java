//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker.elf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.rong.imlib.relinker.elf.Elf.Header;
import io.rong.imlib.relinker.elf.Elf.SectionHeader;

public class Section32Header extends SectionHeader {
    public Section32Header(ElfParser parser, Header header, int index) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        this.info = parser.readWord(buffer, header.shoff + (long)(index * header.shentsize) + 28L);
    }
}
