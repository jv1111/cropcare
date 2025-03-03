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
        NotifierService.startService(this);
        setupButtons();
        setupRecyclerView(getAllCrops());

    }

    private void test() {
        TaskDatabaseHelper tdh = new TaskDatabaseHelper(this);
        CropDatabaseHelper cdh = new CropDatabaseHelper(this);

        List<CropModel> cropList = cdh.getAllCrops();
        if (cropList.isEmpty()) {
            cdh.addNewCrop("Default Crop");
            cropList = cdh.getAllCrops();
        }

        CropModel crop = cropList.get(0);
        Log.i("myTag test", "id: " + crop.getId());
        Log.i("myTag test", "name: " + crop.getName());

        tdh.deleteAllTasks();

        long currentMillis = System.currentTimeMillis();
        long startMillis = currentMillis + 20000;
        long oneMonthPlus = currentMillis + (30L * 24 * 60 * 60 * 1000);

        for (int i = 0; i < 3; i++) {
            tdh.addNewTask(crop.getName(), crop.getId(), "Task " + (i + 1), startMillis, oneMonthPlus, true, 1);
            startMillis += 300000;
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