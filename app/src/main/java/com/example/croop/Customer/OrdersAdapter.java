package com.example.croop.Customer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersAdapter extends FragmentStateAdapter {
    private static final String TAG = "MyViewAdapter";
    public OrdersAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);  // You need to pass a FragmentActivity to the constructor
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_Orders_Pending();
            case 1:
                return new Fragment_Orders_InTransit();
            case 2:
                return new Fragment_Orders_Completed();
            default:
                return new Fragment();  // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
