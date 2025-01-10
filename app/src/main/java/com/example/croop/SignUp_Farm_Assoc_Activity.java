package com.example.croop;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.GroupSellersSingleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp_Farm_Assoc_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc);
        initializeComponents();
    }

    private void initializeComponents() {
        Button nextButton = findViewById(R.id.nextButton4);
        EditText association_name = findViewById(R.id.assocNameText);
        EditText pointPerson_FirstName = findViewById(R.id.a_pPersonFirstNameText);
        EditText pointPerson_LastName = findViewById(R.id.a_pPersonLastNameText);
        EditText pointPerson_Age = findViewById(R.id.a_pPersonAgeText);
        EditText pointPerson_PhoneNumber = findViewById(R.id.a_ppPhoneNumberText);

        validName(pointPerson_FirstName);
        validName(pointPerson_LastName);

        nextButton.setOnClickListener(view ->{
            String association = String.valueOf(association_name);
            String first_name_point_person = String.valueOf(pointPerson_FirstName.getText());
            String last_name_point_person = String.valueOf(pointPerson_LastName.getText());
            String phone_number_point_person = String.valueOf(pointPerson_PhoneNumber.getText());
            int age_point_person = Integer.parseInt(String.valueOf(pointPerson_Age));

            if(phoneNumberValidation(phone_number_point_person)){
                GroupSellers seller = new GroupSellers();
                seller.setName(first_name_point_person + " " + last_name_point_person);
                seller.setGroupName(association);
                seller.setAge(age_point_person);
                GroupSellersSingleton.getInstance().setGroupSellers(seller);
                Intent intent = new Intent(SignUp_Farm_Assoc_Activity.this, SignUp_Farm_Assoc_Activity_2.class);
                startActivity(intent);
            }else{
                if(!phoneNumberValidation(phone_number_point_person)) {
                    Toast.makeText(SignUp_Farm_Assoc_Activity.this,"Wrong Input",Toast.LENGTH_SHORT).show();
                }
            };


        });
    }

    private boolean phoneNumberValidation(String phoneNumber) {
        Pattern p = Pattern.compile("^\\d{11}$");
        Matcher m = p.matcher(phoneNumber);
        return(m.matches());
    }

    //Valid Name is to ensure that the input will only be texts
    private void validName(EditText name) {
        InputFilter filter = new InputFilter(){
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().matches("[a-zA-Z\\s]*")) {
                    return null; // Allow the input
                } else {
                    Toast.makeText(SignUp_Farm_Assoc_Activity.this, "Please input a valid name.",Toast.LENGTH_SHORT).show();
                    return ""; // Reject any non-alphabetic characters
                }
            }
        };

        name.setFilters(new InputFilter[]{filter});
    }

    private void enableLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null && !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // If location services are disabled, prompt the user to enable it
            Toast.makeText(this, "Location services are disabled. Please enable them.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    }
}
