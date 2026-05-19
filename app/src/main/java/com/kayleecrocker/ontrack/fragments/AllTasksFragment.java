package com.kayleecrocker.ontrack.fragments;

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

import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.adapters.TaskAdapter;
import com.kayleecrocker.ontrack.entities.Task;
import com.kayleecrocker.ontrack.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllTasksFragment extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private TaskViewModel taskViewModel;

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
        task.title = "pet my cat!";
        //taskViewModel.insert(task);
    }

    // handles drag + drop task reordering
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