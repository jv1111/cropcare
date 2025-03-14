package com.example.cropcare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.AnimationHelper;
import com.example.cropcare.recycler.AdapterCrops;
import com.example.cropcare.recycler.AdapterTasksSelection;
import com.example.cropcare.services.NotifierService;

import java.util.List;

public class TaskSelectionActivity extends AppCompatActivity implements AdapterTasksSelection.ITasksSelection {

    private TaskDatabaseHelper taskDatabaseHelper;
    private int cropId = -1;
    private int selectedCropId = -1;
    private LinearLayout layoutDelete;
    private Button btnConfirmDelete, btnCancelDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        layoutDelete = findViewById(R.id.layoutDelete);
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete);
        btnCancelDelete = findViewById(R.id.btnCancelDelete);

        cropId = getIntent().getIntExtra("cropId", -1);
        taskDatabaseHelper = new TaskDatabaseHelper(this);

        setupButtonListeners();
        setupRecyclerView(getTasks());
    }

    private void setupButtonListeners() {
        btnConfirmDelete.setOnClickListener(v->{
            taskDatabaseHelper.deleteOneTask(selectedCropId);
            NotifierService.stopService(getApplicationContext());
            NotifierService.startService(getApplicationContext());
            setupRecyclerView(getTasks());
            hideDeletePrompt();
        });

        btnCancelDelete.setOnClickListener(v->{
            Log.i("myTag delete", "hiding delete prompt");
            hideDeletePrompt();
        });
    }

    private void hideDeletePrompt() {
        selectedCropId = -1;
        Log.i("myTag delete", "setting gone");
        layoutDelete.clearAnimation();
        layoutDelete.setVisibility(View.GONE);
    }

    private void setupRecyclerView(List<TaskModel> taskList){

        for ( TaskModel tsk: taskList
             ) {
            Log.i("myTag tasks: ", String.valueOf(tsk.getStartTime()));
        }

        RecyclerView recyclerView = findViewById(R.id.rvTaskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new AdapterTasksSelection(getApplicationContext(), taskList, this));
        //todo create a function for task selection

    }

    private List<TaskModel> getTasks(){
        return taskDatabaseHelper.getAllTasksByCropId(cropId);
    }

    @Override
    public void onSelect(int taskId, String taskNote, int repeatEvery, long startTime, long endTime) {

    }

    @Override
    public void onDelete(int taskId) {
        selectedCropId = taskId;
        showDeletePrompt();
    }

    private void showDeletePrompt(){
        layoutDelete.setVisibility(View.VISIBLE);
        AnimationHelper.popupLinear(layoutDelete, getApplicationContext());
    }

}