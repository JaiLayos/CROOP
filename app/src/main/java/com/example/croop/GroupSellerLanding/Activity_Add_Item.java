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
import com.example.croop.model.GroupSellersItemInventory;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Add_Item extends AppCompatActivity {
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_group);
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
                    Toast.makeText(Activity_Add_Item.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Item_Inventory.class);
            startActivity(intent);
            recreate();
        });
    }

    private void addItemProcess(int id) {
        UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
        GroupSellers groupSellers = new GroupSellers();
        groupSellers.setID(id);
        EditText name, quantity;
        name = findViewById(R.id.nameText);
        quantity = findViewById(R.id.initialText);
        GroupSellersItemInventory groupSellersItemInventory = new GroupSellersItemInventory();
        groupSellersItemInventory.setItemName(name.getText().toString());
        groupSellersItemInventory.setItemStart(Integer.parseInt(quantity.getText().toString()));
        groupSellersItemInventory.setGroupSellers(groupSellers);
        Call<GroupSellersItemInventory> call = userAPI.addItem(groupSellersItemInventory);
        call.enqueue(new Callback<GroupSellersItemInventory>() {
            @Override
            public void onResponse(Call<GroupSellersItemInventory> call, Response<GroupSellersItemInventory> response) {
                Toast.makeText(Activity_Add_Item.this, name.getText().toString() + " ay nadagdag.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Activity_Add_Item.this, Activity_Item_Inventory.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<GroupSellersItemInventory> call, Throwable t) {
                Toast.makeText(Activity_Add_Item.this, ": " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
