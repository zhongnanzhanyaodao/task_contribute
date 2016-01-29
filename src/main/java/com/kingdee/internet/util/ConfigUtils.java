package com.kingdee.internet.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtils {
    @Value("${task.queue.expire.minutes}")
    private long queueExpireMinutes = 10;

    @Value("${task.expire.minutes}")
    private int taskExpireMinutes = 10;

    public long queueExpireMinutes() {
        return queueExpireMinutes;
    }

    public int taskExpireMinutes() {
        return taskExpireMinutes;
    }
}
