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
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.GroupCustomerSingleton;
import com.example.croop.singleton.GroupSellersSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp_COOP_Activity_2 extends AppCompatActivity {
    EditText coopHouse, coopSubdivision, coopCity,
            coopRegion, coopPostal, coopCountry;

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean allowed = false;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.signup_coop_2);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        initializeComponents();
    }

    private void initializeComponents() {
        coopHouse = findViewById(R.id.coopHouseText);
        coopSubdivision = findViewById(R.id.coopSubdivisionText);
        coopCity = findViewById(R.id.coopCityText);
        coopRegion = findViewById(R.id.coopRegionText);
        coopPostal = findViewById(R.id.coopPostalText);
        coopPostal.setInputType(InputType.TYPE_CLASS_NUMBER);
        restrictPostInput(coopPostal);
        coopCountry = findViewById(R.id.coopCountryText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            if (coopHouse.getText().toString().isEmpty() || coopSubdivision.getText().toString().isEmpty() ||
                    coopCity.getText().toString().isEmpty() || coopRegion.getText().toString().isEmpty() ||
                    coopPostal.getText().toString().isEmpty() || coopCountry.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if(allowed){
                getAddressUsingGeocoder(latitude, longitude);
            }else{
                String house = coopHouse.getText().toString();
                String subdivision = coopSubdivision.getText().toString();
                String city = coopCity.getText().toString();
                String region = coopRegion.getText().toString();
                String postal = coopPostal.getText().toString();
                String country = coopCountry.getText().toString();

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
            Intent intent = new Intent(this, SignUp_COOP_Activity_3.class);
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

                coopHouse.setText(houseNum_add != null ? houseNum_add : "");
                coopSubdivision.setText(baranggay_add != null ? baranggay_add : "");
                coopCity.setText(city_add != null ? city_add : "");
                coopRegion.setText(region_add != null ? region_add : "");
                coopCountry.setText(country_add != null ? country_add : "");

                String postal_permitted = String.valueOf(coopPostal.getText());
                String house_permitted = String.valueOf(coopHouse.getText());
                String subdivision_permitted = String.valueOf(coopSubdivision.getText());
                String city_permitted = String.valueOf(coopCity.getText());
                String region_permitted = String.valueOf(coopRegion.getText());
                String country_permitted = String.valueOf(coopCountry.getText());

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city_permitted);
                addressMap.put("Country", country_permitted);
                addressMap.put("House/Street Name", house_permitted);
                addressMap.put("Postal Code", postal_permitted);
                addressMap.put("State/Province/Region", region_permitted);
                addressMap.put("Subdivision/Baranggay", subdivision_permitted);
                GroupSellers groupSellers = GroupSellersSingleton.getInstance().getGroupSellers();
                groupSellers.setAddress(addressMap);

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
