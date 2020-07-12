//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.relinker;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.rong.imlib.relinker.ReLinker.LibraryInstaller;
import io.rong.imlib.relinker.ReLinker.LibraryLoader;
import io.rong.imlib.relinker.ReLinker.LoadListener;
import io.rong.imlib.relinker.ReLinker.Logger;
import io.rong.imlib.relinker.elf.ElfParser;

public class ReLinkerInstance {
    private static final String LIB_DIR = "lib";
    protected final Set<String> loadedLibraries;
    protected final LibraryLoader libraryLoader;
    protected final LibraryInstaller libraryInstaller;
    protected boolean force;
    protected boolean recursive;
    protected Logger logger;

    protected ReLinkerInstance() {
        this(new SystemLibraryLoader(), new ApkLibraryInstaller());
    }

    protected ReLinkerInstance(LibraryLoader libraryLoader, LibraryInstaller libraryInstaller) {
        this.loadedLibraries = new HashSet();
        if (libraryLoader == null) {
            throw new IllegalArgumentException("Cannot pass null library loader");
        } else if (libraryInstaller == null) {
            throw new IllegalArgumentException("Cannot pass null library installer");
        } else {
            this.libraryLoader = libraryLoader;
            this.libraryInstaller = libraryInstaller;
        }
    }

    public ReLinkerInstance log(Logger logger) {
        this.logger = logger;
        return this;
    }

    public ReLinkerInstance force() {
        this.force = true;
        return this;
    }

    public ReLinkerInstance recursively() {
        this.recursive = true;
        return this;
    }

    public void loadLibrary(Context context, String library) {
        this.loadLibrary(context, library, (String)null, (LoadListener)null);
    }

    public void loadLibrary(Context context, String library, String version) {
        this.loadLibrary(context, library, version, (LoadListener)null);
    }

    public void loadLibrary(Context context, String library, LoadListener listener) {
        this.loadLibrary(context, library, (String)null, listener);
    }

    public void loadLibrary(final Context context, final String library, final String version, final LoadListener listener) {
        if (context == null) {
            throw new IllegalArgumentException("Given context is null");
        } else if (TextUtils.isEmpty(library)) {
            throw new IllegalArgumentException("Given library is either null or empty");
        } else {
            this.log("Beginning load of %s...", library);
            if (listener == null) {
                this.loadLibraryInternal(context, library, version);
            } else {
                (new Thread(new Runnable() {
                    public void run() {
                        try {
                            ReLinkerInstance.this.loadLibraryInternal(context, library, version);
                            listener.success();
                        } catch (UnsatisfiedLinkError var2) {
                            listener.failure(var2);
                        } catch (MissingLibraryException var3) {
                            listener.failure(var3);
                        }

                    }
                })).start();
            }

        }
    }

    private void loadLibraryInternal(Context context, String library, String version) {
        if (this.loadedLibraries.contains(library) && !this.force) {
            this.log("%s already loaded previously!", library);
        } else {
            try {
                this.libraryLoader.loadLibrary(library);
                this.loadedLibraries.add(library);
                this.log("%s (%s) was loaded normally!", library, version);
            } catch (UnsatisfiedLinkError var14) {
                this.log("Loading the library normally failed: %s", Log.getStackTraceString(var14));
                this.log("%s (%s) was not loaded normally, re-linking...", library, version);
                File workaroundFile = this.getWorkaroundLibFile(context, library, version);
                if (!workaroundFile.exists() || this.force) {
                    if (this.force) {
                        this.log("Forcing a re-link of %s (%s)...", library, version);
                    }

                    this.cleanupOldLibFiles(context, library, version);
                    this.libraryInstaller.installLibrary(context, this.libraryLoader.supportedAbis(), this.libraryLoader.mapLibraryName(library), workaroundFile, this);
                }

                try {
                    if (this.recursive) {
                        ElfParser parser = null;

                        List dependencies;
                        try {
                            parser = new ElfParser(workaroundFile);
                            dependencies = parser.parseNeededDependencies();
                        } finally {
                            if (parser != null) {
                                parser.close();
                            }

                        }

                        Iterator var7 = dependencies.iterator();

                        while(var7.hasNext()) {
                            String dependency = (String)var7.next();
                            this.loadLibrary(context, this.libraryLoader.unmapLibraryName(dependency));
                        }
                    }
                } catch (IOException var13) {
                }

                this.libraryLoader.loadPath(workaroundFile.getAbsolutePath());
                this.loadedLibraries.add(library);
                this.log("%s (%s) was re-linked!", library, version);
            }
        }
    }

    protected File getWorkaroundLibDir(Context context) {
        return context.getDir("lib", 0);
    }

    protected File getWorkaroundLibFile(Context context, String library, String version) {
        String libName = this.libraryLoader.mapLibraryName(library);
        return TextUtils.isEmpty(version) ? new File(this.getWorkaroundLibDir(context), libName) : new File(this.getWorkaroundLibDir(context), libName + "." + version);
    }

    protected void cleanupOldLibFiles(Context context, String library, String currentVersion) {
        File workaroundDir = this.getWorkaroundLibDir(context);
        File workaroundFile = this.getWorkaroundLibFile(context, library, currentVersion);
        final String mappedLibraryName = this.libraryLoader.mapLibraryName(library);
        File[] existingFiles = workaroundDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.startsWith(mappedLibraryName);
            }
        });
        if (existingFiles != null) {
            File[] var8 = existingFiles;
            int var9 = existingFiles.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                File file = var8[var10];
                if (this.force || !file.getAbsolutePath().equals(workaroundFile.getAbsolutePath())) {
                    file.delete();
                }
            }

        }
    }

    public void log(String format, Object... args) {
        this.log(String.format(Locale.US, format, args));
    }

    public void log(String message) {
        if (this.logger != null) {
            this.logger.log(message);
        }

    }
}
