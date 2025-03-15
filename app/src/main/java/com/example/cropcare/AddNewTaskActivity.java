package com.example.cropcare;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.cropcare.helper.Validator;
import com.example.cropcare.services.NotifierService;

import java.util.Calendar;
import java.util.List;

public class AddNewTaskActivity extends AppCompatActivity {

    private TextView tvCropName, tvStartDate, tvEndDate;
    private Button btnStartDate, btnEndDate, btnOk, btnCancel;
    private CheckBox cbRepeat;
    private String cropName;
    private EditText etTaskNote, etRepeatEvery;
    private TaskDatabaseHelper taskDatabaseHelper;
    private int cropId;

    private long startTime, endTime;
    private int repeatEveryDays = 1;
    private boolean isRepeat = false;
    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        taskDatabaseHelper = new TaskDatabaseHelper(this);

        cropName = getIntent().getStringExtra("cropName");
        cropId = getIntent().getIntExtra("id", -1);

        tvCropName = findViewById(R.id.tvCropName);
        tvCropName.setText(cropName);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        btnStartDate = findViewById(R.id.btnSelectStartDate);
        btnEndDate = findViewById(R.id.btnSelectEndDate);
        cbRepeat = findViewById(R.id.cbRepeat);
        etTaskNote = findViewById(R.id.etTaskNote);
        etRepeatEvery = findViewById(R.id.etDay);
        btnOk = findViewById(R.id.btnOk);
        btnCancel = findViewById(R.id.btnCancel);

        setIsRepeatView(isRepeat);
        setupButtonsFunctions();
    }

    private void setupButtonsFunctions(){
        btnStartDate.setOnClickListener(v -> {
            TimeHelper.showDateTimePicker(this, millis -> {
                startTime = millis;
                String date = "Start Date: " + TimeHelper.convertMillisToDateTime(millis);
                tvStartDate.setText(date);
                Toast.makeText(this, "Start Date Millis: " + millis, Toast.LENGTH_SHORT).show();
            });
        });

        btnEndDate.setOnClickListener(v -> {
            TimeHelper.showDateTimePicker(this, millis -> {
                endTime = millis;
                String date = "End Date: " + TimeHelper.convertMillisToDateTime(millis);
                tvEndDate.setText(date);
                Toast.makeText(this, "Start Date Millis: " + millis, Toast.LENGTH_SHORT).show();
            });
        });

        btnOk.setOnClickListener(v -> {

            String note = etTaskNote.getText().toString().trim();
            boolean isRepeat = cbRepeat.isChecked();
            String repeatDaysStr = etRepeatEvery.getText().toString().trim();
            int repeatEveryDays = repeatDaysStr.isEmpty() ? 0 : Integer.parseInt(repeatDaysStr);

            if (!Validator.validateTaskInput(this, note, isRepeat, startTime, endTime, repeatEveryDays)) {
                return;
            }

            if(!isRepeat){
                repeatEveryDays = 1;
                endTime = startTime;
            }

            taskDatabaseHelper.addNewTask(cropName, cropId, Auth.userId, note, startTime, endTime, isRepeat, repeatEveryDays);
            NotifierService.stopService(this);
            NotifierService.startService(this);
            finish();

        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });

        cbRepeat.setOnCheckedChangeListener((buttonView, isChecked) ->{
            isRepeat = isChecked;
            setIsRepeatView(isRepeat);
        });

        etTaskNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                note = s.toString();
            }
        });
    }

    private void setIsRepeatView(boolean isRepeat){
        if(!isRepeat){
            etRepeatEvery.setEnabled(false);
            btnEndDate.setEnabled(false);
            endTime = 0;
            repeatEveryDays = 1;
            tvEndDate.setText("End Date: Not Set");
        }else{
            etRepeatEvery.setEnabled(true);
            btnEndDate.setEnabled(true);
        }
    }

}