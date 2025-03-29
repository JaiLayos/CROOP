package com.example.croop.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.IndividualSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Seller_Profile extends AppCompatActivity {
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private ImageView profilePicture;
    private TextView name, position, bio, email, number, address;
    private String seller, firebase_id, collection;
    private int seller_id;
    private FirebaseFirestore db;
    private Button contact;
    private FloatingActionButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_seller_profile);
        initializeComponents();
    }

    private void initializeComponents() {
        Intent intent = getIntent();
        seller = intent.getStringExtra("seller");
        firebase_id = intent.getStringExtra("firebase_id");
        seller_id = intent.getIntExtra("seller_id", 1);

        name = findViewById(R.id.userNameText);
        position = findViewById(R.id.userPositionText);
        bio = findViewById(R.id.userBioText);
        email = findViewById(R.id.userEmailText);
        number = findViewById(R.id.userPhoneNumberText);
        address = findViewById(R.id.userCityText);

        profilePicture = findViewById(R.id.profilePicture);
        db = FirebaseFirestore.getInstance();

        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        Toast.makeText(this, seller, Toast.LENGTH_SHORT).show();
        if(seller!=null){
            collection = getCollection(seller);
            switch(seller){
                case "Group Business User (Association)":
                case "Group Business User (Cooperative)":
                    Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(seller_id);
                    groupSellersCall.enqueue(new Callback<GroupSellers>() {
                        @Override
                        public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                            successFindGroupSeller(response);
                        }

                        @Override
                        public void onFailure(Call<GroupSellers> call, Throwable t) {
                            Log.e("Group Sellers Call Error:", t.toString());
                        }
                    });
                    break;
                case "Individual Business User":
                    Call<IndividualSellers> individualSellersCall = userAPI.getIndividualSellers(seller_id);
                    individualSellersCall.enqueue(new Callback<IndividualSellers>() {
                        @Override
                        public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                            sucessFindIndividualSeller(response);
                        }

                        @Override
                        public void onFailure(Call<IndividualSellers> call, Throwable t) {
                            Log.e("Individual Sellers Call Error:", t.toString());
                        }
                    });
                    break;
            }
        }else{
            Log.e("Intent", "Seller is null");
        }

        ;
        contact = findViewById(R.id.contactButton);
        contact.setOnClickListener(v -> {
            db.collection(collection).document(firebase_id).get().addOnSuccessListener(
                    new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String link = documentSnapshot.getString("Messenger Link");
                            openMessenger(link);
                        }
                    }
            );
        });

        back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void sucessFindIndividualSeller(Response<IndividualSellers> response) {
        IndividualSellers individualSellers = response.body();
        name.setText(individualSellers.getName());
        position.setText(individualSellers.returnRole());
        bio.setText(individualSellers.getBio());
        email.setText(individualSellers.getEmail());
        number.setText(individualSellers.getPhoneNum());
        Map<String, String> addressMap = individualSellers.getAddress();
        String formatAddress = formatAddress(addressMap);
        address.setText(formatAddress);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Profile Picture")
                .child(firebase_id)
                .child("Display");
        loadImage(storageRef);
    }

    private void successFindGroupSeller(Response<GroupSellers> response) {
        GroupSellers groupSellers = response.body();
        name.setText(groupSellers.getGroupName());
        position.setText(groupSellers.getPersonPosition());
        bio.setText(groupSellers.getBio());
        email.setText(groupSellers.getEmail());
        number.setText(groupSellers.getPhoneNum());
        Map<String, String> addressMap = groupSellers.getAddress();
        String formatAddress = formatAddress(addressMap);
        address.setText(formatAddress);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                .child("Profile Picture")
                .child(firebase_id)
                .child("Display");
        loadImage(storageRef);
    }

    private String formatAddress(Map<String, String> addressMap) {
        if (addressMap == null) return "N/A";

        String street = addressMap.get("House/Street Name");
        String subdivision = addressMap.get("Subdivision/Baranggay");
        String city = addressMap.get("City");
        String stateRegion = addressMap.get("State/Province/Region");
        String postalCode = addressMap.get("Postal Code");
        String country = addressMap.get("Country");

        if (stateRegion != null && stateRegion.startsWith("null, ")) {
            stateRegion = stateRegion.replace("null, ", "");
        }
        StringBuilder formattedAddress = new StringBuilder();
        if (street != null && !street.isEmpty()) {
            formattedAddress.append(street).append(", ");
        }
        if (subdivision != null && !subdivision.isEmpty()) {
            formattedAddress.append(subdivision).append(", ");
        }
        if (city != null && !city.isEmpty()) {
            formattedAddress.append(city);
        }
        if (stateRegion != null && !stateRegion.isEmpty()) {
            formattedAddress.append(", ").append(stateRegion);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            formattedAddress.append(" ").append(postalCode);
        }
        if (country != null && !country.isEmpty()) {
            formattedAddress.append(", ").append(country);
        }
        return formattedAddress.toString();
    }

    private void loadImage(StorageReference storageRef) {
        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri.toString())
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.sun)
                    .into(profilePicture);
        }).addOnFailureListener(e -> {
            Log.e("FirebaseImageError", "Failed to get download URL: " + e.getMessage());
            profilePicture.setImageResource(R.drawable.logo);
        });
    }

    private String getCollection(String role) {
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
        return collection;
    }

    private void openMessenger(String messenger) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(messenger));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, messenger, Toast.LENGTH_SHORT).show();
            Log.e("Messenger Error: ", e.getMessage());
        }
    }
}
