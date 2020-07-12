//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import io.rong.message.DefaultMessageHandler;
import io.rong.message.MessageHandler;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageTag {
    int NONE = 0;
    int ISPERSISTED = 1;
    int ISCOUNTED = 3;
    int STATUS = 16;

    String value();

    int flag() default 0;

    Class<? extends MessageHandler> messageHandler() default DefaultMessageHandler.class;
}
