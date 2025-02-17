package com.example.croop.GroupSellerLanding;

import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.*;

public class Activity_Edit_Profile extends AppCompatActivity {
    EditText userName, userBio, userEmail, userPhoneNumber, userAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_group_seller);
        initializeComponents();
    }

    private void initializeComponents() {

    }
}
