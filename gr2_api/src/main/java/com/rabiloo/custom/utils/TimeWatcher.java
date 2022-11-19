package com.rabiloo.custom.utils;

import java.util.concurrent.TimeUnit;

public class TimeWatcher {
    long starts;

    public static TimeWatcher start() {
        return new TimeWatcher();
    }

    private TimeWatcher() {
        reset();
    }

    public TimeWatcher reset() {
        starts = System.currentTimeMillis();
        return this;
    }

    public long time() {
        long ends = System.currentTimeMillis();
        return ends - starts;
    }

    public long time(TimeUnit unit) {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
