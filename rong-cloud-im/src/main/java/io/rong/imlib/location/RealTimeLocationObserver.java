//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.location;

import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationErrorCode;
import io.rong.imlib.location.RealTimeLocationConstant.RealTimeLocationStatus;

public interface RealTimeLocationObserver {
    void onStatusChange(RealTimeLocationStatus var1);

    /** @deprecated */
    @Deprecated
    void onReceiveLocation(double var1, double var3, String var5);

    void onReceiveLocationWithType(double var1, double var3, RealTimeLocationType var5, String var6);

    void onParticipantsJoin(String var1);

    void onParticipantsQuit(String var1);

    void onError(RealTimeLocationErrorCode var1);
}
