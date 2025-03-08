package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cropcare.Database.Tables.CoFarmerTable;
import com.example.cropcare.Model.CoFarmerModel;

import java.util.ArrayList;
import java.util.List;

public class CoFarmerDatabaseHelper {
    private final DataBaseHelper dbHelper;

    public CoFarmerDatabaseHelper(Context context) {
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public void addCoFarmer(int parentUserId, String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CoFarmerTable.COL_PARENT_USER_ID, parentUserId);
        values.put(CoFarmerTable.COL_USERNAME, username);
        values.put(CoFarmerTable.COL_PASSWORD, password);
        db.insert(CoFarmerTable.TABLE_NAME, null, values);
        db.close();
    }

    public List<CoFarmerModel> getAllCoFarmers() {
        List<CoFarmerModel> coFarmerList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CoFarmerTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                CoFarmerModel coFarmer = new CoFarmerModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_PARENT_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_PASSWORD))
                );
                coFarmerList.add(coFarmer);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return coFarmerList;
    }

    public CoFarmerModel getCoFarmerByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CoFarmerTable.TABLE_NAME + " WHERE " + CoFarmerTable.COL_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            CoFarmerModel coFarmer = new CoFarmerModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_PARENT_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(CoFarmerTable.COL_PASSWORD))
            );
            cursor.close();
            db.close();
            return coFarmer;
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean isPasswordValid(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CoFarmerTable.TABLE_NAME + " WHERE " + CoFarmerTable.COL_USERNAME + " = ? AND " + CoFarmerTable.COL_PASSWORD + " = ?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public void deleteAllCoFarmers() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(CoFarmerTable.TABLE_NAME, null, null);
        db.close();
    }
}
