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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.croop.R;
import com.example.croop.model.IndividualSellers;
import com.example.croop.singleton.IndividualSellersSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp_Indiv_Farmer_Activity_2 extends AppCompatActivity {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean allowed = false;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;

    EditText houseText, subdivisionText, cityText, regionText, postCodeText, countryText;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_indiv_farmer_2);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        initializeComponents();
    }

    private void initializeComponents() {
        houseText = findViewById(R.id.indivFarmHouseText);
        subdivisionText = findViewById(R.id.indivFarmSubdivisionText);
        cityText = findViewById(R.id.indivFarmCityText);
        regionText = findViewById(R.id.indivFarmRegionText);
        postCodeText = findViewById(R.id.indivFarmPostalText);
        postCodeText.setInputType(InputType.TYPE_CLASS_NUMBER);
        restrictPostInput(postCodeText);
        countryText = findViewById(R.id.indivFarmCountryText);
        Button next = findViewById(R.id.nextButton);
        next.setOnClickListener(view -> {
            if (houseText.getText().toString().isEmpty() || subdivisionText.getText().toString().isEmpty() ||
                    cityText.getText().toString().isEmpty() || regionText.getText().toString().isEmpty() ||
                    postCodeText.getText().toString().isEmpty() || countryText.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if(allowed){
                getAddressUsingGeocoder(latitude, longitude);
            }else{
                String house = houseText.getText().toString();
                String subdivision = subdivisionText.getText().toString();
                String city = cityText.getText().toString();
                String region = regionText.getText().toString();
                String postal = postCodeText.getText().toString();
                String country = countryText.getText().toString();

                    Map<String, String> addressMap = new HashMap<>();
                    addressMap.put("City", city);
                    addressMap.put("Country", country);
                    addressMap.put("House/Street Name", house);
                    addressMap.put("Postal Code", postal);
                    addressMap.put("State/Province/Region", region);
                    addressMap.put("Subdivision/Baranggay", subdivision);
                    IndividualSellers indivFarmer = IndividualSellersSingleton.getInstance().getIndividualSellers();
                    indivFarmer.setAddress(addressMap);

            }
            Intent intent = new Intent(SignUp_Indiv_Farmer_Activity_2.this, SignUp_Indiv_Farmer_Activity_3.class);
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

                houseText.setText(houseNum_add != null ? houseNum_add : "");
                subdivisionText.setText(baranggay_add != null ? baranggay_add : "");
                cityText.setText(city_add != null ? city_add : "");
                regionText.setText(region_add != null ? region_add : "");
                countryText.setText(country_add != null ? country_add : "");

                String postal_permitted = String.valueOf(postCodeText.getText());
                String house_permitted = String.valueOf(houseText.getText());
                String subdivision_permitted = String.valueOf(subdivisionText.getText());
                String city_permitted = String.valueOf(cityText.getText());
                String region_permitted = String.valueOf(regionText.getText());
                String country_permitted = String.valueOf(countryText.getText());

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city_permitted);
                addressMap.put("Country", country_permitted);
                addressMap.put("House/Street Name", house_permitted);
                addressMap.put("Postal Code", postal_permitted);
                addressMap.put("State/Province/Region", region_permitted);
                addressMap.put("Subdivision/Baranggay", subdivision_permitted);
                IndividualSellers indivFarmer = IndividualSellersSingleton.getInstance().getIndividualSellers();
                indivFarmer.setAddress(addressMap);

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

    private void restrictPostInput(EditText postCode){
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4);
        postCode.setFilters(filters);
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
