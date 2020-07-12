//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog;

public class RtLogNative {
    public static final int rt_debug = 1;
    public static final int rt_info = 2;
    public static final int rt_warning = 3;
    public static final int rt_error = 4;
    public static final int rt_none = 5;
    public static final int F = 0;
    public static final int E = 1;
    public static final int W = 2;
    public static final int I = 3;
    public static final int D = 4;

    public RtLogNative() {
    }

    public native int setRtLogDebugLevel(int var1);

    public native int setRtLogListener(RtLogListener var1);

    public native int setCronListener(RtCronListener var1);

    public native int setFullListener(RtFullListener var1);

    public native int initialize(String var1, String var2, String var3);

    public native void writeMessage(int var1, String var2, String var3, String var4, long var5);

    public native int queryCronMessage(int var1, boolean var2);

    public native int updateCronUploadTime(String var1, String var2, int var3, long var4);

    public native int queryFullMessage(int var1, long var2, long var4, boolean var6);

    public native void dispose();
}
