package com.example.croop;

import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.IndividualSellers;
import com.example.croop.singleton.IndividualSellersSingleton;

public class SignUp_Indiv_Farmer_Activity_3 extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_farmer_3);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText passwordText = findViewById(R.id.indivFarmPasswordText);
        EditText repasswordText = findViewById(R.id.indivFarmPasswordReText);
        Button next = findViewById(R.id.nextButton_SUIF_3);

        passwordText.setInputType(TYPE_TEXT_VARIATION_PASSWORD);
        repasswordText.setInputType(TYPE_TEXT_VARIATION_PASSWORD);

        next.setOnClickListener(view -> {
            String password = passwordText.getText().toString();
            String repassword = repasswordText.getText().toString();
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password does not meet the requirements.", Toast.LENGTH_LONG).show();
                return;
            }
            if(password.isEmpty()){
                Toast.makeText(this, "Please input a password!", Toast.LENGTH_SHORT).show();
            }else if(!password.equals(repassword)){
                Toast.makeText(this, "Password doesn't match.", Toast.LENGTH_SHORT).show();
            }else{
                IndividualSellers individualSellers = IndividualSellersSingleton.getInstance().getIndividualSellers();
                individualSellers.setPassword(password);
                Intent intent = new Intent(SignUp_Indiv_Farmer_Activity_3.this, SignUp_Indiv_Farmer_Activity_4.class);
                startActivity(intent);
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
