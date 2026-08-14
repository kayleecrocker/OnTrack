package com.kayleecrocker.ontrack.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.viewmodel.TaskViewModel;

public class TaskDetailsFragment extends Fragment {

    private static final String ARG_TASK_ID = "taskId";
    private int taskId;
    private TaskViewModel taskViewModel;
    private TextView taskTitle;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            taskId = getArguments().getInt(ARG_TASK_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Connect to viewmodel
        taskViewModel = new ViewModelProvider(requireActivity())
                .get(TaskViewModel.class);

        // Views
        taskTitle = view.findViewById(R.id.textview_task_title);

        // Observe task data
        taskViewModel.getTask(taskId).observe(
                getViewLifecycleOwner(),
                task -> {
                    if (task != null) {
                        taskTitle.setText(task.title);
                    }
                }
        );
    }
}