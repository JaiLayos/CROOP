package com.example.croop.IndividualSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.IndividualSellersProductsInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Products_Individual extends AppCompatActivity {
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_products_individual);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        Button add = findViewById(R.id.addButton);
        add.setOnClickListener(v -> {
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            FirebaseUser user = mAuth.getCurrentUser();
            Call<Integer> call = userAPI.getIndividualSellersID(user.getUid());
            call.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if(response.isSuccessful() && response.body()!=null){
                        int id = response.body();
                        addItemProcess(id);
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    Toast.makeText(Activity_Add_Products_Individual.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void addItemProcess(int id) {
        UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
        IndividualSellers individualSellers = new IndividualSellers();
        individualSellers.setID(id);
        EditText name, quantity, price;
        name = findViewById(R.id.nameText);
        quantity = findViewById(R.id.initialText);
        price = findViewById(R.id.priceText);
        IndividualSellersProductsInventory individualSellersProductsInventory = new IndividualSellersProductsInventory();
        individualSellersProductsInventory.setItemName(name.getText().toString());
        individualSellersProductsInventory.setItemStart(Integer.parseInt(quantity.getText().toString()));
        individualSellersProductsInventory.setPrice(Integer.parseInt(price.getText().toString()));
        individualSellersProductsInventory.setIndividualSellers(individualSellers);
        Call<IndividualSellersProductsInventory> call = userAPI.addIndividualProducts(individualSellersProductsInventory);
        call.enqueue(new Callback<IndividualSellersProductsInventory>() {
            @Override
            public void onResponse(Call<IndividualSellersProductsInventory> call, Response<IndividualSellersProductsInventory> response) {
                Toast.makeText(Activity_Add_Products_Individual.this, name.getText().toString() + " ay nadagdag.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Products_Individual.this, Activity_Products_Inventory_Individual.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<IndividualSellersProductsInventory> call, Throwable t) {
                Toast.makeText(Activity_Add_Products_Individual.this, "Nagkaproblema: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
