package com.example.cropcare.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConverter {
    public static String convertMillisToDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        Date date = new Date(millis);
        return sdf.format(date);
    }
}
