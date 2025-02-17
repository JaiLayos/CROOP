package com.example.croop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MyViewAdapter extends FragmentStateAdapter {

    public MyViewAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);  // You need to pass a FragmentActivity to the constructor
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Home_Group_Seller_Fragment();  // Replace with your HomeFragment class
            case 1:
                return new Profile_Group_Seller_Fragment();  // Replace with your ProfileFragment class
            default:
                return new Fragment();  // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 2;  // Number of fragments
    }
}


