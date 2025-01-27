package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.GroupCustomer;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.GroupCustomerSingleton;

public class SignUp_Bulk_Activity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_retailer);
        initializeComponents();
    }

    private void initializeComponents() {
        EditText bulkGroupName = findViewById(R.id.bulkGroupNameText);
        EditText bulkFirstName = findViewById(R.id.bulkPersonFirstNameText);
        EditText bulkLastName = findViewById(R.id.bulkPersonLastNameText);
        EditText bulkAge = findViewById(R.id.bulkPersonAgeText);
        EditText bulkPosition = findViewById(R.id.bulkPersonPositionText);

        bulkFirstName.setInputType(InputType.TYPE_CLASS_TEXT);
        bulkLastName.setInputType(InputType.TYPE_CLASS_TEXT);
        bulkAge.setInputType(InputType.TYPE_CLASS_NUMBER);

        Button next = findViewById(R.id.nextButton_SUC);
        next.setOnClickListener(view -> {
            String groupName = bulkGroupName.getText().toString();
            String firstName = bulkFirstName.getText().toString();
            String lastName = bulkLastName.getText().toString();
            int age = Integer.parseInt(bulkAge.getText().toString());
            String position = bulkPosition.getText().toString();

            GroupCustomer groupCustomer = new GroupCustomer();
            groupCustomer.setGroupName(groupName);
            groupCustomer.setName(firstName + " " + lastName);
            groupCustomer.setAge(age);
            groupCustomer.setPosition(position);

            CurrentRole currentRole = new CurrentRole();
            currentRole.setRole(groupCustomer.returnRole());
            CurrentUserSingleton.getInstance().setCurrentRole(currentRole);
            GroupCustomerSingleton.getInstance().setGroupCustomer(groupCustomer);

            Intent intent = new Intent(this, SignUp_Bulk_Activity_2.class);
            startActivity(intent);
        });

    }
}
