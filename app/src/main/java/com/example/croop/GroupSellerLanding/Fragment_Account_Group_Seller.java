package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.croop.Landing_Activity;
import com.example.croop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fragment_Account_Group_Seller extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public Fragment_Account_Group_Seller() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.account_email_edit_group_seller, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        String collection = getCollection(role);

        Button logOut;
        logOut = view.findViewById(R.id.logOutButton);


        logOut.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to log out?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        mAuth.signOut();
                        if (getActivity() != null) {
                            Intent intent = new Intent(getActivity(), Landing_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        TextView da, cda;

        da = view.findViewById(R.id.daText);
        da.setOnClickListener(v -> {
            String userId = "521426187938826"; // Replace with the actual user ID
            openMessenger(userId);
        });

        cda = view.findViewById(R.id.cdaText);
        cda.setOnClickListener(v -> {
            String userId = "406419702548229"; // Replace with the actual user ID
            openMessenger(userId);
        });

        return view;
    }


    private void openMessenger(String userId) {
        try {
            // Try to open Messenger app using its URI scheme
            String messengerUri = "fb-messenger://user-thread/" + userId;
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(messengerUri));
            startActivity(intent);
        } catch (Exception e) {
            // Fallback to web URL if Messenger app is not installed
            String fallbackUrl = "https://www.facebook.com/messages/t/" + userId;
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(fallbackUrl));
            startActivity(intent);
        }
    }
    private String getCollection(String role) {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String collection;

        switch (role) {
            case "Group Business User (Association)":
                collection = "Farming Association";
                break;
            case "Group Business User (Cooperative)":
                collection = "Farming Cooperatives";
                break;
            case "Individual Business User":
                collection = "Individual Sellers";
                break;
            case "Individual Customer User":
                collection = "Customers";
                break;
            case "Group Customer User":
                collection = "Group Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }
}
