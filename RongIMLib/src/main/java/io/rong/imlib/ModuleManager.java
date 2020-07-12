//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import io.rong.common.RLog;
import io.rong.imlib.RongIMClient.OnReceiveMessageListener;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.cs.CustomServiceManager;
import io.rong.imlib.location.RealTimeLocationManager;
import io.rong.imlib.model.Message;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/** @deprecated */
@Deprecated
public class ModuleManager {
    private static final String TAG = "ModuleManager";
    private static ArrayList<ModuleManager.MessageRouter> messageRouters = new ArrayList();
    private static ArrayList<ModuleManager.ConnectivityStateChangedListener> connectivityStateChangedListeners = new ArrayList();
    private static OnReceiveMessageListener sListener;
    private static Object rongCallClientInstance;

    public ModuleManager() {
    }

    static void init(Context context, IHandler stub, OnReceiveMessageListener listener) {
        RLog.i("ModuleManager", "init");
        sListener = listener;

        try {
            String moduleName = "io.rong.calllib.RongCallClient";
            Class<?> cls = Class.forName(moduleName);
            Constructor<?> constructor = cls.getConstructor(Context.class, IHandler.class);
            rongCallClientInstance = constructor.newInstance(context, stub);
        } catch (Exception var6) {
            RLog.i("ModuleManager", "Can not find RongCallClient module.");
        }

        RealTimeLocationManager.getInstance().init(context, listener, stub);
        CustomServiceManager.getInstance().init(context, listener, stub);
    }

    static void unInit() {
        if (rongCallClientInstance != null) {
            String moduleName = "io.rong.calllib.RongCallClient";

            try {
                Class<?> cls = Class.forName(moduleName);
                Method method = cls.getMethod("unInit", (Class[])null);
                method.setAccessible(true);
                method.invoke(rongCallClientInstance, (Object[])null);
            } catch (ClassNotFoundException var3) {
                RLog.e("ModuleManager", "unInit ClassNotFoundException", var3);
            } catch (NoSuchMethodException var4) {
                RLog.e("ModuleManager", "unInit NoSuchMethodException", var4);
            } catch (IllegalAccessException var5) {
                RLog.e("ModuleManager", "unInit IllegalAccessException", var5);
            } catch (InvocationTargetException var6) {
                RLog.e("ModuleManager", "unInit InvocationTargetException", var6);
            }

            rongCallClientInstance = null;
        }

    }

    static boolean routeMessage(Message message, int left, boolean offline, int cmdLeft) {
        Iterator var4 = messageRouters.iterator();

        ModuleManager.MessageRouter listener;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            listener = (ModuleManager.MessageRouter)var4.next();
        } while(!listener.onReceived(message, left, offline, cmdLeft));

        return true;
    }

    static void connectivityChanged(ConnectionStatus state) {
        Iterator var1 = connectivityStateChangedListeners.iterator();

        while(var1.hasNext()) {
            ModuleManager.ConnectivityStateChangedListener listener = (ModuleManager.ConnectivityStateChangedListener)var1.next();
            listener.onChanged(state);
        }

    }

    public static void addConnectivityStateChangedListener(ModuleManager.ConnectivityStateChangedListener listener) {
        connectivityStateChangedListeners.add(listener);
    }

    public static void addMessageRouter(ModuleManager.MessageRouter listener) {
        messageRouters.add(listener);
    }

    public static void removeMessageRouter(ModuleManager.MessageRouter router) {
        messageRouters.remove(router);
    }

    public static void removeConnectivityStateChangedListener(ModuleManager.ConnectivityStateChangedListener listener) {
        connectivityStateChangedListeners.remove(listener);
    }

    public static OnReceiveMessageListener getListener() {
        return sListener;
    }

    public interface ConnectivityStateChangedListener {
        void onChanged(ConnectionStatus var1);
    }

    public interface MessageRouter {
        boolean onReceived(Message var1, int var2, boolean var3, int var4);
    }
}
