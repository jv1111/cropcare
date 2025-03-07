package com.example.cropcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.CropDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.recycler.AdapterCropSelection;

import java.util.List;

public class CropSelectionForTaskActivity extends AppCompatActivity implements AdapterCropSelection.IOnSelection {

    private CropDatabaseHelper cropDbHelper;
    private String TAG = "myTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crop_selection_for_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cropDbHelper = new CropDatabaseHelper(this);

        setupRecyclerView(getAllCrops());
    }

    public void setupRecyclerView(List<CropModel> cropInfoList){
        RecyclerView recyclerView = findViewById(R.id.rvCropSelectionForTaskCreation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterCropSelection(getApplicationContext(), cropInfoList, this));
    }

    public List<CropModel> getAllCrops(){
        List<CropModel> cropList = cropDbHelper.getAllCrops(Auth.userId);
        Log.i("myTag", "got the crops: crop count = " + cropList.size());
        for (CropModel crop: cropList) {
            Log.i(TAG, crop.getName());
        }
        return cropList;
    }

    @Override
    public void onSelect(String date, String cropName, int id) {
        Log.i("myTag", "selected new: " + cropName);
        Intent intent = new Intent(CropSelectionForTaskActivity.this, AddNewTaskActivity.class);
        intent.putExtra("date", date);
        intent.putExtra("cropName", cropName);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
