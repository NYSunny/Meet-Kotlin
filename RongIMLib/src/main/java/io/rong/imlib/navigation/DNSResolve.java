//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.navigation;

import io.rong.common.RLog;
import io.rong.common.fwlog.FwLog;
import io.rong.common.fwlog.FwLog.LogTag;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DNSResolve {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final String TAG = "DNSResolve";
    private InetAddress address = null;
    private String host;
    private CountDownLatch cdl = new CountDownLatch(1);

    DNSResolve(String host) {
        this.host = host;
    }

    void resolveHost() {
        executorService.execute(new Runnable() {
            public void run() {
                try {
                    if (DNSResolve.this.host.toLowerCase().startsWith("http") || DNSResolve.this.host.toLowerCase().startsWith("https")) {
                        URL url = new URL(DNSResolve.this.host);
                        DNSResolve.this.host = url.getHost();
                    }

                    InetAddress address = InetAddress.getByName(DNSResolve.this.host);
                    DNSResolve.this.set(address);
                    DNSResolve.this.cdl.countDown();
                } catch (MalformedURLException var3) {
                    FwLog.write(1, 1, LogTag.L_DNS_PARSE_F.getTag(), "catch", new Object[]{"MalformedURLException"});
                    RLog.e("DNSResolve", "MalformedURLException ", var3);
                } catch (UnknownHostException var4) {
                    FwLog.write(1, 1, LogTag.L_DNS_PARSE_F.getTag(), "catch", new Object[]{"UnknownHostException"});
                    RLog.e("DNSResolve", "UnknownHostException ", var4);
                } catch (Exception var5) {
                    FwLog.write(1, 1, LogTag.L_DNS_PARSE_F.getTag(), "catch|stacks", new Object[]{"Exception", FwLog.stackToString(var5)});
                }

            }
        });
    }

    private void set(InetAddress address) {
        this.address = address;
    }

    public String getIP() {
        try {
            if (this.cdl.await(500L, TimeUnit.MILLISECONDS)) {
                RLog.i("DNSResolve", "getIP countDownLatch is success");
            } else {
                RLog.i("DNSResolve", "getIP countDownLatch is timeout");
            }
        } catch (InterruptedException var2) {
            RLog.e("DNSResolve", "getIP", var2);
            Thread.currentThread().interrupt();
        }

        return null != this.address ? this.address.getHostAddress() : null;
    }
}
