package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentUsage;
import com.example.croop.singleton.CurrentUsageSingleton;

public class Landing_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        initializeComponent();
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
