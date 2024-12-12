package com.example.croop;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.Customer;
import com.example.croop.model.PhoneVerification;
import com.example.croop.singleton.CustomerSingleton;
import com.example.croop.singleton.PhoneAuthenticationSimpleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class SignUp_IndivCust_MobPhone_valid extends AppCompatActivity {

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_mobphone_valid);
        mAuth = FirebaseAuth.getInstance();
        initializeComponent();
    }

    private void initializeComponent(){
        Customer customer = CustomerSingleton.getInstance().getCustomer();
        Button next = findViewById(R.id.nextButton);

        next.setOnClickListener(view -> {
            EditText otpSent = findViewById(R.id.otpText);
            otpSent.setInputType(InputType.TYPE_CLASS_NUMBER);
            fourDigitFilter(otpSent);
            String otp = otpSent.getText().toString();
            PhoneVerification verifyId = PhoneAuthenticationSimpleton.getInstance().getPhoneVerification();
            String sentVerifyId = verifyId.getVerificationId().toString();
            verifyCode(sentVerifyId, otp);
        });

        
    }

    public void verifyCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Phone Number Verified!");
                    } else {
                        System.out.println("Verification Failed: " + task.getException().getMessage());
                    }
                });
    }

    private void fourDigitFilter(EditText otpSent) {
        InputFilter filters[] = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4);
        otpSent.setFilters(filters);
    }

}

