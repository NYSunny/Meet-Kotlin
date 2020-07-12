//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.GZIPOutputStream;

public class LogZipper {
    public LogZipper() {
    }

    public static boolean gzipFile(String sourceFile, String targetFile) {
        try {
            File file = new File(sourceFile);
            if (file.exists() && file.length() != 0L) {
                byte[] buffer = new byte[1024];
                GZIPOutputStream gzStream = new GZIPOutputStream(new FileOutputStream(targetFile));
                FileInputStream in = new FileInputStream(file);

                int len;
                while((len = in.read(buffer)) > 0) {
                    gzStream.write(buffer, 0, len);
                }

                in.close();
                gzStream.finish();
                gzStream.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }
}
