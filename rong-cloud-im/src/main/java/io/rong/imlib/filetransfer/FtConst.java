//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

public class FtConst {
    public static final String TEMP_TRANSFER_FILE_DIR = "TempFile";

    public FtConst() {
    }

    public static enum ServiceType {
        QI_NIU,
        PRIVATE_CLOUD,
        BAI_DU;

        private ServiceType() {
        }
    }

    public static enum MimeType {
        NONE(0, "none"),
        FILE_IMAGE(1, "image/jpeg"),
        FILE_AUDIO(2, "audio/amr"),
        FILE_VIDEO(3, "video/mpeg4"),
        FILE_TEXT_PLAIN(4, "application/octet-stream"),
        FILE_SIGHT(5, "video/mpeg4"),
        FILE_HTML(6, "text/html");

        private int value;
        private String name;

        private MimeType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }

        public static FtConst.MimeType setValue(int code) {
            FtConst.MimeType[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                FtConst.MimeType c = var1[var3];
                if (code == c.getValue()) {
                    return c;
                }
            }

            return NONE;
        }
    }
}
