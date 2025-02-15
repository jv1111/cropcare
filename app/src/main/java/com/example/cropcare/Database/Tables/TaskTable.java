package com.example.cropcare.Database.Tables;

public class TaskTable implements IDatabaseTable {
    public static final String COL_ID = "id";
    public static final String TABLE_NAME = "Task";
    public static final String COL_DATE = "date";
    public static final String COL_CROP_ID = "crop_id";
    public static final String COL_CROP_NAME = "crop_name";
    public static final String COL_NOTE = "note";

    @Override
    public String createTableQuery() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT NOT NULL, " +
                COL_CROP_ID + " INTEGER NOT NULL, " +
                COL_CROP_NAME + " TEXT NOT NULL, " +
                COL_NOTE + " TEXT, " +
                "FOREIGN KEY (" + COL_CROP_ID + ") REFERENCES Crop(id) ON DELETE CASCADE);";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
