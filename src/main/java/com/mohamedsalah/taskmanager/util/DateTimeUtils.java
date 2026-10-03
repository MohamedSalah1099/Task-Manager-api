package com.mohamedsalah.taskmanager.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Utility methods for date and time operations.
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static String formatDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
