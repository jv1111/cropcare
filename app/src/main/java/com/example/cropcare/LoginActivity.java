package com.example.cropcare;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.CoFarmerDatabaseHelper;
import com.example.cropcare.Database.UserDatabaseHelper;
import com.example.cropcare.Model.CoFarmerModel;
import com.example.cropcare.Model.UserModel;
import com.example.cropcare.helper.AnimationHelper;
import com.example.cropcare.helper.DatabaseBackupHelper;
import com.example.cropcare.helper.LocalStorageHelper;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "myTag loginActivity: ";
    private UserDatabaseHelper userDatabaseHelper;
    private CoFarmerDatabaseHelper coFarmerDatabaseHelper;

    private EditText etUsername;
    private EditText etPassword;

    private Button btnLogin, btnRegister, btnCancelDataShare, btnImport, btnExport, btnDemo;
    private ImageButton btnDataSharing;

    private LinearLayout layoutDataSharing;

    private LocalStorageHelper localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnDemo = findViewById(R.id.btnDemo);
        btnDataSharing = findViewById(R.id.btnDataSharing);
        btnCancelDataShare = findViewById(R.id.btnCancelDataShare);
        layoutDataSharing = findViewById(R.id.layoutDataSharing);
        btnImport = findViewById(R.id.btnImport);
        btnExport = findViewById(R.id.btnExport);

        localStorage = new LocalStorageHelper(this);
        userDatabaseHelper = new UserDatabaseHelper(this);
        coFarmerDatabaseHelper = new CoFarmerDatabaseHelper(this);
        checkLogin();

        buttonsFunctions();
    }

    private void buttonsFunctions(){
        btnLogin.setOnClickListener(v -> {
            login();
        });
        btnRegister.setOnClickListener(v -> {
            navigateToRegister();
        });
        btnDataSharing.setOnClickListener(v->{
            showDataSharingMenu();
        });
        btnCancelDataShare.setOnClickListener(v -> {
            hideDataSharingMenu();
        });
        btnExport.setOnClickListener(v->{
            DatabaseBackupHelper.exportDatabase(this);
        });
        btnImport.setOnClickListener(v->{
            DatabaseBackupHelper.openFilePicker(this, pickFileAndImportDb);
        });
        btnDemo.setOnClickListener(v->{
            loginDemo();
        });
    }

    private void hideDataSharingMenu() {
        layoutDataSharing.clearAnimation();
        layoutDataSharing.setVisibility(View.GONE);
    }

    private void showDataSharingMenu() {
        AnimationHelper.popupLinear(layoutDataSharing, getApplicationContext());
        layoutDataSharing.setVisibility(View.VISIBLE);
    }

    public void checkLogin(){
        if(localStorage.getUserId() != -1){
            Auth.setUserData(localStorage.getUserId(), localStorage.getUsername(), localStorage.isAdmin(), localStorage.getParentId(), false);
            navigateToHomePage();
        }
    }

    public void login() {
        String username = etUsername.getText().toString().toLowerCase().trim();
        String password = etPassword.getText().toString().toLowerCase().trim();

        if (attemptUserLogin(username, password)) return;
        if (attemptCoFarmerLogin(username, password)) return;

        Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
    }

    public void loginDemo(){
        localStorage.saveUserData(0, "demo", true, null, true);
        Auth.setUserData(0, "demo", true, null, true);
        navigateToHomePage();
    }

    private boolean attemptUserLogin(String username, String password) {
        Log.i("myTag: ", "attempt user");
        UserModel user = userDatabaseHelper.getUserByUsername(username);
        if (user == null) return false;

        if (!Objects.equals(user.getPassword(), password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
            return true;
        }

        localStorage.saveUserData(user.getId(), user.getUsername(), user.isAdmin(), null, false);
        Auth.setUserData(user.getId(), user.getUsername(), user.isAdmin(), null, false);

        Toast.makeText(this, "Logged in admin", Toast.LENGTH_LONG).show();
        navigateToHomePage();
        return true;
    }

    private boolean attemptCoFarmerLogin(String username, String password) {
        Log.i("myTag: ", "attempt co farm");
        CoFarmerModel coFarmer = coFarmerDatabaseHelper.getCoFarmerByUsername(username);
        if (coFarmer == null) return false;

        if (!Objects.equals(coFarmer.getPassword(), password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
            return true;
        }

        localStorage.saveUserData(coFarmer.getId(), coFarmer.getUsername(), false, coFarmer.getParentUserId(), false);
        Auth.setUserData(coFarmer.getId(), coFarmer.getUsername(), false, coFarmer.getParentUserId(), false);
        Toast.makeText(this, "Logged in co farmer", Toast.LENGTH_LONG).show();
        navigateToHomePage();
        return true;
    }

    private void navigateToHomePage(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void navigateToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> pickFileAndImportDb = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri fileUri = result.getData().getData();
                    if (fileUri != null) {
                        DatabaseBackupHelper.importDatabase(this, fileUri);
                        hideDataSharingMenu();
                    }
                }
            }
    );

}
