package com.example.croop;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class Sign_In_Success_Group_Seller extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_adapter);  // This will load the correct XML layout

        viewPager = findViewById(R.id.constraint);  // Finding the ViewPager2
        MyViewAdapter adapter = new MyViewAdapter(this);  // Updated to use the correct adapter
        viewPager.setAdapter(adapter);  // Setting the adapter
    }
}
