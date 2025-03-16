package com.coxifred.pimmyandroidpip.beans;

public class CoxifredSleep extends AbstractMessage {
    Long timeToSleep=10000L;

    public Long getTimeToSleep() {
        return timeToSleep;
    }

    public void setTimeToSleep(Long timeToSleep) {
        this.timeToSleep = timeToSleep;
    }
}
