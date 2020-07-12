//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.HardwareResource.ResourceType;
import java.util.List;

public interface IMLibExtensionModule {
    void onCreate(Context var1, String var2);

    void onLogin(String var1, String var2);

    boolean onReceiveMessage(Message var1, int var2, boolean var3, int var4);

    List<Class<? extends MessageContent>> getMessageContentList();

    List<Class<? extends MessageContent>> getCmdMessageContentList();

    void onConnectStatusChanged(ConnectionStatus var1);

    void onLogout();

    void onDestroy();

    boolean onRequestHardwareResource(ResourceType var1);
}
