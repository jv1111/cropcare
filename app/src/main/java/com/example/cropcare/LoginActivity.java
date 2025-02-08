package com.example.cropcare;

import android.content.Intent;
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
import com.example.cropcare.Model.UserModel;
import com.example.cropcare.helper.Validator;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "myTag loginActivity: ";
    private DataBaseHelper dbHelper;

    private EditText etUsername;
    private EditText etPassword;

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

        dbHelper = new DataBaseHelper(this);
    }

    public void navToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        String username = etUsername.getText().toString().toLowerCase().trim();
        String password = etPassword.getText().toString().toLowerCase().trim();

        UserModel user = dbHelper.getUserByUsername(username);
        if (user == null) Toast.makeText(this, "User not found", Toast.LENGTH_LONG).show();
        else
            if(!Objects.equals(user.getPassword(), password)) Toast.makeText(this, "Invalid password", Toast.LENGTH_LONG).show();
        else{
                Log.i(TAG, "Logged in user: " + user.toString());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
    }
}
