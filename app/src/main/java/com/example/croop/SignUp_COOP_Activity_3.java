package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.GroupSellersSingleton;

public class SignUp_COOP_Activity_3 extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_coop_3);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText passwordText = findViewById(R.id.coopPasswordText);
        EditText repasswordText = findViewById(R.id.coopRePasswordText);
        String password = passwordText.getText().toString();
        String repassword = repasswordText.getText().toString();

        Button next = findViewById(R.id.button);
        next.setOnClickListener(view -> {
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password does not meet the requirements.", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.matches(repassword)){
                GroupSellers groupSellers = GroupSellersSingleton.getInstance().getGroupSellers();
                groupSellers.setPassword(password);
                Intent intent = new Intent(this, SignUp_COOP_Activity_4.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
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
        if (!password.matches(".*[!@#$%^&*].*")) {
            return false;
        }
        return true;
    }
}
