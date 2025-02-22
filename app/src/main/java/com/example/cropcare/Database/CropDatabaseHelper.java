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

    public void addNewCrop(String cropName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CropTable.COL_NAME, cropName);
        values.put(CropTable.COL_DATE, System.currentTimeMillis()); // Store date in milliseconds

        long result = db.insert(CropTable.TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("myTag", "Crop insert failed");
        } else {
            Log.i("myTag", "Crop inserted successfully with ID: " + result);
        }
        db.close();
    }


    public List<CropModel> getAllCrops() {
        Log.i("myTag", "getting the crops....");
        List<CropModel> cropList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CropTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                CropModel crop = new CropModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(CropTable.COL_ID)),
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

}
