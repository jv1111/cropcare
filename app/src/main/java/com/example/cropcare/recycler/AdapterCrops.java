package com.example.cropcare.recycler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.CropModel;
import com.example.cropcare.R;

import java.util.List;


public class AdapterCrops extends RecyclerView.Adapter<ViewHolderCrops>{

    Context context;
    List<CropModel> cropInfoList;

    public AdapterCrops(Context context, List<CropModel> cropInfoList){
        this.context = context;
        this.cropInfoList = cropInfoList;
    }

    @NonNull
    @Override
    public ViewHolderCrops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCrops(LayoutInflater.from(context).inflate(R.layout.rec_item_crop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCrops holder, int position) {
        String date = "Date: " + cropInfoList.get(position).getDate();
        holder.tvCropName.setText(cropInfoList.get(position).getName());
        holder.tvCropDate.setText(date);
    }

    @Override
    public int getItemCount() {
        return cropInfoList.size();
    }


}
