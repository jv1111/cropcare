package com.example.cropcare.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.RecordModel;
import com.example.cropcare.R;
import com.example.cropcare.helper.TimeHelper;

import java.util.List;

public class AdapterRecordsCrops extends RecyclerView.Adapter<ViewHolderRecordsCrops> {

    List<RecordModel> recordList;

    public AdapterRecordsCrops(List<RecordModel> recordList) {
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public ViewHolderRecordsCrops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderRecordsCrops(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_crop_rec, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderRecordsCrops holder, int position) {
        RecordModel record = recordList.get(position);
        String date = "Date: " + TimeHelper.convertMillisToDateTime(record.getTime());

        holder.tvCropName.setText("Crop name: " + record.getCropName());
        holder.tvCropDate.setText(date);
        holder.tvStatus.setText("Status: " + record.getStatus());
        holder.tvNote.setText("Note: " + record.getNote());

    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
