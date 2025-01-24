package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.IndividualSellers;
import com.example.croop.model.PhoneVerification;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.IndividualSellersSingleton;
import com.example.croop.singleton.PhoneAuthenticationSimpleton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Indiv_Farmer_Activity_4 extends AppCompatActivity {

    private EditText mobilePhoneText, emailText, messengerText;
    private FirebaseAuth mAuth;

    PhoneVerification verifyId = new PhoneVerification();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_farmer_4);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        mobilePhoneText = findViewById(R.id.indivFarmMobileText);
        emailText = findViewById(R.id.indivFarmEmailText);
        messengerText = findViewById(R.id.indivFarmMessengerText);
        Button next = findViewById(R.id.nextButton_SUIF_2);
        next.setOnClickListener(view -> {
            String mobilePhone = mobilePhoneText.getText().toString();
            String email = emailText.getText().toString();
            String messenger = messengerText.getText().toString();
            if(messenger.isEmpty()){
                Toast.makeText(this, "Please input a valid messenger link.", Toast.LENGTH_SHORT).show();
            }else if(!validEmail(email) || email.isEmpty()){
                Toast.makeText(this, "Please input a valid email!", Toast.LENGTH_SHORT).show();
            }else if(!phoneNumberValidation(mobilePhone)){
                Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Please input a valid phone number!", Toast.LENGTH_SHORT).show();
            }else{
                submitToFirebase(mobilePhone, email, messenger);
            }
        });
    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void submitToFirebase(String mobilePhone, String email, String messenger){
        mobilePhone = mobilePhone.trim();
        String phoneNumber = formatPhone(mobilePhone);
        IndividualSellers individualSellers = IndividualSellersSingleton.getInstance().getIndividualSellers();
        CurrentRole cr = new CurrentRole();
        cr.setRole(individualSellers.returnRole());
        CurrentUserSingleton.getInstance().setCurrentRole(cr);

        individualSellers.setPhoneNum(phoneNumber);
        individualSellers.setEmail(email);
        individualSellers.setMessengerLink(messenger);
        individualSellers.setBio("I'm new here!");
        individualSellers.setCreatedAt(new Date());
        individualSellers.setUpdatedAt(new Date());

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
            Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this,"Individual Seller Added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUp_Indiv_Farmer_Activity_4.this, SignUp_MobPhone_valid.class);
            startActivity(intent);
        }).addOnFailureListener(e -> {
            Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Error!" + e, Toast.LENGTH_SHORT).show();
        });
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("Verification Completed!");
                Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Verification Completed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                System.out.println("Verification Failed: " + e);
                Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                System.out.println("Code Sent: " + verificationId);
                verifyId.setVerificationId(verificationId);
                PhoneAuthenticationSimpleton.getInstance().setPhoneVerification(verifyId);
            }
        };
        sendToPhone(individualSellers, mCallbacks);
    }
    private String formatPhone(String mobilePhone) {
        if (mobilePhone.startsWith("0")) {
            return "+63" + mobilePhone.substring(1); // Replace "0" with "+63"
        }
        return mobilePhone;
    }

    private boolean phoneNumberValidation(String mobilePhone) {
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(mobilePhone);
        return (m.matches());
    }

    private void sendToPhone(IndividualSellers individualSellers, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(individualSellers.getPhoneNum())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
