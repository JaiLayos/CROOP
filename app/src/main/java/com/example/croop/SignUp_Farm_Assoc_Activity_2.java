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

import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.GroupSellersSingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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
    private FusedLocationProviderClient fusedLocationProviderClient;
    EditText assocHouse, assocBaranggay, assocCity, assocRegion, assocPostCode, assocCountry;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_farm_assoc_2);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();
        initializeComponents();
    }

    private void initializeComponents() {
        assocHouse = findViewById(R.id.assocHouseText);
        assocBaranggay = findViewById(R.id.assocSubdivisionText);
        assocCity = findViewById(R.id.assocCityText);
        assocRegion = findViewById(R.id.assocRegionText);
        assocPostCode = findViewById(R.id.assocPostalText);
        assocPostCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        restrictPostInput(assocPostCode);
        assocCountry = findViewById(R.id.assocCountryText);
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener(view -> {
            if (assocHouse.getText().toString().isEmpty() || assocBaranggay.getText().toString().isEmpty() ||
                    assocCity.getText().toString().isEmpty() || assocRegion.getText().toString().isEmpty() ||
                    assocPostCode.getText().toString().isEmpty() || assocCountry.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if(allowed){
                getAddressUsingGeocoder(latitude, longitude);
            }else{
                String house_customer = String.valueOf(assocHouse.getText());
                String subdivision_customer = String.valueOf(assocBaranggay.getText());
                String city_customer = String.valueOf(assocCity.getText());
                String region_customer = String.valueOf(assocRegion.getText());
                String postal_customer = String.valueOf(assocPostCode.getText());
                String country_customer = String.valueOf(assocCountry.getText());

                    Map<String, String> addressMap = new HashMap<>();
                    addressMap.put("City", city_customer);
                    addressMap.put("Country", country_customer);
                    addressMap.put("House_Street_Name", house_customer);
                    addressMap.put("Postal_Code", postal_customer);
                    addressMap.put("State_Province_Region", region_customer);
                    addressMap.put("Subdivision_Baranggay", subdivision_customer);
                    GroupSellers gSellers = GroupSellersSingleton.getInstance().getGroupSellers();
                    gSellers.setAddress(addressMap);

            }
            Intent intent = new Intent(this, SignUp_Farm_Assoc_Activity_3.class);
            startActivity(intent);
        });
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

                assocHouse.setText(houseNum_add != null ? houseNum_add : "");
                assocBaranggay.setText(baranggay_add != null ? baranggay_add : "");
                assocCity.setText(city_add != null ? city_add : "");
                assocRegion.setText(region_add != null ? region_add : "");
                assocCountry.setText(country_add != null ? country_add : "");

                String postal_permitted = String.valueOf(assocPostCode.getText());
                String house_permitted = String.valueOf(assocHouse.getText());
                String subdivision_permitted = String.valueOf(assocBaranggay.getText());
                String city_permitted = String.valueOf(assocCity.getText());
                String region_permitted = String.valueOf(assocRegion.getText());
                String country_permitted = String.valueOf(assocCountry.getText());

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
        } catch (IOException e) {
            Toast.makeText(this, "Please input location manually.", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void restrictPostInput(EditText postCode) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4);
        postCode.setFilters(filters);
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
