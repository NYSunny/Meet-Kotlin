//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.content.Context;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.HardwareResource.ResourceType;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class IMLibExtensionModuleManager implements IMLibExtensionModule {
    private String userId;
    private String token;
    private List<Class<? extends MessageContent>> messageContentClazzList;
    private List<Class<? extends MessageContent>> cmdMessageContentClazzList;
    private List<IMLibExtensionModule> extensionModules;
    private Map<IMLibExtensionModule, List<Class<? extends MessageContent>>> extensionModuleMessageContentMap;
    private static final String[] defaultModules = new String[]{"io.rong.wrapper.RongRemoteControlExtensionModule", "io.rongcloud.moment.lib.RongMomentExtensionModule", "io.rong.signal.core.RCSignalExtensionModule", "cn.rongcloud.rtc.RongRTCExtensionModule"};

    private IMLibExtensionModuleManager() {
        this.init();
    }

    public static IMLibExtensionModuleManager getInstance() {
        return IMLibExtensionModuleManager.IMLibExtensionModuleManagerHolder.instance;
    }

    public void registerModulesByName(List<String> moduleNames) {
        if (moduleNames != null && moduleNames.size() > 0) {
            Iterator var2 = moduleNames.iterator();

            while(var2.hasNext()) {
                String moduleName = (String)var2.next();
                this.loadIMLibModuleByName(moduleName);
            }
        }

    }

    void loadAllIMLibExtensionModules() {
        this.loadDefaultModules();
    }

    public void onCreate(Context context, String appKey) {
        Context applicationContext = context.getApplicationContext();
        Iterator var4 = this.extensionModules.iterator();

        while(var4.hasNext()) {
            IMLibExtensionModule module = (IMLibExtensionModule)var4.next();
            module.onCreate(applicationContext, appKey);
        }

    }

    void onConnected(String userId, String token) {
        if (!TextUtils.equals(this.userId, userId)) {
            this.userId = userId;
            this.token = token;
            Iterator var3 = this.extensionModules.iterator();

            while(var3.hasNext()) {
                IMLibExtensionModule module = (IMLibExtensionModule)var3.next();
                module.onLogin(userId, token);
            }
        }

    }

    public void onLogin(String userId, String token) {
    }

    public boolean onReceiveMessage(Message message, int left, boolean offline, int cmdLeft) {
        boolean handled = false;
        Iterator var6 = this.extensionModuleMessageContentMap.entrySet().iterator();

        while(var6.hasNext()) {
            Entry<IMLibExtensionModule, List<Class<? extends MessageContent>>> entry = (Entry)var6.next();
            if (((List)entry.getValue()).contains(message.getContent().getClass())) {
                handled = ((IMLibExtensionModule)entry.getKey()).onReceiveMessage(message, left, offline, cmdLeft);
                if (handled) {
                    break;
                }
            }
        }

        return handled;
    }

    public List<Class<? extends MessageContent>> getMessageContentList() {
        return this.messageContentClazzList;
    }

    public List<Class<? extends MessageContent>> getCmdMessageContentList() {
        return this.cmdMessageContentClazzList;
    }

    public void onConnectStatusChanged(ConnectionStatus status) {
        Iterator var2 = this.extensionModules.iterator();

        while(var2.hasNext()) {
            IMLibExtensionModule module = (IMLibExtensionModule)var2.next();
            module.onConnectStatusChanged(status);
        }

    }

    public void onLogout() {
        this.userId = null;
        this.token = null;
        Iterator var1 = this.extensionModules.iterator();

        while(var1.hasNext()) {
            IMLibExtensionModule module = (IMLibExtensionModule)var1.next();
            module.onLogout();
        }

    }

    public void onDestroy() {
        Iterator var1 = this.extensionModules.iterator();

        while(var1.hasNext()) {
            IMLibExtensionModule module = (IMLibExtensionModule)var1.next();
            module.onDestroy();
        }

    }

    public boolean onRequestHardwareResource(ResourceType resourceType) {
        boolean handled = false;
        Iterator var3 = this.extensionModules.iterator();

        while(var3.hasNext()) {
            IMLibExtensionModule module = (IMLibExtensionModule)var3.next();
            handled = module.onRequestHardwareResource(resourceType);
            if (handled) {
                RLog.i("IMLibExtensionModuleManager", "handledClass:" + module.getClass().getSimpleName());
                break;
            }
        }

        return handled;
    }

    private void loadDefaultModules() {
        String[] var1 = defaultModules;
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            String moduleClazzName = var1[var3];
            this.loadIMLibModuleByName(moduleClazzName);
        }

    }

    void init() {
        this.extensionModules = new ArrayList();
        this.messageContentClazzList = new ArrayList();
        this.cmdMessageContentClazzList = new ArrayList();
        this.extensionModuleMessageContentMap = new HashMap();
    }

    private void loadIMLibModuleByName(String moduleClazzName) {
        try {
            Class<?> cls = Class.forName(moduleClazzName);
            Constructor<?> constructor = cls.getConstructor();
            IMLibExtensionModule module = (IMLibExtensionModule)constructor.newInstance();
            this.registerInternal(module);
        } catch (Exception var5) {
        }

    }

    private void registerInternal(IMLibExtensionModule module) {
        if (module != null) {
            this.extensionModules.add(module);
            if (module.getMessageContentList() != null) {
                this.messageContentClazzList.addAll(module.getMessageContentList());
                this.extensionModuleMessageContentMap.put(module, module.getMessageContentList());
            }

            if (module.getCmdMessageContentList() != null) {
                this.cmdMessageContentClazzList.addAll(module.getMessageContentList());
            }

        }
    }

    private void unRegisterInternal(IMLibExtensionModule module) {
        if (module != null) {
            this.extensionModules.remove(module);
            if (module.getMessageContentList() != null) {
                this.messageContentClazzList.removeAll(module.getMessageContentList());
            }

            if (module.getCmdMessageContentList() != null) {
                this.cmdMessageContentClazzList.removeAll(module.getCmdMessageContentList());
            }

        }
    }

    private static class IMLibExtensionModuleManagerHolder {
        private static IMLibExtensionModuleManager instance = new IMLibExtensionModuleManager();

        private IMLibExtensionModuleManagerHolder() {
        }
    }
}
