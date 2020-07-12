//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.push.pushconfig;

import io.rong.push.PushErrorCode;

public interface IResultCallback<T> {
    void onSuccess(T var1);

    void onError(PushErrorCode var1);
}
