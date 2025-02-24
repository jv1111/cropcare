package com.example.cropcare.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeConverter {
    public static String convertMillisToDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date date = new Date(millis);
        return sdf.format(date);
    }

    public static String convertMillisToCountdown(long millis) {
        if (millis < 0) return "0 days and 00:00:00";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        return String.format("%d days and %02d:%02d:%02d", days, hours, minutes, seconds);
    }
}
