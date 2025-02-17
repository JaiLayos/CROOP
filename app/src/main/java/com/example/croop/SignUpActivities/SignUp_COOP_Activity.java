package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellers;
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
            String ageStr = coopPersonAge.getText().toString();
            int age;

            try {
                age = Integer.parseInt(ageStr);
                if (age < 18 || age > 80) {
                    Toast.makeText(this, "Age must be between 18 and 80", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ageStr.length() != 2) {
                    Toast.makeText(this, "Please enter a valid 2-digit age", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show();
                return;
            }

            GroupSellers groupSellers = new GroupSellers();
            groupSellers.setGroupName(coopName.getText().toString());
            groupSellers.setName(coopPersonFirstName.getText().toString() + " " + coopPersonLastName.getText().toString());
            groupSellers.setAge(age);
            groupSellers.setPersonPosition(coopPersonPosition.getText().toString());
            GroupSellersSingleton.getInstance().setGroupSellers(groupSellers);

            Intent intent = new Intent(this, SignUp_COOP_Activity_2.class);
            startActivity(intent);
        });
    }
}
