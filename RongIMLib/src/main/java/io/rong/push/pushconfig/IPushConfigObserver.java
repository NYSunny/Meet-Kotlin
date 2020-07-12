//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import io.rong.push.PushErrorCode;
import io.rong.push.PushType;

public interface IPushConfigObserver {
    void onSuccess(PushType var1);

    void onGetPushType(PushType var1);

    void onError(PushErrorCode var1);

    void onFail(PushType var1, PushErrorCode var2);
}
