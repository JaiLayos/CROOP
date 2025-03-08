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
import com.example.croop.model.Customer;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.CustomerSingleton;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp_Customer_Activity_4 extends AppCompatActivity {

    private EditText phoneNumberText, emailText;
    private FirebaseAuth mAuth;
    private RetrofitService RetrofitClient;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_4);
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().setLanguageCode(String.valueOf(Log.DEBUG));
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Customer_Activity_4.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        phoneNumberText = findViewById(R.id.userPhoneNumText);
        emailText = findViewById(R.id.userEmailLabel);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            String mobilePhone = phoneNumberText.getText().toString();
            String email = emailText.getText().toString();
            if(!validEmail(email) || email.isEmpty()){
                Toast.makeText(this, "Mangyaring maglagay ng wastong email.", Toast.LENGTH_SHORT).show();
            }else if(!phoneNumberValidation(mobilePhone)){
                Toast.makeText(SignUp_Customer_Activity_4.this, "Maglagay ng wastong numero ng telepono.", Toast.LENGTH_SHORT).show();
            }else{
                mobilePhone = mobilePhone.trim();
                String phoneNumber = formatPhone(mobilePhone);
                Customer customer = CustomerSingleton.getInstance().getCustomer();
                customer.setPhoneNum(phoneNumber);
                customer.setEmail(email);
                customer.setBio("I'm new here!");
                signUpUser(email, customer.getPassword(), customer);
            }
        });
    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void submitToFirebase(Customer customer, String userID){
        Date date = new Date();
        CurrentRole cr = new CurrentRole();
        cr.setRole(customer.getRoles());
        CurrentUserSingleton.getInstance().setCurrentRole(cr);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("Address", customer.getAddress());
        userProfile.put("Age", customer.getAge());
        userProfile.put("Bio", customer.getBio());
        userProfile.put("Created At", date);
        userProfile.put("Email", customer.getEmail());
        userProfile.put("Messenger Link", customer.getMessengerLink());
        userProfile.put("Name", customer.getName());
        userProfile.put("Password", customer.getPassword());
        userProfile.put("Updated At", date);
        userProfile.put("Phone Number", customer.getPhoneNum());
        customer.setRoles(cr.getRole());
        userProfile.put("Role", customer.getRoles());
        CollectionReference userProfileRef = db.collection("Customers");

        db.collection("Customers").document(userID)
                .set(userProfile)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Matagumpay na naidagdag ang Customer!", Toast.LENGTH_SHORT).show();
                    sendToPhone(customer, userID);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error! " + e, Toast.LENGTH_SHORT).show();
                });
    }

    private void signUpUser(String email, String password, Customer customer) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            submitToFirebase(customer, user.getUid());
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUp_Customer_Activity_4.this, "Hindi ka nakapag-authenticate..",
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

    private void sendToPhone(Customer customer, String userID){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                System.out.println("Verification Completed!");
                Toast.makeText(SignUp_Customer_Activity_4.this, "Kumpleto na ang beripikasyon!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                System.out.println("Verification Failed: " + e);
                Toast.makeText(SignUp_Customer_Activity_4.this, "Hindi matagumpay ang beripikasyon: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                sendToPostgres(customer, userID);
                System.out.println("Code Sent: " + verificationId);;
                Intent intent = new Intent(SignUp_Customer_Activity_4.this, SignUp_MobPhone_valid.class);
                intent.putExtra("V_ID", verificationId);
                startActivity(intent);
            }
        };
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(customer.getPhoneNum())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void sendToPostgres(Customer customer, String userID) {
        try {
            customer.setFirebaseID(userID);
            UserAPI userAPI = RetrofitClient.getClient().create(UserAPI.class);
            Call<Customer> call = userAPI.addCustomer(customer);
            call.enqueue(new Callback<Customer>() {
                @Override
                public void onResponse(Call<Customer> call, Response<Customer> response) {
                    if (response.isSuccessful()) {
                        Log.d("RetrofitAPI", "Data stored successfully in PostgreSQL");
                    } else {
                        Log.e("RetrofitAPI", "Error storing data: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Customer> call, Throwable t) {
                    Log.e("RetrofitAPI", "Failed to send data", t);
                }
            });

        } catch (Exception e) {
            Log.e("RetrofitAPI", "Error building JSON", e);
        }
    }

}
