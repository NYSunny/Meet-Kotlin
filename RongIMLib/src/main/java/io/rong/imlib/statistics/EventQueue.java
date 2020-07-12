//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.statistics;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EventQueue {
    private final StatisticsStore statisticsStore_;

    EventQueue(StatisticsStore statisticsStore) {
        this.statisticsStore_ = statisticsStore;
    }

    int size() {
        return this.statisticsStore_.events().length;
    }

    String events() {
        List<Event> events = this.statisticsStore_.eventsList();
        JSONArray eventArray = new JSONArray();
        Iterator var4 = events.iterator();

        while(var4.hasNext()) {
            Event e = (Event)var4.next();
            eventArray.put(e.toJSON());
        }

        String result = eventArray.toString();
        this.statisticsStore_.removeEvents(events);

        try {
            result = URLEncoder.encode(result, "UTF-8");
        } catch (UnsupportedEncodingException var6) {
        }

        return result;
    }

    void recordEvent(String key, Map<String, String> segmentation, int count, double sum) {
        int timestamp = Statistics.currentTimestamp();
        this.statisticsStore_.addEvent(key, segmentation, timestamp, count, sum);
    }

    StatisticsStore getCountlyStore() {
        return this.statisticsStore_;
    }
}
