//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import io.rong.imlib.relinker.ReLinker.LibraryInstaller;

public class ApkLibraryInstaller implements LibraryInstaller {
    private static final int MAX_TRIES = 5;
    private static final int COPY_BUFFER_SIZE = 4096;

    public ApkLibraryInstaller() {
    }

    private String[] sourceDirectories(Context context) {
        ApplicationInfo appInfo = context.getApplicationInfo();
        if (VERSION.SDK_INT >= 21 && appInfo.splitSourceDirs != null && appInfo.splitSourceDirs.length != 0) {
            String[] apks = new String[appInfo.splitSourceDirs.length + 1];
            apks[0] = appInfo.sourceDir;
            System.arraycopy(appInfo.splitSourceDirs, 0, apks, 1, appInfo.splitSourceDirs.length);
            return apks;
        } else {
            return new String[]{appInfo.sourceDir};
        }
    }

    private ZipFileInZipEntry findAPKWithLibrary(Context context, String[] abis, String mappedLibraryName, ReLinkerInstance instance) {
        ZipFile zipFile = null;
        String[] var6 = this.sourceDirectories(context);
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String sourceDir = var6[var8];
            int var10 = 0;

            while(var10++ < 5) {
                try {
                    zipFile = new ZipFile(new File(sourceDir), 1);
                    break;
                } catch (IOException var18) {
                }
            }

            if (zipFile != null) {
                var10 = 0;

                while(var10++ < 5) {
                    String jniNameInApk = null;
                    ZipEntry libraryEntry = null;
                    String[] var13 = abis;
                    int var14 = abis.length;

                    for(int var15 = 0; var15 < var14; ++var15) {
                        String abi = var13[var15];
                        jniNameInApk = "lib" + File.separatorChar + abi + File.separatorChar + mappedLibraryName;
                        instance.log("Looking for %s in APK %s...", new Object[]{jniNameInApk, sourceDir});
                        libraryEntry = zipFile.getEntry(jniNameInApk);
                        if (libraryEntry != null) {
                            return new ZipFileInZipEntry(zipFile, libraryEntry);
                        }
                    }
                }

                try {
                    zipFile.close();
                } catch (IOException var17) {
                }
            }
        }

        return null;
    }

    public void installLibrary(Context context, String[] abis, String mappedLibraryName, File destination, ReLinkerInstance instance) {
        ZipFileInZipEntry found = null;

        try {
            found = this.findAPKWithLibrary(context, abis, mappedLibraryName, instance);
            if (found == null) {
                throw new MissingLibraryException(mappedLibraryName);
            } else {
                int var7 = 0;

                while(true) {
                    if (var7++ < 5) {
                        instance.log("Found %s! Extracting...", new Object[]{mappedLibraryName});

                        try {
                            if (!destination.exists() && !destination.createNewFile()) {
                                continue;
                            }
                        } catch (IOException var33) {
                            continue;
                        }

                        InputStream inputStream = null;
                        FileOutputStream fileOut = null;

                        try {
                            inputStream = found.zipFile.getInputStream(found.zipEntry);
                            fileOut = new FileOutputStream(destination);
                            long written = this.copy(inputStream, fileOut);
                            fileOut.getFD().sync();
                            if (written != destination.length()) {
                                continue;
                            }
                        } catch (FileNotFoundException var30) {
                            continue;
                        } catch (IOException var31) {
                            continue;
                        } finally {
                            this.closeSilently(inputStream);
                            this.closeSilently(fileOut);
                        }

                        destination.setReadable(true, false);
                        destination.setExecutable(true, false);
                        destination.setWritable(true);
                        return;
                    }

                    instance.log("FATAL! Couldn't extract the library from the APK!");
                    return;
                }
            }
        } finally {
            try {
                if (found != null && found.zipFile != null) {
                    found.zipFile.close();
                }
            } catch (IOException var29) {
            }

        }
    }

    private long copy(InputStream in, OutputStream out) throws IOException {
        long copied = 0L;
        byte[] buf = new byte[4096];

        while(true) {
            int read = in.read(buf);
            if (read == -1) {
                out.flush();
                return copied;
            }

            out.write(buf, 0, read);
            copied += (long)read;
        }
    }

    private void closeSilently(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var3) {
        }

    }

    private static class ZipFileInZipEntry {
        private ZipFile zipFile;
        private ZipEntry zipEntry;

        private ZipFileInZipEntry(ZipFile zipFile, ZipEntry zipEntry) {
            this.zipFile = zipFile;
            this.zipEntry = zipEntry;
        }
    }
}
