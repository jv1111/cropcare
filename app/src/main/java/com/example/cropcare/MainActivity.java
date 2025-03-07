package com.example.cropcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.LocalStorageHelper;
import com.example.cropcare.helper.Permissions;
import com.example.cropcare.helper.TimeHelper;
import com.example.cropcare.recycler.AdapterCrops;
import com.example.cropcare.services.NotifierService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterCrops.ICropListControlCB {

    private CropDatabaseHelper cropDbHelper;
    private String TAG = "myTag";
    private Button btnRecord, btnLogout;
    private RecyclerView rv;
    private AdapterCrops adapter;
    private TextView tvUsername;
    private LocalStorageHelper localStorageHelper;

    @SuppressLint("SetTextI18n")
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

        localStorageHelper = new LocalStorageHelper(this);

        Permissions.checkNotification(this);
        Permissions.setAlarmPermission(this);

        cropDbHelper = new CropDatabaseHelper(this);
        btnRecord = findViewById(R.id.btnRecords);
        btnLogout = findViewById(R.id.btnLogout);
        rv = findViewById(R.id.rv);
        tvUsername = findViewById(R.id.tvUsername);

        tvUsername.setText("Welcome, " + Auth.username);

        NotifierService.startService(this);
        setupButtons();
        setupRecyclerView(getAllCrops());
    }

    private void setupButtons(){
        btnRecord.setOnClickListener(v -> {
            Test.logAllCrops(getApplicationContext());
            Log.i("myTag", "----------------------");
            Test.logAllTasks(getApplicationContext());
        });

        btnLogout.setOnClickListener(v -> {
            localStorageHelper.clearUserData();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        //TODO MODIFY THE DATABASE FOR THE CROP AND TASK, IT MUST HAVE userIds
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView(getAllCrops());
    }

    private void setupRecyclerView(List<CropModel> cropInfoList){
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCrops(getApplicationContext(), cropInfoList, this);
        rv.setAdapter(adapter);
    }

    private List<CropModel> getAllCrops(){
        List<CropModel> cropList = cropDbHelper.getAllCrops(Auth.userId);
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