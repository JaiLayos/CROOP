package com.example.croop.IndividualSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.IndividualSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Setup_Holding_Cost_Indiv extends AppCompatActivity {
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
                Toast.makeText(this, "Mangyaring sagutan ang bawat kahon!", Toast.LENGTH_SHORT).show();
                return;
            }

            getIDofSeller(firebaseID);

        });
    }

    private void getIDofSeller(String firebaseID) {
        Call<IndividualSellers> individualSellersCall = userAPI.getIndividualSellersbyFirebaseID(firebaseID);
        individualSellersCall.enqueue(new Callback<IndividualSellers>() {
            @Override
            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                IndividualSellers individualSellers = response.body();
                int id = individualSellers.getId();
                getSeller(id, firebaseID);
            }

            @Override
            public void onFailure(Call<IndividualSellers> call, Throwable t) {
                Log.e("Getting Individual Seller ID Error: ", t.getMessage());
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
        Call<IndividualSellers> individualSellersCall = userAPI.getIndividualSellers(id);

        individualSellersCall.enqueue(new Callback<IndividualSellers>() {
            @Override
            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                IndividualSellers individualSellers = response.body();
                individualSellers.setProduct_inventory_SC(labor+process+packaging);
                individualSellers.setProduct_inventory_MC(ref+storage+insurance);
                setInventoryThreshold(firebaseID, individualSellers);
            }

            @Override
            public void onFailure(Call<IndividualSellers> call, Throwable t) {
                Log.e("Getting Individual Seller Error: ", t.getMessage());
            }
        });

    }

    private void setInventoryThreshold(String id, IndividualSellers individualSellers) {
        Call<IndividualSellers> individualSellersCall = userAPI.updateIndividualSellersByFirebaseID(id, individualSellers);
        individualSellersCall.enqueue(new Callback<IndividualSellers>() {
            @Override
            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                Toast.makeText(Activity_Add_Setup_Holding_Cost_Indiv.this, "Matagumpay ang pagbigay ng impormasyon", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Setup_Holding_Cost_Indiv.this, Activity_Products_Inventory_Individual.class);
                finish();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<IndividualSellers> call, Throwable t) {
                Log.e("Updating Individual Seller Error: ", t.getMessage());
            }
        });
    }

    private boolean validateInputs() {
        EditText[] fields = {laborText, processText, packagingText, refText, storageText, insuranceText};

        for (EditText field : fields) {
            if (field.getText().toString().trim().isEmpty()) {
                return false;
            }
            field.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        return true;
    }
}
