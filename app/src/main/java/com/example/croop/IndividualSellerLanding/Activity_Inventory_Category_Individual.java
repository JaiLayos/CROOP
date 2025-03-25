package com.example.croop.IndividualSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.GroupSellerLanding.Activity_Item_Inventory;
import com.example.croop.R;
import com.example.croop.model.IndividualSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Inventory_Category_Individual extends AppCompatActivity {
    private RetrofitService RetrofitClient;
    private UserAPI userAPI;
    private FirebaseAuth mAuth;
    private boolean scExist, mcExist;
    private Button product, item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_or_tools_inventory);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        FirebaseUser user = mAuth.getCurrentUser();
        userAPI = RetrofitClient.getClient().create(UserAPI.class);

        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            onBackPressed();
        });
        product = findViewById(R.id.productInventoryButton);
        item = findViewById(R.id.itemInventoryButton);

        product.setOnClickListener(v -> {
            getUserID(user.getUid());
        });
        item.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Item_Inventory.class);
            startActivity(intent);
        });
    }

    private void getUserID(String uid) {
        Call<IndividualSellers> individualSellersCall = userAPI.getIndividualSellersbyFirebaseID(uid);
        individualSellersCall.enqueue(new Callback<IndividualSellers>() {
            @Override
            public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                IndividualSellers individualSellers = response.body();
                int id = individualSellers.getId();
                checkThresholdExist(id);
            }

            @Override
            public void onFailure(Call<IndividualSellers> call, Throwable t) {
                Log.e("Individual Seller Call Error: ", t.getMessage());
            }
        });
    }

    private void checkThresholdExist(int id) {
        Call<Boolean> scCall = userAPI.checkIndividualSellerSC(id);
        scCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                scExist = response.body();
                Log.e("Setup Cost Success:", response.message());
                Call<Boolean> mcCall = userAPI.checkGroupSellerMC(id);
                mcCall.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        mcExist = response.body();
                        Log.e("Holding Cost Success:", response.message());
                        if(mcExist && scExist){
                            Intent intent = new Intent(Activity_Inventory_Category_Individual.this, Activity_Products_Inventory_Individual.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Activity_Inventory_Category_Individual.this,"Mangyaring sagutan ang mga sumusunod.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Activity_Inventory_Category_Individual.this, Activity_Add_Setup_Holding_Cost_Indiv.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.e("Holding Cost Error:", t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Setup Cost Error:", t.getMessage());
            }
        });
    }
}
