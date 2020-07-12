//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;
import android.util.Log;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.rong.imlib.statistics.Statistics.CountlyMessagingMode;

public class ConnectionQueue {
    private StatisticsStore store_;
    private ExecutorService executor_;
    private String appKey_;
    private Context context_;
    private String serverURL_;
    private Future<?> connectionProcessorFuture_;
    private DeviceId deviceId_;
    private SSLContext sslContext_;

    public ConnectionQueue() {
    }

    String getAppKey() {
        return this.appKey_;
    }

    void setAppKey(String appKey) {
        this.appKey_ = appKey;
    }

    Context getContext() {
        return this.context_;
    }

    void setContext(Context context) {
        this.context_ = context;
    }

    String getServerURL() {
        return this.serverURL_;
    }

    void setServerURL(String serverURL) {
        this.serverURL_ = serverURL;
        if (!serverURL.startsWith("https")) {
            this.sslContext_ = null;
        } else {
            try {
                Log.d("Statistics", "init ssl");
                TrustManager[] tm = new TrustManager[]{new X509TrustManager() {
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }};
                this.sslContext_ = SSLContext.getInstance("TLS");
                this.sslContext_.init((KeyManager[])null, tm, (SecureRandom)null);
            } catch (Throwable var3) {
                throw new IllegalStateException(var3);
            }
        }

    }

    StatisticsStore getCountlyStore() {
        return this.store_;
    }

    void setCountlyStore(StatisticsStore statisticsStore) {
        this.store_ = statisticsStore;
    }

    DeviceId getDeviceId() {
        return this.deviceId_;
    }

    public void setDeviceId(DeviceId deviceId) {
        this.deviceId_ = deviceId;
    }

    void checkInternalState() {
        if (this.context_ == null) {
            throw new IllegalStateException("context has not been set");
        } else if (this.appKey_ != null && this.appKey_.length() != 0) {
            if (this.store_ == null) {
                throw new IllegalStateException("countly store has not been set");
            } else if (this.serverURL_ == null || !Statistics.isValidURL(this.serverURL_)) {
                throw new IllegalStateException("server URL is not valid");
            }
        } else {
            throw new IllegalStateException("app key has not been set");
        }
    }

    void beginSession() {
        if (this.store_.uploadIfNeed()) {
            this.checkInternalState();
            String data = "deviceId=" + this.deviceId_.getId() + "&appKey=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp();
            data = data + "&deviceInfo=" + DeviceInfo.getMetrics(this.context_);
            this.store_.addConnection(data);
            this.tick();
        }

    }

    void updateSession(int duration) {
        this.checkInternalState();
        if (duration > 0) {
            String data = "deviceId=" + this.deviceId_.getId() + "&appKey=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp();
            this.store_.addConnection(data);
            this.tick();
        }

    }

    public void tokenSession(String token, CountlyMessagingMode mode) {
        this.checkInternalState();
        final String data = "app_key=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + "&token_session=1&android_token=" + token + "&test_mode=" + (mode == CountlyMessagingMode.TEST ? 2 : 0) + "&locale=" + DeviceInfo.getLocale();
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        worker.schedule(new Runnable() {
            public void run() {
                ConnectionQueue.this.store_.addConnection(data);
                ConnectionQueue.this.tick();
            }
        }, 10L, TimeUnit.SECONDS);
    }

    void endSession(int duration) {
        this.checkInternalState();
        String data = "app_key=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + "&end_session=1";
        if (duration > 0) {
            data = data + "&session_duration=" + duration;
        }

        this.store_.addConnection(data);
        this.tick();
    }

    void sendUserData() {
        this.checkInternalState();
        String userdata = UserData.getDataForRequest();
        if (!userdata.equals("")) {
            String data = "app_key=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + userdata;
            this.store_.addConnection(data);
            this.tick();
        }

    }

    void sendReferrerData(String referrer) {
        this.checkInternalState();
        if (referrer != null) {
            String data = "app_key=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + referrer;
            this.store_.addConnection(data);
            this.tick();
        }

    }

    void sendCrashReport(String error, boolean nonfatal) {
        this.checkInternalState();
        String data = "app_key=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + "&sdk_version=" + "15.06" + "&crash=" + CrashDetails.getCrashData(this.context_, error, nonfatal);
        this.store_.addConnection(data);
        this.tick();
    }

    void recordEvents(String events) {
        this.checkInternalState();
        String data = "deviceId=" + this.deviceId_.getId() + "&appKey=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + "&pushEvent=" + events;
        this.store_.addConnection(data);
        this.tick();
    }

    void recordEvents(String key, String events) {
        this.checkInternalState();
        String data = "deviceId=" + this.deviceId_.getId() + "&appKey=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + "&" + key + "=" + events;
        this.store_.addConnection(data);
        this.tick();
    }

    void recordLocation(String events) {
        this.checkInternalState();
        String data = "app_key=" + this.appKey_ + "&timestamp=" + Statistics.currentTimestamp() + "&events=" + events;
        this.store_.addConnection(data);
        this.tick();
    }

    void ensureExecutor() {
        if (this.executor_ == null) {
            this.executor_ = Executors.newSingleThreadExecutor();
        }

    }

    void tick() {
        if (!this.store_.isEmptyConnections() && (this.connectionProcessorFuture_ == null || this.connectionProcessorFuture_.isDone())) {
            this.ensureExecutor();
            this.connectionProcessorFuture_ = this.executor_.submit(new ConnectionProcessor(this.serverURL_, this.store_, this.deviceId_, this.sslContext_));
        }

    }

    ExecutorService getExecutor() {
        return this.executor_;
    }

    void setExecutor(ExecutorService executor) {
        this.executor_ = executor;
    }

    Future<?> getConnectionProcessorFuture() {
        return this.connectionProcessorFuture_;
    }

    void setConnectionProcessorFuture(Future<?> connectionProcessorFuture) {
        this.connectionProcessorFuture_ = connectionProcessorFuture;
    }
}
