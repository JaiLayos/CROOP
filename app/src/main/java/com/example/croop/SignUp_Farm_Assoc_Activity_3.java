package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class SignUp_Farm_Assoc_Activity_3 extends AppCompatActivity {
    private EditText phoneNum, email;
    private Button next;
    private FirebaseAuth mAuth;

    PhoneVerification verifyId = new PhoneVerification();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc_3);
        mAuth =FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        phoneNum = findViewById(R.id.assocOtpText);
        email = findViewById(R.id.assocEmailText);
        next = findViewById(R.id.nextButton);

        next.setOnClickListener(view -> {
            Date currentDate = new Date();
            GroupSellers gSellers = GroupSellersSingleton.getInstance().getGroupSellers();
            gSellers.setPhoneNum(String.valueOf(phoneNum));
            gSellers.setEmail(String.valueOf(email));
            gSellers.setCreatedAt(currentDate);
            gSellers.setUpdatedAt(currentDate);

            CurrentRole cr = new CurrentRole();
            cr.setRole(gSellers.setRole());
            CurrentUserSingleton.getInstance().setCurrentRole(cr);

            if(phoneNumberValidation(String.valueOf(phoneNum))){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> gSellerProfile = new HashMap<>();
                gSellerProfile.put("Address", gSellers.getAddress());
                gSellerProfile.put("Association", gSellers.getGroupName());
                gSellerProfile.put("Age", gSellers.getAge());
                gSellerProfile.put("Bio", gSellers.getBio());
                gSellerProfile.put("Created At", gSellers.getCreatedAt());
                gSellerProfile.put("Email", gSellers.getEmail());
                gSellerProfile.put("Point Person Name", gSellers.getName());
                gSellerProfile.put("Updated At", gSellers.getUpdatedAt());
                gSellerProfile.put("Phone Number", gSellers.getPhoneNum());
                gSellerProfile.put("Role", cr.getRole());
                CollectionReference gSellerRef = db.collection("Group Seller");

                gSellerRef.add(gSellerProfile).addOnSuccessListener(DocumentReference ->{
                    Toast.makeText(SignUp_Farm_Assoc_Activity_3.this,"Group Seller Added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp_Farm_Assoc_Activity_3.this, SignUp_MobPhone_valid.class);
                }).addOnFailureListener( e ->{

                });

                PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        System.out.println("Verification Complete!");
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        System.out.println("Verification Failed! Error: " + e);
                    }
                    @Override
                    public void onCodeSent(String verificationID, PhoneAuthProvider.ForceResendingToken token){
                        System.out.println("Code Sent: " + verificationID);
                        verifyId.setVerificationId(verificationID);
                        PhoneAuthenticationSimpleton.getInstance().setPhoneVerification(verifyId);
                    }
                };

                sendToPhone(gSellers, mCallbacks);
            }else{
                if(!phoneNumberValidation(String.valueOf(phoneNum))){
                    Toast.makeText(SignUp_Farm_Assoc_Activity_3.this, "Please input a valid phone number!", Toast.LENGTH_SHORT).show();
                }
            }


            }
        );
    }

    private void sendToPhone(GroupSellers gSellers, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(gSellers.getPhoneNum().toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private boolean phoneNumberValidation(String phoneNum){
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(phoneNum);
        return(m.matches());
    }
}
