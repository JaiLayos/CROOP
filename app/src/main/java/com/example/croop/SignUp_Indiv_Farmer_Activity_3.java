package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.PhoneVerification;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.IndividualSellersSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp_Indiv_Farmer_Activity_3 extends AppCompatActivity {

    private EditText mobilePhoneText, emailText, messengerText;
    private FirebaseAuth mAuth;

    PhoneVerification verifyId = new PhoneVerification();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_farmer_3);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        mobilePhoneText = findViewById(R.id.indivFarmMobileText);
        emailText = findViewById(R.id.indivFarmEmailText);
        messengerText = findViewById(R.id.indivFarmMessengerText);
        Button next = findViewById(R.id.nextButton_SUIF_2);
        next.setOnClickListener(view -> {
            IndividualSellers individualSellers = IndividualSellersSingleton.getInstance().getIndividualSellers();
            CurrentRole cr = new CurrentRole();
            cr.setRole(individualSellers.setRole());
            CurrentUserSingleton.getInstance().setCurrentRole(cr);

            String mobilePhone = mobilePhoneText.getText().toString();
            String email = emailText.getText().toString();
            String messenger = messengerText.getText().toString();

            individualSellers.setPhoneNum(mobilePhone);
            individualSellers.setEmail(email);
            individualSellers.setMessengerLink(messenger);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> indivSellerProfile = new HashMap<>();
            indivSellerProfile.put("Address", individualSellers.getAddress());
            indivSellerProfile.put("Age", individualSellers.getAge());
            indivSellerProfile.put("Bio", individualSellers.getBio());
            indivSellerProfile.put("Created At", individualSellers.getCreatedAt());
            indivSellerProfile.put("Email", individualSellers.getEmail());
            indivSellerProfile.put("Messenger Link", individualSellers.getMessengerLink());
            indivSellerProfile.put("Name", individualSellers.getName());
            indivSellerProfile.put("Updated At", individualSellers.getUpdatedAt());
            indivSellerProfile.put("Phone Number", individualSellers.getPhoneNum());
            indivSellerProfile.put("Role", cr.getRole());
            CollectionReference indivSellerRef = db.collection("Individual Sellers");

            indivSellerRef.add(indivSellerProfile).addOnSuccessListener(DocumentReference -> {
                Toast.makeText(SignUp_Indiv_Farmer_Activity_3.this,"Individual Seller Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUp_Indiv_Farmer_Activity_3.this, SignUp_MobPhone_valid.class);
            }).addOnFailureListener(e -> {

            });

        });
    }
}
