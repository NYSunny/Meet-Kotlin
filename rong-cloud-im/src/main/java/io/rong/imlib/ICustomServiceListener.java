//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import io.rong.imlib.model.CSGroupItem;
import io.rong.imlib.model.CustomServiceMode;
import java.util.List;

public interface ICustomServiceListener {
    void onSuccess(CustomServiceConfig var1);

    void onError(int var1, String var2);

    void onModeChanged(CustomServiceMode var1);

    void onQuit(String var1);

    void onPullEvaluation(String var1);

    void onSelectGroup(List<CSGroupItem> var1);
}
