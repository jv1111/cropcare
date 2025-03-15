package com.example.cropcare;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.CoFarmerDatabaseHelper;
import com.example.cropcare.Database.UserDatabaseHelper;
import com.example.cropcare.helper.DimensionHelper;
import com.example.cropcare.helper.Validator;

public class UpdatePasswordActivity extends AppCompatActivity {

    private Button btnCancel, btnUpdate;
    private EditText etPassword, etConfirmPassword;

    UserDatabaseHelper userDb;
    CoFarmerDatabaseHelper coFarmerDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(DimensionHelper.convertToDp(this, 10), systemBars.top, DimensionHelper.convertToDp(this, 10), systemBars.bottom);
            return insets;
        });

        userDb = new UserDatabaseHelper(this);
        coFarmerDb = new CoFarmerDatabaseHelper(this);

        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);

        //TODO ADD AN IMPORT AND EXTRACT DATABASE

        btnCancel.setOnClickListener(v->{
            finish();
        });

        btnUpdate.setOnClickListener(v->{
            updatePassword();
        });

    }

    private void updatePassword(){
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if(!Validator.isPasswordValid(password, getApplicationContext())) return;
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getApplicationContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Auth.isAdmin) userDb.updatePassword(Auth.userId, password);
        else coFarmerDb.updatePassword(Auth.userId, password);

        finish();
    }

}