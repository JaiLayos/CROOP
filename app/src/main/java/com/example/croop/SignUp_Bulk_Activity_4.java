package com.example.croop;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.GroupCustomer;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.GroupCustomerSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class SignUp_Bulk_Activity_4 extends AppCompatActivity {
    private EditText bulkMobileText, bulkEmailText;
    private FirebaseAuth mAuth;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_retailer_4);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }
    private void initializeComponents() {
        bulkMobileText = findViewById(R.id.bulkPhoneText);
        bulkEmailText = findViewById(R.id.bulkEmailText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            String bulkMobile = bulkMobileText.getText().toString();
            String bulkEmail = bulkEmailText.getText().toString();
            if(bulkMobile.isEmpty() || !phoneNumberValidation(bulkMobile)){
                Toast.makeText(this, "Please input a  valid mobile phone number.", Toast.LENGTH_SHORT).show();
            }else if(bulkEmail.isEmpty() || !validEmail(bulkEmail)){
                Toast.makeText(this, "Please input a valid email.", Toast.LENGTH_SHORT).show();
            }else{
                bulkMobile = bulkMobile.trim();
                bulkMobile = formatPhone(bulkMobile);
                GroupCustomer groupCustomer = GroupCustomerSingleton.getInstance().getGroupCustomer();
                groupCustomer.setPhoneNum(bulkMobile);
                groupCustomer.setEmail(bulkEmail);
                signUpUser(bulkEmail, groupCustomer.getPassword(), groupCustomer);
            }
        });
    }

    private void signUpUser(String email, String password, GroupCustomer groupCustomer) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            submitToFirebase(groupCustomer);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp_Bulk_Activity_4.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validEmail(String coopEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(coopEmail).matches();
    }

    public void submitToFirebase(GroupCustomer groupCustomer){
        Date currentDate = new Date();

        CurrentRole cr = CurrentUserSingleton.getInstance().getCurrentRole();

        FirebaseFirestore db =FirebaseFirestore.getInstance();
        Map<String, Object> groupCustomerProfile = new HashMap<>();
        groupCustomerProfile.put("Address", groupCustomer.getAddress());
        groupCustomerProfile.put("Age", groupCustomer.getAge());
        groupCustomerProfile.put("Bio", groupCustomer.getBio());
        groupCustomerProfile.put("Created At", currentDate);
        groupCustomerProfile.put("Email", groupCustomer.getEmail());
        groupCustomerProfile.put("Messenger Link", groupCustomer.getMessengerLink());
        groupCustomerProfile.put("Name", groupCustomer.getName());
        groupCustomerProfile.put("Password", groupCustomer.getPassword());
        groupCustomerProfile.put("Updated At", currentDate);
        groupCustomerProfile.put("Phone Number", groupCustomer.getPhoneNum());
        groupCustomerProfile.put("Role", cr.getRole());
        CollectionReference groupSellerRef = db.collection("Group Customers");
        groupSellerRef.add(groupCustomerProfile).addOnSuccessListener(DocumentReference -> {
            Toast.makeText(this, "Group Customer successfully added!", Toast.LENGTH_SHORT).show();
            sendToPhone(groupCustomer);
        }).addOnFailureListener(e ->{
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        });
    }

    public String formatPhone(String bulkMobile){
        if(bulkMobile.startsWith("0")){
            return "+63" + bulkMobile.substring(1);
        }
        return bulkMobile;
    }

    public boolean phoneNumberValidation(String bulkMobile){
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m =p.matcher(bulkMobile);
        return (m.matches());
    }

    public void sendToPhone(GroupCustomer groupCustomer){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignUp_Bulk_Activity_4.this,"Verification Completed! " + phoneAuthCredential, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(SignUp_Bulk_Activity_4.this, "Verification Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                System.out.println("Code Sent: " + verificationId);
                Intent intent = new Intent(SignUp_Bulk_Activity_4.this, SignUp_MobPhone_valid.class);
                intent.putExtra("V_ID", verificationId);
                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(groupCustomer.getPhoneNum())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
