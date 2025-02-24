package com.example.cropcare;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.CropDatabaseHelper;
import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.helper.Permissions;
import com.example.cropcare.receivers.AlarmReceiver;
import com.example.cropcare.recycler.AdapterCrops;
import com.example.cropcare.services.NotifierService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterCrops.ICropListControlCB {

    private CropDatabaseHelper cropDbHelper;
    private String TAG = "myTag";
    private Button btnRecord;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        test();

        Permissions.checkNotification(this);
        Permissions.setAlarmPermission(this);
        cropDbHelper = new CropDatabaseHelper(this);
        btnRecord = findViewById(R.id.btnRecords);
        startDateReaderService();
        setupButtons();
        setupRecyclerView(getAllCrops());

    }

    private void test(){
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(this);
        CropDatabaseHelper cdh = new CropDatabaseHelper(this);
        List<CropModel> cropList = cdh.getAllCrops();
        CropModel crop = cropList.get(0);
        Log.i("myTag test", "id: " + cropList.get(0).getId());
        Log.i("myTag test", "name: " + cropList.get(0).getName());
        tdh.deleteAllTasks();

        long currentMillis = System.currentTimeMillis();
        long newMillis = currentMillis + 10000;
        long oneMonthPlus = currentMillis + (30L * 24 * 60 * 60 * 1000);
        tdh.addNewTask(crop.getName(), crop.getId(), "hey", newMillis, oneMonthPlus, true, 1);
        newMillis += 300000;
        tdh.addNewTask(crop.getName(), crop.getId(), "Hoo", newMillis, oneMonthPlus, true, 1);
        newMillis += 720000;
        tdh.addNewTask(crop.getName(), crop.getId(), "Haa", newMillis, oneMonthPlus, true, 1);
        //generate a code that will add  a new tasks that have a starTime 10 seconds

        if (getIntent().getExtras() != null) {
            Log.d("NotificationTap", "Notification was tapped");
            AlarmReceiver.stopAlarm();
            int taskId = getIntent().getIntExtra("taskId", -1);
            Log.d("NotificationTap", "Received taskId: " + taskId);
            //TODO ADD THE TASK ID AS EXTRA
            //TODO DELETE OR UPDATE THE TASK IF ISREPEAT TRUE
            //TODO RESTART THE SERVICE TO START A NEW TIMER
        }
    }

    private void setupButtons(){
        btnRecord.setOnClickListener(v -> {
            getAllCrops();
        });
    }

    private void setupRecyclerView(List<CropModel> cropInfoList){
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterCrops(getApplicationContext(), cropInfoList, this));
    }

    private List<CropModel> getAllCrops(){
        List<CropModel> cropList = cropDbHelper.getAllCrops();
        Log.i("myTag", "got the crops: crop count = " + cropList.size());
        for (CropModel crop: cropList
             ) {
            Log.i(TAG, crop.getName());
        }
        return cropList;
    }

    private void startDateReaderService(){
        if(!NotifierService.isRunning){
            Log.i("myTag", "starting the service");
            startService(new Intent(this, NotifierService.class));
        }
    }

    @Override
    public void onSelect(int id, String cropName) {
        Intent intent = new Intent(MainActivity.this, TaskSelectionActivity.class);
        intent.putExtra("cropId", id);
        intent.putExtra("cropName", cropName);
        startActivity(intent);
    }

    @Override
    public void onDelete(int id) {

    }

    public void navigateToAddNew(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

}