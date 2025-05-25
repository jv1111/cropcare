package com.example.cropcare.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.cropcare.Auth;
import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.MainActivity;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.TaskFinishActivity;
import com.example.cropcare.helper.TimeHelper;
import com.example.cropcare.receivers.AlarmReceiver;

public class NotifierService extends Service {
    private TaskDatabaseHelper taskDatabaseHelper;
    private String notificationTitle;
    private static final String CHANNEL_ID = "NotifierServiceChannel";
    private static final int NOTIFICATION_ID = 1;
    public static boolean isRunning = false;
    private final Handler handler = new Handler();
    private boolean isRinging = false;
    private PendingIntent pendingIntent;
    private TaskModel upcomingTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        taskDatabaseHelper = new TaskDatabaseHelper(this);
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
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        upcomingTask = taskDatabaseHelper.getFirstUpcomingTask(Auth.isAdmin ? Auth.userId : Auth.parentId);
        if(upcomingTask != null) notificationTitle = upcomingTask.getCropName() + " note: " + upcomingTask.getNote();
        else notificationTitle = "No upcoming task";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText("....")
                .setContentIntent(pendingIntent) // Set the PendingIntent
                .setAutoCancel(true) // Dismiss notification when clicked
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
        startForeground(NOTIFICATION_ID, notification);
    }

    private void updateNotification(String text, int currentTaskId, int currentCropId, int userId, String cropName) {

        Intent notificationIntent = new Intent(this, TaskFinishActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //setup for tap function
        if (isRinging) { // Make it clickable only if isRinging is true
            notificationIntent.putExtra("from_notification", true);
            notificationIntent.putExtra("taskId", currentTaskId);
            notificationIntent.putExtra("cropId", currentCropId);
            notificationIntent.putExtra("userId", userId);
            notificationIntent.putExtra("cropName", cropName);
            pendingIntent = PendingIntent.getActivity(
                    this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setOnlyAlertOnce(true)
                .setOngoing(true);

        //make it clickable if ringing
        if (isRinging && pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        } else {
            builder.setContentIntent(null); // Not clickable
        }

        Notification notification = builder.build();
        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null) manager.notify(NOTIFICATION_ID, notification);
    }

    private void notifierFunction() {
        TaskModel upcomingTask = taskDatabaseHelper.getFirstUpcomingTask(Auth.isAdmin ? Auth.userId : Auth.parentId);

        if (upcomingTask == null) {
            updateNotification("There is no upcoming task", 0, 0,0, "");
            return;
        }

        long startTime = upcomingTask.getStartTime();
        String cropName = upcomingTask.getCropName();
        String note = upcomingTask.getNote();
        int taskId = upcomingTask.getId();
        int cropId = upcomingTask.getCropId();
        int userId = upcomingTask.getUserId();

        AlarmReceiver.setAlarmMillis(this, startTime);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRunning) {

                    String notificationText;
                    long currentMillis = System.currentTimeMillis();

                    if (isRinging) {
                        notificationText = cropName + " note: " + note;
                    } else {
                        long timeDiff = startTime - currentMillis;
                        notificationText = "Time: " + TimeHelper.convertMillisToCountdown(timeDiff);
                        if (timeDiff <= 0) {
                            isRinging = true;
                        }
                    }

                    updateNotification(notificationText, taskId, cropId, userId, cropName);
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    public static void stopService(Context context) {
        Intent intent = new Intent(context, NotifierService.class);
        context.stopService(intent);
        isRunning = false;
    }

    public static void startService(Context context){
        if(!isRunning){
            context.startService(new Intent(context, NotifierService.class));
        }
    }


    @Override
    public void onDestroy() {
        isRunning = false;
        AlarmReceiver.stopAlarm();
        handler.removeCallbacksAndMessages(null);
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
