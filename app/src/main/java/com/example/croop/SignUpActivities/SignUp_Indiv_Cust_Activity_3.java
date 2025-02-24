package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType; // import statement for password
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;

public class SignUp_Indiv_Cust_Activity_3 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_3);
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink8);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Indiv_Cust_Activity_3.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        EditText userPassword = findViewById(R.id.userPasswordText);
        EditText userRePassword = findViewById(R.id.userRePasswordText);

        userPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        userRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            String password = userPassword.getText().toString();
            String rePassword = userRePassword.getText().toString();
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Hindi sumusunod ang password sa mga patakaran.", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.matches(rePassword)){
                Customer customer = CustomerSingleton.getInstance().getCustomer();
                customer.setPassword(password);
                Intent intent = new Intent(this, SignUp_Indiv_Cust_Activity_4.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Hindi magkatugma ang password.", Toast.LENGTH_SHORT).show();
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
