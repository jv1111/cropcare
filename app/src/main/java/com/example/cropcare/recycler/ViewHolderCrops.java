package com.example.cropcare.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.R;

import org.jetbrains.annotations.NotNull;

public class ViewHolderCrops extends RecyclerView.ViewHolder {
    TextView tvCropName;
    TextView tvCropDate;

    public ViewHolderCrops(@NotNull View itemView ){
        super(itemView);
        tvCropName = itemView.findViewById(R.id.tvCropName);
        tvCropDate = itemView.findViewById(R.id.tvCropDate);
    }

}
