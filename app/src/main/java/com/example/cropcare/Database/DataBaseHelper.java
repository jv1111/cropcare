package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cropcare.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "myDataBase";
    public static final int DATABASE_VERSION = 2;
    public static final String COL_ID = "id";
    public static final String TABLE_USER = "User";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ADMIN = "admin";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT NOT NULL, " +
                COL_PASSWORD + " TEXT NOT NULL, " +
                COL_ADMIN + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        }
    }

    public void addNewUser(String username, String password, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USERNAME, username);
        cv.put(COL_PASSWORD, password);
        cv.put(COL_ADMIN, isAdmin);
        db.insert(TABLE_USER, null, cv);
        db.close();
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COL_ID);
                int usernameIndex = cursor.getColumnIndex(COL_USERNAME);
                int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);
                int adminIndex = cursor.getColumnIndex(COL_ADMIN);

                if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String username = cursor.getString(usernameIndex);
                    String password = cursor.getString(passwordIndex);
                    boolean isAdmin = cursor.getInt(adminIndex) == 1;
                    UserModel user = new UserModel(id, username, password, isAdmin);
                    userList.add(user);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    public UserModel getUserByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_ID);
            int usernameIndex = cursor.getColumnIndex(COL_USERNAME);
            int passwordIndex = cursor.getColumnIndex(COL_PASSWORD);
            int adminIndex = cursor.getColumnIndex(COL_ADMIN);

            if (idIndex != -1 && usernameIndex != -1 && passwordIndex != -1) {
                int id = cursor.getInt(idIndex);
                String password = cursor.getString(passwordIndex);
                boolean isAdmin = cursor.getInt(adminIndex) == 1;
                UserModel user = new UserModel(id, username, password, isAdmin);
                cursor.close();
                db.close();
                return user;
            }
        }

        cursor.close();
        db.close();
        return null; // Return null if no user is found with the given username
    }

    public boolean isPasswordValid(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean isAuthenticated = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return isAuthenticated;
    }

}