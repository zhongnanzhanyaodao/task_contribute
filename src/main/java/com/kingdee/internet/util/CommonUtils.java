package com.kingdee.internet.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class CommonUtils {
    private CommonUtils() {
        throw new UnsupportedOperationException();
    }

    public static String uuid() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}
