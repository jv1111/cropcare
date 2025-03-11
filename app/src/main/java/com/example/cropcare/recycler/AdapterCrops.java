package com.example.cropcare.recycler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.CropModel;
import com.example.cropcare.R;
import com.example.cropcare.helper.TimeHelper;

import java.util.List;


public class AdapterCrops extends RecyclerView.Adapter<ViewHolderCrops>{

    List<CropModel> cropInfoList;
    private ICropListControlCB cb;

    public interface ICropListControlCB{
        void onSelect(int id, String cropName);
        void onDelete(int id);
    }

    public AdapterCrops(Context context, List<CropModel> cropInfoList, ICropListControlCB cb){
        this.cropInfoList = cropInfoList;
        this.cb = cb;
    }

    @NonNull
    @Override
    public ViewHolderCrops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCrops(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_crop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCrops holder, int position) {
        String date = "Date Created: " + TimeHelper.convertMillisToDateTime(cropInfoList.get(position).getDate());
        int id = cropInfoList.get(position).getId();
        String cropName = cropInfoList.get(position).getName();
        holder.tvCropName.setText(cropName);
        holder.tvCropDate.setText(date);

        holder.btnDelete.setOnClickListener(v ->{
            cb.onDelete(id);
        });
        holder.itemView.setOnClickListener(v -> {
            cb.onSelect(id, cropName);
        });
    }

    @Override
    public int getItemCount() {
        return cropInfoList.size();
    }


}
