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

    public interface OnDateTimeSelectedListener {
        void onDateTimeSelected(long millis);
    }

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
            showDateTimePicker(millis -> {
                startTime = millis;
                String date = "Start Date: " + TimeHelper.convertMillisToDateTime(millis);
                tvStartDate.setText(date);
                Toast.makeText(this, "Start Date Millis: " + millis, Toast.LENGTH_SHORT).show();
            });
        });
        btnEndDate.setOnClickListener(v -> {
            showDateTimePicker(millis -> {
                endTime = millis;
                String date = "End Date: " + TimeHelper.convertMillisToDateTime(millis);
                tvEndDate.setText(date);
                Toast.makeText(this, "Start Date Millis: " + millis, Toast.LENGTH_SHORT).show();
            });
        });
        btnOk.setOnClickListener(v -> {
            if (isRepeat) {
                try {
                    repeatEveryDays = Integer.parseInt(etRepeatEvery.getText().toString());
                } catch (NumberFormatException e) {
                    repeatEveryDays = 1; // Default to 0 if input is invalid
                }
                addTask(cropName, cropId, note, startTime, endTime, isRepeat, repeatEveryDays);
            }else{
                endTime = startTime;
                addTask(cropName, cropId, note, startTime, endTime, isRepeat, 1);
            }
        });
        btnCancel.setOnClickListener(v -> {
            List<TaskModel> tasks = taskDatabaseHelper.getAllTasks();
            for (TaskModel task : tasks) {
                Log.i("TaskList", task.toString());
            }
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
            btnEndDate.setEnabled(false);
            endTime = 0;
            repeatEveryDays = 1;
            tvEndDate.setText("End Date: Not Set");
        }else{
            etRepeatEvery.setEnabled(true);
            btnEndDate.setEnabled(true);
            btnEndDate.setEnabled(true);
        }
    }

    private void addTask(String cropName, int cropId, String note, long startTime, long endTime, boolean isRepeat, int repeatEveryDays) {
        if (cropName == null || cropName.isEmpty() || note == null || note.isEmpty() || startTime <= 0 || endTime <= 0 || repeatEveryDays < 0) {
            Toast.makeText(this, "All parameters must be valid and not empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        taskDatabaseHelper.addNewTask(cropName, cropId, Auth.userId, note, startTime, endTime, isRepeat, repeatEveryDays);
        Toast.makeText(this, "Task added successfully!", Toast.LENGTH_SHORT).show();
        NotifierService.stopService(this);
        NotifierService.startService(this);
        finish();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    Toast.makeText(this, selectedDate, Toast.LENGTH_LONG).show();
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showDateTimePicker(OnDateTimeSelectedListener listener) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {

                // Convert selected date and time to milliseconds
                Calendar selectedDateTime = Calendar.getInstance();
                selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                long millis = selectedDateTime.getTimeInMillis();

                listener.onDateTimeSelected(millis); // Pass the millis to the callback

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);//start time
            timePickerDialog.show();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));//start date
        datePickerDialog.show();
    }

}