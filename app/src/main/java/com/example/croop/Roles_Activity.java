package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.SignUpActivities.Population_Seller_Activity;
import com.example.croop.SignUpActivities.SignUp_Customer_Activity;
import com.example.croop.model.CurrentRole;
import com.example.croop.model.CurrentUsage;
import com.example.croop.model.Customer;
import com.example.croop.singleton.CurrentUsageSingleton;
import com.example.croop.singleton.CurrentUserSingleton;

public class Roles_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_or_customer);
        initializeComponents();
    }


    private void initializeComponents() {
        CurrentUsage currentUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
        Customer customer = new Customer();
        CurrentRole currentRole = new CurrentRole();
        currentRole.setRole(customer.getRoles());
        String usage = currentUsage.getCurrentUsage();
        Button forSeller = findViewById(R.id.sellerButton);
        Button forCustomer = findViewById(R.id.customerButton);

        forSeller.setOnClickListener(view -> {
            Intent intent = new Intent(Roles_Activity.this, Population_Seller_Activity.class);
            startActivity(intent);
        });
        forCustomer.setOnClickListener(view -> {
            CurrentUserSingleton.getInstance().setCurrentRole(currentRole);
            if(usage == "Sign In"){
                Intent intent = new Intent(Roles_Activity.this, SignIn_Activity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Roles_Activity.this, SignUp_Customer_Activity.class);
                startActivity(intent);
            }

        });
    }
}
