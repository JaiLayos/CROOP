package com.example.croop.IndividualSellerLanding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.GroupSellerLanding.Sign_In_Success_Group_Seller;
import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Edit_Profile_Individual extends AppCompatActivity {
    EditText userName, userBio, userPosition,
            userStreet, userSubdivision, userCity,
            userRegion, userPostal;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    private RetrofitService RetrofitClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group_seller);

        initializeComponents();

    }

    private void initializeComponents() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String collection = prefs.getString("user_collection", null);
        FloatingActionButton back = findViewById(R.id.backButton);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
            startActivity(intent);
        });

        userName = findViewById(R.id.userNameText);
        userBio = findViewById(R.id.userBioText);
        userPosition = findViewById(R.id.userPositionText);
        userStreet = findViewById(R.id.userStreetText);
        userSubdivision = findViewById(R.id.userSubdivisionText);
        userCity = findViewById(R.id.userCityText);
        userRegion = findViewById(R.id.userRegionText);
        userPostal = findViewById(R.id.userPostalText);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(collection).document(user.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String nameDB = documentSnapshot.getString("Name");
                    String positionDB = documentSnapshot.getString("Position");
                    String bioDB = documentSnapshot.getString("Bio");
                    String cityDB = documentSnapshot.getString("Address.City");
                    String houseDB = documentSnapshot.getString("Address.House_Street_Name");
                    String postDB = documentSnapshot.getString("Address.Postal_Code");
                    String regionDB = documentSnapshot.getString("Address.State_Province_Region");
                    String subdivisionDB = documentSnapshot.getString("Address.Subdivision_Baranggay");

                    userName.setText(nameDB);
                    userBio.setText(bioDB);
                    userPosition.setText(positionDB);
                    userStreet.setText(houseDB);
                    userSubdivision.setText(subdivisionDB);
                    userCity.setText(cityDB);
                    userRegion.setText(regionDB);
                    userPostal.setText(postDB);

                } else {
                    Log.d("FirestoreData", "No such document");
                }
            }
        );

        Button edit = findViewById(R.id.profileEditButton);
        edit.setOnClickListener(v -> {
            String name = userName.getText().toString().trim();
            String bio = userBio.getText().toString().trim();
            String position = userPosition.getText().toString().trim();
            String street = userStreet.getText().toString().trim();
            String subdivision = userSubdivision.getText().toString().trim();
            String city = userCity.getText().toString().trim();
            String region = userRegion.getText().toString().trim();
            String postal = userPostal.getText().toString().trim();
            GroupSellers groupSellers = new GroupSellers();
            groupSellers.setName(name);
            groupSellers.setBio(bio);
            groupSellers.setPersonPosition(position);
            Map<String, String> addressMap = new HashMap<>();
            addressMap.put("City", city);
            addressMap.put("Country", "Philippines");
            addressMap.put("House_Street_Name", street);
            addressMap.put("Postal_Code", postal);
            addressMap.put("State_Province_Region", region);
            addressMap.put("Subdivision_Barangay", subdivision);
            groupSellers.setAddress(addressMap);
            docRef.update(
                    "Address.City", city,
                    "Address.House_Street_Name", street,
                    "Address.Postal_Code", postal,
                    "Address.State_Province_Region", region,
                    "Address.Subdivision_Baranggay", subdivision,
                    "Bio", bio,
                    "Name", name,
                    "Position", position,
                    "Updated At", new Date()
            ).addOnSuccessListener( view -> {
                    Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
                    startActivity(intent);
                    sendToPG(groupSellers);
                }
            ).addOnFailureListener( e-> {
                Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            );
        });


    }

    private void sendToPG(GroupSellers groupSellers) {
        UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
        Call<GroupSellers> call = userAPI.updateGroupSellersByFirebaseID(user.getUid(), groupSellers);
        call.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Activity_Edit_Profile_Individual.this, "Profile updated in PostgreSQL!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Activity_Edit_Profile_Individual.this, "Failed to update profile: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Toast.makeText(Activity_Edit_Profile_Individual.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
