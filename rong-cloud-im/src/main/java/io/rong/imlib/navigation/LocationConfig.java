//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.rong.imlib.navigation;

public class LocationConfig {
    private boolean configure;
    private int[] conversationTypes;
    private int maxParticipant;
    private int refreshInterval;
    private int distanceFilter;

    public LocationConfig() {
    }

    public boolean isConfigure() {
        return this.configure;
    }

    public void setConfigure(boolean configure) {
        this.configure = configure;
    }

    public int[] getConversationTypes() {
        return this.conversationTypes;
    }

    public void setConversationTypes(int[] conversationTypes) {
        this.conversationTypes = conversationTypes;
    }

    int getMaxParticipant() {
        return this.maxParticipant;
    }

    void setMaxParticipant(int maxParticipant) {
        this.maxParticipant = maxParticipant;
    }

    public int getRefreshInterval() {
        return this.refreshInterval;
    }

    void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public int getDistanceFilter() {
        return this.distanceFilter;
    }

    void setDistanceFilter(int distanceFilter) {
        this.distanceFilter = distanceFilter;
    }
}
