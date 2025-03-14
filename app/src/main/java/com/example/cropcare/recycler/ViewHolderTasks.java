package com.example.cropcare.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.R;

import org.jetbrains.annotations.NotNull;

public class ViewHolderTasks extends RecyclerView.ViewHolder {
    TextView tvTaskDate;
    TextView tvTaskNote;
    TextView tvEvery;
    ImageView ivMenu;
    Button btnDelete;
    MotionLayout itemMainLayout;

    public ViewHolderTasks(@NotNull View itemView) {
        super(itemView);
        tvTaskDate = itemView.findViewById(R.id.tvTaskDate);
        tvTaskNote = itemView.findViewById(R.id.tvTaskNote);
        tvEvery = itemView.findViewById(R.id.tvEvery);
        ivMenu = itemView.findViewById(R.id.ivMenu);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        itemMainLayout = itemView.findViewById(R.id.itemMainLayout);
    }
}
