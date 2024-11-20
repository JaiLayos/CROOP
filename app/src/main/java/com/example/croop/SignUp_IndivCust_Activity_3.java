package com.example.croop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUp_IndivCust_Activity_3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_3);
        
        initializeComponent();
    }

    private void initializeComponent() {
        EditText phoneNumber = findViewById(R.id.phoneNumText);
        EditText email = findViewById(R.id.emailText);
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(view ->{
            String phone_customer = String.valueOf(phoneNumber.getText());
            String email_customer = String.valueOf(email.getText());
            Date currentdate = new Date();

            Customer customer = CustomerSingleton.getInstance().getCustomer();
            customer.setCust_PhoneNum(phone_customer.toString());
            customer.setCust_Email(email_customer.toString());
            customer.setCust_CreatedAt(currentdate);
            customer.setCust_UpdatedAt(currentdate);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            Map<String, Object> customerProfile = new HashMap<>();
            customerProfile.put("Address", customer.getCust_Address());
            customerProfile.put("Bio", "Hi! I'm new here.");
            customerProfile.put("Created At", customer.getCust_CreatedAt());
            customerProfile.put("Email", customer.getCust_Email());
            customerProfile.put("Name", customer.getCust_Name());
            customerProfile.put("Updated At", customer.getCust_UpdatedAt());
            customerProfile.put("phone number", customer.getCust_PhoneNum());
            CollectionReference customerRef = db.collection("Customers");
                    customerRef.add(customerProfile).addOnSuccessListener(documentReference -> {
                        Toast.makeText(SignUp_IndivCust_Activity_3.this, "Customer added!", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e ->{
                        Toast.makeText(SignUp_IndivCust_Activity_3.this, "Error!", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
