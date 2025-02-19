package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.croop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Fragment_Home_Group_Seller extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView name, bio;

    public Fragment_Home_Group_Seller() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_group_seller, container, false);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        name = view.findViewById(R.id.userNameDisplay);
        bio = view.findViewById(R.id.userBioDisplay);

        // Get the user role from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        FirebaseUser user = mAuth.getCurrentUser();
        String collection = getCollection(role);
        DocumentReference docRef = db.collection(collection).document(user.getUid());

        // Fetch user data from Firestore
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(getContext(), "Hello! " + role + " " + documentSnapshot.getString("Name"), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name_user = document.getString("Name");
                        name.setText(name_user);
                        String bio_user = document.getString("Bio");
                        bio.setText(bio_user);
                    }
                }
            }
        });

        Button order = view.findViewById(R.id.orderButton);
        order.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Orders_Group.class);
            startActivity(intent);
        });
        Button inventory = view.findViewById(R.id.inventoryButton);
        inventory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Inventory_Category.class);
            startActivity(intent);
        });

        return view;
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
