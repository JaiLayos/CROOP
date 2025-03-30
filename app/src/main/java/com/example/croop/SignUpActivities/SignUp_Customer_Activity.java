package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;

public class SignUp_Customer_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust);
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Customer_Activity.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        EditText firstName = findViewById(R.id.userFirstNameText);
        EditText lastName = findViewById(R.id.userLastNameText);
        firstName.setInputType(InputType.TYPE_CLASS_TEXT);
        lastName.setInputType(InputType.TYPE_CLASS_TEXT);

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(view ->{
            String first_name_customer = String.valueOf(firstName.getText());
            String last_name_customer = String.valueOf(lastName.getText());
            
            Customer customer = new Customer();
            customer.setName(first_name_customer + " " + last_name_customer);
            CustomerSingleton.getInstance().setCustomer(customer);

            Intent intent = new Intent(SignUp_Customer_Activity.this, SignUp_Customer_Activity_2.class);
            startActivity(intent);
        });
    }


}