package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
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
    }

    private void initializeComponents() {
        Button nextButton = findViewById(R.id.nextButton_SUFA);
        EditText association_name = findViewById(R.id.assocNameText);
        EditText pointPerson_FirstName = findViewById(R.id.assocFirstNameText);
        EditText pointPerson_LastName = findViewById(R.id.assocLastNameText);
        EditText pointPerson_Age = findViewById(R.id.assocAgeText);
        EditText pointPerson_Position = findViewById(R.id.assocPositionText);

        pointPerson_FirstName.setInputType(InputType.TYPE_CLASS_TEXT);
        pointPerson_LastName.setInputType(InputType.TYPE_CLASS_TEXT);
        pointPerson_Age.setInputType(InputType.TYPE_CLASS_NUMBER);

        nextButton.setOnClickListener(view ->{
            String association = String.valueOf(association_name.getText());
            String first_name_point_person = String.valueOf(pointPerson_FirstName.getText());
            String last_name_point_person = String.valueOf(pointPerson_LastName.getText());
            String position_point_person = String.valueOf(pointPerson_Position.getText());
            String ageStr = String.valueOf(pointPerson_Age.getText());
            int age_point_person;

            try {
                age_point_person = Integer.parseInt(ageStr);
                if (age_point_person < 18 || age_point_person > 80) {
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

            GroupSellers seller = new GroupSellers();
            seller.setName(first_name_point_person + " " + last_name_point_person);
            seller.setGroupName(association);
            seller.setAge(age_point_person);
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
