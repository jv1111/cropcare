package com.example.cropcare.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.R;

public class ViewHolderRecordsCrops extends RecyclerView.ViewHolder {
    TextView tvCropName;
    TextView tvCropDate;
    TextView tvStatus;
    TextView tvNote;

    public ViewHolderRecordsCrops(@NonNull View itemView) {
        super(itemView);
        tvCropName = itemView.findViewById(R.id.tvCropName);
        tvCropDate = itemView.findViewById(R.id.tvCropDate);
        tvStatus = itemView.findViewById(R.id.tvStatus);
        tvNote = itemView.findViewById(R.id.tvNote);
    }
}
