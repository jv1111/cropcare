package com.example.cropcare.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.cropcare.Database.Tables.CoFarmerTable;
import com.example.cropcare.Database.Tables.CropTable;
import com.example.cropcare.Database.Tables.IDatabaseTable;
import com.example.cropcare.Database.Tables.TaskTable;
import com.example.cropcare.Database.Tables.UserTable;

import java.util.Arrays;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myDataBase";
    private static final int DATABASE_VERSION = 2;
    private static DataBaseHelper instance; // Singleton instance

    private final List<IDatabaseTable> tables = Arrays.asList(
            new UserTable(),
            new CropTable(),
            new TaskTable(),
            new CoFarmerTable()
    );

    // Private constructor to prevent instantiation
    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Singleton pattern: Get the single instance of DataBaseHelper
    public static synchronized DataBaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (IDatabaseTable table : tables) {
            String query = table.createTableQuery();
            Log.i("myTag", "Creating table with query: " + query);
            db.execSQL(query);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (IDatabaseTable table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
        }
        onCreate(db);
    }
}
