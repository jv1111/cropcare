package com.example.cropcare.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class DatabaseBackupHelper {

    private static final String DATABASE_NAME = "myDataBase";
    private static final String BACKUP_FOLDER = "CropCareBackup";
    private static final String BACKUP_FILE_NAME = "CropCareBackup.db";
    private static final String TAG = "DatabaseBackupHelper";

    public static boolean exportDatabase(Context context) {
        try {
            File dbFile = context.getDatabasePath(DATABASE_NAME);
            File exportDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), BACKUP_FOLDER);

            if (!exportDir.exists() && !exportDir.mkdirs()) {
                Log.e(TAG, "Failed to create backup directory");
                return false;
            }

            File backupFile = new File(exportDir, BACKUP_FILE_NAME);

            try (FileChannel src = new FileInputStream(dbFile).getChannel();
                 FileChannel dst = new FileOutputStream(backupFile).getChannel()) {
                dst.transferFrom(src, 0, src.size());
            }
            Log.i(TAG, "Database exported successfully to " + backupFile.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error exporting database", e);
            return false;
        }
    }

    public static void openFilePicker(Activity activity, ActivityResultLauncher<Intent> filePickerLauncher) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/octet-stream");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        filePickerLauncher.launch(intent);
    }

    public static boolean importDatabase(Context context, Uri fileUri) {
        try {
            File dbFile = context.getDatabasePath(DATABASE_NAME);

            try (FileChannel src = new FileInputStream(context.getContentResolver().openFileDescriptor(fileUri, "r").getFileDescriptor()).getChannel();
                 FileChannel dst = new FileOutputStream(dbFile).getChannel()) {
                dst.transferFrom(src, 0, src.size());
            }

            Log.i(TAG, "Database imported successfully from " + fileUri);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error importing database", e);
            return false;
        }
    }

}

