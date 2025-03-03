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
import com.example.cropcare.helper.TimeConverter;
import com.example.cropcare.receivers.AlarmReceiver;
import com.example.cropcare.services.NotifierService;

public class TaskActivity extends AppCompatActivity {

    private int taskId = 0;
    private TaskModel task;

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

        tvCropname = findViewById(R.id.tvCropName);
        tvDate = findViewById(R.id.tvDate);
        tvNote = findViewById(R.id.tvNote);

        btnOk = findViewById(R.id.btnOk);

        setTaskId();
        AlarmReceiver.stopAlarm();

        displayData();
        nextDate();

        NotifierService.stopService(this);
        //TODO UPDATE THE TASK IF IT HAS A REPEAT VALUE
        //TODO RESTART THE SERVICE TO RESTART COUNTING

        btnOk.setOnClickListener(v-> {
            NotifierService.startService(this);
        });
    }

    private long nextDate() {
        int repeatDays = task.getRepeatEveryDays();
        long newStartTime;

        long currentTime = task.getStartTime();
        newStartTime = Math.max(task.getStartTime() + (repeatDays * 86400000L), currentTime + 86400000L);
        Log.i("myTag", "every: " + repeatDays);
        Log.i("myTag", "Next start time: " + TimeConverter.convertMillisToDateTime(newStartTime));
        return newStartTime;
    }

    private void displayData() {
        if (taskId == -1) return;

        task = db.getOneTaskById(taskId);
        if (task != null) {
            tvCropname.setText(task.getCropName());
            tvDate.setText(TimeConverter.convertMillisToDateTime(task.getStartTime()));
            tvNote.setText(task.getNote());
        }
    }

    private void setTaskId(){
        Intent intent = getIntent();
        if (intent.getBooleanExtra("from_notification", false)) {
            taskId = intent.getIntExtra("taskId", -1);
            Toast.makeText(this, "Task ID: " + taskId, Toast.LENGTH_LONG).show();
        }
    }

}