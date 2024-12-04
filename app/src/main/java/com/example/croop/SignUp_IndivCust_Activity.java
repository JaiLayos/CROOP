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

import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;

public class SignUp_IndivCust_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust);
        enableLocation();
        initializeComponents();
    }

    private void initializeComponents() {
        Button nextButton = findViewById(R.id.nextButton);
        EditText firstName = findViewById(R.id.coopNameText);
        EditText lastName = findViewById(R.id.lastNameText);
        validName(firstName);
        validName(lastName);

        nextButton.setOnClickListener(view ->{
            String first_name_customer = String.valueOf(firstName.getText());
            String last_name_customer = String.valueOf(lastName.getText());
            Customer customer = new Customer();
            customer.setCust_Name(first_name_customer + " " + last_name_customer);
            CustomerSingleton.getInstance().setCustomer(customer);
            Intent intent = new Intent(SignUp_IndivCust_Activity.this, SignUp_IndivCust_Activity_2.class);
            startActivity(intent);
        });
    }

    private void validName(EditText name) {
        InputFilter filter = new InputFilter(){
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.toString().matches("[a-zA-Z\\s]*")) {
                    return null; // Allow the input
                } else {
                    Toast.makeText(SignUp_IndivCust_Activity.this, "Please input a valid name.",Toast.LENGTH_SHORT).show();
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