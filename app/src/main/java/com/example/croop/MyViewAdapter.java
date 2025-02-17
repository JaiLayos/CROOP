package com.example.croop;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.croop.GroupSellerLanding.Fragment_Home_Group_Seller;
import com.example.croop.GroupSellerLanding.Fragment_Profile_Group_Seller;

public class MyViewAdapter extends FragmentStateAdapter {
    private static final String TAG = "MyViewAdapter";

    public MyViewAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);  // You need to pass a FragmentActivity to the constructor
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.d(TAG, "Creating Fragment_Profile_Group_Seller");
                return new Fragment_Home_Group_Seller();
            case 1:
                return new Fragment_Profile_Group_Seller();
            default:
                return new Fragment();  // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 2;  // Number of fragments
    }
}


