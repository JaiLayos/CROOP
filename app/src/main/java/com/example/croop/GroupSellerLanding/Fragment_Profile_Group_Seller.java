package com.example.croop.GroupSellerLanding;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.croop.Landing_Activity;
import com.example.croop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

public class Fragment_Profile_Group_Seller extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private TextView userName, userRole, userBio, userEmail, userPhone, userAddress, userGroup;
    private StorageReference storageRef;

    public Fragment_Profile_Group_Seller(){

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.profile_group_seller, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", getContext().MODE_PRIVATE);
        String collection = prefs.getString("user_collection", null);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize components
        userName = rootView.findViewById(R.id.userNameText);
        userRole = rootView.findViewById(R.id.userPositionText);
        userBio = rootView.findViewById(R.id.userBioText);
        userEmail = rootView.findViewById(R.id.userEmailText);
        userPhone = rootView.findViewById(R.id.userPhoneNumberText);
        userAddress = rootView.findViewById(R.id.userCityText);
        userGroup = rootView.findViewById(R.id.userGroupText);

        ImageView displayPicture = rootView.findViewById(R.id.profilePicture);

        initializeComponents(collection);

        Button edit = rootView.findViewById(R.id.profileEditButton);
        edit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Activity_Edit_Profile.class);
            startActivity(intent);
        });
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        storageRef = FirebaseStorage.getInstance().getReference()
                .child("Profile Picture")
                .child(userId)
                .child("Display");

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(displayPicture);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            displayPicture.setImageResource(R.drawable.logo);
        });

        Button logOut;
        logOut = rootView.findViewById(R.id.logOutButton);

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
                            getActivity().finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        TextView da, cda;

        da = rootView.findViewById(R.id.daText);
        da.setOnClickListener(v -> {
            String uid = "521426187938826"; // Replace with the actual user ID
            openMessenger(uid);
        });

        cda = rootView.findViewById(R.id.cdaText);
        cda.setOnClickListener(v -> {
            String uid = "406419702548229"; // Replace with the actual user ID
            openMessenger(uid);
        });


        return rootView;
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

    private void initializeComponents(String collection) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DocumentReference docRef = db.collection(collection).document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> address_map = (Map<String, Object>) document.get("Address");

                            // Access individual fields
                            String city = (String) address_map.get("City");
                            String country = (String) address_map.get("Country");
                            String streetName = (String) address_map.get("House_Street_Name");
                            String postalCode = (String) address_map.get("Postal_Code");
                            String state = (String) address_map.get("State_Province_Region");
                            String subdivision = (String) address_map.get("Subdivision_Baranggay");

                            String name_user = document.getString("Name");
                            userName.setText(name_user);
                            String role_user = document.getString("Position");
                            userRole.setText(role_user);
                            String bio_user = document.getString("Bio");
                            userBio.setText(bio_user);
                            String group_user = document.getString("Group Name");
                            userGroup.setText(group_user);
                            String email_user = document.getString("Email");
                            userEmail.setText(email_user);
                            String phone_user = document.getString("Phone Number");
                            userPhone.setText(phone_user);
                            userAddress.setText(streetName + ", " + subdivision + ", " + city + ", " + state + ", " + postalCode + ", " + country);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }
}
