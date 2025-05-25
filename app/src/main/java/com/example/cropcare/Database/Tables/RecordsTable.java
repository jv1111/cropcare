package com.example.cropcare.Database.Tables;

public class RecordsTable implements IDatabaseTable {
    public static final String TABLE_NAME = "Record";

    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "userId";
    public static final String COL_NOTE = "note";
    public static final String COL_STATUS = "status";
    public static final String COL_TIME = "time";
    public static final String COL_TASK_ID = "taskId";
    public static final String COL_CROP_ID = "cropId";
    public static final String COL_CROP_NAME = "cropName";

    @Override
    public String createTableQuery() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER NOT NULL, " +
                COL_CROP_ID + " INTEGER NOT NULL, " +
                COL_CROP_NAME + " TEXT NOT NULL, " +
                COL_TASK_ID + " INTEGER NOT NULL, " +
                COL_NOTE + " TEXT NOT NULL, " +
                COL_STATUS + " TEXT NOT NULL, " +
                COL_TIME + " INTEGER NOT NULL);";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
