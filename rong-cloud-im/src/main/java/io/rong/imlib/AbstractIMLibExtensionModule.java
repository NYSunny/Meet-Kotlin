//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import java.util.List;

public abstract class AbstractIMLibExtensionModule implements IMLibExtensionModule {
    public AbstractIMLibExtensionModule() {
    }

    public void onCreate(Context context, String appKey) {
    }

    public void onLogin(String userId, String token) {
    }

    public boolean onReceiveMessage(Message message, int left, boolean offline, int cmdLeft) {
        return false;
    }

    public List<Class<? extends MessageContent>> getMessageContentList() {
        return null;
    }

    public List<Class<? extends MessageContent>> getCmdMessageContentList() {
        return null;
    }

    public void onConnectStatusChanged(ConnectionStatus status) {
    }

    public void onLogout() {
    }

    public void onDestroy() {
    }
}
