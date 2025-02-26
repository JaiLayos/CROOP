package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class Authentication_Phone_Number extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_phone);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("storedVerificationId")) {
            Toast.makeText(this, "Hindi mahanap ang Verification ID. Pakisubukan muli", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }

        String verificationId = intent.getStringExtra("storedVerificationId");
        if (verificationId == null || verificationId.isEmpty()) {
            Toast.makeText(this, "Hindi tama ang Verification ID. Pakisubukan muli.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
            return;
        }
        EditText otpText = findViewById(R.id.otpText);
        otpText.setInputType(InputType.TYPE_CLASS_NUMBER);
        otpText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); // Limit input to 6 digits

        Button nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(v -> {
            String otp = otpText.getText().toString().trim();
            if (otp.isEmpty() || otp.length() < 6) {
                Toast.makeText(this, "Pakilagay ang valid 6-digit OTP", Toast.LENGTH_SHORT).show();
                return;
            }
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
                    Intent intent = new Intent(this, Authentication_Email_Password_Change_Email.class);
                    startActivity(intent);
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Nagkaproblema: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
