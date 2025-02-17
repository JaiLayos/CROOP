package com.example.croop;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Profile_Individual_Seller_Activity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    TextView userName, userRole, userBio, userEmail, userPhone, userAddress, userMessenger;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_individual_seller);
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String collection = prefs.getString("user_collection", null);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        initializeComponents(collection);
    }

    private void initializeComponents(String collection) {
        userName = findViewById(R.id.userNameText);
        userRole = findViewById(R.id.userPositionText);
        userBio = findViewById(R.id.userBioText);
        userEmail = findViewById(R.id.userEmailText);
        userPhone = findViewById(R.id.userPhoneNumberText);
        userAddress = findViewById(R.id.userCityText);
        userMessenger = findViewById(R.id.userMessengerText);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, Home_Individual_Seller_Activity.class);
            startActivity(intent);
        });

        FirebaseUser user = mAuth.getCurrentUser();
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
                        String streetName = (String) address_map.get("House/Street Name");
                        String postalCode = (String) address_map.get("Postal Code");
                        String state = (String) address_map.get("State/Province/Region");
                        String subdivision = (String) address_map.get("Subdivision/Baranggay");

                        String name_user = document.getString("Name");
                        userName.setText(name_user);
                        String role_user = document.getString("Role");
                        userRole.setText(role_user);
                        String bio_user = document.getString("Bio");
                        userBio.setText(bio_user);
                        String email_user = document.getString("Email");
                        userEmail.setText(email_user);
                        String phone_user = document.getString("Phone Number");
                        userPhone.setText(phone_user);
                        userAddress.setText(streetName + ", " + subdivision + ", " + city + ", " + state + ", " + postalCode + ", " + country);
                        String messenger_user = document.getString("Messenger Link");
                        userMessenger.setText(messenger_user);
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
