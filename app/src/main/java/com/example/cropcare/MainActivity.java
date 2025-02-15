package com.example.cropcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.CropDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.recycler.AdapterCrops;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        cropDbHelper = new CropDatabaseHelper(this);
        btnRecord = findViewById(R.id.btnRecords);
        setupButtons();

        setupRecyclerView(getAllCrops());
    }

    private void setupButtons(){
        btnRecord.setOnClickListener(v -> {
            getAllCrops();
        });
    }

    public void setupRecyclerView(List<CropModel> cropInfoList){
        RecyclerView recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterCrops(getApplicationContext(), cropInfoList));
    }

    public List<CropModel> getAllCrops(){
        List<CropModel> cropList = cropDbHelper.getAllCrops();
        Log.i("myTag", "got the crops: crop count = " + cropList.size());
        for (CropModel crop: cropList
             ) {
            Log.i(TAG, crop.getName());
        }
        return cropList;
    }

    public void navigateToAddNew(View view) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }
}