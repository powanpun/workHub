package com.example.workhubemployee.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.workhubemployee.R;
import com.example.workhubemployee.dashboard.bottomFragments.ChatFragment;
import com.example.workhubemployee.dashboard.bottomFragments.HomeFragment;
import com.example.workhubemployee.dashboard.bottomFragments.PostsFragment;
import com.example.workhubemployee.dashboard.bottomFragments.ShiftFragment;
import com.example.workhubemployee.employee.employeeFragments.EmployeeChatFragment;
import com.example.workhubemployee.employee.employeeFragments.EmployeeHomeFragment;
import com.example.workhubemployee.employee.employeeFragments.EmployeePostsFragment;
import com.example.workhubemployee.employee.employeeFragments.EmployeeShiftFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeeBottomNavigationActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_bottom_navigation);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        EmployeeHomeFragment homeFragment = new EmployeeHomeFragment();
        EmployeeShiftFragment shiftFragment = new EmployeeShiftFragment();
        EmployeeChatFragment chatFragment = new EmployeeChatFragment();
        EmployeePostsFragment postsFragment = new EmployeePostsFragment();


        setCurrentFragment(postsFragment);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.home) {
                    setCurrentFragment(postsFragment);
                } else if (id == R.id.post) {
                    setCurrentFragment(homeFragment);
                } else if (id == R.id.chat) {
                    setCurrentFragment(chatFragment);
                }else if (id == R.id.shift) {
                    setCurrentFragment(shiftFragment);
                }
                return true;
            }
        });

    }

    public String getName(){
        Intent myIntent = getIntent(); // gets the previously created intent
        String name = myIntent.getStringExtra("name");
        return name;
    }
    public String getUserId(){
        Intent myIntent = getIntent(); // gets the previously created intent
        String userId = myIntent.getStringExtra("userId");
        return userId;
    }

    private void setCurrentFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment,fragment).commit();
    }
}