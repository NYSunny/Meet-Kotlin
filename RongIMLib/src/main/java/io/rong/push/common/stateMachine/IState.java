//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.common.stateMachine;

import android.os.Message;

public interface IState {
    boolean HANDLED = true;
    boolean NOT_HANDLED = false;

    void enter();

    void exit();

    boolean processMessage(Message var1);

    String getName();
}
