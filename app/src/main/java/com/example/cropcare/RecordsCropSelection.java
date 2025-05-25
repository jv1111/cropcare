package com.example.cropcare;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.RecordsDatabaseHelper;
import com.example.cropcare.recycler.AdapterRecordsCrops;

public class RecordsCropSelection extends AppCompatActivity {

    private RecordsDatabaseHelper recordsDbHelper;
    private RecyclerView rv;
    private AdapterRecordsCrops adapter;
    private EditText etFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_records_crop_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFilter = findViewById(R.id.etFilter);
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setupView(etFilter.getText().toString());
            }
        });

        rv = findViewById(R.id.rv);
        setupView(etFilter.getText().toString());
    }

    private void setupView(String cropName){
        recordsDbHelper = new RecordsDatabaseHelper(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterRecordsCrops(recordsDbHelper.getAllRecordsByUserId(Auth.userId, cropName));
        rv.setAdapter(adapter);
    }

}
