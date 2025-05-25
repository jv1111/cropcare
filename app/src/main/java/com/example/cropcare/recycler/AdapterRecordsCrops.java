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

public class AdapterRecordsCrops extends RecyclerView.Adapter<ViewHolderCrops> {

    List<RecordModel> recordList;
    private ICropListControlCB cb;
    private Context context;

    public interface ICropListControlCB {
        void onSelect(int id, int cropId);
    }

    public AdapterRecordsCrops(Context context, List<RecordModel> recordList, ICropListControlCB cb) {
        this.context = context;
        this.recordList = recordList;
        this.cb = cb;
    }

    @NonNull
    @Override
    public ViewHolderCrops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCrops(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_crop_rec, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCrops holder, int position) {
        RecordModel record = recordList.get(position);
        String date = "Date: " + TimeHelper.convertMillisToDateTime(record.getTime());

        holder.tvCropName.setText("Crop ID: " + record.getCropId());
        holder.tvCropDate.setText(date);

        GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                cb.onSelect(record.getId(), record.getCropId());
                return true;
            }
        });

        holder.itemView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
