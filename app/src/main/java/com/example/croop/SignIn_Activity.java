package com.example.croop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.singleton.CurrentUserSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn_Activity extends AppCompatActivity {
    FirebaseAuth mAuth;

    CurrentRole cr = CurrentUserSingleton.getInstance().getCurrentRole();
    String role = cr.getRole();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.signin);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText emailText, passwordText;
        emailText = findViewById(R.id.emailLoginText);
        passwordText = findViewById(R.id.passwordLoginText);

        Button next = findViewById(R.id.signInButton);
        next.setOnClickListener(view -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignIn_Activity.this, "Login successful! " + role, Toast.LENGTH_SHORT).show();
                            switch (role) {
                                case "Group Business User": {
                                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    prefs.edit().putString("user_role", role).apply();
                                    Intent intent = new Intent(SignIn_Activity.this, Home_Group_Seller_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case "Individual Business User": {
                                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    prefs.edit().putString("user_role", role).apply();
                                    Intent intent = new Intent(SignIn_Activity.this, Home_Individual_Seller_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case "Individual Customer User": {
                                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    prefs.edit().putString("user_role", role).apply();
                                    Intent intent = new Intent(SignIn_Activity.this, Home_Individual_Customer_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                case "Group Customer User": {
                                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                    prefs.edit().putString("user_role", role).apply();
                                    Intent intent = new Intent(SignIn_Activity.this, Home_Group_Customer_Activity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                default:
                                    Toast.makeText(SignIn_Activity.this, "Invalid role: " + role, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        } else {
                            Toast.makeText(SignIn_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
