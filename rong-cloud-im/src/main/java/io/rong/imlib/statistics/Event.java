//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class Event {
    private static final String SEGMENTATION_KEY = "segmentation";
    private static final String KEY_KEY = "key";
    private static final String COUNT_KEY = "count";
    private static final String SUM_KEY = "sum";
    private static final String TIMESTAMP_KEY = "timestamp";
    public String key;
    public Map<String, String> segmentation;
    public int count;
    public double sum;
    public int timestamp;

    Event() {
    }

    JSONObject toJSON() {
        JSONObject json = new JSONObject();

        try {
            json.put("key", this.key);
            json.put("count", this.count);
            json.put("timestamp", this.timestamp);
            if (this.segmentation != null) {
                json.put("segmentation", new JSONObject(this.segmentation));
            }

            json.put("sum", this.sum);
        } catch (JSONException var3) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.w("Statistics", "Got exception converting an Event to JSON", var3);
            }
        }

        return json;
    }

    static Event fromJSON(JSONObject json) {
        Event event = new Event();

        try {
            if (!json.isNull("key")) {
                event.key = json.getString("key");
            }

            event.count = json.optInt("count");
            event.sum = json.optDouble("sum", 0.0D);
            event.timestamp = json.optInt("timestamp");
            if (!json.isNull("segmentation")) {
                JSONObject segm = json.getJSONObject("segmentation");
                HashMap<String, String> segmentation = new HashMap(segm.length());
                Iterator nameItr = segm.keys();

                while(nameItr.hasNext()) {
                    String key = (String)nameItr.next();
                    if (!segm.isNull(key)) {
                        segmentation.put(key, segm.getString(key));
                    }
                }

                event.segmentation = segmentation;
            }
        } catch (JSONException var6) {
            if (Statistics.sharedInstance().isLoggingEnabled()) {
                Log.w("Statistics", "Got exception converting JSON to an Event", var6);
            }

            event = null;
        }

        return event != null && event.key != null && event.key.length() > 0 ? event : null;
    }

    public boolean equals(Object o) {
        if (o != null && o instanceof Event) {
            boolean var10000;
            label40: {
                label29: {
                    Event e = (Event)o;
                    if (this.key == null) {
                        if (e.key != null) {
                            break label29;
                        }
                    } else if (!this.key.equals(e.key)) {
                        break label29;
                    }

                    if (this.timestamp == e.timestamp) {
                        if (this.segmentation == null) {
                            if (e.segmentation == null) {
                                break label40;
                            }
                        } else if (this.segmentation.equals(e.segmentation)) {
                            break label40;
                        }
                    }
                }

                var10000 = false;
                return var10000;
            }

            var10000 = true;
            return var10000;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (this.key != null ? this.key.hashCode() : 1) ^ (this.segmentation != null ? this.segmentation.hashCode() : 1) ^ (this.timestamp != 0 ? this.timestamp : 1);
    }
}
