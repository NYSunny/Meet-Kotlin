//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import java.util.ArrayList;

import io.rong.push.PushErrorCode;

public interface PushNaviObserver {
    void onSuccess(ArrayList<String> var1);

    void onError(PushErrorCode var1);
}
