package com.example.cropcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.DataBaseHelper;
import com.example.cropcare.Database.RecordsDatabaseHelper;
import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.TimeHelper;
import com.example.cropcare.receivers.AlarmReceiver;
import com.example.cropcare.services.NotifierService;
import com.example.cropcare.test.TestTask;

public class TaskFinishActivity extends AppCompatActivity {

    private TaskModel task;
    private boolean isTaskEnded;
    private int taskId = -1;
    private int cropId = -1;
    private int userId = -1;
    private String cropName = "";
    private EditText etNote;

    private RecordsDatabaseHelper recDb;

    private TextView tvCropname, tvDate, tvNote;
    private Button btnOk, btnGood, btnBad, btnDead;

    private RelativeLayout promptBackgroundLayout;

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

        getIdsFromIntent();
        task = db.getOneTaskById(taskId);
        isTaskEnded = db.isTaskEnded(taskId);

        tvCropname = findViewById(R.id.tvCropName);
        tvDate = findViewById(R.id.tvDate);
        tvNote = findViewById(R.id.tvNote);
        etNote = findViewById(R.id.etNote);
        btnOk = findViewById(R.id.btnOk);

        promptBackgroundLayout = findViewById(R.id.qBackgroundLayout);
        btnGood = findViewById(R.id.btnGood);
        btnBad = findViewById(R.id.btnBad);
        btnDead = findViewById(R.id.btnDead);

        recDb = new RecordsDatabaseHelper(this);

        AlarmReceiver.stopAlarm();
        displayData();
        setTaskStatus();

        NotifierService.stopService(this);
        NotifierService.startService(this);

        btnOk.setOnClickListener(v -> showPrompt());
        promptBackgroundLayout.setOnClickListener(v -> hidePrompt());

        btnGood.setOnClickListener(v->{
            addRecord("Good");
            finish();
        });
        btnBad.setOnClickListener(v->{
            addRecord("Bad");
            finish();
        });
        btnDead.setOnClickListener(v->{
            addRecord("Dead");
            db.deleteOneTask(taskId);
            finish();
        });
    }


    private void addRecord(String status) {
        if (userId == -1 || cropId == -1) {
            Toast.makeText(this, "Invalid user or crop ID", Toast.LENGTH_SHORT).show();
            return;
        }
        String note = tvNote.getText().toString();
        note = note + "\n" + etNote.getText().toString();
        long time = System.currentTimeMillis();

        if (recDb.addNewRecord(userId, cropId, cropName, taskId, note, status, time)) {
            Toast.makeText(this, "Record saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save record", Toast.LENGTH_SHORT).show();
        }

    }

    private void showPrompt() {
        promptBackgroundLayout.setVisibility(View.VISIBLE);
    }

    private void hidePrompt() {
        promptBackgroundLayout.setVisibility(View.GONE);
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

    private void getIdsFromIntent() {
        Intent intent = getIntent();
        if (intent.getBooleanExtra("from_notification", false)) {
            cropId = intent.getIntExtra("cropId", -1);
            taskId = intent.getIntExtra("taskId", -1);
            userId = intent.getIntExtra("userId", -1);
            cropName = intent.getStringExtra("cropName");
            Toast.makeText(this, "Crop ID: " + cropId + ", Task ID: " + taskId + ", UserId: " + userId, Toast.LENGTH_LONG).show();
        }
    }

}