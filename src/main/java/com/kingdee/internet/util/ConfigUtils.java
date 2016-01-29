package com.kingdee.internet.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigUtils {
    @Value("${task.queue.expire.minutes:10}")
    private long queueExpireMinutes;

    @Value("${task.expire.minutes:10}")
    private int taskExpireMinutes;

    @Value("${not.exists:'Not Exists'}")
    private String notExists;

    public long queueExpireMinutes() {
        return queueExpireMinutes;
    }

    public int taskExpireMinutes() {
        return taskExpireMinutes;
    }
}
