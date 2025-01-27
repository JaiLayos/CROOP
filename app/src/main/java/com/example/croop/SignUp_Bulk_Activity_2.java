package com.example.croop;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.croop.model.GroupCustomer;
import com.example.croop.singleton.GroupCustomerSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp_Bulk_Activity_2 extends AppCompatActivity {
    EditText bulkHouse, bulkSubdivision, bulkCity, bulkRegion,
    bulkPostal, bulkCountry;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean allowed = false;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_retailer_2);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        initializeComponents();
    }

    private void initializeComponents() {
        bulkHouse = findViewById(R.id.bulkhouseText);
        bulkSubdivision = findViewById(R.id.bulkSubdivisionText);
        bulkCity = findViewById(R.id.bulkCityText);
        bulkRegion = findViewById(R.id.bulkRegionText);
        bulkPostal = findViewById(R.id.bulkPostalText);
        bulkPostal.setInputType(InputType.TYPE_CLASS_NUMBER);
        restrictPostInput(bulkPostal);
        bulkCountry = findViewById(R.id.bulkCountryText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            if(allowed){
                getAddressUsingGeocoder(latitude, longitude);
            }else{
                String house = bulkHouse.getText().toString();
                String subdivision = bulkSubdivision.getText().toString();
                String city = bulkCity.getText().toString();
                String region = bulkRegion.getText().toString();
                String postal = bulkPostal.getText().toString();
                String country = bulkCountry.getText().toString();

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city);
                addressMap.put("Country", country);
                addressMap.put("House/Street Name", house);
                addressMap.put("Postal Code", postal);
                addressMap.put("State/Province/Region", region);
                addressMap.put("Subdivision/Baranggay", subdivision);
                GroupCustomer groupCustomer = GroupCustomerSingleton.getInstance().getGroupCustomer();
                groupCustomer.setAddress(addressMap);

            }
            Intent intent = new Intent(this, SignUp_Bulk_Activity_3.class);
            startActivity(intent);
        });

    }

    private void getAddressUsingGeocoder(double latitude, double longitude) {
        Geocoder gecode = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = gecode.getFromLocation(latitude, longitude, 1);
            if(addresses != null || !addresses.isEmpty()){
                Address address = addresses.get(0);
                String city_add = address.getLocality();
                String country_add = address.getCountryName();
                String houseNum_add = address.getSubThoroughfare();
                String region_add = address.getSubAdminArea() + ", " + address.getAdminArea();
                String baranggay_add = address.getSubThoroughfare() + " " + address.getThoroughfare();

                bulkHouse.setText(houseNum_add != null ? houseNum_add : "");
                bulkSubdivision.setText(baranggay_add != null ? baranggay_add : "");
                bulkCity.setText(city_add != null ? city_add : "");
                bulkRegion.setText(region_add != null ? region_add : "");
                bulkCountry.setText(country_add != null ? country_add : "");

                String postal_permitted = String.valueOf(bulkPostal.getText());
                String house_permitted = String.valueOf(bulkHouse.getText());
                String subdivision_permitted = String.valueOf(bulkSubdivision.getText());
                String city_permitted = String.valueOf(bulkCity.getText());
                String region_permitted = String.valueOf(bulkRegion.getText());
                String country_permitted = String.valueOf(bulkCountry.getText());

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city_permitted);
                addressMap.put("Country", country_permitted);
                addressMap.put("House/Street Name", house_permitted);
                addressMap.put("Postal Code", postal_permitted);
                addressMap.put("State/Province/Region", region_permitted);
                addressMap.put("Subdivision/Baranggay", subdivision_permitted);
                GroupCustomer groupCustomer = GroupCustomerSingleton.getInstance().getGroupCustomer();
                groupCustomer.setAddress(addressMap);

            }else{
                Toast.makeText(this,"Please input your location manually!", Toast.LENGTH_SHORT).show();
            }

        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void restrictPostInput(EditText coopPostal) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4);
        coopPostal.setFilters(filters);
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
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        // If location services are enabled, proceed to get the current location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            getAddressUsingGeocoder(latitude, longitude);
                        } else {
                            Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

}
