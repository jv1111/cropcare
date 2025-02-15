package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cropcare.Database.Tables.TaskTable;
import com.example.cropcare.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHelper {
    private final DataBaseHelper dbHelper;

    public TaskDatabaseHelper(Context context) {
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public void addNewTask(String date, int cropId, String cropName, String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskTable.COL_DATE, date);
        values.put(TaskTable.COL_CROP_ID, cropId);
        values.put(TaskTable.COL_CROP_NAME, cropName);
        values.put(TaskTable.COL_NOTE, note);

        long result = db.insert(TaskTable.TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("myTag", "Task insert failed");
        } else {
            Log.i("myTag", "Task inserted successfully with ID: " + result);
        }
        db.close();
    }

    public List<TaskModel> getAllTasks() {
        Log.i("myTag", "getting the tasks....");
        List<TaskModel> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TaskTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                TaskModel task = new TaskModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_NOTE))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
