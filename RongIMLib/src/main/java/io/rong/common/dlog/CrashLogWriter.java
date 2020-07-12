//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.dlog;

import java.io.File;

public class CrashLogWriter extends SimpleLogWriter {
    public CrashLogWriter(String logDir) {
        super(logDir + File.separator + "rong_sdk_crash.log");
    }

    public void write(String log) {
        this.internalWrite(log);
    }
}
