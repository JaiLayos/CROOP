package com.example.croop.GroupSellerLanding;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.croop.MyViewAdapter;
import com.example.croop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Sign_In_Success_Group_Seller extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_group_home);  // This will load the correct XML layout

        viewPager = findViewById(R.id.viewPagerContainer);  // Finding the ViewPager2
        TabLayout tabLayout = findViewById(R.id.tabs);  // Reference to TabLayout
        MyViewAdapter adapter = new MyViewAdapter(this);  // Updated to use the correct adapter
        viewPager.setAdapter(adapter); // TabLayout from XML
        viewPager.setOffscreenPageLimit(2); // Retain both fragments
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Home");
                    break;
                case 1:
                    tab.setText("Profile");
                    break;
            }
        }).attach();
    }
}
