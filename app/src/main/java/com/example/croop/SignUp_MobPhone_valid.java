package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.PhoneVerification;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.PhoneAuthenticationSimpleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class SignUp_MobPhone_valid extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_mobphone_valid);
        mAuth = FirebaseAuth.getInstance();
        initializeComponent();
    }

    private void initializeComponent(){
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            EditText otpSent = findViewById(R.id.otpText);
            otpSent.setInputType(InputType.TYPE_CLASS_NUMBER);
            fourDigitFilter(otpSent);
            String otp = otpSent.getText().toString();
            PhoneVerification verifyId = PhoneAuthenticationSimpleton.getInstance().getPhoneVerification();
            String sentVerifyId = verifyId.getVerificationId();
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
                    switch (CurrentUserSingleton.getInstance().getCurrentRole().getRole()) {
                        case "Individual Business User":
                            Intent intent = new Intent(SignUp_MobPhone_valid.this, Home_Seller_Activity.class);
                            startActivity(intent);
                            System.out.println("Phone Verified");
                            break;
                        case "Group Business User":
                            Intent intent_1 = new Intent(SignUp_MobPhone_valid.this, Home_Group_Activity.class);
                            startActivity(intent_1);
                            System.out.println("Phone Verified");
                            break;
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void fourDigitFilter(EditText otpSent) {
        InputFilter filters[] = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4);
        otpSent.setFilters(filters);
    }

}

