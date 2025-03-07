package com.example.cropcare.Database.Tables;

public class CropTable implements IDatabaseTable {
    public static final String COL_ID = "id";
    public static final String TABLE_NAME = "Crop";
    public static final String COL_NAME = "name";
    public static final String COL_USER_ID = "userId";
    public static final String COL_DATE = "date";

    @Override
    public String createTableQuery() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER NOT NULL, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_DATE + " INTEGER NOT NULL);";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
