//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.rtlog.upload;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

class RtLogCache {
    private Context context;
    private SharedPreferences preferences;
    private LinkedHashMap<String, FullUploadLogCache> fullUploadLogCacheMap = new LinkedHashMap();
    private volatile boolean isMapToSp;

    RtLogCache(Context context, String appKey) {
        this.context = context;
        this.preferences = context.getSharedPreferences("rt_log_cache_" + appKey, 0);
    }

    String loadTimingUploadConfigCache() {
        return this.preferences.getString("log_config", "default_config");
    }

    void saveTimingUploadConfig(String configJson) {
        Editor edit = this.preferences.edit();
        edit.putString("log_config", configJson);
        edit.apply();
    }

    List<FullUploadLogCache> loadFullUploadLogCacheList() {
        if (!this.isMapToSp) {
            this.isMapToSp = true;
            Set<String> cacheCsvSet = this.preferences.getStringSet("full_log_cache", (Set)null);
            if (cacheCsvSet == null) {
                return new ArrayList();
            }

            Iterator var2 = cacheCsvSet.iterator();

            while(var2.hasNext()) {
                String cacheCsv = (String)var2.next();
                FullUploadLogCache cache = FullUploadLogCache.parseFromCSV(cacheCsv);
                if (cache != null && !TextUtils.isEmpty(cache.getLogId())) {
                    this.fullUploadLogCacheMap.put(cache.getLogId(), cache);
                }
            }
        }

        return new ArrayList(this.fullUploadLogCacheMap.values());
    }

    synchronized void addFullUploadTaskCache(String version, String deviceId, String appKey, String uri, String userId, String logId, long startTime, long endTime) {
        FullUploadLogCache cache = new FullUploadLogCache(version, deviceId, appKey, uri, userId, logId, startTime, endTime);
        this.fullUploadLogCacheMap.remove(cache.getLogId());
        this.fullUploadLogCacheMap.put(cache.getLogId(), cache);
        Editor edit = this.preferences.edit();
        edit.putStringSet("full_log_cache", this.convertFullUploadTaskCacheToCsvSet());
        edit.apply();
    }

    synchronized void removeFullUploadTaskCache(String logId) {
        this.fullUploadLogCacheMap.remove(logId);
        Editor edit = this.preferences.edit();
        edit.putStringSet("full_log_cache", this.convertFullUploadTaskCacheToCsvSet());
        edit.apply();
    }

    private Set<String> convertFullUploadTaskCacheToCsvSet() {
        Set<String> csvSet = new HashSet();
        Iterator var2 = this.fullUploadLogCacheMap.values().iterator();

        while(var2.hasNext()) {
            FullUploadLogCache cache = (FullUploadLogCache)var2.next();
            csvSet.add(cache.toCSV());
        }

        return csvSet;
    }
}
