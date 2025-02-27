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

    public void addNewTask(String cropName, int cropId, String note, long startTime, long endTime, boolean isRepeat, int repeatEveryDays) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskTable.COL_CROP_ID, cropId);
        values.put(TaskTable.COL_CROP_NAME, cropName);
        values.put(TaskTable.COL_NOTE, note);
        values.put(TaskTable.COL_START_TIME, startTime);
        values.put(TaskTable.COL_END_TIME, endTime);
        values.put(TaskTable.COL_IS_REPEAT, isRepeat ? 1 : 0);
        values.put(TaskTable.COL_REPEAT_EVERY, repeatEveryDays);

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
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_NOTE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_START_TIME)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_END_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_IS_REPEAT)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_REPEAT_EVERY))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public List<TaskModel> getUpcomingTasks() {
        Log.i("myTag", "getting upcoming tasks...");
        List<TaskModel> taskList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        long currentMillis = System.currentTimeMillis();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TaskTable.TABLE_NAME + " WHERE " + TaskTable.COL_START_TIME + " > ? ORDER BY " + TaskTable.COL_START_TIME + " ASC",
                new String[]{String.valueOf(currentMillis)}
        );

        if (cursor.moveToFirst()) {
            do {
                TaskModel task = new TaskModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_NOTE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_START_TIME)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_END_TIME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_IS_REPEAT)) == 1,
                        cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_REPEAT_EVERY))
                );
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(TaskTable.TABLE_NAME, null, null);
        Log.i("myTag", "Deleted " + deletedRows + " tasks.");
        db.close();
    }

    public TaskModel getOneTaskById(int taskId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        TaskModel task = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TaskTable.TABLE_NAME + " WHERE " + TaskTable.COL_ID + " = ?",
                new String[]{String.valueOf(taskId)}
        );

        if (cursor.moveToFirst()) {
            task = new TaskModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_CROP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COL_NOTE)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_START_TIME)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(TaskTable.COL_END_TIME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_IS_REPEAT)) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COL_REPEAT_EVERY))
            );
        }
        cursor.close();
        db.close();
        return task;
    }


}
