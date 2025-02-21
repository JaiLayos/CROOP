package com.example.croop.GroupSellerLanding;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.croop.R;
import com.example.croop.model.GroupSellersDiscount;
import com.example.croop.retrofit.RetrofitService;
import com.example.croop.retrofit.UserAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Discount_Inventory extends AppCompatActivity {
    private TableLayout table;
    FirebaseAuth mAuth;
    RetrofitService RetrofitClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discounts_group_seller);
        initializeComponents();
    }

    private void initializeComponents() {
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        table = findViewById(R.id.tableLayout);

        Call<List<GroupSellersDiscount>> call = apiService.getDiscountbyFirebaseID(user.getUid());
        call.enqueue(new Callback<List<GroupSellersDiscount>>() {
            @Override
            public void onResponse(Call<List<GroupSellersDiscount>> call, Response<List<GroupSellersDiscount>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GroupSellersDiscount> discounts = response.body();
                    Toast.makeText(Activity_Discount_Inventory.this, "Number of orders fetched: " + discounts.size(), Toast.LENGTH_SHORT).show();
                    populateTableDefault(discounts,table);
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                        Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                        Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GroupSellersDiscount>> call, Throwable t) {
                Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
        Button find, add;
        find = findViewById(R.id.findItemButton);
        find.setOnClickListener(v -> {
            EditText itemNameFind = findViewById(R.id.itemNameFindText);
            String itemName = itemNameFind.getText().toString();
            Call<List<GroupSellersDiscount>> searchItem = apiService.getDiscountByName(itemName);
            searchItem.enqueue(new Callback<List<GroupSellersDiscount>>() {
                @Override
                public void onResponse(Call<List<GroupSellersDiscount>> call, Response<List<GroupSellersDiscount>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<GroupSellersDiscount> items = response.body();
                        Toast.makeText(Activity_Discount_Inventory.this, "Number of orders found: " + items.size(), Toast.LENGTH_SHORT).show();
                        populateTableDefault(items,table);
                    }else{
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error body";
                            Log.e("API_ERROR", "Code: " + response.code() + ", Message: " + errorBody);
                            Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR: Code " + response.code(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<GroupSellersDiscount>> call, Throwable t) {
                    Toast.makeText(Activity_Discount_Inventory.this, "API_ERROR"+ t.toString(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        });
        FloatingActionButton back = findViewById(R.id.backFloat);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, Sign_In_Success_Group_Seller.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void populateTableDefault(List<GroupSellersDiscount> items, TableLayout table) {
        UserAPI apiService = RetrofitClient.getClient().create(UserAPI.class);
        table.removeViews(1, table.getChildCount() - 1);

        for (GroupSellersDiscount item : items) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    0, // Width: 0 means the width will be determined by the weight
                    TableRow.LayoutParams.WRAP_CONTENT, // Height: Wrap content
                    1.0f // Weight: 1 means equal distribution of space
            );

            TextView itemTextView = new TextView(this);
            SpannableString underlinedText = new SpannableString(String.valueOf(item.getProductName()));
            underlinedText.setSpan(new UnderlineSpan(), 0, underlinedText.length(), 0);
            itemTextView.setText(underlinedText);
            itemTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            itemTextView.setLayoutParams(params);
            itemTextView.setTextColor(getResources().getColor(R.color.highlight_green));
            row.addView(itemTextView);

            TextView initialTextView = new TextView(this);
            initialTextView.setText(String.valueOf(item.getOriginalPrice()));
            initialTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            initialTextView.setLayoutParams(params);
            row.addView(initialTextView);

            TextView usedTextView = new TextView(this);
            usedTextView.setText(String.valueOf(item.getDiscountPercent())); // Assuming Customer has a name field
            usedTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            usedTextView.setLayoutParams(params);
            row.addView(usedTextView);

            TextView remainingTextView = new TextView(this);// Assuming this is a JSON string
            remainingTextView.setText(String.valueOf(item.getSalePrice()));
            remainingTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            remainingTextView.setLayoutParams(params);
            row.addView(remainingTextView);

            table.addView(row);
        }
    }
}
