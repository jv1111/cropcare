package com.example.cropcare.test;

import android.content.Context;
import android.util.Log;

import com.example.cropcare.Database.RecordsDatabaseHelper;
import com.example.cropcare.Model.RecordModel;

import java.util.List;

public class TestRecords {

    public static void listAllRecords(Context context) {
        RecordsDatabaseHelper recDb = new RecordsDatabaseHelper(context);
        List<RecordModel> recordList = recDb.getAllRecords();

        if (recordList.isEmpty()) {
            Log.i("myTag", "No records found.");
        } else {
            for (RecordModel record : recordList) {
                Log.i("myTag", "Record ID: " + record.getId() +
                        ", User ID: " + record.getUserId() +
                        ", Crop ID: " + record.getCropId() +
                        ", Task ID: " + record.getTaskId() +
                        ", Note: " + record.getNote() +
                        ", Status: " + record.getStatus() +
                        ", Time: " + record.getTime());
            }
        }
    }
}
