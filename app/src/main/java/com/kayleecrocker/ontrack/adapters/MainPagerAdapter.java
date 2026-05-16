package com.kayleecrocker.ontrack.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.kayleecrocker.ontrack.fragments.HobbyFragment;
import com.kayleecrocker.ontrack.fragments.SleepFragment;
import com.kayleecrocker.ontrack.fragments.TasksFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {

            case 0:
                return new SleepFragment();

            case 1:
                return new TasksFragment();

            case 2:
                return new HobbyFragment();

            default:
                return new TasksFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
