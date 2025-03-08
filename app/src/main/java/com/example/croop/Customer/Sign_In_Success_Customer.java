package com.example.croop.Customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.croop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign_In_Success_Customer extends AppCompatActivity {
    private ViewPager2 viewPager;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header_customer_home);

        viewPager = findViewById(R.id.viewPagerContainer);
        viewPager.setUserInputEnabled(false);
        TabLayout tabLayout = findViewById(R.id.tabs);
        IndividualCustomerViewAdapter adapter = new IndividualCustomerViewAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Home");
                    break;
                case 1:
                    tab.setText("Products");
                    break;
                case 2:
                    tab.setText("Sellers");
                    break;
                case 3:
                    tab.setText("Cart");
                    break;
                case 4:
                    tab.setText("Orders");
                    break;
                case 5:
                    tab.setText("Account");
                    break;
            }
        }).attach();

        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String role = prefs.getString("user_role", null);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String collection = getCollection(role);

        DocumentReference docRef = db.collection(collection).document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Toast.makeText(Sign_In_Success_Customer.this, "Hello! " + role + ", " + documentSnapshot.getString("Name"), Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        TextView greetings = findViewById(R.id.greetingsText);
                        greetings.setText("WELCOME! " + document.getString("Name").toUpperCase());
                    }
                }
            }
        });
    }
    private String getCollection(String role) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String collection;

        switch (role) {
            case "Group Business User (Association)":
                collection = "Farming Association";
                break;
            case "Group Business User (Cooperative)":
                collection = "Farming Cooperatives";
                break;
            case "Individual Business User":
                collection = "Individual Sellers";
                break;
            case "Customer User":
                collection = "Customers";
                break;
            case "Group Customer User":
                collection = "Group Customers";
                break;
            default:
                collection = "Unknown";
                break;
        }

        editor.putString("user_collection", collection).apply();
        return collection;
    }

}