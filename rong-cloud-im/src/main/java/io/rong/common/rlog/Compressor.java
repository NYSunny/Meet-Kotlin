//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.rlog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.rong.common.dlog.LogZipper;

public class Compressor {
    private ExecutorService pool = Executors.newSingleThreadExecutor();
    private ICompressListener mListener;

    public Compressor(ICompressListener pListener) {
        this.mListener = pListener;
    }

    public void compress(final String sourceFile, final String targetFile, final ICompressListener pListener) {
        this.pool.submit(new Runnable() {
            public void run() {
                boolean compressSuccess = LogZipper.gzipFile(sourceFile, targetFile);
                if (pListener != null) {
                    pListener.onCompressFinish(compressSuccess, sourceFile, targetFile);
                }

            }
        });
    }

    public void compress(String sourceFile, String targetFile) {
        this.compress(sourceFile, targetFile, this.mListener);
    }

    interface ICompressListener {
        void onCompressFinish(boolean var1, String var2, String var3);
    }
}
