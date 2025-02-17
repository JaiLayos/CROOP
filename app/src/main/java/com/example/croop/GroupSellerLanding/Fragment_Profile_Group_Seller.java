package com.example.croop.GroupSellerLanding;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.croop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Fragment_Profile_Group_Seller extends Fragment {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView userName, userRole, userBio, userEmail, userPhone, userAddress, userGroup;

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

        initializeComponents(collection);

        return rootView;
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
