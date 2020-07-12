//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;
import android.util.Log;

public class DeviceId {
    private static final String TAG = "DeviceId";
    private static final String PREFERENCE_KEY_ID_TYPE = "ly.count.android.api.DeviceId.type";
    private String id;
    private Type type;

    public DeviceId(Type type) {
        if (type == null) {
            throw new IllegalStateException("Please specify DeviceId.Type, that is which type of device ID generation you want to use");
        } else if (type == Type.DEVELOPER_SUPPLIED) {
            throw new IllegalStateException("Please use another DeviceId constructor for device IDs supplied by developer");
        } else {
            this.type = type;
        }
    }

    public DeviceId(String developerSuppliedId) {
        if (developerSuppliedId != null && !"".equals(developerSuppliedId)) {
            this.type = Type.DEVELOPER_SUPPLIED;
            this.id = developerSuppliedId;
        } else {
            throw new IllegalStateException("Please make sure that device ID is not null or empty");
        }
    }

    public void init(Context context, StatisticsStore store, boolean raiseExceptions) {
        Type overriddenType = this.retrieveOverriddenType(store);
        if (overriddenType != null && overriddenType != this.type) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.i("DeviceId", "Overridden device ID generation strategy detected: " + overriddenType + ", using it instead of " + this.type);
            }

            this.type = overriddenType;
        }

        switch(this.type) {
            case DEVELOPER_SUPPLIED:
            default:
                break;
            case OPEN_UDID:
                if (OpenUDIDAdapter.isOpenUDIDAvailable()) {
                    if (Statistics.sharedInstance().isLoggingEnabled()) {
                        Log.i("DeviceId", "Using OpenUDID");
                    }

                    if (!OpenUDIDAdapter.isInitialized()) {
                        OpenUDIDAdapter.sync(context);
                    }
                } else if (raiseExceptions) {
                    throw new IllegalStateException("OpenUDID is not available, please make sure that you have it in your classpath");
                }
                break;
            case ADVERTISING_ID:
                if (AdvertisingIdAdapter.isAdvertisingIdAvailable()) {
                    if (Statistics.sharedInstance().isLoggingEnabled()) {
                        Log.i("DeviceId", "Using Advertising ID");
                    }

                    AdvertisingIdAdapter.setAdvertisingId(context, store, this);
                } else if (OpenUDIDAdapter.isOpenUDIDAvailable()) {
                    if (Statistics.sharedInstance().isLoggingEnabled()) {
                        Log.i("DeviceId", "Advertising ID is not available, falling back to OpenUDID");
                    }

                    if (!OpenUDIDAdapter.isInitialized()) {
                        OpenUDIDAdapter.sync(context);
                    }
                } else {
                    if (Statistics.sharedInstance().isLoggingEnabled()) {
                        Log.w("DeviceId", "Advertising ID is not available, neither OpenUDID is");
                    }

                    if (raiseExceptions) {
                        throw new IllegalStateException("OpenUDID is not available, please make sure that you have it in your classpath");
                    }
                }
        }

    }

    private void storeOverriddenType(StatisticsStore store, Type type) {
        store.setPreference("ly.count.android.api.DeviceId.type", type == null ? null : type.toString());
    }

    private Type retrieveOverriddenType(StatisticsStore store) {
        String oldTypeString = store.getPreference("ly.count.android.api.DeviceId.type");
        Type oldType;
        if (oldTypeString == null) {
            oldType = null;
        } else if (oldTypeString.equals(Type.DEVELOPER_SUPPLIED.toString())) {
            oldType = Type.DEVELOPER_SUPPLIED;
        } else if (oldTypeString.equals(Type.OPEN_UDID.toString())) {
            oldType = Type.OPEN_UDID;
        } else if (oldTypeString.equals(Type.ADVERTISING_ID.toString())) {
            oldType = Type.ADVERTISING_ID;
        } else {
            oldType = null;
        }

        return oldType;
    }

    public String getId() {
        if (this.id == null && this.type == Type.OPEN_UDID) {
            this.id = OpenUDIDAdapter.getOpenUDID();
        }

        return this.id;
    }

    protected void setId(Type type, String id) {
        if (Statistics.sharedInstance().isLoggingEnabled()) {
            Log.w("DeviceId", "Device ID is " + id + " (type " + type + ")");
        }

        this.type = type;
        this.id = id;
    }

    protected void switchToIdType(Type type, Context context, StatisticsStore store) {
        if (Statistics.sharedInstance().isLoggingEnabled()) {
            Log.w("DeviceId", "Switching to device ID generation strategy " + type + " from " + this.type);
        }

        this.type = type;
        this.storeOverriddenType(store, type);
        this.init(context, store, false);
    }

    public Type getType() {
        return this.type;
    }

    static boolean deviceIDEqualsNullSafe(String id, Type type, DeviceId deviceId) {
        if (type != null && type != Type.DEVELOPER_SUPPLIED) {
            return true;
        } else {
            String deviceIdId = deviceId == null ? null : deviceId.getId();
            return deviceIdId == null && id == null || deviceIdId != null && deviceIdId.equals(id);
        }
    }

    public static enum Type {
        DEVELOPER_SUPPLIED,
        OPEN_UDID,
        ADVERTISING_ID;

        private Type() {
        }
    }
}
