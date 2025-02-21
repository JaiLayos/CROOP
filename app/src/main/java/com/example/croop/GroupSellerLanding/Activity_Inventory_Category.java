package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Activity_Inventory_Category extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_or_tools_inventory);
        initializeComponents();
    }

    private void initializeComponents() {
        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
            startActivity(intent);
        });
        Button product, item;
        product = findViewById(R.id.productInventoryButton);
        item = findViewById(R.id.itemInventoryButton);
        product.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Products_Inventory.class);
            startActivity(intent);
        });
        item.setOnClickListener(v -> {
            Intent intent = new Intent(this, Activity_Item_Inventory.class);
            startActivity(intent);
        });
    }
}
