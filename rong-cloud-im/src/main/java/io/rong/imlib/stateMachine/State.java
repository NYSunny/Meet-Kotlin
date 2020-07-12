//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.stateMachine;

import android.os.Message;

public class State implements IState {
    protected State() {
    }

    public void enter() {
    }

    public void exit() {
    }

    public boolean processMessage(Message msg) {
        return false;
    }

    public String getName() {
        String name = this.getClass().getName();
        int lastDollar = name.lastIndexOf(36);
        return name.substring(lastDollar + 1);
    }
}
