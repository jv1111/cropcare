package com.example.cropcare.recycler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.RVModelCropInfoModel;
import com.example.cropcare.R;

import java.util.List;


public class AdapterCrops extends RecyclerView.Adapter<ViewHolderCrops>{

    Context context;
    List<RVModelCropInfoModel> cropInfoList;

    public AdapterCrops(Context context, List<RVModelCropInfoModel> cropInfoList){
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
        holder.tvCropName.setText(cropInfoList.get(position).getCropName());

    }

    @Override
    public int getItemCount() {
        return cropInfoList.size();
    }


}
