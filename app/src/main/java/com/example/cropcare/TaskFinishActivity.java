package com.example.cropcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.TimeHelper;
import com.example.cropcare.receivers.AlarmReceiver;
import com.example.cropcare.services.NotifierService;

public class TaskFinishActivity extends AppCompatActivity {

    private TaskModel task;
    private boolean isTaskEnded;
    private int taskId = -1;

    private TextView tvCropname, tvDate, tvNote;
    private Button btnOk;

    TaskDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new TaskDatabaseHelper(this);

        taskId = getTaskIdFromIntent();
        task = db.getOneTaskById(taskId);
        isTaskEnded = db.isTaskEnded(taskId);

        tvCropname = findViewById(R.id.tvCropName);
        tvDate = findViewById(R.id.tvDate);
        tvNote = findViewById(R.id.tvNote);
        btnOk = findViewById(R.id.btnOk);

        AlarmReceiver.stopAlarm();
        displayData();
        setTaskStatus();

        NotifierService.stopService(this);
        NotifierService.startService(this);

        btnOk.setOnClickListener(v-> {
            finish();
        });
    }

    private void setTaskStatus(){
        if(!isTaskEnded){
            Log.i("myTag", "updating task time");
            db.updateStartTime(taskId, TimeHelper.getNextDate(task));
            TestTask.listAllTasks(this);
        }else{
            Log.i("myTag", "task ended deleting task");
            db.deleteOneTask(taskId);
        }
    }

    private void displayData() {
        if (taskId == -1) return;

        if (task != null) {
            tvCropname.setText(task.getCropName());
            tvDate.setText(TimeHelper.convertMillisToDateTime(task.getStartTime()));
            tvNote.setText(task.getNote());
        }
    }

    private int getTaskIdFromIntent(){
        int taskId = -1;
        Intent intent = getIntent();
        if (intent.getBooleanExtra("from_notification", false)) {
            taskId = intent.getIntExtra("taskId", -1);
            Toast.makeText(this, "Task ID: " + taskId, Toast.LENGTH_LONG).show();
        }
        return taskId;
    }

}