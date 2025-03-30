package com.example.croop.SignUpActivities;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.CurrentRole;
import com.example.croop.model.GroupSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.GroupSellersSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_COOP_Activity_4 extends AppCompatActivity {
    private EditText coopMobileText, coopEmailText, coopMessengerText;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_coop_4);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_COOP_Activity_4.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        coopMobileText = findViewById(R.id.coopMobileText);
        coopEmailText = findViewById(R.id.coopEmailText);
        coopMessengerText = findViewById(R.id.coopMessengerText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            String coopMobile = coopMobileText.getText().toString();
            String coopEmail = coopEmailText.getText().toString();
            String messenger = coopMessengerText.getText().toString();
            if(messenger.isEmpty()) {
                Toast.makeText(this, "Maglagay ng wastong messenger link.", Toast.LENGTH_SHORT).show();
            } else if(coopMobile.isEmpty() || !phoneNumberValidation(coopMobile)){
                Toast.makeText(this, "Maglagay ng wastong numero ng telepono.", Toast.LENGTH_SHORT).show();
            }else if(coopEmail.isEmpty() || !validEmail(coopEmail)){
                Toast.makeText(this, "Mangyaring maglagay ng wastong email..", Toast.LENGTH_SHORT).show();
            }else{
                coopMobile = coopMobile.trim();
                String coopPhone = formatPhone(coopMobile);
                GroupSellers groupSellers = GroupSellersSingleton.getInstance().getGroupSellers();
                groupSellers.setPhoneNum(coopPhone);
                groupSellers.setEmail(coopEmail);
                groupSellers.setMessengerLink(messenger);
                signUpUser(coopEmail, groupSellers.getPassword(), groupSellers);
            }
        });
    }

    private void signUpUser(String email, String password, GroupSellers groupSellers) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            submitToFirebase(groupSellers, user.getUid());
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp_COOP_Activity_4.this, "Hindi ka nakapag-authenticate.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validEmail(String coopEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(coopEmail).matches();
    }

    public void submitToFirebase(GroupSellers groupSellers, String userId){
        Date currentDate = new Date();
        CurrentRole cr = CurrentUserSingleton.getInstance().getCurrentRole();

        FirebaseFirestore db =FirebaseFirestore.getInstance();
        Map<String, Object> groupSellerProfile = new HashMap<>();
        groupSellerProfile.put("Address", groupSellers.getAddress());
        groupSellerProfile.put("Group Name", groupSellers.getGroupName());
        groupSellerProfile.put("Bio", "Hi! I'm new here");
        groupSellerProfile.put("Created At", currentDate);
        groupSellerProfile.put("Email", groupSellers.getEmail());
        groupSellerProfile.put("Messenger Link", groupSellers.getMessengerLink());
        groupSellerProfile.put("Name", groupSellers.getName());
        groupSellerProfile.put("Password", groupSellers.getPassword());
        groupSellerProfile.put("Position", groupSellers.getPersonPosition());
        groupSellerProfile.put("Updated At", currentDate);
        groupSellerProfile.put("Phone Number", groupSellers.getPhoneNum());
        groupSellers.setRoles(groupSellers.returnRole_coop());
        groupSellerProfile.put("Role", cr.getRole());

        db.collection("Farming Cooperatives").document(userId)
                .set(groupSellerProfile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUp_COOP_Activity_4.this, "Group Seller Added!", Toast.LENGTH_SHORT).show();
                    sendToPhone(groupSellers, userId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUp_COOP_Activity_4.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                });
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

    public void sendToPhone(GroupSellers groupSellers, String userId){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(SignUp_COOP_Activity_4.this,"Verification Completed! " + phoneAuthCredential, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(SignUp_COOP_Activity_4.this, "Verification Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                sendToPostgres(groupSellers,userId);
                System.out.println("Code Sent: " + verificationId);
                Intent intent = new Intent(SignUp_COOP_Activity_4.this, SignUp_MobPhone_valid.class);
                intent.putExtra("V_ID", verificationId);
                startActivity(intent);
            }
        };
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
    private void sendToPostgres(GroupSellers groupSellers, String userID) {
        try {
            groupSellers.setFirebaseID(userID);
            groupSellers.setBio("Hi! I'm new here.");
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            Call<Void> call = userAPI.sendGroupSellers(groupSellers);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("RetrofitAPI", "Data stored successfully in PostgreSQL");
                    } else {
                        Log.e("RetrofitAPI", "Error storing data: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("RetrofitAPI", "Failed to send data", t);
                }
            });

        } catch (Exception e) {
            Log.e("RetrofitAPI", "Error building JSON", e);
        }
    }
}
