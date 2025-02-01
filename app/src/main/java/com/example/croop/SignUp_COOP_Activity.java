package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.GroupSellersSingleton;

public class SignUp_COOP_Activity extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_coop);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText coopName, coopPersonFirstName, coopPersonLastName,
                coopPersonAge, coopPersonPosition;
        coopName = findViewById(R.id.coopNameText);
        coopPersonFirstName = findViewById(R.id.coopPersonFirstNameText);
        coopPersonLastName = findViewById(R.id.coopPersonLastNameText);
        coopPersonAge = findViewById(R.id.coopPersonAgeText);
        coopPersonPosition = findViewById(R.id.coopPersonPositionText);



        Button next = findViewById(R.id.nextButton_SUC);
        next.setOnClickListener(view -> {
            int age = Integer.parseInt(coopPersonAge.getText().toString());
            if (age < 18 || age > 80) {
                Toast.makeText(this, "Age must be between 18 and 80", Toast.LENGTH_SHORT).show();
                return;
            }

            GroupSellers groupSellers = new GroupSellers();
            groupSellers.setGroupName(coopName.getText().toString());
            groupSellers.setName(coopPersonFirstName.getText().toString() + " " + coopPersonLastName.getText().toString());
            groupSellers.setAge(age);

            CurrentRole cr = new CurrentRole();
            cr.setRole(groupSellers.returnRole());
            CurrentUserSingleton.getInstance().setCurrentRole(cr);

            Intent intent = new Intent(this, SignUp_COOP_Activity_2.class);
            startActivity(intent);
        });
    }
}
