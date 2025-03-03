package com.example.cropcare.recycler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cropcare.Model.TaskModel;
import com.example.cropcare.R;
import com.example.cropcare.helper.TimeHelper;

import java.util.List;

public class AdapterTasksSelection extends RecyclerView.Adapter<ViewHolderTasks> {

    Context context;
    List<TaskModel> tasksList;
    private ITasksSelection cb;

    public interface ITasksSelection{
        void onSelect(int taskId, String taskNote, String repeatEvery, String date);
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
        return new ViewHolderTasks(LayoutInflater.from(context).inflate(R.layout.rec_task_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolderTasks holder, int position) {
        String note = tasksList.get(position).getNote();
        int occurance = tasksList.get(position).getRepeatEveryDays();
        String alarmDateTime = TimeHelper.convertMillisToDateTime(tasksList.get(position).getStartTime());

        Log.i("myTag adapter tasks note: ", note);

        holder.tvTaskDate.setText("Date: " + alarmDateTime);
        holder.tvTaskNote.setText("TaskNote: " + note);
        holder.tvEvery.setText("Every: " + (occurance > 0 ? occurance + " days" : "One-time task"));
    }

    @Override
    public int getItemCount() {
        return tasksList.size();
    }
}
