package com.example.cropcare.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.R;

import org.jetbrains.annotations.NotNull;

public class ViewHolderTasks extends RecyclerView.ViewHolder {
    TextView tvTaskDate;
    TextView tvTaskNote;
    TextView tvEvery;
    Button btnDelete;

    public ViewHolderTasks(@NotNull View itemView) {
        super(itemView);
        tvTaskDate = itemView.findViewById(R.id.tvTaskDate);
        tvTaskNote = itemView.findViewById(R.id.tvTaskNote);
        tvEvery = itemView.findViewById(R.id.tvEvery);
        btnDelete = itemView.findViewById(R.id.btnDelete);
    }
}
