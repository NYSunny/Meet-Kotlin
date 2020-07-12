//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import io.rong.imlib.statistics.DeviceId.Type;

public class Statistics {
    public static final String COUNTLY_SDK_VERSION_STRING = "15.06";
    public static final String DEFAULT_APP_VERSION = "1.0";
    public static final String TAG = "Statistics";
    private static final int EVENT_QUEUE_SIZE_THRESHOLD = 10;
    protected static List<String> publicKeyPinCertificates;
    private ConnectionQueue connectionQueue_ = new ConnectionQueue();
    private ScheduledExecutorService timerService_;
    private EventQueue eventQueue_;
    private DeviceId deviceId_Manager_;
    private long prevSessionDurationStartTime_;
    private int activityCount_;
    private boolean disableUpdateSessionRequests_;
    private boolean enableLogging_;
    private CountlyMessagingMode messagingMode_;
    private Context context_;

    public static Statistics sharedInstance() {
        return SingletonHolder.instance;
    }

    Statistics() {
    }

    public Statistics init(Context context, String serverURL, String appKey) {
        return this.init(context, serverURL, appKey, (String)null, OpenUDIDAdapter.isOpenUDIDAvailable() ? Type.OPEN_UDID : Type.ADVERTISING_ID);
    }

    public Statistics init(Context context, String serverURL, String appKey, String deviceID) {
        return this.init(context, serverURL, appKey, deviceID, (Type)null);
    }

    public synchronized Statistics init(Context context, String serverURL, String appKey, String deviceID, Type idMode) {
        if (context == null) {
            throw new IllegalArgumentException("valid context is required");
        } else if (!isValidURL(serverURL)) {
            throw new IllegalArgumentException("valid serverURL is required");
        } else if (appKey != null && appKey.length() != 0) {
            if (deviceID != null && deviceID.length() == 0) {
                throw new IllegalArgumentException("valid deviceID is required");
            } else {
                if (deviceID == null && idMode == null) {
                    if (OpenUDIDAdapter.isOpenUDIDAvailable()) {
                        idMode = Type.OPEN_UDID;
                    } else if (AdvertisingIdAdapter.isAdvertisingIdAvailable()) {
                        idMode = Type.ADVERTISING_ID;
                    }
                }

                if (deviceID == null && idMode == Type.OPEN_UDID && !OpenUDIDAdapter.isOpenUDIDAvailable()) {
                    throw new IllegalArgumentException("valid deviceID is required because OpenUDID is not available");
                } else if (deviceID == null && idMode == Type.ADVERTISING_ID && !AdvertisingIdAdapter.isAdvertisingIdAvailable()) {
                    throw new IllegalArgumentException("valid deviceID is required because Advertising ID is not available (you need to include Google Play services 4.0+ into your project)");
                } else if (this.eventQueue_ == null || this.connectionQueue_.getServerURL().equals(serverURL) && this.connectionQueue_.getAppKey().equals(appKey) && DeviceId.deviceIDEqualsNullSafe(deviceID, idMode, this.connectionQueue_.getDeviceId())) {
                    if (MessagingAdapter.isMessagingAvailable()) {
                        MessagingAdapter.storeConfiguration(context, serverURL, appKey, deviceID, idMode);
                    }

                    if (this.eventQueue_ == null) {
                        DeviceId deviceIdInstance;
                        if (deviceID != null) {
                            deviceIdInstance = new DeviceId(deviceID);
                        } else {
                            deviceIdInstance = new DeviceId(idMode);
                        }

                        StatisticsStore statisticsStore = new StatisticsStore(context);
                        deviceIdInstance.init(context, statisticsStore, true);
                        this.connectionQueue_.setServerURL(serverURL);
                        this.connectionQueue_.setAppKey(appKey);
                        this.connectionQueue_.setCountlyStore(statisticsStore);
                        this.connectionQueue_.setDeviceId(deviceIdInstance);
                        this.eventQueue_ = new EventQueue(statisticsStore);
                    }

                    this.context_ = context;
                    this.connectionQueue_.setContext(context);
                    return this;
                } else {
                    throw new IllegalStateException("Statistics cannot be reinitialized with different values");
                }
            }
        } else {
            throw new IllegalArgumentException("valid appKey is required");
        }
    }

    public synchronized boolean isInitialized() {
        return this.eventQueue_ != null;
    }

    public synchronized void halt() {
        this.eventQueue_ = null;
        StatisticsStore statisticsStore = this.connectionQueue_.getCountlyStore();
        if (statisticsStore != null) {
            statisticsStore.clear();
        }

        this.connectionQueue_.setContext((Context)null);
        this.connectionQueue_.setServerURL((String)null);
        this.connectionQueue_.setAppKey((String)null);
        this.connectionQueue_.setCountlyStore((StatisticsStore)null);
        this.prevSessionDurationStartTime_ = 0L;
        this.activityCount_ = 0;
    }

    public synchronized void onStart() {
        if (this.eventQueue_ == null) {
            throw new IllegalStateException("init must be called before onStart");
        } else {
            ++this.activityCount_;
            if (this.activityCount_ == 1) {
                Log.i("Statistics", "start");
                this.onStartHelper();
            }

            String referrer = ReferrerReceiver.getReferrer(this.context_);
            if (sharedInstance().isLoggingEnabled()) {
                Log.d("Statistics", "Checking referrer: " + referrer);
            }

            if (referrer != null) {
                this.connectionQueue_.sendReferrerData(referrer);
                ReferrerReceiver.deleteReferrer(this.context_);
            }

            CrashDetails.inForeground();
        }
    }

    void onStartHelper() {
        this.prevSessionDurationStartTime_ = System.nanoTime();
        this.connectionQueue_.beginSession();
    }

    public synchronized void onStop() {
        if (this.eventQueue_ == null) {
            throw new IllegalStateException("init must be called before onStop");
        } else if (this.activityCount_ == 0) {
            throw new IllegalStateException("must call onStart before onStop");
        } else {
            --this.activityCount_;
            if (this.activityCount_ == 0) {
                this.onStopHelper();
            }

            CrashDetails.inBackground();
        }
    }

    void onStopHelper() {
        this.connectionQueue_.endSession(this.roundedSecondsSinceLastSessionDurationUpdate());
        this.prevSessionDurationStartTime_ = 0L;
        if (this.eventQueue_.size() > 0) {
            this.connectionQueue_.recordEvents(this.eventQueue_.events());
        }

    }

    public void onRegistrationId(String registrationId) {
        this.connectionQueue_.tokenSession(registrationId, this.messagingMode_);
    }

    public void recordEvent(String key) {
        this.recordEvent(key, (Map)null, 1, 0.0D);
    }

    public void recordEvent(String key, int count) {
        this.recordEvent(key, (Map)null, count, 0.0D);
    }

    public void recordEvent(String key, int count, double sum) {
        this.recordEvent(key, (Map)null, count, sum);
    }

    public void recordEvent(String key, Map<String, String> segmentation, int count) {
        this.recordEvent(key, segmentation, count, 0.0D);
    }

    public synchronized void recordEvent(String key, Map<String, String> segmentation, int count, double sum) {
        if (!this.isInitialized()) {
            throw new IllegalStateException("Statistics.sharedInstance().init must be called before recordEvent");
        } else if (key != null && key.length() != 0) {
            if (count < 1) {
                throw new IllegalArgumentException("Statistics event count should be greater than zero");
            } else {
                if (segmentation != null) {
                    Iterator var6 = segmentation.keySet().iterator();

                    while(var6.hasNext()) {
                        String k = (String)var6.next();
                        if (k == null || k.length() == 0) {
                            throw new IllegalArgumentException("Statistics event segmentation key cannot be null or empty");
                        }

                        if (segmentation.get(k) == null || ((String)segmentation.get(k)).length() == 0) {
                            throw new IllegalArgumentException("Statistics event segmentation value cannot be null or empty");
                        }
                    }
                }

                this.eventQueue_.recordEvent(key, segmentation, count, sum);
                this.sendEventsIfNeeded();
            }
        } else {
            throw new IllegalArgumentException("Valid Statistics event key is required");
        }
    }

    public synchronized void recordEvent(String key, Map<String, String> segmentation) {
        String[] data = new String[segmentation.size() * 2];
        int i = 0;
        if (!this.isInitialized()) {
            throw new IllegalStateException("Countly.sharedInstance().init must be called before recordEvent");
        } else if (key != null && key.length() != 0) {
            String k;
            if (segmentation != null) {
                for(Iterator var5 = segmentation.keySet().iterator(); var5.hasNext(); ++i) {
                    k = (String)var5.next();
                    if (k == null || k.length() == 0) {
                        throw new IllegalArgumentException("Countly event segmentation key cannot be null or empty");
                    }

                    if (segmentation.get(k) == null || ((String)segmentation.get(k)).length() == 0) {
                        throw new IllegalArgumentException("Countly event segmentation value cannot be null or empty");
                    }

                    data[i] = k;
                    ++i;
                    data[i] = (String)segmentation.get(k);
                }
            }

            JSONObject json = new JSONObject();
            DeviceInfo.fillJSONIfValuesNotEmpty(json, data);
            k = json.toString();

            try {
                k = URLEncoder.encode(k, "UTF-8");
            } catch (UnsupportedEncodingException var8) {
            }

            this.connectionQueue_.recordEvents(key, k);
        } else {
            throw new IllegalArgumentException("Valid Countly event key is required");
        }
    }

    public synchronized Statistics setUserData(Map<String, String> data) {
        return this.setUserData(data, (Map)null);
    }

    public synchronized Statistics setUserData(Map<String, String> data, Map<String, String> customdata) {
        UserData.setData(data);
        if (customdata != null) {
            UserData.setCustomData(customdata);
        }

        this.connectionQueue_.sendUserData();
        return this;
    }

    public synchronized Statistics setCustomUserData(Map<String, String> customdata) {
        if (customdata != null) {
            UserData.setCustomData(customdata);
        }

        this.connectionQueue_.sendUserData();
        return this;
    }

    public synchronized Statistics setLocation(double lat, double lon) {
        this.connectionQueue_.getCountlyStore().setLocation(lat, lon);
        if (this.disableUpdateSessionRequests_) {
            this.connectionQueue_.updateSession(this.roundedSecondsSinceLastSessionDurationUpdate());
        }

        return this;
    }

    public synchronized Statistics setCustomCrashSegments(Map<String, String> segments) {
        if (segments != null) {
            CrashDetails.setCustomSegments(segments);
        }

        return this;
    }

    public synchronized Statistics addCrashLog(String record) {
        CrashDetails.addLog(record);
        return this;
    }

    public synchronized Statistics logException(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        this.connectionQueue_.sendCrashReport(sw.toString(), true);
        return this;
    }

    public synchronized Statistics enableCrashReporting() {
        final UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        UncaughtExceptionHandler handler = new UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                Statistics.this.connectionQueue_.sendCrashReport(sw.toString(), false);
                if (oldHandler != null) {
                    oldHandler.uncaughtException(t, e);
                }

            }
        };
        Thread.setDefaultUncaughtExceptionHandler(handler);
        return this;
    }

    public synchronized Statistics setDisableUpdateSessionRequests(boolean disable) {
        this.disableUpdateSessionRequests_ = disable;
        return this;
    }

    public synchronized Statistics setLoggingEnabled(boolean enableLogging) {
        this.enableLogging_ = enableLogging;
        return this;
    }

    public synchronized boolean isLoggingEnabled() {
        return this.enableLogging_;
    }

    void sendEventsIfNeeded() {
        Log.d("Statistics", "sendEventsIfNeeded: queue=" + this.eventQueue_.size());
        if (this.eventQueue_.size() >= 10) {
            this.connectionQueue_.recordEvents(this.eventQueue_.events());
        }

    }

    synchronized void onTimer() {
        boolean hasActiveSession = this.activityCount_ > 0;
        if (hasActiveSession) {
            Log.d("Statistics", "onTimer: update=" + !this.disableUpdateSessionRequests_ + ", queue=" + this.eventQueue_.size());
            if (!this.disableUpdateSessionRequests_) {
                this.connectionQueue_.updateSession(this.roundedSecondsSinceLastSessionDurationUpdate());
            }

            if (this.eventQueue_.size() > 0) {
                this.connectionQueue_.recordEvents(this.eventQueue_.events());
            }
        }

    }

    int roundedSecondsSinceLastSessionDurationUpdate() {
        long currentTimestampInNanoseconds = System.nanoTime();
        long unsentSessionLengthInNanoseconds = currentTimestampInNanoseconds - this.prevSessionDurationStartTime_;
        this.prevSessionDurationStartTime_ = currentTimestampInNanoseconds;
        return (int)Math.round((double)unsentSessionLengthInNanoseconds / 1.0E9D);
    }

    static int currentTimestamp() {
        return (int)(System.currentTimeMillis() / 1000L);
    }

    static boolean isValidURL(String urlStr) {
        boolean validURL = false;
        if (urlStr != null && urlStr.length() > 0) {
            try {
                new URL(urlStr);
                validURL = true;
            } catch (MalformedURLException var3) {
                validURL = false;
            }
        }

        return validURL;
    }

    public static Statistics enablePublicKeyPinning(List<String> certificates) {
        publicKeyPinCertificates = certificates;
        return sharedInstance();
    }

    ConnectionQueue getConnectionQueue() {
        return this.connectionQueue_;
    }

    void setConnectionQueue(ConnectionQueue connectionQueue) {
        this.connectionQueue_ = connectionQueue;
    }

    ExecutorService getTimerService() {
        return this.timerService_;
    }

    EventQueue getEventQueue() {
        return this.eventQueue_;
    }

    void setEventQueue(EventQueue eventQueue) {
        this.eventQueue_ = eventQueue;
    }

    long getPrevSessionDurationStartTime() {
        return this.prevSessionDurationStartTime_;
    }

    void setPrevSessionDurationStartTime(long prevSessionDurationStartTime) {
        this.prevSessionDurationStartTime_ = prevSessionDurationStartTime;
    }

    int getActivityCount() {
        return this.activityCount_;
    }

    boolean getDisableUpdateSessionRequests() {
        return this.disableUpdateSessionRequests_;
    }

    private static class SingletonHolder {
        static final Statistics instance = new Statistics();

        private SingletonHolder() {
        }
    }

    public static enum CountlyMessagingMode {
        TEST,
        PRODUCTION;

        private CountlyMessagingMode() {
        }
    }
}
