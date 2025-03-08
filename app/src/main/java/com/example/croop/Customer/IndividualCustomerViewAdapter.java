package com.example.croop.Customer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class IndividualCustomerViewAdapter extends FragmentStateAdapter {
    private static final String TAG = "MyViewAdapter";
    public IndividualCustomerViewAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);  // You need to pass a FragmentActivity to the constructor
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment_Home_Customer();
            case 1:
                return new Fragment_Products_Customer();
            case 2:
                return new Fragment_Seller_Customer();
            case 3:
                return new Fragment_Cart_Customer();
            case 4:
                return new Fragment_Orders_Customer();
            case 5:
                return new Fragment_Account_Customer();
            default:
                return new Fragment();  // Default case
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
