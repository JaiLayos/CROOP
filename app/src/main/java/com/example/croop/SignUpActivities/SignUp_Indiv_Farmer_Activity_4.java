package com.example.croop.SignUpActivities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.CurrentRole;
import com.example.croop.model.IndividualSellers;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.IndividualSellersSingleton;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_Indiv_Farmer_Activity_4 extends AppCompatActivity {

    private EditText mobilePhoneText, emailText, messengerText;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_farmer_4);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink2);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Indiv_Farmer_Activity_4.this, SignIn_Activity.class);
            startActivity(intent);
        });
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
                Toast.makeText(this, "Maglagay ng wastong messenger link.", Toast.LENGTH_SHORT).show();
            }else if(!validEmail(email) || email.isEmpty()){
                Toast.makeText(this, "Mangyaring maglagay ng wastong email!", Toast.LENGTH_SHORT).show();
            }else if(!phoneNumberValidation(mobilePhone) || mobilePhone.isEmpty()){
                Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Maglagay ng wastong numero ng telepono!", Toast.LENGTH_SHORT).show();
            }else{
                mobilePhone = mobilePhone.trim();
                String phoneNumber = formatPhone(mobilePhone);
                IndividualSellers individualSellers = IndividualSellersSingleton.getInstance().getIndividualSellers();
                individualSellers.setPhoneNum(phoneNumber);
                individualSellers.setEmail(email);
                individualSellers.setMessengerLink(messenger);
                individualSellers.setBio("I'm new here!");
                signUpUser(email, individualSellers.getPassword(), individualSellers);
            }
        });
    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void submitToFirebase(IndividualSellers individualSellers, String userId){
        CurrentRole cr = new CurrentRole();
        cr.setRole(individualSellers.returnRole());
        CurrentUserSingleton.getInstance().setCurrentRole(cr);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> indivSellerProfile = new HashMap<>();
        indivSellerProfile.put("Address", individualSellers.getAddress());
        indivSellerProfile.put("Age", individualSellers.getAge());
        indivSellerProfile.put("Bio", individualSellers.getBio());
        indivSellerProfile.put("Email", individualSellers.getEmail());
        indivSellerProfile.put("Messenger Link", individualSellers.getMessengerLink());
        indivSellerProfile.put("Name", individualSellers.getName());
        indivSellerProfile.put("Password", individualSellers.getPassword());
        indivSellerProfile.put("Phone Number", individualSellers.getPhoneNum());
        individualSellers.setRoles(cr.getRole());
        indivSellerProfile.put("Role", individualSellers.getRoles());

        db.collection("Individual Sellers").document(userId)
                .set(indivSellerProfile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Individual Seller Added!", Toast.LENGTH_SHORT).show();
                    sendToPhone(individualSellers, userId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Error! " + e, Toast.LENGTH_SHORT).show();
                });
    }

    private void signUpUser(String email, String password, IndividualSellers individualSellers) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            submitToFirebase(individualSellers, userId);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private void sendToPhone(IndividualSellers individualSellers, String userId){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("Verification Completed!");
                Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Kumpleto na ang beripikasyon!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                System.out.println("Verification Failed: " + e);
                Toast.makeText(SignUp_Indiv_Farmer_Activity_4.this, "Hindi matagumpay ang beripikasyon: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                sendToPostgres(individualSellers, userId);
                System.out.println("Code Sent: " + verificationId);;
                Intent intent = new Intent(SignUp_Indiv_Farmer_Activity_4.this, SignUp_MobPhone_valid.class);
                intent.putExtra("V_ID", verificationId);
                startActivity(intent);
            }
        };
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
    private void sendToPostgres(IndividualSellers individualSellers, String userID) {
        try {
            individualSellers.setFirebaseID(userID);
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            Call<IndividualSellers> call = userAPI.addIndividualSellers(individualSellers);
            call.enqueue(new Callback<IndividualSellers>() {
                @Override
                public void onResponse(Call<IndividualSellers> call, Response<IndividualSellers> response) {
                    if (response.isSuccessful()) {
                        Log.d("RetrofitAPI", "Data stored successfully in PostgreSQL");
                    } else {
                        Log.e("RetrofitAPI", "Error storing data: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<IndividualSellers> call, Throwable t) {
                    Log.e("RetrofitAPI", "Failed to send data", t);
                }
            });

        } catch (Exception e) {
            Log.e("RetrofitAPI", "Error building JSON", e);
        }
    }
}
