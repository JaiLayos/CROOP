package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Setup_Holding_Cost extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private UserAPI userAPI;
    private RetrofitService RetrofitClient;
    private int id;
    private EditText laborText, processText, packagingText,
    refText, storageText, insuranceText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_setup_maintenance_inventory);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userAPI = RetrofitClient.getClient().create(UserAPI.class);
        laborText = findViewById(R.id.laborText);
        processText = findViewById(R.id.processText);
        packagingText = findViewById(R.id.packagingText);
        refText = findViewById(R.id.refText);
        storageText = findViewById(R.id.storageText);
        insuranceText = findViewById(R.id.insuranceText);

        initializeComponents();
    }

    private void initializeComponents() {
        String firebaseID = user.getUid();
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {

            if (!validateInputs()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            getIDofSeller(firebaseID);

        });
    }

    private void getIDofSeller(String firebaseID) {
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellersbyFirebaseID(firebaseID);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                GroupSellers groupSellers = response.body();
                int id = groupSellers.getId();
                getSeller(id, firebaseID);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Getting Group Seller ID Error: ", t.getMessage());
            }
        });
    }

    private void getSeller(int id, String firebaseID) {
        int labor = Integer.parseInt(laborText.getText().toString());
        int process = Integer.parseInt(processText.getText().toString());
        int packaging = Integer.parseInt(packagingText.getText().toString());
        int ref = Integer.parseInt(refText.getText().toString());
        int storage = Integer.parseInt(storageText.getText().toString());
        int insurance = Integer.parseInt(insuranceText.getText().toString());
        Call<GroupSellers> groupSellersCall = userAPI.getGroupSellers(id);

        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                GroupSellers groupSellers = response.body();
                groupSellers.setProduct_inventory_SC(labor+process+packaging);
                groupSellers.setProduct_inventory_MC(ref+storage+insurance);
                setInventoryThreshold(firebaseID, groupSellers);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Getting Group Seller Error: ", t.getMessage());
            }
        });

    }

    private void setInventoryThreshold(String id, GroupSellers groupSellers) {
        Call<GroupSellers> groupSellersCall = userAPI.updateGroupSellersByFirebaseID(id, groupSellers);
        groupSellersCall.enqueue(new Callback<GroupSellers>() {
            @Override
            public void onResponse(Call<GroupSellers> call, Response<GroupSellers> response) {
                Toast.makeText(Activity_Add_Setup_Holding_Cost.this, "Matagumpay ang pagbigay ng impormasyon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Setup_Holding_Cost.this, Activity_Products_Inventory.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<GroupSellers> call, Throwable t) {
                Log.e("Updating Group Seller Error: ", t.getMessage());
            }
        });
    }

    private boolean validateInputs() {
        EditText[] fields = {laborText, processText, packagingText, refText, storageText, insuranceText};

        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
