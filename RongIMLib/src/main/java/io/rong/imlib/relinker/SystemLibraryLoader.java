//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker;

import android.os.Build;
import android.os.Build.VERSION;

import io.rong.imlib.relinker.ReLinker.LibraryLoader;

final class SystemLibraryLoader implements LibraryLoader {
    SystemLibraryLoader() {
    }

    public void loadLibrary(String libraryName) {
        System.loadLibrary(libraryName);
    }

    public void loadPath(String libraryPath) {
        System.load(libraryPath);
    }

    public String mapLibraryName(String libraryName) {
        return libraryName.startsWith("lib") && libraryName.endsWith(".so") ? libraryName : System.mapLibraryName(libraryName);
    }

    public String unmapLibraryName(String mappedLibraryName) {
        return mappedLibraryName.substring(3, mappedLibraryName.length() - 3);
    }

    public String[] supportedAbis() {
        if (VERSION.SDK_INT >= 21 && Build.SUPPORTED_ABIS.length > 0) {
            return Build.SUPPORTED_ABIS;
        } else {
            return !TextUtils.isEmpty(Build.CPU_ABI2) ? new String[]{Build.CPU_ABI, Build.CPU_ABI2} : new String[]{Build.CPU_ABI};
        }
    }
}
