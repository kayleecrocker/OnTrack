package com.kayleecrocker.ontrack.fragments;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.entities.Task;
import com.kayleecrocker.ontrack.viewmodel.TaskViewModel;

public class EditTaskFragment extends Fragment {

    private static final String ARG_TASK_ID = "taskId";
    private int taskId;
    private Task task;
    private TaskViewModel taskViewModel;
    private EditText taskTitle;
    private ImageButton deleteButton;
    private ImageButton backButton;

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
        return inflater.inflate(R.layout.fragment_edit_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Views
        taskTitle = view.findViewById(R.id.edittext_task_title);
        deleteButton = view.findViewById(R.id.btn_delete);
        backButton = view.findViewById(R.id.btn_back);

        // Connect to viewmodel
        taskViewModel = new ViewModelProvider(requireActivity())
                .get(TaskViewModel.class);

        // Observe task data
        taskViewModel.getTask(taskId).observe(
                getViewLifecycleOwner(),
                task -> {
                    this.task = task;

                    // UI updates
                    if (task != null) {
                        taskTitle.setText(task.title);
                    }
                }
        );

        // OnClick listeners
        deleteButton.setOnClickListener(v -> {

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Delete", (dialog, which) -> {

                        if (task != null) {
                            taskViewModel.delete(task);

                            NavHostFragment.findNavController(this).popBackStack(R.id.nav_tasks, false);
                            Toast.makeText(requireContext(), "Task Deleted", LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Error Deleting Task", LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });

        backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).navigateUp()
        );
    }
}