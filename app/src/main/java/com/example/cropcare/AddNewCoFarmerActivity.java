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
import com.example.cropcare.helper.DimensionHelper;
import com.example.cropcare.helper.Validator;

public class AddNewCoFarmerActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnAdd, btnCancel;
    CoFarmerDatabaseHelper coFarmerDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_co_farmer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(DimensionHelper.convertToDp(this, 10), systemBars.top, DimensionHelper.convertToDp(this, 10), systemBars.bottom);
            return insets;
        });

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);

        coFarmerDbHelper = new CoFarmerDatabaseHelper(this);


        setupButtonsFunctions();

    }

    private void setupButtonsFunctions() {
        btnAdd.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();

            if(!Validator.isUserInputValid(username, password, this)) return;
            if (!Validator.isUsernameAvailable(username, this, new UserDatabaseHelper(this), coFarmerDbHelper)) return;
            if(!Validator.isPasswordValid(password, this)) return;

            coFarmerDbHelper.addCoFarmer(Auth.userId ,username, password);

            finish();
        });

        btnCancel.setOnClickListener(v->{
            finish();
        });

    }
}