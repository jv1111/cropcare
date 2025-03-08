package com.example.cropcare;

import android.os.Bundle;
import android.util.Log;

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
import com.example.cropcare.recycler.AdapterCrops;
import com.example.cropcare.recycler.AdapterTasksSelection;

import java.util.List;

public class TaskSelectionActivity extends AppCompatActivity implements AdapterTasksSelection.ITasksSelection {

    private TaskDatabaseHelper taskDatabaseHelper;
    private int cropId = -1;

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
        cropId = getIntent().getIntExtra("cropId", -1);
        taskDatabaseHelper = new TaskDatabaseHelper(this);
        setupRecyclerView(getTasks());
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
    public void onSelect(int taskId, String taskNote, String repeatEvery, String date) {

    }

    @Override
    public void onDelete(int taskId) {

    }
}