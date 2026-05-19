package com.kayleecrocker.ontrack.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.adapters.TaskAdapter;
import com.kayleecrocker.ontrack.entities.Task;
import com.kayleecrocker.ontrack.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class AllTasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskViewModel taskViewModel;
    private Button createTaskButton;

    // =============================================================================================
    // Fragment lifecycle methods
    // =============================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_all_tasks,
                container,
                false
        );

        recyclerView = view.findViewById(R.id.recyclerview_all_tasks);

        // RecyclerView setup
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        adapter = new TaskAdapter();

        recyclerView.setAdapter(adapter);

        // ViewModel
        taskViewModel = new ViewModelProvider(requireParentFragment())
                .get(TaskViewModel.class);

        // Observe tasks
        taskViewModel.getAllTasks().observe(
                getViewLifecycleOwner(),
                tasks -> {
                    Log.d("mytag", "tasks size = " + tasks.size());
                    adapter.submitList(tasks);
                    Log.d("mytag", "onCreateView: fired");
                }
        );

        // enable drag + drop
        setItemTouchHelper();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Task task = new Task();
        task.title = "make lunch";
        //taskViewModel.insert(task);

        // Views
        createTaskButton = view.findViewById(R.id.button_new_task);

        // onClick listeners
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTaskDialog();
            }
        });
    }

    // =============================================================================================
    // Create task dialog
    // =============================================================================================
    private void showCreateTaskDialog() {

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_task, null);

        // Views
        EditText editTitle = dialogView.findViewById(R.id.edittext_title);
        EditText editNotes = dialogView.findViewById(R.id.edittext_notes);
        SeekBar seekImportance = dialogView.findViewById(R.id.seekbar_importance);
        EditText editDeadlineDate = dialogView.findViewById(R.id.edittext_deadline_date);
        EditText editDeadlineTime = dialogView.findViewById(R.id.edittext_deadline_time);
        LinearLayout moreOptionsLayout = dialogView.findViewById(R.id.layout_more_options);
        TextView textMoreOptions = dialogView.findViewById(R.id.text_more_options);

        // Expand/collapse
        textMoreOptions.setOnClickListener(v -> {

            if (moreOptionsLayout.getVisibility() == View.GONE) {
                moreOptionsLayout.setVisibility(View.VISIBLE);
            } else {
                moreOptionsLayout.setVisibility(View.GONE);
            }
        });

        // onClick listeners
        editDeadlineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupDatePicker(editDeadlineDate);
            }
        });

        editDeadlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupTimePicker(editDeadlineTime);
            }
        });

        // Build dialog
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Create Task")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = editTitle.getText().toString().trim();

                    if (title.isEmpty()) {
                        return;
                    }

                    Task task = new Task();
                    task.title = title;
                    task.notes = editNotes.getText().toString();
                    task.priority = seekImportance.getProgress();
                    taskViewModel.insert(task);
                })
                .show();
    }


    // =============================================================================================
    // Date and time pickers
    // =============================================================================================
    private void setupDatePicker(EditText dateText) {

        dateText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {

                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;

                        dateText.setText(date);

                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.show();
        });
    }

    private void setupTimePicker(EditText timeText) {
        timeText.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireContext(),
                    (view, hourOfDay, minute) -> {

                        String time = String.format(
                                Locale.getDefault(),
                                "%02d:%02d",
                                hourOfDay,
                                minute
                        );

                        timeText.setText(time);

                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
            );

            timePickerDialog.show();
        });
    }

    // =============================================================================================
    // ItemTouchHelper for drag and drop
    // =============================================================================================
    private void setItemTouchHelper() {

        ItemTouchHelper.Callback callback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        0
                ) {
                    @Override
                    public boolean onMove(
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            RecyclerView.ViewHolder target
                    ) {
                        int from = viewHolder.getBindingAdapterPosition();
                        int to = target.getBindingAdapterPosition();

                        List<Task> currentList = new ArrayList<>(adapter.getCurrentList());
                        Collections.swap(currentList, from, to);
                        adapter.submitList(currentList);
                        return true;
                    }
                    @Override
                    public boolean isLongPressDragEnabled() {
                        return false;
                    }
                    @Override
                    public boolean isItemViewSwipeEnabled() {
                        return false;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    }
                    // fancy extra animation stuff
                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);

                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {

                            View itemView = viewHolder.itemView;

                            itemView.animate()
                                    .scaleX(1.05f)
                                    .scaleY(1.05f)
                                    .translationZ(20f)
                                    .setDuration(150)
                                    .start();
                        }
                    }
                    @Override
                    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);

                        View itemView = viewHolder.itemView;

                        itemView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .translationZ(0f)
                                .setDuration(150)
                                .start();
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setItemTouchHelper(itemTouchHelper);
    }
}