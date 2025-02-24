package com.example.cropcare.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.MainActivity;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.TimeConverter;

import java.util.Comparator;
import java.util.List;

public class NotifierService extends Service {
    private static final String CHANNEL_ID = "NotifierServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    public static boolean isRunning = false;
    private final Handler handler = new Handler();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;

        startNotification();
        notifierFunction();

        return START_STICKY;
    }

    private void startNotification(){
        // Create an intent to open MainActivity
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText("This service runs in the background.")
                .setContentIntent(pendingIntent) // Set the PendingIntent
                .setAutoCancel(true) // Dismiss notification when clicked
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void updateNotification(String text) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Service Running")
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOnlyAlertOnce(true) // Prevents re-triggering the notification pop-up
                .setOngoing(true) // Keeps it as a persistent notification
                .build();
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) manager.notify(NOTIFICATION_ID, notification);
    }

    private void notifierFunction(){
        TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper(this);
        List<TaskModel> listOfTask = taskDatabaseHelper.getUpcomingTasks();
        listOfTask.sort(Comparator.comparingLong(TaskModel::getStartTime));

        for (TaskModel task : listOfTask) {
            Log.i("myTag", "Upcoming Task: " + task.getCropName() + ", Start: " + TimeConverter.convertMillisToDateTime(task.getStartTime()));
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    String notificationText = "";
                    long currentMillis = System.currentTimeMillis();
                    if (listOfTask.isEmpty()){
                        notificationText = "There is not upcoming task";
                    }else{
                        long firstTaskTime = listOfTask.get(0).getStartTime();
                        long timeDiff = firstTaskTime - currentMillis;
                        Log.i("TaskScheduler", "CurrentMillis: " + TimeConverter.convertMillisToDateTime(currentMillis) +
                                ", FirstTaskTime: " + TimeConverter.convertMillisToDateTime(firstTaskTime) +
                                ", TimeDiff: " + TimeConverter.convertMillisToCountdown(timeDiff));
                        notificationText = "Next Task in: " + TimeConverter.convertMillisToCountdown(timeDiff);
                        //TODO add an alarm if tasksTime == current
                    }
                    updateNotification(notificationText);
                    handler.postDelayed(this, 1000); // Schedule next execution after 1s
                }
            }
        }, 1000);
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        Log.i("myTag", "destroying the service");
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(serviceChannel);
        }
    }
}
