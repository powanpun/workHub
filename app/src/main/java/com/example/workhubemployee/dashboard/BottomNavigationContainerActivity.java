package com.example.workhubemployee.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.bottomFragments.ChatFragment;
import com.example.workhubemployee.dashboard.bottomFragments.HomeFragment;
import com.example.workhubemployee.dashboard.bottomFragments.PostsFragment;
import com.example.workhubemployee.dashboard.bottomFragments.ShiftFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BottomNavigationContainerActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav_container);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        HomeFragment homeFragment = new HomeFragment();
        ShiftFragment shiftFragment = new ShiftFragment();
        ChatFragment chatFragment = new ChatFragment();
        PostsFragment postsFragment = new PostsFragment();


        setCurrentFragment(homeFragment);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.home) {
                    setCurrentFragment(homeFragment);
                } else if (id == R.id.post) {

                    setCurrentFragment(postsFragment);
                } else if (id == R.id.chat) {
                    setCurrentFragment(chatFragment);
                }else if (id == R.id.shift) {
                    setCurrentFragment(shiftFragment);
                }
                return true;
            }
        });

    }

    private void setCurrentFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,fragment).commit();
    }


}