package com.example.cropcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.CoFarmerDatabaseHelper;
import com.example.cropcare.Database.DataBaseHelper;
import com.example.cropcare.Database.UserDatabaseHelper;
import com.example.cropcare.Model.CoFarmerModel;
import com.example.cropcare.Model.UserModel;
import com.example.cropcare.helper.LocalStorageHelper;
import com.example.cropcare.helper.Validator;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "myTag loginActivity: ";
    private UserDatabaseHelper userDatabaseHelper;
    private CoFarmerDatabaseHelper coFarmerDatabaseHelper;

    private EditText etUsername;
    private EditText etPassword;

    private Button btnLogin, btnRegister;

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
    }

    public void checkLogin(){
        if(localStorage.getUserId() != -1){
            Auth.setUserData(localStorage.getUserId(), localStorage.getUsername(), localStorage.isAdmin(), localStorage.getParentId());
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

    private boolean attemptUserLogin(String username, String password) {
        Log.i("myTag: ", "attempt user");
        UserModel user = userDatabaseHelper.getUserByUsername(username);
        if (user == null) return false;

        if (!Objects.equals(user.getPassword(), password)) {
            Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
            return true;
        }

        localStorage.saveUserData(user.getId(), user.getUsername(), user.isAdmin(), null);
        Auth.setUserData(user.getId(), user.getUsername(), user.isAdmin(), null);

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

        localStorage.saveUserData(coFarmer.getId(), coFarmer.getUsername(), false, coFarmer.getParentUserId());
        Auth.setUserData(coFarmer.getId(), coFarmer.getUsername(), false, coFarmer.getParentUserId());
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

}
