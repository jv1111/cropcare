package com.example.cropcare.helper;

import android.util.Log;

import com.example.cropcare.Model.TaskModel;

public class DateHelper {

    public static long nextDate(TaskModel task) {
        int repeatDays = task.getRepeatEveryDays();
        long newStartTime;

        long currentTime = task.getStartTime();
        newStartTime = Math.max(task.getStartTime() + (repeatDays * 86400000L), currentTime + 86400000L);
        return newStartTime;
    }

}
