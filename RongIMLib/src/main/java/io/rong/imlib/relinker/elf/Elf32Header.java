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
import io.rong.imlib.relinker.elf.Elf.ProgramHeader;
import io.rong.imlib.relinker.elf.Elf.SectionHeader;

public class Elf32Header extends Header {
    private final ElfParser parser;

    public Elf32Header(boolean bigEndian, ElfParser parser) throws IOException {
        this.bigEndian = bigEndian;
        this.parser = parser;
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        this.type = parser.readHalf(buffer, 16L);
        this.phoff = parser.readWord(buffer, 28L);
        this.shoff = parser.readWord(buffer, 32L);
        this.phentsize = parser.readHalf(buffer, 42L);
        this.phnum = parser.readHalf(buffer, 44L);
        this.shentsize = parser.readHalf(buffer, 46L);
        this.shnum = parser.readHalf(buffer, 48L);
        this.shstrndx = parser.readHalf(buffer, 50L);
    }

    public SectionHeader getSectionHeader(int index) throws IOException {
        return new Section32Header(this.parser, this, index);
    }

    public ProgramHeader getProgramHeader(long index) throws IOException {
        return new Program32Header(this.parser, this, index);
    }

    public DynamicStructure getDynamicStructure(long baseOffset, int index) throws IOException {
        return new Dynamic32Structure(this.parser, this, baseOffset, index);
    }
}
