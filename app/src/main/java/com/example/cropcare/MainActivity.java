package com.example.cropcare;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.CropDatabaseHelper;
import com.example.cropcare.Database.TaskDatabaseHelper;
import com.example.cropcare.Model.CropModel;
import com.example.cropcare.helper.AnimationHelper;
import com.example.cropcare.helper.LocalStorageHelper;
import com.example.cropcare.helper.Permissions;
import com.example.cropcare.helper.Validator;
import com.example.cropcare.recycler.AdapterCrops;
import com.example.cropcare.services.NotifierService;
import com.example.cropcare.test.TestRecords;
import com.example.cropcare.test.TestTask;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterCrops.ICropListControlCB {

    private CropDatabaseHelper cropDbHelper;
    private TaskDatabaseHelper taskDatabaseHelper;
    private String TAG = "myTag";
    private Button btnRecord, btnAdd, btnCancelUpdate, btnConfirmUpdate, btnConfirmDelete, btnCancelDelete;
    private Button btnLogout, btnAddCoFarmer, btnUpdatePassword;
    private RecyclerView rv;
    private AdapterCrops adapter;
    private TextView tvUsername, tvAccountType;
    private LocalStorageHelper localStorageHelper;
    private ImageButton btnMenu;
    private LinearLayout layoutMenu, layoutUpdate, layoutUpdatePanel, layoutDelete, layoutDeletePanel;
    private EditText etUpdateCropName;

    private int selectedCropId = -1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        localStorageHelper = new LocalStorageHelper(this);

        cropDbHelper = new CropDatabaseHelper(this);
        taskDatabaseHelper = new TaskDatabaseHelper(this);

        btnRecord = findViewById(R.id.btnRecords);
        btnLogout = findViewById(R.id.btnLogout);
        rv = findViewById(R.id.rv);
        tvUsername = findViewById(R.id.tvUsername);
        tvAccountType = findViewById(R.id.tvAccountType);
        btnMenu = findViewById(R.id.btnMenu);
        layoutMenu = findViewById(R.id.layoutMenu);
        layoutUpdate = findViewById(R.id.layoutUpdate);
        btnAddCoFarmer = findViewById(R.id.btnAddCoFarmer);
        btnAdd = findViewById(R.id.btnAddNewCropTask);
        btnCancelUpdate = findViewById(R.id.btnCancelUpdate);
        btnConfirmUpdate = findViewById(R.id.btnConfirmUpadte);
        layoutUpdatePanel = findViewById(R.id.layoutUpdatePanel);
        etUpdateCropName = findViewById(R.id.etUpdateCropName);
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete);
        btnCancelDelete = findViewById(R.id.btnCancelDelete);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        layoutDelete = findViewById(R.id.layoutDelete);
        layoutDeletePanel = findViewById(R.id.layoutDeletePanel);

        tvUsername.setText("Welcome, " + Auth.username);
        tvAccountType.setText("Type: " + (Auth.isAdmin? "Admin" : "Co-Farmer"));

        setRestrictions();
        setupButtons();
        demoFunction();
        setupRecyclerView(getAllCrops());

        TestRecords.listAllRecords(this);

        if(NotifierService.isRunning) NotifierService.stopService(this);
        NotifierService.startService(this);
    }

    private void setRestrictions(){
        if(!Auth.isAdmin){
            btnAdd.setEnabled(false);
            btnAddCoFarmer.setEnabled(false);
        }
    }

    private void setupButtons(){

        btnRecord.setOnClickListener(v -> {
            TestTask.listAllCrops(getApplicationContext());
            TestTask.listAllTasks(getApplicationContext());
        });

        btnLogout.setOnClickListener(v -> {
            localStorageHelper.clearUserData();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            NotifierService.stopService(this);
            finish();
        });

        btnMenu.setOnClickListener(v->{
            showSettingsMenu();
        });

        layoutMenu.setOnClickListener(v->{
            hideSettingsMenu();
        });

        btnAddCoFarmer.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, AddNewCoFarmerActivity.class));
        });

        btnAdd.setOnClickListener(v->{
            navigateToAddNew();
        });

        btnCancelUpdate.setOnClickListener(v->{
            hideUpdateMenu();
        });

        btnConfirmUpdate.setOnClickListener(v->{
            String cropName = etUpdateCropName.getText().toString().trim();
            if(Validator.isCropNameValid(cropName)){
                cropDbHelper.updateCropName(selectedCropId, cropName);
                taskDatabaseHelper.updateTaskCropName(selectedCropId, cropName);
                etUpdateCropName.setText("");
                setupRecyclerView(getAllCrops());
                hideUpdateMenu();
            }
        });

        btnConfirmDelete.setOnClickListener(v->{
            cropDbHelper.deleteCropById(selectedCropId);
            taskDatabaseHelper.deleteAllTaskByCropId(selectedCropId);
            NotifierService.stopService(getApplicationContext());
            NotifierService.startService(getApplicationContext());
            setupRecyclerView(getAllCrops());
            hideDeletePrompt();
        });

        btnCancelDelete.setOnClickListener(v->{
            Log.i("myTag delete", "hiding delete prompt");
            hideDeletePrompt();
        });

        btnUpdatePassword.setOnClickListener(v->{
            navigateToUpdatePassword();
        });

    }

    private void demoFunction(){
        if(Auth.isDemo){
            TestTask.createThreeTasks(this);
        }
    }

    private void hideSettingsMenu() {
        layoutMenu.clearAnimation();
        layoutMenu.setVisibility(View.GONE);
    }

    private void showSettingsMenu() {
        layoutMenu.setVisibility(View.VISIBLE);
        AnimationHelper.popupLinear(layoutMenu, this);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView(getAllCrops());
    }

    private void setupRecyclerView(List<CropModel> cropInfoList){
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterCrops(getApplicationContext(), cropInfoList, this);
        rv.setAdapter(adapter);
    }

    private List<CropModel> getAllCrops(){
        Auth.printUserInfo();
        List<CropModel> cropList = cropDbHelper.getAllCrops(Auth.isAdmin ? Auth.userId : Auth.parentId);
        Log.i("myTag", "got the crops: crop count = " + cropList.size());
        for (CropModel crop: cropList
             ) {
            Log.i(TAG, crop.getName());
        }
        return cropList;
    }

    private void navigateToAddNew() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

    private void navigateToUpdatePassword(){
        startActivity(new Intent(MainActivity.this, UpdatePasswordActivity.class));
    }

    private void showUpdateMenu(){
        layoutUpdate.setVisibility(View.VISIBLE);
        AnimationHelper.popupLinear(layoutUpdatePanel, getApplicationContext());
    }

    private void hideUpdateMenu(){
        layoutUpdate.clearAnimation();
        layoutUpdate.setVisibility(View.GONE);
        selectedCropId = -1;
    }

    private void showDeletePrompt(){
        layoutDelete.setVisibility(View.VISIBLE);
        AnimationHelper.popupLinear(layoutDelete, getApplicationContext());
    }

    private void hideDeletePrompt(){
        selectedCropId = -1;
        Log.i("myTag delete", "setting gone");
        layoutDelete.clearAnimation();
        layoutDelete.setVisibility(View.GONE);
    }

    @Override
    public void onUpdate(int id, String cropName) {
        selectedCropId = id;
        showUpdateMenu();
    }

    @Override
    public void onSelect(int id, String cropName) {
        Intent intent = new Intent(MainActivity.this, TaskSelectionActivity.class);
        intent.putExtra("cropId", id);
        intent.putExtra("cropName", cropName);
        startActivity(intent);
    }

    @Override
    public void onDelete(int id) {
        selectedCropId = id;
        showDeletePrompt();
    }

}