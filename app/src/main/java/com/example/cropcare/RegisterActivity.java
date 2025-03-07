package com.example.cropcare;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.DataBaseHelper;
import com.example.cropcare.Database.UserDatabaseHelper;
import com.example.cropcare.Model.UserModel;
import com.example.cropcare.helper.Validator;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        userDatabaseHelper = new UserDatabaseHelper(this);

    }


    public void registerUser(View view) {
        String username = etUsername.getText().toString().trim().toLowerCase();
        String password = etPassword.getText().toString().trim();

        if(!Validator.isUserInputValid(username, password, this, userDatabaseHelper)) return;
        if (!Validator.isUsernameAvailable(username, this, userDatabaseHelper)) return;

        userDatabaseHelper.addUser(username, password, true);
        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

}