package com.example.cropcare.helper;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;

import com.example.cropcare.AddNewTaskActivity;
import com.example.cropcare.Model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeHelper {

    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(long millis);
    }

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

    public static long getNextDate(TaskModel task) {
        int repeatDays = task.getRepeatEveryDays();
        long newStartTime;

        long currentTime = task.getStartTime();
        newStartTime = Math.max(task.getStartTime() + (repeatDays * 86400000L), currentTime + 86400000L);
        return newStartTime;
    }

    public static void showDateTimePicker(Context context, OnDateTimeSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view1, hourOfDay, minute) -> {

                // Convert selected date and time to milliseconds
                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                long millis = selectedDateTime.getTimeInMillis();

                listener.onDateTimeSelected(millis); // Pass the millis to the callback

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);//start time
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));//start date
        datePickerDialog.show();
    }

}
