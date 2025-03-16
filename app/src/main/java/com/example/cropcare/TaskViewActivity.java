package com.example.cropcare;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.helper.TimeHelper;
import com.example.cropcare.helper.Validator;
import com.example.cropcare.services.NotifierService;

public class TaskViewActivity extends AppCompatActivity {

    private TextView tvCropName, tvNote, tvStartDateInfo, tvEndDateInfo, tvRepeatEveryInfo;
    private MotionLayout motionLayout;
    private Button btnEdit, btnCancel;
    private TaskDatabaseHelper taskDb;

    private TextView tvStartDate, tvEndDate;
    private Button btnSelectStartDate, btnSelectEndDate, btnConfirmEdit;
    private EditText etTaskNote, etRepeatDay;
    private CheckBox cbRepeat;

    private int taskId;
    private long startTime, endTime;

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
        tvCropName = findViewById(R.id.tvCropName);
        tvNote = findViewById(R.id.tvNote);
        tvStartDateInfo = findViewById(R.id.tvStartDateInfo);
        tvEndDateInfo = findViewById(R.id.tvEndDateInfo);
        tvRepeatEveryInfo = findViewById(R.id.tvRepeatInfo);

        etTaskNote = findViewById(R.id.etTaskNote);
        btnSelectStartDate = findViewById(R.id.btnSelectStartDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        btnSelectEndDate = findViewById(R.id.btnSelectEndDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        cbRepeat = findViewById(R.id.cbRepeat);
        etRepeatDay = findViewById(R.id.etRepeatDay);
        btnConfirmEdit = findViewById(R.id.btnConfirmEdit);
        btnCancel = findViewById(R.id.btnCancel);

        taskDb = new TaskDatabaseHelper(this);

        taskId = getIntent().getIntExtra("id", -1);

        setupRestrictions();
        setupEditView();
        setupInfoView();
        setupButtons();
    }

    private void setupRestrictions() {
        btnEdit.setEnabled(Auth.isAdmin);
    }

    @SuppressLint("SetTextI18n")
    private void setupInfoView() {
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

        btnSelectStartDate.setOnClickListener(v->{
            TimeHelper.showDateTimePicker(this, millis -> {
                startTime = millis;
                String date = "Start Date: " + TimeHelper.convertMillisToDateTime(millis);
                tvStartDate.setText(date);
            });
        });

        btnSelectEndDate.setOnClickListener(v->{
            TimeHelper.showDateTimePicker(this, millis -> {
                endTime = millis;
                String date = "End Date: " + TimeHelper.convertMillisToDateTime(millis);
                tvEndDate.setText(date);
            });
        });

        cbRepeat.setOnCheckedChangeListener((buttonView, isChecked) ->{
            setupEditView();
        });

        btnConfirmEdit.setOnClickListener(v->{
            String note = etTaskNote.getText().toString().trim();
            boolean isRepeat = cbRepeat.isChecked();
            String repeatDaysStr = etRepeatDay.getText().toString().trim();
            int repeatEveryDays = repeatDaysStr.isEmpty() ? 0 : Integer.parseInt(repeatDaysStr);

            if (!Validator.validateTaskInput(this, note, isRepeat, startTime, endTime, repeatEveryDays)) {
                return;
            }

            if(!isRepeat){
                repeatEveryDays = 1;
                endTime = startTime;
            }

            taskDb.updateTaskExceptCropName(taskId, note, startTime, endTime, isRepeat, repeatEveryDays);
            motionLayout.transitionToStart();
            setupInfoView();

            NotifierService.stopService(this);
            NotifierService.startService(this);
        });

    }

    private void setupEditView() {
        if(!cbRepeat.isChecked()){
            endTime = 0;
            tvEndDate.setText("End Date: Not Set");
            etRepeatDay.setEnabled(false);
            btnSelectEndDate.setEnabled(false);
        }else{
            etRepeatDay.setEnabled(true);
            btnSelectEndDate.setEnabled(true);
        }
    }
}
