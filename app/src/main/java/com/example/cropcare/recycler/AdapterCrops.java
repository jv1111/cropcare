package com.example.cropcare.recycler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.CropModel;
import com.example.cropcare.R;
import com.example.cropcare.helper.TimeHelper;

import java.util.List;


public class AdapterCrops extends RecyclerView.Adapter<ViewHolderCrops>{

    List<CropModel> cropInfoList;
    private ICropListControlCB cb;
    private Context context;

    public interface ICropListControlCB{
        void onUpdate(int id, String cropName);
        void onSelect(int id, String cropName);
        void onDelete(int id);
    }

    public AdapterCrops(Context context, List<CropModel> cropInfoList, ICropListControlCB cb){
        this.cropInfoList = cropInfoList;
        this.cb = cb;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderCrops onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderCrops(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_item_crop, parent, false));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderCrops holder, int position) {
        String date = "Date Created: " + TimeHelper.convertMillisToDateTime(cropInfoList.get(position).getDate());
        int id = cropInfoList.get(position).getId();
        String cropName = cropInfoList.get(position).getName();
        holder.tvCropName.setText(cropName);
        holder.tvCropDate.setText(date);

        preventScrollOnTouch(holder);

        GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                cb.onSelect(id, cropName);
                return super.onSingleTapUp(e);
            }
        });

        holder.btnUpdate.setOnClickListener(v->{
            cb.onUpdate(id, cropName);
        });
        holder.btnDelete.setOnClickListener(v ->{
            cb.onDelete(id);
        });
        holder.itemView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    @Override
    public int getItemCount() {
        return cropInfoList.size();
    }

    private void preventScrollOnTouch(ViewHolderCrops holder) {
        holder.itemMainLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {
                holder.itemView.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {}

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {}
        });
    }

}
