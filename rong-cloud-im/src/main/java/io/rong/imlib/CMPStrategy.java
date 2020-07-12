//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import io.rong.common.RLog;
import io.rong.imlib.common.DeviceUtils;
import io.rong.imlib.navigation.NavigationCacheHelper;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CMPStrategy {
    private static final String TAG = CMPStrategy.class.getSimpleName();
    private Context context;
    private String connectNetInfo = "";
    private List<String> cmpList = new ArrayList();
    private volatile boolean isConnectedState;

    public CMPStrategy() {
    }

    public static CMPStrategy getInstance() {
        return CMPStrategy.SingletonHolder.sIns;
    }

    void setEnvironment(Context context) {
        synchronized(this) {
            this.context = context;
            this.cmpList = NavigationCacheHelper.getConnectionCmpList(context);
        }
    }

    public void onGetCmpEntriesFromNavi() {
        synchronized(this) {
            this.cmpList = NavigationCacheHelper.getConnectionCmpList(this.context);
            RLog.d(TAG, "get cmp list from navi:" + this.cmpList.toString());
        }
    }

    void setMainCMP(String cmp) {
        if (this.cmpList.size() > 1) {
            synchronized(this) {
                List<String> tmpCmpList = this.cmpList;

                for(int i = 0; i < tmpCmpList.size(); ++i) {
                    String c = (String)tmpCmpList.get(i);
                    if (cmp.equals(c)) {
                        if (i == 0) {
                            return;
                        }

                        this.cmpList.remove(i);
                        this.cmpList.add(0, cmp);
                        return;
                    }
                }

            }
        }
    }

    void onConnected() {
        this.connectNetInfo = DeviceUtils.getNetworkType(this.context);
        this.isConnectedState = true;
    }

    void updateCmpList() {
        synchronized(this) {
            if (this.cmpList.size() > 1) {
                this.cmpList.add(this.cmpList.remove(0));
            }

        }
    }

    public List<String> getCmpList() {
        synchronized(this) {
            new ArrayList();
            String currentNetInfo = DeviceUtils.getNetworkType(this.context);
            if (currentNetInfo.equals(this.connectNetInfo) && this.cmpList != null && this.cmpList.size() > 0) {
                RLog.d(TAG, "getCmpEntries from cache.");
            } else {
                RLog.d(TAG, "getCmpEntries from SP.");
                this.cmpList = NavigationCacheHelper.getConnectionCmpList(this.context);
            }

            List<String> cmps = this.cmpList;
            return cmps;
        }
    }

    public boolean isCMPValid(String cmp) {
        if (TextUtils.isEmpty(cmp)) {
            return false;
        } else {
            String str = cmp;
            if (!cmp.startsWith("http")) {
                str = "http://" + cmp;
            }

            try {
                URL url = new URL(str);
                if (url.getHost() != null && url.getPort() >= 0) {
                    return true;
                }
            } catch (MalformedURLException var4) {
                var4.printStackTrace();
            }

            return false;
        }
    }

    void clearCache(Context context) {
        synchronized(this) {
            this.cmpList.clear();
            NavigationCacheHelper.clearCache(context);
        }
    }

    private static class SingletonHolder {
        @SuppressLint({"StaticFieldLeak"})
        private static final CMPStrategy sIns = new CMPStrategy();

        private SingletonHolder() {
        }
    }
}
