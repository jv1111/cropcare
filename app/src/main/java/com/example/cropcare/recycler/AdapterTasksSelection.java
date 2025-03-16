package com.example.cropcare.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Auth;
import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.R;
import com.example.cropcare.helper.TimeHelper;

import java.util.List;

public class AdapterTasksSelection extends RecyclerView.Adapter<ViewHolderTasks> {

    Context context;
    List<TaskModel> tasksList;
    private ITasksSelection cb;

    public interface ITasksSelection{
        void onSelect(int taskId, String taskNote, int repeatEvery, long startTime, long endTime);
        void onDelete(int taskId);
    }

    public AdapterTasksSelection(Context context, List<TaskModel> tasksList, ITasksSelection cb) {
        this.context = context;
        this.tasksList = tasksList;
        this.cb = cb;
    }

    @NonNull
    @Override
    public ViewHolderTasks onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolderTasks(LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_task_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderTasks holder, int position) {
        TaskModel currentTask = tasksList.get(position);
        String note = currentTask.getNote();
        int occurance = currentTask.getRepeatEveryDays();
        String alarmDateTime = TimeHelper.convertMillisToDateTime(currentTask.getStartTime());

        preventScrollOnTouch(holder);

        GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                cb.onSelect(currentTask.getId(), currentTask.getNote(), currentTask.getRepeatEveryDays(), currentTask.getStartTime(), currentTask.getEndTime());
                return super.onSingleTapUp(e);
            }
        });

        if(!Auth.isAdmin){
            holder.btnDelete.setEnabled(false);
            holder.btnDelete.setTextColor(context.getResources().getColor(R.color.disabled));
        }

        holder.btnDelete.setOnClickListener(v ->{
            cb.onDelete(currentTask.getId());
        });

        holder.tvTaskDate.setText("Date: " + alarmDateTime);
        holder.tvTaskNote.setText("TaskNote: " + note);
        holder.tvEvery.setText("Every: " + (occurance > 0 ? occurance + " days" : "One-time task"));
        holder.itemView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void preventScrollOnTouch(ViewHolderTasks holder) {
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

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
