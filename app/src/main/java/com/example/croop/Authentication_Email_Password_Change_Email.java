package com.example.croop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Authentication_Email_Password_Change_Email extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_email_password);
        mAuth = FirebaseAuth.getInstance();
        initializeComponents();
    }

    private void initializeComponents() {
        EditText authEmailText, newEmailText, authPassText, authRePassText;
        authEmailText = findViewById(R.id.authEmailText);
        newEmailText = findViewById(R.id.newEmailText);
        authPassText = findViewById(R.id.authPassText);
        authRePassText = findViewById(R.id.authRePassText);

        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {
            String authEmail = authEmailText.getText().toString();
            String newEmail = newEmailText.getText().toString();
            String authPass = authPassText.getText().toString();
            String authRePass = authRePassText.getText().toString();
            if (TextUtils.isEmpty(authEmail)) {
                Toast.makeText(this, "Pakilagay ang iyong email!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(newEmail)) {
                Toast.makeText(this, "Pakilagay ang bagong email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(authPass)) {
                Toast.makeText(this, "Pakilagay ang iyong password!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(authRePass)) {
                Toast.makeText(this, "Pakilagay ang iyong password uli!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!authPass.matches(authRePass)) {
                Toast.makeText(this, "Hindi magkatugma ang password!", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyPassword(authPass, newEmail);
        });
    }

    private void verifyPassword(String password, String newEmail){
        user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e("VerifyPassword", "Walang user na naka-login.");
            Toast.makeText(this, "Mag-sign in po ulit.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail = user.getEmail(); // Get the user's email from Firebase Auth
        if (currentEmail == null || currentEmail.isEmpty()) {
            Log.e("VerifyPassword", "Hindi mahanap ang email ng kasalukuyang user.");
            Toast.makeText(this, "Email ay hindi natagpuan. Mangyaring makipag-ugnayan sa aming partners.", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(currentEmail, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("VerifyPassword", "Matagumpay na na-beripika ang password");
                        Toast.makeText(this, "Beripikado na ang password!", Toast.LENGTH_SHORT).show();
                        updateEmail(newEmail);
                    } else {
                        Log.e("VerifyPassword", "Hindi matagupay ang beripikasyon ng password.", task.getException());
                        Toast.makeText(this, "Mali ang password. Pakisubukan muli.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateEmail(String newEmail) {
        user = mAuth.getCurrentUser();
        user.updateEmail(newEmail)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("ChangeEmail", "Matagumpay na na-update ang email.");
                        Toast.makeText(this, "Matagumpay na na-update ang email!", Toast.LENGTH_SHORT).show();

                        // Send verification email to the new email
                        user.sendEmailVerification()
                                .addOnCompleteListener(verificationTask -> {
                                    if (verificationTask.isSuccessful()) {
                                        Log.d("ChangeEmail", "Naipadala na ang verification email sa: " + newEmail);
                                        Toast.makeText(this, "Naipadala na ang verification email sa iyong bagong email.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("ChangeEmail", "Hindi matagumpay na naipadala ang verification email.", verificationTask.getException());
                                        Toast.makeText(this, "Hindi naipadala ang verification email. Pakisubukan muli.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Update the email in Firestore
                        updateEmailInFirestore(newEmail);
                        mAuth.signOut();
                        Intent intent = new Intent(this, Landing_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        Log.e("ChangeEmail", "Hindi matagumpay na na-update ang email.", task.getException());
                        Toast.makeText(this, "Hindi na-update ang email. Pakisubukan muli.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateEmailInFirestore(String newEmail) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String role = prefs.getString("user_role", null);
        String collection = getCollection(role);
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection).document(userId)
                .update("Email", newEmail)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FirestoreUpdate", "Email updated in Firestore successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreUpdate", "Failed to update email in Firestore.", e);
                });
    }
    private String getCollection(String role) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String collection;

        switch (role) {
            case "Group Business User (Association)":
                collection = "Farming Association";
                break;
            case "Group Business User (Cooperative)":
                collection = "Farming Cooperatives";
                break;
            case "Individual Business User":
                collection = "Individual Sellers";
                break;
            case "Individual Customer User":
                collection = "Customers";
                break;
            case "Group Customer User":
                collection = "Group Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }
}
