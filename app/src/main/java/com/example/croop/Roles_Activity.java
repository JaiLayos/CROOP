package com.example.croop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.SignUpActivities.Population_Seller_Activity;
import com.example.croop.SignUpActivities.SignUp_Customer_Activity;
import com.example.croop.model.CurrentRole;
import com.example.croop.model.CurrentUsage;
import com.example.croop.model.Customer;
import com.example.croop.singleton.CurrentUsageSingleton;
import com.example.croop.singleton.CurrentUserSingleton;

public class Roles_Activity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_or_customer);
        showTermsAndConditionsDialog();
        initializeComponents();
    }

    private void showTermsAndConditionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        TextView textView = new TextView(this);
        textView.setText("Before signing up for an account on CRO-OP, please read the following Terms and Conditions carefully. By creating an account, you agree to comply with these terms:\n\n" +
                "You must be at least 18 years old to use our platform. Providing accurate and complete information during registration is mandatory, and you are solely responsible for maintaining the confidentiality of your account credentials. You agree not to use the platform for any illegal or unauthorized purposes, including but not limited to fraudulent activities, spamming, or harassing other users. Any content you upload or share must not infringe upon the rights of others, including intellectual property rights. CRO-OP reserves the right to suspend or terminate your account if you violate these terms. We may update these Terms and Conditions from time to time, and it is your responsibility to review them periodically. By continuing to use the platform after any updates, you agree to the revised terms. Your use of CRO-OP is at your own risk, and we are not liable for any damages arising from your use of the platform. These terms are governed by the laws of the Philippines, and any disputes will be resolved in accordance with these laws.\n\n" +
                "By proceeding with the signup process, you acknowledge that you have read, understood, and agreed to these Terms and Conditions.");
        textView.setPadding(80, 40, 80, 40); // Add padding for better readability
        textView.setTextSize(16);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(textView);

        builder.setTitle("Terms and Conditions");
        builder.setView(scrollView);

        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onBackPressed();
                finish();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initializeComponents() {
        CurrentUsage currentUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
        Customer customer = new Customer();
        CurrentRole currentRole = new CurrentRole();
        currentRole.setRole(customer.getRoles());
        String usage = currentUsage.getCurrentUsage();
        Button forSeller = findViewById(R.id.sellerButton);
        Button forCustomer = findViewById(R.id.customerButton);
        forSeller.setOnClickListener(view -> {
            Intent intent = new Intent(Roles_Activity.this, Population_Seller_Activity.class);
            startActivity(intent);
        });
        forCustomer.setOnClickListener(view -> {
            CurrentUserSingleton.getInstance().setCurrentRole(currentRole);
            if(usage == "Sign In"){
                Intent intent = new Intent(Roles_Activity.this, SignIn_Activity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Roles_Activity.this, SignUp_Customer_Activity.class);
                startActivity(intent);
            }

        });
    }
}
