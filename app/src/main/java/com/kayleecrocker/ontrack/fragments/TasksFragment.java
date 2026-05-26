package com.kayleecrocker.ontrack.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.google.android.material.tabs.TabLayoutMediator;
import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.adapters.TasksPagerAdapter;
import com.kayleecrocker.ontrack.entities.Task;
import com.kayleecrocker.ontrack.viewmodel.TaskViewModel;

import java.util.Calendar;
import java.util.Locale;

public class TasksFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Button createTaskButton;

    private TaskViewModel taskViewModel;

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    // =============================================================================================
    // Fragment lifecycle methods
    // =============================================================================================
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        TasksPagerAdapter adapter = new TasksPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // ViewModel
        taskViewModel = new ViewModelProvider(requireActivity())
                .get(TaskViewModel.class);

        // set up tablayout
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    if (position == 0) tab.setText("Tasks");
                    else tab.setText("Projects");
                }
        ).attach();

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
}