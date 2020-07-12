//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.common.fwlog;

import android.text.TextUtils;

import java.util.Arrays;

public class LogSplitUtil {
    private static final int MAX_CONTENT_SIZE = 850;
    public static final String SUFFIX_SPLIT = "-X";

    public LogSplitUtil() {
    }

    public static void write(int level, String tag, String keys, Object... values) {
        int length = keys.length();
        Object[] var5 = values;
        int var6 = values.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Object obj = var5[var7];
            String value = obj != null ? obj.toString() : "null";
            length += value.length();
        }

        if (length <= 850) {
            FwLog.write(level, 2, tag, keys, values);
        } else {
            String suffix = tag.substring(tag.lastIndexOf("-"));
            tag = tag.substring(0, tag.lastIndexOf("-"));
            String[] keyArray = keys.split("\\|");
            String splitKeys = "";
            int splitLength = 0;
            int start = 0;
            boolean haveMergedKeys = false;

            for(int i = 0; i < keyArray.length; ++i) {
                if (splitKeys.contains("|")) {
                    haveMergedKeys = true;
                }

                splitKeys = splitKeys + keyArray[i] + "|";
                splitLength += splitKeys.length();
                String value = values[i] != null ? values[i].toString() : "null";
                if (value.length() < 850) {
                    splitLength += value.length();
                    if (splitLength > 850) {
                        merge(level, tag, suffix, start, i, splitKeys, values);
                        suffix = "-X";
                        start = i + 1;
                        splitKeys = keyArray[i] + "|";
                        splitLength = keyArray[i].length() + value.length() + 1;
                    } else if (i == keyArray.length - 1) {
                        if (start == i) {
                            FwLog.write(level, 2, tag + suffix, keyArray[i], new Object[]{value});
                        } else {
                            merge(level, tag, suffix, start, i, splitKeys, values);
                        }
                    }
                } else {
                    if (!haveMergedKeys) {
                        split(level, tag, suffix, keyArray[i], value);
                    } else {
                        if (i > start) {
                            merge(level, tag, suffix, start, i, splitKeys, values);
                            suffix = "-X";
                        }

                        split(level, tag, suffix, keyArray[i], value);
                    }

                    suffix = "-X";
                    splitKeys = "";
                    splitLength = 0;
                    start = i + 1;
                    haveMergedKeys = false;
                }
            }

        }
    }

    private static void merge(int level, String tag, String suffix, int start, int end, String splitKeys, Object... values) {
        if (start < end) {
            Object[] content = new Object[end - start];

            for(int j = 0; j < end; ++j) {
                content[j] = values[start + j];
            }

            splitKeys = splitKeys.substring(0, splitKeys.length() - 1);
            splitKeys = splitKeys.substring(0, splitKeys.lastIndexOf("|"));
            FwLog.write(level, 2, tag + suffix, splitKeys, content);
        }
    }

    private static void split(int level, String tag, String suffix, String key, String value) {
        while(true) {
            if (!TextUtils.isEmpty(value)) {
                String split = value;
                if (value.length() > 850) {
                    split = new String(Arrays.copyOf(value.getBytes(), 850));
                }

                FwLog.write(level, 2, tag + suffix, key, new Object[]{split});
                suffix = "-X";
                if (value.length() >= 850) {
                    value = value.substring(850);
                    continue;
                }
            }

            return;
        }
    }
}
