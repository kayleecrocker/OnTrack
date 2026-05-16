package com.kayleecrocker.ontrack.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kayleecrocker.ontrack.fragments.AllTasksFragment;
import com.kayleecrocker.ontrack.fragments.ProjectFragment;

public class TasksPagerAdapter extends FragmentStateAdapter {

    public TasksPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AllTasksFragment();
            case 1:
                return new ProjectFragment();
            default:
                return new AllTasksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
