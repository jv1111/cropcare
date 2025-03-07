package com.example.cropcare.Database.Tables;

public class TaskTable implements IDatabaseTable {
    public static final String COL_ID = "id";
    public static final String TABLE_NAME = "Task";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_CROP_NAME = "crop_name";
    public static final String COL_CROP_ID = "crop_id";
    public static final String COL_NOTE = "note";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String COL_IS_REPEAT = "is_repeat";
    public static final String COL_REPEAT_EVERY = "repeat_every";

    @Override
    public String createTableQuery() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_ID + " INTEGER NOT NULL, " +
                COL_CROP_ID + " INTEGER NOT NULL, " +
                COL_CROP_NAME + " TEXT NOT NULL, " +
                COL_NOTE + " TEXT, " +
                COL_START_TIME + " INTEGER NOT NULL, " +
                COL_END_TIME + " INTEGER NOT NULL, " +
                COL_IS_REPEAT + " INTEGER NOT NULL, " +
                COL_REPEAT_EVERY + " INTEGER, " +
                "FOREIGN KEY (" + COL_CROP_ID + ") REFERENCES Crop(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (" + COL_USER_ID + ") REFERENCES Users(id) ON DELETE CASCADE);";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
