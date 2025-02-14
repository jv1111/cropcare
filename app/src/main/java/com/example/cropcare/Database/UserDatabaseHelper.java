package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cropcare.Database.Tables.UserTable;
import com.example.cropcare.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper {
    private final DataBaseHelper dbHelper;

    public UserDatabaseHelper(Context context) {
        dbHelper = DataBaseHelper.getInstance(context);
    }

    public void addUser(String username, String password, boolean isAdmin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COL_USERNAME, username);
        values.put(UserTable.COL_PASSWORD, password);
        values.put(UserTable.COL_ADMIN, isAdmin ? 1 : 0);
        db.insert(UserTable.TABLE_NAME, null, values);
        db.close();
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                UserModel user = new UserModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_USERNAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_PASSWORD)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COL_ADMIN)) == 1
                );
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public UserModel getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERNAME + " = ?", new String[]{username});

        if (cursor.moveToFirst()) {
            UserModel user = new UserModel(
                    cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_USERNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COL_PASSWORD)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COL_ADMIN)) == 1
            );
            cursor.close();
            db.close();
            return user;
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean isPasswordValid(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERNAME + " = ? AND " + UserTable.COL_PASSWORD + " = ?", new String[]{username, password});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
}
