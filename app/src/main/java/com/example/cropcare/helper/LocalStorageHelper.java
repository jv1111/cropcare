package com.example.cropcare.helper;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class LocalStorageHelper {
    private SharedPreferences securePrefs;

    public LocalStorageHelper(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            securePrefs = EncryptedSharedPreferences.create(
                    context,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveUserData(int userId, String username, boolean isAdmin, Integer parentId) {
        securePrefs.edit()
                .putInt("userId", userId)
                .putString("username", username)
                .putBoolean("isAdmin", isAdmin)
                .putInt("parentId", parentId != null ? parentId : -1)
                .apply();
    }

    public int getParentId(){
        return securePrefs.getInt("parentId", -1);
    }

    public boolean isAdmin() {
        return securePrefs.getBoolean("isAdmin", false);
    }


    public int getUserId() {
        return securePrefs.getInt("userId", -1);
    }

    public String getUsername() {
        return securePrefs.getString("username", null);
    }

    public void clearUserData() {
        securePrefs.edit().clear().apply();
    }
}
