package com.example.cropcare.test;

import android.content.Context;
import android.util.Log;

import com.example.cropcare.Auth;
import com.example.cropcare.Database.CropDatabaseHelper;
import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.TimeHelper;

import java.util.List;

public class TestTask {

    public static void deleteAllCrops(Context context) {
        CropDatabaseHelper cdh = new CropDatabaseHelper(context);
        cdh.deleteAllCrops();
        Log.i("myTag", "All crops deleted successfully.");
    }

    public static void deleteAllTasks(Context context) {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
        tdh.deleteAllTasks();
        Log.i("myTag", "All tasks deleted successfully.");
    }

    public static void listAllCrops(Context context) {
        CropDatabaseHelper cdh = new CropDatabaseHelper(context);
        int userId = Auth.isAdmin ? Auth.userId : Auth.parentId;
        List<CropModel> cropList = cdh.getAllCrops(userId);

        if (cropList.isEmpty()) {
            Log.i("myTag", "No crops found.");
        } else {
            for (CropModel crop : cropList) {
                Log.i("myTag", "Crop ID: " + crop.getId() + ", User ID: " + crop.getUserId() +
                        ", Name: " + crop.getName() + ", Date: " + crop.getDate());
            }
        }
    }

    public static void listAllTasks(Context context) {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
        List<TaskModel> taskList = tdh.getAllTasks();

        if (taskList.isEmpty()) {
            Log.i("myTag", "No tasks found.");
        } else {
            for (TaskModel task : taskList) {
                Log.i("myTag", "Task ID: " + task.getId() + ", User ID: " + task.getUserId() +
                        ", Crop Name: " + task.getCropName() + ", Crop ID: " + task.getCropId() +
                        ", Note: " + task.getNote() + ", Start Time: " + TimeHelper.convertMillisToDateTime(task.getStartTime()) +
                        ", End Time: " + TimeHelper.convertMillisToDateTime(task.getEndTime()) + ", Repeat: " + task.isRepeat() +
                        ", Repeat Every Days: " + task.getRepeatEveryDays());
            }
        }
    }

    public static void createAnEndingTask(Context context) {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
        CropDatabaseHelper cdh = new CropDatabaseHelper(context);
        int userId = Auth.isAdmin ? Auth.userId : Auth.parentId;

        List<CropModel> cropList = cdh.getAllCrops(userId);
        if (cropList.isEmpty()) {
            cdh.addNewCrop("Default Crop", userId);
            cropList = cdh.getAllCrops(userId);
        }

        CropModel crop = cropList.get(0);
        long startMillis = System.currentTimeMillis() + 10000; // 20 seconds from now
        long endMillis = startMillis; // End time is equal to start time

        tdh.addNewTask(crop.getName(), crop.getId(), userId, "Ending Task", startMillis, endMillis, true, 2);

        Log.i("myTag", "Ending task created successfully.");
    }

    public static void createThreeTasks(Context context) {
        CropDatabaseHelper cdh = new CropDatabaseHelper(context);
        int userId = Auth.isAdmin ? Auth.userId : Auth.parentId;

        List<CropModel> cropList = cdh.getAllCrops(userId);
        if (cropList.isEmpty()) {
            cdh.addNewCrop("Potatoes", userId);
            cdh.addNewCrop("Tomatoes", userId);
            cdh.addNewCrop("Carrots", userId);

            cropList = cdh.getAllCrops(userId);  // Refresh the list to get IDs
            CropModel potatoes = null, tomatoes = null;
            for (CropModel crop : cropList) {
                if ("Potatoes".equals(crop.getName())) {
                    potatoes = crop;
                } else if ("Tomatoes".equals(crop.getName())) {
                    tomatoes = crop;
                }
            }

            TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
            long currentMillis = System.currentTimeMillis();

            if (potatoes != null) {
                long nextDayMillis = currentMillis + 24L * 60 * 60 * 1000;
                tdh.addNewTask(potatoes.getName(), potatoes.getId(), userId,
                        "First task for Potatoes", currentMillis + 20000, currentMillis + 2 * 60 * 60 * 1000, true, 1);
                tdh.addNewTask(potatoes.getName(), potatoes.getId(), userId,
                        "Second task for Potatoes", nextDayMillis, nextDayMillis + 2 * 60 * 60 * 1000, true, 2);
            }

            if (tomatoes != null) {
                long nextWeekMillis = currentMillis + 7L * 24 * 60 * 60 * 1000;
                tdh.addNewTask(tomatoes.getName(), tomatoes.getId(), userId,
                        "First task for Tomatoes", currentMillis + 40000, currentMillis + 2 * 60 * 60 * 1000, true, 1);
                tdh.addNewTask(tomatoes.getName(), tomatoes.getId(), userId,
                        "Second task for Tomatoes", nextWeekMillis, nextWeekMillis + 2 * 60 * 60 * 1000, true, 2);
            }

            Log.i("myTag", "Three crops created with tasks for Potatoes and Tomatoes.");
        }
    }

}

