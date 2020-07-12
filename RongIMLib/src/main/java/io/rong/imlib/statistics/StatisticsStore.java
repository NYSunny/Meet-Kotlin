//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StatisticsStore {
    private static final String PREFERENCES = "COUNTLY_STORE";
    private static final String DELIMITER = ":::";
    private static final String CONNECTIONS_PREFERENCE = "CONNECTIONS";
    private static final String LATEST_UPLOAD = "LATEST";
    private static final String EVENTS_PREFERENCE = "EVENTS";
    private static final String LOCATION_PREFERENCE = "LOCATION";
    private static final int UPLOAD_DURATION = 86400;
    private final SharedPreferences preferences_;

    StatisticsStore(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("must provide valid context");
        } else {
            this.preferences_ = context.getSharedPreferences("COUNTLY_STORE", 0);
        }
    }

    public String[] connections() {
        String joinedConnStr = this.preferences_.getString("CONNECTIONS", "");
        return joinedConnStr.length() == 0 ? new String[0] : joinedConnStr.split(":::");
    }

    public String[] events() {
        String joinedEventsStr = this.preferences_.getString("EVENTS", "");
        return joinedEventsStr.length() == 0 ? new String[0] : joinedEventsStr.split(":::");
    }

    public List<Event> eventsList() {
        String[] array = this.events();
        List<Event> events = new ArrayList(array.length);
        String[] var3 = array;
        int var4 = array.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];

            try {
                Event event = Event.fromJSON(new JSONObject(s));
                if (event != null) {
                    events.add(event);
                }
            } catch (JSONException var8) {
            }
        }

        Collections.sort(events, new Comparator<Event>() {
            public int compare(Event e1, Event e2) {
                return e1.timestamp - e2.timestamp;
            }
        });
        return events;
    }

    public boolean isEmptyConnections() {
        return this.preferences_.getString("CONNECTIONS", "").length() == 0;
    }

    public synchronized void addConnection(String str) {
        if (str != null && str.length() > 0) {
            List<String> connections = new ArrayList(Arrays.asList(this.connections()));
            connections.add(str);
            this.preferences_.edit().putString("CONNECTIONS", join(connections, ":::")).commit();
        }

    }

    public boolean uploadIfNeed() {
        int current = Statistics.currentTimestamp();
        int time = this.preferences_.getInt("LATEST", 0);
        if (Statistics.sharedInstance().isLoggingEnabled()) {
            Log.w("Statistics", "uploadIfNeed : last = " + time + ", current = " + current);
        }

        if (time == 0) {
            this.updateLatestUploadTime();
            return true;
        } else {
            time += 86400;
            if (time <= current) {
                this.updateLatestUploadTime();
                return true;
            } else {
                return false;
            }
        }
    }

    public void updateLatestUploadTime() {
        this.preferences_.edit().putInt("LATEST", Statistics.currentTimestamp()).commit();
    }

    public synchronized void removeConnection(String str) {
        if (str != null && str.length() > 0) {
            List<String> connections = new ArrayList(Arrays.asList(this.connections()));
            if (connections.remove(str)) {
                this.preferences_.edit().putString("CONNECTIONS", join(connections, ":::")).commit();
            }
        }

    }

    void addEvent(Event event) {
        List<Event> events = this.eventsList();
        events.add(event);
        this.preferences_.edit().putString("EVENTS", joinEvents(events, ":::")).commit();
    }

    void setLocation(double lat, double lon) {
        this.preferences_.edit().putString("LOCATION", lat + "," + lon).commit();
    }

    String getAndRemoveLocation() {
        String location = this.preferences_.getString("LOCATION", "");
        if (!location.equals("")) {
            this.preferences_.edit().remove("LOCATION").commit();
        }

        return location;
    }

    public synchronized void addEvent(String key, Map<String, String> segmentation, int timestamp, int count, double sum) {
        Event event = new Event();
        event.key = key;
        event.segmentation = segmentation;
        event.timestamp = timestamp;
        event.count = count;
        event.sum = sum;
        this.addEvent(event);
    }

    public synchronized void removeEvents(Collection<Event> eventsToRemove) {
        if (eventsToRemove != null && eventsToRemove.size() > 0) {
            List<Event> events = this.eventsList();
            if (events.removeAll(eventsToRemove)) {
                this.preferences_.edit().putString("EVENTS", joinEvents(events, ":::")).commit();
            }
        }

    }

    static String joinEvents(Collection<Event> collection, String delimiter) {
        List<String> strings = new ArrayList();
        Iterator var3 = collection.iterator();

        while(var3.hasNext()) {
            Event e = (Event)var3.next();
            strings.add(e.toJSON().toString());
        }

        return join(strings, delimiter);
    }

    static String join(Collection<String> collection, String delimiter) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        Iterator var4 = collection.iterator();

        while(var4.hasNext()) {
            String s = (String)var4.next();
            builder.append(s);
            ++i;
            if (i < collection.size()) {
                builder.append(delimiter);
            }
        }

        return builder.toString();
    }

    public synchronized String getPreference(String key) {
        return this.preferences_.getString(key, (String)null);
    }

    public synchronized void setPreference(String key, String value) {
        if (value == null) {
            this.preferences_.edit().remove(key).commit();
        } else {
            this.preferences_.edit().putString(key, value).commit();
        }

    }

    synchronized void clear() {
        Editor prefsEditor = this.preferences_.edit();
        prefsEditor.remove("EVENTS");
        prefsEditor.remove("CONNECTIONS");
        prefsEditor.commit();
    }
}
