package com.kayleecrocker.ontrack.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayleecrocker.ontrack.R;

public class TaskDetailsFragment extends Fragment {

    private static final String ARG_TASK_ID = "taskId";
    private int taskId;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(int taskId) {
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TASK_ID, taskId);
        fragment.setArguments(args);
        return fragment;
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
}