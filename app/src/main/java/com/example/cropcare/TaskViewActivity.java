package com.example.cropcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.TimeHelper;

public class TaskViewActivity extends AppCompatActivity {

    private TextView tvCropName, tvNote, tvStartDateInfo, tvEndDateInfo, tvRepeatEveryInfo;
    private MotionLayout motionLayout;
    private Button btnEdit, btnCancel;
    private TaskDatabaseHelper taskDb;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        motionLayout = findViewById(R.id.main);
        btnEdit = findViewById(R.id.btnEdit);
        btnCancel = findViewById(R.id.btnCancel);
        tvCropName = findViewById(R.id.tvCropName);
        tvNote = findViewById(R.id.tvNote);
        tvStartDateInfo = findViewById(R.id.tvStartDateInfo);
        tvEndDateInfo = findViewById(R.id.tvEndDateInfo);
        tvRepeatEveryInfo = findViewById(R.id.tvRepeatInfo);

        taskDb = new TaskDatabaseHelper(this);

        taskId = getIntent().getIntExtra("id", -1);

        setupView();
        setupButtons();
    }

    @SuppressLint("SetTextI18n")
    private void setupView() {
        TaskModel task = taskDb.getOneTaskById(taskId);
        tvCropName.setText("Crop: " + task.getCropName());
        tvNote.setText("Note: " + task.getNote());
        tvStartDateInfo.setText("Start Date: " + TimeHelper.convertMillisToDateTime(task.getStartTime()));
        tvEndDateInfo.setText("End Date: " + TimeHelper.convertMillisToDateTime(task.getEndTime()));
        if(task.isRepeat()){
            tvRepeatEveryInfo.setText("Repeat Every: " + task.getRepeatEveryDays() + "day/s");
        }else{
            tvRepeatEveryInfo.setVisibility(TextView.GONE);
        }
    }

    private void setupButtons() {
        btnEdit.setOnClickListener(v -> motionLayout.transitionToEnd());
        btnCancel.setOnClickListener(v -> motionLayout.transitionToStart());
    }
}
