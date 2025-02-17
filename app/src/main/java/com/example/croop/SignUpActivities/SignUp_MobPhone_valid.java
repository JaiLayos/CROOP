package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
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
        Intent intent = getIntent();
        String verificationId = intent.getStringExtra("V_ID");
        initializeComponent(verificationId);
    }

    private void initializeComponent(String verificationId){
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            EditText otpSent = findViewById(R.id.otpText);
            otpSent.setInputType(InputType.TYPE_CLASS_NUMBER);
            fourDigitFilter(otpSent);
            String otp = otpSent.getText().toString();;
            verifyCode(verificationId, otp);
        });
    }

    public void verifyCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    Intent intent = new Intent(this, SignIn_Activity.class);
                    startActivity(intent);
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

