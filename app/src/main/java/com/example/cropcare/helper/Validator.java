package com.example.cropcare.helper;

import android.content.Context;
import android.widget.Toast;

import com.example.cropcare.Database.CoFarmerDatabaseHelper;
import com.example.cropcare.Database.DataBaseHelper;
import com.example.cropcare.Database.UserDatabaseHelper;
import com.example.cropcare.Model.CoFarmerModel;
import com.example.cropcare.Model.UserModel;

public class Validator {

    public static boolean isUserInputValid(String username, String password, Context context){
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isUsernameAvailable(String username , Context context, UserDatabaseHelper userDbHelper, CoFarmerDatabaseHelper coFarmerDbHelper){
        UserModel existingUser = userDbHelper.getUserByUsername(username);
        CoFarmerModel existingCoFarmer = coFarmerDbHelper.getCoFarmerByUsername(username);
        if (existingUser != null || existingCoFarmer != null) {
            Toast.makeText(context, "Username already exists. Please choose another.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isCropNameValid(String str) {
        return str != null && str.length() >= 3;
    }

    public static boolean validateTaskInput(Context context, String note, boolean isRepeat, long startTime, long endTime, int repeatEveryDays) {
        if (note.isEmpty()) {
            Toast.makeText(context, "Note is required!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isRepeat) {
            if (startTime <= 0) {
                Toast.makeText(context, "Start time is required!", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            if (endTime <= 0) {
                Toast.makeText(context, "End time is required for repeating tasks!" + endTime, Toast.LENGTH_SHORT).show();
                return false;
            }

            if (repeatEveryDays <= 0) {
                Toast.makeText(context, "Repeat every days must be greater than 0 for repeating tasks!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }


    public static boolean isPasswordValid(String password, Context context) {
        if (password == null || password.isEmpty() || password.length() < 3) {
            Toast.makeText(context, "Password must be at least 3 characters long and not empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
