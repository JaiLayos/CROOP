package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.GroupSellersSingleton;

public class SignUp_COOP_Activity extends AppCompatActivity {

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_coop);
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink4);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_COOP_Activity.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        EditText coopName, coopPersonFirstName, coopPersonLastName, coopPersonPosition;
        coopName = findViewById(R.id.coopNameText);
        coopPersonFirstName = findViewById(R.id.coopPersonFirstNameText);
        coopPersonLastName = findViewById(R.id.coopPersonLastNameText);
        coopPersonPosition = findViewById(R.id.coopPersonPositionText);

        Button next = findViewById(R.id.nextButton_SUC);
        next.setOnClickListener(view -> {

            GroupSellers groupSellers = new GroupSellers();
            groupSellers.setGroupName(coopName.getText().toString());
            groupSellers.setName(coopPersonFirstName.getText().toString() + " " + coopPersonLastName.getText().toString());
            groupSellers.setPersonPosition(coopPersonPosition.getText().toString());
            GroupSellersSingleton.getInstance().setGroupSellers(groupSellers);

            Intent intent = new Intent(this, SignUp_COOP_Activity_2.class);
            startActivity(intent);
        });
    }
}
