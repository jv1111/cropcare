package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cropcare.Database.Tables.CropTable;
import com.example.cropcare.Model.CropModel;

import java.util.ArrayList;
import java.util.List;

public class CropDatabaseHelper {
    private final DataBaseHelper dbHelper;

    public CropDatabaseHelper(Context context){
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public void addNewCrop(String cropName, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CropTable.COL_NAME, cropName);
        values.put(CropTable.COL_USER_ID, userId);
        values.put(CropTable.COL_DATE, System.currentTimeMillis()); // Store date in milliseconds

        long result = db.insert(CropTable.TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("myTag", "Crop insert failed");
        } else {
            Log.i("myTag", "Crop inserted successfully with ID: " + result);
        }
        db.close();
    }


    public List<CropModel> getAllCrops(int userId) {
        Log.i("myTag", "getting the crops for user ID: " + userId);
        List<CropModel> cropList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CropTable.TABLE_NAME + " WHERE " + CropTable.COL_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                CropModel crop = new CropModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(CropTable.COL_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(CropTable.COL_USER_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(CropTable.COL_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(CropTable.COL_NAME))
                );
                cropList.add(crop);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cropList;
    }

    public void deleteAllCrops() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(CropTable.TABLE_NAME, null, null);
        db.close();
        Log.i("myTag", "Deleted " + rowsDeleted + " crops successfully.");
    }

    public boolean deleteCropById(int cropId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(CropTable.TABLE_NAME, CropTable.COL_ID + " = ?", new String[]{String.valueOf(cropId)});
        db.close();
        if (rowsDeleted > 0) {
            Log.i("myTag", "Crop deleted successfully for ID: " + cropId);
            return true;
        } else {
            Log.e("myTag", "Failed to delete crop for ID: " + cropId);
            return false;
        }
    }

    public void updateCropName(int cropId, String newCropName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CropTable.COL_NAME, newCropName);

        int rowsUpdated = db.update(CropTable.TABLE_NAME, values, CropTable.COL_ID + " = ?",
                new String[]{String.valueOf(cropId)});

        if (rowsUpdated > 0) {
            Log.i("myTag", "Crop name updated successfully for ID: " + cropId);
        } else {
            Log.e("myTag", "Failed to update crop name for ID: " + cropId);
        }
        db.close();
    }


}
