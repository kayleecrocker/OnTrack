package com.kayleecrocker.ontrack.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.tabs.TabLayout;
import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.adapters.TaskAdapter;
import com.kayleecrocker.ontrack.algorithms.TaskPriorityCalculator;
import com.kayleecrocker.ontrack.entities.Task;
import com.kayleecrocker.ontrack.utils.DateTimeUtils;
import com.kayleecrocker.ontrack.viewmodel.TaskViewModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TasksFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private TaskAdapter adapter;
    private Button createTaskButton;
    private RecyclerView recyclerView;

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    // =============================================================================================
    // Fragment lifecycle methods
    // =============================================================================================
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Views
        createTaskButton = view.findViewById(R.id.button_new_task);
        recyclerView = view.findViewById(R.id.recyclerview_all_tasks);

        // ViewModel
        taskViewModel = new ViewModelProvider(requireActivity())
                .get(TaskViewModel.class);

        Task lunch = new Task();
        lunch.title = "make lunch";
        //taskViewModel.insert(task);

        // RecyclerView setup
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        adapter = new TaskAdapter(task -> {

            Bundle bundle = new Bundle();
            bundle.putInt("taskId", task.id);

            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_tasks_to_details, bundle);
        });

        recyclerView.setAdapter(adapter);

        // Observe tasks
        taskViewModel.getAllTasks().observe(
                getViewLifecycleOwner(),
                tasks -> {
                    for (Task task : tasks) {
                        task.priority = TaskPriorityCalculator.calculatePriority(task);
                        Log.d("mytag", "prioity " + task.title + ": " + task.priority);
                        Log.d("mytag", "importance " + task.title + ": " + task.importance);
                    }
                    Collections.sort(tasks);
                    adapter.submitList(tasks);
                }
        );

        // onClick listeners
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateTaskDialog();
            }
        });

        // enable drag + drop
        setItemTouchHelper();
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
                DateTimeUtils.setupDatePicker(requireContext(), editDeadlineDate);
            }
        });

        editDeadlineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeUtils.setupTimePicker(requireContext(), editDeadlineTime);
            }
        });

        // Build dialog
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Create Task")
                .setView(dialogView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Add", (dialog, which) -> {
                    String title = editTitle.getText().toString().trim();

                    Task task = new Task();

                    if (title.isEmpty()) {
                        return;
                    }

                    // get date and time deadline------------------------
                    DateTimeFormatter dateFormatter =
                            DateTimeFormatter.ofPattern("d/M/yyyy");

                    DateTimeFormatter timeFormatter =
                            DateTimeFormatter.ofPattern("HH:mm");

                    if (!editDeadlineDate.getText().toString().isEmpty() && !editDeadlineTime.getText().toString().isEmpty()) {

                        LocalDate date = LocalDate.parse(
                                editDeadlineDate.getText().toString(),
                                dateFormatter
                        );

                        LocalTime time = LocalTime.parse(
                                editDeadlineTime.getText().toString(),
                                timeFormatter
                        );

                        LocalDateTime deadline = LocalDateTime.of(date, time);

                        long deadlineMillis = deadline
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli();

                        task.deadline = deadlineMillis;
                    } else {
                        task.deadline = null;
                    }
                    //----------------------------------------------------


                    task.title = title;
                    task.notes = editNotes.getText().toString();
                    task.importance = seekImportance.getProgress();
                    task.creationDate = System.currentTimeMillis();
                    task.lastUpdated = System.currentTimeMillis();
                    taskViewModel.insert(task);
                })
                .show();
    }

    // =============================================================================================
    // ItemTouchHelper for drag and drop (make utils?)
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