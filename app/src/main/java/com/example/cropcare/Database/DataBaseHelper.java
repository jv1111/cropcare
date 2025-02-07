package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "myDataBase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "myTable";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(String name){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, name);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == 1){
            Toast.makeText(context, "failed", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "succeeded", Toast.LENGTH_LONG).show();
        }
    }

    public List<String> getDataList() {
        List<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                dataList.add(cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        for ( String name :
             dataList) {
        }
        return dataList;
    }

}
