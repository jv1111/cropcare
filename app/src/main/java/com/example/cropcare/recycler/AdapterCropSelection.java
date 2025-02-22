package com.example.cropcare.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.CropModel;
import com.example.cropcare.R;

import java.util.List;

public class AdapterCropSelection extends RecyclerView.Adapter<ViewHolderCrops> {

    Context context;
    List<CropModel> cropInfoList;
    private IOnSelection selectionCallback;

    public interface IOnSelection{
        void onSelect(String date, String cropName, int id);
    }

    public AdapterCropSelection(Context context, List<CropModel> cropInfoList, IOnSelection selectionCallback) {
        this.context = context;
        this.cropInfoList = cropInfoList;
        this.selectionCallback = selectionCallback;
    }

    @NonNull
    @Override
    public ViewHolderCrops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCrops(LayoutInflater.from(context).inflate(R.layout.rec_item_selection_crop, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCrops holder, int position) {
        String date = "Date: " + cropInfoList.get(position).getDate();
        String cropName = cropInfoList.get(position).getName();
        int id = cropInfoList.get(position).getId();

        holder.tvCropName.setText(cropInfoList.get(position).getName());
        holder.tvCropDate.setText(date);

        holder.itemView.setOnClickListener(v ->
                selectionCallback.onSelect(date, cropName, id)
        );
    }

    @Override
    public int getItemCount() {
        return cropInfoList.size();
    }
}
