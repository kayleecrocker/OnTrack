package com.kayleecrocker.ontrack.adapters;

import static android.view.View.GONE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.entities.Task;

public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private ItemTouchHelper itemTouchHelper;

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Task>() {

                @Override
                public boolean areItemsTheSame(Task oldItem, Task newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(Task oldItem, Task newItem) {
                    return oldItem.title.equals(newItem.title)
                            && oldItem.progress == newItem.progress
                            && oldItem.isCompleted == newItem.isCompleted
                            && oldItem.priority == newItem.priority;
                }
            };

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }

    private final OnTaskClickListener listener;

    // constructor that guarantees listener is initialized
    public TaskAdapter(OnTaskClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);

        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull TaskViewHolder holder,
            int position
    ) {

        Task task = getItem(position);

        holder.textTitle.setText(task.title);
        holder.progressBar.setProgress(7*position);
        if (holder.progressBar.getProgress() == 0) {
            holder.progressBar.setVisibility(GONE);
        }

        // set up drag handle
        holder.dragHandle.setOnLongClickListener(v -> {
            itemTouchHelper.startDrag(holder);
            return true;
        });

        // click to open task details
        holder.itemView.setOnClickListener(v -> {
            listener.onTaskClick(task);
        });

        //holder.textProgress.setText(task.progress + "%");

        //holder.checkBox.setChecked(task.isCompleted);
    }

    public void setItemTouchHelper(ItemTouchHelper itemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        ProgressBar progressBar;
        CheckBox checkBox;
        ImageView dragHandle;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textview_task_title);
            progressBar = itemView.findViewById(R.id.progressbar_task_progress);
            dragHandle = itemView.findViewById(R.id.imageview_drag_handle);
            //checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
