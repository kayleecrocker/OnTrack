package com.kayleecrocker.ontrack.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarMenu;
import com.kayleecrocker.ontrack.R;
import com.kayleecrocker.ontrack.views.CustomCurvedBottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private CustomCurvedBottomNavigationView bottomNavigationView;
    private FloatingActionButton navFab;
    private NavController navController;

    // =============================================================================================
    // Activity lifecycle methods
    // =============================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.custom_curved_bottom_navigation_view);
        navFab = findViewById(R.id.fab_nav_tasks);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host);

        NavController navController =
                navHostFragment.getNavController();

        /*
        bottomNavigationView.setOnItemSelectedListener(item -> {
            navController.navigate(item.getItemId());
            return true;
        });
         */

        NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
        );

        navFab.setOnClickListener(v -> {

            MenuItem tasksItem =
                    bottomNavigationView.getMenu().findItem(R.id.nav_tasks);

            NavigationUI.onNavDestinationSelected(
                    tasksItem,
                    navController
            );
        });


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if (destination.getId() == R.id.taskDetailsFragment) {
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });


        // Setup stuff

/*
        // Bottom nav -> ViewPager
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_sleep) {
                viewPager.setCurrentItem(0);
            }
            else if (item.getItemId() == R.id.nav_tasks) {
                viewPager.setCurrentItem(1);
            }
            else if (item.getItemId() == R.id.nav_hobbies) {
                viewPager.setCurrentItem(2);
            }

            return true;
        });


        // ViewPager -> Bottom nav
        viewPager.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {

                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        switch (position) {

                            case 0:
                                bottomNavigationView.getMenu()
                                        .findItem(R.id.nav_sleep)
                                        .setChecked(true);
                                break;

                            case 1:
                                bottomNavigationView.getMenu()
                                        .findItem(R.id.nav_tasks)
                                        .setChecked(true);
                                break;

                            case 2:
                                bottomNavigationView.getMenu()
                                        .findItem(R.id.nav_hobbies)
                                        .setChecked(true);
                                break;
                        }
                    }
                }
        );

        // Fab -> ViewPager
        navFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

 */
    }
}