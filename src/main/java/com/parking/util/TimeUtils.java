package com.parking.util;

import java.time.Duration;
import java.time.Instant;

public class TimeUtils {
    public static boolean isBetweenDates(Instant firstDate, Instant secondDate, Duration duration) {
        return secondDate.isAfter(firstDate.plus(duration));
    }
}
