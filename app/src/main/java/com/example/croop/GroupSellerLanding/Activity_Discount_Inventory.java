package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellersItemInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Discount_Inventory extends AppCompatActivity {
    private TableLayout table;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discounts_group_seller);
        initializeComponents();
    }

    private void initializeComponents() {
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        table = findViewById(R.id.tableLayout);

        Call<List<GroupSellersItemInventory>> call = apiService.getItemsByFirebaseID(user.getUid());
        call.enqueue(new Callback<List<GroupSellersItemInventory>>() {
            @Override
            public void onResponse(Call<List<GroupSellersItemInventory>> call, Response<List<GroupSellersItemInventory>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GroupSellersItemInventory> items = response.body();
                    Toast.makeText(Activity_Discount_Inventory.this, "Number of orders fetched: " + items.size(), Toast.LENGTH_SHORT).show();
                    populateTableDefault(items,table);
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                        Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupSellersItemInventory>> call, Throwable t) {
                Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        Button find, add;
        find = findViewById(R.id.findItemButton);
        find.setOnClickListener(v -> {
            EditText itemNameFind = findViewById(R.id.itemNameFindText);
            String itemName = itemNameFind.getText().toString();
            Call<List<GroupSellersItemInventory>> searchItem = apiService.getItemByName(itemName);
            searchItem.enqueue(new Callback<List<GroupSellersItemInventory>>() {
                @Override
                public void onResponse(Call<List<GroupSellersItemInventory>> call, Response<List<GroupSellersItemInventory>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<GroupSellersItemInventory> items = response.body();
                        Toast.makeText(Activity_Discount_Inventory.this, "Number of orders found: " + items.size(), Toast.LENGTH_SHORT).show();
                        populateTableDefault(items,table);
                    }else{
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                            Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<GroupSellersItemInventory>> call, Throwable t) {
                    Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });
        add = findViewById(R.id.addItemButton);
        add.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Add_Item.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Inventory_Category.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void populateTableDefault(List<GroupSellersItemInventory> items, TableLayout table) {
    }
}
