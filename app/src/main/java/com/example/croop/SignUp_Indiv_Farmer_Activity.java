package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.IndividualSellers;
import com.example.croop.singleton.IndividualSellersSingleton;

public class SignUp_Indiv_Farmer_Activity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_farmer);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText firstNameText = findViewById(R.id.indivFarmFirstName);
        EditText lastNameText = findViewById(R.id.indivFarmLastName);
        EditText ageText = findViewById(R.id.indivFarmAge);
        Button nextButton = findViewById(R.id.nextButtonSUIF);
        nextButton.setOnClickListener(view -> {
            String firstName = firstNameText.getText().toString();
            String lastName = lastNameText.getText().toString();
            int age = Integer.parseInt(ageText.getText().toString());
            IndividualSellers individualSellers = new IndividualSellers();
            individualSellers.setName(firstName + lastName);
            individualSellers.setAge(age);
            IndividualSellersSingleton.getInstance().setIndividualSellers(individualSellers);
            Intent intent = new Intent(SignUp_Indiv_Farmer_Activity.this,SignUp_Indiv_Farmer_Activity_2.class);
            startActivity(intent);
        });
    }
}
