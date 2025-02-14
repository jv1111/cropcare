package com.example.cropcare.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.cropcare.Database.Tables.CropTable;
import com.example.cropcare.Database.Tables.IDatabaseTable;
import com.example.cropcare.Database.Tables.UserTable;
import com.example.cropcare.Model.UserModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME = "myDataBase";
    public static final int DATABASE_VERSION = 3;

    private final List<IDatabaseTable> tables = Arrays.asList(
            new UserTable(),
            new CropTable()
    );

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        for(IDatabaseTable table : tables){
            db.execSQL(table.createTableQuery());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (IDatabaseTable table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table.getTableName());
        }
        onCreate(db);
    }

    public void addNewUser(String username, String password, boolean isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserTable.COL_USERNAME, username);
        cv.put(UserTable.COL_PASSWORD, password);
        cv.put(UserTable.COL_ADMIN, isAdmin);
        db.insert(UserTable.TABLE_NAME, null, cv);
        db.close();
    }

    public List<UserModel> getAllUsers() {
        List<UserModel> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + UserTable.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(UserTable.COL_ID);
                int usernameIndex = cursor.getColumnIndex(UserTable.COL_USERNAME);
                int passwordIndex = cursor.getColumnIndex(UserTable.COL_PASSWORD);
                int adminIndex = cursor.getColumnIndex(UserTable.COL_ADMIN);

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
        String query = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERNAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(UserTable.COL_ID);
            int usernameIndex = cursor.getColumnIndex(UserTable.COL_USERNAME);
            int passwordIndex = cursor.getColumnIndex(UserTable.COL_PASSWORD);
            int adminIndex = cursor.getColumnIndex(UserTable.COL_ADMIN);

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
        String query = "SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.COL_USERNAME + " = ? AND " + UserTable.COL_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean isAuthenticated = cursor.getCount() > 0;

        cursor.close();
        db.close();
        return isAuthenticated;
    }

}