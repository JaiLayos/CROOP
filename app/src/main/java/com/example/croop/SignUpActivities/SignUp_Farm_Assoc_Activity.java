package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.CurrentRole;
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.CurrentUserSingleton;
import com.example.croop.singleton.GroupSellersSingleton;

public class SignUp_Farm_Assoc_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc);
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink4);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Farm_Assoc_Activity.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }

    private void initializeComponents() {
        Button nextButton = findViewById(R.id.nextButton_SUFA);
        EditText association_name = findViewById(R.id.assocNameText);
        EditText pointPerson_FirstName = findViewById(R.id.assocFirstNameText);
        EditText pointPerson_LastName = findViewById(R.id.assocLastNameText);
        EditText pointPerson_Position = findViewById(R.id.assocPositionText);

        pointPerson_FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
        pointPerson_LastName.setInputType(InputType.TYPE_CLASS_TEXT);

        nextButton.setOnClickListener(view ->{
            String association = String.valueOf(association_name.getText());
            String first_name_point_person = String.valueOf(pointPerson_FirstName.getText());
            String last_name_point_person = String.valueOf(pointPerson_LastName.getText());
            String position_point_person = String.valueOf(pointPerson_Position.getText());

            GroupSellers seller = new GroupSellers();
            seller.setName(first_name_point_person + " " + last_name_point_person);
            seller.setGroupName(association);
            seller.setPersonPosition(position_point_person);
            GroupSellersSingleton.getInstance().setGroupSellers(seller);
            CurrentRole currentRole = new CurrentRole();
            currentRole.setRole(seller.returnRole_assoc());
            CurrentUserSingleton.getInstance().setCurrentRole(currentRole);
            Intent intent = new Intent(SignUp_Farm_Assoc_Activity.this, SignUp_Farm_Assoc_Activity_2.class);
            startActivity(intent);
        });
    }

}
