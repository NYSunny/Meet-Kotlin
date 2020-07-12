//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker;

import android.content.Context;

import java.io.File;

public class ReLinker {
    public static void loadLibrary(Context context, String library) {
        loadLibrary(context, library, (String)null, (LoadListener)null);
    }

    public static void loadLibrary(Context context, String library, String version) {
        loadLibrary(context, library, version, (LoadListener)null);
    }

    public static void loadLibrary(Context context, String library, LoadListener listener) {
        loadLibrary(context, library, (String)null, listener);
    }

    public static void loadLibrary(Context context, String library, String version, LoadListener listener) {
        (new ReLinkerInstance()).loadLibrary(context, library, version, listener);
    }

    public static ReLinkerInstance force() {
        return (new ReLinkerInstance()).force();
    }

    public static ReLinkerInstance log(Logger logger) {
        return (new ReLinkerInstance()).log(logger);
    }

    public static ReLinkerInstance recursively() {
        return (new ReLinkerInstance()).recursively();
    }

    private ReLinker() {
    }

    public interface LibraryInstaller {
        void installLibrary(Context var1, String[] var2, String var3, File var4, ReLinkerInstance var5);
    }

    public interface LibraryLoader {
        void loadLibrary(String var1);

        void loadPath(String var1);

        String mapLibraryName(String var1);

        String unmapLibraryName(String var1);

        String[] supportedAbis();
    }

    public interface Logger {
        void log(String var1);
    }

    public interface LoadListener {
        void success();

        void failure(Throwable var1);
    }
}
