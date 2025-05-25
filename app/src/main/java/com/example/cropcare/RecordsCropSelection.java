package com.example.cropcare;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Database.RecordsDatabaseHelper;
import com.example.cropcare.recycler.AdapterRecordsCrops;

public class RecordsCropSelection extends AppCompatActivity implements AdapterRecordsCrops.ICropListControlCB {

    private RecordsDatabaseHelper recordsDbHelper;
    private RecyclerView rv;
    private AdapterRecordsCrops adapter;

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

        rv = findViewById(R.id.rv);
        setupView();
    }

    private void setupView(){
        recordsDbHelper = new RecordsDatabaseHelper(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterRecordsCrops(this, recordsDbHelper.getAllRecords(), this);
        rv.setAdapter(adapter);
    }

    @Override
    public void onSelect(int id, int cropId) {
        Log.i("myTag", "selected: " + id );
    }
}
