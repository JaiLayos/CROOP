package com.example.croop.GroupSellerLanding;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.croop.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In_Success_Group_Seller extends AppCompatActivity {
    private ViewPager2 viewPager;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_seller_home);  // This will load the correct XML layout

        viewPager = findViewById(R.id.viewPagerContainer);  // Finding the ViewPager2
        TabLayout tabLayout = findViewById(R.id.tabs);  // Reference to TabLayout
        GroupSellerViewAdapter adapter = new GroupSellerViewAdapter(this);  // Updated to use the correct adapter
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
                case 2:
                    tab.setText("Account");
                    break;
            }
        }).attach();
    }
}
