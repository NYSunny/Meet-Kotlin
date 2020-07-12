//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker.elf;

import java.io.IOException;

public interface Elf {
    public abstract static class DynamicStructure {
        public static final int DT_NULL = 0;
        public static final int DT_NEEDED = 1;
        public static final int DT_STRTAB = 5;
        public long tag;
        public long val;

        public DynamicStructure() {
        }
    }

    public abstract static class SectionHeader {
        public long info;

        public SectionHeader() {
        }
    }

    public abstract static class ProgramHeader {
        public static final int PT_LOAD = 1;
        public static final int PT_DYNAMIC = 2;
        public long type;
        public long offset;
        public long vaddr;
        public long memsz;

        public ProgramHeader() {
        }
    }

    public abstract static class Header {
        public static final int ELFCLASS32 = 1;
        public static final int ELFCLASS64 = 2;
        public static final int ELFDATA2MSB = 2;
        public boolean bigEndian;
        public int type;
        public long phoff;
        public long shoff;
        public int phentsize;
        public int phnum;
        public int shentsize;
        public int shnum;
        public int shstrndx;

        public Header() {
        }

        public abstract SectionHeader getSectionHeader(int var1) throws IOException;

        public abstract ProgramHeader getProgramHeader(long var1) throws IOException;

        public abstract DynamicStructure getDynamicStructure(long var1, int var3) throws IOException;
    }
}
