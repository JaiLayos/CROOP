package com.example.croop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType; // import statement for password
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.Roles_Activity;

import com.example.croop.GroupSellerLanding.Sign_In_Success_Group_Seller;
import com.example.croop.model.CurrentRole;
import com.example.croop.singleton.CurrentUserSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

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

        TextView signUpHyperlink = findViewById(R.id.signInHyperlink9);
        signUpHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignIn_Activity.this, Roles_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        EditText emailText, passwordText;
        emailText = findViewById(R.id.emailLoginText);
        passwordText = findViewById(R.id.passwordLoginText);
        passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        Button next = findViewById(R.id.signInButton);
        next.setOnClickListener(view -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                String uid = user.getUid(); // Get unique user ID
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                String collect_role = getCollection(role);
                                db.collection(collect_role).document(uid)
                                        .get()
                                        .addOnSuccessListener(document -> {
                                            if (document.exists()) {
                                                String role_firestore = document.getString("Role");
                                                if (role_firestore != null) {
                                                    SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                                                    prefs.edit().putString("user_role", role_firestore).apply();
                                                    navigateToHome(role_firestore);
                                                }else{
                                                    Toast.makeText(SignIn_Activity.this, "Ang user role ay null", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(SignIn_Activity.this, "Collection: "+ collect_role+ " not found", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(SignIn_Activity.this, "Hindi nakuha ang datos ng user", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(SignIn_Activity.this, "Hindi ka nakapag-authenticate.", Toast.LENGTH_SHORT).show();
                        }


                    });
        });
    }

    public void navigateToHome(String role){
        switch (role) {
            case "Group Business User (Association)":
            case "Group Business User (Cooperative)":{
                Intent intent = new Intent(SignIn_Activity.this, Sign_In_Success_Group_Seller.class);
                startActivity(intent);
                finish();
                break;
            }
            case "Individual Business User": {
                Intent intent = new Intent(SignIn_Activity.this, Home_Individual_Seller_Activity.class);
                startActivity(intent);
                finish();
                break;
            }
            case "Individual Customer User": {
                Intent intent = new Intent(SignIn_Activity.this, Home_Individual_Customer_Activity.class);
                startActivity(intent);
                finish();
                break;
            }
            case "Group Customer User": {
                Intent intent = new Intent(SignIn_Activity.this, Home_Group_Customer_Activity.class);
                startActivity(intent);
                finish();
                break;
            }
            default:
                Toast.makeText(SignIn_Activity.this, "Invalid role: " + role, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String getCollection(String role) {
        switch (role) {
            case "Group Business User (Association)":
                return "Farming Association";
            case "Group Business User (Cooperative)":
                return "Farming Cooperatives";
            case "Individual Business User":
                return "Individual Sellers";
            case "Individual Customer User":
                return "Customers";
            case "Group Customer User":
                return "Group Customers";
            default:
                return "Unknown";
        }
    }
}
