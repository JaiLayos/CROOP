package com.example.croop.SignUpActivities;

import static android.text.InputType.*;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
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
        firstNameText.setInputType(TYPE_CLASS_TEXT);
        lastNameText.setInputType(TYPE_CLASS_TEXT);
        ageText.setInputType(TYPE_CLASS_NUMBER);
        Button nextButton = findViewById(R.id.nextButtonSUIF);
        nextButton.setOnClickListener(view -> {
            String firstName = firstNameText.getText().toString();
            String lastName = lastNameText.getText().toString();
            String ageStr = ageText.getText().toString();
            int age;

            try {
                age = Integer.parseInt(ageStr);
                if (age < 18 || age > 80) {
                    Toast.makeText(this, "Age must be between 18 and 80", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ageStr.length() != 2) {
                    Toast.makeText(this, "Please enter a valid 2-digit age", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                return;
            }


            IndividualSellers individualSellers = new IndividualSellers();
            individualSellers.setName(firstName + " " + lastName);
            individualSellers.setAge(age);
            IndividualSellersSingleton.getInstance().setIndividualSellers(individualSellers);
            Intent intent = new Intent(SignUp_Indiv_Farmer_Activity.this,SignUp_Indiv_Farmer_Activity_2.class);
            startActivity(intent);
        });

        TextView signInHyperlink = findViewById(R.id.clickHere);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Indiv_Farmer_Activity.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }
}
