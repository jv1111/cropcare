package com.example.cropcare;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cropcare.Database.CropDatabaseHelper;

public class NewCropActivity extends AppCompatActivity {

    Button btnOk, btnCancle;
    EditText etCropName;
    private CropDatabaseHelper cropDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_crop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cropDb = new CropDatabaseHelper(this);

        btnOk = findViewById(R.id.btnConfirmAddCrop);
        etCropName = findViewById(R.id.etCropName);

        setupButtonFunction();
    }

    private void setupButtonFunction(){
        btnOk.setOnClickListener(v -> {
            String cropName = etCropName.getText().toString();
            Log.i("myTag adding crop: ", cropName);
            cropDb.addNewCrop(cropName);
            finish();
        });
    }
}