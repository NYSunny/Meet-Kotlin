//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker.elf;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ElfParser implements Closeable, Elf {
    private final int MAGIC = 1179403647;
    private final FileChannel channel;

    public ElfParser(File file) throws FileNotFoundException {
        if (file != null && file.exists()) {
            FileInputStream inputStream = new FileInputStream(file);
            this.channel = inputStream.getChannel();
        } else {
            throw new IllegalArgumentException("File is null or does not exist");
        }
    }

    public Header parseHeader() throws IOException {
        this.channel.position(0L);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        if (this.readWord(buffer, 0L) != 1179403647L) {
            throw new IllegalArgumentException("Invalid ELF Magic!");
        } else {
            short fileClass = this.readByte(buffer, 4L);
            boolean bigEndian = this.readByte(buffer, 5L) == 2;
            if (fileClass == 1) {
                return new Elf32Header(bigEndian, this);
            } else if (fileClass == 2) {
                return new Elf64Header(bigEndian, this);
            } else {
                throw new IllegalStateException("Invalid class type!");
            }
        }
    }

    public List<String> parseNeededDependencies() throws IOException {
        this.channel.position(0L);
        List<String> dependencies = new ArrayList();
        Header header = this.parseHeader();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(header.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        long numProgramHeaderEntries = (long)header.phnum;
        if (numProgramHeaderEntries == 65535L) {
            SectionHeader sectionHeader = header.getSectionHeader(0);
            numProgramHeaderEntries = sectionHeader.info;
        }

        long dynamicSectionOff = 0L;

        for(long i = 0L; i < numProgramHeaderEntries; ++i) {
            ProgramHeader programHeader = header.getProgramHeader(i);
            if (programHeader.type == 2L) {
                dynamicSectionOff = programHeader.offset;
                break;
            }
        }

        if (dynamicSectionOff == 0L) {
            return Collections.unmodifiableList(dependencies);
        } else {
            int i = 0;
            List<Long> neededOffsets = new ArrayList();
            long vStringTableOff = 0L;

            DynamicStructure dynStructure;
            do {
                dynStructure = header.getDynamicStructure(dynamicSectionOff, i);
                if (dynStructure.tag == 1L) {
                    neededOffsets.add(dynStructure.val);
                } else if (dynStructure.tag == 5L) {
                    vStringTableOff = dynStructure.val;
                }

                ++i;
            } while(dynStructure.tag != 0L);

            if (vStringTableOff == 0L) {
                throw new IllegalStateException("String table offset not found!");
            } else {
                long stringTableOff = this.offsetFromVma(header, numProgramHeaderEntries, vStringTableOff);
                Iterator var15 = neededOffsets.iterator();

                while(var15.hasNext()) {
                    Long strOff = (Long)var15.next();
                    dependencies.add(this.readString(buffer, stringTableOff + strOff));
                }

                return dependencies;
            }
        }
    }

    private long offsetFromVma(Header header, long numEntries, long vma) throws IOException {
        for(long i = 0L; i < numEntries; ++i) {
            ProgramHeader programHeader = header.getProgramHeader(i);
            if (programHeader.type == 1L && programHeader.vaddr <= vma && vma <= programHeader.vaddr + programHeader.memsz) {
                return vma - programHeader.vaddr + programHeader.offset;
            }
        }

        throw new IllegalStateException("Could not map vma to file offset!");
    }

    public void close() throws IOException {
        this.channel.close();
    }

    protected String readString(ByteBuffer buffer, long offset) throws IOException {
        StringBuilder builder = new StringBuilder();

        short c;
        while((c = this.readByte(buffer, offset++)) != 0) {
            builder.append((char)c);
        }

        return builder.toString();
    }

    protected long readLong(ByteBuffer buffer, long offset) throws IOException {
        this.read(buffer, offset, 8);
        return buffer.getLong();
    }

    protected long readWord(ByteBuffer buffer, long offset) throws IOException {
        this.read(buffer, offset, 4);
        return (long)buffer.getInt() & 4294967295L;
    }

    protected int readHalf(ByteBuffer buffer, long offset) throws IOException {
        this.read(buffer, offset, 2);
        return buffer.getShort() & '\uffff';
    }

    protected short readByte(ByteBuffer buffer, long offset) throws IOException {
        this.read(buffer, offset, 1);
        return (short)(buffer.get() & 255);
    }

    protected void read(ByteBuffer buffer, long offset, int length) throws IOException {
        buffer.position(0);
        buffer.limit(length);

        int read;
        for(long bytesRead = 0L; bytesRead < (long)length; bytesRead += (long)read) {
            read = this.channel.read(buffer, offset + bytesRead);
            if (read == -1) {
                throw new EOFException();
            }
        }

        buffer.position(0);
    }
}
