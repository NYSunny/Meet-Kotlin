//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.filetransfer;

public interface RequestCallBack {
    void onError(int var1);

    void onComplete(String var1);

    void onProgress(int var1);

    void onCanceled(Object var1);
}
