package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.GroupSellers;
import com.example.croop.model.PhoneVerification;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.GroupSellersSingleton;
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

public class SignUp_Farm_Assoc_Activity_4 extends AppCompatActivity {
    private EditText assocMobileText, assocEmailText;
    private FirebaseAuth mAuth;
    PhoneVerification verifyId = new PhoneVerification();

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc_4);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        assocMobileText = findViewById(R.id.assocOtpText);
        assocEmailText = findViewById(R.id.assocEmailText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            String assocMobile = assocMobileText.getText().toString();
            String assocEmail = assocEmailText.getText().toString();
            if(assocMobile.isEmpty() || !phoneNumberValidation(assocMobile)){
                Toast.makeText(this, "Please input a  valid mobile phone number.", Toast.LENGTH_SHORT).show();
            }else if(assocEmail.isEmpty() || !validEmail(assocEmail)){
                Toast.makeText(this, "Please input a valid email.", Toast.LENGTH_SHORT).show();
            }else{
                submitToFirebase(assocMobile, assocEmail);
            }
        });
    }

    private boolean validEmail(String coopEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(coopEmail).matches();
    }

    public void submitToFirebase(String assocMobile, String assocEmail){
        Date currentDate = new Date();
        assocMobile = assocMobile.trim();
        String coopPhone = formatPhone(assocMobile);
        GroupSellers groupSellers = GroupSellersSingleton.getInstance().getGroupSellers();
        groupSellers.setPhoneNum(coopPhone);
        groupSellers.setEmail(assocEmail);
        CurrentRole cr = CurrentUserSingleton.getInstance().getCurrentRole();

        FirebaseFirestore db =FirebaseFirestore.getInstance();
        Map<String, Object> groupSellerProfile = new HashMap<>();
        groupSellerProfile.put("Address", groupSellers.getAddress());
        groupSellerProfile.put("Age", groupSellers.getAge());
        groupSellerProfile.put("Bio", "I'm new here!");
        groupSellerProfile.put("Created At", currentDate);
        groupSellerProfile.put("Email", groupSellers.getEmail());
        groupSellerProfile.put("Messenger Link", groupSellers.getMessengerLink());
        groupSellerProfile.put("Name", groupSellers.getName());
        groupSellerProfile.put("Password", groupSellers.getPassword());
        groupSellerProfile.put("Updated At", currentDate);
        groupSellerProfile.put("Phone Number", groupSellers.getPhoneNum());
        groupSellerProfile.put("Role", cr.getRole());
        CollectionReference groupSellerRef = db.collection("Farming Association");
        groupSellerRef.add(groupSellerProfile).addOnSuccessListener(DocumentReference -> {
            Toast.makeText(this, "Group Seller successfully added!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SignUp_MobPhone_valid.class);
            startActivity(intent);
        }).addOnFailureListener(e ->{
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        });

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignUp_Farm_Assoc_Activity_4.this,"Verification Completed! " + phoneAuthCredential, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(SignUp_Farm_Assoc_Activity_4.this, "Verification Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                System.out.println("Code Sent: " + s);
                verifyId.setVerificationId(s);
                PhoneAuthenticationSimpleton.getInstance().setPhoneVerification(verifyId);
            }
        };
        sendToPhone(groupSellers, mCallbacks);
    }

    public String formatPhone(String coopMobile){
        if(coopMobile.startsWith("0")){
            return "+63" + coopMobile.substring(1);
        }
        return coopMobile;
    }

    public boolean phoneNumberValidation(String coopNumber){
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m =p.matcher(coopNumber);
        return (m.matches());
    }

    public void sendToPhone(GroupSellers groupSellers, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(groupSellers.getPhoneNum())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
