package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Population_Seller_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_or_group);
        initializeComponents();
    }

    private void initializeComponents() {
        Button individual = findViewById(R.id.individualButton);
        Button group = findViewById(R.id.groupButton);
        individual.setOnClickListener(view ->{
            Intent intent = new Intent(Population_Seller_Activity.this, SignUp_Indiv_Farmer_Activity.class);
            startActivity(intent);
        });
    }
}
