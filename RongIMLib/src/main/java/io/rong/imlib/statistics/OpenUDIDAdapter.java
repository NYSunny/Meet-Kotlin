//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OpenUDIDAdapter {
    private static final String OPEN_UDID_MANAGER_CLASS_NAME = "org.openudid.OpenUDID_manager";

    public OpenUDIDAdapter() {
    }

    public static boolean isOpenUDIDAvailable() {
        boolean openUDIDAvailable = false;

        try {
            Class.forName("org.openudid.OpenUDID_manager");
            openUDIDAvailable = true;
        } catch (ClassNotFoundException var2) {
        }

        return openUDIDAvailable;
    }

    public static boolean isInitialized() {
        boolean initialized = false;

        try {
            Class<?> cls = Class.forName("org.openudid.OpenUDID_manager");
            Method isInitializedMethod = cls.getMethod("isInitialized", (Class[])null);
            Object result = isInitializedMethod.invoke((Object)null, (Object[])null);
            if (result instanceof Boolean) {
                initialized = (Boolean)result;
            }
        } catch (ClassNotFoundException var4) {
        } catch (NoSuchMethodException var5) {
        } catch (InvocationTargetException var6) {
        } catch (IllegalAccessException var7) {
        }

        return initialized;
    }

    public static void sync(Context context) {
        try {
            Class<?> cls = Class.forName("org.openudid.OpenUDID_manager");
            Method syncMethod = cls.getMethod("sync", Context.class);
            syncMethod.invoke((Object)null, context);
        } catch (ClassNotFoundException var3) {
        } catch (NoSuchMethodException var4) {
        } catch (InvocationTargetException var5) {
        } catch (IllegalAccessException var6) {
        }

    }

    public static String getOpenUDID() {
        String openUDID = null;

        try {
            Class<?> cls = Class.forName("org.openudid.OpenUDID_manager");
            Method getOpenUDIDMethod = cls.getMethod("getOpenUDID", (Class[])null);
            Object result = getOpenUDIDMethod.invoke((Object)null, (Object[])null);
            if (result instanceof String) {
                openUDID = (String)result;
            }
        } catch (ClassNotFoundException var4) {
        } catch (NoSuchMethodException var5) {
        } catch (InvocationTargetException var6) {
        } catch (IllegalAccessException var7) {
        }

        return openUDID;
    }
}
