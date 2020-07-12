//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.httpdns;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.rong.common.rlog.RLog;

public class RongNetworkStateChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "RongNetworkStateChangeReceiver";
    private boolean isFirstNotify = false;
    private boolean clearCache = true;
    private boolean httpDnsPrefetch = true;
    private String beforeNetInfo = "";
    private boolean isIPv6Reachable = true;
    private boolean isIPv4Reachable = true;
    private long clearCacheStartTime;
    private static final long ALLOW_CLEAR_CACHE_TIMEOUT = 600000L;

    public RongNetworkStateChangeReceiver() {
    }

    void setClearCache(boolean clearCache) {
        this.clearCache = clearCache;
    }

    void setHttpDnsPrefetch(boolean httpDnsPrefetch) {
        this.httpDnsPrefetch = httpDnsPrefetch;
    }

    private void processCacheOnNetworkChange(Context context) {
        Logger.printLog("Network change, clearCache(%b) httpDnsPrefetch(%b)", new Object[]{this.clearCache, this.httpDnsPrefetch});
        HttpDnsClient httpDnsClient = HttpDnsClient.getInstance();
        RongHttpDns httpDns = RongHttpDns.getService(context);
        this.refreshIpReachable();
        ArrayList<String> hosts = httpDns.getHttpDnsCache().getAllHosts();
        long curTime = System.currentTimeMillis();
        if (this.clearCache && this.allowClearCache(curTime)) {
            this.clearCacheStartTime = System.currentTimeMillis();
            httpDns.getHttpDnsCache().clearHostCacheMemory();
        }

        if (this.httpDnsPrefetch) {
            if (this.isIPv6Only()) {
                Logger.printLog("Now the network is Ipv6 Only, Will not send prefetch request. ", new Object[0]);
                return;
            }

            if (hosts == null || hosts.isEmpty()) {
                return;
            }

            HttpDnsCompletion httpDnsCompletion = new HttpDnsCompletion(context);
            httpDnsClient.splitHostsAndSendRequest(hosts, httpDnsCompletion);
        }

    }

    public void onReceive(Context context, Intent intent) {
        if (!this.isFirstNotify) {
            this.isFirstNotify = true;
        } else {
            String netInfo = "";

            try {
                ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService("connectivity");
                if (connMgr == null) {
                    this.processCacheOnNetworkChange(context);
                    return;
                }

                NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(1);
                NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(0);
                if (wifiNetworkInfo != null && wifiNetworkInfo.getExtraInfo() != null && wifiNetworkInfo.getState() == State.CONNECTED) {
                    netInfo = wifiNetworkInfo.getExtraInfo();
                } else if (dataNetworkInfo != null && dataNetworkInfo.getExtraInfo() != null && dataNetworkInfo.getState() == State.CONNECTED) {
                    netInfo = dataNetworkInfo.getExtraInfo();
                }

                if (!this.beforeNetInfo.equals(netInfo) && !netInfo.equals("")) {
                    Logger.printLog("Current net type: %s.", new Object[]{netInfo});
                    this.processCacheOnNetworkChange(context);
                }
            } catch (RuntimeException var8) {
                RLog.e("RongNetworkStateChangeReceiver", "onReceive RuntimeException", var8);

                try {
                    this.processCacheOnNetworkChange(context);
                } catch (Exception var7) {
                    RLog.e("RongNetworkStateChangeReceiver", "processCacheOnNetworkChange Exception", var7);
                }
            }

            this.beforeNetInfo = netInfo;
        }
    }

    public boolean isIPv6Only() {
        return !this.isIPv4Reachable && this.isIPv6Reachable;
    }

    public void refreshIpReachable() {
        RefreshIpReachableTask task = new RefreshIpReachableTask();
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.submit(task);
    }

    private boolean allowClearCache(long curTime) {
        return curTime - this.clearCacheStartTime > 600000L;
    }

    class RefreshIpReachableTask implements Callable<Object> {
        RefreshIpReachableTask() {
        }

        public Object call() {
            SocketAddress ipv6Address = new InetSocketAddress("2001:4860:4860::8888", 443);
            SocketAddress ipv4Address = new InetSocketAddress("180.76.76.76", 80);
            DatagramSocket datagramSocket = null;

            try {
                datagramSocket = new DatagramSocket();
                datagramSocket.connect(ipv4Address);
            } catch (SocketException var16) {
                RongNetworkStateChangeReceiver.this.isIPv4Reachable = false;
            } finally {
                if (datagramSocket != null) {
                    datagramSocket.close();
                }

            }

            try {
                datagramSocket = new DatagramSocket();
                datagramSocket.connect(ipv6Address);
            } catch (SocketException var15) {
                RongNetworkStateChangeReceiver.this.isIPv6Reachable = false;
            } finally {
                if (datagramSocket != null) {
                    datagramSocket.close();
                }

            }

            Logger.printLog("isIPv4Reachable(%s), isIPv6Reachable(%s)", new Object[]{RongNetworkStateChangeReceiver.this.isIPv4Reachable, RongNetworkStateChangeReceiver.this.isIPv6Reachable});
            return null;
        }
    }
}
