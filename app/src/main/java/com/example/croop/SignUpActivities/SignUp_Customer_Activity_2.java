package com.example.croop.SignUpActivities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.croop.R;
import com.example.croop.SignIn_Activity;
import com.example.croop.model.Customer;
import com.example.croop.singleton.CustomerSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp_Customer_Activity_2 extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean allowed = false;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;

    EditText house, subdivision, city, region, postCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_cust_2);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        initializeComponents();
        TextView signInHyperlink = findViewById(R.id.signInHyperlink);
        signInHyperlink.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp_Customer_Activity_2.this, SignIn_Activity.class);
            startActivity(intent);
        });
    }
    public void initializeComponents() {
        house = findViewById(R.id.userHouseText);
        subdivision = findViewById(R.id.userSubdivisionText);
        city = findViewById(R.id.userCityText);
        region = findViewById(R.id.userRegionText);
        postCode = findViewById(R.id.userPostalText);
        Button nextButton = findViewById(R.id.nextButton_SIC);

        postCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        restrictPostInput(postCode);

        nextButton.setOnClickListener(view -> {
            if (house.getText().toString().isEmpty() || subdivision.getText().toString().isEmpty() ||
                    city.getText().toString().isEmpty() || region.getText().toString().isEmpty() ||
                    postCode.getText().toString().isEmpty()) {
                Toast.makeText(this, "Paki-fill up ang lahat ng boxes", Toast.LENGTH_SHORT).show();
                return;
            }
            if(allowed){
                getAddressUsingGeocoder(latitude, longitude);
            }else{
                String house_customer = String.valueOf(house.getText());
                String subdivision_customer = String.valueOf(subdivision.getText());
                String city_customer = String.valueOf(city.getText());
                String region_customer = String.valueOf(region.getText());
                String postal_customer = String.valueOf(postCode.getText());

                    Map<String, String> addressMap = new HashMap<>();
                    addressMap.put("City", city_customer);
                    addressMap.put("Country", "Philippines");
                    addressMap.put("House/Street Name", house_customer);
                    addressMap.put("Postal Code", postal_customer);
                    addressMap.put("State/Province/Region", region_customer);
                    addressMap.put("Subdivision/Baranggay", subdivision_customer);
                    Customer customer = CustomerSingleton.getInstance().getCustomer();
                    customer.setAddress(addressMap);
            }
            Intent intent = new Intent(SignUp_Customer_Activity_2.this, SignUp_Customer_Activity_3.class);
            startActivity(intent);
        });
    }

    private void restrictPostInput(EditText postCode) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4);
        postCode.setFilters(filters);
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    allowed = true;
                    return;
                }
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Hindi pinapayagan", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        // If location services are enabled, proceed to get the current location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            getAddressUsingGeocoder(latitude, longitude);
                        } else {
                            Toast.makeText(this, "Hindi makuha ang lokasyon", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Hindi pinayagan ang pahintulot sa lokasyon", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAddressUsingGeocoder(double latitude, double longitude) {
        Geocoder gecode = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = gecode.getFromLocation(latitude, longitude, 1);
            if(addresses != null || !addresses.isEmpty()){
                Address address = addresses.get(0);
                String city_add = address.getLocality();
                String houseNum_add = address.getSubThoroughfare();
                String region_add = address.getSubAdminArea() + ", " + address.getAdminArea();
                String baranggay_add = address.getSubThoroughfare() + " " + address.getThoroughfare();

                house.setText(houseNum_add != null ? houseNum_add : "");
                subdivision.setText(baranggay_add != null ? baranggay_add : "");
                city.setText(city_add != null ? city_add : "");
                region.setText(region_add != null ? region_add : "");

                String postal_permitted = String.valueOf(postCode.getText());
                String house_permitted = String.valueOf(house.getText());
                String subdivision_permitted = String.valueOf(subdivision.getText());
                String city_permitted = String.valueOf(city.getText());
                String region_permitted = String.valueOf(region.getText());

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city_permitted);
                addressMap.put("Country", "Philippines");
                addressMap.put("House/Street Name", house_permitted);
                addressMap.put("Postal Code", postal_permitted);
                addressMap.put("State/Province/Region", region_permitted);
                addressMap.put("Subdivision/Baranggay", subdivision_permitted);
                Customer customer = CustomerSingleton.getInstance().getCustomer();
                customer.setAddress(addressMap);
            }
        }catch(IOException e){
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
