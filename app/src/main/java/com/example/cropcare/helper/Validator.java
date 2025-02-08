package com.example.cropcare.helper;

import android.content.Context;
import android.widget.Toast;

import com.example.cropcare.Database.DataBaseHelper;
import com.example.cropcare.Model.UserModel;

public class Validator {

    public static boolean isUserInputValid(String username, String password, Context context, DataBaseHelper dbHelper){
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isUsernameAvailable(String username , Context context,DataBaseHelper dbHelper){
        UserModel existingUser = dbHelper.getUserByUsername(username);
        if (existingUser != null) {
            Toast.makeText(context, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
