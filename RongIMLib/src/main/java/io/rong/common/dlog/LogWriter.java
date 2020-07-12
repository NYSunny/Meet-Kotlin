//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import io.rong.common.dlog.DLog.ILogUploadCallback;

public interface LogWriter {
    void write(String var1);

    void flush();

    void flushAndReport(boolean var1, LogReporter var2, ILogUploadCallback var3);

    void open();

    void close();
}
