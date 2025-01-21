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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.croop.model.Customer;
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.CustomerSingleton;
import com.example.croop.singleton.GroupSellersSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SignUp_Farm_Assoc_Activity_2 extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean allowed = false;
    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    EditText house, baranggay, city, region, postCode, country;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc_2);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        initializeComponents();
    }

    private void initializeComponents() {
        house = findViewById(R.id.assocHouseText);
        baranggay = findViewById(R.id.assocSubdivisionText);
        city = findViewById(R.id.assocCityText);
        region = findViewById(R.id.assocRegionText);
        postCode = findViewById(R.id.assocPostalText);
        country = findViewById(R.id.assocCountryText);
        Button nextButton = findViewById(R.id.nextButton);

        postCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        restrictPostInput(postCode);

        nextButton.setOnClickListener(view -> {
            if(allowed){
                getAddressUsingGeocoder(latitude, longitude);
            }else{
                String house_customer = String.valueOf(house.getText());
                String subdivision_customer = String.valueOf(baranggay.getText());
                String city_customer = String.valueOf(city.getText());
                String region_customer = String.valueOf(region.getText());
                String postal_customer = String.valueOf(postCode.getText());
                String country_customer = String.valueOf(country.getText());

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city_customer);
                addressMap.put("Country", country_customer);
                addressMap.put("House/Street Name", house_customer);
                addressMap.put("Postal Code", postal_customer);
                addressMap.put("State/Province/Region", region_customer);
                addressMap.put("Subdivision/Baranggay", subdivision_customer);
                GroupSellers gSellers = GroupSellersSingleton.getInstance().getGroupSellers();
                gSellers.setAddress(addressMap);
            }

            Intent intent = new Intent(SignUp_Farm_Assoc_Activity_2.this, SignUp_IndivCust_Activity_3.class);
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
                getCurrentLocation();  // Retry fetching location after permission is granted
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            getAddressUsingGeocoder(latitude, longitude);
                        } else {
                            Toast.makeText(this, "Location is not available. Trying to refresh...", Toast.LENGTH_SHORT).show();
                            // Retry getting the location
                            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                                    .addOnSuccessListener(location2 -> {
                                        if (location2 != null) {
                                            latitude = location2.getLatitude();
                                            longitude = location2.getLongitude();
                                            getAddressUsingGeocoder(latitude, longitude);
                                        } else {
                                            Toast.makeText(this, "Unable to fetch location. Please check your GPS settings.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        } else {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    private void getAddressUsingGeocoder(double latitude, double longitude) {
        Geocoder gecode = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = gecode.getFromLocation(latitude, longitude, 1);
            if (addresses != null || !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String city_add = address.getLocality();
                String country_add = address.getCountryName();
                String houseNum_add = address.getSubThoroughfare();
                String region_add = address.getSubAdminArea() + ", " + address.getAdminArea();
                String baranggay_add = address.getSubThoroughfare() + " " + address.getThoroughfare();

                house.setText(houseNum_add != null ? houseNum_add : "");
                baranggay.setText(baranggay_add != null ? baranggay_add : "");
                city.setText(city_add != null ? city_add : "");
                region.setText(region_add != null ? region_add : "");
                country.setText(country_add != null ? country_add : "");

                String postal_permitted = String.valueOf(postCode.getText());
                String house_permitted = String.valueOf(house.getText());
                String subdivision_permitted = String.valueOf(baranggay.getText());
                String city_permitted = String.valueOf(city.getText());
                String region_permitted = String.valueOf(region.getText());
                String country_permitted = String.valueOf(country.getText());

                Map<String, String> addressMap = new HashMap<>();
                addressMap.put("City", city_permitted);
                addressMap.put("Country", country_permitted);
                addressMap.put("House/Street Name", house_permitted);
                addressMap.put("Postal Code", postal_permitted);
                addressMap.put("State/Province/Region", region_permitted);
                addressMap.put("Subdivision/Baranggay", subdivision_permitted);
                Customer customer = CustomerSingleton.getInstance().getCustomer();
                customer.setAddress(addressMap);
            }
        } catch (IOException e) {
                Toast.makeText(this, "Please input location manually.", Toast.LENGTH_SHORT).show();
        }
    }
}
