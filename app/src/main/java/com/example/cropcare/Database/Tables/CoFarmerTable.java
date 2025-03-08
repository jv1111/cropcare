package com.example.cropcare.Database.Tables;

public class CoFarmerTable implements IDatabaseTable {
    public static final String TABLE_NAME = "CoFarmer";
    public static final String COL_ID = "id";
    public static final String COL_PARENT_USER_ID = "parentUserId";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    @Override
    public String createTableQuery() {
        return "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PARENT_USER_ID + " INTEGER NOT NULL, " +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_PARENT_USER_ID + ") REFERENCES " + UserTable.TABLE_NAME + "(" + UserTable.COL_ID + "));";
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
