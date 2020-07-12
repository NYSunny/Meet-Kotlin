//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DestructionTag {
    int FLAG_COUNT_DOWN_WHEN_CLICK = 0;
    int FLAG_COUNT_DOWN_WHEN_VISIBLE = 1;

    int destructionFlag() default 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DestructionFlag {
    }
}
