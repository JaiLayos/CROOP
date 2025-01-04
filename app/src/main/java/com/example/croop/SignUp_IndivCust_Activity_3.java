package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.Customer;
import com.example.croop.model.PhoneVerification;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.CustomerSingleton;
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

public class SignUp_IndivCust_Activity_3 extends AppCompatActivity {

    private FirebaseAuth mAuth;

    PhoneVerification verifyId = new PhoneVerification();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_3);
        mAuth = FirebaseAuth.getInstance();
        initializeComponent();
    }

    private void initializeComponent() {
        EditText phoneNumber = findViewById(R.id.userPhoneNumText);
        EditText email = findViewById(R.id.userEmailText);
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(view ->{
            String phone_customer = String.valueOf(phoneNumber.getText());;
            String email_customer = String.valueOf(email.getText());
            if(phoneNumberValidation(phone_customer)){
                Date currentdate = new Date();
                phone_customer= "+63" + phone_customer.substring(1);
                Customer customer = CustomerSingleton.getInstance().getCustomer();
                customer.setPhoneNum(phone_customer.toString());
                customer.setEmail(email_customer.toString());
                customer.setCreatedAt(currentdate);
                customer.setUpdatedAt(currentdate);

                CurrentRole cr = new CurrentRole();
                cr.setRole(customer.setRole());
                CurrentUserSingleton.getInstance().setCurrentRole(cr);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> customerProfile = new HashMap<>();
                customerProfile.put("Address", customer.getAddress());
                customerProfile.put("Age", customer.getAge());
                customerProfile.put("Bio", "Hi! I'm new here.");
                customerProfile.put("Created At", customer.getCreatedAt());
                customerProfile.put("Email", customer.getEmail());
                customerProfile.put("Name", customer.getName());
                customerProfile.put("Updated At", customer.getUpdatedAt());
                customerProfile.put("Phone Number", customer.getPhoneNum());
                customerProfile.put("Role", customer.setRole());
                CollectionReference customerRef = db.collection("Customers");
                customerRef.add(customerProfile).addOnSuccessListener(documentReference -> {
                    Toast.makeText(SignUp_IndivCust_Activity_3.this, "Customer added!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp_IndivCust_Activity_3.this, SignUp_MobPhone_valid.class);
                    startActivity(intent);
                }).addOnFailureListener(e ->{
                    Toast.makeText(SignUp_IndivCust_Activity_3.this, "Error!", Toast.LENGTH_SHORT).show();
                });
                PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {
                        System.out.println("Verification Completed!");
                    }
                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        System.out.println("Verification Failed: " + e.getMessage());
                    }
                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                        System.out.println("Code Sent: " + verificationId);
                        verifyId.setVerificationId(verificationId);
                        PhoneAuthenticationSimpleton.getInstance().setPhoneVerification(verifyId);
                    }
                };
                sendToPhone(customer, mCallbacks);
            }else{
                if(!phoneNumberValidation(phone_customer)){
                    Toast.makeText(SignUp_IndivCust_Activity_3.this, "Please input a valid phone number!", Toast.LENGTH_SHORT).show();
                }
            };
            });
    }

    private boolean phoneNumberValidation(String phoneNum) {
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(phoneNum);
        return(m.matches());
    }

    private void sendToPhone(Customer customer, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(customer.getPhoneNum().toString())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}
