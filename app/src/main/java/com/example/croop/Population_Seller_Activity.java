package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentUsage;
import com.example.croop.singleton.CurrentUsageSingleton;

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
            CurrentUsage cUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
            String current = cUsage.getCurrentUsage();
            switch(current){
                case "Sign Up":
                    Intent intent_signUp = new Intent(Population_Seller_Activity.this, SignUp_Indiv_Farmer_Activity.class);
                    startActivity(intent_signUp);
                    break;
                case "Sign In":
                    Intent intent_signIn = new Intent(Population_Seller_Activity.this, SignIn_Activity.class);
                    startActivity(intent_signIn);
            }
        });
        group.setOnClickListener(view -> {
            CurrentUsage cUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
            String current = cUsage.getCurrentUsage();
            switch(current){
                case "Sign Up":
                    Intent intent_signUp = new Intent(Population_Seller_Activity.this, Group_Seller_Activity.class);
                    startActivity(intent_signUp);
                    break;
                case "Sign In":
                    Intent intent_signIn = new Intent(Population_Seller_Activity.this, SignIn_Activity.class);
                    startActivity(intent_signIn);
            }

        });
    }
}
