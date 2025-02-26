package com.example.croop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.GroupSellerLanding.Sign_In_Success_Group_Seller;
import com.example.croop.IndividualSellerLanding.Sign_In_Success_Individual_Seller;
import com.example.croop.model.CurrentUsage;
import com.example.croop.singleton.CurrentUsageSingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Landing_Activity extends AppCompatActivity {
    FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.landing_page);
        initializeComponent();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String role = prefs.getString("user_role", null);

            if (role == null) {
                Toast.makeText(this, "Walang nakalog in mangyaring mag-sign in muli.", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                return;
            }
            switch (role) {
                case "Group Business User (Association)":
                case "Group Business User (Cooperative)":{
                    Intent intent = new Intent(Landing_Activity.this, Sign_In_Success_Group_Seller.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Individual Business User": {
                    Intent intent = new Intent(Landing_Activity.this, Sign_In_Success_Individual_Seller.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Individual Customer User": {
                    Intent intent = new Intent(Landing_Activity.this, Home_Individual_Customer_Activity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                case "Group Customer User": {
                    Intent intent = new Intent(Landing_Activity.this, Home_Group_Customer_Activity.class);
                    startActivity(intent);
                    finish();
                    break;
                }
                default:
                    Toast.makeText(Landing_Activity.this, "Invalid role: " + role, Toast.LENGTH_SHORT).show();
                    break;
            }
        }else{
            Toast.makeText(Landing_Activity.this, "Mag-login po ulit.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void initializeComponent() {
        Button signIn = findViewById(R.id.signInButton);
        Button signUp = findViewById(R.id.signUpButton);
        signIn.setOnClickListener( view -> {
            CurrentUsage currentUsage = new CurrentUsage();
            currentUsage.setCurrentUsage("Sign In");
            CurrentUsageSingleton.getInstance().setCurrentUsageSingleton(currentUsage);
            Intent intent = new Intent(Landing_Activity.this, Roles_Activity.class);
            startActivity(intent);
        });
        signUp.setOnClickListener(view -> {
            CurrentUsage currentUsage = new CurrentUsage();
            currentUsage.setCurrentUsage("Sign Up");
            CurrentUsageSingleton.getInstance().setCurrentUsageSingleton(currentUsage);
            Intent intent = new Intent(Landing_Activity.this, Roles_Activity.class);
            startActivity(intent);
        });
    }
}
