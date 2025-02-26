package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.GroupSellersProductsInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Products extends AppCompatActivity {
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_products_group);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        Button add = findViewById(R.id.addButton);
        add.setOnClickListener(v -> {
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            FirebaseUser user = mAuth.getCurrentUser();
            Call<Integer> call = userAPI.getGroupSellersID(user.getUid());
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
                    Toast.makeText(Activity_Add_Products.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void addItemProcess(int id) {
        UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
        GroupSellers groupSellers = new GroupSellers();
        groupSellers.setID(id);
        EditText name, quantity, price;
        name = findViewById(R.id.nameText);
        quantity = findViewById(R.id.initialText);
        price = findViewById(R.id.priceText);
        GroupSellersProductsInventory groupSellersProductsInventory = new GroupSellersProductsInventory();
        groupSellersProductsInventory.setItemName(name.getText().toString());
        groupSellersProductsInventory.setItemStart(Integer.parseInt(quantity.getText().toString()));
        groupSellersProductsInventory.setPrice(Integer.parseInt(price.getText().toString()));
        groupSellersProductsInventory.setGroupSellers(groupSellers);
        Call<GroupSellersProductsInventory> call = userAPI.addProduct(groupSellersProductsInventory);
        call.enqueue(new Callback<GroupSellersProductsInventory>() {
            @Override
            public void onResponse(Call<GroupSellersProductsInventory> call, Response<GroupSellersProductsInventory> response) {
                Toast.makeText(Activity_Add_Products.this, name.getText().toString() + " ay nadagdag.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Products.this, Activity_Products_Inventory.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<GroupSellersProductsInventory> call, Throwable t) {
                Toast.makeText(Activity_Add_Products.this, "Nagkaproblema: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
