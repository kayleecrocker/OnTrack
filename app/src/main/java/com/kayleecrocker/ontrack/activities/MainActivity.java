package com.kayleecrocker.ontrack.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.adapters.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        MainPagerAdapter adapter = new MainPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Disable swipe if you want
        // viewPager.setUserInputEnabled(false);

        // Bottom nav -> ViewPager
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_sleep) {
                viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.nav_tasks) {
                viewPager.setCurrentItem(1);
            } else if (item.getItemId() == R.id.nav_hobby) {
                viewPager.setCurrentItem(2);
            }

            return true;
        });

        // ViewPager -> Bottom nav
        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {

                        switch (position) {
                            case 0:
                                bottomNavigationView.setSelectedItemId(R.id.nav_sleep);
                                break;

                            case 1:
                                bottomNavigationView.setSelectedItemId(R.id.nav_tasks);
                                break;

                            case 2:
                                bottomNavigationView.setSelectedItemId(R.id.nav_hobby);
                                break;
                        }
                    }
                }
        );

        viewPager.setCurrentItem(1);
    }
}