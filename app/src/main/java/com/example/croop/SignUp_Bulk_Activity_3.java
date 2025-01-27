package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.GroupCustomer;
import com.example.croop.singleton.GroupCustomerSingleton;

public class SignUp_Bulk_Activity_3 extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_retailer_3);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText bulkPassword = findViewById(R.id.bulkPasswordText);
        EditText bulkRePassword = findViewById(R.id.bulkRePasswordText);

        String password = bulkPassword.getText().toString();
        String repassword = bulkRePassword.getText().toString();

        Button next = findViewById(R.id.nextButton_SUR_3);
        next.setOnClickListener(view -> {
            if(password.matches(repassword)){
                GroupCustomer groupCustomer = GroupCustomerSingleton.getInstance().getGroupCustomer();
                groupCustomer.setPassword(password);
                Intent intent = new Intent(this, SignUp_Bulk_Activity_4.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Password doesn't match.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
