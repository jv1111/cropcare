package com.example.cropcare;

import android.content.Context;
import android.util.Log;

import com.example.cropcare.Database.CropDatabaseHelper;
import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.services.NotifierService;

import java.util.List;

public class Test {


    public static void createMultipleTaskReset(Context context) {
        if (!NotifierService.isRunning) {
            TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
            CropDatabaseHelper cdh = new CropDatabaseHelper(context);

            List<CropModel> cropList = cdh.getAllCrops();
            if (cropList.isEmpty()) {
                cdh.addNewCrop("Default Crop");
                cropList = cdh.getAllCrops();
            }

            CropModel crop = cropList.get(0);
            Log.i("myTag test", "id: " + crop.getId());
            Log.i("myTag test", "name: " + crop.getName());

            tdh.deleteAllTasks();

            long currentMillis = System.currentTimeMillis();
            long startMillis = currentMillis + 10000;
            long oneMonthPlus = currentMillis + (30L * 24 * 60 * 60 * 1000);

            for (int i = 0; i < 3; i++) {
                tdh.addNewTask(crop.getName(), crop.getId(), "Task " + (i + 1), startMillis, oneMonthPlus, true, 1);
                startMillis += 300000;
            }
        } else {
            Log.i("myTag", "creation canceled");
        }
    }

    public static void createTask(Context context) {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
        CropDatabaseHelper cdh = new CropDatabaseHelper(context);

        List<CropModel> cropList = cdh.getAllCrops();
        if (cropList.isEmpty()) {
            cdh.addNewCrop("Default Crop");
            cropList = cdh.getAllCrops();
        }

        CropModel crop = cropList.get(0);
        Log.i("myTag test", "id: " + crop.getId());
        Log.i("myTag test", "name: " + crop.getName());

        tdh.deleteAllTasks();

        long currentMillis = System.currentTimeMillis();
        long startMillis = currentMillis + 10000;
        long oneMonthPlus = currentMillis + (30L * 24 * 60 * 60 * 1000);

        for (int i = 0; i < 3; i++) {
            tdh.addNewTask(crop.getName(), crop.getId(), "Task " + (i + 1), startMillis, oneMonthPlus, true, 1);
            startMillis += 300000;
        }
    }

    public static void deleteAllTest(Context context) {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
        tdh.deleteAllTasks();
        Log.i("myTag", "All tasks deleted successfully.");
    }

    public static void createAnEndingTask(Context context) {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(context);
        CropDatabaseHelper cdh = new CropDatabaseHelper(context);

        List<CropModel> cropList = cdh.getAllCrops();
        if (cropList.isEmpty()) {
            cdh.addNewCrop("Default Crop");
            cropList = cdh.getAllCrops();
        }

        CropModel crop = cropList.get(0);
        Log.i("myTag test", "id: " + crop.getId());
        Log.i("myTag test", "name: " + crop.getName());

        long startMillis = System.currentTimeMillis() + 10 * 1000;
        long endMillis = startMillis;
        int repeatInterval = 1;

        tdh.addNewTask(crop.getName(), crop.getId(), "Ending Task", startMillis, endMillis, true, repeatInterval);
    }

}

