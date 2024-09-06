package com.example.cookmaster;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.cookmaster.databinding.ActivityMainBinding;
import com.example.cookmaster.subscreen.FeedbackFragment;
import com.example.cookmaster.subscreen.HomeFragment;
import com.example.cookmaster.subscreen.ProfileFragment;
import com.example.cookmaster.subscreen.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(),new HomeFragment())
                    .commit();
        }
        onBottomNavigationItemSelected();
    }

    private void onBottomNavigationItemSelected() {
        binding.bottomNavigation.setOnItemSelectedListener(menuItem -> {
            Fragment fragmentSelected = null;
            if(menuItem.getItemId() == R.id.home_menu_item){
                fragmentSelected = new HomeFragment();
            }
            else if(menuItem.getItemId() == R.id.search_menu_item){
                fragmentSelected = new SearchFragment();
            }
            else if(menuItem.getItemId() == R.id.feedback_menu_item){
                fragmentSelected = new FeedbackFragment();
            }
            else{
                fragmentSelected = new ProfileFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.fragmentContainer.getId(),fragmentSelected)
                    .commit();

            return true;
        });
    }

    public static void finishActivity() {

    }
}