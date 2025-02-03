package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.CurrentUsage;
import com.example.croop.model.Customer;
import com.example.croop.model.GroupCustomer;
import com.example.croop.singleton.CurrentUsageSingleton;
import com.example.croop.singleton.CurrentUserSingleton;

public class Population_Customer_Activity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.grocery_or_retail);
        initializeComponents();
    }

    private void initializeComponents() {
        Button grocery = findViewById(R.id.groceryButton);
        Button retail = findViewById(R.id.retailButton);
        grocery.setOnClickListener(view -> {
            Customer customer = new Customer();;
            CurrentRole currentRole = new CurrentRole();
            currentRole.setRole(customer.setRole());
            CurrentUserSingleton.getInstance().setCurrentRole(currentRole);

            CurrentUsage currentUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
            String usage = currentUsage.getCurrentUsage();
            switch(usage){
                case "Sign Up":
                    Intent intent = new Intent(this, SignUp_Indiv_Cust_Activity.class);
                    startActivity(intent);
                    break;
                case "Sign In":
                    Intent intent_1 = new Intent(this, SignIn_Activity.class);
                    startActivity(intent_1);
                    break;
            }
        });
        retail.setOnClickListener(view -> {
            GroupCustomer groupCustomer = new GroupCustomer();
            CurrentRole currentRole = new CurrentRole();
            currentRole.setRole(groupCustomer.returnRole());
            CurrentUserSingleton.getInstance().setCurrentRole(currentRole);

            CurrentUsage currentUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
            String usage = currentUsage.getCurrentUsage();
            switch(usage){
                case "Sign Up":
                    Intent intent = new Intent(this, SignUp_Bulk_Activity.class);
                    startActivity(intent);
                    break;
                case "Sign In":
                    Intent intent_1 = new Intent(this, SignIn_Activity.class);
                    startActivity(intent_1);
                    break;
            }
        });
    }
}
