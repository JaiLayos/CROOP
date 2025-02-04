package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;

public class SignUp_Indiv_Cust_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText firstName = findViewById(R.id.userFirstNameText);
        EditText lastName = findViewById(R.id.userLastNameText);
        EditText age = findViewById(R.id.userAgeText);
        firstName.setInputType(InputType.TYPE_CLASS_TEXT);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT);
        age.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(view ->{
            String first_name_customer = String.valueOf(firstName.getText());
            String last_name_customer = String.valueOf(lastName.getText());
            String string_age = String.valueOf(age.getText());
            int age_customer;
            try {
                age_customer = Integer.parseInt(string_age);
                if (age_customer < 18 || age_customer > 80) {
                    Toast.makeText(this, "Age must be between 18 and 80", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (string_age.length() != 2) {
                    Toast.makeText(this, "Please enter a valid 2-digit age", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Customer customer = new Customer();
            customer.setName(first_name_customer + " " + last_name_customer);
            customer.setAge(age_customer);
            CustomerSingleton.getInstance().setCustomer(customer);

            Intent intent = new Intent(SignUp_Indiv_Cust_Activity.this, SignUp_Indiv_Cust_Activity_2.class);
            startActivity(intent);
        });
    }


}