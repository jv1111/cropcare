package com.example.cropcare;

import android.util.Log;

public class Auth {
    public static int userId;
    public static String username;
    public static boolean isAdmin;
    public static Integer parentId;
    public static boolean isDemo;

    public static void setUserData(int userId, String username, boolean isAdmin, Integer parentId, boolean isDemo) {
        Auth.userId = userId;
        Auth.username = username;
        Auth.isAdmin = isAdmin;
        Auth.parentId = parentId;
        Auth.isDemo = isDemo;
    }

    public static void printUserInfo() {
        Log.i("myTag", "User ID: " + userId);
        Log.i("myTag", "Username: " + username);
        Log.i("myTag", "Is Admin: " + isAdmin);
        Log.i("myTag", "Parent ID: " + (parentId != null ? parentId : "None"));
    }

}
