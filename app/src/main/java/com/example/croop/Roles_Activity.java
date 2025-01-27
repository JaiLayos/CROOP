package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Roles_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_or_customer);
        initializeComponents();
    }

    private void initializeComponents() {
        Button seller = findViewById(R.id.sellerButton);
        Button customer = findViewById(R.id.customerButton);
        seller.setOnClickListener(view -> {
            Intent intent = new Intent(Roles_Activity.this,Population_Seller_Activity.class);
            startActivity(intent);
        });
        customer.setOnClickListener(view -> {
            Intent intent = new Intent(Roles_Activity.this,Population_Customer_Activity.class);
            startActivity(intent);
        });
    }
}
