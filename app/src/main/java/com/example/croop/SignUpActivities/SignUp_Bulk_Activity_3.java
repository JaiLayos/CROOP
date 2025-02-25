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
import com.example.croop.model.GroupCustomer;
import com.example.croop.singleton.GroupCustomerSingleton;

public class SignUp_Bulk_Activity_3 extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_retailer_3);
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink7);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Bulk_Activity_3.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        EditText bulkPassword = findViewById(R.id.bulkPasswordText);
        EditText bulkRePassword = findViewById(R.id.bulkRePasswordText);

        bulkPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        bulkRePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button next = findViewById(R.id.nextButton_SUR_3);
        next.setOnClickListener(view -> {
            String password = bulkPassword.getText().toString();
            String repassword = bulkRePassword.getText().toString();
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Hindi sumusunod ang password sa mga patakaran.", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.matches(repassword)){
                GroupCustomer groupCustomer = GroupCustomerSingleton.getInstance().getGroupCustomer();
                groupCustomer.setPassword(password);
                Intent intent = new Intent(this, SignUp_Bulk_Activity_4.class);
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
