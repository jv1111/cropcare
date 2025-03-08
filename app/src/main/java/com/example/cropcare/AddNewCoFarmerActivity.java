package com.example.cropcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.CoFarmerDatabaseHelper;
import com.example.cropcare.Database.UserDatabaseHelper;
import com.example.cropcare.helper.Validator;

public class AddNewCoFarmerActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnAddCoFarmer;
    CoFarmerDatabaseHelper coFarmerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_co_farmer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnAddCoFarmer = findViewById(R.id.btnAddCoFarmer);

        coFarmerDbHelper = new CoFarmerDatabaseHelper(this);


        setupButtonsFunctions();

    }

    private void setupButtonsFunctions() {
        btnAddCoFarmer.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();

            if(!Validator.isUserInputValid(username, password, this)) return;
            if (!Validator.isUsernameAvailable(username, this, new UserDatabaseHelper(this), coFarmerDbHelper)) return;

            coFarmerDbHelper.addCoFarmer(Auth.userId ,username, password);

            finish();
        });
    }
}