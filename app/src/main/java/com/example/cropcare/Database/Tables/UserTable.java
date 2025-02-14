package com.example.cropcare.Database.Tables;

public class UserTable implements IDatabaseTable {
    public static final String COL_ID = "id";
    public static final String TABLE_NAME = "User";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ADMIN = "admin";

    @Override
    public String createTableQuery() {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_ADMIN + " INTEGER NOT NULL DEFAULT 0);";
        return query;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
