package com.example.cropcare.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.R;

import org.jetbrains.annotations.NotNull;

public class ViewHolderCrops extends RecyclerView.ViewHolder {
    TextView tvCropName;
    TextView tvCropDate;
    Button btnDelete;
    Button btnUpdate;
    ImageView ivMenu;
    LinearLayout layoutMenu;
    MotionLayout itemMainLayout;

    public ViewHolderCrops(@NotNull View itemView) {
        super(itemView);
        tvCropName = itemView.findViewById(R.id.tvCropName);
        tvCropDate = itemView.findViewById(R.id.tvCropDate);
        btnDelete = itemView.findViewById(R.id.btnDelete);
        btnUpdate = itemView.findViewById(R.id.btnUpdate);
        ivMenu = itemView.findViewById(R.id.ivMenu);
        layoutMenu = itemView.findViewById(R.id.layoutMenu);
        itemMainLayout = itemView.findViewById(R.id.itemMainLayout);
    }
}
