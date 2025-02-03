package com.example.croop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.model.CurrentRole;
import com.example.croop.model.CurrentUsage;
import com.example.croop.model.GroupSellers;
import com.example.croop.singleton.CurrentUsageSingleton;
import com.example.croop.singleton.CurrentUserSingleton;

public class Group_Seller_Activity extends AppCompatActivity {
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.assoc_or_coop);
        initializeComponent();
    }

    private void initializeComponent() {
        Button coop = findViewById(R.id.groupCoopButton);
        Button assoc = findViewById(R.id.groupAssocButton);
        coop.setOnClickListener(view -> {
            GroupSellers groupSellers = new GroupSellers();
            CurrentRole cr = new CurrentRole();
            cr.setRole(groupSellers.returnRole());
            CurrentUserSingleton.getInstance().setCurrentRole(cr);
            CurrentUsage currentUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
            String cU = currentUsage.getCurrentUsage();
            switch (cU){
                case "Sign Up":
                    Intent intent = new Intent(this, SignUp_COOP_Activity.class);
                    startActivity(intent);
                    break;
                case "Sign In":
                    Intent intent_1 = new Intent(this, SignIn_Activity.class);
                    startActivity(intent_1);
                    break;
            }
        });
        assoc.setOnClickListener(view -> {
            GroupSellers groupSellers = new GroupSellers();
            CurrentRole cr = new CurrentRole();
            cr.setRole(groupSellers.returnRole());
            CurrentUserSingleton.getInstance().setCurrentRole(cr);
            CurrentUsage currentUsage = CurrentUsageSingleton.getInstance().getCurrentUsageSingleton();
            String cU = currentUsage.getCurrentUsage();
            switch(cU){
                case "Sign Up":
                    Intent intent = new Intent(this, SignUp_Farm_Assoc_Activity.class);
                    startActivity(intent);
                    break;
                case "Sign In":
                    Intent intent_1 = new Intent(this, SignIn_Activity.class);
                    startActivity(intent_1);
                    break;
            }
        });
    }
}
