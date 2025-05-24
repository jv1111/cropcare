package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cropcare.Database.Tables.RecordsTable;
import com.example.cropcare.Model.RecordModel;

import java.util.ArrayList;
import java.util.List;

public class RecordsDatabaseHelper {
    private final DataBaseHelper dbHelper;

    public RecordsDatabaseHelper(Context context) {
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public boolean addNewRecord(int userId, int cropId, int taskId, String note, String status, long time) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RecordsTable.COL_USER_ID, userId);
        values.put(RecordsTable.COL_CROP_ID, cropId);
        values.put(RecordsTable.COL_TASK_ID, taskId);
        values.put(RecordsTable.COL_NOTE, note);
        values.put(RecordsTable.COL_STATUS, status);
        values.put(RecordsTable.COL_TIME, time);
        long result = db.insert(RecordsTable.TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }


    public List<RecordModel> getRecordsByUserId(int userId) {
        List<RecordModel> recordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + RecordsTable.TABLE_NAME + " WHERE " + RecordsTable.COL_USER_ID + " = ? ORDER BY " + RecordsTable.COL_TIME + " DESC",
                new String[]{String.valueOf(userId)}
        );

        if (cursor.moveToFirst()) {
            do {
                RecordModel record = new RecordModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_CROP_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COL_NOTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COL_STATUS)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(RecordsTable.COL_TIME))
                );
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordList;
    }

    public List<RecordModel> getAllRecords() {
        List<RecordModel> recordList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + RecordsTable.TABLE_NAME + " ORDER BY " + RecordsTable.COL_TIME + " DESC",
                null
        );

        if (cursor.moveToFirst()) {
            do {
                RecordModel record = new RecordModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_USER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_CROP_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(RecordsTable.COL_TASK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COL_NOTE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(RecordsTable.COL_STATUS)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(RecordsTable.COL_TIME))
                );
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return recordList;
    }

}
