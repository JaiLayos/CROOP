package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;

public class SignUp_Indiv_Cust_Activity_3 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_3);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText userPassword = findViewById(R.id.userPasswordText);
        EditText userRePassword = findViewById(R.id.userRePasswordText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            String password = userPassword.getText().toString();
            String rePassword = userRePassword.getText().toString();
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password does not meet the requirements.", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.matches(rePassword)){
                Customer customer = CustomerSingleton.getInstance().getCustomer();
                customer.setPassword(password);
                Intent intent = new Intent(this, SignUp_Indiv_Cust_Activity_4.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Password doesn't match.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 32) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*_].*")) {
            return false;
        }
        return true;
    }
}
